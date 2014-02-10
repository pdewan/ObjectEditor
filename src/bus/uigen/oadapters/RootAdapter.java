
package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;
import java.awt.Container;

import bus.uigen.editors.*;import javax.swing.JOptionPane;

import util.models.Hashcodetable;
import util.models.RemotePropertyChangeListener;
import bus.uigen.*;import bus.uigen.introspect.*;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.ars.*;
import bus.uigen.attributes.*;import bus.uigen.view.OEGridLayout;
import bus.uigen.view.AGenericWidgetShell;import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.sadapters.ConcreteType;import bus.uigen.sadapters.RecordStructure;import bus.uigen.uiFrame;import java.util.Enumeration;

public class RootAdapter extends ClassAdapter implements RemotePropertyChangeListener {
	static int uniqueID;
	int myID;
	Container parentContainer;
	/*	public void setParentContainer(Container newVal) {
		parentContainer = newVal;	}	*/
	public RootAdapter () throws RemoteException {
		myID = uniqueID;
		uniqueID++;
		
	}
	public int getUniqueID() {
		return uniqueID;
	}
	public void addUIComponentToParent(VirtualComponent comp) {				childrenCreated = true;		/*
		if (parentContainer != null)			parentContainer.add(comp);		else		*/
		   getUIFrame().setNewChildPanel((VirtualContainer) comp);		
	}
	public boolean processPreferredWidget() {
		return processPreferredWidget(EditorRegistry.getComponentDictionary().getWidgetClassNameForAdapterClass(this));
	}
	public boolean isAtomic() { return false;}
	//public boolean childrenCreated() {return true;}
	public  boolean addChildUIComponents() {
		Enumeration children = getChildren();
		getWidgetAdapter().childComponentsAdditionStarted();
		while (children.hasMoreElements()) {			ObjectAdapter a = (ObjectAdapter) children.nextElement();			a.processPreferredWidget();
		}
		//this.getWidgetAdapter().childComponentsAdded();
		return false;		
	}
	@Override
	public void setChildAdapterMapping(ObjectAdapter adaptor) {
		super.setChildAdapterMapping(adaptor);
		childrenCreated = true;
	}
	@Override
	public void setChildAdapterMapping(String fieldName, ObjectAdapter adaptor) {
		//System.out.println("putting in mapping" + fieldName + "adapter" + adaptor);
		super.setChildAdapterMapping(fieldName, adaptor);
		childrenCreated = true;
		//Object retVal = mapping.put(fieldName.toLowerCase(), adaptor);
		//System.out.println("put in mapping: retVal" + retVal);
		//System.out.println("size" + mapping.size());
		
		//if (mapping.put(fieldName, adaptor) != null);
		//System.out.println("PROPERTY AND FIELD WITH SAME NAME!!!");
	}
	public  ObjectAdapter  topAddChildComponents(uiFrame theTopFrame,
					Object obj,
					myLockManager lman, ObjectAdapter sourceAdapter,
					VirtualContainer childPanel, 
					String title) {
    
		
    RootAdapter rootAdapter = this;    
	rootAdapter.setUIFrame(theTopFrame);	//System.out.println("root adapter:" + rootAdapter);
    //linkAdapterToComponent(topAdapter, topFrame.getChildPanel());	
	//see if we can get away without this	//Connector.linkAdapterToComponent(rootAdapter, childPanel);	String name = "root";	boolean isProperty = false;		Object parentObject = null;	if (sourceAdapter != null) {
		ObjectAdapter sourceParentAdapter = sourceAdapter.getParentAdapter();
		//if (sourceAdapter instanceof uiPrimitiveAdapter && sourceParentAdapter != null) {		if (sourceParentAdapter != null && sourceParentAdapter.getRealObject() != null) {
			//if (sourceParentAdapter != null) {
			parentObject = sourceParentAdapter.getRealObject();
			rootAdapter.setRealObject(parentObject);
			//rootAdapter.setViewObject(sourceParentAdapter.getViewObject());
			rootAdapter.computeAndSetViewObject();			rootAdapter.setConcreteObject(sourceParentAdapter.getConcreteObject());			rootAdapter.setSourceAdapter(sourceParentAdapter);
						    isProperty = sourceAdapter.getAdapterType() == ObjectAdapter.PROPERTY_TYPE ;		}
			
		if ((name = sourceAdapter.getPropertyName()) == null)
			name = "root";
	}
	ClassProxy objClass;
	if (obj == null)
		objClass =StandardProxyTypes.objectClass();
	else
		objClass = RemoteSelector.getClass(obj);
