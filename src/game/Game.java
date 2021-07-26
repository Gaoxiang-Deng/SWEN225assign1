package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import board.*;
import cards.*;

public class Game {
	
	private boolean gameWon = false;
	private ArrayList<Player> players;
	private static Player currentPlayer;
	private Board board;
	
	private HashMap<String, WeaponCard> weapons;
	private HashMap<String, CharacterCard> characters;
	private HashMap<String, LocationCard> locations;

	public static void main(String[] args) {
		Game game = new Game();
		game.setup();
		game.run();
	}
	
	private void setup() {
		board = new Board();
		int numPlayers = getNumPlayers();
		while(numPlayers == 0) {
			numPlayers = getNumPlayers(); //continually asks user for input until satisfactory amount (2..4) is given
		}
		players = createPlayers(numPlayers);
		createCards();
		distributeCards(makeSolution());
	}
	
	/**
	 * This method handles the running of the game - each time through the loop is one player's turn.
	 * @author Nelson
	 */
	private void run() {
		while(!gameWon) {
            //TODO basically the whole game
            int step = rollDice()[2];
            for (int i = 0; i < step; i++) {
                getMoveInput(currentPlayer);
            }
            Board board = new Board();
            Square squares = board.getSquares(currentPlayer.getLocX(), currentPlayer.getLocY());
            if(squares.getIsRoom()){
                if(makeGuess(currentPlayer)){win();}
                else{}
            }else{
                run();
            }
        }
	}
	
	/**
	 * Asks the user for the number of people playing the game.
	 * @return The number of players, or 0 if there is an error.
	 */
	private int getNumPlayers() {
		Scanner scan = new Scanner(System.in);
		System.out.println("How many people are playing? Please enter a number from 2 to 4.");
		if(!scan.hasNextInt()) {
			System.out.println("Error - please enter a numeral (no decimals).");
			scan.close();
			return 0;
		}
		int numPlayers = scan.nextInt();
		if(numPlayers < 2 || numPlayers > 4) {
			System.out.println("Error - number of players must be between 2 and 4 (inclusive).");
			scan.close();
			return 0;
		}
		scan.close();
		return numPlayers;
	}
	
	/**
	 * Creates each Player and assigns them a Character. For ease of setup, Players are created in turn order,
	 * which decides their Character (ie. Player 1 is always Lucilla, Player 2 is always Bert, etc).
	 * @param numPlayers The number of human players - excess Characters will be controlled by the computer
	 * @return A populated List of Players
	 */
	private ArrayList<Player> createPlayers(int numPlayers) {
		ArrayList<Player> players = new ArrayList<Player>();
		Player.Character[] characters = Player.Character.values();
		Scanner scan = new Scanner(System.in);
		for(int i = 0; i < 4; i++) {
			if(i < numPlayers) { //Player is human
				System.out.printf("Player %d, please enter your name:\n", i);
				String name = scan.nextLine();
				System.out.printf("Thank you, %s. As Player %d, you will control %s.\n", name, i, characters[i].name());
				players.add(new Player(name, characters[i], false));
				System.out.println("Please pass control to the next player.");
			}else { //Player is computer
				System.out.printf("Player %d, controlling %s, will be played by the computer.", i, characters[i].name());
				String name = "COMP" + i;
				players.add(new Player(name, characters[i], true));
			}
		}
		scan.close();
		return players;
	}
	
	private void createCards() {
		this.characters = makeCharacterCards();
		this.locations = makeLocationCards();
		this.weapons = makeWeaponCards();
	}
	
	private HashMap<String, CharacterCard> makeCharacterCards() {
		HashMap<String, CharacterCard> characterCards = new HashMap<String, CharacterCard>();
		Player.Character[] characters = Player.Character.values();
		for(int i = 0; i < characters.length; i++) {
			CharacterCard c = new CharacterCard(characters[i].name());
			characterCards.put(c.getValue(), c);
		}
		return characterCards;
	}
	
	private HashMap<String, LocationCard> makeLocationCards() {
		HashMap<String, LocationCard> locationCards = new HashMap<String, LocationCard>();
		Square.Room[] rooms = Square.Room.values();
		for(int i = 0; i < rooms.length; i++) {
			LocationCard l = new LocationCard(rooms[i].name());
			locationCards.put(l.getValue(), l);
		}
		return locationCards;
	}
	
	private HashMap<String, WeaponCard> makeWeaponCards() {
		HashMap<String, WeaponCard> weaponCards = new HashMap<String, WeaponCard>();
		WeaponCard.Weapons[] weapons = WeaponCard.Weapons.values();
		for(int i = 0; i < weapons.length; i++) {
			WeaponCard w = new WeaponCard(weapons[i].name());
			weaponCards.put(w.getValue(), w);
		}
		return weaponCards;
	}
	
