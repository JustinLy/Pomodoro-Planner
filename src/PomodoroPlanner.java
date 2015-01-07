import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import model.*;
import controller.*;
import view.*;

public class PomodoroPlanner {
	static WorkSchedule workSchedule = new WorkSchedule();
	static WorkSession workSession = new WorkSession();
	static ScheduleView scheduleView = new ScheduleView();
	static WorkView workView = new WorkView();
	public static void main(String[] args) {
	/**This class initializes the Model, View and Controller and launches Pomodoro Planner */
		try {
		loadData();
		
		workSchedule.addObserver(scheduleView); //register Observers to models
		workSession.addObserver(workView);
		
		Controller controller = new Controller(  scheduleView, workView, workSchedule, workSession);
		WorkController workController = new WorkController( workView, workSession);
		
		workSchedule.updateObservers();
		scheduleView.run();
		}
		catch(Exception e )
		{
			System.out.println("fail");
		}
	}
	
    /**
     * Loads the current saved model data (WorkSchedule and WorkSession) from the previous session
     */
    public static void loadData() {
    	ObjectContainer data = Db4oEmbedded.openFile(Db4oEmbedded
				 .newConfiguration(), "pomodorodata"); //Open the data file
    	
    	try { //Attempt to load data
			ObjectSet oldSchedule= data.queryByExample(new WorkSchedule());
			ObjectSet oldSession = data.queryByExample(new WorkSession() );
			
			if(oldSchedule.hasNext()) {//Retrieve the WorkSchedule {
				WorkSchedule o1 = (WorkSchedule) oldSchedule.next();
				o1.deleteObservers();
				workSchedule = (WorkSchedule) o1;
    	}
			if( oldSession.hasNext() ) { //Retrieve the WorkSession 
				WorkSession o2 = (WorkSession) oldSession.next();
				o2.deleteObservers();
				workSession = (WorkSession) o2;
			}
				
			//workSchedule.addObserver(scheduleView); //register Observers to models
		//	workSession.addObserver(workView);
		//	workSchedule.updateObservers(); 
	}
	finally {
				 data.close();
				}
    }
}
