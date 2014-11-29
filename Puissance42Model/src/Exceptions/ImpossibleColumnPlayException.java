package Exceptions;

public class ImpossibleColumnPlayException extends Exception{
	private int column;
	public ImpossibleColumnPlayException(int column)
	{
		this.column = column;
	}
	
	public String toString()
	{
		return "Impossible to play at column : "+column+"\n"+getStackTrace().toString();
	}
}
