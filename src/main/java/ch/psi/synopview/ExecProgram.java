package ch.psi.synopview;

import java.util.logging.Logger;

public class ExecProgram {

	private static final Logger logger = Logger.getLogger(ExecProgram.class.getName());

	public static void run(String s) {
		if (s == null)
			return;

		if (s != "") {
			logger.info("Try to run: " + s);
			try {
				Runtime.getRuntime().exec(s);
			} catch (java.io.IOException e) {
				logger.warning("Unable to run: " + s);
			}
			return;
		}
	}
}
