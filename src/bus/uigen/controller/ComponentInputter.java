package bus.uigen.controller;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface ComponentInputter extends KeyListener, MouseListener, MouseMotionListener, AWTEventListener {
	public void openKeyStream();
	public void closeKeyStream() ;
	
	public void openKeyEventStream() ;
	public void closeKeyEventStream() ;
	
	public void openMouseClickEventStream() ;
	public void closeMouseClickEventStream() ;
	
	public void openAWTEventStream() ;
	public void closeAWTEventStream() ;
	
	public char getChar() ;
	public KeyEvent getKeyEvent() ;
	public MouseEvent getMouseClickedEvent();
	public MouseEvent getMouseDraggedEvent();
	public AWTEvent getAWTEvent();	 
}
