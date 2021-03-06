package view;

import java.awt.Cursor;  
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.FlowLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.WorkSchedule;
import model.Task;

public class ScheduleView implements Observer {
	private JFrame frame;
	private JButton settingsButton;
	private JButton addButton;
	private JButton workButton;
	private JButton saveButton;
	private JButton completeButton;
	
	private Column todayColumn;
	private Column tomorrowColumn;
	private Column dayAfterColumn;
	private List<Column> columns = new ArrayList<Column>();
	private JLabel pausedTaskLabel;
	

	/**
	 * Launch the application.
	 */
	public void run() {
		
		this.frame.pack();
		this.frame.setVisible(true);
	}
	public ScheduleView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setPreferredSize(new Dimension(1250, 750));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[434px,grow]", "[71.00px][177.00px][grow]"));
		
		/**Top half of the ScheduleView. Contains most of the buttons*/
		JPanel topHalf = new JPanel();
		frame.getContentPane().add(topHalf, "cell 0 0,grow");
		topHalf.setLayout(new MigLayout("", "[][push,grow][][push,grow][]", "[growprio 0][][][][][][][][]"));
		
		settingsButton = new JButton("Settings");
		settingsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		settingsButton.setHorizontalTextPosition(SwingConstants.CENTER);
		settingsButton.setIcon(new ImageIcon(ScheduleView.class.getResource("/javax/swing/plaf/metal/icons/ocean/homeFolder.gif")));
		topHalf.add(settingsButton, "flowy,cell 0 0,growx");
		
		JLabel lblPomodoroPlanner = new JLabel("Pomodoro Planner");
		lblPomodoroPlanner.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblPomodoroPlanner.setHorizontalAlignment(SwingConstants.CENTER);
		topHalf.add(lblPomodoroPlanner, "cell 2 0,growx,alignx center,aligny center");
		
