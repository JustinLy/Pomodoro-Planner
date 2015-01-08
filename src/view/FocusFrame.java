package view;

import javax.swing.JFrame;

public class FocusFrame extends JFrame {
	public @Override void toFront() {
	    int  sta = super.getExtendedState()&~JFrame.ICONIFIED&JFrame.NORMAL;

	    super.setExtendedState(sta);
	    super.setAlwaysOnTop(true);
	    super.toFront();
	    super.requestFocus();
	    super.setAlwaysOnTop(false);
	    }
}
