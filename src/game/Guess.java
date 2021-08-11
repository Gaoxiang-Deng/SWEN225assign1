package game;

import cards.*;

public class Guess {

	private CharacterCard character;
	private LocationCard location;
	private WeaponCard weapon;

	/**
	 * A guess object represents either an attempt to gain more information about the murder, or
	 * an attempt to solve it.
	 * @param c The character card being guessed - the character this represents will be moved to the guessers location
	 * @param l The location being guessed - this must be where the guesser currently is
	 * @param w The weapon being guessed
	 * @param s True if this guess is an attempt to solve the murder and win the game; otherwise false
	 */
	public Guess(CharacterCard c, LocationCard l, WeaponCard w) {
		this.character = c;
		this.location = l;
		this.weapon = w;
	}

	public boolean processSolve() {
		if (character.getPlayer() != null) {
			return false;
		}
		if (location.getPlayer() != null) {
			return false;
		}
		if (weapon.getPlayer() != null) {
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
		if (character.getPlayer() != null && character.getPlayer().equals(p)) {
			result++;
		}
		if (location.getPlayer() != null && location.getPlayer().equals(p)) {
			result++;
		}
		if (weapon.getPlayer() != null && weapon.getPlayer().equals(p)) {
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

	@Override
	public String toString() {
		return character.getValue() + " in the " + location.getValue() + " with the " + weapon.getValue();
	}

}