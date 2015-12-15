package ch.psi.synopview;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import ch.psi.synopview.svp.devices.Device;
import ch.psi.synopview.svp.parser.SVParser;
import ch.psi.synopview.svp.devices.*;
import ch.psi.synopview.svp.visual.*;

/**
 * DrawingSurface (double-buffered, can use ScrollPane)
 * Creation date: (12/7/99 15:45:43)
 * @author: Matej Sekoranja
 */
public class DrawingSurface extends Canvas implements MouseListener, MouseMotionListener {
	// hierarchy hasthtable
	Hashtable hierarchy = null;

	// double-buffering variables (ups, tripple-buffering actually)
	Image offscreen;
	Dimension offscreensize;
	Graphics offgraphics;

	Image devicesImage;
	Dimension devicesSize;
	Graphics devicesGraphics;
	
	Image navigatorImage;				// nav. img. (created on on coord. load)
	Dimension navigatorSize;
	Graphics navigatorGraphics;
	ViewProperties navigatorView;		// view prop.
	Rectangle navigator;				// navigator area

	boolean navigatorDrag = false;

	// does not refersh devices only redraws previous buffer image
	boolean fastDrawing = false;		
	boolean fastDrawingOnce = false;		
	// does not refersh devices only draw a hilited object (executed only once)
	boolean drawOnlyHilitedOnce = false;		
	
	// list of all devices
	DevicesData data;

	// machine data
	MachinesData machines;

	// system data
///	SystemsData systems;

	// previous cursor
	Cursor previousCursor = null;

	// current view
	ViewProperties view;

	// ... and history
	ViewHistory viewHistory = null;

	// special mouse-action switches
	boolean mouseSelection = false;

	// temp. coordinates
	int tx1, ty1, tx2, ty2;

	// cursors
	Cursor defaultCursor = null;
	Cursor handCursor = null;
	
	boolean bDrawCenterlines = false;
	// last selected Device (last clicked Device)
	String slastDevice = null;	
	
	// control interface
	SVControlSystem sc;

	TreeControl treeControl;
	//{{DECLARE_CONTROLS
	//}}
/**
 * Insert the method's description here.
 * Creation date: (2/9/99 16:54:40)
 */
public DrawingSurface() {
	data = new DevicesData();
	view = new ViewProperties();
	setBackground(Constants.backgroundColor);
	navigator = new Rectangle(0, 0, 0, 0);

	defaultCursor = getCursor();
	handCursor = new Cursor(Cursor.HAND_CURSOR);

		//{{INIT_CONTROLS
		//}}
	
		//{{REGISTER_LISTENERS
		SymComponent aSymComponent = new SymComponent();
		this.addComponentListener(aSymComponent);
		//}}
	}

/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:36:37)
 * @param groups java.util.Hashtable
 * @param dev svp.devices.Device
 */
