package battle;

public class Square {
	private int x;
	private int y;
	private boolean free;
	private boolean hit;
	private final int MINIMUM_SIZE = 0;

	/**
	 * Square constructor Creates new form Square
	 * 
	 * @param x coordinate horizontal
	 * @param y coordinate vertical
	 */
	public Square(int x, int y) {
		this.free = true;
		this.hit = false;

		if (x >= this.MINIMUM_SIZE && y >= this.MINIMUM_SIZE) {
			this.x = x;
			this.y = y;
		} else {
			this.x = this.MINIMUM_SIZE;
			this.y = this.MINIMUM_SIZE;
			System.err.println("Square : Square() : parameter \"x\" or \"y\" is too low.");
		}
	}

	/**
	 * Set free boolean to false
	 */
	public void setBusy() {
		this.free = false;
	}

	/**
	 * Set hit boolean to true
	 */
	public void setHit() {
		this.hit = true;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	/**
	 * Return the state of free boolean
	 * 
	 * @return true if the square is free
	 */
	public boolean isFree() {
		return this.free;
	}

	/**
	 * Return the state of hit boolean
	 * 
	 * @return true if the square is hit
	 */
	public boolean isHit() {
		return this.hit;
	}

	/**
	 * Returns a string representation of the Square
	 * 
	 * @return a string that "textually represents" this object
	 */
	public String toString() {
		String square = "Square :\n";
		String x = "\tX\t: " + this.getX() + "\n";
		String y = "\tY\t: " + this.getY() + "\n";
		String state = "\tState\t: " + ((this.isFree()) ? "free" : "busy") + "\n";
		String hitOrNot = "\tHit\t: " + ((this.isHit()) ? "true" : "false") + "\n";

		return square + x + y + state + hitOrNot;
	}
}
