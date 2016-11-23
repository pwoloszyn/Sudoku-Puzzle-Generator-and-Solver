import java.util.ArrayList;
import java.util.Random;

/**
 * This class is responsible for generating sudoku puzzle grid.
 * To generate a puzzle run the generateSudokuPuzzle() method, and it will
 * return one.
 * @author Piotr Woloszyn
 *
 */
public class SudokuGen {
	
	// The random number generator object
	private Random rnd;
	// The number of clues in the generated puzzle
	private int num_of_clues;

	/**
	 * The constructor.
	 */
	public SudokuGen() {
		// Nothing here
	}
	
	/**
	 * This method is the main means of generating a sudoku puzzle from
	 * outside the class. Firstly a new random number generator is initialized,
	 * secondly a new empty sudoku puzzle object is created, thirdly a completed
	 * sudoku puzzle is made via the generateCompletedPuzzle() method, fourthly
	 * the puzzle is slowly trimmed of numbers until no more numbers can be removed
	 * without affecting the uniqueness of the solution to the puzzle. Finally
	 * the generated puzzle is returned.
	 * @return (SudokuPuzzle) A ready sudoku puzzle
	 */
	public SudokuPuzzle generateSudokuPuzzle() {
		rnd = new Random();
		SudokuPuzzle sudoku = new SudokuPuzzle();
		generateCompletedPuzzle(sudoku);
		trimPuzzle(sudoku, 17);
		sudoku.setNumOfClues(num_of_clues);
		return sudoku;
	}

	/**
	 * This method creates a solved sudoku puzzle. It works by generating a row of numbers
	 * and then trying to find a random row of numbers that doesn't conflict with the
	 * Preceding ones, if it fails to do so in 10 attempts the previous row is remade.
	 * @param sudoku (SudokuPuzzle) the sudoku puzzle object being worked on
	 */
	private void generateCompletedPuzzle(SudokuPuzzle sudoku) {
		int[] allowed_nums;
		int row_attempts_count = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				allowed_nums = sudoku.getAllowedNums(j, i);
				if (allowed_nums.length > 0) {
					sudoku.setSquare(j, i, allowed_nums[rnd.nextInt(allowed_nums.length)]);
				} else {
					row_attempts_count += 1;
					sudoku.clearRow(i);
					if (row_attempts_count > 10) {
						row_attempts_count = 0;
						i -= 2;
						sudoku.clearRow(i + 1);
					} else {
						j = -1;
					}
				}
			}
		}
	}

	/**
	 * is the main puzzle generator method Creates an array of numbers within
	 * the specified range and then randomizes the order of those numbers in
	 * that array before returning it.
	 * @param from (int) the start of the range - inclusive
	 * @param to (int) the end of the range - inclusive
	 * @return (int[]) and array of numbers in random order
	 */
	private int[] getRandomSequence(int from, int to) {
		rnd.setSeed(System.currentTimeMillis());
		int sequence_array_size = to - from + 1;
		int[] sequence = new int[sequence_array_size];
		for (int i = 0; i < sequence_array_size; i++) {
			sequence[i] = i + from;
		}
		// Randomize!
		for (int i = 0; i < sequence_array_size; i++) {
			int random_num = rnd.nextInt(to);
			int tmp = sequence[i];
			sequence[i] = sequence[random_num];
			sequence[random_num] = tmp;
		}
		return sequence;
	}

	/**
	 * Removes clues at random from the puzzle, after each number is removed it
	 * calls the special solver to see if the puzzle still has a unique
	 * solution, if it doesn't it puts the number back and takes another one out
	 * in the sequence.
	 * @param sudoku (SudokuPuzzle) the puzzle object being worked on
	 * @param min_clues (int) specifies the minimum number of clues in the puzzle
	 */
	private void trimPuzzle(SudokuPuzzle sudoku, int min_clues) {
		int[] random_num_sequence = getRandomSequence(0, 80);
		ArrayList<Integer> empty_squares = new ArrayList<Integer>();
		num_of_clues = 81;
		int min_clues_allowed = (min_clues > 17) ? min_clues : 17;

		for (int i = 0; i < 81; i++) {
			int x = random_num_sequence[i] % 9;
			int y = random_num_sequence[i] / 9;
			if (sudoku.getNumberAt(x, y) != 0) {
				int tmp = sudoku.clearSquare(x, y);
				empty_squares.add(random_num_sequence[i]);
				if (SolverModule.specialSolver(sudoku.clone(), (ArrayList<Integer>) empty_squares.clone()) == 2) {
					sudoku.setSquare(x, y, tmp);
					empty_squares.remove(empty_squares.size() - 1);
				} else {
					--num_of_clues;
					if (num_of_clues == min_clues_allowed)
						i = 81;
				}
			}
		}
	}
}
