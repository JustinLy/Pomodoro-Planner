package model;

import java.util.concurrent.TimeUnit;

/**Class to keep track of current time on the timer for a pomodoro or break **/
public class TimeState {

	private int originalTime;
	private int millis;
	private long minutes;
	private long seconds;
	
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
	
	
	/**
	 * Returns a string representation of the TimeState in the Mins:Secs format
	 */
	@Override
	public String toString() {
		String stringMins = Integer.toString((int) minutes); 
		String stringSecs = Integer.toString( (int) seconds );
		if( minutes < 10 ) 
			stringMins = "0" + stringMins; //add a 0 if single digit minutes
		if( seconds < 10 ) 
			stringSecs = "0" + stringSecs; //add a 0 if single digit minutes
		return stringMins + ":" + stringSecs; //return in a min:secs format
	}
	
	/**Decreases millis by 1000 and update the minutes and seconds in this PomTime
	 */
	public void countdown() {
		millis -= 1000; //Countdown by 1 second
		minutes = TimeUnit.MILLISECONDS.toMinutes( millis );
		int remainder = (int) (millis - TimeUnit.MINUTES.toMillis(minutes));
		seconds = TimeUnit.MILLISECONDS.toSeconds(remainder);
	}
	
	/**Reset this TimeState to its original time */
	public void reset() {
		millis = originalTime;
	}
	
}