		saveButton = new JButton("Save");
		saveButton.setIcon(new ImageIcon(ScheduleView.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		topHalf.add(saveButton, "cell 4 0,growx,alignx right,aligny top");
		
		pausedTaskLabel = new JLabel("");
		topHalf.add(pausedTaskLabel, "cell 2 3,alignx center");
		
		addButton = new JButton("+ Task");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addButton.setFocusable(false);
		
		topHalf.add(addButton, "cell 0 8,growx");
		
		workButton = new JButton("Start Working");
		workButton.setFont(new Font("Arial", Font.PLAIN, 18));
		workButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		workButton.setBackground(Color.GREEN);
		//workButton.setContentAreaFilled(true);
		//workButton.setOpaque(true);
		//workButton.setBorder(BorderFactory.createLineBorder(Color.blue));
		topHalf.add(workButton, "cell 2 8,grow");
		
		completeButton = new JButton("<html>Complete<br />Day</html>");
		completeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		completeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		topHalf.add(completeButton, "cell 4 8,growx,alignx right,aligny top");
		
		/**Bottom half of the ScheduleView. Contains the schedule*/
		
		JPanel schedulePanel = new JPanel();
		schedulePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(schedulePanel, "cell 0 1 1 2,grow");
		schedulePanel.setLayout(new GridLayout(0, 3, 0, 0));
				
		//Building the "Today" Panel
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		schedulePanel.add(scrollPane);
		
		todayColumn = new Column(WorkSchedule.TODAY);
		scrollPane.setViewportView(todayColumn);
		todayColumn.setLayout(new MigLayout("fillx"));
		scrollPane.setColumnHeaderView(todayColumn.getLabel());
		
		//Building the Tomorrow Panel
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		schedulePanel.add(scrollPane_1);
		
		tomorrowColumn = new Column(WorkSchedule.TOMORROW);
		tomorrowColumn.setLayout(new MigLayout("fillx"));
		scrollPane_1.setViewportView(tomorrowColumn);
		scrollPane_1.setColumnHeaderView(tomorrowColumn.getLabel());
	
		
		//Building the Day After Panel
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		schedulePanel.add(scrollPane_2);
		
		dayAfterColumn = new Column(WorkSchedule.DAY_AFTER);
		dayAfterColumn.setLayout(new MigLayout("fillx"));
		scrollPane_2.setViewportView(dayAfterColumn);
		scrollPane_2.setColumnHeaderView(dayAfterColumn.getLabel());
		
		//Add all 3 columns to List for update processing later
		columns.add(todayColumn);
		columns.add(tomorrowColumn);
		columns.add(dayAfterColumn);
		
	}
	
	/**
	 * Adds ALL the required EventListeners from the controller to the components in this ScheduleView
	 */
	public void registerListeners( EventListener[] listeners) {
		for( Column col : columns ) { //Add the ColumnComponentsListener to all 3 columns
			col.addContainerListener( (ContainerAdapter) listeners[0]);
		}
		
		addButton.addActionListener( (ActionListener) listeners[1]); //Add addButtonListener to addButton
		settingsButton.addActionListener( (ActionListener) listeners[2]); //Add SettingsListener to settingsButton
		completeButton.addActionListener( (ActionListener) listeners[3]); //Add completeDayListener to completeButton
		saveButton.addActionListener( (ActionListener) listeners[4]); //Add SaveListener to saveButton
		workButton.addActionListener( (ActionListener) listeners[5]); //Add WorkListener to workButton
	}
	/**
	 * Receives information about the WorkSchedule model from the controller and displays its
	 * information onto this ScheduleView.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if( arg0.getClass() != WorkSchedule.class) {
			throw new IllegalArgumentException( "Error receiving WorkSchedule data into view\n");
		}
		else {
			WorkSchedule schedule = (WorkSchedule) arg0;
			int currentTaskPosition = 1; //Used to calculate the position of the current task worked on in the list
			
			for( int ind = 0; ind < WorkSchedule.NUM_COLUMNS; ind ++ ) { 
				columns.get(ind).removeAll(); //Remove the old task list information from each column
				columns.get(ind).add(new Space(), "wrap" ); //Add initial spaces to columns (to support inserting tasks at top of lists)
			}
			
			for( int ind1 = 0; ind1 < WorkSchedule.NUM_COLUMNS; ind1++ ) { //Iterate through all 3 tasklists
				List<Task> currentTaskList = schedule.getTaskList(ind1);
				Column currentColumn = columns.get(ind1);
				
				for( int ind2 = 0; ind2 < currentTaskList.size(); ind2++ ) { //Iterate through tasks in each list
					Task task = currentTaskList.get(ind2);
					TaskPanel newPanel = new TaskPanel( task.getTaskName(), task.getTaskLength());
					if( task.isComplete() ) {//Make TaskPanel uneditable if Task is complete
						newPanel.setUneditable();
						currentTaskPosition +=2; //Increase the position since this task is complete (2 to skip over Space panels)
					}
					currentColumn.add( newPanel, "wrap, align center"); //Add TaskPanel with currentTask's info
					currentColumn.add(new Space(), "wrap"); //Add a Space placeholder after each TaskPanel
				}

				//System.out.println( ( (TaskPanel)columns.get(WorkSchedule.TODAY).getComponent(currentTaskPosition) ).getName() );
			}
			if( currentTaskPosition < columns.get(WorkSchedule.TODAY).getComponentCount() ) //Guard to prevent index out of bounds
//				 columns.get(WorkSchedule.TODAY).getComponent(currentTaskPosition).setBackground(Color.GREEN); //Highlight the current task being worked on
			//TODO: Make the green highlight only happen when PAUSE is clicked. 
			
			frame.revalidate();
			frame.repaint();
		}
	}
	
	/**
	 * Toggles between default cursor and moving cursor (dragging tasks) 
	 * @param moving - A boolean indicating whether the cursor should be MOVING CURSOR or not
	 */
	public void setMovingCursor(boolean moving ) {
		if( moving )
			frame.setCursor( Cursor.MOVE_CURSOR);
		else
			frame.setCursor(Cursor.DEFAULT_CURSOR);
	}
	
	/**Sets schedule view visibility, for when WorkView is on */
	public void setVisible( boolean visible) {
		frame.setVisible(visible);
	}
}