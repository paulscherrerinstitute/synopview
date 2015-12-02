package svp.devices;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (12/7/99 21:38:34)
 * @author: Matej Sekoranja
 */
public class DevicesData {
	public Vector devices;
	public int minX = Integer.MAX_VALUE;
	public int minY = Integer.MAX_VALUE;
	public int maxX = Integer.MIN_VALUE;
	public int maxY = Integer.MIN_VALUE;
	public double scale = 1.0;			// max scale (all devices are on screen)
	public int xsize;					// size
	public int ysize;
	public int cx;						// origin correction
	public int cy;

/**
 * DevicesData constructor comment.
 */
public DevicesData() {
	devices = new Vector();
}

/**
 * Insert the method's description here.
 * Creation date: (12/7/99 21:59:11)
 * @param dev svp.devices.SimpleDevice
 */
public void addDevice(SimpleDevice dev) {
	devices.addElement(dev);
}

/**
 * Code to perform when this object is garbage collected.
 * 
 * Any exception thrown by a finalize method causes the finalization to
 * halt. But otherwise, it is ignored.
 */
protected void finalize() throws Throwable {
	// Insert code to finalize the receiver here.
	// This implementation simply forwards the message to super.  You may replace or supplement this.
	devices = null;
    super.finalize();
}

/**
 * Insert the method's description here.
 * Creation date: (19/11/99 16:30:50)
 * @returns svp.devices.Device
 * @param deviceName java.lang.String
 */
public Device getDevice(String deviceName) {
	Device dev;
	Device found = null;

	for (Enumeration e = devices.elements(); e.hasMoreElements() && (found==null); 	) {
		dev = (Device)e.nextElement();
		if (dev.getName().equals(deviceName)) found=dev;
	}
	return found;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 12:30:50)
 * @returns svp.devices.Device
 * @param x int
 * @param y int
 */
public Device getDeviceAt(int x, int y) {
	Device dev;
	Device found = null;
	for (int i=devices.size()-1; (i>=0) && (found==null); i--) {
		dev = (Device)(devices.elementAt(i));
		if (dev.isVisible())
			if (dev.getDeviceForm().contains(x,y)) found=dev;
	}
	return found;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 23:38:46)
 * @return java.util.Vector
 * @param x int
 * @param y int
 */
public Vector getDevicesAt(int x, int y) {
	Device dev;
	Vector devs = new Vector();
	for (int i=devices.size()-1; i>=0; i--) {
		dev = (Device)(devices.elementAt(i));
		if (dev.isVisible())
			if (dev.getDeviceForm().contains(x,y)) devs.addElement(dev);
	}
	return devs;
}

/**
 * Insert the method's description here.
 * Creation date: (14/7/99 23:38:46)
 * @return java.util.Vector
 * @param java.awt.Rectangle rect
 */
public Vector getDevicesIntersecting(Device device, java.awt.Rectangle rect) {
	int i;
    Device dev;
	int size = devices.size();
	Vector devs = new Vector();

	// search only for device which are drawn over 'device' (include it)
	// skip devices under 'device'
	for (i=0; (i<size) && (devices.elementAt(i)!=device); i++);
	for (; i<size; i++) {
		dev = (Device)(devices.elementAt(i));
		if (dev.isVisible())
			if (dev.getDeviceForm().getBounds().intersects(rect)) devs.addElement(dev);
	}
	return devs;
}

/**
 * Insert the method's description here.
 * Creation date: (12/7/99 21:52:42)
 */
public void scale(java.awt.Dimension viewport) {
	xsize = Math.abs(minX)+Math.abs(maxX);
	ysize = Math.abs(minY)+Math.abs(maxY);
	
	double scaleX = viewport.width/(double)xsize;
	double scaleY = viewport.height/(double)ysize;
	if (scaleX < scaleY) scale = scaleX; else scale = scaleY;
	scale = scale*0.95;			// a little bit of free space - 5%

	cx = -minX;		// give only positive coordinates (translation using min. values)
	cy = -minY;

	Enumeration e = devices.elements();
	while (e.hasMoreElements()) 
		((Device)(e.nextElement())).getDeviceForm().translate(cx, cy);
}

/**
 * Insert the method's description here.
 * Creation date: (3/12/99 22:29:43)
 * @param machine int
 * @param system int
 * @param state boolean
 */
public void setDevicesVisible(int machine, int system, boolean state) {
	Device dev;
	Vector devs = new Vector();
	for (int i=devices.size()-1; i>=0; i--) {
		dev = (Device)(devices.elementAt(i));
		if ((dev.getMachine()==machine) && 
			(dev.getSystem()==system))
		 dev.setVisible(state);
	}

}

/**
 * Insert the method's description here.
 * Creation date: (11/19/99 16:37:06)
 */
public void updateScale(java.awt.Dimension viewport) {

	double scaleX = viewport.width/(double)xsize;
	double scaleY = viewport.height/(double)ysize;

	if (scaleX < scaleY) scale = scaleX; else scale = scaleY;
	scale = scale*0.95;			// a little bit of free space - 5%
}
}
