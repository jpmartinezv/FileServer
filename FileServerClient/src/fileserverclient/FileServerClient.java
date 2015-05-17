/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserverclient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jpmartinezv
 */
public class FileServerClient {

    private static final int PORT = 9080;
    private static StreamSocket socket;
    private final String id;

    private static String filesPath = System.getProperty("user.dir") + "/files/";

    public FileServerClient(String serverAddress) throws Exception {
        socket = new StreamSocket(
                InetAddress.getByName(serverAddress), PORT);
        this.id = socket.receiveMessage();
        System.out.println("Nuevo cliente: " + this.id);
    }

    public void run() throws Exception {
        Scanner e = new Scanner(System.in);
        String command;
        while (true) {
            command = e.nextLine();
            if (command.startsWith("sube")) {
                File file = new File(filesPath + command.substring(5));
                socket.sendMessage("sube " + command.substring(5));
                socket.sendFile(file);
                System.out.println(socket.receiveMessage());
            } else if (command.startsWith("baja")) {
                socket.sendMessage("baja " + command.substring(5));
                File file = new File(filesPath + command.substring(5));
                if (socket.receiveMessage().equals("success")) {
                    socket.receiveFile(file);
                }
                System.out.println(socket.receiveMessage());
            } else {
                System.out.println("Comando incorrecto");
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        FileServerClient client = new FileServerClient(serverAddress);
        client.run();
    }
}
