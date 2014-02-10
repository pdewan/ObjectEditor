package bus.uigen.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.compose.ButtonCommand;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.widgets.ADelegatingVirtualComponent;
import bus.uigen.widgets.ButtonSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualTextField;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;


public class DecIncWidget extends /*JPanel*/ ADelegatingVirtualComponent implements VirtualComponent {
	//JTextField jtxt;
	VirtualTextField jtxt;
	VirtualContainer container;
	//JScrollBar jScroll;
	//JPanel wijPane;
	VirtualContainer wijPane;
	//JButton up;
	VirtualButton up;
	//JButton down;
	VirtualButton down;
	ModelClass modelObject = null;
	int decincunit;
	Object obj;
	Object parentObject;
	ObjectAdapter objectAdapter;
	ObjectAdapter parentAdapter;
	MethodProxy incrementer, decrementer;
	boolean hasIncrementerObject;
	boolean hasIncrementerParentObject;
	
	
	/*
	public VirtualContainer getWidget() {
		return container;
	}
	*/
	//JButton getIncrementerButton () {
	VirtualButton getIncrementerButton () {
		Icon upIcon = new ImageIcon("up.gif");
		/*
		JButton up = new JButton();
		up.setIcon(upIcon);
		*/
		VirtualButton up = ButtonSelector.createButton();
		up.setIcon( upIcon);
		up.setVerticalAlignment(SwingConstants.TOP);
		return up;
		
	}
	//JButton getDecrementerButton () {
	VirtualButton getDecrementerButton () {
		Icon downIcon = new ImageIcon("dn.gif");
		//JButton down = new JButton();
		VirtualButton down = ButtonSelector.createButton();
		down.setIcon(downIcon);
		down.setVerticalAlignment(SwingConstants.TOP);
		return down;
		
	}
	
	
	void addButtons (VirtualContainer c, VirtualButton up, VirtualButton down) {
		//JPanel bPanel = new JPanel(new GridLayout(2,1));
		VirtualContainer bPanel = PanelSelector.createPanel();
		bPanel.setLayout(new GridLayout(2,1));
		bPanel.add(up);
		bPanel.add(down);
		c.add(bPanel, BorderLayout.EAST);
		
	}
	public DecIncWidget() {
		init ("", 5, 1);		
	}
	
