package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.EditorPaneSelector;
import bus.uigen.widgets.TextAreaSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualEditorPane;
import bus.uigen.widgets.VirtualTextArea;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import util.trace.Tracer;

import java.awt.event.*;
import java.io.IOException;
import java.net.URL;


public class EditorPaneAdapter	extends TextComponentAdapter implements HyperlinkListener{	
//implements DocumentListener, ActionListener, FocusListener {
  
 
  
  // Implementation of 
  // abstract methods
	
	public String getType() {
		// this should really return a Class
	    return URL.class.getName();
	  }
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
    
	  jtf = EditorPaneSelector.createEditorPane();
	  jtf.setEditable(false);
	  adapter.setSelectionIsLinkAttribute(true);
	  getEditorPane().addHyperlinkListener(this);
	  //((VirtualTextArea) jtf).setLineWrap(true);
	  	  	  return jtf;
  }
  
  VirtualEditorPane getEditorPane() {
	  return ( (VirtualEditorPane) jtf);
  }
 
  public void setUIComponentTypedValue(Object newval) {
	if (newval instanceof URL) {
		getEditorPane().setPage((URL) newval);
	} else if (newval instanceof String)  {
		getEditorPane().setText((String) newval);
	}
  }
  
  public void hyperlinkUpdate(HyperlinkEvent event) {
	    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    	URL link = event.getURL();
	    	  adapter.setExpansionObjectAttribute(link);
	    	  
	    	  try {
	    		  link.getContent();
	    	  adapter.getUIFrame().getBrowser().browseObject(adapter);
	    	  } catch (Exception e) {
	    		  Tracer.userMessage("Cannot display: " + link + ". Only html can be displayed." );
	    	  }
	    	  //getEditorPane().setPage(event.getURL());
	    	  //getEditorPane().setText(event.getURL().toExternalForm());
	      
	    }
	  }
  
  public boolean delegateSelectionToWidgetShell() {
	  return false;
  }
  public boolean delegateOpenToWidgetShell() {
	  return false;
  }

@Override
public boolean getIncrementalChildAddition() {
	// TODO Auto-generated method stub
	return false;
}
  
  
  /* uiFrame frame;

  public void setUIComponentTypedValue(Object newval) {	  super.setUIComponentTypedValue(newval);
	  //if (frame != null) return;	  //frame = getObjectAdapter().getUIFrame(); 
	  
	  //if (frame != null) {	  //   frame.addUIFrameToolBarButton(uiFrame.UPDATE_ALL_COMMAND, null);
	  //   frame.showToolBar();
	  //}    
	 //
  }
  */
 /*
  public Object getUIComponentValue() {
    String val = new String("");	//System.out.println( "ui component" + getUIComponent());
    try {
      JTextArea ta = (JTextArea) getUIComponent();	  Document d = ta.getDocument();	  val = ta.getText();
      //val =d.getText(d.getStartPosition(), d.getEndPosition());
	  //ta.setText("updated");	//System.out.println("returning " + val + ta);
    } catch (Exception e) {		System.out.println(e);
    }	//System.out.println("returning " + val);
    return val;
  }
  
  public void setUIComponentEditable() {
    ((JTextArea) getUIComponent()).setEditable(true);
  }
  
  public void setUIComponentUneditable() {
    ((JTextArea) getUIComponent()).setEditable(false);
  }
  
 
  Color oldColor = Color.white;
  public void setUIComponentSelected() {	super.setUIComponentSelected();
    if (getUIComponent().getBackground() != SelectionColorSelector.getColor())
      oldColor = getUIComponent().getBackground();	else oldColor = getOriginalBackground(getUIComponent());
    getUIComponent().setBackground(SelectionColorSelector.getColor());
    // Repaint the component to get the color right
    getUIComponent().repaint();
  }
  public void setUIComponentDeselected() {	super.setUIComponentDeselected();
    getUIComponent().setBackground(oldColor);
    getUIComponent().repaint();
  }
  public void linkUIComponentToMe(Component t) {	 // System.out.println("component linked" + t);
    if (t instanceof JTextArea) {		//System.out.println("text area linked" + t);
      JTextArea ta = (JTextArea) t;
      PlainDocument document = new PlainDocument();
      ta.setDocument(document);
      document.addDocumentListener(this);
      //ta.addActionListener(this);
      ta.addFocusListener(this);
    }
  }
  
  // DocumentListener interface
  public void changedUpdate(DocumentEvent e) {	  System.out.println("changed text area");
    if (!actionMode)
      uiComponentValueChanged();
  }
  boolean firstTime = true;
  public void insertUpdate(DocumentEvent e) {	  if (!actionMode)
      uiComponentValueChanged();
  }

  public void removeUpdate(DocumentEvent e) {
    if (!actionMode)
      uiComponentValueChanged();
  }
  
  public void actionPerformed(ActionEvent e) {	  System.out.println("text area changed");
    if (actionMode) {
       uiComponentValueChanged();
    }
  }

  public boolean processAttribute(Attribute attrib) {
    if (attrib.getName().equals("actionMode")) {
      if (attrib.getValue() instanceof Boolean) {
	actionMode = ((Boolean) attrib.getValue()).booleanValue();
      }
      return true;
    }
    else
      return super.processAttribute(attrib);
  }

  // Methods invoked on focus gain/loss in the view
  public void focusGained(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusGained();
    }
  }

  public void focusLost(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusLost();
      //awtComponent.setBackground(Color.white);
    }
  }  */
}



