package unrollTest;

public class Main {

	public static void main(String[] args) {
		// Create a StringBatcher with a limit of 50 characters total length
		final Batcher<String> batcher = new StringBatcher(50);

		// spawn a bunch of new threads and have them interact with the batcher, adding strings of various length
		for(int i=0; i < 10000; i++){
			new Thread(new Runnable() {
				public void run() { 
					// Start adding Strings to the batcher and print out what it's returning
					System.out.println(batcher.submit("a"));
					System.out.println(batcher.submit("bb"));
					System.out.println(batcher.submit("ccc"));
					System.out.println(batcher.submit("dddd"));
					System.out.println(batcher.submit("eeeee"));
					System.out.println(batcher.submit("ffffff"));
					System.out.println(batcher.submit("ggggggg"));
					System.out.println(batcher.submit("hhhhhhhh"));
					System.out.println(batcher.submit("iiiiiiiii"));
					System.out.println(batcher.submit("jjjjjjjjjj"));
				}
			}).start();
		}

	}

}
