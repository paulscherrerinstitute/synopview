package ch.psi.synopview;

/**
 * Insert the type's description here.
 * Creation date: (4/9/99 14:01:23)  
 * @author: Matej Sekoranja
 */
public interface SVControlSystem {
  
  // methode declared in DrawingSurface
  public void sc_baseview();  
  public void sc_centerDevice();  
  public void sc_zoomIn();  
  public void sc_zoomOut();  
  public void sc_previousView();  
  public void sc_hilitedDevice(String deviceName);  
  public void sc_setSelectedDeviceLabel(String dev);
  
  // methods declared in Time
  public void sc_setDateandTime(String sDateandTime);  
  
  // methods declared in JavaCdev
  public void sc_showDeviceProperties(String deviceName);  
  public void sc_reconnectCdev();
  
  // methods declared in ResultPanel
  public void sc_setTextArea(String sInfo);
  public void sc_setTextAreaLabel(String sInfo);
  public void sc_appendTextArea(String sInfo);
  public void sc_setDebugMode(boolean bDebug);
  public void sc_setCdevMode(boolean bDebug);
  public void sc_setInterrestGroup(String str);
  public void sc_addInterrestChoice(String str);

  // methods declared in SelectDialog
  public void sc_enableSelectButton(boolean set);
  public void sc_selectDevice(int machine, int system, boolean visible);  
  
  // methods declared in SV
  public void sc_select();  
  public boolean sc_getAutoCenter ();  
  public void sc_startPanel(String commandLine);  
}
