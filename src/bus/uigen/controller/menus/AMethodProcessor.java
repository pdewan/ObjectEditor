package bus.uigen.controller.menus;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import util.misc.Common;
import util.models.ADynamicSparseList;
import util.models.Hashcodetable;
import bus.uigen.WidgetAdapter;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AnInheritedAttributeValue;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.compose.ButtonCommand;
import bus.uigen.controller.models.FrameModelRegistry;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.ConstructorDescriptorProxy;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.VirtualMethodDescriptor;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.ReflectUtil;
import bus.uigen.trace.MenuAdditionEnded;
import bus.uigen.trace.MenuAdditionStarted;
import bus.uigen.trace.ObjectMenuAdditionStarted;
import bus.uigen.trace.PredefinedMenuAttributeSettingStarted;
import bus.uigen.widgets.UniversalWidget;
import bus.uigen.widgets.VirtualButton;

public class AMethodProcessor {
	static String[] excludeMethodCategoriesArray = { "Constructors" };

	static Vector excludeMethodCategories = util.misc.Common
			.arrayToVector(excludeMethodCategoriesArray);

	static Hashcodetable objCommands = null;

	// static Hashtable methodString = new Hashtable(); //just storing a
	// hashtable of the string form of the meths. and props
	static Hashcodetable methodString = null;

	static Object currentObject = null;

	static Vector<ButtonCommand> commandList = new Vector();

	static Hashcodetable methodHash = new Hashcodetable();

	public static void addMenuObjects(uiFrame frame, Object object) {
		MenuAdditionStarted.newCase(object, AMethodProcessor.class);
		ObjectAdapter adapter = frame.getAdapter();
		if (adapter == null) return;
		if (adapter.getShowObjectMenus())
			addMethodsToMenus(frame, object, null);	
		// this was commented out for some reason!
		if (adapter.getShowSystemMenus()) 
			addPredefinedMenuModels(frame, adapter, object);
		//if (adapter.getShowSystemMenus()) 
			frame.registerFullNamesInMenuTree();
		//}
		// AttributeRegistry.registerAll();
		if (adapter.getShowObjectMenus() || adapter.getShowSystemMenus()) {
			frame.displayMenuTree();
			frame.checkPreInMenuTreeAndButtonCommands();
		}
		frame.checkPreInToolbar();
		MenuAdditionEnded.newCase(object, AMethodProcessor.class);


	}

	public static void addPredefinedMenuModels(uiFrame frame, ObjectAdapter adapter, Object obj) {
		setPredefinedMenuAttributes(frame, adapter, obj);
		FrameModelRegistry.registerAll(frame, obj, adapter);
		// addFileMenuModel(frame);
		/*
		 * AFileMenuModel fileMenuModel = frame.getFileMenuModel();
		 * frame.addMenuObject(fileMenuModel);
		 */

	}

	public static void setPredefinedMenuAttributes(uiFrame frame, ObjectAdapter adapter, Object obj) {
		PredefinedMenuAttributeSettingStarted.newCase(adapter, AMethodProcessor.class);
		String[] menusChoice = adapter.getPredefinedMenusChoice();
		if (menusChoice.length == 0)
			return;
		if (menusChoice[0].equals(AttributeNames.ALL_MENUS_CHOICE))
			MenuDescriptorRegistry.setAll(frame.getMenuDescriptor(), frame);		
//			MenuDescriptorRegistry.setAll(frame.getMenuDescriptor(), frame);
		else
			MenuDescriptorRegistry.setMenus(frame.getMenuDescriptor(), frame, menusChoice);	
		
		
	}

	/*
	 * public static void setFileMenuAttributes(uiFrame frame) {
	 * 
	 * //ObjectEditor.setLabel(AFileOperationsModel.class,
	 * uiFrame.FILE_MENU_NAME + "'"); AMenuDescriptor menuDescriptor =
	 * frame.getMenuDescriptor();
	 * menuDescriptor.setAttribute(uiFrame.FILE_MENU_NAME + "'",
	 * AttributeNames.POSITION, 0);
	 * //ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "open",
	 * "Open..");
	 * //ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "load",
	 * "Load.."); menuDescriptor.setAttribute(uiFrame.FILE_MENU_NAME + "'" +
	 * uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_OBJECT_MENU_NAME,
	 * AttributeNames.POSITION, 0);
	 *  }
	 */

	// method is never called!
	public static void addFileMenuModel(uiFrame frame, Object theObject) {
		FrameModelRegistry.registerAll(frame, theObject, null);
		/*
		 * AFileOperationsModel fileMenuModel = frame.getFileMenuModel();
		 * frame.addMenuObject(fileMenuModel);
		 */
		/*
		 * ObjectEditor.createCommands(frame, fileMenuModel);
		 */

	}
	
	public static String getLabel (MethodDescriptorProxy md) {
		String label = (String) md.getValue(AttributeNames.LABEL);
		if (label == null)
			return md.getDisplayName();
		else
			return label;
			
	}

	public static void addMethodsToMenus(uiFrame frame, Object object) {
		addMethodsToMenus(frame, object, null);
	}

	public static void addMethodsToMenus(uiFrame frame, Object object,
			String menu) {
		addMethodsToMenus(frame, object, menu, true);
	}
	
