import java.io.*;

class SaveOutput extends PrintStream {
  static OutputStream logfile;
  static PrintStream oldStdout;
  static PrintStream oldStderr;

  SaveOutput(PrintStream ps) {
	  super(ps);
  }  
  
  // Starts copying stdout and 
  //stderr to the file f.
  public static void start(
   String f) throws IOException {
	  // Save old settings.
	  oldStdout = System.out;
	  oldStderr = System.err;

	  // Create/Open logfile.
	  logfile = new PrintStream(
		  new BufferedOutputStream(
		  new FileOutputStream(f)));

	  // Start redirecting the output.
	  System.setOut(
	   new SaveOutput(System.out));
	  System.setErr(
	   new SaveOutput(System.err));
  }  
  
  // Restores the original settings.
  public static void stop() {
	  if (oldStdout != null) {
        // stdout was defined by calling SaveOutput.start... so stop it now
	    System.setOut(oldStdout);
	    System.setErr(oldStderr);
	    try {
	      System.out.println("Logfile.close()..." );
		  logfile.close();
	    } catch (Exception e) {
		  e.printStackTrace();
	    }
      }
  }  
  
  // PrintStream override.
  public void write(
   byte buf[], int off, int len) {
	  try {
		  logfile.write(buf, off, len);
	  } catch (Exception e) {
		  e.printStackTrace();
		  setError();
	  }
	  super.write(buf, off, len);
  }  
  // PrintStream override.
  public void write(int b) {
	  try {
		  logfile.write(b);
	  } catch (Exception e) {
		  e.printStackTrace();
		  setError();
	  }
	  super.write(b);
  }  
}
