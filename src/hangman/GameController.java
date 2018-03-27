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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;

import javax.swing.*;

public class GameController {

    private final ExecutorService executorService;
    static Game game;
    private ImageView[] images = new ImageView[5];

    public GameController(Game game) {
        this.game = game;
        executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

    @FXML
    private HBox board;
    @FXML
    private Label statusLabel;
    @FXML
    private Label userInput;
    @FXML
    private GridPane buttons;
    @FXML
    private Button startButton;
    @FXML
    private TextField buttonText;
    @FXML
    private Label guessWordLabel;


    public void initialize() throws IOException {
        statusLabel.setText(Game.GameStatus.OPEN.toString());
        System.out.println("in initialize");
        setUpImagesOnBoard();
        loadFonts();
        setUpStatusLabelBindings();
        setUpGuessWordLabelBindings();
        initializeButtons();

    }

    private void setUpImagesOnBoard() {

        images[0] = new ImageView();
        setImageViews("file:resources/images/bigRightLeg.jpg", 0);

        images[1] = new ImageView();
        setImageViews("file:resources/images/bigRightArm.jpg", 1);

        images[2] = new ImageView();
        setImageViews("file:resources/images/bigHead.jpg", 2);

        images[3] = new ImageView();
        setImageViews("file:resources/images/bigLeftArm.jpg", 3);

        images[4] = new ImageView();
        setImageViews("file:resources/images/bigLeftLeg.jpg", 4);

        // TODO DELETE
        for(int i = 1; i< 6;i++){
            drawHangman(i);
        }

    }

    private void setImageViews(String url, int index) {
        Image image = new Image(url, 100, 250, true, true);
        ImageView imv = images[index];
        imv.setImage(image);
        board.getChildren().add(imv);
        imv.setVisible(false);
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

    void drawHangman(int badMoves) {

        /*drawHangman toggles visibility of images to true
            when player makes a bad guess.

            With each increment in the number of bad moves,
            another image has their visibility set to true, showing on the board.
         */

        Node node = null;

        switch (badMoves) {
            case 1:
                node = board.getChildren().get(badMoves - 1);
                node.setVisible(true);
                break;

            case 2:
                node = board.getChildren().get(badMoves - 1);
                node.setVisible(true);
                break;

            case 3:
                node = board.getChildren().get(badMoves - 1);
                node.setVisible(true);
                break;

            case 4:
                node = board.getChildren().get(badMoves - 1);
                node.setVisible(true);
                break;

            case 5:
                node = board.getChildren().get(badMoves - 1);
                node.setVisible(true);
                break;
        }

    }

    @FXML
    private void newHangman() {
        System.out.println("reset");

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