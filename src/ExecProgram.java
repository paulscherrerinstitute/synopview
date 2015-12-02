import java.util.*;

public class ExecProgram {

  public static void run (String s) {
	if (s == null)
	  return;

	if (s != "") {
	  System.out.println  ("Info:    try to run: " + s);
	  try {
		Runtime.getRuntime().exec(s);
	  } catch (java.io.IOException e) {
		System.out.println("Error:   try to run: " + s);
	  }
	  return;
	}
  }  
}
