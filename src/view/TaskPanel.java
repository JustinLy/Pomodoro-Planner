package view;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class TaskPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel taskName;
    JTextField length;
     int size;
     int day;
    
    public TaskPanel( )
    {
        
       
      //  this.taskName.setBorder( BorderFactory.createLineBorder( Color.BLACK ));
        //this.length.setBorder( BorderFactory.createLineBorder( Color.BLACK));
        setBorder( BorderFactory.createLineBorder( Color.YELLOW ));
        taskName = new JLabel("<html>tunak tunak tuntunak tunak tuntunaksdfsdfsdfsdfsdf sdfsdfsdfsdfdsfdfsdfsdfsdfsdfsdfsdfsdf</html>");
        taskName.setAlignmentY(CENTER_ALIGNMENT);
        length = new JTextField(2);
 
        length.setText("2");
        length.setAlignmentY(CENTER_ALIGNMENT);
  
      setLayout( new BorderLayout() );
 
       
       
        add( taskName, BorderLayout.CENTER );
        add( length, BorderLayout.EAST);
      
        setMaximumSize( getPreferredSize() );
    }
    

    
    public Dimension getPreferredSize( )
    {
        return new Dimension( getMaximumSize().width, 50 );
    }
    
}
