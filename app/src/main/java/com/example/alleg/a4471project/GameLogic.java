package com.example.alleg.a4471project;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {

    public interface OnGameEnd {
        void onGameWin(int score);
        void onGameLose(int score);
        void updateHighScore(int score);
    }

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
    int currentHighScore;
    OnGameEnd ender;

    public GameLogic(Button[][] btns, TextView sd, OnGameEnd endHandler, int high) {
        gameArr = new GameArray();
        buttons = btns;
        score = 0;
        currentHighScore = high;
        ender = endHandler;

        scoreDisplay = sd;

        this.updateDisplay();
    }

    private void updateDisplay() {
        this.updateDisplay(new int[] {-1, -1});
    }

    private void updateDisplay(int[] newNum) {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        final int[] newLoc = newNum;

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                scoreDisplay.setText(Integer.toString(score));

                for (int i = 0; i < 4; i ++ ) {
                    for (int j = 0; j < 4; j ++) {
                        int value = gameArr.getValue(i, j);

                        if (newLoc[0] == i && newLoc[1] == j) {
                            buttons[i][j].setBackgroundColor(Color.YELLOW);
                        } else {
                            buttons[i][j].setBackgroundColor(getColor(value));
                        }

                        if (value == 0) {
                            buttons[i][j].setText("");
                        } else {
                            buttons[i][j].setText(Integer.toString(value));
                        }

                        if (value > 4) {
                            buttons[i][j].setTextColor(Color.WHITE);
                        } else {
                            buttons[i][j].setTextColor(Color.BLACK);
                        }
                    }
                }
            }
        });
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

    private void finishMove(GameArray.FinishState state) {
        boolean cnMv = canMove();

        if (state.score != GameArray.UNMOVED_TOKEN) {
            score += state.score;
            this.updateDisplay(gameArr.addNumber());
        } else if (cnMv){
            this.updateDisplay();


        }

        if (score > currentHighScore) {
            currentHighScore = score;
            ender.updateHighScore(score);
        }

        if (state.winningMove) {
            ender.onGameWin(score);
        } else if (!cnMv) {
            ender.onGameLose(score);
        }
    }

    private boolean canMove() {
        for (int row = 0; row < 4; row++){
            for (int col = 0; col<4; col++){
                int currentValue = gameArr.getValue(row, col);

                if (currentValue == 0) {
                    return true;
                } else {
                    int[][] moves = {{row - 1, col}, {row + 1, col}, {row, col-1}, {row, col + 1}};

                    for (int[] move : moves) {
                        // if a valid other square
                        if (move[0] >= 0 && move[0] < 4 && move[1] >= 0 && move[1] < 4) {
                            if (gameArr.getValue(move[0], move[1]) == currentValue) {
                                // can combine these
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

}
