package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> optUser = userRepository.findById(id);
        User user = optUser.get();
        return user;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUserById(int Id) {
        userRepository.deleteById(Id);

    }

    public AppAccount getAppAccountOfConnectedUser() {

System.out.println("in GetbankAccountsForUser, auth about to be called");
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    User user = userRepository.findByEmail(email);
   AppAccount appAccount = user.getAppAccount();
    System.out.println("appAccount of connected User retrieved");
    System.out.println("iban = "+ appAccount.getIban());
    return appAccount;
}
public User getUserFromEmail(String email){
        return userRepository.findByEmail(email);
}
}
