package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
    @Query(value = "SELECT target FROM connection WHERE author = :id UNION ALL SELECT author FROM connection WHERE target = :id", nativeQuery = true)
    Iterable<Integer> findFriendsIdsForOneUser(@Param("id") int userIdRequested);


}