	public static boolean isMenuMethod (uiFrame frame, MethodDescriptorProxy md,  MethodProxy method, Object targetObject) {
		return isRightMenuMethod(frame, md, method, targetObject, frame.getOriginalAdapter());
		/*
		try {
		return frame.getOriginalAdapter().getConcreteObject().getTargetObject() != targetObject || // in case of menu object this is not the case
		!frame.getOriginalAdapter().getConcreteObject().isPatternMethod(method)   || 			
		(!frame.getOriginalAdapter().getConcreteObject().isEditingMethod(method) && ClassDescriptor.getInheritedAttribute(frame, md,
				AttributeNames.PATTERN_METHODS_NOT_COMMANDS).getValue().equals(Boolean.FALSE))||
			ClassDescriptor.getInheritedAttribute(frame, md,
					AttributeNames.EDITING_METHODS_NOT_COMMANDS).getValue().equals(Boolean.FALSE);
			
		} catch (Exception e) {
			return true; // in case of drawing this path will be taken. Is this really an exception?
		}
		*/
		
		
	}
	
	
	public static boolean isButtonMethod(uiFrame frame, MethodDescriptorProxy md,
			MethodProxy method, Object targetObject, ObjectAdapter adapter) {
		
		Integer rowNum = (Integer) AttributeManager	.getInheritedAttribute(
				frame,
				md,
				AttributeNames.ROW,
				adapter).getValue();
		Integer colNum = (Integer)  AttributeManager	.getInheritedAttribute(
				frame,
				md,
				AttributeNames.COLUMN,
				adapter).getValue();
		return (rowNum != null && rowNum >= 0) || (colNum != null && colNum >= 0);		
		
	}
	
	
	public static boolean isMainMenuMethod(uiFrame frame, MethodDescriptorProxy md,
			MethodProxy method, Object targetObject, ObjectAdapter adapter) {
		try {
			boolean isPatternMethod = adapter.getConcreteObject().isPatternMethod(method);
			boolean isButtonMethod = isButtonMethod(frame, md, method, targetObject, adapter);
			
			return adapter.unparseAsToString() 
					|| adapter.getConcreteObject().getTargetObject() != targetObject
					|| (!isPatternMethod && !isButtonMethod)
					|| (isPatternMethod && AttributeManager.getInheritedAttribute(
									frame,
									md,
									AttributeNames.PATTERN_METHODS_IN_MAIN_MENU,
									adapter).getValue().equals(Boolean.TRUE))
					|| (isButtonMethod && AttributeManager.getInheritedAttribute(
							frame,
							md,
							AttributeNames.BUTTON_METHODS_IN_MAIN_MENU,
							adapter).getValue().equals(Boolean.TRUE));

//			return 
//					adapter.unparseAsToString() ||
//					adapter.getConcreteObject().getTargetObject() != targetObject
//					|| (!adapter.getConcreteObject().isPatternMethod(method)) && 
//					 // in case of menu object this is not the case
//					(!adapter.getConcreteObject().isPatternMethod(method) 
//							|| AttributeManager	.getInheritedAttribute(
//									frame,
//									md,
//									AttributeNames.PATTERN_METHODS_IN_MAIN_MENU,
//									adapter).getValue().equals(Boolean.TRUE))
//			|| // chec
//			 (isButtonMethod(frame, md, method, targetObject, adapter)
//					 
//					&& AttributeManager	.getInheritedAttribute(
//							frame,
//							md,
//							AttributeNames.BUTTON_METHODS_IN_MAIN_MENU,
//							adapter).getValue().equals(Boolean.TRUE));


		} catch (Exception e) {
			return true; // in case of drawing this path will be taken. Is this
							// really an exception?
		}		
	}
	public static boolean isRightMenuMethod(uiFrame frame, MethodDescriptorProxy md,
			MethodProxy method, Object targetObject, ObjectAdapter adapter) {
		try {
			boolean isPatternMethod = adapter.getConcreteObject().isPatternMethod(method);
			boolean isButtonMethod = isButtonMethod(frame, md, method, targetObject, adapter);
			
			return  adapter.getConcreteObject().getTargetObject() != targetObject
					|| (!isPatternMethod && !isButtonMethod)
					|| (isPatternMethod && AttributeManager.getInheritedAttribute(
									frame,
									md,
									AttributeNames.PATTERN_METHODS_IN_RIGHT_MENU,
									adapter).getValue().equals(Boolean.TRUE))
					|| (isButtonMethod && AttributeManager.getInheritedAttribute(
							frame,
							md,
							AttributeNames.BUTTON_METHODS_IN_RIGHT_MENU,
							adapter).getValue().equals(Boolean.TRUE));

//			return adapter.getConcreteObject().getTargetObject() != targetObject
//					|| // in case of menu object this is not the case
//					(!adapter.getConcreteObject().isPatternMethod(method) 
//							|| AttributeManager	.getInheritedAttribute(
//									frame,
//									md,
//									AttributeNames.PATTERN_METHODS_IN_RIGHT_MENU,
//									adapter).getValue().equals(Boolean.TRUE))
//					||	(isButtonMethod(frame, md, method, targetObject, adapter)
//							 
//							&& AttributeManager	.getInheritedAttribute(
//									frame,
//									md,
//									AttributeNames.BUTTON_METHODS_IN_RIGHT_MENU,
//									adapter).getValue().equals(Boolean.TRUE));
			
			// this was commented out earlier
									
//					|| (!adapter.getConcreteObject().isEditingMethod(method) 
//							|| AttributeManager.getInheritedAttribute(
//									frame,
//									md,
//									AttributeNames.EDITING_METHODS_IN_RIGHT_MENU,
//									adapter).getValue().equals(Boolean.TRUE));

		} catch (Exception e) {
			return true; // in case of drawing this path will be taken. Is this
							// really an exception?
		}		
	}
	public static boolean isToolBarMethod(uiFrame frame, MethodDescriptorProxy md,
			MethodProxy method, Object targetObject, ObjectAdapter adapter) {
		try {
//			boolean isPatternMethod = adapter.getConcreteObject().isPatternMethod(method);
//			boolean isButtonMethod = isButtonMethod(frame, md, method, targetObject, adapter);
//			
//			return  adapter.getConcreteObject().getTargetObject() != targetObject
//					|| (!isPatternMethod && !isButtonMethod)
//					|| (isPatternMethod && AttributeManager.getInheritedAttribute(
//									frame,
//									md,
//									AttributeNames.PATTERN_METHODS_IN_TOOL_BAR,
//									adapter).getValue().equals(Boolean.TRUE))
//					|| (isButtonMethod && AttributeManager.getInheritedAttribute(
//							frame,
//							md,
//							AttributeNames.BUTTON_METHODS_IN_TOOL_BAR,
//							adapter).getValue().equals(Boolean.TRUE));

			return adapter.getConcreteObject().getTargetObject() != targetObject
					|| // in case of menu object this is not the case
					(!adapter.getConcreteObject().isPatternMethod(method) 
							|| AttributeManager	.getInheritedAttribute(
									frame,
									md,
									AttributeNames.PATTERN_METHODS_IN_TOOL_BAR,
									adapter).getValue().equals(Boolean.TRUE))
			|| // chec
			 (isButtonMethod(frame, md, method, targetObject, adapter)
					 
					&& AttributeManager	.getInheritedAttribute(
							frame,
							md,
							AttributeNames.BUTTON_METHODS_IN_TOOL_BAR,
							adapter).getValue().equals(Boolean.TRUE));
			
//					|| (!adapter.getConcreteObject().isEditingMethod(method) 
//							|| AttributeManager.getInheritedAttribute(
//									frame,
//									md,
//									AttributeNames.EDITING_METHODS_IN_TOOL_BAR,
//									adapter).getValue().equals(Boolean.TRUE));

		} catch (Exception e) {
			return true; // in case of drawing this path will be taken. Is this
							// really an exception?
		}		
	}
//	public static boolean isMenuMethodOld (uiFrame frame, MethodDescriptorProxy md,  MethodProxy method, Object targetObject, ObjectAdapter adapter ) {
//		try {
//			
//		return adapter.getConcreteObject().getTargetObject() != targetObject || // in case of menu object this is not the case
//		!adapter.getConcreteObject().isPatternMethod(method)   || 			
//		(!adapter.getConcreteObject().isEditingMethod(method) && AttributeManager.getInheritedAttribute(frame, md,
//				AttributeNames.PATTERN_METHODS_IN_RIGHT_MENU, null).getValue().equals(Boolean.FALSE))||
//			AttributeManager.getInheritedAttribute(frame, md,
//					AttributeNames.EDITING_METHODS_IN_RIGHT_MENU, null).getValue().equals(Boolean.FALSE);
//			
//		} catch (Exception e) {
//			return true; // in case of drawing this path will be taken. Is this really an exception?
//		}
//		
//		
//	}
	public static String getToolTipText (uiFrame frame, MethodDescriptorProxy md) {
		if (md == null)
			return "";
		String retVal =  (String) md.getValue(AttributeNames.EXPLANATION);
		if (retVal == null)
			return "";
		return retVal;
			
		/*	
		return (String) ClassDescriptor.getInheritedAttribute(frame, md,
				AttributeNames.TOOL_TIP_TEXT).getValue();
				*/
	}
	public static String getFontName (uiFrame frame, MethodDescriptorProxy md, ObjectAdapter adapter) {
		return (String) AttributeManager.getInheritedAttribute(frame, md,
				AttributeNames.FONT_NAME, adapter).getValue();
	}
	public static Integer getFontStyle (uiFrame frame, MethodDescriptorProxy md, ObjectAdapter adapter) {
		return (Integer) AttributeManager.getInheritedAttribute(frame, md,
				AttributeNames.FONT_STYLE, adapter).getValue();
	}
	public static Integer getFontSize (uiFrame frame, MethodDescriptorProxy md,ObjectAdapter adapter) {
		return (Integer) AttributeManager.getInheritedAttribute(frame, md,
				AttributeNames.FONT_SIZE, adapter).getValue();
	}
	public static Font getFont (uiFrame frame, MethodDescriptorProxy md, ObjectAdapter adapter) {
		return (Font) AttributeManager.getInheritedAttribute(frame, md,
				AttributeNames.FONT, adapter).getValue();
	}
	
