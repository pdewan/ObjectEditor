package bus.uigen.editors;

import bus.uigen.*;

import javax.swing.*;
import java.awt.Component;import java.awt.Container;
import java.awt.Color;
import java.awt.event.*;
import java.util.Vector;

import bus.uigen.introspect.Attribute;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.swing.SwingButton;

public class uiColorEditor 
		extends JButton 
		implements WidgetAdapterInterface, ActionListener {
  
  private JDialog dialog = null;
  public JColorChooser chooser = null;
  private ComponentValueChangedListener objectAdapter = null;
  Color lastValue = Color.white;

  
  public uiColorEditor() {
    addActionListener(this);
    chooser = new JColorChooser();
    dialog = JColorChooser.createDialog(null,
					  "uiColorEditor",
					  false,
					  chooser,
					  this,
					  null);
  }  public void setPreWrite() {};

  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(this)) {
      System.out.println("Showing the dialog");
      dialog.show();
    }
    else {
      System.out.println("Got a color selection"+chooser.getColor());
      setUIComponentValue(chooser.getColor());
      // let the listener know that something's changed
      objectAdapter.uiComponentValueChanged(this);
    }
  }

  public boolean processAttribute(Attribute a) {return true;}

  // Implementing the widgetadaptor interface
  public void setUIComponent(Component c) {}
  public VirtualComponent getUIComponent() {return SwingButton.virtualButton(this);}
  public VirtualComponent instantiateComponent(ClassProxy c, ObjectAdapter adapter) {return SwingButton.virtualButton(this);}

  public void setViewObject(Object obj) {}

  
  public void addUIComponentValueChangedListener(ComponentValueChangedListener l) {
    objectAdapter = l;
  }
  public ObjectAdapter getObjectAdapter() {
    return (ObjectAdapter) objectAdapter;
  }
  
  public void objectValueChanged(ValueChangedEvent evt) {
    setUIComponentValue(evt.getNewValue());
  }

  public void uiComponentFocusGained() {}
  public void uiComponentFocusLost() {}

  // This should never get invoked
  public void setUIComponentValue(Object newValue) {
    if (newValue instanceof Color) {
      lastValue = (Color) newValue;
      setBackground(lastValue);
    }
  }
  
  public Object getUIComponentValue() {return lastValue;}

  public void setUIComponentEditable() {}
  public void setUIComponentUneditable() {}

  public void setUIComponentSelected() {}
  public void setUIComponentDeselected() {}

  public void linkUIComponentToMe(Component c) {}  public boolean processDirection(String direction) {return true;};  public void removeUIComponentValueChangedListener(ComponentValueChangedListener l) {  }  public void setUIComponentContainer(Container parent) {};  public String componentToText() {
	  return this.getObjectAdapter().toTextLine();  }  public void add (Container parent, Component comp, ObjectAdapter childAdapter) {	  parent.add(comp);
  }
 
  public boolean processDescendentAttribute(ObjectAdapter descendent, Attribute attrib) {
		
		return true;
		
		
		
		
		
}
  
  public void add (Container parent, Component comp, int pos) {
	  
  }
  
  public void remove (Container parent, Component comp, ObjectAdapter childAdapter) {
	  
  }
  public void remove (Component comp, ObjectAdapter childAdapter) {
	  
  }
  public void remove (Container parent, int index, ObjectAdapter childAdapter) {
	  
  }
  
  
  
  public void remove (int index, ObjectAdapter childAdapter) {
	  
  }
  public void remove (int index) {
	  
  }
  public void remove (Container parent, Component component) {
	  
  }
  public void remove (Component component) {
	  
  }
  public void removeAllProperties (Container widget) {
	  
  }
  public void removeAll () {
	  
  }
  public void removeLast() {
	  
  }
  public boolean uiIsContainer() {
	  return false;
  } 
  public void invalidate() {
	  
  }
@Override
public void add(VirtualContainer parent, VirtualComponent comp,
		ObjectAdapter childAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void add(VirtualContainer parent, VirtualComponent comp, int pos) {
	// TODO Auto-generated method stub
	
}
@Override
public void add(VirtualComponent comp, int pos) {
	// TODO Auto-generated method stub
	
}
@Override
public void childComponentsAdded(boolean hasProperties) {
	// TODO Auto-generated method stub
	
}
@Override
public void childComponentsAdditionStarted() {
	// TODO Auto-generated method stub
	
}
@Override
public void cleanUp() {
	// TODO Auto-generated method stub
	
}
@Override
public boolean delegateOpenToWidgetShell() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public boolean delegateSelectionToWidgetShell() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public Vector<ObjectAdapter> getChildrenAdaptersInDisplayOrder() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public VirtualContainer getParentContainer() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String getType() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public boolean isComponentAtomic() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public boolean isEmpty() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public void linkUIComponentToMe(VirtualComponent c) {
	// TODO Auto-generated method stub
	
}
@Override
public void linkUIComponentToMe() {
	// TODO Auto-generated method stub
	
}
@Override
public void processAttributes() {
	// TODO Auto-generated method stub
	
}
@Override
public void processDirection() {
	// TODO Auto-generated method stub
	
}
@Override
public void remove(VirtualContainer parent, VirtualComponent comp,
		ObjectAdapter childAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void remove(VirtualComponent comp, ObjectAdapter childAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void remove(VirtualContainer parent, int index,
		ObjectAdapter childAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void remove(ObjectAdapter compAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void remove(VirtualContainer parent, VirtualComponent component) {
	// TODO Auto-generated method stub
	
}
@Override
public void remove(VirtualComponent component) {
	// TODO Auto-generated method stub
	
}
@Override
public void removeAllProperties(VirtualContainer widget) {
	// TODO Auto-generated method stub
	
}
@Override
public void removeForReplacement(VirtualContainer parent, int index,
		ObjectAdapter childAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void removeForReplacement(int index, ObjectAdapter childAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void removeFromParentUIContainer() {
	// TODO Auto-generated method stub
	
}
@Override
public void setObjectAdapter(ObjectAdapter adapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void setParentContainer(VirtualContainer c) {
	// TODO Auto-generated method stub
	
}
@Override
public void setUIComponent(VirtualComponent c) {
	// TODO Auto-generated method stub
	
}
@Override
public void setUIComponentDeselected(ObjectAdapter[] child) {
	// TODO Auto-generated method stub
	
}
@Override
public void setUIComponentSelected(ObjectAdapter[] child) {
	// TODO Auto-generated method stub
	
}
@Override
public void userInputUpdated(boolean newVal) {
	// TODO Auto-generated method stub
	
}
@Override
public boolean getIncrementalChildAddition() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public void setIncrementalChildAddition(boolean newVal) {
	// TODO Auto-generated method stub
	
}
@Override
public void processDeferredFillColumnTitlePanel(CompositeAdapter adapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void descendentUIComponentsAdded() {
	// TODO Auto-generated method stub
	
}
@Override
public void refillColumnTitle(CompositeAdapter firstRowAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void add(ObjectAdapter compAdapter) {
	// TODO Auto-generated method stub
	
}
@Override
public void rebuildPanel() {
	// TODO Auto-generated method stub
	
}
@Override
public int defaultHeight() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public int defaultWidth() {
	// TODO Auto-generated method stub
	return 0;
}
public boolean needChildrenObjectAdapters() {
	return true;
}
@Override
public boolean hasCommands() {
	// TODO Auto-generated method stub
	return false;
}


}

