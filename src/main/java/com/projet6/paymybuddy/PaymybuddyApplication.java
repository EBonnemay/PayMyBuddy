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
	

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}
	@Override
	public void run(String...args) throws Exception{


	}

}
