package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.model.UserPrincipal;
import com.projet6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        //UserDetails userD = new UserPrincipal(user);
        //return userD;
        return new UserPrincipal(user);
    }
}
