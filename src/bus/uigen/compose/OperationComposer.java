package bus.uigen.compose;

import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*; 
import java.util.*;

import java.lang.reflect.*;

import javax.swing.*;

import util.models.Hashcodetable;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.beans.*;

import bus.uigen.*;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.widgets.FrameSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualFrame;


public class OperationComposer
{
	
	
	uiFrame oeFrame = null;
	Hashtable stateWidgets = null;  //holds an all objects state widgets/hash obj. to get a vector of its widgets
	Hashtable primAdapters = null;  //holds an all objects primitive Adapters/hash obj. to get a vector of its adapter
	Hashcodetable commandsHash = null; //holds all the Commands for each object
	//	Hashtable oeFrames = new Hashtable(); //holds the uiFrames for each object
	Object[] objects = null;
	Vector sharedInvokeButtons = null;
	Vector stackPanels = null;
	VirtualFrame stackedFrame = null;
	Hashtable singlePanels = null;
	Hashtable singleFrames = null;
	Hashtable classToObj = null; //wanna be able to map a class to an object of that type for retargeting
	//saves time if you can avoid checking stuff if different
	
	JFrame sharedFrame = null;
	JFrame transfersFrame = null;
	
	Hashcodetable objsMethStr= null;
	Hashtable objsPropStr = null;
	Hashtable objsTypeCount = null;
	
	Hashtable sharedMeths = null;
	Hashtable sharedProps = null;
	

	Vector unsharedMeths = null;
	Vector unsharedProps = null;
	
	static Hashtable retargetCache = null;
	
	JFrame us = null;
	JCheckBox[] ckboxz = null;
	JFrame usB = null;
	Hashtable pantobtns = null;
	Hashtable pantoSbtns = null;
	Hashtable commandsToObj = null;
	
	Hashtable cleanNameToObj = null;
	Hashtable objToCleanName = null;
	
	static Vector initExcludeMethods = (Vector)AClassDescriptor.excludeMethods.clone(); //store initial exclude methods as a clone
	public void init (Vector _components, Vector names) {
		if ((_components != null) && (_components.size() > 0)) {

			//have the system reset the GUI components automatically b/c it may still
			//have the old junk from a previous call.
			retargetCache = new Hashtable();
			// doing it in edit overlay list anyway
			//resetAllGUIComponents();  //plus it is needed the 1st time to instantiate the collector vars of generation process.
			
			objects = new Object[_components.size()]; //make the array that everything else uses
			String devName = "";
			String[] devSplit = null;
			cleanNameToObj = new Hashtable();
			objToCleanName = new Hashtable();
			for (int i = 0; i < objects.length; i++)  { //copy vector contents to it.
				objects[i] = (Object)_components.elementAt(i);
				if (names != null && names.size() == _components.size())
					devName = (String) names.elementAt(i);
				else {
				
				 devName = objects[i].toString().replace('_',' ');
				devSplit = devName.split("@");
				devName = devSplit[0];
				}
				cleanNameToObj.put(devName, objects[i]);
				objToCleanName.put (objects[i], devName);
			}
			
			
			bus.uigen.ObjectEditor.editOverlayList(_components, false);
			
			
			stateWidgets = bus.uigen.uiGenerator.getAllWidgets();
			primAdapters = bus.uigen.uiGenerator.getAllPrimAdapters();
			
			commandsHash = bus.uigen.uiGenerator.getAllCommands();
			//	System.out.println("Hashed commands " + commandsHash.size());
			//	System.out.println("Hashed widgets " + stateWidgets.size());
			
			objsMethStr = bus.uigen.uiGenerator.getAllMethodStrings();
			objsPropStr = bus.uigen.uiGenerator.getAllPNameTypeString();
			objsTypeCount = bus.uigen.uiGenerator.getAllTypeCount();
			
			oeFrame = bus.uigen.uiGenerator.getTopFrame();
			oeFrame.hide();
			oeFrame.setVisible(false);

		}
		
	}
	public OperationComposer(Vector _components, Vector names) { 
		init (_components, names);
	}
	public OperationComposer(Vector _components) {  //collects all the necessary stuff for all	
		init (_components, null);
		/*
		//ui compositions for these objects[]
		if ((_components != null) && (_components.size() > 0)) {

			//have the system reset the GUI components automatically b/c it may still
			//have the old junk from a previous call.
			retargetCache = new Hashtable();
			resetAllGUIComponents();  //plus it is needed the 1st time to instantiate the collector vars of generation process.
			
			objects = new Object[_components.size()]; //make the array that everything else uses
			String devName = "";
			String[] devSplit = null;
			cleanNameToObj = new Hashtable();
			for (int i = 0; i < objects.length; i++)  { //copy vector contents to it. 
				objects[i] = (Object)_components.elementAt(i);
				 devName = objects[i].toString().replace('_',' ');
				devSplit = devName.split("@");
				devName = devSplit[0];			
				cleanNameToObj.put(devName, objects[i]);
			}
			
			
			bus.uigen.ObjectEditor.editOverlayList(_components, false);
			
			
			stateWidgets = bus.uigen.uiGenerator.getAllWidgets();
			primAdapters = bus.uigen.uiGenerator.getAllPrimAdapters();
			
			commandsHash = bus.uigen.uiGenerator.getAllCommands();
			//	System.out.println("Hashed commands " + commandsHash.size());
			//	System.out.println("Hashed widgets " + stateWidgets.size());
			
			objsMethStr = bus.uigen.uiGenerator.getAllMethodStrings();
			objsPropStr = bus.uigen.uiGenerator.getAllPNameTypeString();
			objsTypeCount = bus.uigen.uiGenerator.getAllTypeCount();
			
			oeFrame = bus.uigen.uiGenerator.getTopFrame();
			oeFrame.hide();
			oeFrame.setVisible(false);

		}
		*/
		
		
	}
	
