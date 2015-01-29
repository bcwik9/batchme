package unrollTest;

/**
 * StringBatcher is a Batcher that has a limit based on string size (number of characters)
 */
public class StringBatcher extends Batcher<String> {

	public StringBatcher(int limit) throws IllegalArgumentException {
		super(limit);
	}

	/**
	 * Since the limit represents the total number of characters for all strings, all we need to change
	 * is how we determine how much space a string takes up
	 */
	protected int sizeOfSingleObject(String s) {
		return s.length();
	}
}
