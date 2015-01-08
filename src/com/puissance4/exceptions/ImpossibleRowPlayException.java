package com.puissance4.exceptions;

public class ImpossibleRowPlayException extends Exception{
	private int row;
	public ImpossibleRowPlayException(int row)
	{
		this.row = row;
	}
	
	public String toString()
	{
		return "Impossible to play at row : "+row+"\n"+getStackTrace().toString();
	}
}