private void add2groups(Hashtable groups, Device dev) {
	if (dev.getParentName().equalsIgnoreCase("VOID")) return;
	
	GroupData gd = (GroupData)(groups.get(dev.getParentName()));
	if (gd==null) {
		// gm 02suppress this message if grp = void_grp, because i modified groups.txt
		if (dev.getParentName().equalsIgnoreCase("void_grp"));
		else
		  System.out.println("Warning: Device '"+dev.getName()+"' is member of '"+dev.getParentName()+
							"' group, which is not defined... Device not added to hierarchy...");
	}
	else {
		if (gd.getTable().containsKey(dev.getName()) && 
			!dev.getName().equalsIgnoreCase("VOID"))			// multiple VOID dev. are allowed
			System.out.println("Warning: Device with name '"+dev.getName()+"' already exist in group '"+dev.getParentName()+
								"'... Device not added to hierarchy...");
		else
			gd.getTable().put(dev.getName(), dev);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 15:16:39)
 */
public void addMouseListeners() {
	addMouseListener(this);
	addMouseMotionListener(this);
}
/**
 * Insert the method's description here.
 * Creation date: (16/7/99 17:52:44)
 */
public void sc_baseview() {
	Dimension d = getViewSize();
 	view.setBasicView(d);
	repaint();
}
/**
 * Insert the method's description here.
 * Creation date: (11/19/99 16:53:15)
 */
public void sc_centerDevice(String deviceName) {
	Device dev = data.getDevice(deviceName);
	if (dev==null) return;

	getViewHistory().addView(new HistoryViewData(view.rx, view.ry, view.scale));

	Polygon poly = new Polygon(dev.getDeviceForm().xpoints, 
							   dev.getDeviceForm().ypoints, 
							   dev.getDeviceForm().npoints);

	Rectangle devRect = poly.getBounds();

	int xspace = (getViewSize().width-(int)(devRect.width*view.scale))/2;
	int yspace = (getViewSize().height-(int)(devRect.height*view.scale))/2;

	int nrx = (int)(devRect.x*view.scale)-xspace;
	int nry = (int)(devRect.y*view.scale)-yspace;

	view.viewPosition(nrx, nry, getViewSize());
	
	fastDrawing = fastDrawingOnce = drawOnlyHilitedOnce = false;
	bDrawCenterlines = true;  //gm02
	repaint();
}
/**
 * Creation date: (12/7/99 18:54:40)
 */
public void changeOrigin(int x, int y) {
	view.viewPosition(x, y, getViewSize());
	repaint();
}
/**
 * Insert the method's description here.
 * Creation date: (30/7/99 16:00:56)
 * @param deviceName java.lang.String
 */
public void displayDeviceInfo(String deviceName) {
	// call method which displays detailed device info on right panel
	sc.sc_showDeviceProperties(deviceName);
}
/**
 * Insert the method's description here.
 * Creation date: (15/7/99 11:20:03)
 */
public void dump() {
	System.out.println("--------------------");
	System.out.println("x0: "+view.x0);
	System.out.println("y0: "+view.y0);
	System.out.println("rx: "+view.rx);
	System.out.println("ry: "+view.ry);
	System.out.println("width: "+view.width);
	System.out.println("height: "+view.height);
	System.out.println("scale: "+view.scale);
	System.out.println("--------------------");

}
/**
 * Code to perform when this object is garbage collected.
 * 
 * Any exception thrown by a finalize method causes the finalization to
 * halt. But otherwise, it is ignored.
 */
protected void finalize() throws Throwable {
	// Insert code to finalize the receiver here.
	data = null;
	// This implementation simply forwards the message to super. 
	super.finalize();
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 14:25:32)
 * @return svp.devices.Device
 * @param name java.lang.String
 */
public Device getDevice(String name) {

	SimpleDevice[] devs = new SimpleDevice[data.devices.size()];
	data.devices.copyInto(devs);
	for (int i=0; i < devs.length; i++)
		if (devs[i].getName().equals(name)) return devs[i];

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (18/7/99 19:27:24)
 * @return java.util.Hashtable
 */
public Hashtable getHierarchy() {
	if (hierarchy==null) {
		Hashtable groups = ch.psi.synopview.svp.parser.SVParser.parseGroups("SV_groups.txt");
		if (groups==null) return null;

		Device dev;
		Enumeration e = data.devices.elements();			// sort devices into groups
		while (e.hasMoreElements()) {
			dev = (Device)(e.nextElement());
			add2groups(groups, dev);
		}

		hierarchy = makeHierarchy(groups);
	}

	return hierarchy;
}
/**
 * Insert the method's description here.
 * Creation date: (4.12.1999 12:09:40)
 * @return int[]
 */
///public int[] getMachine() {
///	if (machines==null) return null;
///	else return machines.getMachine();
///}
/**
 * Insert the method's description here.
 * Creation date: (4.12.1999 12:07:21)
 * @return java.lang.String[]
 */
///public String[] getMachineName() {
///	if (machines==null) return null;
///	  else return machines.getMachineName();
///}
public String getSelectedDevice () {
	return slastDevice;
}
/**
 * Insert the method's description here.
 * Creation date: (4.12.1999 12:10:57)
 * @return int[]
 */
///public int[] getSystem() {
///	if (systems==null) return null;
///	else return systems.getSystem();
///}
/**
 * Insert the method's description here.
 * Creation date: (4.12.1999 12:10:45)
 * @return java.lang.String[]
 */
///public String[] getSystemName() {
///	if (systems==null) return null;
///	else return systems.getSystemName();
///}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 12:11:54)
 * @return svp.visual.ViewHistory
 */
public ViewHistory getViewHistory() {
	if (viewHistory==null) {
		viewHistory = new ViewHistory(10);			// save 10 views
	}
	return viewHistory;
}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 13:05:29)
 * @return java.awt.Dimension
 */
private Dimension getViewSize() {
	return this.getSize();
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/99 19:19:02)
 * @param device svp.devices.Device
 */
public void hiliteDevice(Device device) {
	drawOnlyHilitedOnce=true;
	view.setHilited(device);
	// hiliteDevice = device.getName()
	repaint();
}
/**
 * Insert the method's description here.
 * Creation date: (30/7/99 14:04:19)
 * @return java.util.Hashtable
 * @param groups java.util.Hashtable
 * @param data svp.devices.GroupData
 */
private Hashtable makeHierarchy(Hashtable groups) {

	Hashtable hierarchy = new Hashtable();
		
	GroupData gd, parent;
	Enumeration e = groups.elements();
	while (e.hasMoreElements()) {
		gd = (GroupData)(e.nextElement());
		parent = (GroupData)(groups.get(gd.getParentName()));
		
		if (parent!=null) {
			parent.getTable().put(gd.getName(), gd);
		}
		else if (gd.getParentName().equalsIgnoreCase("VOID"))
			hierarchy.put(gd.getName(), gd);

		else if (parent==null) {									
			System.out.println("Warning: Group '"+gd.getName()+"' is member of '"+gd.getParentName()+
							   "' group, which is not defined... Group is now subgroup of root...");
			hierarchy.put(gd.getName(), gd);
		}
	}
	return hierarchy;
}

/**
 * Insert the method's description here.
 * Creation date: (2/9/99 19:32:54)
 */
private void makeNavigationImage() {
	Dimension d = getViewSize();
	d.width /= 5;
	d.height /= 5;

	navigator.setBounds(getViewSize().width-d.width, 0, d.width, d.height);
	
	if ((navigatorImage==null) || (d.width!=navigatorSize.width) || 
								  (d.height!=navigatorSize.height)) {
			navigatorImage = createImage(d.width, d.height);
			navigatorSize = d;
			navigatorGraphics = navigatorImage.getGraphics();
		}

	if (navigatorView==null)
		navigatorView = new ViewProperties();

	double nscale;
	double xscale = d.width/(double)view.xsize;
	double yscale = d.height/(double)view.ysize;

	if (xscale<yscale) nscale=xscale;
	else nscale=yscale;
	nscale *= 0.9;

  	navigatorView.loadBasic(d, data);
	navigatorView.setClip(view.clip);
	navigatorView.scaleChanged(nscale);
	navigatorView.x0 = (d.width-navigatorView.width)/2;
	navigatorView.y0 = (d.height-navigatorView.height)/2;

	navigatorGraphics.setColor(Constants.backgroundColor);
	navigatorGraphics.fillRect(0, 0, d.width, d.height);
	
	navigatorGraphics.setColor(Color.black);
	navigatorGraphics.drawRect(0, 0, d.width-1, d.height-1);

	SimpleDevice[] devs = new SimpleDevice[data.devices.size()];
	data.devices.copyInto(devs);
	for (int i=0; i < devs.length; i++) 
		devs[i].paint(navigatorGraphics, navigatorView);

	
}
/**
 * mouseClicked method comment.
 */
public void mouseClicked(java.awt.event.MouseEvent e) {
	int x = (int)((e.getX()-view.x0+view.rx)/view.scale);
	int y = (int)((e.getY()-view.y0+view.ry)/view.scale);
	Device dev = data.getDeviceAt(x,y);
	if (dev!=null) {
		boolean leftButtonPush = (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0;
 		boolean rightButtonPush  = (e.getModifiers() & InputEvent.BUTTON3_MASK) != 0;

 		drawOnlyHilitedOnce=true;
		view.setHilited(dev);
		repaint();

		// tree following
		if (treeControl!=null) treeControl.hiliteDevice(dev);
		
		if (leftButtonPush) {
			if (e.getClickCount() == 1) {
                // Set the DeviceName to the Center Button Label 
			    setSelectedDevice(dev.getName());
			    // get the Properties for this device
			    displayDeviceInfo(dev.getName());
			} else {
			    showEngineeringScreen(dev);
		    }
		} else {
		    // gm02
			sc_centerDevice(dev.getName());
		}
	}
}
/**
 * mouseDragged method comment.
 */
public void mouseDragged(java.awt.event.MouseEvent e) {
	if (mouseSelection) {
		tx2=e.getX()+view.rx;
		ty2=e.getY()+view.ry;
		repaint();
	}
	else if (navigatorDrag) {
		double factor = view.scale/navigatorView.scale;
		tx2 = (int)((e.getX() - navigator.x - navigatorView.x0)*factor);
		ty2 = (int)((e.getY() - navigator.y - navigatorView.y0)*factor);
		view.viewPosition(view.rx+tx2-tx1, view.ry+ty2-ty1, getViewSize());
		tx1=tx2; ty1=ty2;
		repaint();
	}
	else {
		int nrx = view.rx + e.getX() - tx1;
		int nry = view.ry + e.getY() - ty1;
		changeOrigin(nrx, nry);
		tx1=e.getX(); ty1=e.getY();
	}
}
/**
 * mouseEntered method comment.
 */
public void mouseEntered(java.awt.event.MouseEvent e) {}
/**
 * mouseExited method comment.
 */
public void mouseExited(java.awt.event.MouseEvent e) {}
/**
 * mouseMoved method comment.
 */
public void mouseMoved(java.awt.event.MouseEvent e) {
	if (navigator.contains(e.getX(), e.getY())) return;

	int x = (int)((e.getX()-view.x0+view.rx)/view.scale);
	int y = (int)((e.getY()-view.y0+view.ry)/view.scale);
	Device dev = data.getDeviceAt(x,y);
	if (dev!=view.getHilited()) {
		if ((dev!=null)) sc.sc_hilitedDevice(dev.getName());
		else sc.sc_hilitedDevice("");
		
		drawOnlyHilitedOnce=true;
		view.setHilited(dev);
		repaint();
	}
}
/**
 * mousePressed method comment.
 */
public void mousePressed(java.awt.event.MouseEvent e) {
	if (navigator.contains(e.getX(), e.getY())) {
		double factor = view.scale/navigatorView.scale;
		tx1 = (int)((e.getX() - navigator.x - navigatorView.x0)*factor);
		ty1 = (int)((e.getY() - navigator.y - navigatorView.y0)*factor);
		navigatorDrag = true;

		Rectangle rect = new Rectangle(view.rx, view.ry, 
									   getViewSize().width, getViewSize().height);
		if (!rect.contains(tx1, ty1)) {
			view.viewPosition(tx1, ty1, getViewSize());
			repaint();
		}
	}
	else {
		if (e.isShiftDown()) {			// drag-move
			if (handCursor!=null) setCursor(handCursor);
			tx1=e.getX();
			ty1=e.getY();
		}
		else {							// zoom selection
			mouseSelection=true;
			tx1=tx2=e.getX()+view.rx;
			ty1=ty2=e.getY()+view.ry;
			fastDrawing=true;
		}
	}
}
/**
 * mouseReleased method comment.
 */
public void mouseReleased(java.awt.event.MouseEvent e) {
	if (mouseSelection) {
		mouseSelection=false;
		repaint();
		fastDrawing=false;
		zoominArea(tx1, ty1, tx2, ty2);
	} 
	else if (navigatorDrag) {
		navigatorDrag = false;
	}
	else
		if (defaultCursor!=null) setCursor(defaultCursor);
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 20:12:52)
 * @param fileName java.lang.String
 * @return boolean
 */
 public boolean open(String fileName) {
   	waitCursor();
	hierarchy = null;
  	data = SVParser.parse(fileName, view.getColorModel());
	if (data==null) { 
		removeMouseListeners();
		restoreCursor();
		return false;
	}
	
	Dimension d = getViewSize();
   	data.scale(d);
   	view.loadBasic(d, data);
 	view.setBasicView(d);
	view.setClip(new Rectangle(0, 0, d.width, d.height));
 	makeNavigationImage();
 	repaint();
	addMouseListeners();
	restoreCursor();

	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 18:26:11)
 * @param g java.awt.Graphics
 */
public void paint(Graphics g) {
	Dimension d = getViewSize();
	if ((offscreen==null) || 
		(d.width!=offscreensize.width) || (d.height!=offscreensize.height))
		update(g);
	else redraw(g);
}
/**
 * Insert the method's description here.
 * Creation date: (14/7/99 22:01:49)
 * @param g java.awt.Graphics
 */

public void paintAllDevices(Graphics g) {

		Device hilited = view.getHilited();
		view.setHilited(null);
		
		Dimension d = getViewSize();
	
		if ((devicesImage==null) || (d.width!=devicesSize.width) || 
								(d.height!=devicesSize.height)) {
			devicesImage = createImage(d.width, d.height);
			devicesSize = d;
			devicesGraphics = devicesImage.getGraphics();

			if (navigatorSize!=null)
				navigator.setBounds(d.width-navigatorSize.width, 0, 
									navigatorSize.width, navigatorSize.height);

		}

		devicesGraphics.setColor(view.getBackgroundColor());
		devicesGraphics.fillRect(0, 0, d.width, d.height);
	
		devicesGraphics.setColor(Color.black);
		devicesGraphics.drawRect(0, 0, d.width-1, d.height-1);

		SimpleDevice[] devs = new SimpleDevice[data.devices.size()];
		data.devices.copyInto(devs);
		for (int i=0; i < devs.length; i++) 
			devs[i].paint(devicesGraphics, view);

		if (navigatorImage!=null) {
			
			devicesGraphics.drawImage(navigatorImage, navigator.x, 0, null);

			// draw current position rectangle
			double factor = navigatorView.scale/view.scale;
			int x1 = (int)(view.rx*factor) + navigatorView.x0 + navigator.x;
			int y1 = (int)(view.ry*factor) + navigatorView.y0;
			int w = (int)(getViewSize().width*factor);
			int h = (int)(getViewSize().height*factor);

			devicesGraphics.setClip(navigator.x+1, navigator.y+1, 
									navigator.width-2, navigator.height-2);

			devicesGraphics.setColor(Color.red);
			devicesGraphics.drawRect(x1, y1, w, h);

			final int min = 10;
			if ((w<min) || (h<min)) {
				devicesGraphics.setColor(Color.lightGray);
				devicesGraphics.drawRect(x1-min, y1-min, w+2*min, h+2*min);
			}

			devicesGraphics.setClip(0, 0, d.width, d.height);
		}
		
		// new gm02
		if (bDrawCenterlines) {
				devicesGraphics.setColor(Color.red);
				// | Line
				devicesGraphics.drawLine((d.width/2), 0, (d.width/2), d.height);
				// -- Line
				devicesGraphics.drawLine(0, (d.height/2), d.width, (d.height/2));
				bDrawCenterlines = false;
		}
		
		if (g!=null) g.drawImage(devicesImage, 0, 0, null);

		view.setHilited(hilited);


}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 12:11:17)
 */
public void sc_previousView() {
	HistoryViewData hvd = getViewHistory().getPreviousView();
	if (hvd!=null) {

		waitCursor();

		double nscale = hvd.getScale();
		double factor = nscale/view.scale;

		double lscale = view.scale;
		view.scaleChanged(nscale);
	
		view.viewPosition(hvd.get_rx0(), hvd.get_ry0(), getViewSize());

		repaint();
		restoreCursor();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 21:12:54)
 * @return boolean
 * @param fileName java.lang.String
 */
public boolean readColorFile(String fileName) {
	DeviceColorModel colorModel = ch.psi.synopview.svp.parser.SVParser.parseColors(fileName);
	if (colorModel!=null) {
		view.setColorModel(colorModel);
		return true;
	}
	else return false;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:34:04)
 * @return boolean
 * @param fileName java.lang.String
 */
///public boolean readMachineFile(String fileName) {
///	machines = svp.parser.SVParser.parseMachines(fileName);
///	if (machines!=null) return true;
///	else return false;
///}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:34:15)
 * @return boolean
 * @param fileName java.lang.String
 */
///public boolean readSystemFile(String fileName) {
///	systems = svp.parser.SVParser.parseSystems(fileName);
///	if (systems!=null) return true;
///	else return false;
///}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 16:20:18)
 * @param g java.awt.Graphics
 */
public void redraw(Graphics g) {
	if (offscreen==null) update(g);
	else g.drawImage(offscreen, 0, 0, null);
}
/**
 * Insert the method's description here.
 * Creation date: (14/7/99 22:35:33)
 * @param g java.awt.Graphics
 */
public void redrawAllDevices(Graphics g) {
	if (devicesImage==null) paintAllDevices(g);
	else g.drawImage(devicesImage, 0, 0, null);
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 15:37:48)
 */
public void removeMouseListeners() {
	removeMouseListener(this);
	removeMouseMotionListener(this);
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 21:35:14)
 */
public void restoreCursor() {
	setCursor(previousCursor);
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 14:16:28)
 * @param sc svp.visual.SVControlSystem
 */
public void setControlSystem(SVControlSystem sc) {
	this.sc=sc;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 23:20:13)
 * @param machine int
 * @param system int
 * @param state boolean
 */
public void setDevicesVisible(int machine, int system, boolean state) {
	data.setDevicesVisible(machine, system, state);
}
/**
 * Set the DeviceName to the Center Button Label and Center the Drawing... 
 * Creation date: 
 * @param sDevice String
 */
public void setSelectedDevice (String sDevice) {
    // Set the DeviceName to the Center Button Label 
	sc.sc_setSelectedDeviceLabel(sDevice);
	// save this Device as slastDevice
	slastDevice = sDevice;
	// set the Center in the Drawing to this device, if this Option is On
	if (sc.sc_getAutoCenter()) {
		sc_centerDevice(sDevice);
	}
}   
/**
 * Insert the method's description here.
 * Creation date: (28.1.2000 17:51:13)
 * @param treeControl TreeControl
 */
public void setTreeControl(TreeControl treeControl) {
	this.treeControl=treeControl;	
}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 17:44:10)
 * @param group java.util.Hashtable
 * @param state boolean
 */
public void setVisibility(Hashtable group, boolean state) {

	Object obj;
	Device dev;

	Enumeration e = group.elements();

	while (e.hasMoreElements()) {
		obj = e.nextElement();
		if (obj instanceof GroupData) 
			setVisibility(((GroupData)obj).getTable(), state);
		else if (obj instanceof Device) {
			dev = (Device)obj;
			dev.setVisible(state);
		}
	}

}
/**
 * Insert the method's description here.
 * Creation date: (30/7/99 16:02:16)
 * @param device svp.devices.Device
 */
public void showEngineeringScreen(Device device) {
	// call method which brings up engineering screen(s)
	sc.sc_startPanel(device.getCommandLine());
}
/**
 * Insert the method's description here.
 * Creation date: (18/7/99 21:44:17)
 * @return java.util.Hashtable
 * @param table java.util.Hashtable
 * @param key java.lang.String
 */
private Hashtable subHashtable(Hashtable table, String key) {
	Hashtable tbl = (Hashtable)(table.get(key));
	if (tbl==null) {
		tbl = new Hashtable();
		table.put(key, tbl);
	}
	return tbl;
}
/**
 * Paint method (using double-buffering)
 * Creation date: (12/7/99 18:26:35)
 * @param g java.awt.Graphics
 */
public void update(Graphics g) {
	Dimension d = getViewSize();

	if ((offscreen==null) || (d.width!=offscreensize.width) || 
							 (d.height!=offscreensize.height)) {
	    offscreen = createImage(d.width, d.height);
	    offscreensize = d;
	    offgraphics = offscreen.getGraphics();

	    data.updateScale(d);
	    view.loadBasic(d, data);
	    view.setClip(new Rectangle(0, 0, d.width, d.height));

	}

	if (!fastDrawing && !fastDrawingOnce && !drawOnlyHilitedOnce) {
		
		/*----------------------------- paint here ------------------------------*/

		paintAllDevices(offgraphics);

		/*-----------------------------------------------------------------------*/

	} else {
		redrawAllDevices(offgraphics);
		if (fastDrawingOnce) fastDrawingOnce=false;
	}
	
	if (mouseSelection) {
		int px = (tx1>tx2 ? tx2:tx1);
		int py = (ty1>ty2 ? ty2:ty1);
		int w = Math.abs(tx1-tx2);
		int h = Math.abs(ty1-ty2);
		
		offgraphics.setColor(Color.red);
		offgraphics.drawRect(px-view.rx, py-view.ry, w, h);
	}
	else if (drawOnlyHilitedOnce) {
		drawOnlyHilitedOnce=false;
		Device dev = view.getHilited();
		if (dev!=null) {

			Device dev2;
			Vector devs = data.getDevicesIntersecting(dev, dev.getDeviceForm().getBounds());
			for (int i=0; i<devs.size(); i++) {
				dev2 = (Device)(devs.elementAt(i));
					dev2.paint(offgraphics, view);
			}
			
			dev.roundDevice(offgraphics, view);
		}
	}

	redraw(g);			// draws offscreen image

}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 12:29:30)
 * @param nscale double
 */
