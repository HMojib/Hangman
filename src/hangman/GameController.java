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
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Label enterALetterLabel ;
    @FXML
    private Label userInput ;
    @FXML
    private TextField textField ;
    @FXML
    private GridPane buttons;

    public void initialize() throws IOException {
        System.out.println("in initialize");
        Font.loadFont(Hangman.class.getResource("JosefinSans-Light.ttf").toExternalForm(), 10);
        drawHangman();
        addTextBoxListener();
        setUpStatusLabelBindings();
        initializeButtons();
    }

    private void initializeButtons() {
//        buttons.add(new Button("test"), 0, 0);
        int i = 0;
        int j = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            String letter = String.valueOf(c);
            if (i == 9) {
                i = 0;
                j++;
            }
            buttons.add(new LetterButton(letter), i++, j);
        }
    }

    private void addTextBoxListener() {
        textField.setPrefColumnCount(2);
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if(newValue.length() > 0) {
                    textField.setText(newValue);
                    System.out.print(newValue);
                    game.makeMove(newValue);
                    textField.clear();
                }
            }
        });
    }

    private void setUpStatusLabelBindings() {

        System.out.println("in setUpStatusLabelBindings");
        statusLabel.setFont(Font.font("Josefin Sans", FontWeight.BOLD, 70));
        statusLabel.textProperty().bind(Bindings.format("%s", game.gameStatusProperty()));
        enterALetterLabel.textProperty().bind(Bindings.format("%s", "Enter a letter:"));

        //      Used letters here
        userInput.textProperty().bind(Bindings.format("%s", "Letters used: "));

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

        Line line = new Line();
        line.setStartX(25.0f);
        line.setStartY(0.0f);
        line.setEndX(25.0f);
        line.setEndY(25.0f);

        Circle c = new Circle();
        c.setRadius(10);

        board.getChildren().add(line);
        board.getChildren().add(c);

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