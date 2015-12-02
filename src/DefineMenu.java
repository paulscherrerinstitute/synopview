import java.awt.*;
import java.util.*;
import svp.devices.*;

public class DefineMenu extends java.awt.Menu 
{
	Vector data = null;
		
	public DefineMenu() {
	}
	public void addRunMenu (Menu mb) {
	  String sfileName = "SV_runmenu.txt";
	  data = svp.parser.SVParser.parseMenus(sfileName);
	  if (data!=null) {
	    
  	    String menuItem, menuShortCut;                  // String for the for the menu
	    MenuData md;                                    // MenuData defined in Msvp.devices.MenuData.java
	    int iMenuItems = data.size();                   // number of valied MenuItems
	    //System.out.println("Info:    added " + iMenuItems + " items to the Execute menu");
	    java.awt.MenuItem mi;                           // menuitem

	    Enumeration e = data.elements();
	    // loop for each menu item in the execute menu...
		while (e.hasMoreElements()) {
	      md = (MenuData)(e.nextElement());
	      menuItem     = md.getMenuItem();
	      menuShortCut = md.getMenuShortCut();  // not used yet
	      mi = new MenuItem(menuItem);
	      //example of how to implement short keys, parser currently not support this
	      //mi.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_O,false));
		  mi.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
			  MenuItem clickedItem = (MenuItem)e.getSource();  
			  menuClicked(clickedItem.getLabel()); 
			}
		  }
		  );  // end mi.addActionListener
		  mb.add(mi);
		}
	  }
	}
  private void menuClicked (String menuItem) {
	MenuData md;                // MenuData defined in MenuData.java
	String   menuExec = null;   // String to execute

	Enumeration e = data.elements();
	while (e.hasMoreElements()) { // search the menuExec String to execute
	  md = (MenuData)(e.nextElement());
	  if (menuItem.equals(md.getMenuItem())) {
		menuExec = md.getMenuExec();
	  }
	} // end while e.hasMoreElements
	ExecProgram.run(menuExec);
  }  // end of this method    
}
