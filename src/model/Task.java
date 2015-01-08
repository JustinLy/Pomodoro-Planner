package model;

import java.util.UUID;

public class Task {
	
	private String taskName;
	private int taskLength;
	private boolean complete;
	private UUID id = UUID.randomUUID(); //Required for checking equality when attempting to resume Tasks after loading from file
	
	/** Getters and Setters for taskName, taskLength, complete **/
	
	public Task( String taskName, int taskLength ) {
		this.taskName = taskName;
		this.taskLength = taskLength;
		complete = false;
	}
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getTaskLength() {
		return taskLength;
	}
	public void setTaskLength(int taskLength) {
		this.taskLength = taskLength;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public UUID getId() {
		return id;
	}
	
}
