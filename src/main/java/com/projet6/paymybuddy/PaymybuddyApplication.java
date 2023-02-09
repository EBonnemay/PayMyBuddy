package com.projet6.paymybuddy;

import com.projet6.paymybuddy.service.AppAccountService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.ConnectionService;

import com.projet6.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymybuddyApplication implements CommandLineRunner {
	@Autowired
	private UserService userService;
	@Autowired
	private AppAccountService appAccountService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ConnectionService connectionService;

	public static void main(String[] args) {
		System.out.println("main called");
		SpringApplication.run(PaymybuddyApplication.class, args);
	}
	@Override
	public void run(String...args) throws Exception{
		System.out.println("run called");


	}

}
