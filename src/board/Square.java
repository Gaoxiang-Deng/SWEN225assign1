package board;

import game.*;

public class Square {

	public static enum Room {
		HAUNTED_HOUSE(2, 2, 6, 6),
		MANIC_MANOR(17, 2, 21, 6),
		PERIL_PALACE(17, 17, 21, 21),
		CALAMITY_CASTLE(2, 17, 6, 21),
		VILLA_CELIA(9, 10, 14, 13);

		private int x1, y1;
		private int x2, y2;

		/**
		 * The four characters in Murder Madness, each controlled by a player
		 * @param x Initial x position of this Character
		 * @param y Initial y position of this Character
		 */
		private Room(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public int[] getCoords() {
			int[] arr = {
					x1,
					y1
			};
			return arr;
		}
	}

	private Room room;
	private boolean isRoom = false;
	private Player player;

	//Accessibility fields - can you move this direction from this square?
	private boolean up = true;
	private boolean down = true;
	private boolean left = true;
	private boolean right = true;

	public Square() {

	}

	public void setUp(boolean val) {
		up = val;
	}
	public void setDown(boolean val) {
		down = val;
	}
	public void setLeft(boolean val) {
		left = val;
	}
	public void setRight(boolean val) {
		right = val;
	}
	public boolean getUp() {
		return up;
	}
	public boolean getDown() {
		return down;
	}
	public boolean getLeft() {
		return left;
	}
	public boolean getRight() {
		return right;
	}
	public void setPlayer(Player p) {
		player = p;
	}
	public Player getPlayer() {
		return player;
	}
	public void setIsRoom() {
		isRoom = true;
	}
	public boolean getIsRoom() {
		return isRoom;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public Room getRoom() {
		return room;
	}

}