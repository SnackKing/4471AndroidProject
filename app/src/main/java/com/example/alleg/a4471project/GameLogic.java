package com.example.alleg.a4471project;

import android.graphics.Color;
import android.widget.Button;

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

    public GameLogic(Button[][] btns) {
        gameArr = new GameArray();
        buttons = btns;

        this.updateDisplay();
    }

    private void updateDisplay() {
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
        gameArr.swipeRight();
        this.updateDisplay();
    }

    public void swipeLeft() {
        gameArr.swipeLeft();
        this.updateDisplay();
    }

    public void swipeUp() {
        gameArr.swipeUp();
        this.updateDisplay();
    }

    public void swipeDown() {
        gameArr.swipeDown();
        this.updateDisplay();
    }
}
