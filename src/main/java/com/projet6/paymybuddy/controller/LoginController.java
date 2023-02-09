package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    ConnectionService connectionService;
    @Autowired
    UserService userService;

    @Autowired
    AppAccountService appAccountService;
    @GetMapping("/home")//url sur lequel répond la méthode
    public String goHome(Model model){
        //final UserDetails currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();


        model.addAttribute("myfriends", connectionService.getFriendsUsersOfConnectedUser());
        model.addAttribute("myappaccount", userService.getAppAccountOfConnectedUser() );
        System.out.println("home page fulfilled, about to display");
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        System.out.println("login controller called via url will be asked to return login html page");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "login";
    }
    @GetMapping("/deleteAppAccount")
    public String deleteAppAccount(){
        appAccountService.deleteAppAccountOfConnectedUser();
        return "redirect:/home";
    }
    @PostMapping("/addFriend")
    public String addFriend(@RequestParam("email") String email) {
        connectionService.saveConnectionForCurrentUserWithEmailParameter(email);
        return "redirect:/home";
    }
    @GetMapping("deleteFriend")
    public String deleteConnection(@RequestParam("email") String email){
        connectionService.deleteConnection(email);
        return "redirect:/home";
    }
    @GetMapping("newTransaction")
    public String newTransaction(@RequestParam("email") String email){
        return "add_transaction";


    }
    @GetMapping("updateAppAccount")
    public String updateAppAccount(@RequestParam("id") String id, Model model) {

       Optional<AppAccount> opt  = appAccountService.getAppAccountById(Integer.parseInt(id));
       AppAccount appAccount = opt.get();
       model.addAttribute("appaccount", appAccount);
        return "edit_appaccount_form";
    }





}

