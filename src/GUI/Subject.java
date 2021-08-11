// ATTRIBUTION:
// This code was taken from TutorialsPoint.com
//https://www.tutorialspoint.com/design_pattern/observer_pattern.htm

package GUI;
import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Board.Direction;
import cards.Card;
import cards.CharacterCard;
import cards.WeaponCard;
import cards.WeaponCard.Weapons;
import game.Player;

public class Subject {

    private List<Observer> observers = new ArrayList<Observer>();

    /**
     * 
     * @return The game board
     */
    public Board getBoard() {
        return null;
    }
    
    /**
     * 
     * @return the current player
     */
    public Player getPlayer() {
        return null;
    }

    public ArrayList<Player> getAllPlayers() {
        return null;
    }
    
    /**
     * 
     * @return A list of all cards in the game
     */
    public List<List<? extends Card>> getDeck(){
        return null;
    }

    /**
     * 
     * @return Information about the game state
     */
    public String getInfo(){
        return null;
    }

    /**
     * Adds a  weaponcard to the current guess
     * @param w
     */
    public void addToGuess(WeaponCard w) { }

    /**
     * Adds a character card to the current guess
     * @param c
     */
    public void addToGuess(CharacterCard c) { }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void moveCharacter(Direction dir) {
    }

	public void guess() {
	}

    public void solve() {
    }

    public void setRevealedCard(Card card) {
    }
}