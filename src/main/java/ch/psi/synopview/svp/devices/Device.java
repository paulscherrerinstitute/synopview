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

protected void finalize() throws Throwable {
	form = null;
	scaled = null;
	super.finalize();
}

public String getCommandLine() {
	if (cmd==null) return "";
	else return cmd;
}

public Polygon getDeviceForm() {
	return form;
}

public Polygon getDeviceScaledForm() {
	return scaled;
}

public int getMachine() {
	return machine;
}

public String getName() {
	return name;
}

public String getParentName() {
	return parent;
}

public int getSystem() {
	return system;
}

public boolean isVisible() {
	return visible;
}

public void paint(Graphics g, ViewProperties view) {
	if (visible) {
		if (scaleWLU!=view.scale) update(view);
		paintDevice(g, view);
	}
}

public abstract void paintDevice(Graphics g, ViewProperties view);

public void roundDevice(Graphics g, ViewProperties view) {
	Polygon poly = new Polygon(scaled.xpoints, scaled.ypoints, scaled.npoints);
	poly.translate(-view.rx+view.x0, -view.ry+view.y0);

	// draw red rectangle round device
	g.setColor(Color.red);
	Rectangle devRect = poly.getBounds();
	g.drawRect(devRect.x, devRect.y, devRect.width, devRect.height);
}

public void setVisible(boolean state) {
	visible=state;
}

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

public void updateColor(DeviceColorModel colorModel) {
	color=null;
	if (colorModel!=null) color = colorModel.getColor(color_index);
	if (color==null) color = Constants.defaultColor;
}
}
