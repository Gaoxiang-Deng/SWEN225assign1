package cards;

import game.Player;

public class LocationCard extends Card {

	public LocationCard(Player p, String v) {
		super(p, v);
	}
	
	public LocationCard(String v) {
		super(v);
	}
	
	public LocationCard clone() {
		return new LocationCard(this.player, this.value);
	}

}
