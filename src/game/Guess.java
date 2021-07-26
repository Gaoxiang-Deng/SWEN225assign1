package game;

import cards.*;

public class Guess {
	
	private CharacterCard character;
	private LocationCard location;
	private WeaponCard weapon;
	private boolean isSolve;
	
	/**
	 * A guess object represents either an attempt to gain more information about the murder, or
	 * an attempt to solve it.
	 * @param c The character card being guessed - the character this represents will be moved to the guessers location
	 * @param l The location being guessed - this must be where the guesser currently is
	 * @param w The weapon being guessed
	 * @param s True if this guess is an attempt to solve the murder and win the game; otherwise false
	 */
	public Guess(CharacterCard c, LocationCard l, WeaponCard w, boolean s) {
		this.character = c;
		this.location = l;
		this.weapon = w;
		this.isSolve = s;
	}
	
	public boolean execute() {
		if(isSolve) {
			return solveAttempt();
		}else {
			return guessAttempt();
		}
	}
	
	private boolean guessAttempt() {
		//is a guess that isnt a solve attemp
		//TODO this is where the logic of resolving a guess goes

		if(!this.isSolve){
			List<Player> players = Game.getPlayers();
			for(Player p : players){
				//needs to determine how many of the cards in the guess each player has
				List<Card> correct = new ArrayList<Card>();
				if(p.hasCard(character)){
					correct.add(character);
				}
				if(p.hasCard(weapon)){
					correct.add(weapon);
				}
				if(p.hasCard(location)){
					correct.add(location);
				}

				if(correct.size() == 0){
					System.out.println("Player" + p + " does not possess any of the guessed cards");
					break;
				}else if(correct.size() == 1){
					Card c = correct.get(0);
					System.out.println("Forcing player " + p + "to show card " + c);
				}else if(correct.size() == 2){
					System.out.println("Player " + p + " may choose from " + correct.get(0) + "and " + correct.get(1) + " to reaveal");
				}

			}
		}else{
			return false;
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
