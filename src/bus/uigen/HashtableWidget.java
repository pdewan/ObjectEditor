package bus.uigen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import bus.uigen.adapters.HashtableWidgetAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.DoneMenuItem;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.view.SelectionColorSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;

public class HashtableWidget extends JPanel implements VirtualActionListener, ActionListener {
  
  private JScrollPane scrollPane;
  private JTable tableView;
  private TableModel tableModel;
  private JPanel     buttonPanel;
  private JButton editButton, selectButton, deleteButton, insertButton;

  private boolean selected = false;
  
  public HashtableWidget() {
    
    setLayout(new BorderLayout());
    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(200, 200));
    add(scrollPane, BorderLayout.CENTER);
    
    buttonPanel = initButtonPanel();
    add(buttonPanel, BorderLayout.SOUTH);
    disableWidget(true);
    //setPreferredSize(new Dimension(400, 200));
  }

  public void initTableView(TableModel model) {
    if (tableView != null) {
      scrollPane.remove(tableView);
    }
    tableModel = model;
    tableView = new JTable(tableModel);
    ListSelectionModel selectionModel = new DefaultListSelectionModel();
    tableView.setSelectionModel(selectionModel);
    scrollPane.setViewportView(tableView);
  }

  private JPanel initButtonPanel() {
    JPanel panel = new JPanel();
    //panel.setLayout(new GridLayout(1, 4));
    JButton button = new JButton("Select");
    button.addActionListener(this);
    panel.add(button);
    selectButton = button;

    button = new JButton("Insert");
    button.addActionListener(this);
    panel.add(button);
    insertButton = button;
    
    button = new JButton("Delete");
    button.addActionListener(this);
    panel.add(button);
    deleteButton = button;
    
    button = new JButton("Edit");
    button.addActionListener(this);
    panel.add(button);
    editButton = button;
    return panel;
  }
  
  Color oldColor = getBackground();
  
  public void enableWidget(boolean loop) {
    insertButton.setEnabled(true);
    deleteButton.setEnabled(true);
    editButton.setEnabled(true);
    if (loop &&
	tableModel != null &&
	tableModel instanceof HashtableWidgetAdapter)
      ((HashtableWidgetAdapter) tableModel).setSelected(true);
    setBackground((Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR));
  }

  public void disableWidget(boolean loop) {
    insertButton.setEnabled(false);
    deleteButton.setEnabled(false);
    editButton.setEnabled(false);
    if (loop &&
	tableModel != null &&
	tableModel instanceof HashtableWidgetAdapter)
      ((HashtableWidgetAdapter) tableModel).setSelected(false);
    setBackground(oldColor);
  }
  public void actionPerformed(VirtualActionEvent evt) {
	    Object source = evt.getSource();
	    String name   = evt.getActionCommand();
	    if (source instanceof VirtualButton) {
	      if (name.equals("Select")) {
		if (selected) {
		  disableWidget(true);
		  selected = false;
		}
		else {
		  enableWidget(true);
		  selected = true;
		}
	      }
	      else if (name.equals("Insert")) {
		int size = tableModel.getRowCount(); // Get number of rows in the Table
		// Update the model
		((HashtableWidgetAdapter) tableModel).insertEntry();
		// Update the view
		//tableView.tableChanged(new TableModelEvent(tableModel, size, size, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		tableView.tableChanged(new TableModelEvent(tableModel));
	      }
	      else if (name.equals("Delete")) {
		int row = tableView.getSelectedRow();
		// Update the model
		((HashtableWidgetAdapter) tableModel).deleteEntry(row);
		// Update the view
		tableView.tableChanged(new TableModelEvent(tableModel, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	      }
	      else if (name.equals("Edit")) {
		// Put up a class choice window
		String initialValue;
		int row = tableView.getSelectedRow();
		
		// Get the selected row "attribute"
		Object value = tableModel.getValueAt(row, 1);
		
		initialValue = value.getClass().getName();
		Object result = JOptionPane.showInputDialog(null,
							    "Enter class",
							    "Class",
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
		    Object obj = value;
		    if (!c.isInstance(obj))
		      obj = Instantiator.newInstance(c);
		    
		    // Also add the adaptors as children of the hashtable adaptor!!
		    
		    uiFrame f = uiGenerator.generateUIFrame(obj);
		    uiGenerator.uiAddConstants(f, obj);
		    VirtualMenuItem doneButton = f.addDoneItem();
		    doneButton.addActionListener(this);
		    //f.pack();
		    f.setVisible(true);
		  } catch (Exception ex) {
		  }
		}
	      }
	    }
	    else if (evt.getSource() instanceof DoneMenuItem &&
		     name.equals("Done")) {
	      // Get the value represented by the uiFrame
	      uiFrame c = ((DoneMenuItem) evt.getSource()).getUIFrame();
	      Object value =  c.getAdapter().getValue();
	      ((uiFrame) c).dispose();
	      // Set the attribute value
	      int row = tableView.getSelectedRow();
	      // Update the Model
	      tableModel.setValueAt(value, row, 1);
	      // Update the Table
	      tableView.tableChanged(new TableModelEvent(tableModel, row));
	    }
	  }



  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();
    String name   = evt.getActionCommand();
    if (source instanceof JButton) {
      if (name.equals("Select")) {
	if (selected) {
	  disableWidget(true);
	  selected = false;
	}
	else {
	  enableWidget(true);
	  selected = true;
	}
      }
      else if (name.equals("Insert")) {
	int size = tableModel.getRowCount(); // Get number of rows in the Table
	// Update the model
	((HashtableWidgetAdapter) tableModel).insertEntry();
	// Update the view
	//tableView.tableChanged(new TableModelEvent(tableModel, size, size, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	tableView.tableChanged(new TableModelEvent(tableModel));
      }
      else if (name.equals("Delete")) {
	int row = tableView.getSelectedRow();
	// Update the model
	((HashtableWidgetAdapter) tableModel).deleteEntry(row);
	// Update the view
	tableView.tableChanged(new TableModelEvent(tableModel, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
      }
      else if (name.equals("Edit")) {
	// Put up a class choice window
	String initialValue;
	int row = tableView.getSelectedRow();
	
	// Get the selected row "attribute"
	Object value = tableModel.getValueAt(row, 1);
	
	initialValue = value.getClass().getName();
	Object result = JOptionPane.showInputDialog(null,
						    "Enter class",
						    "Class",
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
	    Object obj = value;
	    if (!c.isInstance(obj))
	      obj = Instantiator.newInstance(c);
	    
	    // Also add the adaptors as children of the hashtable adaptor!!
	    
	    uiFrame f = uiGenerator.generateUIFrame(obj);
	    uiGenerator.uiAddConstants(f, obj);
	    VirtualMenuItem doneButton = f.addDoneItem();
	    doneButton.addActionListener(this);
	    //f.pack();
	    f.setVisible(true);
	  } catch (Exception ex) {
	  }
	}
      }
    }
    else if (evt.getSource() instanceof DoneMenuItem &&
	     name.equals("Done")) {
      // Get the value represented by the uiFrame
      uiFrame c = ((DoneMenuItem) evt.getSource()).getUIFrame();
      Object value =  c.getAdapter().getValue();
      ((uiFrame) c).dispose();
      // Set the attribute value
      int row = tableView.getSelectedRow();
      // Update the Model
      tableModel.setValueAt(value, row, 1);
      // Update the Table
      tableView.tableChanged(new TableModelEvent(tableModel, row));
    }
  }
}






