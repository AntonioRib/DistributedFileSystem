package example;

import java.util.*;

public class MyServerV1
{
	List<String> l1;

	protected MyServerV1() {
		l1 = new ArrayList<String>();
	}

	public synchronized int countStart(String s) {
		Iterator<String> it = l1.iterator();
		int n = 0;
		while (it.hasNext()) {
			if (it.next().startsWith(s))
				n++;
		}
		return n;
	}

	public synchronized void add(String s) {
		l1.add(s);
	}

	public static void main(String[] args) throws InterruptedException {
		final MyServerV1 s = new MyServerV1();
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
