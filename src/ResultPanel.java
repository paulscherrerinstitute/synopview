import java.awt.*;import com.sun.java.swing.*;public class ResultPanel extends java.awt.Panel{	// sv control interface	SVControlSystem sc;	public ResultPanel()	{		//{{INIT_CONTROLS		setLayout(new BorderLayout(0,0));		//Insets ins = getInsets();		setSize(200,500);		buttonPanel.setLayout(null);		add("North",buttonPanel);		buttonPanel.setBackground(java.awt.Color.lightGray);		buttonPanel.setBounds(0,0,200,192);		previousButton.setLabel("previous view");		buttonPanel.add(previousButton);		previousButton.setBounds(12,36,180,24);		reconnectButton.setLabel("reconnect");		buttonPanel.add(reconnectButton);		reconnectButton.setBounds(120,84,72,24);		centerButton.setLabel("center");		buttonPanel.add(centerButton);		centerButton.setBounds(12,60,180,24);		zoomOutButton.setLabel("zoom out");		buttonPanel.add(zoomOutButton);		zoomOutButton.setBounds(12,12,60,24);		statusText.setEditable(false);		buttonPanel.add(statusText);		statusText.setBackground(java.awt.Color.white);		statusText.setBounds(12,84,108,24);		baseViewButton.setLabel("base view");		buttonPanel.add(baseViewButton);		baseViewButton.setBounds(72,12,60,24);		textAreaLabel.setEditable(false);		buttonPanel.add(textAreaLabel);		textAreaLabel.setBackground(java.awt.Color.white);		textAreaLabel.setBounds(9,162,180,24);		zoomInButton.setLabel("zoom in");		buttonPanel.add(zoomInButton);		zoomInButton.setBounds(132,12,60,24);		selectButton.setLabel("select");		buttonPanel.add(selectButton);		selectButton.setBounds(129,111,60,24);		buttonPanel.add(interrestChoice);		interrestChoice.setBounds(96,138,93,25);		interrestLabel.setText("interrest group");		buttonPanel.add(interrestLabel);		interrestLabel.setBounds(12,132,72,36);		add("Center",textArea);		textArea.setFont(new Font("MonoSpaced", Font.PLAIN, 12));		textArea.setBounds(0,192,200,308);		//}}		//{{REGISTER_LISTENERS		SymAction lSymAction = new SymAction();		zoomInButton.addActionListener(lSymAction);		previousButton.addActionListener(lSymAction);		zoomOutButton.addActionListener(lSymAction);		baseViewButton.addActionListener(lSymAction);		centerButton.addActionListener(lSymAction);		reconnectButton.addActionListener(lSymAction);		selectButton.addActionListener(lSymAction);		SymItem lSymItem = new SymItem();		interrestChoice.addItemListener(lSymItem);		//}}				GridBagLayout gridbag = new GridBagLayout();		//--------------------------------------------------------				GridBagConstraints c = new GridBagConstraints();	    	    buttonPanel.setLayout(gridbag);		c.weightx = 1.0;		c.ipady = 5;   // additional height of cells		c.insets = new java.awt.Insets(1, 1, 1, 1);				// set for all buttons...        // zoomIn        c.gridx=0; c.gridy=0;		c.fill = GridBagConstraints.HORIZONTAL;		//c.anchor = GridBagConstraints.WEST;		c.gridwidth = 1;	   	   //reset to the default        gridbag.setConstraints(zoomInButton, c);                // baseView        c.gridx=1; c.gridy=0;        gridbag.setConstraints(baseViewButton, c);        // zoomOut        c.gridx=2; c.gridy=0;		//c.anchor = GridBagConstraints.EAST;        //c.gridwidth = GridBagConstraints.REMAINDER; //end row        gridbag.setConstraints(zoomOutButton, c);        // previous        c.gridx=0; c.gridy=1;		c.gridwidth = 3;        gridbag.setConstraints(previousButton, c);                // center        c.gridx=0; c.gridy=2;        gridbag.setConstraints(centerButton, c);                // select        c.gridx=0; c.gridy=3;		c.gridwidth = 1;        gridbag.setConstraints(selectButton, c);						// statusText        c.gridx=0; c.gridy=4;		c.gridwidth = 2;	   	   //reset to the default        gridbag.setConstraints(statusText, c);                // reconnect        c.gridx=2; c.gridy=4;		c.gridwidth = 1;        gridbag.setConstraints(reconnectButton, c);        // left of "select" button" is free...        c.gridx=0; c.gridy=5;		c.gridwidth = 2;        // gridbag.setConstraints(widget, c);        // select        c.gridx=2; c.gridy=5;		c.gridwidth = 1;        gridbag.setConstraints(selectButton, c); 		// interrestLabel        c.gridx=0; c.gridy=6;		c.gridwidth = 1;        gridbag.setConstraints(interrestLabel, c);        // interrestChaoice        c.gridx=2; c.gridy=6;		c.gridwidth = 2;        gridbag.setConstraints(interrestChoice, c);        // textAreaLabel        c.gridx=0; c.gridy=7;		c.gridwidth = 3;		c.insets = new java.awt.Insets(1, 1, 10, 1);    // add some space at the end..        gridbag.setConstraints(textAreaLabel, c);	}	//{{DECLARE_CONTROLS	java.awt.Panel buttonPanel = new java.awt.Panel();	java.awt.Button previousButton = new java.awt.Button();	java.awt.Button reconnectButton = new java.awt.Button();	java.awt.Button centerButton = new java.awt.Button();	java.awt.Button zoomOutButton = new java.awt.Button();	java.awt.TextField statusText = new java.awt.TextField();	java.awt.Button baseViewButton = new java.awt.Button();	java.awt.TextField textAreaLabel = new java.awt.TextField();	java.awt.Button zoomInButton = new java.awt.Button();	java.awt.Button selectButton = new java.awt.Button();	java.awt.Choice interrestChoice = new java.awt.Choice();	java.awt.Label interrestLabel = new java.awt.Label();	java.awt.TextArea textArea = new java.awt.TextArea(1,2);	//}}    /**    * set the label of the center label to the last clicked device    * default is: "center last clicked device"    */    public void sc_setCenterLabel (String s) {	    centerButton.setVisible(true);	    centerButton.setLabel(s);    }      public void setSVControlSystem(SVControlSystem sc) {	    this.sc=sc;    }      public void sc_setTextArea(String str) {        textArea.setText(str);    }      public void sc_setTextAreaLabel(String str) {        textAreaLabel.setText(str);    }      public void sc_appendTextArea(String str) {        textArea.append(str);    }     public void setConnectButtonLabel(String s) {        reconnectButton.setLabel(s);    }	class SymAction implements java.awt.event.ActionListener	{		public void actionPerformed(java.awt.event.ActionEvent event)		{			Object object = event.getSource();			if (object == zoomInButton)				zoomInButton_ActionPerformed(event);			else if (object == previousButton)				previousButton_ActionPerformed(event);			else if (object == zoomOutButton)				zoomOutButton_ActionPerformed(event);			else if (object == baseViewButton)				baseViewButton_ActionPerformed(event);			else if (object == centerButton)				centerButton_ActionPerformed(event);			else if (object == reconnectButton)				reconnectButton_ActionPerformed(event);			else if (object == selectButton)				selectButton_ActionPerformed(event);		}	}	void zoomInButton_ActionPerformed(java.awt.event.ActionEvent event)	{        sc.sc_zoomIn();	}	void previousButton_ActionPerformed(java.awt.event.ActionEvent event)	{	    sc.sc_previousView();	}	void zoomOutButton_ActionPerformed(java.awt.event.ActionEvent event)	{        sc.sc_zoomOut();    }	void baseViewButton_ActionPerformed(java.awt.event.ActionEvent event)	{        sc.sc_baseview();	    }	void centerButton_ActionPerformed(java.awt.event.ActionEvent event)	{		sc.sc_centerDevice();	}	void reconnectButton_ActionPerformed(java.awt.event.ActionEvent event)	{		sc.sc_reconnectCdev();			 	}	void selectButton_ActionPerformed(java.awt.event.ActionEvent event)	{		sc.sc_select();	}	class SymItem implements java.awt.event.ItemListener	{		public void itemStateChanged(java.awt.event.ItemEvent event)		{			Object object = event.getSource();			if (object == interrestChoice)				interrestChoice_ItemStateChanged(event);					}	}	void interrestChoice_ItemStateChanged(java.awt.event.ItemEvent event)	{        // and clear the TextArea......	    sc.sc_setTextArea("");	    // and set the new InterrestGroup...		sc.sc_setInterrestGroup((String) event.getItem());	}    public void addInterrestChoice(String s) {        try {	        //System.out.println("ResultPanel addInterrestChoice..." + s);            interrestChoice.add(s);	    } catch (Exception e) {	        System.out.println("Error: ResultPanel addInterrestChoice...");	    }    }}