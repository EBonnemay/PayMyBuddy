package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.MyException;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {


    private ConnectionRepository connectionRepository;

    private UserRepository userRepository;

    private UserService userService;

    static final Logger logger = LogManager.getLogger();

    public ConnectionService(UserService userService, UserRepository userRepository, ConnectionRepository connectionRepository){
        this.userService = userService;
        this.userRepository= userRepository;
        this.connectionRepository=connectionRepository;
    }



    public Iterable<Integer> getFriendsIdsForOneUserById(int id) {
        return connectionRepository.findFriendsIdsForOneUser(id);
    }

    public Iterable<Integer> getFriendsIdsForOneUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        int id = user.getId();
        return getFriendsIdsForOneUserById(id);
    }

    @Transactional
    public void deleteConnection(String emailOfUserToRemoveFromConnection){
        //try{
        String email = userService.getCurrentUsersMailAddress();

            User connectedUser = userRepository.findByEmail(email);
            User userToDeleteFromConnection = userRepository.findByEmail(emailOfUserToRemoveFromConnection);

            connectionRepository.deleteRelationBetweenThoseUsers(connectedUser.getId(), userToDeleteFromConnection.getId());
            logger.info("connection with user "+ emailOfUserToRemoveFromConnection + " deleted successfully");

    }
    public Connection saveNewConnectionForCurrentUserWithEmailParameter(String friendEmail) {
        Connection newConnection = new Connection();
        List<MyException> listOfConnectionExceptions = new ArrayList<>();

        String email = userService.getCurrentUsersMailAddress();
        User author = userRepository.findByEmail(email);
        //si le mail ne se trouve pas dans la base ou input nul
        try {
            if (userRepository.findByEmail(friendEmail)==null||friendEmail.equals(email)) {
                String message = "you must enter a valid email address";
                MyException exception = new MyException(message);
                logger.error("user input error : input cannot be void");
                throw exception;
            }
        }catch(MyException exception){
            listOfConnectionExceptions.add(exception);
            newConnection.setExceptions(listOfConnectionExceptions);
            return newConnection;
        }
        User target = userRepository.findByEmail(friendEmail);
        //si l'utilisateur "target" est deleted
        try {
            if (target.isDeleted()) {
                String message = "this user is deleted";
                MyException exception = new MyException(message);
                logger.error("user input error : this user is deleted");
                throw exception;
            }
        }catch(MyException exception) {
                listOfConnectionExceptions.add(exception);
                newConnection.setExceptions(listOfConnectionExceptions);
                return newConnection;
        }
        int authorId = author.getId();
        int targetId = target.getId();

        Iterable<Integer> iterable = getFriendsIdsForOneUserById(authorId);
        Iterator<Integer> iterator = iterable.iterator();
        List<Integer> friendsIds = new ArrayList<>();
        iterable.forEach(friendsIds::add);
        //si l'ami est déjà dans la liste
        try {
            if (friendsIds.contains(targetId)) {
                String message = "friend already in list";
                MyException exception = new MyException(message);
                logger.error("user input error : friend already in list");
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
        public List<User> getActualOrFormerFriendsUsersOfConnectedUser () {
            String email = userService.getCurrentUsersMailAddress();

            List<User> friends = new ArrayList<>();

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

    public ArrayList<User> getNonDeletedFriendsUsersOfConnectedUser () {
        String email = userService.getCurrentUsersMailAddress();

        ArrayList<User> friends = new ArrayList<>();

        Iterable<Integer> listOfIds = getFriendsIdsForOneUserByEmail(email);
        for (Integer id : listOfIds) {
            Optional<User> optUser = userRepository.findById(id);
            User user = optUser.get();
            if(!user.isDeleted()){
                friends.add(user);
            }

        }


        return friends ;
    }



}



