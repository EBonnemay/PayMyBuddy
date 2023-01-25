package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user" )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "adress")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "email")
    private String email;

    @Column(name = "email_password")
    private String emailPassord;

    @Column(name = "social_network_login")
    private String socialNetworkLogin;

    @Column(name = "social_network_password")
    private String socialNetworkPassWord;

    @Column(name = "connected")
    private Boolean connected;

   @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )

    @JoinColumn(name = "user_id")
    private List<BankAccount> bankAccounts = new ArrayList<>();


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(name = "user1_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id", referencedColumnName = "id")
    )
    private List<User> connections = new ArrayList<>();
    /*
   @OneToMany(
           cascade = CascadeType.ALL,
           orphanRemoval = true,
           fetch = FetchType.EAGER
   )
   @JoinColumn(name = "user1_id")
   @OneToMany(
           cascade = CascadeType.ALL,
           orphanRemoval = true,
           fetch = FetchType.EAGER
   )

   private List<Connection> connections = new ArrayList<>();*/



}