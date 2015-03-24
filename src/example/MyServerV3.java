package example;

import java.util.*;
import java.util.concurrent.*;

public class MyServerV3
{
	ConcurrentLinkedDeque<String> l1;

	protected MyServerV3() {
		l1 = new ConcurrentLinkedDeque<String>();
	}

	public int countStart(String s) {
		int n = 0;
			Iterator<String> it = l1.iterator();
			while (it.hasNext()) {
				if (it.next().startsWith(s))
					n++;
			}
		return n;
	}

	public void add(String s) {
			l1.add(s);
	}

	public static void main(String[] args) throws InterruptedException {
		final MyServerV3 s = new MyServerV3();
		Thread th1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100000; i++) {
					s.add("texto" + i);
				}
			}

		});
		Thread th2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10000; i++) {
					s.countStart("texto");
				}
			}
		});
		long timestart = System.currentTimeMillis();
		th1.start();
		th2.start();
		th1.join();
		th2.join();
		long timeend = System.currentTimeMillis();
		System.out.println( "runtime = " + (timeend - timestart));

	}

}
