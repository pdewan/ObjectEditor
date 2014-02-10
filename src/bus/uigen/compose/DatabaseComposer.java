package bus.uigen.compose;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import bus.uigen.*;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;

public class DatabaseComposer
{
	
	//these values below should really just be passed b/w methods so that we can make this generic?
	
	Vector components = null;
	Hashtable properties = null;
	JFrame queryFrame = null;
	JPanel propPanels = null;
	Vector passVector = null;
	
	Hashtable pNamesToVals = null; //would be the propname hashed to e.g  text box
	
	public DatabaseComposer(Vector _components) {  //collects all the necessary stuff for all													  	
		
		components = _components;
	}
	
	
	public void setUpQuery() {
		if (components != null) {
			
			queryFrame = new JFrame("Query");  //make frame
			properties = new Hashtable();
			
			
			for (int i=0; i < components.size(); i++)  {
				Enumeration propNames = IntrospectUtility.getAllPropertiesNames(components.elementAt(i));  //get this objects properties
				
				//hash each name to avoid duplicates
				while (propNames.hasMoreElements()) {
					Object aname = propNames.nextElement();
					properties.put(aname,aname);  
				}
				
			}//should have union of all properties P for all components in hashtable
			
			//ADD  property list to frame
			
			makeQueryPanel(properties);
			
		}
	}
	
	
	public void makeQueryPanel(Hashtable properties) {
		
		propPanels = new JPanel(new GridLayout(properties.size()+1, 1));
		Enumeration propNames = properties.elements();
		pNamesToVals = new Hashtable();
		
		while (propNames.hasMoreElements()) {
			JPanel propPanel = new JPanel(new FlowLayout());
			String name = ((String)propNames.nextElement());
			propPanel.add(new JLabel(name));
			//need to makes ure you can collect the value later...so hash name to t-fld.
			JTextField forVal = new JTextField(12);
			propPanel.add(forVal);
			pNamesToVals.put(name,forVal);
			propPanels.add(propPanel);
		}
		
		JButton queryButton = new JButton("Run Query");
		
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			doQuery();
			}}
									  );
		propPanels.add(queryButton);
		

		queryFrame.getContentPane().add(propPanels);
		
		
		queryFrame.show();
		queryFrame.pack();
		
	}//end meth
	
	
	public void doQuery() {
		System.out.println("DO QUERY CALLED");
		
		try {
			passVector = new Vector();
			for (int i=0; i < components.size(); i++)  {
				//for each component get methods and see if it implements property and 
				//check value if it matches nametoval hash values
				Enumeration propNames = IntrospectUtility.getAllPropertiesNames(components.elementAt(i));
				Hashtable propHash = new Hashtable();
				while (propNames.hasMoreElements()) {
					Object aname = propNames.nextElement();
					propHash.put(aname,new Boolean(true));
				}
				
				Enumeration valKeys = pNamesToVals.keys();
				boolean pass = true;
				
				while (valKeys.hasMoreElements()) {
					String aprop= (String)valKeys.nextElement();
					
					String queryVal = ((JTextField)(pNamesToVals.get(aprop))).getText();
					
					if (queryVal.length() > 0) {
						
						
						if (((Boolean)propHash.get(aprop)).booleanValue() == true)  {//if this is one of the query properties
							//get the type of this property
							//get the vals from txtfld and the component to see if they match
							
							
							MethodProxy getter = IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(components.elementAt(i)),aprop);
							ClassProxy returnType = getter.getReturnType();
							
							if ((returnType.equals(String.class)) ) {//if is a string then compar w/ .equals
								if ( !( ((String)getter.invoke(components.elementAt(i),null)).equals( queryVal )) )
									//check comparisons if get val! = txtfield val. then pass = false return
									pass = false;
								
							}//end if retyp
							
							
							//if it is any primitive type...and not string..we can do basic casting of txfield to returntype and == comparison.
							else 
								if (returnType.isPrimitive()) {
									
									if (returnType.getName().equals("int")) {
										if ( ((Integer)getter.invoke(components.elementAt(i),null)).intValue() != Integer.valueOf(queryVal).intValue() )
											pass = false;
									}
									
									else if (returnType.getName().equals("double")) {
										if ( ((Double)getter.invoke(components.elementAt(i),null)).doubleValue() != Double.valueOf(queryVal).doubleValue()  )
											pass = false;
									}
									
									else if (returnType.getName().equals("long")) {
										if ( ((Long)getter.invoke(components.elementAt(i),null)).longValue() != Long.valueOf(queryVal).longValue()  )
											pass = false;
									}

									else if (returnType.getName().equals("boolean")) {
										if ( ((Boolean)getter.invoke(components.elementAt(i),null)).booleanValue() != Boolean.getBoolean(queryVal) )
											pass = false;
									}

									else if (returnType.getName().equals("float")) {
										if ( ((Float)getter.invoke(components.elementAt(i),null)).floatValue() != Float.valueOf(queryVal).floatValue()  )
											pass = false;
									}
									
								}//end else	
							
							
						}//endif hash
						else {
							pass = false;
							return;
						}
						
					}//if >0
					
				}//endwhile
				
				if (pass) //after all run thrus of the object's properties
					passVector.addElement(components.elementAt(i));
				
			}//end for
			
			if (passVector.size() > 0)  {
				//	bus.uigen.ObjectEditor.editOverlayList(passVector);  //start the OE for these passed components
				OperationComposer oposer = new OperationComposer(passVector);
				//maybe make myTVs null here because the array already took care of it.
				//oposer.showValueTransfers();
				//oposer.showSharedCommands();
				oposer.showStackedComponents();
			}
			
			queryFrame.setVisible(false);
			queryFrame.dispose();
			
			
		}//end try
		
		
		catch (Exception ex)  {
			ex.printStackTrace();}
		
		
	}//end method
}//end class
