package com.example.alleg.a4471project;

import java.util.Arrays;
import java.util.Random;

public class GameArray {
    public static final int[] PROGRESSION = {0,2,4,8,16,32,64,128,256,512,1024,2048};
    public static final int UNMOVED_TOKEN = -1;

    private int[][] values;

    private Random rand;

    public GameArray() {
        values = new int[4][4];

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

    public int swipeRight() {
        int score = 0;

        boolean moved = moveRight();

        //merge
        for (int i = 0; i < values.length; i++) {
            for (int j = values[0].length - 2; j >= 0; j --) {
                if (values[i][j+1] == values[i][j]) {
                    moved = true;

                    values[i][j+1] *= 2;
                    values[i][j] = 0;

                    score += values[i][j+1];
                }
            }
        }

        moved = moveRight() || moved;

        if (!moved)
            return UNMOVED_TOKEN;
        else return score;
    }

    public int swipeLeft() {
        int score = 0;

        boolean moved = moveLeft();

        for (int i = 0; i < values.length; i ++) {
            for (int j = values.length-2; j >= 0 ; j ++) {
                if (values[i][j+1] == values[i][j]) {
                    moved = true;

                    // can merger
                    values[i][j+1] *= 2;
                    values[i][j] = 0;

                    score += values[i][j+1];
                }
            }
        }

        moved = moveLeft() || moved;
        if (!moved) return UNMOVED_TOKEN;
        else return score;
    }

    public int swipeUp() {
        int score = 0;

        boolean moved = moveUp();

        // merging
        for (int j = 0; j < values[0].length; j++) {
            for (int i = 1; i < values.length; i ++ ) {
                if (values[i][j] == values[i-1][j]) {
                    moved = true;

                    values[i-1][j] *= 2;
                    values[i][j] = 0;
                    score += values[i-1][j];
                }
            }
        }

        moved = moveUp() || moved;
        if (!moved) return UNMOVED_TOKEN;
        else return score;
    }

    public int swipeDown() {
        int score = 0;

        boolean moved = moveDown();

        // merge
        for (int j = 0; j < values[0].length; j ++) {
            for (int i = 1; i < values.length; i ++) {
                if (values[i][j] == values[i-1][j]) {
                    moved = true;

                    values[i - 1][j] *= 2;
                    values[i][j] = 0;
                    score += values[i-1][j];
                }
            }
        }

        moved = moveDown() || moved;
        if (!moved) return UNMOVED_TOKEN;
        else return score;
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
