package net.codejava.networking.chat.server;

import java.io.*;
import java.net.*;

/*
UserThread კლასი პასუხისმგებელია კლიენტისგან გაგზავნილი შეტყობინებების წაკითხვაზე
 და სხვა კლიენტისთვის შეტყობინებების გადაცემაზე. პირველ რიგში, ის ახალ მომხმარებელს უგზავნის ონლაინ მომხმარებელთა სიას,
  შემდეგ კითხულობს მომხმარებლის სახელს და აცნობებს სხვა მომხმარებლებს ახალი მომხმარებლის შესახებ.
 */
public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /*
     * ვაგზავნით ონლაინ მომხმარებლების სიას ახალი იუზერისთვის
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /*
     * ვაგზავნით მესიჯს კლიენტისთვის
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}