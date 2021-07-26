package cards;

import game.Player;

public class WeaponCard extends Card {
	
	public static enum Weapons {BROOM, SCISSORS, KNIFE, SHOVEL, IPAD}
	
	public WeaponCard(Player p, String v) {
		super(p, v);
	}
	
	public WeaponCard(String v) {
		super(v);
	}
	
	public WeaponCard clone() {
		return new WeaponCard(this.player, this.value);
	}

}
