package bus.uigen.compose;

import java.util.Hashtable;

import bus.uigen.uiFrame;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.SetGetLastCommand;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComboBox;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;
import bus.uigen.widgets.swing.SwingButton;
import bus.uigen.widgets.swing.SwingButtonFactory;

public class TxfrJButton /* extends bus.uigen.widgets.swing.SwingButton extends JButton*/ implements /*VirtualButton,*/ VirtualActionListener {  //need a special Jbutton that stores its index so that we
	//can find the JCombo box to match it  to get the object to get the prop. value from
	//will be handling its own actions
	
	//JComboBox matchCombo = null;  //the Jcombo box that it is matched with
	VirtualComboBox matchCombo = null;  //the Jcombo box that it is matched with
	Object toSet = null;
	String propertyName = "";
	uiFrame oeFrame = null;
	Hashtable cleanNameToObj = null;
	SwingButton button;
	
	//public TxfrJButton(JComboBox _myMatch, Object _toSet, String propName, uiFrame _oeFrame /*frame needed to send to uiMIM*/) {
	public TxfrJButton(VirtualComboBox _myMatch, Object _toSet, String propName, uiFrame _oeFrame /*frame needed to send to uiMIM*/) {
		
		//super("Transfer");
		button =  SwingButtonFactory.createJButton("Transfer");
		//super.init(button.getAWTComponent());
		oeFrame = _oeFrame;
		matchCombo = _myMatch;
		toSet = _toSet;
		propertyName = propName; 

		button.addActionListener(this);
		
	}


	//public TxfrJButton(JComboBox _myMatch, Hashtable _cleanNameToObj, Object _toSet, String propName, uiFrame _oeFrame /*frame needed to send to uiMIM*/) {
	public TxfrJButton(VirtualComboBox _myMatch, Hashtable _cleanNameToObj, Object _toSet, String propName, uiFrame _oeFrame /*frame needed to send to uiMIM*/) {
		button =  SwingButtonFactory.createJButton("Transfer");
		//super("Transfer");
		cleanNameToObj = _cleanNameToObj;
		oeFrame = _oeFrame;
		matchCombo = _myMatch;
		toSet = _toSet;
		propertyName = propName; 

		button.addActionListener(this);
		
	}

	
public void actionPerformed(VirtualActionEvent e) {
		
		try {			String selectedStr = (String)matchCombo.getSelectedItem(); //get the selected object to get the property value from			
						Object selected = cleanNameToObj.get(selectedStr);			
			MethodProxy getter = IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(selected), propertyName);
			MethodProxy setter = IntrospectUtility.getSetterMethod(ACompositeLoggable.getTargetClass(toSet), propertyName);
			MethodProxy myGetter = IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(toSet), propertyName);
						//DO TYPE CHECKING HERE. to make sure get returns same type as set's parameter			
			//for now i'll just get the getter method of the property and get its type.
			//to get the properties type...call getobject and get its type
			ClassProxy[] params = setter.getParameterTypes();
			ClassProxy setype = params[0];
			ClassProxy getype = getter.getReturnType();
						
			if (!getype.toString().equals(setype.toString())) {  //if they aren't the same type then go
																			
				button.setEnabled(false);  //disable the buttn and return from the method
																
				return;  //break because we don't expect the source to have two diff properties that share a name
			}
						
			Object[] toSetVal = new Object[1];
			toSetVal[0] = getter.invoke(selected,null);  //get the value to set it to.
			if (toSetVal[0] != null) {  //invoke the setter here using oe original invocation method to ensure proper updates				System.out.println("trying to invokemethod");
				//uiMethodInvocationManager iman = new uiMethodInvocationManager(oeFrame,toSet,setter,toSetVal);
				oeFrame.getUndoer().execute (
						new SetGetLastCommand(oeFrame.getAdapter(), 
							setter, 
							toSet,
							toSetVal,
							myGetter							
							));

				System.out.println("just invoked "+ setter.getName() + " to " + toSetVal[0].toString());				oeFrame.doImplicitRefresh();
			}
		}
		catch (Exception ex) {ex.printStackTrace();}	}

	public VirtualButton getButton() {
		return button;
	}
		
	
	
	/*	
	public int index;

	public void setIndex(int dex) {
	index = dex;
	}
	*/
	
	/*	NONDEMO public void actionPerformed(ActionEvent e) {
		
		try {			Object selected = matchCombo.getSelectedItem(); //get the selected object to get the property value from			
						
			Method getter = uiBean.getGetterMethod(selected.getClass(), propertyName);
			Method setter = uiBean.getSetterMethod(toSet.getClass(), propertyName);
						//DO TYPE CHECKING HERE. to make sure get returns same type as set's parameter			
			//for now i'll just get the getter method of the property and get its type.
			//to get the properties type...call getobject and get its type
			Class[] params = setter.getParameterTypes();
			Class setype = params[0];
			Class getype = getter.getReturnType();
						
			if (!getype.toString().equals(setype.toString())) {  //if they aren't the same type then go
																			
				this.setEnabled(false);  //disable the buttn and return from the method
																
				return;  //break because we don't expect the source to have two diff properties that share a name
			}
						
			Object[] toSetVal = new Object[1];
			toSetVal[0] = getter.invoke(selected,null);  //get the value to set it to.
			if (toSetVal[0] != null) {  //invoke the setter here using oe original invocation method to ensure proper updates				System.out.println("trying to invokemethod");
				uiMethodInvocationManager iman = new uiMethodInvocationManager(oeFrame,toSet,setter,toSetVal);

				System.out.println("just invoked "+ setter.getName() + " to " + toSetVal[0].toString());				
			}
		}
		catch (Exception ex) {ex.printStackTrace();}	}
		*/
		
} 	
