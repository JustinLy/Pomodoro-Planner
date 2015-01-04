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

public class ScheduleView {
	public static int halp;
	private JFrame frame;
	
	//Constants to identify the day that a "Column" corresponds to
	public static int TODAY = 0;
	public static int TOMORROW = 1;
	public static int DAY_AFTER = 2;
	
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
		
		//Top half of the ScheduleView. Contains most of the buttons
		JPanel topHalf = new JPanel();
		frame.getContentPane().add(topHalf, "cell 0 0,grow");
		topHalf.setLayout(new MigLayout("", "[][push,grow][][push,grow][]", "[growprio 0][][][][][][][]"));
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		settingsButton.setHorizontalTextPosition(SwingConstants.CENTER);
		settingsButton.setIcon(new ImageIcon(ScheduleView.class.getResource("/javax/swing/plaf/metal/icons/ocean/homeFolder.gif")));
		topHalf.add(settingsButton, "flowy,cell 0 0");
		
		JLabel lblPomodoroPlanner = new JLabel("Pomodoro Planner");
		lblPomodoroPlanner.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblPomodoroPlanner.setHorizontalAlignment(SwingConstants.CENTER);
		topHalf.add(lblPomodoroPlanner, "cell 2 0,alignx center,aligny center");
		
		JButton saveButton = new JButton("Save");
		saveButton.setIcon(new ImageIcon(ScheduleView.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
		topHalf.add(saveButton, "cell 4 0,alignx right,aligny top");
		
		JButton addButton = new JButton("+ Task");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		topHalf.add(addButton, "cell 0 7");
		
		JButton workButton = new JButton("      Start Working     ");
		workButton.setBackground(Color.GREEN);
		workButton.setContentAreaFilled(false);
		workButton.setOpaque(true);
		workButton.setBorder(BorderFactory.createLineBorder(Color.blue));
		topHalf.add(workButton, "cell 2 7,alignx center");
		
		JButton completeButton = new JButton("<html>Complete<br />Day</html>");
		completeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		completeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		topHalf.add(completeButton, "cell 4 7, alignx right, aligny top");
		
		//Bottom half of the ScheduleView. Contains the schedule
		
		JPanel schedulePanel = new JPanel();
		schedulePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(schedulePanel, "cell 0 1 1 2,grow");
		schedulePanel.setLayout(new GridLayout(0, 3, 0, 0));
				
		//Building the "Today" Panel
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		schedulePanel.add(scrollPane);
		
		JLabel lblToday = new JLabel("Today");
		lblToday.setOpaque(true);
		lblToday.setBackground(new Color(255, 255, 0));
		lblToday.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblToday.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblToday);
		lblToday.addMouseListener( new MouseAdapter() {
			public void mouseClicked( MouseEvent e ) {
				lblToday.requestFocusInWindow();
			}
		});
		Column todayColumn = new Column();
		scrollPane.setViewportView(todayColumn);
		todayColumn.setLayout(new MigLayout("fillx"));
		
		//Building the Tomorrow Panel
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		schedulePanel.add(scrollPane_1);
		
		JLabel lblTomorrow = new JLabel("Tomorrow");
		lblTomorrow.setOpaque(true);
		lblTomorrow.setBackground(new Color(255, 0, 0));
		lblTomorrow.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTomorrow.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblTomorrow);
		
		Column tomorrowColumn = new Column();
		tomorrowColumn.setLayout(new MigLayout("fillx"));
		scrollPane_1.setViewportView(tomorrowColumn);
		
		//Building the Day After Panel
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		schedulePanel.add(scrollPane_2);
		
		JLabel lblDayAfter = new JLabel("Day After");
		lblDayAfter.setOpaque(true);
		lblDayAfter.setBackground(new Color(30, 144, 255));
		lblDayAfter.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDayAfter.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_2.setColumnHeaderView(lblDayAfter);
		
		Column dayAfterColumn = new Column();
		dayAfterColumn.setLayout(new MigLayout("fillx"));
		scrollPane_2.setViewportView(dayAfterColumn);
	}
}
