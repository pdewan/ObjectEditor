//
// uiMethodInvocationManager
// A class that handles all the work related to
// getting parameters for a method invocation.
//

package bus.uigen.controller;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import util.misc.Common;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.UnivMethodInvocation;
import bus.uigen.uiFrame;
import bus.uigen.uiFrameList;
import bus.uigen.attributes.AnInheritedAttributeValue;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.compose.AnObjectMethod;
import bus.uigen.controller.menus.AMethodProcessor;
import bus.uigen.controller.menus.OEPopupMenu;
import bus.uigen.controller.models.AnInteractiveMethodInvoker;
import bus.uigen.controller.models.InteractiveActualParameter;
import bus.uigen.controller.models.InteractiveMethodInvoker;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.DefaultRegistry;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.introspect.uiClassMapper;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.LoggableRegistry;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.reflect.local.AVirtualMethod;
import bus.uigen.trace.SeparateThreadRequest;
import bus.uigen.trace.UnsuccessfulValidation;
import bus.uigen.translator.TranslatorRegistry;
import bus.uigen.undo.Command;
import bus.uigen.undo.CommandCreator;
import bus.uigen.undo.CommandList;
import bus.uigen.undo.CommandListener;
import bus.uigen.viewgroups.APropertyAndCommandFilter;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;

