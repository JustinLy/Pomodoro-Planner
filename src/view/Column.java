package view;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;

public class Column extends JPanel implements Scrollable {

	JLabel label;
	int day;
	
	/**
	 * Creates a new Column object for the specified day.
	 * @param day - Day you want to create this Column for. Should be constants TODAY, TOMORROW, or DAY_AFTER
	 */
	public Column( int day ) {
		switch(day) {
		case 0: { //Today
			
		}
		}
	}
	/**Scrollable was implemented so we could set getScrollableTracksViewportWidth()
	 * to return "true", to keep the enclosing JScrollPane set to this column's width.
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
