package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.TransactionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
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
    public Transaction makeANewTransaction(String emailFriend, String amount, String description)throws Exception{
        Transaction transaction = new Transaction();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String localDateString = dtf.format(localDate);



        BigDecimal bdAmountNotRound = new BigDecimal(amount);
        BigDecimal bdAmount= bdAmountNotRound.setScale(2, RoundingMode.HALF_UP);
       // if (bdAmount.compareTo(BigDecimal.ZERO) < 0){
        //    return transaction;
        //    }
        if (!amount.matches("^\\d+(\\.\\d+)?$")) {

            throw new IllegalArgumentException("Invalid amount: " + amount);
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
        transactionRepository.save(transaction);

        return transaction;
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
