package com.puissance4.model.exceptions;

public class WrongHeightException extends Exception {
    public String toString() {
        return "Impossible to set height to the new value\n" + getStackTrace().toString();
    }
}
