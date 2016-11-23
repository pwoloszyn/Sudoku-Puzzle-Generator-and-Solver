import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * The core class of the application where both the GUI
 * and the primary features are defined. 
 * @author Piotr Woloszyn
 *
 */
public class SudokuWindow extends JFrame implements ActionListener {

	// GUI variables.
	JPanel puzzle_panel;
	JPanel button_panel;
	JPanel message_panel;
	JTextField message_field;
	GridBagConstraints gbc;
	
	// The font variable for the grid cells.
	NumberFormat numf;
	
	// An array storing 9 sub panels of the grid.
	ArrayList<JPanel> subpanels = new ArrayList<JPanel>();
	// An array storing all the 81 cells of the grid.
	ArrayList<JFormattedTextField> textfields = new ArrayList<JFormattedTextField>();
	
	// SUdoku puzzle variable, stores the generated puzzle.
	SudokuPuzzle sudoku;
	
	// This variable determines which Validator to use.
	boolean generated_sudoku = false;

	/**
	 * The constructor, just sets up the frame name and runs the
	 * buildWindow() method.
	 */
	public SudokuWindow() {
		super("SUDOKU");
		buildWindow();
	}

	/**
	 * Builds the GUI and initializes all of the
	 * features of the application.
	 */
	public void buildWindow() {
		this.getContentPane().setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		this.setSize(420, 500);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		puzzle_panel = new JPanel();
		puzzle_panel.setBackground(Color.BLACK);
		puzzle_panel.setLayout(new GridBagLayout());

		button_panel = new JPanel();
		button_panel.setBackground(Color.WHITE);
		
		message_panel = new JPanel();
		message_panel.setBackground(Color.WHITE);

		for (int i = 0; i < 9; i++) {
			JPanel sjp = new JPanel();
			sjp.setLayout(new GridBagLayout());
			sjp.setBackground(Color.BLACK);
			subpanels.add(sjp);
		}

		gbc = new GridBagConstraints();

		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 30, 10, 30);
		this.add(puzzle_panel, gbc);

		gbc.gridy = 1;
		gbc.weighty = 0.2;
		this.add(button_panel, gbc);
		
		gbc.gridy = 2;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		this.add(message_panel, gbc);
		
		numf = NumberFormat.getIntegerInstance();
		numf.setMaximumIntegerDigits(1);

		for (int i = 0; i < 81; i++) {
			JFormattedTextField ftf = new JFormattedTextField(numf) {
				protected void processFocusEvent(FocusEvent e) {
					if (e.getID() == FocusEvent.FOCUS_LOST) {
						if (getText() == null || getText().isEmpty()) {
							setValue(null);
						}
					} else if (e.getID() == FocusEvent.FOCUS_GAINED) {
						if (getText() != null && !getText().isEmpty()) {
							SwingUtilities.invokeLater(new Runnable() {	// Wait for the formatter to finish 
								public void run() {
									setCaretPosition(1);
								}
							});
						}
					}
					super.processFocusEvent(e);
				}
			};
			ftf.setFont(new Font("Arial", Font.BOLD, 20));
			ftf.setForeground(Color.BLUE);
			ftf.setEditable(true);
			ftf.setHorizontalAlignment(SwingConstants.CENTER);
			ftf.addKeyListener(new LocalKeyListerner(ftf, i, textfields));
			textfields.add(ftf);
		}

		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		for (int i = 0; i < 9; i++) {
			createSubSquare(i);
			puzzle_panel.add(subpanels.get(i), gbc);
			gbc.gridx += 1;
			if ((i + 1) % 3 == 0) {
				gbc.gridy += 1;
				gbc.gridx = 0;
			}
		}

		JButton btn1 = new JButton("Generate");
		btn1.setToolTipText("Generates a Random Sudoku");
		btn1.setBackground(Color.WHITE);
		btn1.addActionListener(this);
		btn1.setActionCommand("generate");
		button_panel.add(btn1);

		JButton btn2 = new JButton("Solve");
		btn2.setToolTipText("Displays the solution to the current sudoku puzzle");
		btn2.setBackground(Color.WHITE);
		btn2.addActionListener(this);
		btn2.setActionCommand("solve");
		button_panel.add(btn2);

