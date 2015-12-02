package svp.visual;

/**
 * Insert the type's description here.
 * Creation date: (28/7/99 12:03:19)
 * @author: Matej Sekoranja 
 */
public class HistoryViewData {
	private int rx0;
	private int ry0;
	private double scale;
/**
 * HistoryViewData constructor comment.
 */
public HistoryViewData(int rx0, int ry0, double scale) {
	this.rx0=rx0;
	this.ry0=ry0;
	this.scale=scale;
}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 12:15:35)
 * @return int
 */

public int get_rx0() {
	return rx0;
}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 12:16:01)
 * @return int
 */

public int get_ry0() {
	return ry0;
}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 12:16:17)
 * @return double
 */
public double getScale() {
	return scale;
}
}
