/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jpmartinezv
 */
public class FileServerClient {

    private static final int PORT = 9080;
    private final Socket socket;
    private static BufferedReader input;
    private static PrintWriter output;
    private final String id;

    public FileServerClient(String serverAddress) throws Exception {
        this.socket = new Socket(serverAddress, PORT);
        input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        this.id = input.readLine();
        System.out.println("Nuevo cliente: " + this.id);
    }

    public void run() throws Exception {
        new Thread() {
            @Override
            public void run() {
                String response;
                try {
                    while (true) {
                        response = input.readLine();
                        System.out.println(response);
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException | IOException ex) {
                    Logger.getLogger(FileServerClient.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileServerClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }.start();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        FileServerClient replica = new FileServerClient(serverAddress);
        replica.run();
        Scanner e = new Scanner(System.in);
        String command;
        while (true) {
            command = e.nextLine();
            if (command.startsWith("sube")) {
                output.println("sube " + command.substring(4));
            } else if (command.startsWith("baja")) {
                output.println("baja " + command.substring(4));
            } else {
                System.out.println("Comando incorrecto");
            }
        }
    }
}
