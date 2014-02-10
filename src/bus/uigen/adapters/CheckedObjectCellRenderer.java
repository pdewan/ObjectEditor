package bus.uigen.adapters;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;

import bus.uigen.WidgetAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.CheckBoxSelector;
import bus.uigen.widgets.LabelSelector;
import bus.uigen.widgets.VirtualCheckBox;
import bus.uigen.widgets.VirtualLabel;

import util.models.CheckedObject;

public class CheckedObjectCellRenderer implements ListCellRenderer	{
	Map<CheckedObject, JCheckBox> checkedObjectToBox = new Hashtable();
	ObjectAdapter adapter;
	JLabel templateLabel;
	boolean templateLabelInitialized;
	public CheckedObjectCellRenderer(ObjectAdapter theAdapter) {
		adapter = theAdapter;
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
//		JCheckBox checkBox = new JCheckBox();
//		checkBox.setLabel(value.toString());
//		checkBox.setUI((BasicCheckBoxUI) MyCheckBoxUI.createUI(checkBox));
//		checkBox.setPreferredSize(new Dimension(214, 25));
//		return checkBox;
		
//		if(index ==-1)
//			return new JLabel("Select features!");
		
		CheckedObject checkedObject = (CheckedObject) value;
		if (checkedObject.isTemplate())
			return getTemplateLabel(checkedObject);
		
		JCheckBox checkBox = getCheckBox(checkedObject);
		checkBox.setSelected(checkedObject.getStatus());
		return checkBox;
	}
	JLabel getTemplateLabel(CheckedObject checkedObject) {
		if (!templateLabelInitialized) {
			templateLabelInitialized = true;
			VirtualLabel vLabel = LabelSelector.createLabel(checkedObject.getObject().toString());
			if (!(vLabel.getPhysicalComponent() instanceof JLabel)) {
				
				return new JLabel("Expecting Swing Label");
			}
			templateLabel = (JLabel) vLabel.getPhysicalComponent();
			WidgetAdapter.setColors(vLabel, adapter);
		}
		return templateLabel;
	}
	JCheckBox getCheckBox(CheckedObject checkedObject) {
		JCheckBox checkBox = checkedObjectToBox.get(checkedObject);
		if (checkBox == null) {
			VirtualCheckBox vBox = CheckBoxSelector.createCheckBox();
			if (!(vBox.getPhysicalComponent() instanceof JCheckBox)) {
				
				return new JCheckBox("Expecting Swing Checkbox");
			}
			//checkBox = new JCheckBox();
			checkBox = ((JCheckBox) vBox.getPhysicalComponent());
			checkedObjectToBox.put(checkedObject, checkBox);
			checkBox.setLabel(checkedObject.getObject().toString());
			checkBox.setUI((BasicCheckBoxUI) MyCheckBoxUI.createUI(checkBox));
			checkBox.setPreferredSize(new Dimension(214, 25));
			checkBox.setEnabled(true);
			WidgetAdapter.setColors(vBox, adapter);
		}
		return checkBox;
		
	}
	static class MyCheckBoxUI extends BasicCheckBoxUI {
	    public static ComponentUI createUI(JComponent c) {
	      return new MyCheckBoxUI();
	    }
	  }
}
