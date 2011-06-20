import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.border.Border;
import java.util.Vector;

interface Function
{
	public double evaluate(double x);
	public double evaluate_1(double x);
	public String toString();
}

class MyTestFunction implements Function
{
	public double evaluate(double x)
	{
		return Math.log(x * x + 1) - Math.exp(0.4 * x) * Math.cos(Math.PI * x);
	}
	
	public double evaluate_1(double x)
	{
		return (2 * x / x * x + 1) - 0.4 * Math.exp(0.4 * x) * Math.cos(Math.PI * x) + 
						Math.PI * Math.exp(0.4 * x) * Math.sin(Math.PI * x);
	}
	
	public String toString()
	{
		return new String("ln(x^2 + 1) - (e^0.4*x)*cos(PI*x)");
	}
}

class FunctionGraphic
{
	//quality  - the less the better
	private final static double EPSILON = 0.005;
	
	private Function F;
	
	FunctionGraphic(Function f)
	{
		F = f;
	}
	
	public double evaluate(double value)
	{
		return F.evaluate(value);
	}
	public double evaluate_1(double value)
	{
		return F.evaluate_1(value);
	}
  
	public void drawFunctionToGraphic(Graphics2D g, int x, int y, int UNITYX, int UNITYY)
	{
		int _x, _y;
		
		g.setColor(Color.red);
		g.drawString("f(x) = " + F.toString(), 15, 20);

		g.setColor(Color.blue);
		for(double i = 0;; i += EPSILON)
		{
			_x = (int)(i * UNITYX) + x / 2;
			_y = -(int)(F.evaluate(i) * UNITYY) + y / 2;
			
			g.drawLine(_x, _y,
			   (int)((i + EPSILON) * UNITYX) + x / 2, 
			   -(int)(F.evaluate(i + EPSILON) * UNITYY) + y / 2);
                	
			if(_x < 0  || _x > x || _y < 0 || _y > y)				
				break;
		}
               	
		for(double i = 0;; i -= EPSILON)
		{
			_x = (int)(i * UNITYX) + x / 2;
			_y = -(int)(F.evaluate(i) * UNITYY) + y / 2;
			
			g.drawLine(_x, _y,
			   (int)((i + EPSILON) * UNITYX) + x / 2, 
			   -(int)(F.evaluate(i + EPSILON) * UNITYY) + y / 2);
                	
			if(_x < 0  || _x > x || _y < 0 || _y > y)				
				break;
		}                     
	}  
}

class PointDP
{
	public double x;
	public double y;
	
	public PointDP()
	{
		x = 0;
		y = 0;
	}
	
	public PointDP(double a, double b)
	{
		x = a;
		y = b;
	}
}

class Graphic extends JComponent implements ActionListener
{        	
	private static Graphic instance;
     	
	private int frameWidth;
	private int frameHeight;
	private int unityX;
	private int unityY;
	
	private FunctionGraphic function = null;
	
	private Timer theTimer = new Timer(1000, this);
	
	private int type;
	private double err;
	private boolean complete;
	
	private Vector<PointDP> An = new Vector<PointDP>();
	private Vector<PointDP> Bn = new Vector<PointDP>();
	private Vector<PointDP> Cn = new Vector<PointDP>();
	
	Graphic(int width, int height, int ux, int uy)
	{   
		frameWidth = width;
		frameHeight = height;
		
		unityX = ux;
		unityY = uy;
		
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(frameWidth, frameHeight));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		instance = this;
		
