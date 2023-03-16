package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "appaccount" )
public class AppAccount {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;


        @Column(name = "account_balance")
        private BigDecimal accountBalance;

       
        @OneToMany(
                fetch = FetchType.EAGER,
                mappedBy = "debitedAccount",
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        private List<Transaction> transactionsAsDebitedAccount = new ArrayList<>();


    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "creditedAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Transaction> transactionsAsCreditedAccount = new ArrayList<>();//pas de clé étrangère ici? la "user_id" mentionnée dans USER avec @join column + liste de bankAccounts

        @OneToOne(
                //tentative ?? pour créer une entité
                //cascade = CascadeType.ALL,//(ou PERSIST)
                fetch = FetchType.LAZY
        )

        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;



}
