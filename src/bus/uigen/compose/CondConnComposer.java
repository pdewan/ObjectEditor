package bus.uigen.compose;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.*;
import java.beans.*;


import bus.uigen.*;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.view.WidgetShell;


public class CondConnComposer extends OperationComposer  implements PropertyChangeListener, ActionListener
{
	
	//Hashtable stateWidgets = null;  //holds  all objects state widgets/hash obj. to get a vector of its widgets
	//Hashtable commandsHash = null; //holds all the Commands for each object
	//	Hashtable oeFrames = new Hashtable(); //holds the uiFrames for each object
	//Object[] objects = null;
	//Hashtable nameToObj = null;
	Hashtable objPropCond = new Hashtable();
	Hashtable objMeth = new Hashtable();
	JButton connect = null;
	
	Hashtable conditions = null;
	Vector truecommands = null;
	//uiFrame oeFrame = null;
	public CondConnComposer(Vector _components, Vector names) {
		super (_components, names);
		
	}
	public CondConnComposer(Vector _components) {
		super (_components);
		
	}

	public void init (Vector _components, Vector names) {
		super.init (_components, names);
		System.out.println("making condconn");
		if ((_components != null) && (_components.size() > 0)) {
			System.err.println("making condconn");
			//nameToObj = new Hashtable();
			for (int i = 0; i < objects.length; i++) {//copy vector contents to it.
				objects[i] = (Object)_components.elementAt(i);
				//need to store hashtable too
				//nameToObj.put(objects[i].toString(), objects[i]);
				ObjectAdapter.maybeAddPropertyChangeListener(objects[i], this);
					
				//((PropertyFirer)objects[i]).addPropertyChangeListener(this);
			}
			/*
			bus.uigen.uiGenerator.resetAllGUIComponents();;  //plus it is needed the 1st time to instantiate the collector vars of generation process.
			objects = new Object[_components.size()]; //make the array that everything else uses
			nameToObj = new Hashtable(); 
			
			for (int i = 0; i < objects.length; i++) {//copy vector contents to it.
				objects[i] = (Object)_components.elementAt(i);
				//need to store hashtable too
				nameToObj.put(objects[i].toString(), objects[i]);
				uiObjectAdapter.maybeAddPropertyChangeListener(objects[i], this);
					
				//((PropertyFirer)objects[i]).addPropertyChangeListener(this);
			}
			System.out.println("abougtoeditedolis");
			oeFrame = bus.uigen.ObjectEditor.editOverlayList(_components);
			//oeFrame.setVisible(false);
			
			
			stateWidgets = bus.uigen.uiGenerator.getAllWidgets();  //get widgets
			commandsHash = bus.uigen.uiGenerator.getAllCommands(); //get methods
			//System.out.println("Hashed commands " + commandsHash.size());
			//System.out.println("Hashed widgets " + stateWidgets.size());
			oeFrame = bus.uigen.uiGenerator.getTopFrame();
			//oeFrame.hide();
			 
			 */
			//objPropCond = new Hashtable();
			//objMeth = new Hashtable();
		}
	}
	public  void resetAllGUIComponents() {
		bus.uigen.uiGenerator.resetAllGUIComponents(); 
	}
	Hashtable widgetToPropertyName = new Hashtable();
	Hashtable<String, MethodProxy> labelToMethod = new Hashtable();
	public void makeGUI() {
		JFrame condFrame = new JFrame("Conditional Connect");
		condFrame.getContentPane().setLayout(new GridLayout(1,2));

		
		JPanel props = new JPanel(new GridLayout(objects.length, 1));
		JPanel meths = new JPanel(new GridLayout(objects.length + 1, 1));
		
		
		for (int i = 0; i < objects.length; i++) {
			Vector commands = (Vector)commandsHash.get(objects[i]);
			Vector widgets = (Vector)stateWidgets.get(objects[i]);
			
			Hashtable propWidget = new Hashtable();  //stores this object prop names and their widgets
			
			JPanel objectPropPanel = new JPanel();
			
			objectPropPanel.setLayout( new GridLayout(widgets.size(), 1));
			
			//objectPropPanel.setBorder(BorderFactory.createTitledBorder(objects[i].toString()));
			objectPropPanel.setBorder(BorderFactory.createTitledBorder((String) objToCleanName.get(objects[i])));
			
			System.err.println("has " + widgets.size());
			sort(widgets);
			
			for (int j = 0; j < widgets.size(); j++)  {//dump widgets to state panel
				JPanel pairPanel = new JPanel();
				
				JLabel thelabel = new JLabel(((WidgetShell)widgets.elementAt(j)).getLabel().toString());
				//pairPanel.add(  (((uiGenericWidget)widgets.elementAt(i)).getComponent()).getClass().newInstance());  
				//make a new class for the component because we aren't trying to change the value...just
				//reuse what OE already found out about its representative widget
				
				//for now I'll use a textbox instead of what OE designates for it...because of compleixty in getting the value later
				JTextField jtext = new JTextField(8);
				
				pairPanel.add(thelabel);  //try to just get the property name
				pairPanel.add(jtext);
				objectPropPanel.add(pairPanel);
				String propName = ((WidgetShell)widgets.elementAt(j)).getObjectAdapter().getPropertyName();
				
				//propWidget.put(thelabel, jtext);
				propWidget.put(propName.toLowerCase(),jtext);
								
				objPropCond.put(objects[i], propWidget);
				objectPropPanel.add(pairPanel);
				
			}
			JScrollPane objectPropPanelScrollPane = new JScrollPane(objectPropPanel);
			//objectPropPanelScrollPane.add (objectPropPanel);
			if (widgets.size() > 0)
				//props.add(objectPropPanel);
				props.add(objectPropPanelScrollPane);
			
			Hashtable cmdCheck= new Hashtable();
			
		
			JPanel objectMethPanel = new JPanel();
			
			objectMethPanel.setLayout(new GridLayout(commands.size(), 1));
			objectMethPanel.setBorder(BorderFactory.createTitledBorder((String) objToCleanName.get(objects[i])));
			sort(commands);
			for (int j = 0; j < commands.size(); j++)  {//dump widgets to state panel
				JPanel pairPanel = new JPanel();
				String label = ((ButtonCommand)commands.elementAt(j)).displayName;
				//JLabel commandName = new JLabel(((Command)commands.elementAt(j)).displayName);
				JLabel commandName = new JLabel(label);
				labelToMethod.put(objects[i].toString()+label, ((ButtonCommand)commands.elementAt(j)).getMethod());
				JCheckBox check = new JCheckBox();
				pairPanel.add(commandName);
				pairPanel.add(check);
				objectMethPanel.add(pairPanel);
				cmdCheck.put(commandName,check);
				objMeth.put(objects[i], cmdCheck);
				objectMethPanel.add(pairPanel);
				
			}
			JScrollPane objectMethPanelScrollPane = new JScrollPane(objectMethPanel);
			//objectMethPanelScrollPane.add ();	
			if (commands.size() > 0)
				//meths.add(objectMethPanel);
				meths.add(objectMethPanelScrollPane);
			
			
			
		}//end for each object
		
		connect = new JButton("Connect");
		connect.addActionListener(this);
		
		JPanel cnctPane = new JPanel();	
		cnctPane.add(connect);		
		meths.add(cnctPane);
		
		props.setBorder(BorderFactory.createTitledBorder("Select Condition(s)"));	
		meths.setBorder(BorderFactory.createTitledBorder("Select Event(s)"));
		
		
		condFrame.getContentPane().add(props);
		condFrame.getContentPane().add(meths);
		condFrame.show();
		condFrame.pack();
			
		
	}//end method
	
