package controller;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import controller.Controller.ScheduleController.EditLengthListener;
import controller.Controller.ScheduleController.EditNameListener;
import controller.Controller.ScheduleController.MovementListener;
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
	MovementListener moveListener = scheduleController.new MovementListener();
	EditNameListener nameListener = scheduleController.new EditNameListener();
	EditLengthListener lengthListener = scheduleController.new EditLengthListener();
	
	
	public Controller( ScheduleView scheduleView, WorkView workView,
						WorkSchedule workSchedule, WorkSession workSession ) {
			this.scheduleView = scheduleView;
			this.workView = workView;
			this.workSchedule = workSchedule;
			this.workSession = workSession;
			registerScheduleComponents();
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
		scheduleView.registerListeners(listeners);
		workView.registerReopenListener(scheduleController.new ReopenListener());
	}
	

    
	class ScheduleController {
		/**Controls all interactions between ScheduleView and WorkSchedule*/
		
		class ColumnComponentsListener extends ContainerAdapter {
			/**Adds TaskMovementListener, EditTaskListener, and DeleteTaskListener to new Tasks and Spaces added to the To-do lists */
			@Override
			public void componentAdded( ContainerEvent e ) {
				if( !(e.getChild().getClass() == TaskPanel.class || 
						e.getChild().getClass() == Space.class) ) //Sanity check: Should always be a JPanel anyways
					throw new UnsupportedOperationException();
				
				JPanel newComp = (JPanel) e.getChild();
				TaskPanel newTask = null; //taskpanel if it is one
				
				if( newComp.getClass() == TaskPanel.class ) {
					newTask = (TaskPanel) newComp;
					if( newTask.isEditable() ) { //Only add both EditListeners to the taskpanel if it is editable
					newComp.addMouseListener( nameListener);
					newComp.addKeyListener( nameListener ); //Need to register both types, because EditNameListener uses both
					//Registering the EditLengthListener to the Length Textfield inside the Task
					newTask.getLengthField().addActionListener(lengthListener); 
					newTask.getLengthField().addFocusListener(lengthListener); //again, need to register both types of listeners	
					}
					newComp.addKeyListener(scheduleController.new DeleteTaskListener() ); //Add the DeleteTaskListener
				}
				
				if( newTask == null || newTask.isEditable()) { //Only register to Spaces and "editable" TaskPanels
					newComp.addMouseListener(moveListener); 
					newComp.addMouseMotionListener(moveListener); //Need to add mouse and mousemotion since taskListener uses both
				}
			} 
		}
		
		class MovementListener extends MouseAdapter implements MouseMotionListener {
			/**Handles user requests (from the view) to change the positions of Tasks in the To-do lists */
			  private  boolean componentMoving = false;
			    private  boolean canDrop = false;
			    private  JComponent movingTask;
			    private  JComponent targetComponent; //Could be a Task, Space or JLabel of a Column
			    
			    /**Identifies the Task being dragged and disables multiple tasks from being dragged at the same time*/
			    public void mouseDragged( MouseEvent e ) {
			    	//If mouse dragged on a "Task" itself or the JLabel inside the task
			    	if( componentMoving == false) { //Only executes if no component is already being moved
			    		
			    		Object source = e.getSource();
			    	
				        if( source.getClass() == TaskPanel.class ) {//Mouse dragged on "Task" itself 
				        		movingTask = (TaskPanel) source;
				        		componentMoving = true;
				        }
				        else if( ( ( (JComponent)source).getParent() ).getClass() == TaskPanel.class){  //Mouse dragged on JLabel inside "Task"
				        	movingTask = (TaskPanel) ((Component)source).getParent(); //Get the "Task" that the JLabel belongs to
				        	componentMoving = true;
				        }
				        
				        if( componentMoving) {
				      		System.out.println( "moving"); //for testing
				      		scheduleView.setMovingCursor(true);//Change cursor to indicate Task being dragged
				        }
			    	}
			    }
			    
			    /**Identifies the targetComponent (or the destination) of the movingTask, and enables dropping */
			    public void mouseEntered( MouseEvent e ) {
			       //only records the component entered if instance of Task, Space or Column
			        if( componentMoving ) {
			        	if( e.getSource() instanceof Space )
			        		System.out.println( "space" );
			        	else if( e.getSource() instanceof TaskPanel )
			        		System.out.println( "taskpanel");
			        	canDrop = true; //Confirms that moving Task can be dropped on this Space or Column
			            targetComponent = (JComponent) e.getSource();
			            System.out.println( "can drop"); //for testing
			        }
			        else {
			            canDrop = false;
			            System.out.println( "can not drop");
			        }
			    }
			    
			    /**Sets canDrop to false and destroys the previously recorded targetComponent when exiting that movingComponent in the view */
			    public void mouseExited( MouseEvent e ) {
			    	canDrop = false;
			    	targetComponent = null;
			    }
			    
			    /**Moves the Task(s) in the appropriate manner (switch, insert, append) if the appropriate conditions are met 
			     * (componentMoving and canDrop are true). 
			     */
			    public void mouseReleased( MouseEvent e ) {
			    	if( componentMoving && canDrop ) { //Task being moved and component can be dropped on 
			    		//Should refactor this. First 4 lines for Switch and Insert are the exact same....
			    		if( targetComponent.getClass() == TaskPanel.class && targetComponent != movingTask ) { //Task switch
			    			int position1 = getModelPosition(movingTask); //Convert the TaskPanel view position to a model position
			    			int position2 = getModelPosition( targetComponent );
			    			int day1 = ( (Column) movingTask.getParent() ).getDay(); //day that this Task is scheduled on
			    			int day2 = ( (Column) targetComponent.getParent() ).getDay();
			    			workSchedule.switchTasks(day1, position1, day2, position2); //Update model by making the switch
			    		}
			    		else if( targetComponent.getClass() == Space.class ) { //Inserting a Task into another position
			    			int oldPosition = getModelPosition( movingTask );
			    			int newPosition = getModelPosition( targetComponent );
			    			int oldDay =  ( (Column) movingTask.getParent() ).getDay(); 
			    			int newDay = ( (Column) targetComponent.getParent() ).getDay();
			    			workSchedule.moveTask(oldDay, oldPosition, newDay, newPosition); //Move movingTask to new position in model
			    		}
			    		
			    	}
			        componentMoving = false; //Drag and drop operation complete
			        canDrop = false;
			        scheduleView.setMovingCursor(false);
			    }
			    
		
		}
		
		class EditNameListener extends MouseAdapter implements KeyListener {
			//TODO: Implement this
			 	/*boolean editing = false; //indicates if a Task is being edited. Only one can be at a time
			    TaskPanel editingTask; //Task being edited
			    JTextField editField; //Field that will be used to edit tasks
			    
			    public void mouseClicked( MouseEvent e ) {
			    	
			    }*/
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			/**Handles user requests (from the view) to edit the name of Tasks */
			
		}
		
	
		
		class EditLengthListener extends FocusAdapter implements ActionListener {
			/**Handles user requests (from the view) to edit the length of tasks */
			/*TODO: Fix this so it doesn't do editLength(e) when actionPerformed fires
			@Override
			public void focusLost(FocusEvent e) {
				editLength(e);
			}/*/

			@Override
			public void actionPerformed(ActionEvent e) {
				editLength(e);
			}
			
			/**Method to edit the length of a Task in the model and then update it in the TaskPanel of the View*/
			private void editLength( EventObject e ) {
				if( e.getSource().getClass() != JTextField.class) //sanity check
					throw new IllegalStateException( "Error, edit length listener broken");
				JTextField length = (JTextField) e.getSource();
				TaskPanel taskPanel = (TaskPanel) length.getParent();
				int day = ( (Column) taskPanel.getParent()).getDay(); //Get day that task belongs to
				int position = getModelPosition(taskPanel); //Get the position of the Task represnted by taskPanel
				
				try {
					int newLength = Integer.parseInt(length.getText()); 
					if( newLength <= 0 )
						throw new IllegalArgumentException();
					workSchedule.editTaskLength(newLength, day, position); //Update the task length
				}
				catch( Exception ex ) { //Updates the view to show the old task length in the model, when user enters an invalid length
					Task task = workSchedule.getTaskList(day).get(position); //Get the actual task in the model
					workSchedule.editTaskLength(task.getTaskLength(), day, position); 
				}
			}
		}
		
		class AddTaskListener implements ActionListener {
			/**Handles user requests (from the view) to add a Task to a To-do list */
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Column.safeToChange(false); //temporary hack to fix add task bug
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
                	if( taskLength < 0 )
                		throw new IllegalArgumentException();
                    workSchedule.addTask(taskName, taskLength, Column.getFocusedColumn()); //update the model
                    Column.safeToChange(true); //temporary hack done, back to normal
                }
              
            }
                catch( 
                		IllegalArgumentException ex )
                {
                    JOptionPane.showMessageDialog(null, "Error: Enter a positive integer for length");
                }
			}
			
		}
		
		class DeleteTaskListener extends KeyAdapter {
			/**Handles user requests (from the view) to delete a specified Task from a To-do list */
			public void keyReleased( KeyEvent e ) { //TODO: Add JConfirmDialog to confirm deletion
				if( e.getKeyCode() == KeyEvent.VK_DELETE) {
					TaskPanel taskPanel = (TaskPanel) e.getSource();
					int day = ( (Column) taskPanel.getParent() ).getDay();
					int position = getModelPosition(taskPanel);
					workSchedule.deleteTask(day, position); //Delete the task from the model and update view
				}
			}
		}
		
		class SettingsListener implements ActionListener {
			/**Handles user requests (from the view) to change the settings of the Pomodoro Planner */
			JPanel inputPanel = new JPanel();
			//TODO: Check for invalid input
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings settings = workSession.getSettings(); //Get current settings to display them
				//Create the input window, and display current settings
				inputPanel.setLayout( new GridLayout( 0, 2 ));
				inputPanel.removeAll(); //Clear old components
		        inputPanel.add( new JLabel( "Pomodoro Length (mins): " ));
		        inputPanel.add( new JTextField(""+TimeUnit.MILLISECONDS.toMinutes(settings.getPomLength()), 4));
		        inputPanel.add( new JLabel( "Short Break (mins)"));
		        inputPanel.add( new JTextField(""+TimeUnit.MILLISECONDS.toMinutes(settings.getShortBreak()), 4 ));
		        inputPanel.add( new JLabel( "Long Break (mins): "));
		        inputPanel.add( new JTextField( ""+TimeUnit.MILLISECONDS.toMinutes(settings.getLongBreak()), 4 ));
		        inputPanel.add( new JLabel( "Pomodoros till Long Break: "));
		        inputPanel.add( new JTextField( ""+settings.getPomsForLongBreak(),4 ));
		        
				int result = JOptionPane.showConfirmDialog( null, inputPanel, "Choose your settings", JOptionPane.OK_CANCEL_OPTION );
                
                if( result == JOptionPane.OK_OPTION  ) //Retrieve input and send it to the WorkSession model to update Settings
                {   
                	int pomLength = Integer.parseInt( ((JTextField)inputPanel.getComponent(1)).getText() );
                	int shortBreak = Integer.parseInt( ( (JTextField)inputPanel.getComponent(3) ).getText() );
                	int longBreak = Integer.parseInt( ( (JTextField)inputPanel.getComponent(5) ).getText() );
                	int pomsTillBreak = Integer.parseInt( ( (JTextField)inputPanel.getComponent(7) ).getText() );
                	
                	Settings newSettings = workSession.getSettings(); //Update the new settings in the model
                	newSettings.setPomLength(pomLength);
                	newSettings.setShortBreak(shortBreak);
                	newSettings.setLongBreak(longBreak);
                	newSettings.setPomsForLongBreak(pomsTillBreak);
                }
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
				ObjectContainer data = Db4oEmbedded.openFile(Db4oEmbedded
						 .newConfiguration(), "pomodorodata"); //Open the data file
				int confirm = JOptionPane.showConfirmDialog (null, "Saving will overwrite current data. Continue?","Warning",JOptionPane.YES_NO_OPTION);
				
				if( confirm == JOptionPane.YES_OPTION) {
					try { //Delete old data 
							ObjectSet result= data.queryByExample(new Object());
							while(result.hasNext()) 
							 data.delete(result.next());
							
							data.store(workSchedule); //Store both models
							data.store(workSession);
							JOptionPane.showMessageDialog(null, "Schedule and progress saved!");
					}
					finally {
								 data.close();
								}
				}
			
			}
		}

		class WorkListener implements ActionListener {
			/**Handles user request (from the view) to start working on the tasks */
			@Override
			public void actionPerformed(ActionEvent e) {
				Queue<Task> todoList = new LinkedList();
				for(Task task : workSchedule.getTaskList(WorkSchedule.TODAY)) { //Add all unfinished tasks for Today's list to queue
					if( !task.isComplete() )
						todoList.add(task);
				}
				
				if( todoList.isEmpty()) //Check if there are no tasks to complete in TODAY list
					JOptionPane.showMessageDialog(null, "Error: Need at least 1 incomplete task in \"TODAY\" List");
				else {
				scheduleView.setVisible(false); //hide the schedule view 
				workSession.workOnTasks(todoList); //Start working on today's tasks
				workView.run();
				}
			}
		}
		
		class ReopenListener extends ComponentAdapter {
			/**Handles user request (from WorkView) to re-open the ScheduleView and update it */
			@Override
			public void componentHidden( ComponentEvent e ) {
				workSchedule.updateObservers(); //updates the schedule view
				scheduleView.setVisible(true); //reopen schedule view
			}
		}
		
	    /**Helper method for several event listeners in this class including MovementListener and both Edit listeners.
	     * Converts a "TaskPanel" position or "Space" position (in the view) to an actual "Task" position in the Model.
	     * Conversion is required because the View uses "Space" placeholders for task positions which are not present in the Model
	     * @param viewComp - A TaskPanel or Space component from the View whose position you want to convert to a model position
	     */
	    public int getModelPosition( Object viewComp ) {
	    	int viewPosition;
	    	int modelPosition = 0;
	    	Container parentColumn;
	    			    	
	    	if( !(viewComp instanceof JPanel) )  //sanity check
	    		throw new IllegalArgumentException( "Error converting View position to Model");
	    	
	    	parentColumn =  ( ( (Container) viewComp).getParent());
	    	if( viewComp.getClass() == Space.class ) 
	    		viewPosition = 0; //Spaces are even, start at 0
	    	else
	    		viewPosition = 1; //TaskPanels are odd, start at 1
	    	
	    	while( viewPosition < parentColumn.getComponentCount() ) { //iterate through all parent Column's components till found
	    		if( parentColumn.getComponent(viewPosition) == viewComp ) 
	    			return modelPosition;
	    		else {
	    			viewPosition += 2; //Increment 2 because TaskPanels and Space alternates
	    			modelPosition++; 
	    		}
	    	}
	    	throw new IllegalStateException( "Error: Converting: viewComp not in its own container...");
	    }
	    

		
		
	}
}
