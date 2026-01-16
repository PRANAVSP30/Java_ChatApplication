import java.io.*;
import java.net.*;

public class ChatClient{

    public static void main(String[] args){
        try{
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to chat server....");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Thread readThread = new Thread(() -> {
                try{
                    String msg;
                    while((msg = in.readLine()) != null){
                        System.out.println(msg);
                    }
                } catch (Exception e){
                    System.out.println("Connection closed.");
                }
            });

            readThread.start();

            BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));
            String userInput;

            while ((userInput = keyboard.readLine()) != null){
                out.println(userInput);
            }
        
        } catch (Exception e){
            e.printStackTrace();
            
        }
    }
}