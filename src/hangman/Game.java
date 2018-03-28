package hangman;

import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private static String answer;
    private String tmpAnswer;
    private String[] letterAndPosArray;
    private int moves;
    int badMoves;
    private boolean correctGuess;
    private boolean onStart = true;
    private final ReadOnlyObjectWrapper<GameStatus> gameStatus;
    private final ReadOnlyObjectWrapper<String> tmpAnswerShown;
    private ObjectProperty<Boolean> gameState = new ReadOnlyObjectWrapper<Boolean>();
    private List<String> dictionary = new ArrayList<String>();


    public enum GameStatus {
        GAME_OVER {
            @Override
            public String toString() {
                return "Game over! The word was " + answer + "!";
            }
        },
        BAD_GUESS {
            @Override
            public String toString() { return "Bad guess..."; }
        },
        GOOD_GUESS {
            @Override
            public String toString() {
                return "Good guess!";
            }
        },
        WON {
            @Override
            public String toString() {
                return "You won!";
            }
        },
        OPEN {
            @Override
            public String toString() {
                return "Game on, let's go!";
            }
        }

    }

    public Game() {
        tmpAnswerShown = new ReadOnlyObjectWrapper<String>(this, "tmpAnswerShown", "");
        gameStatus = new ReadOnlyObjectWrapper<GameStatus>(this, "gameStatus", GameStatus.OPEN);
        badMoves = 0;

        gameStatus.addListener(new ChangeListener<GameStatus>() {
            @Override
            public void changed(ObservableValue<? extends GameStatus> observable,
                                GameStatus oldValue, GameStatus newValue) {
                if (gameStatus.get() != GameStatus.OPEN) {
                    log("in Game: in changed");
                    //currentPlayer.set(null);
                }
            }

        });

        prepDictionary();
        setRandomWord();
        prepTmpAnswer();
        prepLetterAndPosArray();
        moves = 0;

        gameState.setValue(false); // initial state
        createGameStatusBinding();
    }

    void createGameStatusBinding() {
        List<Observable> allObservableThings = new ArrayList<>();
        ObjectBinding<GameStatus> gameStatusBinding = new ObjectBinding<GameStatus>() {
            {
                super.bind(gameState);
            }
            @Override
            public GameStatus computeValue() {
                log("in computeValue");
                GameStatus check = checkForWinner();
                if(check != null ) {
                    return check;
                }

                if(onStart){
                    log("new game");
                    onStart = false;
                    return GameStatus.OPEN;
                }
                else if (correctGuess){
                    log("good guess");
                    return GameStatus.GOOD_GUESS;
                }
                else{
                    moves++;
                    log("bad guess");

//                    TODO: how do i call drawHangman() in GameController.java
                    printHangman(moves);
                    return GameStatus.BAD_GUESS;
                }
            }
        };
        gameStatus.bind(gameStatusBinding);
    }

    void printHangman(int moves) {
//        GameController.drawHangman(moves);
    }

    public ReadOnlyObjectProperty<GameStatus> gameStatusProperty() {
        return gameStatus.getReadOnlyProperty();
    }
    public ReadOnlyObjectProperty<String> getTmpAnswerShown() {
        return tmpAnswerShown;
    }
    public GameStatus getGameStatus() {
        return gameStatus.get();
    }

    private void setRandomWord() {
        prepDictionary();
        int idx = (int) (Math.random() * dictionary.size());
        answer = dictionary.get(idx).trim(); // remove new line character
    }

    private void prepDictionary(){
        try{

            File file = new File("resources/dictionary.txt");
            Scanner in = new Scanner(file);

            while(in.hasNext())
                dictionary.add(in.next() + "");

        } catch (FileNotFoundException ex){
            System.out.println("Dictionary File Not Found!");
        }

        //System.out.println(dictionary.toString());
    }

    private void prepTmpAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < answer.length(); i++) {
            sb.append("_");
        }
        updateTmpAnswer(sb.toString());
    }

    private void prepLetterAndPosArray() {
        letterAndPosArray = new String[answer.length()];
        for(int i = 0; i < answer.length(); i++) {
            letterAndPosArray[i] = answer.substring(i,i+1);
        }
    }

    private ArrayList<Integer> getValidIndices(String input) {
        correctGuess = false;
        ArrayList<Integer> indices = new ArrayList<>();
        for(int i = 0; i < letterAndPosArray.length; i++) {
            if(letterAndPosArray[i].equals(input)) {
                indices.add(i);
                letterAndPosArray[i] = "";
                correctGuess = true;
            }
        }
        return indices;
    }

    private void updateTmpAnswer(String tmp) {
        tmpAnswer = tmp;
        tmpAnswerShown.set(tmp.replace(""," ").trim());
    }

    private void update(String input) {
        ArrayList<Integer> indices = getValidIndices(input);
        if (correctGuess) {
            StringBuilder sb = new StringBuilder(tmpAnswer);
            for (int index : indices) {
                sb.setCharAt(index, input.charAt(0));
            }
            updateTmpAnswer(sb.toString());
        }
    }

    private static void drawHangmanFrame() {}

    public void makeMove(String letter) {
        log("\nin makeMove: " + letter);
        update(letter);
        // this will toggle the state of the game
        gameState.setValue(!gameState.getValue());
    }

    public void reset() {
        badMoves = 0;
        correctGuess = false;
        onStart = true;
        prepDictionary();
        setRandomWord();
        prepTmpAnswer();
        prepLetterAndPosArray();
        moves = 0;
        gameState.setValue(false); // initial state
        createGameStatusBinding();
    }

    private int numOfTries() {
        return 5; // TODO, fix me
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public int getMoves(){
        return moves;
    }

    private GameStatus checkForWinner() {
        log("in checkForWinner");
        if(tmpAnswer.equals(answer) && moves < numOfTries() - 1) {
            log("won");
            return GameStatus.WON;
        }
        else if(moves == numOfTries() - 1) {
            log("game over");
            return GameStatus.GAME_OVER;
        }
        else{
            return null;
        }
    }
}
