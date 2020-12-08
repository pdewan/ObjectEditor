package bus.uigen.compose;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;

import util.models.Hashcodetable;
import bus.uigen.Primitive;
import bus.uigen.WidgetAdapter;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMethodProcessor;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.VirtualMethodDescriptor;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.oadapters.PrimitiveAdapterInterface;
import bus.uigen.oadapters.RootAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.view.WidgetShell;
import bus.uigen.viewgroups.APropertyAndCommandFilter;
import bus.uigen.widgets.ButtonSelector;
import bus.uigen.widgets.ComboBoxSelector;
import bus.uigen.widgets.LabelSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComboBox;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualLabel;

public class ComponentPanel
{
	
	//JComboBox[] txfrCombos = null;
	//TxfrJButton[] txfrButtons = null;
	Vector txfrCombos = null;
	Vector txfrButtons = null;
	
	String name = "";
	Object componentObj = null; 
	
	Vector _objWidgets = null;
	Vector _commands = null;
	Vector _primAdapters = null;
	
	static Hashtable nameToComponent=new Hashtable();
	//looks like remove needs either the component's actual variable or the index to remove
	//so i'll have to hash the names of each componentname to its actual component variable
	//in some hash
	

	//	Hashtable oeFrames = null;
	uiFrame oeFrame = null; //the topFrame composing other obj. panels
	JFrame container = null;
	VirtualContainer singleControl = null;
	
	VirtualContainer statusPanel = null;  //will allow multiple methods to access the status panel individually
	VirtualContainer buttonPanel = null;  //...and also button panel
	
	String componentClassName  = null;
	
	public void setFrame(JFrame _container) {
		container = _container;
		container.setTitle(name);
	}
	
	public void setObject (Object _new) {
		componentObj = _new; 
		name = _new.toString(); }
	public void init () {
		controlPanel = PanelSelector.createPanel();
		controlPanel.setLayout(new BorderLayout());
			//new JPanel(new BorderLayout());
	}
	public ComponentPanel() {
		init();
		
	};
	public ComponentPanel(String _name, Object _componentObj) {  //accepts the actual object and its 'name'
		
		init (_name, _componentObj);
		/*
		componentObj = _componentObj;
		if (_name.equals(""))
			name = componentObj.toString();
		else
			name = _name;
		
		componentClassName = componentObj.getClass().toString();
		*/
	}
	public void init (String _name, Object _componentObj) {  //accepts the actual object and its 'name'
		
		init();
		componentObj = _componentObj;
		if (_name.equals(""))
			name = componentObj.toString();
		else
			name = _name;
		
		componentClassName = componentObj.getClass().toString();
	}
	public void init (String _name, Object _componentObj, ObjectAdapter adapter) {  //accepts the actual object and its 'name'
		
		init (_name, _componentObj);
		createContolPanelComponents(adapter);
	}
	
	
	public String getObjectLabel() { return componentObj.toString();}
	public Vector getCommands() { return _commands;}
	public Vector getWidgets() { return _objWidgets;}
	public Object getRealObject() {return componentObj;}
	
	
	public VirtualContainer getTransferPanel(Vector setters, Hashtable propGetterHash, uiFrame _oeFrame, Vector objWidgets) {
		
		oeFrame = _oeFrame;
		_objWidgets = objWidgets;
		
		
		//JPanel txfrContent = new JPanel(new BorderLayout());
		//JPanel txfrdata = new JPanel(new FlowLayout());
		VirtualContainer txfrContent = PanelSelector.createPanel();
		txfrContent.setLayout(new BorderLayout());
		//JPanel txfrdata = new JPanel(new FlowLayout());
		VirtualContainer txfrdata = PanelSelector.createPanel();
		txfrdata.setLayout(new FlowLayout());
			//new JPanel(new FlowLayout());
		//System.out.println("genericWidgets " + objWidgets.size());
		for (int i= 0; i < objWidgets.size(); i++)   //for each widget, place it on the frame's panel
			txfrdata.add(((WidgetShell)objWidgets.elementAt(i)).getContainer());
		
		//VirtualContainer txfrPanel = new JPanel(); //panel to contain this objects transfers
		VirtualContainer txfrPanel = PanelSelector.createPanel(); //panel to contain this objects transfers
		//txfrPanel.setBorder(BorderFactory.createTitledBorder("Transfer Values"));
		GridLayout txfrGrid = new GridLayout();
		txfrPanel.setLayout(txfrGrid);
		
		//txfrCombos = new JComboBox[setters.size()];
		//txfrButtons = new JButton[setters.size()];
		
		txfrCombos = new Vector();
		txfrButtons = new Vector();
		
		int txfrCount = 0; //count the number so we can properly show the gridlayout
		for (int j = 0; j < setters.size(); j++) {//for each property 
			Vector getters = (Vector)propGetterHash.get(setters.elementAt(j)); //see if there are transfers
			
			if (getters != null)  { //if there are transfers then make the panel for the property
				//JPanel propPanel = new JPanel();
				VirtualContainer propPanel = PanelSelector.createPanel();
				VirtualLabel label = LabelSelector.createLabel((String)setters.elementAt(j));
				propPanel.add(label);
				//propPanel.add(new JLabel((String)setters.elementAt(j)));
				//JComboBox aTxfrCombo = new JComboBox(getters);
				
				VirtualComboBox aTxfrCombo = ComboBoxSelector.createComboBox(getters);
				//aTxfrCombo.removeItem(this.name);
				
				txfrCombos.addElement(aTxfrCombo);
				propPanel.add(aTxfrCombo);
				VirtualButton aTxfrButton = (new TxfrJButton(aTxfrCombo,componentObj,(String)setters.elementAt(j),oeFrame)).getButton();
				
				//assoc. button to jcombo 
				
				txfrButtons.addElement(aTxfrButton);
				//set buttonlister already set to self
				propPanel.add(aTxfrButton);
				txfrCount++;
				txfrPanel.add(propPanel);
			}
			//else do nothing
			
		}//endfor
		txfrGrid.setColumns(1);
		txfrGrid.setRows(txfrCount);
		//System.out.println("component object has " + txfrCount + "on it's panel");
		
		txfrContent.add(txfrdata, BorderLayout.NORTH);
		txfrContent.add(txfrPanel, BorderLayout.CENTER);
		return txfrContent;
		
	}//end gettxf
	
	
		public VirtualContainer getTransferPanelDemo(String holder, Hashtable cleanNameToObj, Hashtable objToCleanName, Vector setters, Hashtable propGetterHash, uiFrame _oeFrame, Vector objWidgets ) {
		
		oeFrame = _oeFrame;
		
		
		//VirtualContainer txfrContent = new JPanel(new BorderLayout());
		VirtualContainer txfrContent = PanelSelector.createPanel();
		txfrContent.setLayout(new BorderLayout());
		//VirtualContainer txfrdata = new JPanel(new FlowLayout());
		VirtualContainer txfrdata = PanelSelector.createPanel();
		txfrdata.setLayout(new FlowLayout());
		System.err.println("genericWidgets " + objWidgets.size());
		for (int i= 0; i < objWidgets.size(); i++)   //for each widget, place it on the frame's panel
			txfrdata.add(((WidgetShell)objWidgets.elementAt(i)).getContainer());
		
		//VirtualContainer txfrPanel = new JPanel(); //panel to contain this objects transfers
		VirtualContainer txfrPanel = PanelSelector.createPanel(); //panel to contain this objects transfers
		//txfrPanel.setBorder(BorderFactory.createTitledBorder("Transfer Values"));
		GridLayout txfrGrid = new GridLayout();
		txfrPanel.setLayout(txfrGrid);
		
		//txfrCombos = new JComboBox[setters.size()];
		//txfrButtons = new JButton[setters.size()];
		
		txfrCombos = new Vector();
		txfrButtons = new Vector();
		
		int txfrCount = 0; //count the number so we can properly show the gridlayout
		for (int j = 0; j < setters.size(); j++) {//for each property 
			Vector globalGetters = (Vector)propGetterHash.get(setters.elementAt(j)); //see if there are transfers
			Vector getters = (Vector) globalGetters.clone();
			if (getters.remove(this.getRealObject()))
			if (getters != null && getters.size() >= 1)  { //if there are transfers then make the panel for the property
				//VirtualContainer propPanel = new JPanel();
				VirtualContainer propPanel = PanelSelector.createPanel();
				
				//propPanel.add(new JLabel((String)setters.elementAt(j)));
				propPanel.add(LabelSelector.createLabel((String)setters.elementAt(j)));
				//JComboBox aTxfrCombo = new JComboBox(getters);
				
				//JComboBox aTxfrCombo = new JComboBox();
				VirtualComboBox aTxfrCombo = ComboBoxSelector.createComboBox();
				for (int g = 0; g < getters.size(); g++) {
					/*
					String devName = getters.elementAt(g).toString().replace('_',' ');

					String[] devSplit = devName.split("@");
					devName = devSplit[0];	
					*/
					String devName = (String) objToCleanName.get(getters.elementAt(g));
					if (!devName.equals(holder))					
						aTxfrCombo.addItem(devName);
				} 
				
				txfrCombos.addElement(aTxfrCombo);
				propPanel.add(aTxfrCombo);
				VirtualButton aTxfrButton = (new TxfrJButton(aTxfrCombo,cleanNameToObj, componentObj,(String)setters.elementAt(j),oeFrame)).getButton();
				//assoc. button to jcombo 
				
				txfrButtons.addElement(aTxfrButton);
				//set buttonlister already set to self
				propPanel.add(aTxfrButton);
				txfrCount++;
				txfrPanel.add(propPanel);
			}
			//else do nothing
			
		}//endfor
		txfrGrid.setColumns(1);
		txfrGrid.setRows(txfrCount);
		System.out.println("component object has " + txfrCount + "on it's panel");
		
		txfrContent.add(txfrdata, BorderLayout.NORTH);
		txfrContent.add(txfrPanel, BorderLayout.CENTER);
		return txfrContent;
		
	}//end gettxf


	
	
