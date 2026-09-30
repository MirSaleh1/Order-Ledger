package com.example.orderledger.service;

import com.example.orderledger.entity.*;
import com.example.orderledger.exception.BusinessException;
import com.example.orderledger.mapper.OrderMapper;
import com.example.orderledger.model.*;
import com.example.orderledger.model.request.CreateOrderRequest;
import com.example.orderledger.model.request.OrderItemRequest;
import com.example.orderledger.model.response.OrderResponse;
import com.example.orderledger.model.response.OrderStatusHistoryResponse;
import com.example.orderledger.repository.OrderRepository;
import com.example.orderledger.repository.OrderStatusHistoryRepository;
import com.example.orderledger.repository.ProductRepository;
import com.example.orderledger.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import static com.example.orderledger.exception.ExceptionConstants.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final CouponService couponService;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {

        User user= userRepository.findById(createOrderRequest.userId()).
                orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest orderItemRequest: createOrderRequest.orderItemRequests()){

            Product product=productRepository.findById(orderItemRequest.productId()).
                    orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND));

            if(product.getStockQuantity()<orderItemRequest.quantity()){
                throw new BusinessException(INSUFFICIENT_STOCK);
            }
            product.setStockQuantity(product.getStockQuantity() - orderItemRequest.quantity());
            productRepository.save(product);

          OrderItem orderItem = OrderItem.builder().order(order).product(product).
                  quantity(orderItemRequest.quantity()).unitPrice(product.getPrice()).build();
            order.getItems().add(orderItem);
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.quantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        if (createOrderRequest.couponCode() != null && !createOrderRequest.couponCode().isBlank()) {
            totalAmount = couponService.applyCoupon(createOrderRequest.couponCode(), totalAmount);
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        createOrderStatusHistory(order);
        return OrderMapper.toOrderResponse(order);
    }
    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {

        Order order= orderRepository.findById(orderId).
                orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
        validateStatusTransition(order.getOrderStatus(),orderStatus);
        order.setOrderStatus(orderStatus);
        createOrderStatusHistory(order);
        return  OrderMapper.toOrderResponse(orderRepository.save(order));
    }
    @Transactional
    public OrderResponse cancelOrder(Long orderId){

        Order order= orderRepository.findById(orderId).
                orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
        if(order.getOrderStatus() == OrderStatus.SHIPPED ||
                order.getOrderStatus() == OrderStatus.COMPLETED ||
                order.getOrderStatus() == OrderStatus.CANCELLED){
            throw new BusinessException(FINISHED_ORDER_CANNOT_BE_CANCELLED);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        for (OrderItem oi: order.getItems()){
            Product product=oi.getProduct();
            product.setStockQuantity(product.getStockQuantity() + oi.getQuantity());
            productRepository.save(product);
        }
       createOrderStatusHistory(order);
        return  OrderMapper.toOrderResponse(orderRepository.save(order));
    }

    public  OrderResponse getOrderById(Long orderId){
        Order order= orderRepository.findById(orderId).
                orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
        return OrderMapper.toOrderResponse(order);
    }
    @Transactional
    public List<OrderResponse> getOrdersByUserId(Long userId){
        userRepository.findById(userId).
                orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
      List<Order>orders= orderRepository.findAllByUserId(userId);
        return orders.stream().map(OrderMapper::toOrderResponse).toList();
    }
    public List<OrderStatusHistoryResponse> getOrderStatusHistory(Long orderId){
       List<OrderStatusHistory>orderStatusHistories= orderStatusHistoryRepository.findAllByOrderId(orderId);
       return orderStatusHistories.stream().map(OrderMapper::toOrderStatusHistoryResponse).toList();
    }
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {

        if (currentStatus == newStatus) {
            throw new BusinessException(ORDER_ALREADY_IN_STATUS);
        }
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.COMPLETED) {
            throw new BusinessException(FINISHED_ORDER_CANNOT_BE_CHANGED);
        }
        if (newStatus == OrderStatus.PAID && currentStatus != OrderStatus.CREATED) {
            throw new BusinessException(ONLY_NEW_ORDER_CAN_BE_PAID);
        }
        if (newStatus == OrderStatus.SHIPPED && currentStatus != OrderStatus.PAID) {
            throw new BusinessException(ONLY_PAID_ORDER_CAN_BE_SHIPPED);
        }
        if (newStatus == OrderStatus.COMPLETED && currentStatus != OrderStatus.SHIPPED) {
            throw new BusinessException(ONLY_SHIPPED_ORDER_CAN_BE_COMPLETED);
        }
    }
    private void createOrderStatusHistory(Order order){
        OrderStatusHistory orderStatusHistory=OrderStatusHistory.builder().order(order).
                status(order.getOrderStatus()).build();
        orderStatusHistoryRepository.save(orderStatusHistory);
    }
}
