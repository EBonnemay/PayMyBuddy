package com.projet6.paymybuddy.ServiceTest;
import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")

public class AppAccountServiceTest {
    @Mock
    UserService userService;
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


    private AppAccountService appAccountService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appAccountService = new AppAccountService(appAccountRepository, userRepository,userService );


    }
    @Test
    public void findAllAppAccountInTableTest(){
        List<AppAccount> list = new ArrayList<>();

        when(appAccountRepository.findAll()).thenReturn(list);
        ArrayList<AppAccount> foundList = (ArrayList<AppAccount>) appAccountService.findAllAppAccountInTable();
        verify(appAccountRepository, times(1)).findAll();
        assertEquals(list, foundList);


    }
    @Test
    public void getAppAccountByIdTest(){
        User user = new User();
        AppAccount appAccount=new AppAccount();
        when(appAccountRepository.findById(2)).thenReturn(Optional.of(appAccount));

        AppAccount foundAppAccount = appAccountService.getAppAccountById(2);

        verify(appAccountRepository,times(1)).findById(2);
        assertEquals(appAccount, foundAppAccount);

    }
    @Test
    public void setConnectedAccountToZeroTest(){
        //ARRANGE
        User user = new User();
        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        AppAccount appAccount = new AppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(123));
        user.setAppAccount(appAccount);
        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);

        //ACT
        appAccountService.setConnectedAccountToZero();

        //ASSERT
        assertEquals(BigDecimal.valueOf(0), user.getAppAccount().getAccountBalance() );

    }
    @Test
    public void withdrawMoneyFromConnectedAccountTest(){

        User user = new User();
        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        AppAccount appAccount = new AppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(123));
        user.setAppAccount(appAccount);
        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);

        //ACT
        appAccountService.withdrawMoneyFromConnectedAccount("10");

        //ASSERT
        assertEquals(BigDecimal.valueOf(113), user.getAppAccount().getAccountBalance() );
    }
    @Test
    public void addMoneyFromConnectedAccountTest(){
        User user = new User();
        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        AppAccount appAccount = new AppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(123));
        user.setAppAccount(appAccount);
        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);

        //ACT
        appAccountService.addMoneyOnConnectedAccount("10");

        //ASSERT
        assertEquals(BigDecimal.valueOf(133), user.getAppAccount().getAccountBalance() );
    }
}
