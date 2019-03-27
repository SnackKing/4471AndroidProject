package com.example.alleg.a4471project;

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {

    private static int getColor(int value) {
        String color;
        switch (value) {
            case 0:
                color = "#C7B9AC";
                break;
            case 2:
                color = "#EEE5DB";
                break;
            case 4:
                color = "#EEE3CF";
                break;
            case 8:
                color = "#F4B37F";
                break;
            case 16:
                color = "#EC8D53";
                break;
            case 32:
                color = "#F57C5F";
                break;
            case 64:
                color = "#E95937";
                break;
            case 128:
                color = "#EACC79";
                break;
            case 256:
                color = "#ECCC60";
                break;
            case 512:
                color = "#E5C02A";
                break;
            case 1024:
                color = "#EDC53F";
                break;
            case 2048:
                color = "#E6BF03";
                break;
            case 4096:
                color = "#D73133";
                break;
            default:
                // values larger than 4096
                color = "#DA191A";
        }

        return Color.parseColor(color);
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
        this.updateDisplay(new int[] {-1, -1});
    }

    private void updateDisplay(int[] newNum) {
        scoreDisplay.setText(Integer.toString(score));

        for (int i = 0; i < 4; i ++ ) {
            for (int j = 0; j < 4; j ++) {
                int value = gameArr.getValue(i, j);

                if (newNum[0] == i && newNum[1] == j) {
                    buttons[i][j].setBackgroundColor(Color.YELLOW);
                } else {
                    buttons[i][j].setBackgroundColor(getColor(value));
                }

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
            score += newScore;
            this.updateDisplay(gameArr.addNumber());
        } else {
            this.updateDisplay();
        }

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
