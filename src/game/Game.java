package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import GUI.GUI;
import GUI.Subject;
import board.*;
import board.Board.Direction;
import cards.*;

public class Game extends Subject {

	private boolean gameWon = false;
	private static ArrayList < Player > players;
	private static Player currentPlayer;
	private static Player movingPlayer;
	private static int currRoll;
	private Board board;
	public static Scanner scan = new Scanner(System.in);
	public String currText = "";
	static GUI gui;

	private HashMap < String, WeaponCard > weapons = new HashMap < > ();
	private HashMap < String, CharacterCard > characters = new HashMap < > ();
	private HashMap < String, LocationCard > locations = new HashMap < > ();
	private WeaponCard selectedWeapon;
	private CharacterCard selectedCharacter;
	private boolean cardSelectAllowed = false;
	private boolean manualGuess = false;
	private Card revealedCard;

	public static void main(String[] args) {
		Game game = new Game();
		gui = new GUI(game);
		game.setup();
		game.run();
	}

	private void setup() {
		board = new Board();
		int numPlayers = getNumPlayers();
		while (numPlayers == 0) {
			numPlayers = getNumPlayers(); // continually asks user for input until satisfactory amount (2..4) is given
		}
		players = createPlayers(numPlayers);
		currentPlayer = players.get(0);
		createCards();
		distributeCards(makeSolution());
	}

	/**
	 * This method handles the running of the game - each time through the loop is
	 * one player's turn.
	 *
	 */
	private void run() {
		while (!gameWon) {
			if (!currentPlayer.isComp()) {
				// roll dice
				int rolls[] = rollDice();
				currRoll = rolls[2];
				movingPlayer = currentPlayer;

				// MOVING ====================================================================
				// player may input moves until they have no more moves, or decide to guess manually
				while (currRoll > 0 && !manualGuess) {
					if (System.currentTimeMillis() % 100 == 0) {
						notifyAllObservers();
						print("You rolled a " + rolls[2] + "\nNumber of moves left: " + currRoll);
					}
				}
				movingPlayer = null;
				manualGuess = false;

				// GUESSING ====================================================================
				// player may select two cards (displayed on screen)
				cardSelectAllowed = true;
				Square currentPlayerLocation = board.getSquare(currentPlayer.getLocX(), currentPlayer.getLocY());
				if (currentPlayerLocation.getIsRoom() && !currentPlayer.getFailedSolve()) {
					while (selectedCharacter == null || selectedWeapon == null) {
						String s = "";
						s += "Please select a character and a weapon for your guess!\n";
						if (selectedCharacter != null) s += selectedCharacter.getValue();
						if (selectedWeapon != null) s += selectedWeapon.getValue();
						print(s);
					}
					cardSelectAllowed = false;
					Guess currGuess = new Guess(selectedCharacter, locations.get(currentPlayerLocation.getRoom().name()), selectedWeapon);
					String guessInfo = "Your guess: " + selectedCharacter.getValue() +
							" in the " + currentPlayerLocation.getRoom().name() +
							" with the " + selectedWeapon.getValue();

					print(guessInfo);
					processGuess(currGuess);
				}
			}

			// NEXT TURN ====================================================================
			selectedWeapon = null;
			selectedCharacter = null;
			// end turn - pass to next player
			handOverTablet(currentPlayer, getNextPlayer());
			currentPlayer = getNextPlayer();
		}
	}

	/**
	 * Asks the user for the number of people playing the game.
	 *
	 * @return The number of players, or 0 if there is an error.
	 */
	private int getNumPlayers() {
		String input = gui.inputDialog("How many people are playing? Please enter a number from 2 to 4.");
		int numPlayers = 0;
		try {
			numPlayers = Integer.parseInt(input);
		} catch (NumberFormatException e) { // input is not an int
			//println("Error - please enter a numeral (no decimals).");
			gui.messageDialog("Error - please enter a numeral (no decimals).");
			return 0;
		}
		if (numPlayers < 2 || numPlayers > 4) {
			//println("Error - number of players must be between 2 and 4 (inclusive).");
			gui.messageDialog("Error - number of players must be between 2 and 4 (inclusive).");
			return 0;
		}
		return numPlayers;
	}