	public static void setFont(UniversalWidget component, uiFrame frame, MethodDescriptorProxy md, ObjectAdapter adapter) {
		WidgetAdapter.setFont (component, 
				getFont(frame, md, adapter),
				getFontName(frame, md, adapter),
				getFontStyle(frame, md, adapter),
				getFontSize(frame, md, adapter)
				);
	}
	
	public static boolean isDisplayedCommand (uiFrame frame, MethodDescriptorProxy md, MethodProxy m, Object object, ObjectAdapter adapter) {
		//Class cl = object.getClass();
		Object isVisibleObject = AttributeManager.getInheritedAttribute(frame, md,
				AttributeNames.VISIBLE, adapter).getValue();
		if (isVisibleObject != null) {
			boolean isVisible = (Boolean) isVisibleObject;
			if (!isVisible)
				return false;
		}
		
		if (IntrospectUtility.isPre(m))
			return false;
			
//		if (!isRightMenuMethod(frame, md, m, object, adapter))
//			return false;		
		if (AClassDescriptor.excludeMethod(m))
				return false;
//		if (adapter != null && adapter.getConcreteObject().isPatternMethod(m))
//			return false;
		return true;
		
	}
	
	

	public static void addMethodsToMenus(uiFrame frame, Object object,
			String menu, boolean addToToolbar) {
		
		// Class myClass = object.getClass();
		// System.out.println("adding methods");
		// addPredefinedMenuModels(frame);
		ObjectMenuAdditionStarted.newCase(object, AMethodProcessor.class);
		ObjectAdapter adapter = frame.getAdapter();
		ClassProxy objectClass;
		if (object == null)
			objectClass = StandardProxyTypes.objectClass();
		else
			objectClass = ACompositeLoggable.getTargetClass(object);
		objectClass = ReflectUtil.toMaybeProxyTargetClass(objectClass, object);

		// ViewInfo cdesc =
		// ClassDescriptorCache.getClassDescriptor(object.getClass());
		ClassDescriptorInterface cdesc = ClassDescriptorCache.getClassDescriptor(objectClass,
				object);
		MethodDescriptorProxy[] methods = cdesc.getMethodDescriptors();

		if (methods == null) // if no methods to add to menus then stop
			return;
		// comp.
		// newadds
		ADynamicSparseList<MethodDescriptorProxy> sortedMethods = AClassDescriptor
				.sortMethodDescriptors(methods);
		commandList = new Vector();
		methodHash = new Hashcodetable();
		// for (int i = 0; i < methods.length; i++) { //otherwise for each
		// method
		
		MethodDescriptorProxy dmd = cdesc.getDynamicCommandsMethodDescriptor();
		if (dmd != null) {	
			String toolTipText = getToolTipText(frame, dmd);
			frame.getAnnotationManager().put(dmd, toolTipText, frame.getOriginalAdapter(), objectClass);
		}
		
		for (int i = 0; i < sortedMethods.size(); i++) { // otherwise for
															// each method
			
			MethodDescriptorProxy md = sortedMethods.get(i);
			String toolTipText = getToolTipText(frame, md);
			if (md == null)
				continue;
			// System.out.println("adding: " + methods[i].getName());
			// Method vMethod = methods[i].getMethod(); //get the actual metho
			// VirtualMethod vMethod =
			// VirtualMethodDescriptor.getVirtualMethod(methods[i]); //get the
			// actual metho
			MethodProxy vMethod = VirtualMethodDescriptor
					.getVirtualMethod(md); // get the actual metho
//			if (vMethod.getName().startsWith("move"))
//				System.out.println(vMethod.getName());
			// Object isVisibleObject =
			// methods[i].getValue(AttributeNames.VISIBLE);
			// Object isVisibleObject = ClassDescriptor.getInheritedAttribute
			// (methods[i], AttributeNames.VISIBLE);
			if (!isDisplayedCommand(frame, md, vMethod, object, frame.getOriginalAdapter()))
				continue;
			if (!isMainMenuMethod(frame, md, vMethod, object, frame.getOriginalAdapter()))
				continue;
			
			/*
			Object isVisibleObject = ClassDescriptor.getInheritedAttribute(frame, md,
					AttributeNames.VISIBLE).getValue();
			if (isVisibleObject != null) {
				boolean isVisible = (Boolean) isVisibleObject;
				if (!isVisible)
					continue;
			}
			
			if (uiBean.isPre(vMethod))
				continue;
			if (!isMenuMethod(frame, md, vMethod, object))
				continue;

			//if (excludeMethods.contains(vMethod.getName())) // check if it's one
				// that we don't care

				// for
				if (excludeMethod(vMethod))
					continue;
			
			// System.out.println("pos:" + position);
			*/
			String methodMenu = menu;
			if (methodMenu == null)
				
				// menu = ClassDescriptor.getMethodMenuName(vMethod);
				//if (vMethod.getConstructor() != null)
				if (vMethod.isConstructor())
					methodMenu = AClassDescriptor.getLabel(objectClass)
							+ uiFrame.MENU_NESTING_DELIMITER
							+ AttributeNames.NEW_OBJECT_MENU_NAME;
				else if (md instanceof VirtualMethodDescriptor && adapter != null)
					methodMenu = AMethodProcessor
							.getMethodMenuName(adapter, (VirtualMethodDescriptor) md, objectClass);
				else
					methodMenu = AMethodProcessor.getMethodMenuName(adapter, md, objectClass);

			// System.out.println("menu:" + menu);

			if (methodMenu == null)
				continue;
			if (excludeMethodCategories.contains(methodMenu))
				continue;
			
			if (methodMenu != null) { // if the intended menu parent is
				// already there. then add it if desired
				// to be shown
				// System.out.println("menu:" + menu);
				
				//String toolTipText = getToolTipText(frame, md);
					
				
				//frame.addMethodMenuItem(object, methodMenu, /* p, */md, md
				//		.getDisplayName(), vMethod, frame.getMenuSetter(), toolTipText);
						
				frame.addMethodMenuItem(object, methodMenu, /* p, */md, getLabel(md)
						, vMethod, frame.getMenuSetter(), toolTipText);
				
			}
			// System.out.println("not excluded" );
			if (!addToToolbar)
				continue;
			// Object toolbar = md.getValue(AttributeNames.TOOLBAR);
			AnInheritedAttributeValue toolbarInheritedValue = AttributeManager.getInheritedAttribute(frame, md,
					AttributeNames.TOOLBAR, null);
			
			Boolean toolbar = (Boolean) toolbarInheritedValue.getValue();
			if (toolbar != null && 
					toolbarInheritedValue.getInheritanceKind() != AnInheritedAttributeValue.InheritanceKind.SYSTEM_DEFAULT && 
					
					toolbar)
				frame.getTopViewManager().setNonDefaultToolbarMethodFound(true);
			// should it go into the toolbar

			Object icon = md.getValue(AttributeNames.ICON);
			// System.out.println("tollbar" + toolbar);
			// if (toolbar != null) {

			// the idea we want to run is to see if
			// 1) if is supposed to go on a toolbar
			// 2) if so, then have a getMenu like thing that takes
			// the name of the Toolbar and see's if it is already there and adds
			// it.
			// or if not then creates a new one and adds the methods button
			// there
			// 
			// issues - can you name toolbars
			// can you have multiple toolbars.
			//

			if (ClassDescriptorCache.toBoolean(toolbar)) {
				// if it's supposed to go on A toolbar then

				// comp.
				// newadds add this command to this "object"s command
				// list...prev. topframe.topobject but we aren't creating
				// multiple frames anymore. if problems just use the
				// currentObject variable.
				// commandList.addElement(new Command(topFrame, object,
				// methods[i].getDisplayName(), method));

				// The following comment and line of code address the placement
				// of buttons on the screen
				// change to pass in method descriptor instead of name...since
				// we want placement stuff
				/*
				 * commandList.addElement(new Command(topFrame, object,
				 * methods[i], vMethod));
				 */
				//commandList.addElement(new ButtonCommand(uiGenerator.getTopFrame(),
				commandList.addElement(new ButtonCommand(frame,
						object, md, vMethod, adapter));
				// System.out.println("Inserting " +
				// methods[i].getDisplayName());
				//methodHash.put(md.getDisplayName().toUpperCase().replaceAll(
				methodHash.put(getLabel(md).toUpperCase().replaceAll(		
						"OR", "/").replaceAll("_", "").trim(), new Integer(1)); // i'm
																				// making
																				// this
																				// take
																				// in
																				// the
																				// most
				// clean version e.g. OR --> /
				/*
				 * String place_toolbar = (String) md
				 * .getValue(AttributeNames.PLACE_TOOLBAR);
				 */
				String place_toolbar = (String) (AttributeManager
						.getInheritedAttribute(frame, md, AttributeNames.PLACE_TOOLBAR, null)).getValue();
				// get it's place toolbar name defined as one of its attributes

				/*
				 * performance...i'm commenting this out to see if we can
				 * exclude making the toolbar for now...in this simple fashion
				 */

				if (place_toolbar != null) {

					if (icon == null) {
						VirtualButton toolButton = frame.addToolBarButton(object, (Common.beautify(md
								//.getName(), md.getDisplayName())), // add it.
								.getName(), getLabel(md))), // add it.
								// it's over here were we have to pass it a
								// toolbar name.
								null, vMethod, place_toolbar);
						if (toolButton != null) {
							if (toolTipText != null && toolTipText != "")
							toolButton.setToolTipText(toolTipText);
							setFont(toolButton, frame, md, adapter);
						}
					// System.out.println(methods[i].getName());
					} else
						// frame.addToolBarButton(beautify(methods[i].getName(),
						// methods[i].getDisplayName()),
						frame.addToolBarButton(object, getLabel(md),
						//frame.addToolBarButton(object, md.getDisplayName(),
								(new javax.swing.ImageIcon((String) md
										.getValue(AttributeNames.ICON))),
								vMethod, place_toolbar);
				}// fi place_t..

			}

		}

		// comp.
		// put the commands for this object in the hashablei
		if (objCommands == null)
			resetCommands();
		// if (objCommands != null)
		if (object != null)
		objCommands.put(object, commandList);
		if (methodString != null && object != null)
			methodString.put(object, methodHash); // need to set this up to
													// have
		// the method strings be the
		// most cleaned up version of
		// the method name? i.e. OR ->
		// /?

		// System.out.println("adding constructors");
		ConstructorDescriptorProxy[] cons = cdesc.getConstructorDescriptors();
		for (int i = 0; i < cons.length; i++) {
			MethodProxy c = cons[i].getConstructor();
			// System.out.println("adding: " + cons[i].getName());
			Integer position = (Integer) cons[i]
					.getValue(AttributeNames.POSITION);
			String cMenu = (String) cons[i].getValue(AttributeNames.METHOD_MENU_NAME);
			if (cMenu != null) {
				int p;
				if (position == null)
					p = -1;
				else
					p = position.intValue();
				frame.addConstructorMenuItem(cMenu, p, Common.beautify(
						cons[i].getName(), cons[i].getDisplayName()), c);
						//cons[i].getName(), getLabel(cons[i])), c);
			}
		}
		// System.out.println("adding new");
		// Add zero argument constructors for all visited classes?
		Set classes = ClassDescriptorCache.getClasses();
		for (Object nextElement:classes) {
//		while (classes.hasMoreElements()) {
//			Object nextElement = classes.nextElement();
			if (!(nextElement instanceof ClassProxy)) // this should be class proxy, but ClassDescriptorcache does not support this method
				continue;
			// ViewInfo vinfo = ClassDescriptorCache.getClassDescriptor((Class)
			// classes.nextElement());
			ClassDescriptorInterface vinfo = ClassDescriptorCache
					.getClassDescriptor((ClassProxy) nextElement);
			ConstructorDescriptorProxy[] cs = vinfo.getConstructorDescriptors();
			for (int i = 0; i < cs.length; i++) {
				if ((AttributeNames.NEW_OBJECT_MENU_NAME.equals(cs[i]
						.getValue(AttributeNames.METHOD_MENU_NAME)))
						&& !cs[i].getDisplayName().endsWith("...")) {
					frame.addConstructorMenuItem(AttributeNames.NEW_OBJECT_MENU_NAME,
							0, Common.beautify(cs[i].getName(), cs[i]
									.getDisplayName()),
							// if
							// ("File".equals(cs[i].getValue(AttributeNames.MENU_NAME)))
							// {
							// frame.addConstructorMenuItem("File", 0, beautify
							// (cs[i].getName(), cs[i].getDisplayName()),
							// frame.addConstructorMenuItem((String)
							// cs[i].getValue(AttributeNames.MENU_NAME), 0,
							// beautify (cs[i].getName(),
							// cs[i].getDisplayName()),
							cs[i].getConstructor());
				}
			}
		}
		// frame.getRootMenuGroup().displayMenuTree();
		// System.out.println("Adding methods over");
	}

	

