package board;

import java.util.Random;

import board.Square.Room;
import game.*;

public class Board {

	public static enum Direction {
		UP("u", "up", "n", "north"), DOWN("d", "down", "s", "south"), LEFT("l", "left", "w", "west"), RIGHT("r", "right", "e", "east");
		
		private String[] inputs;
		
		private Direction(String...strings) {
			this.inputs = strings;
		}
		
		public boolean getFromString(String input) {
			for(String s : inputs) {
				if(s.equals(input)) {
					return true;
				}
			}
			return false;
		}
	}

	private Square[][] squares;

	public Board() {
		squares = new Square[24][24];
		makeBoard();
	}

	/**
	 * Builds the board by setting the 'walls' of all squares
	 * according to board layout
	 */
	private void makeBoard() {

		// this string represents board layout. The key below shows which characters
		// represent what state of the tile's walls.
		// u = wall up
		// l = wall left
		// r = wall right
		// d = wall down
		// q = wall up & left
		// p = wall up & right
		// z = wall down and left
		// m = wall down and right
		// (space) = no walls
		// NB this is NOT meant for visual utility or to be seen by the player - only a simple way to build the board

		String s = "";
		s += "quuuuuuuuuuuuuuuuuuuuuup";
		s += "l ddddd          ddddd r";
		s += "lrquuupl        rquuuplr";
		s += "lrl             rl   rlr";
		s += "lrl   rl   dd   rl   rlr";
		s += "lrl   rl  rqpl       rlr";
		s += "lrzdddml  rzml  rzdddmlr";
		s += "l uuuuu    uu    uuuuu r";
		s += "l                       ";
		s += "l        ddd dd        r";
		s += "l    dd rquu upl dd    r";
		s += "l   rqplrl      rqpl   r";
		s += "l   rzml      rlrzml   r";
		s += "l    uu rzd ddml uu    r";
		s += "l        uu uuu        r";
		s += "l                      r";
		s += "l d ddd    dd    d ddd r";
		s += "lrq uupl  rqpl  rq uuplr";
		s += "lrl       rzml  rl   rlr";
		s += "lrl   rl   uu   rl   rlr";
		s += "lrl   rl             rlr";
		s += "lrzdddml        rzdddmlr";
		s += "l uuuuu          uuuuu r";
		s += "zddddddddddddddddddddddm";

		
		// and this string layout is converted into actual walls inside square objects
		for(int y = 0; y < 24; y++) {
			for(int x = 0; x < 24; x++){
				squares[y][x] = new Square();
				char curr = s.charAt(y*24 + x);
				if(curr == 'u') squares[y][x].setUp(false);
				else if(curr == 'l') squares[y][x].setLeft(false);
				else if(curr == 'd') squares[y][x].setDown(false);
				else if(curr == 'r') squares[y][x].setRight(false);
				else if(curr == 'q') {
					squares[y][x].setUp(false);
					squares[y][x].setLeft(false);
				}
				else if(curr == 'p') {
					squares[y][x].setUp(false);
					squares[y][x].setRight(false);
				}
				else if(curr == 'z') {
					squares[y][x].setDown(false);
					squares[y][x].setLeft(false);
				}
				else if(curr == 'm') {
					squares[y][x].setDown(false);
					squares[y][x].setRight(false);
				}
				
				if((x > 2 && x < 6) && (y > 2 && y < 6)) {
					squares[y][x].setIsRoom();
					squares[y][x].setRoom(Room.HAUNTED_HOUSE);
				}
				else if((x > 17 && x < 21) && (y > 2 && y < 6)) {
					squares[y][x].setIsRoom();
					squares[y][x].setRoom(Room.MANIC_MANOR);
				}
				else if((x > 2 && x < 6) && (y > 17 && y < 21)) {
					squares[y][x].setIsRoom();
					squares[y][x].setRoom(Room.CALAMITY_CASTLE);
				}
				else if((x > 17 && x < 21) && (y > 17 && y < 21)) {
					squares[y][x].setIsRoom();
					squares[y][x].setRoom(Room.PERIL_PALACE);
				}
				else if((x > 9 && x < 14) && (y > 10 && y < 13)) {
					squares[y][x].setIsRoom();
					squares[y][x].setRoom(Room.VILLA_CELIA);
				}
				
			}
		}
		
		
		
	}
	
	/**
	 * Move the given player one square in the given direction
	 * (by setting the current square's player field to null,
	 * and the new square's field to the given player)
	 * @param player
	 * @param direction
	 * @return False if move is impossible, else true
	 */
	public boolean move(Player player, Board.Direction direction) {
		
		int x = player.getLocX();
		int y = player.getLocY();
		if(!canMove(player, direction)) return false;
		
		if(direction == Direction.DOWN) {
			squares[x][y].setPlayer(null);
			squares[x][y+1].setPlayer(player);
			player.setLocation(x, y+1);
		}
		else if(direction == Direction.UP) {
			squares[x][y].setPlayer(null);
			squares[x][y-1].setPlayer(player);
			player.setLocation(x, y -1);
		}
		else if(direction == Direction.LEFT) {
			squares[x][y].setPlayer(null);
			squares[x-1][y].setPlayer(player);
			player.setLocation(x-1, y);
		}
		else if(direction == Direction.RIGHT) {
			squares[x][y].setPlayer(null);
			squares[x+1][y].setPlayer(player);
			player.setLocation(x+1, y);
		}
		
		return true;
	}

	/**
	 * Moves the player into a random square in the given room.
	 * If the square is occupied by another player, recurses until it finds a non-occupied square.
	 * @param player
	 * @param room
	 * @return
	 */
	public boolean moveToRoom(Player player, Square.Room room){
		Random r = new Random();
		int x, y;

		if(room == Square.Room.VILLA_CELIA){
			x = room.getCoords()[0] + r.nextInt(6);
			y = room.getCoords()[1] + r.nextInt(4);
		}
		else{
			x = room.getCoords()[0] + r.nextInt(5);
			y = room.getCoords()[1] + r.nextInt(5);
		}
		
		if(squares[x][y].getPlayer() == null){
			player.setLocation(x, y);
			return true;
		}
		else{
			return moveToRoom(player, room);
		}
	}
	
	/**
	 * Returns true if the given player can move in the given direction
	 * @param player
	 * @param direction
	 * @return
	 */
	private boolean canMove(Player player, Board.Direction direction) {
		Square curr = squares[player.getLocX()][player.getLocY()];
		if(direction == Direction.DOWN) {
			if(player.getLocY()+1 > 23) return false;
			if(squares[player.getLocX()][player.getLocY()+1].getPlayer() != null) 
				return false;
			return curr.getDown();
		}
		else if(direction == Direction.UP) {
			if(player.getLocY()-1 < 0) return false;
			if (squares[player.getLocX()][player.getLocY() - 1].getPlayer() != null)
				return false;
			return curr.getUp();
		}
		else if(direction == Direction.LEFT) {
			if(player.getLocX()-1 < 0) return false;
			if (squares[player.getLocX() - 1][player.getLocY()].getPlayer() != null)
				return false;
			return curr.getLeft();
		}
		else if(direction == Direction.RIGHT) {
			if(player.getLocX()+1 > 23) return false;
			if (squares[player.getLocX() + 1][player.getLocY()].getPlayer() != null)
				return false;
			return curr.getRight();
		}
		else return false;
	}
	
	public Square getSquare(int x, int y) {
		return squares[y][x];
	}

}