public void updateView(double nscale) {
	if (nscale>10) {
		System.out.println("Maximum zoom reached...");
		return;
	}
	else if (nscale<0.001) {
		System.out.println("Minimum zoom reached...");
		return;
	}
	
	waitCursor();

	getViewHistory().addView(new HistoryViewData(view.rx, view.ry, view.scale));
	
	double factor = nscale/view.scale;
	int nrx = view.rx;
	int nry = view.ry;
	
	double lscale = view.scale;
	view.scaleChanged(nscale);
	
	Dimension vp = getViewSize();
	
	nrx = (int)(factor*(nrx-view.x0)+(factor-1)*vp.width/2.0);	
	nry = (int)(factor*(nry-view.y0)+(factor-1)*vp.height/2.0);

	view.viewPosition(nrx, nry, getViewSize());

	repaint();			// object will be updated 
	restoreCursor();

}
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 21:33:36)
 */
public void waitCursor() {
	previousCursor = getCursor();
	setCursor(new Cursor(Cursor.WAIT_CURSOR));
}
/**
 * Insert the method's description here.
 * Creation date: (11/19/99 16:48:53)
 */
public void zoomDevice(String deviceName) {
	Device dev = data.getDevice(deviceName);
	if (dev==null) return;

	getViewHistory().addView(new HistoryViewData(view.rx, view.ry, view.scale));

	Polygon poly = new Polygon(dev.getDeviceForm().xpoints, 
							   dev.getDeviceForm().ypoints, 
							   dev.getDeviceForm().npoints);

	Rectangle devRect = poly.getBounds();

	// zoom
	double nscale;
	double xscale = getViewSize().width/(double)devRect.width;
	double yscale = getViewSize().height/(double)devRect.height;

	if (xscale<yscale) nscale=xscale;
	else nscale=yscale;

	nscale *= 0.25;
	view.scaleChanged(nscale);


	// center	
	int xspace = (getViewSize().width-(int)(devRect.width*view.scale))/2;
	int yspace = (getViewSize().height-(int)(devRect.height*view.scale))/2;

	int nrx = (int)(devRect.x*view.scale)-xspace;
	int nry = (int)(devRect.y*view.scale)-yspace;

	view.viewPosition(nrx, nry, getViewSize());
	
	fastDrawing = fastDrawingOnce = drawOnlyHilitedOnce = false;
	repaint();
	

}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 11:41:59)
 */
