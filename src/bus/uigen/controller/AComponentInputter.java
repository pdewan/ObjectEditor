package bus.uigen.controller;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import bus.uigen.OEFrame;
import bus.uigen.widgets.VirtualComponent;

public class AComponentInputter implements ComponentInputter{
	boolean queueChars;
	boolean queueKeyEvents;
	boolean queueMouseClickEvents;
	boolean queueMouseDraggedEvents;
	boolean queueAWTEvents;
	BlockingQueue<Character> keyBlockingQueue = new LinkedBlockingQueue<Character>();
	BlockingQueue<KeyEvent> keyEventBlockingQueue = new LinkedBlockingQueue<KeyEvent>();
	BlockingQueue<MouseEvent> mouseClickEventBlockingQueue = new LinkedBlockingQueue<MouseEvent>();
	BlockingQueue<MouseEvent> mouseDraggedEventBlockingQueue = new LinkedBlockingQueue<MouseEvent>();
	BlockingQueue<AWTEvent> awtEventBlockingQueue = new LinkedBlockingQueue<AWTEvent>();	
	Component component;
	public AComponentInputter(VirtualComponent aComponent) {
		aComponent.addMouseListener(this);
		aComponent.addKeyListener(this);
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
	}
	public AComponentInputter(Component aComponent) {
		init(aComponent);
//		aComponent.addMouseListener(this);
//		aComponent.addKeyListener(this);
//		aComponent.addMouseMotionListener(this);
//		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

	}
	public AComponentInputter(OEFrame anOEFrame) {
		Component aComponent = anOEFrame.getDrawComponent();
		init(aComponent);
	}	
	void init (Component aComponent) {
		component = aComponent;
		aComponent.setFocusable(true);
		aComponent.requestFocusInWindow();
//		aComponent.addMouseListener(this);		
//		aComponent.addKeyListener(this);
//		aComponent.addMouseMotionListener(this);
//		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);		
	}
	boolean awtListenerAdded;
	void maybeAddAWTListener() {
		if (awtListenerAdded)
			return;
		awtListenerAdded = true;
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);		

		
//		component.addMouseListener(this);		

	}
	
	boolean mouseListenerAdded;
	void maybeAddMouseListener() {
		if (mouseListenerAdded)
			return;
		mouseListenerAdded = true;
		
		component.addMouseListener(this);		

	}
	boolean mouseMotionListenerAdded;
	void maybeAddMouseMouseMotionListener() {
		if (mouseMotionListenerAdded)
			return;
		mouseMotionListenerAdded = true;
		
		component.addMouseMotionListener(this);		

	}
	
	boolean keyListenerAdded;
	void maybeAddKeyListener() {
		if (keyListenerAdded)
			return;
		keyListenerAdded = true;
		
		component.addKeyListener(this);		

	}
	
	public void openKeyStream() {
		queueChars = true;
	}	
	public void closeKeyStream() {
		queueChars = false;
	}
	
	public void openKeyEventStream() {
		queueKeyEvents = true;
	}	
	public void closeKeyEventStream() {
		queueKeyEvents = false;
	}
	
	public void openMouseClickEventStream() {
		queueMouseClickEvents = true;
	}	
	public void closeMouseClickEventStream() {
		queueMouseClickEvents = false;
	}
	public void openAWTEventStream() {
		queueAWTEvents = true;
	}	
	public void closeAWTEventStream() {
		queueAWTEvents = false;
	}
	
	public char getChar() {
		maybeAddKeyListener();
		try {
		queueChars = true;
		return keyBlockingQueue.take();
		} catch (InterruptedException e) {
			return 0;
		}		
	}
	public KeyEvent getKeyEvent() {
		maybeAddKeyListener();
		try {
		queueKeyEvents = true;
		return keyEventBlockingQueue.take();
		} catch (InterruptedException e) {
			return null;
		}		
	}
	public MouseEvent getMouseClickedEvent() {
		maybeAddMouseListener();
		try {
		queueMouseClickEvents = true;
		return mouseClickEventBlockingQueue.take();
		} catch (InterruptedException e) {
			return null;
		}		
	}
	@Override
	public MouseEvent getMouseDraggedEvent() {
		maybeAddMouseMouseMotionListener();
		try {
			queueMouseDraggedEvents = true;
//			System.out.println("taking event");
			return mouseDraggedEventBlockingQueue.take();
			} catch (InterruptedException e) {
				return null;
			}	
	}
	public AWTEvent getAWTEvent() {
		maybeAddAWTListener();
		try {
		queueAWTEvents = true;
		return awtEventBlockingQueue.take();
		} catch (InterruptedException e) {
			return null;
		}		
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		if (queueChars) {
			keyBlockingQueue.add(arg0.getKeyChar());
		}
		if (queueKeyEvents) {
			keyEventBlockingQueue.add(arg0);
		}		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		maybeAddMouseListener();

//		System.out.println("mouse clicked");
		if (queueMouseClickEvents) {
			mouseClickEventBlockingQueue.add(arg0);
		}
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (queueMouseDraggedEvents) {
			mouseDraggedEventBlockingQueue.add(arg0);
		}
	}
	@Override
	public void eventDispatched(AWTEvent arg0) {
		
		if (queueAWTEvents) {
			if (arg0.getID() == KeyEvent.KEY_TYPED ||
			arg0.getID() == MouseEvent.MOUSE_CLICKED ||
			arg0.getID() == MouseEvent.MOUSE_DRAGGED)
					
			awtEventBlockingQueue.add(arg0);
		}		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	


}
