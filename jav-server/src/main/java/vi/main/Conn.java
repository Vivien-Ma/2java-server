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
            try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
                Object object = ois.readObject();
                if (object instanceof Message) {
                    System.out.println("recieved:");
                    Message receivedMessage = (Message) object;
                    Map<String, Object> data = receivedMessage.getData();
                    if ("test".equals(data.get("action"))) {
                        System.out.println("Received test message: " + data.get("message"));

                        // Prepare and send a response
                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("response", "Test successful");
                        Message responseMessage = new Message(responseData);

                        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
                            oos.writeObject(responseMessage);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
