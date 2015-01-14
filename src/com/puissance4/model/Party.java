package com.puissance4.model;

import com.puissance4.model.exceptions.*;
import java.io.Serializable;

public class Party implements Serializable{
	private Grid grid;
	private int countPlayers;
	private int userId;
	private Player[] players;
	private int currentPlayer;
	
	
	public Party(String[] playerNames, int userId, int height, int width)
	{
		this.userId = userId;
		countPlayers = playerNames.length;
		if(countPlayers>0)
		{
			players = new Player[countPlayers];
			grid = new Grid(height, width, countPlayers);
			for(int i = 0; i<countPlayers; i++)
			{
				players[i] = new Player(playerNames[i], i);
				
			}
			currentPlayer = 0;
		}
	}

	public Party(String[] playerNames, int height, int width)
	{
		countPlayers = playerNames.length;
		if(countPlayers>0)
		{
			players = new Player[countPlayers];
			grid = new Grid(height, width, countPlayers);
			for(int i = 0; i<countPlayers; i++)
			{
				players[i] = new Player(playerNames[i], i);

			}
			currentPlayer = 0;
		}
	}

	//orientation = 0 for regular phone orientation, 1 otherwise
	public void nextMove(int columnId, int orientation) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException, FullRowException, ImpossibleRowPlayException, NotPlayerTurnException {
		if(userId != currentPlayer)
		{
			throw new NotPlayerTurnException();
			//return;
		}
		playMove(columnId, orientation);
	}

    public void nextOpponentMove(int columnId, int orientation, Player opponent) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException, FullRowException, ImpossibleRowPlayException, NotPlayerTurnException {
        int opponentId = -1;
        for(int i=0; i<players.length; i++) {
            if(players[i].getName().equals(opponent.getName())) {
                opponentId = i;
                break;
            }
        }
        if(opponentId != currentPlayer)
        {
            throw new NotPlayerTurnException();
        }
        playMove(columnId, orientation);
    }

    public void playMove(int columnId, int orientation) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException, FullRowException, ImpossibleRowPlayException, NotPlayerTurnException {
        int resultMove = -1;
        if(orientation==0)
        {
            resultMove = grid.playAtColumn(columnId, currentPlayer);
        }
        else
        {
            resultMove = grid.playAtRow(columnId, currentPlayer);
        }
        if(resultMove == 0)
        {
            if(grid.hasWon())
            {
                System.out.println("Le joueur "+players[currentPlayer]+" a gagné");
            }
            else
            {
                changeCurrentPlayer();
            }
        }
    }

        private void changeCurrentPlayer()
	{
		if(currentPlayer<countPlayers-1)
		{
			currentPlayer++;
		}
		else
		{
			currentPlayer = 0;
		}
	}
	
	public Player getCurrentPlayer() {
		return players[currentPlayer];
	}
	
	public Player getWinner() {
		if(grid.hasWon())
		{
			return players[currentPlayer];
		}
		return null;
	}
	
	public String toString()
	{
		String result = "Liste des joueurs : \n";
		for(int i=0; i<countPlayers; i++)
		{
			result += "- "+players[i].getName()+"\n";
		}
		result+="\n"+grid.toString();
		return result;
	}

	public boolean isPartyNull() {
		return grid.isNullGame();
	}

	public void shuffle() {
		grid.shuffle(currentPlayer);
		changeCurrentPlayer();
	}

	public Grid getGrid() {
		return grid;
	}
}
