package svp.visual;

import java.util.*;
import java.awt.*;

/**
 * Insert the type's description here.
 * Creation date: (3.12.1999 20:42:50)
 * @author: Matej Sekoranja
 */
public class DeviceColorModel {
	public static final int backgroundColor = 0;
	public static final int outlineColor = 255;
	
	Hashtable colors;
/**
 * DeviceColorModel constructor comment.
 */
public DeviceColorModel() {
	colors = new Hashtable();
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:45:38)
 * @param id int
 * @param color java.awt.Color
 */
public void addColor(int id, Color color) {
	colors.put(new Integer(id), color);
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:48:11)
 */
protected void finalize() throws Throwable {
	colors = null;
	super.finalize();
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:47:18)
 * @return java.awt.Color
 */
public Color getBackgroundColor() {
	Color color = getColor(backgroundColor);
	
	if (color==null) return Constants.backgroundColor;
	else return color;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:46:19)
 * @return java.awt.Color
 * @param id int
 */
public Color getColor(int id) {
	return (Color)colors.get(new Integer(id));
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 20:47:44)
 * @return java.awt.Color
 */
public Color getOutlineColor() {
	Color color = getColor(outlineColor);

	if (color==null) return Constants.outlineColor;
	else return color;
}
}
