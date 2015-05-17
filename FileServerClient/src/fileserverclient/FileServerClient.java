/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileserverclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author jpmartinezv
 */
public class FileServerClient {

    private static final int PORT = 9080;
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private final String id;

    public FileServerClient(String serverAddress) throws Exception {
        this.socket = new Socket(serverAddress, PORT);
        this.input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.id = input.readLine();
        System.out.println("Nuevo cliente: " + this.id);
    }

    public void run() throws Exception {
        String response;
        try {
            while (true) {
                output.println("cli cli cli");
                Thread.sleep(5000);
            }
        } finally {
            socket.close();
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws Exception {
        String serverAddress = (args.length == 0) ? "localhost" : args[1];
        FileServerClient replica = new FileServerClient(serverAddress);
        replica.run();
    }
}