	public void actionPerformed(ActionEvent e) {		//	System.out.println(e.getActionCommand() + " " +  objects.length );
//	System.out.println(e.getSource().toString());
						if (e.getActionCommand().equals("Connect")) {  //collect all the connections 
				
				conditions = new Hashtable();				truecommands = new Vector();
								
				for (int i = 0; i < objects.length; i++) {					//String objName = objects[i].toString();
					String objName = (String) objToCleanName.get(objects[i]);
					String truecmd = null;					Hashtable cmdWidget = (Hashtable)objMeth.get(objects[i]);					Enumeration keys = cmdWidget.keys();					while (keys.hasMoreElements()) {
						JLabel lab = (JLabel)keys.nextElement();
						truecmd = objName+ ":::"+ lab.getText();
						JCheckBox ck = (JCheckBox)cmdWidget.get(lab);
						if (ck.isSelected()) {
							
							truecommands.addElement(truecmd);
							System.err.println("***********"+truecmd);
						}
					}
				}//endfor
//i'm hashing the string nameof the object+propertyname+value to check
					//the vector for commands to invoke is the object+:::+methodname												for (int i = 0; i < objects.length; i++) {
					String tohash = null;					//String objName = objects[i].toString();
					String objName = (String) objToCleanName.get(objects[i]);
					Hashtable pWidget = (Hashtable)objPropCond.get(objects[i]);					Enumeration keys = pWidget.keys();					while (keys.hasMoreElements()) {
						//again; we're assuming textfield and strings
						//JLabel lab = (JLabel)keys.nextElement();
						String propName = (String) keys.nextElement(); 
						tohash =  objName + propName;
						//tohash = objName+lab.getText().toLowerCase();
						//JTextField txt = (JTextField)pWidget.get(lab);
						JTextField txt = (JTextField)pWidget.get(propName);
						if (txt.getText().length() > 0) {  //if there was anything in there
													     //then its a condition
							
							tohash = tohash+txt.getText();
							System.out.println("***********"+tohash);
							conditions.put(tohash, new Boolean(true));  //store it in a hash
							
						}
						
					}//end while
				}//end for
						
				
							}//end if	}//end method
		
