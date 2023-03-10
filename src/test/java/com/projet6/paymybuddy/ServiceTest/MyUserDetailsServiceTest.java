package com.projet6.paymybuddy.ServiceTest;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class MyUserDetailsServiceTest {
}
