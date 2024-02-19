package vi.main;
import java.io.*;
import java.net.*;

public class Conn {
    private ServerSocket connection;
    private boolean isRunning = false;

    public Conn(){
        try{
            // créé un socket
            connection = new ServerSocket(3000);
            isRunning = true;
            System.out.println("Server is running. listening on port 3000 ...");;
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void acceptconn(){
        if (isRunning){
            while (true) {
                try {
                    Socket clientSocket = connection.accept();
                    System.out.println("Client connected" + clientSocket);

                    //logique pour chaque client dans un thread pour chaque client
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeConnection() {
        try {
            connection.close();
            isRunning = false;
            System.out.println("Server connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("recieved from " + clientSocket.getInetAddress() + message);

                    //logique de reponse pour chaque thread client ici !!
                    String reponse = "server recieved:" + message;

                    //close the client conn
                    clientSocket.close();
                    System.out.println("client disconnected: " + clientSocket.getInetAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
