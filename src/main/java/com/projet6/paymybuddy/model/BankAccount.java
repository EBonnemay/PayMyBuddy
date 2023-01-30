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
        private int id;

        @Column(name = "iban")
        private String iban;

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
                joinColumns = @JoinColumn(name = "credited_account", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "debited_account", referencedColumnName = "id")
        )
        private List<BankAccount> bankAccountsOfFriendsGivingMoneyToMe = new ArrayList<>();
        //pas de clé étrangère ici? la "user_id" mentionnée dans USER avec @join column + liste de bankAccounts
        @ManyToMany(cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
        },
                fetch = FetchType.EAGER
        )
        @JoinTable(
                name = "transaction",
                joinColumns = @JoinColumn(name = "debited_account", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "credited_account", referencedColumnName = "id")
        )
        private List<BankAccount> bankAccountsOfFriendsIGaveMoneyTo = new ArrayList<>();

        @ManyToOne(
                cascade = CascadeType.ALL
        )

        @JoinColumn(name = "user_id")
        private User owner;
}