		JButton btn3 = new JButton("Validate");
		btn3.setToolTipText("Checks if the inputed puzzle is valid (unique solution and >16 numbers) or if the "
				+ "numbers provided for an already existing puzzle are valid");
		btn3.setBackground(Color.WHITE);
		btn3.addActionListener(this);
		btn3.setActionCommand("validate");
		button_panel.add(btn3);

		JButton btn4 = new JButton("Clear");
		btn4.setToolTipText("If there is input present on the grid it clears it, and if there is non it clears the whole puzzle");
		btn4.setBackground(Color.WHITE);
		btn4.addActionListener(this);
		btn4.setActionCommand("clear");
		button_panel.add(btn4);
		
		message_field = new JTextField("...");
		message_field.setForeground(Color.BLUE);
		message_field.setBackground(Color.WHITE);
		message_field.setHorizontalAlignment(JTextField.CENTER);
		message_field.setColumns(25);
		message_field.setEditable(false);
		message_panel.add(message_field);

		this.setVisible(true);
	}

	/**
	 * This method assigns grid cells to one of the 9 subpanels.
	 * @param pos (int) Position of the subpanel in the subpanel array.
	 */
	public void createSubSquare(int pos) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				subpanels.get(pos).add(textfields.get(j + ((pos%3)*3) + (i*9) + ((pos/3)*27)), gbc);
				gbc.gridx += 1;
			}
			gbc.gridy += 1;
			gbc.gridx = 0;
		}
	}

	/**
	 * This method clears all the cells on the grid.
	 */
	public void clearGrid() {
		for (JFormattedTextField txtfld : textfields) {
			txtfld.setForeground(Color.BLUE);
			txtfld.setEditable(true);
			txtfld.setText(null);
		}
		generated_sudoku = false;
	}
	
	/**
	 * This method removes all the inputed numbers from the grid.
	 * @return (boolean) true if there was any input on the grid, else return false.
	 */
	public boolean clearGridOfInput() {
		boolean any_input = false;
		for(int i=0; i<81; i++) {
			JFormattedTextField txtfld = textfields.get(i);
			if(!txtfld.getText().isEmpty()) {
				if(txtfld.isEditable()) {
					any_input = true;
					txtfld.setText(null);
				}
			}
		}
		return any_input;
	}
	
	/**
	 * This method solves the given sudoku puzzle
	 * 
	 */
	public void solveSudoku() {
		SudokuPuzzle solved_sudoku = new SudokuPuzzle();
		Pair<Integer, SudokuPuzzle> sudokupair = SolverModule.solver(sudoku.clone(), sudoku.getEmptySquares());
		solved_sudoku = sudokupair.getValTwo();
		for(int i=0; i<81; i++) {
			JFormattedTextField txtfld = textfields.get(i);
			if(txtfld.getText().isEmpty()) {
				txtfld.setForeground(Color.BLUE);
				txtfld.setEditable(true);
				txtfld.setText(solved_sudoku.getNumberAt(i%9, i/9)+"");
			}
		}
	}
	
	/**
	 * Sets the font of the empty cells to blue.
	 */
	public void setForgroundOfEmptyFields() {
		for(int i=0; i<81; i++) {
			JFormattedTextField txtfld = textfields.get(i);
			if(txtfld.getText().isEmpty()) {
				txtfld.setForeground(Color.BLUE);
			}
		}
	}
	
	/**
	 * Generates a valid sudoku puzzle.
	 */
	public void generateSudoku() {
		clearGrid();
		SudokuGen sudoku_generator = new SudokuGen();
		sudoku = sudoku_generator.generateSudokuPuzzle();
		fillGrid();
		generated_sudoku = true;
	}
	
	/**
	 * This method checks to see if the inputed puzzle is valid.
	 * @return (int) Returns 0 if puzzle is valid; 1 if it's invalid;
	 * 2 if there is too few clues; 3 if there are multiple solutions.
	 */
	public int validatePuzzle() {
		SudokuPuzzle tmp_puzzle = new SudokuPuzzle();
		ArrayList<Integer> empty_squares = new ArrayList<Integer>();
		int count = 0;
		for(int i=0; i<81; i++) {
			JFormattedTextField txtfld = textfields.get(i);
			if(!txtfld.getText().isEmpty()) {
				count += 1;
				if(!tmp_puzzle.setSquare(i%9, i/9, Integer.parseInt(txtfld.getText())))
					return 1;
			} else {
				empty_squares.add(i);
			}
		}
		if(count < 17)
			return 2;
		int solutions = SolverModule.specialSolver(tmp_puzzle, empty_squares);
		if(solutions == 1) {
			sudoku = tmp_puzzle;
			fillGrid();
			return 0;
		}
		return 3;
	}
	
	/**
	 * This method checks whether the imputed numbers 
	 * are valid or if the puzzle is completed.
	 * @return (int) Returns 0 if there are invalid placements;
	 * returns 1 if all the placements are valid; returns 2 if
	 * the puzzle is completed.
	 */
	public int validateMoves() {
		int is_valid = 1;
		SudokuPuzzle tmppuzzle = sudoku.clone();
		int spacescount = 0;
		for(int i=0; i<81; i++) {
			JFormattedTextField txtfld = textfields.get(i);
			if(!txtfld.getText().isEmpty()) {
				spacescount += 1;
				if(txtfld.isEditable()) {
					if(!tmppuzzle.setSquare(i%9, i/9, Integer.parseInt(txtfld.getText()))) {
						textfields.get(i).setForeground(Color.RED);
						is_valid = 0;
					}
				}
			}
		}
		if(spacescount == 81)
			is_valid = 2;
		return is_valid;
	}

	/**
	 * This methods fills the cells of the grid with
	 * the numbers stored in the sudoku variable; that is,
	 * it puts the puzzle on the grid.
	 */
	public void fillGrid() {
		for(int i = 0; i <81; i++) {
			int num = sudoku.getNumberAt(i%9, i/9);
			if (num != 0) {
				textfields.get(i).setForeground(Color.BLACK);
				textfields.get(i).setText(num + "");
				textfields.get(i).setEditable(false);
			}
		}
	}
	
	/**
	 * This method sets the content and the color
	 * of the message displayed at the bottom
	 * of the GUI.
	 * @param color (Color)
	 * @param message (String)
	 */
	public void setMessageField(Color color, String message) {
		message_field.setForeground(color);
		message_field.setText(message);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String inp = e.getActionCommand();
		// If the 'Generate' button is pressed generate a new puzzle
		// and display it on the frame.
		if (inp == "generate") {
			generateSudoku();
			setMessageField(Color.BLACK, "Number Of Clues: "+sudoku.getNumOfClues());
		// If the 'Solve' button is pressed solve
		// the puzzle displayed and fill in the
		// empty spaces with numbers from the solution.
		} else if (inp == "solve") {
			if(generated_sudoku) {
				solveSudoku();
				setMessageField(Color.BLUE, "Peace Of Cake");
			} else {
				setMessageField(Color.RED, "No Valid Sudoku On The Grid");
			}
		// If the 'Validate' button is pressed do one of two things.
		} else if (inp == "validate") {
			// If a puzzle has already been generated, check if
			// the inputed numbers are in valid locations.
			if(!generated_sudoku) {
				int puzzlestatus = validatePuzzle();
				if(puzzlestatus == 0) {
					setMessageField(Color.BLUE, "Puzzle Is Valid");
				} else if (puzzlestatus == 1){
					setMessageField(Color.RED, "Puzzle Is Invalid");
				} else if (puzzlestatus == 2) {
					setMessageField(Color.RED, "Puzzle Has Too Few Numbers");
				} else {
					setMessageField(Color.RED, "Puzzle Has Multiple Solutions");
				}
			// If a puzzle has not been generated yet, check if
			// the current set of number on the grid constitutes
			// a valid puzzle, if it does convert the numbers into
			// a generated puzzle.
			} else {
				int puzzlestatus = validateMoves();
				if(puzzlestatus == 1) {
					setMessageField(Color.BLUE, "Good So Far");
				} else if (puzzlestatus == 0){
					setMessageField(Color.RED, "Wrong Placement!");
				} else if (puzzlestatus == 2) {
					setMessageField(Color.BLUE, "Well Done!");
				}
			}
		// If the 'Validate' button is pressed do one of two things.
		} else if (inp == "clear") {
			setMessageField(Color.BLUE, "...");
			// If there are input numbers present (blue),
			// clear only them.
			if(!clearGridOfInput()){
				// Else remove all numbers from the grid.
				clearGrid();
			}
		}
	}
}
