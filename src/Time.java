import java.util.*;
import java.io.*;

public class Time implements Runnable {
  // SV control interface
  SVControlSystem sc;

  private Thread clock;
  private int    delay;

  public Time() 
  {
	  delay = 10000;        // all 10 seconds
	  start();
  }  
  public void run() {
	Thread.currentThread().setPriority(Thread.MIN_PRIORITY);	
	while(true) {
	  // WinNT date = Tue Nov 30 10:23:25 CET 1999
	  // Linux date = Tue Nov 30 10:23:25 CMT+01:00 1999
	  //              sW  sM  sD    sT    sTz        sY

      String sDate;
	  String sTimeDate;
	  try {
	    Date date = new Date();
	    sDate = date.toString();

		StringTokenizer st = new StringTokenizer(sDate, " \t\r\n");
		String sW  = st.nextToken();
		String sM  = st.nextToken(); 
		String sD  = st.nextToken();
		String sT  = st.nextToken();
		String sTz = st.nextToken();
		String sY  = st.nextToken();
		
		// the sTimeDate will look lke:
		// Tue Nov 30 1999  10:23:25
		sTimeDate = ""+sW+" "+sM+" "+sD+" "+sY+"  "+sT;
	    sc.sc_setDateandTime(sDate);
	  } catch( Exception e ) {
	  }

	  try {
		Thread.sleep(delay);
	  } catch( InterruptedException e ) {
		System.out.println("Warning: Time.java Tread.sleep Exception!");  
	  }
	}  // end while    
  }    // end run()  
  public void setSVControlSystem(SVControlSystem sc) {
	this.sc=sc;
  }  
  public void start() {
	clock = new Thread(this); 	// start the thread
	clock.start();	
  }  
  public void stop() {
	clock.stop();
  }  
}
