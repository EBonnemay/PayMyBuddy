package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
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


    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "deleted")
    private boolean deleted;



    @OneToOne(mappedBy = "user")
    private AppAccount appAccount;

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

    @Transient
    List<MyException> exceptions;


}