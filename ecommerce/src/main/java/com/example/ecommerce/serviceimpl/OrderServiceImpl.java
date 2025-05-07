package com.example.ecommerce.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dao.OrderDao;
import com.example.ecommerce.dao.ProductDao;
import com.example.ecommerce.dao.UserDao;
import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private ProductDao productDao;

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {
        User user = userDao.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderRequest.getUserId()));
        
        Product product = productDao.findById(orderRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderRequest.getProductId()));
        
        if (product.getStockQuantity() < orderRequest.getQuantity()) {
            throw new RuntimeException("Not enough stock available for product: " + product.getName());
        }

        Order order = new Order(user, product, orderRequest.getQuantity());
        product.setStockQuantity(product.getStockQuantity() - orderRequest.getQuantity());
        productDao.save(product);
        
        Order savedOrder = orderDao.save(order);
        return convertToDto(savedOrder);
    }
    
    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToDto(order);
    }
    
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderDao.findAll();
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderDao.findByUserId(userId);
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public void deleteOrder(Long id) {
        if (!orderDao.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderDao.deleteById(id);
    }
    
    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setUserName(order.getUser().getName());
        orderDto.setProductId(order.getProduct().getId());
        orderDto.setProductName(order.getProduct().getName());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderDate(order.getOrderDate());
        return orderDto;
    }
}
