import java.beans.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.Vector;

class Polynomial {
    private int[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)
	
	
	// a * x^b
    public Polynomial(int a, int b) {
        coef = new int[b+1];
        coef[b] = a;
        deg = degree();
    }
    
    public Polynomial(int[] pol)
    {
		coef = new int[pol.length];
		for(int i = 0; i < pol.length; i++)
			coef[i] = pol[i];
        deg = degree();
    } 
    

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != 0) d = i;
        return d;
    }

    // return c = a + b
    public Polynomial plus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] += b.coef[i];
        c.deg = c.degree();
        return c;
    }

    // return (a - b)
    public Polynomial minus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] -= b.coef[i];
        c.deg = c.degree();
        return c;
    }

    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double x) {
        double p = 0;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (x * p);
        return p;
    }

    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (deg == 0) return new Polynomial(0, 0);
        Polynomial deriv = new Polynomial(0, deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
        return deriv;  
    }					

    // convert to string representation
    public String toString() {
        if (deg ==  0) return "" + coef[0];
        if (deg ==  1) return coef[1] + "x + " + coef[0];
        String s = coef[deg] + "x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
            if      (coef[i] == 0) continue;
            else if (coef[i]  > 0) s = s + " + " + ( coef[i]);
            else if (coef[i]  < 0) s = s + " - " + (-coef[i]);
            if      (i == 1) s = s + "x";
            else if (i >  1) s = s + "x^" + i;
        }
        return s;
    }    

}

class Curve
{
	private Polynomial P1;
	private Polynomial P2;
	
	Curve(Polynomial p1, Polynomial p2)
	{
		P1 = p1;
        	P2 = p2;
        }
        
        public boolean isGC1(Curve anotherCurve)
        {
        	if(!( (P1.evaluate(0) == anotherCurve.P1.evaluate(0) && 
        		P2.evaluate(0) == anotherCurve.P2.evaluate(0)))) return false;
        	
        	Curve diffCurve = diffCurve(this);
        	Curve anotherDiffCurve = diffCurve(anotherCurve);
        	
        	int p1 = (int)diffCurve.P1.evaluate(0);
        	int p2 = (int)diffCurve.P2.evaluate(0);
        	int q1 = (int)anotherDiffCurve.P1.evaluate(0);
        	int q2 = (int)anotherDiffCurve.P2.evaluate(0);
        	
        	if(!(p1 != 0 && p2 != 0 && q1 != 0 && q2 != 0)) return false;
        	
        	if(!(p2 / p1 == q2 / q1)) return false;
        		
        	return true;
        }
        
        public Point getPoint(int value)
        {
        	return new Point((int)P1.evaluate(value), (int)P2.evaluate(value));
        }
        
    	private Curve diffCurve(Curve curve)
    	{
    		return new Curve(curve.P1.differentiate(), curve.P2.differentiate());
    	}
        
    	public void drawToGraphic(Graphics2D g, int x, int y, double EPSILON, int UNITYX, int UNITYY, int nrCrt)
	{
		int _x, _y;
		
		
		
		for(double i = 0;; i += EPSILON)
		{
			_x = (int)(P1.evaluate(i) * UNITYX) + x / 2;
			_y = -(int)(P2.evaluate(i) * UNITYY) + y / 2;
                	g.drawLine(_x, _y,
                	   (int)((P1.evaluate(i + EPSILON)) * UNITYX) + x / 2, 
                	   -(int)((P2.evaluate(i + EPSILON)) * UNITYY) + y / 2);
                	
                	if(_x < 0  || _x > x || _y < 0 || _y > y)
                	{
                		int __x = ( (_x < 0)? (_x + 10) : ((_x > x)? _x - 30 : _x) );
                		int __y = ( (_y < 0)? (_y + 15) : ((_y > y)? _y - 20 : _y) ); 
                		g.drawString("C" + nrCrt, __x, __y);
                		
                		break; 
                	}
               	}
               	
               	for(double i = 0;; i -= EPSILON)
		{
			_x = (int)(P1.evaluate(i) * UNITYX) + x / 2;
			_y = -(int)(P2.evaluate(i) * UNITYY) + y / 2;
                	g.drawLine(_x, _y,
                	   (int)((P1.evaluate(i + EPSILON)) * UNITYX) + x / 2, 
                	   -(int)((P2.evaluate(i + EPSILON)) * UNITYY) + y / 2);
                	
                	if(_x < 0  || _x > x || _y < 0 || _y > y) break;
               	}
               	
              
               	
                        
	}
	
	
        
}


