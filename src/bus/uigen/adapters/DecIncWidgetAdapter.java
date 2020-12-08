package bus.uigen.adapters;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.view.DecIncWidget;
import bus.uigen.view.ModelClass;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualTextField;
import bus.uigen.widgets.events.VirtualActionEvent;


//public class uiJTextFieldAdapter extends uiWidgetAdapter public class DecIncWidgetAdapter extends
TextComponentAdapter {
//implements DocumentListener, ActionListener, FocusListener, KeyListener {
  
 /*
  public uiJTextFieldAdapter() {
  }	
  
  // Implementation of 
  // abstract methods

  public String getType() {
    return "java.lang.String";
  }
  //JTextField jtf;  JTextComponent jtf;
  String text;
	*/
	
 	
  DecIncWidget jDItf = null;  
  boolean modelCreated = false;  VirtualButton upB = null;
  VirtualButton downB = null;  public static int DECINCUNIT = 1;  
    
  ModelClass modelObject = null;			   
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  //modelObject = new ModelClass(getObjectAdapter().getPropertyClass());  //expecting to inherit the getObjAdpt() from super.super...uiWidgetAdaptper

	  System.err.println("instantiating jtextfield with decinc " + DECINCUNIT + ", " + NUM_COLUMNS);	  //jDItf = new DecIncWidget("", NUM_COLUMNS, DECINCUNIT);  //pass in object
	  try {
	  Object widget = cclass.newInstance();
	  if (widget instanceof DecIncWidget)
		  jDItf = (DecIncWidget) widget;
	  else
		  jDItf = new DecIncWidget();
	  } catch (Exception e) {
		  jDItf = new DecIncWidget();
	  }
	  //jDItf = cclass.newInstance();	  init (jDItf);
	  /*
	  jtf = jDItf.getTextField();  //need to give this thing a jtf
									//so that the inherited methods can use it
	  	  
	  upB = jDItf.getUp();
	  downB = jDItf.getDown();
	  */
	  	  return jDItf;
      //return new JTextField("", NUM_COLUMNS);
  }
  void init (DecIncWidget t) {
	  jDItf = (DecIncWidget) t;
	  jtf = jDItf.getTextField();  //need to give this thing a jtf
		//so that the inherited methods can use it


	  upB = jDItf.getUp();
	  downB = jDItf.getDown();
  }
  public void linkUIComponentToMe(VirtualComponent t) {
	  if (jDItf != t) {
		  init ((DecIncWidget) t);
		  /*
		  jDItf = (DecIncWidget) t;
		  jtf = jDItf.getTextField();  //need to give this thing a jtf
			//so that the inherited methods can use it


		  upB = jDItf.getUp();
		  downB = jDItf.getDown();
		  */
	  }
	  linkUIComponentToMe();
  }
  
  public VirtualComponent getUIComponent() {
	  return jDItf;
  }   public void setUIComponentTypedValue(Object newval) {	 	 if (!modelCreated)  { 
		 if (getObjectAdapter() != null) {

	    //	System.out.println("instantiating jtextfield for class " + getObjectAdapter().getPropertyClass().getName());			 			modelObject = new ModelClass(getObjectAdapter().getPropertyClass());  //expecting to inherit the getObjAdpt() from super.super...uiWidgetAdaptper			
			jDItf.setModelObject(modelObject);
			jDItf.setObjectAdapter(this.getObjectAdapter());			modelCreated = true;		 }		 else			System.err.println("XXXXXXXXXattempt to make model failed");
		 	 }	 	 	try {      jtf.setText((String) newval);
	  text = (String) newval;    } catch (ClassCastException e) {
    }	 
	///*JTextField*/ jtf1 = (JTextField) jtf;
	 jtf1 = (VirtualTextField) jtf;
	int oldLength = jtf1.getColumns();	jtf1.setColumns(Math.max(jtf1.getColumns(), numColumns ((String) newval))/2);
 } //JTextField jtf1;
 VirtualTextField jtf1;  
 /*
  public void setUIComponentTypedValue(Object newval) {
    super.setUIComponentTypedValue(newval);
	DecIncWidget jDItf1 = (DecIncWidget) jDItf;	jDItf1.getJTextField().setColumns(Math.max(jtf1.getColumns(), numColumns ((String) newval)));
  }  */
	 // should I implement my own getUIComponent that returns the textfield in this or  //should I just implement the methods that get called after getUIcomponent? getUIComponent()
   /*
  public Object getUIComponentValue() {
    String val = new String("");
    try {
     // jtf = (JTextField) getUIComponent();
      val = jtf.getText();
    } catch (ClassCastException e) {
    }
    return val;
  }
  */ 
  public void setUIComponentEditable() {
    jtf.setEditable(true);	upB.setEnabled(true);	downB.setEnabled(true);
  }
  
  public void setUIComponentUneditable() {
    jtf.setEditable(false);	upB.setEnabled(false);	downB.setEnabled(false);
 }
  
