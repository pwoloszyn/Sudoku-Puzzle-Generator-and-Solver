import java.util.ArrayList;

/**
 * This class contains methods that solve passed in sudoku puzzles,
 * details are in the method descriptions.
 * @author Piotr Woloszyn
 *
 */
public class SolverModule {
	
	/**
	 * A recursive solver that assigns the solved state to the output_sudoku variable.
	 * @param sudoku (SudokuPuzzle) the puzzle object being worked on
	 * @param empty_squares	(ArrayList<Integer>) a list of empty squares on the grid
	 * @param output_sudoku (SudokuPuzzle) output variable to which the solution is assigned
	 * @return (int) returns 1 or 0, 1 if there is a solution, 0 if there is none
	 */
	public static Pair<Integer, SudokuPuzzle> solver(SudokuPuzzle sudoku, ArrayList<Integer> empty_squares) {
		if (empty_squares.size() == 0) {
			return new Pair<Integer, SudokuPuzzle>(1, sudoku);
		}
		empty_squares_Qsort(sudoku, empty_squares, 0, empty_squares.size() - 1);
		int current_square = empty_squares.get(0);
		int[] allowed_nums = sudoku.getAllowedNums(current_square % 9, current_square / 9);
		if (allowed_nums.length == 0)
			return new Pair<Integer, SudokuPuzzle>(0, sudoku);
		empty_squares.remove(0);
		Pair<Integer, SudokuPuzzle> sudokupair = new Pair<Integer, SudokuPuzzle>(0, sudoku);
		for (int i = 0; i < allowed_nums.length; i++) {
			SudokuPuzzle cloned_sudoku = sudoku.clone();
			cloned_sudoku.setSquare(current_square % 9, current_square / 9, allowed_nums[i]);
			sudokupair = solver(cloned_sudoku, (ArrayList<Integer>) empty_squares.clone());
			if (sudokupair.getValOne() > 0)
				return sudokupair;
		}
		return sudokupair;
	}

	/**
	 * A recursive solver that looks for multiple solutions in the passed puzzle
	 * it returns either 0 if there are no solutions, 1 if there is one solution
	 * and 2 if there is more than 1 solution.
	 * @param sudoku (SudokuPuzzle) the puzzle object being worked on
	 * @return (int) returns 2, 1 or 0, 2 if there are multiple solutions 
	 * 1 if there is a solution, 0 if there is none
	 */
	public static int specialSolver(SudokuPuzzle sudoku, ArrayList<Integer> empty_squares) {
		if (empty_squares.size() == 0) {
			return 1;
		}
		empty_squares_Qsort(sudoku, empty_squares, 0, empty_squares.size() - 1);
		int current_square = empty_squares.get(0);
		int[] allowed_nums = sudoku.getAllowedNums(current_square % 9, current_square / 9);
		if (allowed_nums.length == 0)
			return 0;
		empty_squares.remove(0);
		int solution_count = 0;
		for (int i = 0; i < allowed_nums.length; i++) {
			SudokuPuzzle cloned_sudoku = sudoku.clone();
			cloned_sudoku.setSquare(current_square % 9, current_square / 9, allowed_nums[i]);
			solution_count += specialSolver(cloned_sudoku, (ArrayList<Integer>) empty_squares.clone());
			if (solution_count >= 2)
				return 2;
		}
		return solution_count;
	}
	
	// QUICKSORT and helper methods
	
	/**
	 * @param sudoku (SudokuPuzzle) the puzzle object being worked on
	 * @param empty_squares (ArrayList<Integer>) a list of empty squares on the grid
	 * @param index (int) location on the grid
	 * @return (int) the number of possible numbers allowed at the designated location of the grid
	 */
	public static int allowedNums(SudokuPuzzle sudoku, ArrayList<Integer> empty_squares, int index) {
		int current_square = empty_squares.get(index);
		int[] allowed_nums_min = sudoku.getAllowedNums(current_square % 9, current_square / 9);
		return allowed_nums_min.length;
	}
	
	/**
	 * @param sudoku (SudokuPuzzle) the puzzle object being worked on
	 * @param empty_squares (ArrayList<Integer>) a list of empty squares on the grid
	 * @param lo
	 * @param hi
	 */
	public static void empty_squares_Qsort(SudokuPuzzle sudoku, ArrayList<Integer> empty_squares, int lo, int hi) {
		if (lo < hi) {
			int p = Qsort_part(sudoku, empty_squares, lo, hi);
			empty_squares_Qsort(sudoku, empty_squares, lo, p);
			empty_squares_Qsort(sudoku, empty_squares, p + 1, hi);
		}
	}
	
	/**
	 * @param sudoku (SudokuPuzzle) the puzzle object being worked on
	 * @param empty_squares (ArrayList<Integer>) a list of empty squares on the grid
	 * @param lo
	 * @param hi
	 * @return
	 */
	public static int Qsort_part(SudokuPuzzle sudoku, ArrayList<Integer> empty_squares, int lo, int hi) {
		int pivot = allowedNums(sudoku, empty_squares, lo);
		int i = lo - 1;
		int j = hi + 1;
		while (true) {
			i += 1;
			while (allowedNums(sudoku, empty_squares, i) < pivot)
				i += 1;
			j -= 1;
			while (allowedNums(sudoku, empty_squares, j) > pivot)
				j -= 1;
			if (i >= j)
				return j;
			int tmp = empty_squares.get(i);
			empty_squares.set(i, empty_squares.get(j));
			empty_squares.set(j, tmp);
		}
	}
}