	public void convertUI (Object target, String tarClassName, ClassProxy targRealClass, Vector targetMeth, 
						   Hashtable metHash, Hashcodetable commandHash, Vector targetProp, Hashtable targetHash,
						   Hashtable primAdapters, Hashtable propHash, uiFrame oeFrame, Hashtable sharedMeths,
						   Hashtable sharedProps, Vector unsharedMeths, Vector unsharedProps/*, PrintWriter out*/) {
		
/*		long time;
		System.currentTimeMillis();
*/		
		
		//passing in the class of the target
		
		//source and target won't be same class so you don
		if (primAdapters.size() <= 0) return;
		
		try {
			//this only converts the single device control panel's commands
		
			commandHash.remove(this.componentObj);  //need to remove the entry of commands for this object b/c it won't 
			//have such a UI as prevoius ... THIS SHOULD NOT REMOVE THE ACTUAL INSTANCE VAR though
			
//			System.out.println("# Cmds = "+ _commands.size());
			Vector newCommands = new Vector();
		/*	
			long time; 
			time = System.currentTimeMillis();
			out.print(" " + time);
			
			time = System.currentTimeMillis();
			out.print(" " + time);
		*/	
			
			for (int j=0; j < _commands.size(); j++) { //for each command in the UI
				
				ButtonCommand sourceCmd = (ButtonCommand)_commands.elementAt(j);  //need at least one b/c using the varibles below for it
				//Command sourceCmd = (Command)cmdEnum.nextElement();  //need at least one b/c using the varibles below for it
				String sourceCmdName = sourceCmd.displayName;   //need to make sure this part is doing the clean up of / to OR etc.
				
//				System.out.print("looking for..." + sourceCmdName);
				//boolean found = false;
				//Method aM = ((Method)metHash.get(sourceCmdName)); 
				MethodProxy aM = ((MethodProxy)metHash.get(sourceCmdName));
				if ( aM != null) {
						sourceCmd.setTargetObject(target, aM);
//						System.out.println("...shared");
						/*
					}
					else	
						sourceCmd.setTargetObject(target); */
					newCommands.addElement(sourceCmd);	//instead of doing this trick replacing biz just create a new version					
				}
				
				//_commands.addElement(sourceCmd); //replace the previous comamnd object
				else { //not shared
					//sourceCmd.getButton().setEnabled(false);
					buttonPanel.remove(sourceCmd.getButton());
//					System.out.println("...don't share");
					//cmdsToDel.addElement(sourceCmd);  //this will only point to the object so dont stress mem?
				}
			}//end for 

			for (int s= 0; s < unsharedMeths.size(); s++) {
				
				String targetC = (String)unsharedMeths.elementAt(s);
//				System.out.println("trying to add -" + targetC+"-");				
				MethodProxy m = ((MethodProxy)metHash.get(targetC));
				MethodDescriptorProxy md = new VirtualMethodDescriptor(m);
				md.setDisplayName(targetC);

				ButtonCommand newCmd = new ButtonCommand(oeFrame, target, md,  m, null );
				//newCmd.setDisplayName(uiGenerator.beautify(md.getDisplayName()));
				//VirtualButton cmdBtn = new JButton(targetC);
				VirtualButton cmdBtn = ButtonSelector.createButton(targetC);
				//note that objectEditor takes an additional step to put a '...' if a method
				//requires parameters to elude to the dialog box that follows.  
				//not gonna do this yet.	
				
				//nameToComponent.put(targetC, cmdBtn);
				cmdBtn.setMargin(new Insets(0,0,0,0));
				//cmdBtn.setMaximumSize(new Dimension(2,2));
				cmdBtn.addActionListener(newCmd);
				newCmd.setButton(cmdBtn);
				buttonPanel.add(cmdBtn);  
			}
		/*	
			time = System.currentTimeMillis();
			out.print(" " + time);
			Thread.sleep(30000);
		*/	
			
			//RE Comment below not using remove anymore so I'll just comment out 'nameToComponent'
			//looks like remove needs either the component's actual variable or the index to remove
			//so i'll have to hash the names of each componentname to its actual component variable
			//in some hash
			
			primAdapters.remove(this.componentObj);  //this source object connections are no longer valid so let's remove them
			//propHash.remove(this.componentObj);
			
			//RE Comment below - already have a list from the evaluation in Oposer
			//let's remove the none shared ones.
			//System.out.println("# Prims = "+_primAdapters.size());
			//Vector primsToDel = new Vector();
			//Vector widsToDel = new Vector();
			
			Vector newprimAdapters  = new Vector();  //will store new ones for the target and then replace ref. on this object 
			Vector newobjWidgets  = new Vector();				   
		/*	
			time = System.currentTimeMillis();
			out.print(" " + time);
		*/	
			
			ClassAdapter parentAdapter =  (ClassAdapter) ((PrimitiveAdapter) _primAdapters.elementAt(0)).getParentAdapter();
			Container topPanel = (Container) parentAdapter.getUIComponent();
			RootAdapter rootAdapter = parentAdapter.getRootAdapter();
			VirtualContainer rootPanel = (VirtualContainer) rootAdapter.getUIComponent();
			for (int y = 0; y < _primAdapters.size(); y++) {  //for each primAdapter
				
				PrimitiveAdapter pAdapter = (PrimitiveAdapter)_primAdapters.elementAt(y); //get the prim adapter
				
				
				//uiPrimitiveAdapter pAdapter = (uiPrimitiveAdapter)primEnum.nextElement(); //get the prim adapter
				WidgetShell sourceWid = pAdapter.getGenericWidget();  //get its assoc  widget
				//Object currentValue = pAdapter.getRealObject();
								
				//String sourceWidName = sourceWid.getLabel(); //and name	
				String propName = pAdapter.getPropertyName();
				
				//System.out.println("-"+sourceCmdName+"-");
				
				//boolean found = false;
				
				//below shows we need shareProps because it serves as type checking 
				//if (sharedProps.get(sourceWidName) != null) {  //the formatting of sourcewid and strngs in prop hash might not be the sam
				if (sharedProps.get(propName) != null) { 
				//go ahead and retarget
					//found = true;
					//System.out.println("share -"+sourceWidName+"-");	
					String retargProp = pAdapter.getPropertyName();
					
					
					//char chars[] = retargProp.toCharArray();
					//chars[0] = Character.toUpperCase(chars[0]);
					//retargProp = new String(chars);			
					
					//System.out.println("(**retargeting " + retargProp);
					
					
					//Object obj = (sourceAdapter.getPropertyReadMethod()).invoke(target,null); //get it's value (obj...see uigenerator.uiAddComponents(..)
					//try something else from above...i'm thinking the above doesn't work because
					//they don't have the same exact method signatures 
					//!!!!WILL THIS CREATE ISSUES IN UPDATING?  MAYBE IF I SET A NEW PROPERTY READ AND WRITE METHODS?
					
					//commenting out for new structure
					//so i'm just trying to get its getter
					//Method getter = uiBean.getGetterMethod(targRealClass, retargProp);
					//pAdapter.setPropertyReadMethod(getter);
					//System.out.println(sourceAdapter.getPropertyReadMethod().getName());
					
					//Method setter = uiBean.getSetterMethod(targRealClass, retargProp);
					//pAdapter.setPropertyWriteMethod(setter);
					
					
					//Object obj = getter.invoke(target,null);
					
					//System.out.println("value is" + obj.toString());
					//replacing code below
					
					
					
					parentAdapter.refreshSelf(target, false);
					
					
					//pAdapter.getParentAdapter().setRealObject(target); //this may have a certain importance when doing structured types
					//pAdapter.getParentAdapter().setViewObject(target);//added the below since it seems like it should be needed 
					//as I did it for the primitive properties the object contains below
					//value/viewObject/RealObject i.e. target
					//THUS THE ABOVE ASSUME A TWO LEVEL OBJECT AND IT DOESN'T GO DEEPER THAN THE ACTUAL OBJECT AND ITS PRIMITIVE SUBJOBJECTS
										
					
					//will comment the below out because it should be automatically done later in implicit refresh?
					//retargeting seems to still work after removing the below
					//pAdapter.getParentAdapter().setValue(target);    
					
					//plus if you look in implicitRefresh this adapter will use the parent adapter's
					
					
					
					//i.e. the parent of the individual vector elements might be the vector not the object containing the object
					//then the vectors parent is the realobject 
					//so a type checking might need to be done
					
					
					//System.out.println( "record striucture in component panel" + parentAdapter.getRecordStructure());
					Object obj = parentAdapter.getRecordStructure().get(retargProp);
					
					ObjectAdapter.setAdapterAttributes(pAdapter, obj ,target, retargProp);
					//uiGenerator.linkPropertyToAdapter(target,retargProp,pAdapter);
					ObjectAdapter.linkPropertyToAdapter(target,retargProp,pAdapter);
					if (obj != null){						
						if (obj instanceof Primitive) {

							((Primitive) obj).addObjectValueChangedListener(pAdapter);
						}

						
						/*              ________the assumption is that we are dealing with primitive types
						________________so we can do string comparisons of the values  hopefully the 
						string comparisons will be faster than the setviewobject calls and implicit refreshing of the widget
						*/
			
						//pAdapter.setViewObject(obj);
						//pAdapter.setRealObject(obj);
						parentAdapter.refreshChild(target, retargProp, false);
						//pAdapter.setValueFast(obj);
						//pAdapter.implicitRefresh(); //should update the UI...i think that's what I tried to do below
						//in the line below from pre-proposal time
						//oeFrame.deepElide(sourceAdapter);
						//implicit refresh notneeded.
						
					//	}
						newprimAdapters.addElement(pAdapter);
						newobjWidgets.addElement(pAdapter.getGenericWidget());
					}
				}//if they share it
				//_commands.addElement(sourceCmd); //replace the previous comamnd object
				else { //not shared
					//sourceWid.setVisible(false);			
					
					//sourceWid.getComponent(0).setEnabled(false);  //assuming primitive property display is 2
					//sourceWid.setEnabled(false);
					statusPanel.remove(sourceWid.getContainer());
					//stuff w/ the UI when actually removing it.
					
					//cmdsToDel.addElement(sourceCmd);  //this will only point to the object so dont stress mem?
				}//end else
			}//end for all primadapt
		/*
			time = System.currentTimeMillis();
			out.print(" " + time);
			Thread.sleep(30000);
			
			time = System.currentTimeMillis();
			out.print(" " + time);
		*/	
			//statusPanel.setLayout(new GridLayout(  (_commands.size()+unsharedMeths.size()) /4,4));
			
			this.name = target.toString();
			
			if (unsharedProps.size() > 0) { //then we have some unshared properties
				
				//statusPanel.setLayout(new GridLayout((unsharedProps.size() + sharedProps.size()), 1));
				bus.uigen.uiGenerator.generateUIProperties(oeFrame,target,null,null,name, rootPanel, statusPanel, sharedProps);
				
				bus.uigen.uiGenerator.resetWidgets(); //refresh the vector of widgets/properties for next possible time
				bus.uigen.uiGenerator.resetAdapters(); 
				
				Vector  padapt =  bus.uigen.uiGenerator.getPrimAdapters(target);    //(Vector)sWidgets.get(target);
				Vector widgets = bus.uigen.uiGenerator.getStateWidgets(target);
				for (int u = 0; u < padapt.size(); u++)  {//append new widgets/primadapts to new"lists"
					newprimAdapters.addElement(padapt.elementAt(u));  // two vecs should be same size
					newobjWidgets.addElement(widgets.elementAt(u));
					
					//statusPanel.add(((uiGenericWidget)(widgets.elementAt(u))).getContainer());
				}

				
			}//end if unshared
			
			//close it up
			_commands = newCommands;
			_primAdapters = newprimAdapters;
			_objWidgets = newobjWidgets;
			
//			this.name = target.toString();
			this.componentObj = target;
			commandHash.put(target, _commands);//add the object to the hash
			//propHash.put(target, _objWidgets);
			
			primAdapters.put(target, _primAdapters);
			componentClassName = tarClassName;
			
			//singleControl.setBorder(BorderFactory.createTitledBorder(this.name));  //rename the border of this panel
			/*
			time = System.currentTimeMillis();
			out.print(" " + time);			
			*/
			
			singleControl.invalidate();  //refresh screen
			singleControl.validate(); 
			/*
			time = System.currentTimeMillis();
			out.println(" " + time);
			
			
			time = System.currentTimeMillis();
			out.println(" " + time);			
			Thread.sleep(30000);
			*/
			
		}//end try
		catch (Exception x) {x.printStackTrace();}
		
		
	}//end convertUI
	