	public static void resetCommands() {
		objCommands = new Hashcodetable();
		resetMethodString();
	}

	public static void oldResetCommands() { // looks like this never needs to be
											// called b/c i'm updating it
											// already within the code that uses
											// it
		if (currentObject != null) { // current object stores the current one
										// that we're working on
			// it is assigned in each terminal generateUIframe (the ones that
			// dont call others)

			// the vector is already put inside the hashtable somewhere below
			commandList = new Vector();
			methodHash = new Hashcodetable();
		}
	}

	public static Vector getCommands(Object theObject) { // returns the
															// objects commands
		return (Vector) objCommands.get(theObject);
	}

	public static Hashcodetable getAllCommands() { // returns the hashtable of all
												// the objects buttons
		return objCommands;
	}

	public static Hashtable getObjMethods(Object theObject) { // returns the
																// hashtable of
																// this objs
																// methods as
																// strings
		return (Hashtable) methodString.get(theObject);
	}

	public static void resetMethodString() {
		methodString = new Hashcodetable();
	}

	public static Hashcodetable getAllMethodStrings() {
		return methodString;
	}

	public static Vector createCommandList(uiFrame frame, Object object, ObjectAdapter adapter, ClassProxy propertyAndCommandFilterClass) {
		ClassProxy objectClass;

		commandList = new Vector();
		if (object == null)
			objectClass = StandardProxyTypes.objectClass();
		else
			objectClass = ACompositeLoggable.getTargetClass(object);

		String virtualClass = IntrospectUtility.getVirtualClass(object);
		if (virtualClass != null) {
		/*
		if (virtualClass !=null && object != null) {
			ViewInfo vcd = ClassDescriptorCache.getClassDescriptor(virtualClass, object);
			
			addToCommandList(commandList, frame, object, vcd, adapter);
		} else
		if (virtualClass != null && object == null) {
		*/
			ClassDescriptorInterface vcd = ClassDescriptorCache
					.getVirtualClassDescriptor(virtualClass, propertyAndCommandFilterClass);
			addToCommandList(commandList, frame, object, vcd, adapter);
		} else {
//			ClassDescriptorInterface cdesc = ClassDescriptorCache
//					.getClassDescriptor(objectClass);
			ClassDescriptorInterface cdesc = ClassDescriptorCache
			.getClassDescriptor(objectClass, object);
			addToCommandList(commandList, frame, object, cdesc, adapter);
		}

		/*
		 * MethodDescriptor[] methods = cdesc.getMethodDescriptors(); for (int
		 * i=0; i< methods.length; i++) { //otherwise for each method
		 * //System.out.println("adding: " + methods[i].getName()); //Method
		 * method = methods[i].getMethod(); //get the actual metho VirtualMethod
		 * method = VirtualMethodDescriptor.getVirtualMethod(methods[i]);
		 * 
		 * if (excludeMethods.contains(method.getName())) //check if it's one
		 * that we don't care for continue; //System.out.println("checking
		 * position"); Integer position = (Integer)
		 * methods[i].getValue(AttributeNames.POSITION); //get it's position
		 * (order in list i guess)
		 * 
		 * 
		 * 
		 * Object toolbar = methods[i].getValue(AttributeNames.TOOLBAR);
		 * //should it go into the toolbar
		 * 
		 * Object icon = methods[i].getValue(AttributeNames.ICON);
		 * //System.out.println("tollbar" + toolbar); //if (toolbar != null) {
		 *  // the idea we want to run is to see if //1) if is supposed to go on
		 * a toolbar //2) if so, then have a getMenu like thing that takes //
		 * the name of the Toolbar and see's if it is already there and adds it. //
		 * or if not then creates a new one and adds the methods button there //
		 * //issues - can you name toolbars // can you have multiple toolbars. //
		 * 
		 * 
		 * if (ClassDescriptorCache.toBoolean(toolbar)) { //if it's supposed to
		 * go on A toolbar then
		 *  // comp. //newadds add this command to this "object"s command
		 * list...prev. topframe.topobject but we aren't creating //multiple
		 * frames anymore. if problems just use the currentObject variable.
		 * //commandList.addElement(new Command(topFrame, object,
		 * methods[i].getDisplayName(), method));
		 * 
		 * //The following comment and line of code address the placement of
		 * buttons on the screen //change to pass in method descriptor instead
		 * of name...since we want placement stuff commandList.addElement(new
		 * Command(frame, object, methods[i], method)); }
		 * //System.out.println("Inserting " + methods[i].getDisplayName()); }
		 */
		return commandList;
	}

