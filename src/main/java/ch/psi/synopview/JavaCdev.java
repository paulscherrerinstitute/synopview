package ch.psi.synopview;

/*
** Synoptic Viewer Java Cdev part to do all cdev Services...
** Marcel.Grunder@psi.ch
** aug.30.2000
*/
import java.util.StringTokenizer;
import cdev.data.*;
import cdev.*;

// gm02 new
import ch.psi.synopview.svp.devices.*;
import java.util.Hashtable;
import java.util.*;

public class JavaCdev {
  private cdev.ControlSystem sys;   // Cdev Gateway Server ControlSystem
  private boolean bIsConnected = false;  // Cdev Gateway connected
  // ControlSystem used to display results in the ControlPanel
  // (all ControlSystem methods are declared in ControlSystem.java and
  // are defined in SV.java)
  SVControlSystem sc;

  // to enable or disable debug infos... 
  int idebug = 0;       // default for the synoptic viewer
                        // 1 = Debug Info in the SV
  // to define the Mode Cdev gets the Results...
  // true = same as in cdevGet
  // false = sendNoBlock (Default)
  boolean bCdevMode = false;
  
  // 
  Hashtable interrests = null;

  // the selected InterrestsData (from Choice widget)
  InterrestsData idSelected = null;

/*
** JavaCdev() constructor creates a connection to the ControlSystem
**
** parameter: ControlSystem = cs...
**            sHost = host to connect to the Gateway server 
**            port = port number of cdevGateway
**
*/
  public JavaCdev (SVControlSystem sc, String sHost, int iPort) {
	this.sc = sc;
	try {
	  sys   = cdev.ControlSystem.getDefault();
	  sys.connect(sHost, iPort);
	  bIsConnected = true;
	  sc.sc_setTextArea("Connected to " + sHost + " " + iPort + "...\n");
	} catch (Exception e) {
	  bIsConnected = false;      
	  System.out.println("NOT connected to " + sHost + " " + iPort + "...");
	  sc.sc_setTextArea("NOT connected to " + sHost + " " + iPort + "...\n");
	}
  }

  /*
  ** to enable or disable debug infos...
  **
  ** 0  no debug information, just the results will be displayed
  ** 1  some debug info, used in the sls synoptic viewer...
  ** 2 	max debug information
  */
  public void setDebug(int status) {
    idebug = status;
  }

  /*
  ** showDeviceProperties()
  **
  ** parameter: sDevice		device name to get the properties and their results
  **
  ** in the synoptic viewer, this method is called when the user clicks on a device in the drawing
  ** or select it from the tree.
  ** the result are displayed in the ControlPanel. ControlSystem provides the interface to this class
  */
  public void sc_showDeviceProperties(String sDevice) {
    String[] saProperties = null;

    if (bIsConnected) {
	  // clip has connectet to host
	  // get the device properties...
	  //-----------------------------
	  // show the selected Device on the Panel
	  sc.sc_setTextAreaLabel("Properties: " + sDevice);
      sc.sc_setTextArea("");       
	  
	  // try to get the Properties of the DDL file
      saProperties = getProperties(sDevice);
	  if (saProperties == null)
		return;
	  
	  // control print of properties
	  int len = saProperties.length;
	  if (idebug == 2) {
	    for (int i = 0; i < len; i++)
	      System.out.println("[" + i + "] = " + saProperties[i]);	  
		System.out.println("\n");
	  }
	  
	  // get the values of this properties
	  // je nach dem welcher Mode mit den Checkboxes ausgew�hlt ist..
	  if (!bCdevMode)
		  getValues(sDevice, saProperties);
	  else
	      send(sDevice, saProperties);
	} else {
	  // Cdev is not connected... so clear the text areas...    
	  sc.sc_setTextArea("cdev not connected GM02");
	  sc.sc_setTextAreaLabel("");
	} 
  }