		function = new FunctionGraphic(new MyTestFunction());
	}
	
	public void setUnityX(int ux)
	{
		unityX = ux;
		repaint();
	}
	
	public void setUnityY(int uy)
	{
		unityY = uy;
		repaint();
	}
	
	public void setDimensions(Dimension dim)
	{
		frameWidth = dim.width;
		frameHeight = dim.height;
		
		setSize(frameWidth, frameHeight);
	}
	
	private void startCurrentMethodClock(double a, double b)
	{
		An.clear();
		Bn.clear();
		Cn.clear();
		
		switch(type)
		{
			case 0:	//bisectiei
				An.add(new PointDP(a, 0.0));
				Bn.add(new PointDP(b, 0.0));
				Cn.add(new PointDP(((An.lastElement()).x + (Bn.lastElement()).x) / 2, 0.0));
			break;
			case 1:	//coardei
			case 2:	//secantei
				An.add(new PointDP(a, 0.0));
				An.add(new PointDP(b, 0.0));
			break;
			case 3:	//newton
				An.add(new PointDP(a, 0.0));
			break;
		}
		
		theTimer.start();
		
		repaint();
	}
	private void doCurrentMethodStep()
	{
		switch(type)
		{
			case 0:	//bisectiei
				if(Math.abs(function.evaluate((Cn.lastElement()).x)) < err)
				{
					complete = true;
					theTimer.stop();
				}
				else if(function.evaluate((An.lastElement()).x) * function.evaluate((Cn.lastElement()).x) < 0)
				{
					Bn.add(Cn.lastElement());
					Cn.add(new PointDP(((An.lastElement()).x + (Bn.lastElement()).x) / 2, 0.0));
				}
				else 
				{
					An.add(Cn.lastElement());
					Cn.add(new PointDP(((An.lastElement()).x + (Bn.lastElement()).x) / 2, 0.0));
				}
			break;
			case 1:	//coardei
			{
				double evn = function.evaluate((An.lastElement()).x);
				if(Math.abs(evn) < err)
				{
					complete = true;
					theTimer.stop();
				}
				else
				{
					double ev0 = function.evaluate((An.firstElement()).x);
					double newVal = (An.firstElement().x * evn - An.lastElement().x * ev0) / (evn - ev0);
					An.add(new PointDP(newVal, 0.0));	
				}
			}
			break;
			case 2:	//secantei
			{
				double exn = function.evaluate((An.lastElement()).x);
				if(Math.abs(exn) < err)
				{
					complete = true;
					theTimer.stop();
				}
				else
				{
					double exn1 = function.evaluate((An.elementAt(An.size() - 2)).x);
					double newVal = (An.elementAt(An.size() - 2).x * exn - (An.lastElement()).x * exn1) / (exn - exn1);
					An.add(new PointDP(newVal, 0.0));	
				}
			}
			break;
			case 3:	//newton
				double xn = An.lastElement().x;
				if(Math.abs(function.evaluate(xn)) < err)
				{
					complete = true;
					theTimer.stop();
				}
				An.add(new PointDP(xn - function.evaluate(xn) / function.evaluate_1(xn), 0.0));	
			break;
		}
	}
	
	private void paintCurrentMethodState(Graphics2D g2d)
	{
		switch(type)
		{
			case 0:	//bisectiei
				if(Cn.size() > 0)
				{
					g2d.setColor(Color.green);
					for(int pnt = 0; pnt < An.size(); pnt++)
						drawPoint(An.elementAt(pnt), g2d);
					g2d.setColor(Color.orange);
					for(int pnt = 0; pnt < Bn.size(); pnt++)
						drawPoint(Bn.elementAt(pnt), g2d);
						
					g2d.setColor(Color.red);
					drawPoint(Cn.lastElement(), g2d);
					
					g2d.setColor(Color.blue);
					g2d.drawString("Pasul " + Cn.size(), 15, 35);
					g2d.drawString("[An, Bn] = [" + An.lastElement().x + ", " + Bn.lastElement().x + "]", 15, 50);
					g2d.drawString("Cn = " + Cn.lastElement().x, 15,65);
					
					if(complete)
					{
						g2d.setColor(Color.red);
						g2d.drawString("Algoritm terminat in " + Cn.size() + " pasi.", 15, 80);
						g2d.drawString("Rezultatule este " + Cn.lastElement().x + ", cu eroarea de +-" + err + ".", 15, 95);
					}
				}
			break;
			case 3:	//newton
			case 2:	//secantei
			case 1:	//coardei
				g2d.setColor(Color.green);
				for(int pnt = 0; pnt < An.size() - 1; pnt++)
					drawPoint(An.elementAt(pnt), g2d);
				g2d.setColor(Color.red);
					drawPoint(An.lastElement(), g2d);
					
				g2d.setColor(Color.blue);
				g2d.drawString("Pasul " + An.size(), 15, 35);
				if(type != 3)
					g2d.drawString("[x0, x1] = [" + An.elementAt(0).x + ", " + An.elementAt(1).x + "]", 15, 50);
				else
					g2d.drawString("x0 = " + An.elementAt(0).x, 15, 50);	
				g2d.drawString("xn = " + An.lastElement().x, 15,65);
				if(complete)
				{
					g2d.setColor(Color.red);
					g2d.drawString("Algoritm terminat in " + An.size() + " pasi.", 15, 80);
					g2d.drawString("Rezultatule este " + An.lastElement().x + ", cu eroarea de +-" + err + ".", 15, 95);
				}
			break;
		}
	}
	
	private void drawPoint(PointDP point, Graphics2D g)
	{
		g.fillOval(frameWidth / 2 + (int)(point.x * unityX) - 3, frameHeight / 2 - (int)(point.y * unityY) - 3, 6, 6);
	}
	
	public void startApplyingMethod(int type, double err, double a, double b)
	{
		this.type = type;
		this.err = err;
		complete = false;
		
		startCurrentMethodClock(a, b);
	}
		
	public void actionPerformed(ActionEvent e) 
	{
    Object source = e.getSource();
    if( source instanceof Timer )
		{
			doCurrentMethodStep();
			repaint();
    }
	}
	
	public void paintComponent(Graphics g) 
	{ 
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.setStroke(new BasicStroke(1.0f));
		
		if (isOpaque()) 
		{ 
			g2d.setColor(getBackground());
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
			
		g2d.setColor(Color.black);    
		g2d.drawLine(0, frameHeight / 2, frameWidth, frameHeight / 2);
		g2d.drawLine(frameWidth / 2, 0, frameWidth / 2, frameHeight);
			
		for(int i = unityX ; i < frameWidth / 2; i += unityX)
		{
			g2d.drawLine(frameWidth / 2 + i, frameHeight / 2 - 3, frameWidth / 2 + i, frameHeight / 2 + 3);
			g2d.drawLine(frameWidth / 2 - i, frameHeight / 2 - 3, frameWidth / 2 - i, frameHeight / 2 + 3);
		}
			
		for(int i = unityY ; i < frameHeight / 2; i += unityY)
		{
			g2d.drawLine(frameWidth / 2 - 3, frameHeight / 2 + i, frameWidth / 2 + 3, frameHeight / 2 + i);
			g2d.drawLine(frameWidth / 2 - 3, frameHeight / 2 - i, frameWidth / 2 + 3, frameHeight / 2 - i);
		}
			
		g2d.setColor(Color.blue);
		function.drawFunctionToGraphic(g2d, frameWidth, frameHeight, unityX, unityY);		
		
		paintCurrentMethodState(g2d);
	
		g2d.dispose();
	}
}

