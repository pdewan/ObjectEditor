package bus.uigen.controller.models;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.widgets.VirtualLabel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.BEAN_PATTERN)
public class AColumnSiblingDisplayerModel extends AColumnDisplayerAbstractModel {
	/*
	static final String SHOW_COMMAND_PREFIX = "Show ";
	uiObjectAdapter adapter;
	ClassProxy parentClass;
	*/
	String[] dynamicCommands;
	
	Vector<String> hiddenSiblingCommands = new Vector();
	Vector<String> allSiblingCommands = new Vector();
	/*
	Vector<String> projectionGroupNames = new Vector();
	Vector<String> projectionGroupCommands = new Vector();
	*/
	Vector<String> dynamicCommandsVector = new Vector();
	Vector<String> componentNamesVector = new Vector();
	//Vector<String> componentNames;
	
	//String[] allSiblingsArray;
	//Map<String, List<String>> projectionGroups;
	public  AColumnSiblingDisplayerModel (ObjectAdapter theAdapter) {
		super(theAdapter);
		/*
		adapter = theAdapter;
		parentClass = adapter.getParentAdapter().getPropertyClass();
		uiContainerAdapter parentAdapter = adapter.getParentAdapter();
		BeanToRecord recordStructure = (BeanToRecord) parentAdapter.getConcreteObject();
		componentNames = recordStructure.componentNames();
		projectionGroups = adapter.getParentAdapter().getUnParsedProjectionGroups();
		processProjectionGroups();
		*/
		processComponentNames();
		//dynamicCommandsVector.addAll(projectionGroupCommands);
		dynamicCommandsVector = allSiblingCommands;
		dynamicCommands = new String[0];
		dynamicCommands = dynamicCommandsVector.toArray(dynamicCommands);
		
	}
	/*
	String toShowCommand(String property) {
		return SHOW_COMMAND_PREFIX + Character.toUpperCase(property.charAt(0)) + property.substring(1, property.length());
	}
	String getPropertyOrGroupName(String showCommand) {
		return showCommand.substring(SHOW_COMMAND_PREFIX.length(), showCommand.length());
		
	}
	
	void processProjectionGroups() {
		Iterator<String> keys = projectionGroups.keySet().iterator();
		while (keys.hasNext()) {
			String nextElement = keys.next();
			String nextCommand = toShowCommand(nextElement);
			projectionGroupNames.add(nextElement);
			projectionGroupCommands.add(nextCommand);
		}
		
	}
	*/
	
	void processComponentNames() {
		allSiblingCommands.clear();
		
		/*
		for (int i = 0; i < componentNames.size(); i++ ) { 
			if (adapter.getPropertyName().equals(componentNames.get(i)))
				continue;
			allSiblingCommands.add(toShowCommand(componentNames.get(i)));
		}
		*/
		for (int i = 0; i < labelNames.size(); i++ ) { 
			if (adapter.columnTitle().equals(labelNames.get(i)))
				continue;
			allSiblingCommands.add(toShowCommand(labelNames.get(i)));
		}
		/*
		allSiblingsArray = new String[1];
		allSiblingsArray = allSiblingCommands.toArray(allSiblingsArray);
		*/
		
	}
	
	
	 void resetHiddenSiblingCommands() {
		hiddenSiblingCommands.clear();
		
		/*
		for (int i = 0; i < componentNames.size(); i++ ) { 
			String originalPropertyName = componentNames.get(i);
			String propertyName = toRealPropertyName(originalPropertyName);
			
			//Boolean isVisible = ObjectEditor.getVisible(parentClass, propertyName);
			//Boolean isVisible = adapter.getParentAdapter().getChildAdapterAtIndex(propertyName).isVisible();
			uiObjectAdapter siblingAdapter = adapter.getSiblingAdapter(propertyName);
			Boolean isVisible = false;
			if (siblingAdapter != null && siblingAdapter.isVisible())
				isVisible = true;
			//Boolean isVisible = adapter.getTablePeerAdapter(propertyName).isVisible();
			//if (isVisible != null && !isVisible)
			if (!isVisible)
				hiddenSiblingCommands.add(toShowCommand(originalPropertyName));
		}
	 */
		for (int i = 0; i < labelNames.size(); i++ ) { 
			String labelName = labelNames.get(i);
			String propertyName = toPropertyName(labelName);
			/*
			if (adapter.getAdapterType() == uiObjectAdapter.INDEX_TYPE && propertyName.length() == 1) {
				propertyName = "" +  (propertyName.charAt(0) - 'A');
			}
			*/
			//Boolean isVisible = ObjectEditor.getVisible(parentClass, propertyName);
			//Boolean isVisible = adapter.getParentAdapter().getChildAdapterAtIndex(propertyName).isVisible();
			ObjectAdapter siblingAdapter = adapter.getSiblingAdapter(propertyName);
			Boolean isVisible = false;
			if (siblingAdapter != null && siblingAdapter.isVisible())
				isVisible = true;
			//Boolean isVisible = adapter.getTablePeerAdapter(propertyName).isVisible();
			//if (isVisible != null && !isVisible)
			if (!isVisible)
				hiddenSiblingCommands.add(toShowCommand(labelName));
		}
		
	}
	
