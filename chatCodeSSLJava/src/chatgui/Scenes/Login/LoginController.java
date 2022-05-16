package chatgui.Scenes.Login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.List;

import chatgui.ChatGui;
import chatgui.Database.ChatAppSQL;
import chatgui.SceneData;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Label wrongLogin;
    @FXML
    private Button createAccountButton;

    private ChatAppSQL chatAppSQL = new ChatAppSQL();
    private ChatGui chatGui = new ChatGui();

    public void checkEnter(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            checkLogin();
        }
    }

    public void checkLogin() {
        try {
            String usernameInput = username.getText();
            String passwordInput = password.getText();

            List<String> usernameList = chatAppSQL.connectAndGetAttributes("username", "account");
            List<String> passwordList = chatAppSQL.connectAndGetAttributes("password", "account");

            if(usernameList.contains(usernameInput) && passwordList.contains(passwordInput)) {
                SceneData.accountName = usernameInput;
                chatGui.changeScene("Scenes/Chat/Chat.fxml");
            } else if(usernameInput.isEmpty() || passwordInput.isEmpty()) {
                wrongLogin.setText("Please enter your data");
            } else {
                wrongLogin.setText("Wrong username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccount() {
        try {
            chatGui.changeScene("Scenes/CreateAccount/CreateAccount.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewMessages() {
        try {
            String usernameInput = username.getText();
            String passwordInput = password.getText();

            List<String> usernameList = chatAppSQL.connectAndGetAttributes("username", "account");
            List<String> passwordList = chatAppSQL.connectAndGetAttributes("password", "account");

            if(usernameList.contains(usernameInput) && passwordList.contains(passwordInput)) {
                SceneData.accountName = usernameInput;
                chatGui.changeScene("Scenes/ViewMessages/ViewMessages.fxml");
            } else if(usernameInput.isEmpty() || passwordInput.isEmpty()) {
                wrongLogin.setText("Please enter your data");
            } else {
                wrongLogin.setText("Wrong username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
