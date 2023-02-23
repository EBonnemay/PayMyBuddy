package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "transaction" )
public class Transaction {
    //revoir primary key d'une table de liaison dans sprintBoot JPA
    @Id
    @Column(name = "id")
    private int transactionId;
//aurais du mettre credit_account_id
    //credit_account et debit_account sont des clés étrangères
    //@Column(name = "credited_account")
    //private int creditedAccount_Id;

    // @Column(name = "debited_account")
    // private int debitedAccount_Id;
    @Column(name = "description")
    private String description;

    @Column(name = "amount_of_transaction")

   // @Positive(message = "amount must be positive")
    private BigDecimal amountOfTransaction;

    @Column(name = "cost_of_transaction")
    private BigDecimal costOfTransaction;

    @Column(name = "date_time")
    private String date;


    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "debited_account")
    private AppAccount debitedAccount;
    //means that my transaction entity has a foreign key column named debited_account
    //referring to the primary k
    @ManyToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "credited_account")
    private AppAccount creditedAccount;


//créer un objet
@Transient
    List<MyException> exceptions;

}