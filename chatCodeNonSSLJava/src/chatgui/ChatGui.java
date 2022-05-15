package chatgui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatGui extends Application {
    private static Stage stg;

    @Override
    public void start(Stage stage) throws Exception {
        stg = stage;
        stage.setResizable(false);
        stage.setTitle("Client App");
        Parent root = FXMLLoader.load(getClass().getResource("Scenes/Chat/Chat.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

      public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.sizeToScene();
      }

    public static void main(String[] args) {
        launch(args);
    }
}
