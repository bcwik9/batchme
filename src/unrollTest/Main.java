package unrollTest;

import java.util.ArrayList;

public class Main {

	/**
	 * Prints out a list unless it's null
	 * @param list
	 */
	private static void printUnlessNull(ArrayList<String> list){
		if(list != null)
			System.out.println(list);
	}

	public static void main(String[] args) {
		// Create a StringBatcher with a limit of 50 characters total length
		final Batcher<String> batcher = new StringBatcher(50);
		final ArrayList<Thread> threads = new ArrayList<Thread>();

		// spawn a bunch of new threads and have them interact with the batcher, adding strings of various length
		for(int i=0; i < 10000; i++){
			Thread t = new Thread(new Runnable() {
				public void run() {
					// Start adding Strings to the batcher and print out what it's returning
					printUnlessNull(batcher.submit("a"));
					printUnlessNull(batcher.submit("bb"));
					printUnlessNull(batcher.submit("ccc"));
					printUnlessNull(batcher.submit("dddd"));
					printUnlessNull(batcher.submit("eeeee"));
					printUnlessNull(batcher.submit("ffffff"));
					printUnlessNull(batcher.submit("ggggggg"));
					printUnlessNull(batcher.submit("hhhhhhhh"));
					printUnlessNull(batcher.submit("iiiiiiiii"));
					printUnlessNull(batcher.submit("jjjjjjjjjj"));
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
		
		// All done!
		System.out.println("All threads have completed (" + threads.size() + " total)");

	}

}
