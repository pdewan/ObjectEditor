package bus.uigen.adapters;
import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.VirtualComponent;

//import bus.uigen.*;
import java.awt.Component;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;//import bus.uigen.introspect.uiBean;

public class HashtableWidgetAdapter extends WidgetAdapter implements TableModel {

  
  public HashtableWidgetAdapter() {
  }

  public String getType() {
     return "java.util.Hashtable";
  }

  public void setUIComponentTypedValue(Object newval) {
  }

  public Object getUIComponentValue() {
    return "";
  }

  public void setUIComponentEditable() {
  }

  public void setUIComponentUneditable() {
  }

  public void setUIComponentSelected() {
    ((HashtableWidget) getUIComponent()).enableWidget(false);
  }

  public void setUIComponentDeselected() {
    ((HashtableWidget) getUIComponent()).disableWidget(false);
  }

  public void linkUIComponentToMe(Component c) {
    if (c instanceof HashtableWidget) {
     ((HashtableWidget) c).initTableView((TableModel) this);
    }
  }

  // TableModel implementation
  private Hashtable table;
  
  public void setViewObject(Object object) {	  /*
    if (object instanceof Hashtable)
      table = (Hashtable) object;	  */	  table = IntrospectUtility.toHashtable(object);
  }

  public Hashtable getViewObject() {
    return table;
  }


  TableModel model = new DefaultTableModel();
  
  // Implementation of TableModel interface
  public void addTableModelListener(TableModelListener l) {
    model.addTableModelListener(l);
  }
  
  public void removeTableModelListener(TableModelListener l) {
    model.removeTableModelListener(l);
  }

  public Class getColumnClass(int columnIndex) {
    try {
      return Class.forName("java.lang.String");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public int getColumnCount() {
    return 2;
  }

  public String getColumnName(int columnIndex) {
    if (columnIndex == 0)
      return "Key";
    else
      return "Value";
  }

  public int getRowCount() {
    if (getViewObject() == null) {
      System.out.println("View object is null!!");
      return 0;
    }
    return getViewObject().size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Enumeration keys = getViewObject().keys();
    int i = -1;
    Object key = null;
    while (i < rowIndex) {
      key = keys.nextElement();
      i++;
    }
    if (columnIndex == 0)
      return key;
    else
      return getViewObject().get(key);
  }

  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    Enumeration keys = getViewObject().keys();
    int i = -1;
    Object key = null;
    while (i < rowIndex) {
      key = keys.nextElement();
      i++;
    }
    if (columnIndex == 0) {
      Object oldval = table.get("new entry");
      if(table.remove("new entry") != null)
	table.put(value, oldval);
    }
    if (columnIndex == 1 && key != null) {
      getViewObject().put(key, value);
    }
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (! selected)
      return false;
    if (columnIndex == 0) {
      String key = getValueAt(rowIndex, 0).toString();
      if (key.equals("new entry"))
	return true;
      else
	return false;
    }
    else
      return true;
  }

  public void insertEntry() {
   getViewObject().put("new entry", "new value");
  }

  public void deleteEntry(int row) {
    Enumeration keys = table.keys();
    int i = -1;
    Object key = null;
    while (i < row) {
      key = keys.nextElement();
      i++;
    }
    if (key != null)
      table.remove(key);
  }

  private boolean selected = false;
  public void setSelected(boolean value) {
    selected  = value;
    if (selected) 
      uiComponentFocusGained();
    else 
      uiComponentFocusLost();
  }

@Override
public VirtualComponent getUIComponent() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void linkUIComponentToMe(VirtualComponent c) {
	// TODO Auto-generated method stub
	
}

@Override
public VirtualComponent instantiateComponent(ClassProxy cclass,
		ObjectAdapter o) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void linkUIComponentToMe() {
	// TODO Auto-generated method stub
	
}
}
