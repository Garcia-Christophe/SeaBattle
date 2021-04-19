package battle;

import java.util.ArrayList;

public class AutoPlayer extends Player {
	private boolean areShipsPlaced;
	private final int SMALLEST_INDEX_ARRAY = 0;
	private final int ONE_COORDINATE = 1;

	/**
	 * AutoPlayer constructor Creates new form AutoPlayer
	 * 
	 * @param fleet  list of ships
	 * @param name   name of the player
	 * @param width  number of squares of width
	 * @param height number of squares of height
	 */
	public AutoPlayer(ArrayList<Ship> fleet, String name, int width, int height) {
		super(fleet, name, width, height);
		do {
			this.shipPlacement();
		} while (!this.areShipsPlaced);
	}

	@Override
	public int[] newShot() {
		ArrayList<int[]> listShots = new ArrayList<int[]>();

		for (int i = this.SMALLEST_INDEX_ARRAY; i < this.opponentGrid.length; i++) {
			for (int j = this.SMALLEST_INDEX_ARRAY; j < this.opponentGrid[this.SMALLEST_INDEX_ARRAY].length; j++) {
				if (!this.opponentGrid[i][j].isHit())
					listShots.add(new int[] { i, j });
			}
		}

		int[] ret = listShots.get((int) (Math.random() * listShots.size()));

		return ret;
	}

