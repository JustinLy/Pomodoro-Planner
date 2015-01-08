package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class WorkSchedule extends Observable {
	List< ArrayList<Task> > schedule; //List containing 3 tasklists, one for today, tomorrow, day after
	
	//Constants for each of the 3 lists
	public static int TODAY = 0;
	public static int TOMORROW = 1;
	public static int DAY_AFTER = 2;
	
	/**Creates an empty workschedule */
	public WorkSchedule() {
		schedule = new ArrayList< ArrayList<Task> >();
		schedule.add(new ArrayList<Task>() );
		schedule.add(new ArrayList<Task>() );
		schedule.add(new ArrayList<Task>() );
	}
	
	/**
	 * Loads work schedule from previous WorkSchedule saved on file
	 * @param schedule - The previous WorkSchedule saved on file
	 */
	public WorkSchedule( WorkSchedule previousSchedule ) {
		this.schedule = previousSchedule.schedule;
		updateObservers();
		}
	
	/**
	 * Loads the saved WorkSchedule from the data file and returns it, if it exists, else returns null
	 * @return the current saved WorkSchedule or new empty WorkSchedule if it doesn't exist in the file
	 */
	public static WorkSchedule loadSchedule() {
		ObjectContainer data = Db4oEmbedded.openFile(Db4oEmbedded
				 .newConfiguration(), "pomodorodata"); //Open the data file
   	
   	try { //Attempt to load data
			ObjectSet scheduleData= data.queryByExample(new WorkSchedule());
			
			if(scheduleData.hasNext()) {//Retrieve the WorkSchedule {
				WorkSchedule oldSchedule = (WorkSchedule) scheduleData.next();
				oldSchedule.deleteObservers(); //Deletes old observers 
				System.out.println( "schedule");
				return oldSchedule;
		}
			else
				return new WorkSchedule(); //If no WorkSchedule found in the file
	}
   	finally {
		 data.close();
		}
	}
	
	/**
	 * Returns the list of tasks scheduled for the specified day
	 * @param day - The day whose list of tasks you want returned 
	 * @requires - "Day" must be one of WorkSchedule's constants, TODAY, TOMORROW, DAY_AFTER
	 * @return - The list of tasks scheduled for the specified day
	 */
	public List<Task> getTaskList( int day ) {
		if( day < 0 || day > 2 )
			throw new IllegalArgumentException( "Error: Must input TODAY, TOMORROW, or DAY_AFTER" );
		else
			return schedule.get(day);
	}
	
	/**
	 * Adds a new task to the specified day's task list using the specified task name and task length
	 * @param taskName - Name of task to be added
	 * @param taskLength - Number of pomodoro units to complete the task
	 * @param day - The day whose task list you want to add the new task to
	 */
	public void addTask( String taskName, int taskLength, int day ) {
		if( day < 0 || day > 2 )
			throw new IllegalArgumentException( "Error: Must input TODAY, TOMORROW, OR DAY_AFTER" );
		else {
			schedule.get(day).add( new Task(taskName, taskLength));
			updateObservers();
		}
	}
	
	/**
	 * Moves the task at a position on the schedule to a new position on the schedule
	 * @param oldDay - The day whose list of tasks you want to move the specified task away from
	 * @param oldPosition - The position of the task on the list that you want to move
	 * @param newDay - The day whose list of tasks you want to add the specified task to
	 * @param newPosition  - The position on the new list that you want to add the task to
	 */
	public void moveTask( int oldDay, int oldPosition, int newDay, int newPosition ) {
		if( oldPosition >= schedule.get(oldDay).size())
			throw new IllegalArgumentException( "Error: Task position does not exist\n" );
		else {
			List<Task> oldList = schedule.get(oldDay);
			Task movingTask = oldList.get(oldPosition);
			oldList.remove(oldPosition); //Remove from original day's list
			List<Task> newList = schedule.get(newDay);
			if( newDay == oldDay && newPosition > oldPosition )  //Moving Task within same list
				newPosition--; //Size of list went down by 1 when removed from original position
			newList.add(newPosition, movingTask); //Add to new day's list at specified position
			updateObservers();
		}
	}
	
	/**
	 * Switches the position of two tasks on the schedule
	 * @param day1 - day that first task belongs to
	 * @param position1 - position in list of first task
	 * @param day2 - day that second task belongs to
	 * @param position2 - position in list of second task
	 */
	public void switchTasks( int day1, int position1, int day2, int position2 ) {
		if( day1 == day2 ) //Can just use Collections switch if in same list 
			Collections.swap(getTaskList(day1), position1, position2);
		else {
		moveTask( day1, position1, day2, position2 ); //Move task1 to  position of task2
		moveTask( day2, position2+1, day1, position1 ); //Move task2 to position of task 1
		}
		updateObservers();
	}
	
	/**
	 * Deletes task at the specified position
	 * @param day - Day that task you want to delete belongs to
	 * @param position - Position in the list of task you want to delete
	 */
	public void deleteTask( int day, int position ) {
		if( day < 0 || day > 2 || position >= schedule.get(day).size() )
			throw new IllegalArgumentException("Error: Invalid day or position\n" );
		else {
			schedule.get(day).remove(position);
			updateObservers();
		}
	}
	
	/**
	 * Removes all "Completed" Tasks from the TODAY list and then shifts all TOMORROW list tasks to TODAY, and all 
	 * DAY_AFTER list tasks to TOMORROW. DAY_AFTER list will be empty after this. 
	 * Also erases any progress in the WorkSession including Pomodros Completed, Pomodoros till a long break, and any
	 * state it may have been in prior to this method being called. (Settings are preserved however) 
	 */
	public void completeDay() {
		List<Task> today = schedule.get(WorkSchedule.TODAY);
		List<Task> tomorrow = schedule.get(WorkSchedule.TOMORROW);
		List<Task> dayAfter = schedule.get(WorkSchedule.DAY_AFTER);
		
		Iterator<Task> todayIterator = today.iterator();
		while( todayIterator.hasNext() ) { //Remove all completed tasks from TODAY list
			Task currentTask = todayIterator.next();
			if( currentTask.isComplete() )
				todayIterator.remove(); 
		}
		
		today.addAll(tomorrow); //Move all of tomorrow's tasks to today (since "tomorrow" will be today)
		tomorrow.clear();
		tomorrow.addAll(dayAfter); //Move all of DAY_AFTER's tasks to tomorrow
		dayAfter.clear(); 
		updateObservers();
	}
	
	/**
	 * Edits the name of the specified Task
	 * @param newName - new name you want to give to the Task
	 * @param day - The day that the Task is scheduled on
	 * @param position - Position of the Task in its tasklist
	 */
	public void editTaskName( String newName, int day, int position ) {
		List<Task> taskList = schedule.get(day);
		Task task = taskList.get(position);
		task.setTaskName(newName);
		updateObservers();
	}
	/**
	 * Edits the length of a task (in pomodoros)
	 * @param newLength - new length of the task
	 * @param day - day that Task is scheduled on
	 * @param position - Position of the Task in its tasklist
	 */
	public void editTaskLength( int newLength, int day, int position) {
		List<Task> taskList = schedule.get(day);
		Task task = taskList.get(position);
		task.setTaskLength(newLength);
		updateObservers();
	}
	
	/**
	 * Saves this WorkSchedule into a datafile and overwrites previous WorkSchedule data
	 */
	public void save() {
		ObjectContainer data = Db4oEmbedded.openFile(Db4oEmbedded
				 .newConfiguration(), "pomodorodata"); //Open the data file
		try { //Delete old data
			ObjectSet result= data.queryByExample(WorkSchedule.class);
			while(result.hasNext()) 
			 data.delete(result.next());
			
			data.store(this); //Store this workschedule
			System.out.println("hi");
	}
	finally {
				 data.close();
			}
	}
	
	/**Updates the Observers attached to this Observable object*/
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}
	
	
	
	
}
