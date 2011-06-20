import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ST {

	static volatile int[] s;
	static volatile int[] t;

	static Queue<Integer> comm = new ConcurrentLinkedQueue<Integer>();

	static class PS implements Runnable {
		@Override
		public void run() {
			boolean running = true;
			while (running) {
				synchronized (comm) {
					int posMax = maxs();

					comm.offer(s[posMax]);

					comm.notifyAll(); //pnot
					try {
						comm.wait();
					} catch (InterruptedException e) {
					}

					if (!comm.isEmpty()) {
						s[posMax] = comm.poll();
					} else {
						running = false;
					}
				}
			}
		}
	}

	static class PT implements Runnable {
		@Override
		public void run() {
			boolean running = true;
			synchronized (comm) {
				
				//in cazul in care PS ajunge la linia psnot inainte 
				//ca PT sa ajunga la ptwait, fara verificarea cozii
				//de comunicare avem deadlock
				if (comm.isEmpty()) {
					try {
						comm.wait(); //ptwait
					} catch (InterruptedException e) {
					}
				}
				
				while (running) {

					int posMin = mint();

					int maxs = comm.poll();

					if (maxs > t[posMin]) {
						comm.offer(t[posMin]);
						t[posMin] = maxs;
					} else {
						running = false;
					}

					comm.notifyAll();

					if (running) {
						try {
							comm.wait();
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
	}

	static int maxs() {
		int imax = 0;

		for (int i = 0; i < s.length; i++) {
			if (s[imax] < s[i])
				imax = i;
		}

		return imax;
	}

	static int mint() {
		int imin = 0;

		for (int i = 0; i < t.length; i++) {
			if (t[imin] > t[i])
				imin = i;
		}

		return imin;
	}

	static void printSets() {
		System.out.print("S: {");
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[i]);
			if (i < s.length - 1)
				System.out.print(", ");
		}
		System.out.println("}");

		System.out.print("T: {");
		for (int i = 0; i < t.length; i++) {
			System.out.print(t[i]);
			if (i < t.length - 1)
				System.out.print(", ");
		}
		System.out.println("}");
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 2) {
			System.out.println("usage:java ST ns nt");
			return;
		}

		int ns = 0;
		int nt = 0;
		try {
			ns = Integer.parseInt(args[0]);
			nt = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("error: ns, nt must be numbers");
			return;
		}

		s = new int[ns];
		t = new int[nt];

		for (int i = 0; i < ns; i++)
			s[i] = (int) (Math.random() * 100);
		for (int i = 0; i < nt; i++)
			t[i] = (int) (Math.random() * 100);

		System.out.println("Before:");
		printSets();

		Thread ps = new Thread(new PS());
		Thread pt = new Thread(new PT());

		ps.start();
		pt.start();

		ps.join();

		System.out.println("After:");
		printSets();
	}

}
