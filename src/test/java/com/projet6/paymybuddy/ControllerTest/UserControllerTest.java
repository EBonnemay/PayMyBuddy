package com.projet6.paymybuddy.ControllerTest;

import com.projet6.paymybuddy.controller.UserController;
import com.projet6.paymybuddy.model.AppAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.MyException;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.AppAccountRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.ConnectionService;
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
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")

public class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;
    @Mock
   private ConnectionService connectionService;
    @Mock
    private AppAccountService appAccountService;


    @Mock
    private AppAccountRepository appAccountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    //@Mock
    //private SecurityContextHolder securityContextHolder;

    @Mock
    private Authentication auth;

    private UserController userController;

    //private UserController userController;

    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService, appAccountService,connectionService, userRepository );
        //when(userService.getUserRepository()).thenReturn(userRepository);

    }
    @Test
    public void displayPersonalPageTest(){
        //ARRANGE
        ArrayList<User>list = new ArrayList();
        User friend1 = new User();
        User friend2 = new User();
        list.add(friend1);
        list.add(friend2);
        User user = new User();
        AppAccount appAccount = new AppAccount();
        user.setAppAccount(appAccount);
        when(connectionService.getNonDeletedFriendsUsersOfConnectedUser()).thenReturn(list);
        when(userService.getAppAccountOfConnectedUser()).thenReturn(appAccount);
        Model model = new ConcurrentModel();
        //ACT
        String view = userController.displayPersonalPage( model);
        //ASSERT
        assertEquals("personalPage", view);
        assertTrue(model.containsAttribute("myNonDeletedFriends"));
        assertTrue(model.containsAttribute("myappaccount"));
    }


   @Test
    public void addConnectionAndRedirectPersonalPageTest(){
       //ARRANGE
       Connection connection = new Connection();
       ArrayList<MyException>listOfExceptions = new ArrayList<>();
       MyException exception = new MyException("testException");
       listOfExceptions.add(exception);
       ArrayList<User>list = new ArrayList<>();
       connection.setExceptions(listOfExceptions);
       User friend1 = new User();
       User friend2 = new User();
       list.add(friend1);
       list.add(friend2);
       User user = new User();
       friend1.setEmail("janedoe@example.com");
       AppAccount appAccount = new AppAccount();
       user.setAppAccount(appAccount);
       when(connectionService.saveNewConnectionForCurrentUserWithEmailParameter(friend1.getEmail())).thenReturn(connection);
       when(connectionService.getNonDeletedFriendsUsersOfConnectedUser()).thenReturn(list);
       when(userService.getAppAccountOfConnectedUser()).thenReturn(appAccount);
       Model model = new ConcurrentModel();
       //ACT
       String view = userController.addConnectionAndRedirectPersonalPage(friend1.getEmail(), model);
       //ASSERT
       assertEquals("/personalPage", view);
       assertTrue(model.containsAttribute("myNonDeletedFriends"));
       assertTrue(model.containsAttribute("myappaccount"));
       assertTrue(model.containsAttribute("connectionError"));

}

    @Test
    public void deleteConnectionAndRedirectPersonalPageTest(){
        //ARRANGE
        Connection connection = new Connection();
        ArrayList<MyException>listOfExceptions = new ArrayList<>();
        MyException exception = new MyException("testException");
        listOfExceptions.add(exception);
        ArrayList<User>list = new ArrayList<>();
        connection.setExceptions(listOfExceptions);
        User friend1 = new User();
        User friend2 = new User();
        list.add(friend1);
        list.add(friend2);
        User user = new User();
        friend1.setEmail("janedoe@example.com");
        AppAccount appAccount = new AppAccount();
        user.setAppAccount(appAccount);
        doNothing().when(connectionService).deleteConnection(friend1.getEmail());

        //ACT
        String view = userController.deleteConnectionAndRedirectPersonalPage(friend1.getEmail());
        //ASSERT
        assertEquals("redirect:/personalPage", view);
        verify(connectionService, times(1)).deleteConnection(friend1.getEmail());



        }

    @Test
    public void displayProfilePageTest(){
        //ARRANGE
        User user = new User();
        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        Model model = new ConcurrentModel();
        user.setDeleted(false);
        AppAccount appAccount = new AppAccount();
        appAccount.setAccountBalance(BigDecimal.valueOf(12));
        user.setAppAccount(appAccount);

        when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);
        //ACT
        String view = userController.displayProfilePage(model);
        //ASSERT
        assertEquals("profile", view);
        assertTrue(model.containsAttribute("name"));
        assertTrue(model.containsAttribute("status"));
        assertTrue(model.containsAttribute("email"));
        assertTrue(model.containsAttribute("balance"));
    }
    @Test
    public void displayUpdateAppAccountPageTest() {
        //ARRANGE
        AppAccount appAccount = new AppAccount();
        appAccount.setId(1);

        User user = new User();

        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDeleted(false);
        appAccount.setUser(user);


        Model model = new ConcurrentModel();


        //appAccount.setAccountBalance(BigDecimal.valueOf(12));


        when(appAccountService.getAppAccountById(1)).thenReturn(appAccount);
        //when(userService.getCurrentUsersMailAddress()).thenReturn("johndoe@example.com");
       // when(userRepository.findByEmail("johndoe@example.com")).thenReturn(user);
        //ACT
        String view = userController.displayUpdateAppAccountPage("1", model);
        //ASSERT
        assertEquals("update_appaccount", view);
        assertTrue(model.containsAttribute("name"));
        assertTrue(model.containsAttribute("myappaccount"));



    }
    @Test
    public void  addMoneyOnMyAppAccountAndRedirectPersonalPageTest() {
        //ARRANGE
        String amount_added = "13";



        doNothing().when(appAccountService).addMoneyOnConnectedAccount( amount_added);

        //ACT
        String view = userController.addMoneyOnMyAppAccountAndRedirectPersonalPage( amount_added);

        //ASSERT
        assertEquals("redirect:/personalPage", view);
        verify(appAccountService, times(1)).addMoneyOnConnectedAccount(amount_added);


    }


    @Test
    public void withdrawMoneyFromMyAppAccountAndRedirectPersonalPageTest() {
        //ARRANGE
        String amount_withdrawed = "13";



        doNothing().when(appAccountService).withdrawMoneyFromConnectedAccount( amount_withdrawed);

        //ACT
        String view = userController.withdrawMoneyFromMyAppAccountAndRedirectPersonalPage(amount_withdrawed);

        //ASSERT
        assertEquals("redirect:/personalPage", view);
        verify(appAccountService, times(1)).withdrawMoneyFromConnectedAccount(amount_withdrawed);

        }

    @Test
    public void emptyMyAppAccountAndRedirectPersonalPageTest(){

        doNothing().when(appAccountService).setConnectedAccountToZero();
        String view = userController.emptyMyAppAccountAndRedirectPersonalPage();
        verify(appAccountService, times(1)).setConnectedAccountToZero();
        assertEquals("redirect:/personalPage", view);
    }
}