/* 
  Color oldColor = Color.white;
  public void setUIComponentSelected() {	super.setUIComponentSelected();
    if (getUIComponent().getBackground() != SELECTION_COLOR)
      oldColor = getUIComponent().getBackground();	else oldColor = getOriginalBackground(getUIComponent());
    getUIComponent().setBackground(SELECTION_COLOR);
    // Repaint the component to get the color right
    getUIComponent().repaint();
  }
  public void setUIComponentDeselected() {	super.setUIComponentDeselected();
    getUIComponent().setBackground(oldColor);
    getUIComponent().repaint();
  }    */  
  public void linkUIComponentToMe(/*VirtualComponent t*/) {	  //System.out.println("linking textfield");	  /*
    if (t instanceof JTextComponent) {
     jtf = (JTextComponent) t;	  */	  //if (t instanceof DecIncWidget) {		 		
		//jtf = (VirtualTextComponent)(((DecIncWidget)t).getJTextField());
	  
	  /*  button stuff in encapsulated in widget only and taken care of w/ model		upB = (JButton)(((DecIncWidget)t).getUp());
		downB = (JButton)(((DecIncWidget)t).getDown());		*/		
		PlainDocument document = new PlainDocument();
		jtf.setDocument(document);
		document.addDocumentListener(this);			    if (jtf instanceof VirtualTextField)
         ((VirtualTextField) jtf).addActionListener(this);					jtf.addKeyListener(this);
		jtf.addFocusListener(this);
		
		/* button is independent		upB.addActionListener(this);
		upB.addFocusListener(this);		downB.addActionListener(this);		downB.addFocusListener(this);		*/		
    //}
  }    //at this point  /*
  uiObjectAdapter adapter;
  uiGenericWidget genericWidget;
  public void uiComponentValueChanged() {
	  	  if (textChanged()) {		super.uiComponentValueChanged();	  }
		
  }  
  public void uiComponentValueEdited() {
	  	  //if (textChanged())		super.uiComponentValueEdited();		
  }  
  boolean textChanged() {
	  if (text == null) return true;	  	  return !text.equals(jtf.getText());	  
  }  
  
  // DocumentListener interface
  public void changedUpdate(DocumentEvent e) {
    if (!actionMode)
      uiComponentValueChanged();
  }  boolean firstTime = true;
  public void insertUpdate(DocumentEvent e) {	  if (!actionMode)
      uiComponentValueChanged();
  }

  public void removeUpdate(DocumentEvent e) {
    	  if (!actionMode)
      uiComponentValueChanged();
  }  */
  boolean actionPerformed = false;
  public void actionPerformed(VirtualActionEvent e) {
	  
	//	System.out.println("adapter actionperformed called");		 
		if (e.getSource() == upB) {
		  //increment textfield						
		}
	  
		if (e.getSource() == downB) {
		  //decrement textfield
		
		
		}
		
		if (e.getSource() == jtf) {
			
		//	System.out.println("jtextfield fires an event");
			
		}		
		super.actionPerformed(e);
     	
  }
/*
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
  }  public void keyReleased(KeyEvent k) {  }  public void keyPressed(KeyEvent k) {  }  public void keyTyped (KeyEvent k) {	  if (actionPerformed)		  actionPerformed = false;	  else
	      super.uiComponentValueEdited();	  }  */

}



