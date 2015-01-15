package com.puissance4.model.exceptions;

public class NotPlayerTurnException extends Exception {
    public String toString() {
        return "Move attempt from the player not at his turn\n" + getStackTrace();
    }
}
