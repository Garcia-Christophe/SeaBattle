package battle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Battleship is a game in which two players must place "ships" on a secret grid
 * and attempt to "hit" the opposing ships. The winner is the one who manages to
 * sink all of the opponent's ships before all of his or her own ships are sunk.
 * A ship is said to be sunk if each of its squares has been hit by an
 * opponent's move.
 * 
 * @author Christophe
 * @version 1.0
 */
public class BattleShip {
	private int width;
	private int height;
	private final String DELIMITER = "\\s*:\\s*";
	private Mode mode;
	private ArrayList<Ship> fleet;
	private Game gamePlay;

	/**
	 * BattleShip constructor Creates new form BattleShip
	 * 
	 * @param fileName    name of the file
	 * @param playerName1 name of the first player
	 * @param playerName2 name of the second player
	 */
	public BattleShip(String fileName, String playerName1, String playerName2) {
		this.fleet = new ArrayList<Ship>();
		this.configure(fileName);
		this.printConfiguration();
		this.gamePlay = new Game(this.fleet, playerName1, playerName2, this.width, this.height, this.mode);
		this.gamePlay.start();
	}

	/**
	 * Initializes the attributes of BattleShip
	 * 
	 * @param fileName name of the file
	 */
	private void configure(String fileName) {
		if (fileName != null) {
			Scanner in;
			try {
				// Opening the file
				in = new Scanner(new FileReader(fileName));

				// Reading and initializing data one by one
				while (in.hasNext()) {
					String nextLine = in.next();
					ArrayList<String> data = new ArrayList<>(Arrays.asList(nextLine.split(this.DELIMITER)));
					boolean isWidth = false;
					boolean isHeight = false;
					boolean isMode = false;
					boolean isShip = false;
					String shipName = null;
					for (String s : data) {
						if (isWidth) {
							this.width = Integer.parseInt(s);
							isWidth = false;
							isHeight = true;
						} else if (isHeight) {
							this.height = Integer.parseInt(s);
							isHeight = false;
						} else if (isMode) {
							if (s.equalsIgnoreCase("HH"))
								this.mode = Mode.HH;
							else if (s.equalsIgnoreCase("AH") || s.equalsIgnoreCase("HA"))
								this.mode = Mode.HA;
							else if (s.equalsIgnoreCase("AA"))
								this.mode = Mode.AA;
							else
								System.err.println("BattleShip : configure() : there is no mode.");
							isMode = false;
						} else if (isShip) {
							this.fleet.add(new Ship(shipName, Integer.valueOf(s)));
							isShip = false;
						} else if (s.equalsIgnoreCase("dimension")) {
							isWidth = true;
						} else if (s.equalsIgnoreCase("mode")) {
							isMode = true;
						} else {
							shipName = s;
							isShip = true;
						}
					}
				}

				// Closing the file
				in.close();
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.err.println("BattleShip : configure() : parameter \"fileName\" null.");
		}
	}

	public Game getGamePlay() {
		return this.gamePlay;
	}

	/**
	 * Displays the value of the attributes on the screen
	 */
	public void printConfiguration() {
		System.out.println();
		System.out.println("Here are all the data collected:");
		System.out.println("********************************");

		System.out.println("\nGame:");
		System.out.println("\tWidth\t: " + this.width);
		System.out.println("\tHeight\t: " + this.height);
		System.out.println("\tMode\t: " + this.mode);
		System.out.println("\tShips\t: " + this.fleet.size());

		System.out.println("\nShips:");
		for (Ship ship : this.fleet) {
			System.out.println("\tName\t: " + ship.getName());
			System.out.println("\tSize\t: " + ship.getSize());
			System.out.println();
		}
	}
}