  /*
  ** getProperties
  **
  ** parameter: sDevice		like "ALIVA-VG-5"
  ** return:	Sting[]		properties of the directory Service
  **						return something like "STATUS" or "I-READ"
  */
  public String[] getProperties (String sDevice) {
	try {
		String[] saProperty         = null;
		int iLen;
		
		// execute the directory Service for the NEW style DDL file
		saProperty = directoryService("queryAttributes", sDevice);
		if (saProperty == null) {
    		saProperty = directoryService("query", sDevice + ":");
		    if (saProperty == null) {
			    sc.sc_appendTextArea("No Properties found\n");
                return null;
            }		
		}
		if (idebug > 0)
	        sc.sc_appendTextArea(saProperty.length + " Properties found\n");
        saProperty = getInterrestProperty(saProperty);
	   	if (idebug > 0)
	        sc.sc_appendTextArea(saProperty.length + " Interrest Properties found\n");
        return saProperty;    
    
    } catch (Exception e) {
        System.out.println("Error in JavaCdev.getProperties() " + e);       
        return null;
    }  
  }

  /*
  ** getInterrestProperties
  **
  ** parameter: Strinng[] saProperties		    device Properties
  ** return:	Sting[]	  saInterrestProperty	Interrest Properties 
  **						return something like "STATUS" or "I-READ"
  */
  public String[] getInterrestProperty (String[] saProperties) {
	try {
		String[] saInterrestProperty         = null;
		int iLen;
		String sIGroup = null; // Selected Interrest Group String
		int iViewLen = 0;
		
		// if the interrest group is defined, use only the properties define in this group
		if (idSelected != null) {
			iLen = saProperties.length;
			sIGroup = idSelected.getName();         // the current selected Interrest Group
            if (idSelected.getVName().size() == 0) {
                // in the InterrestGroup defined in the file SV_interrest.txt there are no properties
                // defined for this group,
                // so all properties will be shown...
                return saProperties;
            }
            
			// durchsuche alle Properties und speichere die Anzahl der ViewProperties...
            for (int i = 0; i < iLen; i++) {
			    // teste ob die Interrest Gruppe dieses Property enth�lt
			    if (idSelected.getVName().contains(saProperties[i])) {
				    //System.out.println(sIGroup + " contains " + saProperties[i]);
				    iViewLen++;
				}
			}
			if (iViewLen == 0) {
			    sc.sc_appendTextArea("No Interrest Properties defined\n");
			    sc.sc_appendTextArea("Make shure that the correct INTERREST GROUP is selected\n");
		        return null;
			}
				
			saInterrestProperty = new String[iViewLen];
			// now save the view Properties
			iViewLen = 0;
			for (int i = 0; i < iLen; i++) {
				// teste ob die Interrest Gruppe dieses Property enth�lt
			    if (idSelected.getVName().contains(saProperties[i])) {
					saInterrestProperty[iViewLen] = saProperties[i];
					iViewLen++;
				}
			}
		    return saInterrestProperty;
		} else {
            sc.sc_appendTextArea("No correct INTERREST GROUP is defined, use all properties\n");
			return saProperties;
		}

    } catch (Exception e) {
        System.out.println("Error in JavaCdev.getInterrestProperty() " + e);       
        return null;
    }  
  }
     