class userDialog extends JDialog
			implements PropertyChangeListener
{
	private JOptionPane optionPane;
	private JTextField left;
	private JTextField right;
	private JTextField error;
	
	private String button1 = "Ok";
	private String button2 = "Cancel";
	
	private String text1 = "A:";
	private String text2 = "B:";
	private String text3 = "Eroare:";
	
	private ApplicationFrame parentFrame;
	private Graphic graphic;
	
	userDialog(ApplicationFrame frame, Graphic gr)
	{
		super(frame, "Precizati intervalul...", true);
		parentFrame = frame;
		graphic = gr;
		
		left = new JTextField(10);
		right = new JTextField(10);
		error = new JTextField(10);
		
		Object[] optionPaneComponent = { text1, left, text2, right, text3, error };
		Object[] optionPaneButtons = { button1, button2 }; 
		
		optionPane = new JOptionPane(optionPaneComponent,
					JOptionPane.QUESTION_MESSAGE,
                                    	JOptionPane.YES_NO_OPTION,
                                    	null,optionPaneButtons,
                                    	optionPaneButtons[0]);
                                    	
		setContentPane(optionPane);
		pack();

		optionPane.addPropertyChangeListener(this);
		
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setResizable(false);	
	}
    	
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		
		if (isVisible()
			&& (e.getSource() == optionPane)
			&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
				JOptionPane.INPUT_VALUE_PROPERTY.equals(prop)))
		{
			Object value = optionPane.getValue();
			
			if (value == JOptionPane.UNINITIALIZED_VALUE) return;
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			
			if(button1.equals(value))
			{
				try
				{
					double a = Double.parseDouble(left.getText());
					double b = Double.parseDouble(right.getText());
					double err = Double.parseDouble(error.getText());
					
					if(a > b)
					{
						JOptionPane.showMessageDialog(
											this,
											"A < B!!!",
											null,
											JOptionPane.ERROR_MESSAGE);	
					}
					else
					{
						hideIt();
						graphic.startApplyingMethod(parentFrame.getSelectedMethod(), err, a, b);					
					}
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(
											this,
											"Trebuie sa fie numar real!",
											null,
											JOptionPane.ERROR_MESSAGE);	
				}
			}
			
			else if(button2.equals(value))
			{
				hideIt();
			}
		}		
	}
	
	public void showIt()
	{
		left.setText(null);
		right.setText(null);
		
		setLocationRelativeTo(parentFrame);
		
		setVisible(true);
	}
	
	public void hideIt()
	{
		setVisible(false);
	}
}

