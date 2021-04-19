package battle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class HumanPlayer extends Player {
	private JFrame frame;
	private final String DELIMITER = "\\s*:\\s*";
	private final int SMALLEST_INDEX_ARRAY = 0;
	private final int ONE_SQUARE = 1;

	/**
	 * HumanPlayer constructor Creates new form HumanPlayer
	 * 
	 * @param fleet  list of ships
	 * @param name   name of the player
	 * @param width  number of squares of width
	 * @param height number of squares of height
	 */
	public HumanPlayer(ArrayList<Ship> fleet, String name, int width, int height) {
		super(fleet, name, width, height);
		this.shipPlacement();
	}

	@Override
	public int[] newShot() {
		int column = this.ONE_SQUARE;
		int line = this.ONE_SQUARE;
		int[] ret = new int[column + line];

		boolean hasAlreadyPlayedHere = true;
		int x = this.SMALLEST_INDEX_ARRAY;
		int y = this.SMALLEST_INDEX_ARRAY;
		while (x < this.SMALLEST_INDEX_ARRAY || x >= this.myGrid.length || y < this.SMALLEST_INDEX_ARRAY
				|| y >= this.myGrid[this.SMALLEST_INDEX_ARRAY].length || hasAlreadyPlayedHere) {
			hasAlreadyPlayedHere = false;
			String xString = JOptionPane.showInputDialog(null, "Enter the X position for the new shot :", "New Shot",
					JOptionPane.INFORMATION_MESSAGE);
			String yString = JOptionPane.showInputDialog(null, "Enter the Y position for the new shot :", "New Shot",
					JOptionPane.INFORMATION_MESSAGE);

			if (xString == null || yString == null) {
				throw new IllegalArgumentException("HumanPlayer: newShot(): x null or y null.");
			} else if (xString.length() == 1 && yString.length() == 1 && Character.isDigit(xString.charAt(0))
					&& Character.isDigit(yString.charAt(0))) {
				x = Integer.parseInt(xString);
				y = Integer.parseInt(yString);
			} else {
				x = -1;
				y = -1;
			}

			// Checks if the shot hasn't already been fired
			for (int i = this.SMALLEST_INDEX_ARRAY; i < this.getWidth(); i++) {
				for (int j = this.SMALLEST_INDEX_ARRAY; j < this.getHeight(); j++) {
					if (this.opponentGrid[i][j].getX() == x && this.opponentGrid[i][j].getY() == y) {
						if (this.opponentGrid[i][j].isHit())
							hasAlreadyPlayedHere = true;
					}
				}
			}
		}
		ret[this.SMALLEST_INDEX_ARRAY] = x;
		ret[this.ONE_SQUARE] = y;

		return ret;
	}

	@Override
	public void shipPlacement() {
		this.readConfiguration();
	}

	/**
	 * Reads a file with the data of the player's ships
	 */
	private void readConfiguration() {
		boolean areShipsSeparatedByOneSquare = true;

		String fileName = "";
		// While the filename given by the user is incorrect : asks again
		while (!(new File(fileName).exists())) {
			fileName = JOptionPane.showInputDialog(null,
					"Enter the pathname of the ships placement configuration file :", "File Pathname",
					JOptionPane.INFORMATION_MESSAGE);
			if (fileName == null)
				throw new IllegalArgumentException("HumanPlayer : readConfigure() : given filename null.");
		}
		Scanner in;
		try {
			// Opening the file
			in = new Scanner(new FileReader(fileName));

			int index = this.SMALLEST_INDEX_ARRAY;

			// While there are data missing in the file and ships are all separeted by one
			// or more square(s)
			while (in.hasNext() && areShipsSeparatedByOneSquare) {
				Ship ship = this.fleet.get(index);
				String nextLine = in.next();
				ArrayList<String> data = new ArrayList<>(Arrays.asList(nextLine.split(this.DELIMITER)));

				boolean isXOrigin = false;
				boolean isYOrigin = false;
				boolean isDirection = false;

				// Reading data one by one
				for (String s : data) {
					if (isXOrigin) {
						int x = Integer.valueOf(s);
						if (x >= this.SMALLEST_INDEX_ARRAY && (ship.getDirection() == Direction.VERTICAL)
								? x < this.myGrid.length
								: x + ship.getSize() - this.ONE_SQUARE < this.myGrid.length) {
							ship.setXOrigin(x);
							isYOrigin = true;
						} else {
							throw new IllegalArgumentException(
									"HumanPlayer: readConfiguration(): the configuration file contains badly placed ships (x coordinate not conform).");
						}
						isXOrigin = false;
					} else if (isYOrigin) {
						int y = Integer.valueOf(s);
						if (y >= this.SMALLEST_INDEX_ARRAY && (ship.getDirection() == Direction.VERTICAL)
								? y + ship.getSize() - this.ONE_SQUARE < this.myGrid[this.SMALLEST_INDEX_ARRAY].length
								: y < this.myGrid[this.SMALLEST_INDEX_ARRAY].length) {
							ship.setYOrigin(y);

						} else {
							throw new IllegalArgumentException(
									"HumanPlayer: readConfiguration(): the configuration file contains badly placed ships (y coordinate not conform).");
						}
						isYOrigin = false;
					} else if (isDirection) {
						if (s.equalsIgnoreCase(Direction.HORIZONTAL.toString())) {
							ship.setDirection(Direction.HORIZONTAL);
						} else {
							ship.setDirection(Direction.VERTICAL);
						}
						isDirection = false;
						isXOrigin = true;
					} else {
						isDirection = true;
					}
				}

				// Making square occupied by the ship busy
				if (ship != null) {
					if (ship.getDirection() == Direction.HORIZONTAL) {
						for (int x = ship.getXOrigin(); x < (ship.getXOrigin() + ship.getSize()); x++) {
							this.myGrid[x][ship.getYOrigin()].setBusy();
						}
					} else {
						for (int y = ship.getYOrigin(); y < (ship.getYOrigin() + ship.getSize()); y++) {
							this.myGrid[ship.getXOrigin()][y].setBusy();
						}
					}

					// Checking if there is no ships next to this current ship
					int iStart;
					int iFinish;

					// Checks the line above the ship
					if (ship.getYOrigin() - this.ONE_SQUARE >= this.SMALLEST_INDEX_ARRAY) {
						iStart = (ship.getXOrigin() - this.ONE_SQUARE >= this.SMALLEST_INDEX_ARRAY)
								? ship.getXOrigin() - this.ONE_SQUARE
								: ship.getXOrigin();
						if (ship.getDirection() == Direction.HORIZONTAL) {
							iFinish = (ship.getXOrigin() + ship.getSize() < this.myGrid.length)
									? ship.getXOrigin() + ship.getSize()
									: ship.getXOrigin() + ship.getSize() - this.ONE_SQUARE;
						} else {
							iFinish = (ship.getXOrigin() + this.ONE_SQUARE < this.myGrid.length)
									? ship.getXOrigin() + this.ONE_SQUARE
									: ship.getXOrigin();
						}
						for (int i = iStart; i <= iFinish; i++) {
							if (!this.myGrid[i][ship.getYOrigin() - this.ONE_SQUARE].isFree())
								areShipsSeparatedByOneSquare = false;
						}
					}

					// Checks the line below the ship
					if (areShipsSeparatedByOneSquare && (((ship.getDirection() == Direction.HORIZONTAL)
							&& (ship.getYOrigin() + this.ONE_SQUARE < this.myGrid[this.SMALLEST_INDEX_ARRAY].length))
							|| ((ship.getDirection() == Direction.VERTICAL) && (ship.getYOrigin()
									+ ship.getSize() < this.myGrid[this.SMALLEST_INDEX_ARRAY].length)))) {
						iStart = (ship.getXOrigin() - this.ONE_SQUARE >= this.SMALLEST_INDEX_ARRAY)
								? ship.getXOrigin() - this.ONE_SQUARE
								: ship.getXOrigin();
						if (ship.getDirection() == Direction.HORIZONTAL) {
							iFinish = (ship.getXOrigin() + ship.getSize() < this.myGrid.length)
									? ship.getXOrigin() + ship.getSize()
									: ship.getXOrigin() + ship.getSize() - this.ONE_SQUARE;
						} else {
							iFinish = (ship.getXOrigin() + this.ONE_SQUARE < this.myGrid.length)
									? ship.getXOrigin() + this.ONE_SQUARE
									: ship.getXOrigin();
						}
						for (int i = iStart; i <= iFinish; i++) {
							if (!this.myGrid[i][ship.getDirection() == Direction.VERTICAL
									? ship.getYOrigin() + ship.getSize()
									: ship.getYOrigin() + this.ONE_SQUARE].isFree())
								areShipsSeparatedByOneSquare = false;
						}
					}

					// Checks the line on the left-side of the ship
					if (areShipsSeparatedByOneSquare
							&& ship.getXOrigin() - this.ONE_SQUARE >= this.SMALLEST_INDEX_ARRAY) {
						iStart = ship.getYOrigin();
						if (ship.getDirection() == Direction.HORIZONTAL) {
							iFinish = ship.getYOrigin();
						} else {
							iFinish = ship.getYOrigin() + ship.getSize() - this.ONE_SQUARE;
						}
						for (int i = iStart; i <= iFinish; i++) {
							if (!this.myGrid[ship.getXOrigin() - this.ONE_SQUARE][i].isFree())
								areShipsSeparatedByOneSquare = false;
						}
					}

					// Checks the line on the right-side of the ship
					if (areShipsSeparatedByOneSquare && (((ship.getDirection() == Direction.HORIZONTAL)
							&& (ship.getXOrigin() + ship.getSize() < this.myGrid.length))
							|| ((ship.getDirection() == Direction.VERTICAL)
									&& (ship.getXOrigin() + this.ONE_SQUARE < this.myGrid.length)))) {
						iStart = ship.getYOrigin();
						if (ship.getDirection() == Direction.HORIZONTAL) {
							iFinish = ship.getYOrigin();
						} else {
							iFinish = ship.getYOrigin() + ship.getSize() - this.ONE_SQUARE;
						}
						for (int i = iStart; i <= iFinish; i++) {
							if (!this.myGrid[ship.getDirection() == Direction.HORIZONTAL
									? ship.getXOrigin() + ship.getSize()
									: ship.getXOrigin() + this.ONE_SQUARE][i].isFree())
								areShipsSeparatedByOneSquare = false;
						}
					}

					// Deleting the ship which is too close from the previous one.
					if (!areShipsSeparatedByOneSquare) {
						throw new IllegalArgumentException(
								"HumanPlayer: readConfiguration(): the configuration file contains badly separated ships.");
					}
				}

				// Next ship
				if (index < this.fleet.size()) {
					index++;
				}
			}

			// Closing the file
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