	@Override
	public void shipPlacement() {
		this.areShipsPlaced = true;
		int shipsPlaced = 0;
		boolean isAboveFree = true;
		boolean isBelowFree = true;
		boolean isLeftFree = true;
		boolean isRightFree = true;
		boolean areShipsSquaresFree = true;
		Direction shipDirection;
		ArrayList<Square> squareList = new ArrayList<Square>();
		ArrayList<Square> squaresAlreadyUsed = new ArrayList<Square>();

		// Creates a temporary grid that will be copied when shipPlacement is done
		Square[][] squareArray = new Square[this.myGrid.length][this.myGrid[this.SMALLEST_INDEX_ARRAY].length];
		for (int i = this.SMALLEST_INDEX_ARRAY; i < this.myGrid.length; i++) {
			for (int j = this.SMALLEST_INDEX_ARRAY; j < this.myGrid[this.SMALLEST_INDEX_ARRAY].length; j++) {
				squareArray[i][j] = new Square(i, j);
			}
		}

		while (this.areShipsPlaced && shipsPlaced < this.fleet.size()) {
			Ship ship = this.fleet.get(shipsPlaced);
			int directionRandom = (int) (Math.random() * 2);
			shipDirection = (directionRandom == 0) ? Direction.HORIZONTAL : Direction.VERTICAL;
			ship.setDirection(shipDirection);
			int iStart = 0;
			int iFinish = 0;

			// Retrieves all the squares from the grid where the ship can be placed
			if (shipDirection == Direction.HORIZONTAL) {
				for (int i = this.SMALLEST_INDEX_ARRAY; i <= squareArray.length - ship.getSize(); i++) {
					for (int j = this.SMALLEST_INDEX_ARRAY; j < squareArray[this.SMALLEST_INDEX_ARRAY].length; j++) {

						// Checks the line above the squares
						if (j - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) {
							iStart = (i - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) ? i - this.ONE_COORDINATE
									: i;
							iFinish = (i + ship.getSize() < squareArray.length) ? i + ship.getSize()
									: i + ship.getSize() - this.ONE_COORDINATE;
							for (int k = iStart; k <= iFinish; k++) {
								if (!squareArray[k][j - this.ONE_COORDINATE].isFree())
									isAboveFree = false;
							}
						}

						// Checks the line below the squares
						if (j + this.ONE_COORDINATE < squareArray[this.SMALLEST_INDEX_ARRAY].length) {
							iStart = (i - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) ? i - this.ONE_COORDINATE
									: i;
							iFinish = (i + ship.getSize() < squareArray.length) ? i + ship.getSize()
									: i + ship.getSize() - this.ONE_COORDINATE;
							for (int k = iStart; k <= iFinish; k++) {
								if (!squareArray[k][j + this.ONE_COORDINATE].isFree())
									isBelowFree = false;
							}
						}

						// Checks the line on the left-side of the squares
						if (i - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) {
							if (!squareArray[i - this.ONE_COORDINATE][j].isFree())
								isLeftFree = false;
						}

						// Checks the line on the right-side of the squares
						if (i + ship.getSize() < squareArray.length) {
							if (!squareArray[i + ship.getSize()][j].isFree())
								isRightFree = false;
						}

						// Adds the square to the list of squares where the ship can be placed, only if
						// all around it is free.
						if (isAboveFree && isBelowFree && isLeftFree && isRightFree)
							squareList.add(squareArray[i][j]);
						isAboveFree = true;
						isBelowFree = true;
						isLeftFree = true;
						isRightFree = true;
					}
				}
			} else {
				for (int i = this.SMALLEST_INDEX_ARRAY; i < squareArray.length; i++) {
					for (int j = this.SMALLEST_INDEX_ARRAY; j <= squareArray[this.SMALLEST_INDEX_ARRAY].length
							- ship.getSize(); j++) {

						// Checks the line above the squares
						if (j - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) {
							iStart = (i - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) ? i - this.ONE_COORDINATE
									: i;
							iFinish = (i + this.ONE_COORDINATE < squareArray.length) ? i + this.ONE_COORDINATE : i;

							for (int k = iStart; k <= iFinish; k++) {
								if (!squareArray[k][j - this.ONE_COORDINATE].isFree())
									isAboveFree = false;
							}
						}

						// Checks the line below the squares
						if (j + ship.getSize() < squareArray[this.SMALLEST_INDEX_ARRAY].length) {
							iStart = (i - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) ? i - this.ONE_COORDINATE
									: i;
							iFinish = (i + this.ONE_COORDINATE < squareArray.length) ? i + this.ONE_COORDINATE : i;
							for (int k = iStart; k <= iFinish; k++) {
								if (!squareArray[k][j + ship.getSize()].isFree())
									isBelowFree = false;
							}
						}

						// Checks the line on the left-side of the squares
						if (i - this.ONE_COORDINATE >= this.SMALLEST_INDEX_ARRAY) {
							iStart = j;
							iFinish = j + ship.getSize() - this.ONE_COORDINATE;
							for (int k = iStart; k <= iFinish; k++) {
								if (!squareArray[i - this.ONE_COORDINATE][k].isFree())
									isLeftFree = false;
							}
						}

						// Checks the line on the right-side of the squares
						if (i + this.ONE_COORDINATE < squareArray.length) {
							iStart = j;
							iFinish = j + ship.getSize() - this.ONE_COORDINATE;
							for (int k = iStart; k <= iFinish; k++) {
								if (!squareArray[i + this.ONE_COORDINATE][k].isFree())
									isRightFree = false;
							}
						}

						// Adds the square to the list of squares where the ship can be placed, only if
						// all around it is free.
						areShipsSquaresFree = true;
						if (ship.getDirection() == Direction.HORIZONTAL) {
							for (int k = i; k < i + ship.getSize(); k++) {
								if (!squareArray[k][j].isFree())
									areShipsSquaresFree = false;
							}
						} else {
							for (int k = j; k < j + ship.getSize(); k++) {
								if (!squareArray[i][k].isFree())
									areShipsSquaresFree = false;
							}
						}

						// Add to the choice list the square only if it's free all around.
						if (isAboveFree && isBelowFree && isLeftFree && isRightFree && areShipsSquaresFree)
							squareList.add(squareArray[i][j]);
						isAboveFree = true;
						isBelowFree = true;
						isLeftFree = true;
						isRightFree = true;
					}
				}
			}

			// Remove from the choice list all squares already used
			for (Square square : squaresAlreadyUsed)
				squareList.remove(square);

			// The ship is placed in a random location
			// If the list is empty, starts the random placement again.
			if (squareList.isEmpty()) {
				this.areShipsPlaced = false;
			} else {
				int randomPlace = (int) (Math.random() * squareList.size());
				Square squareShip = squareList.get(randomPlace);
				squaresAlreadyUsed.add(squareShip);
				squareList.clear();
				ship.setXOrigin(squareShip.getX());
				ship.setYOrigin(squareShip.getY());

				if (ship.getDirection() == Direction.HORIZONTAL) {
					for (int x = ship.getXOrigin(); x < (ship.getXOrigin() + ship.getSize()); x++) {
						squareArray[x][ship.getYOrigin()].setBusy();
					}
				} else {
					for (int y = ship.getYOrigin(); y < (ship.getYOrigin() + ship.getSize()); y++) {
						squareArray[ship.getXOrigin()][y].setBusy();
					}
				}

				shipsPlaced++;
				if (shipsPlaced == this.fleet.size()) {
					for (int x = 0; x < this.myGrid.length; x++) {
						for (int y = 0; y < this.myGrid[this.SMALLEST_INDEX_ARRAY].length; y++) {
							this.myGrid[x][y] = squareArray[x][y];
						}
					}
				}
			}
		}
	}
}
