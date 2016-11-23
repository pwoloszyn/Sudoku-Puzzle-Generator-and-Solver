import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;

/**
 * This class provides the functionality of the KeyListener 
 * interface which is defined here to provide two features:
 * - Reseting the color of the font in the cell after the input
 * in it is deleted.
 * - keyboard control of the caret on the grid by the use of
 * the arrow keys.
 * 
 * @author Piotr Woloszyn
 *
 */
public class LocalKeyListerner implements KeyListener {
	
	JFormattedTextField ftf;
	int pos;
	ArrayList<JFormattedTextField> textfields;
	
	/**
	 * The constructor.
	 * @param ftf (JFormattedTextField)
	 * @param pos (int)
	 * @param textfields (ArrayList<JFormattedTextField>)
	 */
	public LocalKeyListerner(JFormattedTextField ftf, int pos, ArrayList<JFormattedTextField> textfields) {
		this.ftf = ftf;
		this.pos = pos;
		this.textfields = textfields;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// No code here
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// If backspace or delete is pressed clear the selected
		// square on the grid.
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
			ftf.setText(null);
			ftf.setForeground(Color.BLUE);
		}		
		// The following four conditions define arrow
		// key based movement on the grid
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(pos < 72)
				textfields.get(pos+9).requestFocusInWindow();
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(pos > 8)
				textfields.get(pos-9).requestFocusInWindow();
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(pos%9 > 0)
				textfields.get(pos-1).requestFocusInWindow();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(pos%9 < 8)
				textfields.get(pos+1).requestFocusInWindow();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// No code here
	}

}
