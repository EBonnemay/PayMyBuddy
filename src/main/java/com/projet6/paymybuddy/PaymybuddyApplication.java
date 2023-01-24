package com.projet6.paymybuddy;

import com.projet6.paymybuddy.model.BankAccount;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.service.BankAccountService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class PaymybuddyApplication implements CommandLineRunner {
	@Autowired
	private UserService userService;
	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private TransactionService transactionService;

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}
	@Override
	public void run(String...args) throws Exception{
		Iterable<User> users = userService.getUsers();
		users.forEach(user -> System.out.println(user.getEmail()));

		Iterable<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
		bankAccounts.forEach(bankAccount -> System.out.println(bankAccount.getAccountBalance()));

		Iterable<Transaction> transactions = transactionService.getTransactions();
		transactions.forEach(transaction -> System.out.println(transaction.getDateTime()));

		Optional<BankAccount> optBankAccount = bankAccountService.getBankAccountById(1);
		BankAccount bankAccountId1 = optBankAccount.get();
		System.out.println(bankAccountId1.getAccountBalance());

		Optional<Transaction> optTransaction = transactionService.getTransactionById(1);
		Transaction transactionId1 = optTransaction.get();
		System.out.println(transactionId1.getAmountOfTransaction());

		Optional<User> optUser = userService.getUserById(1);
		User userId1 = optUser.get();
		System.out.println(userId1.getFirstName());
	}

}
