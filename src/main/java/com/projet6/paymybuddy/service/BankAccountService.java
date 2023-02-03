package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.BankAccount;
import com.projet6.paymybuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public Iterable<BankAccount> getBankAccounts(){
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getBankAccountById(Integer id){
        return bankAccountRepository.findById(id);
    }
    //public Optional<BankAccount> getPrincipalBankAccountByEmail()

    public BankAccount addBankAccount(BankAccount bankAccount){return bankAccountRepository.save(bankAccount);}
    public void deleteBankAccount(BankAccount bankaccount){bankAccountRepository.delete(bankaccount);}
    public void deleteBankAccountById(int Id){bankAccountRepository.deleteById(Id);}
}
