package com.projet6.paymybuddy.ControllerTest;

import com.projet6.paymybuddy.controller.SignAndLogController;

import com.projet6.paymybuddy.model.User;

import com.projet6.paymybuddy.repository.UserRepository;

import com.projet6.paymybuddy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")

public class SignAndLogControllerTest {
    @Mock
    UserService userService;
    @Mock
    UserRepository userRepository;

    private SignAndLogController signAndLogController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signAndLogController = new SignAndLogController(userService, userRepository);


    }
    @Test
    public void displayLoginPageTest(){
        String viewName = signAndLogController.displayLoginPage();
        Assertions.assertEquals("login", viewName);

    }
    @Test
    public void displayRegisterPageTest(){
        String viewName = signAndLogController.displayRegisterPage();
        Assertions.assertEquals("registerPage", viewName);

    }
    @Test
    public void registerUserTest(){
        User user = new User();

        Model model = new ConcurrentModel();
        when(userService.registerNewUserAccount("John", "Doe", "jdoe@example.com", "jdoe")).thenReturn(user);


        //ACT

        Assertions.assertEquals("login", signAndLogController.registerUser("John", "Doe", "jdoe@example.com", "jdoe", model));
    }
    @Test
    public void displayLoginPageFromLogoutTest(){
        Assertions.assertEquals("login", signAndLogController.displayLoginPageFromLogout());

    }
    @Test
    public void unsubscribeTest(){
        User user = new User();

        when(userService.getCurrentUsersMailAddress()).thenReturn("jdoe@example.com");
        when(userRepository.findByEmail("jdoe@example.com")).thenReturn(user);
        when(userService.markUserAsDeleted(user)).thenReturn(user);
        Assertions.assertEquals("redirect:/login", signAndLogController.unsubscribe());
    }


}
