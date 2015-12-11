package ch.psi.synopview;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextField;

public class StatusBar extends Panel {

	private static final long serialVersionUID = 1L;
	
	private TextField statusText;
	
	public StatusBar() {
		statusText = new TextField();
		statusText.setEditable(false);
		statusText.setBounds(0,0,478,30);
        statusText.setFont(new Font("Dialog", Font.BOLD, 12));
		
		setLayout(new BorderLayout(0,0));
		setSize(645,30);
		add("Center", statusText);
	}

	public void setText(String str) {
		statusText.setText(str);
	}

}
