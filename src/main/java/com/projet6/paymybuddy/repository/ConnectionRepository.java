package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
    @Query(value = "SELECT user2_id FROM connection WHERE user1_id = :id UNION ALL SELECT user1_id FROM connection WHERE user2_id = :id", nativeQuery = true)
    Iterable<Integer> findFriendsIdForOneUser(@Param("id") int userRequested);


}
