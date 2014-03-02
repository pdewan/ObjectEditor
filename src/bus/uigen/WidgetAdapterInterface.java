package bus.uigen;
//
// An abstract class from which all widget adaptors
// derive. This class contains all the widget related
// information 
// 

import java.util.*;

import bus.uigen.ars.*;
import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;


public interface WidgetAdapterInterface {
  
  // Property accessor methods for the
  // uiComponent property.
  /*
  public void setUIComponent(Component c);
  public Component getUIComponent();
  public void setParentContainer (Container c);
  public Container getParentContainer();
  */
  public void setUIComponent(VirtualComponent c);
  public VirtualComponent getUIComponent();
  public void setParentContainer (VirtualContainer c);
  public VirtualContainer getParentContainer();
  public Vector<ObjectAdapter> getChildrenAdaptersInDisplayOrder();
  /*
  public void setIndex (int  newVal);
  public int getIndex();
  */  public boolean processDirection(String direction);
  public void processDirection();  //public void setEdited(boolean newVal);  //public boolean getEdited(); 


  // Dummy method 
  public void setViewObject(Object obj);
  //public Component instantiateComponent(Class cclass, uiObjectAdapter o);
  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter o);  public void setPreWrite();

  // Method for objectAdapter to register as
  // a listener for uiValueChanged events
  public void addUIComponentValueChangedListener(ComponentValueChangedListener l);  public void removeUIComponentValueChangedListener(ComponentValueChangedListener l);
  public void cleanUp();
  public ObjectAdapter getObjectAdapter();
  public void setObjectAdapter(ObjectAdapter adapter);



  // Method invoked when thene is a value change in the
  // Object (by the objectAdapter).
  public void objectValueChanged(ValueChangedEvent evt);
  public void uiComponentFocusGained();
  public void uiComponentFocusLost();
  public boolean isComponentAtomic();
  public boolean needChildrenObjectAdapters();


  public boolean processAttribute(Attribute attrib);
  public boolean processDescendentAttribute(ObjectAdapter descendent, Attribute attrib);
  public void processAttributes();

  
  // Abstract methods to be implemented by
  // actual widgetAdapters.
  public  void setUIComponentValue(Object newValue);
  public  Object getUIComponentValue();
 

  public  void setUIComponentEditable();
  public void setUIComponentUneditable();

  public  void setUIComponentSelected();
  public  void setUIComponentDeselected();
  
  public  void setUIComponentSelected(ObjectAdapter[] child);
  public  void setUIComponentDeselected(ObjectAdapter[] child);

  public  void linkUIComponentToMe(VirtualComponent c);
  public  void linkUIComponentToMe();  //public void setUIComponentContainer(Container parent);
  public String componentToText();  //public void emptyComponent();
  /*  public void add (Container parent, Component comp, uiObjectAdapter childAdapter);
  public void add (Container parent, Component comp, int pos);
  public void add (Component comp, int pos);  public void remove (Container parent, Component comp, uiObjectAdapter childAdapter);
  public void remove (Component comp, uiObjectAdapter childAdapter);
  public void remove (Container parent, int index, uiObjectAdapter childAdapter);
  public void remove (int index, uiObjectAdapter childAdapter);
  public void remove (int index);
  public void remove (Container parent, Component component);
  public void remove (Component component);
  public void removeForReplacement (Container parent, int index, uiObjectAdapter childAdapter);
  public void removeForReplacement (int index, uiObjectAdapter childAdapter);
  public void removeAllProperties (Container widget);
  */
  public void add (VirtualContainer parent, VirtualComponent comp, ObjectAdapter childAdapter);
  public void add (VirtualContainer parent, VirtualComponent comp, int pos);
  public void add (VirtualComponent comp, int pos);
  // this is called when objectadapter's parent's UI componnets is not parent of objectAdapter's ui component
  // by uiShapeAdapter
  public void removeFromParentUIContainer();
  public void remove (VirtualContainer parent, VirtualComponent comp, ObjectAdapter childAdapter);
  public void remove (VirtualComponent comp, ObjectAdapter childAdapter);
  public void remove (VirtualContainer parent, int index, ObjectAdapter childAdapter);
  public void remove (int index, ObjectAdapter childAdapter);
  public void remove (int index);
  public  void remove(ObjectAdapter compAdapter);
  public void remove (VirtualContainer parent, VirtualComponent component);
  public void remove (VirtualComponent component);
  public void removeForReplacement (VirtualContainer parent, int index, ObjectAdapter childAdapter);
  public void removeForReplacement (int index, ObjectAdapter childAdapter);
  public void removeAllProperties (VirtualContainer widget);
  
  public void removeAll () ;
  public void removeLast();
  public String getType();
  public void setIncrementalChildAddition(boolean newVal);
  public boolean getIncrementalChildAddition();
  public void childComponentsAdditionStarted();
  public void childComponentsAdded(boolean hasProperties);
  public void descendentUIComponentsAdded();
  public boolean uiIsContainer();
  public void invalidate();
  public boolean isEmpty();
  public boolean delegateSelectionToWidgetShell();
  public boolean delegateOpenToWidgetShell();
  public void userInputUpdated(boolean newVal) ;
  //public void setLayout (LayoutManager l);
void processDeferredFillColumnTitlePanel(CompositeAdapter adapter);
void refillColumnTitle(CompositeAdapter firstRowAdapter);
void add(ObjectAdapter compAdapter);
void rebuildPanel();
public int defaultWidth();
public int defaultHeight();
public boolean hasCommands();
public void setAttributes();


//Vector<uiObjectAdapter> getDisplayChildrenAdapters();

}










