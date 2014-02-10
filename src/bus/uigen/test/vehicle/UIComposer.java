package bus.uigen.test.vehicle;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class UIComposer {
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 400;
	static JFrame frame = new JFrame();
	static JMenuBar menuBar = new JMenuBar();
	static JMenuItem moveRightMenuItem = new JMenuItem("Move Right");
	static JMenuItem moveLeftMenuItem = new JMenuItem("Move Left");
	static JMenuItem magnifyMenuItem = new JMenuItem("Magnify");
	static JMenuItem shrinkMenuItem = new JMenuItem("Shrink");
	
	static JButton moveRightButton = new JButton("Move Right");
	static JButton moveLeftButton = new JButton("Move Left");
	static JButton magnifyButton = new JButton("Magnify");
	static JButton shrinkButton = new JButton("Shrink");
	
	static Container mainPanel = new JPanel();
	
	static JCheckBox doubleDeckerCheckBox = new JCheckBox("Double Decker");
//	static JButton magnify = new JButton("Magnify");
//	static JButton compress = new JButton("Compress");
	

	
	static JMenu operationsMenu =  new JMenu("Scaling");	
	
	static void  createDisplay() {
		frame.setLayout(new BorderLayout());
		JPanel buttonContainer = new JPanel();
		frame.add(buttonContainer, BorderLayout.NORTH);
		frame.setJMenuBar(menuBar);
		frame.getJMenuBar().add(operationsMenu);
//		operationsMenu.add(moveRightMenuItem);
//		operationsMenu.add(moveLeftMenuItem);
//		buttonContainer.add(magnifyButton);
//		buttonContainer.add(shrinkButton);
		operationsMenu.add(magnifyMenuItem);
		operationsMenu.add(shrinkMenuItem);
		buttonContainer.add(moveLeftButton);
		buttonContainer.add(moveRightButton);
		buttonContainer.add(doubleDeckerCheckBox);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
	}
	
	static void  createDisplayMenuBar() {
		frame.add(mainPanel);
		frame.setJMenuBar(menuBar);
		frame.getJMenuBar().add(operationsMenu);
		operationsMenu.add(moveRightMenuItem);
		operationsMenu.add(moveLeftMenuItem);
		operationsMenu.add(magnifyMenuItem);
		operationsMenu.add(shrinkMenuItem);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
	}
	static void  createDisplayButton() {
		frame.setLayout(new BorderLayout());
		frame.add(mainPanel);
		JPanel buttonContainer = new JPanel();
		frame.add(buttonContainer, BorderLayout.NORTH);
//		frame.setJMenuBar(menuBar);
//		frame.getJMenuBar().add(operationsMenu);
//		buttonContainer.setLayout(new GridLayout(4, 1));
		buttonContainer.add(doubleDeckerCheckBox);
		buttonContainer.add(moveRightButton);
		buttonContainer.add(moveLeftButton);
		buttonContainer.add(magnifyButton);
		buttonContainer.add(shrinkButton);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
	}

}
