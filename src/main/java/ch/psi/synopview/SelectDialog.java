package ch.psi.synopview;

import java.awt.*;
import java.lang.Object.*;
import java.util.*;
///test
import ch.psi.synopview.svp.devices.*;

public class SelectDialog extends java.awt.Dialog
{
	SVControlSystem sc;        // control interface

	public String[] asMachine = null;   // Names of buttons in x direction
	public String[] asSystem  = null;   // Names of buttons in y direction
	public String sAll        = new String (" ALL ");   // upper left button name
	public String sComa       = new String (",");       // seperator used to give checkboxes a name
	boolean[][] abCheckbox;                             // boolean array indicating the selected devices
	Checkbox[][] aCheckbox;                             // checkboxes to select devices from
	public Stack selectStack  = new Stack();            // filo buffer to store the selection array

	SymItem lSymItem          = new SymItem();
	
	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

    /// test...
    SystemsData systems;    // system data
	MachinesData machines;  // machine data


	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == SelectDialog.this)
				SelectDialog_WindowClosing(event);
		}
	}

	//{{DECLARE_CONTROLS
	java.awt.Panel centerPanel = new java.awt.Panel();
	java.awt.Panel eastPanel = new java.awt.Panel();
	java.awt.Panel buttonPanel = new java.awt.Panel();
	java.awt.Label bufferSizeLabel = new java.awt.Label();
	java.awt.Button viewButton = new java.awt.Button();
	java.awt.Button previousButton = new java.awt.Button();
	java.awt.Button okButton = new java.awt.Button();
	public class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			String sCommand = event.getActionCommand();
			Object object = event.getSource();
		    // System.out.println("Command = " + sCommand);
		    if (sCommand.equals(sAll)) {
				// if one checkbox is selected, the unselect all
				setCheckbox ("ALL", 0, !(getCheckboxSelected("ALL", 0)));
			} else if (object == viewButton)
				viewButton_ActionPerformed(event);
			else if (object == previousButton)
				previousButton_ActionPerformed(event);
			else if (object == okButton)
				okButton_ActionPerformed(event);
			else {
				for (int x=0; x<asMachine.length; x++) {
					if (sCommand.equals(asMachine[x])) {
						//System.out.println("Machine x = " + x + " " + asMachine[x]);
						setCheckbox ("COLUMN", x, !(getCheckboxSelected("COLUMN", x)));
					}
				}
				for (int y=0; y<asSystem.length; y++) {
					if (sCommand.equals(asSystem[y])) {
						//System.out.println("System y   = " + y + " " + asSystem[y]);
						setCheckbox ("ROW", y, !(getCheckboxSelected("ROW", y)));
					}
				}
				
			}
		}
	}
	
	
	public class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			String sItem = (String) event.getItem();
		 
			// get the x and y coords from the checkboxname (that is hidden)
			int iComa = sItem.indexOf(sComa);  // -1 not found
			String sX = sItem.substring(0, iComa);
			String sY = sItem.substring(iComa+1, sItem.length());
			// konvert the string to int
			int iX = parseChar(sX);
			int iY = parseChar(sY);
		   
			if (event.getStateChange() == event.SELECTED) {
				abCheckbox[iX][iY] = true;
				//System.out.println("SELECT   x = " + iX + ", y = " + iY);
				
			} else {
				abCheckbox[iX][iY] = false;
				//System.out.println("DESELECT x = " + iX + ", y = " + iY);
			
			}
		}
	}

	
	/*
	** constructor SelectDialog
	*/
	public SelectDialog(Frame parent)
	{
		super(parent);
		//{{INIT_CONTROLS
		setLayout(new BorderLayout(0,0));
		setSize(315,180);
		setVisible(false);
		centerPanel.setLayout(null);
		add("Center",centerPanel);
		centerPanel.setBackground(java.awt.Color.lightGray);
		centerPanel.setBounds(0,0,229,180);
		eastPanel.setLayout(new BorderLayout(0,0));
		add("East",eastPanel);
		eastPanel.setBackground(java.awt.Color.lightGray);
		eastPanel.setBounds(229,0,86,180);
		buttonPanel.setLayout(new GridLayout(4,1,0,0));
		eastPanel.add("South",buttonPanel);
		buttonPanel.setBackground(java.awt.Color.white);
		buttonPanel.setBounds(0,88,86,92);
		buttonPanel.add(bufferSizeLabel);
		bufferSizeLabel.setBackground(java.awt.Color.lightGray);
		bufferSizeLabel.setBounds(0,0,86,23);
		viewButton.setLabel("View");
		buttonPanel.add(viewButton);
		viewButton.setBackground(java.awt.Color.lightGray);
		viewButton.setBounds(0,23,86,23);
		previousButton.setLabel("    Previous    ");
		buttonPanel.add(previousButton);
		previousButton.setBackground(java.awt.Color.lightGray);
		previousButton.setBounds(0,46,86,23);
		okButton.setLabel("Close");
		buttonPanel.add(okButton);
		okButton.setBackground(java.awt.Color.lightGray);
		okButton.setBounds(0,69,86,23);
		setTitle("Select Dialog");
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		viewButton.addActionListener(lSymAction);
		previousButton.addActionListener(lSymAction);
		okButton.addActionListener(lSymAction);
		//}}
	}

	public SelectDialog(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);
	}
	public SelectDialog(Frame parent, boolean modal)
	{
		this(parent);
		setModal(modal);;
	}
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}
//===========================
	boolean[][] clone2dBooleanArray (boolean[][] ab) {
		boolean[][] xxabIndex = new boolean[asMachine.length][asSystem.length];
		// line below ?? ok asmachine.len
		for (int i=0; i<asMachine.length; i++) 
			xxabIndex[i] = (boolean[]) ab[i].clone();
		return xxabIndex;
	}
	//}}

