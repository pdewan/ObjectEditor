package bus.uigen;

import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.view.OEFrameSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.awt.AWTComponent;
import bus.uigen.widgets.awt.AWTContainer;

public class ModelBinder {
	public static ClassAdapter setModel(Object o, String property,
			Component component) {
		return ObjectEditor.setModel(o, property, component);
	}

	public static ClassAdapter setModel(Object o, String property,
			VirtualComponent virtualComponent) {
		return ObjectEditor.setModel(o, property, virtualComponent);
	}

	public static ClassAdapter setModels(Object o, String[] properties,
			VirtualComponent[] virtualComponents) {
		return ObjectEditor.setModels(o, properties, virtualComponents);
	}

	public static ClassAdapter setModels(Object o, String[] properties,
			Component[] components) {
		return ObjectEditor.setModels(o, properties, components);

	}

	public static uiFrame addModel(Object object, Container drawingContainer) {
		return ObjectEditor.addModel(object, drawingContainer);
	}

	public static uiFrame addModel(Object object,
			VirtualContainer drawingContainer) {
		return ObjectEditor.addModel(object, drawingContainer);

	}

}