public class MethodInvocationManager extends Frame implements ActionListener,
		VirtualActionListener, DoneListener {

	static boolean manualUI = false;

	Object parentObject;
	Object[] parentObjects = new Object[1];
	Hashtable<Object, MethodProxy> objectMethods;
	CommandListener commandListener;
	MethodProxy method;
	MethodDescriptorProxy md;
	// Constructor constructor;
	MethodProxy constructor;
	public Vector comboBoxToParameterMapping, panelToParameterMapping,
			textFieldToParameterMapping;
	uiFrame parentFrame = null;
	int parameterNumber = -1;
	Object result = null;
	JButton invokeButton;

	ClassProxy[] parameterTypes;
	public Object[] parameterValues;
	boolean parameterValuesSet = false;
	boolean displayResult = true;

	static Hashtable<MethodProxy, uiFrame> invokerEditors = new Hashtable();
	// static Hashtable<MethodProxy, uiFrame> invokerEditors = new Hashtable();
	boolean autoEditMethod = false;

	void autoEdit(InteractiveMethodInvoker theInvoker) {
		// ObjectEditor.edit(theInvoker, false);
		autoEditMethod = parentFrame != null && parentFrame.isDummy();
//		invokerEditor = ObjectEditor.edit(theInvoker, false, null, null);
//		invokerEditor = ObjectEditor.edit(theInvoker);

		invokerEditor = ObjectEditor.edit(theInvoker, false);
		

		ClassAdapter invokerAdapter = (ClassAdapter) invokerEditor
				.getTopAdapter();
		ObjectAdapter resultAdapter = invokerAdapter
				.getExistingOrDeletedAdapter("result");
		// resultAdapter.setTempAttributeValue(AttributeNames.LABEL,
		// uiGenerator.beautify(methodOrConstructor.getName()));
		resultAdapter.setLabel(Common.beautify(methodOrConstructor.getName()));
		// editor.setSize(450, 190);
		invokerEditor.setSize(150, 150);
		invokerEditor.getFrame().pack();
		invokerEditor.setTitle(Common.beautify(getSignature()));
		invokerEditors.put(methodOrConstructor, invokerEditor);
		if (parentFrame != null) {
			parentFrame.notifyMethodInvocationFrameCreated(parentFrame, invokerEditor, theInvoker);
		}
	}

	void edit(InteractiveMethodInvoker theInvoker) {
		if (userParameterComponents == null) {
			autoEdit(theInvoker);
			return;
		}
		InteractiveActualParameter[] parameters = theInvoker.getParameters();
		int numParameters = parameters.length;
		if (userParameterComponents.length < numParameters) {
			autoEdit(theInvoker);
			return;
		}
		for (int i = 0; i < numParameters; i++) {
			// String parameterName = parameters[i].getName();
			ObjectEditor.setModel(parentFrame, parameters[i], "value",
					userParameterComponents[i]);
		}
		ClassDescriptorInterface cd = ClassDescriptorCache
				.getClassDescriptor(theInvoker);
		MethodProxy[] methods = cd.getVirtualMethods();
		MethodProxy method = IntrospectUtility.getMethod(methods, invokeName,
				null, null);
		ClassAdapter invokerAdapter = null;
		if (resultComponent != null) {
			invokerAdapter = ObjectEditor.setModel(parentFrame, theInvoker,
					"result", resultComponent);
			invokerAdapter.setManualUI(true);
		}
		if (userInvokeButton != null)
			ObjectEditor
					.bind(parentFrame, theInvoker, method, userInvokeButton);
		else if (userInvokeMenuItem != null) {

			ObjectEditor.bind(parentFrame, theInvoker, method,
					userInvokeMenuItem);
		} else {
			invoker.doImplicitInvoke(true);
		}
		/*
		 * if (invokerAdapter != null)
		 * parentFrame.getBrowser().getAdapterHistory
		 * ().addAdapter(invokerAdapter);
		 */
	}

	void initInvokerAndEditor(MethodProxy methodOrConstructor) {
		// MethodProxy methodOrConstructor = (method !=
		// null)?method:constructor;
		invokerEditor = invokerEditors.get(methodOrConstructor);
		if (invokerEditor == null) {

			invoker = new AnInteractiveMethodInvoker(this, invokeName,
					methodOrConstructor, Common.beautify(getSignature()),
					// method,
					parentObject, parameterNames, resultComponent != null);
			edit(invoker);
			
			/*
			 * //uiFrame invokerEditor = ObjectEditor.edit(invoker, false);
			 * invokerEditor = ObjectEditor.edit(invoker, false); uiClassAdapter
			 * invokerAdapter = (uiClassAdapter) invokerEditor.getTopAdapter();
			 * uiObjectAdapter resultAdapter =
			 * invokerAdapter.getExistingOrDeletedAdapter("result");
			 * //resultAdapter.setTempAttributeValue(AttributeNames.LABEL,
			 * uiGenerator.beautify(methodOrConstructor.getName()));
			 * resultAdapter
			 * .setLabel(uiGenerator.beautify(methodOrConstructor.getName()));
			 * //editor.setSize(450, 190); invokerEditor.setSize(150, 150);
			 * invokerEditor.getFrame().pack();
			 * invokerEditor.setTitle(uiGenerator.beautify(getSignature()));
			 * invokerEditors.put(methodOrConstructor, invokerEditor);
			 */
		} else {
			invoker = (InteractiveMethodInvoker) invokerEditor
					.getTopAdapter().getRealObject();
			invoker.setInvocationManager(this);
			invokerEditor.setVisible(true);
		}
	}

	public MethodInvocationManager(Object parent, MethodProxy m) {
		super();
		// System.out.println("Method invoked" + m);
		method = m;
		md = getMethodDescriptor(m);
		init(parent);
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(Object parent, MethodProxy m,
			Object[] parameters) {
		super();
		// System.out.println("Method invoked" + m);
		method = m;
		md = getMethodDescriptor(m);
		parameterValues = parameters;
		parameterValuesSet = true;
		init(parent);
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public Object[] getParameters() {
		return parameterValues;
	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			CommandListener cl) {
		super();
		// System.out.println("Method invoked" + m);
		init(frame, parent, m, cl);
		/*
		 * 
		 * method = m; parentFrame = frame; commandListener = cl; init(parent);
		 * if (!checkIfArgumentsAvailable()) initUI();
		 */
	}

	static boolean implicitRefresh = true;
	MethodDescriptorProxy methodDescriptor;

	public MethodInvocationManager(uiFrame frame, Object parent,
			MethodDescriptorProxy md, MethodProxy m, CommandListener cl) {
		super();
		methodDescriptor = md;
		// boolean oldSomeoneElseWillRefresh;
		implicitRefresh = (Boolean) AttributeManager
				.getInheritedAttributeValue(methodDescriptor,
						AttributeNames.IMPLICIT_REFRESH, null);
		// System.out.println("Method invoked" + m);

		init(frame, parent, m, cl);
		implicitRefresh = true;
		/*
		 * 
		 * method = m; parentFrame = frame; commandListener = cl; init(parent);
		 * if (!checkIfArgumentsAvailable()) initUI();
		 */
	}

	public static MethodDescriptorProxy getMethodDescriptor(MethodProxy m) {
		if (m == null)
			return null;
		ClassProxy c = m.getDeclaringClass();
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache
				.getClassDescriptor(c);
		return cd.getMethodDescriptor(m);

	}

	void init(uiFrame frame, Object parent, MethodProxy m, CommandListener cl) {
		if (m != null) {

		}

		method = m;
		md = getMethodDescriptor(m);

		parentFrame = frame;
		commandListener = cl;
		init(parent);
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	VirtualButton userInvokeButton;
	VirtualMenuItem userInvokeMenuItem;
	VirtualComponent[] userParameterComponents;
	VirtualComponent resultComponent;

	public void setUserParameterComponents(
			VirtualComponent[] theUserParameterComponents) {
		userParameterComponents = theUserParameterComponents;
	}

	public void setUserInvokeButton(VirtualButton theUserInvokeButton) {
		userInvokeButton = theUserInvokeButton;
	}

	public void setUserInvokeMenuItem(VirtualMenuItem theUserInvokeMenuItem) {
		userInvokeMenuItem = theUserInvokeMenuItem;
	}

	public void setParameter(VirtualMenuItem theUserInvokeMenuItem) {
		userInvokeMenuItem = theUserInvokeMenuItem;
	}

	public void setResultComponent(VirtualComponent theResultComponent) {
		resultComponent = theResultComponent;
	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m) {
		super();
		// System.out.println("Method invoked" + m);
		if (frame != null) {
			if (!m.isConstructor())
				// if (!m.isConstuctor())
				// if (m.isMethod())
				init(frame, parent, m, frame.getAdapter());
			else {
				commandListener = null;
				parentFrame = frame;
				// initConstructor (parent, c);
				initConstructor(parent, m);
			}
		} else
			init(null, parent, m, (CommandListener) null);
		/*
		 * 
		 * method = m; parentFrame = frame; commandListener = cl; init(parent);
		 * if (!checkIfArgumentsAvailable()) initUI();
		 */
	}

	static Hashtable methodToParameterNames = new Hashtable();

	public static void registerParameterNames(MethodProxy m,
			String[] parameterNames) {
		methodToParameterNames.put(m, parameterNames);
	}

	public static void registerParameterNames(Method m, String[] parameterNames) {
		methodToParameterNames.put(AVirtualMethod.virtualMethod(m),
				parameterNames);
	}

	public String getParameterName(int parameterIndex) {
		String defaultName = "Parameter " + (parameterIndex + 1) + ":";
		if ((parameterNames == null)
				|| (parameterIndex >= parameterNames.length))
			return defaultName;
		return parameterNames[parameterIndex] + ":";

	}

	static Hashtable<MethodProxy, Object[]> methodToParameterValues = new Hashtable();

	public static void registerDefaultParameterValues(MethodProxy m,
			Object[] parameterValues) {
		methodToParameterValues.put(m, parameterValues);
	}

	public static void registerDefaultParameterValues(Method m,
			Object[] parameterValues) {
		methodToParameterValues.put(AVirtualMethod.virtualMethod(m),
				parameterValues);
	}

	public Object getDefaultParameterValue(int parameterIndex,
			ClassProxy parameterType) {
		Object defaultValue = bus.uigen.misc.OEMisc.defaultValue(parameterType);
		Object[] defaultParameterValues = methodToParameterValues
				.get(methodOrConstructor);
		if ((defaultParameterValues == null)
				|| (parameterIndex >= defaultParameterValues.length))
			return defaultValue;
		return defaultParameterValues[parameterIndex];

	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			Hashtable objectMethodsParam) {
		super();
		init(frame, parent, m, objectMethodsParam);
		/*
		 * //System.out.println("Method invoked" + m); commandListener = null;
		 * parentFrame = frame; objectMethods = objectMethodsParam; if
		 * (objectMethods == null || objectMethods.size() == 0) return; method =
		 * (MethodProxy) objectMethods.elements().nextElement(); md =
		 * getMethodDescriptor(method); init(parent); if
		 * (!checkIfArgumentsAvailable()) initUI();
		 */
	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			Hashtable objectMethodsParam, VirtualButton theUserInvokeButton,
			VirtualComponent[] theUserParameterComponents,
			VirtualComponent theResultComponent) {
		super();
		setUserInvokeButton(theUserInvokeButton);
		setUserParameterComponents(theUserParameterComponents);
		setResultComponent(theResultComponent);
		init(frame, parent, m, objectMethodsParam);

	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			Hashtable objectMethodsParam, VirtualMenuItem theUserInvokeButton,
			VirtualComponent[] theUserParameterComponents,
			VirtualComponent theResultComponent) {
		super();
		setUserInvokeMenuItem(theUserInvokeButton);
		setUserParameterComponents(theUserParameterComponents);
		setResultComponent(theResultComponent);
		init(frame, parent, m, objectMethodsParam);

	}

	public void init(uiFrame frame, Object parent, MethodProxy m,
			Hashtable objectMethodsParam) {

		// System.out.println("Method invoked" + m);
		commandListener = null;
		parentFrame = frame;
		objectMethods = objectMethodsParam;
		if (objectMethods == null || objectMethods.size() == 0)
			return;
		method = (MethodProxy) objectMethods.elements().nextElement();
		md = getMethodDescriptor(method);
		init(parent);
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			boolean displayResultParam) {
		super();
		// System.out.println("Method invoked" + m);

		commandListener = null;
		method = m;
		parentFrame = frame;
		init(parent);
		displayResult = displayResultParam;
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			Object[] parameters) {
		super();
		// System.out.println("Method invoked" + m);

		commandListener = null;
		method = m;
		parentFrame = frame;
		init(parent);
		parameterValues = parameters;
		parameterValuesSet = true;
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(uiFrame frame, Object parent, MethodProxy m,
			Object[] parameters, boolean displayResultParam) {
		super();
		// System.out.println("Method invoked" + m);

		commandListener = null;
		method = m;
		parentFrame = frame;
		init(parent);
		parameterValues = parameters;
		parameterValuesSet = true;
		displayResult = displayResultParam;
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(uiFrame frame, Object parent,
			Object[] parents, MethodProxy m) {
		super();
		// System.out.println("Method invoked" + m);

		commandListener = null;
		method = m;
		parentFrame = frame;
		if (parents.length > 0)
			init(parentObjects[0]);
		else
			init(null);
		parentObjects = parents;
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(uiFrame frame, Object parent,
			AnObjectMethod[] parents, MethodProxy m) {
		super();
		// System.out.println("Method invoked" + m);

		commandListener = null;
		method = m;
		parentFrame = frame;
		if (parents.length > 0)
			init(parentObjects[0]);
		else
			init(null);
		parentObjects = parents;
		doAll = true;
		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public MethodInvocationManager(uiFrame frame, Object parent,
			Object[] parents, MethodProxy m, Object[] parameters) {
		super();
		// System.out.println("Method invoked" + m);

		commandListener = null;
		method = m;
		parentFrame = frame;
		if (parents.length > 0)
			init(parentObjects[0]);
		else
			init(null);
		parentObjects = parents;

		parameterValues = parameters;
		parameterValuesSet = true;

		if (!checkIfArgumentsAvailable())
			initUI();
	}

	public static final Class OBJECT_CLASS = Object.class;
	public static final ClassProxy OBJECT_CLASS_PROXY = AClassProxy
			.classProxy(Object.class);

	// public void initConstructor (Object parent, Constructor c){
	public void initConstructor(Object parent, MethodProxy c) {
		// System.out.println("constructor called" + c);
		constructor = c;
		init(parent);
		if (c == null)
			return;

		// System.out.println("Got Constructor for" +
		// c.getDeclaringClass().toString());
		if (c.getDeclaringClass() == OBJECT_CLASS_PROXY) {
			creatingObject = true;
			// System.out.println("Creating Object");
			initUI();
			// System.out.println("Finished init UI");
		} else if (!checkIfArgumentsAvailable())
			initUI();
	}

	/*
	 * public uiMethodInvocationManager(Object parent, Constructor c) {
	 * initConstructor(parent, c); //super();
	 * 
	 * }
	 */

	/*
	 * public uiMethodInvocationManager(uiFrame frame, Object parent,
	 * Constructor c) { //public uiMethodInvocationManager(uiFrame frame, Object
	 * parent, VirtualMethod c) {
	 * 
	 * //this(parent, c); //System.out.println("Constructir" + frame);
	 * commandListener = null; parentFrame = frame; initConstructor (parent, c);
	 * 
	 * }
	 */
	// uiMethodInvocationManager parentMIM = null;
	// ActionListener parentMIM = null;
	MethodInvocationManager parentMIM = null;

	// public uiMethodInvocationManager(uiMethodInvocationManager theParentMIM,
	// int theParameterNumber, Object parent, Constructor c) {
	public MethodInvocationManager(MethodInvocationManager theParentMIM,
			int theParameterNumber, Object parent, MethodProxy c) {

		// this(parent, c);
		// System.out.println("Constructir" + frame);
		commandListener = null;
		parentMIM = theParentMIM;
		this.parameterNumber = theParameterNumber;
//		displayResult = false;
		initConstructor(parent, c);
		/*
		 * super(); System.out.println("Constructor called" + c); constructor =
		 * c; parentFrame = frame; init(parent); if
		 * (!checkIfArgumentsAvailable()) initUI();
		 */
	}

	boolean creatingObject = false;

	private boolean checkIfArgumentsAvailable() {

		if (parameterValuesSet)
			return true;

		if (parameterTypes.length == 0) {
			// System.out.println("length 0");
			// parameterValues = null;
			invokeMethod();
			dispose();
			return true;
		}

		if (parameterTypes.length == 1) {
			ClassProxy ptype = parameterTypes[0];
			// System.out.println("Ptype" + ptype);
			// System.out.println("Integer.TYPE" + Integer.TYPE);
			// if (ptype == Integer.TYPE)
			// System.out.println("Integer.TYPE" + Integer.TYPE);
			if (ptype.isPrimitive()) {
				// System.out.println(ptype.toString());
				ptype = DefaultRegistry.getWrapper(ptype);
				// System.out.println("Ptype" + ptype);
			}
			// Object obj = ObjectClipboard.get();
			ObjectAdapter selection;
			Object obj;

			// System.out.println("selection" +
			// (uiSelectionManager.getCurrentSelection().getObject()).getClass());

			if (((selection = (ObjectAdapter) SelectionManager
					.getCurrentSelection()) != null) &&
			// ((obj = selection.getRealObject()) != null) &&
					((obj = selection.getObject()) != null) &&

					// Object obj = ((uiObjectAdapter)
					// uiSelectionManager.getCurrentSelection()).getRealObject();
					// if (obj != null &&
					ptype.isAssignableFrom(ACompositeLoggable
							.getTargetClass(obj))) {

				/*
				 * if (((selection = (uiObjectAdapter)
				 * uiSelectionManager.getCurrentSelection()) != null) && //((obj
				 * = selection.getRealObject()) != null) && ((obj =
				 * selection.getObject()) != null) ) { Class selectionType =
				 * obj.getClass(); if (ptype.isAssignableFrom(selectionType))
				 */
				// if (obj instanceof ptype)
				// Ask permission first!
				/*
				 * System.out.println("obj" + selection.getObject());
				 * System.out.println("robj" + selection.getRealObject());
				 * System.out.println("vobj" + selection.getViewObject());
				 */
				if (MethodParameters.ConfirmOnMethod == false
						|| (JOptionPane.showConfirmDialog(null,
								"Invoke command with selected argument " + obj
										+ " ?", "Confirm selection",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
					parameterValues[0] = obj;
					invokeMethod();
					dispose();
					return true;
				}
			} else // check if single argument is a vector.
			if (IntrospectUtility.isVector(ptype, false)) {
				ClassProxy elementClass = IntrospectUtility
						.getVectorElementClass(ptype);
				Vector selections = SelectionManager.getSelectedObjects();
				if (selections.size() == 0)
					return false;
				for (int i = 0; i < selections.size(); i++)
					if (!elementClass
							.isAssignableFrom(ACompositeLoggable
									.getTargetClass(selections.elementAt(i)
											.getClass())))
						return false;
				if (MethodParameters.ConfirmOnMethod == false
						|| (JOptionPane.showConfirmDialog(
								null,
								"Invoke command with selected elements: "
										+ uiFrame.toStringVector(selections,
												"", ", ", "") + " ?",
								"Confirm selection", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
					try {
						Object vectorObject = ptype.newInstance();

						MethodProxy addElementMethod = IntrospectUtility
								.getAddElementMethod(ptype);
						for (int i = 0; i < selections.size(); i++) {
							Object[] addParams = { selections.elementAt(i) };
							addElementMethod.invoke(vectorObject, addParams);
						}
						parameterValues[0] = vectorObject;
						invokeMethod();
						dispose();
						return true;
					} catch (Exception e) {
						return false;
					}
				} else
					return false;
			} else {// check if selections are all of ptype
				Vector selections = SelectionManager.getSelectedObjects();
				if (selections.size() == 0)
					return false;
				for (int i = 0; i < selections.size(); i++)
					if (!ptype
							.isAssignableFrom(ACompositeLoggable
									.getTargetClass(selections.elementAt(i)
											.getClass())))
						return false;
				if (MethodParameters.ConfirmOnMethod == false
						|| (JOptionPane.showConfirmDialog(
								null,
								"Invoke command  on each of the selected elements: "
										+ uiFrame.toStringVector(selections,
												"", ", ", "") + " ?",
								"Confirm selection", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
					try {
						Vector retVal = new Vector();
						for (int i = 0; i < selections.size(); i++) {
							parameterValues[0] = selections.elementAt(i);
							retVal.addElement(method.invoke(parentObject,
									parameterValues));
						}
						ObjectEditor.edit(retVal, "Return Values");
						return true;
					} catch (Exception e) {
						return false;
					}
				}

			}
		} else { // check multiple arguments
			Vector selections = SelectionManager.getSelectedObjects();
			if (parameterTypes.length != selections.size())
				return false;
			for (int i = 0; i < selections.size(); i++) {
				if (!parameterTypes[i].isAssignableFrom(ACompositeLoggable
						.getTargetClass(selections.elementAt(i).getClass())))
					return false;
			}
			if (MethodParameters.ConfirmOnMethod == false
					|| (JOptionPane
							.showConfirmDialog(
									null,
									"Invoke command with selected elements: "
											+ uiFrame
													.toStringVector(selections)
											+ " ?", "Confirm selection",
									JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
				try {
					for (int i = 0; i < selections.size(); i++) {
						parameterValues[i] = selections.elementAt(i);
					}
					invokeMethod();
					dispose();
					return true;
				} catch (Exception e) {
					return false;
				}
			}

		}

		return false;
	}

	public void dispose() {

		if (invokerEditor != null) {
			if (invoker.getAutoClose())
//			if (!((Boolean) AttributeManager.getInheritedAttribute(
//					methodOrConstructor, AttributeNames.PERSIST_INVOCATION_WINDOW,
//					null).getValue())) {

				// invokerEditor.getFrame().dispose();
				invokerEditor.getFrame().setVisible(false);
				if (
				// !((Boolean)
				// ClassDescriptor.getInheritedAttribute(methodOrConstructor,
				// AttributeNames.IMPLICIT_METHOD_INVOCATION).getValue()) &&
//						
//				((Boolean) AttributeManager.getInheritedAttribute(
//						methodOrConstructor,
//						AttributeNames.RESET_METHOD_INOVOKER, null).getValue()))
				 invoker.getAutoReset())
					invoker.resetAll();
			}
//		}
// not sure we want the window disposed
//		super.dispose();
	}

	private String getSignature() {
		// System.out.println("getSignature" + method + constructor);
		if (md != null)
			return AMethodProcessor.getLabel(md);
		if (method != null) {
			return method.getName();
		} else
			// return constructor.getName();
			return "Create "
					+ AClassDescriptor.toShortName(constructor
							.getDeclaringClass().getName());
	}

	/*
	 * public static String getSignature(MethodProxy theMethod) {
	 * //System.out.println("getSignature" + method + constructor);
	 * MethodDescriptorProxy theMD = getMethodDescriptor(theMethod);
	 * 
	 * if (theMD != null) return AMethodProcessor.getLabel(theMD); if (theMethod
	 * != null) { return theMethod.getName(); } else //return
	 * constructor.getName(); return "Create " +
	 * ClassDescriptor.toShortName(constructor.getDeclaringClass().getName()); }
	 */
	String invokeName = "Apply";

	InteractiveMethodInvoker invoker;
	uiFrame invokerEditor;

	private ClassProxy[] getParameterTypes() {
		if (method != null)
			return method.getParameterTypes();
		else if (constructor != null)
			return constructor.getParameterTypes();
		else
			return null;
	}

	JComboBox objectComboBox;
	String[] parameterNames;
	MethodProxy methodOrConstructor;

	private void initUI() {
		if (method != null)
			parameterNames = (String[]) methodToParameterNames.get(method);
		// should add for constructor also
		if (parameterNames == null && method != null) {
			util.annotations.ParameterNames parameterNamesAnnotation = (util.annotations.ParameterNames) method
					.getAnnotation(util.annotations.ParameterNames.class);
			if (parameterNamesAnnotation != null)
				parameterNames = parameterNamesAnnotation.value();
		}
		comboBoxToParameterMapping = new Vector();
		panelToParameterMapping = new Vector();
		textFieldToParameterMapping = new Vector();
		if (creatingObject)
			setTitle("Creating a new object");
		else
			setTitle("Parameters of " + Common.beautify(getSignature()));
		// getContentPane().setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 1));
		this.setSize(150, 150);
		// topPanel.add(new Label("Method Signature:"+getSignature()));
		if (creatingObject)
			topPanel.add(new Label("Enter Class of new Object"));
		/*
		 * else topPanel.add(new
		 * Label("Enter Class and Value of Each Parameter of " +
		 * uiGenerator.beautify(getSignature()))); //
		 * getContentPane().add(topPanel, "North");
		 * 
		 * topPanel.add(new
		 * Label("Click Right Button on Parameter Prompt for Popup Commands"));
		 */
		add(topPanel, "North");

		MenuBar mbar = new MenuBar();
		setMenuBar(mbar);
		Menu fileMenu = new Menu("File");
		MenuItem item = new MenuItem("Close");
		item.addActionListener(this);
		fileMenu.add(item);
		mbar.add(fileMenu);
      
		// InvokeButton invoke = new InvokeButton("Invoke command");
		MethodDescriptorProxy md = IntrospectUtility.toMethodDescriptor(method);
		// real hack, have to change this at some time
		String invokeNameChoice = AttributeNames.SIGNATURE_TITLE;
		AnInheritedAttributeValue inheritedAttribute =  AttributeManager
		.getInheritedAttribute(method,
				AttributeNames.METHOD_BUTTON_TITLE, null);
		if (inheritedAttribute != null)
			invokeNameChoice = (String) inheritedAttribute.getValue();
//		
//			(String) AttributeManager
//				.getInheritedAttribute(method,
//						AttributeNames.METHOD_BUTTON_TITLE, null).getValue();
		if (invokeNameChoice.equals(AttributeNames.APPLY_TITLE))
			invokeName = "Apply";
		else if (invokeNameChoice.equals(AttributeNames.NAME_TITLE)) {
			invokeName = null;
			invokeName = (String) md.getValue(AttributeNames.LABEL);
			if (invokeName == null)
				invokeName = Common.beautify(method.getName());
		} else if (invokeNameChoice.equals(AttributeNames.SIGNATURE_TITLE)) {
			invokeName = null;
			if (md != null) {
				invokeName = md.getDisplayName();
				if (invokeName == null)
					invokeName = (String) md.getValue(AttributeNames.LABEL);
			}
			if (invokeName == null)
				invokeName = Common.beautify(getSignature());
		} else
			invokeName = Common.beautify(getSignature());
		InvokeButton invoke = new InvokeButton(invokeName);
		if (!creatingObject) {
			JPanel buttonPanel = new JPanel();
			// InvokeButton invoke = new InvokeButton("Invoke method");
			invoke.setFrame(this);
			invoke.addActionListener(this);
			buttonPanel.add(invoke);
			// getContentPane().add(buttonPanel, "South");
			add(buttonPanel, "South");
			invokeButton = invoke;
		}

		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new GridLayout(parameterTypes.length, 1));
		// getContentPane().add(parameterPanel, "Center");
		add(parameterPanel, "Center");
		// MethodProxy methodOrConstructor = method;
		methodOrConstructor = method;
		if (method == null)
			methodOrConstructor = constructor;
		manualUI = (Boolean) AttributeNames
				.getDefaultOrSystemDefault(AttributeNames.MANUAL_INVOCATION_UI);
		// AnInteractiveMethodInvoker invoker = new AnInteractiveMethodInvoker(
		if (!manualUI) {
			initInvokerAndEditor(methodOrConstructor);
			/*
			 * invoker = new AnInteractiveMethodInvoker( this, invokeName,
			 * methodOrConstructor, //method, parentObject, parameterNames );
			 * //uiFrame invokerEditor = ObjectEditor.edit(invoker, false);
			 * invokerEditor = ObjectEditor.edit(invoker, false);
			 * //editor.setSize(450, 190); invokerEditor.setSize(150, 150);
			 * invokerEditor.getFrame().pack();
			 * invokerEditor.setTitle("Parameters of " +
			 * uiGenerator.beautify(getSignature()));
			 */
		}

		class ComboListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("combo chnaged");
				if (e.getActionCommand().equals("comboBoxChanged") /*
																	 * || e.
																	 * getActionCommand
																	 * (
																	 * ).equals(
																	 * "comboBoxEdited"
																	 * )
																	 */)
					// createObject(e, false);
					createObject(e, true);

			}
		}

		ComboListener comboListener = new ComboListener();

		MouseAdapter mouseadapter = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// System.out.println("mouse pressed");
				maybeShowPopup(e);
			}

			public void mouseReleased(MouseEvent e) {
				// System.out.println("mouse released");
				maybeShowPopup(e);
			}

			public void maybeShowPopup(MouseEvent e) {
				// System.out.println("maybe popup");
				if (e.isPopupTrigger()) {
					// System.out.println("popup trigger");
					// Configure the menu
					OEPopupMenu.configurePopupMenu(true, null);
					OEPopupMenu.getPopupMenu().show(e.getComponent(), e.getX(),
							e.getY());
				}
			}
		};
		if (creatingObject) {
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(1, 2));
			// JLabel l = new JLabel("Parameter "+(i+1)+":");
			LabelWithPopupSupport l = new LabelWithPopupSupport() {
				public void actionPerformed(ActionEvent evt) {
					String command = evt.getActionCommand();
					if (command.equals("Paste"))
						doPasteAction(evt);
					else if (command.equals("Create"))
						doCreateAction(evt);
					else if (command.equals("Edit"))
						doEditAction(evt);
				}
			};
			l.setText("Object Class:");
			p.add(l);
			l.addMouseListener(mouseadapter);
			// System.out.println("cause of error message starting");
			// Vector choices =
			// uiClassMapper.getClassMapping((Object.class).getName());
			Vector choices = uiClassMapper.getClassMapping((method
					.getDeclaringClass().objectClass()));

			// System.out.println("cause ended");
			// System.out.println("choices size" + choices.size());
			JComboBox t = new JComboBox(choices);
			objectComboBox = t;
			// t.addActionListener(this);
			t.addActionListener(comboListener);

			t.setSelectedIndex(0);
			t.setEnabled(true);
			t.setEditable(true);

			p.add(t);
			comboBoxToParameterMapping.addElement(t);
			/*
			 * 
			 * JTextField tf = new JTextField(""); //tf.setEditable(false);
			 * tf.addActionListener(this);
			 * textFieldToParameterMapping.addElement(tf); p.add(tf);
			 */
			parameterPanel.add(p);
		} else {

			for (int i = 0; i < parameterTypes.length; i++) {
				JPanel p = new JPanel();
				p.setLayout(new GridLayout(1, 4));
				// JLabel l = new JLabel("Parameter "+(i+1)+":");
				LabelWithPopupSupport l = new LabelWithPopupSupport() {
					public void actionPerformed(ActionEvent evt) {
						String command = evt.getActionCommand();
						if (command.equals("Paste"))
							doPasteAction(evt);
						else if (command.equals("Create"))
							doCreateAction(evt);
						else if (command.equals("Edit"))
							doEditAction(evt);
					}
				};
				// l.setText("Parameter "+(i+1)+":");
				l.setText(getParameterName(i));
				p.add(l);
				l.addMouseListener(mouseadapter);
				// Vector choices =
				// uiClassMapper.getClassMapping(parameterTypes[i].getName());
				Vector choices = uiClassMapper
						.getClassMapping(parameterTypes[i]);
				// System.out.println("choices size" + choices.size());
				JComboBox t = new JComboBox(choices);
				// t.addActionListener(this);
				t.addActionListener(comboListener);
				t.setSelectedIndex(0);
				t.setEnabled(true);
				t.setEditable(true);

				p.add(t);
				comboBoxToParameterMapping.addElement(t);

				JTextField tf = new JTextField("");

				// JTextField tf = new JTextField("", 20);
				// tf.setEditable(false);
				tf.addActionListener(this);
				textFieldToParameterMapping.addElement(tf);
				p.add(tf);
				parameterPanel.add(p);
				panelToParameterMapping.addElement(p);
				uiFrame f;

				// if ((f = editParameter(i)) != null && (i ==
				// parameterTypes.length - 1)) {
				if ((f = editParameter(i)) != null
						&& (parameterTypes.length == 1)) {
					invoke.setVisible(false);
					this.setVisible(false);
					return;
				}
				;

			}
		}
		uiFrame firstFrame = (uiFrame) uiFrameList.getList().elementAt(0);
		firstFrame.setFontSize(this);
		// firstFrame.setFontSize(parameterPanel);
		pack();
		if (manualUI) {
			setVisible(true);
		}
		// show();

	}

	private void init(Object parent) {
		// System.out.println("init:" + parent);
		setExcludeClasses();

		// System.out.println("exclude classes set");
		parentObject = parent;
		parentObjects[0] = parent;
		parameterTypes = getParameterTypes();
		if (parameterTypes == null)
			return;

		// System.out.println("parameter types set");
		parameterValues = new Object[parameterTypes.length];

	}

	public uiFrame constructorFrame;

	public Object getResult() {
		return result;
	}

	public static boolean invokeSelectMethod(ObjectAdapter a) {
		if (a == null)
			return false;
		Object argument = a.getRealObject();
		if (argument == null)
			return false;
		// Class argumentClass = argument.getClass();
		/*
		 * uiContainerAdapter parentAdapter = a.getParentAdapter(); if
		 * (parentAdapter == null) return false; Object parentObject =
		 * parentAdapter.getRealObject(); if (parentObject == null) return
		 * false;
		 */
		// return invokeSelectMethod(a, argument, argumentClass, parentAdapter,
		// parentObject, parentObject.getClass() );
		return invokeSelectMethod(a, argument);

	}

	public static boolean invokeExpandMethod(ObjectAdapter a,
			boolean hasExpanded) {
		if (a == null)
			return false;
		Object argument = a.getRealObject();
		if (argument == null)
			return false;

		return invokeExpandMethod(a, argument, hasExpanded);

	}

	public static boolean invokeSingleArgumentSelectMethod(ObjectAdapter a,
			Object argument, MethodProxy method, Object handlerObject) {
		try {
			if (!method.getParameterTypes()[0]
					.isAssignableFrom(ACompositeLoggable
							.getTargetClass(argument)))
				return false;
			Object[] parameterValues = new Object[1];
			parameterValues[0] = argument;
			method.invoke(handlerObject, parameterValues);
			// invokeMethod(handlerObject, method, parameterValues);
			// a.getUIFrame().doImplicitRefresh();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean invokeDoubleArgumentSelectMethod(ObjectAdapter a,
			Object argument, MethodProxy method, Object parentObject,
			Object handlerObject) {
		try {
			if (!method.getParameterTypes()[1]
					.isAssignableFrom(ACompositeLoggable
							.getTargetClass(argument)))
				return false;
			if (!method.getParameterTypes()[0]
					.isAssignableFrom(ACompositeLoggable
							.getTargetClass(parentObject)))
				return false;
			Object[] parameterValues = new Object[2];
			parameterValues[0] = parentObject;
			parameterValues[1] = argument;
			method.invoke(handlerObject, parameterValues);
			// invokeMethod(handlerObject, method, parameterValues);
			// a.getUIFrame().doImplicitRefresh();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean invokeExpandMethod(ObjectAdapter a, Object argument,
			MethodProxy method, Object parentObject, Object handlerObject,
			boolean hasExpanded) {
		try {
			if (!method.getParameterTypes()[1]
					.isAssignableFrom(ACompositeLoggable
							.getTargetClass(argument)))
				return false;
			if (!method.getParameterTypes()[0]
					.isAssignableFrom(ACompositeLoggable
							.getTargetClass(parentObject)))
				return false;
			Object[] parameterValues = new Object[3];
			parameterValues[0] = parentObject;
			parameterValues[1] = argument;
			parameterValues[2] = hasExpanded;
			method.invoke(handlerObject, parameterValues);
			// invokeMethod(handlerObject, method, parameterValues);
			// a.getUIFrame().doImplicitRefresh();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean invokeSelectMethod(ObjectAdapter a, Object argument) {
		ObjectAdapter parent = a.getParentAdapter();
		if (parent == null)
			return false;
		Object parentObject = parent.getRealObject();
		if (parentObject == null)
			return false;
		ClassProxy parentClass = ACompositeLoggable
				.getTargetClass(parentObject);
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache
				.getClassDescriptor(ACompositeLoggable
						.getTargetClass(parentObject));
		List handlers = parent.getSelectHandlers();
		if (!handlers.contains(parentObject))
			handlers.add(parentObject);
		boolean retVal = false;
		for (int i = 0; i < handlers.size(); i++) {
			retVal |= invokeSelectMethod(a, argument, parentObject,
					handlers.get(i));
		}
		if (retVal) {
			uiFrame parentFrame = a.getUIFrame();
			parentFrame.doImplicitRefresh();
		}
		return retVal;
	}

	public static boolean invokeExpandMethod(ObjectAdapter a, Object argument,
			boolean hasExpanded) {
		ObjectAdapter parent = a.getParentAdapter();
		if (parent == null)
			return false;
		Object parentObject = parent.getRealObject();
		if (parentObject == null)
			return false;
		ClassProxy parentClass = ACompositeLoggable
				.getTargetClass(parentObject);
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache
				.getClassDescriptor(parentObject.getClass());
		List handlers = parent.getExpandHandlers();
		if (!handlers.contains(parentObject))
			handlers.add(parentObject);
		boolean retVal = false;
		for (int i = 0; i < handlers.size(); i++) {
			retVal |= invokeExpandMethod(a, argument, parentObject,
					handlers.get(i), hasExpanded);
		}
		return retVal;
	}

	public static boolean invokeSelectMethod(ObjectAdapter a, Object argument,
			Object parentObject, Object handlerObject) {
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache
				.getClassDescriptor(handlerObject.getClass());
		MethodProxy[] methods = cd.getSelectMethods();
		MethodProxy target = null;
		boolean retVal = false;
		if (methods == null)
			return false;
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getParameterTypes().length == 1) {
				// retVal |= invokeSingleArgumentSelectMethod(a, argument,
				// methods[i], handlerObject);
				boolean success = invokeSingleArgumentSelectMethod(a, argument,
						methods[i], handlerObject);
				retVal |= success;
			} else if (methods[i].getParameterTypes().length == 2)
				retVal |= invokeDoubleArgumentSelectMethod(a, argument,
						methods[i], parentObject, handlerObject);

		return retVal;

	}

	public static boolean invokeExpandMethod(ObjectAdapter a, Object argument,
			Object parentObject, Object handlerObject, boolean hasExpanded) {
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache
				.getClassDescriptor(handlerObject.getClass());
		MethodProxy[] methods = cd.getExpandMethods();
		MethodProxy target = null;
		boolean retVal = false;
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getParameterTypes().length == 3) {
				// retVal |= invokeSingleArgumentSelectMethod(a, argument,
				// methods[i], handlerObject);
				boolean success = invokeExpandMethod(a, argument, methods[i],
						parentObject, handlerObject, hasExpanded);
			}

		return retVal;

	}

	public static boolean invokeDoubleClickMethod(ObjectAdapter a) {
		if (a == null)
			return false;
		Object argument = a.getRealObject();
		if (argument == null)
			return false;
		ClassProxy argumentClass = ACompositeLoggable.getTargetClass(argument);
		return invokeDoubleClickMethod(a, argument, argumentClass);
		/*
		 * Object parentObject = parent.getRealObject(); if (parentObject ==
		 * null) return false; ClassDescriptor cd = (ClassDescriptor)
		 * ClassDescriptorCache.getClassDescriptor(parentObject.getClass());
		 * Method[] methods = cd.getDoubleClickMethods(); Method target = null;
		 * for (int i=0; i < methods.length; i++) if
		 * (methods[i].getParameterTypes()[0].isAssignableFrom(argumentClass))
		 * if (target == null) target = methods[i]; else return false; if
		 * (target == null) return false; else { Object[] parameterValues = new
		 * Object[1]; parameterValues[0] = argument;
		 * invokeMethod(a.getUIFrame(), parent, target, parameterValues); }
		 * 
		 * return true;
		 */
	}

	public static boolean invokeDoubleClickMethod(ObjectAdapter a,
			Object argument, ClassProxy argumentClass) {
		ObjectAdapter parent = a.getParentAdapter();
		if (parent == null)
			return false;
		Object parentObject = parent.getRealObject();
		if (parentObject == null)
			return false;
		AClassDescriptor cd = (AClassDescriptor) ClassDescriptorCache
				.getClassDescriptor(parentObject.getClass());
		MethodProxy[] methods = cd.getDoubleClickMethods();
		MethodProxy target = null;
		// boolean found = false;
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getParameterTypes()[0]
					.isAssignableFrom(argumentClass))
				if (target == null) {
					target = methods[i];

				} else {
					if (target.getDeclaringClass().isAssignableFrom(
							methods[i].getDeclaringClass())) // get the most
																// specific open
																// method
						target = methods[i];
				}
		/*
		 * else {// why return here? found = false; //return false; }
		 */
		if (target != null) {
			Object[] parameterValues = new Object[1];
			parameterValues[0] = argument;
			invokeMethod(a.getUIFrame(), parentObject, target, parameterValues);
			// a.getUIFrame().doImplicitRefresh();
			return true;
		} else
			return invokeDoubleClickMethod(parent, argument, argumentClass);
	}

	public static Object invokeMethod(MethodProxy method, Object parentObject,
			Object[] parameterValues) {
		return invokeMethod(parentObject, method, parameterValues);
	}

	public static Object invokeMethod(Method method, Object parentObject,
			Object[] parameterValues) {
		return invokeMethod(AVirtualMethod.virtualMethod(method), parentObject,
				parameterValues);
		// return invokeMethod(parentObject, method, parameterValues);
	}

	public static Object invokeMethod(Object parentObject, MethodProxy method,
			Object[] parameterValues) {
		Object result;
		try {
			// Runtime.getRuntime().traceMethodCalls(true);
			if (parentObject instanceof APropertyAndCommandFilter) {
				Object sourceObject = method.getSourceObject();
				if (sourceObject != null)
					parentObject = sourceObject;
			}
			if (parentObject instanceof ACompositeLoggable) {
				// if (parentObject instanceof AnIdentifiableLoggable) {
				result = LoggableRegistry.invokeMethod(
						(ACompositeLoggable) parentObject, method,
						parameterValues);
				// result =
				// LoggableRegistry.invokeMethod((AnIdentifiableLoggable)
				// parentObject, method, parameterValues);
			} else if (!ObjectEditor.shareBeans()) {
				result = method.invoke(parentObject, parameterValues);
			} else {
				
				result = ObjectRegistry
						.logUnivMethodInvocation(new UnivMethodInvocation(
								parentObject, method, parameterValues));
			}
			// Runtime.getRuntime().traceMethodCalls(false);
		} catch (NullPointerException e) {
			System.out
					.println("E** Unexpected null argument to invoke method. Object "
							+ parentObject
							+ " Method "
							+ method
							+ " Parameters " + parameterValues);
			e.printStackTrace();
			result = null;
		} catch (InvocationTargetException ite) {

			Throwable actualException = ite.getTargetException();
			System.out.println(actualException.getClass());
			if (actualException instanceof java.rmi.ConnectException) {
				System.out.println(actualException.getMessage());
			} else {
				String s = "Error when calling method.";
				s = s + "\nObject: " + parentObject + "\nMethod: "
						+ method.getName();
				// s = s + "\nParameters: " + parameterValues.toString();
				s = s + "\nParameters: " + toString(parameterValues);
				s = s + "\nException: " + actualException.getMessage();
				actualException.printStackTrace();
				s = s + "\nPlease trace method";
				JOptionPane.showMessageDialog(null, s);
				ite.printStackTrace();
			}
			result = null;

		} catch (IllegalArgumentException e) {
			String s = "Illegal argument to method:";
			s = s + "\nIn Class: " + parentObject.getClass().getName();
			s = s + "\nObject: " + parentObject + "\nMethod: "
					+ method.getName();
			// s = s + "\nParameters: " + parameterValues.toString();
			s = s + "\nParameters: " + toString(parameterValues);
			s = s + "\nPlease trace the method.";
			JOptionPane.showMessageDialog(null, s);
			result = null;
			e.printStackTrace();
		} catch (Exception e) {
			String s = "Error when calling method: " + e;
			s = s + "\nObject: " + parentObject + "\nMethod: "
					+ method.getName();
			// s = s + "\nParameters: " + parameterValues.toString();
			s = s + "\nParameters: " + toString(parameterValues);
			s = s + "\nPlease trace the method.";
			JOptionPane.showMessageDialog(null, s);
			// System.out.println(s);
			result = null;
			e.printStackTrace();
		}
		// System.out.println("Result of invoke method:" + method.getName() +
		// " " + result);

		return result;
		// parentFrame.doImplicitRefresh();
	}

	public static String toString(Object[] objectArray) {
		if (objectArray.length == 0)
			return "[]";
		String retVal = "[";
		for (int i = 0; i < objectArray.length - 1; i++) {
			retVal += objectArray[i].toString() + ",";
		}
		return retVal + objectArray[objectArray.length - 1] + "]";
	}

	// parameter order more consistent with Method.invoke
	public static Object invokeMethod(uiFrame parentFrame, MethodProxy method,
			Object parentObject, Object[] parameterValues) {
		return invokeMethod(parentFrame, parentObject, method, parameterValues);
	}

	// this method is subsumed by invokeMethods and can be deleted or can call
	// the latter
	public static Object invokeMethod(uiFrame parentFrame, Object parentObject,
			MethodProxy method, Object[] parameterValues) {
		return invokeMethod(parentFrame, parentObject, method, parameterValues,
				parentFrame.getAdapter());
	}

	public static Object invokeMethod(uiFrame parentFrame, Object parentObject,
			MethodProxy method, Object[] parameterValues, CommandListener cl) {
		Object[] parentObjects = { parentObject };
		return invokeMethods(parentFrame, parentObjects, method,
				parameterValues, cl);
		/*
		 * if (parentFrame != null) parentFrame.doUpdateAll(); Object result =
		 * null;
		 * 
		 * try {
		 * 
		 * // Following line required for some version of 1.2beta
		 * //method.setAccessible(true); if (method.getReturnType() ==
		 * Void.TYPE) {
		 * 
		 * 
		 * if (Modifier.isSynchronized(method.getModifiers()) && (parentFrame!=
		 * null) && parentFrame.createMethodInvocationThreads() ) {
		 * 
		 * parentFrame.getMethodInvocationBuffer().put( new
		 * MethodInvocation(parentObject, method, parameterValues)); }
		 * 
		 * else { //Runtime.getRuntime().traceInstructions(true); result =
		 * invokeMethod(parentObject, method, parameterValues); //result =
		 * method.invoke(parentObject, parameterValues);
		 * //Runtime.getRuntime().traceInstructions(false); if (parentFrame !=
		 * null) parentFrame.doImplicitRefresh(); } } else { //result =
		 * method.invoke(parentObject, parameterValues); result =
		 * invokeMethod(parentObject, method, parameterValues); if (parentFrame
		 * != null) parentFrame.doImplicitRefresh(); } } catch (Exception e) {
		 * e.printStackTrace(); }
		 * 
		 * return result;
		 */

	}

	public static Object invokeMethods(uiFrame parentFrame,
			Object[] parentObjects, MethodProxy method, Object[] parameterValues) {
		return invokeMethods(parentFrame, parentObjects, method,
				parameterValues, parentFrame.getAdapter());
	}

	public static Object invokeMethods(uiFrame parentFrame,
			Object[] parentObjects, MethodProxy method,
			Object[] parameterValues, CommandListener cl) {
		Hashtable objectMethods = new Hashtable();
		for (int i = 0; i < parentObjects.length; i++) {
			objectMethods.put(parentObjects[i], method);
		}
		return invokeMethods(parentFrame, objectMethods, parameterValues, cl);
		/*
		 * 
		 * parentFrame.doUpdateAll(); Vector results = new Vector(); Object
		 * result = null; CommandList cmdList = new CommandList();
		 * 
		 * try {
		 * 
		 * // Following line required for some version of 1.2beta
		 * //method.setAccessible(true); if ((method.getReturnType() ==
		 * Void.TYPE) && Modifier.isSynchronized(method.getModifiers()) &&
		 * parentFrame != null && parentFrame.createMethodInvocationThreads() )
		 * { for (int i = 0; i < parentObjects.length; i++)
		 * 
		 * parentFrame.getMethodInvocationBuffer().put( new
		 * MethodInvocation(parentObjects[i], method, parameterValues)); } else
		 * {
		 * 
		 * if (parentFrame == null) { for (int i = 0; i < parentObjects.length;
		 * i++) { results.addElement(invokeMethod(parentObjects[i], method,
		 * parameterValues));
		 * //cmdList.addElement(CommandCreator.createCommand(null, method,
		 * parentObjects[i], parameterValues)); } } else if
		 * (parentObjects.length == 1) {
		 * results.addElement(parentFrame.getUndoer().execute(
		 * (CommandCreator.createCommand(parentFrame.getAdapter(), method,
		 * parentObjects[0], parameterValues))));
		 * 
		 * } else { for (int i = 0; i < parentObjects.length; i++) {
		 * //results.addElement(invokeMethod(parentObjects[i], method,
		 * parameterValues));
		 * cmdList.addElement(CommandCreator.createCommand(null, method,
		 * parentObjects[i], parameterValues)); } results = (Vector)
		 * parentFrame.getUndoer().execute(cmdList); }
		 * 
		 * } } catch (Exception e) { e.printStackTrace(); } if (parentFrame !=
		 * null) parentFrame.doImplicitRefresh(); if (method.getReturnType() ==
		 * Void.TYPE || results.size() == 0) return null; else if
		 * (results.size() == 1) return results.elementAt(0); else return
		 * results;
		 */
	}

	public static Object invokeMethods(uiFrame parentFrame,
			AnObjectMethod[] parentObjects, MethodProxy method,
			Object[] parameterValues, CommandListener cl) {
		Hashtable objectMethods = new Hashtable();
		for (int i = 0; i < parentObjects.length; i++) {
			objectMethods.put(parentObjects[i].getObject(),
					parentObjects[i].getMethod());
		}
		return invokeMethods(parentFrame, objectMethods, parameterValues, cl);

	}

	static Vector pendingObjectMethodsList = new Vector();
	static Vector pendingParameterValuesList = new Vector();
	static uiFrame pendingFrame = null;
	static Vector pendingCommandListenerList = new Vector();
	static boolean middleOfTransaction = false;

	static public boolean middleOfTransaction() {
		return middleOfTransaction;
	}

	public static void beginTransaction() {
		middleOfTransaction = true;
	}

	public static void endTransaction() {
		invokeMethods(pendingFrame, pendingObjectMethodsList,
				pendingParameterValuesList, pendingCommandListenerList);
		middleOfTransaction = false;
		pendingObjectMethodsList.removeAllElements();
		pendingParameterValuesList.removeAllElements();
		pendingCommandListenerList.removeAllElements();
		pendingFrame = null;
	}

	/*
	 * void add (Vector source, Vector addition) { for (Enumeration
	 * additionElements = addition.elements();
	 * additionElements.hasMoreElements();
	 * source.addElement(additionElements.nextElement())); }
	 */

	public static void addToPending(uiFrame parentFrame,
			Hashtable objectMethods, Object[] parameterValues,
			CommandListener cl) {
		if (pendingFrame != null && parentFrame != pendingFrame) {
			endTransaction();
			beginTransaction();
		}
		pendingFrame = parentFrame;
		/*
		 * invokeMethods(parentFrame, objectMethods, parameterValues, cl);
		 * return; pendingFrame = parentFrame;
		 */
		pendingObjectMethodsList.addElement(objectMethods);
		pendingParameterValuesList.addElement(parameterValues);
		pendingCommandListenerList.addElement(cl);
	}

	public static Object invokeMethods(uiFrame parentFrame,
			Hashtable objectMethods, Object[] parameterValues,
			CommandListener cl) {
		if (middleOfTransaction()) {
			addToPending(parentFrame, objectMethods, parameterValues, cl);
			return null;
		} else {
			Vector objectMethodsList = new Vector();
			objectMethodsList.addElement(objectMethods);
			Vector parameterValuesList = new Vector();
			parameterValuesList.addElement(parameterValues);
			Vector commandListenerList = new Vector();
			commandListenerList.addElement(cl);
			return invokeMethods(parentFrame, objectMethodsList,
					parameterValuesList, commandListenerList);
		}
	}

	public static Object invokeMethods(uiFrame parentFrame,
			Hashtable objectMethods, Object[] parameterValues) {
		return invokeMethods(parentFrame, objectMethods, parameterValues,
				parentFrame.getAdapter());
		/*
		 * if (middleOfTransaction()) { addToPending(parentFrame, objectMethods,
		 * parameterValues, parentFrame.getAdapter()); return null; } else {
		 * Vector objectMethodsList = new Vector();
		 * objectMethodsList.addElement(objectMethods); Vector
		 * parameterValuesList = new Vector();
		 * parameterValuesList.addElement(parameterValues); Vector
		 * commandListenerList = new Vector();
		 * commandListenerList.addElement(parentFrame.getAdapter()); return
		 * invokeMethods (parentFrame, objectMethodsList, parameterValuesList);
		 * }
		 */

		/*
		 * parentFrame.doUpdateAll(); Vector results = new Vector(); Enumeration
		 * keys = objectMethods.keys(); CommandList cmdList = new CommandList();
		 * while (keys.hasMoreElements()) { Object parentObject =
		 * keys.nextElement(); Method method = (Method)
		 * objectMethods.get(parentObject);
		 * 
		 * try {
		 * 
		 * // Following line required for some version of 1.2beta
		 * //method.setAccessible(true);
		 * 
		 * if (uiBean.isAsynchronous(method) && parentFrame != null &&
		 * parentFrame.createMethodInvocationThreads() ) {
		 * parentFrame.getMethodInvocationBuffer().put( new
		 * MethodInvocation(parentFrame, parentObject, method,
		 * parameterValues)); } else {
		 * 
		 * Command cmd; if (parentFrame == null || (cmd =
		 * CommandCreator.createCommand(parentFrame.getAdapter(), method,
		 * parentObject, parameterValues)) == null) { Object retVal =
		 * invokeMethod(parentObject, method, parameterValues);
		 * results.addElement(retVal);
		 * //cmdList.addElement(CommandCreator.createCommand(null, method,
		 * parentObjects[i], parameterValues));
		 * 
		 * } else {
		 * 
		 * cmdList.addElement(cmd); } } } catch (Exception e) {
		 * e.printStackTrace(); } } Vector cmdResults; if (cmdList.isNoOp())
		 * return null; if (cmdList.size() == 1) {
		 * results.addElement(parentFrame
		 * .getUndoer().execute(cmdList.elementAt(0)));
		 * 
		 * } else if (cmdList.size() > 1) { cmdResults = (Vector)
		 * parentFrame.getUndoer().execute(cmdList); for (int i = 0; i <
		 * cmdResults.size(); i++) results.addElement(cmdResults.elementAt(0));
		 * } if (parentFrame != null) parentFrame.doImplicitRefresh(); if (
		 * results.size() == 0) return null; else if (results.size() == 1)
		 * return results.elementAt(0); else return results;
		 */
	}

	public static boolean isSeparateThread(MethodProxy method, Object object) {
		AnInheritedAttributeValue separateThread = AttributeManager
				.getInheritedAttribute(object, method,
						AttributeNames.SEPARATE_THREAD, null);
		if (separateThread == null)
			return false;
		return (Boolean) separateThread.getValue();
	}

	static Object toReturnValue(Vector results) {
		if (results.size() == 0)
			return null;
		else if (results.size() == 1)
			return results.elementAt(0);
		else
			return results;
	}

	public static Object invokeMethods(uiFrame parentFrame,
			Vector objectMethodsList, Vector parameterValuesList,
			Vector commandListenerList) {
		if (parentFrame != null)
			parentFrame.doUpdateAll();
		Vector results = new Vector();
		CommandList cmdList = new CommandList();
		boolean someoneElseWillRefresh = false;
		if (parentFrame != null) {
			someoneElseWillRefresh = parentFrame.getRefreshWillHappen();
			if (!someoneElseWillRefresh)
				parentFrame.setRefreshWillHappen(true);
			parentFrame.setReceivedNotification(false);
		}
		// boolean foundVoid = false;
		for (int commandNum = 0; commandNum < objectMethodsList.size(); commandNum++) {
			// uiFrame parentFrame = (uiFrame)
			// parentFrameList.elementAt(commandNum);
			Hashtable objectMethods = (Hashtable) objectMethodsList
					.elementAt(commandNum);
			Object[] parameterValues = (Object[]) parameterValuesList
					.elementAt(commandNum);
			CommandListener commandListener = (CommandListener) commandListenerList
					.elementAt(commandNum);
			// Vector results = new Vector();
			Enumeration keys = objectMethods.keys();
			// why do it here?
			/*
			 * if (parentFrame != null) { someoneElseWillRefresh =
			 * parentFrame.getRefreshWillHappen(); if (!someoneElseWillRefresh)
			 * parentFrame.setRefreshWillHappen(true); }
			 */
			// CommandList cmdList = new CommandList();
			while (keys.hasMoreElements()) {
				Object parentObject = keys.nextElement();
				MethodProxy method = (MethodProxy) objectMethods
						.get(parentObject);
				if (!validateMethod(method, parentObject, parameterValues)) {
					UnsuccessfulValidation.newCase(method, parentObject, parameterValues, MethodInvocationManager.class);
//					Tracer.warning("Validation of method: " + method
//							+ "of object: " + parentObject + " with parameter values " + parameterValues + " returns false");
					continue;

				}

				try {

					// Following line required for some version of 1.2beta
					// method.setAccessible(true);
					/*
					 * if ((method.getReturnType() == Void.TYPE) &&
					 * Modifier.isSynchronized(method.getModifiers()) &&
					 * parentFrame != null &&
					 * parentFrame.createMethodInvocationThreads() ) {
					 * parentFrame.getMethodInvocationBuffer().put( new
					 * MethodInvocation(parentFrame, parentObject, method,
					 * parameterValues)); }
					 */
					// if (isSeparateThread(method) &&
					// uiBean.isVoidSynchronized(method)
					if (isSeparateThread(method, parentObject)
//							&& IntrospectUtility.isVoidSynchronized(method)
							&& parentFrame != null
							&& parentFrame.createMethodInvocationThreads()) {
//						Tracer.warning("Using a separate ObjectEditor thread for invoking: " + method + 
//						".\n   If you know threads, you might want to create a thread yourself.");
						SeparateThreadRequest.newCase(method, MethodInvocationManager.class);
						parentFrame.getMethodInvocationBuffer().put(
								new MethodInvocation(parentFrame, parentObject,
										method, parameterValues));
					} else {
						/*
						 * Object retVal = invokeMethod(parentObject, method,
						 * parameterValues); if (retVal != null)
						 * results.addElement(retVal);
						 */
						Command cmd;
						boolean isVoid = method.getReturnType() != method
								.getDeclaringClass().voidType();
						// foundVoid = foundVoid || isVoid;
						if (parentFrame == null || commandListener == null
								||
								// (cmd =
								// CommandCreator.createCommand(parentFrame.getAdapter(),
								// method, parentObject, parameterValues)) ==
								// null) {

								(cmd = CommandCreator.createCommand(
										commandListener, method, parentObject,
										parameterValues)) == null) {
							Object retVal = invokeMethod(parentObject, method,
									parameterValues);
							if (method.getReturnType() != method
									.getDeclaringClass().voidType())
								results.addElement(retVal);
							// cmdList.addElement(CommandCreator.createCommand(null,
							// method, parentObjects[i], parameterValues));

						} else {
							// if (method.getReturnType() != Void.TYPE)
							cmdList.addElement(cmd);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		Vector cmdResults;
		if (cmdList.isNoOp()
		// && !parentFrame.isDummy()
		) {
			if (parentFrame != null && !someoneElseWillRefresh) {
				// parentFrame.doImplicitRefresh();
				parentFrame.setRefreshWillHappen(false);
			}

			// return null;
			return toReturnValue(results);
		}
		if (cmdList.size() == 1) {
			results.addElement(parentFrame.getUndoer().execute(
					cmdList.elementAt(0)));

		} else if (cmdList.size() > 1) {
			cmdResults = (Vector) parentFrame.getUndoer().execute(cmdList);
			for (int i = 0; i < cmdResults.size(); i++)
				results.addElement(cmdResults.elementAt(0));
		}

		// if (parentFrame != null && !someoneElseWillRefresh && results.size()
		// == 0) {
		// put and remove method return values!
		if (parentFrame != null && !someoneElseWillRefresh
				&& !parentFrame.getReceivedNotification() && implicitRefresh) {
			parentFrame.doImplicitRefresh();
			parentFrame.setRefreshWillHappen(false);
		}
		if (parentFrame.getReceivedNotification()) {
			parentFrame.checkPreInMenuTreeAndButtonCommands();
			parentFrame.setReceivedNotification(false);
		}
		return toReturnValue(results);
		/*
		 * if ( results.size() == 0) return null; else if (results.size() == 1)
		 * return results.elementAt(0); else return results;
		 */
	}

	boolean doAll = false;

	public void setParameterValue(int parameterNumber, Object newValue) {
		parameterValues[parameterNumber] = newValue;
	}

	String getResultTitle(Object result) {
		if (method == null && constructor == null)
			return parameterNames[parameterNumber] + ":" + result.toString();
		String methodName = "";
		if (method != null)
			// String methodName = uiGenerator.beautify(method.getName());
			methodName = Common.beautify(method.getName());
		else
			methodName = Common.beautify(constructor.getName());
		// String parameterArrayToString = parameterValues.toString();
		String parameterSignature = "(";
		for (int i = 0; i < parameterValues.length; i++) {
			if (i > 0)
				parameterSignature += ", ";
			parameterSignature += parameterValues[i];
		}
		parameterSignature += ")";
		// String parameterSignature = '(' +
		// parameterArrayToString.substring(1,parameterArrayToString.length() -1
		// ) + ')';
		return methodName + parameterSignature;

	}

	// public void invokeMethod() {
	public uiFrame displayResult(Object result) {
		uiFrame tf = (uiFrame) ObjectEditor.edit(result);
		// tf.setTitle(result.toString());
		tf.setTitle(getResultTitle(result));
		return tf;
	}

	static boolean validateMethod(MethodProxy theMethod, Object parentObject,
			Object[] parameterValues) {
		if (theMethod == null)
			return true;
		ClassProxy classProxy = theMethod.getDeclaringClass();
		MethodProxy validateMethod = IntrospectUtility.getValidate(theMethod,
				classProxy);
		if (validateMethod == null)
			return true;
		Boolean retVal = (Boolean) MethodInvocationManager.invokeMethod(
				validateMethod, parentObject, parameterValues);
		return retVal;
	}

	public Object invokeMethod() {
		recalculateParameters();
		/*
		 * if (!validateMethod()) { Message.warning("Validation of:" +
		 * method.getName() + " returns false"); return null; }
		 */

		result = null;
		CommandListener cl;
		if ((commandListener == null) && (parentFrame != null))
			cl = parentFrame.getAdapter();
		else
			cl = commandListener;

		if (method != null) {
			if (objectMethods != null) {
				result = invokeMethods(parentFrame, objectMethods,
						parameterValues, cl);
			} else if (doAll)
				result = invokeMethods(parentFrame, parentObjects, method,
						parameterValues, cl);
			else
				result = invokeMethods(parentFrame, parentObjects, method,
						parameterValues, cl);
			/*
			 * try {
			 * 
			 * if (method.getReturnType() == Void.TYPE) { result = null; if
			 * (parentFrame.getAnimationMode())
			 * parentFrame.getMethodInvocationBuffer().put( new
			 * MethodInvocation(parentObject, method, parameterValues)); else
			 * result = method.invoke(parentObject, parameterValues); } else
			 * result = method.invoke(parentObject, parameterValues); } catch
			 * (Exception e) { e.printStackTrace(); }
			 */
		} else if (constructor != null) {
			try {
				// System.out.println("Constructor is "+constructor);
				// if (parentObject == null)
				// System.out.println("Null parent object");
				// System.out.println("Parent object is "+parentObject);
				// System.out.println("Parameters are ");
				// for (int i=0; i<parameterValues.length; i++)
				// System.out.println("Parameter "+(i+1)+":"+parameterValues[i]);

				result = constructor.newInstance(parameterValues);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		 * if (result != null && parameterNumber == -1) {
		 */
		// boolean inPlaceResult = (Boolean)
		// ClassDescriptor.getAttributeValue(method,
		// AttributeNames.INPLACE_METHOD_RESULT);
		if (resultComponent != null
				|| autoEditMethod
				|| (method != null && !manualUI && (Boolean) AttributeNames
						.getDefaultOrSystemDefault(AttributeNames.INPLACE_METHOD_RESULT)))
			// if (resultComponent != null || (method != null && !manualUI &&
			// inPlaceResult) )
			return result;
		if (result != null && displayResult) {
//		if (result != null && displayResult) {
			// Display the return value
			
			
			// uiGenerator temp = new uiGenerator();
			// System.out.println("calling generate" + parentFrame);
			// uiFrame tf = uiGenerator.generateUIFrame(parentFrame, result);
			// uiFrame tf = uiGenerator.generateUIFrame(result);
			// if ( !manualUI && (Boolean)
			// AttributeNames.getDefaultOrSystemDefault(AttributeNames.INPLACE_METHOD_RESULT)
			// ) {
			uiFrame tf = displayResult(result);
			/*
			 * uiFrame tf = ObjectEditor.edit(result);
			 * //tf.setTitle(result.toString());
			 * tf.setTitle(getResultTitle(result));
			 */

			// tf.addDoneItem();
			// tf.pack();
			// tf.show();
			// tf.toolBar();
			tf.getTopViewManager().maybeShowToolbar();
			// System.exit(0);
			constructorFrame = tf;
			constructorFrame.addDoneListener(parentMIM);
			VirtualMenuItem okButton = constructorFrame.addDoneItem();
			if (okButton != null)
				okButton.addActionListener(parentMIM);
			// System.out.println(parameterNumber + " " + this);
			constructorFrame.setParameterNumber(parameterNumber);
			// }
			// System.out.println("setting parameter number");
			// f.pack();
			constructorFrame.setLocation(new Point(0,
					150 * (parameterNumber + 1)));

			// let us initialize parameter number before object is edited
			if (parameterNumber >= 0) {
				ObjectAdapter adaptor = constructorFrame.getAdapter();
				parentMIM.parameterValues[parameterNumber] = adaptor.getValue();
				// Feedback
				parentMIM.notifyObjectParameterListeners(parameterNumber,
						adaptor.getValue());
				JTextField ptf = (JTextField) parentMIM.textFieldToParameterMapping
						.elementAt(parameterNumber);
				ptf.setText(parentMIM.parameterValues[parameterNumber]
						.toString());
				if (parentMIM.invokerEditor != null)
					parentMIM.invokerEditor.getFrame().pack();
			}

			// f.setLocation( new Point (0, 150*(parameterNumber + 1)));
			/*
			 * if (parentFrame == null) { uiFrame tf =
			 * uiGenerator.generateUIFrame(result, null); //tf.addDoneItem();
			 * tf.pack(); tf.show(); constructorFrame = tf; } else { uiFrame tf
			 * = uiGenerator.generateUIFrame(parentFrame, result, null);
			 * tf.pack(); tf.show(); //uiObjectAdapter topAdapter =
			 * parentFrame.getAdapter(); //topAdapter.setValue(result); }
			 */
		}
		else 
			if (parameterNumber >= 0) {
				parentMIM.parameterValues[parameterNumber] = result;
				parentMIM.notifyObjectParameterListeners(parameterNumber,
						result);
				JTextField ptf = (JTextField) parentMIM.textFieldToParameterMapping
						.elementAt(parameterNumber);
				ptf.setText(parentMIM.parameterValues[parameterNumber]
						.toString());
				if (parentMIM.invokerEditor != null)
					parentMIM.invokerEditor.getFrame().pack();
			
		}
		return result;
		/*
		 * if (parentFrame != null) parentFrame.doImplicitRefresh();
		 */

	}

	public Object getParameterValue(int i) {
		return parameterValues[i];
	}

	public void frameDone(uiFrame theFrame) {
		System.out.println("done menu");
		// uiFrame c = ((DoneMenuItem) evt.getSource()).getUIFrame();
		uiFrame c = theFrame;
		int parameterNumber = ((uiFrame) c).getParameterNumber();
		if (parameterNumber < 0) {
			System.out.println("parameter number < 0");
			dispose();
			return;
		}

		// set the parameter value;
		ObjectAdapter adaptor = ((uiFrame) c).getAdapter();
		parameterValues[parameterNumber] = adaptor.getValue();
		// Dispose of the frame
		// System.out.println("disposing frame");
		// ((Frame) c).dispose();
		((uiFrame) c).dispose();
		// Feedback
		JTextField tf = (JTextField) textFieldToParameterMapping
				.elementAt(parameterNumber);
		tf.setText(parameterValues[parameterNumber].toString());
		if (!invokeButton.isVisible()) {
			invokeMethod();
			dispose();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		// System.out.println("action performed");

		if (evt.getSource() instanceof Button
				|| evt.getSource() instanceof JButton) {

			// if (evt.getActionCommand().equals("Invoke method")) {
			// if (evt.getActionCommand().equals("Invoke command")) {
			if (evt.getActionCommand().equals(invokeName)) {

				InvokeButton c = (InvokeButton) evt.getSource();
				if (c.getFrame().equals(this) /* && (parentMIM != null) */) {

					invokeMethod();
					dispose();
				}/*
				 * else { //uiMethodInvocationManager mim =
				 * (uiMethodInvocationManager) c.getFrame();
				 * //System.out.println("action performed on invoke button");
				 * //parameterValues[mim.parameterNumber] = mim.result; //if
				 * (parentMIM != null) { invokeMethod(); dispose(); //}
				 * 
				 * }
				 */
			}
		} else if (evt.getSource() instanceof MenuItem) {
			if (evt.getActionCommand().equals("Close")) {
				dispose();
			} else if (evt.getSource() instanceof DoneMenuItem /*
																 * && evt.
																 * getActionCommand
																 * (
																 * ).equals("Done"
																 * )
																 */) {
				System.out.println("done menu");
				uiFrame c = ((DoneMenuItem) evt.getSource()).getUIFrame();
				int parameterNumber = ((uiFrame) c).getParameterNumber();
				if (parameterNumber < 0) {
					System.out.println("parameter number < 0");
					dispose();
					return;
				}
				/*
				 * if (parameterNumber < 0) { uiMethodInvocationManager mim =
				 * (uiMethodInvocationManager) c.getFrame();
				 * parameterValues[mim.parameterNumber] = mim.result; }
				 */
				// set the parameter value;
				ObjectAdapter adaptor = ((uiFrame) c).getAdapter();
				parameterValues[parameterNumber] = adaptor.getValue();
				// Dispose of the frame
				// System.out.println("disposing frame");
				// ((Frame) c).dispose();
				((uiFrame) c).dispose();
				// Feedback
				JTextField tf = (JTextField) textFieldToParameterMapping
						.elementAt(parameterNumber);
				tf.setText(parameterValues[parameterNumber].toString());
				if (!invokeButton.isVisible()) {
					invokeMethod();
					dispose();
				}
			}
		} else if (evt.getSource() instanceof JTextField) {
			// System.out.println("JTextField edited");
			JTextField tf = (JTextField) evt.getSource();
			int parameterNumber = textFieldToParameterMapping.indexOf(tf);
			Object obj = null;
			ClassProxy ptype = getParameterType(parameterNumber);
			try {
				// System.out.println(tf.getText());
				obj = TranslatorRegistry.convert(ptype.getName(), tf.getText());
			} catch (bus.uigen.translator.FormatException e) {
			}
			if (obj != null)
				parameterValues[parameterNumber] = obj;
			else {
				JOptionPane.showMessageDialog(null,
						"Couldnt convert String to " + ptype.getName(),
						"Conversion error", JOptionPane.ERROR_MESSAGE);
			}
		}
		/*
		 * else if (evt.getSource() instanceof JComboBox) { JComboBox cb =
		 * (JComboBox) evt.getSource(); int param =
		 * comboBoxToParameterMapping.indexOf(cb); if (param != -1) { String
		 * selection = (String) cb.getSelectedItem();
		 * System.out.println("For type "
		 * +parameterTypes[param].getName()+" event is "+selection); // Now
		 * modify the class mapping Class pclass = getParameterType(param); if
		 * (!pclass.equals(parameterTypes[param]))
		 * uiClassMapper.updateClassMapping(parameterTypes[param].getName(),
		 * pclass.getName()); } }
		 */
	}
	
	public void actionPerformed(VirtualActionEvent evt) {
		// System.out.println("action performed");

		if (evt.getSource() instanceof Button
				|| evt.getSource() instanceof JButton) {

			// if (evt.getActionCommand().equals("Invoke method")) {
			// if (evt.getActionCommand().equals("Invoke command")) {
			if (evt.getActionCommand().equals(invokeName)) {

				InvokeButton c = (InvokeButton) evt.getSource();
				if (c.getFrame().equals(this) /* && (parentMIM != null) */) {

					invokeMethod();
					dispose();
				}/*
				 * else { //uiMethodInvocationManager mim =
				 * (uiMethodInvocationManager) c.getFrame();
				 * //System.out.println("action performed on invoke button");
				 * //parameterValues[mim.parameterNumber] = mim.result; //if
				 * (parentMIM != null) { invokeMethod(); dispose(); //}
				 * 
				 * }
				 */
			}
		} else if (evt.getSource() instanceof MenuItem) {
			if (evt.getActionCommand().equals("Close")) {
				dispose();
			} else if (evt.getSource() instanceof DoneMenuItem /*
																 * && evt.
																 * getActionCommand
																 * (
																 * ).equals("Done"
																 * )
																 */) {
				System.out.println("done menu");
				uiFrame c = ((DoneMenuItem) evt.getSource()).getUIFrame();
				int parameterNumber = ((uiFrame) c).getParameterNumber();
				if (parameterNumber < 0) {
					System.out.println("parameter number < 0");
					dispose();
					return;
				}
				/*
				 * if (parameterNumber < 0) { uiMethodInvocationManager mim =
				 * (uiMethodInvocationManager) c.getFrame();
				 * parameterValues[mim.parameterNumber] = mim.result; }
				 */
				// set the parameter value;
				ObjectAdapter adaptor = ((uiFrame) c).getAdapter();
				parameterValues[parameterNumber] = adaptor.getValue();
				// Dispose of the frame
				// System.out.println("disposing frame");
				// ((Frame) c).dispose();
				((uiFrame) c).dispose();
				// Feedback
				JTextField tf = (JTextField) textFieldToParameterMapping
						.elementAt(parameterNumber);
				tf.setText(parameterValues[parameterNumber].toString());
				if (!invokeButton.isVisible()) {
					invokeMethod();
					dispose();
				}
			}
		} else if (evt.getSource() instanceof JTextField) {
			// System.out.println("JTextField edited");
			JTextField tf = (JTextField) evt.getSource();
			int parameterNumber = textFieldToParameterMapping.indexOf(tf);
			Object obj = null;
			ClassProxy ptype = getParameterType(parameterNumber);
			try {
				// System.out.println(tf.getText());
				obj = TranslatorRegistry.convert(ptype.getName(), tf.getText());
			} catch (bus.uigen.translator.FormatException e) {
			}
			if (obj != null)
				parameterValues[parameterNumber] = obj;
			else {
				JOptionPane.showMessageDialog(null,
						"Couldnt convert String to " + ptype.getName(),
						"Conversion error", JOptionPane.ERROR_MESSAGE);
			}
		}
		/*
		 * else if (evt.getSource() instanceof JComboBox) { JComboBox cb =
		 * (JComboBox) evt.getSource(); int param =
		 * comboBoxToParameterMapping.indexOf(cb); if (param != -1) { String
		 * selection = (String) cb.getSelectedItem();
		 * System.out.println("For type "
		 * +parameterTypes[param].getName()+" event is "+selection); // Now
		 * modify the class mapping Class pclass = getParameterType(param); if
		 * (!pclass.equals(parameterTypes[param]))
		 * uiClassMapper.updateClassMapping(parameterTypes[param].getName(),
		 * pclass.getName()); } }
		 */
	}

	public uiFrame processParamType(int i, ClassProxy pclass,
			String pChangedType, boolean chooseConstructor) {
		// System.out.println("process paramtype" + pChangedType);
		if (pclass == null)
			return null;
		if (pChangedType == null)
			return null;
		ClassProxy nclass;
		try {
			// ClassProxy nclass = uiClassFinder.forName(pclass, pChangedType);
			nclass = uiClassFinder.forName(pclass, pChangedType);
			// ClassProxy nclass = pclass.forName(pChangedType);
			if (nclass == null)
				return null;
			if (nclass != null && pclass.isAssignableFrom(nclass)) {
				pclass = nclass;
			} else {
				// System.out.println(pclass.getName()+" is not assignable from "+nclass.getName());
				/*
				 * String interfacePackageName =
				 * Misc.packageName(pclass.getName()); String inputPackageName =
				 * Misc.packageName(pChangedType); if (interfacePackageName !=
				 * null && inputPackageName == null) { try { nclass =
				 * Class.forName(inputPackageName + '.' + pChangedType); } catch
				 * (Exception e) { ((JTextField)
				 * textFieldToParameterMapping.elementAt(i)).setEnabled(false);
				 * }
				 * 
				 * }
				 */
				((JTextField) textFieldToParameterMapping.elementAt(i))
						.setEnabled(false);
				return null;
			}
		} catch (Exception e) {
			// System.out.println("Class finder exception" + pclass);
			return null;

		}
		// System.out.println("checking interfcae");
		if (pclass.isInterface())
			return null;
		if (Modifier.isAbstract(pclass.getModifiers()))
			return null;
		// System.out.println("checking length" + pclass +
		// pclass.getConstructors().length );
		if (pclass.getConstructors().length == 0)
			return null;
		// System.out.println("checking composite");
		if (!isComposite(pclass)) {
			return null;
		}

		// System.out.println("found composite");
		if (!creatingObject && i >= 0)
			((JTextField) textFieldToParameterMapping.elementAt(i))
					.setEnabled(false);
		// Constructor constructor;
		return instantiateClass(i, pclass, chooseConstructor);
		/*
		 * MethodProxy constructor; if (chooseConstructor) {
		 * uiConstructorChooser cChooser = new uiConstructorChooser(pclass);
		 * constructor = cChooser.getConstructor(); } else { //constructor =
		 * pclass.getConstructors()[0]; //constructor =
		 * Misc.parameterLessConstructor(pclass); constructor =
		 * ClassDescriptor.parameterLessConstructor(pclass); if (constructor ==
		 * null) constructor = pclass.getConstructors()[0]; //constructor =
		 * ClassDescriptor.getConstructors(pclass)[0]; //constructor =
		 * pclass.getConstructors()[0]; } // Create a new MIManager.
		 * //System.out.println("UImETHOD" + parentFrame); //setVisible(true);
		 * uiMethodInvocationManager mim = new uiMethodInvocationManager(this,
		 * i, null, //uiMethodInvocationManager mim = new
		 * uiMethodInvocationManager(parentFrame, null, constructor);
		 * 
		 * //System.out.println("cons rame" + mim.constructorFrame); if
		 * (!creatingObject) {
		 * 
		 * //System.out.println("Constructor manager successfully created"); if
		 * ( !creatingObject && (mim.invokeButton != null ) &&
		 * mim.invokeButton.isVisible()) {
		 * 
		 * mim.invokeButton.addActionListener(this); }
		 * //System.out.println("setting parameter number" + i + mim);
		 * mim.parameterNumber = i;
		 * 
		 * 
		 * //uiFrame f = mim.constructorFrame; //MenuItem okButton =
		 * f.addDoneItem(); //okButton.addActionListener(this);
		 * //constructorFrame.setLocation( new Point (0, 150*(i + 1)));
		 * 
		 * } else dispose(); return mim.constructorFrame;
		 */
	}

	public uiFrame instantiateClass(int i, ClassProxy pclass,
			boolean chooseConstructor) {

		// System.out.println("found composite");
		if (!creatingObject && i >= 0)
			((JTextField) textFieldToParameterMapping.elementAt(i))
					.setEnabled(false);
		// Constructor constructor;
		MethodProxy constructor;
		if (chooseConstructor) {
			ConstructorChooser cChooser = new ConstructorChooser(pclass);
			constructor = cChooser.getConstructor();
		} else {
			// constructor = pclass.getConstructors()[0];
			// constructor = Misc.parameterLessConstructor(pclass);
			constructor = AClassDescriptor.parameterLessConstructor(pclass);
			if (constructor == null)
				constructor = pclass.getConstructors()[0];
			// constructor = ClassDescriptor.getConstructors(pclass)[0];
			// constructor = pclass.getConstructors()[0];
		}
		// Create a new MIManager.
		// System.out.println("UImETHOD" + parentFrame);
		// setVisible(true);
		MethodInvocationManager mim = new MethodInvocationManager(this, i,
				null,
				// uiMethodInvocationManager mim = new
				// uiMethodInvocationManager(parentFrame, null,
				constructor);

		// System.out.println("cons rame" + mim.constructorFrame);
		if (!creatingObject) {

			// System.out.println("Constructor manager successfully created");
			if (!creatingObject && (mim.invokeButton != null)
					&& mim.invokeButton.isVisible()) {

				mim.invokeButton.addActionListener(this);
			}/*
			 * else if (mim.constructorFrame != null) {
			 * mim.constructorFrame.getDoneItem().addActionListener(this);
			 * mim.constructorFrame.setParameterNumber(i); }
			 */
			// System.out.println("setting parameter number" + i + mim);
			mim.parameterNumber = i;
			/*
			 * 
			 * //let us initialize parameter number before object is edited if
			 * (mim.constructorFrame != null) { uiObjectAdapter adaptor =
			 * mim.constructorFrame.getAdapter(); parameterValues[i] =
			 * adaptor.getValue(); // Feedback JTextField tf = (JTextField)
			 * textFieldToParameterMapping.elementAt(i);
			 * tf.setText(parameterValues[i].toString()); }
			 */

			// uiFrame f = mim.constructorFrame;
			// MenuItem okButton = f.addDoneItem();
			// okButton.addActionListener(this);
			// constructorFrame.setLocation( new Point (0, 150*(i + 1)));

		} else
			dispose();
		return mim.constructorFrame;

	}

	public static boolean chooseParameterLessCostructor = false;

	public static void setChooseParameterLessConstructor(boolean newVal) {
		chooseParameterLessCostructor = newVal;
	}

	public static boolean getChooseParameterLessConstructor() {
		return chooseParameterLessCostructor;
	}

	public static uiFrame instantiateClass(ClassProxy pclass) {
		// Constructor constructor = null;
		MethodProxy constructor = null;
		// Constructor constructor = parameterLessConstructor(pclass);
		if (getChooseParameterLessConstructor())
			// constructor = Misc.parameterLessConstructor(pclass);
			constructor = AClassDescriptor.parameterLessConstructor(pclass);
		if (constructor == null) {
			ConstructorChooser cChooser = new ConstructorChooser(pclass);
			if (cChooser == null)
				return null;
			constructor = cChooser.getConstructor();
		}
		MethodInvocationManager mim = new MethodInvocationManager(null, -1,
				null, constructor);
		return mim.constructorFrame;
		// mim.invokeButton.addActionListener(actionListener);
	}

	void createObject(ActionEvent evt, boolean chooseConstructor) {
		// System.out.println("do create");
		Component c;
		if (!((c = (Component) evt.getSource()) instanceof JComboBox)) {
			JPopupMenu popup = (JPopupMenu) ((Component) evt.getSource())
					.getParent();
			c = popup.getInvoker();
		}
		/*
		 * JPopupMenu popup = (JPopupMenu)
		 * ((Component)evt.getSource()).getParent(); Component c =
		 * popup.getInvoker();
		 */
		if (c.getParent() instanceof JPanel) {
			ClassProxy pclass = OBJECT_CLASS_PROXY;
			String pChangedType;
			int i = -1;
			if (!creatingObject) {
				i = panelToParameterMapping.indexOf(c.getParent());

				// Class pclass = parameterTypes[i];
				pclass = parameterTypes[i];

				// String pChangedType = (String) ((JComboBox)
				// comboBoxToParameterMapping.elementAt(i)).getSelectedItem();*/
				pChangedType = (String) ((JComboBox) comboBoxToParameterMapping
						.elementAt(i)).getSelectedItem();
				// System.out.println("pchanged type " + pChangedType);
				// Object pobj = parameterValues[i];
			} else {
				pclass = OBJECT_CLASS_PROXY;
				pChangedType = (String) objectComboBox.getSelectedItem();
				// System.out.println("pchanged type " + pChangedType);
			}
			processParamType(i, pclass, pChangedType, chooseConstructor);
			// System.out.println("processed param type");
		}
	}

	/*
	 * public void createObject(int i, ClassProxy pclass, String pChangedType,
	 * boolean chooseConstructor) { //System.out.println("do create");
	 * 
	 * 
	 * if (creatingObject) { pclass = OBJECT_CLASS_PROXY; pChangedType =
	 * (String) objectComboBox.getSelectedItem();
	 * //System.out.println("pchanged type " + pChangedType); }
	 * processParamType(i, pclass, pChangedType, chooseConstructor);
	 * //System.out.println("processed param type");
	 * 
	 * }
	 */
	public void recalculateParameters() {
		JTextField tf;
		if (parameterValues == null)
			return;
		if (!manualUI)
			return;
		for (int i = 0; i < parameterValues.length; i++) {
			if (textFieldToParameterMapping == null)
				return;
			tf = (JTextField) textFieldToParameterMapping.elementAt(i);
			if (tf == null)
				return;
			ClassProxy ptype = getParameterType(i);
			if (tf.isEnabled() && !isComposite(ptype)) {
				Object obj = null;
				try {
					// System.out.println(tf.getText());
					obj = TranslatorRegistry.convert(ptype.getName(),
							tf.getText());
				} catch (bus.uigen.translator.FormatException e) {
				}
				if (obj != null)
					parameterValues[i] = obj;
			}
		}

	}

	private void doCreateAction(ActionEvent evt) {
		createObject(evt, true);
		/*
		 * System.out.println("do create"); Component c; if (!((c = (Component)
		 * evt.getSource()) instanceof JComboBox)) { JPopupMenu popup =
		 * (JPopupMenu) ((Component)evt.getSource()).getParent(); c =
		 * popup.getInvoker(); }
		 * 
		 * if (c.getParent() instanceof JPanel) { Class pclass = OBJECT_CLASS;
		 * String pChangedType; int i = -1; if (!creatingObject) { i =
		 * panelToParameterMapping.indexOf(c.getParent());
		 * 
		 * 
		 * //Class pclass = parameterTypes[i]; pclass = parameterTypes[i];
		 * 
		 * //String pChangedType = (String) ((JComboBox)
		 * comboBoxToParameterMapping.elementAt(i)).getSelectedItem();
		 * pChangedType = (String) ((JComboBox)
		 * comboBoxToParameterMapping.elementAt(i)).getSelectedItem();
		 * //System.out.println("pchanged type " + pChangedType); Object pobj =
		 * parameterValues[i]; } else { pclass = OBJECT_CLASS; pChangedType =
		 * (String) objectComboBox.getSelectedItem();
		 * System.out.println("pchanged type " + pChangedType); }
		 * processParamType(i, pclass, pChangedType, true);
		 * System.out.println("processed param type");
		 * 
		 * 
		 * }
		 */

	}

	// Check if the user has specialised a parameter
	// type. if so return this if it is valid
	private ClassProxy getParameterType(int i) {
		ClassProxy pclass = parameterTypes[i];
		String pChangedType = (String) ((JComboBox) comboBoxToParameterMapping
				.elementAt(i)).getSelectedItem();
		try {
			ClassProxy nclass = uiClassFinder.forName(pChangedType);
			if (pclass.isAssignableFrom(nclass)) {
				// Update the class mapping
				/*
				 * uiClassMapper.updateClassMapping(pclass.getName(),
				 * nclass.getName());
				 */
				uiClassMapper.updateClassMapping(pclass,
						AClassDescriptor.toShortName(nclass.getName()));
				return nclass;
			}
		} catch (Exception e) {
		}
		return pclass;
	}

	String[] excludeClassesArray = { "java.lang.Object", "java.lang.String" };

	Vector<ClassProxy> excludeClasses = new Vector();

	void setExcludeClasses() {
		for (int i = 0; i < excludeClassesArray.length; i++)
			try {
				excludeClasses.addElement(RemoteSelector
						.forName(excludeClassesArray[i]));
			} catch (Exception e) {
				System.out.println("Internal Error in Coverting "
						+ excludeClassesArray[i] + "to class");
			}
		;
	}

	public boolean isComposite(ClassProxy pclass) {
		// System.out.println("composite" + pclass);
		if (pclass.isPrimitive())
			return false;
		// System.out.println("execlide" + excludeClasses);
		// boolean b= excludeClasses.contains(pclass);
		// System.out.println(b);
		// if (b) return false;
		if (excludeClasses.contains(pclass))
			return false;
		return true;
	}

	public uiFrame editParameter(int i) {
		// System.out.println("edit paame" + i);
		if (i < 0)
			System.out.println("negative i " + i);
		Object pobj = parameterValues[i];
		ClassProxy pclass = getParameterType(i);
		return this.processParamType(i, pclass, pclass.getName(), false);
		/*
		 * if (!isComposite(pclass)) { return null; } ((JTextField)
		 * textFieldToParameterMapping.elementAt(i)).setEnabled(false);
		 * 
		 * 
		 * if (pobj == null || !pclass.isInstance(pobj)) { pobj =
		 * uiInstantiator.newInstance(pclass); } if (pobj != null) { uiFrame f =
		 * uiGenerator.generateUIFrame(pobj, null);
		 * //uiGenerator.uiAddConstants(f, pobj, this); f.setParameterNumber(i);
		 * MenuItem okButton = f.addDoneItem();
		 * okButton.addActionListener(this);
		 * 
		 * f.pack(); f.setLocation( new Point (0, 150*(i + 1)));
		 * f.setVisible(true); return f; } else {
		 * System.out.println("Couldnt instantiate parameter type " +
		 * pclass.getName()); return null; }
		 */
	}

	private void doEditAction(ActionEvent evt) {
		if (creatingObject) {
			createObject(evt, false);
			return;
		}
		JPopupMenu popup = (JPopupMenu) ((Component) evt.getSource())
				.getParent();
		Component c = popup.getInvoker();
		if (c.getParent() instanceof JPanel) {
			int i = panelToParameterMapping.indexOf(c.getParent());
			uiFrame f;

			if ((f = editParameter(i)) != null
					&& (i == parameterTypes.length - 1)) {

				if (invokeButton != null)
					invokeButton.setVisible(false);

			}
			;

			// editParameter(i);
		}
	}

	// Do everything related to a Paste
	// user action.
	private void doPasteAction(ActionEvent evt) {
		// First find out which parameter this
		// corresponds to.
		JPopupMenu popup = (JPopupMenu) ((Component) evt.getSource())
				.getParent();
		Component c = popup.getInvoker();
		if (c.getParent() instanceof JPanel) {
			int parameterNumber = panelToParameterMapping
					.indexOf(c.getParent());
			// Get the current selection and check if type matches.
			// Object selection = ((uiObjectAdapter)
			// uiSelectionManager.getCurrentSelection()).getRealObject();
			// System.out.println(selection.getClass().toString());
			Object selection = ObjectClipboard.getFirst();
			if (parameterTypes[parameterNumber]
					.isAssignableFrom(ACompositeLoggable
							.getTargetClass(selection))) {
				// Can perform a paste
				// System.out.println("Selection is "+selection);
				parameterValues[parameterNumber] = selection;
				// Feedback
				JTextField tf = (JTextField) textFieldToParameterMapping
						.elementAt(parameterNumber);
				tf.setText(selection.toString());
			} else
				System.out.println("Wrong parameter type:"
						+ ACompositeLoggable.getTargetClass(selection)
								.toString() + " Expecting: "
						+ parameterTypes[parameterNumber].toString());
		}
	}

	HashSet<ObjectParameterListener> objectParameterListeners = new HashSet();

	public void addObjectParameterListener(ObjectParameterListener theListener) {
		objectParameterListeners.add(theListener);
	}

	public void notifyObjectParameterListeners(int theParameterNumber,
			Object theValue) {
		Iterator<ObjectParameterListener> listeners = objectParameterListeners
				.iterator();
		while (listeners.hasNext())
			listeners.next().newUserValue(theParameterNumber, theValue);
	}

}
