package svp.devices;

import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (30/7/99 11:20:44)
 * @author: Matej Sekoranja, Marcel Grunder
 */
public class MenuData {
	String menuItem;
	String menuExec;
	String menuShortCut;
/**
 * MenuData constructor comment.
 */
public MenuData(String menuItem, String menuExec, String menuShortCut) {
	this.menuItem     = menuItem;
	this.menuExec     = menuExec;
	this.menuShortCut = menuShortCut;
}
/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:23:33)
 * @return java.lang.String
 */
public String getMenuExec() {
	return menuExec;
}
/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:22:41)
 * @return java.lang.String
 */
public String getMenuItem() {
	return menuItem;
}
/**
 * Insert the method's description here.
 * Creation date: (30/7/99 11:23:33)
 * @return java.lang.String
 */
public String getMenuShortCut() {
	return menuShortCut;
}
}
