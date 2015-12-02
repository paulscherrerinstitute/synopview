package ch.psi.synopview.svp.parser;

import java.io.*;
import java.net.*;
import java.util.*;

import ch.psi.synopview.svp.devices.DevicesData;
import ch.psi.synopview.svp.devices.GroupData;
import ch.psi.synopview.svp.devices.InterrestsData;
import ch.psi.synopview.svp.devices.MachinesData;
import ch.psi.synopview.svp.devices.MenuData;
import ch.psi.synopview.svp.devices.SystemsData;
import ch.psi.synopview.svp.visual.DeviceColorModel;
import java.awt.Color;

/**
 * Insert the type's description here.
 * Creation date: (12/7/99 19:10:17)
 * @author: Matej Sekoranja
 */
public class SVParser {
	static final char quoteChar = '"';
/**
 * Gets StreamTokenizer for given file
 * tries to get the path from the run env var SV_DIR
 * @return java.io.StreamTokenizer
 * @param fileName java.lang.String
 */
public static StreamTokenizer getStreamTokenizer(String fileName) {

	FileInputStream fi = null;
	StreamTokenizer tokenizer = null;

	String sPath = null;
	String sFileName = null;
	try {
		  sPath = new String (java.lang.System.getProperty("SV_DIR"));
	} catch (Exception e) {
		  //System.out.println("Warning: SV_DIR not defined when program started");
		  // if the env var SV_DIR is not defined at startup, this exception
		  // will be executed and so the file will be loaded from the current dir
		  sFileName = fileName;
	}
	if (sPath != null) {
   		sFileName = new String (sPath + fileName);
	}
	
	try	{
		// Try to load datafile: sFileName
		fi = new FileInputStream(sFileName);
		tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(fi)));
		initializeTokenizer(tokenizer);
		System.out.println("Info:    load datafile " + sFileName + " success");
	} catch (IOException e) {
		System.out.println("\nError:   while opening " + fileName + "'");
		System.out.println("        " + e);
	}

	return tokenizer;
}
/**
 * Initializes tokenizer
 * @param st java.io.StreamTokenizer
 */
public static void initializeTokenizer(StreamTokenizer tokenizer) {
	tokenizer.whitespaceChars(0, 32);
	tokenizer.wordChars(33, 255);			// reset
	tokenizer.eolIsSignificant(true);
	tokenizer.commentChar('#');
	tokenizer.quoteChar(quoteChar);
}
/**
 * Parses SV database file
 * Creation date: (12/7/99 19:12:14)
 * @return svp.devices.DevicesData
 * @param fileName java.lang.String
 * @param colorModel svp.visual.DeviceColorModel
 */
public static DevicesData parse(String fileName, ch.psi.synopview.svp.visual.DeviceColorModel colorModel) {
	DevicesData data = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		data = new DevicesData();
		processData(data, colorModel, tokenizer, fileName);
	}
	
	return data;
}
/**
 * Parses SV color file
 * Creation date: (3.12.1999 21:00:40)
 * @return svp.visual.DeviceColorModel
 * @param fileName java.lang.String
 */
 
public static ch.psi.synopview.svp.visual.DeviceColorModel parseColors(String fileName) {
	DeviceColorModel colorModel = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		colorModel = new DeviceColorModel();
		processColorData(colorModel, tokenizer, fileName);
	}
	
	return colorModel;
}
/**
 * Parses SV database file
 * Creation date: (12/7/99 19:12:14)
 * @return java.awt.Hashtable
 * @param fileName java.lang.String
 */
public static Hashtable parseGroups(String fileName) {
	Hashtable groups = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		groups = new Hashtable();
		processGroupData(groups, tokenizer, fileName);
	}
	
	return groups;
}
/**
 * Parses SV machines file
 * Creation date: (3/12/99 19:12:14)
 * @return svp.device.MachinesData
 * @param fileName java.lang.String
 */
public static MachinesData parseMachines(String fileName) {
	MachinesData machines = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		machines = new MachinesData();
		processMachineData(machines, tokenizer, fileName);
	}
	
	return machines;
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 19:12:14)
 * @return java.awt,Vector
 * @param fileName java.lang.String
 */
public static Vector parseMenus(String fileName) {
	Vector menus = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		menus = new Vector();
		processMenuData(menus, tokenizer, fileName);
	}	
	return menus;
}
/**
 * Parses SV systems file
 * Creation date: (3/12/99 19:22:14)
 * @return svp.device.SystemsData
 * @param fileName java.lang.String
 */
public static SystemsData parseSystems(String fileName) {
	SystemsData systems = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		systems = new SystemsData();
		processSystemData(systems, tokenizer, fileName);
	}
	
	return systems;
}

/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 21:04:11)
 * @param colorModel svp.visual.DeviceColorModel
 * @param tokenizer java.io.StreamTokenizer
 * @param fileName java.lang.String
 */
