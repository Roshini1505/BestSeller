package com.example.ecommerce.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserId(Long userId);
}