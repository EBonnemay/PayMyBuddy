package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

    @Autowired
    ConnectionService connectionService;

    @GetMapping("/personalPage")//url sur lequel répond la méthode
    public String goHome(Model model){
        //final UserDetails currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();


        model.addAttribute("friends", connectionService.getNamesOfFriends());
        return "personalPage";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "login";
    }
}