	/**
	 * Creates each Player and assigns them a Character. For ease of setup, Players
	 * are created in turn order, which decides their Character (ie. Player 1 is
	 * always Lucilla, Player 2 is always Bert, etc).
	 *
	 * @param numPlayers The number of human players - excess Characters will be
	 *                   controlled by the computer
	 * @return A populated List of Players
	 */
	private ArrayList < Player > createPlayers(int numPlayers) {
		ArrayList < Player > players = new ArrayList < Player > ();
		for (int i = 0; i < 4; i++) {
			if (i < numPlayers) { // Player is human
				String name = gui.inputDialog("Player %d, please enter your name:\n", i + 1);
				Player.Character thisChar = gui.radioButton("Choose a character:");
				gui.messageDialog("Thank you, %s. You will control %s.\n", name, thisChar.name());
				players.add(new Player(name, thisChar, false));
				thisChar.setSelected(true);
				gui.messageDialog("Please pass control to the next player.");
			} else { // Player is computer
				boolean compHasChar = false;
				for (Player.Character c: Player.Character.values()) {
					// assign computer to a character that hasn't been selected yet
					if (!c.getSelected()) {
						gui.messageDialog("Player %d, controlling %s, will be played by the computer.\n", i + 1, c.name());
						String name = "COMP" + i;
						players.add(new Player(name, c, true));
						c.setSelected(true);
						compHasChar = true;
						break;
					}
				}
				if (!compHasChar) throw new IllegalArgumentException("Computer has no character to play!");
			}
		}
		return players;
	}

	private void createCards() {
		this.characters = makeCharacterCards();
		this.locations = makeLocationCards();
		this.weapons = makeWeaponCards();
	}

	private HashMap < String, CharacterCard > makeCharacterCards() {
		HashMap < String, CharacterCard > characterCards = new HashMap < String, CharacterCard > ();
		Player.Character[] characterNames = Player.Character.values();
		for (int i = 0; i < characterNames.length; i++) {
			CharacterCard c = new CharacterCard(characterNames[i].name());
			characterCards.put(c.getValue(), c);
		}
		return characterCards;
	}

	private HashMap < String, LocationCard > makeLocationCards() {
		HashMap < String, LocationCard > locationCards = new HashMap < String, LocationCard > ();
		Square.Room[] rooms = Square.Room.values();
		for (int i = 0; i < rooms.length; i++) {
			LocationCard l = new LocationCard(rooms[i].name());
			locationCards.put(l.getValue(), l);
		}
		return locationCards;
	}

	private HashMap < String, WeaponCard > makeWeaponCards() {
		HashMap < String, WeaponCard > weaponCards = new HashMap < String, WeaponCard > ();
		WeaponCard.Weapons[] weaponNames = WeaponCard.Weapons.values();
		for (int i = 0; i < weaponNames.length; i++) {
			WeaponCard w = new WeaponCard(weaponNames[i].name());
			weaponCards.put(w.getValue(), w);
		}
		return weaponCards;
	}

	/**
	 * This method is used by distributeCards() to separate out the cards in the
	 * solution - these cards are in no players' hand and have null player value.
	 *
	 * @return A Guess object containing the solution to the game.
	 */
	private Guess makeSolution() {
		CharacterCard solveCharacter = (CharacterCard) getRandomFromSet(characters.values());
		LocationCard solveLocation = (LocationCard) getRandomFromSet(locations.values());
		WeaponCard solveWeapon = (WeaponCard) getRandomFromSet(weapons.values());
		return new Guess(solveCharacter, solveLocation, solveWeapon);
	}

	/**
	 * Deals the cards in a random order to players, checking against the supplied
	 * Guess object to ensure Cards in the solution aren't dealt to players.
	 *
	 * @param solution A Guess object containing the solution to the game.
	 */
	private void distributeCards(Guess solution) {
		// First, collate all sets of cards together
		ArrayList < Card > deckUnshuffled = new ArrayList < Card > ();
		deckUnshuffled.addAll(characters.values());
		deckUnshuffled.addAll(locations.values());
		deckUnshuffled.addAll(weapons.values());
		// Next, shuffle the deck and assign it to a stack for dealing
		Collections.shuffle(deckUnshuffled);
		ArrayDeque < Card > deckShuffled = new ArrayDeque < Card > (deckUnshuffled);
		// Finally, deal cards from the deck to each player, unless that card is in the
		// solution, in which case it is ignored
		int dealTo = 0;
		while (deckShuffled.peek() != null) {
			Card top = deckShuffled.poll();
			if (solution.contains(top)) {
				continue;
			} else {
				Player p = players.get(dealTo);
				top.setPlayer(p);
				p.giveCard(top);
				if (dealTo == 3) {
					dealTo = 0;
				} else {
					dealTo++;
				}
			}
		}

	}

