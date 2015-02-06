import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import model.*;
import controller.*;
import view.*;

public class PomodoroPlanner {
	
	public static void main(String[] args) {
	/**This class initializes the Model, View and Controller and launches Pomodoro Planner */
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			System.out.println("Error Nimbus not available");
		}
		
		try {
		WorkSchedule workSchedule = WorkSchedule.loadSchedule(); //Attempt to load WorkSchedule from file
		WorkSession workSession = WorkSession.loadWorkSession(); //Attempt to load WorkSession from file
		ScheduleView scheduleView = new ScheduleView();
		WorkView workView = new WorkView();
		
		workSchedule.addObserver(scheduleView); //register Observers to models
		workSession.addObserver(workView);
		
		ScheduleController scheduleController = new ScheduleController(  scheduleView, workView, workSchedule, workSession);
		WorkController workController = new WorkController( workView, workSession);
		
		workSchedule.updateObservers();
		scheduleView.run();
		}
		catch(Exception e )
		{
			System.out.println("Error launching Pomodoro Planner");
		}
		
		
	}
	

}