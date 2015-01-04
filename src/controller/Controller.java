package controller;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller.ScheduleController.EditTaskListener;
import controller.Controller.ScheduleController.TaskMovementListener;
import view.*;
import model.*;
public class Controller {

	//View references
	ScheduleView scheduleView;
	WorkView workView;
	
	//Model references
	WorkSchedule workSchedule;
	WorkSession workSession;
	
	//Need a shared Listener for these across all components
	ScheduleController scheduleController = new ScheduleController();
	TaskMovementListener taskListener = scheduleController.new TaskMovementListener();
	EditTaskListener editListener = scheduleController.new EditTaskListener();
	
	
	public Controller( ScheduleView scheduleView, WorkView workView,
						WorkSchedule workSchedule, WorkSession workSession ) {
			this.scheduleView = scheduleView;
			this.workView = workView;
			this.workSchedule = workSchedule;
			this.workSession = workSession;
		}

	/**
	 * Creates an array of EventListeners containing all one of every listener in the ScheduleController class
	 * and registers the listeners to the correct components in ScheduleView
	 */
	public void registerScheduleComponents( ) {
		EventListener[] listeners = new EventListener[6];
		listeners[0] = scheduleController.new ColumnComponentsListener();
		listeners[1] = scheduleController.new AddTaskListener();
		listeners[2] = scheduleController.new SettingsListener();
		listeners[3] = scheduleController.new completeDayListener();
		listeners[4] = scheduleController.new SaveListener();
		listeners[5] = scheduleController.new WorkListener();
	}
	class ScheduleController {
		/**Controls all interactions between ScheduleView and WorkSchedule*/
		
		class ColumnComponentsListener extends ContainerAdapter {
			/**Adds TaskMovementListener, EditTaskListener, and DeleteTaskListener to new Tasks and Spaces added to the To-do lists */
			@Override
			public void componentAdded( ContainerEvent e ) {
				
			}
		}
		
		class TaskMovementListener extends MouseAdapter implements MouseMotionListener {
			/**Handles user requests (from the view) to change the positions of Tasks in the To-do lists */
			
		}
		
		class EditTaskListener extends MouseAdapter {
			/**Handles user requests (from the view) to edit the name of Tasks or their length */
		}
		
		class AddTaskListener implements ActionListener {
			/**Handles user requests (from the view) to add a Task to a To-do list */
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Create the input window
				JPanel inputPanel = new JPanel();
				inputPanel.setLayout( new GridLayout( 0, 2 ));
		        inputPanel.add( new JLabel( "Task: "));
		        inputPanel.add( new JTextField( 16));
		        inputPanel.add( new JLabel( "Length"));
		        inputPanel.add( new JTextField( 3 ));
		        
				int result = JOptionPane.showConfirmDialog( null, inputPanel, "Enter task name and length (in pomodoros)", JOptionPane.OK_CANCEL_OPTION );
                try{
                if( result == JOptionPane.OK_OPTION  ) //Retrieve input and send it to the WorkSchedule model to create new task
                {   
                	String taskName = ((JTextField)inputPanel.getComponent(1)).getText();
                	int taskLength = Integer.parseInt( ( (JTextField)inputPanel.getComponent(3) ).getText() );
                    workSchedule.addTask(taskName, taskLength, Column.getFocusedColumn()); //update the model
                   // ((JTextField)inputPanel.getComponent(1)).setText(""); //resets both text fields to empty
                   // ((JTextField)inputPanel.getComponent(3) ).setText( "" );
                }
              
            }
                catch( NumberFormatException ex )
                {
                    JOptionPane.showMessageDialog(null, "Error: Enter an integer for length");
                   // ((JTextField)inputPanel.getComponent(1)).setText(""); //resets both text fields to 0
                   // ((JTextField)inputPanel.getComponent(3) ).setText( "" );
                }
			}
			
		}
		
		class DeleteTaskListener extends KeyAdapter {
			/**Handles user requests (from the view) to delete a specified Task from a To-do list */
		}
		
		class SettingsListener implements ActionListener {
			/**Handles user requests (from the view) to change the settings of the Pomodoro Planner */
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
		}
		
		class completeDayListener implements ActionListener {
			/**Handles user requests (from the view) to stop working for today and prepare the planner for tomorrow
			 by appending the tasks for "Tomorrow" to "Today" and "Day After" to "Tomorrow"*/
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
		class SaveListener implements ActionListener {
			/**Handles user requests (from the view) to save the current tasklists, settings and progress of the Pomodoro Planner */
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
		class WorkListener implements ActionListener {
			/**Handles user request (from the view) to start working on the tasks */
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		
		
		
		
	}
}
