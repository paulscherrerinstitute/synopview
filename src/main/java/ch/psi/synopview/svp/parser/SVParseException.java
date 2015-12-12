package ch.psi.synopview.svp.parser;

public class SVParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public SVParseException() {
		super();
	}

	public SVParseException(String s) {
		super(s);
	}

	public SVParseException(String desc, java.io.StreamTokenizer t, String fileName) {
		super(desc);
		System.out.print("\no) Parse error in file '" + fileName + "', line " + t.lineno() + ": ");

	}
}
