package net.codejava.networking.chat.client;

import java.io.*;
import java.net.*;

/*
 *
 * Thread-ი სერვერის ინფუთის წასაკითხად და დასაბეჭდად
 * ეშვება უსასრულო ციკლი სანამ მომხმარებელი არ გამოეთიშება სერვერს
 *
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);

                // ვბეჭდავთ მომხმარებლის სახელს იმის შემდეგ რაც დავბეჭდეთ სერვერის მესიჯი
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}