package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
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
    @Column(name = "credit_account")
    private int creditAccount;

    @Column(name = "debit_account")
    private int debitAccount;

    @Column(name = "amount_of_transaction")
    private float amountOfTransaction;

    @Column(name = "date_time")
    private Date dateTime;

    @Column(name = "transaction_completed")
    private Boolean statusOfTransaction;






}