	public void propertyChange(PropertyChangeEvent evt) {
		//String condition = evt.getSource().toString() +  evt.getPropertyName().toLowerCase() + evt.getNewValue();
		String condition = (String) objToCleanName.get(evt.getSource()) +  evt.getPropertyName().toLowerCase() + evt.getNewValue();
				try {			if (conditions !=null && conditions.get(condition) != null) {				System.err.println("condition met" + truecommands.size() );				for (int x = 0; x < truecommands.size(); x++) {					
					String cmdStr =  (String)truecommands.elementAt(x);
					System.err.println("__ " + cmdStr);					String objName = cmdStr.substring(0, cmdStr.indexOf(":::") );					String cmdName = cmdStr.substring(cmdStr.indexOf(":::")+ 3);					//Object theobj = nameToObj.get(objName);					Object theobj = cleanNameToObj.get(objName);
					System.err.println("__ " + objName);
					//stem.out.println("__ " + cmdName);
					if (theobj != null) {
						
						while (cmdName.indexOf(" ") >= 0) 							cmdName = cmdName.substring(0,cmdName.indexOf(" ")) + cmdName.substring(cmdName.indexOf(" ")+1);
						
					System.out.println("__ " + cmdName);						MethodProxy tocall =  labelToMethod.get(theobj.toString() + cmdName);		
											//Method tocall = uiBean.getMethod(theobj,cmdName);
					
										tocall.invoke(theobj, null);
					}
					else {						System.out.println("warrior");
						System.exit(0);
					}					//currently only supporting non parameter methods
				}//end for
			}//end conditions			
		}		catch (Exception er) {er.printStackTrace();}
				System.out.println("prop. changed " + evt.getSource().toString() + " " + evt.getPropertyName()+ " " + evt.getNewValue()); 
  }

	public static void sort (List objectList) {
		Hashtable stringToObject = new Hashtable();
		Vector strings = new Vector();
		for (int i = 0; i < objectList.size(); i++) {
			String string = objectList.get(i).toString();
			strings.addElement(string);
			stringToObject.put(string, objectList.get(i));
		}
		Collections.sort(strings);
		for (int i = 0; i < strings.size(); i++) {
			objectList.set(i, stringToObject.get(strings.elementAt(i)));
		}
				
	}
  	
}//end class
				
				
					

