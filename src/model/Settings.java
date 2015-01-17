package model;

import java.util.concurrent.TimeUnit;

public class Settings {
	/**Time parameters for the pomodoro technique and the methods to use and modify them **/
	
	private int pomLength = 1500000; //25 minutes default length
	private int pomsForLongBreak = 5; //5 pomodoros for long break default
	private int shortBreak = 300000; //5 mins default short break
	private int longBreak = 1200000; //20 mins default longBreak
	
	public int getPomLength() {
		return pomLength;
	}
	public void setPomLength(int pomLength) {
		this.pomLength = (int) ( TimeUnit.MINUTES.toMillis(pomLength) / 10 ) ;
	}
	public int getPomsForLongBreak() {
		return pomsForLongBreak;
	}
	public void setPomsForLongBreak(int pomsForLongBreak) {
		this.pomsForLongBreak = pomsForLongBreak;
	}
	public int getShortBreak() {
		return shortBreak;
	}
	public void setShortBreak(int shortBreak) {
		this.shortBreak = (int)TimeUnit.MINUTES.toMillis(shortBreak);
	}
	public int getLongBreak() {
		return longBreak;
	}
	public void setLongBreak(int longBreak) {
		this.longBreak = (int)TimeUnit.MINUTES.toMillis(longBreak) / 10;
	}
}