	public void add(VirtualComponent comp) {		
		
		statusPanel.add(comp);
		
	}
	public void remove(VirtualComponent comp) {		
		
		statusPanel.remove(comp);
		
	}
	//JPanel controlPanel ;
	//VirtualContainer controlPanel = new JPanel(new BorderLayout());
	VirtualContainer controlPanel;
	public VirtualContainer getContainer() {
		return controlPanel;
	}
	public void  setContainer(VirtualContainer c) {
		controlPanel = c;
	}
	
	public void createContolPanelComponents(ObjectAdapter adapter) {
		int numComponents;
		if (adapter instanceof PrimitiveAdapter) {
			numComponents = 1;
		} else {
			numComponents = ((CompositeAdapter) adapter).getChildCount();
		}
		statusPanel = createStatusPanel (numComponents);
					
		
		
		
		//controlPanel.add(statPanParent, BorderLayout.CENTER);
		//controlPanel.add(statusPanel, BorderLayout.CENTER);
		Vector commandList = AMethodProcessor.createCommandList(adapter.getUIFrame(), adapter.getRealObject(), adapter, APropertyAndCommandFilter.propertyAndCommandFilterClass);
		buttonPanel = createAndAddButtonPanel (commandList);
		addButtonPanel (controlPanel, buttonPanel);
		addStatusPanel (controlPanel, statusPanel);
		
		controlPanel.setMaximumSize(new VirtualDimension(200,200));
		
	}
	
