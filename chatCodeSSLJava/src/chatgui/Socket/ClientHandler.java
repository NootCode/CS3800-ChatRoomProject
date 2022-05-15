package chatgui.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// SSL
import javax.net.ssl.SSLSocket;

public class ClientHandler implements Runnable {

   public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); 
   private SSLSocket sslSocket;
   private BufferedReader bufferedReader;
   private BufferedWriter bufferedWriter;
   private String clientUserName;

   public ClientHandler(SSLSocket sslSocket) {
       try {
           this.sslSocket = sslSocket;
           this.bufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
           this.clientUserName = bufferedReader.readLine();
           clientHandlers.add(this);
           broadcastMessage("SERVER: " + clientUserName + " has entered the chat!");
       } catch (IOException e) {
           e.printStackTrace();
           closeEverything(sslSocket, bufferedReader, bufferedWriter);
       }
   }

   @Override
   public void run() {
       String messageFromClient;
       while (sslSocket.isConnected()) {
           try {
               messageFromClient = bufferedReader.readLine();
               if(messageFromClient == null) throw new IOException();
               broadcastMessage(messageFromClient);
           } catch (IOException e) {
               closeEverything(sslSocket, bufferedReader, bufferedWriter);
               break;
           }
       }
   }

   public void broadcastMessage(String messageToSend) {
        String accountToDirectMessage = messageIsDirectMessage(messageToSend);
        if(accountToDirectMessage != "") {
            messageToSend = messageToSend.replace(String.format("(%s)", accountToDirectMessage), "");
            messageToSend = "(private) " + messageToSend;
            for(ClientHandler clientHandler : clientHandlers) {
                try {
                    if (clientHandler.clientUserName.equals(accountToDirectMessage)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                        closeEverything(sslSocket, bufferedReader, bufferedWriter);
                }
            }
        }
        else {
            for(ClientHandler clientHandler : clientHandlers) {
                try {
                    if (!clientHandler.clientUserName.equals(clientUserName)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                        closeEverything(sslSocket, bufferedReader, bufferedWriter);
                }
            }
        }
   }

   private String messageIsDirectMessage(String message) {
       List<String> matchList = new ArrayList<String>();
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(message);
        while(m.find()) {
            matchList.add(m.group(1));
        }

        if(matchList.isEmpty()) {
            return "";
        }
        else {
            String accountToDirectMessage = matchList.get(0);
            return accountToDirectMessage;
        }
    }

   public void removeClientHandler() {
       broadcastMessage("SERVER: " + clientUserName + " has left the chat!");
       System.out.println("A client has left");
       clientHandlers.remove(this);
   }

   public void closeEverything(SSLSocket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
       removeClientHandler();
       try {
           if(bufferedReader != null) {
               bufferedReader.close();
           }
           if(bufferedWriter != null) {
               bufferedWriter.close();
           }
           if(socket != null) {
               socket.close();
           }
       } catch(IOException e) {
           e.printStackTrace();
       }
   }
}