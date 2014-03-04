package bus.uigen.adapters;import java.awt.Color;import java.awt.Image;import java.awt.event.KeyEvent;import java.awt.event.KeyListener;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;import java.net.URI;import javax.swing.Icon;import javax.swing.ImageIcon;import javax.swing.SwingConstants;import util.misc.Common;import util.models.ALabelBeanModel;import util.models.LabelBeanModel;import bus.uigen.WidgetAdapter;import bus.uigen.attributes.AttributeNames;import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;import bus.uigen.view.WidgetShell;import bus.uigen.widgets.LabelSelector;import bus.uigen.widgets.ScrollPaneSelector;import bus.uigen.widgets.VirtualComponent;import bus.uigen.widgets.VirtualLabel;import bus.uigen.widgets.events.VirtualFocusEvent;import bus.uigen.widgets.events.VirtualFocusListener;//import shapes.LabelModel;public  class LabelAdapter extends WidgetAdapter 
implements  VirtualFocusListener, KeyListener, MouseListener {
  
//  String htmlLinkRegex = "((https?|http|ftp|file)\\://[:/?#\\[\\]@!%$&'()*+,;=a-zA-Z0-9._\\-~]+)";//  Pattern htmlLinkPattern = Pattern.compile(htmlLinkRegex);  URI htmlLink;  LabelBeanModel labelModel = new ALabelBeanModel(null, null);  public static int NUM_COLUMNS = 5;      boolean linked = false;  boolean sizeSet = false;    Integer width;  Integer height;
 
  public boolean isComponentAtomic() {  	return true;  }
  
  

  public String getType() {	  return LabelBeanModel.class.getName();
//    return "java.lang.String";
  }
      VirtualLabel label;
     void setText() {	  String text = labelModel.getText();	  if (text == null)		  return;	  htmlLink = Common.extractURI(text);      if (htmlLink != null) {    	 label.setToolTipText(htmlLink.toString()); // should integrate with tooltip attribute;      }            label.setText(text);	  		    }  void setIcon() {	  Icon icon = labelModel.getIcon();	  if (icon == null)		  return;	  if (sizeSet) {		  ImageIcon imageIcon = (ImageIcon) icon;		  Image image = imageIcon.getImage();		  		  if (width == null)			  width = imageIcon.getIconWidth();		  if (height == null)			  height = imageIcon.getIconHeight();		  Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);		  icon = new ImageIcon(scaledImage);	  }	  label.setIcon(icon);  }    public void setUIComponentTypedValue(Object newVal) {	    try {	    	//	    	if (firstFocus)//	    		return;	    			  //System.out.println("Setting Text Component Adapter" + newval);	      //jtf = (JTextComponent) getUIComponent();	  //	    	if (newval.equals(labelModel))		    if (labelModel.equals(newVal))	    		return;		      label = (VirtualLabel) getUIComponent();		    if (newVal instanceof String)		    	label.setText((String) newVal);		    else {	    	LabelBeanModel newLabelModel = (LabelBeanModel) newVal;//	      label = (VirtualLabel) getUIComponent();		  //System.out.println(jtf.getColumns() + " " + tf.getColumns());		  //if (tf != jtf) System.out.println("setUI: not the same tf");		  //int length = ((String) newval).length();		      if (newVal == null) {	    	  labelModel.set(null, null);	      } else {	    	  labelModel.set(newLabelModel.getText(), newLabelModel.getIcon());//	    	  labelModel.setIcon(newLabelModel.getIcon());	      }	      setText();	      setIcon();		    }////		  text = (String) newval;//	      htmlLink = Common.extractURI(labelModel.getText());//	      if (htmlLink != null) {////	    	  System.out.println(htmlLink.getRawPath());////	    	  System.out.println(htmlLink);//	    	 label.setToolTipText(htmlLink.toString()); // should integrate with tooltip attribute;//	      }//	      }//	    	  //		  /*//		  if (notInRange(newval))//			  jtf.setText("");//		  else//		  *///	      label.setText((String) newval);//	      label.addMouseListener(this);		  //tf.setColumns(NUM_COLUMNS);		  //tf.validate();		  //tf.setColumns(NUM_COLUMNS);		  //System.out.println((String) newval);		  //if (!isEditable)	      if (getObjectAdapter().isUnEditable())			  this.setUIComponentUneditable();	    } catch (ClassCastException e) {	    	e.printStackTrace();	    }	  }
 
//  public void oldSetUIComponentTypedValue(Object newval) {
//    try {////    	if (firstFocus)////    		return;//    	//	  //System.out.println("Setting Text Component Adapter" + newval);
//      //jtf = (JTextComponent) getUIComponent();//    	if (newval.equals(text))//    		return;//      label = (VirtualLabel) getUIComponent();
//	  //System.out.println(jtf.getColumns() + " " + tf.getColumns());//	  //if (tf != jtf) System.out.println("setUI: not the same tf");
//	  //int length = ((String) newval).length();	//      if (newval == null)//    	  text = "";//      else
//	  text = (String) newval;//      htmlLink = Common.extractURI(text);//      if (htmlLink != null) {////    	  System.out.println(htmlLink.getRawPath());////    	  System.out.println(htmlLink);//    	 label.setToolTipText(htmlLink.toString()); // should integrate with tooltip attribute;//      }//    	  //	  /*//	  if (notInRange(newval))//		  jtf.setText("");//	  else//	  */
//      label.setText((String) newval);//      label.addMouseListener(this);//	  //tf.setColumns(NUM_COLUMNS);
//	  //tf.validate();//	  //tf.setColumns(NUM_COLUMNS);//	  //System.out.println((String) newval);//	  //if (!isEditable)//      if (getObjectAdapter().isUnEditable())//		  this.setUIComponentUneditable();
//    } catch (ClassCastException e) {//    	e.printStackTrace();
//    }
//  }
 
  public Object getUIComponentValue() {
    String val = new String("");
    try {
     // jtf = (JTextField) getUIComponent();
      val = label.getText();
    } catch (ClassCastException e) {
    }
    return val;
  }  //  public void setPreWrite() {
//	//  }  //
//  
//  public void setUIComponentEditable() {
//  }
//  
//  public void setUIComponentUneditable() {//	  
//  }
 
  public void setUIComponentSelected() {	super.setUIComponentSelected();
    getUIComponent().setBackground((Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR));
    getUIComponent().repaint();
  }
  public void setUIComponentDeselected() {	super.setUIComponentDeselected();		getUIComponent().setBackground(readOnlyColor);
    getUIComponent().repaint();
  }  Color readOnlyColor;  Color editableColor;
  public void linkUIComponentToMe() {	  //System.out.println("linking textfield");
    //if (t instanceof JTextComponent) {
     //jtf = (JTextComponent) t;     //jtf = (VirtualTextComponent) t;
	  /*	  if (jtf == null) {		  jtf = tf;		  jtf.setColumns(NUM_COLUMNS);	  }	  */	  if (linked) return;	  //prompt = adapter.getPrompt();	              Color overriddenColor = adapter.getComponentBackground();      if (overriddenColor != null) {    	  readOnlyColor = overriddenColor;      } else {                 readOnlyColor  = (Color) label.getBackground();            }	  label.addKeyListener(this);	  
      label.addFocusListener(this);       label.addMouseListener(this);      linked = true;      if (spane != null) {    	  setSize(spane);		  	setColors(spane);      } else      setAttributes(label);      
  }  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {	  	  if (label != null) return label;	  label = LabelSelector.createLabel("");	  label.setName("Component " + adapter + " LabelAdapter.instantiateComponent");	  linked = false;	  instantiatedComponent = true;//	  label.setVerticalAlignment(SwingConstants.CENTER);	  if (adapter.isScrolled()) {		  spane = ScrollPaneSelector.createScrollPane();		  		  spane.setScrolledComponent(label);		  //spane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		  return spane;	  }  	  return label;        }  public VirtualComponent getUIComponent() {	  return label;  }  public void linkUIComponentToMe(VirtualComponent t) {	  //System.out.println("linking textfield");    //if (t instanceof JTextComponent) {     //jtf = (JTextComponent) t;	  //if (jtf == t) return;	  if (t != spane)     label = (VirtualLabel) t;     linkUIComponentToMe();    //}  }
  WidgetShell genericWidget;
//  public boolean uiComponentValueChanged() {
//	  boolean retVal;//	  if (textChanged()) {//		retVal = super.uiComponentValueChanged();//		if (retVal)
//		refreshText();//		else {//			Message.userMessage("Cannot commit edited value. Restoring original value");//			restoreText();//		}//		return retVal;//	  } else {//		  uiComponentValueEdited(false);//		  return false;//	  }	//	
//  }
//  public void uiComponentValueEdited() {
//	  //	  //if (textChanged())//		super.uiComponentValueEdited(true);	//	
//  }//  void refreshText() {
////	  text = label.getText();//	  labelModel.setText(label.getText());//  }  void restoreText() {	  label.setText(labelModel.getText());  }
  boolean textChanged() {
	  if (labelModel.getText() == null) return true;	  	  return !labelModel.getText().equals(label.getText());	  
  }//  public void textValueChanged(TextEvent evt) {//	    if (!actionMode) {//	      uiComponentValueChanged();//	    }//	  }
  
//  // DocumentListener interface
//  public void changedUpdate(DocumentEvent e) {
//    if (!actionMode)
//      uiComponentValueChanged();
//	/*//	else//		uiComponentValueEdited();//	*/
//  }//  boolean firstTime = true;
//  public void insertUpdate(DocumentEvent e) {//	  if (!actionMode)
//      uiComponentValueChanged();
//	  /*//	  else if (!firstTime)//		  uiComponentValueEdited();//	  firstTime = false;//	  */
//  }
//
//  public void removeUpdate(DocumentEvent e) {
//    //	  if (!actionMode)
//      uiComponentValueChanged();
//	  /*//	  else//		  uiComponentValueEdited();//	  */
//  }
//  boolean actionPerformed = false;
//  public void actionPerformed(ActionEvent e) {
//    if (actionMode) {//		actionPerformed = true;//		keyTyped = false;
//       uiComponentValueChanged();
//    } 
//  }

//  public boolean processAttribute(Attribute attrib) {//  	if (attrib.getName().equals("actionMode") ||//    		attrib.getName().equals(AttributeNames.INCREMENTAL)) {
//      if (attrib.getValue() instanceof Boolean) {
//	actionMode = ((Boolean) attrib.getValue()).booleanValue();
//      }
//      return true;
//    }
//    else
//      return super.processAttribute(attrib);
//  }
  boolean firstFocus = true;
  // Methods invoked on focus gain/loss in the view
  public void focusGained(VirtualFocusEvent e) {	  
      uiComponentFocusGained(e);
    	
  }

  public void focusLost(VirtualFocusEvent e) {	  //System.out.println("focus lost");
    
      uiComponentFocusLost();
      //awtComponent.setBackground(Color.white);
    
  }  public void keyReleased(KeyEvent k) {  }  public void keyPressed(KeyEvent k) {  }  boolean keyTyped = false;  public void keyTyped (KeyEvent k) { // received after action performed event, key = \n	    }@Overridepublic void mouseClicked(MouseEvent e) {//	System.out.println("Mouse clicked"); 	// do this in a thread//	if (htmlLink != null) {//		Common.browse(htmlLink);//	}}@Overridepublic void mousePressed(MouseEvent e) {//	System.out.println("Mouse pressed");}@Overridepublic void mouseReleased(MouseEvent e) {	if (htmlLink != null) {		Common.browse(htmlLink);		}}@Overridepublic void mouseEntered(MouseEvent e) {	// TODO Auto-generated method stub	}@Overridepublic void mouseExited(MouseEvent e) {	// TODO Auto-generated method stub	}public void setSize(VirtualComponent component) {	super.setSize(component);}// maybe -1 only  for text labelspublic int defaultHeight() {	return -1;}public int defaultWidth() {	return -1;}// use super class setSize//public void setSize(VirtualComponent component) {//	if (!instantiatedComponent)//		return;////	if (labelModel.getIcon() == null) {////		 super.setSize(component); //  let text have default sizes////		 return;////	}//	width = adapter.getComponentWidth();//	height = adapter.getComponentHeight();		//	sizeSet = width != null ||height != null;//	// we will adjust size of image and the widget will adjust size////	if (!sizeSet) // use image size////		return;//	//	//}  
}