	public boolean preDynamicCommands(String theCommand) {
		resetHiddenSiblingCommands();
		return hiddenSiblingCommands.contains(theCommand);
		
	}
	
	public String[] getDynamicCommands() {
		//return allSiblingsArray;
		return dynamicCommands;
	}
	/*
	String lastGroupCommandSelected = null;
	List<String> displayedProperties = null;
	*/
	public void invokeDynamicCommand(String theCommand) {
		//lastGroupCommandSelected = null;
		//displayedProperties = null;
		String propertyOrGroupName = getPropertyOrGroupName(theCommand);
		propertyOrGroupName = toPropertyName(propertyOrGroupName).toLowerCase();
		//uiObjectAdapter siblingAdapter = adapter.getSiblingAdapter(propertyOrGroupName);
		
			ClassProxy tableClass = adapter.setSiblingAttributeRelativeToTableClass(getComponentName(propertyOrGroupName), AttributeNames.VISIBLE, true);
//			adapter.getTopAdapter().refreshAttributes(adapter.getTableAdapter().getPropertyClass());
			adapter.getTopAdapter().refreshAttributes(tableClass);
			adapter.getUIFrame().setImplicitRefresh(false);
			if (adapter.grandParentFlatRowHasFlatTableRowDescendents())
				adapter.redoDisplayChildrenOfTableWidgetAdapter();
			else
				adapter.redoVisibleChildrenOfPeerParentAdapters();
			//adapter.redoVisibleChildrenOfPeerParentAdapters();
			adapter.getUIFrame().refreshWindow();
		/*
		} 
		else {			
			ObjectEditor.setVisible(parentClass, propertyOrGroupName, true);
			adapter.getTopAdapter().refreshAttributes(parentClass);
		}
		*/
		//adapter.getTopAdapter().refreshAttributes(parentClass);
		//adapter.getTopAdapter().refreshAttributes(adapter.getTopAdapter().getPropertyClass());
		/*
		} else {
			lastGroupCommandSelected = theCommand;
			displayedProperties = projectionGroups.get(propertyOrGroupName);
			for (int i = 0; i < componentNames.size(); i++) {
				String nextComponent = componentNames.get(i);
				if (displayedProperties.contains(nextComponent))
				ObjectEditor.setVisible(parentClass, nextComponent, true);
				else
					ObjectEditor.setVisible(parentClass, nextComponent, false);
			}
			adapter.getTopAdapter().refreshAttributes(parentClass);
			
		}
		*/
	}
	/*
	public boolean preShowAll() {
		resetHiddenSiblingCommands();
		return hiddenSiblingCommands.size() > 0;
	}
	public void showAll() {
		//resetHiddenSiblingCommands();		
		for (int i = 0; i < componentNames.size(); i++) {
			ObjectEditor.setVisible(parentClass, componentNames.get(i), true);
		}
		adapter.getTopAdapter().refreshAttributes(parentClass);
	}
	public void hide() {
		//if (adapter.getParentAdapter() == null) return;
		ObjectEditor.setVisible(parentClass, adapter.getPropertyName(), false);
		adapter.getTopAdapter().refreshAttributes(parentClass);
		//ObjectEditor.refreshAttributes(this, adapter.getParentAdapter().getPropertyClass());
		
	}
	*/

}
