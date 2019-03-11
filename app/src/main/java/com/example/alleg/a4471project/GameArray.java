package com.example.alleg.a4471project;

public class GameArray {
    private int[][] values;

    public GameArray() {
        values = new int[4][4];
    }

    public int getValue(int row, int col) {
        return values[row][col];
    }

    public void swipeRight() {}
    public void swipeLeft() {}
    public void swipeUp() {}
    public void swipeDown() {}

}
