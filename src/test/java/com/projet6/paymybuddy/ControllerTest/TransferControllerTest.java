package com.projet6.paymybuddy.ControllerTest;

import com.projet6.paymybuddy.controller.TransferController;
import com.projet6.paymybuddy.model.MyException;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")

public class TransferControllerTest {
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
    @Mock
    private TransactionService transactionService;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private AppAccountService appAccountService;


    private TransferController transferController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transferController = new TransferController(userService, transactionService, connectionService, appAccountService);


    }

    @Test
    public void displayTransactionPageTest(){
        Model model = new ConcurrentModel();
        User user1 = new User();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<User>listOfFriendsForTest = new ArrayList<>();
        List<User>listOfNonDeletedFriendsForTest = new ArrayList<>();
        User user2 = new User();
        user2.setDeleted(true);
        User user3 = new User();
        user3.setDeleted(false);
        User user4 = new User();
        user4.setDeleted(false);
        listOfFriendsForTest.add(user2);
        listOfFriendsForTest.add(user3);
        listOfFriendsForTest.add(user4);
        ArrayList<Transaction> listOfTransactions = new ArrayList();
        listOfTransactions.add(transaction1);
        listOfTransactions.add(transaction2);
        when(transactionService.getConnectedUsersTransactions()).thenReturn(listOfTransactions);
        when(connectionService.getActualOrFormerFriendsUsersOfConnectedUser()).thenReturn(listOfFriendsForTest);
        String viewName = transferController.displayTransactionPage(model);
        assertEquals("transactions", viewName);
        assertTrue(model.containsAttribute("myTransactions"));
        assertTrue(model.containsAttribute("myFriends"));
    }
    @Test
    public void makeANewTransactionAndRedirectTransactionPageTest() {

        //ARRANGE
        List<MyException> listOfExceptions = new ArrayList<>();

        Transaction transaction = new Transaction();
        Model model = new ConcurrentModel();
        String email = "jdoe@example.com";
        Number amount = 12;
        String description = "cinema";
        transaction.setTransactionId(1);
        transaction.setExceptions(listOfExceptions);
        BigDecimal bdAmount = new BigDecimal(amount.toString());
        transaction.setAmountOfTransaction(bdAmount);
        transaction.setDescription(description);
        ArrayList<Transaction> listOfTransactions = new ArrayList();
        ArrayList<User> listOfNonDeletedFriends= new ArrayList<>();
        when(transactionService.makeANewTransaction(email, amount, description)).thenReturn(transaction);
        when(transactionService.getConnectedUsersTransactions()).thenReturn(listOfTransactions);
        when(connectionService.getNonDeletedFriendsUsersOfConnectedUser()).thenReturn(listOfNonDeletedFriends);


        //ACT
        String viewName = transferController.makeANewTransactionAndRedirectTransactionPage(email, amount, description, model);

        //ASSERT
        assertEquals("/transactions", viewName);
        assertTrue(model.containsAttribute("myTransactions"));
        assertTrue(model.containsAttribute("myFriends"));
        assertTrue(model.containsAttribute("transactionError"));
    }

}