//	if (IntrospectUtility.isProxyClass(objClass)) {
//		ClassProxy targetClass = IntrospectUtility.getProxyTargetClass(obj);
//		if (targetClass != null)
//			objClass = targetClass;
//	}
	//System.out.print("BT<");	ObjectAdapter topAdapter = uiGenerator.createObjectAdapter(
				
				//childPanel,				//topFrame.getChildPanel(), // parent widget 
				 rootAdapter,               // parent adaptor
				 obj,                      // object
				 /*obj.getClass(),           // object's class*/				 objClass,           // object's class*/
				 (-1),                     // position 
				 name,                   // name of object
				 parentObject,                     // parent object
				 isProperty);                   // property?
	 /*
	if (topAdapter instanceof uiContainerAdapter)
	   	System.out.println("topAdapter direction " + ((uiContainerAdapter) topAdapter).getDirection());
   */
   rootAdapter.setChildAdapterMapping(name, topAdapter);
   			rootAdapter.setChildAdapterMapping(topAdapter);						//rootAdapter.childrenCreated = true;
   if (title != null) {    topAdapter.setLocalAttribute(new Attribute(AttributeNames.TITLE, title));   }
   if (theTopFrame != null && topAdapter != null)
   topAdapter.initAttributes(theTopFrame.getSelfAttributes(), theTopFrame.getChildrenAttributes());
   	/*
	uiWidgetAdapterInterface wa = topAdapter.getWidgetAdapter();	if (wa != null && topAdapter instanceof uiClassAdapter &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      topAdapter.getWidgetAdapter().setUIComponent(topFrame.getChildPanel());
    }
	*/
   //System.out.print("DC<");	/*
	if (!textMode && uiBean.isShapeModel(topAdapter.getViewObject().getClass()))		topAdapter.getUIFrame().emptyMainPanel();	else	
   */
   /*
   if (topAdapter instanceof uiContainerAdapter)
   	System.out.println("topAdapter direction " + ((uiContainerAdapter) topAdapter).getDirection());
   	*/	    //uiGenerator.deepCreateChildren(topAdapter);
   if (childPanel != null && theTopFrame.getDrawPanel() == childPanel)
	   return topAdapter;
	    uiGenerator.deepCreateChildren(rootAdapter, true);	//System.out.println(">DC");		//System.out.print("UI<");
		/*		if ( childPanel == null ) {
			rootAdapter.setPreferredWidget();
			rootAdapter.processPreferredWidget();
		}
		*/
		//rootAdapter.setParentContainer(childPanel);		//uiGenerator.deepProcessAttributes(topAdapter, rootAdapter);
	if (sourceAdapter != null) {		//System.out.println("obj in uigenerator" + obj);		topAdapter.setSourceAdapter(sourceAdapter);
		topAdapter.setRealObject(sourceAdapter.getRealObject());		//System.out.println("real obj in uigenerator" + sourceAdapter.getRealObject());
		//topAdapter.setViewObject(sourceAdapter.getViewObject());		topAdapter.computeAndSetViewObject();
		topAdapter.setEditable(sourceAdapter.isEditable);
		//topAdapter.setAdapterField(sourceAdapter.getAdapterField());		
		//System.out.println("view obj in uigenerator" + sourceAdapter.getViewObject());		//System.out.println("real obj in uigenerator" + topAdapter.getRealObject());		//topAdapter.setValue(sourceAdapter.getValue());
		//}		//if (topAdapter instanceof uiPrimitiveAdapter) {		
			if (topAdapter.getAdapterType() == ObjectAdapter.PROPERTY_TYPE) {
				//topAdapter.setPropertyReadMethod(sourceAdapter.getPropertyReadMethod());				//topAdapter.setPropertyWriteMethod(sourceAdapter.getPropertyWriteMethod());
				//topAdapter.setPreReadMethod(sourceAdapter.getPreReadMethod());
				//topAdapter.setPreWriteMethod(sourceAdapter.getPreWriteMethod());				//System.out.println("real obj in uigenerator" + topAdapter.getRealObject());				//if (topAdapter instanceof uiPrimitiveAdapter)
				/*
				    if (topAdapter instanceof uiContainerAdapter)
				    	topAdapter.setViewObject(sourceAdapter.getViewObject());
				    else {
				    */
				    if (!(topAdapter instanceof CompositeAdapter)) {
				    	topAdapter.refreshValue(sourceAdapter.getValue());
				    	topAdapter.refreshConcreteObject(sourceAdapter.computeAndMaybeSetViewObject());
				    }				//System.out.println("real obj in uigenerator" + topAdapter.getRealObject());
			}    else {
				//parentObject = sourceAdapter.getParentAdapter().getViewObject();
				//if (parentObject instanceof Vector) {
				if (sourceAdapter.getParentAdapter() instanceof VectorAdapter){					topAdapter.setAdapterIndex(((VectorAdapter )sourceAdapter.getParentAdapter()).getChildAdapterRealIndex(sourceAdapter));
					// In these cases, we'll have to perform a 
					// vector.setElementAt().
					
				} else if 	(sourceAdapter.getParentAdapter() instanceof HashtableAdapter){					topAdapter.setAdapterIndex(((HashtableAdapter )sourceAdapter.getParentAdapter()).getChildAdapterRealIndex(sourceAdapter));					topAdapter.setAdapterType(sourceAdapter.getAdapterType());					topAdapter.setKey(sourceAdapter.getKey());
					// In these cases, we'll have to perform a 
					// vector.setElementAt().
					
				}			}	
		//}
	}	
	//topFrame.addCurrentAdapter(topAdapter);	
	//(new ProcessAttributesAdapterVisitor (topAdapter)).traverse();
    if (topAdapter instanceof ClassAdapter && 		topAdapter.getWidgetAdapter() != null &&
	topAdapter.getWidgetAdapter().getUIComponent() == null) {
      topAdapter.getWidgetAdapter().setUIComponent(childPanel);
    } 	//System.out.print(">UI");
	
    if (topAdapter != null && topAdapter.getPropertyClass() == null)
      topAdapter.setPropertyClass(RemoteSelector.getClass(obj));
    // Add the methods.	/*
    uiAddMethods(topFrame, obj);
    uiAddConstants(topFrame, obj);
    addHelperObjects(topFrame, obj);
	*/	
	/*	if (!uiFrame.isSavable(topAdapter))
    //if (!(obj instanceof java.io.Serializable))
      topFrame.setSaveMenuItemEnabled(false);
    // Set the frame title
    String title = (String)topAdapter.getAttributeValue(AttributeNames.TITLE);
    if (title == null) {
      // Check the class attributes
      try {
	title = (String) AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(), AttributeNames.TITLE).getValue();
      } catch (Exception e) {
	title = ClassDescriptor.toShortName(obj.getClass().getName());
      }
    }
    topFrame.setTitle(title);
	*/	//topFrame.addUIFrameToolBarButton("Elide", null);		//System.out.println("setting label visible");
	//uiGenerator.deepProcessAttributes(topAdapter, rootAdapter);	if (sourceAdapter != null) {
	   //topAdapter.getGenericWidget().setLabelVisible(true);		//topAdapter.setTempAttributeValue
		topAdapter.setLabel(sourceAdapter.getBeautifiedPath());	   //topAdapter.getGenericWidget().setLabel(sourceAdapter.getBeautifiedPath());
	   topAdapter.setEdited(sourceAdapter.isEdited());	   topAdapter.setWidgetShellEdited();
	   /*		if (topAdapter.isEdited()) {			uiGenericWidget genWidget = topAdapter.getGenericWidget();			genWidget.setEdited();					}
	   */	}	//System.out.print(">FG");
    //System.out.println("Done frame generation");
	//System.out.println("Beginning deep elide");	
	//System.out.print("DE<");
	/*	
	if (!textMode && uiBean.isShapeModel(topAdapter.getViewObject().getClass()))		topAdapter.getUIFrame().emptyMainPanel();	else		    topFrame.deepElide(topAdapter);	System.out.println(">DE");
		//topFrame.maybeHideMainPanel();
	//System.out.println("Ending deep elide");	return topAdapter;
    //return topFrame;
  }
	*/	return topAdapter;
	}
	public ObjectAdapter topAddProperties(uiFrame topFrame, Object obj,
			myLockManager lman, ObjectAdapter sourceAdapter,
			VirtualContainer childPanel,
			String title, Hashtable sharedPs) {

		RootAdapter rootAdapter = this;
		rootAdapter.setUIFrame(topFrame);
		//System.out.println("root adapter:" + rootAdapter);
		//linkAdapterToComponent(topAdapter, topFrame.getChildPanel());

		//see if we can get away without this
		//Connector.linkAdapterToComponent(rootAdapter, childPanel);
		String name = "root";
		boolean isProperty = false;
		Object parentObject = null;
		if (sourceAdapter != null) {
			ObjectAdapter sourceParentAdapter = sourceAdapter
					.getParentAdapter();
			//if (sourceAdapter instanceof uiPrimitiveAdapter &&
			// sourceParentAdapter != null) {
			if (sourceParentAdapter != null) {
				parentObject = sourceParentAdapter.getRealObject();
				rootAdapter.setRealObject(parentObject);
				//rootAdapter.setViewObject(sourceParentAdapter.getViewObject());
				rootAdapter.computeAndSetViewObject();
				rootAdapter.setConcreteObject(sourceParentAdapter
						.getConcreteObject());
				rootAdapter.setSourceAdapter(sourceParentAdapter);

				isProperty = sourceAdapter.getAdapterType() == ObjectAdapter.PROPERTY_TYPE;
			}

			if ((name = sourceAdapter.getPropertyName()) == null)
				name = "root";
		}
		ClassProxy objClass;
		if (obj == null)
			objClass = StandardProxyTypes.objectClass();
		else
			objClass = RemoteSelector.getClass(obj);
		//System.out.print("BT<");
		
		ObjectAdapter topAdapter = uiGenerator.createObjectAdapter(
				//childPanel,
		//topFrame.getChildPanel(), // parent widget
				rootAdapter, // parent adaptor
				obj, // object
				/* obj.getClass(), // object's class */
				objClass, // object's class*/
				(-1), // position
				name, // name of object
				parentObject, // parent object
				isProperty); // property?
		/*
		 * if (topAdapter instanceof uiContainerAdapter)
		 * System.out.println("topAdapter direction " + ((uiContainerAdapter)
		 * topAdapter).getDirection());
		 */

		rootAdapter.setChildAdapterMapping(name, topAdapter);

		rootAdapter.setChildAdapterMapping(topAdapter);
		//rootAdapter.childrenCreated = true;
		if (title != null) {
			topAdapter.setLocalAttribute(new Attribute(AttributeNames.TITLE,
					title));
		}

		/*
		 * uiWidgetAdapterInterface wa = topAdapter.getWidgetAdapter(); if (wa !=
		 * null && topAdapter instanceof uiClassAdapter &&
		 * topAdapter.getWidgetAdapter().getUIComponent() == null) {
		 * topAdapter.getWidgetAdapter().setUIComponent(topFrame.getChildPanel()); }
		 */
		//System.out.print("DC<");
		/*
		 * if (!textMode &&
		 * uiBean.isShapeModel(topAdapter.getViewObject().getClass()))
		 * topAdapter.getUIFrame().emptyMainPanel(); else
		 */
		/*
		 * if (topAdapter instanceof uiContainerAdapter)
		 * System.out.println("topAdapter direction " + ((uiContainerAdapter)
		 * topAdapter).getDirection());
		 */
		uiGenerator.deepCreateChildren(topAdapter, sharedPs);
		//System.out.println(">DC");
		//System.out.print("UI<");
		/*
		 * if ( childPanel == null ) { rootAdapter.setPreferredWidget();
		 * rootAdapter.processPreferredWidget(); }
		 */
		//rootAdapter.setParentContainer(childPanel);
		//uiGenerator.deepProcessAttributes(topAdapter, rootAdapter);
		if (sourceAdapter != null) {
			//System.out.println("obj in uigenerator" + obj);
			topAdapter.setSourceAdapter(sourceAdapter);
			topAdapter.setRealObject(sourceAdapter.getRealObject());
			//System.out.println("real obj in uigenerator" +
			// sourceAdapter.getRealObject());
			//topAdapter.setViewObject(sourceAdapter.getViewObject());
			topAdapter.computeAndSetViewObject();
			//topAdapter.setAdapterField(sourceAdapter.getAdapterField());

			//System.out.println("view obj in uigenerator" +
			// sourceAdapter.getViewObject());
			//System.out.println("real obj in uigenerator" +
			// topAdapter.getRealObject());
			//topAdapter.setValue(sourceAdapter.getValue());
			//}
			//if (topAdapter instanceof uiPrimitiveAdapter) {

			if (topAdapter.getAdapterType() == ObjectAdapter.PROPERTY_TYPE) {
				//topAdapter.setPropertyReadMethod(sourceAdapter.getPropertyReadMethod());
				//topAdapter.setPropertyWriteMethod(sourceAdapter.getPropertyWriteMethod());
				//topAdapter.setPreReadMethod(sourceAdapter.getPreReadMethod());
				//topAdapter.setPreWriteMethod(sourceAdapter.getPreWriteMethod());
				//System.out.println("real obj in uigenerator" +
				// topAdapter.getRealObject());
				//if (topAdapter instanceof uiPrimitiveAdapter)
				/*
				 * if (topAdapter instanceof uiContainerAdapter)
				 * topAdapter.setViewObject(sourceAdapter.getViewObject()); else {
				 */
				if (!(topAdapter instanceof CompositeAdapter)) {
					topAdapter.refreshValue(sourceAdapter.getValue());
					topAdapter.refreshConcreteObject(sourceAdapter
							.computeAndMaybeSetViewObject());
				}
				//System.out.println("real obj in uigenerator" +
				// topAdapter.getRealObject());
			} else {
				//parentObject =
				// sourceAdapter.getParentAdapter().getViewObject();
				//if (parentObject instanceof Vector) {
				if (sourceAdapter.getParentAdapter() instanceof VectorAdapter) {
					topAdapter.setAdapterIndex(((VectorAdapter) sourceAdapter
							.getParentAdapter())
							.getChildAdapterRealIndex(sourceAdapter));
					// In these cases, we'll have to perform a
					// vector.setElementAt().

				} else if (sourceAdapter.getParentAdapter() instanceof HashtableAdapter) {
					topAdapter
							.setAdapterIndex(((HashtableAdapter) sourceAdapter
									.getParentAdapter())
									.getChildAdapterRealIndex(sourceAdapter));
					topAdapter.setAdapterType(sourceAdapter.getAdapterType());
					topAdapter.setKey(sourceAdapter.getKey());
					// In these cases, we'll have to perform a
					// vector.setElementAt().

				}
			}
			//}
		}

		//topFrame.addCurrentAdapter(topAdapter);

		//(new ProcessAttributesAdapterVisitor (topAdapter)).traverse();
		if (topAdapter instanceof ClassAdapter
				&& topAdapter.getWidgetAdapter() != null
				&& topAdapter.getWidgetAdapter().getUIComponent() == null) {
			topAdapter.getWidgetAdapter().setUIComponent(childPanel);
		}
		//System.out.print(">UI");

		if (topAdapter.getPropertyClass() == null)
			topAdapter.setPropertyClass(RemoteSelector.getClass(obj));
		// Add the methods.
		/*
		 * uiAddMethods(topFrame, obj); uiAddConstants(topFrame, obj);
		 * addHelperObjects(topFrame, obj);
		 */

		/*
		 * if (!uiFrame.isSavable(topAdapter)) //if (!(obj instanceof
		 * java.io.Serializable)) topFrame.setSaveMenuItemEnabled(false); // Set
		 * the frame title String title =
		 * (String)topAdapter.getAttributeValue(AttributeNames.TITLE); if (title ==
		 * null) { // Check the class attributes try { title = (String)
		 * AttributeManager.getEnvironment().getAttribute(obj.getClass().getName(),
		 * AttributeNames.TITLE).getValue(); } catch (Exception e) { title =
		 * ClassDescriptor.toShortName(obj.getClass().getName()); } }
		 * topFrame.setTitle(title);
		 */
		//topFrame.addUIFrameToolBarButton("Elide", null);
		//System.out.println("setting label visible");
		//uiGenerator.deepProcessAttributes(topAdapter, rootAdapter);
		if (sourceAdapter != null) {
			//topAdapter.getGenericWidget().setLabelVisible(true);
			//topAdapter.setTempAttributeValue
			topAdapter.setLabel(sourceAdapter.getBeautifiedPath());
			//topAdapter.getGenericWidget().setLabel(sourceAdapter.getBeautifiedPath());
			topAdapter.setEdited(sourceAdapter.isEdited());
			topAdapter.setWidgetShellEdited();
			/*
			 * if (topAdapter.isEdited()) { uiGenericWidget genWidget =
			 * topAdapter.getGenericWidget(); genWidget.setEdited(); }
			 */
		}
		//System.out.print(">FG");
		//System.out.println("Done frame generation");
		//System.out.println("Beginning deep elide");

		//System.out.print("DE<");
		/*
		 * 
		 * if (!textMode &&
		 * uiBean.isShapeModel(topAdapter.getViewObject().getClass()))
		 * topAdapter.getUIFrame().emptyMainPanel(); else
		 * topFrame.deepElide(topAdapter); System.out.println(">DE");
		 * 
		 * //topFrame.maybeHideMainPanel(); //System.out.println("Ending deep
		 * elide"); return topAdapter; //return topFrame; }
		 */
		return topAdapter;
	}
	BasicObjectRegistry basicObjectRegistry = new ABasicObjectRegistry();
	public BasicObjectRegistry getBasicObjectRegistery() {
		return basicObjectRegistry;
	}
	 Hashcodetable<Object, Set<String>> visitedObjects = new Hashcodetable();
	 public Hashcodetable<Object, Set<String>> getVisitedObjects() {
		 return visitedObjects;
	 }
}









