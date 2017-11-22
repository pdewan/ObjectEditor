package bus.uigen.test;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;

import bus.uigen.CompleteOEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.jung.AJungGraphManagerCustomization;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.jung.AVertexObjectToColor;
import bus.uigen.jung.AVertexObjectToLabel;
import bus.uigen.jung.JungGraphManager;
import bus.uigen.jung.JungGraphManagerCustomization;
import bus.uigen.jung.VertexObjectToColorFactory;
import bus.uigen.jung.VertexObjectToLabelFactory;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.swing.DelegateJPanel;

public class JungGraphAndTextDisplayDemoer {
	public static void main (String[] args) {
		ACompositeExampleWithBackLink graphObject = new ACompositeExampleWithBackLink();

		ObjectEditor.setDefaultAttribute(AttributeNames.NODE_LABEL_IS_TO_STRING, true);
		VertexObjectToColorFactory.setColorer(new AVertexObjectToColor());
		VertexObjectToLabelFactory.setLabeler(new AVertexObjectToLabel());
		JungGraphManagerCustomization aJungGraphCustomization = new AJungGraphManagerCustomization<>();
		JungGraphManager aJungGraphManager = ALogicalStructureDisplayer.treeAndGraphDisplay(graphObject, 
				aJungGraphCustomization);
		JungGraphManager aJungGraphManager2 = ALogicalStructureDisplayer.treeAndGraphDisplay(graphObject, 
				aJungGraphCustomization);
		ObjectEditor.edit(aJungGraphCustomization);		
	}

}