  /*
  ** directoryService
  **
  ** parameter: sCommand	tested with "query" and "queryAttributes"
  **			sDevice		like "ALIVA-VG-5"
  ** return:	Sting[]		properties of the directoryService
  **						return something like "STATUS" or "I-READ"
  */
  public String[] directoryService (String sCommand, String sDevice) {
	try {
		cdev.Device     cDevice     = new cdev.Device("cdevDirectory");
		cdev.data.Data  cData       = new cdev.data.Data();
		cdev.data.Data  cResult     = new cdev.data.Data();
		String[]        saProperty  = null;
		int iLen = 0;

		if (idebug == 2) {
			System.out.println("cDevice.send ( " + sCommand + ", *, " + sDevice + ")");
		}
		// execute the directoryService for the NEW style DDL file
		cData.insert("device", sDevice);
        // if iLen < ca. 1100 this failed... see below:
        // xdr import String array, len=1134
        // CLIP exception java.lang.NegativeArraySizeException
		if (cDevice.send(sCommand, cData, cResult) != null) {
    		if (idebug > 0)
		        sc.sc_appendTextArea(sCommand + ": no properties found\n");
			return null;    // error execute directoryService() (or device not found...)
		}
		// convert the result to a String Array
		saProperty = cResult.getDataEntry("value").stringArray();
		iLen = saProperty.length; // len of the properties result array
		if (iLen == 0) {
		    // if there are no new style properties found...
		    if (idebug > 0)
		        sc.sc_appendTextArea(sCommand + ": no properties found\n");
            return null;
        }
		if (sCommand.equals("query")) {
		    if (idebug > 0) {
	            sc.sc_appendTextArea("Warning: old style DDL properties found\n");        
                // old style properties like: ALIDI-ICT-1:ADC-2TRIG ALIDI-ICT-1:ADC-TRIG
                // now remouve the ALIDI-ICT-1: (device name :)
                saProperty = removeDevice (saProperty);
            }
        }

        return saProperty;    
//    } catch (java.io.IOException e) {
    } catch (Exception e) {
        System.out.println("Error in JavaCdev.directoryService() NegativeArraySizeException = " + e);       
        bIsConnected = false;
        return null;
    }
  }
    
  /*
  ** getValues (device, properties)
  **
  ** parameter:   sDevice 
  **			  properties = the string array of properties to send
  */
  public void getValues (String sDevice, String[] saProperties) {
	int len                    = saProperties.length;
	cdev.data.Data[] caResult;
	caResult                   = new cdev.data.Data[len];
	
	try {
	  cdev.Device cDevice;
	  cDevice = sys.getDevice(sDevice);
		for (int i = 0; i < len; i++) {
	  	  caResult[i] = new cdev.data.Data();
		  cDevice.sendNoBlock("get " + saProperties[i], null, caResult[i]);
		}
	} catch (Exception e) {
      sc.sc_appendTextArea("failed JavaCdev.sendNoBlock()\n");	 // failed to sendNoBlock 
	}
	
 	// sys.pend = method in class cdev.ControlSystem 
	// Wait for server responses for the given duration. 
	try {
	  sys.pend();
	} catch (Exception e) {
      sc.sc_appendTextArea("failed sys.pend()\n");	  
	}
	
	// convert and display the caResult[] 
	try {
	  convertResults (caResult, saProperties);
	} catch (Exception e) {
      sc.sc_appendTextArea("Error: JavaCdev.convertResults()... " + e + "\n");	  
	}
  } 
  
  public String[] removeDevice(String[] saDP) {
    // remove the XXX : part of "XXXX:PPPP" from a String Array  
    int iLen;
	int iPointPos = 0;      // the position of the ":" Char in the Device Name (only old style)
    String saProperty[];
    
    try {
        iLen = saDP.length;
		// old style properties contains ":" 
        iPointPos = saDP[0].indexOf(":");
        saProperty = new String[iLen];
        //
        for (int i=0; i<iLen; i++) {
          saProperty[i] = saDP[i].substring(iPointPos+1);
        }
        return saProperty;
    } catch (Exception e) {
        System.out.println("Error in JavaCdev.removeDevice()... " + e);
        return null;    
    }
  
  }

