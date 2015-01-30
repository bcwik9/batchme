/**
 * 
 */
package unrollTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class BatcherTest {
	// simple lock to prevent ArrayList corruption when we add output from the batcher buffer
	String lock = "lock!";

	/**
	 * Prints out a list unless it's null. Also adds it to output list
	 * @param list
	 */
	private void printUnlessNull(ArrayList<String> list, ArrayList<String> output){
		if(list != null) {
			synchronized(lock) {
				System.out.println(list);
				output.addAll(list);
			}
		}
	}
	
	/**
	 * Tests to make sure initializing a Batcher with an invalid limit throws an exception
	 */
	@Test
	public void testInvalidLimit() {
		try {
			// Create a new batcher with invalid limit
			new Batcher<String>(-1);
			// Fail because the above statement should have thrown an exception!
			fail("passed illegal limit to Batcher and it didn't throw an exception!");
		} catch(IllegalArgumentException e) {
			// Ignore, it was supposed to throw this exception
		}

	}
	
	/**
	 * Test to make sure an exception is thrown for trying to submit an object which is too big to
	 * fit in an empty batch buffer
	 */
	@Test
	public void testInvalidObjectSubmit() {
		// Create a StringBatcher with a limit of 5
		StringBatcher batcher = new StringBatcher(5);
		
		try {
			// Submit a string with a length longer than 5, should throw an exception
			batcher.submit("hello world");
			fail("previous submit should have thrown an exception!");
		} catch(IllegalArgumentException e) {
			// Ignore, it was supposed to throw this exception
		}
	}

	/**
	 * Test to ensure we get the correct amount of flushed output from a StringBatcher
	 */
	@Test
	public void testCorrectNumOutput() {
		// Create a StringBatcher with a limit of 50 characters total length
		final Batcher<String> batcher = new StringBatcher(50);
		// track the threads we're going to spawn
		ArrayList<Thread> threads = new ArrayList<Thread>();
		// output list to track what batcher flushes out
		final ArrayList<String> output = new ArrayList<String>();
		// create some words to submit to the batcher
		final ArrayList<String> words = new ArrayList<String>();
		words.add("a");
		words.add("bb");
		words.add("ccc");
		words.add("dddd");
		words.add("eeeee");
		words.add("ffffff");
		words.add("ggggggg");
		words.add("hhhhhhhh");
		words.add("iiiiiiiii");
		words.add("jjjjjjjjjj");

		// spawn a bunch of new threads and have them interact with the batcher, adding strings of various length
		int num_threads = 10000;
		for(int i=0; i < num_threads; i++){
			Thread t = new Thread(new Runnable() {
				public void run() {
					// Start adding Strings to the batcher and print out what it's returning
					for(String word: words)
						printUnlessNull(batcher.submit(word), output);
				}
			});

			// Have the thread start and add it to our list of threads
			t.start();
			threads.add(t);
		}

		// Wait for all threads to complete
		for(Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// All done! Output some info
		System.out.println("All threads have completed (" + threads.size() + " total)");
		int processedSize = output.size() + batcher.flush().size();
		System.out.println("Number of words that have been processed: " + processedSize);
		System.out.flush();
		// Check to make sure we got the correct number of strings
		if(processedSize != (num_threads * words.size()))
			fail("number of processed words is not what we expected!");
	}

}
