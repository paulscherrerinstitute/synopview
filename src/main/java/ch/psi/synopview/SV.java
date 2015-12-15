package ch.psi.synopview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

/*
	This simple extension of the java.awt.Frame class
	contains all the elements necessary to act as the
	main window of an application.
 */

/*
** not yet implemented properly...:
**		processInterrestData  ()
**
**
*/

//import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.String;
import java.util.logging.Logger;

class SV extends java.awt.Frame implements SVControlSystem {
	
	private static final Logger logger = Logger.getLogger(SV.class.getName());

	private static final long serialVersionUID = 1L;
	
	// define some Strings for the ResultPanel Buttons
	static String RECONNECT_STRING = "ReConnect";
	static String DISCONNECT_STRING = "DisConnect";
	static String PLEASE_WAIT = "Please wait...";

	// boolean to indicate autoCenter is on or off
	boolean bAutoCenter = false;

	// Synoptic is the ...
	Synoptic synoptic;

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	java.awt.MenuBar mainMenuBar = new java.awt.MenuBar();
//	java.awt.Menu fileMenu = new java.awt.Menu();
	java.awt.MenuItem Separator2 = new java.awt.MenuItem("-");
//	java.awt.MenuItem exitMenuItem = new java.awt.MenuItem();
	java.awt.Menu viewMenu = new java.awt.Menu();
	java.awt.CheckboxMenuItem treeCheckBox = new java.awt.CheckboxMenuItem("Tree");
	java.awt.CheckboxMenuItem buttonsCheckBox = new java.awt.CheckboxMenuItem("Buttons");
	java.awt.MenuItem Separator1 = new java.awt.MenuItem("-");
	java.awt.CheckboxMenuItem autoCenterCheckBox = new java.awt.CheckboxMenuItem();

	class SymItem implements java.awt.event.ItemListener {
		public void itemStateChanged(java.awt.event.ItemEvent event) {
			Object object = event.getSource();
			if (object == treeCheckBox)
				treeCheckBox_ItemStateChanged(event);
			else if (object == buttonsCheckBox)
				buttonsCheckBox_ItemStateChanged(event);
			else if (object == autoCenterCheckBox)
				autoCenterCheckBox_ItemStateChanged(event);
		}
	}

	public SV() {
		
		setLayout(new BorderLayout(0, 0));
		setBackground(java.awt.Color.lightGray);
		setSize(403, 348);
		setVisible(false);
		setTitle("Synoptic Viewer - 1.21");

		viewMenu.setLabel("View");
		treeCheckBox.setLabel("Tree");
		treeCheckBox.setState(true);
		viewMenu.add(treeCheckBox);
		buttonsCheckBox.setLabel("Buttons");
		buttonsCheckBox.setState(true);
		viewMenu.add(buttonsCheckBox);
		viewMenu.add(Separator1);
		viewMenu.add(Separator2);
		autoCenterCheckBox.setLabel("Auto Center");
		autoCenterCheckBox.setState(false);
		viewMenu.add(autoCenterCheckBox);
		mainMenuBar.add(viewMenu);
		setMenuBar(mainMenuBar);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		SymItem lSymItem = new SymItem();
		treeCheckBox.addItemListener(lSymItem);
		buttonsCheckBox.addItemListener(lSymItem);
		autoCenterCheckBox.addItemListener(lSymItem);

		// --------------------------------------
		setSize(800, 600); // this is needed and set the size
		synoptic = new Synoptic(this);

		this.add("West", synoptic.getTreeControl());
		this.add("Center", synoptic.getDrawingSurface());
		this.add("South", synoptic.getStatusBar());
		this.add("East", synoptic.getResultPanel());

		// read System and Machine Files for the Select Dialog
		if (synoptic.readMachineFile("SV_machine.txt") != true) {
			logger.warning("Unable to read machine file");
			System.exit(1);
		}
		if (synoptic.readSystemFile("SV_system.txt") != true) {
			logger.warning("Unable to read system file");
			System.exit(1);
		}
		// when the Machine and System files are read correct,
		// create the Elements in the SelectDialog
		synoptic.createSelectElements();
	}

	public void addNotify() {
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++) {
			Point p = components[i].getLocation();
			p.translate(getInsets().left, getInsets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	void autoCenterCheckBox_ItemStateChanged(java.awt.event.ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED) {
			bAutoCenter = true;
		} else {
			bAutoCenter = false;
		}
	}

	/*
	 ** menu view buttons ItemStateChenged display or hide the ResultPanel
	 */
	void buttonsCheckBox_ItemStateChanged(java.awt.event.ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED)
			this.add("East", synoptic.getResultPanel());
		else
			this.remove(synoptic.getResultPanel());
		this.validate();
	}