public static void processColorData(DeviceColorModel colorModel, StreamTokenizer tokenizer, String fileName) {

	int id, red, green, blue;

	try	{
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_NUMBER) {

			// read color id
			id=(int)tokenizer.nval;

			// read red
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_NUMBER) red=(int)tokenizer.nval;
			else throw (new SVParseException("Invalid data (red value - integer expected)...", tokenizer, fileName));

			// read green
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_NUMBER) green=(int)tokenizer.nval;
			else throw (new SVParseException("Invalid data (green value - integer expected)...", tokenizer, fileName));

			// read green
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_NUMBER) blue=(int)tokenizer.nval;
			else throw (new SVParseException("Invalid data (blue value - integer expected)...", tokenizer, fileName));

			colorModel.addColor(id, new Color(red, green, blue));

		  }
  		  
	} catch (Exception exc) {
		System.out.println("\nError: parsing " + fileName + "...");
		System.out.println("       " + exc);
	}}

public static Hashtable parseInterrests(String fileName) {
	Hashtable interrests = null;
	
	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) {
		interrests = new Hashtable();
		processInterrestData(interrests, tokenizer, fileName);
	}
	return interrests;
}

/**
 * Insert the method's description here.
 * Creation date: (12/7/99 19:19:42)
 * @param data svp.devices.DevicesData
 * @param tokenizer java.io.StreamTokenizer
 * @param fileName java.lang.String
 * @param colorModel svp.visual.DeviceColorModel
 */
public static void processData(DevicesData data, ch.psi.synopview.svp.visual.DeviceColorModel colorModel, StreamTokenizer tokenizer, String fileName) {

	final int maxCoords = 30;
	
	String name, parent, cmd;
	int machine, system, color;
	int i;
	int[] x = new int[maxCoords];
	int[] y = new int[maxCoords];

	try	{
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_WORD) {

			// read device name
			name=tokenizer.sval;

			// read device parent
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_WORD) parent=tokenizer.sval;
			else throw (new SVParseException("Invalid data (device parent - string expected)...", tokenizer, fileName));

			// read device command line
			tokenizer.nextToken();
			if (tokenizer.ttype==quoteChar) cmd=tokenizer.sval;
			else throw (new SVParseException("Invalid data (command line within quote char '\"' expected)...", tokenizer, fileName));
			if (cmd.trim().equals("")) cmd = null;

			// read device machine
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_NUMBER) machine=(int)tokenizer.nval;
			else throw (new SVParseException("Invalid data (device machine - integer expected)...", tokenizer, fileName));

			// read device system
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_NUMBER) system=(int)tokenizer.nval;
			else throw (new SVParseException("Invalid data (device system - integer expected)...", tokenizer, fileName));

			// read device color
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_NUMBER) color=(int)tokenizer.nval;
			else throw (new SVParseException("Invalid data (device color - integer expected)...", tokenizer, fileName));

			i = 0;
			while ((tokenizer.nextToken() != tokenizer.TT_EOL) &&
				   (tokenizer.ttype != tokenizer.TT_EOF) && (i<maxCoords)) {
					   
				// read x coordinate
				//tokenizer.nextToken();
				if (tokenizer.ttype==tokenizer.TT_NUMBER) x[i]=(int)(tokenizer.nval*1000);
				else throw (new SVParseException("Invalid data (x coord. - integer expected)...", tokenizer, fileName));
				if (x[i] < data.minX) data.minX=x[i];
				if (x[i] > data.maxX) data.maxX=x[i];

				// read y coordinate
				tokenizer.nextToken();
				if (tokenizer.ttype==tokenizer.TT_NUMBER) y[i]=-(int)(tokenizer.nval*1000);	
				else throw (new SVParseException("Invalid data (y coord. - integer expected)...", tokenizer, fileName));
				if (y[i] < data.minY) data.minY=y[i];
				if (y[i] > data.maxY) data.maxY=y[i];

				i++;
			}

			data.addDevice(new ch.psi.synopview.svp.devices.SimpleDevice(name, parent, cmd, machine, system, color, new java.awt.Polygon(x, y, i), colorModel));

			// read to the end of line if necessary
			while ((tokenizer.ttype != tokenizer.TT_EOL) &&
			   	   (tokenizer.ttype != tokenizer.TT_EOF)) tokenizer.nextToken();
				
		  }
  		  
	} catch (Exception exc) {
		System.out.println("\nError: parsing " + fileName + "...");
		System.out.println("       " + exc);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/99 19:19:42)
 * @param groups java.util.Hashtable
 * @param tokenizer java.io.StreamTokenizer
 * @param fileName java.lang.String
 */
