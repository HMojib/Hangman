package hangman;

import javafx.scene.control.Button;

public class LetterButton extends Button {
    LetterButton(String text) {
        super(text);
        setMinWidth(40);
        setStyle("-fx-font-size: 20");
    }

}
