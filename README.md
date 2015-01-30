# batchme
- A thread safe Batcher class, in Java, that will collect items that are submitted to it, returning them as a “batch” when a limit is reached.
- It is instantiated with a numerical limit and has a submit method.
- Submitting an item should return null, unless the limit has been reached, in which case the batcher should flush its collected items.
- For the sake of reusability, the Batcher class should be able to support items that will be batched by a limit that isn’t quantity, e.g. batching strings by a total string length limit.
- It should be expected that many threads will be trying to submit items very rapidly to one batcher instance

# Run
This is an Eclipse project (https://eclipse.org/). You can import by cloning my git repository and clicking Import in Eclipse, then import Java project and select where you cloned to. Then open the Unit test class (BatcherTest) and click Run.