public void sc_zoomin() {
	updateView(view.scale*1.25);
	//repaint();
}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 18:37:38)
 */
public void zoominArea(int tx1, int ty1, int tx2, int ty2) {
	int px = (tx1>tx2 ? tx2:tx1);
	int py = (ty1>ty2 ? ty2:ty1);
	int w = Math.abs(tx1-tx2);
	int h = Math.abs(ty1-ty2);

	if ((w<10) || (h<10)) return;			// small drags (=clicks) prevention

	waitCursor();

	getViewHistory().addView(new HistoryViewData(view.rx, view.ry, view.scale));

	double factor;
	double xfactor = getViewSize().width/(double)w;
	double yfactor = getViewSize().height/(double)h;

	if (xfactor<yfactor) factor=xfactor;
	else factor=yfactor;

	double nscale = view.scale*factor;

	if (nscale>10) {
		System.out.println("Maximum zoom reached...");
		repaint();
		restoreCursor();
		return;
	}

	int nrx = px-view.x0;	
	int nry = py-view.y0;

	double lscale = view.scale;
	view.scaleChanged(nscale);
	
	nrx = (int)(nrx*factor);	
	nry = (int)(nry*factor);
	
	view.viewPosition(nrx, nry, getViewSize());


	repaint();
	restoreCursor();

}
/**
 * Insert the method's description here.
 * Creation date: (13/7/99 11:42:09)
 */
public void sc_zoomout() {
	updateView(view.scale*0.75);
	//repaint();
}

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentHidden(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == DrawingSurface.this)
				DrawingSurface_ComponentHidden(event);
		}

		public void componentShown(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == DrawingSurface.this)
				DrawingSurface_ComponentShown(event);
		}
	}

	void DrawingSurface_ComponentShown(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
	}

	void DrawingSurface_ComponentHidden(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
	}
}
