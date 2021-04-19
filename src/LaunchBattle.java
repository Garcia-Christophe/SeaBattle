import java.awt.EventQueue;

import javax.swing.JOptionPane;

import battle.BattleShip;

public class LaunchBattle {

	/**
	 * Main
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String pathname = JOptionPane.showInputDialog(null, "Enter the configuration file of the game :",
						"File pathname", JOptionPane.INFORMATION_MESSAGE);
				String namePlayer1 = JOptionPane.showInputDialog(null, "Enter the first player name :", "Name Player 1",
						JOptionPane.INFORMATION_MESSAGE);
				String namePlayer2 = JOptionPane.showInputDialog(null, "Enter the second player name :",
						"name Player 2", JOptionPane.INFORMATION_MESSAGE);
				new BattleShip(pathname, namePlayer1, namePlayer2);
			}
		});
	}
}
