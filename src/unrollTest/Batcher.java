package unrollTest;

import java.util.ArrayList;


/**
 * Batcher is a class that you can submit objects to and it will store them until it reaches a limit,
 * at which point it will flush the batch out
 * Some of the methods have been synchronized to preserve thread safety
 */
public class Batcher<E> {
	private ArrayList<E> batch;
	private int limit;
	private int currentSize;

	/**
	 * Initializer takes a "limit", which determines when the batch buffer size.
	 * We also track the current size of the batch buffer to make size calculations faster
	 * @return a new Batcher
	 */
	public Batcher(int limit) throws IllegalArgumentException {
		if(limit <= 0)
			throw new IllegalArgumentException("limit specified is invalid");

		this.limit = limit;
		currentSize = 0;
		this.batch =  new ArrayList<E>();
	}

	/**
	 * @return the current size of the batch buffer
	 */
	private synchronized int getCurrentSize() {
		return currentSize;
	}

	/**
	 * @return true if the batch buffer can hold the object without extending past the batch buffer limit
	 */
	private synchronized boolean canAddWithoutFlush(E e) {
		return (getCurrentSize() + sizeOfSingleObject(e)) <= getLimit();
	}

	/**
	 * Determines the size of an individual object will take up in the batch buffer
	 * @return size of a single object
	 */
	protected int sizeOfSingleObject(E e) {
		// Return 1 by default since it takes up a single batch buffer entry
		return 1;
	}

	/**
	 * @return true if the object size is smaller than the batch buffer limit
	 */
	public boolean willFit(E e) {
		return sizeOfSingleObject(e) <= getLimit();
	}

	/**
	 * @return the batch buffer limit
	 */
	public int getLimit() {
		return limit;
	}
	
	/**
	 * Creates a copy of the batch buffer and flushes the batch bufferout
	 * @return a copy of the batch buffer
	 */
	public ArrayList<E> flush() {
		ArrayList<E> ret;

		synchronized(this){
			// Make a copy of the batch buffer to return
			ret = new ArrayList<E>(batch);

			// Clear out the batch buffer and reset size variable
			batch.clear();
			currentSize = 0;
		}

		// Return the copy
		return ret;
	}

	/**
	 * Add an entry to the batch buffer. If we have reached the batch buffer limit, flush the batch buffer
	 * Synchronized to preserve thread safety since we're expecting multiple threads
	 * to be submitting objects to the batch
	 * @return null if the limit hasn't been reached, otherwise return the flushed batch buffer
	 */
	public synchronized ArrayList<E> submit(E e) {
		// Make sure that the object can fit in an empty batch buffer (ie. size less than the buffer limit)
		if(!willFit(e))
			throw new IllegalArgumentException("object being submitted is too large for current limit!");

		// Flush if we can't hold the object in our current batch
		ArrayList<E> ret = null;
		if(!canAddWithoutFlush(e))
			ret = flush();

		// Add object to batch and increment size variable
		batch.add(e);
		currentSize += sizeOfSingleObject(e);

		return ret;
	}
}
