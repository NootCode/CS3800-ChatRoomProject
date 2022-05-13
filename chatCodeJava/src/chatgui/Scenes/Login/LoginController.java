package chatgui.Scenes.Login;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField cardNumber;
 
    @FXML
    private PasswordField pin;

    @FXML
    private Label wrongLogin;

    @FXML
    private TextField atmField;
}
