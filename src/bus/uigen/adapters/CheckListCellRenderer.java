package bus.uigen.adapters;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;

public class CheckListCellRenderer implements ListCellRenderer	{
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		if(index ==-1)
			return new JLabel("Select features!");
		JCheckBox checkBox = (JCheckBox)value;
		checkBox.setUI((BasicCheckBoxUI) MyCheckBoxUI.createUI(checkBox));
		checkBox.setPreferredSize(new Dimension(214, 25));
		checkBox.setEnabled(true);
		return checkBox;
	}
	static class MyCheckBoxUI extends BasicCheckBoxUI {
	    public static ComponentUI createUI(JComponent c) {
	      return new MyCheckBoxUI();
	    }
	  }
}
