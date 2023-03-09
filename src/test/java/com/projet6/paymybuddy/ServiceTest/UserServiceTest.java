package com.projet6.paymybuddy.ServiceTest;

import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")


public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AppAccountRepository appAccountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    //@Mock
    //private SecurityContextHolder securityContextHolder;
    @Mock
    private Authentication auth;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, appAccountRepository, passwordEncoder);
    }

    //
    @Test
    public void getUserByIdTest() {
        User user = new User();
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(2);

        verify(userRepository,times(1)).findById(2);
        assertEquals(user, foundUser);


    }
    @Test
    public void getAppAccountOfConnectedUserTest(){
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = new User();
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@example.com";
        String password = "password";
        //String name = "johndoe@example.com";
        AppAccount johns_account = user.getAppAccount();
        when(auth.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        AppAccount appaccount = userService.getAppAccountOfConnectedUser();

        verify(auth, times(1)).getName();
        verify(userRepository, times(1)).findByEmail(email);
        assertEquals(johns_account, appaccount);



    }
    @Test
    public void getCurrentUsersMailAddressTest(){


        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.getName()).thenReturn("test@test.com");

        String requiredMail = userService.getCurrentUsersMailAddress();

        assertEquals("test@test.com", requiredMail );


    }
    @Test
    public void registerNewUserTest(){
        String firstName = "John";
        String lastName = "Doe";
        String email = "johndoe@example.com";
        String password = "password";

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDeleted(false);
        when(passwordEncoder.encode(password)).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        AppAccount account = new AppAccount();
        account.setAccountBalance(BigDecimal.valueOf(0));
        account.setUser(user);

        when(appAccountRepository.save(any(AppAccount.class))).thenReturn(account);

        User registeredUser = userService.registerNewUserAccount(firstName, lastName, email, password);


        assertNotNull(registeredUser);
        assertEquals(firstName, registeredUser.getFirstName());
        assertEquals(lastName, registeredUser.getLastName());
        assertEquals(email, registeredUser.getEmail());
        assertFalse(registeredUser.isDeleted());
        verify(userRepository, times(2)).save(registeredUser);
        verify(passwordEncoder, times(1)).encode(password);
        verify(appAccountRepository, times(1)).save(any(AppAccount.class));
    }
    @Test
    public void markUserAsDeletedTest(){
        //ARRANGE
        User user = new User();
        //ACT
        user = userService.markUserAsDeleted(user);
        //ASSERT
        assertTrue(user.isDeleted());
        //pourquoi userRepo peut il être appelé sans renvoyer NULL?(pas de when)???
        verify(userRepository, times(1)).save(user);
    }
}

