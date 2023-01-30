package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.BankAccount;
import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.repository.BankAccountRepository;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    public Iterable<Connection> getConnections(){
        return connectionRepository.findAll();
    }

    public Optional<Connection> getConnectionById(Integer id){
        return connectionRepository.findById(id);
    }

    public Connection addConnection(Connection connection){return connectionRepository.save(connection);}
    public Iterable<Connection> getConnectionsForOneUser(Integer Id){
        return connectionRepository.findConnectionsForOneUser(Id);
    }

}

