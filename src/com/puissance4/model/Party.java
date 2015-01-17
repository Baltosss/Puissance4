package com.puissance4.model;

import com.puissance4.model.exceptions.FullColumnException;
import com.puissance4.model.exceptions.FullRowException;
import com.puissance4.model.exceptions.ImpossibleColumnPlayException;
import com.puissance4.model.exceptions.ImpossibleRowPlayException;
import com.puissance4.model.exceptions.NoneMoveException;
import com.puissance4.model.exceptions.NotPlayerTurnException;
import com.puissance4.model.exceptions.WrongHeightException;
import com.puissance4.model.exceptions.WrongWidthException;

import java.io.Serializable;

public class Party implements Serializable {
    private Grid grid;
    private int countPlayers;
    private int userId;
    private Player[] players;
    private int currentPlayer;
    private boolean isManagingTurns;

    public Party(String[] playerNames, int userId, int height, int width) {
        isManagingTurns = true;
        this.userId = userId;
        countPlayers = playerNames.length;
        if (countPlayers > 0) {
            players = new Player[countPlayers];
            grid = new Grid(height, width, countPlayers);
            for (int i = 0; i < countPlayers; i++) {
                players[i] = new Player(playerNames[i], i);

            }
            currentPlayer = 0;
        }
    }

    public Party(String[] playerNames, int height, int width) {
        isManagingTurns = false; //TEST MODE
        countPlayers = playerNames.length;
        if (countPlayers > 0) {
            players = new Player[countPlayers];
            grid = new Grid(height, width, countPlayers);
            for (int i = 0; i < countPlayers; i++) {
                players[i] = new Player(playerNames[i], i);

            }
            currentPlayer = 0;
        }
    }

    //orientation = 0 for regular phone orientation, 1 otherwise
    public void nextMove(int columnId, int orientation) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException, FullRowException, ImpossibleRowPlayException, NotPlayerTurnException {
        if (isManagingTurns && userId != currentPlayer) {
            throw new NotPlayerTurnException();
        }
        playMove(columnId, orientation);
    }

    public void nextOpponentMove(int columnId, int orientation, Player opponent) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException, FullRowException, ImpossibleRowPlayException, NotPlayerTurnException {
        int opponentId = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getName().equals(opponent.getName())) {
                opponentId = i;
                break;
            }
        }
        if (opponentId != currentPlayer || !isManagingTurns) {
            throw new NotPlayerTurnException();
        }
        playMove(columnId, orientation);
    }

    public void playMove(int columnId, int orientation) throws FullColumnException, ImpossibleColumnPlayException, NoneMoveException, FullRowException, ImpossibleRowPlayException, NotPlayerTurnException {
        if (orientation == 0) {
            grid.playAtColumn(columnId, currentPlayer);
        } else {
            grid.playAtRow(columnId, currentPlayer);
        }
        if (grid.hasWon()) {
            System.out.println("Le joueur " + players[currentPlayer] + " a gagné");
        } else {
            changeCurrentPlayer();
        }
    }

    private void changeCurrentPlayer() {
        if (currentPlayer < countPlayers - 1) {
            currentPlayer++;
        } else {
            currentPlayer = 0;
        }
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public Player getWinner() {
        if (grid.hasWon()) {
            return players[currentPlayer];
        }
        return null;
    }

    public String toString() {
        String result = "Liste des joueurs : \n";
        for (int i = 0; i < countPlayers; i++) {
            result += "- " + players[i].getName() + "\n";
        }
        result += "\n" + grid.toString();
        return result;
    }

    public boolean isPartyNull() {
        return grid.isNullGame();
    }

    public void shuffle() throws NotPlayerTurnException {
        if (currentPlayer == userId) {
            grid.shuffle(currentPlayer);
            changeCurrentPlayer();
        } else {
            throw new NotPlayerTurnException();
        }
    }

    public void opponentShuffle(int[][] grid, Player opponentPlayer) throws WrongWidthException, WrongHeightException, NotPlayerTurnException {
        if (!players[currentPlayer].getName().equals(opponentPlayer.getName())) {
            throw new NotPlayerTurnException();
        }
        this.grid.setGrid(grid);
        changeCurrentPlayer();
    }

    public Player[] getPlayers() {
        return players;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getLastSlotRow() {
        return grid.getLastSlotRow();
    }

    public int getLastSlotColumn() {
        return grid.getLastSlotColumn();
    }

    public WinningLineDirection getWinDirection() {
        return grid.getWinDirection();
    }

    public boolean isInWinSlots(int column, int row) {
        return grid.isInWinSlots(column, row);
    }
}
