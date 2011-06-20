import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Problema Producător-Consumator cu mai mulţi producători şi mai mulţi
 * consumatori.
 * 
 * @author Calin
 */
public class ProdCons implements Runnable, PaintListener {
	final Display display;
	final Shell shell;

	Queue<Character> produse = new ConcurrentLinkedQueue<Character>();
	List<Producator> producatori = new ArrayList<Producator>();
	List<Consumator> consumatori = new ArrayList<Consumator>();

	Individ accesatorCurent = null;

	private interface Individ {
		int getCode();

		char getType();
	}

	private class Producator implements Runnable, Individ {
		int code;

		public Producator(int c) {
			code = c;
		}

		@Override
		public int getCode() {
			return code;
		}

		@Override
		public char getType() {
			return 'P';
		}

		@Override
		public void run() {
			while (!shell.isDisposed()) {
				synchronized (produse) {
					synchronized (ProdCons.class) {
						producatori.remove(this);
						accesatorCurent = this;
					}

					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
					}

					char produs = (char) ('A' + (char) (Math.random() * ('Z' - 'A')));

					produse.offer(produs);

					synchronized (ProdCons.class) {
						producatori.add(this);
						accesatorCurent = null;
					}
				}

				try {
					Thread.sleep(300 + (int) (Math.random() * 1500));
				} catch (InterruptedException e) {
				}
			}
		}

		@Override
		public String toString() {
			return "" + getType() + getCode();
		}
	}

	private class Consumator implements Runnable, Individ {
		int code;

		public Consumator(int c) {
			code = c;
		}

		@Override
		public int getCode() {
			return code;
		}

		@Override
		public char getType() {
			return 'C';
		}

		@Override
		public void run() {
			while (!shell.isDisposed()) {
				synchronized (produse) {
					synchronized (ProdCons.class) {
						consumatori.remove(this);
						accesatorCurent = this;
					}

					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
					}

					produse.poll();

					synchronized (ProdCons.class) {
						consumatori.add(this);
						accesatorCurent = null;
					}
				}

				try {
					Thread.sleep(300 + (int) (Math.random() * 1500));
				} catch (InterruptedException e) {
				}
			}
		}

		@Override
		public String toString() {
			return "" + getType() + getCode();
		}
	}

	public ProdCons(Display d, Shell s, int prod, int cons) {
		display = d;
		shell = s;

		// paint listener
		shell.addPaintListener(this);

		// spawn repaint thread
		new Thread(this).start();

		// spawn producatori
		for (int i = 0; i < prod; i++) {
			Producator p = new Producator(i);
			producatori.add(p);
			new Thread(p).start();
		}

		// spawn consumatori
		for (int i = 0; i < cons; i++) {
			Consumator c = new Consumator(i);
			consumatori.add(c);
			new Thread(c).start();
		}
	}

	@Override
	public void paintControl(PaintEvent e) {
		GC context = e.gc;

		// Rectangle rect = shell.getClientArea();

		synchronized (ProdCons.class) {
			context.drawString("Consumatori", 0, 0);
			context.drawString(consumatori.toString(), 0, 40);

			context.drawString("Produse", 350, 0);

			String msg = "Activ: ";

			if (accesatorCurent != null)
				msg += accesatorCurent;
			else
				msg += "-";

			context.drawString(msg, 350, 40);
			context.drawText(produse.toString(), 350, 70);

			context.drawString("Producatori", 700, 0);
			context.drawString(producatori.toString(), 700, 40);
		}
	}

	@Override
	public void run() {
		while (!shell.isDisposed()) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (!shell.isDisposed())
						shell.redraw();
				}
			});

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		int nprod = 0;
		int ncons = 0;

		try {
			nprod = Integer.parseInt(args[0]);
			ncons = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("usasge:java ProdCons nprod ncons");
			return;
		}

		new ProdCons(display, shell, nprod, ncons);

		shell.setText("Producatori consumatori");
		shell.setBackground(new Color(display, 255, 255, 255));
		shell.setBounds(0, 0, 800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
