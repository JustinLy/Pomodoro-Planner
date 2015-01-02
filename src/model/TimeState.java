package model;

import java.util.concurrent.TimeUnit;

/**Class to keep track of current time on the timer for a pomodoro or break **/
public class TimeState {

	int originalTime;
	int millis;
	long minutes;
	long seconds;
	
	public TimeState( int millis ) {
		this.originalTime = millis;
		this.millis = millis;
		minutes = TimeUnit.MILLISECONDS.toMinutes( millis );
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
	}
	
	public int getOriginalTime() {
		return originalTime;
	}
	
	public int getMillis() {
		return millis;
	}
	
	public long getMinutes() {
		return minutes;
	}

	public long getSeconds() {
		return seconds;
	}
	
	/**Decreases millis by 1000 and update the minutes and seconds in this PomTime
	 */
	public void countdown() {
		millis -= 1000; //Countdown by 1 second
		minutes = TimeUnit.MILLISECONDS.toMinutes( millis );
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
	}
	
	/**Reset this TimeState to its original time */
	public void reset() {
		millis = originalTime;
	}
	
}
