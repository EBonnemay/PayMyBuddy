package com.projet6.paymybuddy;

import com.projet6.paymybuddy.model.BankAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.Transaction;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.service.BankAccountService;
import com.projet6.paymybuddy.service.TransactionService;
import com.projet6.paymybuddy.service.ConnectionService;

import com.projet6.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class PaymybuddyApplication implements CommandLineRunner {
	@Autowired
	private UserService userService;
	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ConnectionService connectionService;

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}
	@Override
	public void run(String...args) throws Exception{
		/** print the email of every user*/
		Iterable<User> users = userService.getUsers();
		users.forEach(user -> System.out.println(user.getEmail()));//print email de chaque objet
		/**print the name of the User with id 1*/
		Optional<User> optUser = userService.getUserById(1);//optUser = objet 1 potentiel
		User firstUser = optUser.get(); //firstUser
		System.out.println(firstUser.getFirstName());
		/**print the numbers (iban) of user with id 1 - check later if can do the same from bankaccount entity*/
		firstUser.getBankAccounts().forEach(
				bankAccount -> System.out.println(bankAccount.getIban()));


		/**considering I am user with id 1, print the first name of users I selected to be my friends*/
		//firstUser.getUsersIconnectedTo().forEach(//je peux aussi récupérer les connections
				//user ->System.out.println(user.getFirstName()));
		/**considering I am user with id 1, print the first name of users who selected me to be my friends*/
		//firstUser.getUsersConnectedWithMe().forEach(//je peux aussi récupérer les connections
				//user ->System.out.println(user.getFirstName()));
		/**considering I am User with id 1, print all my friends*/
		/*List<User> friends = new ArrayList<>();
			firstUser.getUsersIconnectedTo().forEach(
				user -> {if (!friends.contains(user))
						{ friends.add(user);}});
			firstUser.getUsersConnectedWithMe().forEach(
				user ->	{if (!friends.contains(user))
						{ friends.add(user);}});
		friends.forEach(
				friend -> System.out.println(friend.getFirstName())
		);*/
		/**print the amount of money on each account*/
		Iterable<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
		bankAccounts.forEach(bankAccount -> System.out.println(bankAccount.getAccountBalance()));

		/**print the amount of money on bankaccount with id 2*/
		Optional<BankAccount> optBankAccount = bankAccountService.getBankAccountById(2);
		BankAccount bankAccountId2 = optBankAccount.get();
		System.out.println(bankAccountId2.getAccountBalance());
		/**given I own bankaccount with id 2, give me the firstName of persons who gave me money, and the iban and id of the bankaccount they used*/
		bankAccountId2.getBankAccountsOfFriendsGivingMoneyToMe().forEach(
				account -> System.out.println("this count gave me money "+account.getOwner().getFirstName()+" from his/her count with iban "+ account.getIban()+" and id "+account.getId()));
		/**print the status of all transactions*/
		Iterable<Transaction> transactions = transactionService.getTransactions();
		transactions.forEach(transaction -> System.out.println(transaction.getStatusOfTransaction()));
		/**print amount of transaction with id 1*/
		Optional<Transaction> optTransaction = transactionService.getTransactionById(1);
		Transaction transactionId1 = optTransaction.get();
		System.out.println(transactionId1.getAmountOfTransaction());

		/**print firstName of the owner of the debited count of transaction with id 1*/
		int giverAccountId = transactionId1.getDebitedAccount_Id();
		Optional<BankAccount> optBank = bankAccountService.getBankAccountById(giverAccountId);//je pourrais aussi faire un lien direct transaction user.
		BankAccount giverAccount = optBank.get();
		System.out.println ("giver of first transaction is "+giverAccount.getOwner().getFirstName());
//ajout d'un user en db
		/*User newUser = new User();
		newUser.setFirstName("Orson");
		newUser.setLastName("Welles");
		userService.addUser(newUser);*/
//suppression d'un user en db

		//userService.deleteUserById(6);
//ajout d'un bank account en db//
		/*BankAccount newBankAccount = new BankAccount();
		newBankAccount.setIban("aaaaaaaaaaaaaaa");
		newBankAccount.setAccountBalance(1000);
		bankAccountService.addBankAccount(newBankAccount);*/
//suppression d'un bank account en db

		//bankAccountService.deleteBankAccountById(6);
/** print the list of connection ids for user 4*/
		Iterable<Connection> connections = connectionService.getConnectionsForOneUser(4);
		connections.forEach(connection -> System.out.println(connection.getConnectionId()));


	}

}
