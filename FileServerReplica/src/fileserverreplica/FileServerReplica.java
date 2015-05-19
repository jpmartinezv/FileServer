package fileserverreplica;

import java.io.File;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.InetAddress;

/**
 *
 * @author jpmartinezv
 */
public class FileServerReplica {

    private static final int PORT = 9090;
    private static StreamSocket socket;
    private final String id;
    private static String filesPath = System.getProperty("user.dir") + "/files/";

    public FileServerReplica(String serverAddress) throws Exception {
        socket = new StreamSocket(
                InetAddress.getByName(serverAddress), PORT);
        this.id = socket.receiveMessage();
        filesPath = filesPath + "replica" + this.id + "/";
        System.out.println("Nueva replica: " + this.id);
        File file = new File(filesPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void run() throws Exception {
        String command;
        try {
            while (true) {
                command = socket.receiveMessage();
                if (command == null) {
                    return;
                } else if (command.startsWith("sube")) {
                    System.out.println("Nuevo archivo: " + command.substring(5));
                    File outFile = new File(filesPath + command.substring(5));
                    socket.receiveFile(outFile);
                } else if (command.startsWith("baja")) {
                    File file = new File(filesPath + command.substring(5));
                    if (file.exists() && !file.isDirectory()) {
                        System.out.println("Descarga: " + command.substring(5));
                        socket.sendMessage("success");
                        socket.sendFile(file);
                    } else {
                        socket.sendMessage("error");
                    }
                } else if (command.startsWith("live")) {
                    socket.sendBit();
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            exit(0);
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
