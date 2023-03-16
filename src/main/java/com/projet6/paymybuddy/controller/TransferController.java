package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;

import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TransferController {


    static final Logger logger = LogManager.getLogger();
    //@Autowired
    private AppAccountService appAccountService;
    private ConnectionService connectionService;
    private UserService  userService;
private TransactionService transactionService;

    public TransferController(UserService userService, TransactionService transactionService, ConnectionService connectionService, AppAccountService appAccountService){
        this.userService = userService;
        this.transactionService = transactionService;
        this.connectionService = connectionService;
        this.appAccountService = appAccountService;
    }
    @GetMapping("/transactions")

public String displayTransactionPage(Model model) {

     model.addAttribute("myTransactions", transactionService.getConnectedUsersTransactions());
     //le code ci dessous affiche les amis en première intention
     List<User> listOfFriends = connectionService.getActualOrFormerFriendsUsersOfConnectedUser();
     List<User>listOfNonDeletedFriends = new ArrayList<>();
     for (User user : listOfFriends){
         if(!user.isDeleted()){
             listOfNonDeletedFriends.add(user);
         }
     }
    model.addAttribute("myFriends", listOfNonDeletedFriends);
    return "transactions";
}


    @PostMapping("/makeANewTransaction")

    public String makeANewTransactionAndRedirectTransactionPage(@RequestParam(name="email", required=false) String email, @RequestParam (name="amount", required=false) Number amount, @RequestParam(name="description", required=false) String description, Model model){
        
        Transaction transaction = transactionService.makeANewTransaction(email,  amount, description);

            model.addAttribute("myTransactions", transactionService.getConnectedUsersTransactions());
            model.addAttribute("myFriends", connectionService.getNonDeletedFriendsUsersOfConnectedUser());


            model.addAttribute("transactionError", transaction.getExceptions());
            if(transaction.getExceptions()!=null){
                logger.error("errors in transaction MyExceptions list");
                for(Exception exception : transaction.getExceptions()){
                    String message = exception.getMessage();
                }
            }
            return "/transactions";
    }


}

