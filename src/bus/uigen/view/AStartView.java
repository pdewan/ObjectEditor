package bus.uigen.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import slm.SLModel;
import util.misc.ThreadSupport;
import util.trace.TraceableBus;
import util.trace.uigen.DrawingEditorGenerationEnded;
import util.trace.uigen.EditorGenerationEnded;
import util.trace.uigen.EditorGenerationStarted;
import util.trace.uigen.FrameSetVisibleEnded;
import util.trace.uigen.MajorStepInfo;
import util.trace.uigen.TreeEditorGenerationEnded;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AboutManager;
import bus.uigen.controller.models.BuildTimeManager;
import bus.uigen.models.ComputerDefinition;
import bus.uigen.models.ComputerDefinitionsGenerator;
import bus.uigen.models.ComputerDefinitionsManager;
import bus.uigen.models.LogoReader;
import bus.uigen.widgets.swing.DelegateJPanel;


public class AStartView  implements StartView{
//	public static final int START_VIEW_WIDTH = 500;
//	public static final int START_VIEW_HEIGHT = 230;
	public static final int START_VIEW_WIDTH = 470;
	public static final int START_VIEW_HEIGHT = 240;
	public static final int DEFAULT_SLEEP_INTERVAL = 1000;
	public static final int IDLE_TIME = 1000;
	public static final String DEFINITIONS_BEHAVIOR_MESSAGE = "Click to see another randomly selected definition";
	
	public static final int LOGO_X =290;
	public static final int LOGO_Y = 5;
	public static final int LOGO_HEIGHT = 75;
	public static final int LOGO_WIDTH = 150;

	long lastEventTime;
	boolean automaticallyIconified = false;
	boolean manuallyDeIconified = false;
	boolean manuallyIconified = false;
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int START_VIEW_X = (screenSize.width - START_VIEW_WIDTH)/2;
	public static final int START_VIEW_Y = (screenSize.height - START_VIEW_HEIGHT)/2;
	int remainingEditors; // make use of the fact that main editor generation does not end until drawing editor ends
	static Color LIGHT_GREEN = new Color (230, 238, 213);
	static Color DARK_GREEN = new Color (118, 146, 60);
	static Color BLUISH_GREEN = new Color (0, 153, 102);
	static Color GREEN_BLUE_RED = new Color (51, 204, 51);
	static Color GREENISH_BLUE = new Color (0, 102, 102);
	static Color GOOGLE_BLUE = new Color (97, 136, 245);
	static String OBJECT_EDITOR = "ObjectEditor";
//	static int OBJECT_EDITOR_HEIGHT = 70;
	static int OBJECT_EDITOR_HEIGHT = 75;
	static int STATUS_HEIGHT = 70;
	

	static Color MIDDLE_BLUE = new Color (167, 191, 222);
	static Color DARK_BLUE = new Color (79, 129, 189);
	static Color BLACK_GREEN = new Color (0, 102, 0);
	static final Color ABOUT_BACKGROUND = AttributeNames.CAROLINA_BLUE;
	static final Color ABOUT_FOREGROUND = Color.WHITE;
//	static final Color STATUS_COLOR = AttributeNames.LIGHT_BLUE;
//	static final Color STATUS_BACKGROUND = AttributeNames.CAROLINA_BLUE;
	static final Color STATUS_BACKGROUND = AttributeNames.LIGHT_BLUE;

//	static final Color STATUS_FOREGROUND = Color.WHITE;

//	static final Color PROGRESS_COLOR = BLUISH_GREEN;
	static final Color PROGRESS_COLOR = AttributeNames.CAROLINA_BLUE;
	static final Color STATUS_FOREGROUND = Color.DARK_GRAY;
	
	


//	static final Color DEFINITION_COLOR = DARK_BLUE;
	static final Color DEFINITION_BACKGROUND = GOOGLE_BLUE;
//	static final Color DEFINITION_BACKGROUND = DARK_BLUE;

//	static final Color DEFINITION_BACKGROUND = AttributeNames.CAROLINA_BLUE;
	static final Color DEFINITION_FOREGROUND = Color.WHITE;
	static final Color BACKGROUND_COLOR = Color.WHITE;
	static final int BORDER_OFFSET = 2;
	static final int LINE_X_OFFSET = 5;
	static final int FIRST_LINE_Y_OFFSET = 0;
	static final int OBJECT_EDITOR_Y =BORDER_OFFSET;
	static final int STATUS_Y = OBJECT_EDITOR_HEIGHT + OBJECT_EDITOR_Y + BORDER_OFFSET;
	static final int DEFINITION_Y = STATUS_HEIGHT + STATUS_Y + BORDER_OFFSET;
	String lastTraceableMessage = "";
	static Font DEFAULT_LARGE_FONT = new Font (Font.SANS_SERIF, Font.PLAIN, 15);
	static Image logoImage;