	static public void main(String args[]) {
		try {
			SV synop_view = new SV();
			synop_view.setVisible(true);

			if (!synop_view.synoptic.readColorFile("SV_color.txt"))
				System.out.println("Error:   reading file ... default color used!");

			if (synop_view.synoptic.readCoordFile("SV_coords.txt") != true) {
				System.out.println("Error:   reading file ... ");
				System.exit(1);
			}
		} catch (Exception e) {
			System.out.println("Error:   starting SV... ");
			System.err.println(e);
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Shows or hides the component depending on the boolean flag b.
	 * 
	 * @param b
	 *            if true, show the component; otherwise, hide the component.
	 * @see java.awt.Component#isVisible
	 */
	public void setVisible(boolean b) {
		if (b) {
			setLocation(50, 50);
		}
		super.setVisible(b);
	}

//	void SV_WindowClosing(java.awt.event.WindowEvent event) {
//		// to do: code goes here.
//
//		SV_WindowClosing_Interaction1(event);
//	}

//	void SV_WindowClosing_Interaction1(java.awt.event.WindowEvent event) {
//		System.exit(0);
//	}

	/*
	 ** menu view tree ItemStateChenged display or hide the treeContral panel
	 */
	void treeCheckBox_ItemStateChanged(java.awt.event.ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED)
			this.add("West", synoptic.getTreeControl());
		else
			this.remove(synoptic.getTreeControl());
		this.validate();
	}


	public void sc_baseview() {
		synoptic.getDrawingSurface().sc_baseview();
	}

	public void sc_centerDevice() {
		String sDevice = synoptic.getDrawingSurface().getSelectedDevice();
		synoptic.getDrawingSurface().sc_centerDevice(sDevice);
	}

	public boolean sc_getAutoCenter() {
		return bAutoCenter;
	}

	public void sc_enableSelectButton(boolean set) {
		synoptic.getResultPanel().selectButton.setEnabled(set);
	}

	public void sc_hilitedDevice(String deviceName) {
		synoptic.getStatusBar().setText(deviceName);
	}

	public void sc_previousView() {
		synoptic.getDrawingSurface().sc_previousView();
	}

	public void sc_select() {
		try {
			// show the Select Dialog...
			synoptic.getSelectDialog().setVisible(true);

			// when the select dialog is shown,
			// the "select" button in the ResultPanel will be disabled
			synoptic.getResultPanel().selectButton.setEnabled(false);

			synoptic.getSelectDialog().setControlSystem(this);

		} catch (Exception e) {
			System.out.println("Error:   creting Select Dialog..." + e);
		}
	}

	public void sc_selectDevice(int machine, int system, boolean visible) {
		// System.out.println("SV.selectDevice m = " +machine+ ", s = "+
		// (system-1));
		synoptic.setDevicesVisible(machine, system - 1, visible);
		synoptic.getDrawingSurface().repaint();
	}

	/**
	 * Set the DeviceName to the Center Button Label
	 * 
	 */
	public void sc_setSelectedDeviceLabel(String sDevice) {
		synoptic.getResultPanel().sc_setCenterLabel("center " + sDevice);
	}

	/**
	 ** showDeviceProperties() is the equal method as in SV v1.x called after
	 * clicking on a device
	 **
	 ** parameter: sDevice = Device name specfied as starting argument like
	 * ALIVA-VG-5 ...
	 **
	 */
	public void sc_showDeviceProperties(String sDevice) {
		// TODO This is the place to hook into the selection
		System.out.println("SHOW DEVICE "+sDevice);
	}

	/**
	 * When a device is double clicked, the Program defined as CommandLine will
	 * be executed (defined in the file SV_coords.txt)
	 *
	 * This event is executed from the DrawingSurface - MouseClicked...
	 */
	public void sc_startPanel(String commandLine) {
		// System.out.println("Info: startPanel(\""+commandLine+"\")");
		ExecProgram.run(commandLine);
	}

	public void sc_zoomIn() {
		synoptic.getDrawingSurface().sc_zoomin();
	}

	public void sc_zoomOut() {
		synoptic.getDrawingSurface().sc_zoomout();
	}

	public void sc_setTextArea(String sInfo) {
		synoptic.getResultPanel().sc_setTextArea(sInfo);
	}

	public void sc_appendTextArea(String sInfo) {
		synoptic.getResultPanel().sc_appendTextArea(sInfo);
	}

}
