package com.example.ecommerce.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dao.UserDao;
import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        User savedUser = userDao.save(user);
        return convertToDto(savedUser);
    }
    
    @Override
    public UserDto getUserById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDto(user);
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        
        User updatedUser = userDao.save(existingUser);
        return convertToDto(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        if (!userDao.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userDao.deleteById(id);
    }
    
    @Override
    public UserDto findByEmail(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToDto(user);
    }
    
    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }
    
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }
}
