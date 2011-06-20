import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Conway {

	static char[] input = new char[100];
	static char[] temp = new char[100];
	static char[] output = new char[100];

	static Display display = new Display();

	static Shell shell1 = new Shell(display);
	static final Text text1 = new Text(shell1, SWT.BORDER | SWT.SINGLE);

	static {
		shell1.setLayout(new FillLayout());
		shell1.setText("Input");
		shell1.setSize(300, 50);
		shell1.open();
		text1.setTextLimit(99);
	}

	static Shell shell2 = new Shell(display);
	static final Text text2 = new Text(shell2, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);

	static {
		shell2.setLayout(new FillLayout());
		shell2.setText("Output");
		shell2.setSize(300, 400);
		shell2.open();
	}

	static boolean running = true;
	
	static Citeste citeste = new Citeste();

	static class Citeste implements KeyListener {

		String consoleInput;

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.CR) {
				consoleInput = text1.getText();
				text1.setText("");

				synchronized (input) {

					char[] tmpImp = consoleInput.toCharArray();

					System.arraycopy(tmpImp, 0, input, 0, tmpImp.length);
					input[tmpImp.length] = '$';

					synchronized (temp) {
						System.arraycopy(input, 0, temp, 0, tmpImp.length + 1);

						text1.removeKeyListener(this);
						
						temp.notifyAll();
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

	static class Temp implements Runnable {
		public void run() {
			while (running) {
				synchronized (temp) {
					try {
						temp.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(!running)
						break;

					synchronized (output) {
						int state = 0;
						int i = 0; // temp
						int j = 0; // output

						while (i < temp.length || j < output.length) {
							char ch = temp[i];

							if (ch == '*') {
								switch (state) {
								case 0: {
									state = 1;
									i++;
									break;
								}

								case 1: {
									state = 0;
									output[j++] = '!';
									i++;
									break;
								}
								}
							} else {
								if (state == 1) {
									output[j++] = '*';
									state = 0;
								}
								output[j++] = temp[i++];
							}

							if (ch == '$')
								break;
						}

						output.notifyAll();
					}
				}
			}
		}
	}

	static class Scrie implements Runnable {
		public void run() {
			while (running) {
				synchronized (output) {
					try {
						output.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(!running)
						break;

					int ch = 0;
					final StringBuffer sb = new StringBuffer();
					do {
						sb.append(output[ch]);
					} while (output[ch++] != '$');

					display.syncExec(new Runnable() {
						public void run() {
							text2.append(sb.toString());
							text2.append("\n");

							text1.addKeyListener(citeste);
						}
					});
				}
			}
		}
	}

	public static void main(String[] args) {
		text1.addKeyListener(citeste);
		new Thread(new Temp()).start();
		new Thread(new Scrie()).start();


		while (!shell1.isDisposed() && !shell2.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		running = false;
		synchronized (temp) {
			temp.notifyAll();
		}
		synchronized (output) {
			output.notifyAll();
		}
		
		display.dispose();
	}
}
