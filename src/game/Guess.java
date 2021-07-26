package game;

import cards.*;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Guess {
	
	private CharacterCard character;
	private LocationCard location;
	private WeaponCard weapon;
	private boolean isSolve;
	public Player currentPlayer;
	
	/**
	 * A guess object represents either an attempt to gain more information about the murder, or
	 * an attempt to solve it.
	 * @param c The character card being guessed - the character this represents will be moved to the guessers location
	 * @param l The location being guessed - this must be where the guesser currently is
	 * @param w The weapon being guessed
	 * @param s True if this guess is an attempt to solve the murder and win the game; otherwise false
	 */
	public Guess(CharacterCard c, LocationCard l, WeaponCard w, boolean s, Player currentPlayer) {
		this.character = c;
		this.location = l;
		this.weapon = w;
		this.isSolve = s;
		this.currentPlayer = currentPlayer;
	}
	
	public boolean execute() {
		if(isSolve) {
			return solveAttempt();
		}else {
			return guessAttempt();
		}
	}

	public void reveal(Card c, Player playerTo){
		System.out.println("Card " + c + " is being revealed to " + playerTo);
		System.out.println("Please ensure they have seen this screen before proceeding");
;	}
	
	private boolean guessAttempt() {
		Player currentPlayer = this.currentPlayer;
		List<Player> players = Game.getPlayers();
		List<Player> turnPassed = new ArrayList<Player>();
		int startingIndex = 0;
		boolean cont = true;

		if(currentPlayer.name == "LUCILLA") {
			startingIndex = 1;
		}else if(currentPlayer.name == "BERT"){
			startingIndex = 2;
		}else if(currentPlayer.name == "MALINA"){
			startingIndex = 3;
		}else if(currentPlayer.name == "PERCY") {
			startingIndex = 0;
		}

		while(cont) {
			for (int i = startingIndex; i < players.size(); i++) {
				Player p = players.get(i);
				Card revealedCard;
				List<Card> guessedCardsInHand = getCorrectCards(p);

				System.out.println("It is " + p.getName() + "'s turn. Please pass the tablet to them.");

				if (guessedCardsInHand.size() == 0) {
					System.out.println("You not have any of the guessed cards in their hand. Please pass the tablet to the next player.");
				} else if (guessedCardsInHand.size() == 1) {
					revealedCard = guessedCardsInHand.get(0);
					System.out.println("You have only one of the guessed cards in your hand. You must show " + guessedCardsInHand.get(0));
					reveal(revealedCard, currentPlayer);
				} else if (guessedCardsInHand.size() == 2) {
					System.out.println("You have 2 of the guessed cards in your hand. Which would you like to show? (Please select 1 or 2");
					Scanner sc = new Scanner(System.in);
					String line = sc.nextLine();
					if(line.equals("1")){
						revealedCard = guessedCardsInHand.get(0);
						reveal(revealedCard, currentPlayer);
					}else if(line.equals("2")){
						revealedCard = guessedCardsInHand.get(1);
						reveal(revealedCard, currentPlayer);
					}
				}
			}
		}
		return false;
	}
	
	private boolean solveAttempt() {
		if(character.getPlayer() != null) {
			return false;
		}
		if(location.getPlayer() != null) {
			return false;
		}
		if(weapon.getPlayer() != null) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("javadoc")
	public CharacterCard getCharacter() {
		return character;
	}

	@SuppressWarnings("javadoc")
	public LocationCard getLocation() {
		return location;
	}

	@SuppressWarnings("javadoc")
	public WeaponCard getWeapon() {
		return weapon;
	}
	
	/**
	 * Checks whether this guess contains a given card.
	 * @param c The card being checked
	 * @return True if the card is in the guess; otherwise false
	 */
	public boolean contains(Card c) {
		return c.equals(character) || c.equals(location) || c.equals(weapon);
	}

	public List<Card> getCorrectCards(Player p){
		List<Card> done = new ArrayList<Card>();
		if(character.getPlayer().equals(p)){
			done.add(character);
		}
		if(location.getPlayer().equals(p)){
			done.add(location);
		}
		if(weapon.getPlayer().equals(p)){
			done.add(weapon);
		}
		return done;
	}
	
	/**
	 * Finds the number of cards in the guess that are in the given player's hand.
	 * @param p The player being checked
	 * @return The number of cards in that player's hand that match those in the guess
	 */
	public int getNumHeld(Player p) {
		int result = 0;
		if(character.getPlayer().equals(p)) {
			result++;
		}
		if(location.getPlayer().equals(p)) {
			result++;
		}
		if(weapon.getPlayer().equals(p)) {
			result++;
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((character == null) ? 0 : character.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((weapon == null) ? 0 : weapon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Guess other = (Guess) obj;
		if (character == null) {
			if (other.character != null)
				return false;
		} else if (!character.equals(other.character))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (weapon == null) {
			if (other.weapon != null)
				return false;
		} else if (!weapon.equals(other.weapon))
			return false;
		return true;
	}
	
}

