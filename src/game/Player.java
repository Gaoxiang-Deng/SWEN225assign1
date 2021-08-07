package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import cards.Card;

public class Player {
	
	/**
	 * The names of the characters in the game. Their order also serves as the turn order.
	 * @author Michael
	 */
	public enum Character {
		LUCILLA(11, 1), BERT(1, 9), MALINA(9, 22), PERCY(22, 14);
		
		private int startX;
		private int startY;
		private boolean selected = false;
		
		/**
		 * The four characters in Murder Madness, each controlled by a player
		 * @param x Initial x position of this Character
		 * @param y Initial y position of this Character
		 */
		private Character(int x, int y) {
			this.startX = x;
			this.startY = y;
		}
		
		public int getX() {
			return startX;
		}
		
		public int getY() {
			return startY;
		}

		public void setSelected(boolean selected){
			this.selected = selected;
		}

		public boolean getSelected() {
			return selected;
		}
	}
	
	private HashSet<Card> hand = new HashSet<Card>();
	private String name;
	private boolean isComp;
	private boolean failedSolve = false;
	private Character character;
	
	//The Player's location on the board, as x and y coordinates.
	int locX;
	int locY;

	public Player(){}
	
	public Player(String name, Character c, boolean isComp) {
		this.name = name;
		this.character = c;
		this.isComp = isComp;
		this.locX = c.getX();
		this.locY = c.getY();
	}
	
	@SuppressWarnings("javadoc")
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("javadoc")
	public Character getCharacter() {
		return character;
	}
	
	@SuppressWarnings("javadoc")
	public int getLocX() {
		return locX;
	}
	
	@SuppressWarnings("javadoc")
	public int getLocY() {
		return locY;
	}
	
	/**
	 * Updates the player's X and Y coordinates
	 * @param x The new X coordinate
	 * @param y The new Y coordinate
	 */
	public void setLocation(int x, int y) {
		locX = x;
		locY = y;
	}
	
	public boolean isComp() {
		return isComp;
	}
	
	public boolean getFailedSolve() {
		return failedSolve;
	}

	public List<Card> getHand(){
		List<Card> cards = new ArrayList<>();
		for(Card c : hand) { cards.add(c); }
		//return Collections.unmodifiableList(cards);
		return cards;
	}
	
	/**
	 * Called when a player makes a solve attempt that is incorrect.
	 */
	public void setFailedSolve() {
		failedSolve = true;
	}
	
	/**
	 * Using this is inefficient - Cards know which player holds them.
	 * @param c
	 * @return True if the player has the specified Card; false otherwise.
	 */
	public boolean hasCard(Card c) {
		return hand.contains(c);
	}
	
	/**
	 * Adds the given card to the player's hand.
	 * @param c The card to be added
	 */
	public void giveCard(Card c) {
		hand.add(c);
	}
	
	public Card chooseCardToShow(Guess guess){
		ArrayList<Card> cards = new ArrayList<>();
		if(hasCard(guess.getCharacter())) cards.add(guess.getCharacter());
		if(hasCard(guess.getWeapon())) cards.add(guess.getWeapon());
		if(hasCard(guess.getLocation())) cards.add(guess.getLocation());

		System.out.println("You have the following cards:");
		for(int i = 0; i < cards.size(); i++){
			System.out.println(i + ": " + cards.get(i).getValue());
		}
		System.out.println("Choose the number of the card you'd like to show the guesser:");

		String next = "";
		int index = -1;
		boolean validCard = false;
		while(!validCard){
			next = Game.scan.nextLine();
			try{
				index = Integer.parseInt(next);
			}
			catch(Exception e){
				System.out.println("Please input a valid number");
			}
			if(index > 0 && index < cards.size()+1){
				validCard = true;
			}
			else{
				System.out.println("Please input a valid number");
				System.out.println("You have the following cards:");
				for (int i = 0; i < cards.size(); i++) {
					System.out.println(i + ": " + cards.get(i).getValue());
				}
			}
		}
		return cards.get(index-1);
	}
	
	/**
	 * @param c
	 * @return True if this Player is the specified Character; false otherwise.
	 */
	public boolean isCharacter(Character c) {
		return this.character == c;
	}
	
}
