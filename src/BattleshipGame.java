import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class BattleshipGame {

	int numberOfGuesses;
	char[][] board = new char[5][5];
	ArrayList<Selection> previouslySelected;

	public BattleshipGame(int numberOfGuesses) {
		this.numberOfGuesses = numberOfGuesses;
		previouslySelected = new ArrayList<Selection>();
	}

	private void printBoard() {
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				// If it is previously selected show the true value
				boolean isSelected = false;
				for (Selection selection : previouslySelected) {
					if (row == selection.getRow() && col == selection.getCol()) {
						isSelected = true;
						break;
					}
				}
				if (isSelected) {
					System.out.print(board[row][col] + "\t");
				} else {
					System.out.print("*" + "\t");
				}

			}
			System.out.println();
		}
	}

	public void play() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int selectedRow = 0, selectedCol = 0;

		/*
		 * Setup the board W -> water S -> ship "*" -> covered region
		 */

		// Initialize the board with all water
		for (int row = 0; row < 5; row++)
			for (int col = 0; col < 5; col++)
				board[row][col] = 'W';

		/*
		 * Insert the ship in a random location by generating two random numbers
		 * between 0 and 5 for both the array indices
		 */
		Random random = new Random();
		int rowRandom = random.nextInt(5), colRandom = random.nextInt(5);
		board[rowRandom][colRandom] = 'S';

		System.out.println("Hey! Welcome to Battleship! You get " + this.numberOfGuesses
				+ " guesses to find and bomb the enemy's battleship! Play by inputting your selection's row and column! Good luck and happy hunting!");
		printBoard();

		int guessCounter = numberOfGuesses;
		while (guessCounter != 0) {
			// Read the selection from the user
			System.out.println("Where do you want to bomb?");
			try {
				System.out.println("Row: ");
				selectedRow = Integer.parseInt(bufferedReader.readLine());
				System.out.println("Column: ");
				selectedCol = Integer.parseInt(bufferedReader.readLine());

				// Create a Selection object, but don't add it to
				// previouslySelected arraylist yet
				Selection selection = new Selection(selectedRow, selectedCol);

				// Check if user already bombed here, i.e present in the
				// previouslySelected arraylist
				boolean alreadySelected = false;
				for (Selection previousSelection : previouslySelected) {
					if (previousSelection.getRow() == selection.getRow()
							&& previousSelection.getCol() == selection.getCol()) {
						alreadySelected = true;
						break;
					}
				}

				if (alreadySelected) {
					// User already bombed here, don't count this as a guess!
					printBoard();
					System.out.println(
							"You already bombed here! Try another location! Don't worry, won't count as a guess!");
				} else if (board[selection.getRow()][selection.getCol()] == 'S') {
					// Player guessed correctly, he wins
					previouslySelected.add(selection);
					printBoard();
					System.out.println("BOMBED! You sunk the enemy ship! Congrats  captain!");
				} else {
					// Add current selection to the previouslySelected arraylist
					previouslySelected.add(selection);
					printBoard();
					System.out.println("Nope, bomb again!");
					guessCounter--;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Invalid input, give row and column must be an integer in the range 0..4");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input, give row and column must be an integer in the range 0..4");
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		System.out.println("Aww, you lost...Then enemy's ship destroyed your port. Better luck next time!");
	}

	public static void main(String[] args) {

		int numberOfGuesses = Integer.valueOf(args[0]);
		BattleshipGame battleShipGame = new BattleshipGame(numberOfGuesses);
		battleShipGame.play();

	}

}