	public static Vector addToCommandList(Vector<ButtonCommand> commandList, uiFrame frame,
			Object object, ClassDescriptorInterface cdesc, ObjectAdapter adapter) {
		MethodDescriptorProxy[] methods = cdesc.getMethodDescriptors();
		for (int i = 0; i < methods.length; i++) { // otherwise for each method
			// System.out.println("adding: " + methods[i].getName());
			// Method method = methods[i].getMethod(); //get the actual metho
			
			MethodProxy method = VirtualMethodDescriptor
					.getVirtualMethod(methods[i]);
			String command = method.getName();
			// if (excludeMethods.contains(method.getName())) //check if it's
			// one that we don't care for
			if (!isDisplayedCommand(frame, methods[i], method, object, adapter))
				continue;
			// why not allow command to be in toolbar and inside
			// commenting this out causes issues
			// this stuff needs to be cleaned at some pt
			if (!isToolBarMethod(frame, methods[i], method, object, adapter))
				continue;
			//Boolean showButton = (Boolean) AttributeManager.getInheritedAttribute(methods[i], AttributeNames.SHOW_BUTTON, null).getValue();
			Boolean showButton = (Boolean) AttributeManager.getInheritedAttribute(methods[i], AttributeNames.SHOW_BUTTON, adapter).getValue();
			if (showButton == null) {
				Integer  row = (Integer) AttributeManager.getInheritedAttribute (frame, methods[i], AttributeNames.ROW, adapter).getValue();
				Integer column = (Integer) AttributeManager.getInheritedAttribute (frame, methods[i], AttributeNames.COLUMN, adapter).getValue();
				Boolean showUnBoundButtons = adapter.getShowUnboundButtons();
				showButton = (showUnBoundButtons != null && showUnBoundButtons) || 
				   (
			         (row != null &&  
			              row != -1 ) ||
			         (column != null &&  
			              column != -1 ) 
			         );
			}
			if (!showButton)
				continue;
			/*
			if (excludeMethods.contains(command)) // check if it's one that we
													// don't care for
				continue;
				*/
			// System.out.println("checking position");
			/*
			Integer position = (Integer) methods[i]
					.getValue(AttributeNames.POSITION); // get it's position
														// (order in list i
														// guess)
														 * */
														 

			Object toolbar = methods[i].getValue(AttributeNames.TOOLBAR);
			// should it go into the toolbar

			Object icon = methods[i].getValue(AttributeNames.ICON);
			// System.out.println("tollbar" + toolbar);
			// if (toolbar != null) {

			// the idea we want to run is to see if
			// 1) if is supposed to go on a toolbar
			// 2) if so, then have a getMenu like thing that takes
			// the name of the Toolbar and see's if it is already there and adds
			// it.
			// or if not then creates a new one and adds the methods button
			// there
			// 
			// issues - can you name toolbars
			// can you have multiple toolbars.
			//

			// add all commandes, why just toolbar ones!

			// if (ClassDescriptorCache.toBoolean(toolbar)) {
			// if it's supposed to go on A toolbar then

			// comp.
			// newadds add this command to this "object"s command list...prev.
			// topframe.topobject but we aren't creating
			// multiple frames anymore. if problems just use the currentObject
			// variable.
			// commandList.addElement(new Command(topFrame, object,
			// methods[i].getDisplayName(), method));

			// The following comment and line of code address the placement of
			// buttons on the screen
			// change to pass in method descriptor instead of name...since we
			// want placement stuff
			ButtonCommand buttonCommand = new ButtonCommand(frame, object, methods[i],
					method, adapter);
			buttonCommand.setTargetObjectAdapter(adapter);
			//commandList.addElement(new ButtonCommand(frame, object, methods[i],
					//method));
			commandList.addElement(buttonCommand);
			String toolTipText = getToolTipText(frame, methods[i]);
			buttonCommand.setToolTipText(toolTipText);
			//buttonCommand.getButton().setToolTipText(toolTipText);
			// }
			// System.out.println("Inserting " + methods[i].getDisplayName());
		}
		return commandList;

	}

