package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
    @Query(value = "SELECT id FROM connection WHERE user1_id = :id OR user2_id = :id", nativeQuery = true)
    public Iterable<Connection> findConnectionsForOneUser(@Param("id") int userRequested);
}
