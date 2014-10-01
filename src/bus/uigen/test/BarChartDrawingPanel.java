package bus.uigen.test;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bus.uigen.attributes.AttributeNames;
import util.awt.ADelegateFrame;
import util.awt.DelegateFramePainter;

public class BarChartDrawingPanel extends JPanel implements MouseListener, PropertyChangeListener  {
	
	protected int x = 100, y = 100;
	public static int X_OFFSET = 15;
	public static int Y_OFFSET = 0;
	public static int WIDTH = 5;
	public static int PIXELS_PER_UNIT = 4;
	protected char lastChar = ' ';
	SquaringCounterWithButtons counter;
	public BarChartDrawingPanel(SquaringCounterWithButtons aCounter) {
		setBackground(AttributeNames.CAROLINA_BLUE);
		counter = aCounter;
		addMouseListener(this);
		counter.addPropertyChangeListener(this);
	}	
	// called when an enqueued paint event for this component is dequeued
	public void paint (Graphics g) {
		super.paint(g); // clears the window
		// better to use FontMetrics to center circle
		
		g.fillRect(x, Y_OFFSET, WIDTH, counter.getNumber()*PIXELS_PER_UNIT);
		g.fillRect(x + X_OFFSET, Y_OFFSET, WIDTH, counter.getSquare()*PIXELS_PER_UNIT);

		
	}
	
	
	public void mousePressed(MouseEvent event) {
		x = event.getX();
		y = event.getY();
		repaint(); // enqueues a paint event	
	}
	
	public void mouseEntered(MouseEvent event) {	}	
	public void mouseExited(MouseEvent event) {}	
	public void mouseReleased(MouseEvent event) {}	
	public void keyPressed(KeyEvent event) {}	
	public void keyReleased(KeyEvent event) {} 	
	public void mouseClicked(MouseEvent event) {}
	
	
	public static void main (String[] args) {
		JFrame frame = new JFrame();
		BarChartDrawingPanel aDrawer = new BarChartDrawingPanel(new SquaringCounterWithButtons());
		frame.setContentPane(aDrawer);
		frame.setSize(300, 200);
		frame.setVisible(true);
		
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
		
	}
}