	public static String getMethodMenuName(VirtualMethodDescriptor md) {	
		  Object menuName = md.getValue(AttributeNames.METHOD_MENU_NAME);
		  if (menuName == null)
			  return AMethodProcessor.getMethodMenuName (md.getVirtualMethod());
		  else
			  return (String) menuName;
		  
	  }
	public static String getMethodMenuName(ObjectAdapter adapter, VirtualMethodDescriptor md, ClassProxy cl) {	
		  Object menuName = md.getValue(AttributeNames.METHOD_MENU_NAME);
		  if (menuName != null)
			  return (String) menuName;
		  			  
		  return AMethodProcessor.getMethodMenuName (md.getVirtualMethod(), adapter, cl);
		  
		  
	  }
	public static String getMethodMenuName(MethodProxy m, ObjectAdapter adapter, ClassProxy cl) {
		String menuName = adapter.getMenuName();
		if (menuName != null) return menuName;		
		if (adapter.getShowInterfaceMenus())
		  return AClassDescriptor.getLabel (AClassDescriptor.getMostSpecificType(m));
		else if (adapter.getShowSuperclassMenus())
		  return AClassDescriptor.getLabel (AClassDescriptor.getMostSpecificClass(m));		
		else
			return AClassDescriptor.getLabel(cl);
			
	}

