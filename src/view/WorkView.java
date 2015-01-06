package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ComponentListener;
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

public class WorkView implements Observer {

	final String BLANKSPACE = ("________________");
	private FocusFrame frame;
	JLabel currentTask = new JLabel();
	JLabel progress = new JLabel();
	JButton resetButton = new JButton("Reset");
	JLabel currentTime = new JLabel();
	JLabel pomsTillLongBreak = new JLabel();
	JButton pauseButton = new JButton("Pause / Return to Schedule Window");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WorkView window = new WorkView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		frame.getContentPane().setLayout(new MigLayout("", "[][][][][][][grow][][][][]", "[][][][][][][]") );
		
		//Task Name
		currentTask.setText("Tunak tunak tun");
		currentTask.setFont(new Font("Tahoma", Font.BOLD, 26));
		currentTask.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		progress.setText("4 / 5 Pomodoros Completed");
		frame.getContentPane().add(progress, "cell 6 2,alignx center");
		
		frame.getContentPane().add(resetButton, "flowx,cell 3 4");
		
		currentTime.setText("23:00");
		currentTime.setFont(new Font("Tahoma", Font.BOLD, 30));
		currentTime.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(currentTask, "cell 6 0,alignx center,aligny top,grow");
		frame.getContentPane().add(currentTime, "cell 6 4,growx,alignx center");
		
		pomsTillLongBreak.setText("3 / 5 till Long Break");
		frame.getContentPane().add(pomsTillLongBreak, "cell 9 4");
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
				pomsTillLongBreak.setText(session.getPomsTillLong() + " more till Long Break");
				
				if( taskTime.getMillis() == 0 )
					frame.toFront(); //Make window pop up to notify user
				
				frame.revalidate();
				frame.repaint();
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
				
				if( breakTime.getMillis() == 0 )
					frame.toFront(); //Make window pop up to notify user
				
				frame.revalidate();
				frame.repaint();
				break;
				
			case TASKDONE:
				setIsMessage(true);
				progress.setText(BLANKSPACE); //Nothing to display 
				pomsTillLongBreak.setText(BLANKSPACE); //Nothing to display
				currentTask.setText( task.getTaskName() + " Complete");
				TimeState doneTime = (TimeState) session.getState(); //test
				
				if( doneTime.getMillis() == WorkSession.MESSAGEDELAY )
					frame.toFront(); //Make window pop up to notify user
				
				frame.toFront(); //Make window pop up to notify user
				frame.revalidate();
				frame.repaint();
				/*try {
					Thread.sleep(1000); //Pauses for 1 second so user can see the "Task Complete" message
				} catch (InterruptedException e1) {
					System.out.println( "Error: TASKDONE"); //TODO: replace with error dialog later.
				} */
				break;
				
			case ALLDONE:
				setIsMessage(true);
				JOptionPane pane = new JOptionPane("All tasks for today complete!", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}); 
				JDialog doneMessage = pane.createDialog("Done");
				doneMessage.toFront();
				frame.revalidate();
				frame.repaint();
				/*try {
					Thread.sleep(2000); //Pause for 2 seconds so user can see the message
				} catch (InterruptedException e) {
					System.out.println( "Error: ALLDONE"); //TODO: replace with error dialog later
				} */
				doneMessage.dispose(); //Get rid of the message
				frame.setVisible(false); //Hide the WorkView
				break;
				
			default: break;
			}
		
			

			
			
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
	
	/**Helper method to hide buttons and unneeded labels during Task Completion and Todo-list Completion messages*/
	private void setIsMessage( boolean message) {
		resetButton.setVisible(!message);
		pauseButton.setVisible(!message);
		progress.setVisible(!message);
		pomsTillLongBreak.setVisible(!message);
	}
	
}
