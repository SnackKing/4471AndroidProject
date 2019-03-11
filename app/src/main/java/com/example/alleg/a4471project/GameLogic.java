package com.example.alleg.a4471project;

import android.widget.Button;

public class GameLogic {

    private static int getColor(int value) {
        return 0;
    }

    GameArray gameArr;
    Button[][] buttons;

    public GameLogic(Button[][] buttons) {
        gameArr = new GameArray();
        buttons = buttons;
    }

    private void updateDisplay() {
        for (int i = 0; i < 4; i ++ ) {
            for (int j = 0; j < 4; j ++) {
                buttons[i][j].setBackgroundColor(gameArr.getValue(i, j));
                buttons[i][j].setText(gameArr.getValue(i, j));
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