class ApplicationFrame extends JFrame 
		       implements ActionListener, ComponentListener, ChangeListener
{

    	private int frameWidth = 700;
    	private int frameHeight = 725;
		
		private static final int SL_UNIT_MIN = 2;
		private static final int SL_UNIT_MAX = 100;
		private int ux = 20;
		private int uy = 20;
		
		private String[] Methods = {"Metoda Bisectiei", "Metoda Coardei", "Metoda Secantei", "Metoda lui Newton"};
		private JComboBox ChooseMethod = new JComboBox();
		
		JSlider unitXSlider = new JSlider(JSlider.HORIZONTAL, SL_UNIT_MIN, SL_UNIT_MAX, ux);
		JSlider unitYSlider = new JSlider(JSlider.HORIZONTAL, SL_UNIT_MIN, SL_UNIT_MAX, uy);
		
		private Graphic graph = new Graphic(frameWidth - 50, frameHeight - 75, ux, uy);
		
		private JPanel panel = new JPanel(new BorderLayout());	
		private JPanel buttonPanel = new JPanel();
		userDialog dialog = new userDialog(this, graph);
	
        ApplicationFrame()
        {
        	super("Ecuatii Neliniare");
        	 
			setSize(frameWidth, frameHeight);                             
			setDefaultCloseOperation(this.EXIT_ON_CLOSE); 
			
			panel.setOpaque(true);
			getContentPane().add(panel);                   
			addComponentListener(this);
			
			for (int i = 0; i < Methods.length; i++)
				ChooseMethod.addItem(Methods[i]);
			buttonPanel.add(ChooseMethod);
			
			buttonPanel.setOpaque(true);
			JButton buttonAdd = new JButton("Aplica...");
			buttonAdd.setPreferredSize(new Dimension(125, 25));
			buttonAdd.addActionListener(this);
			buttonPanel.add(buttonAdd);
			
	        unitXSlider.setMinorTickSpacing(5);
	        unitXSlider.setPaintTicks(true);
			unitXSlider.addChangeListener(this);
			unitYSlider.setMinorTickSpacing(5);
	        unitYSlider.setPaintTicks(true);
			unitYSlider.addChangeListener(this);
			buttonPanel.add(unitXSlider);
			buttonPanel.add(unitYSlider);

			Border padding = BorderFactory.createEmptyBorder(0,20,20,20);
			panel.setBorder(padding);			
			panel.add(buttonPanel, BorderLayout.NORTH);     
			panel.add(graph, BorderLayout.CENTER);  
			
			pack();
			  
			setVisible(true);
        }
		
		public int getSelectedMethod()
		{
			return ChooseMethod.getSelectedIndex();
		}
    
        public void actionPerformed(ActionEvent e) 
    	{
			dialog.showIt();
		}
   	
		public void componentHidden(ComponentEvent e) 
    	{ 
    	}
    	
    	public void componentShown(ComponentEvent e) 
    	{ 
		}
   	
		public void componentMoved(ComponentEvent e) 
    	{ 
    	}
    	
    	public void componentResized(ComponentEvent e) 
    	{
			Dimension dim = getSize();
			dim.height -= 75;
			dim.width -= 50;
    		graph.setDimensions(dim);
    	}   

		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider)e.getSource();

			if(source == unitXSlider)
			{
				ux = source.getValue();
				graph.setUnityX(ux);
			}
			else
			{
				uy = source.getValue();
				graph.setUnityY(uy);
			}
		}
}

public class EcuatiiNeliniare
{

	public static void main(String[] args)
	{
		new ApplicationFrame();
	}
}