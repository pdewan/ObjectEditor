
package bus.uigen;
import java.util.Hashtable;

import bus.uigen.adapters.CommandAndStatePanelAdapter;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.view.AClassWidgetPair;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualCheckBox;
import bus.uigen.widgets.VirtualComboBox;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualFormattedTextField;
import bus.uigen.widgets.VirtualLabel;
import bus.uigen.widgets.VirtualList;
import bus.uigen.widgets.VirtualPasswordField;
import bus.uigen.widgets.VirtualProgressBar;
import bus.uigen.widgets.VirtualRadioButton;
import bus.uigen.widgets.VirtualSlider;
import bus.uigen.widgets.VirtualTextArea;
import bus.uigen.widgets.VirtualTextField;
import bus.uigen.widgets.table.VirtualTable;
import bus.uigen.widgets.tree.VirtualTree;

// Implements a lookup table for default
// class names to component mappings

public class ComponentDictionary {
	static private Hashtable components = new Hashtable();

	static private Hashtable adapters = new Hashtable();

	static private Hashtable editors = new Hashtable();

	static private Hashtable widgets = new Hashtable();

	static private Hashtable objectAdapterToWidget = new Hashtable();
	
	static private Hashtable<ClassProxy, ClassProxy> widgetAdapterToWidget = new Hashtable();

	public static Object putComponent(String type, String widget) {
		return components.put(type, widget);
	}

	public static Object putAdapter(String component, String adapter) {
		return adapters.put(component, adapter);
	}
	
	static {
		registerDefaults();
	}

