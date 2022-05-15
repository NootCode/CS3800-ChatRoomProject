package chatgui.Scenes.CreateAccount;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.List;

import chatgui.ChatGui;
import chatgui.Database.ChatAppSQL;

public class CreateAccountController {
    @FXML
    private TextField username;
 
    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label wrongCreate;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button returnToLogin;

    private ChatAppSQL chatAppSQL = new ChatAppSQL();
    private ChatGui chatGui = new ChatGui();

    public void checkEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            checkCreateAccount();
        }
    }

    public void checkCreateAccount() {
        try {
            String usernameInput = username.getText();
            String passwordInput = password.getText();

            List<String> usernameList = chatAppSQL.connectAndGetAttributes("username", "account");

            wrongCreate.setTextFill(Color.RED);
            if(usernameList.contains(usernameInput) && !(passwordInput.isEmpty())) {
                wrongCreate.setText("Username already exists in database");
            } else if(usernameInput.isEmpty() || passwordInput.isEmpty()) {
                wrongCreate.setText("Please enter your data");
            } else {
                chatAppSQL.connectAndInsertSpecificAttributes("account", "username, password", 
                                                                String.format("\"%s\", \"%s\"", usernameInput, passwordInput));
                wrongCreate.setTextFill(Color.GREEN);
                wrongCreate.setText("Success: added account to database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnToLogin()  {
        try {
            chatGui.changeScene("Scenes/Login/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}