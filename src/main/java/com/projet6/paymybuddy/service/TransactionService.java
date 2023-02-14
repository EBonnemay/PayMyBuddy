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
    public Transaction makeANewTransaction(String emailFriend, String amount){
        Transaction transaction = new Transaction();

        BigDecimal bdAmount = new BigDecimal(amount);
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

        fromAppAccount.setAccountBalance(fromAppAccount.getAccountBalance().subtract(bdAmount));
        toAppAccount.setAccountBalance(toAppAccount.getAccountBalance().add(bdAmount));

        appAccountRepository.save(fromAppAccount);
        appAccountRepository.save(toAppAccount);

        transaction.setCreditedAccount(toAppAccount);
        transaction.setDebitedAccount(fromAppAccount);
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
