package ch.psi.synopview;

/**
 * Insert the type's description here.
 * Creation date: (4/9/99 14:30:29)
 * @author: Matej Sekoranja
 */
class Synoptic {
	DrawingSurface drawingSurface;
	TreeControl    treeControl;
	StatusBar      statusbar;
	ResultPanel    resultpanel;
    SelectDialog   selectDialog = null;     // Select Dialog to choose devices...
/**
 * Synoptic constructor comment.
 */
public Synoptic(SVControlSystem sc) {
	drawingSurface = new DrawingSurface();
	drawingSurface.setControlSystem(sc);

	treeControl    = new TreeControl(drawingSurface);
	drawingSurface.setTreeControl(treeControl);
	
	statusbar      = new StatusBar(); 
	
	resultpanel   = new ResultPanel();
    resultpanel.setSVControlSystem(sc);

	Time mt         = new Time();
	mt.setSVControlSystem(sc);
	
	// SelectDialog Create and show as NOT modal			
	selectDialog = new SelectDialog((java.awt.Frame) sc);
}

public ResultPanel getResultPanel() {
	return resultpanel;
}

public SelectDialog getSelectDialog() {
	return selectDialog;
}

public boolean readSystemFile(String fileName) {
    return selectDialog.readSystemFile(fileName);
}

public boolean readMachineFile(String fileName) {
  return selectDialog.readMachineFile(fileName);
}

public void createSelectElements() {
  selectDialog.createSelectElements();
}

/**
 * Insert the method's description here.
 * Creation date: (4/9/99 15:22:36)
 * @return DrawingSurface
 */
public DrawingSurface getDrawingSurface() {
	return drawingSurface;
}

public StatusBar getStatusBar() {
	return statusbar;
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 15:01:06)
 * @return TreeControl
 */
public TreeControl getTreeControl() {
	return treeControl;
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 21:09:50)
 * @return boolean
 * @param fileName java.lang.String
 */
public boolean readColorFile(String fileName) {
  return drawingSurface.readColorFile(fileName);
}
  /**
   * Returns true if I/O operations were successful
   * Creation date: (4/9/99 15:00:28)
   * @param fileName java.lang.String
   * @return boolean
   */
   public boolean readCoordFile(String fileName) {
	 if (drawingSurface.open(fileName)) {
	   getTreeControl().loadHierarchy(drawingSurface.getHierarchy());
	   return true;
	 }
	 else 
	   return false;
  }  
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 23:22:50)
 * @param machine int
 * @param system int
 * @param state boolean
 */
public void setDevicesVisible(int machine, int system, boolean state) {
	drawingSurface.setDevicesVisible(machine, system, state);
}
}
