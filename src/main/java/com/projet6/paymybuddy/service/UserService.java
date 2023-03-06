package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
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
    UserService(UserRepository userRepository, AppAccountRepository appAccountRepository){
        this. userRepository = userRepository;
        this.appAccountRepository=appAccountRepository;//?? faut-il mettre le repo appaccount en paramètre du userService?

    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> optUser = userRepository.findById(id);
        User user = optUser.get();
        return user;
    }

   /*public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUserById(int Id) {
        userRepository.deleteById(Id);

    }*/

    public AppAccount getAppAccountOfConnectedUser() {
    String email = getCurrentUsersMailAddress();
    User user = userRepository.findByEmail(email);
   AppAccount appAccount = user.getAppAccount();

    return appAccount;
}
public User getUserFromEmail(String email){
        return userRepository.findByEmail(email);
}
public String getCurrentUsersMailAddress(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return email;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    //@Override
    public User registerNewUserAccount(String firstName, String lastName, String email, String password ) {

        User user = new User();

        user.setFirstName(firstName);

        user.setLastName(lastName);
       user.setPassword(passwordEncoder.encode(password));
       user.setEmail(email);

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
    //il faut aussi créer un appaccount pour cette personne, sinon la page personalPage ne s'affiche pas
}
