package com.projet6.paymybuddy.ServiceTest;


import com.projet6.paymybuddy.model.AppAccount;

import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.TransactionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class TransactionServiceTest {
    @Mock
    AppAccountRepository appAccountRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    private ConnectionRepository connectionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private Authentication authentication;

    private TransactionService transactionService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository, userService, userRepository, appAccountRepository);
    }

    /*@Test
    public void makeANewTransactionCaseSuccessTest(){
        transactionService.makeANewTransaction(String emailFriend, Number amount, String description)
    }*/
    @Test
    public void makeANewTransactionCaseFailNoValidTargetInputTest(){

        Transaction transaction = transactionService.makeANewTransaction(null,12, "cafe");
        //Assertions.assertThrows(MyException.class, ()-> transactionService.makeANewTransaction(null,12, "cafe"));
        assertFalse(transaction.getExceptions().isEmpty());
        assertEquals("you must enter a valid email address", transaction.getExceptions().get(0).getMessage());
    }

    @Test
    public void makeANewTransactionCaseFailNoValidDescriptionInputTest(){
        Transaction transaction = transactionService.makeANewTransaction("janedoe@example.com", 13, null);

        assertFalse(transaction.getExceptions().isEmpty());
        assertEquals("you must enter a description", transaction.getExceptions().get(0).getMessage());

    }
    @Test
    public void makeANewTransactionCaseFailAmountInputNullTest(){
        Transaction transaction = transactionService.makeANewTransaction("janedoe@example.com", null, "cafe");
        assertFalse(transaction.getExceptions().isEmpty());
        assertEquals("invalid amount", transaction.getExceptions().get(0).getMessage());

    }
    @Test
    public void makeANewTransactionCaseFailAmountInputNegativeTest(){
        Transaction transaction = transactionService.makeANewTransaction("janedoe@example.com", -13, "cafe");
        assertFalse(transaction.getExceptions().isEmpty());
        assertEquals("amount cannot be negative", transaction.getExceptions().get(0).getMessage());

    }
    @Test
    public void makeANewTransactionCaseFailAmountAccountNotProvisionedTest(){
        User user1 = new User();
        user1.setEmail("johndoe@example.com");
        AppAccount appAccount = new AppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(10));
        user1.setAppAccount(appAccount);
        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user1);

        Transaction transaction = transactionService.makeANewTransaction("johndoe@example.com", 10, "juice");

        assertFalse(transaction.getExceptions().isEmpty());
        assertEquals("your account is not provisioned for this operation", transaction.getExceptions().get(0).getMessage());

    }


    @Test
    public void getConnectedUsersTransactionsTest(){
        //ARRANGE
        List<Integer> expectedList = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("johndoe@example.com");
        AppAccount appAccount = new AppAccount();
        appAccount.setId(1);
        appAccount.setAccountBalance(BigDecimal.valueOf(1));
        user1.setAppAccount(appAccount);
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        transaction1.setTransactionId(8);
        transaction2.setTransactionId(9);
        transaction1.setDebitedAccount(appAccount);
        transaction2.setDebitedAccount(appAccount);
        expectedList.add(8);
        expectedList.add(9);
        Iterable<Integer> iterablelist = expectedList;

        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user1);
        when(transactionRepository.getTransactionsIdsWithMyAccountWhenDebitedOrCredited(1)).thenReturn(expectedList);
        when(transactionRepository.findById(8)).thenReturn(Optional.of(transaction1));
        when(transactionRepository.findById(9)).thenReturn(Optional.of(transaction2));

         //ACT
        List<Transaction> myEffectiveTransactions = transactionService.getConnectedUsersTransactions();

        //ASSERT
        assertEquals(2, myEffectiveTransactions.size());
        assertTrue(myEffectiveTransactions.contains(transaction1)); //.contains.getTransactionId()==8);
        assertTrue(myEffectiveTransactions.contains(transaction2)); //.contains.getTransactionId()==8);

    }

}
