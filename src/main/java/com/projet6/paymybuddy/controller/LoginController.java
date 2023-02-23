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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    //transformer le get personal Page en post??,
    @GetMapping("/personalPage")//url sur lequel répond la méthode
    public String displayPersonalPage(Model model){
        //final UserDetails currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        //<Integer>friendsId = connectionRepository.findFriendsIdsForOneUser(1);
        //Page<User> friendsPage = friends.findAll(PageRequest.of(0,5));
        model.addAttribute("myfriends", connectionService.getFriendsUsersOfConnectedUser());
        model.addAttribute("myappaccount", userService.getAppAccountOfConnectedUser() );
        System.out.println("personal page page fulfilled, about to display");
        return "personalPage";
    }

    @GetMapping("/login")
    public String displayLoginPage(){
        System.out.println("login controller called via url will be asked to return login html page");
        return "login";
    }

    @GetMapping("/logout")
    public String displayLoginPageFromLogout(){
        return "login";
    }
    @GetMapping("/deleteAppAccount")
    public String deleteAppAccountAndRedirectPersonalPage(){
        appAccountService.deleteAppAccountOfConnectedUser();
        return "redirect:/personalPage";
    }
    @PostMapping("/addFriend")
    public String addConnectionAndRedirectPersonalPage(@RequestParam("email") String email) {
        connectionService.saveConnectionForCurrentUserWithEmailParameter(email);
        return "redirect:/personalPage";
    }
    @GetMapping("/deleteFriend")
    public String deleteConnectionAndRedirectPersonalPage(@RequestParam("email") String email){
        connectionService.deleteConnection(email);
        return "redirect:/personalPage";
    }
    /*
    @GetMapping("/newTransaction")
    /*public String newTransaction(@RequestParam("id") String id, Model model){
        //User friend = userService.getUserById(Integer.parseInt(id));

        //return "transactions";
    public String newTransaction(Model model){
        model.addAttribute("myFriends", connectionService.getFriendsUsersOfConnectedUser());

        model.addAttribute("mytransactions", transactionService.getConnectedUsersTransactions());
        return "transactions";

    }*/



    @GetMapping("/transactions")

public String displayTransactionPage( Model model) {
    //ici ajouter un paramètre booléen pour signaler si erreur +
        // number of transactions to display per page
     model.addAttribute("myTransactions", transactionService.getConnectedUsersTransactions());
     model.addAttribute("myFriends", connectionService.getFriendsUsersOfConnectedUser());
     //model.addAttribute("lastTransaction", )
    //model.addAttribute("transactionError", "Transaction amount cannot be negative.");

    return "/transactions";
}


    @GetMapping("/updateAppAccount")
    public String displayUpdateAppAccountPage(@RequestParam("id") String id, Model model) {

       Optional<AppAccount> opt  = appAccountService.getAppAccountById(Integer.parseInt(id));
       AppAccount appAccount = opt.get();
       model.addAttribute("myappaccount", appAccount);
        return "update_appaccount";
    }
    @PostMapping("/addMoneyToMyAppAccount")
    public String addMoneyOnMyAppAccountAndRedirectPersonalPage(@RequestParam("amount_added")String amount_added){
        appAccountService.addMoneyFromConnectedAccount(amount_added);
        return "redirect:/personalPage";
    }
    @PostMapping("/withdrawMoneyFromMyAppAccount")
    public String withdrawMoneyFromMyAppAccountAndRedirectPersonalPage(@RequestParam("amount_withdrawed")String amount_withdrawed){
        appAccountService.withdrawMoneyFromConnectedAccount(amount_withdrawed);
        return "redirect:/personalPage";
    }
    @PostMapping("/emptyMyAppAccount")
    public String emptyMyAppAccountAndRedirectPersonalPage(@RequestParam("id")String id){
        //cette ligne fonctionneOptional<AppAccount> opt  = appAccountService.getAppAccountById(Integer.parseInt(id));
        //cette ligne fonctionneAppAccount appAccount = opt.get();

       //cette ligne fonctionne appAccountService.setConnectedAccountToZero();

        appAccountService.setConnectedAccountToZero();

        return "redirect:/personalPage";

    }
    @PostMapping("/makeANewTransaction")

    public String makeANewTransactionAndRedirectTransactionPage(@RequestParam("email") String email, @RequestParam ("amount") Number amount, @RequestParam("description") String description, Model model){

        Transaction transaction = transactionService.makeANewTransaction(email,  amount, description);

            //return"redirect:/transactions";
       // public String displayTransactionPage( Model model) {
            //ici ajouter un paramètre booléen pour signaler si erreur +
            // number of transactions to display per page
            model.addAttribute("myTransactions", transactionService.getConnectedUsersTransactions());
            model.addAttribute("myFriends", connectionService.getFriendsUsersOfConnectedUser());
            //model.addAttribute("lastTransaction", )
            model.addAttribute("transactionError", transaction.getExceptions());
            if(transaction.getExceptions()!=null){
                for(Exception exception : transaction.getExceptions()){
                    String message = exception.getMessage();
                }
            }

            return "/transactions";

    }



    /*  @GetMapping("/transactionsWithErrors")
    public String newTransactionWithErrors(@RequestParam(defaultValue = "0") int page, Model model) {
        //ici ajouter un paramètre booléen pour signaler si erreur +
        int pageSize = 7; // number of transactions to display per page
        List<Transaction> transactions = transactionService.getConnectedUsersTransactions();

        int totalPages = (int) Math.ceil((double) transactions.size() / pageSize);
        int start = page * pageSize;
        int end = Math.min(start + pageSize, transactions.size());

        List<Transaction> pageTransactions = transactions.subList(start, end);
        model.addAttribute("transactionError", "Invalid amount, only digits and one point");


        model.addAttribute("myFriends", connectionService.getFriendsUsersOfConnectedUser());
        //model.addAttribute("transactionError", "Transaction amount cannot be negative.");

        return "/transaction";
    }*/
}

