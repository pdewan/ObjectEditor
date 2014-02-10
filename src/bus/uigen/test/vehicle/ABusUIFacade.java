package bus.uigen.test.vehicle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ABusUIFacade {
	
	int startX = 0;
	int startY = 0;
	public static final int DRAW_FRAME_WIDTH = 750;
	public static final int DRAW_FRAME_HEIGHT = 400;
	public static final int TREE_FRAME_WIDTH = 400;
	public static final int TREE_FRAME_HEIGHT = 200;
	public static final int TEXT_FRAME_WIDTH = 750;
	public static final int TEXT_FRAME_HEIGHT = 100;
    JFrame drawFrame = new JFrame();
	JMenuBar menuBar = new JMenuBar();
	JMenuItem moveRightMenuItem = new JMenuItem("Move Right");
	JMenuItem moveLeftMenuItem = new JMenuItem("Move Left");
	JMenuItem magnifyMenuItem = new JMenuItem("Magnify");
	JMenuItem shrinkMenuItem = new JMenuItem("Shrink");
	
	JButton moveRightButton = new JButton("Move Right");
	JButton moveLeftButton = new JButton("Move Left");
	JButton magnifyButton = new JButton("Magnify");
	JButton shrinkButton = new JButton("Shrink");
	
	Container mainPanel = new JPanel();
	
	 JCheckBox doubleDeckerCheckBox = new JCheckBox("Double Decker");
	
	 JFrame textFrame = new JFrame();

	
	 JTextArea textDisplay = new JTextArea();
	
	 JTextField tireJTextField = new JTextField();
	 JTextField deckJTextField = new JTextField();
	
	 JLabel tireLabel = new JLabel("Tires:");
	 JLabel deckLabel = new JLabel("Decks:");
	
	 JPanel tirePanel = new JPanel();
	 JPanel deckPanel = new JPanel();
	
	 JLabel textLabel = new JLabel("Bus Text:");
	 JTextField textTextField = new JTextField();
	 JPanel textPanel = new JPanel();

	
//	static JFrame treeFrame = new JFrame();
	
//	static JTree jTree = new JTree();

//	static JButton magnify = new JButton("Magnify");
//	static JButton compress = new JButton("Compress");
	

	
	 JMenu operationsMenu =  new JMenu("Scaling");	
	 
	 public ABusUIFacade(int anX, int aY) {
		 startX = anX;
		 startY = aY;
		 drawFrame.setLocation(startX, startY);
		 
	 }
	
	 void  createDrawDisplay() {
		drawFrame.setLayout(new BorderLayout());
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new BoxLayout(buttonContainer,BoxLayout.LINE_AXIS));
		drawFrame.add(buttonContainer, BorderLayout.NORTH);
		drawFrame.setJMenuBar(menuBar);
		drawFrame.getJMenuBar().add(operationsMenu);
//		operationsMenu.add(moveRightMenuItem);
//		operationsMenu.add(moveLeftMenuItem);
//		buttonContainer.add(magnifyButton);
//		buttonContainer.add(shrinkButton);
		operationsMenu.add(magnifyMenuItem);
		operationsMenu.add(shrinkMenuItem);
		buttonContainer.add(moveLeftButton);
		buttonContainer.add(moveRightButton);
		buttonContainer.add(doubleDeckerCheckBox);
		drawFrame.setSize(DRAW_FRAME_WIDTH, DRAW_FRAME_HEIGHT);
//		drawFrame.add(textDisplay, BorderLayout.SOUTH);
		mainPanel.setBackground(Color.WHITE);
		drawFrame.add(mainPanel);
//		drawFrame.setTitle("Bus Graphics");
		drawFrame.setVisible(true);
	}
	 void  createTextDisplay() {
//		textFrame.setLayout(new BorderLayout());
		textFrame.setLocation(drawFrame.getX(), drawFrame.getHeight() + drawFrame.getY());
		textFrame.add(textPanel);
		textPanel.setLayout(new BorderLayout());
		textPanel.add(textLabel, BorderLayout.WEST);
		textPanel.add(textTextField);
//		deckPanel.add(deckLabel);
//		deckPanel.add(deckJTextField);
//		tirePanel.add(tireLabel);
//		tirePanel.add(tireJTextField);
//		textFrame.setTitle("Bus Text");
//		textFrame.add(textDisplay);
//		drawFrame.setLayout(new BorderLayout());
//		JPanel buttonContainer = new JPanel();
//		drawFrame.add(buttonContainer, BorderLayout.NORTH);
//		drawFrame.setJMenuBar(menuBar);
//		drawFrame.getJMenuBar().add(operationsMenu);
////		operationsMenu.add(moveRightMenuItem);
////		operationsMenu.add(moveLeftMenuItem);
////		buttonContainer.add(magnifyButton);
////		buttonContainer.add(shrinkButton);
//		operationsMenu.add(magnifyMenuItem);
//		operationsMenu.add(shrinkMenuItem);
//		buttonContainer.add(moveLeftButton);
//		buttonContainer.add(moveRightButton);
//		buttonContainer.add(doubleDeckerCheckBox);
//		drawFrame.setSize(DRAW_FRAME_WIDTH, DRAW_FRAME_HEIGHT);
////		drawFrame.add(textDisplay, BorderLayout.SOUTH);
//		mainPanel.setBackground(Color.WHITE);
//		drawFrame.add(mainPanel);
		textFrame.setSize(TEXT_FRAME_WIDTH, TEXT_FRAME_HEIGHT);
		textFrame.setVisible(true);
	}
	
//	static void createTreeDisplay() {
//		treeFrame.setLayout(new BorderLayout());
//		treeFrame.add(jTree);
//		treeFrame.setSize(TREE_FRAME_HEIGHT, TREE_FRAME_WIDTH);
//		treeFrame.setVisible(true);
//	}
	
//	static void  createDisplayMenuBar() {
//		drawFrame.add(mainPanel);
//		drawFrame.setJMenuBar(menuBar);
//		drawFrame.getJMenuBar().add(operationsMenu);
//		operationsMenu.add(moveRightMenuItem);
//		operationsMenu.add(moveLeftMenuItem);
//		operationsMenu.add(magnifyMenuItem);
//		operationsMenu.add(shrinkMenuItem);
//		drawFrame.setSize(DRAW_FRAME_WIDTH, DRAW_FRAME_HEIGHT);
//		drawFrame.setVisible(true);
//	}
//	static void  createDisplayButton() {
//		drawFrame.setLayout(new BorderLayout());
//		drawFrame.add(mainPanel);
//		JPanel buttonContainer = new JPanel();
//		drawFrame.add(buttonContainer, BorderLayout.NORTH);
////		frame.setJMenuBar(menuBar);
////		frame.getJMenuBar().add(operationsMenu);
////		buttonContainer.setLayout(new GridLayout(4, 1));
//		buttonContainer.add(doubleDeckerCheckBox);
//		buttonContainer.add(moveRightButton);
//		buttonContainer.add(moveLeftButton);
//		buttonContainer.add(magnifyButton);
//		buttonContainer.add(shrinkButton);
//		drawFrame.setSize(DRAW_FRAME_WIDTH, DRAW_FRAME_HEIGHT);
//		drawFrame.setVisible(true);
//	}
	
	public static void main (String[] args) {
		ABusUIFacade facade = new ABusUIFacade(0, 0);		
		facade.createDrawDisplay();
		facade.createTextDisplay();
		ABusUIFacade busOEUI2 = new ABusUIFacade(700, 0);	
		busOEUI2.createDrawDisplay();
		busOEUI2.createTextDisplay();
//		createTreeDisplay();
	}

}
