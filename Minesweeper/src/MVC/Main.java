package MVC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/NewGame.fxml"));
        primaryStage.setTitle("Сапер");
        primaryStage.setScene(new Scene(root, 560, 500));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/image/icon.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
