package com.projet6.paymybuddy.RepositoryTest;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.UserRepository;

import com.projet6.paymybuddy.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserRepositoryTest {

    UserRepository userRepository;


    @Test void findByEmailTest(){
        //arrange

        //act
        User user1=  userRepository.findByEmail("jane@doe.net");

        //assert
        assertEquals(user1.getFirstName(), "Jane");

    }


}
