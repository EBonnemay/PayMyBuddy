package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getTransactions(){
        return transactionRepository.findAll();

    }
    public Optional<Transaction> getTransactionById(Integer id){
        return transactionRepository.findById(id);
    }


}
