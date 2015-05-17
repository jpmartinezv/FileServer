package fileserverreplica;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jpmartinezv
 */
public class FileServerReplica {

    private static final int PORT = 9090;
    private static StreamSocket socket;
    private final String id;

    public FileServerReplica(String serverAddress) throws Exception {
        socket = new StreamSocket(
                InetAddress.getByName(serverAddress), PORT);
        this.id = socket.receiveMessage();
        System.out.println("Nueva replica: " + this.id);
    }

    public void run() throws Exception {
        new Thread() {
            @Override
            public void run() {
                String response;
                try {
                    while (true) {
                        socket.sendMessage("la la la");
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException | IOException ex) {
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
