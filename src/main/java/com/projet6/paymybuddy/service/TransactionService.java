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
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppAccountRepository appAccountRepository;

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
        Transaction transaction = new Transaction();
        List<MyException> listOfExceptions = new ArrayList<MyException>();//nouvel objet transaction
        transaction.setExceptions(listOfExceptions);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String localDateString = dtf.format(localDate); //créer une date "maintenant"



        BigDecimal bdAmountNotRound = new BigDecimal(amount.toString());
        BigDecimal bdAmount= bdAmountNotRound.setScale(2, RoundingMode.HALF_UP);


       // try {bdAmount.compareTo(BigDecimal.ZERO) > 0;
        try {
            if (bdAmount.compareTo(BigDecimal.ZERO) < 0) {
                String message = "amount cannot be negative";
                MyException exception = new MyException(message);
                logger.info("user input error : amount is negative");
                throw exception;
            }
            // do something if bdAmount is positive or zero
        } catch (MyException e) {
            transaction.getExceptions().add(e);
            return transaction;
        }
        try{
            if (!amount.toString().matches("^\\d+(,\\d{1,2})?$")){
                String message = "invalid amount";
                MyException exception = new MyException(message);
                logger.info("user input error : invalid amount");
                throw exception;
            }
        }catch(MyException e){
            transaction.getExceptions().add(e);
            return transaction;
        }
        BigDecimal costOfThisTransactionNotRound = bdAmount.multiply(BigDecimal.valueOf(0.5)).divide(BigDecimal.valueOf(100));
        BigDecimal costOfThisTransaction = costOfThisTransactionNotRound.setScale(2, RoundingMode.HALF_UP);

        System.out.println(costOfThisTransaction);
        transaction.setAmountOfTransaction(bdAmount);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailConnectedUser = auth.getName();
        System.out.println(emailConnectedUser);
        User connectedUser = userRepository.findByEmail(emailConnectedUser);
        AppAccount fromAppAccount = connectedUser.getAppAccount();
        try{
            if (bdAmount.compareTo(fromAppAccount.getAccountBalance())==1){
                String message = "your account is not provisioned for this operation";
                MyException exception = new MyException(message);
                logger.info("user input error : account not provisioned for this operation");
                throw exception;
            }
        }catch(MyException e){
            transaction.getExceptions().add(e);
            return transaction;
        }

        User friend = userRepository.findByEmail(emailFriend);
        //Optional<User> opt = Optional.ofNullable(userRepository.findByEmail(emailFriend));
        //User friend = opt.get();
        AppAccount toAppAccount = friend.getAppAccount();

        fromAppAccount.setAccountBalance(fromAppAccount.getAccountBalance().subtract(bdAmount.add(costOfThisTransaction)));
        toAppAccount.setAccountBalance(toAppAccount.getAccountBalance().add(bdAmount));

        appAccountRepository.save(fromAppAccount);
        appAccountRepository.save(toAppAccount);

        transaction.setCreditedAccount(toAppAccount);
        transaction.setDebitedAccount(fromAppAccount);
        transaction.setDate(localDateString);
        transaction.setDescription(description);
        transaction.setCostOfTransaction(costOfThisTransaction);
        transaction.setExceptions(transaction.getExceptions());
        transactionRepository.save(transaction);

        return transaction;//rajouter les erreurs. rajouter un booleen Transient dans le modele, true s'il y a des erreurs. dans la vue regarder si true et afficher les erreurs
    };
    public ArrayList<Transaction> getConnectedUsersTransactions(){
        ArrayList<Transaction> myTransactions= new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println(email);
        User connectedUser = userRepository.findByEmail(email);

        AppAccount usersAccount = connectedUser.getAppAccount();
        Iterable<Integer> result = transactionRepository.getTransactionsWithMyAccountWhenDebited(usersAccount.getId());
        for (Integer i: result){
            Optional<Transaction> optT = getTransactionById(i);
            Transaction transaction = optT.get();
            myTransactions.add(transaction);
        }
        return myTransactions;
    }

}
