package cards;
import game.Player;

/**
 * Represents a general card. Superclass for creating character, location and weapon cards. 
 * Best practice is to, where possible, use the subclasses instead of this.
 * Implements Cloneable as futureproofing - it may not be necessary to use these features in the final implementation.
 * 
 * @author Michael
 *
 */
public class Card implements Cloneable {
	
	/**
	 * The player who holds the card. If null then this card is part of the solution.
	 */
	protected Player player;
	
	/**
	 * The card's value (its name).
	 */
	protected String value;
	
	/**
	 * @param p The player who holds the card.
	 * @param v The card's value (its name).
	 */
	public Card(Player p, String v) {
		this.player = p;
		this.value = v;
	}
	
	public Card(String v) {
		this.value = v;
	}

	@SuppressWarnings("javadoc")
	public Player getPlayer() {
		return player;
	}
	
	@SuppressWarnings("javadoc")
	public void setPlayer(Player p) {
		this.player = p;
	}

	@SuppressWarnings("javadoc")
	public String getValue() {
		return value;
	}
	
	public Card clone() {
		return new Card(this.player, this.value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * Check if two cards are equal. As whoever holds the cards is irrelevant for this purpose,
	 * we only check the cards' values.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