	public static void resetExcludeMethods() {
		AClassDescriptor.excludeMethods = initExcludeMethods;
	}
	/*
	public OperationComposer(Vector _components, Hashtable _cmdFilter) {  //collects all the necessary stuff for all													  
	//ui compositions for these objects[]
	if ((_components != null) && (_components.size() > 0)) {

	//have the system reset the GUI components automatically b/c it may still
	//have the old junk from a previous call.
	retargetCache = new Hashtable();
	resetAllGUIComponents();  //pluz it is needed the 1st time to instantiate the collector vars of generation process.
	
	objects = new Object[_components.size()]; //make the array that everything else uses
	
	for (int i = 0; i < objects.length; i++) //copy vector contents to it.
	objects[i] = (Object)_components.elementAt(i);
	
	bus.uigen.uiGenerator.ex
	
	bus.uigen.ObjectEditor.editOverlayList(_components, false);
	
	
	stateWidgets = bus.uigen.uiGenerator.getAllWidgets();
	primAdapters = bus.uigen.uiGenerator.getAllPrimAdapters();
	
	commandsHash = bus.uigen.uiGenerator.getAllCommands();
	//	System.out.println("Hashed commands " + commandsHash.size());
	//	System.out.println("Hashed widgets " + stateWidgets.size());
	
	objsMethStr = bus.uigen.uiGenerator.getAllMethodStrings();
	objsPropStr = bus.uigen.uiGenerator.getAllPNameTypeString();
	
	oeFrame = bus.uigen.uiGenerator.getTopFrame();
	oeFrame.hide();
	oeFrame.setVisible(false);

	}
	
	}
	
	*/	
	
	
	
	
	public static void loadFilter(String fileName, ClassProxy tofilt) {
		/* assume its the actual class name . the command name so might have to go and edit text files pulled from
		excel so that we can support multiple devices at once to evaluate and have their class names as an index to the
		1st level hashtable*/
		
		try {		
			FileReader file = null;
			BufferedReader input = null;
			file = new FileReader(fileName); // Open the file.      
			input = new BufferedReader(file); // Tie 'input' to this file.    }			
			String line;   
			Hashtable usedcmds = new Hashtable();
			
			while( (line = input.readLine()) != null ) { 
				StringTokenizer st = new StringTokenizer(line,".");
				String device = st.nextElement().toString().trim();
				String cmd = st.nextElement().toString().trim();

				usedcmds.put(cmd, new Integer(0));
				
			}
			
			
			Vector meths = uiGenerator.arrayToVector(tofilt.getMethods());
			Vector methNames = new Vector();
			for (int a = 0; a < meths.size(); a++)
				methNames.addElement(((Method)meths.elementAt(a)).getName());
			
			Vector unused = new Vector();
			
			for (int b = 0; b < methNames.size(); b++) {
				if (!initExcludeMethods.contains(methNames.elementAt(b))) {
					if (usedcmds.get(methNames.elementAt(b)) == null)
						unused.addElement(methNames.elementAt(b));
				}
				//System.out.println("unused is " + (String)methNames.elementAt(a));
			}
			
			//now have unused; lets add to exclude method
			
			AClassDescriptor.excludeMethods.addAll(unused); //should join the list together
			/*
			for (int a = 0; a < uiGenerator.excludeMethods.size(); a++) {
			
			System.out.println(a + " unused is " + (String)uiGenerator.excludeMethods.elementAt(a));
			}
			
			*/
			input.close();
			file.close();
			
			
		}
		
		catch (Exception x) {x.printStackTrace();}
		
	}
	

	
	public void removeSingleFrames() {
		Enumeration en = singleFrames.elements();
		
		while (en.hasMoreElements()) 
			((JFrame)en.nextElement()).dispose();
	}
	
	
	
	
	public OperationComposer(String location) {  //collects all the necessary stuff for all													  
		//ui compositions for these objects[]
		try {
			//have the system reset the GUI components automatically b/c it may still
			//have the old junk from a previous call.
			resetAllGUIComponents();
			
			bus.uigen.ObjectEditor oE = new bus.uigen.ObjectEditor();
			
			
			Vector _components = new Vector();
			
			//System.out.print(System.currentTimeMillis());
			Object theObj = (Object)Naming.lookup(location);  //get remote object
			_components.addElement(theObj);
			
			
			if ((_components != null) && (_components.size() > 0)) {
				
				objects = new Object[_components.size()]; //make the array that everything else uses
				
				for (int i = 0; i < objects.length; i++) //copy vector contents to it.
					objects[i] = (Object)_components.elementAt(i);
				
				oE.editOverlayList(_components, false);
				
				
				stateWidgets = bus.uigen.uiGenerator.getAllWidgets();
				primAdapters = bus.uigen.uiGenerator.getAllPrimAdapters();
				
				commandsHash = bus.uigen.uiGenerator.getAllCommands();
				
				//	System.out.println("Hashed commands " + commandsHash.size());
				//	System.out.println("Hashed widgets " + stateWidgets.size());
				oeFrame = bus.uigen.uiGenerator.getTopFrame();
				oeFrame.hide();
				oeFrame.setVisible(false);

				
			}
		}	

		catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	
	public  void resetAllGUIComponents() { //clears adapters/widgets/state for all object passed in previous vector
		
		//NOTE NOT DEALING W/ COND.CONNECT OR QUERY YET.
		try {
			bus.uigen.uiGenerator.resetAllGUIComponents();
			//uiGenerator.resetAllGUIComponents();
			if (stackedFrame != null)
				stackedFrame.dispose();
			if (sharedFrame != null)
				sharedFrame.dispose();				
			if (transfersFrame != null)
				transfersFrame.dispose();				
			if (singleFrames != null) {
				Enumeration elts = singleFrames.elements();
				while (elts.hasMoreElements()) 
					((JFrame)elts.nextElement()).dispose();
			}
			
		}
		catch (Exception x) {}
		
	}
	
	
	
	
	public void refresh(Vector _components) {  //similar to constructor but allows a refresh of the vector w/o having to instantiate
		//new objecteditor frame components
		// for these objects[]
		//keeps the retarget items cache still loaded...NO NEW 
		if ((_components != null) && (_components.size() > 0)) {

			//have the system reset the GUI components automatically b/c it may still
			//have the old junk from a previous call.
			resetAllGUIComponents();
			
			objects = new Object[_components.size()]; //make the array that everything else uses
			
			for (int i = 0; i < objects.length; i++) //copy vector contents to it.
				objects[i] = (Object)_components.elementAt(i);
			
			//bus.uigen.ObjectEditor.editOverlayList(_components, false);
			bus.uigen.ObjectEditor.replaceOverlayList(_components, oeFrame);  //replace frame instead of regenerating junk
			stateWidgets = bus.uigen.uiGenerator.getAllWidgets();
			primAdapters = bus.uigen.uiGenerator.getAllPrimAdapters();
			
			commandsHash = bus.uigen.uiGenerator.getAllCommands();
			//	System.out.println("Hashed commands " + commandsHash.size());
			//	System.out.println("Hashed widgets " + stateWidgets.size());

			objsMethStr = bus.uigen.uiGenerator.getAllMethodStrings();
			objsPropStr = bus.uigen.uiGenerator.getAllPNameTypeString();
			objsTypeCount = bus.uigen.uiGenerator.getAllTypeCount();
			
			oeFrame = bus.uigen.uiGenerator.getTopFrame();
			oeFrame.hide();
			oeFrame.setVisible(false);

		}
	}
	
	
	public void DAuserSelect() { /* creates a frame 
		
		
		for each object
		show its components
		have a check box for user to see if they want it
		
		*/
		
		// @@ NOTE all the creation of the individual device stuff is found in ComponentPanel.getControlPanel
		//  so when doing anything involving the simulation of real UIs then go to this class...maybe 
		// 'import' the stuff that oe already stores for the device UI placement stuff---in BeanInfo?
		
		
		us = new JFrame("Please select component(s) ... ");
		JPanel usP = new JPanel(new GridLayout(objects.length+1, 1));
		ckboxz = new JCheckBox[objects.length];
		
		for (int i=0; i<objects.length; i++) {
			JPanel x = new JPanel();
			JLabel l = new JLabel(objects[i].toString());
			ckboxz[i] = new JCheckBox();
			x.add(l);
			x.add(ckboxz[i]);
			usP.add(x);
		}
		JPanel btnPan = new JPanel();
		JButton done = new JButton("done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Vector touse = new Vector();
			for (int j=0; j<ckboxz.length; j++) {
			if (ckboxz[j].isSelected())
			touse.add(objects[j]);
			}
			Object[] objects2 = new Object[touse.size()];
			for (int k=0; k<touse.size(); k++) 
			objects2[k] = touse.elementAt(k);
			
			objects = objects2;
			if (us != null) 
			us.dispose();
			
			//now let's let them pick the buttons the want.
			//set up frame to be divided for each device

			usB = new JFrame("Please select 'Do All' button(s) to show ...");
			JPanel usSB = new JPanel();
			Vector usSBP = new Vector();
			pantoSbtns = new Hashtable();	
			
			commandsToObj = new Hashtable(); //a hashtable for all commands to object implementers
			
			
			//set up the hashtable
			//System.out.println("num objs " + objects.length);
			for (int i = 0; i < objects.length; i++) {
			Vector objCommands = (Vector)commandsHash.get(objects[i]);  //the commands of the objects[i]
			
			for (int j = 0; j < objCommands.size(); j++) {
			
			MethodProxy currMeth = ((ButtonCommand)objCommands.elementAt(j)).getMethod();
			System.out.print ("currmeth " + currMeth.getName() + "--  " );
			Vector objsWithCommand = (Vector)commandsToObj.get( currMeth );  
			//hope that hashing a method that is the same basic method but from different object
			//hashes to the same bucket...otherwise i'll have to change to hashing the methodname..a more basic type
			
			if (objsWithCommand == null) { //if an object already has claimed this command then  just insert this object w/ it
			objsWithCommand = new Vector();
			}
			objsWithCommand.addElement(objects[i]); 
			System.out.println("  "+objects[i].toString() + " | ");
			commandsToObj.put(currMeth ,objsWithCommand);
			}//end for all commands
			System.out.println();
			}//end for all objects.
			
			//find the buckets in the hashtable that have more than one object
			
			//not dealing with individual components here so no need to call componentpanel.	
			
			System.out.println("cmd to obj has "+commandsToObj.size());
			
			Enumeration allMethods = commandsToObj.keys();
			sharedInvokeButtons = new Vector();
			for (allMethods = commandsToObj.keys() ; allMethods.hasMoreElements() ;) {
			Method currMeth = (Method)allMethods.nextElement();
			System.out.print("looking at "+currMeth.toString());
			Vector objWithCommand = ((Vector)commandsToObj.get(currMeth));
			if (objWithCommand.size() > 1) { //if this command has more than one obj implementing it
			//then we want to create the Panel for it consisting of the the devices that should be in it
			System.out.println("  has shared objects");
			JPanel asB = new JPanel();
			asB.setBorder(BorderFactory.createTitledBorder(currMeth.toString()));
			Hashtable ckB = new Hashtable();  //will store this panels chkboxes
			for (int j=0; j<objWithCommand.size(); j++) {
			JPanel aDwSB = new JPanel();
			
			aDwSB.setBorder(BorderFactory.createEtchedBorder());					
			JLabel lc = new JLabel(objWithCommand.elementAt(j).toString());
			aDwSB.add(lc);
			JCheckBox ckbx = new JCheckBox();
			aDwSB.add(ckbx);
			asB.add(aDwSB); //put the panel for the lb and ckb in the shrd cmd. panel
			ckB.put(lc.getText(),ckbx);
			}
			usSBP.addElement(asB);  //store this panel
			usSB.add(asB);
			pantoSbtns.put(currMeth,ckB);  //store this commands check boxes
			}//end if
			System.out.println("  doesn't have shared objs");
			
			}  //end for each shared command
			JPanel btnPan2 = new JPanel();
			JButton done2 = new JButton("done");
			done2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//rebuild commands to Obj with the ones that the user actually wants.
			Enumeration allMethods;
			for (allMethods = commandsToObj.keys() ; allMethods.hasMoreElements() ;) {
			Method currMeth = (Method)allMethods.nextElement();
			Hashtable chkB = (Hashtable)pantoSbtns.get(currMeth);
			Vector objs = (Vector)commandsToObj.get(currMeth);
			int s = objs.size();
			Vector objsCopy = (Vector)objs.clone();
			for (int o=0; o<s; o++) {//for all of them
			//System.out.println("checking" + ((Command)commands.elementAt(o)).toString());
			JCheckBox x = ((JCheckBox)chkB.get(objs.elementAt(o).toString()));
			if (!x.isSelected()) //if it selected
			objsCopy.removeElement(objs.elementAt(o)); //if not then remove
			
			}
			objs = objsCopy;
			commandsToObj.put(currMeth,objs);  //reassign with the new filtered set of objs
			}//end for all meth
			
			//by now all things should be set
			if (usB != null)   //remove this frame
			usB.dispose();
			
			//now crete the buttons as usual
			//Enumeration allMethods = commandsToObj.keys();
			sharedInvokeButtons = new Vector();
			
			for (allMethods = commandsToObj.keys() ; allMethods.hasMoreElements() ;) {
			Method currMeth = (Method)allMethods.nextElement();
			Vector objWithCommand = ((Vector)commandsToObj.get(currMeth));
			if (objWithCommand.size() > 1) { //if this command has more than one obj implementing it
			//then we want to create the button for it
			
				//SharedInvokeButton sharedButton = new SharedInvokeButton(oeFrame,objWithCommand,currMeth);
			SharedInvokeButton sharedButton = new SharedInvokeButton(oeFrame,objWithCommand,currMeth.getName());
			sharedInvokeButtons.addElement(sharedButton);
			
			}	//end if		 
			
			}//end for all methd.enums
			
			
			//make the window here
			
			JPanel sharedPanel = new JPanel();
			
			int gridRoot = (int)	Math.sqrt(sharedInvokeButtons.size());
			
			
			
			sharedPanel.setLayout(new GridLayout(gridRoot,gridRoot));
			
			for (int i=0; i<sharedInvokeButtons.size(); i++) 
			sharedPanel.add((SharedInvokeButton)sharedInvokeButtons.elementAt(i));

			JFrame sharedFrame = new JFrame("Shared Commands");
			sharedFrame.getContentPane().add(sharedPanel);		
			sharedFrame.show();
			sharedFrame.pack();
			}//end acperfo
			
			
			}); //add actn lis

