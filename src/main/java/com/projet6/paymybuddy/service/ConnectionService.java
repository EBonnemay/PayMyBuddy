package com.projet6.paymybuddy.service;

import com.projet6.paymybuddy.model.Connection;
import com.projet6.paymybuddy.model.User;
import com.projet6.paymybuddy.repository.ConnectionRepository;
import com.projet6.paymybuddy.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    public void deleteConnection(String emailOfUserToRemoveFromConnection){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication ok, called auth - here auth is email");
        String email = auth.getName();

        User connectedUser = userRepository.findByEmail(email);
        //charger le userConnected par son email
        //passer en revue les amis du user
        //si l'un d'entre eux est userToRemove
        Iterable<Integer> ConnectionIdsForOneUserAsAuthorOrTarget =  connectionRepository.findConnectionIdsForOneUserAsAuthorOrTarget(connectedUser.getId());
        //pour chaque objet connection de la personne connectée
        for (Integer connectionId : ConnectionIdsForOneUserAsAuthorOrTarget ){
            Optional<Connection> opt = connectionRepository.findById(connectionId);
            Connection connection = opt.get();
            //si author ou targer .equals userToRemove
            if(connection.getAuthor().getEmail().equals(emailOfUserToRemoveFromConnection)|| connection.getTarget().getEmail().equals(emailOfUserToRemoveFromConnection)){
                connectionRepository.delete(connection);
                System.out.println(email);
                System.out.println(emailOfUserToRemoveFromConnection);//edaugherty0@ebay.com
                User userToDeleteFromConnection = userRepository.findByEmail(emailOfUserToRemoveFromConnection);
                System.out.println(connectionRepository.findConnectionIdsForOneUserAsAuthorOrTarget(connectedUser.getId()));
//expected 15, (16), 11
                System.out.println(connectionRepository.findConnectionIdsForOneUserAsAuthorOrTarget(userToDeleteFromConnection.getId()));

                //expected 14, 12, (16)
            }


        }
        Iterable<Connection> conn = connectionRepository.findAll();
        connectionRepository.saveAll(conn);

    }
    public void saveConnectionForCurrentUserWithEmailParameter(String friendEmail) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication ok, called auth - here auth is email");
        String email = auth.getName();

        User author = userRepository.findByEmail(email);
        User target = userRepository.findByEmail(friendEmail);

        int authorId = author.getId();
        int targetId = target.getId();

        Iterable<Integer> iterable = getFriendsIdsForOneUserById(authorId);
        Iterator<Integer> iterator = iterable.iterator();
        List<Integer> friendsIds = new ArrayList<Integer>();
        iterable.forEach(friendsIds::add);

        if(!friendsIds.contains(targetId)) {
            Connection newConnection = new Connection();
            newConnection.setAuthor(author);
            newConnection.setTarget(target);
            connectionRepository.save(newConnection);
        }
        else{
            System.out.println("vous avez déjà cet ami");
        }

    }
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



