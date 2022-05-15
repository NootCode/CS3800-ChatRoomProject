package chatgui.Scenes.ViewMessages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import chatgui.ChatGui;
import chatgui.SceneData;
import chatgui.Database.ChatAppSQL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ViewMessagesController implements Initializable {
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;

    private ChatAppSQL chatAppSQL = new ChatAppSQL();
    private ChatGui chatGui = new ChatGui();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMessage();
    }

    public void returnToLogin() {
        try {
            chatGui.changeScene("Scenes/Login/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMessage() {
        String query = String.format("select message from messages where sender = \"%s\" limit 50", SceneData.accountName);
        List<String> messages = chatAppSQL.connectAndExecuteQuery(query);
        for(String message : messages) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");
            
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));
            textFlow.setMaxWidth(300);

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);
        }
    }
}
