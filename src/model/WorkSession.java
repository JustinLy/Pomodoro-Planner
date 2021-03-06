package model;

import java.util.Observable;
import java.util.Queue;

import javax.swing.Timer;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkSession extends Observable {
	public static final int MESSAGEDELAY = 2000; //Duration of delay for TASKDONE and ALLDONE states (for giving user alerts)
	
	private Settings settings = new Settings(); //time parameters for pomodoro lengths, breaks, etc
	private Queue<Task> taskList; //Tasks for today that still need to be completed
	private Task currentTask; 		//task currently being worked on
	private int pomsCompleted = 0; //poms completed for current task so far
	private int longBreakCount = 0; //Counter to keep track of when to give long break
	private WorkState state; //current state of the WorkSession
	private Timer timer = null; //timer to time the current pomodoro or break

	/**
	 * Loads the saved WorkSession from the data file and returns it, if it exists, else returns null
	 * @return the current saved WorkSession or a new empty WorkSession if it doesn't exist in the file
	 */
	public static WorkSession loadWorkSession() {
		ObjectContainer data = Db4oEmbedded.openFile(Db4oEmbedded
				 .newConfiguration(), "pomodorodata"); //Open the data file
   	
   	try { //Attempt to load data
			ObjectSet sessionData = data.queryByExample(WorkSession.class );
			
			if( sessionData.hasNext() ) { //Retrieve the WorkSession 
				WorkSession oldSession = (WorkSession) sessionData.next();
				oldSession.deleteObservers();
				//System.out.println(( (TimeState)oldSession.getState() ).toString() );
				return oldSession; 
			}
			else
				return new WorkSession(); //If no WorkSession saved on file		
	}
	finally {
				 data.close();
			}
	}
	
	/**
	 * Works on the given task list using the Pomodoro Technique continuously until
	 * "pause" is set by the user or all tasks in the list have been completed.
	 * @param taskList - Queue of tasks to complete 
	 * @requres taskList is not empty and contains no null tasks
	 */
	public void workOnTasks( Queue<Task> taskList ) {
		this.taskList = taskList;
		if( currentTask == null || !currentTask.getId().equals(taskList.peek().getId()) ) { //Check if session needs to be (re-)initialized
			//System.out.println( currentTask.getTaskName() + " " + taskList.peek().getTaskName() );
			currentTask = taskList.remove(); //Get task on top of list
			pomsCompleted = 0; //Reset pomodoros completed to 0 for the new task
			if( state == null || state.getName() != StateName.BREAK ) //Defaults to Pomodoro state if not resuming from Break
				state = new Pomodoro( settings.getPomLength() );
		}
		else {
			currentTask = taskList.remove(); //We know the task on top of queue is same as currentTask, so remove the duplicate
			if( ( (TimeState) state ).getOriginalTime() != settings.getPomLength() ) //check if User has changed the pomodoro length in settings
				state = new Pomodoro( settings.getPomLength() );
			
		}
		
		setChanged(); //Updating observers right away to prevent displaying old information
		notifyObservers();
		timer = new Timer( 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				TimeState currentTime = null;
				if( state != null && state instanceof TimeState ) { //Sanity check. Should always be a TimeState here anyways.
					currentTime = (TimeState) state;
					setChanged();
					notifyObservers(); //Display current time on view
					currentTime.countdown(); //decrease pom or break time by 1
				}
			
				if( currentTime.getMillis() == -1000 ) 
					state.complete(); //Complete current state by updating view, and moving to next state
			}
		});
		timer.start();
	}
	
	/**Pauses the WorkSession */
	public void pause() {
		timer.stop();
	}
	
	/**Resets the current pomodoro or break to its starting time */
	public void resetTime() {
		if( state != null && state instanceof TimeState ) { //Sanity check. Is only called when its a TimeState anyways.
			TimeState currentTime = (TimeState) state;
			currentTime.reset();
		}
	}
	
	/**
	 * Resets the current State, task Pomodoros completed, and LongBreakCount to their default values. This effectively clears the 
	 * WorkSession's data, except for the Settings.
	 */
	public void resetProgress() {
		currentTask = null;
		state = null;
		pomsCompleted = 0;
		longBreakCount = 0;
	}
	
	/**
	 * Saves this WorkSession into a datafile and overwrites previous WorkSession data
	 */
	public void save() {
		ObjectContainer data = Db4oEmbedded.openFile(Db4oEmbedded
				 .newConfiguration(), "pomodorodata"); //Open the data file
		try { //Delete old data
			ObjectSet result= data.queryByExample(WorkSession.class);
			while(result.hasNext()) 
			 data.delete(result.next());
			
			data.store(this); //Store this worksession
	}
	finally {
				 data.close();
			}
	}
	
	public Settings getSettings() {
		return settings;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public int getPomsCompleted() {
		return pomsCompleted;
	}

	public WorkState getState() {
		return state;
	}
	
	/**
	 * Returns true if this WorkSession's current state is about to change, false otherwise
	 */
	public boolean stateChanging() {
		return state.isChanging();
	}
	
	/**
	 * Returns the StateName of the current state. Used to identify this WorkSession's State without needing the state object itself.
	 */
	public StateName getStateName() {
		return state.getName();
	}
	
	/**
	 * Get the number of pomodoros that need to be completed until the next Long Break
	 */
	public int getPomsTillLong() {
		//Calculating Pomodoros till long break pomsTillBreak
		int temp = longBreakCount % settings.getPomsForLongBreak();
		int pomsTillBreak ;
		if( longBreakCount >= settings.getPomsForLongBreak() ) //Need to use "temp" and formula if greater. Display pomsForLongBreak if formula calculates 0
			pomsTillBreak = temp != 0 ? (settings.getPomsForLongBreak() - temp) : 0;
		else //If less, just subtract longBreakCount from poms needed for long break
			pomsTillBreak = settings.getPomsForLongBreak() - longBreakCount;
		
		return pomsTillBreak;
	}
	
	public enum StateName {
		POMODORO, BREAK, TASKDONE, ALLDONE
	}
	
	interface WorkState {
		/**
		 * Performs the actions required by this state, sets up this WorkSession
		 * for the next state, and then switches to the next state.
		 */
		public void complete();
		public boolean isChanging(); //Returns true if this state is about to change to another state
		public StateName getName();
	}
	
	
	/**Working on a Pomodoro for a specified time. Upon completion, will move to either TaskDone or Break. */
	 class Pomodoro extends TimeState implements WorkState  {
		
		 StateName name = StateName.POMODORO;
		/**Creates a Pomodoro state with the given time in milliseconds for countdown
		 * @param millis - Time to spend in this TimeState (milliseconds)
		 */
		public Pomodoro(int millis) {
			super(millis);
		}

		public void complete() {
			pomsCompleted++; 
			longBreakCount++;
			int breakDuration;
			
			if( longBreakCount % settings.getPomsForLongBreak() == 0 ) //Decide if next break short or long
				breakDuration =  settings.getLongBreak();
			else
				breakDuration =  settings.getShortBreak();
			
			if( pomsCompleted == currentTask.getTaskLength() ) { //Check if currentTask complete
				state = new TaskDone( breakDuration );
			}
			else
				state = new Break( breakDuration );
		}

		public StateName getName() {
			return name;
		}

		@Override
		public boolean isChanging() {
			return getMillis() == 0;
		}
	}
	
	/**Taking a break. Upon completion, will move to Pomodoro */
	class Break extends TimeState implements WorkState {
		
		StateName name = StateName.BREAK;
		/**
		 * Creates new Break state with the given time in milliseconds for countdown
		 * @param millis - time to spend in this TimeState (milliseconds)
		 */
		public Break(int millis) {
			super(millis);
		}

		public void complete() {
			state = new Pomodoro(settings.getPomLength());
		}

		public StateName getName() {
			return name;
		}
		
		@Override
		public boolean isChanging() {
			return getMillis() == 0;
		}
	}
	
	/**Alerts user that task is done and loads next task if possible. Upon completion, moves to AllDone or Break*/
	class TaskDone extends TimeState implements WorkState {
		
		StateName name = StateName.TASKDONE;
		int potentialBreak;
		/**
		 * Creates a TaskDone state with the specified potential break time. 
		 * @param potentialBreak - The break time to set for the next Break state upon completion, if there are more tasks
		 */
		public TaskDone(int potentialBreak) {
			super( MESSAGEDELAY );
			this.potentialBreak = potentialBreak;
		}

		public void complete() {
			currentTask.setComplete(true);
			setChanged();
			notifyObservers(); //Update the view to alert users of task completion
			pomsCompleted = 0; //Reset number of pomodoros completed to 0 for next state
			
			if( (currentTask = taskList.poll() ) != null )  //Check if you can load next task, or done
				state = new Break(potentialBreak);
			else {
				state = new AllDone(); 
			}
		}

		public StateName getName() {
			return name;
		}
		
		@Override
		public boolean isChanging() {
			return getMillis() == MESSAGEDELAY;
		}
	}
	
	/**All tasks on the Today list are complete. Stops working. Upon completion, assigns state to null */
	class AllDone extends TimeState implements WorkState {
		
		public AllDone() {
			super(MESSAGEDELAY);
		}

		StateName name = StateName.ALLDONE;
		public void complete() {
			timer.stop();
			setChanged();
			notifyObservers();
			currentTask = null;
			//state = null; //reset state to null for when user starts a new Today list
		}
		
		public StateName getName() {
			return name;
		}
		
		@Override
		public boolean isChanging() {
			return getMillis() == 0;
		}
	}
}
