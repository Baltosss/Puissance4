package com.puissance4.model.exceptions;

public class FullColumnException extends Exception {
    private int column;

    public FullColumnException(int column) {
        this.column = column;
    }

    public String toString() {
        return "Full column : " + column + "\n" + getStackTrace().toString();
    }
}
