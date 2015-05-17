package fileserverreplica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author jpmartinezv
 */
public class FileServerReplica {

    private static final int PORT = 9090;
    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private String id;

    public FileServerReplica(String serverAddress) throws Exception {
        this.socket = new Socket(serverAddress, PORT);
        this.input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.id = input.readLine();
        System.out.println("Nueva replica: " + this.id);
    }

    public void run() throws Exception {
        String response;
        try {
            while (true) {
                output.println("la la la");
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
        FileServerReplica replica = new FileServerReplica(serverAddress);
        replica.run();
    }
}
