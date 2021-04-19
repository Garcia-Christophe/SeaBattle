package battle;

import java.util.ArrayList;

import view.GridTableFrame;

public abstract class Player {
	private String name;
	private int width;
	private int height;
	protected ArrayList<Ship> fleet;
	protected Square[][] myGrid;
	protected Square[][] opponentGrid;
	private final int MINIMUM_SIZE_GAME = 1;
	private final int SMALLEST_INDEX_ARRAY = 0;
	private GridTableFrame myOtframe;
	private GridTableFrame opponentOtframe;

	/**
	 * Player constructor Creates new form Player
	 * 
	 * @param fleet  list of ships
	 * @param name   name of the player
	 * @param width  number of squares of width
	 * @param height number of squares of height
	 */
	public Player(ArrayList<Ship> fleet, String name, int width, int height) {
		this.createCopy(fleet);

		if (name != null)
			this.name = name;
		else {
			this.name = "";
			System.err.println("Player : Player() : parameter \"name\" null.");
		}

		if (width >= this.MINIMUM_SIZE_GAME && height >= this.MINIMUM_SIZE_GAME) {
			this.width = width;
			this.height = height;
		} else {
			this.width = this.MINIMUM_SIZE_GAME;
			this.height = this.MINIMUM_SIZE_GAME;
			System.err.println("Player : Player() : parameter \"width\" or \"height\" not conform.");
		}

		this.initializeMyGrid();
		this.initializeOpponentGrid();
	}

	/**
	 * Create e deep copy of the ship's list
	 * 
	 * @param fleet the list to copy
	 */
	protected void createCopy(ArrayList<Ship> fleet) {
		this.fleet = new ArrayList<Ship>();
		if (fleet != null) {
			for (Ship ship : fleet) {
				this.fleet.add(new Ship(ship.getName(), ship.getSize()));
			}
		} else
			System.err.println("Player : createCopy() : parameter \"fleet\" null.");
	}

	/**
	 * Initializes current player's grid
	 */
	protected void initializeMyGrid() {
		this.myGrid = new Square[this.width][this.height];
		for (int i = this.SMALLEST_INDEX_ARRAY; i < this.width; i++) {
			for (int j = this.SMALLEST_INDEX_ARRAY; j < this.height; j++) {
				this.myGrid[i][j] = new Square(i, j);
			}
		}
	}

	/**
	 * Initializes opponent's grid
	 */
	protected void initializeOpponentGrid() {
		this.opponentGrid = new Square[this.width][this.height];
		for (int i = this.SMALLEST_INDEX_ARRAY; i < this.width; i++) {
			for (int j = this.SMALLEST_INDEX_ARRAY; j < this.height; j++) {
				this.opponentGrid[i][j] = new Square(i, j);
			}
		}
	}

	/**
	 * Tests if the ship (according to the coordinates) passed in parameter is sunk.
	 * 
	 * @param x horizontal coordinate
	 * @param y vertical coordinate
	 * @return true if the ship is sunk
	 */
	public boolean isSunk(int x, int y) {
		boolean isSunk = false;
		for (Ship ship : this.fleet) {
			if (ship.contains(x, y) && ship.isSunk())
				isSunk = true;
		}
		return isSunk;
	}

	/**
	 * Allows to define if all the player's ships have been sunk
	 * 
	 * @return true if all ships have been sunk
	 */
	public boolean allSunk() {
		boolean allSunk = true;
		for (Ship ship : this.fleet) {
			if (!ship.isSunk())
				allSunk = false;
		}
		return allSunk;
	}

	/**
	 * Displays my grid on the screen
	 */
	public void displayMygrid() {
		this.myOtframe = new GridTableFrame(this.myGrid);
		this.myOtframe.showIt("My Grid", 0, 0);
	}

	/**
	 * Close the frame which displays the player grid
	 */
	public void closeMyOTFrame() {
		this.myOtframe.dispose();
	}

	/**
	 * Displays opponent grid on the screen
	 */
	public void displayOpponentGrid() {
		this.opponentOtframe = new GridTableFrame(this.opponentGrid);
		this.opponentOtframe.showIt("Opponent Grid", 640, 0);
	}

	/**
	 * Close the frame which displays the opponent grid
	 */
	public void closeOpponentOTFrame() {
		this.opponentOtframe.dispose();
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Get shot coordinates
	 * 
	 * @return shot coordinates
	 */
	public abstract int[] newShot();

	/**
	 * Places all ships on the grid
	 */
	public abstract void shipPlacement();
}
