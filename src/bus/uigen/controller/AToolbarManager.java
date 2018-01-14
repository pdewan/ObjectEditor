package bus.uigen.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import util.trace.uigen.ToobarButtonAdded;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.compose.ComponentPanel;
import bus.uigen.controller.menus.VirtualMethodAction;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.view.ATopViewManager;
import bus.uigen.widgets.ButtonSelector;
import bus.uigen.widgets.LabelSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.ToolBarSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualLabel;
import bus.uigen.widgets.VirtualToolBar;
import bus.uigen.widgets.events.VirtualActionListener;

public class AToolbarManager {
	uiFrame frame;
	public static int toolbarCount = 0;
	//boolean toolBarInCenter = false;
	boolean toolPanelAsToolBar = false;
	public AToolbarManager(uiFrame theFrame) {
		frame = theFrame;
	}
	/*
	private void createToolBar() {
		
		//we want a toolbar with these special additions to be added only the 1st time
		//also we do not want the toolbars to be vertical if the object doesn't have any defined 
		//toolbars other than native one.
		
		//we can check somehow if there are several ToolGroups that have been customized and
		//then create another toolbar panel layer so that the one doesn't go off the screen.
		//would frame be in classdescriptor or the beaninfo file of the object.
		
		
		//you may also be able to set the layout for the toolbar (see JToolbar.setLayout) which
		//may then help in arranging framee buttons in a more pleasant manner if too many of ther
		//are there for frame current design.
		
		if (uiFrame.toolbarCount == 0) {
			
			
			GridLayout toolGrid = new GridLayout(1,toolbars.size(),0,0);
			//FlowLayout toolGrid  = new FlowLayout(FlowLayout.LEFT,0,0);
			
			//toolGrid.setHgap(0);
			//toolGrid.setVgap(0);
			
			toolPanel = new JPanel(toolGrid);
			frame.isOEMainFrame = true;
		}
		else 		
			toolPanel = new JPanel(new FlowLayout());
		
		uiFrame.toolbarCount++;  //new toolbar is created
		//toolBar = new JToolBar(javax.swing.SwingConstants.VERTICAL);
		toolBar = new JToolBar(javax.swing.SwingConstants.HORIZONTAL);
		
		
		//toolBar   = new JPanel();
		//toolBar.setVisible(false);
		toolBarIsVisible = true; //lets make  it visible by default
		//toolBar.setName(frame.TOOLBAR_PANEL_NAME);
		
		//the idea is that the base toolbar is created already.  so frame put in early to the hashtable of toolbars
		
		toolbars.put(frame.TOOLBAR_PANEL_NAME, toolBar);
		//frame.showToolBar();
		
	}
	*/
	//JPanel toolPanel ;
	
	
	//JToolBar toolBar;
	VirtualToolBar toolBar;
	VirtualContainer toolPanel;
	VirtualContainer manualToolbar;
	Hashtable<String, VirtualToolBar> toolbars = new Hashtable();
	boolean hasManualToolbar = false;
	public void useManualToolbar (boolean newVal) {
		hasManualToolbar = newVal;
	}
	public VirtualContainer getToolBar () {
		return toolBar;
	}
	public VirtualContainer getToolPanel() {
		return toolPanel;
	}
	
	public VirtualComponent getToolBarOrPanel() {
		if (toolPanelAsToolBar)
			return getToolPanel();
		else
			return getToolBar();
		
	}
	
	public void setToolPanelAsToolBar(boolean newVal) {
		toolPanelAsToolBar = newVal;
	}
	
	/*
	public Container createManualToolbar() {		
		Container buttons = ComponentPanel.createButtonPanel(uiGenerator.getCommands(frame.getTopAdapter().getRealObject()));
		return buttons;
		//frame.add(buttons, toolbarOrientation());
		//return ;
		
	}
	*/
	/*
	public void addManualToolbar() {		
		Container buttons = ComponentPanel.createButtonPanel(uiGenerator.getCommands(frame.getTopAdapter().getRealObject()));
		frame.add(buttons, toolbarOrientation());
		return ;
		
	}
	*/
	
