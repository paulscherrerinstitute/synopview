import java.awt.*;
import java.beans.*;
import com.sun.java.swing.*;

public class StatusBar extends java.awt.Panel 
{
	//{{DECLARE_CONTROLS
	java.awt.TextField statusText = new java.awt.TextField();
	java.awt.Panel eastPanel = new java.awt.Panel();
	java.awt.Label timelabel = new java.awt.Label();
	public StatusBar()
	{

		//{{INIT_CONTROLS
		setLayout(new BorderLayout(0,0));
		Insets ins = getInsets();
		setSize(645,30);
		statusText.setEditable(false);
		add("Center",statusText);
		statusText.setBounds(0,0,478,30);
		eastPanel.setLayout(new BorderLayout(0,0));
		add("East",eastPanel);
		eastPanel.setBackground(java.awt.Color.lightGray);
		eastPanel.setBounds(478,0,167,30);
		timelabel.setText(" Tue Nov 30 1999  11:00:00 ");
		eastPanel.add("East", timelabel);
		timelabel.setBackground(java.awt.Color.lightGray);
		timelabel.setBounds(0,0,167,30);
		//}}

        // to make the Device Name better visible...
        statusText.setFont(new Font("Dialog", Font.BOLD, 12));

		//{{REGISTER_LISTENERS
		//}}
	}
	public void setText(String str) {
		statusText.setText(str);
	}
	//}}

	public void setTimeText(String str) {
		timelabel.setText(str);
	}
}