	public static String getMethodMenuName(MethodProxy m) {
		  return AClassDescriptor.getLabel (AClassDescriptor.getMostSpecificType(m));
		  /*
		  Class methodClass = m.getDeclaringClass();
		  Class[] interfaces = methodClass.getInterfaces();
		  if (m != null) {
		  Class declaringInterface = getDeclaringInterface(m, interfaces);
		  if (declaringInterface != null)
			  methodClass = declaringInterface;
		  }
		  //return toShortName(methodClass.getName());
		  return getLabel (methodClass);
		  */
		  /*
		  ViewInfo cd = ClassDescriptorCache.getClassDescriptor(methodClass);
		  String name = (String) cd.getAttribute(AttributeNames.LABEL);
		  if (name == null) name = toShortName(methodClass.getName());
		  
		  return name;
		  */
		  /*
		  Class methodClass = m.getDeclaringClass();
		  Class[] interfaces = methodClass.getInterfaces();
		  if (m.getMethod() != null) {
		  Class declaringInterface = getDeclaringInterface(m.getMethod(), interfaces);
		  if (declaringInterface != null)
			  methodClass = declaringInterface;
		  }
		  //return toShortName(methodClass.getName());
		  
		  ViewInfo cd = ClassDescriptorCache.getClassDescriptor(methodClass);
		  String name = (String) cd.getAttribute(AttributeNames.LABEL);
		  if (name == null) name = toShortName(methodClass.getName());
		  
		  return name;
		  */
		  
	  }

