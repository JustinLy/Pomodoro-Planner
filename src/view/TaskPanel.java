package view;

import javax.swing.*; 

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TaskPanel extends JPanel
{
    /**
	 * TaskPanels are used to display a "Task" from the model. Displays their name and length (and whether or not it's "complete")
	 */
	private static final long serialVersionUID = 1L;
	private JLabel taskName;
    private JTextField length;
    private int size;
    private int day;
    private boolean editable = true;
    
    public TaskPanel(String name, int duration )
    {
    	size = duration;
        setBorder( BorderFactory.createLineBorder( Color.BLACK ));
        
        taskName = new JLabel("<html>" + name + "</html>");
        taskName.setAlignmentY(CENTER_ALIGNMENT);
        
        length = new JTextField(2);
        length.setText(Integer.toString(duration));
        length.setAlignmentY(CENTER_ALIGNMENT);
        //NOTE: need to make editing TaskLength part of EditTaskListener. But how? JTextField is INSIDE the task (unless I make a getter)
        //Maybe 2 separate Listeners? EditTask and EditLength. Make public method in Task class that adds them
  
      setLayout( new BorderLayout() );
      add( taskName, BorderLayout.CENTER );
        add( length, BorderLayout.EAST);
      
        setMaximumSize( getPreferredSize() );
        
        //Highlights Task with same color as its Column when clicked and focuses it. (purely for aesthetics)
        addMouseListener( new MouseAdapter() {
        	public void mousePressed( MouseEvent e ) {
        		requestFocusInWindow();
        		setBorder(BorderFactory.createLineBorder( ( (Column)getParent() ).getColor() ) );
        	}
        });
        
        //Sets the Task's border back to black when it loses focus (purely for aesthetics)
        addFocusListener( new FocusAdapter() {
        	public void focusLost( FocusEvent e ) {
        		setBorder(BorderFactory.createLineBorder(Color.BLACK));
        	}
        });
    }

    /**
     * Gets the "Task Length" component of this TaskPanel. 
     * @return - a JTextField containing the length displayed by this TaskPanel
     */
    public JTextField getLengthField() {
    	return length;
    }
    
    /**
     * Sets this TaskPanel to be "uneditable", so that its position and attributes cannot be changed by the user, except
     * by deleting the TaskPanel.
     */
    public void setUneditable() {
    	editable = false;
    	//TODO: add the strike-through effect on this JLabel
    }
    
    /**Returns true if this TaskPanel is editable, false otherwise */
    public boolean isEditable() {
    	return editable;
    }
    
    public Dimension getPreferredSize( )
    {
        return new Dimension( getMaximumSize().width, 40 + 10*size ); //Taller panel if longer length
    }
    
}
