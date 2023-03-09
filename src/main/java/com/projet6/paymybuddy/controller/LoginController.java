package com.projet6.paymybuddy.controller;

import com.projet6.paymybuddy.model.*;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    static final Logger logger = LogManager.getLogger();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppAccountRepository appAccountRepository;

    @GetMapping("/personalPage")//url sur lequel répond la méthode
    public String displayPersonalPage(Model model){
        //final UserDetails currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        //<Integer>friendsId = connectionRepository.findFriendsIdsForOneUser(1);
        //Page<User> friendsPage = friends.findAll(PageRequest.of(0,5));
        //model.addAttribute("myFriends", connectionService.getActualOrFormerFriendsUsersOfConnectedUser());
        model.addAttribute("myNonDeletedFriends", connectionService.getNonDeletedFriendsUsersOfConnectedUser());
        model.addAttribute("myappaccount", userService.getAppAccountOfConnectedUser() );

        System.out.println("personal page page fulfilled, about to display");
        return "personalPage";
    }

    @GetMapping("/login")
    public String displayLoginPage(){
        //System.out.println("login controller called via url will be asked to return login html page");
        return "login";
    }
    @GetMapping("/homePage")
    public String displayhomePage(){
        //System.out.println("login controller called via url will be asked to return login html page");
        return "homePage";
    }

    @GetMapping("/registrationPage")
    public String displayRegisterPage() {
        return "registerPage";
    }
    @PostMapping("/registerNewUser")
    public String registerUser(@RequestParam("first_name") String firstName, @RequestParam("last_name")String lastName, @RequestParam("email") String email, @RequestParam("password") String password){
        System.out.println("in controller to register user");
        User newUser = userService.registerNewUserAccount(firstName, lastName, email, password);
        System.out.println("user's first name is "+ newUser.getFirstName());
        return "login";
    }


    @GetMapping("/logout")
    public String displayLoginPageFromLogout(){
        return "login";
    }

    @PostMapping("/addFriend")
    public String addConnectionAndRedirectPersonalPage(@RequestParam(name="email", required=false) String email, Model model) {
        Connection connection = connectionService.saveNewConnectionForCurrentUserWithEmailParameter(email);
        //List<MyException> list = new ArrayList<>();
        //connection.setExceptions(list);
        //System.out.println(connection.getExceptions().size());
        model.addAttribute("myNonDeletedFriends", connectionService.getNonDeletedFriendsUsersOfConnectedUser());
        model.addAttribute("myappaccount", userService.getAppAccountOfConnectedUser() );
        model.addAttribute("connectionError", connection.getExceptions());
        if(connection.getExceptions()!=null){
            for(Exception exception : connection.getExceptions()){
                String message = exception.getMessage();
            }
        }
        return "/personalPage";
//?? ou redirect???
        //si connectionService.saveCOnnection a échoué, renvoyer
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

public String displayTransactionPage(Model model) {
    //ici ajouter un paramètre booléen pour signaler si erreur +
        // number of transactions to display per page
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

        //cet appel n'affiche pas les amis en première intention//
    /* model.addAttribute("myFriends ", connectionService.getNonDeletedFriendsUsersOfConnectedUser());
        for (User user : connectionService.getNonDeletedFriendsUsersOfConnectedUser()){
            System.out.println(user.getFirstName());
        }*/
     //model.addAttribute("lastTransaction", )
        //List <MyException> list = new ArrayList<>();
   // model.addAttribute("transactionError", list);

    return "transactions";
}

    @GetMapping("/profile")
    public String displayProfilePage(Model model) {

        String email = userService.getCurrentUsersMailAddress();
        User currentUser = userRepository.findByEmail(email);
        model.addAttribute( model.addAttribute("name", currentUser.getFirstName()+" "+currentUser.getLastName()));;
        if(!currentUser.isDeleted()){
            model.addAttribute("status", "utilisateur inscrit");
        }
        else {
            model.addAttribute("status", "non inscrit");
        }
        model.addAttribute("email", email);
        model.addAttribute("balance", currentUser.getAppAccount().getAccountBalance());
        return "profile";
    }
    @PostMapping("/unsubscribe")
    public String unsubscribe(){
        String email = userService.getCurrentUsersMailAddress();
        User currentUser = userRepository.findByEmail(email);
        currentUser = userService.markUserAsDeleted(currentUser);
        return("redirect:/login");

    }
    @GetMapping("/updateAppAccount")
    public String displayUpdateAppAccountPage(@RequestParam("id") String id, Model model) {

        AppAccount appAccount  = appAccountService.getAppAccountById(Integer.parseInt(id));

       User user = appAccount.getUser();
       model.addAttribute("name", user.getFirstName()+" "+user.getLastName());
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

    public String makeANewTransactionAndRedirectTransactionPage(@RequestParam(name="email", required=false) String email, @RequestParam (name="amount", required=false) Number amount, @RequestParam(name="description", required=false) String description, Model model){
        //calling service
        Transaction transaction = transactionService.makeANewTransaction(email,  amount, description);

            model.addAttribute("myTransactions", transactionService.getConnectedUsersTransactions());
            model.addAttribute("myFriends", connectionService.getNonDeletedFriendsUsersOfConnectedUser());


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

