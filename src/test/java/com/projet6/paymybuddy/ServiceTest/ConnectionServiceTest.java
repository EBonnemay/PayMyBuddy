package com.projet6.paymybuddy.ServiceTest;

import com.jsoniter.any.Any;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ConnectionServiceTest {
    @Mock
    private ConnectionRepository connectionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private Authentication authentication;

    private ConnectionService connectionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        connectionService = new ConnectionService(userService, userRepository, connectionRepository);
    }
    @Test
    public void getFriendsIdsForOneUserByIdTest(){
        List<Integer> expectedlist = new ArrayList<>();
        expectedlist.add(2);
        when(connectionRepository.findFriendsIdsForOneUser(1)).thenReturn(expectedlist);

        List<Integer> actualList = (List<Integer>) connectionService.getFriendsIdsForOneUserById(1);
        verify(connectionRepository,times(1)).findFriendsIdsForOneUser(1);
    }
    @Test
    public void getFriendsIdsForOneUserByEmailTest() {
        User user = new User();
        user.setEmail("johndoe@example.com");
        user.setId(1);

        List<Integer> expectedlist = new ArrayList<>();
        expectedlist.add(2);
        when(connectionRepository.findFriendsIdsForOneUser(1)).thenReturn(expectedlist);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        List<Integer> actualList = (List<Integer>) connectionService.getFriendsIdsForOneUserByEmail("johndoe@example.com");
        verify(connectionRepository, times(1)).findFriendsIdsForOneUser(1);
        assertEquals(connectionService.getFriendsIdsForOneUserByEmail("johndoe@example.com"), connectionService.getFriendsIdsForOneUserById(1));
    }
        @Test
    public void deleteConnectionTest(){

    }

    @Test
    public void saveConnectionForCurrentUserWithEmailParameterTest(){
        //ARRANGE
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("johndoe@example.com");

        user1.setPassword("AZERT11!");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("janedoe@example.com");

        user2.setPassword("wxc0??");
        Connection expectedConnection = new Connection();
        expectedConnection.setAuthor(user1);
        expectedConnection.setTarget(user2);




        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user1);


        when(userRepository.findByEmail("janedoe@example.com")).thenReturn(user2);
        when (connectionRepository.save(any(Connection.class))).thenReturn(expectedConnection);

        //ACT
        Connection foundConnection = connectionService.saveNewConnectionForCurrentUserWithEmailParameter("janedoe@example.com");
        //ASSERT
        verify(userRepository, times(3)).findByEmail(any(String.class));
        assertEquals(expectedConnection, foundConnection);
    }

    @Test
    public void getActualOrFormerFriendsUsersOfConnectedUserTest(){

    }
    @Test
    public void getNonDeletedFriendsUsersOfConnectedUser(){

    }

}
