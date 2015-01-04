package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;



public class Space extends JPanel {
	
	    
	   public Dimension getPreferredSize()
	   {
	       return new Dimension( getMaximumSize().width, 35);
	   }
	   
	   public Space( )
	   {
	       
	      setMaximumSize( getPreferredSize() );
	       setBackground( Color.LIGHT_GRAY);
	   }
	

}
