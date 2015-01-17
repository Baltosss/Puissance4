package com.puissance4.tests;

import com.puissance4.model.Grid;
import com.puissance4.model.Party;
import com.puissance4.model.Player;
import com.puissance4.model.exceptions.FullColumnException;
import com.puissance4.model.exceptions.ImpossibleColumnPlayException;
import com.puissance4.model.exceptions.NoneMoveException;
import com.puissance4.model.exceptions.NotPlayerTurnException;
import com.puissance4.model.exceptions.WrongHeightException;
import com.puissance4.model.exceptions.WrongWidthException;

import java.util.Random;

public class TestModel {

    public static void main(String[] args) {
        //gridTest();
        //playVerticalTest();
        //playHorizontalTest();
        //playBothSidesTest();
        //testCheckWinner();
        testShuffle();
        //testPartie();
        //testPartieNulle();
        //testReshuffleNulle();
        //testOpponentShuffle();
    }

    private static void gridTest() {
        System.out.println("Test grille 5/5");
        Grid grid = new Grid(5, 5, 0);
        System.out.println(grid.toString());
        System.out.println("Test grille 3x10");
        grid = new Grid(3, 10, 0);
        System.out.println(grid.toString());
    }

    private static void playVerticalTest() {
        Grid grid = new Grid(10, 10, 0);
        System.out.println("Test ajout de jeton dans les colones");
        try {
            grid.playAtColumn(3, 0);
            grid.playAtColumn(3, 1);
            grid.playAtColumn(11, 0);
            for (int i = 0; i < 10; i++) {
                grid.playAtColumn(3, 1);
            }
        } catch (ImpossibleColumnPlayException e) {
            System.out.println(e.toString());
        } catch (NoneMoveException e) {
            System.out.println(e.toString());
        } catch (FullColumnException e) {
            System.out.println(e.toString());
        }
        System.out.println(grid.toString());
    }

    private static void playHorizontalTest() {
        Grid grid = new Grid(10, 10, 0);
        System.out.println("Test ajout de jeton dans les lignes");
        try {
            grid.playAtRow(3, 0);
            grid.playAtRow(3, 1);
            grid.playAtRow(11, 0);
            for (int i = 0; i < 10; i++) {
                grid.playAtRow(3, 1);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        System.out.println(grid.toString());
    }

    private static void playBothSidesTest() {
        Grid grid = new Grid(10, 10, 0);
        System.out.println("Test ajout de jeton dans les lignes et colones");
        try {
            grid.playAtColumn(3, 0);
            grid.playAtColumn(3, 1);
            grid.playAtColumn(11, 0);
            for (int i = 0; i < 10; i++) {
                grid.playAtColumn(3, 1);
            }
            grid.playAtRow(3, 0);
            grid.playAtRow(3, 1);
            grid.playAtRow(11, 0);
            for (int i = 0; i < 10; i++) {
                grid.playAtRow(3, 1);
            }
            grid.playAtColumn(1, 0);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(grid.toString());
    }

    private static void testCheckWinner() {
        Grid grid = new Grid(10, 10, 0);
        try {

            for (int i = 0; i < 4; i++) {
                grid.playAtColumn(9, 1);
                grid.playAtColumn(9, 0);
            }
            for (int i = 0; i < 1; i++) {
                grid.playAtRow(0, 0);
            }
            grid.playAtRow(1, 0);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(grid.hasWon());
        System.out.println(grid.toString());
    }

    public static void testShuffle() {
        Grid grid = new Grid(10, 10, 2);
        try {
            grid.playAtColumn(3, 0);
            grid.playAtColumn(4, 1);
            grid.playAtColumn(4, 0);
            grid.playAtColumn(3, 1);
            grid.playAtColumn(6, 0);
            grid.playAtColumn(5, 1);
            grid.playAtColumn(6, 0);
            grid.playAtColumn(5, 1);
            grid.playAtColumn(6, 0);
            System.out.println(grid.toString());
            grid.shuffle(1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(grid.toString());
    }

    public static void testPartie() {
        String[] players = {"Lucas", "Fred"};
        Party party = new Party(players, 0, 10, 10);
        Random random = new Random();
        System.out.println("C'est à " + party.getCurrentPlayer().getName() + " de commencer");
        Player winner = null;
        boolean isMyTurn = true;
        while (winner == null) {
            try {
                if (isMyTurn) {
                    isMyTurn = false;
                    party.nextMove(random.nextInt(10), random.nextInt(2));
                } else {
                    isMyTurn = true;
                    party.nextOpponentMove(random.nextInt(10), random.nextInt(2), party.getPlayers()[1]);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            winner = party.getWinner();
        }
        System.out.println(party.toString());
        System.out.println("Le vainqueur est " + winner.getName());
    }

    public static void testPartieNulle() {
        String[] players = {"Lucas", "Fred"};
        Party party = new Party(players, 0, 10, 10);
        Random random = new Random();
        System.out.println("C'est à " + party.getCurrentPlayer().getName() + " de commencer");
        boolean isPartyNull = false;
        while (!isPartyNull) {
            try {
                party.nextMove(random.nextInt(10), random.nextInt(2));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            isPartyNull = party.isPartyNull();
        }
        System.out.println(party.toString());
        System.out.println("Le dernier joueur est " + party.getCurrentPlayer().getName());
    }

    public static void testReshuffleNulle() {
        String[] players = {"Lucas", "Fred"};
        Party party = new Party(players, 0, 10, 10);
        Random random = new Random();
        System.out.println("C'est à " + party.getCurrentPlayer().getName() + " de commencer");
        int opponentId = 1;
        boolean isPartyNull = false;
        boolean isMyTurn = true;
        while (!isPartyNull) {
            try {
                if (isMyTurn) {
                    isMyTurn = false;
                    party.nextMove(random.nextInt(10), random.nextInt(2));

                } else {
                    isMyTurn = true;
                    party.nextOpponentMove(random.nextInt(10), random.nextInt(2), party.getPlayers()[opponentId]);

                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            isPartyNull = party.isPartyNull();
        }
        isPartyNull = party.isPartyNull();
        try {
            party.shuffle();
        } catch (NotPlayerTurnException e) {
            e.printStackTrace();
        }
        System.out.println(party.toString());
        System.out.println("Le dernier joueur est " + party.getCurrentPlayer().getName());
    }

    public static void testOpponentShuffle() {
        String[] players = {"Lucas", "Fred"};
        Party party = new Party(players, 0, 10, 10);
        Party party2 = new Party(players, 0, 10, 10);
        Random random = new Random();
        int opponentId = 1;
        boolean isMyTurn = true;
        for (int i = 0; i < 10; i++) {
            try {
                if (isMyTurn) {
                    isMyTurn = false;
                    party.nextMove(random.nextInt(10), random.nextInt(2));
                    party2.nextMove(random.nextInt(10), random.nextInt(2));

                } else {
                    isMyTurn = true;
                    party.nextOpponentMove(random.nextInt(10), random.nextInt(2), party.getPlayers()[opponentId]);
                    party2.nextOpponentMove(random.nextInt(10), random.nextInt(2), party.getPlayers()[opponentId]);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        System.out.println(party.toString());
        System.out.println(party2.toString());
        try {
            party2.opponentShuffle(party.getGrid().getGrid(), party2.getCurrentPlayer());
        } catch (WrongWidthException e) {
            e.printStackTrace();
        } catch (WrongHeightException e) {
            e.printStackTrace();
        } catch (NotPlayerTurnException e) {
            e.printStackTrace();
        }
        System.out.println(party2.toString());
    }
}
