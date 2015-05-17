package fileserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jpmartinezv
 */
public class FileServer {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        final ServerSocket listenerClient = new ServerSocket(9080);
        final ServerSocket listenerReplica = new ServerSocket(9090);

        System.out.println("War Server is Running");

        final Master master = new Master();

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        master.addReplica(listenerReplica.accept());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FileServer.class.getName()).
                            log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        listenerReplica.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileServer.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        master.addClient(listenerClient.accept());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FileServer.class.getName()).
                            log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        listenerReplica.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileServer.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();

    }
}

class Master {

    private int cntReplica = 0;
    private final HashSet<Replica> replicas = new HashSet<>();

    private int cntClient = 0;
    private final HashSet<Client> clients = new HashSet<>();

    String filesPath = System.getProperty("user.dir") + "/upload/";

    public Master() {

    }

    public void addReplica(Socket socket) throws IOException {
        Replica new_replica = new Replica(socket, cntReplica + 100);
        new_replica.start();
        replicas.add(new_replica);
        cntReplica++;
    }

    public void addClient(Socket socket) throws IOException {
        Client new_client = new Client(socket, cntReplica + 100);
        new_client.start();
        clients.add(new_client);
        cntClient++;
    }

    class Replica extends Thread {

        public String id;
        public StreamSocket socket;

        public Replica(Socket socket, int id) throws IOException {
            this.socket = new StreamSocket(socket);
            this.id = Integer.toString(id);
            this.socket.sendMessage(Integer.toString(id));
            System.out.println("New client: " + id);
        }

        /**
         * The run method of this thread.
         */
        @Override
        public void run() {
            String response;
            try {
                while (true) {
                    response = socket.receiveMessage();
                    if (response == null) {
                        return;
                    } else {
                        System.out.println(
                                "Replica " + this.id + ": " + response
                        );
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    class Client extends Thread {

        public String id;
        public StreamSocket socket;

        public Client(Socket socket, int id) throws IOException {
            this.socket = new StreamSocket(socket);
            this.id = Integer.toString(id);
            this.socket.sendMessage(Integer.toString(id));
            System.out.println("New client: " + id);
        }

        /**
         * The run method of this thread.
         */
        @Override
        public void run() {
            String response;
            try {
                while (true) {
                    response = socket.receiveMessage();
                    if (response == null) {
                        return;
                    } else if (response.startsWith("sube")) {
                        System.out.println("Nuevo archivo: " + response.substring(5));

                        File outFile = new File(filesPath + response.substring(5));

                        socket.receiveFile(outFile);
                        socket.sendMessage("Archivo subido");
                    } else if (response.startsWith("baja")) {
                        File file = new File(filesPath + response.substring(5));
                        if (file.exists() && !file.isDirectory()) {
                            socket.sendMessage("success");
                            socket.sendFile(file);
                            socket.sendMessage("Archivo descargado");
                        } else {
                            socket.sendMessage("error");
                            socket.sendMessage("Archivo no encontrado");
                        }

                    }
                }
            } catch (IOException ex) {
                System.out.println(ex);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
