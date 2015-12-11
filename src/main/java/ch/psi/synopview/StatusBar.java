package ch.psi.synopview;

import java.awt.*;

public class StatusBar extends java.awt.Panel {

	private static final long serialVersionUID = 1L;
	
	java.awt.TextField statusText = new java.awt.TextField();
	java.awt.Panel eastPanel = new java.awt.Panel();
	java.awt.Label timelabel = new java.awt.Label();
	
	public StatusBar() {
		setLayout(new BorderLayout(0,0));
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

        statusText.setFont(new Font("Dialog", Font.BOLD, 12));
	}

	public void setText(String str) {
		statusText.setText(str);
	}

	public void setTimeText(String str) {
		timelabel.setText(str);
	}
}
