package example;

import java.util.*;

public class MyServerV2
{
	List<String> l1;

	protected MyServerV2() {
		l1 = new ArrayList<String>();
	}

	public int countStart(String s) {
		int n = 0;
		synchronized( l1) {
			Iterator<String> it = l1.iterator();
			while (it.hasNext()) {
				if (it.next().startsWith(s))
					n++;
			}
		}
		return n;
	}

	public void add(String s) {
		synchronized( l1) {
			l1.add(s);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final MyServerV2 s = new MyServerV2();
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
