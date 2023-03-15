package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SignAndLogController {



    UserService userService;


    private UserRepository userRepository;
    static final Logger logger = LogManager.getLogger();



    public SignAndLogController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository=userRepository;

    }

    @GetMapping("/login")
    public String displayLoginPage(){
        return "login";
    }

    @GetMapping("/registrationPage")
    public String displayRegisterPage() {
        return "registerPage";
    }
    @PostMapping("/registerNewUser")
    public String registerUser(@RequestParam("first_name") String firstName, @RequestParam("last_name")String lastName, @RequestParam("email") String email, @RequestParam("password") String password){
        User newUser = userService.registerNewUserAccount(firstName, lastName, email, password);
        return "login";
    }


    @GetMapping("/logout")
    public String displayLoginPageFromLogout(){
        return "login";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(){
        String email = userService.getCurrentUsersMailAddress();
        User currentUser = userRepository.findByEmail(email);
        currentUser = userService.markUserAsDeleted(currentUser);
        return("redirect:/login");

    }


}