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
        score += gameArr.swipeRight();
        this.updateDisplay();
    }

    public void swipeLeft() {
        score += gameArr.swipeLeft();
        this.updateDisplay();
    }

    public void swipeUp() {
        score += gameArr.swipeUp();
        this.updateDisplay();
    }

    public void swipeDown() {
        score += gameArr.swipeDown();
        this.updateDisplay();
    }
}
