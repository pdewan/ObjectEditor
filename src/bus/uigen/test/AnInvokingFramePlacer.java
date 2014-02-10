package bus.uigen.test;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.controller.models.InteractiveMethodInvoker;
import bus.uigen.controller.models.MethodInvocationFrameCreationListener;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualPoint;

public class AnInvokingFramePlacer implements MethodInvocationFrameCreationListener {

	@Override
	public void methodInvocationFrameCreated(OEFrame aParentFrame,
			OEFrame anInvocationFrame,
			InteractiveMethodInvoker anInteractiveMethodInvoker) {
		VirtualPoint aParentFrameLocation = aParentFrame.getLocation();
		VirtualDimension aParentFrameSize = aParentFrame.getSize();
		anInvocationFrame.setLocation((int) (aParentFrameLocation.getX() +(aParentFrameSize.getWidth()/2)), 
				(int) aParentFrameLocation.getY() + (aParentFrameSize.getHeight()/2));
	}
	
	public static void main (String[] args) {
		OEFrame mainFrame = ObjectEditor.edit(new ACompositeExample());
		mainFrame.addMethodInvocationFrameCreationListener(new AnInvokingFramePlacer());
	}

}
