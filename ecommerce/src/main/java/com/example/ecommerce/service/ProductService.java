package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.dto.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    List<ProductDto> searchProductsByName(String name);
    List<ProductDto> getAvailableProducts();
}