	void addStatusPanel (VirtualContainer controlPanel, VirtualContainer statusPanel) {
		
		//VirtualContainer statPanParent = new JPanel();
		VirtualContainer statPanParent = PanelSelector.createPanel();
		statPanParent.add(statusPanel);
		
		//JScrollPane statPanParentScroll = new JScrollPane(statPanParent);
			
		VirtualContainer statPanParentScroll = ScrollPaneSelector.createScrollPane();
		statPanParentScroll.add(statPanParent);
		controlPanel.add(statPanParentScroll, BorderLayout.CENTER);
		
	}
	void addButtonPanel (VirtualContainer controlPanel, VirtualContainer statusPanel) {
		//VirtualContainer butPanParent = new JPanel();
		VirtualContainer butPanParent = PanelSelector.createPanel();
		butPanParent.add(buttonPanel);
		//controlPanel.add(butPanParent, BorderLayout.NORTH);		
		controlPanel.add(buttonPanel, BorderLayout.NORTH);
		
	}
	
	public  VirtualContainer createStatusPanel (int numComponents) {
		//VirtualContainer retVal = new JPanel(new GridLayout((numComponents/3), 5));
		VirtualContainer retVal = PanelSelector.createPanel();
		retVal.setLayout(new GridLayout((numComponents/3), 5));
		/*
		JPanel statPanParent = new JPanel();
		statPanParent.add(retVal);
		
		JScrollPane statPanParentScroll = new JScrollPane(statPanParent);
		*/
//		controlPanel.add(statPanParentScroll, BorderLayout.SOUTH);
		//controlPanel.add(statPanParentScroll, BorderLayout.CENTER);
		return retVal;
		
	}
	/*
	public  JPanel oldCreateAndAddButtonPanel (Vector commands) {		
		JPanel buttonPanel = new JPanel(new GridLayout(commands.size()/4,4));
		
		Hashtable groupButtons = new Hashtable();
		Hashtable nameToButton = new Hashtable();
		for (int i=0; i < commands.size(); i++) {			//for each command.
			
			Command cmd = (Command)commands.elementAt(i);
		//	String cmdBtnName = cmd.toString().toUpperCase().replaceAll("OR","/").replaceAll("_","").trim();
			String cmdBtnName = cmd.toString();
			VirtualButton cmdBtn = new JButton(cmdBtnName);
		//	cmdBtn.setBackground(java.awt.Color.white);
			
			//nameToComponent.put(cmd.toString(), cmdBtn);
			//nameToButton.put(cmd.toString(), cmdBtn);
			nameToComponent.put(cmdBtnName, cmdBtn);  //updated to store the fixed name for the button i.e. or -> / etc
			nameToButton.put(cmdBtnName, cmdBtn);
			
			//cmdBtn.setFont(new Font(null,Font.BOLD,10));
			
			cmdBtn.setMargin(new Insets(0,0,0,0));
			cmdBtn.setMaximumSize(new Dimension(2,2));
			//cmdBtn.setSize(new Dimension(2,2));
			cmdBtn.addActionListener(cmd);
			cmd.setButton(cmdBtn);
			
			

			//DON'T THINK THE NEXT 7 lines do thing useful
			
			String place_toolbar = (String) cmd.getMD().getValue(AttributeNames.PLACE_TOOLBAR);  
			//get it's place toolbar name defined as one of its attributes
			
			//see if vector made for it
			Vector cmdList = (Vector)groupButtons.get(place_toolbar);
			
			if (cmdList == null)  // make new vector
				cmdList = new Vector();
			
			cmdList.addElement(cmdBtn);
				
		}
		
		Enumeration names = nameToButton.keys();
		ArrayList namesAL = Collections.list(names);
		Collections.sort(namesAL);
		Object[] namesAr = namesAL.toArray();
		for (int y=0; y<namesAr.length; y++) {
			VirtualButton cb = (JButton)(nameToButton.get(namesAr[y]));
			buttonPanel.add(cb);  
		}
		
		//buttonPanel.add(cmdBtn);  
		
		
		JPanel butPanParent = new JPanel();
		//butPanParent.add(buttonPanel);
		//controlPanel.add(butPanParent, BorderLayout.NORTH);
		
		
		controlPanel.add(buttonPanel, BorderLayout.NORTH);
		return buttonPanel;
	}
	*/
	public  VirtualContainer createAndAddButtonPanel (Vector commands) {
		VirtualContainer buttonPanel = createButtonPanel (commands);
				
		controlPanel.add(buttonPanel, BorderLayout.NORTH);
		return buttonPanel;
	}
	public static void createButtons (Vector commands, ObjectAdapter theAdapter)  {
		for (int i=0; i < commands.size(); i++) {			//for each command.
			
			ButtonCommand cmd = (ButtonCommand)commands.elementAt(i);
			MethodDescriptorProxy md = cmd.getMethodDescriptor();
			if (!AttributeManager.getMethodVisible(md, theAdapter))
				continue;
		//	String cmdBtnName = cmd.toString().toUpperCase().replaceAll("OR","/").replaceAll("_","").trim();
			String cmdBtnName = cmd.toString();
			//JButton cmdBtn = new JButton(cmdBtnName);
			
			//VirtualButton cmdBtn = new JButton(); 
			VirtualButton cmdBtn = ButtonSelector.createButton();
			cmdBtn.setName(cmdBtnName + ":" + cmd.targetAdapter);
			/*
			String displayIcon = cmd.getDisplayIcon();
			if (displayIcon == null) {
				cmdBtn = new JButton(cmdBtnName);
			} else {
				cmdBtn = new JButton();
				Icon icon = new ImageIcon(displayIcon);
				cmdBtn.setIcon(icon);
			}
			*/
			
			ButtonCommand.maybeChangeLabelOrIcon(cmdBtn, cmdBtnName, cmd.getDisplayIcon());
			
			Color background = cmd.getComponentBackground();
			if (background != null)
			cmdBtn.setBackground(background);
			Color foreground = cmd.getComponentForeground();
			if (foreground != null)
			cmdBtn.setForeground(foreground);
			Integer width = cmd.getComponentWidth();
			Integer height = cmd.getComponentHeight();
			WidgetAdapter.setSize (cmdBtn, width, height );
			
		//	cmdBtn.setBackground(java.awt.Color.white);
			
			//nameToComponent.put(cmd.toString(), cmdBtn);
			//nameToButton.put(cmd.toString(), cmdBtn);
			
			//cmdBtn.setFont(new Font(null,Font.BOLD,10));
			
			cmdBtn.setMargin(new Insets(0,0,0,0));
			cmdBtn.setMaximumSize(new VirtualDimension(2,2));
			//cmdBtn.setSize(new Dimension(2,2));
			cmdBtn.addActionListener(cmd);
			cmd.setButton(cmdBtn);
				
		}
		
	}
	public static Vector createCommandsWithButtons (uiFrame frame, Object object, ObjectAdapter adapter) { 
	Vector commands = AMethodProcessor.createCommandList(frame, object, adapter, APropertyAndCommandFilter.propertyAndCommandFilterClass);
	createButtons (commands, adapter);
	return commands;
	}
	public static VirtualContainer createButtonPanel (Vector commands) {		
		//VirtualContainer buttonPanel = new JPanel(new GridLayout(commands.size()/4,4));
		VirtualContainer buttonPanel = PanelSelector.createPanel();
		buttonPanel.setLayout(new GridLayout(commands.size()/4,4));
		
		Hashtable groupButtons = new Hashtable();
		Hashtable nameToButton = new Hashtable();
		for (int i=0; i < commands.size(); i++) {			//for each command.
			
			ButtonCommand cmd = (ButtonCommand)commands.elementAt(i);
			System.err.println (cmd.displayName + " Row:" + cmd.getRow());
			System.err.println (cmd.displayName + " Col:" + cmd.getColumn());
		//	String cmdBtnName = cmd.toString().toUpperCase().replaceAll("OR","/").replaceAll("_","").trim();
			String cmdBtnName = cmd.toString();
			//VirtualButton cmdBtn = new JButton(cmdBtnName);
			VirtualButton cmdBtn =  ButtonSelector.createButton(cmdBtnName);
			Color color = cmd.getComponentBackground();
			if (color != null)
			cmdBtn.setBackground(color);
		//	cmdBtn.setBackground(java.awt.Color.white);
			
			//nameToComponent.put(cmd.toString(), cmdBtn);
			//nameToButton.put(cmd.toString(), cmdBtn);
			nameToComponent.put(cmdBtnName, cmdBtn);  //updated to store the fixed name for the button i.e. or -> / etc
			nameToButton.put(cmdBtnName, cmdBtn);
			
			//cmdBtn.setFont(new Font(null,Font.BOLD,10));
			
			cmdBtn.setMargin(new Insets(0,0,0,0));
			cmdBtn.setMaximumSize(new VirtualDimension(2,2));
			//cmdBtn.setSize(new Dimension(2,2));
			cmdBtn.addActionListener(cmd);
			cmd.setButton(cmdBtn);
			
			

			//DON'T THINK THE NEXT 7 lines do thing useful
			
			String place_toolbar = (String) cmd.getMethodDescriptor().getValue(AttributeNames.PLACE_TOOLBAR);  
			//get it's place toolbar name defined as one of its attributes
			if (place_toolbar == null)
				continue;
			Vector cmdList = (Vector)groupButtons.get(place_toolbar);
			//see if vector made for it
			/*
			Vector cmdList = null;
			if (place_toolbar != null)
			 cmdList = (Vector)groupButtons.get(place_toolbar);
			 */
			
			if (cmdList == null)  // make new vector
				cmdList = new Vector();
			
			cmdList.addElement(cmdBtn);
				
		}
		
		Enumeration names = nameToButton.keys();
		ArrayList namesAL = Collections.list(names);
		Collections.sort(namesAL);
		Object[] namesAr = namesAL.toArray();
		for (int y=0; y<namesAr.length; y++) {
			//VirtualButton cb = (JButton)(nameToButton.get(namesAr[y]));
			VirtualButton cb = (VirtualButton)(nameToButton.get(namesAr[y]));
			buttonPanel.add(cb);  
		}
		
		//buttonPanel.add(cmdBtn);  
		
		
		//JPanel butPanParent = new JPanel();
		//butPanParent.add(buttonPanel);
		//controlPanel.add(butPanParent, BorderLayout.NORTH);
		
		
		
		return buttonPanel;
	}
	
