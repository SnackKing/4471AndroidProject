package com.example.alleg.a4471project;

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {

    private static int getColor(int value) {
        if (value == 0) {
            return Color.GRAY;
        } else {
            return Color.CYAN;
        }
    }

    GameArray gameArr;
    Button[][] buttons;
    TextView scoreDisplay;
    int score;

    public GameLogic(Button[][] btns, TextView sd) {
        gameArr = new GameArray();
        buttons = btns;
        score = 0;

        scoreDisplay = sd;

        this.updateDisplay();
    }

    private void updateDisplay() {
        scoreDisplay.setText(Integer.toString(score));

        for (int i = 0; i < 4; i ++ ) {
            for (int j = 0; j < 4; j ++) {
                int value = gameArr.getValue(i, j);

                buttons[i][j].setBackgroundColor(getColor(value));

                if (value == 0) {
                    buttons[i][j].setText("");
                } else {
                    buttons[i][j].setText(Integer.toString(value));
                }
            }
        }
    }

    public void swipeRight() {
        finishMove(gameArr.swipeRight());
    }

    public void swipeLeft() {
        finishMove(gameArr.swipeLeft());
    }

    public void swipeUp() {
        finishMove(gameArr.swipeUp());
    }

    public void swipeDown() {
        finishMove(gameArr.swipeDown());
    }

    private void finishMove(int newScore) {
        if (newScore != GameArray.UNMOVED_TOKEN) {
            gameArr.addNumber();
            score += newScore;
        }

        this.updateDisplay();

        boolean won = hasWon();

        if (won) {
            win();
        }

        if (won && !canMove()) {
            lose();
        }
    }

    // TODO
    private boolean canMove() { return true; }

    private boolean hasWon() {
        if (score < 2048) {
            return false;
        }

        return false;
    }
    private void lose() {
        endGame();
    }

    private void win() {
        endGame();
    }

    private void endGame() {
        // update high scores and such
    }

}
