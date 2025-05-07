package com.example.ecommerce.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dao.ProductDao;
import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product savedProduct = productDao.save(product);
        return convertToDto(savedProduct);
    }
    
    @Override
    public ProductDto getProductById(Long id) {
        Product product = productDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDto(product);
    }
    
    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productDao.findAll();
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setTax(productDto.getTax());
        existingProduct.setStockQuantity(productDto.getStockQuantity());
        
        Product updatedProduct = productDao.save(existingProduct);
        return convertToDto(updatedProduct);
    }
    
    @Override
    public void deleteProduct(Long id) {
        if (!productDao.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productDao.deleteById(id);
    }
    
    @Override
    public List<ProductDto> searchProductsByName(String name) {
        List<Product> products = productDao.findByNameContainingIgnoreCase(name);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<ProductDto> getAvailableProducts() {
        List<Product> products = productDao.findAvailableProducts();
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setTax(productDto.getTax());
        product.setStockQuantity(productDto.getStockQuantity());
        return product;
    }
    
    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setTax(product.getTax());
        productDto.setStockQuantity(product.getStockQuantity());
        return productDto;
    }
}
