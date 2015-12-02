package ch.psi.synopview.svp.devices;

import java.awt.*;

import ch.psi.synopview.svp.visual.Constants;
import ch.psi.synopview.svp.visual.DeviceColorModel;
import ch.psi.synopview.svp.visual.ViewProperties;

/**
 * Insert the type's description here.
 * Creation date: (13/7/99 10:11:41)
 * @author: Matej Sekoranja
 */

public abstract class Device {

	protected String name;			// device name
	protected int machine;			// device machine
	protected int system;			// device system
	protected String cmd;			// command line string
	protected String parent;		// device parent
	protected boolean visible;				
	protected Polygon form;  		// device form
	protected Polygon scaled;  		// ... and scaled form

	protected double scaleWLU = 0.0;			// scale when last updated
	protected Color color = null;
	protected int color_index;

/**
 * Device constructor comment.
 * @param name java.lang.String
 * @param parent java.lang.String
 * @param cmd java.lang.String
 * @param machine int
 * @param system int
 * @param color_index int
 * @param form java.awt.Polygon
 * @param colorModel svp.visual.DeviceColorModel
 */

public Device(String name, String parent, String cmd, int machine, int system, 
			  int color_index, Polygon form, DeviceColorModel colorModel) {

	this.name=name;
	this.machine=machine;
	this.system=system;
	this.form=form;
	this.parent=parent;
	this.cmd=cmd;
	this.color_index=color_index;

	if (colorModel!=null) 
		color = colorModel.getColor(color_index);
	if (color==null) color = Constants.defaultColor;

	visible = true;
}

/**
 * Insert the method's description here.
 * Creation date: (12/7/99 21:39:05)
 * @exception java.lang.Throwable The exception description.
 */
protected void finalize() throws Throwable {
	form = null;
	scaled = null;
	super.finalize();
}

/**
 * Insert the method's description here.
 * Creation date: (4/9/99 11:30:05)
 * @return java.lang.String
 */
public String getCommandLine() {
	if (cmd==null) return "";
	else return cmd;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 9:50:38)
 * @return java.awt.Polygon
 */
public Polygon getDeviceForm() {
	return form;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 22:22:06)
 * @return java.awt.Polygon
 */
public Polygon getDeviceScaledForm() {
	return scaled;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 12:40:58)
 * @return int
 */
public int getMachine() {
	return machine;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 12:40:16)
 * @returns java.lang.String
 */
public String getName() {
	return name;
}

/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:37:41)
 * @return java.lang.String
 */
public String getParentName() {
	return parent;
}

/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:17:32)
 * @return int
 */
public int getSystem() {
	return system;
}

/**
 * Insert the method's description here.
 * Creation date: (28/7/99 11:53:36)
 * @return boolean
 */
public boolean isVisible() {
	return visible;
}

/**
 * This method was created in VisualAge.
 * @param g java.awt.Graphics
 * @param view svp.visual.ViewProperties
 */
public void paint(Graphics g, ViewProperties view) {
	if (visible) {
		if (scaleWLU!=view.scale) update(view);
		paintDevice(g, view);
	}
}

/**
 * This method was created in VisualAge.
 * @param g java.awt.Graphics
 * @param view svp.visual.ViewProperties
 */
public abstract void paintDevice(Graphics g, ViewProperties view);

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 23:45:56)
 * Warning: not updated!
 * @param g java.awt.Graphics
 * @param view svp.visual.ViewProperties
 */
public void roundDevice(Graphics g, ViewProperties view) {
	Polygon poly = new Polygon(scaled.xpoints, scaled.ypoints, scaled.npoints);
	poly.translate(-view.rx+view.x0, -view.ry+view.y0);

	// draw red rectangle round device
	g.setColor(Color.red);
	Rectangle devRect = poly.getBounds();
	g.drawRect(devRect.x, devRect.y, devRect.width, devRect.height);
}

/**
 * Insert the method's description here.
 * Creation date: (28/7/99 17:47:50)
 * @param state boolean
 */
public void setVisible(boolean state) {
	visible=state;
}

/**
 * This method was created in VisualAge.
 */
public void update(ViewProperties view) {
	scaleWLU=view.scale;
	
	int[] x = new int[form.npoints];
	int[] y = new int[form.npoints];
	
	for (int i=0; i < form.npoints; i++) {
		x[i] = (int)(form.xpoints[i]*view.scale);
		y[i] = (int)(form.ypoints[i]*view.scale);
	}

	scaled = new Polygon(x, y, form.npoints);
}

/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 23:57:16)
 * @param colorModel svp.visual.DeviceColorModel
 */
public void updateColor(DeviceColorModel colorModel) {
	color=null;
	if (colorModel!=null) color = colorModel.getColor(color_index);
	if (color==null) color = Constants.defaultColor;
}
}
