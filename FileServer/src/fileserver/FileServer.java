package fileserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                        master.addReplica(listenerClient.accept());
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
    private final HashSet<Replica> clients = new HashSet<>();

    public Master() {

    }

    public void addReplica(Socket socket) {
        Replica new_replica = new Replica(socket, cntReplica + 100);
        new_replica.start();
        replicas.add(new_replica);
        cntReplica++;
    }

    public void addClient(Socket socket) {
        Replica new_replica = new Replica(socket, cntReplica + 100);
        new_replica.start();
        clients.add(new_replica);
        cntClient++;
    }

    class Replica extends Thread {

        public String id;
        Socket socket;
        public BufferedReader input;
        public PrintWriter output;

        public Replica(Socket socket, int id) {
            this.socket = socket;
            try {
                this.input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                this.output = new PrintWriter(socket.getOutputStream(), true);
                this.id = Integer.toString(id);
                this.output.println(this.id);
            } catch (IOException e) {
                System.out.println("Replica died: " + e);
            }
            System.out.println("New Replica: " + id);
        }

        /**
         * The run method of this thread.
         */
        @Override
        public void run() {
            String response;
            try {
                while (true) {
                    response = input.readLine();
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
        Socket socket;
        public BufferedReader input;
        public PrintWriter output;

        public Client(Socket socket, int id) {
            this.socket = socket;
            try {
                this.input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                this.output = new PrintWriter(socket.getOutputStream(), true);
                this.id = Integer.toString(id);
                this.output.println(this.id);
            } catch (IOException e) {
                System.out.println("Client died: " + e);
            }
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
                    response = input.readLine();
                    if (response == null) {
                        return;
                    } else {
                        System.out.println(
                                "Client " + this.id + ": " + response
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
}
