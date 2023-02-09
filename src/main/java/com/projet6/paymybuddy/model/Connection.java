package com.projet6.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "connection" )
public class Connection {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int connectionId;

        @ManyToOne(
                cascade = CascadeType.PERSIST,
                fetch = FetchType.EAGER
        )
        @JoinColumn(name = "author")
        private User author;

        @ManyToOne(
                cascade = CascadeType.PERSIST,
                fetch = FetchType.EAGER

        )
        @JoinColumn(name = "target")
        private User target;

}