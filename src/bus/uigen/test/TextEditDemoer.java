package bus.uigen.test;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;

import bus.uigen.CompleteOEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.swing.DelegateJPanel;

public class TextEditDemoer {
	public static void main (String[] args) {
		ObjectEditor.textEdit(new ALabelAndString());
//		ACompositeExampleWithBackLink graphObject = new ACompositeExampleWithBackLink();
//		ObjectEditor.textEdit(graphObject);
//		CompleteOEFrame aFrame = ObjectEditor.textEdit(labelAndString);
//		aFrame.showDrawPanel();
//		aFrame.hideMainPanel();
//		aFrame.showTreePanel();
//		VirtualComponent aVirtualComponent = aFrame.getDrawVirtualComponent();
//		Container aJPanel = (Container) aFrame.getDrawPanel().getPhysicalComponent();
//		Container aContainer = (Container) aVirtualComponent.getPhysicalComponent();
//		JFrame aJFrame = (JFrame) aFrame.getFrame().getPhysicalComponent();
//		ALogicalStructureDisplayer.createLogicalStructureDisplay(labelAndString, null, aContainer);
//		aJFrame.validate();
		
	}

}
