package net.codejava.networking.chat.client;

import java.io.*;
import java.net.*;

/*
 * thread-ი მომხმარებლის მიერ შეყვანილი ინფორმაციის წასაკითხად და სერვერისთვის გასაგზავნად
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        Console console = System.console();

        try {
            String userName = console.readLine("\nEnter your name: ");

            client.setUserName(userName);
            writer.println(userName);

            String text;

            do {
                text = console.readLine("[" + userName + "]: ");
                writer.println(text);

            } while (!text.equals("Dasruleba"));

            try {
                socket.close();
            } catch (IOException ex) {

                System.out.println("Error writing to server: " + ex.getMessage());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
}