package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bankaccount" )
public class BankAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int bankAccountId;

        @Column(name = "number_of_account")
        private String numberOfBankAccount;

        @Column(name = "account_balance")
        private float accountBalance;

        @ManyToMany(cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
        },
                fetch = FetchType.EAGER
        )
        //
        @JoinTable(
                name = "transaction",
                joinColumns = @JoinColumn(name = "credit_account", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "debit_account", referencedColumnName = "id")
        )
        private List<BankAccount> myCreditTransactionsAccounts = new ArrayList<>();
        //pas de clé étrangère ici? la "user_id" mentionnée dans USER avec @join column + liste de bankAccounts
        @ManyToMany(cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
        },
                fetch = FetchType.EAGER
        )
        @JoinTable(
                name = "transaction",
                joinColumns = @JoinColumn(name = "debit_account", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "credit_account", referencedColumnName = "id")
        )
        private List<BankAccount> myDebitTransactionsAccounts = new ArrayList<>();
}