	public DecIncWidget(String text, int numColumns, int _decincunit) {
		init (text, numColumns, _decincunit);
	}
		
		
public void init (String text, int numColumns, int _decincunit) {
		
		decincunit = _decincunit;
		container = PanelSelector.createPanel();
		super.init(container);
		//wijPane = new JPanel();
		//wijPane = this;
		wijPane = container;
		//wijPane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		wijPane.setLayout(new BorderLayout());
		
		//wijPane.setBackground(Color.gray);

		//jtxt = new JTextField(text, numColumns);
		jtxt = TextFieldSelector.createTextField(text);
		jtxt.setColumns(numColumns);
		
		//new JTextField(text, numColumns);
		wijPane.add(jtxt);

		//JPanel bPanel = new JPanel(new GridLayout(2,1));
		/*
		up = new JButton();
		Icon upIcon = new ImageIcon("up.gif");
		up.setIcon(upIcon);
		*/
		up = getIncrementerButton();

			
		up.addActionListener(new VirtualActionListener() {
			public void actionPerformed(VirtualActionEvent e) {
				try {
					if (hasIncrementerObject) {
						Object[] args = {};
						//incrementer.invoke(obj, args);
						MethodInvocationManager.invokeMethod(objectAdapter.getUIFrame(),
								   objectAdapter.getValue(),
								   incrementer,
								   args);
						
					} else if (hasIncrementerParentObject) {
						//Object[] args = {objectAdapter.getValue()};
						Object[] args = {};
						//incrementer.invoke(parentObject,  args);
						MethodInvocationManager.invokeMethod(objectAdapter.getUIFrame(),
								   parentObject,
								   incrementer,
								   args);
						
					} else
					
					if (modelObject != null) { //don't do anything until there is a model
						//System.out.println("the next value is: "+ modelObject.getNextValue(jtxt.getText()));
						jtxt.setText(modelObject.getNextValue(jtxt.getText(), decincunit));
					
						jtxt.postActionEvent(); //since setText doesn't fire an event once you
						//incrememnt the value...hence this could cause the jtf inside the Adapter to
						//fire an event. subsequently calling actionperformed inside the adapter since
						//it's linked.
					}//mod!null
					
				}//try
				
				catch(Exception exe) {}
				
			}
			});
		
		up.setMargin(new Insets(0,0,0,0));
		//up.setVerticalAlignment(SwingConstants.TOP);
		up.setMaximumSize(new VirtualDimension(0,0));
		//bPanel.add(up);
		/*
		down = new JButton();
		Icon downIcon = new ImageIcon("dn.gif");
		down.setIcon(downIcon);
		*/
		down = getDecrementerButton();
		
		
		down.addActionListener(new VirtualActionListener() {
			public void actionPerformed(VirtualActionEvent e) {
				try {
					if (hasIncrementerObject) {
						Object[] args = {};
						//decrementer.invoke(obj, args);
						MethodInvocationManager.invokeMethod(objectAdapter.getUIFrame(),
								objectAdapter.getValue(),
								   decrementer,
								   args);
						
					} else if (hasIncrementerParentObject) {
						//Object[] args = {objectAdapter.getValue()};
						Object[] args ={};
						MethodInvocationManager.invokeMethod(objectAdapter.getUIFrame(),
								   parentObject,
								   decrementer,
								   args);
						//decrementer.invoke(parentObject,  args);
						
					} else
					if (modelObject != null) {  //don't do anything until there is a model

				/*
						int newI = new Integer(jtxt.getText()).intValue();
						newI--;
						jtxt.setText(Integer.toString(newI));
					
				*/
						
						//the adapter should probably do the following since it has to 
						//eventually set the actuall objects value
						//can't assume order actionPerformeds so you actually passin getValue()?
						
						
						jtxt.setText(modelObject.getPreviousValue(jtxt.getText(), decincunit));
						//model should call the the translator to get the actual
						//value and perform the operation of incrememting and then
						//go back to the string type...types are retrieved from objectAdapter
						jtxt.postActionEvent();
					}//if mod.!null
				}//try
			catch(Exception exe) {}
			}
			});
		
		
		down.setMargin(new Insets(0,0,0,0));
		//down.setVerticalAlignment(SwingConstants.TOP);
		up.setMaximumSize(new VirtualDimension(0,0));
		//bPanel.add(down);
		//wijPane.add(bPanel);
		//wijPane.add(bPanel, BorderLayout.EAST);
		addButtons(wijPane, up, down);
		//this.add(wijPane);
		
	}//end const
	
	
	public void setModelObject(ModelClass _modelObject) {
		modelObject = _modelObject;
	}
	public static boolean isFileName (String string) {
		return string.endsWith(".gif");
	}
	/*
	public static void maybeChangeLabel (MethodDescriptor md, JButton b) {
		if (md == null) return;
			//MethodDescriptor md = cdIncDec.getMethodDescriptor(incrementer.getName());
			String iconFile = (String) md.getValue(AttributeNames.ICON);
			String label = (String) md.getValue(AttributeNames.LABEL);
			maybeChangeLabel (b, label, iconFile);
			
		
		
	}
	public static void maybeChangeLabel ( JButton b, String label, String iconFile) {
		
			if (iconFile == null && label!= null && isFileName(label))
				iconFile = label;
			if (iconFile != null) {
				 Icon icon = new ImageIcon(iconFile);
				 b.setIcon(icon);
				 b.setLabel("");
				 //return;
			}
			if (label != null)
				b.setLabel(label);
		
		
	}
	*/
	void maybeChangeLabels() {
		if (cdIncDec == null) return;
		ButtonCommand.maybeChangeLabel (cdIncDec.getMethodDescriptor(incrementer.getName()), up);
		ButtonCommand.maybeChangeLabel (cdIncDec.getMethodDescriptor(decrementer.getName()), down);
	}
	public void setObjectAdapter (ObjectAdapter newVal) {
		if (newVal == null) return ;
		objectAdapter = newVal;
		obj = newVal.getRealObject();
		if (obj == null) return;
		//Class childType = obj.getClass();
		ClassProxy childType = objectAdapter.getPropertyClass();
		if (childType == null)  childType = RemoteSelector.getClass(obj);
		hasIncrementerObject = hasIncrementerObject(newVal, childType);
		if (hasIncrementerObject) return;
		parentAdapter = newVal.getParentAdapter();
		while (parentAdapter != null && !hasIncrementerParentObject) {;
		hasIncrementerParentObject = hasIncrementerParentObject(parentAdapter, childType);
		parentAdapter = parentAdapter.getParentAdapter();
		}
		maybeChangeLabels();
		return;
		
	}
	