	static Font largeFont = DEFAULT_LARGE_FONT;
	static Font DEFAULT_MEDIUM_FONT = new Font (Font.SANS_SERIF, Font.PLAIN, 12);
	static Font mediumFont = DEFAULT_MEDIUM_FONT;
	static Font DEFAULT_SMALL_FONT = new Font (Font.SANS_SERIF, Font.PLAIN, 10);
	static Font smallFont = new Font (Font.SANS_SERIF, Font.PLAIN, 10);
	static int largeHeight;
	static int mediumHeight;
	static int smallHeight;
	 FontMetrics largeFontMetrics;
	 FontMetrics mediumFontMetrics;
	 FontMetrics smallFontMetrics;
	static String OBJECT_EDITOR_NAME = "ObjectEditor";
	static String version = AboutManager.getObjectEditorDescription().getVersion();
//	static String objectEditorBuildTime = BuildTimeManager.getBuildTimeInTextFile();
	static String versionDetails = "Version " + AboutManager.getObjectEditorDescription().getVersion() + " Built on "+ BuildTimeManager.getBuildTimeInTextFile();
//	Font statusFont = new Font (Font.SANS_SERIF, Font.PLAIN, 12);
	 String majorStepMessage = "";
	 String statusMessage = "";
	 Object mainHeader = null;
	 String mainHeaderToString;
	 String aboutMessage = AboutManager.getAbout();
	 int numSeconds;
	 boolean completed;
	 boolean stopUpdating;
	 EditorGenerationStarted currentGenerationStarted;
	 EditorGenerationEnded currentGenerationEnded;
	 static int sleepInterval = DEFAULT_SLEEP_INTERVAL;
	 ComputerDefinition computerDefinition;
	 JFrame frame = new JFrame();
	 DelegateJPanel drawComponent = new DelegateJPanel();
	 Thread timeThread;
	 
	public  AStartView () {	
		timeThread = new Thread(this);
		timeThread.start();
		drawComponent.setBackground(BACKGROUND_COLOR);
		drawComponent.addPainter(this);
		drawComponent.addMouseListener(this);
		drawComponent.addKeyListener(this);
		TraceableBus.addTraceableListener(this);
		frame.setContentPane(drawComponent);
		frame.setLocation(START_VIEW_X, START_VIEW_Y);
		frame.setSize(new Dimension(START_VIEW_WIDTH, START_VIEW_HEIGHT));
		frame.setState(frame.ICONIFIED);
		try {
			ComputerDefinitionsManager.readComputerDefinitions();
		 computerDefinition = ComputerDefinitionsManager.getRandomDefinition();
		} catch (Exception e) {
			computerDefinition = null;
//			Tracer.error("Could not find definitions file");
		}
		
		try {
			logoImage = LogoReader.openLogoFile();
		} catch (Exception e) {
			logoImage = null;
//			Tracer.error("Could not find definitions file");
		}

//		try {
//		 objectEditorBuildTime = BuildTimeManager.getBuildTimeInJavaFile();
//		} catch (Exception e)
//		
		frame.setVisible(true);
	}

