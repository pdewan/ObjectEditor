package bus.uigen.compose;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeManager;import bus.uigen.attributes.AttributeNames;import bus.uigen.introspect.MethodDescriptorProxy;import bus.uigen.reflect.MethodProxy;import bus.uigen.widgets.VirtualButton;import bus.uigen.widgets.VirtualComponent;import bus.uigen.widgets.VirtualLabel;import java.awt.event.*;import java.lang.reflect.*;import java.beans.*;import javax.swing.Icon;import javax.swing.ImageIcon;import javax.swing.JButton;import javax.swing.JComponent;import javax.swing.JLabel;//newadds
//this just holds a method , display name, and object.
public class Command implements ActionListener {	
  public String displayName= null;  public String displayIcon = null;
  public MethodProxy method= null;  public Object targetObject= null;  public uiFrame frame= null;  public MethodDescriptorProxy md = null;  //public JButton button = null;  public VirtualButton button = null;

  //changed to see if you don't need the frame ..orignally added it to support uimethinvmgr invoker below  //but found that it is overloaded so differet constr. set.
  
  //dunno why but this seems to need the uiFrame to work properly with method 
  //invocations... maybe in future see if things would work out if I just called the invokeMethod form UImim  public Command(uiFrame _frame, Object _target, MethodDescriptorProxy _md, MethodProxy m) {	frame = _frame;	targetObject = _target;
    method = m;	md = _md;	if (md != null){		displayName = (String) md.getValue(AttributeNames.LABEL);		displayIcon = (String) md.getValue(AttributeNames.ICON);		if (displayName == null)
    displayName = md.getDisplayName().toUpperCase().replaceAll("OR","/").replaceAll("_","").trim();  //think I need to add this since I updated the thing.;	//displayName = md.getDisplayName();
	}	else displayName = m.getName();
	  }
    public String getDisplayIcon() {	  return displayIcon;  }
  public void setDisplayName(String _displayName) {
	  displayName = _displayName;
  }  
  public void setTargetObject(Object newTarget) {  //can get away with this if the target and source have the 													//class becasue the methods are exactly the same  													//however, need a new method for the target if they differ in class
	  targetObject = newTarget; 
  }    public void setButton(VirtualButton bttn) {	  button = bttn;
	  //displayName = bttn.getLabel();	  displayName = bttn.getText();  }  public VirtualButton getButton() {	  return button;  }
    
  public void setTargetObject(Object newTarget, MethodProxy m) {  //hence the overriding of this method
	  method = m;
	  targetObject = newTarget; 
  }  
    public Object getTargetObject() {
	  return targetObject; 
  }
    public uiFrame getTopFrame() {return frame;}
    /*  public Command(Object _target, String label, Method m) {	  targetObject = _target;
					method = m;
    displayName = label;
  }  */  public MethodProxy getMethod() {	  return method;  }
  
  public void actionPerformed(ActionEvent e) {
    //Object object = frame.getAdapter().getRealObject();	//Object object = frame.toUIGen;
    if (method != null) {
      bus.uigen.controller.MethodInvocationManager iman = new bus.uigen.controller.MethodInvocationManager(frame, targetObject,
									     method);	//uiMethodInvocationManager iman = new uiMethodInvocationManager(targetObject, method);									 
    //frame.doImplicitRefresh();    }
  }
  
  public String toString() {
	  return displayName;
  }
  
  public MethodDescriptorProxy getMD() {
	  return md;
  }    public int getPosition () {   	Object pos = AttributeManager.getInheritedAttribute (frame, md, AttributeNames.POSITION, null);   	//if (row == null) return row = AttributeNames.getDefault();  	return ((Integer) pos).intValue();  }  public Object getAddConstraint () {   	Object retVal = AttributeManager.getInheritedAttribute (frame, md, AttributeNames.ADD_CONSTRAINT, null);   	//if (row == null) return row = AttributeNames.getDefault();  	return retVal;  }     public int getRow () {   	Object row = AttributeManager.getInheritedAttribute (frame, md, AttributeNames.ROW, null).getValue();   	if (row == null) return -1;   	//if (row == null) return row = AttributeNames.getDefault();  	return ((Integer) row).intValue();  }   public int getColumn () {   	Object col = AttributeManager.getInheritedAttribute (frame, md, AttributeNames.COLUMN, null).getValue();   	//if (col == null) return -1;  	return ((Integer) col).intValue();  }   public String getLabelAbove () {   	return (String) AttributeManager.getInheritedAttribute (frame, md, AttributeNames.LABEL_ABOVE, null).getValue();  }   public String getLabelBelow() {   	return (String) AttributeManager.getInheritedAttribute (frame, md, AttributeNames.LABEL_BELOW, null).getValue();  }   public String getLabelLeft() {   	return (String) AttributeManager.getInheritedAttribute (frame, md, AttributeNames.LABEL_LEFT, null).getValue();  }   public String getLabelRight() {   	return (String) AttributeManager.getInheritedAttribute (frame, md, AttributeNames.LABEL_RIGHT, null).getValue();  }   public static boolean isFileName (String string) {		return string.endsWith(".gif");	}   public static void maybeChangeLabel (MethodDescriptor md, VirtualButton b) {		if (md == null) return;			//MethodDescriptor md = cdIncDec.getMethodDescriptor(incrementer.getName());			String iconFile = (String) md.getValue(AttributeNames.ICON);			String label = (String) md.getValue(AttributeNames.LABEL);			maybeChangeLabelAndIcon (b, label, iconFile);			/*			if (iconFile == null && label!= null && isFileName(label))				iconFile = label;			if (iconFile != null) {				 Icon icon = new ImageIcon(iconFile);				 b.setIcon(icon);				 b.setLabel("");				 //return;			}			if (label != null)				b.setLabel(label);				*/					}	public static void maybeChangeLabelAndIcon(VirtualButton b, String label,			String iconFile) {		if (iconFile == null && label != null && isFileName(label))			iconFile = label;		if (iconFile != null) {			Icon icon = new ImageIcon(iconFile);			b.setIcon(icon);			//b.setLabel("");			b.setText("");			// return;		}		if (label != null)			//b.setLabel(label);			b.setText(label);	}	public static void maybeChangeLabelOrIcon(VirtualButton b, String label,			String iconFile) {		if (iconFile == null && label != null && isFileName(label))			iconFile = label;		if (iconFile != null) {			Icon icon = new ImageIcon(iconFile);			b.setIcon(icon);			//b.setLabel("");			b.setText("");			return;		}		if (label != null)			//b.setLabel(label);			b.setText(label);	}		public static void maybeChangeLabelOrIcon(JLabel b, String label,			String iconFile) {		if (iconFile == null && label != null && isFileName(label))			iconFile = label;		if (iconFile != null) {			Icon icon = new ImageIcon(iconFile);			b.setIcon(icon);			b.setText("");			return;		}		if (label != null)			b.setText(label);	}	public static void maybeChangeLabelOrIcon(VirtualLabel b, String label,			String iconFile) {		if (iconFile == null && label != null && isFileName(label))			iconFile = label;		if (iconFile != null) {			Icon icon = new ImageIcon(iconFile);			b.setIcon(icon);			b.setText("");			return;		}		if (label != null)			b.setText(label);	}		  
}
