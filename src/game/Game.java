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
	private static ArrayList<Player> players;
	private static Player currentPlayer;
	private Board board;
	private static Scanner scan = new Scanner(System.in);
	
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
	 * 
	 */
	private void run() {
		while(!gameWon) {
			//roll dice
			int[] rolls = rollDice();
			System.out.printf("%s rolled a %d and %d, for a total of %d.", currentPlayer.getName(), rolls[0], rolls[1], rolls[2]);
			//move
			for(int i = rolls[2]; i > 0; i--) {
				if(!getMoveInput(currentPlayer)) { //asking for move input and checking if valid
					i++; //if invalid, increment i to ask again
				}
			}
			Square currentPlayerLocation = board.getSquare(currentPlayer.getLocX(), currentPlayer.getLocY());
			//in order to guess or solve, players must be in a room and not have previously failed a solve attempt
			//computer players don't guess or solve
			if(!currentPlayer.isComp() && currentPlayerLocation.getIsRoom() && !currentPlayer.getFailedSolve()) {
				makeGuess(currentPlayer);
				boolean cont = true;
				while(cont) {
					int result = askForSolve(currentPlayer);
					if(result == 0) {
						cont = false;
						break;
					}else if(result == 1) {
						if(makeSolve(currentPlayer)) {
							//win
						}else {
							currentPlayer.setFailedSolve();
						}
						cont = false;
					}
				}
			}
			//end turn - pass to next player
			handOverTablet(currentPlayer, getNextPlayer());
			currentPlayer = getNextPlayer();
		}
	}
	
	/**
	 * Asks the user for the number of people playing the game.
	 * @return The number of players, or 0 if there is an error.
	 */
	private int getNumPlayers() {
		System.out.println("How many people are playing? Please enter a number from 2 to 4.");
		String input = scan.nextLine();
		int numPlayers = 0;
		try {
			numPlayers = Integer.parseInt(input);
		}catch(NumberFormatException e) {//input is not an int
			System.out.println("Error - please enter a numeral (no decimals).");
			return 0;
		}
		if(numPlayers < 2 || numPlayers > 4) {
			System.out.println("Error - number of players must be between 2 and 4 (inclusive).");
			return 0;
		}
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
				if(dealTo == 3) {
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
	 * Asks the player if they would like to make a solve attempt
	 * @param p The player who may solve
	 * @return 1 if yes, 0 if no, -1 if error
	 */
	private int askForSolve(Player p) {
		System.out.printf("%s, would you like to make a solve attempt? Y/N", p.getName());
		String next = scan.nextLine();
		if(next.equalsIgnoreCase("Y") || next.equalsIgnoreCase("yes")) {
			return 1;
		}else if(next.equalsIgnoreCase("N") || next.equalsIgnoreCase("no")) {
			return 0;
		}
		System.out.println("Invalid entry - please try again.");
		return -1; //invalid input or parse error
	}
	
	private boolean makeSolve(Player p) {
		return false;
	}
	
	/**
	 * Called at end of turn to pass to the next player. Also called by the Guess class, to enable refutations
	 * when a player is guessing.
	 * @param p1 The player passing the tablet
	 * @param p2 The player to who the tablet is being passed
	 */
	public static void handOverTablet(Player p1, Player p2) {
		System.out.printf("%s, please pass the tablet to %s.", p1.getName(), p2.getName());
		System.out.printf("%s, please confim you have the tablet.", p2.getName());
		scan.nextLine(); //user must enter something to confirm - but can enter anything
	}
	
	/**
	 * Called when a player wins the game. Displays a congratulatory message and the correct
	 * solution, then ends the game.
	 * @param p The winning player
	 * @param g The winning guess
	 */
	private void win(Player p, Guess g) {
		System.out.printf("%s is the winner!\n", p.getName());
		System.out.printf("The correct solution was: %s in the %s with the %s.\n", g.getCharacter().getValue(), g.getLocation().getValue(), g.getWeapon().getValue());
		gameWon = true;
		scan.close();
	}
	
	/**
	 * Given a Character, returns the Player controlling that Character
	 * @param c
	 * @return The Player object whose Character is c
	 */
	public Player getPlayerFromCharacter(Player.Character c) {
		for(Player p : players) {
			if(p.isCharacter(c)) {
				return p;
			}
		}
		throw new IllegalArgumentException("Could not find player with character " + c.toString());
	}
	
	/**
	 * Finds the next player in turn order
	 * @return The Player object representing the next player in turn order
	 */
	private Player getNextPlayer() {
		int pos = currentPlayer.getCharacter().ordinal();
		Player.Character[] characters = Player.Character.values();
		if(pos == characters.length -1) {
			pos = 0;
		}else {
			pos++;
		}
		return getPlayerFromCharacter(characters[pos]);
	}
	
	public static ArrayList<Player> getPlayers() {
		return players;
	}

}
