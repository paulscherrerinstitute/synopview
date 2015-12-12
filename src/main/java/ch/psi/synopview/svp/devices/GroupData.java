package ch.psi.synopview.svp.devices;

import java.util.Hashtable;

public class GroupData {
	private String name;
	private String parentName;
	private Hashtable table;

	public GroupData(String name, String parentName) {
		this.name = name;
		this.parentName = parentName;
		table = new Hashtable();
	}

	public String getName() {
		return name;
	}

	public String getParentName() {
		return parentName;
	}

	public Hashtable getTable() {
		return table;
	}
}
