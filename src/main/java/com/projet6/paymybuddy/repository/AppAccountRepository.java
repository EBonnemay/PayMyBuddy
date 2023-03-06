package com.projet6.paymybuddy.repository;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppAccountRepository extends CrudRepository<AppAccount, Integer> {
    boolean existsAppAccountByUser(User user);

}
