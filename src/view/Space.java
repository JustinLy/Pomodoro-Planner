package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;



public class Space extends JPanel {
	
	    
	   public Dimension getPreferredSize()
	   {
	       return new Dimension( getMaximumSize().width, 35);
	   }
	   
	   public Space( )
	   {
	       
	      setMaximumSize( getPreferredSize() );
	     //  setBackground( Color.LIGHT_GRAY);
	       
	     //Focuses space when clicked. Helps with giving users more areas to click on for defocusing when editing tasks, etc
	        addMouseListener( new MouseAdapter() {
	        	public void mousePressed( MouseEvent e ) {
	        		requestFocusInWindow();
	          	}
	        });
	   }
	

}