	/**
	 * Used by makeSolution() to get a random card from each set of Cards
	 * (Character, Location, Weapon). This returns a Card object, so makeSolution()
	 * casts the result.
	 *
	 * @param set The set of cards to pull from randomly
	 * @return A random card from the set.
	 */
	private Card getRandomFromSet(Collection < ? extends Card > set) {
		int random = ThreadLocalRandom.current().nextInt(0, set.size());
		int count = 0;
		for (Card c: set) {
			if (count == random) {
				return c;
			}
			count++;
		}
		return null; //should be unreachable
	}

	/**
	 * Rolls two six-sided dice and returns an array of the results.
	 *
	 * @return Array of results where [0],[1] are the raw results, and [2] is the
	 *         sum.
	 */
	private int[] rollDice() {
		int[] results = new int[3];
		results[0] = ThreadLocalRandom.current().nextInt(1, 7); // max value is exclusive, so add 1 to get range of 1-6
		results[1] = ThreadLocalRandom.current().nextInt(1, 7);
		results[2] = results[0] + results[1];
		return results;
	}

	public void moveCharacter(Direction dir) {
		if (movingPlayer == null) return;
		if (!board.move(movingPlayer, dir) || currRoll <= 0) {
			println(movingPlayer.getName() + " cannot move " + dir.name());
		}
		currRoll--;
		notifyAllObservers();
	}

	private void processGuess(Guess g) {
		ArrayList < Player > inTurnOrder = getPlayersInTurnOrder();
		String info = "";

		for (Player p: inTurnOrder) {
			if (p == currentPlayer) continue;
			handOverTablet(currentPlayer, p, info);
			int numHeld = g.getNumHeld(p);
			info = String.format("%s has guessed %s.", currentPlayer.getName(), g.toString());
			if (numHeld == 0) {
				handOverTablet(p, currentPlayer, info + "\nYou do not hold any of these cards.");
				info = String.format("%s does not reveal a card.", p.getName());
			} else {
				Card card = chooseCardToShow(g);
				handOverTablet(p, currentPlayer, String.format("You have decided to reveal %s.", card.getValue()));
				info = String.format("%s reveals %s.", p.getName(), card.getValue());
			}
		}

	}

	// wait for player to click on a card in the guess and return it
	public Card chooseCardToShow(Guess guess) {
		String info = String.format("%s has guessed %s.", currentPlayer.getName(), guess.toString());
		while (revealedCard == null) {
			if (System.currentTimeMillis() % 100 == 0) {
				print(info + "\n Select a card to reveal from your hand");
				notifyAllObservers();
			}
		}
		Card val = revealedCard;
		revealedCard = null;
		return val;
	}

	/**
	 * Clears all text from the console, to prevent players from seeing information they shouldn't.
	 */
	public void flushConsole() {
		print("\033[H\033[2J");
		System.out.flush();
	}

	/**
	 * Returns an ArrayList of Players in turn order, starting from (but not including) the current player.
	 * @return
	 */
	private ArrayList < Player > getPlayersInTurnOrder() {
		ArrayList < Player > inTurnOrder = new ArrayList < Player > ();
		int index = currentPlayer.getCharacter().ordinal();
		//Set actual start to player after current
		if (index == 3) {
			index = 0;
		} else {
			index++;
		}
		while (inTurnOrder.size() < 3) {
			inTurnOrder.add(players.get(index));
			index++;
			if (index > 3) {
				index = 0;
			}
		}
		return inTurnOrder;
	}

