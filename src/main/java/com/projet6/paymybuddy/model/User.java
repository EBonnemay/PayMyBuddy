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
@Table(name = "user" )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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
           fetch = FetchType.EAGER,
           mappedBy = "user",
           cascade = CascadeType.ALL,
            orphanRemoval = true
           // fetch = FetchType.EAGER//quand on recupere user, tous les ba viennent en même temps
    )

    //@JoinColumn(name = "user_id_email")
    private List<BankAccount> bankAccounts = new ArrayList<>();

   public void addBankAccount(BankAccount bankAccount){
       bankAccounts.add(bankAccount);
       bankAccount.setUser(this);
   }
   public void removeBankAccount(BankAccount bankAccount){
       bankAccounts.add(bankAccount);
       bankAccount.setUser(this);
   }



/*
    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },
            fetch = FetchType.EAGER
    )

    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(name = "user2_id", referencedColumnName = "id"),//creates 1 FK in connection, pointing to id in User.
            inverseJoinColumns = @JoinColumn(name = "user1_id", referencedColumnName = "id")

    )
    private List<User> usersConnectedWithMe = new ArrayList<>();






    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(name = "user1_id", referencedColumnName = "id"),//creates 1 FK in connection, pointing to id in User.
            inverseJoinColumns = @JoinColumn(name = "user2_id", referencedColumnName = "id")

    )
    private List<User> usersIconnectedTo = new ArrayList<>();



    public void addFriend(User user) {
        friends.add(user);
        user.getFriends().add(this);
    }

    public void removeFriend(User user) {
        usersIconnectedTo.remove(user);
        user.setUsersIconnectedTo(null);
    }
// faire une seule liste d'amis?
public void addBankAccount(BankAccount bankAccount) {
    bankAccounts.add(bankAccount);
    bankAccounts.setBankAccounts(bankAccounts)

    public void removeFriend(User user) {
        usersIconnectedTo.remove(user);
        user.setUsersIconnectedTo(null);
    }*/
@OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "author",
        cascade = CascadeType.ALL,
        orphanRemoval = true
)
private List<Connection> connectionsAsAuthor = new ArrayList<>();
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "target",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Connection> connectionsAsTarget = new ArrayList<>();
}