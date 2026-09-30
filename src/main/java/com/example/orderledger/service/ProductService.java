package com.example.orderledger.service;

import com.example.orderledger.entity.Product;
import com.example.orderledger.exception.BusinessException;
import com.example.orderledger.model.request.ProductRequest;
import com.example.orderledger.model.response.ProductResponse;
import com.example.orderledger.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.orderledger.exception.ExceptionConstants.PRODUCT_ALREADY_EXISTS;

import  static com.example.orderledger.exception.ExceptionConstants.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request){
        if(productRepository.existsByName(request.name()))
            throw new BusinessException(PRODUCT_ALREADY_EXISTS);
       Product product= productRepository.save(Product.builder().name(request.name()).price(request.price()).
                stockQuantity(request.stockQuantity()).build());
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());
    }
    @Transactional
    public ProductResponse updateProduct(Long productId,ProductRequest request){
        Product product=findProductById(productId);
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        return new ProductResponse(productId, product.getName(), product.getPrice(), product.getStockQuantity());
    }
    @Transactional
    public void deleteProduct(Long productId){
        Product product=findProductById(productId);
        productRepository.delete(product);
      }
    public ProductResponse getProduct(Long productId){
        Product product=findProductById(productId);
        return new ProductResponse(productId, product.getName(), product.getPrice(), product.getStockQuantity());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products=productRepository.findAll();
        return products.stream().map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                product.getStockQuantity())).toList();
    }
    private Product findProductById(Long productId){
      return productRepository.findById(productId).
                orElseThrow(() -> new BusinessException(PRODUCT_NOT_FOUND));
    }
}
