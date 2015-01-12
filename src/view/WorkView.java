package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import model.Settings;
import model.Task;
import model.TimeState;
import model.WorkSession;
import model.WorkSession.StateName;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;

public class WorkView implements Observer {

	private final String BLANKSPACE = ("________________");
	private FocusFrame frame;
	private JLabel currentTask = new JLabel();
	private JLabel progress = new JLabel();
	private JButton resetButton = new JButton("Reset");
	private JLabel currentTime = new JLabel();
	private JLabel pomsTillLongBreak = new JLabel();
	private JButton pauseButton = new JButton("Pause / Return to Schedule Window");

/**
 * Makes the window visible
 */
public void run() {
		
		this.frame.pack();
		this.frame.setVisible(true);
	}
	
	/**
	 * Create the application.
	 */
	public WorkView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new FocusFrame();
		frame.setBounds(100, 100, 493, 248);
	//	frame.setPreferredSize(new Dimension(650, 450));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[][][][grow][][grow][grow][][][grow][]", "[grow][grow][grow][grow][grow][grow][grow]") );
		
		//Task Name
		currentTask.setText("Tunak tunak tun");
		currentTask.setFont(new Font("Tahoma", Font.BOLD, 26));
		currentTask.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		progress.setText("4 / 5 Pomodoros Completed");
		frame.getContentPane().add(progress, "cell 6 2,alignx center");
		resetButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		resetButton.setBackground(Color.GREEN);
		resetButton.setContentAreaFilled(false);
		resetButton.setOpaque(true);
		frame.getContentPane().add(resetButton, "flowx,cell 3 4");
		
		currentTime.setText("23:00");
		currentTime.setFont(new Font("Tahoma", Font.BOLD, 30));
		currentTime.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(currentTask, "cell 6 0,alignx center,aligny top,grow");
		frame.getContentPane().add(currentTime, "cell 6 4,growx,alignx center");
		
		pomsTillLongBreak.setText("3 / 5 till Long Break");
		frame.getContentPane().add(pomsTillLongBreak, "cell 9 4");
		pauseButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		pauseButton.setBackground(Color.RED);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setOpaque(true);
		frame.getContentPane().add( pauseButton, "cell 6 6");
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if( arg0.getClass() != WorkSession.class) {
			throw new IllegalArgumentException( "Error receiving WorkSession data into view\n");
		}
		else {
			WorkSession session = (WorkSession) arg0;
			StateName state = session.getStateName();
			Task task = session.getCurrentTask();
		
			switch( state ) { //Displays information on screen depending on the model's State
			case POMODORO: 
				setIsMessage(false); //Not a message;
				TimeState taskTime = (TimeState) session.getState(); //We know it's a Pomodoro state so we can cast TimeSTate
				currentTask.setText(task.getTaskName()); //Display current Task
				progress.setText(session.getPomsCompleted() +  " / " + task.getTaskLength() + " Pomodoros Completed" );
				currentTime.setText(taskTime.toString());
				if( session.getPomsTillLong() == 0 ) //Long break already happened so display full # of poms for long break
					pomsTillLongBreak.setText(session.getSettings().getPomsForLongBreak() + " more till Long Break");
				else
					pomsTillLongBreak.setText(session.getPomsTillLong() + " more till Long Break");
				break;
				
			case BREAK :
				setIsMessage(false); //not a message;
				TimeState breakTime = (TimeState) session.getState(); //We know it's a Break state so we can cast TimeSTate
				currentTime.setText(breakTime.toString());
				progress.setText(BLANKSPACE); //Nothing to display for progress of a break
				pomsTillLongBreak.setText(BLANKSPACE); //Nothing to display for a break
				if( session.getPomsTillLong() == 0 )
					currentTask.setText( "Long Break" );
				else
					currentTask.setText( "Short Break" );
				break;
				
			case TASKDONE:
				setIsMessage(true);
				progress.setText(BLANKSPACE); //Nothing to display 
				pomsTillLongBreak.setText(BLANKSPACE); //Nothing to display
				currentTask.setText( task.getTaskName() + " Complete");
				TimeState taskMsgTime = (TimeState) session.getState(); //test
				break;
				
			case ALLDONE:
				setIsMessage(true);
				currentTask.setText( "All Tasks Today Completed!");
				break;
				
			default: break;
			}
			
			if( session.stateChanging()) { //Make window up up to alert users when changing states (ie from Pomodoro to Break)
				frame.toFront();
				if( state == WorkSession.StateName.ALLDONE)
					frame.setVisible(false); //Hide the WorkView, trigger listener to return to ScheduleView
			}
			
			frame.revalidate();
			frame.repaint();	
		}
	}
	
	/**Sets schedule view visibility, for when WorkView is on */
	public void setVisible( boolean visible) {
		frame.setVisible(visible);
	}

	/**
	 * Registers the ReopenListener to this WorkView, to re-open the ScheduleView when WorkView closes
	 */
	public void registerReopenListener(ComponentListener listener) {
		frame.addComponentListener(listener);
	}
	
	/**
	 * Adds ALL the required EventListeners from the WorkController to the components in this WorkView
	 */
	public void registerWorkListeners( EventListener[] listeners) {
		pauseButton.addActionListener((ActionListener) listeners[0]);
		resetButton.addActionListener((ActionListener) listeners[1]);
	}
	
	/**Helper method to hide buttons and unneeded labels during Task Completion and Todo-list Completion messages*/
	private void setIsMessage( boolean message) {
		resetButton.setVisible(!message);
		pauseButton.setVisible(!message);
		progress.setVisible(!message);
		pomsTillLongBreak.setVisible(!message);
	}
	
}
