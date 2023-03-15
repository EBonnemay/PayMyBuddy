package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppAccountService {


    private AppAccountRepository appAccountRepository;

    private UserRepository userRepository;

    private UserService userService;
    //public AppAccount getAppAccount(){
       // return appAccountRepository.
    //}
    public AppAccountService(AppAccountRepository appAccountRepository, UserRepository userRepository, UserService userService){
        this.appAccountRepository=appAccountRepository;
        this.userRepository=userRepository;
        this.userService=userService;
    }
    public Iterable<AppAccount> findAllAppAccountInTable(){
        return appAccountRepository.findAll();
    }
    public AppAccount getAppAccountById(Integer id){
        Optional<AppAccount> optAppAccount = appAccountRepository.findById(id);
        AppAccount appAccount = optAppAccount.get();
        return appAccount;
    }
   // public AppAccount addAppAccount(AppAccount appAccount){return appAccountRepository.save(appAccount);}





    public void setConnectedAccountToZero(){
        String email = userService.getCurrentUsersMailAddress();
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(0));
        appAccountRepository.save(appAccount);
    }

    public void withdrawMoneyFromConnectedAccount(String currency_field_withdraw){
        String email = userService.getCurrentUsersMailAddress();
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        BigDecimal currentBalance = appAccount.getAccountBalance();
        BigDecimal withdrawedAmount = new BigDecimal(currency_field_withdraw);
        appAccount.setAccountBalance(currentBalance.subtract(withdrawedAmount));
        appAccountRepository.save(appAccount);

    }

    public void addMoneyOnConnectedAccount(String currency_field_add){
        String email = userService.getCurrentUsersMailAddress();
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        BigDecimal currentBalance = appAccount.getAccountBalance();
        BigDecimal addedAmount = new BigDecimal(currency_field_add);
        appAccount.setAccountBalance(currentBalance.add(addedAmount));
        appAccountRepository.save(appAccount);

    }
}
