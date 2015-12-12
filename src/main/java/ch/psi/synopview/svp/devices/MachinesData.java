package ch.psi.synopview.svp.devices;

import java.util.*;

public class MachinesData {
	final static int maxNameLength = 10;

	private Vector machines;

	public MachinesData() {
		machines = new Vector();
	}

	public void addMachine(int index, String name) {
		if (name.length() > maxNameLength)
			name.substring(0, maxNameLength);
		Object[] obj = new Object[] { new Integer(index), name };
		machines.addElement(obj);
	}

	public int[] getMachine() {
		int size = machines.size();
		int[] indexes = new int[size];

		Enumeration e = machines.elements();
		for (int i = 0; e.hasMoreElements(); i++)
			indexes[i] = ((Integer) ((Object[]) e.nextElement())[0]).intValue();

		return indexes;
	}

	public String[] getMachineName() {
		int size = machines.size();
		String[] names = new String[size];

		Enumeration e = machines.elements();
		for (int i = 0; e.hasMoreElements(); i++)
			names[i] = (((Object[]) e.nextElement())[1]).toString();

		return names;
	}

	public int getMachineSize() {
		return machines.size();
	}

}