  public void convertResults (cdev.data.Data[] acResult, String[] saProperties) {
	try {
 	  int len = acResult.length;
	  cdev.data.DataEntry de;
	
    if (idebug == 0) {
      // no debug infos...
	  for (int i=0; i<len; i++) {
		de = acResult[i].getDataEntry("value");
		if (de == null)
		  // format the result with tabs
		  if (saProperties[i].length() < 7)
  		    sc.sc_appendTextArea(saProperties[i] + " \t\t = null\n");		
		  else
  		    sc.sc_appendTextArea(saProperties[i] + " \t = null\n");		
		else
		  // format the result with tabs
		  if (saProperties[i].length() < 7)
		    sc.sc_appendTextArea(saProperties[i] + " \t\t = " + dataString((DataEntry)de) + "\n");		
          else
		    sc.sc_appendTextArea(saProperties[i] + " \t = " + dataString((DataEntry)de) + "\n");		
	  }
    }
    else {
      // some debug infos...
	  for (int i=0; i<len; i++) {
		de = acResult[i].getDataEntry("value");
		if (de == null)
		  sc.sc_appendTextArea("[" + i + "] " + saProperties[i] + " \t = \tnull\n");		
		else
		  sc.sc_appendTextArea("[" + i + "] " + saProperties[i] + " = \t" + dataString((DataEntry)de) + "\n");		
	  }
    }

	} catch (Exception e) {
	  sc.sc_appendTextArea("Fehler in convertResults " + e + "\n");	  
    }
  } 

  /*
  ** convertResult() to convert the Result only from cdevGet()
  **
  */
  public void convertResult (cdev.data.Data cResult, String saProperty) {
	try {
	  cdev.data.DataEntry de;
	
	  de = cResult.getDataEntry("value");
	  if (de == null)
		sc.sc_appendTextArea(saProperty + " = \tnull\n");		
	  else
		sc.sc_appendTextArea(saProperty + " = \t" + dataString((DataEntry)de) + "\n");		
	} catch (Exception e) {
	  sc.sc_appendTextArea("Error in convertResults " + e + "\n");	  
    }
  } 


 public String dataString(DataEntry data) {
  switch (data.getEnumeratedType()) {
	case Data.INT   : 
    	if (idebug < 2)
			return String.valueOf(data.intValue());
        else
			return String.valueOf(data.intValue() + "\t(INT)");
	case Data.LONG :
    	if (idebug < 2)
			return String.valueOf(data.longValue());
        else
            return String.valueOf(data.longValue() + "\t(LONG)");
	case Data.SHORT :
    	if (idebug < 2)
			return String.valueOf(data.shortValue());
        else
			return String.valueOf(data.shortValue() + "\t(SHORT)");
	case Data.FLOAT :
    	if (idebug < 2)
			return String.valueOf(data.floatValue());
        else
			return String.valueOf(data.floatValue() + "\t(FLOAT)");
	case Data.DOUBLE :
    	if (idebug < 2)
			return String.valueOf(data.doubleValue());
        else
			return String.valueOf(data.doubleValue() + "\t(DOUBLE)");
	case Data.BYTE :
    	if (idebug < 2)
			return String.valueOf(data.byteValue());
        else
            return String.valueOf(data.byteValue() + "\t(BYTE)");
	case Data.STRING :
    	if (idebug < 2)
			return String.valueOf(data.stringValue());
        else
			return String.valueOf(data.stringValue() + "\t(STRING)");
	default    : return "???";
  }
}

 /**
  * isConnected() returns the the connection status
  *
  * return: true  = connected to the cdevGatewayServer
  *         false = not conneted
  *
  * note; set also flag bIsConnected to true or false...
  *
  **/
  public boolean isConnected () {
	try {                             
//	  bIsConnected = sys.isConnected();
	  return bIsConnected;
	} catch (Exception e) {
	  System.out.println("JavaCdev.isConnected failed... ");
	  bIsConnected = false;
	  return false;
	}
  }


 /**
  ** disconnect()
  **
  **/
  public void disconnect () {
	try {
	  sys.disconnect();
	  if (idebug > 0)
	    sc.sc_setTextArea("CDev is now disconnected...\n Reconnect again to get any results...\n");
	  bIsConnected = false;
	} catch (Exception e) {
	    System.out.println("JavaCdev().disconnect() " + e);
	}
  }

  /*
  ** reconnectCdev() if the connection is broken, reconnect to cdevGateway
  **
  */
  public void sc_reconnectCdev () {
	try {
	  sys.reconnect();
	  //if (idebug > 0)
	    sc.sc_setTextArea("Cdev reconnect...\n");
	  bIsConnected = true;
	} catch (Exception e) {
	    sc.sc_setTextArea("Cdev reconnect failed...\n");
	    bIsConnected = false;      
	}
  }