//---------------------------
	/*
	** create Button in the Gridbaglayout 
	*/
	public void createButton(String name,
						   GridBagLayout gridbag,
						   GridBagConstraints c) {
		Button button = new Button(name);
		gridbag.setConstraints(button, c);

		SymAction lSymAction = new SymAction();
		button.addActionListener(lSymAction);
//        System.out.println("createButton("+name+")"); 
		centerPanel.add(button);
	}
	/*
	** create Checkboxe in Gridbaglayout
	*/
	public void createCheckbox(String name,
							 int x,
							 int y,
							 GridBagLayout gridbag,
							 GridBagConstraints c) {
 
		aCheckbox[x][y] = new Checkbox(name);
		aCheckbox[x][y].setForeground(Color.lightGray);
		gridbag.setConstraints(aCheckbox[x][y], c);
		aCheckbox[x][y].addItemListener(lSymItem);
		centerPanel.add(aCheckbox[x][y]);
	}
	/*
	** create the Table with Checkboxes
	** dimension
	** x = asMachine.length
	** y = asSystem.length
	** top row and left column are buttons labeld withasSystem, asMachine
	**
	** note: readMachineFile and readSystemFile must be called before createSelectElements
	*/
	public void createSelectElements() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

        //    System.out.println("MA = " + asMachine.length + ", SY = " + asSystem.length);
	    // boolean array to store the status of the select checkboxes
	    abCheckbox = new boolean[asMachine.length][asSystem.length];
	    // define Checkboxes 
	    aCheckbox = new Checkbox[asMachine.length][asSystem.length];
  
		// setFont(new Font("Helvetica", Font.PLAIN, 14));
		centerPanel.setLayout(gridbag);
   
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		//c.ipadx = 5;
		//c.ipady = 5;

		int x=0;
		for (int y=0; y<= asSystem.length; y++) {
			if ((y>0) && (x==asMachine.length)) {
				// make 1 button and the checkboxes
				x=0;
                // System.out.print("B" + x + "," + y + " ");
   	            c.gridwidth = 1;	   	   //reset to the default
				createButton(asSystem[y-1], gridbag, c);
				for (x=0; x<asMachine.length; x++) {
					if (x >= asMachine.length-1) {
						// right most button of the machine buttons (top)
	 	                c.gridwidth = GridBagConstraints.REMAINDER; //end row
					}
					createCheckbox(""+x+sComa+(y-1), x, (y-1), gridbag, c);
                    // System.out.print("C" + x + "," + (y-1) + " ");
				}
                // System.out.println("");
			} else {
				// top row, make all buttons with machine labels
				//System.out.print(sAll + "  ");
   	            c.gridwidth = 1;	   	   //reset to the default
				createButton(sAll, gridbag, c);
				for (x=0; x<asMachine.length; x++) {
					// make the top row Machine label buttons
					if (x >= asMachine.length-1) {
						// right most button of the machine buttons (top)
	 	                c.gridwidth = GridBagConstraints.REMAINDER; //end row
					}
					createButton(asMachine[x], gridbag, c);
					//System.out.print("B" + x + "," + y + " ");
				}
				//System.out.println("");
			} // end if
		}
		bufferSizeLabel.setText("Buffer = 0");
		
		// set all checkboxes and the corresponding boolean array to true
		setCheckbox ("ALL", 0, true);
	}
	/*
	** checks the boolean array wether the row, column or any
	** boxes are true
	** s = "ALL" and column = n     -> check all boxes
	** s = "ROW" and column = n     -> check ow n boxes
	** s = "COLUMN" and column = n  -> check column n boxes
	*/
	public boolean getCheckboxSelected(String s, int n) {
		if (s.equals("ALL")) {
			for (int y=0; y<asSystem.length; y++)
				for (int x=0; x<asMachine.length; x++)
		            if (abCheckbox[x][y])
		                return true;
			return false;
		}
		else if (s.equals("ROW")) {
			for (int x=0; x<asMachine.length; x++)
		        if (abCheckbox[x][n])
		            return true;
			return false;
		}
		else if (s.equals("COLUMN")) {
			for (int y=0; y<asSystem.length; y++)
		        if (abCheckbox[n][y])
		            return true;
			return false;
		}
		//System.out.println("noch nicht da");
		return false;
	}
	/*
	** return 0 if nothing is selected in the boolean Array...
	*/
	public int getFirstSelected() {
		for (int y=0; y<asSystem.length; y++)
			for (int x=0; x<asMachine.length; x++)
		        if (abCheckbox[x][y])
		            return x+1;
		return 0;
	}
