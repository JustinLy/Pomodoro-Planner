package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.BoxLayout;

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
	JButton settingsButton;
	JButton addButton;
	JButton workButton;
	JButton saveButton;
	JButton completeButton;
	
	Column todayColumn;
	Column tomorrowColumn;
	Column dayAfterColumn;
	List<Column> columns = new ArrayList<Column>();
	

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public static void main( String[] args ) {
		ScheduleView blah = new ScheduleView();
		blah.frame.pack();
		blah.frame.setVisible(true);
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
		topHalf.setLayout(new MigLayout("", "[][push,grow][][push,grow][]", "[growprio 0][][][][][][][]"));
		
		settingsButton = new JButton("Settings");
		settingsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		settingsButton.setHorizontalTextPosition(SwingConstants.CENTER);
		settingsButton.setIcon(new ImageIcon(ScheduleView.class.getResource("/javax/swing/plaf/metal/icons/ocean/homeFolder.gif")));
		topHalf.add(settingsButton, "flowy,cell 0 0");
		
		JLabel lblPomodoroPlanner = new JLabel("Pomodoro Planner");
		lblPomodoroPlanner.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblPomodoroPlanner.setHorizontalAlignment(SwingConstants.CENTER);
		topHalf.add(lblPomodoroPlanner, "cell 2 0,alignx center,aligny center");
		
		saveButton = new JButton("Save");
		saveButton.setIcon(new ImageIcon(ScheduleView.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		topHalf.add(saveButton, "cell 4 0,alignx right,aligny top");
		
		addButton = new JButton("+ Task");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addButton.setFocusable(false);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "clicked");
			}
		});
		topHalf.add(addButton, "cell 0 7");
		
		workButton = new JButton("      Start Working     ");
		workButton.setBackground(Color.GREEN);
		workButton.setContentAreaFilled(false);
		workButton.setOpaque(true);
		workButton.setBorder(BorderFactory.createLineBorder(Color.blue));
		topHalf.add(workButton, "cell 2 7,alignx center");
		
		completeButton = new JButton("<html>Complete<br />Day</html>");
		completeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		completeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		topHalf.add(completeButton, "cell 4 7, alignx right, aligny top");
		
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
			
			for( int ind = 0; ind < 3; ind ++ ) { 
				columns.get(ind).removeAll(); //Remove the old task list information from each column
				columns.get(ind).add(new Space() ); //Add initial spaces to columns (to support inserting tasks at top of lists)
			}
			
			for( int ind1 = 0; ind1 < 3; ind1++ ) { //Iterate through all 3 tasklists
				List<Task> currentTaskList = schedule.getTaskList(ind1);
				Column currentColumn = columns.get(ind1);
				
				for( int ind2 = 0; ind2 < currentTaskList.size(); ind2++ ) { //Iterate through tasks in each list
					Task currentTask = currentTaskList.get(ind2);
					currentColumn.add( new TaskPanel( currentTask.getTaskName(), currentTask.getTaskLength())); //Add TaskPanel with currentTask's info
					currentColumn.add(new Space()); //Add a Space placeholder after each TaskPanel
				}
			}
		}
	} 
}
