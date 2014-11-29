package Exceptions;

public class WrongWidthException extends Exception{
	public String toString() {
		return "Impossible to set width to the new value\n"+getStackTrace().toString();
	}
}
