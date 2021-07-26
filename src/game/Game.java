package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import cards.*;

public class Game {

    private boolean gameWon = false;
    private ArrayList<Player> players;
    private Player currentPlayer;

    private HashMap<String, WeaponCard> weapons;
    private HashMap<String, CharacterCard> characters;
    private HashMap<String, LocationCard> locations;

    public static void main(String[] args) {
        Game game = new Game();
        game.setup();
    }

    private void setup() {
        createPlayers(getNumPlayers());

    }

    private void run() {
        while (!gameWon) {
            //TODO basically the whole game
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

            }
        }
        }
    }

    private int getNumPlayers() {
        Scanner scan = new Scanner(System.in);
        int numPlayers = 0;
        //TODO read number of players from System.in - remember to check value is between 2..4
        System.out.println("This game only allow 2 ~ 4 player ");
        numPlayers = scan.nextInt();
        return numPlayers;
    }

    private ArrayList<Player> createPlayers(int numPlayers) {
        ArrayList<Player> players = new ArrayList<Player>();
        //TODO populate list with Player objects
        if (numPlayers >= 2 && numPlayers <= 4) {
            System.out.println("You can chioce LUCILLA, BERT, MALINA, PERCY");
            for (int i = 1; i <= numPlayers; i++) {
                Scanner scan = new Scanner(System.in);
                String namePlay = scan.next();
                if (namePlay == "LUCILLA") {
                    players.add(new Player("LUCILLA", 1, 1));
                } else if (namePlay == "BERT") {
                    players.add(new Player("BERT", 1, 1));
                } else if (namePlay == "MALINA") {
                    players.add(new Player("MALINA", 1, 1));
                } else if (namePlay == "PERCY") {
                    players.add(new Player("PERCY", 1, 1));
                }
            }
        }else{
            System.out.println("over the limit");
        }
        return players;
    }

    //This may need to return something - MD
    private void createCards() {
    }

    private void makeSolution() {
        //TODO cards in the solution have null player value
        //May be able to combine this with distributeCards()
    }

    private void distributeCards() {
        //TODO collate cards into one collection, then distribute randomly from there to players
    }

    /**
     * Rolls two six-sided dice and returns an array of the results.
     *
     * @return Array of results where [0],[1] are the raw results, and [2] is the sum.
     */
    private int[] rollDice() {
        int[] results = new int[3];
        results[0] = ThreadLocalRandom.current().nextInt(1, 7); //max value is exclusive, so add 1 to get range of 1-6
        results[1] = ThreadLocalRandom.current().nextInt(1, 7);
        results[2] = results[0] + results[1];
        return results;
    }

    private void getMoveInput() {

    }

    /**
     * Called when a player makes a guess. Asks the current player what cards they would like to guess, then
     * creates a new Guess object with those cards. The guess object will handle the logic of processing the guess.
     *
     * @param p The player making the guess
     * @return True if the guess was a successful solve attempt; otherwise false.
     */
    private boolean makeGuess(Player p) {
        return false;
    }

    /**
     * Called when a player wins the game. Displays a congratulatory message and the correct
     * solution, then ends the game.
     */
    private void win() {
        gameWon = true;
    }

    public Player getPlayerFromCharacter(Player.Character c) {
        for (Player p : players) {
            if (p.isCharacter(c)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Could not find player with character " + c.toString());
    }

}
