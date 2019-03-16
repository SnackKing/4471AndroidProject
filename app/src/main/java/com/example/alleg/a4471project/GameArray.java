package com.example.alleg.a4471project;

import java.util.Arrays;
import java.util.Random;

public class GameArray {
    public static final int[] PROGRESSION = {0,2,4,8,16,32,64,128,256,512,1024,2048};

    private int[][] values;

    private Random rand;

    public GameArray() {
        values = new int[4][4];

        for (int i = 0; i < values.length; i++) {
            Arrays.fill(values[i], PROGRESSION[0]);
        }

        rand = new Random();

        for (int i = 0; i < 2; i++ ){
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
        }
    }

    public int getValue(int row, int col) {
        return values[row][col];
    }

    // these return the score increase

    public int swipeRight() { return 0; }
    public int swipeLeft() { return 0; }
    public int swipeUp() { return 0; }
    public int swipeDown() { return 0; }

}
