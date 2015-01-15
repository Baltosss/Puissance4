package com.puissance4.model.exceptions;

public class TooManyReshuffleException extends Exception {
    public String toString() {
        return "Too many reshuffle attempts\n" + getStackTrace();
    }
}
