package tests;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import javax.swing.JFrame;

import model.*;
public class TestWorkSession {

	
	public void run() {
	
		WorkSession work = new WorkSession();
		Queue<Task> tasks = new LinkedList();
		tasks.add( new Task( "MP5", 1));
		tasks.add( new Task("tor", 1));
		tasks.add( new Task( "do stuff", 1));
		
		//configure settings to make it easier for testing
		work.getSettings().setPomLength(1);
		work.getSettings().setShortBreak(1);
		work.getSettings().setLongBreak(2);
		work.getSettings().setPomsForLongBreak(3);
		
		TestObserver obs = new TestObserver();
		work.addObserver(obs);
		work.workOnTasks(tasks);
		
	}
	
	public static void main( String[] args ) {
		JFrame theFrame = new JFrame();
		 theFrame.setSize(500,400);
	        theFrame.setLocationRelativeTo( null );
	        theFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	        theFrame.setVisible(true);
		TestWorkSession blah = new TestWorkSession();
		blah.run();
		
	}
	
	class TestObserver implements Observer {

		@Override
		public void update(Observable arg0, Object arg1) {
			if( !(arg0 instanceof WorkSession))
				throw new IllegalArgumentException();
			else {
				WorkSession work = (WorkSession) arg0;
				Settings settings = work.getSettings();
				if( work.getStateName() == WorkSession.StateName.POMODORO || 
						work.getStateName() == WorkSession.StateName.BREAK) {
				TimeState state = (TimeState) work.getState();
				System.out.println( work.getCurrentTask().getTaskName() + "\t");
				System.out.println( state.getMinutes() + ":" + state.getSeconds() + "\t");
				
				if(work.getStateName() == WorkSession.StateName.POMODORO ) 
					System.out.println( "Pomodoro" + "\t" );
				
				else if( work.getStateName() == WorkSession.StateName.BREAK ) {
					if( state.getOriginalTime() == settings.getShortBreak())
						System.out.println( "Short" + "\t" );
					else if( state.getOriginalTime() == settings.getLongBreak() )
						System.out.println( "Long" + "\t" );
				}
				System.out.println( "\n");
				}
				if( work.getStateName() == WorkSession.StateName.TASKDONE) 
					System.out.println( work.getCurrentTask().getTaskName() + " complete!");
				if( work.getStateName() == WorkSession.StateName.ALLDONE)
					System.out.println( "DONE" );
				}
			}
		}
		
	}