class Graphic extends JComponent
{        
	private final static double EPSILON = 0.005;
     	private final static int UNITYX = 20;
     	private final static int UNITYY = 20;
     	private final static int DEFAULT_FRAME_WIDTH = 800;
     	private final static int DEFAULT_FRAME_HEIGHT = 600;
     	
     	private static Graphic instance;
     	
	private int frameWidth;
	private int frameHeight;
	private int unityX;
	private int unityY;
	
	private Color[] color = {Color.BLUE, Color.DARK_GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, 
				Color.ORANGE, Color.RED, Color.YELLOW};
	
	
	private Vector<Curve> Curves = new Vector<Curve>();
	private Vector<Point> Highlits = new Vector<Point>();
	
	
        Graphic(int width, int height)
        {   
        	
        	
        	frameWidth = width;
        	frameHeight = height;
        	
        	unityX = (int)((double)UNITYX * ((double)frameWidth / (double)DEFAULT_FRAME_WIDTH));
		unityY = (int)((double)UNITYY * ((double)frameHeight / (double)DEFAULT_FRAME_HEIGHT));
        	
        	this.setOpaque(true);
        	this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        	this.setBorder(BorderFactory.createBevelBorder(1));
        	
        	instance = this;
	}
	
	public void setDimensions(Dimension dim)
	{
		unityX = (int)((double)UNITYX * ((double)dim.width / (double)DEFAULT_FRAME_WIDTH));
		unityY = (int)((double)UNITYY * ((double)dim.height / (double)DEFAULT_FRAME_HEIGHT));
		
		frameWidth = dim.width;
		frameHeight = dim.height;
		
		setSize(frameWidth, frameHeight);
	}
	
	public void addCurveToGraphic(Curve curve)
	{
		Curves.add(curve);
		ceckGC1();
		this.repaint();
	}
	
	public void clearGraphic()
	{
		Curves.clear();
		Highlits.clear();
		this.repaint();
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
                
                
                for(int curveIndex = 0; curveIndex < Curves.size(); curveIndex++)
                {
                	g2d.setColor(color[curveIndex % color.length]);
                	Curves.elementAt(curveIndex).drawToGraphic(g2d, frameWidth, frameHeight, EPSILON, unityX, unityY, curveIndex + 1);
            	}
            	
            	
            	g2d.setColor(Color.red);
            	float dash[] = {3.0f};
         	g2d.setStroke(new BasicStroke(1.0f,
             		BasicStroke.CAP_BUTT,
             		BasicStroke.JOIN_MITER,
             		3.0f, dash, 0.0f));
             		
             		
            	for(int pnt = 0; pnt < Highlits.size(); pnt++)
            	{
            		Point crd = Highlits.elementAt(pnt);
            		g2d.drawLine(frameWidth / 2 + (crd.x * unityX), frameHeight / 2, frameWidth / 2 + (crd.x * unityX), frameHeight / 2 - (crd.y * unityY));
            		g2d.drawLine(frameWidth / 2, frameHeight / 2 - (crd.y * unityY), frameWidth / 2 + (crd.x * unityX), frameHeight / 2 - (crd.y * unityY));
            		
            		g2d.fillOval(frameWidth / 2 + (crd.x * unityX) - 3, frameHeight / 2 - (crd.y * unityY) - 3, 6, 6);
            		
            		g2d.drawString( "" + crd.x, frameWidth / 2 + (crd.x * unityX), frameHeight / 2 + 12);
            		g2d.drawString( "" + crd.y, frameWidth / 2 - 12, frameHeight / 2 - (crd.y * unityY));
            	}
            	
            	g2d.dispose();
	}
	
	
	public void ceckGC1()
	{
		if(Curves.size() < 2) return;
		
		
		for(int i = 0; i < Curves.size() - 1; i++)
			if(Curves.elementAt(Curves.size() - 1).isGC1(Curves.elementAt(i)))
			{
				final int j = i;
				final Point p = Curves.elementAt(Curves.size() - 1).getPoint(0);
				Highlits.add(p);
				
				(new Thread()
				{
					public void run()
					{
						JOptionPane.showMessageDialog(
                                    			instance,
                                    			"The new curve and curve C" + (j+1) + 
                                    			"\nhave GC1 property in point (" + p.x + ", " + p.y + ") !",
                                    			null,
                                    			JOptionPane.INFORMATION_MESSAGE);
                                    	}
                               }).start();
			}
				
			
	}
            
}

class MyIntArray
{
	private int[] vector;
	private int capacity;
	public int length;
	
	MyIntArray(int cap)
	{
		vector = new int[cap];
		capacity = cap;
		length = 0;
		
		for(int i = 0; i < capacity; i++) vector[i] = 0;
	}
	
