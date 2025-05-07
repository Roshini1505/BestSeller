package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    UserDto findByEmail(String email);
}