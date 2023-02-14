package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    ConnectionService connectionService;
    @Autowired
    UserService userService;

    @Autowired
    AppAccountService appAccountService;
    @Autowired
    TransactionService transactionService;
    //transformer le get home en post??,
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
    @GetMapping("/deleteFriend")
    public String deleteConnection(@RequestParam("email") String email){
        connectionService.deleteConnection(email);
        return "redirect:/home";
    }
    @GetMapping("/newTransaction")
    /*public String newTransaction(@RequestParam("id") String id, Model model){
        User friend = userService.getUserById(Integer.parseInt(id));

        return "add_transaction";*/
    public String newTransaction(Model model){
        model.addAttribute("myFriends", connectionService.getFriendsUsersOfConnectedUser());

        model.addAttribute("mytransactions", transactionService.getConnectedUsersTransactions());
        return "add_transaction";

    }
    @GetMapping("/updateAppAccount")
    public String updateAppAccount(@RequestParam("id") String id, Model model) {

       Optional<AppAccount> opt  = appAccountService.getAppAccountById(Integer.parseInt(id));
       AppAccount appAccount = opt.get();
       model.addAttribute("myappaccount", appAccount);
        return "update_appaccount";
    }
    @PostMapping("/addMoneyToMyAppAccount")
    public String addMoneyOnMyAppAccount(@RequestParam("amount_added")String amount_added){
        appAccountService.addMoneyFromConnectedAccount(amount_added);
        return "redirect:/home";
    }
    @PostMapping("/withdrawMoneyFromMyAppAccount")
    public String withdrawMoneyFromMyAppAccount(@RequestParam("amount_withdrawed")String amount_withdrawed){
        appAccountService.withdrawMoneyFromConnectedAccount(amount_withdrawed);
        return "redirect:/home";
    }
    @PostMapping("/emptyMyAppAccount")
    public String emptyMyAppAccount(@RequestParam("id")String id){
        //cette ligne fonctionneOptional<AppAccount> opt  = appAccountService.getAppAccountById(Integer.parseInt(id));
        //cette ligne fonctionneAppAccount appAccount = opt.get();

       //cette ligne fonctionne appAccountService.setConnectedAccountToZero();

        appAccountService.setConnectedAccountToZero();

        return "redirect:/home";

    }
    @PostMapping("/makeANewTransaction")

    public String makeANewTransaction(@RequestParam("email") String email, @RequestParam ("amount") String amount){
        transactionService.makeANewTransaction(email, amount);
        return"redirect:/newTransaction";
    }


}

/*public String makeANewTransaction(@RequestParam("friendId")String friendId, @RequestParam ("amount") String amount) {
        transactionService.makeANewTransaction(friendId, amount);
        return "redirect:/add_transaction";
    }*/