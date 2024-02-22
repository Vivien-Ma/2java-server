package vi.main;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

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
        // Initialize ObjectOutputStream first to send ACK
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        Map<String, Object> ackData = new HashMap<>();
        ackData.put("type", "ACK");
        ackData.put("message", "Connection established successfully.");
        Message ackMessage = new Message(ackData);
        oos.writeObject(ackMessage);
        oos.flush();

        // Now, initialize ObjectInputStream
        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        System.out.println("Received connection");

        // Rest of your server logic here...
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    }
}