	@Override
	public void paint(Object g) {
		
//		super.paint(g);
//		System.out.println("Start Paint start view");
		Graphics2D graphics = (Graphics2D) g;
		
		
		largeFontMetrics = graphics.getFontMetrics(largeFont);
		mediumFontMetrics = graphics.getFontMetrics(mediumFont);
		smallFontMetrics = graphics.getFontMetrics(smallFont);	
		largeHeight = largeFontMetrics.getHeight();
		mediumHeight = mediumFontMetrics.getHeight();
		smallHeight = smallFontMetrics.getHeight();	
		int firstLineYBase = largeHeight + FIRST_LINE_Y_OFFSET;
		int secondLineYBase = firstLineYBase + mediumHeight;
		int thirdLineYBase = secondLineYBase + smallHeight;
		int fourthLineYBase = thirdLineYBase + smallHeight;		
		graphics.setColor(ABOUT_BACKGROUND);
		graphics.fillRect(BORDER_OFFSET,BORDER_OFFSET, drawComponent.getWidth() -BORDER_OFFSET*2, OBJECT_EDITOR_HEIGHT);
		graphics.setColor(ABOUT_FOREGROUND);
		graphics.setFont(largeFont);
		graphics.drawString(OBJECT_EDITOR_NAME, LINE_X_OFFSET, OBJECT_EDITOR_Y + firstLineYBase);	
		graphics.setFont(mediumFont);
//		graphics.drawString("(" + objectEditorVersion + ": " + objectEditorBuildTime + ")", 90, 20);
		graphics.drawString(versionDetails, LINE_X_OFFSET, OBJECT_EDITOR_Y + secondLineYBase );	
		graphics.setFont(smallFont);
		graphics.drawString(AboutManager.getObjectEditorDescription().getCopyRight(), LINE_X_OFFSET, OBJECT_EDITOR_Y + thirdLineYBase);
		graphics.drawString(AboutManager.getObjectEditorDescription().getPatent(), LINE_X_OFFSET, OBJECT_EDITOR_Y + fourthLineYBase);
		graphics.setColor(STATUS_BACKGROUND);
		graphics.fillRect(BORDER_OFFSET, STATUS_Y, drawComponent.getWidth() -BORDER_OFFSET*2, STATUS_HEIGHT);
//		graphics.setColor(Color.DARK_GRAY);
		graphics.setColor(STATUS_FOREGROUND);
//		graphics.drawLine(0, 32, drawComponent.getWidth(), 33);
		graphics.setFont(largeFont);
//		System.out.println("Drawstring edited object:" + editedObject);
//
//		System.out.println("Drawstring edited object toSTring:" + editedObject.toString());
//		System.out.println("Drawstring firstLine Base:" + firstLineYBase);

//		if (mainHeader != null)
//		graphics.drawString( mainHeader.toString(), LINE_X_OFFSET, STATUS_Y +  firstLineYBase);
//		System.out.println(" Painting main header to string ");

		if (mainHeaderToString != null)
			graphics.drawString( mainHeaderToString, LINE_X_OFFSET, STATUS_Y +  firstLineYBase); // this will deadlock
//			graphics.drawString( mainHeader.toString(), LINE_X_OFFSET, STATUS_Y +  firstLineYBase);


//		graphics.drawString( "" + editedObject + "(" + editorGenerationMessage + ")", 5, 80);
		graphics.setFont(mediumFont);
		graphics.drawString( majorStepMessage, LINE_X_OFFSET, STATUS_Y +  secondLineYBase);
		graphics.setFont(smallFont);		
		graphics.drawString(statusMessage, LINE_X_OFFSET, STATUS_Y + thirdLineYBase);
		String timeString = "(" + numSeconds +"s)";
		graphics.drawString( timeString, LINE_X_OFFSET, STATUS_Y + fourthLineYBase);
		int timeStringLength = smallFontMetrics.stringWidth(timeString);
		graphics.setColor(PROGRESS_COLOR);
		graphics.fillRect(LINE_X_OFFSET + timeStringLength + 3, STATUS_Y +  fourthLineYBase -10, numSeconds*5, 12);
		graphics.setColor(DEFINITION_BACKGROUND);
		graphics.fillRect(BORDER_OFFSET, DEFINITION_Y, drawComponent.getWidth()-BORDER_OFFSET*2, drawComponent.getHeight()- DEFINITION_Y - BORDER_OFFSET);	
		graphics.setFont(largeFont);
		graphics.setColor(DEFINITION_FOREGROUND);
		if (computerDefinition != null) {
		graphics.drawString(computerDefinition.getWord(), LINE_X_OFFSET, DEFINITION_Y + firstLineYBase);
		graphics.setFont(mediumFont);
		graphics.drawString(computerDefinition.getMeaning(), LINE_X_OFFSET, DEFINITION_Y + secondLineYBase);
		graphics.setFont(smallFont);
		graphics.drawString(DEFINITIONS_BEHAVIOR_MESSAGE,  LINE_X_OFFSET, DEFINITION_Y + thirdLineYBase);
		} else {
			graphics.drawString("Missing definition file:" + ComputerDefinitionsGenerator.DICTIONARY_FILE, LINE_X_OFFSET, DEFINITION_Y + firstLineYBase);

		}
		
		if (logoImage != null) {
			graphics.drawImage(logoImage, LOGO_X, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT, drawComponent);
//			graphics.drawImage(logoImage, LOGO_X, LOGO_Y, drawComponent);

		}
//		System.out.println("End Paint start view");



//		graphics.drawString(ComputerDefinitions.definitions[0], LINE_X_OFFSET, DEFINITION_Y + firstLineYBase);


//		graphics.drawString(editorGenerationMessage, 10, 40);
//		graphics.drawString("" + numSeconds +"s", 50, 80);
//		graphics.drawString(lastTraceable, 100, 100);
	}
//
//	@Override
//	public void newEvent(Exception aTraceable) {
//		lastTraceableMessage = aTraceable.getMessage();
//		if (aTraceable instanceof EditorGenerationStarted) {
//			if (remainingEditors == 0 && frame.getState() == frame.ICONIFIED) {
//			     frame.setState(frame.NORMAL);
//			}
//			currentGenerationStarted = ((EditorGenerationStarted)aTraceable);
//			editedObject = currentGenerationStarted.getTargetObject();
//			
//			editorGenerationMessage =aTraceable.getMessage();	
//			remainingEditors++;
//			if (currentGenerationStarted instanceof MainEditorGenerationStarted) {
//				editorGenerationMessage = "Main Editor Generation";
//			} else if (currentGenerationStarted instanceof DrawingEditorGenerationStarted) {
//				editorGenerationMessage = "Drawing Editor Generation";
//			} else if (currentGenerationStarted instanceof TreeEditorGenerationStarted) {
//				editorGenerationMessage = "Tree Editor Generation";
//			}
////			if (aTraceable instanceof MainEditorGenerationStarted)
////			   numSeconds = 0;
//		} else if (aTraceable instanceof EditorGenerationEnded) {
//			currentGenerationEnded = (EditorGenerationEnded) aTraceable;
//			remainingEditors--;
////			if (remainingEditors == 0) {
//////				completed = true;
////				frame.setState(frame.ICONIFIED);
////				remainingEditors = 0;
////			}
//			if (currentGenerationEnded instanceof MainEditorGenerationEnded) {
//				editorGenerationMessage = "Main Editor Generation Completed";
//			} else if (currentGenerationEnded instanceof DrawingEditorGenerationEnded) {
//				editorGenerationMessage = "Drawing Editor Generation Completed";
//			} else if (currentGenerationEnded instanceof TreeEditorGenerationEnded) {
//				editorGenerationMessage = "Tree Editor Generation Ended";
//			}
//		} else if (aTraceable instanceof FrameSetVisibleEnded) {
//			frame.setState(frame.ICONIFIED);
//		}
//		else
//		{
//			statusMessage = aTraceable.getMessage();
//		}
//		drawComponent.repaint();
//	}
	
