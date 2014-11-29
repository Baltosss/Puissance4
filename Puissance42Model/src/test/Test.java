package test;

import java.util.Random;

import Exceptions.FullColumnException;
import Exceptions.ImpossibleColumnPlayException;
import Exceptions.NoneMoveException;
import model.*;

public class Test {

	public static void main(String[] args) {
		//gridTest();
		//playVerticalTest();
		//playHorizontalTest();
		//playBothSidesTest();
		//testCheckWinner();
		//testShuffle();
		//testPartie();
		//testPartieNulle();
		testReshuffleNulle();
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
			for(int i=0;i<10;i++)
			{
				grid.playAtColumn(3, 1);
			}
		} catch(ImpossibleColumnPlayException e)
		{
			System.out.println(e.toString());
		} catch(NoneMoveException e)
		{
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
			for(int i=0;i<10;i++)
			{
				grid.playAtRow(3, 1);
			}
		}catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println(grid.toString());
	}
	
	private static void playBothSidesTest() {
		Grid grid = new Grid(10, 10, 0);
		System.out.println("Test ajout de jeton dans les lignes et colones");
		try
		{
			grid.playAtColumn(3, 0);
			grid.playAtColumn(3, 1);
			grid.playAtColumn(11, 0);
			for(int i=0;i<10;i++)
			{
				grid.playAtColumn(3, 1);
			}
			grid.playAtRow(3, 0);
			grid.playAtRow(3, 1);
			grid.playAtRow(11, 0);
			for(int i=0;i<10;i++)
			{
				grid.playAtRow(3, 1);
			}
			grid.playAtColumn(1, 0);
		} catch(Exception e)
		{
			System.out.println(e.toString());
		}
		System.out.println(grid.toString());
	}
	
	private static void testCheckWinner() {
		Grid grid = new Grid(10, 10, 0);
		try{
			
			for(int i=0;i<4;i++)
			{
				grid.playAtColumn(9, 1);
				grid.playAtColumn(9, 0);
			}
			for(int i=0;i<1;i++)
			{
				grid.playAtRow(0, 0);
			}
			grid.playAtRow(1, 0);
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(grid.hasWon());
		System.out.println(grid.toString());
	}
	
	public static void testShuffle() {
		Grid grid = new Grid(10, 10, 2);
		try{
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
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(grid.toString());
	}
	
	public static void testPartie() {
		String[] players = {"Lucas", "Fred"};
		Party party = new Party(players, 0, 10, 10);
		Random random = new Random();
		System.out.println("C'est à "+party.getCurrentPlayer().getName()+" de commencer");
		Player winner = null;
		while(winner==null)
		{
			try{
				party.nextMove(random.nextInt(10), random.nextInt(2));
			} catch(Exception e)
			{
				System.out.println(e.toString());
			}
			winner = party.getWinner();
		}
		System.out.println(party.toString());
		System.out.println("Le vainqueur est "+winner.getName());
	}
	
	public static void testPartieNulle() {
		String[] players = {"Lucas", "Fred"};
		Party party = new Party(players, 0, 10, 10);
		Random random = new Random();
		System.out.println("C'est à "+party.getCurrentPlayer().getName()+" de commencer");
		boolean isPartyNull = false;
		while(!isPartyNull)
		{
			try {
				party.nextMove(random.nextInt(10), random.nextInt(2));
			} catch(Exception e) {
				System.out.println(e.toString());
			}
			isPartyNull = party.isPartyNull();
		}
		System.out.println(party.toString());
		System.out.println("Le dernier joueur est "+party.getCurrentPlayer().getName());
	}
	
	public static void testReshuffleNulle() {
		String[] players = {"Lucas", "Fred"};
		Party party = new Party(players, 0, 10, 10);
		Random random = new Random();
		System.out.println("C'est à "+party.getCurrentPlayer().getName()+" de commencer");
		boolean isPartyNull = false;
		while(!isPartyNull)
		{
			try {
				party.nextMove(random.nextInt(10), random.nextInt(2));
			} catch(Exception e) {
				System.out.println(e.toString());
			}
			isPartyNull = party.isPartyNull();
		}
		party.shuffle();
		System.out.println(party.toString());
		System.out.println("Le dernier joueur est "+party.getCurrentPlayer().getName());
	}
}
