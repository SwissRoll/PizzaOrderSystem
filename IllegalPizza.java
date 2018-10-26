package application;
/**
 * An exception object used by the Pizza class.
 * @author Declan Rowett
 */
@SuppressWarnings("serial")
public class IllegalPizza extends Exception {
	
	/**
	 * Propagates exception to <code>Exception</code> class.
	 * @param message Error message to be propagated to <code>Exception</code>.
	 */
	public IllegalPizza(String message) {
		super(message);
	}
	
}
