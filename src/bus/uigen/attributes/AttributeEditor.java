
package bus.uigen.attributes;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import bus.uigen.Instantiator;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.controller.ConstantMenuItem;
import bus.uigen.controller.DoneMenuItem;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;

public class AttributeEditor extends Frame implements ActionListener, VirtualActionListener{
  
  private AttributeCollection attributeSource;
  private Vector attributeList;

  private JTable tableView;
  private TableModel tableModel;


  public AttributeEditor(AttributeCollection source) {
    super();
    attributeSource = source;
    attributeList = CopyAttributeVector.copyVector(source.getAttributes());
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	dispose();
      }
    });
    
    MenuBar mbar = new MenuBar();
    Menu menu;
    MenuItem item;
    
    setMenuBar(mbar);
    
    menu = new Menu("File");
    item = new MenuItem("Reload");
    item.addActionListener(this);
    menu.add(item);
    item = new MenuItem("Update");
    item.addActionListener(this);
    menu.add(item);
    menu.add(new MenuItem("-"));
    item = new MenuItem("Close");
    item.addActionListener(this);
    menu.add(item);
    mbar.add(menu);
    
    menu = new Menu("Edit");
    item = new MenuItem("New attribute");
    item.addActionListener(this);
    item.setActionCommand("New");
    menu.add(item);
    item = new MenuItem("Delete attribute");
    item.addActionListener(this);
    item.setActionCommand("Delete");
    menu.add(item);
    item = new MenuItem("Edit value");
    item.setActionCommand("Value");
    item.addActionListener(this);
    menu.add(item);
    mbar.add(menu);


    tableModel = new AbstractTableModel() {
      public int getColumnCount() { return 2; }
      public int getRowCount() { return attributeList.size(); }
      public Object getValueAt(int row, int col) {
	Attribute attrib = (Attribute) attributeList.elementAt(row);
	if (col == 0)
	  return attrib.getName();
	else
	  return attrib.getValue();
      }
      public boolean isCellEditable(int row, int col) {
	
	Attribute attrib = (Attribute) attributeList.elementAt(row);
	if (col == 0) {
	  if(attrib.getName().equals("new attribute"))
	    return true;
	  else
	    return false;
	}
	if (attrib.isEditable() &&
	    attrib.getValue() instanceof String)
	  return true;
	else
	  return false;
      }
      public void setValueAt(Object aValue, int row, int col) {
	Attribute attrib = (Attribute) attributeList.elementAt(row);
	if (col == 1)
	  attrib.setValue(aValue);
	else
	  attrib.setName((String) aValue);
	attrib.CHANGED = true;
      }
      public String getColumnName(int column) {
	if (column == 0)
	  return "Name";
	else if (column == 1)
	  return "Value";
	else
	  return "";
      }
    };
    
 
    tableView = new JTable(tableModel);
    ListSelectionModel selectionModel = new DefaultListSelectionModel();
    selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tableView.setSelectionModel(selectionModel);
    JScrollPane scrollPane = new JScrollPane(tableView);
    scrollPane.setPreferredSize(new Dimension(400, 600));
    add(scrollPane);
    pack();
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof MenuItem) {
      String command = e.getActionCommand();
      if (e.getSource() instanceof DoneMenuItem &&
	  command.equals("Done")) {
	// Get the value represented by the uiFrame
	uiFrame c = ((DoneMenuItem) e.getSource()).getUIFrame();
	// CHANGED!!!!
	Object value = ((uiFrame) c).getAdapter().getWidgetAdapter().getUIComponentValue();
	c.dispose();
	// Set the attribute value
	int row = tableView.getSelectedRow();
	Attribute a = (Attribute) attributeList.elementAt(row);
	a.setValue(value);
	// Update the Table
	tableView.tableChanged(new TableModelEvent(tableModel, row));
      } else if (command.equals("Reload")) {
	attributeList = attributeSource.getAttributes();
	tableView.tableChanged(new TableModelEvent(tableModel));
      } else if (command.equals("Update")) {
	//attributeSource.setAttributes(attributeList);		  attributeSource.setLocalAttributes(attributeList);
      } else if (command.equals("Close")) {
	dispose();
      } else if (command.equals("New")) {
	int size = attributeList.size();
	attributeList.addElement(new Attribute("new attribute", "no value"));
	// Refresh the table
	tableView.tableChanged(new TableModelEvent(tableModel, size, size, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT)); 
      } else if (command.equals("Delete")) {
	int row = tableView.getSelectedRow();
	attributeList.removeElementAt(row);
	tableView.tableChanged(new TableModelEvent(tableModel, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
      } else if (command.equals("Value")) {
	// Put up a class choice window
	String initialValue;
	int row = tableView.getSelectedRow();
	Attribute attrib = (Attribute) attributeList.elementAt(row);
	initialValue = attrib.getValue().getClass().getName();
	Object result = JOptionPane.showInputDialog(null,
						    "Enter the attribute class",
						    "Attribute Class",
						    JOptionPane.QUESTION_MESSAGE,
						    null,
						    null,
						    initialValue);
	// uiGenerator to edit an instance of the class
	// add a done button which sets the value of the 
	// attribute
	if (result != null) {
	  ClassProxy c;
	  try {
	    c = uiClassFinder.forName((String) result);
	    Object obj = attrib.getValue();
	    if (!c.isInstance(obj))
	      obj = Instantiator.newInstance(c);
	    uiFrame f = uiGenerator.generateUIFrame(obj);
	    uiGenerator.uiAddConstants(f, obj);
	    VirtualMenuItem doneButton = f.addDoneItem();
	    doneButton.addActionListener(this);
	    //f.pack();
	    f.setVisible(true);
	  } catch (Exception ex) {
	  }
	}
      } else if (e.getSource() instanceof ConstantMenuItem) {
	ConstantMenuItem item = (ConstantMenuItem) e.getSource();
	Object obj = item.getConstant();
	int row = tableView.getSelectedRow();
	Attribute a = (Attribute) attributeList.elementAt(row);
	a.setValue(obj);
      }
    } 
  }
  public void actionPerformed(VirtualActionEvent e) {
	    if (e.getSource() instanceof MenuItem) {
	      String command = e.getActionCommand();
	      if (e.getSource() instanceof DoneMenuItem &&
		  command.equals("Done")) {
		// Get the value represented by the uiFrame
		uiFrame c = ((DoneMenuItem) e.getSource()).getUIFrame();
		// CHANGED!!!!
		Object value = ((uiFrame) c).getAdapter().getWidgetAdapter().getUIComponentValue();
		c.dispose();
		// Set the attribute value
		int row = tableView.getSelectedRow();
		Attribute a = (Attribute) attributeList.elementAt(row);
		a.setValue(value);
		// Update the Table
		tableView.tableChanged(new TableModelEvent(tableModel, row));
	      } else if (command.equals("Reload")) {
		attributeList = attributeSource.getAttributes();
		tableView.tableChanged(new TableModelEvent(tableModel));
	      } else if (command.equals("Update")) {
		//attributeSource.setAttributes(attributeList);
			  attributeSource.setLocalAttributes(attributeList);
	      } else if (command.equals("Close")) {
		dispose();
	      } else if (command.equals("New")) {
		int size = attributeList.size();
		attributeList.addElement(new Attribute("new attribute", "no value"));
		// Refresh the table
		tableView.tableChanged(new TableModelEvent(tableModel, size, size, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT)); 
	      } else if (command.equals("Delete")) {
		int row = tableView.getSelectedRow();
		attributeList.removeElementAt(row);
		tableView.tableChanged(new TableModelEvent(tableModel, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	      } else if (command.equals("Value")) {
		// Put up a class choice window
		String initialValue;
		int row = tableView.getSelectedRow();
		Attribute attrib = (Attribute) attributeList.elementAt(row);
		initialValue = attrib.getValue().getClass().getName();
		Object result = JOptionPane.showInputDialog(null,
							    "Enter the attribute class",
							    "Attribute Class",
							    JOptionPane.QUESTION_MESSAGE,
							    null,
							    null,
							    initialValue);
		// uiGenerator to edit an instance of the class
		// add a done button which sets the value of the 
		// attribute
		if (result != null) {
		  ClassProxy c;
		  try {
		    c = uiClassFinder.forName((String) result);
		    Object obj = attrib.getValue();
		    if (!c.isInstance(obj))
		      obj = Instantiator.newInstance(c);
		    uiFrame f = uiGenerator.generateUIFrame(obj);
		    uiGenerator.uiAddConstants(f, obj);
		    VirtualMenuItem doneButton = f.addDoneItem();
		    doneButton.addActionListener(this);
		    //f.pack();
		    f.setVisible(true);
		  } catch (Exception ex) {
		  }
		}
	      } else if (e.getSource() instanceof ConstantMenuItem) {
		ConstantMenuItem item = (ConstantMenuItem) e.getSource();
		Object obj = item.getConstant();
		int row = tableView.getSelectedRow();
		Attribute a = (Attribute) attributeList.elementAt(row);
		a.setValue(obj);
	      }
	    } 
	  }
}