	public static String getMethodMenuName(MethodDescriptorProxy md) {	
		  Object menuName = md.getValue(AttributeNames.METHOD_MENU_NAME);
		  if (menuName == null)
			  //return AMethodProcessor.getMethodMenuName (md.getMethod());
		  	  return AMethodProcessor.getMethodMenuName (IntrospectUtility.getMethod(md));
		  else
			  return (String) menuName;
		  
	  }
	public static String getMethodMenuName(ObjectAdapter adapter, MethodDescriptorProxy md, ClassProxy cl) {	
		  Object menuName = md.getValue(AttributeNames.METHOD_MENU_NAME);
		  if (menuName == null)
			  //return AMethodProcessor.getMethodMenuName (adapter, md.getMethod(), cl);
		  	  return AMethodProcessor.getMethodMenuName (adapter, IntrospectUtility.getMethod(md), cl);
		  else
			  return (String) menuName;
		  
	  }
	/*
	public static String getMethodMenuName(Method m) {	  
		  Class methodClass = m.getDeclaringClass();
		  Class[] interfaces = methodClass.getInterfaces();
		  if (m != null) {
		  Class declaringInterface = ClassDescriptor.getDeclaringInterface(m, interfaces);
		  if (declaringInterface != null)
			  methodClass = declaringInterface;
		  }
		  //return toShortName(methodClass.getName());
		  
		  ViewInfo cd = ClassDescriptorCache.getClassDescriptor(methodClass);
		  String name = (String) cd.getAttribute(AttributeNames.LABEL);
		  if (name == null) name = ClassDescriptor.toShortName(methodClass.getName());
		  
		  return name;
		  
	  }
	  */
	
	public static String getMethodMenuName(ObjectAdapter adapter, MethodProxy m, ClassProxy cl) {	  
		  ClassProxy methodClass = m.getDeclaringClass();
		  if (adapter != null && adapter.getShowInterfaceMenus()) {
		  ClassProxy[] interfaces = methodClass.getInterfaces();
		  if (m != null) {
		  ClassProxy declaringInterface = AClassDescriptor.getDeclaringInterface(m, interfaces);
		  if (declaringInterface != null)
			  methodClass = declaringInterface;
		  }
		  }
		  //return toShortName(methodClass.getName());
		  if (adapter!= null && adapter.getShowSuperclassMenus()) {
//		  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(methodClass);
//		  String name = (String) cd.getAttribute(AttributeNames.LABEL);
//		  if (name == null) name = AClassDescriptor.toShortName(methodClass.getName());		  
//		  return name;
			 return  AClassDescriptor.getMenuName(cl);
		  }
		  else {
			  if (methodClass.isAssignableFrom(cl))
				  return AClassDescriptor.getLabel(cl);
			  else // proxy class
				  return AClassDescriptor.getLabel(methodClass);
		  }
		  
	  }
}
