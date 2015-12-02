package svp.parser;

/**

 * Insert the type's description here.

 * Creation date: (12/7/99 19:06:01)

 * @author: Matej Sekoranja

 */

public class SVParseException extends Exception {

/**

 * SVParseException constructor comment.

 */

public SVParseException() {

	super();

}
/**

 * SVParseException constructor comment.

 * @param s java.lang.String

 */

public SVParseException(String s) {

	super(s);

}
/**

 * Insert the method's description here.

 * Creation date: (12/7/99 19:06:46)

 * @param desc java.lang.String

 * @param t java.io.StreamTokenizer

 * @param fileName java.lang.String

 */

public SVParseException(String desc, java.io.StreamTokenizer t, String fileName) {

	super(desc);

	System.out.print("\no) Parse error in file '"+fileName+"', line "+t.lineno()+": ");

}
}
