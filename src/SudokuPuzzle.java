import java.util.ArrayList;

/**
 * This class defines the sudoku puzzle object where the grid
 * data, access and modification methods are.
 * @author Piotr Woloszyn
 *
 */
public class SudokuPuzzle {

	// The main game grid, stores the numbers
	private int[] grid;
	// Helper grid the stores where given numbers can and can't be placed
	private int[][] auxilary_grid;
	// The amount of clues in the puzzle
	private int number_of_clues;

	/**
	 * The constructor.
	 */
	public SudokuPuzzle() {
		grid = new int[81];
		auxilary_grid = new int[81][9];
	}

	/**
	 * The inner constructor used by the clone() method.
	 */
	private SudokuPuzzle(int[] grid, int[][] auxilary_grid) {
		this.grid = grid;
		this.auxilary_grid = auxilary_grid;
	}
	
	/**
	 * Sets the number_of_clues variable in the sudoku puzzle.
	 * @param number_of_clues (int) self explanatory really 
	 */
	public void setNumOfClues(int number_of_clues) {
		this.number_of_clues = number_of_clues;
	}
	
	/**
	 * Returns the number_of_clues variable.
	 * @return (int)
	 */
	public int getNumOfClues() {
		return number_of_clues;
	}

	/**
	 * Attempts to add the passed number to the grid at the specified
	 * coordinates.
	 * @param x (int) the horizontal coordinate
	 * @param y (int) the vertical coordinate
	 * @param val (int) the number that is being added
	 * @return (boolean) returns true if the number is successfully added
	 *         returns false if it isn't
	 */
	public boolean setSquare(int x, int y, int val) {
		if (auxilary_grid[x + y * 9][val - 1] == 0) {
			grid[x + y * 9] = val;
			tagRowsColumnsSquares(x, y, val);
			return true;
		} else
			return false;
	}

	/**
	 * Removes and returns the number at the specified Location.
	 * @param x (int) the horizontal coordinate
	 * @param y (int) the vertical coordinate
	 * @return (int) the number at the given coordinates
	 */
	public int clearSquare(int x, int y) {
		int out = grid[x + y * 9];
		untagRowsColumnsSquares(x, y, out);
		grid[x + y * 9] = 0;
		return out;
	}

	/**
	 * Returns the number at the specified Location.
	 * @param x (int) the horizontal coordinate
	 * @param y (int) the vertical coordinate
	 * @return (int) the number at the given coordinates
	 */
	public int getNumberAt(int x, int y) {
		return grid[x + y * 9];
	}

	/**
	 * Clears the entire row of numbers.
	 * @param y (int) the vertical coordinate
	 */
	public void clearRow(int y) {
		for (int i = 0; i < 9; i++) {
			clearSquare(i, y);
		}
	}
	
	/**
	 * Retrieves a list of positions on the grid where there are
	 * no numbers present.
	 * @return (ArrayList<Integer>) a list of empty squares
	 */
	public ArrayList<Integer> getEmptySquares() {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<81; i++) {
			if(grid[i] == 0)
				out.add(i);
		}
		return out;
	}

	/**
	 * Creates and returns an array of numbers that can be placed at the given
	 * coordinates.
	 * @param x (int) the horizontal coordinate
	 * @param y (int) the vertical coordinate
	 * @return (int[]) array of numbers that can be placed at the given
	 *         coordinates
	 */
	public int[] getAllowedNums(int x, int y) {
		int nums_count = 0;
		for (int i = 0; i < 9; i++) {
			if (auxilary_grid[x + y * 9][i] == 0)
				nums_count += 1;
		}
		int[] allowed_nums = new int[nums_count];
		int array_ptr = 0;
		for (int i = 0; i < 9; i++) {
			if (auxilary_grid[x + y * 9][i] == 0) {
				allowed_nums[array_ptr] = i + 1;
				array_ptr += 1;
			}
		}
		return allowed_nums;
	}

	/**
	 * Empties the entire puzzle of numbers.
	 */
	public void clear() {
		grid = new int[81];
		auxilary_grid = new int[81][9];
	}

	/**
	 * Prints out the entire sudoku game grid.
	 */
	public void printOut() {
		for (int i = 0; i < 81; i++) {
			if (grid[i] == 0)
				System.out.print(" ");
			else
				System.out.print(grid[i]);
			if (i % 9 == 8) {
				System.out.println();
				if (i % 27 == 26 && i < 80)
					System.out.println("-----------");
			} else if (i % 3 == 2)
				System.out.print("|");
		}
		System.out.println();
	}

	/**
	 * Marks the rows, columns, and sub squares in the puzzle that they are
	 * being covered by a given number.
	 * @param x (int) the horizontal coordinate
	 * @param y (int) the vertical coordinate
	 * @param val (int) the number that is doing the covering
	 */
	private void tagRowsColumnsSquares(int x, int y, int val) {
		if (val > 0) {
			// Tag Rows
			for (int i = 0; i < 9; i++)
				auxilary_grid[x + i * 9][val - 1] += 1;
			// Tag Cols
			for (int i = 0; i < 9; i++)
				auxilary_grid[i + y * 9][val - 1] += 1;
			// Tag Sqrs
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					auxilary_grid[((x / 3) * 3 + i) + ((y / 3) * 3 + j) * 9][val - 1] += 1;
				}
			}
		}
	}

	/**
	 * Unmarks the rows, columns, and sub squares in the puzzle that they are
	 * being covered by a given number.
	 * @param x (int) the horizontal coordinate
	 * @param y (int) the vertical coordinate
	 * @param val (int) the number that is doing the covering
	 */
	private void untagRowsColumnsSquares(int x, int y, int val) {
		if (val > 0) {
			// Untag Rows
			for (int i = 0; i < 9; i++)
				auxilary_grid[x + i * 9][val - 1] -= 1;
			// Untag Cols
			for (int i = 0; i < 9; i++)
				auxilary_grid[i + y * 9][val - 1] -= 1;
			// Untag Sqrs
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					auxilary_grid[((x / 3) * 3 + i) + ((y / 3) * 3 + j) * 9][val - 1] -= 1;
				}
			}
		}
	}

	/**
	 * The clone method, returns a copy of this sudoku puzzle object.
	 */
	public SudokuPuzzle clone() {
		int[][] aux_grid_copy = new int[81][9];
		for (int i = 0; i < 81; i++) {
			for (int j = 0; j < 9; j++) {
				aux_grid_copy[i][j] = auxilary_grid[i][j];
			}
		}
		SudokuPuzzle sudoku = new SudokuPuzzle(grid.clone(), aux_grid_copy);
		return sudoku;
	}
}
