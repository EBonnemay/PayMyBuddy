package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Integer id){
        return userRepository.findById(id);
    }
}
