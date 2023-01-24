package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bank_account" )
public class BankAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int bankAccountId;

        @Column(name = "number_of_account")
        private String numberOfBankAccount;

        @Column(name = "account_balance")
        private float accountBalance;


//pas de clé étrangère ici? la "user_id" mentionnée dans USER avec @join column + liste de bankAccounts
    }
