package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    @Query(value = "SELECT id FROM transaction WHERE debited_account = :accountId", nativeQuery = true)
    Iterable<Integer> getTransactionsIdsWithMyAccountWhenDebited(@Param("accountId") int accountId);

    @Query(value = "SELECT id FROM transaction WHERE credited_account = :accountId", nativeQuery = true)
    Iterable<Integer> getTransactionsIdsWithMyAccountWhenCredited(@Param("accountId") int accountId);

    //ajout après soutenance :
    @Query(value = "SELECT id FROM transaction WHERE credited_account = :accountId UNION ALL SELECT id FROM transaction WHERE debited_account = :accountId", nativeQuery = true)
    Iterable<Integer> getTransactionsIdsWithMyAccountWhenDebitedOrCredited(@Param("accountId")int accountId);

}

