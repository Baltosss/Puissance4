package com.puissance4.model.exceptions;

public class FullRowException extends Exception {
    private int row;

    public FullRowException(int row) {
        this.row = row;
    }

    public String toString() {
        return "Full row : " + row + "\n" + getStackTrace().toString();
    }
}
