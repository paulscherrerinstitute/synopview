package ch.psi.synopview.svp.devices;

import java.util.*;

public class SystemsData {
	final static int maxNameLength = 15;

	private Vector systems;

	public SystemsData() {
		systems = new Vector();
	}

	public void addSystem(int index, String name) {
		if (name.length() > maxNameLength)
			name.substring(0, maxNameLength);
		Object[] obj = new Object[] { new Integer(index), name };
		systems.addElement(obj);
	}

	public int[] getSystem() {
		int size = systems.size();
		int[] indexes = new int[size];

		Enumeration e = systems.elements();
		for (int i = 0; e.hasMoreElements(); i++)
			indexes[i] = ((Integer) ((Object[]) e.nextElement())[0]).intValue();

		return indexes;
	}

	public String[] getSystemName() {
		int size = systems.size();
		String[] names = new String[size];

		Enumeration e = systems.elements();
		for (int i = 0; e.hasMoreElements(); i++)
			names[i] = (((Object[]) e.nextElement())[1]).toString();

		return names;
	}

	public int getSystemSize() {
		return systems.size();
	}

}