			btnPan2.add(done2);
			usSB.add(btnPan2);
			usSB.setLayout(new BoxLayout(usSB, BoxLayout.Y_AXIS));
			
			JScrollPane topPanel = new JScrollPane(usSB);
			
			usB.setContentPane(topPanel);
			
			usB.pack();
			usB.show();
			
			
			}
			});
		
		btnPan.add(done);
		usP.add(btnPan);
		
		us.setContentPane(usP);
		us.pack();
		us.show();
		
	}


	public void showSharedCommands() {
		
		Hashtable commandsToObj = new Hashtable(); //a hashtable for all commands to object implementers
		
		
		//set up the hashtable
		for (int i = 0; i < objects.length; i++) {
			Vector objCommands = (Vector)commandsHash.get(objects[i]);  //the commands of the objects[i]
			for (int j = 0; j < objCommands.size(); j++) {
				MethodProxy currMeth = ((ButtonCommand)objCommands.elementAt(j)).getMethod();
				String currMethodName = currMeth.getName().toLowerCase();
				//Vector objsWithCommand = (Vector)commandsToObj.get( currMeth ); 
				Vector objsWithCommand = (Vector)commandsToObj.get( currMethodName );  
				System.out.println("Method: " + currMeth);
				System.out.println("Name: " + currMeth.getName());
				//hope that hashing a method that is the same basic method but from different object
				//hashes to the same bucket...otherwise i'll have to change to hashing the methodname..a more basic type
				
				if (objsWithCommand == null) { //if an object already has claimed this command then  just insert this object w/ it
					objsWithCommand = new Vector();
				}
				//objsWithCommand.addElement(objects[i]);
				objsWithCommand.addElement(new AnObjectMethod(objects[i], currMeth));
				//commandsToObj.put(currMeth ,objsWithCommand);
				commandsToObj.put(currMethodName ,objsWithCommand);
			}//end for all commands
		}//end for all objects.
		
		//find the buckets in the hashtable that have more than one object
		
		//not dealing with individual components here so no need to call componentpanel.	
		
		Enumeration allMethods = commandsToObj.keys();
		sharedInvokeButtons = new Vector();
		
		for (allMethods = commandsToObj.keys() ; allMethods.hasMoreElements() ;) {
			
			//Method currMeth = (Method)allMethods.nextElement();
			String currMeth = (String)allMethods.nextElement();
			//System.out.print(currMeth.getName() + " ");
			Vector objWithCommand = ((Vector)commandsToObj.get(currMeth));
			if (objWithCommand.size() > 1) { //if this command has more than one obj implementing it
				//then we want to create the button for it
				
				SharedInvokeButton sharedButton = new SharedInvokeButton(oeFrame,objWithCommand,currMeth);
				sharedInvokeButtons.addElement(sharedButton);
				
			}			 
			
		}//end for all methd.enums
		
		if (sharedInvokeButtons.size() <= 0) {
			System.out.println("No shared Commands");
			return;
		}
		//make the window here
		
		JPanel sharedPanel = new JPanel();
		
		int gridRoot = (int)	Math.sqrt(sharedInvokeButtons.size());
		
		
		
		sharedPanel.setLayout(new GridLayout(gridRoot,gridRoot));
		
		for (int i=0; i<sharedInvokeButtons.size(); i++) 
			sharedPanel.add((SharedInvokeButton)sharedInvokeButtons.elementAt(i));

		JFrame sharedFrame = new JFrame("Shared Commands");
		sharedFrame.getContentPane().add(sharedPanel);		
		sharedFrame.show();
		sharedFrame.pack();
		
		
		
		
	}//method

	
	public void mysteryCompose(boolean forProps) {  //show property operations too?

		Hashtable commandsToObj = new Hashtable(); //a hashtable for all commands to object implementers
		Hashtable propsToObj = new Hashtable(); //...for all props.
		
		//set up the hashtable
		for (int i = 0; i < objects.length; i++) {
			
			//do methods
			Vector objCommands = (Vector)commandsHash.get(objects[i]);  //the commands of the objects[i]
			for (int j = 0; j < objCommands.size(); j++) {
				MethodProxy currMeth = ((ButtonCommand)objCommands.elementAt(j)).getMethod();
				Vector objsWithCommand = (Vector)commandsToObj.get( currMeth );  
				//hope that hashing a method that is the same basic method but from different object
				//hashes to the same bucket...otherwise i'll have to change to hashing the methodname..a more basic type
				
				if (objsWithCommand == null) { //if an object already has claimed this command then  just insert this object w/ it
					objsWithCommand = new Vector();
				}
				objsWithCommand.addElement(objects[i]); 
				commandsToObj.put(currMeth ,objsWithCommand);
			}//end for all commands
			
			
			//do properties
			
			Enumeration objProps = IntrospectUtility.getAllPropertiesNames(objects[i]);  //the commands of the objects[i]
			if (objProps != null) {
				while (objProps.hasMoreElements()) {
					String currProp = (String)objProps.nextElement();
					
					Vector objsWithProp = (Vector)propsToObj.get( currProp );  
					
					if (objsWithProp == null) { //if an object already has claimed this command then  just insert this object w/ it
						objsWithProp = new Vector();
					}
					objsWithProp.addElement(objects[i]); 
					propsToObj.put(currProp ,objsWithProp);
				}//end for all props
				
			}			
			
		}//end for all objects.
		
		//find the buckets in the hashtable that have more than one object
		//not dealing with individual components here so no need to call componentpanel.	
		
		Enumeration allMethods = commandsToObj.keys();
		sharedInvokeButtons = new Vector();
		for (allMethods = commandsToObj.keys() ; allMethods.hasMoreElements() ;) {
			Method currMeth = (Method)allMethods.nextElement();
			Vector objWithCommand = ((Vector)commandsToObj.get(currMeth));
			if (objWithCommand.size() > 1) { //if this command has more than one obj implementing it
				//then we want to create the button for it
				
				//SharedInvokeButton sharedButton = new SharedInvokeButton(oeFrame,objWithCommand,currMeth);
				SharedInvokeButton sharedButton = new SharedInvokeButton(oeFrame,objWithCommand,currMeth.getName());
				sharedInvokeButtons.addElement(sharedButton);
				
			}			 
			
		}//end for all methd.enums
		
		
		
		//do properties
		Enumeration allProps = propsToObj.keys();
		for (allProps = propsToObj.keys() ; allProps.hasMoreElements() ;) {
			String currProp = (String)allProps.nextElement();
			Vector objWithProp = ((Vector)propsToObj.get(currProp));
			if (objWithProp.size() > 1) { //if this property has more than one obj implementing it
				Vector sampVec = (Vector)propsToObj.get(currProp);  //need to sample this vectors first object to get type of prop
				//get its type if it's numerical and make a buttons for average /total
				
				ClassProxy propType = (IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(sampVec.elementAt(0)),currProp)).getReturnType();
				String typeName = propType.getName();
				
				if ( typeName.equals("int") || typeName.equals("double") || typeName.equals("long") || typeName.equals("float")) {
					//if number type make buttons
					
					JButton total = new JButton("All " + currProp + " Total"); 
					total.addActionListener(new CalcActionListener(objWithProp,currProp,typeName,"total")); 
					sharedInvokeButtons.addElement(total);
					
					JButton avg = new JButton("All " + currProp + " Average");
					avg.addActionListener(new CalcActionListener(objWithProp,currProp,typeName,"average")); 
					sharedInvokeButtons.addElement(avg);

					
				}//end typename...
			}//end >1 
		}//end for all prop.enums

		
		
		//make the window here
		
		JPanel sharedPanel = new JPanel();
		
		int gridRoot = (int)	Math.sqrt(sharedInvokeButtons.size());
		
		sharedPanel.setLayout(new GridLayout(gridRoot,gridRoot));
		
		for (int i=0; i<sharedInvokeButtons.size(); i++) 
			sharedPanel.add((JButton)sharedInvokeButtons.elementAt(i));
		
		
		sharedFrame = new JFrame("Shared Commands");
		sharedFrame.getContentPane().add(sharedPanel);		
		sharedFrame.show();
		sharedFrame.pack();
		
		
	}//method
	