	boolean hasIncrementerObject (ObjectAdapter newVal, ClassProxy childType) {
		//if (newVal == null) return false;	
		/*
		obj = newVal.getRealObject();
		incrementer = uiBean.getIncrementer(obj.getClass());
		*/
		incrementer = IntrospectUtility.getIncrementer(childType);
		if (incrementer == null) return false;
		decrementer = IntrospectUtility.getDecrementer(childType);
		if (decrementer == null) return false;
		return true;
	}
	ClassDescriptorInterface cdIncDec;
	boolean hasIncrementerParentObject (ObjectAdapter newVal, ClassProxy childType) {
		//if (newVal == null) return false;		
		parentObject = newVal.computeAndMaybeSetViewObject();
		//parentObject = newVal.getRealObject();
		if (parentObject == null) return false;
		//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(parentObject.getClass(), newVal);
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(RemoteSelector.getClass(parentObject), newVal.getRealObject());
		//incrementer = uiBean.getChildIncrementer(parentObject.getClass(), childType);
		//incrementer = uiBean.getChildIncrementer(parentObject.getClass(), objectAdapter.getPropertyName());
		incrementer = IntrospectUtility.getChildIncrementer(cd, objectAdapter.getPropertyName());
		if (incrementer == null) return false;
		cdIncDec = cd;
		//ViewInfo cd = ClassDescriptorCache.getClassDescriptor(parentObject.getClass(), newVal);
		cd.setMethodAttribute(incrementer.getName(), AttributeNames.TOOLBAR, new Boolean(false));
		//decrementer = uiBean.getChildDecrementer(parentObject.getClass(), objectAdapter.getPropertyName());
		decrementer = IntrospectUtility.getChildDecrementer(cd, objectAdapter.getPropertyName());
		if (decrementer == null) return false;
		cd.setMethodAttribute(decrementer.getName(), AttributeNames.TOOLBAR, new Boolean(false));
		return true;
	}
	
	
	//public JTextField getJTextField() {
	public VirtualTextField getTextField() {
		return jtxt;
	}
	
	
	//public JButton getUp() {
	public VirtualButton getUp() {
		return up;
	}
			
	//public JButton getDown() {
		public VirtualButton getDown() {	
		return down;
	}
	
	public void setText(String newVal) {
		jtxt.setText(newVal);
	}
	
	public String getText() {
		return jtxt.getText();
	}
	
	public void setColumns(int cols) {
		jtxt.setColumns(cols);
	}
	
	public int getColumns() {
		return jtxt.getColumns();
	}
	
	public void setDecincunit(int unit) {
		decincunit = unit;
	}
	
	public int getDecincunit() {
		return decincunit;
	}
	
	public void setValue(int newVal) {
		jtxt.setText(String.valueOf(newVal));
	}
	
	public int getValue() {
		return new Integer(jtxt.getText()).intValue();
	}
	
	public String getStringValue() {
		return jtxt.getText();
	}
		
		
	
}

