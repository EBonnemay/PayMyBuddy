package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.AppAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppAccountRepository extends CrudRepository<AppAccount, Integer> {
}