//below val.trans. should have device name fixes and should elimitate showing the same objects name as a possible passer
	public void showValueTransfers()
	{

		Hashtable setterHash = null; //list of setter method key is object and method are values
		Vector setterHashes = null;


		Vector propGetters = null;
		Hashtable propGetterHash = null;


		setterHashes = new Vector();
		propGetterHash = new Hashtable();
		setterHash = new Hashtable();
		Vector getterObjects = null;
		Hashtable getterNamesToObjects = null;


		for (int i = 0; i < objects.length; i++)
		{
			/*				
			bus.uigen.ObjectEditor.edit(objects[i],false);  // 2nd parameter is to show ALL (true) or NO (false) menu options
			bus.uigen.uiGenerator.resetWidgets();//need to create a new object for widgets in uigen since it's static
			
			//getting the hastable directly
			//Vector objWidgets = bus.uigen.uiGenerator.getStateWidgets(objects[i]); 
			//stateWidgets.put(objects[i],objWidgets);
			
			
			
			Vector commands = bus.uigen.uiGenerator.getCommands();
			
			
			//need to get the UI frames for each property so that you can
			//pass it to the method invocation manager to refresh the state after hitting
			//transfer...before it was only showing the last one b/c of the below line only stores
			//the last object in the above loops.  So instead wand a vector of uiFrames and
			//then pass the uiframe that transfer needs into it's constructor
			
			if ((commands != null) && (commands.size() > 0)) 
			oeFrames.put(objects[i],((Command)commands.elementAt(0)).frame); //first one '0' is good enough
			
			
			*/

			/*
			if (commandsHash == null) 
				System.out.println("can't see commmandsHash");
			else
				System.out.println("cmdhash size " +commandsHash.size());
			
			Vector commands = (Vector)commandsHash.get(objects[i]);
			
			System.out.println("has " + commands.size());
			*/
						Vector setters = new Vector(); //the objects setters




			MethodProxy[] commands = ACompositeLoggable.getTargetClass(objects[i]).getMethods();
			for (int j = 0; j < commands.length; j++)
			{

				MethodProxy currentMethod = commands[j];
				System.out.println(currentMethod.getName());
				if (IntrospectUtility.isSetter(currentMethod))
				{ //if we found a setter then store it 
					setters.addElement(IntrospectUtility.getPropertyName(currentMethod));
					System.out.println("found a setter " + currentMethod.getName() + " for" + objects[i].toString());
				}
				if (IntrospectUtility.isGetter(currentMethod))
				{  //if we found a getter
					System.out.println("found a getter " + currentMethod.getName() + " for" + objects[i].toString());
					Vector propVec = (Vector)propGetterHash.get(IntrospectUtility.getPropertyName(currentMethod));

					//see if this property hash an entry already
					if (
						propVec == null)
					{ //if not 
						System.out.println("new getter");
						getterObjects = new Vector();//make one
						getterObjects.addElement(objects[i]);
						propGetterHash.put(IntrospectUtility.getPropertyName(currentMethod), getterObjects);
						
						
					}
					else
					{//already have this property in the the hashtable
						System.out.println("used getter");
						((Vector)propVec).addElement(objects[i]); //just add to it
						propGetterHash.put(IntrospectUtility.getPropertyName(currentMethod), propVec);
						//reput just in case pointers aren't straight
					}

				}//end if gett
			}//end for meths

			if (setters.size() > 0)
				setterHash.put(objects[i], setters);  //add to hashtable of obj to their setters

			System.out.println("setterhash items" + setterHash.size());

		}//end for objects.

		// already being done in constr.  stateWidgets = bus.uigen.uiGenerator.getAllWidgets();  

		//by now we should have a hashtable of object to their property setters and 
		//a hashtable of property (getters) to their objects

		//now let's make a panel that shows these objects and their passings
		//JPanel transfersPanel = new JPanel();
		VirtualContainer transfersPanel = PanelSelector.createPanel();
		GridLayout txfrsLayout = new GridLayout();
		transfersPanel.setLayout(txfrsLayout);

		int txfrCount = 0;
		for (int i = 0; i < objects.length; i++)
		{  //for each object
			Vector toSet = (Vector)setterHash.get(objects[i]); //if it has any setters
			if ((toSet != null) && (toSet.size() > 0))
			{
				/*
				String devName = objects[i].toString().replace('_', ' ');

				String[] devSplit = devName.split("@");
				devName = devSplit[0];
				*/
				String devName = (String) objToCleanName.get(objects[i]);

				ComponentPanel aComPanel = new ComponentPanel(devName, objects[i]);  //create a panel maker
				VirtualContainer objTxfrPanel = aComPanel.getTransferPanelDemo(devName, cleanNameToObj, objToCleanName, toSet, propGetterHash, oeFrame, (Vector)stateWidgets.get(objects[i]));
				//JPanel objTxfrPanel = aComPanel.getTransferPanel(toSet, propGetterHash, oeFrame, (Vector)stateWidgets.get(objects[i]));
				objTxfrPanel.setBorder(BorderFactory.createTitledBorder(devName));
				transfersPanel.add(objTxfrPanel);  //get the transferPanel and add it			
				txfrCount++;
			}
			//else ignore the object
		}//endfor

		txfrsLayout.setColumns(1);
		System.out.println("transfers cnt" + txfrCount);

		txfrsLayout.setRows(txfrCount);
		//JFrame transfersFrame = new JFrame("Value Transfers");
		VirtualFrame transfersFrame = FrameSelector.createFrame("Value Transfers");
		transfersFrame.getContentPane().add(transfersPanel);
		transfersFrame.setVisible(true);
		transfersFrame.pack();



	}



