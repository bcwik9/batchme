package unrollTest;

import java.util.ArrayList;


/**
 * Batcher is a class that you can submit objects to and it will store them until it reaches a limit,
 * at which point it will flush the batch out
 */
public class Batcher<E> {
	private ArrayList<E> batch;
	private final int limit;
	private int currentSize;

	/**
	 * Initializer takes a "limit", which determines when the batch buffer is full and needs
	 * to be flushed. We also track the current size of the batch buffer to make size calculations faster
	 * @return a new Batcher
	 */
	public Batcher(int limit) throws IllegalArgumentException {
		if(limit < 0)
			throw new IllegalArgumentException("limit specified is invalid");

		this.limit = limit;
		currentSize = 0;
		this.batch =  new ArrayList<E>();
	}

	/**
	 * @return true if we have reached the limit, false otherwise
	 */
	private synchronized int getCurrentSize() {
		return currentSize;
	}

	/**
	 * @return true if the batch can hold the object without extending past the limit
	 */
	private synchronized boolean canAddWithoutFlush(E e) {
		return (getCurrentSize() + sizeOfSingleObject(e)) <= getLimit();
	}

	/**
	 * Determines the size of an individual object
	 * @return size of a single object in terms of batch limit
	 */
	protected int sizeOfSingleObject(E e) {
		// Return 1 by default since it takes up a single batch entry
		return 1;
	}

	/**
	 * @return true if the object size is smaller than the limit
	 */
	public boolean willFit(E e) {
		return sizeOfSingleObject(e) <= getLimit();
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	
	/**
	 * Creates a copy of the batch and flushes the batch out
	 * @return a copy of the batch
	 */
	public ArrayList<E> flush() {
		ArrayList<E> ret;

		synchronized(this){
			// Make a copy of the batch to return
			ret = new ArrayList<E>(batch);

			// Clear out the batch and reset size variable
			batch.clear();
			currentSize = 0;
		}

		// Return the copy
		return ret;
	}

	/**
	 * Add an entry to the batch. If we have reached the limit, flush the batch
	 * Synchronized to preserve thread safety since we're expecting multiple threads
	 * to be submitting objects to the batch
	 * @return null if the limit hasn't been reached, otherwise return the flushed batch
	 */
	public synchronized ArrayList<E> submit(E e) {
		// Make sure that the object can fit in an empty batch (ie. size less than the limit)
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