	/**
	 * Asks the player if they would like to make a solve attempt
	 *
	 * @param p The player who may solve
	 * @return 1 if yes, 0 if no, -1 if error
	 */
	private int askForSolve(Player p) {
		printf("%s, would you like to make a solve attempt? Y/N", p.getName());
		String next = scan.nextLine();
		if (next.equalsIgnoreCase("Y") || next.equalsIgnoreCase("yes")) {
			return 1;
		} else if (next.equalsIgnoreCase("N") || next.equalsIgnoreCase("no")) {
			return 0;
		}
		println("Invalid entry - please try again.");
		return -1; // invalid input or parse error
	}

	/**
	 * Called whenever the tablet needs to be passed from one player to another -
	 * Generally when guessing or at end of turn.
	 *
	 * @param p1 The player passing the tablet
	 * @param p2 The player to who the tablet is being passed
	 */
	public void handOverTablet(Player p1, Player p2, String info) {
		gui.messageDialog(info + "\n%s, please pass the tablet to %s.\n", p1.getName(), p2.getName());
		//printf("%s, please confim you have the tablet.\n", p2.getName());
		//scan.nextLine(); // user must enter something to confirm - but can enter anything
	}

	/**
	 * Called whenever the tablet needs to be passed from one player to another -
	 * Generally when guessing or at end of turn.
	 *
	 * @param p1 The player passing the tablet
	 * @param p2 The player to who the tablet is being passed
	 */
	public void handOverTablet(Player p1, Player p2) {
		gui.messageDialog("%s, please pass the tablet to %s.\n", p1.getName(), p2.getName());
		// printf("%s, please confim you have the tablet.\n", p2.getName());
		// scan.nextLine(); // user must enter something to confirm - but can enter
		// anything
	}

	/**
	 * Called when a player wins the game. Displays a congratulatory message and the
	 * correct solution, then ends the game.
	 *
	 * @param p The winning player
	 * @param g The winning guess
	 */
	private void win(Player p, Guess g) {
		printf("%s is the winner!\n", p.getName());
		printf("The correct solution was: %s\n", g.toString());
		gameWon = true;
		scan.close();
	}

	/**
	 * Given a Character, returns the Player controlling that Character
	 *
	 * @param c
	 * @return The Player object whose Character is c
	 */
	public Player getPlayerFromCharacter(Player.Character c) {
		for (Player p: players) {
			if (p.isCharacter(c)) {
				return p;
			}
		}
		throw new IllegalArgumentException("Could not find player with character " + c.toString());
	}

	/**
	 * Finds the next player in turn order
	 *
	 * @return The Player object representing the next player in turn order
	 */
	private Player getNextPlayer() {
		int pos = currentPlayer.getCharacter().ordinal();
		Player.Character[] characters = Player.Character.values();
		if (pos == characters.length - 1) {
			pos = 0;
		} else {
			pos++;
		}
		return getPlayerFromCharacter(characters[pos]);
	}

	//====================================================
	//=============== GUI METHODS ========================
	//====================================================

	public void setRevealedCard(Card c) {
		revealedCard = c;
	}

	@Override
	public ArrayList < Player > getAllPlayers() {
		return players;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public Player getPlayer() {
		return currentPlayer;
	}

	@Override
	public List < List < ? extends Card >> getDeck() {
		List < List < ? extends Card >> allCards = new ArrayList < > ();
		List < WeaponCard > w = List.copyOf(weapons.values());
		List < CharacterCard > c = List.copyOf(characters.values());
		List < LocationCard > l = List.copyOf(locations.values());
		allCards.add(w);
		allCards.add(c);
		allCards.add(l);
		return allCards;
	}

	public void addToGuess(WeaponCard w) {
		if (cardSelectAllowed) selectedWeapon = w;
	}

	public void addToGuess(CharacterCard c) {
		if (cardSelectAllowed) selectedCharacter = c;
	}

	public void guess() {
		cardSelectAllowed = true;
		manualGuess = true;
	}

	public void solve() {}

	@Override
	public String getInfo() {
		return currText;
	}

	void println(Object o) {
		//System.out.println(o);
		currText = o.toString();
		notifyAllObservers();
	}

	void print(Object o) {
		//System.out.print(o);
		String s = "It is " + currentPlayer.getName() + "'s (" + currentPlayer.getCharacter().name() + "'s) turn\n";
		currText = s + o.toString();
		notifyAllObservers();
	}

	void printf(String s, Object...args) {
		//System.out.printf(s, args);
		currText = s.toString();
		notifyAllObservers();
	}
}