	public void bindLabelToComponent (String label, Component wid) {
		nameToComponent.put(label, wid);
	}
	
	
	public VirtualContainer getControlPanel(Vector objWidgets, Vector commands, Vector primAdapters, uiFrame _oeFrame) {
		//get the individual control panel for this object.	
		
		_objWidgets = objWidgets;
		_commands = commands;
		_primAdapters = primAdapters;
		
		//JPanel controlPanel = new JPanel(new BorderLayout());
		//JPanel controlPanel = new JPanel();
		
		//statusPanel = new JPanel(new GridLayout((objWidgets.size()/3), 5));
		//if (objWidgets == null) return null;
		statusPanel = createStatusPanel ( objWidgets.size());
		//statusPanel = new JPanel(new GridLayout(6, 5));
		//statusPanel = new JPanel(new FlowLayout());
		//statusPanel = new JPanel(new GridLayout((objWidgets.size()), 2));
		
		for (int i = 0; i < objWidgets.size(); i++) { //dump widgets to state panel
			
			//System.out.println(objWidgets.size());
			WidgetShell aWid = ((WidgetShell)(objWidgets.elementAt(i)));
			
			
			//if (aWid == null) 
			//	System.out.println("AWID IS NULL");
			
			nameToComponent.put(aWid.getLabel(), aWid);
			add (aWid.getContainer());
			//statusPanel.add(aWid.getContainer());
			
		}
		/*
		JPanel statPanParent = new JPanel();
		statPanParent.add(statusPanel);
		JScrollPane statPanParentScroll = new JScrollPane(statPanParent);
				
		
		//controlPanel.add(statPanParentScroll, BorderLayout.SOUTH);
		controlPanel.add(statPanParentScroll, BorderLayout.CENTER);
		*/
		
		
		//controlPanel.add(statPanParent, BorderLayout.CENTER);
		//controlPanel.add(statusPanel, BorderLayout.CENTER);
		buttonPanel = createAndAddButtonPanel(commands);
		/*
		JPanel butPanParent = new JPanel();
		//butPanParent.add(buttonPanel);
		//controlPanel.add(butPanParent, BorderLayout.NORTH);
		
		
		controlPanel.add(buttonPanel, BorderLayout.NORTH);
		*/
		addStatusPanel (controlPanel, statusPanel);
		addButtonPanel (controlPanel, buttonPanel);
		/*
		buttonPanel = new JPanel(new GridLayout(commands.size()/4,4));
		
		Hashtable groupButtons = new Hashtable();
		Hashtable nameToButton = new Hashtable();
		for (int i=0; i < commands.size(); i++) {			//for each command.
			
			Command cmd = (Command)commands.elementAt(i);
		//	String cmdBtnName = cmd.toString().toUpperCase().replaceAll("OR","/").replaceAll("_","").trim();
			String cmdBtnName = cmd.toString();
			JButton cmdBtn = new JButton(cmdBtnName);
		//	cmdBtn.setBackground(java.awt.Color.white);
			
			//nameToComponent.put(cmd.toString(), cmdBtn);
			//nameToButton.put(cmd.toString(), cmdBtn);
			nameToComponent.put(cmdBtnName, cmdBtn);  //updated to store the fixed name for the button i.e. or -> / etc
			nameToButton.put(cmdBtnName, cmdBtn);
			
			//cmdBtn.setFont(new Font(null,Font.BOLD,10));
			
			cmdBtn.setMargin(new Insets(0,0,0,0));
			cmdBtn.setMaximumSize(new Dimension(2,2));
			//cmdBtn.setSize(new Dimension(2,2));
			cmdBtn.addActionListener(cmd);
			cmd.setButton(cmdBtn);
			
			

			//DON'T THINK THE NEXT 7 lines do thing useful
			
			String place_toolbar = (String) cmd.getMD().getValue(AttributeNames.PLACE_TOOLBAR);  
			//get it's place toolbar name defined as one of its attributes
			
			//see if vector made for it
			Vector cmdList = (Vector)groupButtons.get(place_toolbar);
			
			if (cmdList == null)  // make new vector
				cmdList = new Vector();
			
			cmdList.addElement(cmdBtn);
				
		}
		
		Enumeration names = nameToButton.keys();
		ArrayList namesAL = Collections.list(names);
		Collections.sort(namesAL);
		Object[] namesAr = namesAL.toArray();
		for (int y=0; y<namesAr.length; y++) {
			JButton cb = (JButton)(nameToButton.get(namesAr[y]));
			buttonPanel.add(cb);  
		}
		
		//buttonPanel.add(cmdBtn);  
		
		
		JPanel butPanParent = new JPanel();
		//butPanParent.add(buttonPanel);
		//controlPanel.add(butPanParent, BorderLayout.NORTH);
		
		
		controlPanel.add(buttonPanel, BorderLayout.NORTH);	
		//JScrollPane butPanParentScroll = new JScrollPane(buttonPanel);			
		
		//controlPanel.add(butPanParentScroll);
		//controlPanel.add(butPanParentScroll, BorderLayout.NORTH);
		//controlPanel.add(butPanParentScroll, BorderLayout.CENTER);
		//controlPanel.add(buttonPanel, BorderLayout.NORTH);
		 * 
		 */
		
		singleControl = controlPanel;
		//_oeFrame.setSize(200,200);
		controlPanel.setMaximumSize(new VirtualDimension(200,200));
		
		return controlPanel;
	}
	
	
	
	
	
	
	
