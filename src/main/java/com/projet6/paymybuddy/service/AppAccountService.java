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

    @Autowired
    private AppAccountRepository appAccountRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired UserService userService;
    //public AppAccount getAppAccount(){
       // return appAccountRepository.
    //}
    public Iterable<AppAccount> findAllAppAccountInTable(){
        return appAccountRepository.findAll();
    }
    public Optional<AppAccount> getAppAccountById(Integer id){
        return appAccountRepository.findById(id);
    }
    /*public Optional<BankAccount> getPrincipalBankAccountByEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        int id = user.getId();
        List<BankAccount> listOfBankAccounts = new ArrayList<>();
        Optional<BankAccount> optList = getBankAccountById(id);
        listOfBankAccounts = optList.get();

        //JE CONFONDS ID USER ET ID BANKACCOUNTS
    }*/
    public AppAccount getAppAccountOfConnectedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication ok, called auth - here auth is email");
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        return user.getAppAccount();
    }

    public AppAccount addAppAccount(AppAccount appAccount){return appAccountRepository.save(appAccount);}
    public void deleteAppAccountOfConnectedUser() {

        //récupérer le mail authentifiant de la session courante, puis l'utilisateur associé, puis son compte
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println(email);
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        appAccountRepository.delete(appAccount);

        System.out.println("authentication ok, called auth - here auth is email");


    }
    public void setConnectedAccountToZero(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println(email);
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(0));
        appAccountRepository.save(appAccount);
    }
    /*public void setAccountToZero(int idOfCount){

        Optional<AppAccount> opt = appAccountRepository.findById(idOfCount);
        AppAccount appAccount = opt.get();
        appAccount.setAccountBalance(BigDecimal.valueOf(0));
        appAccountRepository.save(appAccount);
    }*/
    public void withdrawMoneyFromConnectedAccount(String currency_field_withdraw){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println(email);
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        BigDecimal currentBalance = appAccount.getAccountBalance();
        BigDecimal withdrawedAmount = new BigDecimal(currency_field_withdraw);
        appAccount.setAccountBalance(currentBalance.subtract(withdrawedAmount));
        appAccountRepository.save(appAccount);

    }

    public void addMoneyFromConnectedAccount(String currency_field_add){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println(email);
        User connectedUser = userRepository.findByEmail(email);
        AppAccount appAccount = connectedUser.getAppAccount();
        BigDecimal currentBalance = appAccount.getAccountBalance();
        BigDecimal addedAmount = new BigDecimal(currency_field_add);
        appAccount.setAccountBalance(currentBalance.add(addedAmount));
        appAccountRepository.save(appAccount);

    }
}
