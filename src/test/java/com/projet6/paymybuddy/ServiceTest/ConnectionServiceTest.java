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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
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
        User user2 = new User();
        user2.setId(2);
        user2.setEmail("janedoe@example.com");
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("johndoe@example.com");
        Connection connection = new Connection();
        connection.setAuthor(user2);
        connection.setTarget(user1);
        when(userService.getCurrentUsersMailAddress()).thenReturn("janedoe@example.com");
        when(userRepository.findByEmail("janedoe@example.com")).thenReturn(user2);
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user1);

        connectionService.deleteConnection("johndoe@example.com");
        verify(connectionRepository,times(1)).deleteRelationBetweenThoseUsers(2,1);

    }

    @Test
    public void saveNewConnectionForCurrentUserWithEmailParameterTest(){
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
        List<Integer> expectedListOfIds = new ArrayList<>();
        expectedListOfIds.add(2);
        expectedListOfIds.add(3);
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        user1.setEmail("johndoe@example.com");
        user2.setEmail("janedoe@example.com");
        user3.setEmail("jackdoe@example.com");
        user1.setDeleted(false);
        user2.setDeleted(false);
        user3.setDeleted(true);
        Connection connection1_2 = new Connection();
        Connection connection1_3 = new Connection();
        connection1_2.setAuthor(user1);
        connection1_2.setTarget(user2);
        connection1_3.setAuthor(user3);
        connection1_3.setTarget(user1);



        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user1);

        //when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3)).thenReturn(Optional.of(user3));

        when(connectionRepository.findFriendsIdsForOneUser(1)).thenReturn(expectedListOfIds);
       // when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        List<User>friends = connectionService.getActualOrFormerFriendsUsersOfConnectedUser();

        assertTrue(friends.contains(user2));
        assertTrue(friends.contains(user3));


    }
    @Test
    public void getNonDeletedFriendsUsersOfConnectedUser(){
        List<Integer> expectedListOfIds = new ArrayList<>();
        expectedListOfIds.add(2);
        expectedListOfIds.add(3);
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        user1.setEmail("johndoe@example.com");
        user2.setEmail("janedoe@example.com");
        user3.setEmail("jackdoe@example.com");
        user1.setDeleted(false);
        user2.setDeleted(false);
        user3.setDeleted(true);
        Connection connection1_2 = new Connection();
        Connection connection1_3 = new Connection();
        connection1_2.setAuthor(user1);
        connection1_2.setTarget(user2);
        connection1_3.setAuthor(user3);
        connection1_3.setTarget(user1);



        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user1);

        //when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3)).thenReturn(Optional.of(user3));

        when(connectionRepository.findFriendsIdsForOneUser(1)).thenReturn(expectedListOfIds);
        // when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        List<User>friends = connectionService.getNonDeletedFriendsUsersOfConnectedUser();

        assertTrue(friends.contains(user2));
        assertFalse(friends.contains(user3));


    }

}