public static void processGroupData(Hashtable groups, StreamTokenizer tokenizer, String fileName) {

	String name, parent;

	try	{
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_WORD) {

			// read device name
			name=tokenizer.sval;

			// read parent
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_WORD) parent=tokenizer.sval;
			else throw (new SVParseException("Invalid data (parent - string expected)...", tokenizer, fileName));

            // parent ist immer das selbe, z.b. VOID -> 
			groups.put(name, new GroupData(name, parent));
		    //System.out.print ("@");
		  }
  		  //System.out.println("end...");
	} catch (Exception exc) {
		System.out.println("\nError: while parsing: " + fileName + "...");
		System.out.println("      " + exc);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:26:52)
 * @param machines svp.devices.MachinesData
 * @param tokenizer java.io.StreamTokenizer
 * @param fileName java.lang.String
 */
public static void processMachineData(MachinesData machines, StreamTokenizer tokenizer, String fileName) {

	String name;
	int index;

	try	{
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_NUMBER) {

			// read machine
			index=(int)tokenizer.nval;

			// read machine name
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_WORD) name=tokenizer.sval;
			else throw (new SVParseException("Invalid data (machine_name - string expected)...", tokenizer, fileName));

			machines.addMachine(index, name);
		  }
  		  
	} catch (Exception exc) {
		System.out.println("\nError: while parsing: " + fileName + "...");
		System.out.println("      " + exc);
	}
}
public static void processMenuData(Vector menus, StreamTokenizer tokenizer, String fileName) {
  String menuItem, menuExec, menuShortCut;

  try {
	while (tokenizer.nextToken() != tokenizer.TT_EOF)
	  if (tokenizer.ttype == tokenizer.TT_WORD) {
		// read menuItem
		menuItem = tokenizer.sval;
		
//        if (menuItem.equalsIgnoreCase("null")) {
//          System.out.println("Info:    file " + fileName + " at line " + tokenizer.lineno() + " is a seperator");
//          // menuItem = null is a seperator menuItem
//          tokenizer.nextToken();
//          menuExec = null;
//          tokenizer.nextToken();
//          menuShortCut = null;
//        } else { 
		  // read menuExec (including parametess, this field mus be in "")
		    tokenizer.nextToken();
		  if (tokenizer.ttype==quoteChar) 
			menuExec=tokenizer.sval;
		  else {
			menuExec = "";
			System.out.println("Warning: parsing " + fileName + " at line " + tokenizer.lineno() + ": no program defined to execute!");
		  }
		  if (menuExec.trim().equals("")) menuExec = null;

		  // read menuShortCut
		  tokenizer.nextToken();
		  if (tokenizer.ttype==tokenizer.TT_WORD) 
			menuShortCut=tokenizer.sval;
		  else {
			menuShortCut = null;
		  }
//        }
		if (menuExec != null) 
		  menus.addElement(new MenuData(menuItem, menuExec, menuShortCut));
	  }
  } catch (Exception exc) {
	System.out.println("\nError:   while parsing " + fileName + "...");
	System.out.println("      " + exc);
  }

}

/**
 * Insert the method's description here.
 * Creation date: (3.12.1999 22:26:52)
 * @param systems svp.devices.SystemsData
 * @param tokenizer java.io.StreamTokenizer
 * @param fileName java.lang.String
 */
public static void processSystemData(SystemsData systems, StreamTokenizer tokenizer, String fileName) {

	String name;
	int index;

	try	{
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_NUMBER) {

			// read system
			index=(int)tokenizer.nval;

			// read system name
			tokenizer.nextToken();
			if (tokenizer.ttype==tokenizer.TT_WORD) name=tokenizer.sval;
			else throw (new SVParseException("Invalid data (machine_name - string expected)...", tokenizer, fileName));

			systems.addSystem(index, name);
		  }
  		  
	} catch (Exception exc) {
		System.out.println("\nError: while parsing: " + fileName + "...");
		System.out.println("      " + exc);
	}
}

public static void processInterrestData(Hashtable interrests, StreamTokenizer tokenizer, String fileName) {

	String          sGroup;
	String          sProperty;
	final int       maxItems = 100;
    int             i;
    Vector          vProperties;
    
	try	{
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_WORD) {

			// read interrest group name
			sGroup = tokenizer.sval;

			i = 0;
			vProperties = new Vector();
			while ((tokenizer.nextToken() != tokenizer.TT_EOL) &&
				   (tokenizer.ttype != tokenizer.TT_EOF) && (i<maxItems)) {
			
			    i++;
			    // read property
			    if (tokenizer.ttype==tokenizer.TT_WORD) {
			        sProperty = tokenizer.sval;
			        vProperties.addElement(sProperty);
			    } else { 
			        throw (new SVParseException("Invalid data (Property - string expected)...", tokenizer, fileName));
                   }
			}
			interrests.put(sGroup, new InterrestsData(sGroup, vProperties));
		  }
	} catch (Exception exc) {
		System.out.println("\nError: while parsing: " + fileName + "...");
		System.out.println("      " + exc);
	}
}

}
