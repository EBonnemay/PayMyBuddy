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

        /*user1_id INT NOT NULL,
    -> user2_id INT NOT NULL,
    -> FOREIGN KEY (user1_id) REFERENCES user (id),
    -> FOREIGN KEY (user2_id) REFERENCES user (id),
    -> CONSTRAINT relationship_not_repeated UNIQUE(user1_id, user2_id),
    -> CONSTRAINT ids_not_equal CHECK(user1_id != user2_id),
    -> PRIMARY KEY (user1_id, user2_id)*/
}