package bus.uigen.test;


import bus.uigen.ObjectEditor;
import bus.uigen.widgets.FrameSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualToolkit;
import bus.uigen.widgets.awt.AWTToolkit;
import bus.uigen.widgets.swing.SwingToolkit;

public class TestSwingFactroryDemoer {
	public static void main (String[] anArgs) {
		ObjectEditor.edit(new ACompositeColorer());
		PanelSelector.setPanelFactory(new MySwingPanelFactory());
		TextFieldSelector.setTextFieldFactory(new TestSwingTextFieldFactory());

		ObjectEditor.edit(new ACompositeColorer());		
	}

}