	public void add(int value, int index)
	{
		if(index < 0) return;
		else
		{
			if(index < capacity)
			{
				vector[index] += value;
				if(length <= index) length = index + 1;
			}
			
			else
			{
				while (index >= capacity) capacity *= 2;
				int[] newVector = new int[capacity];
				for(int i = 0; i < capacity; i++) vector[i] = 0;
				
				for(int i = 0; i < length; i++)
				{
					newVector[i] = vector[i];
				}
				
				vector = newVector;
				
				vector[index] += value;
				length = index + 1;
			}
		}
	}
	
	public int[] toIntArray()
	{
		int[] v = new int[length];
		for(int i = 0; i < length; i++)
			v[i] = vector[i];
		return v;
	}
	
	public String toString()
	{
		String str = new String();
		for(int i = 0; i < length; i++)
			str += (vector[i] + " ");
		
		return str;
	}
}

class Converter
{
	public static Curve stringsToCurve(String str1, String str2)
	{
		
		
		try
		{
			Integer.parseInt(str1);			//this is very nasty --- do not! ever do this
			Integer.parseInt(str2);			
			return null;
		}
		catch(NumberFormatException ex)
		{
		}
		
		MyIntArray poly1 = new MyIntArray(10);
		MyIntArray poly2 = new MyIntArray(10);
		
		
		int beginIndex;
		int endIndex;
		int sign;
		int sgn;

		try
		{
			
		beginIndex = 0;
		endIndex = 0;
			
		switch(str1.charAt(0))
		{
			case '+':
				beginIndex ++;
				sgn = 1;
			break;
			
			case '-':
				beginIndex ++;
				sgn = -1;
			break;
			
			default:
				sgn = 1;
			break;
		}
		while(endIndex != -1)
		{
			sign = sgn;
			sgn = 1;
			int indexPlus = str1.indexOf('+', beginIndex);
			int indexMinus = str1.indexOf('-', beginIndex);
			
			if(indexPlus < indexMinus)
			{			
				if(indexPlus != -1) endIndex = indexPlus;
				else
				{
					endIndex = indexMinus;
					sgn = -1;
				}
			}
			
			else
			{
				if(indexMinus != -1)
				{
					endIndex = indexMinus;
					sgn = -1;
				}
				else endIndex = indexPlus;
			}
			
			String toParse = str1.substring(beginIndex, (endIndex == -1)? str1.length() : endIndex);
			
			int value = 0;
			int index = 0;
			
			try
			{
				value = sign * Integer.parseInt(toParse);
			}
			catch(NumberFormatException ex1)
			{
				try
				{
					
					if(toParse.indexOf('x') == 0) value = 1;
					else value = sign * Integer.parseInt(toParse.substring(0, toParse.indexOf('x')));
					
					if(toParse.indexOf('^') == -1) index = 1;
					else index = Integer.parseInt(toParse.substring(toParse.indexOf('^') + 1, toParse.length()));
					
					
				}
				catch(NumberFormatException ex2)
				{
					return null;
				}
			}
			
			
			poly1.add(value, index);
			
			beginIndex = endIndex + 1;
		}
		
		
		beginIndex = 0;
		endIndex = 0;
			
		switch(str2.charAt(0))
		{
			case '+':
				beginIndex ++;
				sgn = 1;
			break;
			
			case '-':
				beginIndex ++;
				sgn = -1;
			break;
			
			default:
				sgn = 1;
			break;
		}
		while(endIndex != -1)
		{
			sign = sgn;
			sgn = 1;
			int indexPlus = str2.indexOf('+', beginIndex);
			int indexMinus = str2.indexOf('-', beginIndex);
			
			if(indexPlus < indexMinus)
			{			
				if(indexPlus != -1) endIndex = indexPlus;
				else
				{
					endIndex = indexMinus;
					sgn = -1;
				}
			}
			
			else
			{
				if(indexMinus != -1)
				{
					endIndex = indexMinus;
					sgn = -1;
				}
				else endIndex = indexPlus;
			}
			
			String toParse = str2.substring(beginIndex, (endIndex == -1)? str2.length() : endIndex);
			
			int value = 0;
			int index = 0;
			
			try
			{
				value = sign * Integer.parseInt(toParse);
			}
			catch(NumberFormatException ex1)
			{
				try
				{
					if(toParse.indexOf('x') == 0) value = 1;
					else value = sign * Integer.parseInt(toParse.substring(0, toParse.indexOf('x')));
					
					if(toParse.indexOf('^') == -1) index = 1;
					else index = Integer.parseInt(toParse.substring(toParse.indexOf('^') + 1, toParse.length()));
				}
				catch(NumberFormatException ex2)
				{
					return null;
				}
			}
			
			poly2.add(value, index);
			
			beginIndex = endIndex + 1;
		}
		
		}
		catch(Exception exc)
		{
			return null;
		}
		
		//System.out.println(poly1);	
		//System.out.println(poly2);		
		
		return new Curve(new Polynomial(poly1.toIntArray()), new Polynomial(poly2.toIntArray()));
	}
	
}

