package bus.uigen.compose;

import bus.uigen.*;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;

import bus.uigen.ars.*;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;

import java.util.Vector;

public class SharedInvokeButton extends JButton implements ActionListener {  //need a special Jbutton that stores its index so that we
	
	uiFrame oeFrame = null;
	//since you'll have multiple objects here...may have to do the generateUIinFrame stuff here
	//so that we can just have one UI frame.  test this out on the tvload file.
	AnObjectMethod[] targets = null;
	//Method method = null;
	String method = null;
	
	//public SharedInvokeButton(uiFrame _oeFrame, Vector _objects, Method _method) {
	public SharedInvokeButton(uiFrame _oeFrame, Vector _objects, String _method) {
		//super(_method.getName() + " All");
		super(_method + " All");
		oeFrame = _oeFrame;
		
		if (_objects != null) {
			targets = new AnObjectMethod[_objects.size()];
		
			for (int i=0; i < targets.length; i++) 
				targets[i] =  (AnObjectMethod)_objects.elementAt(i);
		}
		else 
			System.err.println("Objects has no elements for ALL");
		
		method = _method;
		addActionListener(this);
	}

	
	public void actionPerformed(ActionEvent e) {
				//not sure whether old uigen had threads for each invocation.	
		try {
		//		for (int j=0;   j < objects.length; j++)									System.err.println("trying invoke" + method + "on num obj" + targets.length);
													//uiMethodInvocationManager iman = new uiMethodInvocationManager(oeFrame,targets[0],targets,method);
				MethodProxy firstMethod = ((AnObjectMethod) targets[0]).getMethod();
				//uiMethodInvocationManager iman = new uiMethodInvocationManager(oeFrame,targets[0],targets,method);
				MethodInvocationManager iman = new MethodInvocationManager(oeFrame,targets[0],targets,firstMethod);
				//will below work will null uiframe?
				//uiMethodInvocationManager iman = new uiMethodInvocationManager(null,null,objects,method);
	
				//uiMethodInvocationManager.invokeMethod(objects[j],method);
							System.err.println("just invoked all");		
		}
		catch (Exception ex) {ex.printStackTrace();}	}
	
} 	
