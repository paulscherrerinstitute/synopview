package ch.psi.synopview.svp.devices;

import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (3.12.1999 22:08:22)
 * @author: Matej Sekoranja
 */
public class SystemsData {
	final static int maxNameLength = 15;
	
	Vector systems;
/**
 * MachinesData constructor comment.
 */
public SystemsData() {
	systems = new Vector();
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:11:53)
 * @param index int
 * @param name java.lang.String;
 */
public void addSystem(int index, String name) {
	if (name.length() > maxNameLength) name.substring(0, maxNameLength);
	Object[] obj = new Object[] { new Integer(index), name };
	systems.addElement(obj);
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:14:54)
 * @return int[]
 */
public int[] getSystem() {
	int size = systems.size();
	int[] indexes = new int[size];
	
	Enumeration e = systems.elements();
	for (int i=0; e.hasMoreElements(); i++)
		indexes[i] = ((Integer)((Object[])e.nextElement())[0]).intValue();

	return indexes;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:18:05)
 * @return java.lang.String[]
 */
public String[] getSystemName() {
	int size = systems.size();
	String[] names = new String[size];
	
	Enumeration e = systems.elements();
	for (int i=0; e.hasMoreElements(); i++)
		names[i] = (((Object[])e.nextElement())[1]).toString();

	return names;
}

public int getSystemSize() {
	return systems.size();
}

}
