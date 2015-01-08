package view;

import javax.swing.*; 

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;

public class TaskPanel extends JPanel
{
    /**
	 * TaskPanels are used to display a "Task" from the model. Displays their name and length (and whether or not it's "complete")
	 */
	private static final long serialVersionUID = 1L;
	private JLabel taskName;
	private String name;
    private JTextField length;
    private int size;
    private boolean editable = true;
    
    public TaskPanel(String name, int duration )
    {
    	this.name = name; //Used in setUneditable 
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
      
        setBackground( Color.white);
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
     * by deleting the TaskPanel. Also puts a strikethrough on the font of this TaskPanel to indicate completion.
     */
    public void setUneditable() {
    	Font font = new Font("tahoma", Font.PLAIN, 12);
    	Map  attributes = font.getAttributes();
    	attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
    	Font newFont = new Font(attributes);
    	taskName.setText("<html><s>" + name + "</s></html>" ); //Put a strikethrough on the font to indicate completion
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