	@Override
	public void newEvent(Exception aTraceable) {
		lastEventTime = System.currentTimeMillis();
		lastTraceableMessage = aTraceable.getMessage();
		if (aTraceable instanceof EditorGenerationStarted) {
			if (remainingEditors == 0 && frame.getState() == frame.ICONIFIED) {
			     frame.setState(frame.NORMAL);
			}
			currentGenerationStarted = ((EditorGenerationStarted)aTraceable);
			mainHeader = currentGenerationStarted.getTargetObject();
			if (mainHeader != null)
				mainHeaderToString = mainHeader.toString(); // this is a synchronized method that causes paint method to block
			else
				mainHeaderToString = null;
			
//			majorStepMessage =aTraceable.getMessage();	
			remainingEditors++;
//			if (currentGenerationStarted instanceof MainEditorGenerationStarted) {
//				majorStepMessage = "Main Editor Generation";
//			} else if (currentGenerationStarted instanceof DrawingEditorGenerationStarted) {
//				majorStepMessage = "Drawing Editor Generation";
//			} else if (currentGenerationStarted instanceof TreeEditorGenerationStarted) {
//				majorStepMessage = "Tree Editor Generation";
//			}
//			if (aTraceable instanceof MainEditorGenerationStarted)
//			   numSeconds = 0;
		} else if (aTraceable instanceof EditorGenerationEnded) {
			currentGenerationEnded = (EditorGenerationEnded) aTraceable;
			remainingEditors--;
			if (
				(aTraceable instanceof TreeEditorGenerationEnded || aTraceable instanceof DrawingEditorGenerationEnded)
					
					&& remainingEditors == 0) {
				frame.setState(frame.ICONIFIED);
				automaticallyIconified = true;
			}
//			majorStepMessage =aTraceable.getMessage();	
//			if (remainingEditors == 0) {
////				completed = true;
//				frame.setState(frame.ICONIFIED);
//				remainingEditors = 0;
//			}
//			if (currentGenerationEnded instanceof MainEditorGenerationEnded) {
//				majorStepMessage = "Main Editor Generation Completed";
//			} else if (currentGenerationEnded instanceof DrawingEditorGenerationEnded) {
//				majorStepMessage = "Drawing Editor Generation Completed";
//			} else if (currentGenerationEnded instanceof TreeEditorGenerationEnded) {
//				majorStepMessage = "Tree Editor Generation Ended";
//			}
		} else if (aTraceable instanceof FrameSetVisibleEnded) {
			frame.setState(frame.ICONIFIED);
			automaticallyIconified = true;
		} else if (aTraceable instanceof MajorStepInfo && 
				!(((MajorStepInfo) aTraceable).getTargetObject() instanceof SLModel)) { // Ignore drawing object steps
			majorStepMessage =aTraceable.getMessage();	

		}
		else
		{
			statusMessage = aTraceable.getMessage();
		}
		repaintDrawingComponent();
//		drawComponent.repaint();
	}
	
