package bus.uigen.controller.models;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.widgets.VirtualLabel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AColumnDisplayerModel implements Serializable {
	static final String HIDE_COMMAND = "hide";
	static final String SHOW_COMMAND_PREFIX = "Show ";
	ObjectAdapter adapter;
	ClassProxy parentClass;
	String[] dynamicCommands;
	
	Vector<String> hiddenSiblingCommands = new Vector();
	Vector<String> allSiblingCommands = new Vector();
	Vector<String> projectionGroupNames = new Vector();
	Vector<String> projectionGroupCommands = new Vector();
	Vector<String> dynamicCommandsVector = new Vector();
	Vector<String> componentNames;
	String[] allSiblingsArray;
	Map<String, List<String>> projectionGroups;
	public  AColumnDisplayerModel (ObjectAdapter theAdapter) {
		
		adapter = theAdapter;
		parentClass = adapter.getParentAdapter().getPropertyClass();
		CompositeAdapter parentAdapter = adapter.getParentAdapter();
		BeanToRecord recordStructure = (BeanToRecord) parentAdapter.getConcreteObject();
		componentNames = recordStructure.componentNames();
		projectionGroups = adapter.getParentAdapter().getUnParsedProjectionGroups();
		processProjectionGroups();
		processComponentNames();
		dynamicCommandsVector.addAll(projectionGroupCommands);
		dynamicCommandsVector.addAll(allSiblingCommands);
		dynamicCommands = new String[0];
		dynamicCommands = dynamicCommandsVector.toArray(dynamicCommands);
		
	}
	
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
	
	void processComponentNames() {
		allSiblingCommands.clear();
		
		
		for (int i = 0; i < componentNames.size(); i++ ) { 
			if (adapter.getPropertyName().equals(componentNames.get(i)))
				continue;
			allSiblingCommands.add(toShowCommand(componentNames.get(i)));
		}
		/*
		allSiblingsArray = new String[1];
		allSiblingsArray = allSiblingCommands.toArray(allSiblingsArray);
		*/
		
	}
	
	
	 void resetHiddenSiblingCommands() {
		hiddenSiblingCommands.clear();
		
		
		for (int i = 0; i < componentNames.size(); i++ ) { 
			String propertyName = componentNames.get(i);
			Boolean isVisible = ObjectEditor.getPropertyVisible(parentClass, propertyName);
			if (isVisible != null && !isVisible)
				hiddenSiblingCommands.add(toShowCommand(propertyName));
		}
		
	}
	
	public boolean preDynamicCommands(String theCommand) {
		resetHiddenSiblingCommands();
		if (allSiblingCommands.contains(theCommand))
		return hiddenSiblingCommands.contains(theCommand);
		else
			return !theCommand.equals(lastGroupCommandSelected);
	}
	
	public String[] getDynamicCommands() {
		//return allSiblingsArray;
		return dynamicCommands;
	}
	String lastGroupCommandSelected = null;
	List<String> displayedProperties = null;
	public void invokeDynamicCommand(String theCommand) {
		lastGroupCommandSelected = null;
		displayedProperties = null;
		String propertyOrGroupName = getPropertyOrGroupName(theCommand).toLowerCase();
		if (allSiblingCommands.contains(theCommand)) {
		ObjectEditor.setVisible(parentClass, propertyOrGroupName, true);
		adapter.getTopAdapter().refreshAttributes(parentClass);
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
	}
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

}