	/**
	 * This method is used by distributeCards() to separate out the cards in the solution - 
	 * these cards are in no players' hand and have null player value.
	 * @return A Guess object containing the solution to the game.
	 */
	private Guess makeSolution() {
		CharacterCard solveCharacter = (CharacterCard) getRandomFromSet(characters.values());
		LocationCard solveLocation = (LocationCard) getRandomFromSet(locations.values());
		WeaponCard solveWeapon = (WeaponCard) getRandomFromSet(weapons.values());
		return new Guess(solveCharacter, solveLocation, solveWeapon, false, currentPlayer, players);
	}
	
	/**
	 * Deals the cards in a random order to players, checking against the supplied Guess object to ensure
	 * Cards in the solution aren't dealt to players.
	 * @param solution A Guess object containing the solution to the game.
	 */
	private void distributeCards(Guess solution) {
		//First, collate all sets of cards together
		ArrayList<Card> deckUnshuffled = new ArrayList<Card>();
		deckUnshuffled.addAll(characters.values());
		deckUnshuffled.addAll(locations.values());
		deckUnshuffled.addAll(weapons.values());
		//Next, shuffle the deck and assign it to a stack for dealing
		Collections.shuffle(deckUnshuffled);
		ArrayDeque<Card> deckShuffled = new ArrayDeque<Card>(deckUnshuffled);
		//Finally, deal cards from the deck to each player, unless that card is in the solution, in which case it is ignored
		int dealTo = 0;
		while(deckShuffled.peek() != null) {
			Card top = deckShuffled.poll();
			if(solution.contains(top)) {
				continue;
			}else {
				Player p = players.get(dealTo);
				top.setPlayer(p);
				p.giveCard(top);
				if(dealTo == 4) {
					dealTo = 0;
				}else {
					dealTo++;
				}
			}
		}
		
	}
	
	/**
	 * Used by makeSolution() to get a random card from each set of Cards (Character, Location, Weapon).
	 * This returns a Card object, so makeSolution() casts the result.
	 * @param set The set of cards to pull from randomly
	 * @return A random card from the set.
	 */
	private Card getRandomFromSet(Collection<? extends Card> set) {
		Card[] cards = (Card[]) set.toArray();
		int random = ThreadLocalRandom.current().nextInt(0, cards.length);
		return cards[random];
	}
	
	/**
	 * Rolls two six-sided dice and returns an array of the results.
	 * @return Array of results where [0],[1] are the raw results, and [2] is the sum.
	 */
	private int[] rollDice() {
		int[] results = new int[3];
		results[0] = ThreadLocalRandom.current().nextInt(1, 7); //max value is exclusive, so add 1 to get range of 1-6
		results[1] = ThreadLocalRandom.current().nextInt(1, 7);
		results[2] = results[0] + results[1];
		return results;
	}
	
	/**
	 * Gets move input from System.in
	 *  - if input is valid and move is allowed, move is applied and method returns true
	 *  - if input is invalid, or move is not allowed, method returns false
	 * @param player
	 * @return
	 */
	private boolean getMoveInput(Player player) {
		Scanner scan = new Scanner(System.in);
		boolean validMove = false;
		System.out.print("Enter a direction to move: ");
		String next = scan.nextLine();
		next = next.toLowerCase();
		if(next == "u" || next == "up" || next == "n" || next == "north"){
			if(board.move(player, Board.Direction.UP)) return true;
		}
		else if (next == "r" || next == "right" || next == "e" || next == "east") {
			if(board.move(player, Board.Direction.RIGHT)) return true;
		}
		else if (next == "d" || next == "down" || next == "s" || next == "south") {
			if(board.move(player, Board.Direction.DOWN)) return true;
		}
		else if (next == "l" || next == "left" || next == "w" || next == "west") {
			if(board.move(player, Board.Direction.LEFT)) return true;
		}
		else{
			System.out.println("Invalid move!");
			return false;
		}
		System.out.println(player.getName() + " cannot move " + next);
		return false;
	}
	
	/**
	 * Called when a player makes a guess. Asks the current player what cards they would like to guess, then
	 * creates a new Guess object with those cards. The guess object will handle the logic of processing the guess.
	 * @param p The player making the guess
	 * @return True if the guess was a successful solve attempt; otherwise false.
	 */
	private boolean makeGuess(Player p) {
		return false;
	}
	
	/**
	 * INCOMPLETE!
	 * @param p
	 */
	public static void handOverTablet(Player p) {
		System.out.printf("%s, please pass the tablet to %s.", currentPlayer.getName(), p.getName());
		System.out.printf("%s, please confim you have the tablet.", p.getName());
	}
	
	/**
	 * Called when a player wins the game. Displays a congratulatory message and the correct
	 * solution, then ends the game.
	 */
	private void win() {
		gameWon = true;
	}
	
	public Player getPlayerFromCharacter(Player.Character c) {
		for(Player p : players) {
			if(p.isCharacter(c)) {
				return p;
			}
		}
		throw new IllegalArgumentException("Could not find player with character " + c.toString());
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

}
