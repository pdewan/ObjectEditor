package bus.uigen.adapters;
import javax.swing.JTextArea;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.TextAreaSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualTextArea;


public class TextAreaAdapter	extends TextComponentAdapter {	
//implements DocumentListener, ActionListener, FocusListener {
  
 //VirtualScrollPane spane;
  
  // Implementation of 
  // abstract methods
    public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {
    //return new JTextField(NUM_COLUMNS);
	  //System.out.println("instantiating jtextarea");	  //JTextArea ta = new JTextArea("");	  //jtf = new JTextArea("");
	  //spane = ScrollPaneSelector.createScrollPane();
	  jtf = TextAreaSelector.createTextArea("");
	  instantiatedComponent = true;
	  jtf.setName(adapter.toText());
	  ((VirtualTextArea) jtf).setLineWrap(true);
	  ((VirtualTextArea) jtf).setWrapStyleWord(true);
	  //VirtualContainer panel = PanelSelector.createPanel();
	  //panel.add(jtf);
	  //spane.setScrolledComponent(jtf);
	  //spane.setScrolledComponent(panel);
	  //((VirtualTextArea) jtf).setWrapStyleWord(true);
	  //ScrollPane newScrollPane = new ScrollPane();
	  //newScrollPane.add(ta = new JTextArea(""));	//return new JTextField("", NUM_COLUMNS);	//return newScrollPane;
	  if (adapter.isScrolled()) {
		  spane = ScrollPaneSelector.createScrollPane();		  
		  spane.setScrolledComponent(jtf);
		  //spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		  return spane;
	  }
		  	  	  return jtf;
	  //return spane;
  }
  
  void setSizeOrColumns (VirtualTextArea textArea) {
	  int numColumns = getObjectAdapter().getTextFieldLength();
	  	if (numColumns != 0)
	  		textArea.setColumns(numColumns);
	  	else {
	  		super.setAttributes(textArea);
	  		
	  	}
	  
  }
 
  public void linkUIComponentToMe(VirtualComponent t) {
	  //System.out.println("linking textfield");
	  //if (t == jtf) {
	  if (t == spane) {
//		  	int numColumns = getObjectAdapter().getTextFieldLength();
//		  	if (numColumns != 0)
//		  	((VirtualTextArea) jtf).setColumns(numColumns);
//		  	else {
//		  		super.setSize(jtf);
//		  		
//		  	}
		  // set preferredsize screws up scrollpane
		  //setSizeOrColumns((VirtualTextArea) jtf);
		  	setSize(spane);
		  	setColors(spane);
		  	super.linkUIComponentToMe(jtf); 	
	  }
	  else {
		  setSizeOrColumns((VirtualTextArea) t);
		  //spane = ScrollPaneSelector.createScrollPane();
		  //spane.setScrolledComponent(t);
		  super.linkUIComponentToMe(t);
		  //super.linkUIComponentToMe(spane);
	  }
  	
  }
  
  protected void setText(String newVal) {
	  super.setText(newVal);
//	  if (spane != null) {
		 VirtualTextArea vta = (VirtualTextArea) jtf;		 
		 JTextArea jTextArea = (JTextArea) vta.getPhysicalComponent();
		 jTextArea.setCaretPosition(newVal.length());
//	  }

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



