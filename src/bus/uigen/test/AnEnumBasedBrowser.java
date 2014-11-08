package bus.uigen.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JRadioButton;

import bus.uigen.ObjectEditor;
import util.annotations.Position;
import util.annotations.PreferredWidgetClass;
import util.models.ADynamicEnum;
import util.models.DynamicEnum;
import util.models.PropertyListenerRegistrar;

public class AnEnumBasedBrowser implements PropertyChangeListener {
	DynamicEnum<String> topLevel = new ADynamicEnum();
	DynamicEnum<String> secondLevel = new ADynamicEnum();
	Map<String, List<String>> groupToMembers = new HashMap();
	public AnEnumBasedBrowser() {
		List<String> students = new ArrayList();
		students.add("Jacob");
		students.add("Andrew");
		students.add("Will");
		List<String> faculty = new ArrayList();
		faculty.add("Prasun");
		faculty.add("Kevin");
		groupToMembers.put("Students", students);
		groupToMembers.put("Faculty", faculty);	
		topLevel.addChoice("Students");
		topLevel.addChoice("Faculty");	
		topLevel.setValue("Students");
		secondLevel.setChoices(students);
		topLevel.addPropertyChangeListener(this);
		
		
	}
	@PreferredWidgetClass(JRadioButton.class)
	@Position(0)
	public DynamicEnum<String> getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(DynamicEnum<String> topLevel) {
		this.topLevel = topLevel;
	}
	@Position(1)
	public DynamicEnum<String> getSecondLevel() {
		return secondLevel;
	}

	public void setSecondLevel(DynamicEnum<String> secondLevel) {
		this.secondLevel = secondLevel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == topLevel) {
		String newValue = (String) evt.getNewValue();
		List<String> newChoices = groupToMembers.get(newValue);
//		secondLevel.setValue(newChoices.get(0));
		secondLevel.setChoices(newChoices);
		}
		
	}
	public static void main (String[] args) {
		AnEnumBasedBrowser browser = new AnEnumBasedBrowser();
		ObjectEditor.edit(browser);
	}

}
