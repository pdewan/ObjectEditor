package bus.uigen;

import util.trace.uigen.WidgetInstantiated;
import bus.uigen.ars.*;
import bus.uigen.editors.*;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.StandardProxyTypes;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.widgets.VirtualComponent;
public class Connector {	 static ComponentDictionary componentMapping = EditorRegistry.getComponentDictionary();
	public static VirtualComponent linkAdapterToComponent(ObjectAdapter adaptor,
												   String componentType) 
		throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		// First decide if the component is an editor
		// or a widget. If it is an editor, we dont need to
		// instantiate an adapter. Otherwise we do.
		ClassProxy cclass = null;
		if (componentType != null) {
			//cclass = Class.forName(componentType);
			cclass = uiClassFinder.forName(componentType);
			if (cclass != null)
				componentType = cclass.getName();
		}
		//Class eclass = Class.forName("bus.uigen.uiWidgetAdapterInterface");
		ClassProxy eclass = AClassProxy.classProxy(bus.uigen.WidgetAdapterInterface.class);
		if (cclass != null && eclass.isAssignableFrom(cclass)) {
			// Component is an editor
			
			WidgetAdapterInterface wa = (WidgetAdapterInterface) cclass.newInstance();
			link (adaptor, wa);			/*
			adaptor.setWidgetAdapter(wa);
			wa.addUIComponentValueChangedListener(adaptor);			//System.out.println("Setting view object" + adaptor.getViewObject());
			wa.setViewObject(adaptor.getViewObject());			*/
			return (VirtualComponent) wa;
		}
		else {
			// Component needs an adapter
			// The component might need component-specific instantiation
			// Let the adapter handle all this
			//String widgetAdapterClass = EditorRegistry.getDefaultWidgetAdapter(componentType);
						Object viewObject = adaptor.computeAndMaybeSetViewObject();			String objectClassName;
			ClassProxy objectClass;			if (adaptor.getPropertyClass() == null) {				//objectClassName = "";
				objectClassName = "java.lang.Object";
				//objectClass = null;
				objectClass = StandardProxyTypes.objectClass();
			} else if (viewObject == null) {				objectClassName = adaptor.getPropertyClass().getName();
				objectClass = adaptor.getPropertyClass();
			}			else {				objectClassName = ACompositeLoggable.getTargetClass(viewObject).getName();
				objectClass = ACompositeLoggable.getTargetClass(viewObject);
			}
			//String widgetAdapterClassName = (String) adaptor.getTempAttributeValue(AttributeNames.PREFERRED_WIDGET_ADAPTER);
			String widgetAdapterClassName = (String) adaptor.getPreferredWidgetAdapter();			//System.out.println("object class:" + objectClass);						//String widgetAdapterClass = EditorRegistry.getDefaultWidgetAdapter(objectClass, componentType);
			if (widgetAdapterClassName == null )
			  //widgetAdapterClassName = EditorRegistry.getDefaultWidgetAdapter(objectClassName, componentType);
			widgetAdapterClassName = EditorRegistry.getDefaultWidgetAdapter(objectClass, componentType);
			if (adaptor.getWidgetAdapter() != null 
					&& adaptor.getWidgetAdapter().getClass().getName().equals(widgetAdapterClassName)
					&& adaptor.getUIComponent() != null)
			 {
				return adaptor.getUIComponent();
			}
			if (widgetAdapterClassName.equals("none")) {				System.out.println("no widget adapter class found");
				return (VirtualComponent) cclass.newInstance();			}
			String componentClass = EditorRegistry.getDefaultWidget(objectClassName, widgetAdapterClassName);
			//if (componentClass != null) cclass = Class.forName(componentClass);
			if (componentClass != null) cclass = uiClassFinder.forName(componentClass);			//System.out.println("creating widget adapter for" + widgetAdapterClass );
			WidgetAdapterInterface wa = adaptor.getWidgetAdapter();
			boolean widgetAdapterInstantiated = false;
			VirtualComponent c = adaptor.getUIComponent();
			if (wa == null || wa.getClass() != Class.forName(widgetAdapterClassName) ) {
			//if (wa == null || wa.getClass() != uiClassFinder.forName(widgetAdapterClassName) )
			/*uiWidgetAdapterInterface*/ wa  = (WidgetAdapterInterface) (Class.forName(widgetAdapterClassName).newInstance());
			    widgetAdapterInstantiated = true;
			}
			//else
				//System.out.println("Reusing widget adaptor");			//System.out.println("callding instantiate comp" );
			if (widgetAdapterInstantiated || c == null) {
				 c = wa.instantiateComponent(cclass, adaptor);
			if (c != null) {
				WidgetInstantiated.newCase(adaptor, c, Connector.class);
			}
				//VirtualComponent c = wa.instantiateComponent(cclass, adaptor);
			link (adaptor, wa, c);
			}
			/*			//System.out.println("instantiated comp" );
			wa.setUIComponent(c);			if (!(adaptor.isRootAdapter()))
				wa.addUIComponentValueChangedListener(adaptor);
			//wa.setUIComponent(c);			//System.out.println("set UI Component" );
			adaptor.setWidgetAdapter(wa);			//System.out.println("set widget adapter" );			if (!(adaptor.isRootAdapter())) {
				//wa.addUIComponentValueChangedListener(adaptor);
				wa.setViewObject(adaptor.getViewObject());
			//System.out.println("Connector: seeting typed value" + adaptor.getViewObject() + wa);				wa.setUIComponentValue(adaptor.getViewObject());
			//wa.setUIComponentValue(adaptor.getObject());
			}
			//adaptor.setWidgetAdapter(wa);
			//wa.setUIComponent(c);
			*/			
			return c;
		}
	}
	public static void link (ObjectAdapter adaptor, WidgetAdapterInterface wa) {		if (!(adaptor.isRootAdapter()))
				wa.addUIComponentValueChangedListener(adaptor);
			//wa.setUIComponent(c);			//System.out.println("set UI Component" );
			adaptor.setWidgetAdapter(wa);
			wa.setObjectAdapter(adaptor);			//System.out.println("set widget adapter" );			if (!(adaptor.isRootAdapter())) {
				//wa.addUIComponentValueChangedListener(adaptor);
				wa.setViewObject(adaptor.computeAndMaybeSetViewObject());
			//System.out.println("Connector: seeting typed value" + adaptor.getViewObject() + wa);				//wa.setUIComponentValue(adaptor.getViewObject());
			//wa.setUIComponentValue(adaptor.getObject());
			}
	}
	public static void link (ObjectAdapter adaptor, WidgetAdapterInterface wa, VirtualComponent c) {		//wa.setUIComponent(c);
		link (adaptor, wa);		wa.setUIComponent(c);
		if (!(adaptor.isRootAdapter()))				wa.setUIComponentValue(adaptor.computeAndMaybeSetViewObject());					
	}	public static void linkAdapterToComponent(ObjectAdapter adaptor,
					    VirtualComponent component) {	  //we maynot have a component - I am not sure what this means 
		if (component == null) return; 
	  /*	  System.out.println ("linking");	  System.out.println("adaptor" + adaptor);	  System.out.println("component" + component);	  System.out.println("parent adaptor" + adaptor.getParentAdapter());	  System.out.println("parent component" + component.getParent());	  */	  //System.out.println("component" + component);

    if (adaptor.getUIComponent() != null) return;	 if (component instanceof WidgetAdapterInterface) {		 
      WidgetAdapterInterface wa = (WidgetAdapterInterface) component;	  /*
      adaptor.setWidgetAdapter(wa);
      wa.addUIComponentValueChangedListener(adaptor);
      wa.setViewObject(adaptor.getViewObject());		 */		 link (adaptor, wa);
    }		 
    
    else {
      try {
	//String widgetAdapterClass = componentMapping.getDefaultAdapter(component.getClass().getName());
	
    ClassProxy widgetAdapterClass = null;
    ClassProxy viewClass = null;
    if (adaptor.computeAndMaybeSetViewObject() != null)
    	//viewClass = ACompositeLoggable.getTargetClass(adaptor.computeAndMaybeSetViewObject().getClass());
    	viewClass = ACompositeLoggable.getTargetClass(adaptor.computeAndMaybeSetViewObject());
    if (component != null)
    	//widgetAdapterClass = componentMapping.getDefaultAdapter(component.getPhysicalComponent().getClass());    	widgetAdapterClass = componentMapping.getDefaultWidgetAdapter(viewClass, ACompositeLoggable.getTargetClass(component.getPhysicalComponent()));
    else
    	widgetAdapterClass = componentMapping.getDefaultAdapter((ClassProxy) null);
	WidgetAdapterInterface wa = null;
	if (widgetAdapterClass == null) return;	 //wa = (uiWidgetAdapterInterface) (Class.forName(widgetAdapterClass).newInstance());	
	wa = (WidgetAdapterInterface) (widgetAdapterClass.newInstance());
	link (adaptor, wa, component);
	/*	wa.setUIComponent(component);
	adaptor.setWidgetAdapter(wa);
	wa.addUIComponentValueChangedListener(adaptor);
	wa.setViewObject(adaptor.getViewObject());       	*/
      } catch (Exception e) {
	e.printStackTrace();
      }
    }
  }
}
