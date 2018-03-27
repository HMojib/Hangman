package hangman;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Hangman extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {
        Game game = new Game();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Hangman.fxml"));
        loader.setController(new GameController(game));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 900);
        scene.getStylesheets().add(getClass().getResource("Hangman.css").toExternalForm());

        primaryStage.getIcons().add(new Image("file:resources/images/peach.png"));
        primaryStage.setTitle("Hangman");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
