package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import model.Settings;
import model.Task;
import model.TimeState;
import model.WorkSession;
import net.miginfocom.swing.MigLayout;

public class WorkView implements Observer {

	
	private JFrame frame;
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
		frame = new JFrame();
		frame.setBounds(100, 100, 493, 248);
		frame.setPreferredSize(new Dimension(650, 450));
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
			Task task = session.getCurrentTask();
			
			//Calculating the current time in task or break
			TimeState time = (TimeState) session.getState(); 
			String mins = Integer.toString( (int)time.getMinutes() );
			String secs = Integer.toString( (int)time.getSeconds() );
	
			if( mins.equals("0")) //Add extra 0 if min or sec is 0 (time display formatting)
				mins += "0";
			if( secs.equals(0))
				secs+= "0"; 
			
			//Calculating Pomodoros till long break pomsTillBreak
			Settings settings = session.getSettings();
			int temp = session.getLongBreakCount() % settings.getLongBreak()  ;
			int pomsTillBreak ;
			if( session.getLongBreakCount() > settings.getLongBreak() ) //Need to use "temp" and formula
				pomsTillBreak = session.getSettings().getLongBreak() - temp;
			else
				pomsTillBreak = settings.getLongBreak() - session.getLongBreakCount();
			
			
		
			currentTask.setText(task.getTaskName());
			progress.setText(session.getPomsCompleted() +  " / " + task.getTaskLength() + " Pomodoros Completed" );
			currentTime.setText(mins + ":" + secs);
			pomsTillLongBreak.setText( pomsTillBreak + " till Long Break");
		}
		
	}
	
	/**Sets schedule view visibility, for when WorkView is on */
	public void setVisible( boolean visible) {
		frame.setVisible(visible);
	}

}
