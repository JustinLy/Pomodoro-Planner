import model.*;
import controller.*;
import view.*;

public class PomodoroPlanner {

	public static void main(String[] args) {
	/**This class initializes the Model, View and Controller and launches Pomodoro Planner */
		
		WorkSchedule workSchedule = new WorkSchedule();
		WorkSession workSession = new WorkSession();
		ScheduleView scheduleView = new ScheduleView();
		WorkView workView = new WorkView();
		
		workSchedule.addObserver(scheduleView); //register Observers to models
		workSession.addObserver(workView);
		
		Controller controller = new Controller(  scheduleView, workView, workSchedule, workSession);
		scheduleView.run();
	}

}
