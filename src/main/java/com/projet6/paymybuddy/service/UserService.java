package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private UserRepository userRepository;
    private AppAccountRepository appAccountRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AppAccountRepository appAccountRepository, PasswordEncoder passwordEncoder){
        this. userRepository = userRepository;
        this.appAccountRepository=appAccountRepository;//?? faut-il mettre le repo appaccount en paramètre du userService?
        this.passwordEncoder=passwordEncoder;
    }


    public User getUserById(int id) {
        Optional<User> optUser = userRepository.findById(id);
        User user = optUser.get();
        return user;
    }



    public AppAccount getAppAccountOfConnectedUser() {
    String email = getCurrentUsersMailAddress();
    User user = userRepository.findByEmail(email);
    AppAccount appAccount = user.getAppAccount();

    return appAccount;
}

public String getCurrentUsersMailAddress(){
        //Authentication auth = getAuthentication();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return email;
    }


    //@Override
    public User registerNewUserAccount(String firstName, String lastName, String email, String password ) {

        User user = new User();

        user.setFirstName(firstName);

        user.setLastName(lastName);
       user.setPassword(passwordEncoder.encode(password));
       user.setEmail(email);
        user.setDeleted(false);
        userRepository.save(user);

        AppAccount account = new AppAccount();
        account.setAccountBalance(BigDecimal.valueOf(0));

        //account.set
        account.setUser(user);
        appAccountRepository.save(account);
        //user.setAppAccount(account);//sert à rien? avant de faire le setUp accountsave account.sauver account avant

        userRepository.save(user);

        return user;
    }
    public User markUserAsDeleted(User user){
        user.setDeleted(true);
        userRepository.save(user);
        return user;
    }


}
