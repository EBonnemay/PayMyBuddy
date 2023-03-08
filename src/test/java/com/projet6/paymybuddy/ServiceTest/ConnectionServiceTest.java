package com.projet6.paymybuddy.ServiceTest;

import com.jsoniter.any.Any;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ConnectionServiceTest {
    @Mock
    UserService userService;//the class I want to mock

    @Autowired
    private ConnectionService connectionService;//the class to test

    @Autowired
    private ConnectionRepository connectionRepository;// pose problème : considéré comme null

    @Mock
    UserRepository userRepository;
    @Test
    public void saveConnectionForCurrentUserWithEmailParameterTest(){
        //ARRANGE
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("johnY@doe.net");

        user1.setPassword("AZERT11!");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("janedoe234@yahoo.com");

        user2.setPassword("wxc0??");

        when(userService.getCurrentUsersMailAddress()).thenReturn("johnY@doe.net");
        when(userRepository.findByEmail("johnY@doe.net")).thenReturn(user1);


        when(userRepository.findByEmail("janedoe234@yahoo.com")).thenReturn(user2);

        //ACT
        connectionService.saveNewConnectionForCurrentUserWithEmailParameter("janedoe234@yahoo.com");
        //ASSERT
        assertEquals(connectionRepository.count(),1);
    }



}