	synchronized void repaintDrawingComponent() {
//		System.out.println("Repainting drawing component");
		drawComponent.repaint();
//		System.out.println("Finioshed Repainting drawing component");


	}

	@Override
	public void run() {
		while (true) {
//			if (stopUpdating) {
//				break;
//			}
			ThreadSupport.sleep(sleepInterval);
//			if (remainingEditors > 0) {
			numSeconds++;
//			if (numSeconds == 1 ) {
//				frame.setState(frame.NORMAL);
//			}
			long timeToLastEvent = System.currentTimeMillis() - lastEventTime;	
			if ((automaticallyIconified || manuallyIconified) && frame.getState() != frame.ICONIFIED) {
				automaticallyIconified = false;
				manuallyDeIconified = true;
			}
			if (manuallyDeIconified && frame.getState() != frame.NORMAL) {
				manuallyDeIconified =false;
				manuallyIconified = true;
			}
			if (timeToLastEvent > IDLE_TIME && frame.getState() == frame.NORMAL && ! manuallyDeIconified) {
				frame.setState(frame.ICONIFIED);
				automaticallyIconified = true;
			} else if (frame.getState() == frame.NORMAL && (mainHeader != null && mainHeader.toString() != null ))
				repaintDrawingComponent();
//			drawComponent.repaint();
			
		}
			
//		}
		
	}

	@Override
	public void setVisible(boolean newVal) {
		frame.setVisible(newVal);		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		 computerDefinition = ComputerDefinitionsManager.getRandomDefinition();
		 drawComponent.repaint();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}	

}
