package bus.uigen.test;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;

import bus.uigen.CompleteOEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.jung.JungGraphManager;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.swing.DelegateJPanel;

public class TreeAndJungGraphDisplayDemoer {
	public static void main (String[] args) {
		ACompositeExampleWithBackLink graphObject = new ACompositeExampleWithBackLink();
		String aString = new String("hello");
		String aString2 = new String("hello");
		System.out.println (aString.hashCode());
		System.out.println (aString.hashCode());
		System.out.println (System.identityHashCode(aString));
		System.out.println (System.identityHashCode(aString2));


//		ObjectEditor.textEdit(graphObject);
		JungGraphManager aJungGraphManager = ALogicalStructureDisplayer.treeAndGraphDisplay(graphObject);
		ObjectEditor.edit(aJungGraphManager);
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