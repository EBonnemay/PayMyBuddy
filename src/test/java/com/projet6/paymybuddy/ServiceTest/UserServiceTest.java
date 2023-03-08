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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

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

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, appAccountRepository, passwordEncoder);

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
        verify(userRepository, times(1)).save(user);
    }
}

/*@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppAccountRepository appAccountRepository;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterNewUserAccount() {
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
        verify(userRepository, times(2)).save(user);
        verify(passwordEncoder, times(1)).encode(password);
        verify(appAccountRepository, times(1)).save(account);
    }
}

    /*public class UserServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private AppAccountRepository appAccountRepository;

        @InjectMocks
        private UserService userService;

        @Test
        public void testRegisterNewUserAccount() {
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
            verify(userRepository, times(2)).save(user);
            verify(passwordEncoder, times(1)).encode(password);
            verify(appAccountRepository, times(1)).save(account);
        }
    }



    /*@Test
    public void registerNewUserAccountTest(){
        //ARRANGE
        //if(!userRepository.existsUserByEmail("john@doe.net")){
        //    userRepository.deleteByEmail("john@doe.net");
        //}


        //ACT
        //User newUser = userService.registerNewUserAccount("john", "doe", "john@doe.net", "9876DCBE");
       // AppAccount newAppAccount = newUser.getAppAccount();
        //ASSERT
       // Assertions.assertTrue(userRepository.existsUserByEmail("john@doe.net"));
        //Assertions.assertTrue(appAccountRepository.existsAppAccountByUser(newUser));

    }

}*/
