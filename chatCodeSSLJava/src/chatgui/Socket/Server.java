package chatgui.Socket;

import java.io.IOException;

// SSL
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server {
    
    private SSLServerSocket sslServerSocket;

    public Server(SSLServerSocket sslServerSocket) {
        this.sslServerSocket = sslServerSocket;
    }

    public void startServer() {
        try {
            while(!sslServerSocket.isClosed()) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("A new client has connected");
                ClientHandler ClientHandler = new ClientHandler(sslSocket);

                Thread thread = new Thread(ClientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if(sslServerSocket != null) {
                sslServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        if (System.getProperty("javax.net.ssl.keyStore") == null || System.getProperty("javax.net.ssl.keyStorePassword") == null) {
            System.setProperty("javax.net.ssl.keyStore", "config/keystore");
            System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        }

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(1234);
        Server server = new Server(sslServerSocket);
        server.startServer();
    }
}
