package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.MyException;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.UserRepository;

import com.sun.tools.jconsole.JConsoleContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserRepository userRepository;

    static final Logger logger = LogManager.getLogger();

    public Iterable<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    public Optional<Connection> getConnectionById(Integer id) {
        return connectionRepository.findById(id);
    }

    public Connection addConnection(Connection connection) {
        return connectionRepository.save(connection);
    }

    public Iterable<Integer> getFriendsIdsForOneUserById(int id) {
        return connectionRepository.findFriendsIdsForOneUser(id);
    }

    public Iterable<Integer> getFriendsIdsForOneUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        int id = user.getId();
        return getFriendsIdsForOneUserById(id);
    }

    public void saveConnection(Connection connection) {
        connectionRepository.save(connection);
    }
    @Transactional
    public void deleteConnection(String emailOfUserToRemoveFromConnection){
        //try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication ok, called auth - here auth is email");
            String email = auth.getName();

            User connectedUser = userRepository.findByEmail(email);
            User userToDeleteFromConnection = userRepository.findByEmail(emailOfUserToRemoveFromConnection);

            connectionRepository.deleteRelationBetweenThoseUsers(connectedUser.getId(), userToDeleteFromConnection.getId());
            logger.info("connection with user "+ emailOfUserToRemoveFromConnection + " deleted successfully");

    }
    public Connection saveConnectionForCurrentUserWithEmailParameter(String friendEmail) {
        Connection newConnection = new Connection();
        List<MyException> listOfConnectionExceptions = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication ok, called auth - here auth is email");
        String email = auth.getName();

        User author = userRepository.findByEmail(email);
        try {
            if (userRepository.findByEmail(friendEmail)==null) {
                String message = "you must enter an email address";
                MyException exception = new MyException(message);
                logger.debug("user input error : input cannot be void");
                throw exception;
            }
        }catch(MyException exception){
            listOfConnectionExceptions.add(exception);
            newConnection.setExceptions(listOfConnectionExceptions);
            return newConnection;
        }
        User target = userRepository.findByEmail(friendEmail);

        int authorId = author.getId();
        int targetId = target.getId();

        Iterable<Integer> iterable = getFriendsIdsForOneUserById(authorId);
        Iterator<Integer> iterator = iterable.iterator();
        List<Integer> friendsIds = new ArrayList<Integer>();
        iterable.forEach(friendsIds::add);
        try {
            if (friendsIds.contains(targetId)) {
                String message = "friend already in list";
                MyException exception = new MyException(message);
                logger.debug("user input error : friend already in list");
                throw exception;
            }
        }catch(MyException exception){
            listOfConnectionExceptions.add(exception);
            newConnection.setExceptions(listOfConnectionExceptions);
            return newConnection;
        }

        newConnection.setAuthor(author);
        newConnection.setTarget(target);
        connectionRepository.save(newConnection);
        logger.info("connection with user "+ friendEmail + " added successfully");
        return newConnection;
    }

    //cette méthode fournit la liste déroulante d'amis
        public List<User> getFriendsUsersOfConnectedUser () {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication ok, called auth - here auth is email");
            String email = auth.getName();

            List<User> friends = new ArrayList();
            System.out.println("friends about to be retrived from auth");
            Iterable<Integer> listOfIds = getFriendsIdsForOneUserByEmail(email);
            for (Integer id : listOfIds) {
                Optional<User> optUser = userRepository.findById(id);
                User user = optUser.get();
                friends.add(user);
            }

            return friends ;
        }
        //récupérer l'id de l'utilisateur connecté et injecter en param ICI
        //récupérer utilisateur connecté couramment
        public Iterable<String> getNamesOfFriendsForPrincipalUSer () {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            List<String> listOfFriendsNames = new ArrayList();
//>>
            Iterable<Integer> listOfIds = getFriendsIdsForOneUserByEmail(email);
            for (Integer id : listOfIds) {
                Optional<User> optUser = userRepository.findById(id);
                User user = optUser.get();
                listOfFriendsNames.add(user.getFirstName() + " " + user.getLastName());
            }
            return listOfFriendsNames;
        }


}



