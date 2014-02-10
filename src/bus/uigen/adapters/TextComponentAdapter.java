package bus.uigen.adapters;import java.awt.Color;import java.awt.event.KeyEvent;import java.awt.event.KeyListener;import java.awt.event.TextEvent;import java.awt.event.TextListener;import javax.swing.event.DocumentEvent;import javax.swing.event.DocumentListener;import javax.swing.text.PlainDocument;import util.trace.Tracer;import bus.uigen.WidgetAdapter;import bus.uigen.attributes.AttributeNames;import bus.uigen.introspect.Attribute;import bus.uigen.oadapters.PrimitiveAdapter;import bus.uigen.view.SelectionColorSelector;import bus.uigen.view.WidgetShell;import bus.uigen.widgets.VirtualComponent;import bus.uigen.widgets.VirtualTextComponent;import bus.uigen.widgets.events.VirtualActionEvent;import bus.uigen.widgets.events.VirtualActionListener;import bus.uigen.widgets.events.VirtualFocusEvent;import bus.uigen.widgets.events.VirtualFocusListener;public abstract class TextComponentAdapter extends WidgetAdapter 
implements DocumentListener, VirtualActionListener, TextListener, VirtualFocusListener, KeyListener {
  
  private boolean actionMode = true;  public static int NUM_COLUMNS = 5;    public static int NUM_ROWS = 5;  boolean linked = false;  boolean mutatingString = false;  String prompt;  boolean sendUnchangedText = true;  boolean isString = true;  boolean isPrimitive = true;
 
  public boolean isComponentAtomic() {  	return true;  }
  
  // Implementation of 
  // abstract methods

  public String getType() {
    return "java.lang.String";
  }
  //JTextField jtf;  //JTextComponent jtf;  VirtualTextComponent jtf;
  String text;
  /*  public Component instantiateComponent(Class cclass) {
	  //System.out.println("instantiating jtextfield");	  jtf = new JTextField("", NUM_COLUMNS);	  return jtf;
      //return new JTextField("", NUM_COLUMNS);
  }
  */        public static int numColumns (String text) {
	  if (text == null) return 0;	  int retVal = 0;	  
	  int curRowSize = 0;
	  for (int i = 1; i < text.length(); i++) {		  if (text.charAt(i) == '\n') {		    retVal = Math.max(curRowSize, retVal);					    curRowSize = 0;		  } else			  curRowSize++;
	  }
	  return Math.max(curRowSize, retVal);  }  /*  boolean notInRange (Object newVal) {	  if (!(newVal instanceof Integer)) return false;	  int intVal = (Integer) newVal;	  int maxVal = (Integer) getObjectAdapter().getMaxValue();	  int minVal = (Integer) getObjectAdapter().getMinValue();	  return intVal < minVal || intVal > maxVal;	    }  */

  public synchronized void setUIComponentTypedValue(Object newval) {
    try {    	if (prompt != null &&     			firstFocus && (newval == null || newval.toString().equals("")))    		return;    	isString = newval instanceof String;    	isPrimitive = getObjectAdapter() instanceof PrimitiveAdapter;    		  //System.out.println("Setting Text Component Adapter" + newval);
      //jtf = (JTextComponent) getUIComponent();    	// do we need this code?    	if (spane == null)      jtf = (VirtualTextComponent) getUIComponent();//    	else//    		jtf = (VirtualTextComponent) spane.getScrolledComponent();
	      	    	//System.out.println(jtf.getColumns() + " " + tf.getColumns());	  //if (tf != jtf) System.out.println("setUI: not the same tf");
	  //int length = ((String) newval).length();	      if (newval == null)    	  text = "";      else if (isPrimitive || getObjectAdapter().unparseAsToString())
//	  text = (String) newval;    	  text = newval.toString();      else {    	      	  text = getObjectAdapter().toFullTextLine();      }                          	  /*	  if (notInRange(newval))		  jtf.setText("");	  else	  */            
//      jtf.setText((String) newval);      //      jtf.setText( text);      setText(text);      	  //tf.setColumns(NUM_COLUMNS);
	  //tf.validate();	  //tf.setColumns(NUM_COLUMNS);	  //System.out.println((String) newval);	  //if (!isEditable)      if (getObjectAdapter().isUnEditable() || !isPrimitive)		  this.setUIComponentUneditable();
    } catch (ClassCastException e) {    	e.printStackTrace();
    }
  }    protected void setText(String newVal) {//	  System.out.println("setting text" +newVal);      jtf.setText( text);  }
 
  public Object getUIComponentValue() {
    String val = new String("");
    try {
     // jtf = (JTextField) getUIComponent();
      val = jtf.getText();
    } catch (ClassCastException e) {
    }
    return val;
  }    public void setPreWrite() {
//	  ((VirtualTextComponent) getUIComponent()).setEditable(
//							getObjectAdapter().getPreWrite());	  jtf.setEditable(				getObjectAdapter().getPreWrite());  }    boolean isEditable = true;
  
  public void setUIComponentEditable() {	  isEditable = true;	  // in case this is scrolled, getUIComponent() will not work	  if (getUIComponent() != null)
    //((VirtualTextComponent) getUIComponent()).setEditable(true);	  jtf.setEditable(true);	 Boolean selectable = adapter.getComponentSelectable();	 if ((selectable == null || !selectable) && getUIComponent().getBackground() == AttributeNames.getSelectionColor())		 getUIComponent().setBackground(editableColor);//	  Color background =  (Color) getUIComponent().getBackground();//	  if (background != SelectionColorSelector.getColor())//	    	//	    	oldUIComponentColor = (Color) getUIComponent().getBackground();	 // oldUIComponentColor = (Color) getUIComponent().getBackground();
  }
  
  public void setUIComponentUneditable() {	  isEditable = false;	  //jtf.setEditable(false);	 
    //((VirtualTextComponent) getUIComponent()).setEditable(false);    jtf.setEditable(false);
  }
  
 // this should probably move to uiWidgetAdapter
  //Color oldComponentColor = Color.white;
  public void setUIComponentSelected() {	super.setUIComponentSelected();			//	//System.out.println("changing selection color");
//    if (getUIComponent().getBackground() != SelectionColorSelector.getColor())
//    	oldUIComponentColor = (Color) getUIComponent().getBackground();//	else oldUIComponentColor = (Color) getOriginalBackground(getUIComponent());
    getUIComponent().setBackground(AttributeNames.getSelectionColor());
    // Repaint the component to get the color right
    getUIComponent().repaint();
  }
  public void setUIComponentDeselected() {	super.setUIComponentDeselected();	if (isEditable)		getUIComponent().setBackground(editableColor);	else		getUIComponent().setBackground(readOnlyColor);	//System.out.println("changing unselection color");
    //getUIComponent().setBackground(oldUIComponentColor);
    getUIComponent().repaint();
  }  Color readOnlyColor;  Color editableColor;
  public void linkUIComponentToMe() {	  //System.out.println("linking textfield");
    //if (t instanceof JTextComponent) {
     //jtf = (JTextComponent) t;     //jtf = (VirtualTextComponent) t;
	  /*	  if (jtf == null) {		  jtf = tf;		  jtf.setColumns(NUM_COLUMNS);	  }	  */	  if (linked) return;	  prompt = adapter.getPrompt();	              
      PlainDocument document = new PlainDocument();
      jtf.setDocument(document);      if (prompt != null) {    	  mutatingString = true;    	  jtf.setText(prompt);    	  //jtf.validate();    	  mutatingString = false;    	  //KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();      }  
      document.addDocumentListener(this);      // for some reason this was commented out      //jtf.addActionListener(this);      Color overriddenColor = adapter.getComponentBackground();      if (overriddenColor != null) {    	  editableColor = overriddenColor;    	  readOnlyColor = overriddenColor;      } else {      boolean isInitiallyEditable = jtf.isEditable();      jtf.setEditable(true);      editableColor = (Color) jtf.getBackground();      jtf.setEditable(false);      readOnlyColor  = (Color) jtf.getBackground();      jtf.setEditable(isInitiallyEditable);            }      jtf.addTextListener(this);            /*	  if (jtf instanceof JTextField) {
         ((JTextField) jtf).addActionListener(this);	  //((JTextField) jtf).setColumns(100);	  }	  */	  jtf.addKeyListener(this);	  
      jtf.addFocusListener(this);      //getObjectAdapter().getUIFrame().addKeyListener(jtf);            linked = true;            
    //}
  }  public VirtualComponent getUIComponent() {	  if (spane == null)	  return jtf;	  else return spane;  }  public void linkUIComponentToMe(VirtualComponent t) {	  //System.out.println("linking textfield");    //if (t instanceof JTextComponent) {     //jtf = (JTextComponent) t;	  //if (jtf == t) return;     jtf = (VirtualTextComponent) t;     linkUIComponentToMe();    //}  }
  WidgetShell genericWidget;
  public boolean uiComponentValueChanged() {
	  boolean retVal;	  if (sendUnchangedText || textChanged()) {		retVal = super.uiComponentValueChanged();		if (retVal)
		refreshText();		else {			Tracer.userMessage("Cannot commit edited value. Restoring original value");			restoreText();		}		return retVal;	  } else {		  uiComponentValueEdited(false);		  return false;	  }		
  }
  public void uiComponentValueEdited() {
	  	  //if (textChanged())		super.uiComponentValueEdited(true);		
  }  void refreshText() {
	  text = jtf.getText();  }  void restoreText() {	  jtf.setText(text);  }
  boolean textChanged() {
	  if (text == null) return true;	  	  return !text.equals(jtf.getText());	  
  }  public void textValueChanged(TextEvent evt) {	    if (!actionMode) {	      uiComponentValueChanged();	    }	  }
  
  // DocumentListener interface
  public void changedUpdate(DocumentEvent e) {
    if (!actionMode)
      uiComponentValueChanged();
	/*	else		uiComponentValueEdited();	*/
  }  boolean firstTime = true;
  public void insertUpdate(DocumentEvent e) {	  if (!actionMode)
      uiComponentValueChanged();
	  /*	  else if (!firstTime)		  uiComponentValueEdited();	  firstTime = false;	  */
  }

  public void removeUpdate(DocumentEvent e) {
    	  if (!actionMode)
      uiComponentValueChanged();
	  /*	  else		  uiComponentValueEdited();	  */
  }
  boolean actionPerformed = false;
  public void actionPerformed(VirtualActionEvent e) {
    if (actionMode) {		actionPerformed = true;		keyTyped = false;
       uiComponentValueChanged();
    } 
  }

  public boolean processAttribute(Attribute attrib) {  	if (attrib.getName().equals("actionMode") ||    		attrib.getName().equals(AttributeNames.INCREMENTAL)) {
      if (attrib.getValue() instanceof Boolean) {
	actionMode = ((Boolean) attrib.getValue()).booleanValue();
      }
      return true;
    }
    else
      return super.processAttribute(attrib);
  }
  boolean firstFocus = true;
  // Methods invoked on focus gain/loss in the view
  public void focusGained(VirtualFocusEvent e) {	   //System.out.println("focus gained");	  if (firstFocus && prompt != null) {		   mutatingString = true;		   jtf.setText("");		   mutatingString = false;		   firstFocus = false;	   }
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusGained(e);
    }	
  }

  public void focusLost(VirtualFocusEvent e) {	  //System.out.println("focus lost");
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusLost();
      //awtComponent.setBackground(Color.white);
    }
  }  public void keyReleased(KeyEvent k) {  }  public void keyPressed(KeyEvent k) {  }  boolean keyTyped = false;  public void keyTyped (KeyEvent k) { // received after action performed event, key = \n	  if (keyTyped) return;	  if (actionPerformed) {		  actionPerformed = false;		  //keyTyped = false;	  } else if (jtf.isEditable()) {		  keyTyped = true;
	      super.uiComponentValueEdited(true);	  }  }  public void userInputUpdated(boolean newVal) {	  keyTyped = false;  }
}



