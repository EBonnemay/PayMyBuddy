package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    @Query(value = "SELECT id FROM transaction WHERE debited_account = :accountId", nativeQuery = true)
    Iterable<Integer> getTransactionsWithMyAccountWhenDebited(@Param("accountId") int accountId);

    @Query(value = "SELECT id FROM transaction WHERE credited_account = :accountId", nativeQuery = true)
    Iterable<Integer> getTransactionsWithMyAccountWhenCredited(@Param("accountId") int accountId);

    /*@Query (value = "SELECT id FROM transaction WHERE debited_account = :accountId UNION ALL SELECT id FROM connection WHERE credited_account = :accountId")
    Iterable<Integer> getTransactionsWithMyAccountWhenDebitedOrCredited(@Param("accountId") int accountId);
*/
}

