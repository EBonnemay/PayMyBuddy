package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.BankAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.BankAccountRepository;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserRepository userRepository;

    public Iterable<Connection> getConnections(){
        return connectionRepository.findAll();
    }

    public Optional<Connection> getConnectionById(Integer id){
        return connectionRepository.findById(id);
    }

    public Connection addConnection(Connection connection){return connectionRepository.save(connection);}
    public Iterable<Integer> getFriendsIdForOneUser(Integer Id){
        return connectionRepository.findFriendsIdForOneUser(Id);
    }
    public Iterable<String> getNamesOfFriends(Integer Id) {
        List<String> listOfFriends = new ArrayList();

        Iterable<Integer> listOfIds = getFriendsIdForOneUser(Id);
        for(Integer id : listOfIds){
            Optional < User > optUser = userRepository.findById(id);
            User user = optUser.get();
            listOfFriends.add(user.getFirstName() +" "+ user.getLastName());
        }
        return listOfFriends;
    }

    }