//----------------------------------------

	void okButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		//System.out.println("OK");
		okButton_ActionPerformed_Interaction1(event);
	}
	void okButton_ActionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			setCheckbox ("ALL", 0, true);
		    setDevicesVisible (abCheckbox);
		} catch (Exception e) {
		    System.out.println("Error:   close Select Dialog...");
		}
		// when closing the select dialog enable the button again...
	    sc.sc_enableSelectButton(true);
	    dispose();
	}
	public int parseChar(String str) {
		if (str.equals("0"))
			return (0);
		else if (str.equals("1"))
			return (1);
		else if (str.equals("2"))
			return (2);
		else if (str.equals ("3"))
			return (3);
		else if (str.equals ("4"))
			return (4);
		else if (str.equals ("5"))
			return (5);
		else if (str.equals ("6"))
			return (6);
		else if (str.equals ("7"))
			return (7);
		else if (str.equals ("8"))
			return (8);
		else if (str.equals ("9"))
			return (9);
		else if (str.equals ("10"))
			return (10);
		else if (str.equals ("11"))
			return (11);
		else if (str.equals ("12"))
			return (12);
		else return (-1);
	}
	void previousButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		//System.out.println("Previous");
		if (! selectStack.empty()) {
			// clear    
			setCheckbox ("ALL", 0, false);
			abCheckbox = (boolean[][]) selectStack.pop();
			// set checkboxes to the current boolean array
			setCheckboxes(abCheckbox);
			//traceCheckboxes(abCheckbox);
			//System.out.println("----------------------------");
			bufferSizeLabel.setText("Buffer = " + selectStack.size());
			// select the checked devices in the drawing surface....	    
			setDevicesVisible (abCheckbox);
		} else 
			bufferSizeLabel.setText("Buffer EMPTY");
	}
	void SelectDialog_WindowClosing(java.awt.event.WindowEvent event)
	{
		//System.out.println("SelectDialog_WindowClosing()");
		// set all checkboxes and the corresponding boolean array to true
		try {
			setCheckbox ("ALL", 0, true);
		    setDevicesVisible (abCheckbox);
		} catch (Exception e) {
		    System.out.println("Error:   close Select Dialog...\nmaybe no Checkboxes have been created");
		}
		// when closing the select dialog enable the button again...
	    sc.sc_enableSelectButton(true);
	    dispose();
	}
	/*
	** this set or reset the checkboxes and the corresponding boolean array
	** ALL, ROW n, COLUMN n 
	*/
	public void setCheckbox (String s, int n, boolean set) {
		if (s.equals("ALL"))
			for (int y=0; y<asSystem.length; y++) {
				for (int x=0; x<asMachine.length; x++) {
		            abCheckbox[x][y] = set;
		            aCheckbox[x][y].setState(set);
		        }
		} else if (s.equals("ROW")) {
			for (int x=0; x<asMachine.length; x++) {
		        abCheckbox[x][n] = set;
		        aCheckbox[x][n].setState(set);
		    }
		} else if (s.equals("COLUMN")) {
			for (int y=0; y<asSystem.length; y++) {
	            abCheckbox[n][y] = set;
	            aCheckbox[n][y].setState(set);
			}
		}
	}
	/*
	** set the Checkboxed with the contents of the boolean Array
	*/
	public void setCheckboxes (boolean[][] abC) {
		for (int y=0; y<asSystem.length; y++) {
			for (int x=0; x<asMachine.length; x++) {
		        aCheckbox[x][y].setState(abC[x][y]);
				//if (abC[x][y])
				//    System.out.println("SET x = " +x + ", y = " + y);
		    }
		}
	}
	public void setControlSystem(SVControlSystem sc) {
	    this.sc=sc;
	}
	/*
	** set the checked devices visible in the drawing area
	*/
	void setDevicesVisible (boolean[][] cb) {
		for (int y=0; y<asSystem.length; y++) {
			for (int x=0; x<asMachine.length; x++) {
		        if (cb[x][y]) {
					sc.sc_selectDevice((x+1), (y+1), true);	
				} else 
					sc.sc_selectDevice((x+1), (y+1), false);	
		    }
		}
	}
	/*
	** set the String Array with Machine Names
	*/
	public void setMachineName (String[] as) {
		asMachine = as;
	}
	/*
	** set the String Array with System Names
	*/
	public void setSystemName (String[] as) {
		asSystem = as;
	}
	public void setVisible(boolean b)
	{
	    //System.out.println("setVisible " + b );
		if (b)
		{
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();

			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				bounds.y + (bounds.height - abounds.height)/2);
		} 
		super.setVisible(b);
	}
	/*
	** control print of the boolean Array...
	*/
	public void traceCheckboxes(boolean[][] abArray) {
		for (int y=0; y<asSystem.length; y++) {
			for (int x=0; x<asMachine.length; x++) {
		        if (abArray[x][y]) {
		            System.out.print("1");
		        } else
		            System.out.print("0");
		    }
		    System.out.println("");
		}
		System.out.println("------");
	}
	void viewButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		//System.out.println("View");
		selectStack.push(clone2dBooleanArray(abCheckbox));
		bufferSizeLabel.setText("Buffer = " + selectStack.size());

		// select the checked devices in the drawing surface....	    
		setDevicesVisible (abCheckbox);
	}


    public boolean readSystemFile(String fileName) {
	    systems = ch.psi.synopview.svp.parser.SVParser.parseSystems(fileName);
	    if (systems==null) return false;

        asSystem = new String[systems.getSystemSize()];
		asSystem = systems.getSystemName();

        return true;
    }
    
    public boolean readMachineFile(String fileName) {
	    machines = ch.psi.synopview.svp.parser.SVParser.parseMachines(fileName);
	    if (machines==null) return false;

        asMachine = new String[machines.getMachineSize()];
		asMachine = machines.getMachineName();
        
        return true;
    }
}
