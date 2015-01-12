package com.puissance4.model.exceptions;

public class NoneMoveException extends Exception{
	public String toString()
	{
		return "Impossible to play a none state slot\n"+getStackTrace().toString();
	}
}