	String toolbarOrientation () {
		if (frame.getComponentCount() != 0)  return BorderLayout.NORTH;
		//if (drawPanelIsVisible ||   mainScrollPaneIsVisible || treePanelIsVisible) return BorderLayout.NORTH;
		return BorderLayout.CENTER;
	}
	public VirtualContainer createManualToolbar() {
		if (manualToolbar != null) return manualToolbar;
		manualToolbar  = ComponentPanel.createButtonPanel(uiGenerator.getCommands(frame.getTopAdapter().getRealObject()));
		return manualToolbar;
		
	}
	public boolean hasManualToolbar() {
		return hasManualToolbar;
	}
	 //static int toolbarCount = 0;
public void createToolBar() {
		
		//we want a toolbar with these special additions to be added only the 1st time
		//also we do not want the toolbars to be vertical if the object doesn't have any defined 
		//toolbars other than native one.
		
		//we can check somehow if there are several ToolGroups that have been customized and
		//then create another toolbar panel layer so that the one doesn't go off the screen.
		//would this be in classdescriptor or the beaninfo file of the object.
		
		
		//you may also be able to set the layout for the toolbar (see JToolbar.setLayout) which
		//may then help in arranging thise buttons in a more pleasant manner if too many of ther
		//are there for this current design.
		
		if (toolbarCount == 0) {
			
			
			GridLayout toolGrid = new GridLayout(1,toolbars.size(),0,0);
			//FlowLayout toolGrid  = new FlowLayout(FlowLayout.LEFT,0,0);
			
			//toolGrid.setHgap(0);
			//toolGrid.setVgap(0);
			
			//toolPanel = new JPanel(toolGrid);
			toolPanel = PanelSelector.createPanel();
			frame.addKeyListener(toolPanel);
			toolPanel.setName("ToolPanel (createToolBar)");
			toolPanel.setLayout(toolGrid);
			frame.isOEMainFrame = true;
		}
		else 	{	
			//toolPanel = new JPanel(new FlowLayout());
			toolPanel = PanelSelector.createPanel();
			toolPanel.setLayout(new FlowLayout());
		}
		
		toolbarCount++;  //new toolbar is created
		//toolBar = new JToolBar(javax.swing.SwingConstants.VERTICAL);
		//toolBar = new JToolBar(javax.swing.SwingConstants.HORIZONTAL);
		toolBar = ToolBarSelector.createToolBar(javax.swing.SwingConstants.HORIZONTAL);
		toolBar.setName("ToolBar(createToolBar)");
		
		
		
		//toolBar   = new JPanel();
		//toolBar.setVisible(false);
		//AVM toolBarIsVisible = true; //lets make  it visible by default
		//toolBar.setName(this.TOOLBAR_PANEL_NAME);
		
		//the idea is that the base toolbar is created already.  so this put in early to the hashtable of toolbars
		
		toolbars.put(ATopViewManager.TOOLBAR_PANEL_NAME, toolBar);
		//this.showToolBar();
		
	}
//Vector methodActions = new Vector();
//get a toolbar...if it's not already made, then make a new one and add it.	
//private JToolBar getToolBar(String name)   {

private VirtualToolBar getToolBar(String name)   {

	//JToolBar tb   = (JToolBar) toolbars.get(name);
	VirtualToolBar tb   =  toolbars.get(name);
	if (tb == null)   { //then have to make one
		//if (name.equals(methodsMenuName))   {
		//tb = new JToolBar(javax.swing.SwingConstants.VERTICAL);
		//tb = new JToolBar(javax.swing.SwingConstants.HORIZONTAL);
		tb = ToolBarSelector.createToolBar(javax.swing.SwingConstants.HORIZONTAL);
		//toolBar   = new JPanel();
		//toolBar.setVisible(false);
		
		tb.setName(name);
		
		//the idea is that the base toolbar is created already.  so this put in early.
		
		toolbars.put(name, tb);

		//menu = new uiExtendedMenu(name);
		//menus.put(name, menu);
		//menuBar.add(menu);
		
		//toolPanel.add(new Label(name));
		//toolPanel.add(tb);

		//Box toolGroup = new Box(javax.swing.BoxLayout.Y_AXIS);
		//BoxLayout tGLay = new BoxLayout(toolGroup, javax.swing.BoxLayout.Y_AXIS);									  
		//toolGroup.setLayout(tGlay);

		//JPanel toolGroup = new JPanel();
		VirtualContainer toolGroup = PanelSelector.createPanel();
		if (toolGroup.getPhysicalComponent() instanceof Container) {
		BoxLayout tGLay = new BoxLayout((Container) toolGroup.getPhysicalComponent(), javax.swing.BoxLayout.Y_AXIS);									  
		toolGroup.setLayout(tGLay);
		}
		//toolGroup.setBorder(BorderFactory.createEtchedBorder());
		toolGroup.setBorder(BorderFactory.createRaisedBevelBorder());
		
		
		//JLabel tbname = new JLabel(name.toUpperCase(),javax.swing.SwingConstants.CENTER);
		VirtualLabel tbname = LabelSelector.createLabel(name.toUpperCase());
		tbname.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		tbname.setVerticalAlignment(SwingConstants.TOP);
		toolGroup.add(tbname);
		
		//toolGroup.add(new Label(name));
		toolGroup.add(tb);
		
		toolPanel.add(toolGroup);
	}
	return tb;
}

public void checkPre () {
	try {
	Vector<VirtualMethodAction> methodActions = frame.getBrowser().getMenuAdapter().getMethodActions();
	for (int i = 0; i < methodActions.size(); i++) {
		methodActions.elementAt(i).checkPre();
	}
	} catch (Exception e) {
		
		System.out.println("");
		
	}
	/*
	Enumeration<VirtualToolBar> elements = toolbars.elements();
	while (elements.hasMoreElements()) {
		VirtualToolBar nextToolbar = elements.nextElement();
		checkPre(nextToolbar);
	}
	*/
	
}
/*
public void checkPre (VirtualToolBar theToolBar) {
	VirtualComponent[] components = theToolBar.getComponents();
	for (int i = 0; i < components.length; i++) {
		VirtualComponent component = components[i];
		if (component instanceof VirtualMethodAction) {
			((VirtualMethodAction) component).checkPre();
		}
	}
	
}
*/

public VirtualButton addToolBarButton(String label,   Icon icon, MethodProxy method, String place_toolbar, int pos) {
	return addToolBarButton (null, label, icon, method, place_toolbar, pos );
}
//
// Add a ToolBar item
//
//Hashtable<VirtualToolBar, VirtualMethodAction> toolBarToMethodAction = new Hashtable();
public VirtualButton addToolBarButton(Object targetObject, String label,   Icon icon, MethodProxy method, String place_toolbar, int pos) {
	//toolBar.setVisible(true);
	//showToolBar();
	//toolBar.setFloatable(true);
	// why do we need  this line, I am commenting it out
	if (frame.getBrowser().getMenuAdapter() == null) return null; 
	//Vector methodActions = browser.getTopAdapter().getMethodActions();
	Vector methodActions = frame.getBrowser().getMenuAdapter().getMethodActions();
	VirtualMethodAction methodAction   = new VirtualMethodAction(frame, targetObject, label, icon, method);
	//return toolBar.add(new MethodAction(this, label, icon, method));
	VirtualMethodAction existing   = methodAction.getDuplicate(methodActions);
	if (existing != null)   { 
		existing.addMethod(method);
		return null;
	}   else {
		methodActions.addElement(methodAction);
		
		
		
		//here you should get the toolbar.
		//JToolBar inTB = getToolBar(place_toolbar);  //i guess if it's not there already...then add it.
		VirtualToolBar inTB = getToolBar(place_toolbar);  //i guess if it's not there already...then add it.
		//JButton button  = toolBar.add(methodAction);   //Toolbar here.
		//JButton button = null;
		//if (pos == -1)
			//JButton button = inTB.add(methodAction);   //Toolbar here.
			VirtualButton button = inTB.add(methodAction);   //Toolbar here.
			
		
		methodAction.setButton(button);
		ToobarButtonAdded.newCase(targetObject, label, icon, method, place_toolbar, pos, this);
		return  button;
	}
}
void printToolbarButtons() {
	VirtualComponent[] comps   = toolBar.getComponents();
	System.out.println("Toolbar Buttons");
	for (int i=0;   i<comps.length; i++)
		System.out.println(((VirtualButton)   comps[i]).getLabel());
}
public void removeToolBarButtons(ObjectAdapter adapter)   {   
	System.out.println("removing buttons of" + adapter.getMethodActions());     
	printToolbarButtons();
	Vector methodActions = adapter.getMethodActions();
	for (int i=0;   i < methodActions.size(); i++) {
		VirtualMethodAction ma =   (VirtualMethodAction) methodActions.elementAt(i);
		//System.out.print(" rembut" + ma.getButton().getLabel());
		//System.out.print("index" + indexOf(toolBar.getComponents(),   ma.getButton()));
		toolBar.remove(frame.indexOf(toolBar.getComponents(), ma.getButton()));
		//toolBar.remove(ma.getButton());
	}
	
}
public void addToolBarButtons(ObjectAdapter   adapter) {
	//System.out.println("adding buttons of" + adapter.getMethodActions());
	System.out.println("Toolbar Buttons");
	Vector methodActions = adapter.getMethodActions();
	for (int i=0;   i < methodActions.size(); i++) {
		VirtualMethodAction ma =   (VirtualMethodAction) methodActions.elementAt(i);
		//System.out.print("addbut" + ma.getButton().getLabel());
		toolBar.add(ma.getButton());
	}
	//validate();
	//htoolBar.setVisible(false);
	toolBar.setVisible(true);
	
}
//
// Add a UI frame   command
//
//JButton forwardButton, backButton;
VirtualButton forwardButton, backButton;
public void addUIFrameToolBarButton(String label,   Icon icon) {
	addUIFrameToolBarButton(label, icon, frame);
	/*
	if (toolBarButtons.contains(label)) return;
	//showToolBar();
	//toolBar.setVisible(true);
	//toolBar.setFloatable(true);
	JButton button = new JButton(label, icon);
	button.addActionListener(this);   
	toolBarButtons.addElement(label);   
	if (label   == "Save"){
	setSaveButton(button);
	toolBar.add(button);
	}    else if (label == FORWARD_ADAPTER_NAME) {
	forwardButton   = button;
	toolBar.add(button);
	}   else if (label == BACK_ADAPTER_NAME) {
	backButton = button;
	toolBar.add(button);
	}   else
	toolBar.add(button);
	//toolBar.removeAll();
	*/
}
//public JButton addUIFrameToolBarButton(String   label, Icon icon, ActionListener listener) {
public VirtualButton addUIFrameToolBarButton(String   label, Icon icon, VirtualActionListener listener) {	
	System.out.println("Adding Button " + label);
	if (toolBarButtons.contains(label)) return null;
	//showToolBar();
	//toolBar.setVisible(true);
	//toolBar.setFloatable(true);
	//JButton button = new JButton(label, icon);
	VirtualButton button = ButtonSelector.createButton(label);
	button.setIcon(icon);
	if (listener != null)
		button.addActionListener(listener);   
	toolBarButtons.addElement(label);   
	if (label   == frame.SAVE_COMMAND){
		frame.setSaveButton(button);
		toolBar.add(button);
	}    else if (label == frame.FORWARD_ADAPTER_NAME) {
		forwardButton   = button;
		toolBar.add(button);
	}   else if (label == frame.BACK_ADAPTER_NAME) {
		backButton = button;
		toolBar.add(button);
	}   else
		toolBar.add(button);
	//toolBar.removeAll();
	return button;
}
public void setForwardEnabled (boolean enabled) {
	
	uiFrame.setButtonEnabled(forwardButton, enabled);
}
public   void setBackEnabled (boolean enabled) {
	
	uiFrame.setButtonEnabled(backButton, enabled);
}
//static Object[]   toolBarFrameButtonsArray = {BACK_ADAPTER_NAME, FORWARD_ADAPTER_NAME, DEEP_ELIDE_4, "Refresh" , UPDATE_ALL_COMMAND}; 
static Object[] toolBarFrameButtonsArray = {};
Vector toolBarButtons   = new Vector();
static java.util.Vector toolBarFrameButtons =   uiGenerator.arrayToVector(toolBarFrameButtonsArray);

public static   void addUIFrameToolBarButtons(uiFrame topFrame) {
	for (Enumeration e = toolBarFrameButtons.elements(); e.hasMoreElements();)
		topFrame.addUIFrameToolBarButton((String)   e.nextElement(), null);
	topFrame.hideToolBar();
}
public int toolbarCount() {
	return toolbarCount;
}
//Container manualToolbar;
public VirtualComponent getManualToolbar() {
	return manualToolbar;
}

public VirtualButton addUIGenToolBarButton(String label, Icon icon,   Object obj) {
	//showToolBar();
	toolBar.setVisible(true);
	return toolBar.add(new GenAction(label, icon, obj));
}
	
	public void addToBottomPanel(VirtualContainer toadd) {  
		
		//toolPanel.add(toadd);   //for now let's do a simple add at end of flow
		
		frame.add(toadd, BorderLayout.SOUTH);
		
		if (toadd != null)
			System.out.println("added a toolpanel to orig");
		
	}

	public void addToMiddlePanel(VirtualContainer toadd) {  
		
		//toolPanel.add(toadd);   //for now let's do a simple add at end of flow
		
		frame.add(toadd, BorderLayout.CENTER);
		
		if (toadd != null)
			System.out.println("added a toolpanel to mid - orig");
		
	}
	
	

}
