package model;

import java.util.Observable;
import java.util.Queue;

public class WorkSession extends Observable {

	int pomLength = 1500000; //25 minutes default length
	int pomsForLongBreak = 5; //5 pomodoros for long break default
	int shortBreak = 300000; //5 mins default short break
	int longBreak = 1200000; //20 mins default longBreak
	
	Queue<Task> taskList; //Tasks for today that still need to be completed
	Task currentTask; 		//task currently being worked on
	PomTime currentTime; //current state of timer
	int pomsCompleted = 0; //poms completed for current task so far
	WorkState state; //current state of the WorkSession
	
	/**
	 * Works on the given task list using the Pomodoro Technique continuously until
	 * "pause" is set by the user or all tasks in the list have been completed.
	 * @param taskList - Queue of tasks to complete 
	 */
	public void workOnTasks( Queue<Task> taskList ) {
		if( currentTask == null || currentTask != taskList.peek() ) { //Check if session needs to be initialized
		}
	}
	interface WorkState {
		/**
		 * Performs the actions required by this state, sets up this WorkSession
		 * for the next state, and then switches to the next state.
		 */
		public void complete();
	}
	
	/**Working on a pomodoro. Upon completion, will move to either TaskDone or Break. */
	class Pomodoro implements WorkState {
		public void complete() {
			pomsCompleted++; 
			
			if( pomsCompleted % pomsForLongBreak == 0 ) //Decide if next break short or long
				currentTime = new PomTime( longBreak );
			else
				currentTime = new PomTime( shortBreak);
			
			if( pomsCompleted == currentTask.getTaskLength() ) //Check if currentTask complete
				state = new TaskDone();
			else
				state = new Break();
		}
	}
	
	/**Taking a break. Upon completion, will move to Pomodoro */
	class Break implements WorkState {
		public void complete() {
			currentTime = new PomTime( pomLength );
			state = new Pomodoro();
		}
	}
	
	/**Alerts user that task is done and loads next task if possible. Upon completion, moves to AllDone or Break*/
	class TaskDone implements WorkState {
		public void complete() {
			currentTask.setComplete(true);
			notifyObservers(); //Update the view to alert users of task completion
			pomsCompleted = 0; //Reset number of pomodoros completed to 0 for next state
			
			if( (currentTask = taskList.poll() ) != null )  //Check if you can load next task, or done
				state = new Break();
			else
				state = new AllDone(); 
		}
	}
	
	/**All tasks on the Today list are complete. Stops working. Upon completion, assigns state to null */
	class AllDone implements WorkState {
		public void complete() {
			notifyObservers();
			state = null; //reset state to null for when user starts a new Today list
		}
	}
	
}
