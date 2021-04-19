package battle;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Game {
	private ShotResult result;
	private Player auto;
	private Player captain;
	private Player current;
	private ArrayList<Ship> fleet;
	private final int MINIMUM_BOARD_SIZE = 3;
	private final int MAXIMUM_BOARD_SIZE = 15;
	private final int ARRAY_SHOT_SIZE = 2;
	private final int FIRST_COORDINATE = 0;
	private final int SECOND_COORDINATE = 1;

	/**
	 * Game constructor Creates new form Game
	 * 
	 * @param fleet       list of ships
	 * @param playerName1 name of the first player
	 * @param playerName2 name of the second player
	 * @param width       number of squares of width
	 * @param height      number of squares of height
	 * @param mode        game mode
	 */
	public Game(ArrayList<Ship> fleet, String playerName1, String playerName2, int width, int height, Mode mode) {
		if (fleet == null || playerName1 == null || playerName2 == null || mode == null)
			throw new IllegalArgumentException("Game : Game() : parameter null.");
		else {
			this.fleet = new ArrayList<Ship>();
			for (Ship ship : fleet) {
				this.fleet.add(new Ship(ship.getName(), ship.getSize()));
			}

			if (width >= this.MINIMUM_BOARD_SIZE && width <= this.MAXIMUM_BOARD_SIZE
					&& height >= this.MINIMUM_BOARD_SIZE && height <= this.MAXIMUM_BOARD_SIZE) {
				if (mode == Mode.HH) {
					this.captain = new HumanPlayer(this.fleet, playerName1, width, height);
					this.auto = new HumanPlayer(this.fleet, playerName2, width, height);
				} else if (mode == Mode.HA) {
					this.captain = new HumanPlayer(this.fleet, playerName1, width, height);
					this.auto = new AutoPlayer(this.fleet, playerName2, width, height);
				} else {
					this.captain = new AutoPlayer(this.fleet, playerName1, width, height);
					this.auto = new AutoPlayer(this.fleet, playerName2, width, height);
				}
				this.current = this.captain;
			} else
				System.err.println("Game : Game() : \"width\" or \"height\" too low.");
		}
	}

	/**
	 * Game loop which displays the description at the start. Indicates who starts
	 * and launches the game loop which asks the current player to shoot at each
	 * turn. Then analyzes the result.
	 */
	public void start() {
		System.out.println(this.description());
		int noRound = 1;

		// As long as the current player has at least one living ship, the game
		// continues.
		while (!this.allSunk(this.current)) {
			this.current.displayMygrid();
			this.current.displayOpponentGrid();

			int[] shot = this.readShot(this.current);
			this.result = this.analyzeShot(shot);
			if (this.current == this.captain) {
				System.out.println("Round n°" + noRound);
				System.out.println(this.current.getName() + "\t: " + this.result);
			} else {
				System.out.println(this.current.getName() + "\t: " + this.result + "\n");
				noRound++;
			}

			this.current.closeMyOTFrame();
			this.current.closeOpponentOTFrame();

			this.changeCurrent();
		}

		this.captain.displayMygrid();
		this.auto.displayMygrid();

		this.endOfGame();
	}

	/**
	 * Change the current player.
	 */
	private void changeCurrent() {
		this.current = (this.current == this.captain) ? this.auto : this.captain;
	}

	/**
	 * Ask the player to make another move.
	 * 
	 * @param player the player who must play
	 * @return new move coordinates
	 */
	public int[] readShot(Player player) {
		int[] ret = new int[this.ARRAY_SHOT_SIZE];
		if (player != null) {
			ret = player.newShot();
		}

		return ret;
	}

	/**
	 * Returns the result of the shot (use the ShotResult listing) and add a hit to
	 * the ship if he was shot.
	 * 
	 * @param shot the shot made by the current player
	 * @return the shot result
	 */
	public ShotResult analyzeShot(int[] shot) {
		ShotResult ret = ShotResult.MISS;
		Player opponent = (this.current == this.captain) ? this.auto : this.captain;

		this.current.opponentGrid[shot[this.FIRST_COORDINATE]][shot[this.SECOND_COORDINATE]].setHit();
		opponent.myGrid[shot[this.FIRST_COORDINATE]][shot[this.SECOND_COORDINATE]].setHit();

		for (Ship ship : opponent.fleet) {
			if (ship.contains(shot[this.FIRST_COORDINATE], shot[this.SECOND_COORDINATE])) {
				this.current.opponentGrid[shot[this.FIRST_COORDINATE]][shot[this.SECOND_COORDINATE]].setBusy();
				ship.addHit();
				ret = ShotResult.HIT;
				if (ship.isSunk())
					ret = ShotResult.SUNK;
			}
		}

		return ret;
	}

	/**
	 * Check if all ships of the player passed as a parameter are sunk or not.
	 * 
	 * @param player the player to whom ask if his ships are sunk
	 * @return true if all ships of the player are sunk
	 */
	public boolean allSunk(Player player) {
		boolean ret = false;
		if (player != null)
			ret = player.allSunk();

		return ret;
	}

	/**
	 * Returns the description of the game. This description can be read from a
	 * file.
	 * 
	 * @return the description of the game as a string
	 */
	public String description() {
		String l1 = " ________________________________________________________________\n";
		String l2 = "|                          GAME RULES :                          |\n";
		String l3 = "| Battleship is a game in which two players must place ships on  |\n";
		String l4 = "| a secret grid and attempt to hit the opposing ships. The winner|\n";
		String l5 = "| is the one who manages to sink all of the opponent's ships     |\n";
		String l6 = "| before all of his or her own ships are sunk. A ship is said to |\n";
		String l7 = "| be sunk if each of its squares has been hit by an opponent's   |\n";
		String l8 = "| move.                                                          |\n";
		String l9 = "|                                                                |\n";
		String l10 = "|                                                                |\n";
		String l11 = "|                         CONFIG FILES :                         |\n";
		String l12 = "| Edit the config1.txt file to change the game mode, available   |\n";
		String l13 = "| boats and grid size. Edit the config2.txt file to change the   |\n";
		String l14 = "| layout of your ships.                                          |\n";
		String l15 = "|                                                                |\n";
		String l16 = "|                       HAVE A GOOD GAME !                       |\n";
		String l17 = "|________________________________________________________________|\n";

		return l1 + l2 + l3 + l4 + l5 + l6 + l7 + l8 + l9 + l10 + l11 + l12 + l13 + l14 + l15 + l16 + l17;
	}

	/**
	 * Realizes what needs to be done when the game is over and gives the winner.
	 */
	public void endOfGame() {
		if (this.current == this.captain)
			JOptionPane.showMessageDialog(null, this.auto.getName() + " won against " + this.captain.getName(),
					"CONGRATULATIONS", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, this.captain.getName() + " won against " + this.auto.getName() + " !",
					"CONGRATULATIONS", JOptionPane.INFORMATION_MESSAGE);
	}
}
