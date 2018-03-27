package hangman;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;

import javax.swing.*;

public class GameController {

    private final ExecutorService executorService;
    private final Game game;

    public GameController(Game game) {
        this.game = game;
        executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

    @FXML
    private VBox board ;
    @FXML
    private Label statusLabel ;
    @FXML
    private Label userInput ;
    @FXML
    private GridPane buttons;
    @FXML
    private Button startButton;
    @FXML
    private TextField buttonText;
    @FXML
    private Label guessWordLabel;


    public void initialize() throws IOException {
        System.out.println("in initialize");
        loadFonts();
        drawHangman();
        setUpStatusLabelBindings();
        setUpGuessWordLabelBindings();
        initializeButtons();
    }

    private void loadFonts() {
        String baseFontPath = "file:resources/fonts/JosefinSans-";
        int baseFontSize = 10;
        Font.loadFont(baseFontPath + "Bold.ttf", baseFontSize);
        Font.loadFont(baseFontPath + "Light.ttf", baseFontSize);
        Font.loadFont(baseFontPath + "Regular.ttf", baseFontSize);
        Font.loadFont(baseFontPath + "SemiBold.ttf", baseFontSize);
        Font.loadFont(baseFontPath + "Thin.ttf", baseFontSize);
    }

    private void initializeButtons() {
        int i = 0;
        int j = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            String letter = String.valueOf(c);
            if (i == 9) {
                i = 0;
                j++;
            }
            Button btn = new LetterButton(letter);
            btn.setOnAction(event -> {
                game.makeMove(letter.toLowerCase());
                btn.setDisable(true);
            });
            buttons.add(btn, i++, j);
        }
    }


//    private void addButtonListener() {
//        startButton.setOnAction(new EventHandler<ActionEvent>(){
//            @Override
//            public void handle(ActionEvent event){
//                if()
//            }
//        });
//    }

//    private class ButtonHandler implements EventHandler<ActionEvent> {
//
//        @Override
//        public void handle(ActionEvent event) {
//            if(event.getSource().equals(startButton)) {
//                Game.GameStatus.OPEN;
//            }
//        }
//    }

    private void setUpGuessWordLabelBindings() {
        System.out.println("in setUpGuessWordLabelBindings");
        guessWordLabel.textProperty().bind(Bindings.format("%s", game.getTmpAnswerShown()));
    }

    private void setUpStatusLabelBindings() {
        System.out.println("in setUpStatusLabelBindings");
        statusLabel.setFont(Font.font("Josefin Sans", FontWeight.BOLD, 70));
        statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));

		/*	Bindings.when(
					game.currentPlayerProperty().isNotNull()
			).then(
				Bindings.format("To play: %s", game.currentPlayerProperty())
			).otherwise(
				""
			)
		);
		*/
    }

    private void drawHangman() {
        ImageView imv = new ImageView();
        Image image = new Image("file:resources/images/bigHead.jpg");
        imv.setImage(image);
        board.getChildren().add(imv);
    }

    @FXML
    private void newHangman() {
        game.reset();
    }

    @FXML
    private void quit() {
        board.getScene().getWindow().hide();
    }


//    The "How to Play" and "Credits" dialogs.
    @FXML
    private void howToPlay() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("miscDialogs.css").toExternalForm());
        dialogPane.getStyleClass().add("miscDialogs");

        alert.setTitle("How to play Hangman");
        alert.setHeaderText(null);
        alert.setContentText("Guess the letters of the secret word by typing in a letter in the text box. " +
                "\n\n - Each correct guess reveals where your letter is in the word." +
                "\n\n - Each wrong guess leads you closer to THE SHADOW REALM.");
        alert.showAndWait();
    }

    @FXML
    private void credits() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("miscDialogs.css").toExternalForm());
        dialogPane.getStyleClass().add("miscDialogs");

        alert.setTitle("Credits");
        alert.setHeaderText("This project is brought to you by: ");
        alert.setContentText("Amir Yamini" +
                "\nApril Lima" +
                "\nHamoun Mojib" +
                "\nSamir Matin");
        alert.showAndWait();
    }

}