package chatgui.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import chatgui.Scenes.Chat.ChatController;
import javafx.scene.layout.VBox;

// SSL
import javax.net.ssl.SSLSocket;

public class Client {
    private SSLSocket sslSocket;    
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private boolean exit;

    public Client(SSLSocket sslSocket, String username) {
        try {
            this.sslSocket = sslSocket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            this.exit = false;
        } catch (IOException e) {
            closeEverything(sslSocket, bufferedReader, bufferedWriter);
        }
    }

    // Default Send Message
    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(sslSocket, bufferedReader, bufferedWriter);
        }
    }


    public void sendMessage(String message) {
        try {
            bufferedWriter.write(username + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(sslSocket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(VBox vBox) {
        new Thread(new Runnable() {
           @Override 
           public void run() {
                String messageFromGroupChat;

                while(sslSocket.isConnected() && !exit) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        ChatController.addLabel(messageFromGroupChat, vBox);
                    } catch (IOException e) {
                        closeEverything(sslSocket, bufferedReader, bufferedWriter);
                    }
                }
           }
        }).start();
    }

    public void kill() {
        try {
            exit = true;
            sslSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeEverything(SSLSocket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter  != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}