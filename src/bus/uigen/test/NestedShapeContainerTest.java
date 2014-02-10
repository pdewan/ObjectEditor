package bus.uigen.test;

import java.util.ArrayList;
import java.util.List;

import shapes.RemoteShape;
import util.annotations.IsNestedShapesContainer;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.misc.ShapeListMouseClickListener;
import bus.uigen.shapes.ALineModel;
import bus.uigen.shapes.AnOvalModel;
import bus.uigen.widgets.events.VirtualMouseEvent;
@IsNestedShapesContainer(true)
public class NestedShapeContainerTest implements ShapeListMouseClickListener  {
	List shapes = new ArrayList();
	ALineMagnifier foo = 	 new ALineMagnifier(20, 20, 30, 30);

	public  NestedShapeContainerTest() {
		shapes.add(foo);
	}
	@Override
	public void mouseClicked(List<RemoteShape> theShapes,
			VirtualMouseEvent mouseEvent) {
		shapes.add(new AnOvalModel(mouseEvent.getX(), mouseEvent.getY(), 50, 50));
		// TODO Auto-generated method stub
		
	}
	public List getShapes() {
		return shapes;
	}
	
//	public ALineModel getFoo() {
//		return foo;
//	}
	
	public static void main(String args[]) {
		NestedShapeContainerTest test = new NestedShapeContainerTest();
		//test.getShapes().add(new ALineModel(0, 0, 10, 10));
		ObjectEditor.setDefaultAttribute(AttributeNames.SHOW_SYSTEM_MENUS, false);
		ObjectEditor.edit(test);
	}
	

}