  /**
   * read the Interrest file that is used to store the
   * information what properties will be displayed for each device
   *
   */
  public boolean readInterrestFile(String fileName) {
	  boolean bFirstElement = true;
	  interrests = ch.psi.synopview.svp.parser.SVParser.parseInterrests(fileName);
	  if (interrests==null) return false;

	  // after reading the file, add the elements to the InterrestGroup Choice Widget
	  try {
        InterrestsData id;
	    Enumeration e = interrests.elements();
	    
		// add all Interrest Groups to the Choice Widget...
		while (e.hasMoreElements()) {
          id = (InterrestsData) (e.nextElement()); 
	      sc.sc_addInterrestChoice(id.getName());
	      // if added the first Element to Choice... then 
		  if (bFirstElement) {
		  	setInterrestGroup(id.getName());
		    bFirstElement = false;
		  }
		}
	  } catch (Exception e) {
	    System.out.println("Error: JavaCdev readInterrestFile() addInterrestChoice..." + e);
	  }
	  return true;
  }

  /*
  ** send    will call cdevGet for each element in the saProperties
  **
  ** parameter:   properties = the string array of properties to send
  **			  example: ALIVA-VG-5:STATUS ...
  */
  public void send (String sDevice, String[] saProperties) {
	int len = saProperties.length;

    if (idebug > 0) {
	  sc.sc_appendTextArea("Info: cdevGet called " + len + " times\n\n");
    }
	for (int i = 0; i < len; i++) {
      cdevGet(sDevice + ":" + saProperties[i]);
    }
  } 

  /*
  ** cdevGet() does a "normal" cdevGet for one device
  **
  ** parameter:    device = the device to pass to getCdev (like "hunt:ai001")
  ** return:       String of cdevGet or null when failed
  */
  public void cdevGet (String device) {
	String sChannel = null;  // CDEV device name
	String sField   = null;  // EPICS filed name
	String sResult = null;

	StringTokenizer st = new StringTokenizer(device,".",false);
	sChannel = st.nextToken();

	try {
	 sField = st.nextToken();
	} catch (Exception e) {
	  sField = "VAL";
	}

	try {
	  cdev.data.Data cResult = new cdev.data.Data();
	  cdev.Device    myDev   = new cdev.Device(sChannel);

      // do the cdev call..
	  myDev.get(sField, cResult);
	  
	  // convert the result to string result sResult
      // ALIVA-VG-5:I-SET -> show only I-SET
      convertResult (cResult, device.substring((device.indexOf(":")+1)) );

	} catch (Exception e) {
	  // failed cdevGet
      sc.sc_appendTextArea("failed cdevGet() " + device + "\n");	  
	}
	return;
  }  

  public void setCdevMode(boolean b) {
    bCdevMode = b;
  }

  /**
   * Set the Interest Group for the device properties to the changed group.
   *
   * If there was a device selected, then get the results for this device
  */
  public void setInterrestGroup(String str) {    
	// str is the selected Interrest Group (like "BD" "CONTROLS" ...)
    idSelected = (InterrestsData)(interrests.get(str));;
/* not used anymore, because when debug is on and the interestgroup is changed...
   this output will be overwritten...
if (idebug > 0) {
        sc.sc_setTextArea("");       // clear
        if (str.equals("ALL")) {
            sc.sc_appendTextArea("ALL Properties of group " + str + " will be shown (" + idSelected.getVName().size() + ")\n");
        } else {
            sc.sc_appendTextArea("Properties of group " + str + " will be shown (" + idSelected.getVName().size() + ")\n");
            sc.sc_appendTextArea("This group contains the following properties\n");
            // show all the active selected Properties...
	        Enumeration e = idSelected.getVName().elements();
	        int i = 0;
	        while (e.hasMoreElements()) {
                String sP = (String) e.nextElement();
                sc.sc_appendTextArea("[" + i + "] = " + sP + "\n");
                i++;
	        }
	    }
	}
*/
  }

}