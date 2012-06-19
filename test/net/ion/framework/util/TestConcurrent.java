package net.ion.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

public class TestConcurrent extends TestCase {

	public void testSemaphore() {
		Runnable limitedCall = new Runnable() {
			final Random rand = new Random();
			final Semaphore available = new Semaphore(3);
			int count = 0;

			public void run() {
				int time = rand.nextInt(15);
				int num = count++;

				try {
					available.acquire();

					System.out.println("Executing " + "long-running action for " + time + " seconds... #" + num);

					Thread.sleep(time * 1000);

					System.out.println("Done with #" + num + "!");

					available.release();
				} catch (InterruptedException intEx) {
					intEx.printStackTrace();
				}
			}
		};

		for (int i = 0; i < 10; i++)
			new Thread(limitedCall).start();

		// new InfinityThread().startNJoin();
	}

	public void testRace() throws InterruptedException, java.io.IOException {
		System.out.println("Prepping...");

		Race r = new Race("Beverly Takes a Bath", "RockerHorse", "Phineas", "Ferb", "Tin Cup", "I'm Faster Than a Monkey", "Glue Factory Reject");

		System.out.println("It's a race of " + r.getDistance() + " lengths");

		System.out.println("Press Enter to run the race....");
		System.in.read();

		r.run();
		// new InfinityThread().startNJoin();
	}

}

class Race {
	private Random rand = new Random();

	private int distance = rand.nextInt(250);
	private CountDownLatch start;
	private CountDownLatch finish;

	private List<String> horses = new ArrayList<String>();

	public Race(String... names) {
		this.horses.addAll(Arrays.asList(names));
	}

	public int getDistance() {
		return distance;
	}

	public void run() throws InterruptedException {
		System.out.println("And the horses are stepping up to the gate...");
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch finish = new CountDownLatch(horses.size());
		final List<String> places = Collections.synchronizedList(new ArrayList<String>());

		for (final String h : horses) {
			new Thread(new Runnable() {
				public void run() {
					try {
						System.out.println(h + " stepping up to the gate...");
						start.await();

						int traveled = 0;
						while (traveled < distance) {
							// In a 0-2 second period of time....
							Thread.sleep(rand.nextInt(3) * 1000);

							// ... a horse travels 0-14 lengths
							traveled += rand.nextInt(15);
							System.out.println(h + " advanced to " + traveled + "!");
						}
						finish.countDown();
						System.out.println(h + " crossed the finish!");
						places.add(h);
					} catch (InterruptedException intEx) {
						System.out.println("ABORTING RACE!!!");
						intEx.printStackTrace();
					}
				}
			}).start();
		}

		System.out.println("And... they're off!");
		start.countDown();

		finish.await();
		System.out.println("And we have our winners!");
		System.out.println(places.get(0) + " took the gold...");
		System.out.println(places.get(1) + " got the silver...");
		System.out.println("and " + places.get(2) + " took home the bronze.");
	}
}