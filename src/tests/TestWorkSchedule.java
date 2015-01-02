package tests;

import static org.junit.Assert.*;
import model.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestWorkSchedule {
	static WorkSchedule work = new WorkSchedule();
	static Task today1;
	static Task today2;
	static Task tomorrow1; 
	static Task tomorrow2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 today1 = new Task( "Math", 3 );
		today2 = new Task( "MP 5", 5 );
		 tomorrow1 = new Task( "Tor's Lab", 4 );
		 tomorrow2 = new Task( "readings", 3 );
		work.getTaskList(WorkSchedule.TODAY).add(today1);
		work.getTaskList(WorkSchedule.TODAY).add(today2);
		work.getTaskList(WorkSchedule.TOMORROW).add(tomorrow1);
		work.getTaskList(WorkSchedule.TOMORROW).add(tomorrow2);

	}

	@Test
	public void testSwitchTask() {
		work.switchTasks(WorkSchedule.TODAY, 0, WorkSchedule.TOMORROW, 1);
		assertTrue( work.getTaskList(WorkSchedule.TODAY).get(0) == tomorrow2
		&& work.getTaskList(WorkSchedule.TOMORROW).get(1) == today1 );
	}
	
	@Test
	public void testMoveTask() {
		work.moveTask(WorkSchedule.TOMORROW, 0, WorkSchedule.DAY_AFTER, 0);
		assertEquals( work.getTaskList(WorkSchedule.DAY_AFTER).get(0), tomorrow1);
	}
	
	@Test
	public void testAddTask() {
		work.addTask("do stuff", 3, WorkSchedule.TODAY);
		assertEquals( work.getTaskList(WorkSchedule.TODAY).get(2).getTaskName(), "do stuff" );
	}
	
	@Test
	public void testCompleteDay() {
		WorkSchedule work2 = new WorkSchedule();
		work2.addTask("math", 2, WorkSchedule.TODAY);
		work2.addTask("eece210", 3, WorkSchedule.TODAY);
		work2.addTask("tor", 1, WorkSchedule.TOMORROW);
		work2.addTask("do stuff", 2, WorkSchedule.DAY_AFTER);
		work2.getTaskList(WorkSchedule.TODAY).get(0).setComplete(true); //Make math complete
		work2.completeDay();
		
		assertTrue( work2.getTaskList(WorkSchedule.TODAY).size() == 2 && 
				work2.getTaskList(WorkSchedule.TOMORROW).size() ==1 &&
				work2.getTaskList(WorkSchedule.DAY_AFTER).size() == 0 );
		
	}

}