/*
old no cleanup of device name	
	public void showValueTransfers() {
		
		
		Hashtable setterHash = null; //list of setter method key is object and method are values
		
		Vector setterHashes = null;
		
		Vector propGetters = null;
		Hashtable propGetterHash = null;

		setterHashes = new Vector();
		propGetterHash = new Hashtable();
		setterHash = new Hashtable();
		Vector getterObjects = null;
		
		for (int i = 0; i < objects.length; i++) {
			
			Vector setters = new Vector(); //the objects setters
			
			Method[] commands = objects[i].getClass().getMethods();
			for (int j=0; j < commands.length; j++) {
				
				Method currentMethod = commands[j];
				//System.out.println(currentMethod.getName());
				if (uiBean.isSetter(currentMethod)) { //if we found a setter then store it 
					setters.addElement(uiBean.getPropertyName(currentMethod));
					//System.out.println("found a setter " + currentMethod.getName()+ " for" + objects[i].toString());
				}
				if (uiBean.isGetter(currentMethod)) {  //if we found a getter
					//System.out.println("found a getter " + currentMethod.getName()+ " for" + objects[i].toString());
					Vector propVec = (Vector)propGetterHash.get(uiBean.getPropertyName(currentMethod));
					
					//see if this property hash an entry already
					if (
						propVec == null) { //if not 
						//System.out.println("new getter");
						getterObjects = new Vector();//make one
						getterObjects.addElement(objects[i]);
						propGetterHash.put(uiBean.getPropertyName(currentMethod), getterObjects);
					}
					else  {//already have this property in the the hashtable
						//System.out.println("used getter");
						((Vector)propVec).addElement(objects[i]); //just add to it
						propGetterHash.put(uiBean.getPropertyName(currentMethod), propVec);
						//reput just in case pointers aren't straight
					}
					
				}//end if gett
			}//end for meths
			
			if (setters.size() > 0)
				setterHash.put(objects[i],setters);  //add to hashtable of obj to their setters
			
			//System.out.println("setterhash items" + setterHash.size());
			
		}//end for objects.
		
		// already being done in constr.  stateWidgets = bus.uigen.uiGenerator.getAllWidgets();  
		
		//by now we should have a hashtable of object to their property setters and 
		//a hashtable of property (getters) to their objects
		
		//now let's make a panel that shows these objects and their passings
		JPanel transfersPanel = new JPanel();
		GridLayout txfrsLayout = new GridLayout();
		transfersPanel.setLayout(txfrsLayout);
		
		int txfrCount = 0;
		for (int i = 0; i < objects.length; i++) {  //for each object
			Vector toSet = (Vector)setterHash.get(objects[i]); //if it has any setters
			if ((toSet != null) && (toSet.size() > 0)) {  
				ComponentPanel aComPanel = new ComponentPanel("",objects[i]);  //create a panel maker
				JPanel objTxfrPanel = aComPanel.getTransferPanel(toSet,propGetterHash, oeFrame, (Vector)stateWidgets.get(objects[i]));
				objTxfrPanel.setBorder(BorderFactory.createTitledBorder(objects[i].toString()));
				transfersPanel.add(objTxfrPanel);  //get the transferPanel and add it			
				txfrCount++;
			}
			//else ignore the object
		}//endfor
		
		txfrsLayout.setColumns(1);
		//System.out.println("transfers cnt" + txfrCount);
		
		txfrsLayout.setRows(txfrCount);
		transfersFrame = new JFrame("Value Transfers");
		transfersFrame.getContentPane().add(transfersPanel);
		transfersFrame.show();
		transfersFrame.pack();

		
		
	}

 */
  
	public void UMuserSelect() { /* creates a frame 
		for each object
		show its components
		have a check box for user to see if they want it
		
		*/
		
		// @@ NOTE all the creation of the individual device stuff is found in ComponentPanel.getControlPanel
		//  so when doing anything involving the simulation of real UIs then go to this class...maybe 
		// 'import' the stuff that oe already stores for the device UI placement stuff---in BeanInfo?
		
		
		us = new JFrame("Please select component(s) ... ");
		JPanel usP = new JPanel(new GridLayout(objects.length+1, 1));
		ckboxz = new JCheckBox[objects.length];
		
		for (int i=0; i<objects.length; i++) {
			JPanel x = new JPanel();
			x.setBorder(BorderFactory.createEtchedBorder());
			JLabel l = new JLabel(objects[i].toString());
			ckboxz[i] = new JCheckBox();
			x.add(l);
			x.add(ckboxz[i]);
			usP.add(x);
		}
		JPanel btnPan = new JPanel();
		JButton done = new JButton("done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Vector touse = new Vector();
			for (int j=0; j<ckboxz.length; j++) {
			if (ckboxz[j].isSelected())
			touse.add(objects[j]);
			}
			Object[] objects2 = new Object[touse.size()];
			for (int k=0; k<touse.size(); k++) 
			objects2[k] = touse.elementAt(k);
			
			objects = objects2;
			if (us != null) 
			us.dispose();
			
			//now let's let them pick the buttons the want.
			//set up frame to be divided for each device

			usB = new JFrame("Please select button(s) to show ...");
			JPanel uspB = new JPanel(new GridLayout(objects.length+1,1));//might have to put fixed values here
			JPanel[] usDevices = new JPanel[objects.length];
			pantobtns = new Hashtable();
			
			
			//System.out.println("size "+objects.length);
			for (int l = 0; l < objects.length; l++) {
			usDevices[l] = new JPanel();  //create this objects individual panel
			
			//System.out.println(objects[l].toString());
			Vector commands = (Vector)commandsHash.get(objects[l]);
			if (commands == null) {
				System.out.println("see Oposer 864");
				System.exit(0);
			}
			usDevices[l].setLayout(new GridLayout(6,(commands.size()/6)));

			String objName = objects[l].toString();
			usDevices[l].setBorder(BorderFactory.createTitledBorder( objName.substring(0,objName.indexOf('@')))); 
			//				usDevices[l].setBorder(BorderFactory.createTitledBorder(objects[l].toString()));
			//ckboxzB = new Vector();
			//JCheckBox[] ckB = new JCheckBox[commands.size()];  // will store its checkboxes
			Hashtable ckB = new Hashtable();


			
			Hashtable groupC = new Hashtable();
			Hashtable nameToChex = new Hashtable();
			for (int m=0; m<commands.size(); m++) {  //for each button
			

			JPanel usC = new JPanel();   //create a panel, label, and checkbox
			String cname = ((ButtonCommand)commands.elementAt(m)).toString().toUpperCase().replaceAll("OR","/").replaceAll("_","");
			JLabel lc = new JLabel(cname );
			usC.add(lc);
			//ckB[m] = new JCheckBox();
			JCheckBox ckbx = new JCheckBox();
			usC.add(ckbx);

			
			//	cmdBtn.setBackground(java.awt.Color.white);
			
			
			ckB.put(lc.getText(),ckbx);
			nameToChex.put(cname, usC);
			
			}
			
			Enumeration names = nameToChex.keys();
			ArrayList namesAL = Collections.list(names);
			Collections.sort(namesAL);
			Object[] namesAr = namesAL.toArray();
			for (int y=0; y<namesAr.length; y++) {
			JPanel ck = (JPanel)(nameToChex.get(namesAr[y]));
			usDevices[l].add(ck); //put the panel for the lb and ckb in the obj. panel
			
			}

			uspB.add(usDevices[l]);

			/*

			
			for (int m=0; m<commands.size(); m++) {  //for each button
			JPanel usC = new JPanel();   //create a panel, label, and checkbox
			String cname = ((Command)commands.elementAt(m)).toString().toUpperCase().replaceAll("OR","/").replaceAll("_","");
			JLabel lc = new JLabel(cname );
			usC.add(lc);
			//ckB[m] = new JCheckBox();
			JCheckBox ckbx = new JCheckBox();
			usC.add(ckbx);
			usDevices[l].add(usC); //put the panel for the lb and ckb in the obj. panel
			ckB.put(lc.getText(),ckbx);
			}
			uspB.add(usDevices[l]);
			pantobtns.put(objects[l],ckB); //store this objects check boxes

			*/


			}//end for each obj
			JPanel btnPan2 = new JPanel();
			JButton done2 = new JButton("done");
			done2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			for (int n=0; n<objects.length; n++) { // for each selected object
			Hashtable chkB = (Hashtable)pantobtns.get(objects[n]);  //get checkboxes
			Vector commands = (Vector)commandsHash.get(objects[n]);  //and its full set of cmds
			//System.out.println("cs "+commands.size());
			int s = commands.size();
			Vector cmdCopy = (Vector)commands.clone();
			for (int o=0; o<s; o++) {//for all of them
			//System.out.println("checking" + ((Command)commands.elementAt(o)).toString());
			JCheckBox x = ((JCheckBox)chkB.get(((ButtonCommand)commands.elementAt(o)).toString()));
			if (!x.isSelected()) //if it selected
			cmdCopy.removeElement(commands.elementAt(o)); //if not then remove
			
			}
			commands = cmdCopy;
			commandsHash.put(objects[n],commands);  //reassign with the new filtered set of cmds
			}
			
			//by now all things should be set
			if (usB != null)   //remove this frame
			usB.dispose();
			
			showStackedComponents();
			}
			});
			btnPan2.add(done2);
			uspB.add(btnPan2);
			
			JScrollPane topPanel = new JScrollPane(uspB);
			
			usB.setContentPane(topPanel);
			
			usB.pack();
			usB.show();
			
			
			}
			});
		
		btnPan.add(done);
		usP.add(btnPan);
		
		us.setContentPane(usP);
		us.pack();
		us.show();
		
	}

	public void showOverlayList() {
		oeFrame.setVisible(true);
	}

	public void  showStackedComponents() { /* creates a frame 
		for each object passed in to oposer,
		get its commands and widgets
		create a component panel that will hold this objects contents
		pass the panel the widgets and commands so that it can create the 'view' of the 
		device's individual controls
		add this device's panel to the stacked view of all the device (objects) passed
		
		*/
		
		// @@ NOTE all the creation of the individual device stuff is found in ComponentPanel.getControlPanel
		//  so when doing anything involving the simulation of real UIs then go to this class...maybe 
		// 'import' the stuff that oe already stores for the device UI placement stuff---in BeanInfo?
		

		
		try {
			stackPanels = new Vector();
			//stackedFrame = new JFrame("Stacked Components");
			VirtualFrame stackedFrame = FrameSelector.createFrame("Stacked Components");
			//JPanel components = new JPanel();
			VirtualContainer components = PanelSelector.createPanel();

			components.setLayout(new GridLayout(objects.length,1));
			
			
			for (int i = 0; i < objects.length; i++) {
				Vector commands = (Vector)commandsHash.get(objects[i]);
				Vector widgets = (Vector)stateWidgets.get(objects[i]);
				ComponentPanel aComPanel = new ComponentPanel("",objects[i]);
				VirtualContainer acomponent = aComPanel.getControlPanel(widgets,commands, (Vector)primAdapters.get(objects[i]), oeFrame );
				
				//String objName = objects[i].toString();
				
				//acomponent.setBorder(BorderFactory.createTitledBorder(objName.substring(0,objName.indexOf('@'))));
				acomponent.setBorder(BorderFactory.createTitledBorder((String) objToCleanName.get (objects[i])));
				
				components.add(acomponent);
				stackPanels.addElement(acomponent);
				
			}
			//JScrollPane topPanel = new JScrollPane(components);
			//stackedFrame.getContentPane().add(topPanel); 
			stackedFrame.getContentPane().add(components); 
			stackedFrame.setVisible(true);
			stackedFrame.pack();
		}
		catch (Exception e) { e.printStackTrace();}
		
	}

	
	public void  showStackedComponentsHoriz() { /* creates a frame 
		for each object passed in to oposer,
		get its commands and widgets
		create a component panel that will hold this objects contents
		pass the panel the widgets and commands so that it can create the 'view' of the 
		device's individual controls
		add this device's panel to the stacked view of all the device (objects) passed
		
		*/
		
		// @@ NOTE all the creation of the individual device stuff is found in ComponentPanel.getControlPanel
		//  so when doing anything involving the simulation of real UIs then go to this class...maybe 
		// 'import' the stuff that oe already stores for the device UI placement stuff---in BeanInfo?
		

		
		try {
			stackPanels = new Vector();
			//stackedFrame = new JFrame("Stacked Components");
			stackedFrame = FrameSelector.createFrame("Stacked Components");
			//JPanel components = new JPanel();
			VirtualContainer components = PanelSelector.createPanel();
			components.setLayout(new GridLayout(1, objects.length));
			
			
			for (int i = 0; i < objects.length; i++) {
				Vector commands = (Vector)commandsHash.get(objects[i]);
				Vector widgets = (Vector)stateWidgets.get(objects[i]);
				ComponentPanel aComPanel = new ComponentPanel("",objects[i]);
				VirtualContainer acomponent = aComPanel.getControlPanel(widgets,commands, (Vector)primAdapters.get(objects[i]), oeFrame );
				
				//String objName = objects[i].toString();
				
				acomponent.setBorder(BorderFactory.createTitledBorder((String) objToCleanName.get (objects[i])));
				
				components.add(acomponent);
				stackPanels.addElement(acomponent);
				
			}
			//JScrollPane topPanel = new JScrollPane(components);
			//stackedFrame.getContentPane().add(topPanel); 
			stackedFrame.getContentPane().add(components); 
			stackedFrame.setVisible(true);
			stackedFrame.pack();
		}
		catch (Exception e) { e.printStackTrace();}
		
	}
	
	

	public void showSingleComponents() { /*  {using this for retargeting when multiple devices are there
		creates  multiple single device windows for the set of objects
		passed in.  will then extend this to search for the UI of the
		device that is most similar to the device to be retargeted to
		FLOW:
		for each object passed in to oposer,
		creates a frame
		get its commands and widgets
		create a component panel that will hold this objects contents
		pass the panel the widgets and commands so that it can create the 'view' of the 
		device's individual controls
		create a window for the device's panel 
		*/
		
		// @@ NOTE all the creation of the individual device stuff is found in ComponentPanel.getControlPanel
		//  so when doing anything involving the simulation of real UIs then go to this class...maybe 
		// 'import' the stuff that oe already stores for the device UI placement stuff---in BeanInfo?
		
		singlePanels = new Hashtable(); 
		classToObj = new Hashtable();
		singleFrames = new Hashtable();
		
		
		
		try {
			
			VirtualFrame singleFrame = null;
			for (int i = 0; i < objects.length; i++) {
				//singleFrame = new JFrame(objects[i].toString());
				//singleFrame = new JFrame((String) objToCleanName.get(objects[i]));
				singleFrame = FrameSelector.createFrame();
				Vector commands = (Vector)commandsHash.get(objects[i]);
				Vector widgets = (Vector)stateWidgets.get(objects[i]);
				ComponentPanel aComPanel = new ComponentPanel("",objects[i]);
				
				VirtualContainer acomponent = aComPanel.getControlPanel(widgets,commands, (Vector)primAdapters.get(objects[i]), oeFrame);
				
				singleFrame.setContentPane(acomponent);
				singleFrames.put(objects[i].toString(), singleFrame);
				classToObj.put(objects[i].getClass().toString(), objects[i]); //can overwrite if you have multiple object of same type
				
				singlePanels.put(objects[i].toString(), aComPanel); //change to putting panel..but not the display panel
				singleFrame.setVisible(true);
				singleFrame.pack();
				
			}
			
			//System.out.println("SingleFramesHash has " + singleFrames.size());
			
		}
		catch (Exception e) { e.printStackTrace();}
		
	}
	
	
	

	public void retargetSame(Object newTarget, Object sameClass, String newTargetName) { 		
		//System.out.println("retargeting two " + newTarget.toString() + " & " + sameClass.toString());
		Vector commands = (Vector)commandsHash.get(sameClass);		
		//((JPanel)singlePanels.get(sameClass)).setBorder(BorderFactory.createTitledBorder(newTarget.toString()));
		
		for (int i=0; i < commands.size(); i++) {			//for each command.
			
			ButtonCommand cmd = (ButtonCommand)commands.elementAt(i);
			commands.removeElement(cmd);
			cmd.setTargetObject(newTarget);
			commands.addElement(cmd);
		}
		
		//NOOOOOOOOOOOOVEEEEEEEEEEEEECCCCCCCCCCCCCCCTTTTTTTTTTTTTTOOOOOOOOOOOOOORRRRRRRRRRRRRRSSSSSSSSSS
		
		//for now let's assume that there is one object that's displayed and one to remap to.
		//this means all primitive adapters are for the oldTarget number '0' ...but we'll just use the 
		//hashing anyways since future may involve > 1
		
		Vector adapters = (Vector)primAdapters.get(sameClass); 
		if (adapters.size() <= 0) return;
		ClassAdapter parentAdapter = (ClassAdapter) ((PrimitiveAdapter) adapters.elementAt(0)).getParentAdapter();

		//System.out.println("?????" + newTarget.getClass().toString());
		
		//System.out.println("????? method invoking" + primAdapter.getPropertyReadMethod().toString());
		
//		Object obj = (primAdapter.getPropertyReadMethod()).invoke(newTarget,null); //get it's value (obj...see uigenerator.uiAddComponents(..)
		
		
		
		parentAdapter.setRealObject(newTarget); //this may have a certain importance when doing structured types
		parentAdapter.setViewObject(newTarget);//added the below since it seems like it should be needed 
		parentAdapter.getRecordStructure().setTarget(newTarget);
		//System.out.println("record structure: of" + parentAdapter + "is" + parentAdapter.getRecordStructure());
		
		try {
			for (int i=0; i < adapters.size(); i++) {  //for each adapter
				PrimitiveAdapter primAdapter = (PrimitiveAdapter)adapters.elementAt(i);
				adapters.removeElement(primAdapter); //remove them b/c they'll change
				Object currentValue = primAdapter.getRealObject();
				Object obj = parentAdapter.getRecordStructure().get(primAdapter.getPropertyName());
				//primAdapter.getParentAdapter().setValue(newTarget);    //as I did it for the primitive properties the object contains below
				//primAdapter.getParentAdapter().setV
				//i.e. the parent of the individual vector elements might be the vector not the object containing the object
				//then the vectors parent is the realobject 
				//so a type checking might need to be done
				
				
				primAdapter.setAdapterAttributes(primAdapter, obj ,newTarget, primAdapter.getPropertyName());
				primAdapter.linkPropertyToAdapter(newTarget,primAdapter.getPropertyName(),primAdapter);
				
				if (obj instanceof Primitive) {
					if (obj != null)
						((Primitive) obj).addObjectValueChangedListener(primAdapter);
				}
				/*
				uiGenerator.setAdapterAttributesPublic(primAdapter, obj ,newTarget, primAdapter.getPropertyName());
				uiGenerator.linkPropertyToAdapter(newTarget,primAdapter.getPropertyName(),primAdapter);
				*/
				if (!obj.toString().equals(currentValue.toString())) {
					//System.out.println("record structure: of" + parentAdapter + "is" + parentAdapter.getRecordStructure());
					parentAdapter.refreshChild(newTarget, primAdapter.getPropertyName(), false);
					/*
					primAdapter.setViewObject(obj);
					primAdapter.setRealObject(obj);
					primAdapter.setValueFast(obj);
					*/
					//	primAdapter.implicitRefresh(); //should update the UI...i think that's what I tried to do below
					//primAdapter.atomicRefresh();
					//in the line below from pre-proposal time
					//oeFrame.deepElide(primAdapter);
				}
				
				adapters.addElement(primAdapter); //remove them b/c they'll change
				
			}//end for
			
			
			ComponentPanel panel = (ComponentPanel)singlePanels.get(sameClass.toString());
			panel.setObject(newTarget);
			
			
			singlePanels.remove(sameClass.toString());
			singlePanels.put(newTarget.toString(), panel);
			JFrame frm  = (JFrame)singleFrames.get(sameClass.toString());
			frm.setTitle(newTargetName);
			panel.setFrame(frm);
			singleFrames.remove(sameClass.toString());
			singleFrames.put(newTarget.toString(), frm);

			frm.invalidate();
			frm.validate();
			
		}
		catch(Exception e) {e.printStackTrace();}
		
		
	}
	

	public double evaluateTime( int Ca, int Pis, int Pbs, int Pss,
								int Pia, int Pba, int Psa, int Pir,int Pbr,	int Psr) {
		
		
		
		return 0.16*Ca + 6.17*Pia + 3.63*Pba + 5.67*Psa + 3.37*Pir + 2.94*Pbr + 3.28*Psr +
			   0.31*Pis+ 0.36*Pbs + 0.50*Pss - 35.22;					 
	}
	
	public double evaluateGTime( int Ca, int Pia, int Pba, int Psa) {
		
		
		
		return 2.381*Ca +0.178*(Pia^3) - 1.767*(Pia^2) + 14.731*Pia - 0.044*(Pba^3) + 0.685*(Pba^2) 
			   + 4.07*Pba - 0.223*(Psa^2)+ 11.208*Psa + 95.778;
		
	}
	
	public void retarget2(Object newTarget/*, PrintWriter out*/) {
		retarget2(newTarget, newTarget.toString());
	}
	
	public void retarget2(Object newTarget/*, PrintWriter out*/, String newTargetName) { 	//This is the MAIN retarget ... with caching capabilities
		/*note that there are two kinds of caching that I could do.  In one I could just cache the meth/prop info of a target
		which would avoid the two loops below of storing info.  furthermore, and secondly, I could cache the diff information
		of a previous retargeting.  This second one would avoid more processing.  Right now I'll just implement both together
		one because it will show greater results...later if time I could look at just performing only the first...from profiling
		this method though, I don't think that this is the limiting step.
		*/
		
		/*		
		long time;
		System.currentTimeMillis();		
		
		time = System.currentTimeMillis();
		out.print(" " + time);
		*/		
		String numericTypes = "java.lang.Integerjava.lang.Double";  //used to tell if a prop is a numeric types...my devices only deal these two 
		cleanNameToObj.put(newTargetName, newTarget);
		objToCleanName.put (newTarget, newTargetName);
		
		ClassProxy targRealClass = ACompositeLoggable.getTargetClass(newTarget);
		String targClass = targRealClass.toString();
		Object sameClass = classToObj.get(targClass); //here's an object of the same class
		RetargetCacheItem tarItem = null;
		if (sameClass != null) { //then there is a panel of the same class
			retargetSame(newTarget, sameClass, newTargetName);
			return;
		}
		
		
		String closestObj = "";
		double closestTime = 0;
		
		if (singlePanels.size() == 0) { //if there aren't any UIs to retarget to then generate a single UI
			objects = new Object[1];
			objects[0] = newTarget;
			showSingleComponents();
			
		}
		else {
			//get a list of the methods and properties of the newObject
			//this should include size
			
			//what i'll do is create a hash table so that I can just hash the properties of the other objects
			//and if it returns null then it doesn't share...otherwise it is.
			
			
			Vector metVec = null;
			Vector propVec = null;

			Hashtable propHash = null;
			Hashtable metHash = null;
			
			int tPi = 0;
			int tPb = 0;
			int tPs = 0;
			int tC = 0;
			
			double estGenTime = 0;
			
			//System.out.println("Target Props");
			Enumeration newProps = null;
			tarItem = ((RetargetCacheItem)(retargetCache.get(targClass)));  //do we have past information about this obj. that can
			//be used to avoid the two  loops below?
			if (tarItem != null) { //we have it so fill them up
				//	System.out.println("Item 1 found "+targClass);
				//	System.out.println(tarItem.estGenTime);
				propVec = tarItem.propVec;
				metVec = tarItem.metVec;
				propHash = tarItem.propHash;
				metHash = tarItem.metHash;
				estGenTime = tarItem.estGenTime;
				
			}//end if prev tarItem
			
			else {//need to go thru all this.
				//System.out.println("Item 1 didn't find "+targClass);
				newProps = IntrospectUtility.getAllPropertiesNames(newTarget);
				
				propHash = new Hashtable();
				metHash = new Hashtable();
				
				propVec = new Vector();  //will just store their names
				metVec = new Vector();
				
				if (newProps != null) {
					while (newProps.hasMoreElements()) {   //store the beautified names of the properties
						String aprop = (String)newProps.nextElement();
						
						//went to uiClassAdatper and set the label of the widget for displaying the property to start
						//start with an upper case ...note the issues in the task list in the subwindow below.  
						//by changing it there, I can go on to directly compare propertynames later w/o issues of formatting

						//char chars[] = aprop.toCharArray();
						//chars[0] = Character.toUpperCase(chars[0]);
						//aprop = new String(chars);			
						
						
						//System.out.print("-"+aprop+"-");
						propHash.put(aprop, aprop);
						propVec.addElement(aprop);
					}
				}
				
				
				//I took the following code from uiAddmethods in the uiGenerator class
				//i'm trying to simulate how it filters the methods that are actually commands not stuff
				//like toString and the methods for accessing the properties.
				//some changesmade though
				
				ClassDescriptorInterface cdesc = ClassDescriptorCache.getClassDescriptor(targRealClass);
				MethodDescriptorProxy[] methods = cdesc.getMethodDescriptors();
				
				if (methods != null) { //if no methods to add to menus then stop ...prev. from uiMeth.  here it could have just properties

					//System.out.println("Target Cmds");
					for (int i = 0; i < methods.length; i++) {  //otherwise for each method
						//System.out.println("adding: " + methods[i].getName());
						
						MethodProxy realMeth = methods[i].getMethod();
						if (!AClassDescriptor.excludeMethods.contains(realMeth.getName())) {   //if it's the kind of method care for			
							//note that excludeMethods array did not elimitate the "toString" method 
							//rather than changing the array in uiGenerator
							//I will put code in the below so that it doens't include it.
							if (!IntrospectUtility.isPropertyMethod(realMeth)) {
								//String aMeth = (((MethodDescriptor)methods[i]).getDisplayName());      
								String aMeth = (((MethodDescriptor)methods[i]).getDisplayName()).toUpperCase().replaceAll("OR","/").replaceAll("_","").trim();      
								//get the display name of the method
								 //think I need to add these new updates for the Uppercase/replaceAll stuff (don't want to change old methoddescriptor
								
//								System.out.print("targethas..." + aMeth + "...");
								if (!( realMeth.getName().equals("toString") || realMeth.getName().equals("addPropertyChangeListener") )) {
									//System.out.print("-"+aMeth+"-");
									metHash.put(aMeth, realMeth); //putting this in here because in ControlPanel, need a hash of cmd name to real method
									//may later have to have one for the method descriptor
									
									//metVec.addElement(aMeth);
									metVec.addElement(aMeth);
								}//end if !tostr.
							}//end if not propmethd
						}//end if
					}//end for
				}//end if has methods
				
				//now lets store this information in the retarget cache
				tarItem = new RetargetCacheItem();
				tarItem.metHash = metHash;
				tarItem.metVec = metVec;
				tarItem.propHash = propHash;
				tarItem.propVec = propVec;
				tC = metVec.size();
				
			}//end else need to fill targets Vec and hash info since not cached
			
			
			//int closestCmd = 0;
			//int closestProp = 0;
			//int closestCmdDiff = 0;
			//int closestPropDiff = 0;
			Object closestRealObj = null;
			
			Enumeration allPanels = singlePanels.elements();
			Hashtable currObjMeth = null;//the hash table of this crnt objs meth Strings.
			Hashtable currObjProp = null;//the hash table of this crnt objs prop Name-Type Strings.
			
			//don't think I really need the below:
			sharedMeths = new Hashtable();  //these 2 DSs will store the best objects set of shared Meth/Prop strings
			sharedProps = new Hashtable();
			//Vector currNSMeth = null;
			
			while(allPanels.hasMoreElements() ) {
				//System.out.println("After while");
				int curCmd = 0;  //count number of meth/prop that are shared w/ current UI
				int curProp = 0;
				
				//below is number of given typed properties to remap
				int curNProp = 0;   //ideally this should be a numeric type but since i'm only using 
				//need to collect this b/c there are differences in the way OE handles diff types
				int curBProp = 0;
				int curSProp = 0;

				//below are those tho add
				int curNAProp = 0;   //ideally this should be a numeric type but since i'm only using 
				//need to collect this b/c there are differences in the way OE handles diff types
				int curBAProp = 0;
				int curSAProp = 0;
				
				//below are those to remove
				int curNRProp = 0;   //ideally this should be a numeric type but since i'm only using 
				//need to collect this b/c there are differences in the way OE handles diff types
				int curBRProp = 0;
				int curSRProp = 0;
				double esttime = 0;

				
				ComponentPanel current = (ComponentPanel)allPanels.nextElement();
				String currentLabel = current.getObjectLabel();
				
				Object currentRealObj = current.getRealObject();
				//String currClass = currentRealObj.getClass().toString();
				String currClass = current.componentClassName;
				
				
				//now that we have a retarget item instantiated either from previous run of this run; we will
				//look to see whether there is any retCacheItem 2's for the current UI's class that will allow us
				//to avoid all this stuff.
				Hashtable currSharedMeth = null;
				Vector currNSMeth = null;

				Hashtable currSharedProp = null;  //may need to make individual typed ones
				Vector currNSProp = null;
				
				//int curCmdDiff; 
				//int curPropDiff;
				
				
				//see if we have some cached info to avoid  collecting data need to compareUI()
				
				RetargetCacheItem2 compItem = ((RetargetCacheItem2)(tarItem.comparedItems.get(currClass)));
				
				if (compItem != null)  {  //we have info about the transformation 
					//between this classes source UI to the target objs UI
					//	System.out.println("Item 2 found "+targClass+ " connection to " + currClass);
					
					//	System.out.println(compItem.numSNProps + " " + compItem.numSBProps + " " + compItem.numSSProps);
					
					//System.out.println(compItem.esttime + " " + compItem.numRNProps + " " + compItem.unsharedProps);
					/*
					curCmd = compItem.numSCmds;
					curProp = compItem.numSProps;
					*/
					curNProp = compItem.numSNProps;
					curBProp = compItem.numSBProps;
					curSProp = compItem.numSSProps;
					
					curNAProp = compItem.numANProps;
					curBAProp = compItem.numABProps;
					curSAProp = compItem.numASProps;
					
					curNRProp = compItem.numRNProps;
					curBRProp = compItem.numRBProps;
					curSRProp = compItem.numRSProps;
					esttime = compItem.esttime;

					//curCmdDiff = compItem.methDiff;
					//curPropDiff = compItem.propDiff;
					
					currSharedMeth = compItem.sharedMeths;
					currNSMeth = compItem.unsharedMeths;
					
					currSharedProp = compItem.sharedProps;
					currNSProp = compItem.unsharedProps;
					
				}
				
				else {
					//	System.out.println("Item 2 didn't find "+targClass+ " connection to " + currClass);
					currObjMeth = (Hashtable)objsMethStr.get(currentRealObj);  //the hash table of this crnt objs meth Strings.
					
					currSharedMeth = new Hashtable();
					currNSMeth = new Vector();
					
					for (int j = 0; j < metVec.size(); j++) {  //get the shared methods
						String targetCmd = ((String)metVec.elementAt(j)).trim();
						//System.out.print("******Looking for...." + targetCmd);
						if (currObjMeth.get(targetCmd) != null) { //if targetMet in curr UI
							curCmd++;
							currSharedMeth.put(targetCmd, new Integer(1));
//														System.out.println("shared..." + targetCmd+"...");
							//break; //already found it
						}
						else { 
							currNSMeth.addElement(targetCmd);     //it is not shared
//														System.out.println("notshared..." + targetCmd+"...");
						}
						
						
						//Let's sort the non-shared method list in the same manner that that the shared are sorted.
						//not resorting the whole thing b/c requires removing already placed buttons (raises time/performance issues)
						//doing the sorting here instead of inside ComponentPanel.convertUI so that you can store teh vector order in the cache
						//so you don't have to do sorts each time inside convertUI.
						Collections.sort(currNSMeth);
		
						
						

					}//by this time it's gone thru the methods
					
					currObjProp = (Hashtable)objsPropStr.get(currentRealObj);  //the hash table of this crnt objs prop Strings.
					currSharedProp = new Hashtable();
					
					currNSProp = new Vector();

					for (int a = 0; a < propVec.size(); a++) {
						String targetProp = ((String)propVec.elementAt(a)).trim();
						//System.out.print("Looking for...." + targetProp);
						
						//for type checking I will concat the string of propertyname and type together to see
						//if the candidate UI has such an instance
						MethodProxy gttr = IntrospectUtility.getGetterMethod(targRealClass, targetProp);
						String tarType  = PrimitiveClassList.getWrapperType(gttr.getReturnType()).getName();  
						//targetProp = uiGenerator.beautify(targetProp);
						String toCheck = targetProp.concat(tarType);
						
						if (currObjProp.get(toCheck) != null) { //if prop name-type in curr UI
							
							//System.out.println("share -"+toCheck+"-");
							curProp++;
							if (numericTypes.indexOf(tarType) >= 0)
								curNProp++;
							else if (tarType.equals("java.lang.String"))
								curSProp++;
							else if (tarType.equals("java.lang.Boolean"))
								curBProp++;
							
							currSharedProp.put(targetProp, new Integer(1));
							//currSharedProp.addElement(targetProp);
							//break; //already found it
						}
						else  {
							
							currNSProp.addElement(targetProp);
							if (numericTypes.indexOf(tarType) >= 0)
								curNAProp++;
							else if (tarType.equals("java.lang.String"))
								curSAProp++;
							else if (tarType.equals("java.lang.Boolean"))
								curBAProp++;
							
							//System.outS.println("don't share -"+toCheck+"-");
						}
						
					}//by this time it's gone thru the props
					
					//Need to calculate the ones that need to be removed...just subtract the per type total
					//from the number shared per type and then that's it.... this total is calcualted in
					//uigenerator.class during generation
					
					
					int[] currObjTypeCnt = (int[])objsTypeCount.get(currentRealObj);  //the hash table of this crnt objs type counts.
					
					curNRProp = currObjTypeCnt[0] - curNProp;  //total in UI - number shared
					curBRProp = currObjTypeCnt[1] - curBProp;  //total in UI - number shared
					curSRProp = currObjTypeCnt[2] - curSProp;  //total in UI - number shared
					
					
					
					esttime = evaluateTime(currNSMeth.size(), curNProp, curBProp, curSProp,
										   curNAProp, curBAProp, curSAProp, curNRProp,curBRProp,curSRProp);

					
					//curCmdDiff = Math.abs(current.getCommands().size() - metVec.size());  //calc diff even though it may not be
					//needed in the the case that there is only one UI available
					//curPropDiff = Math.abs(current.getWidgets().size() - propVec.size());
					

					//cache the stuff just collected
					
					compItem = new RetargetCacheItem2();
					
					//compItem.numSCmds = curCmd;
					//compItem.numSProps = curProp;
					//compItem.methDiff = curCmdDiff;
					//compItem.propDiff = curPropDiff;
					
					compItem.sharedMeths = currSharedMeth;
					compItem.unsharedMeths = currNSMeth;
					
					compItem.sharedProps = currSharedProp;
					compItem.unsharedProps = currNSProp;
					
					compItem.numSNProps = curNProp;
					compItem.numSBProps = curBProp  ;
					compItem.numSSProps = curSProp  ;
					
					compItem.numANProps = curNAProp  ;
					compItem.numABProps = curBAProp  ;
					compItem.numASProps = curSAProp  ;
					
					compItem.numRNProps = curNRProp  ;
					compItem.numRBProps = curBRProp  ;
					compItem.numRSProps = curSRProp  ;
					
					compItem.esttime = esttime;
					
					tPi = curNProp+curNAProp;  
					tPb = curBProp+curBAProp;
					tPs = curSProp+curSAProp;
					//tC = currSharedMeth.size() + currNSMeth.size();
					
					tarItem.comparedItems.put(currClass, compItem);  //put these comparisons in the cache
					/*					
					System.out.println(curNProp + " " +	curBProp + " " + curSProp + " " + curNAProp + " " + curBAProp
					+ " " + curSAProp + " " + curNRProp + " " + curBRProp + " " + curSRProp 
					+ " " + currNSMeth.size() + " " + esttime);
					*/
					
					//System.exit(0);
					
				}//end else no cached info
				//commented the below out because it's reptition...				
				/*
				if  (singlePanels.size() == 1) {//if there is one UI panel in the bucket then the single object in the array has to be the closest
				closestObj = objects[0].toString();  
				closestTime = esttime;
				closestRealObj = currentRealObj;
				
				sharedMeths = currSharedMeth;  //instead of storing everyones shared and non shared
				//just incrementally store the best one (in the end the last
				//will be the actual best
				sharedProps = currSharedProp;
				unsharedMeths = currNSMeth;
				unsharedProps = currNSProp;
				
				// don't need this break because while above's enumeration of all panels will already be exhausted after
				//the first (THIS) time break;  //hopefully outta while all panels loop
				}
				else { //find the best one outta others
				*/					
				
				//double esttime = evaluateTime(currNSMeth.size(), curNProp, curBProp, curSProp,
				//			 curNAProp, curNBProp, curSAProp, curNRProp,curBRProp,curSRProp);
				
				if ((esttime < closestTime) || (closestObj.equals("")) ) { //if there's a lower time or it starts of with nothing return this current
					closestObj = currentLabel;		//first time, the 1st part of this if won't be true because the init value for esttime = 0;
					closestTime = esttime;

					closestRealObj = currentRealObj;

					//closestCmd = curCmd;
					//closestProp = curProp;
					//closestCmdDiff = curCmdDiff;
					//closestPropDiff = curPropDiff;
					sharedMeths = currSharedMeth;  //instead of storing everyones shared and non shared
					//just incrementally store the best one (in the end the last
					//will be the actual best
					sharedProps = currSharedProp;
					unsharedMeths = currNSMeth;
					unsharedProps = currNSProp;
				}
				//				}//end else
				
				
			}//end while allpanels

			if (tarItem.estGenTime == 0) //instatiated to be 0
				tarItem.estGenTime = evaluateGTime(tC,tPi,tPb,tPs);
			
			//System.out.println(tPi + "  " + tPb + "  " +tPs + "  " +tC+ " " + tarItem.estGenTime);
			//System.exit(0);
			

			
			retargetCache.put(targClass, tarItem);  //put all the caching work above in the major cache.
			
			
			//			time = System.currentTimeMillis();
			//			out.println(" " + time);
			
			
//			System.out.println("*******FINAL Closest is " + closestObj);
			
			//by this point you should have the closestObj so hash it inside that table to get its panel and begin
			//increasing or decreasing

			//for evaluation we wanna force retarget*******	if (tarItem.estGenTime > closestTime) {  //if it faster to retarget then
			
			ComponentPanel bestPanel = (ComponentPanel)singlePanels.get(closestObj);
			
			if (bestPanel == null) {
				System.out.println("didn't find a best panel - Oposer 1715");
				System.exit(0);
			}
			bestPanel.convertUI(newTarget, targClass, targRealClass, metVec,
								metHash, commandsHash ,propVec, propHash,
								primAdapters,stateWidgets, oeFrame, sharedMeths,
								sharedProps, unsharedMeths, unsharedProps/*, out*/);  //  will change this UI's buttons to the UI buttons of the other object
			
			
			
			singlePanels.remove(closestObj);
			singlePanels.put(newTarget.toString(), bestPanel);
			
			JFrame frm  = (JFrame)singleFrames.get(closestObj);  //dunno why i'm keying objs in snglFrame and strngs in snglPnal
			bestPanel.setFrame(frm);
			singleFrames.remove(closestObj);
			singleFrames.put(newTarget.toString(), frm);
			//*******			}
			/*			else {  //generate a UI
			Vector objs = new Vector();
			objs.addElement(newTarget);
			refresh(objs);
			showSingleComponents();
			}
			*/			
			
			
			//}//end else there are one or more panels available
		}//else > 0 panels to evaluate
	}//end retarget2
	
	
	
	

	
	
	
	
	
	

	
	

	
	
	

	
	

	public void UMuserSelect2() { /* creates a frame 
		for each object
		show its components
		have a check box for user to see if they want it
		
		*/
		
		// @@ NOTE all the creation of the individual device stuff is found in ComponentPanel.getControlPanel
		//  so when doing anything involving the simulation of real UIs then go to this class...maybe 
		// 'import' the stuff that oe already stores for the device UI placement stuff---in BeanInfo?
		
		
		us = new JFrame("Please select component(s) ... ");
		JPanel usP = new JPanel(new GridLayout(objects.length+1, 1));
		ckboxz = new JCheckBox[objects.length];
		
		for (int i=0; i<objects.length; i++) {
			JPanel x = new JPanel();
			x.setBorder(BorderFactory.createEtchedBorder());
			JLabel l = new JLabel(objects[i].toString());
			ckboxz[i] = new JCheckBox();
			x.add(l);
			x.add(ckboxz[i]);
			usP.add(x);
		}
		JPanel btnPan = new JPanel();
		JButton done = new JButton("done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Vector touse = new Vector();
			for (int j=0; j<ckboxz.length; j++) {
			if (ckboxz[j].isSelected())
			touse.add(objects[j]);
			}
			Object[] objects2 = new Object[touse.size()];
			for (int k=0; k<touse.size(); k++) 
			objects2[k] = touse.elementAt(k);
			
			objects = objects2;
			if (us != null) 
			us.dispose();
			
			//now let's let them pick the buttons the want.
			//set up frame to be divided for each device

			usB = new JFrame("Please select operations in sequence order...");
			JPanel uspB = new JPanel(new GridLayout(objects.length+1,1));//might have to put fixed values here
			JPanel[] usDevices = new JPanel[objects.length];
			pantobtns = new Hashtable();
			
			
			//System.out.println("size "+objects.length);
			for (int l = 0; l < objects.length; l++) {
			usDevices[l] = new JPanel();  //create this objects individual panel
			
			//System.out.println(objects[l].toString());
			Vector commands = (Vector)commandsHash.get(objects[l]);
			if (commands == null)  {		
				System.out.println("see Oposer 2336");
				System.exit(0);
			}
		
			usDevices[l].setLayout(new GridLayout(6,(commands.size()/6)));
			String objName = objects[l].toString();
			usDevices[l].setBorder(BorderFactory.createTitledBorder( objName.substring(0,objName.indexOf('@')))); 
			//ckboxzB = new Vector();
			//JCheckBox[] ckB = new JCheckBox[commands.size()];  // will store its checkboxes
			Hashtable ckB = new Hashtable();
			/*				
			for (int m=0; m<commands.size(); m++) {  //for each button
			JPanel usC = new JPanel();   //create a panel, label, and checkbox
			String cname = ((Command)commands.elementAt(m)).toString().toUpperCase().replaceAll("OR","/").replaceAll("_","");
			JLabel lc = new JLabel(cname );
			usC.add(lc);
			//ckB[m] = new JCheckBox();
			JCheckBox ckbx = new JCheckBox();
			usC.add(ckbx);
			usDevices[l].add(usC); //put the panel for the lb and ckb in the obj. panel
			ckB.put(lc.getText(),ckbx);
			}
			uspB.add(usDevices[l]);
			pantobtns.put(objects[l],ckB); //store this objects check boxes
			}//end for each obj

			*/



			
			Hashtable groupC = new Hashtable();
			Hashtable nameToChex = new Hashtable();
			for (int m=0; m<commands.size(); m++) {  //for each button
			

			JPanel usC = new JPanel();   //create a panel, label, and checkbox
			String cname = ((ButtonCommand)commands.elementAt(m)).toString().toUpperCase().replaceAll("OR","/").replaceAll("_","");
			JLabel lc = new JLabel(cname );
			usC.add(lc);
			//ckB[m] = new JCheckBox();
			JCheckBox ckbx = new JCheckBox();
			usC.add(ckbx);

			
			//	cmdBtn.setBackground(java.awt.Color.white);
			
			
			ckB.put(lc.getText(),ckbx);
			nameToChex.put(cname, usC);
			
			}
			
			Enumeration names = nameToChex.keys();
			ArrayList namesAL = Collections.list(names);
			Collections.sort(namesAL);
			Object[] namesAr = namesAL.toArray();
			for (int y=0; y<namesAr.length; y++) {
			JPanel ck = (JPanel)(nameToChex.get(namesAr[y]));
			usDevices[l].add(ck); //put the panel for the lb and ckb in the obj. panel
			
			}

			uspB.add(usDevices[l]);


			}



















			JPanel btnPan2 = new JPanel();
			JButton done2 = new JButton("done");
			done2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			for (int n=0; n<objects.length; n++) { // for each selected object
			Hashtable chkB = (Hashtable)pantobtns.get(objects[n]);  //get checkboxes
			Vector commands = (Vector)commandsHash.get(objects[n]);  //and its full set of cmds
			//System.out.println("cs "+commands.size());
			int s = commands.size();
			Vector cmdCopy = (Vector)commands.clone();
			for (int o=0; o<s; o++) {//for all of them
			//System.out.println("checking" + ((Command)commands.elementAt(o)).toString());
			JCheckBox x = ((JCheckBox)chkB.get(((ButtonCommand)commands.elementAt(o)).toString()));
			if (!x.isSelected()) //if it selected
			cmdCopy.removeElement(commands.elementAt(o)); //if not then remove
			
			}
			commands = cmdCopy;
			commandsHash.put(objects[n],commands);  //reassign with the new filtered set of cmds
			}
			
			//by now all things should be set
			if (usB != null)   //remove this frame
			usB.dispose();
			
			showStackedComponents();
			}
			});
			JPanel jtfs = new JPanel();
			JLabel jl = new JLabel("Order:");
			JTextField jtf = new JTextField(55);
			jtfs.add(jl);
			jtfs.add(jtf);

			JPanel jtfs2 = new JPanel();
			JLabel jl2 = new JLabel("Button Name:");
			JTextField jtf2 = new JTextField(15);
			jtfs2.add(jl2);
			jtfs2.add(jtf2);


			btnPan2.add(jtfs);
			btnPan2.add(jtfs2);
			btnPan2.add(done2);
			uspB.add(btnPan2);
			
			JScrollPane topPanel = new JScrollPane(uspB);
			
			usB.setContentPane(topPanel);
			
			usB.pack();
			usB.show();
			
			
			}
			});
		
		btnPan.add(done);
		usP.add(btnPan);
		
		us.setContentPane(usP);
		us.pack();
		us.show();
		
	}





	
}//end class