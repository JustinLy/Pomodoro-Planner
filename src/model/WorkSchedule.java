package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

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
		setChanged();
		notifyObservers();
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
			setChanged();
			notifyObservers();
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
			newList.add(newPosition, movingTask); //Add to new day's list at specified position
			setChanged();
			notifyObservers();
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
		moveTask( day1, position1, day2, position2 ); //Move task1 to  position of task2
		moveTask( day2, position2+1, day1, position1 ); //Move task2 to position of task 1
		setChanged();
		notifyObservers();
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
			setChanged();
			notifyObservers();
		}
	}
	
	
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
		setChanged();
		notifyObservers();
	}
	
	
	
	
	
	
}
