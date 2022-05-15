package chatgui.Scenes.Chat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chatgui.ChatGui;
import chatgui.SceneData;
import chatgui.Database.ChatAppSQL;
import chatgui.Socket.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

// SSL
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ChatController implements Initializable{

    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private Label user_name;
    @FXML
    private Label invalid_account;

    private Client client;
    private ChatGui chatGui = new ChatGui();
    private ChatAppSQL chatAppSQL = new ChatAppSQL();

    public void checkEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            sendMessageHelper();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String username = SceneData.accountName;
        invalid_account.setTextFill(Color.RED);

        // SSL
        try {
        if(System.getProperty("javax.net.ssl.trustStore") == null || System.getProperty("javax.net.ssl.trustStorePassword") == null) {
			System.setProperty("javax.net.ssl.trustStore", "config/truststore");
			System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		}
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 1234);
            client = new Client(sslSocket, username);
        } catch (IOException e) {
            e.printStackTrace();
        }


        user_name.setText("You Are: " + username);
        client.sendMessage(); // Initialize the client
        client.listenForMessage(vbox_messages);

        // Automatic scrolling for incoming messages
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    sp_main.setVvalue((Double) newValue);
            }
        });

        // Send message when the client clicks send
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    sendMessageHelper();
            }
        });
    }

    private void sendMessageHelper() {
        invalid_account.setText("");
        String messageToSend = tf_message.getText();
        if(!messageToSend.isEmpty()) {
            String accountToDirectMessage = messageIsDirectMessage(messageToSend);
            if(accountToDirectMessage != "") {
                List<String> usernameList = chatAppSQL.connectAndGetAttributes("username", "account");
                if(!usernameList.contains(accountToDirectMessage)) {
                    invalid_account.setText("Account to private message does not exist");
                    invalid_account.setVisible(true);
                    return;
                }
                else {
                    String values = String.format("\"%s\", \"%s\", \"%s\"", SceneData.accountName, accountToDirectMessage, messageToSend);
                    chatAppSQL.connectAndInsertSpecificAttributes("messages", "sender, receiver, message", values);
                }
            }
            else {
                    String values = String.format("\"%s\", \"%s\"", SceneData.accountName, messageToSend);
                    chatAppSQL.connectAndInsertSpecificAttributes("messages", "sender, message", values);
            }
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");
            
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));
            textFlow.setMaxWidth(300);

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);

            client.sendMessage(messageToSend);
            tf_message.clear();
        }
    }

   private String messageIsDirectMessage(String message) {
        List<String> matchiList = new ArrayList<String>();
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(message);
        while(m.find()) {
            matchiList.add(m.group(1));
        }

        if(matchiList.isEmpty()) {
            return "";
        }
        else {
            String accountToDirectMessage = matchiList.get(0);
            return accountToDirectMessage;
        }
    }

    // Add message that we receive
    public static void addLabel(String incomingMessage, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(incomingMessage);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                        "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.setMaxWidth(300);
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public void logout() {
        try {
            client.kill();
            String scence = "Scenes/Login/Login.fxml";
            chatGui.changeScene(scence);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}