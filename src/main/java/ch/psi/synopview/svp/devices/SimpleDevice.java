package ch.psi.synopview.svp.devices;

import java.awt.*;

import ch.psi.synopview.svp.visual.DeviceColorModel;
import ch.psi.synopview.svp.visual.ViewProperties;

/**
 * Insert the type's description here.
 * Creation date: (12/7/99 19:50:56)
 * @author: Matej Sekoranja
 */
public class SimpleDevice extends Device {
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 19:53:17)
 * @param name java.lang.String
 * @param parent java.lang.String
 * @param cmd java.lang.String
 * @param machine int
 * @param system int
 * @param color int
 * @param form java.awt.Polygon
 * @param colorModel svp.visual.DeviceColorModel
 */

public SimpleDevice(String name, String parent, String cmd, int machine, int system, 
					int color, Polygon form, DeviceColorModel colorModel) {
		super(name, parent, cmd, machine, system, color, form, colorModel);
}

/**
 * Insert the method's description here.
 * Creation date: (12/7/99 21:39:05)
 * @exception java.lang.Throwable The exception description.
 */
protected void finalize() throws Throwable {
	super.finalize();
}

/**
 * Insert the method's description here.
 * Creation date: (12/7/99 20:03:39)
 * @param g java.awt.Graphics
 * @param view svp.visual.ViewProperties
 */
public void paintDevice(Graphics g, ViewProperties view) {
	Polygon poly = new Polygon(scaled.xpoints, scaled.ypoints, scaled.npoints);
	poly.translate(-view.rx+view.x0, -view.ry+view.y0);
	if (poly.getBounds().intersects(view.clip)) {		// clipping
		if (this==view.getHilited()) g.setColor(new Color(0xFFFFFF - color.getRGB()));
		else g.setColor(color);
	
		g.fillPolygon(poly);
	if (this==view.getHilited()) g.setColor(Color.red);			// bounding poly
		else g.setColor(view.getOutlineColor());
		g.drawPolygon(poly);
	}
}
}
