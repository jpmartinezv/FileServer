package fileserverreplica;

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
public class FileServerReplica {

    private static final int PORT = 9090;
    private final Socket socket;
    private static BufferedReader input;
    private static PrintWriter output;
    private final String id;

    public FileServerReplica(String serverAddress) throws Exception {
        this.socket = new Socket(serverAddress, PORT);
        input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        this.id = input.readLine();
        System.out.println("Nueva replica: " + this.id);
    }

    public void run() throws Exception {
        new Thread() {
            @Override
            public void run() {
                String response;
                try {
                    while (true) {
                        output.println("la la la");
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileServerReplica.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileServerReplica.class.getName()).log(Level.SEVERE, null, ex);
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
        FileServerReplica replica = new FileServerReplica(serverAddress);
        replica.run();
    }
}
