/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserverclient;

import java.io.File;
import java.net.InetAddress;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author jpmartinezv
 */
public class FileServerClient {

    private static final int PORT = 9080;
    private static StreamSocket socket;
    private final String id;

    private static String filesPath = System.getProperty("user.dir") + "/files/";

    public FileServerClient(String serverAddress, String username) throws Exception {
        socket = new StreamSocket(
                InetAddress.getByName(serverAddress), PORT);
        socket.sendMessage(username);
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
                if (socket.receiveMessage().startsWith("success")) {
                    socket.sendFile(file);
                }
                System.out.println(socket.receiveMessage() + "\n");
            } else if (command.startsWith("baja")) {
                File file = new File(filesPath + command.substring(5));
                socket.sendMessage("baja " + command.substring(5));
                if (socket.receiveMessage().startsWith("success")) {
                    socket.receiveFile(file);
                }
                System.out.println(socket.receiveMessage() + "\n");
            } else {
                System.out.println("Comando incorrecto\n");
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        String username = JOptionPane.showInputDialog(null, null, "username");
        FileServerClient client = new FileServerClient(serverAddress, username);
        client.run();
    }
}
