package svp.devices;

import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (3.12.1999 22:08:22)
 * @author: Matej Sekoranja
 */
public class MachinesData {
	final static int maxNameLength = 10;
	
	Vector machines;
/**
 * MachinesData constructor comment.
 */
public MachinesData() {
	machines = new Vector();
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:11:53)
 * @param index int
 * @param name java.lang.String;
 */
public void addMachine(int index, String name) {
	if (name.length() > maxNameLength) name.substring(0, maxNameLength);
	Object[] obj = new Object[] { new Integer(index), name };
	machines.addElement(obj);
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:14:54)
 * @return int[]
 */
public int[] getMachine() {
	int size = machines.size();
	int[] indexes = new int[size];
	
	Enumeration e = machines.elements();
	for (int i=0; e.hasMoreElements(); i++)
		indexes[i] = ((Integer)((Object[])e.nextElement())[0]).intValue();

	return indexes;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:18:05)
 * @return java.lang.String[]
 */
public String[] getMachineName() {
	int size = machines.size();
	String[] names = new String[size];
	
	Enumeration e = machines.elements();
	for (int i=0; e.hasMoreElements(); i++)
		names[i] = (((Object[])e.nextElement())[1]).toString();

	return names;
}

public int getMachineSize() {
	return machines.size();
}

}
