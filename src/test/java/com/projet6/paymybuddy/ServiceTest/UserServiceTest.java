package com.projet6.paymybuddy.ServiceTest;

import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private AppAccountRepository appAccountRepository;

    @Test
    public void registerNewUserAccountTest(){
        //ARRANGE
        //userRepository.deleteById(1);
        //userRepository.deleteById(2);
        User user = new User();
        user.setFirstName("al");
        user.setLastName("di");
        user.setEmail("aldi@yahoo.com");
        user.setEmailPassword("9876DCBE");


        //ACT
        userService.registerNewUserAccount(user);
        //ASSERT
        Assertions.assertTrue(userRepository.existsUserByEmail("aliernest@yahoo.com"));
        Assertions.assertTrue(appAccountRepository.existsAppAccountByUser(user));

    }

}
