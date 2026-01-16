import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer{
    static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Chat server started on port 5000");

            while (true) {
                Socket socket = server.accept();
                System.out.println("New client connected: " );

                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void broadcast(String msg) {
        for (ClientHandler c : clients) {
                c.sendMessage(msg);
        }
    }

    static class ClientHandler extends Thread{
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String username;

        ClientHandler(Socket socket) {
            this.socket = socket;
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public void run(){
            try{
                out.println("Enter your username: ");
                username = in.readLine();
                broadcast("[Server]" + username + " has joined the chat.");

                String msg;
                while ((msg= in.readLine()) != null) {
                    String time = new SimpleDateFormat("HH:mm").format(new Date());
                    broadcast("[" + time + "]" + username + ":" + msg);
                }

            } catch (Exception e){
                System.out.println(username + "disconnected");
            } finally {
                try{
                    clients.remove(this);
                    broadcast("[Server]" + username + " has left the chat.");
                    socket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        void sendMessage(String msg) {
            out.println(msg);
        }
    }
}