	public static void registerDefaults() {

		// Load defaults
		components.put("java.lang.Character", "bus.uigen.widgets.VirtualTextField");
		components.put("java.lang.String", "bus.uigen.widgets.VirtualTextField");
		components.put("java.lang.Boolean", "bus.uigen.widgets.VirtualCheckBox");
		components.put("java.lang.Number", "bus.uigen.widgets.VirtualTextField");
		//components.put("java.lang.Integer", "javax.swing.JTextField");
		components.put("java.lang.Integer", "bus.uigen.widgets.VirtualTextField");
		//components.put("java.lang.Integer", "javax.swing.JTextField");
		//components.put("java.lang.Double", "javax.swing.JTextField");
		components.put("java.lang.Double", "bus.uigen.widgets.VirtualTextField");
		components.put("java.lang.Byte", "bus.uigen.widgets.VirtualTextField");
		//components.put("java.util.Vector", "bus.uigen.widgets.VirtualPanel");
		//components.put("java.lang.Object", "javax.swing.JPanel");
		//components.put("java.lang.Object", "bus.uigen.widgets.VirtualPanel");
		//components.put("java.util.Hashtable", "bus.uigen.uiHashtableWidget");
		//components.put("java.util.Hashtable", "bus.uigen.widgets.VirtualPanel");
		components.put("java.net.URL", "javax.swing.JEditorPane");
		/*
		components.put(java.lang.Character.class),javax.swing.JTextField.class));
		components.put(java.lang.String.class),javax.swing.JTextField.class));
		components.put(java.lang.Boolean.class),javax.swing.JCheckBox.class));
		components.put(java.lang.Number.class),javax.swing.JTextField.class));
		//components.put(java.lang.Integer.class),javax.swing.JTextField.class));
		components.put(java.lang.Integer.class), ClassSelector.classProxy(VirtualTextField.class));
		//components.put(java.lang.Double.class),javax.swing.JTextField.class));
		components.put(java.lang.Double.class), ClassSelector.classProxy(VirtualTextField.class));
		components.put(java.lang.Byte.class),java.awt.TextField.class));
		components.put(java.util.Vector.class),javax.swing.JPanel.class));
		//components.put(java.lang.Object.class),javax.swing.JPanel);;
		components.put(java.lang.Object.class),javax.swing.JPanel.class));
		components.put(java.util.Hashtable.class),ClassSelector.classProxy(bus.uigen.uiHashtableWidget.class));
		components.put(java.util.Hashtable.class),javax.swing.JPanel.class));
		components.put(java.net.URL.class), javax.swing.JEditorPane.class));
		*/
		components.put(RemoteSelector.classProxy(java.lang.Character.class), RemoteSelector.classProxy(VirtualTextField.class));
		components.put(RemoteSelector.classProxy(java.lang.String.class), RemoteSelector.classProxy(VirtualTextField.class));
		components.put(RemoteSelector.classProxy(java.lang.Boolean.class), RemoteSelector.classProxy(VirtualCheckBox.class));
		components.put(RemoteSelector.classProxy(java.lang.Number.class), RemoteSelector.classProxy(VirtualTextField.class));
		//components.put(ClassSelector.classProxy(java.lang.Integer.class),javax.swing.JTextField.class));
		components.put(RemoteSelector.classProxy(java.lang.Integer.class), RemoteSelector.classProxy(VirtualTextField.class));
		//components.put(ClassSelector.classProxy(java.lang.Double.class),javax.swing.JTextField.class));
		components.put(RemoteSelector.classProxy(java.lang.Double.class), RemoteSelector.classProxy(VirtualTextField.class));
		components.put(RemoteSelector.classProxy(java.lang.Byte.class), RemoteSelector.classProxy(VirtualTextField.class));
		components.put(RemoteSelector.classProxy(java.util.Vector.class), RemoteSelector.classProxy(VirtualContainer.class));
		//components.put(ClassSelector.classProxy(java.lang.Object.class),javax.swing.JPanel);;
		components.put(RemoteSelector.classProxy(java.lang.Object.class), RemoteSelector.classProxy(VirtualContainer.class));
		components.put(RemoteSelector.classProxy(java.util.Hashtable.class),RemoteSelector.classProxy(bus.uigen.HashtableWidget.class));
		//components.put(ClassSelector.classProxy(java.util.Hashtable.class),javax.swing.JPanel.class));
		components.put(RemoteSelector.classProxy(java.util.Hashtable.class), RemoteSelector.classProxy(VirtualContainer.class));
		components.put(RemoteSelector.classProxy(java.net.URL.class), RemoteSelector.classProxy(javax.swing.JEditorPane.class));

		adapters.put("java.awt.TextField",
				"bus.uigen.adapters.TextFieldAdapter");
		adapters.put(RemoteSelector.classProxy(java.awt.TextField.class),
		RemoteSelector.classProxy(bus.uigen.adapters.TextFieldAdapter.class));
		
		adapters.put("java.awt.Label",
		"bus.uigen.adapters.LabelAdapter");
adapters.put(RemoteSelector.classProxy(java.awt.Label.class),
RemoteSelector.classProxy(bus.uigen.adapters.LabelAdapter.class));
		
		adapters.put("java.awt.TextArea",
		"bus.uigen.adapters.TextAreaAdapter");
		/*
adapters.put(java.awt.TextArea.class),
bus.uigen.adapters.TextAreaAdapter.class));
*/
		
		adapters.put("javax.swing.JTextField",
				"bus.uigen.adapters.TextFieldAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JTextField.class),
		RemoteSelector.classProxy(bus.uigen.adapters.TextFieldAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualTextField.class),
				RemoteSelector.classProxy(bus.uigen.adapters.TextFieldAdapter.class));
		
		adapters.put("javax.swing.JLabel",
		"bus.uigen.adapters.LabelAdapter");
adapters.put(RemoteSelector.classProxy(javax.swing.JLabel.class),
RemoteSelector.classProxy(bus.uigen.adapters.LabelAdapter.class));
adapters.put(RemoteSelector.classProxy(VirtualLabel.class),
		RemoteSelector.classProxy(bus.uigen.adapters.LabelAdapter.class));

		adapters.put("javax.swing.JFormattedTextField",
			"bus.uigen.adapters.FormattedTextFieldAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JFormattedTextField.class),
				RemoteSelector.classProxy(bus.uigen.adapters.FormattedTextFieldAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualFormattedTextField.class),
				RemoteSelector.classProxy(bus.uigen.adapters.FormattedTextFieldAdapter.class));
		
		adapters.put("javax.swing.JTextArea",
		"bus.uigen.adapters.TextAreaAdapter");		
		adapters.put(RemoteSelector.classProxy(javax.swing.JTextArea.class),
		RemoteSelector.classProxy(bus.uigen.adapters.TextAreaAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualTextArea.class),
				RemoteSelector.classProxy(bus.uigen.adapters.TextAreaAdapter.class));
		
		adapters.put("javax.swing.JEditorPane",
		"bus.uigen.adapters.EditorPaneAdapter");		
		adapters.put(RemoteSelector.classProxy(javax.swing.JEditorPane.class),
		RemoteSelector.classProxy(bus.uigen.adapters.EditorPaneAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualTextArea.class),
				RemoteSelector.classProxy(bus.uigen.adapters.TextAreaAdapter.class));
		
		//adapters.put("java.awt.Panel", "bus.uigen.adapters.uiPanelAdapter");
		//adapters.put(ClassSelector.classProxy(java.awt.Panel.class),ClassSelector.classProxy(bus.uigen.adapters.uiPanelAdapter.class));
		adapters.put(RemoteSelector.classProxy(java.awt.Panel.class), RemoteSelector.classProxy(bus.uigen.adapters.PanelAdapter.class));
		//adapters.put("java.applet.Applet", "bus.uigen.adapters.uiPanelAdapter");
		//adapters.put(ClassSelector.classProxy(java.applet.Applet.class), ClassSelector.classProxy(bus.uigen.adapters.uiPanelAdapter.class));
		adapters.put(RemoteSelector.classProxy(java.applet.Applet.class), RemoteSelector.classProxy(bus.uigen.adapters.PanelAdapter.class));
		//adapters.put("javax.swing.JApplet", "bus.uigen.adapters.uiJPanelAdapter");
		adapters.put("javax.swing.JApplet", "bus.uigen.adapters.CommandAndStatePanelAdapter");
		//adapters.put(ClassSelector.classProxy(javax.swing.JApplet.class), ClassSelector.classProxy(bus.uigen.adapters.uiJPanelAdapter.class));
		adapters.put(RemoteSelector.classProxy(javax.swing.JApplet.class), RemoteSelector.classProxy(CommandAndStatePanelAdapter.class));
		/*
		adapters.put("bus.uigen.uiPanel", "bus.uigen.adapters.uiPanelAdapter");
		adapters.put(ClassSelector.classProxy(bus.uigen.uiPanel.class), ClassSelector.classProxy(bus.uigen.adapters.uiPanelAdapter.class));
		*/
		
		/*
		adapters.put("javax.swing.JPanel", "bus.uigen.adapters.uiJPanelAdapter");
		adapters.put(ClassSelector.classProxy(javax.swing.JPanel.class), ClassSelector.classProxy(bus.uigen.adapters.uiJPanelAdapter.class));
		*/
		
		
		adapters.put("javax.swing.JPanel", "bus.uigen.adapters.CommandAndStatePanelAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JPanel.class), RemoteSelector.classProxy(bus.uigen.adapters.CommandAndStatePanelAdapter.class));
		adapters.put(RemoteSelector.classProxy(bus.uigen.widgets.VirtualContainer.class), RemoteSelector.classProxy(bus.uigen.adapters.CommandAndStatePanelAdapter.class));
		
		
		
		/*
		adapters.put("javax.swing.JScrollPane",
				"bus.uigen.adapters.uiJPanelAdapter");
		adapters.put(ClassSelector.classProxy(javax.swing.JScrollPane.class),
		bus.uigen.adapters.uiJPanelAdapter.class));
		*/
		adapters.put("javax.swing.JScrollPane",
		"bus.uigen.adapters.CommandAndStatePanelAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JScrollPane.class),
				RemoteSelector.classProxy(bus.uigen.adapters.CommandAndStatePanelAdapter.class));
		/*
		adapters.put(VirtualScrollPane.class),
				bus.uigen.adapters.CommandAndStatePanelAdapter.class));
				*/
		adapters.put("javax.swing.JTabbedPane",
				"bus.uigen.adapters.TabbedPaneAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JTabbedPane.class),
		RemoteSelector.classProxy(bus.uigen.adapters.TabbedPaneAdapter.class));
		adapters.put(RemoteSelector.classProxy(bus.uigen.widgets.VirtualTabbedPane.class),
				RemoteSelector.classProxy(bus.uigen.adapters.TabbedPaneAdapter.class));
		
		adapters.put("javax.swing.JDesktopPane",
		"bus.uigen.adapters.DesktopPaneAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JDesktopPane.class),
				RemoteSelector.classProxy(bus.uigen.adapters.DesktopPaneAdapter.class));
		adapters.put(RemoteSelector.classProxy(bus.uigen.widgets.VirtualDesktopPane.class),
				RemoteSelector.classProxy(bus.uigen.adapters.TabbedPaneAdapter.class));
		
		adapters.put("javax.swing.JSplitPane",
		"bus.uigen.adapters.SplitPaneAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JSplitPane.class),
				RemoteSelector.classProxy(bus.uigen.adapters.SplitPaneAdapter.class));
		adapters.put(RemoteSelector.classProxy(bus.uigen.widgets.VirtualDesktopPane.class),
				RemoteSelector.classProxy(bus.uigen.adapters.SplitPaneAdapter.class));
		/*
		adapters
				.put("java.awt.ScrollPane", "bus.uigen.adapters.uiPanelAdapter");
				
		adapters
		.put("java.awt.ScrollPane", "bus.uigen.adapters.uiPanelAdapter");
		
		adapters
		.put(ClassSelector.classProxy(java.awt.ScrollPane.class), ClassSelector.classProxy(bus.uigen.adapters.uiPanelAdapter.class));
		*/
		adapters
		.put(RemoteSelector.classProxy(java.awt.ScrollPane.class), RemoteSelector.classProxy(bus.uigen.adapters.PanelAdapter.class));
		adapters.put("javax.swing.JPasswordField",
				"bus.uigen.adapters.PasswordFieldAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JPasswordField.class),
		RemoteSelector.classProxy(bus.uigen.adapters.PasswordFieldAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualPasswordField.class),
				RemoteSelector.classProxy(bus.uigen.adapters.PasswordFieldAdapter.class));
		
		adapters
				.put("javax.swing.JSlider", "bus.uigen.adapters.SliderAdapter");
		adapters
		.put(RemoteSelector.classProxy(javax.swing.JSlider.class), RemoteSelector.classProxy(bus.uigen.adapters.SliderAdapter.class));
		adapters
		.put(RemoteSelector.classProxy(VirtualSlider.class), RemoteSelector.classProxy(bus.uigen.adapters.SliderAdapter.class));
		
		adapters
		.put("javax.swing.JProgressBar", "bus.uigen.adapters.ProgressBarAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JProgressBar.class), RemoteSelector.classProxy(bus.uigen.adapters.ProgressBarAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualProgressBar.class), RemoteSelector.classProxy(bus.uigen.adapters.ProgressBarAdapter.class));
		
		adapters.put("bus.uigen.uiHashtableWidget",
				"bus.uigen.adapters.HashtableWidgetAdapter");
		adapters.put(RemoteSelector.classProxy(bus.uigen.HashtableWidget.class),
		RemoteSelector.classProxy(bus.uigen.adapters.HashtableWidgetAdapter.class));
		adapters.put("javax.swing.JCheckBox",
				"bus.uigen.adapters.CheckBoxAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JCheckBox.class),
		RemoteSelector.classProxy(bus.uigen.adapters.CheckBoxAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualCheckBox.class),
				RemoteSelector.classProxy(bus.uigen.adapters.CheckBoxAdapter.class));
		
		adapters.put("bus.uigen.widgets.DecIncWidget",
				"bus.uigen.adapters.DecIncWidgetAdapter");
		adapters.put(RemoteSelector.classProxy(bus.uigen.view.DecIncWidget.class),
		RemoteSelector.classProxy(bus.uigen.adapters.DecIncWidgetAdapter.class));
		adapters.put("bus.uigen.widgets.DecIncWidgetEastWest",
		"bus.uigen.adapters.DecIncWidgetAdapter");
adapters.put(RemoteSelector.classProxy(bus.uigen.view.DecIncWidgetEastWest.class),
RemoteSelector.classProxy(bus.uigen.adapters.DecIncWidgetAdapter.class));
		
		adapters.put("javax.swing.JComboBox",
				"bus.uigen.adapters.ComboBoxAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JComboBox.class),
		RemoteSelector.classProxy(bus.uigen.adapters.ComboBoxAdapter.class));
		adapters.put(RemoteSelector.classProxy(bus.uigen.widgets.VirtualComboBox.class),
				RemoteSelector.classProxy(bus.uigen.adapters.ComboBoxAdapter.class));
		
		adapters.put("javax.swing.JSpinner",
		"bus.uigen.adapters.SpinnerAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JSpinner.class),
					RemoteSelector.classProxy(bus.uigen.adapters.SpinnerAdapter.class));
		adapters.put(RemoteSelector.classProxy(bus.uigen.widgets.VirtualSpinner.class),
				RemoteSelector.classProxy(bus.uigen.adapters.SpinnerAdapter.class));
		
		adapters.put("javax.swing.JRadioButton",
		"bus.uigen.adapters.RadioButtonPanelAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JRadioButton.class),
				RemoteSelector.classProxy(bus.uigen.adapters.RadioButtonPanelAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualRadioButton.class),
				RemoteSelector.classProxy(bus.uigen.adapters.RadioButtonPanelAdapter.class));
		adapters.put("javax.swing.JButton",
		"bus.uigen.adapters.ButtonPanelAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JButton.class),
				RemoteSelector.classProxy(bus.uigen.adapters.ButtonPanelAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualButton.class),
				RemoteSelector.classProxy(bus.uigen.adapters.ButtonPanelAdapter.class));
		adapters.put("javax.swing.JTree", "bus.uigen.editors.TreeAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JTree.class), RemoteSelector.classProxy(bus.uigen.editors.TreeAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualTree.class), RemoteSelector.classProxy(bus.uigen.editors.TreeAdapter.class));
		//adapters.put("bus.uigen.ObjectEditorApplet",
				//"bus.uigen.adapters.uiJPanelAdapter");
		adapters.put("javax.swing.JTable", "bus.uigen.editors.TableAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JTable.class), RemoteSelector.classProxy(bus.uigen.editors.TableAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualTable.class), RemoteSelector.classProxy(bus.uigen.editors.TableAdapter.class));
		
		adapters.put("javax.swing.JList", "bus.uigen.editors.ListAdapter");
		adapters.put(RemoteSelector.classProxy(javax.swing.JList.class), RemoteSelector.classProxy(bus.uigen.editors.ListAdapter.class));
		adapters.put(RemoteSelector.classProxy(VirtualList.class), RemoteSelector.classProxy(bus.uigen.editors.ListAdapter.class));
		
		
		
		
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.ClassAdapter.class),
				RemoteSelector.classProxy(VirtualContainer.class));
				//javax.swing.JPanel.class));
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.VectorAdapter.class),
				RemoteSelector.classProxy(VirtualContainer.class));
				//javax.swing.JPanel.class));
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.HashtableAdapter.class),
				RemoteSelector.classProxy(VirtualContainer.class));
				//javax.swing.JPanel.class));
		/*
		objectAdapterToWidget.put(ClassSelector.classProxy(bus.uigen.oadapters.uiArrayAdapter.class),
				VirtualContainer.class));
				*/
				//javax.swing.JPanel.class));
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.EnumerationAdapter.class),
				RemoteSelector.classProxy(VirtualComboBox.class));
				//javax.swing.JComboBox.class));
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.PrimitiveAdapter.class),
				RemoteSelector.classProxy(VirtualTextField.class));
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.ReferenceAdapter.class),
				RemoteSelector.classProxy(VirtualLabel.class));
				//javax.swing.JTextField.class));
		objectAdapterToWidget.put(RemoteSelector.classProxy(bus.uigen.oadapters.RootAdapter.class),
				RemoteSelector.classProxy(bus.uigen.widgets.VirtualContainer.class));
			}

	public ComponentDictionary() {

		

	}

	// Method to override a default mapping
	static public void setComponentMapping(String typeClass,
			String componentClass) {
		components.put(typeClass, componentClass);
	}

	static public boolean hasWidgetClass(String type) {
		return components.get(type) != null;
	}

	static public void setAdapterMapping(String componentClass,
			String adapterClass) {
		adapters.put(componentClass, adapterClass);
	}

	static public void setEditorMapping(String typeClass,
			String componentClass, String adapterClass) {
		//AClassWidgetPair classWidgetPair = new AClassWidgetPair(typeClass, componentClass);
		try {
		editors.put(typeClass, adapterClass);
		widgets.put(typeClass, componentClass);
		setEditorMapping (AClassProxy.staticForName (typeClass), 
				AClassProxy.staticForName  (componentClass), 
				AClassProxy.staticForName  (adapterClass));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public void setEditorMapping(ClassProxy typeClass,
			ClassProxy componentClass, ClassProxy adapterClass) {
		//AClassWidgetPair classWidgetPair = new AClassWidgetPair(typeClass, componentClass);
		editors.put(typeClass, adapterClass);
		widgets.put(typeClass, componentClass);
		widgetAdapterToWidget.put(adapterClass, componentClass);
	}

	static public void setEditorMapping(String typeClass, String adapterClass) {
		//AClassWidgetPair classWidgetPair = new AClassWidgetPair(typeClass, componentClass);
		editors.put(typeClass, adapterClass);
	}

	static public void setComponentAndAdapterMapping(String typeClass,
			String componentClass, String adapterClass) {
		//setComponentMapping(typeClass, componentClass);
		//setAdapterMapping(componentClass, adapterClass);
		setEditorMapping(typeClass, componentClass, adapterClass);
	}

	static public Hashtable getDefaultComponentMapping() {
		return components;
	}

	// Query methods.

	// Get the default component for a specified type
	static public String getDefaultComponent(String typeClass) {
		if (components.containsKey(typeClass)) {
			return (String) components.get(typeClass);
		} else {
			// Default not specified.
			// Return the "class" default
			return (String) components.get("Class");
		}
	}

	static public String getWidgetClassForObjectClass(String typeClass) {
		if (components.containsKey(typeClass)) {
			return (String) components.get(typeClass);
		}
		return null;
	}
	static public ClassProxy getWidgetClassForObjectClass(ClassProxy typeClass) {
		if (components.containsKey(typeClass)) {
			return (ClassProxy) components.get(typeClass);
		}
		return null;
	}

	// Get the adapter for a specified component
static public String getDefaultAdapter(String componentClass) {
    if (componentClass != null && adapters.containsKey(componentClass)) {
      return (String) adapters.get(componentClass);
    }
    else {
    	try {
    		ClassProxy compClass = RemoteSelector.forName (componentClass);    	
    		ClassProxy superClass = compClass.getSuperclass();
    		if (superClass != compClass.objectClass() && superClass != null) 
    			return getDefaultAdapter (superClass.getName()); 
    		else {
    			
    			// Default not specified
    			// Check if the pattern
    			// <componentClass>Adapter is a defined class
    			String adapterClass = componentClass+"Adapter";
    			try {
    				RemoteSelector.forName (adapterClass);
    				return adapterClass;
    			}
    			//catch (ClassNotFoundException e) {
    			catch (Exception e) {
    				// Couldnt match that pattern.
    				return null;
    			}
    		//}} catch (ClassNotFoundException e) {
    		}} catch (Exception e) {
    			// Couldnt match that pattern.
    			return null;
    		}
    	}
    			
  }
static public ClassProxy getDefaultWidgetAdapter(ClassProxy objectClass, ClassProxy componentClass) {
	if (objectClass == null)
		return getDefaultAdapter(componentClass);
	ClassProxy widgetAdapter = (ClassProxy) editors.get(objectClass);
	if (widgetAdapter == null)
		return getDefaultAdapter(componentClass);
	ClassProxy registeredComponentClass = widgetAdapterToWidget.get(widgetAdapter);
	if (registeredComponentClass == null)
		return getDefaultAdapter(componentClass);
	if (!registeredComponentClass.isAssignableFrom(componentClass))
		return getDefaultAdapter(componentClass);
	return widgetAdapter;
	
}
static public ClassProxy getDefaultAdapter(ClassProxy componentClass) {
    if (componentClass != null && adapters.containsKey(componentClass)) {
      return (ClassProxy) adapters.get(componentClass);
    }
    else {
    	   		   	
    		ClassProxy superClass = componentClass.getSuperclass();
    		if (superClass != componentClass.objectClass()) 
    			return getDefaultAdapter (superClass); 
    		else {
    			
    			// Default not specified
    			// Check if the pattern
    			// <componentClass>Adapter is a defined class
    			String adapterClass = componentClass.getName()+"Adapter";
    			try {
    				return RemoteSelector.forName (adapterClass);
    				
    			}
    			//catch (ClassNotFoundException e) {
    			catch (Exception e) {
    				// Couldnt match that pattern.
    				return null;
    			}
    		}
    	}
    			
  }
/*
	static public String getWidgetClass(uiObjectAdapter objectAdapter) {
		String retVal;
		Class retClass;
		Class propertyClass = objectAdapter.getPropertyClass();
		if (propertyClass == null)
			return getWidgetClassNameForAdapterClass(objectAdapter);
		retVal = getWidgetClassForObjectClass(propertyClass.getName());
		if (retVal == null)
			return getWidgetClassNameForAdapterClass(objectAdapter);
		return retVal;

	}
	*/
	static public String getWidgetClass(ObjectAdapter objectAdapter) {
		String retVal;
		ClassProxy retClass;
		ClassProxy propertyClass = objectAdapter.getPropertyClass();
		if (propertyClass == null)
			return getWidgetClassNameForAdapterClass(objectAdapter);			
		retVal = getWidgetClassForObjectClass(propertyClass.getName());
		if (retVal == null)
			return getWidgetClassNameForAdapterClass(objectAdapter);
		return retVal;

	}
	

	static public String getWidgetClassNameForAdapterClass(
			ObjectAdapter objectAdapter) {
		
		ClassProxy widgetClass1 = getWidgetClassForAdapterClass(objectAdapter);
		if (widgetClass1 != null)
			return widgetClass1.getName();
			

		String objectAdapterClassName = objectAdapter.getClass().getName();
		String widgetClass = (String) objectAdapterToWidget
				.get(objectAdapterClassName);
		if (widgetClass == null)
			return getWidgetClassForObjectClass("java.lang.Object");
		//widgetClass = "javax.swing.JPanel";
		//widgetClass = PanelSelector.
		//getDefaultComponent("java.lang.Object");
		return widgetClass;
	}
	static public ClassProxy getWidgetClassForAdapterClass(
			ObjectAdapter objectAdapter) {

		ClassProxy objectAdapterClass = ACompositeLoggable.getTargetClass(objectAdapter);
		 ClassProxy widgetClass = (ClassProxy) objectAdapterToWidget
				.get(objectAdapterClass);
		if (widgetClass == null)
			return getWidgetClassForObjectClass(objectAdapterClass.objectClass());
		//widgetClass = "javax.swing.JPanel";
		//widgetClass = PanelSelector.
		//getDefaultComponent("java.lang.Object");
		return widgetClass;
	}

	static public String getDefaultAdapter(String typeClass,
			String componentClass) {
		AClassWidgetPair classWidgetPair = new AClassWidgetPair(typeClass,
				componentClass);
		Object adapter = editors.get(typeClass);
		if (adapter == null)
			return getDefaultAdapter(componentClass);
		else
			return (String) adapter;
	}
	static public ClassProxy getDefaultAdapter(ClassProxy typeClass,
			ClassProxy componentClass) {
		
		Object adapter = editors.get(typeClass);
		if (adapter == null)
			return getDefaultAdapter(componentClass);
		else
			return (ClassProxy) adapter;
	}

	static public String getDefaultWidget(String typeClass, String adapterClass) {
		//AClassWidgetPair classWidgetPair = new AClassWidgetPair(typeClass, componentClass);
		//String componentClass = widgets.get(typeClass);
		if (editors.get(typeClass) != null)
			return (String) widgets.get(typeClass);
		else
			return null;
	}

}
