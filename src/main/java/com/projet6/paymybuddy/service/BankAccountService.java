package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.BankAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.BankAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private UserRepository userRepository;

    public Iterable<BankAccount> getBankAccounts(){
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getBankAccountById(Integer id){
        return bankAccountRepository.findById(id);
    }
    /*public Optional<BankAccount> getPrincipalBankAccountByEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        int id = user.getId();
        List<BankAccount> listOfBankAccounts = new ArrayList<>();
        Optional<BankAccount> optList = getBankAccountById(id);
        listOfBankAccounts = optList.get();
    }*/

    public BankAccount addBankAccount(BankAccount bankAccount){return bankAccountRepository.save(bankAccount);}
    public void deleteBankAccount(BankAccount bankaccount){bankAccountRepository.delete(bankaccount);}
    public void deleteBankAccountById(int Id){bankAccountRepository.deleteById(Id);}
}
