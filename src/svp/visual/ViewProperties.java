package svp.visual;

import java.awt.*;
import svp.devices.*;

/**
 * Insert the type's description here.
 * Creation date: (13/7/99 10:26:13)
 * @author: Matej Sekoranja
 */

public class ViewProperties {

	public int x0 = 0;				// origin (center)
	public int y0 = 0;

	public int rx = 0;				// translation (from origin)
	public int ry = 0;

	
	public double scale = 0.0;		// scale
	public int width = 0; 			// viewport size 
	public int height = 0;


	public double bscale = 0.0;		// basic view (all devices are visible)
	public int bwidth = 0; 			
	public int bheight = 0;

	public int xsize = 0; 			
	public int ysize = 0;

	public Rectangle clip = null;
	private Device hilited = null;

	private DeviceColorModel colorModel = null;
	private Color outlineColor = Constants.outlineColor;
	private Color backgroundColor = Constants.backgroundColor;
/**

 * Insert the method's description here.

 * Creation date: (13/7/99 17:53:48)

 * @param size java.awt.Dimension

 */

public void center(Dimension size) {

/*	int w, h;

	if (size.width>width) w = size.width;

	else w=width;



	if (size.height>height) h = size.height;

	else h=height;



	x0 = (w-width)/2;

	y0 = (h-height)/2;*/

	x0=y0=0;

}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 23:38:35)
 * @return java.awt.Color
 */
public Color getBackgroundColor() {
	return backgroundColor;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:52:38)
 * @return svp.visual.DeviceColorModel
 */
public DeviceColorModel getColorModel() {
	return colorModel;
}
/**

 * Insert the method's description here.

 * Creation date: (14/7/99 13:44:54)

 * @return svp.devices.Device

 */

public Device getHilited() {

	return hilited;

}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 21:39:54)
 */
public Color getOutlineColor() {
	return outlineColor;
}
/**

 * Insert the method's description here.

 * Creation date: (13/7/99 13:42:33)

 * @param d java.awt.Dimension

 * @param data svp.devices.DevicesData

 */

public void loadBasic(Dimension size, DevicesData data) {

	this.bscale = data.scale;

	xsize = data.xsize;

	ysize = data.ysize;

	bwidth = (int)(ysize*bscale);

	bheight = (int)(xsize*bscale);

}
/**

 * Insert the method's description here.

 * Creation date: (13/7/99 14:13:51)

 * @param d java.awt.Dimension

 * @param scale double

 */

public void scaleChanged(double scale) {

	this.scale=scale;

	width = (int)(xsize*scale);

	height = (int)(ysize*scale);

}
/**

 * Insert the method's description here.

 * Creation date: (13/7/99 13:50:48)

 * @param d java.awt.Dimension

 */

public void setBasicView(Dimension d) {

	scale = bscale;

	width = bwidth;

	height = bheight;

	rx=ry=0;

	center(d);

}
/**

 * Insert the method's description here.

 * Creation date: (13/7/99 13:47:26)

 * @param clip java.awt.Rectangle

 */

public void setClip(Rectangle clip) {

	this.clip = clip;

}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:54:43)
 * @param colorModel svp.visual.DeviceColorModel
 */
public void setColorModel(DeviceColorModel colorModel) {
	this.colorModel = colorModel;
	if (colorModel!=null) {
		outlineColor = colorModel.getOutlineColor();
		backgroundColor = colorModel.getBackgroundColor();
	}
	else {
		outlineColor = Constants.outlineColor;
		backgroundColor = Constants.backgroundColor;
	}
}
/**

 * Insert the method's description here.

 * Creation date: (14/7/99 13:45:19)

 * @param dev svp.devices.Device

 */

public void setHilited(Device dev) {

	hilited = dev;

}
/**

 * Insert the method's description here.

 * Creation date: (14/7/99 9:58:16)

 * @param rx int

 * @param ry int

 * @param viewport java.awt.Dimension

 */

public void viewPosition(int rx, int ry, Dimension viewport) {

/*	if (rx<0) this.rx=0;

	//else if (rx>width) this.rx=width;

	else*/ this.rx=rx;

	



/*	if (ry<0) this.ry=0;

	//else if (ry>height) this.ry=height;

	else*/ this.ry=ry;

}
}
