package com.example.service.userService;

import com.example.entity.User;

import java.util.Optional;

public interface UserService {
    public User saveUser(User user);
    public Optional<User> getUserById(int id);
}
