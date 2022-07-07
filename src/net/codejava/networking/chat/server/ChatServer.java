package net.codejava.networking.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;


public class ChatServer {
    private int port ;
    //სეტს ვიყენებთ, იმიტომ რომ ის არ იძლევა მონაცემთა დუბლირების საშუალებას, ხოლო ელემენტების თანმიმდევრობას ჩვენს შემთხვევაში არ აქვს მნიშვნელობა
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /*
     * გადასცემს მესიჯს ერთი იუზერისგან მეორესთან
     */
    void broadcast(String message,UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /*
     * ვინახავთ ახალი დაკავშირებული მომხმარებლის სახელებს
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /*
     * როდესაც კლიენტი გაეთიშა სერვერს,ვშლით დაკავშირებულ სახელს და userThread-ს
     */
    void removeUser(String userName, net.codejava.networking.chat.server.UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /*
     * აბრუნებს თრუს თუ სხვა მომხმარებლები არინ დაკავშირებულები (არ ითვლება ახლანდელი დაქონექთებელი იუზერი)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}