package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import model.WorkSession;
import view.WorkView;

public class WorkController {
/**This class is responsible for processing user interactions between the WorkView and the WorkSession model */
	WorkView workView;
	WorkSession workSession;
	public WorkController( WorkView workView, WorkSession workSession ) {
		this.workView = workView;
		this.workSession = workSession;
		registerWorkComponents(); //Register listeners to the components of WorkView
	}
	
	/**
	 * Creates an array of EventListeners containing all one of every listener in the ScheduleController class
	 * and registers the listeners to the correct components in WorkView
	 */
	public void registerWorkComponents( ) {
		EventListener[] listeners = new EventListener[2];
		listeners[0] = new PauseListener();
		listeners[1] = new ResetListener();
		workView.registerWorkListeners(listeners);
	}
	
	class PauseListener implements ActionListener{
		/**Handles user request (from WorkView) to pause the current Pomodoro or Break and return to the ScheduleView */
		public void actionPerformed( ActionEvent e ) {
			workSession.pause(); //Pause the timer for current Pomodoro or Break
			workView.setVisible(false); //Hide the WorkView (will trigger schedule view to re-open)
		}
	}
	
	class ResetListener implements ActionListener{
		/**Handles user request (from WorkView) to reset the current Pomodoro or Break's timer (to its original value) */
		public void actionPerformed( ActionEvent e ) {
			workSession.reset();
		}
	}
}
