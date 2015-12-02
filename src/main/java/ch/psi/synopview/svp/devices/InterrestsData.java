package ch.psi.synopview.svp.devices;
import java.util.*;
import java.util.Hashtable;

public class InterrestsData {
	String name;
	//String parentName;
	Vector vName;
	Hashtable table;

	/**
	 * InterrestsData constructor comment.
	 */
	public InterrestsData(String name, Vector vName) {
		this.name=name;
		this.vName=vName;
		table = new Hashtable();
	}

	/**
	 * Insert the method's description here.
	 *
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 * Insert the method's description here.
	 *
	 *
	 */
	public Vector getVName() {
		return vName;
	}

	/**
	 * Insert the method's description here.
	 *
	 *
	 */
	public Hashtable getTable() {
		return table;
	}

}