class userDialog extends JDialog
			implements PropertyChangeListener
{
	
	private JOptionPane optionPane;
	private JTextField poly1;
	private JTextField poly2;
	
	private String button1 = "Ok";
	private String button2 = "Cancel";
	
	private String text1 = "Enter first polynomial:";
	private String text2 = "Enter second polynomial:";
	
	private JFrame parentFrame;
	private Graphic graphic;
	
	userDialog(JFrame frame, Graphic gr)
	{
		super(frame, "Enter Polynomials!", true);
		parentFrame = frame;
		graphic = gr;
		
		poly1 = new JTextField(15);
		poly2 = new JTextField(15);
		
		Object[] optionPaneComponent = { text1, poly1, text2, poly2 };
		Object[] optionPaneButtons = { button1, button2 }; 
		
		optionPane = new JOptionPane(optionPaneComponent,
					JOptionPane.QUESTION_MESSAGE,
                                    	JOptionPane.YES_NO_OPTION,
                                    	null,optionPaneButtons,
                                    	optionPaneButtons[0]);
                                    	
                setContentPane(optionPane);
                pack();
                
                
                addComponentListener(new ComponentAdapter() 
                {
            		public void componentShown(ComponentEvent ce)
            		{
                		poly1.requestFocusInWindow();
            		}
        	});
        	
        	//poly1.addActionListener(this);
        	//poly2.addActionListener(this);
		
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
      				Curve newCurve = Converter.stringsToCurve(poly1.getText(), poly2.getText());
      				if(newCurve != null)
      				{
      					hideIt();
      					graphic.addCurveToGraphic(newCurve);					
      				}
      				
      				else
      				{
      					JOptionPane.showMessageDialog(
                                    			this,
                                    			"Invalid entry!",
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
		poly1.setText(null);
		poly2.setText(null);
		setLocationRelativeTo(parentFrame);
        	setVisible(true);
	}
	
	public void hideIt()
	{
		setVisible(false);
	}
}

class ApplicationFrame extends JFrame 
		       implements ActionListener, ComponentListener
{

    	private int frameWidth = 700;
    	private int frameHeight = 725;
	private Graphic graph = new Graphic(frameWidth - 50, frameHeight - 75);
	private JPanel panel = new JPanel(new BorderLayout());	
	private JPanel buttonPanel = new JPanel();
	userDialog dialog = new userDialog(this, graph);
	
        ApplicationFrame()
        {
        	super("Bezier Curve Application");
        	 
		setSize(frameWidth, frameHeight);                             
		setDefaultCloseOperation(this.EXIT_ON_CLOSE); 
		
		panel.setOpaque(true);
		getContentPane().add(panel);                   
		addComponentListener(this);
		
		buttonPanel.setOpaque(true);
		JButton buttonAdd = new JButton("Add Curve");
		buttonAdd.setPreferredSize(new Dimension(125, 25));
		buttonAdd.addActionListener(this);
		buttonPanel.add(buttonAdd);
	
		JButton buttonClear = new JButton("Clear Graphic");
		buttonClear.setPreferredSize(new Dimension(125, 25));
		buttonClear.addActionListener(this);
		buttonPanel.add(buttonClear);
		
		
		Border padding = BorderFactory.createEmptyBorder(0,20,20,20);
		panel.setBorder(padding);			
		panel.add(buttonPanel, BorderLayout.NORTH);     
		panel.add(graph, BorderLayout.CENTER);  
		
		pack();
		  
		setVisible(true);
        }
    
        public void actionPerformed(ActionEvent e) 
    	{
    	//	        		graph.addCurveToGraphic(new Curve(new Polynomial(v1), new Polynomial(v2)));
        	if(e.getActionCommand().equals("Add Curve"))
        	{
        		dialog.showIt();
        	}
        	
        	if(e.getActionCommand().equals("Clear Graphic"))
        	{
			graph.clearGraphic();
        	}
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
       
}

public class Bezier
{

	public static void main(String[] args)
	{
		new ApplicationFrame();
	}
}