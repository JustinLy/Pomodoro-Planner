package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class Column extends JPanel implements Scrollable {

	private JLabel label;
	private int day;
	private Color color;
	private static int focusedColumn = 0; //Column being focused for adding tasks
	private static boolean safeToChange = true; //A (hopefully) temporary hack to fix the AddTask bug.
	
	/**
	 * Creates a new Column object for the specified day.
	 * @param day - Day you want to create this Column for. Should be constants TODAY, TOMORROW, or DAY_AFTER
	 */
	public Column( int day ) {
		this.day = day;
		//Initialize the JLabel for this Column
		switch(day) {
		case 0: { //Today
			label = new JLabel("Today");
			color = (new Color(255, 255, 0));
			break;
		}
		case 1: { //Tomorrow
			label = new JLabel("Tomorrow");
			color = new Color(255, 0, 0) ;
			break;
		}
		case 2: { //Day After 
			label = new JLabel( "Day After" );
			color =  new Color(30, 144, 255);
			break;
		}
		default: throw new IllegalArgumentException( "Must input TODAY, TOMORROW, or DAY_AFTER");
		}
		
		label.setOpaque(true);
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBackground(color);
		
		//Listener for focusing column when clicked
		label.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent e ) {
				label.requestFocusInWindow();
			}
		});
		
		//Removes the highlighting, if focus lost. Returns "focusedColumn" to default value "TODAY".
		label.addFocusListener( new FocusAdapter() {
			public void focusLost( FocusEvent e ) {
				//This terrible line of code un-highlights the JScrollPane
				if( safeToChange) {
					((JComponent) ( (JViewport)Column.this.getParent() ).getParent()).setBorder( BorderFactory.createLineBorder(Color.BLACK));
					Column.focusedColumn = 0; //reset to default today
					System.out.println( "reset to today");
				}
			}
			
			public void focusGained( FocusEvent e ) {
				//This terrible line of code here, highlights the JScrollPane (because dif Columns will have uneven sizes, making uneven highlighted boxes)
				((JComponent) ( (JViewport)Column.this.getParent() ).getParent()).setBorder( BorderFactory.createLineBorder(color,3));
				Column.focusedColumn = Column.this.day;
				System.out.println( "set to " + Column.this.day);
			}
		});

	}
	
	/**Temporary hack method to preserve the focusedColumn when AddTask is clicked. Stops FocusLost event during Task adding*/
	public static void safeToChange( boolean answer) {
		safeToChange = answer; 
	}
	
	public JLabel getLabel() {
		return label;
	}

	public int getDay() {
		return day;
	}

	public static int getFocusedColumn() {
		return focusedColumn;
	}

	public Color getColor() {
		return color;
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
