package ch.psi.synopview;

import java.awt.*;

public class StatusBar extends java.awt.Panel {

	private static final long serialVersionUID = 1L;
	
	java.awt.TextField statusText = new java.awt.TextField();
	
	public StatusBar() {
		setLayout(new BorderLayout(0,0));
		setSize(645,30);
		statusText.setEditable(false);
		add("Center",statusText);
		statusText.setBounds(0,0,478,30);

        statusText.setFont(new Font("Dialog", Font.BOLD, 12));
	}

	public void setText(String str) {
		statusText.setText(str);
	}

}
