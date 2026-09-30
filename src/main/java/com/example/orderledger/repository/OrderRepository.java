package com.example.orderledger.repository;

import com.example.orderledger.entity.Order;
import com.example.orderledger.entity.OrderItem;
import com.example.orderledger.entity.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
}
