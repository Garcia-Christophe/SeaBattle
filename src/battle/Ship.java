package battle;

public class Ship {
	private String name;
	private int size;
	private int xOrigin;
	private int yOrigin;
	private int hitNumber;
	private Direction direction;
	private final int MINIMUM_SIZE = 0;

	/**
	 * Ship constructor Creates new form Ship
	 * 
	 * @param name name of the new Ship
	 * @param size length of the new Ship
	 */
	public Ship(String name, int size) {
		if (name != null && size >= this.MINIMUM_SIZE) {
			this.name = new String(name);
			this.size = size;
			this.direction = Direction.HORIZONTAL;
			this.hitNumber = this.MINIMUM_SIZE;
		} else {
			throw new IllegalArgumentException(
					"Ship : Ship() : parameter \"name\" null or parameter \"size\" negative.");
		}
	}

	/**
	 * Adds a hit to the current ship
	 */
	public void addHit() {
		this.hitNumber++;
	}

	/**
	 * Returns true if the current ship is sunk
	 * 
	 * @return true if the ship's size is equals to its hit number
	 */
	public boolean isSunk() {
		return this.size == this.hitNumber;
	}

	/**
	 * Checks to see if there's a part of the ship at these coordinates.
	 * 
	 * @param x coordinate horizontal
	 * @param y coordinate vertical
	 * @return true if these coordinates point to a part of the ship
	 */
	public boolean contains(int x, int y) {
		boolean contains = false;
		if (this.direction == Direction.HORIZONTAL) {
			if (this.yOrigin == y) {
				for (int i = this.xOrigin; i < this.xOrigin + this.size; i++) {
					if (i == x)
						contains = true;
				}
			}
		} else {
			if (this.xOrigin == x) {
				for (int i = this.yOrigin; i < this.yOrigin + this.size; i++) {
					if (i == y)
						contains = true;
				}
			}
		}
		return contains;
	}

	public String getName() {
		return this.name;
	}

	public int getSize() {
		return this.size;
	}

	public int getHitNumber() {
		return this.hitNumber;
	}

	public int getXOrigin() {
		return this.xOrigin;
	}

	public void setXOrigin(int x) {
		this.xOrigin = x;
	}

	public int getYOrigin() {
		return this.yOrigin;
	}

	public void setYOrigin(int y) {
		this.yOrigin = y;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction dir) {
		this.direction = dir;
	}

	/**
	 * Returns a string representation of the Ship
	 * 
	 * @return a string that "textually represents" this object
	 */
	public String toString() {
		String nameText = "\tName\t: " + this.name;
		String sizeText = "\n\tSize\t: " + this.size;
		String xOrigText = "\n\txOrigin\t: " + this.xOrigin;
		String yOrigText = "\n\tyOrigin\t: " + this.yOrigin;
		String nbHitText = "\n\tNb hits\t: " + this.hitNumber;
		String directionText = "\n\tDirect.\t: " + this.direction;

		return nameText + sizeText + xOrigText + yOrigText + nbHitText + directionText;
	}
}
