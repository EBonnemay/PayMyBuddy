package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.Connection;

import com.projet6.paymybuddy.model.User;

import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;

import com.projet6.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {


    private ConnectionService connectionService;

   private  UserService userService;


    private AppAccountService appAccountService;


    @Autowired
    private UserRepository userRepository;

    static final Logger logger = LogManager.getLogger();

    public UserController(UserService userService, AppAccountService appAccountService, ConnectionService connectionService, UserRepository userRepository ){
        this.userService=userService;
        this.appAccountService=appAccountService;
        this.connectionService=connectionService;
        this.userRepository = userRepository;
    }
    @GetMapping("/personalPage")
    public String displayPersonalPage(Model model) {

        model.addAttribute("myNonDeletedFriends", connectionService.getNonDeletedFriendsUsersOfConnectedUser());
        model.addAttribute("myappaccount", userService.getAppAccountOfConnectedUser());
        return "personalPage";
    }


    @PostMapping("/addFriend")
    public String addConnectionAndRedirectPersonalPage(@RequestParam(name = "email", required = false) String email, Model model) {
        Connection connection = connectionService.saveNewConnectionForCurrentUserWithEmailParameter(email);
        model.addAttribute("myNonDeletedFriends", connectionService.getNonDeletedFriendsUsersOfConnectedUser());
        model.addAttribute("myappaccount", userService.getAppAccountOfConnectedUser());
        model.addAttribute("connectionError", connection.getExceptions());
        if (connection.getExceptions() != null) {
            logger.error("exceptions in connection MyExceptions list");
            for (Exception exception : connection.getExceptions()) {
                String message = exception.getMessage();
            }
        }
        return "/personalPage";
    }

    @GetMapping("/deleteFriend")
    public String deleteConnectionAndRedirectPersonalPage(@RequestParam("email") String email) {
        connectionService.deleteConnection(email);
        return "redirect:/personalPage";
    }


    @GetMapping("/profile")
    public String displayProfilePage(Model model) {

        String email = userService.getCurrentUsersMailAddress();
        User currentUser = userRepository.findByEmail(email);
        model.addAttribute(model.addAttribute("name", currentUser.getFirstName() + " " + currentUser.getLastName()));

        if (!currentUser.isDeleted()) {
            model.addAttribute("status", "registered user");
        } else {
            model.addAttribute("status", "non registered user");
        }
        model.addAttribute("email", email);
        model.addAttribute("balance", currentUser.getAppAccount().getAccountBalance());
        return "profile";
    }

    @GetMapping("/updateAppAccount")
    public String displayUpdateAppAccountPage(@RequestParam("id") String id, Model model) {

        AppAccount appAccount = appAccountService.getAppAccountById(Integer.parseInt(id));

        User user = appAccount.getUser();
        model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
        model.addAttribute("myappaccount", appAccount);
        return "update_appaccount";
    }

    @PostMapping("/addMoneyToMyAppAccount")
    public String addMoneyOnMyAppAccountAndRedirectPersonalPage(@RequestParam("amount_added") String amount_added) {
        appAccountService.addMoneyOnConnectedAccount(amount_added);
        return "redirect:/personalPage";
    }

    @PostMapping("/withdrawMoneyFromMyAppAccount")
    public String withdrawMoneyFromMyAppAccountAndRedirectPersonalPage(@RequestParam("amount_withdrawed") String amount_withdrawed) {
        appAccountService.withdrawMoneyFromConnectedAccount(amount_withdrawed);
        return "redirect:/personalPage";
    }

    @PostMapping("/emptyMyAppAccount")
    public String emptyMyAppAccountAndRedirectPersonalPage() {

        appAccountService.setConnectedAccountToZero();

        return "redirect:/personalPage";

    }
}