	/*
	public void convertUIComp (Object target,  Vector targetMeth, Class targRealClass, Hashtable metHash, Hashtable commandHash,
						   Vector targetProp, Hashtable targetHash, Hashtable primAdapters, Hashtable propHash,
						   uiFrame oeFrame, Hashtable sharedMeths, Hashtable sharedProps, Vector unsharedMeths,
						   Vector unsharedProps, PrintWriter out) {
		
		
	
		try {
			//this only converts the single device control panel's commands
			commandHash.remove(this.componentObj);  //need to remove the entry of commands for this object b/c it won't 
			//have such a UI as prevoius ... THIS SHOULD NOT REMOVE THE ACTUAL INSTANCE VAR though
			
			//System.out.println("# Cmds = "+ _commands.size());
			Vector newCommands = new Vector();

			long time; 
			time = System.currentTimeMillis();
			out.print(" " + time);

			
			
			for (int j=0; j < _commands.size(); j++) { //for each command in the UI
				
				Command sourceCmd = (Command)_commands.elementAt(j);  //need at least one b/c using the varibles below for it
				//Command sourceCmd = (Command)cmdEnum.nextElement();  //need at least one b/c using the varibles below for it
				String sourceCmdName = sourceCmd.displayName;
				
				//boolean found = false;
				Method aM = ((Method)metHash.get(sourceCmdName));
				if ( aM != null) {
					//go ahead and retarget
					//found = true;
					//System.out.println("share -"+sourceCmdName+"-");	
					if (targRealClass != componentObj.getClass()) {
						
						//Method newMeth = uiBean.getMethod(target, sourceCmd.getMethod().getName()); //might be issue with capitalization
						//System.out.println("Looking for " + sourceCmdName);
						
						//System.out.println("Looking for " + sourceCmdName + " go method " + newMeth.toString());
						//sourceCmd.setTargetObject(target, newMeth);
						//((Method)(metHash.get(sourceCmd.getMethod().getName()))).getName()
						sourceCmd.setTargetObject(target, aM); 
					}
					else	
						sourceCmd.setTargetObject(target);
					newCommands.addElement(sourceCmd);	//instead of doing this trick replacing biz just create a new version					
				}
				
				//_commands.addElement(sourceCmd); //replace the previous comamnd object
				else { //not shared
					sourceCmd.getButton().setEnabled(false);


					//System.out.println("don't share -"+sourceCmdName+"-");
					//cmdsToDel.addElement(sourceCmd);  //this will only point to the object so dont stress mem?
				}
			}//end for 
			
			time = System.currentTimeMillis();
			out.print(" " + time);
			Thread.sleep(30000);



		
		//	buttonPanel.setLayout(new GridLayout(  (_commands.size()+unsharedMeths.size()) /7,7));
			
			time = System.currentTimeMillis();
			out.print(" " + time);
		
			for (int s= 0; s < unsharedMeths.size(); s++) {
				
				String targetC = (String)unsharedMeths.elementAt(s);
				Method m = ((Method)metHash.get(targetC));
				MethodDescriptor md = new MethodDescriptor(m);
				md.setDisplayName(targetC);
				//System.out.println("adding -" + targetC+"-");
				Command newCmd = new Command(oeFrame, target, md,  m );
				//newCmd.setDisplayName(uiGenerator.beautify(md.getDisplayName()));

				JButton cmdBtn = new JButton(targetC);
				//note that objectEditor takes an additional step to put a '...' if a method
				//requires parameters to elude to the dialog box that follows.  
				//not gonna do this yet.	
				
				//nameToComponent.put(targetC, cmdBtn);
				cmdBtn.setMargin(new Insets(0,0,0,0));
				//cmdBtn.setMaximumSize(new Dimension(2,2));
				cmdBtn.addActionListener(newCmd);
				newCmd.setButton(cmdBtn);

				buttonPanel.add(cmdBtn);  
			}

			time = System.currentTimeMillis();
			out.print(" " + time);
			Thread.sleep(30000);

			primAdapters.remove(this.componentObj);  //this source object connections are no longer valid so let's remove them
			
			Vector newprimAdapters  = new Vector();  //will store new ones for the target and then replace ref. on this object 
			Vector newobjWidgets  = new Vector();				   

			time = System.currentTimeMillis();
			out.print(" " + time);


			for (int y = 0; y < _primAdapters.size(); y++) {  //for each primAdapter
				
				uiPrimitiveAdapter pAdapter = (uiPrimitiveAdapter)_primAdapters.elementAt(y); //get the prim adapter
				
				//uiPrimitiveAdapter pAdapter = (uiPrimitiveAdapter)primEnum.nextElement(); //get the prim adapter
				uiGenericWidget sourceWid = pAdapter.getGenericWidget();  //get its assoc  widget
				String sourceWidName = sourceWid.getLabel(); //and name				
				//System.out.println("-"+sourceCmdName+"-");
				
				//boolean found = false;
				
				//below shows we need shareProps because it serves as type checking 
				if (sharedProps.get(sourceWidName) != null) {  //the formatting of sourcewid and strngs in prop hash might not be the sam
					//go ahead and retarget
					//found = true;
					//System.out.println("share -"+sourceWidName+"-");	
					String retargProp = pAdapter.getPropertyName();
					
					
					char chars[] = retargProp.toCharArray();
					chars[0] = Character.toUpperCase(chars[0]);
					retargProp = new String(chars);			
					
					//so i'm just trying to get its getter
					Method getter = uiBean.getGetterMethod(targRealClass, retargProp);
					pAdapter.setPropertyReadMethod(getter);
					//System.out.println(sourceAdapter.getPropertyReadMethod().getName());
					
					Method setter = uiBean.getSetterMethod(targRealClass, retargProp);
					pAdapter.setPropertyWriteMethod(setter);
					
					
					Object obj = getter.invoke(target,null);
					
					//System.out.println("value is" + obj.toString());
					
					pAdapter.getParentAdapter().setRealObject(target); //this may have a certain importance when doing structured types
					pAdapter.getParentAdapter().setViewObject(target);//added the below since it seems like it should be needed 
					pAdapter.getParentAdapter().setValue(target);    //as I did it for the primitive properties the object contains below
					
					uiGenerator.setAdapterAttributesPublic(pAdapter, obj ,target, retargProp);
					uiGenerator.linkPropertyToAdapter(target,retargProp,pAdapter);
					if (obj != null){						
						if (obj instanceof uiPrimitive) {

							((uiPrimitive) obj).addObjectValueChangedListener(pAdapter);
						}


						
						pAdapter.setViewObject(obj);
						pAdapter.setRealObject(obj);
						pAdapter.setValue(obj);
						pAdapter.implicitRefresh(); //should update the UI...i think that's what I tried to do below
						//in the line below from pre-proposal time
						//oeFrame.deepElide(sourceAdapter);
						
						newprimAdapters.addElement(pAdapter);
						newobjWidgets.addElement(pAdapter.getGenericWidget());
					}
				}//if they share it
				
				//_commands.addElement(sourceCmd); //replace the previous comamnd object
				else { //not shared
					//sourceWid.setEnabled(false);
					sourceWid.getComponent(0).setEnabled(false);  //assuming primitive property display is 2
					
				}//end else
			}//end for all primadapt
			
			time = System.currentTimeMillis();
			out.print(" " + time);
			Thread.sleep(30000);
			
			//statusPanel.setLayout(new GridLayout(  (_commands.size()+unsharedMeths.size()) /4,4));
			time = System.currentTimeMillis();
			out.print(" " + time);
			this.name = target.toString();
			if (unsharedProps.size() > 0) { //then we have some unshared properties
				
				//statusPanel.setLayout(new GridLayout(((_objWidgets.size()+ unsharedProps.size()) /4), 4));
				JPanel statusPanel = new JPanel(new GridLayout((unsharedProps.size() + sharedProps.size()), 1));
				
				bus.uigen.uiGenerator.generateUIProperties(oeFrame,target,null,null,name,sharedProps);
				
				bus.uigen.uiGenerator.resetWidgets(); //refresh the vector of widgets/properties for next possible time
				bus.uigen.uiGenerator.resetAdapters(); 

				Vector  padapt =  bus.uigen.uiGenerator.getPrimAdapters(target);    //(Vector)sWidgets.get(target);
				Vector widgets = bus.uigen.uiGenerator.getStateWidgets(target);
				for (int u = 0; u < padapt.size(); u++)  {//append new widgets/primadapts to new"lists"
					newprimAdapters.addElement(padapt.elementAt(u));  // two vecs should be same size
					newobjWidgets.addElement(widgets.elementAt(u));
				
					statusPanel.add(((uiGenericWidget)(widgets.elementAt(u))));
					
				}
				
			}//end if unshared
			time = System.currentTimeMillis();
			out.print(" " + time);			
	
			Thread.sleep(30000);
			
			
			
			//close it up
			_commands = newCommands;
			_primAdapters = newprimAdapters;
			_objWidgets = newobjWidgets;
			
//			this.name = target.toString();
			this.componentObj = target;
			commandHash.put(target, _commands);//add the object to the hash
			//propHash.put(target, _objWidgets);
			
			primAdapters.put(target, _primAdapters);
			
			//singleControl.setBorder(BorderFactory.createTitledBorder(this.name));  //rename the border of this panel
			time = System.currentTimeMillis();
			out.print(" " + time);			

			singleControl.invalidate();  //refresh screen
			singleControl.validate(); 

			time = System.currentTimeMillis();
			out.println(" " + time);			
			
			Thread.sleep(30000);
			
			
		}//end try
		catch (Exception x) {x.printStackTrace();}
		
	}//end convertUI
	
	
	
	
	
	public void convertUITime (Object target, String tarClassName, Class targRealClass,  Vector targetMeth, Hashtable metHash, Hashtable commandHash,
						   Vector targetProp, Hashtable targetHash, Hashtable primAdapters, Hashtable propHash,
						   uiFrame oeFrame, Hashtable sharedMeths, Hashtable sharedProps, Vector unsharedMeths,
						   Vector unsharedProps,
						   PrintWriter out, PrintWriter scout,PrintWriter nsout,PrintWriter addout,
						   PrintWriter outp, PrintWriter scoutp,PrintWriter nsoutp,PrintWriter addoutp, PrintWriter fini) {
		
		long time;
//		System.out.println("cvtime");
		System.currentTimeMillis();
		
		
		
		
		//   RIGHT NOW ITS JUST SET TO CALUCLATE CMD
		
				
		try {
			//this only converts the single device control panel's commands
			commandHash.remove(this.componentObj);  //need to remove the entry of commands for this object b/c it won't 
			//have such a UI as prevoius ... THIS SHOULD NOT REMOVE THE ACTUAL INSTANCE VAR though
			
			//System.out.println("# Cmds = "+ _commands.size());
			Vector newCommands = new Vector();

			
//to collect the times for removing or retargeting a command I am going to just encapsulate the loop below
//then make the objects the changed things...ie. i will use object that only share one command
//see the cost of retargeting that then make them not share any commands and see the cost of removing that

			
			//Thread.sleep(3000);
			
			for (int j=0; j < _commands.size(); j++) { //for each command in the UI
			
				Command sourceCmd = (Command)_commands.elementAt(j);  //need at least one b/c using the varibles below for it
				//Command sourceCmd = (Command)cmdEnum.nextElement();  //need at least one b/c using the varibles below for it
				String sourceCmdName = sourceCmd.displayName;
								
				//boolean found = false;
				Method aM = ((Method)metHash.get(sourceCmdName));
				
				
				if ( aM != null) {
						sourceCmd.setTargetObject(target, aM); 
						newCommands.addElement(sourceCmd);	//instead of doing this trick replacing biz just create a new version					
						
			}
				
				//_commands.addElement(sourceCmd); //replace the previous comamnd object
				else { //not shared
					//sourceCmd.getButton().setEnabled(false);
					buttonPanel.remove(sourceCmd.getButton());
					//System.out.println("don't share -"+sourceCmdName+"-");
					//cmdsToDel.addElement(sourceCmd);  //this will only point to the object so dont stress mem?
				}
			}//end for 
		
			for (int s= 0; s < unsharedMeths.size(); s++) {
				
				
				String targetC = (String)unsharedMeths.elementAt(s);
				Method m = ((Method)metHash.get(targetC));
				MethodDescriptor md = new MethodDescriptor(m);
				md.setDisplayName(targetC);
				//System.out.println("adding -" + targetC+"-");
				Command newCmd = new Command(oeFrame, target, md,  m );
				//newCmd.setDisplayName(uiGenerator.beautify(md.getDisplayName()));
				JButton cmdBtn = new JButton(targetC);
				//note that objectEditor takes an additional step to put a '...' if a method
				//requires parameters to elude to the dialog box that follows.  
				//not gonna do this yet.	
				
				//nameToComponent.put(targetC, cmdBtn);
				cmdBtn.setMargin(new Insets(0,0,0,0));
				//cmdBtn.setMaximumSize(new Dimension(2,2));
				cmdBtn.addActionListener(newCmd);
				newCmd.setButton(cmdBtn);
				buttonPanel.add(cmdBtn);  
				
			}
		//	time = System.currentTimeMillis();
		//	addout.println(" " + time);
			
			
			primAdapters.remove(this.componentObj);  //this source object connections are no longer valid so let's remove them
			//propHash.remove(this.componentObj);
			
			//RE Comment below - already have a list from the evaluation in Oposer
			//let's remove the none shared ones.
			//System.out.println("# Prims = "+_primAdapters.size());
			//Vector primsToDel = new Vector();
			//Vector widsToDel = new Vector();
			
			Vector newprimAdapters  = new Vector();  //will store new ones for the target and then replace ref. on this object 
			Vector newobjWidgets  = new Vector();				   

			time = System.currentTimeMillis();
			outp.print(" " + time);
				
			
			
			for (int y = 0; y < _primAdapters.size(); y++) {  //for each primAdapter

				uiPrimitiveAdapter pAdapter = (uiPrimitiveAdapter)_primAdapters.elementAt(y); //get the prim adapter
				
				//uiPrimitiveAdapter pAdapter = (uiPrimitiveAdapter)primEnum.nextElement(); //get the prim adapter
				uiGenericWidget sourceWid = pAdapter.getGenericWidget();  //get its assoc  widget
				Object currentValue = pAdapter.getRealObject();
								
				String sourceWidName = sourceWid.getLabel(); //and name				
				//System.out.println("-"+sourceCmdName+"-");
				
				
				//boolean found = false;
				
				//below shows we need shareProps because it serves as type checking 
				if (sharedProps.get(sourceWidName) != null) {  //the formatting of sourcewid and strngs in prop hash might not be the sam
					//go ahead and retarget
					//found = true;
					//System.out.println("share -"+sourceWidName+"-");	
					
					String retargProp = pAdapter.getPropertyName();
					char chars[] = retargProp.toCharArray();
					chars[0] = Character.toUpperCase(chars[0]);
					retargProp = new String(chars);			
					
					//System.out.println("(**retargeting " + retargProp);
					
					
					//Object obj = (sourceAdapter.getPropertyReadMethod()).invoke(target,null); //get it's value (obj...see uigenerator.uiAddComponents(..)
					//try something else from above...i'm thinking the above doesn't work because
					//they don't have the same exact method signatures 
					//!!!!WILL THIS CREATE ISSUES IN UPDATING?  MAYBE IF I SET A NEW PROPERTY READ AND WRITE METHODS?
					
					//so i'm just trying to get its getter
					Method getter = uiBean.getGetterMethod(targRealClass, retargProp);
					pAdapter.setPropertyReadMethod(getter);
					//System.out.println(sourceAdapter.getPropertyReadMethod().getName());
					
					Method setter = uiBean.getSetterMethod(targRealClass, retargProp);
					pAdapter.setPropertyWriteMethod(setter);
					
					
					Object obj = getter.invoke(target,null);
					
					//System.out.println("value is" + obj.toString());
					
					
					pAdapter.getParentAdapter().setRealObject(target); //this may have a certain importance when doing structured types
					pAdapter.getParentAdapter().setViewObject(target);//added the below since it seems like it should be needed 
					//pAdapter.getParentAdapter().setValue(target);    //as I did it for the primitive properties the object contains below
					//plus if you look in implicitRefresh it this adapter will use the parent adapter's
					//value/viewObject/RealObject i.e. target
					//THUS THE ABOVE ASSUME A TWO LEVEL OBJECT AND IT DOESN'T GO DEEPER THAN THE ACTUAL OBJECT AND ITS PRIMITIVE SUBJOBJECTS
					
					
					//i.e. the parent of the individual vector elements might be the vector not the object containing the object
					//then the vectors parent is the realobject 
					//so a type checking might need to be done
					
					uiGenerator.setAdapterAttributesPublic(pAdapter, obj ,target, retargProp);
					uiGenerator.linkPropertyToAdapter(target,retargProp,pAdapter);
					if (obj != null){						
						if (obj instanceof uiPrimitive) {

							((uiPrimitive) obj).addObjectValueChangedListener(pAdapter);
						}

					//if (obj.toString().equals
						
						//System.out.print("targ value is" + obj.toString());
						//System.out.println("  source value is" + currentValue.toString());
						//System.out.println();

						
	//will comment the if statement below because it makes the check uneven...sometimes change value displayed and sometimes
	//don't chances are values will be different so by default change automatically.
	//will make the constant time in the data collection					
	//					if (!obj.toString().equals(currentValue.toString())) {
						pAdapter.setViewObject(obj);
						pAdapter.setRealObject(obj);
						pAdapter.setValueFast(obj);
						//pAdapter.implicitRefresh(); //should update the UI...i think that's what I tried to do below
						//in the line below from pre-proposal time
						//oeFrame.deepElide(sourceAdapter);
						//implicit refresh notneeded.
						
					//	}
						newprimAdapters.addElement(pAdapter);
						newobjWidgets.addElement(pAdapter.getGenericWidget());
					}
								
				}//if they share it
				
				//_commands.addElement(sourceCmd); //replace the previous comamnd object
				else { //not shared
					//sourceWid.setVisible(false);			
					
					//sourceWid.getComponent(0).setEnabled(false);  //assuming primitive property display is 2
					//sourceWid.setEnabled(false);
					
					
					
					statusPanel.remove(sourceWid);
					
					//stuff w/ the UI when actually removing it.
					
					//cmdsToDel.addElement(sourceCmd);  //this will only point to the object so dont stress mem?
				}//end else
			}//end for all primadapt
		
			time = System.currentTimeMillis();
			outp.println(" " + time);
			
				
			//Thread.sleep(3000);
			


			//statusPanel.setLayout(new GridLayout(  (_commands.size()+unsharedMeths.size()) /4,4));
			this.name = target.toString();
			if (unsharedProps.size() > 0) { //then we have some unshared properties
		
		//	time = System.currentTimeMillis();
		//	addoutp.print(" " + time);

				bus.uigen.uiGenerator.generateUIProperties(oeFrame,target,null,null,name,sharedProps);
				
				bus.uigen.uiGenerator.resetWidgets(); //refresh the vector of widgets/properties for next possible time
				bus.uigen.uiGenerator.resetAdapters(); 

				Vector  padapt =  bus.uigen.uiGenerator.getPrimAdapters(target);    //(Vector)sWidgets.get(target);
				Vector widgets = bus.uigen.uiGenerator.getStateWidgets(target);
				
				for (int u = 0; u < padapt.size(); u++)  {//append new widgets/primadapts to new"lists"
					newprimAdapters.addElement(padapt.elementAt(u));  // two vecs should be same size
					newobjWidgets.addElement(widgets.elementAt(u));
					
					statusPanel.add(((uiGenericWidget)(widgets.elementAt(u))));
					
					
				}
				
		
				//time = System.currentTimeMillis();
				//addoutp.println(" " + time);

				
			}//end if unshared
			
			time = System.currentTimeMillis();
			fini.print(" " + time);
			
			//close it up
			_commands = newCommands;
			_primAdapters = newprimAdapters;
			_objWidgets = newobjWidgets;
			
			//this.name = target.toString();
			this.componentObj = target;
			commandHash.put(target, _commands);//add the object to the hash
			//propHash.put(target, _objWidgets);
			
			primAdapters.put(target, _primAdapters);
			componentClassName = tarClassName;
			
			//singleControl.setBorder(BorderFactory.createTitledBorder(this.name));  //rename the border of this panel
			
			
			singleControl.invalidate();  //refresh screen
			singleControl.validate(); 
			
			time = System.currentTimeMillis();
			fini.println(" " + time);
			
			
		}//end try
		catch (Exception x) {x.printStackTrace();}
		
	}//end convertUI
	
*/	

	
	
}//end class
