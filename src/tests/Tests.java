package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import board.*;
import cards.*;
import game.*;

@SuppressWarnings("javadoc")
class Tests {

	@Test
	void testCardSetup_01() {
		//double check that enum.name() works the way I think it does
		WeaponCard w = new WeaponCard(WeaponCard.Weapons.BROOM.name());
		assertTrue(w.getValue().equals("BROOM"));
	}
	
	@Test
	void testCardCasting_01() {
		WeaponCard w = new WeaponCard(WeaponCard.Weapons.BROOM.name());
		Card d = (Card) w;
		assertTrue(d.getValue().equals("BROOM"));
	}
	
	@Test
	void testCardCasting_02() {
		CharacterCard c = new CharacterCard(Player.Character.BERT.name());
		Card d = (Card) c;
		assertTrue(d.getValue().equals("BERT"));
	}
	
	@Test
	void testCardCasting_03() {
		LocationCard l = new LocationCard(Square.Room.CALAMITY_CASTLE.name());
		Card d = (Card) l;
		assertTrue(d.getValue().equals("CALAMITY_CASTLE"));
	}
	
	@Test
	void testSolve_01() {
		//If all cards have null Player value then the solve is correct
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.processSolve());
	}
	
	@Test
	void testSolve_02() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(p, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertFalse(g.processSolve());
	}
	
	@Test
	void testSolve_03() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(p, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertFalse(g.processSolve());
	}
	
	@Test
	void testSolve_04() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(p, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertFalse(g.processSolve());
	}
	
	@Test
	void testSolve_05() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(p, Player.Character.BERT.name());
		LocationCard l = new LocationCard(p, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(p, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertFalse(g.processSolve());
	}
	
	@Test
	void testNumHeld_01() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(p, Player.Character.BERT.name());
		LocationCard l = new LocationCard(p, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(p, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 3);
	}
	
	@Test
	void testNumHeld_02() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 0);
	}
	
	@Test
	void testNumHeld_03() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(p, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 1);
	}
	
	@Test
	void testNumHeld_04() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(p, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 1);
	}
	
	@Test
	void testNumHeld_05() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(p, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 1);
	}
	
	@Test
	void testNumHeld_06() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(p, Player.Character.BERT.name());
		LocationCard l = new LocationCard(p, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 2);
	}
	
	@Test
	void testNumHeld_07() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(p, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(p, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 2);
	}
	
	@Test
	void testNumHeld_08() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(p, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(p, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		assertTrue(g.getNumHeld(p) == 2);
	}
	
	@Test
	void testGuessEquals_01() {
		CharacterCard c = new CharacterCard(null, Player.Character.BERT.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c, l, w);
		Guess h = new Guess(c, l, w);
		assertTrue(g.equals(h));
	}
	
	@Test
	void testGuessEquals_02() {
		CharacterCard c1 = new CharacterCard(null, Player.Character.BERT.name());
		CharacterCard c2 = new CharacterCard(null, Player.Character.LUCILLA.name());
		LocationCard l = new LocationCard(null, Square.Room.CALAMITY_CASTLE.name());
		WeaponCard w = new WeaponCard(null, WeaponCard.Weapons.BROOM.name());
		Guess g = new Guess(c1, l, w);
		Guess h = new Guess(c2, l, w);
		assertFalse(g.equals(h));
	}
	
	@Test
	//Test to make sure the isCharacter method can use == (and not .equals())
	void testPlayerSetup_01() {
		Player p = new Player("TEST", Player.Character.BERT, false);
		assertTrue(p.isCharacter(Player.Character.BERT));
	}

}
