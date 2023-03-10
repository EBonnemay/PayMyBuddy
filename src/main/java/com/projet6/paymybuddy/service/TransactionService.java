package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.MyException;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.TransactionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    private UserRepository userRepository;


    private UserService userService;


    private AppAccountRepository appAccountRepository;

    public TransactionService(TransactionRepository transactionRepository, UserService userService, UserRepository userRepository, AppAccountRepository appAccountRepository){
        this.transactionRepository= transactionRepository;
        this.userService=userService;
        this.userRepository = userRepository;
        this.appAccountRepository=appAccountRepository;

    }

    static final Logger logger = LogManager.getLogger();
    public Iterable<Transaction> getTransactions(){
        return transactionRepository.findAll();

    }
    public Optional<Transaction> getTransactionById(Integer id){
        return transactionRepository.findById(id);
    }
    public Transaction addTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }


    @Transactional
    public Transaction makeANewTransaction(String emailFriend, Number amount, String description){
        logger.info("system called makeANewTransaction with parameters "+ emailFriend +" "+ amount+" "+ description+ " ");
        //System.out.println("system called makeANewTransaction with parameters "+ emailFriend +" "+ amount+" "+ description+ " ");


       //instanciate object transaction with listOfTransactionExceptions
        Transaction transaction = new Transaction();
        List<MyException> listOfTransactionExceptions = new ArrayList<MyException>();//nouvel objet transaction
        transaction.setExceptions(listOfTransactionExceptions);


        try {
            if(emailFriend==null){
                //if (userRepository.findByEmail(emailFriend)==null) {
                String message = "you must enter a valid email address";
                MyException exception = new MyException(message);
                logger.debug("user input error : input cannot be void");
                throw exception;
            }
        }catch(MyException wrongEmailInput){
            listOfTransactionExceptions.add(wrongEmailInput);
            transaction.setExceptions(listOfTransactionExceptions);
            return transaction;

        }
        try{
            if(description==null){
                String message = "you must enter a description";
                MyException exception = new MyException(message);
                logger.debug("user input error : description cannot be void");
                throw exception;
            }
        }catch(MyException wrongEmailInput){
            listOfTransactionExceptions.add(wrongEmailInput);
            transaction.setExceptions(listOfTransactionExceptions);
            return transaction;

        }

        //handle no amount input
        try{
            if(amount==null) {
                String message = "invalid amount";
                MyException exception = new MyException(message);
                logger.debug("user input error : invalid amount");
                throw exception;
            }
            BigDecimal bdAmountNotRound = new BigDecimal(amount.toString());
            BigDecimal bdAmount= bdAmountNotRound.setScale(2, RoundingMode.HALF_UP);

            if(bdAmount.compareTo(BigDecimal.ZERO) < 0) {
                String message = "amount cannot be negative";
                MyException exception = new MyException(message);
                logger.debug("user input error : amount is negative");
                throw exception;
            }

            if (!amount.toString().matches("^\\d+(.\\d{1,2})?$")) {
                String message = "invalid amount";
                MyException exception = new MyException(message);
                logger.debug("user input error : invalid amount");
                throw exception;
            }
            BigDecimal costOfThisTransactionNotRound = bdAmount.multiply(BigDecimal.valueOf(0.5)).divide(BigDecimal.valueOf(100));
            BigDecimal costOfThisTransaction = costOfThisTransactionNotRound.setScale(2, RoundingMode.HALF_UP);

            String emailConnectedUser = userService.getCurrentUsersMailAddress();
            logger.info("authenticated User has email "+ emailConnectedUser);
            User connectedUser = userRepository.findByEmail(emailConnectedUser);
            AppAccount fromAppAccount = connectedUser.getAppAccount();


            if (bdAmount.compareTo(fromAppAccount.getAccountBalance())==1){
                String message = "your account is not provisioned for this operation";
                MyException exception = new MyException(message);
                logger.debug("user input error : account not provisioned for this operation");
                throw exception;
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            String localDateString = dtf.format(localDate); //créer une date "maintenant"

            User friend = userRepository.findByEmail(emailFriend);
            AppAccount toAppAccount = friend.getAppAccount();

            fromAppAccount.setAccountBalance(fromAppAccount.getAccountBalance().subtract(bdAmount.add(costOfThisTransaction)));
            toAppAccount.setAccountBalance(toAppAccount.getAccountBalance().add(bdAmount));

            appAccountRepository.save(fromAppAccount);
            appAccountRepository.save(toAppAccount);
            transaction.setAmountOfTransaction(bdAmount);
            transaction.setCreditedAccount(toAppAccount);
            transaction.setDebitedAccount(fromAppAccount);
            transaction.setDate(localDateString);
            transaction.setDescription(description);
            transaction.setCostOfTransaction(costOfThisTransaction);
            transactionRepository.save(transaction);

        }catch(MyException exception){
            transaction.getExceptions().add(exception);

        }
        return transaction;
    }
    public ArrayList<Transaction> getConnectedUsersTransactions(){
        ArrayList<Transaction> myTransactions= new ArrayList<>();
        String email = userService.getCurrentUsersMailAddress();
        System.out.println(email);
        User connectedUser = userRepository.findByEmail(email);

        AppAccount usersAccount = connectedUser.getAppAccount();
        Iterable<Integer> result = transactionRepository.getTransactionsWithMyAccountWhenDebited(usersAccount.getId());
        System.out.println(result.toString());//correct
        for (Integer i: result){
            //Transaction transaction = getTransactionById(i);
            Optional<Transaction> optT = getTransactionById(i);

            Transaction transaction = optT.get();//no value present
            myTransactions.add(transaction);
        }
        return myTransactions;
    }
}
