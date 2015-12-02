package svp.devices;
import java.util.Hashtable;

/**
 * Insert the type's description here.
 * Creation date: (30/7/99 11:20:44)
 * @author: Matej Sekoranja
 */
public class GroupData {
	String name;
	String parentName;
	Hashtable table;

/**
 * GroupData constructor comment.
 */

public GroupData(String name, String parentName) {
	this.name=name;
	this.parentName=parentName;
	table = new Hashtable();
}

/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:22:41)
 * @return java.lang.String
 */
public String getName() {
	return name;
}

/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:23:33)
 * @return java.lang.String
 */
public String getParentName() {
	return parentName;
}

/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:24:22)
 * @return java.util.Hashtable
 */
public Hashtable getTable() {
	return table;
}
}
