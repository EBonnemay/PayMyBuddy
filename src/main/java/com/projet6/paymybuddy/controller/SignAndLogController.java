package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.*;

import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    //public String registerUser(@RequestParam("first_name") String firstName, @RequestParam("last_name")String lastName, @RequestParam("email") String email, @RequestParam("password") String password, Model model){
    public String registerUser(@RequestParam(name="first_name", required=false) String firstName, @RequestParam (name="last_name", required=false) String lastName, @RequestParam(name="email", required=false)String email, @RequestParam(name="password", required=false) String password, Model model){
        User newUser = userService.registerNewUserAccount(firstName, lastName, email, password);

        if(newUser.getExceptions()!=null){
            model.addAttribute("error", "Incorrect input");
            logger.error("failed registering");
            return("registerPage");

        }
        logger.info("successful registering");
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