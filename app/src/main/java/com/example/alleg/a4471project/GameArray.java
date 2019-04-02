package com.example.alleg.a4471project;

import java.util.Arrays;
import java.util.Random;

public class GameArray {
    public static final int[] PROGRESSION = {0,2,4,8,16,32,64,128,256,512,1024,2048};
    public static final int UNMOVED_TOKEN = -1;

    private int[][] values;
    private boolean hasWon;

    private Random rand;

    public class FinishState {
        // this is just a tuple
        boolean winningMove;
        int score;

        FinishState(boolean w, int s) {
            score = s;
            winningMove = w;
        }
    }

    public GameArray() {
        values = new int[4][4];
        hasWon = false;

        for (int i = 0; i < values.length; i++) {
            Arrays.fill(values[i], PROGRESSION[0]);
        }

        rand = new Random();

        for (int i = 0; i < 2; i++ ){
            addNumber();
        }
    }

    public int getValue(int row, int col) {
        return values[row][col];
    }

    // these return the score increase

    private boolean winningMove(int i, int j) {
        if (values[i][j] == 2048 && !hasWon) {
            hasWon = true;
            return true;
        }

        return false;
    }

    public FinishState swipeRight() {
        int score = 0;
        boolean w = false;

        boolean moved = moveRight();

        //merge
        for (int i = 0; i < values.length; i++) {
            for (int j = values[0].length - 2; j >= 0; j --) {
                if (values[i][j+1] == values[i][j] && values[i][j] != 0) {
                    moved = true;

                    values[i][j+1] *= 2;
                    values[i][j] = 0;

                    w = w || winningMove(i, j+1);

                    score += values[i][j+1];
                }
            }
        }

        moved = moveRight() || moved;

        if (!moved)
            return new FinishState(false, score);
        else return new FinishState(w, score);
    }

    public FinishState swipeLeft() {
        int score = 0;
        boolean w = false;

        boolean moved = moveLeft();

        for (int i = 0; i < values.length; i ++) {
            for (int j = values.length-2; j >= 0 ; j --) {
                if (values[i][j+1] == values[i][j] && values[i][j] != 0) {
                    moved = true;

                    // can merger
                    values[i][j+1] *= 2;
                    values[i][j] = 0;

                    w = w || winningMove(i, j+1);

                    score += values[i][j+1];
                }
            }
        }

        moved = moveLeft() || moved;
        if (!moved) return new FinishState(false, UNMOVED_TOKEN);
        else return new FinishState(w, score);
    }

    public FinishState swipeUp() {
        int score = 0;
        boolean w = false;

        boolean moved = moveUp();

        // merging
        for (int j = 0; j < values[0].length; j++) {
            for (int i = 1; i < values.length; i ++ ) {
                if (values[i][j] == values[i-1][j] && values[i][j] != 0) {
                    moved = true;

                    values[i-1][j] *= 2;
                    values[i][j] = 0;

                    w = w || winningMove(i - 1, j);

                    score += values[i-1][j];
                }
            }
        }

        moved = moveUp() || moved;
        if (!moved) return new FinishState(false, UNMOVED_TOKEN);
        else return new FinishState(w, score);
    }

    public FinishState swipeDown() {
        int score = 0;

        boolean w = false;

        boolean moved = moveDown();

        // merge
        for (int j = 0; j < values[0].length; j ++) {
            for (int i = 1; i < values.length; i ++) {
                if (values[i][j] == values[i-1][j] && values[i][j] != 0) {
                    moved = true;

                    values[i - 1][j] *= 2;
                    values[i][j] = 0;

                    w = w || winningMove(i-1, j);

                    score += values[i-1][j];
                }
            }
        }

        moved = moveDown() || moved;
        if (!moved) return new FinishState(false, UNMOVED_TOKEN);
        else return new FinishState(w, score);
    }

    public int[] addNumber() {
        //60% chance of 2, 40% chance of 4
        int starter = PROGRESSION[rand.nextInt(5) < 3 ? 1 : 2];

        int row = rand.nextInt(4);
        int col = rand.nextInt(4);

        // find the first random unused location
        while (values[row][col] != 0) {
            row = rand.nextInt(4);
            col = rand.nextInt(4);
        }

        values[row][col] = starter;

        return new int[] {row, col};
    }

    private boolean moveRight() {
        boolean moved = false;

        for (int i = 0; i < values.length; i++ ){
            for (int j = values[0].length - 2; j >= 0; j --) {
                if (values[i][j] != 0) {
                    // else do nothing
                    int walker = j + 1;

                    while (walker < values[0].length && values[i][walker] == 0) {
                        moved = true;
                        values[i][walker] = values[i][walker - 1];
                        values[i][walker - 1] = 0;
                        walker += 1;
                    }
                }
            }
        }

        return moved;
    }

    private boolean moveLeft() {
        boolean moved = false;

        for (int i = 0; i < values.length; i++) {
            for (int j = 1; j < values[0].length; j ++) {
                if (values[i][j] != 0) {
                    // else do nothing
                    int walker = j - 1;

                    while (walker >= 0 && values[i][walker] == 0) {
                        moved = true;

                        values[i][walker] = values[i][walker + 1];
                        values[i][walker + 1] = 0;
                        walker -= 1;
                    }
                }
            }
        }

        return moved;
    }

    private boolean moveUp() {
        boolean moved = false;

        for (int j = 0; j < values[0].length; j ++) {
            for (int i = 1; i < values.length; i ++ ) {
                if (values[i][j] != 0) {
                    int walker = i - 1;

                    while (walker >= 0 && values[walker][j] == 0) {
                        moved = true;
                        values[walker][j] = values[walker + 1][j];
                        values[walker + 1][j] = 0;
                        walker -= 1;
                    }
                }
            }
        }

        return moved;
    }

    private boolean moveDown() {
        boolean moved = false;

        for (int j = 0; j < values[0].length; j++) {
            for (int i = values.length - 2; i >= 0; i --) {
                if (values[i][j] != 0) {
                    int walker = i + 1;

                    while (walker < values.length && values[walker][j] == 0) {
                        moved = true;
                        values[walker][j] = values[walker-1][j];
                        values[walker-1][j] = 0;
                        walker ++;
                    }
                }
            }
        }

        return moved;
    }
}
