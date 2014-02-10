
package bus.uigen;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

import bus.uigen.ars.*;
import bus.uigen.oadapters.ObjectAdapter;

public class MulticastEditor extends Frame implements ActionListener{
		/*
	private uiAttributeCollection attributeSource;
	private Vector attributeList;

	private JTable tableView;
	private TableModel tableModel;	*/

	private JTextField member;	private ObjectAdapter sourceObject;

	public MulticastEditor(ObjectAdapter source) {
		super();
		sourceObject = source;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			dispose();
			}
			});
		
		MenuBar mbar = new MenuBar();
		Menu menu;
		MenuItem item;
		
		setMenuBar(mbar);
		
		menu = new Menu("Broadcast/Multicast");		item = new MenuItem("Reset to Broadcast");		item.addActionListener(this);		menu.add(item);		menu.add(new MenuItem("-"));
		item = new MenuItem("Reset to Multicast");		item.addActionListener(this);		menu.add(item);
		item = new MenuItem(" Add Multicast Member");
		item.addActionListener(this);
		menu.add(item);
		item = new MenuItem(" Remove Multicast Member");
		item.addActionListener(this);
		menu.add(item);
		item = new MenuItem(" Remove All Multicast Members");
		item.addActionListener(this);
		menu.add(item);
		item = new MenuItem(" Couple Replicas");
		item.addActionListener(this);
		item.setActionCommand("Couple");
		menu.add(item);
		item = new MenuItem(" Uncouple Replicas");
		item.addActionListener(this);
		item.setActionCommand("Uncouple");
		menu.add(item);
		menu.add(new MenuItem("-"));
		item = new MenuItem("Close");
		item.addActionListener(this);
		menu.add(item);
		mbar.add(menu);
		
		menu = new Menu("Effect Range");
		item = new MenuItem("Object Only");
		item.addActionListener(this);
		menu.add(item);
		item = new MenuItem("Include All Descendants");
		item.addActionListener(this);
		menu.add(item);
		mbar.add(menu);

/*
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
		tableView.setSelectionModel(selectionModel);*/		member = new JTextField();		JScrollPane scrollPane = new JScrollPane(member);
		scrollPane.setPreferredSize(new Dimension(400, 30));		add(scrollPane);
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof MenuItem) {
			String command = e.getActionCommand();
			if (command.equals(" Add Multicast Member")) {				System.out.println("Adding "+member.getText()+" to ["+sourceObject.getCompletePathOnly()+"]");				ObjectRegistry.logAddMulticastUser(sourceObject.getCompletePathOnly(),member.getText());
			} else if (command.equals(" Remove Multicast Member")) {
				System.out.println("Removing "+member.getText()+" from ["+sourceObject.getCompletePathOnly()+"]");
				ObjectRegistry.logDeleteMulticastUser(sourceObject.getCompletePathOnly(),member.getText());			} else if (command.equals(" Remove All Multicast Members")) {
				System.out.println("Removing All Members from ["+sourceObject.getCompletePathOnly()+"]");
				ObjectRegistry.logDeleteAllMulticastUsers(sourceObject.getCompletePathOnly());			} else if (command.equals("Reset to Broadcast")){				System.out.println("Broadcast on for ["+sourceObject.getCompletePathOnly()+"]");
				ObjectRegistry.logResetToBroadcast(sourceObject.getCompletePathOnly());			} else if (command.equals("Reset to Multicast")){				System.out.println("Multicast on for ["+sourceObject.getCompletePathOnly()+"]");				ObjectRegistry.logResetToMulticast(sourceObject.getCompletePathOnly());
			} else if (command.equals("Close")) {			} else if (command.equals("Couple")) {
				System.out.println("Coupling ["+sourceObject.getCompletePathOnly()+"]");				ObjectRegistry.logCoupleProgramReplicas(sourceObject.getCompletePathOnly(),true);
			} else if (command.equals("Uncouple")) {				System.out.println("Uncoupling ["+sourceObject.getCompletePathOnly()+"]");				ObjectRegistry.logCoupleProgramReplicas(sourceObject.getCompletePathOnly(),false);
			} else if (command.equals("Object Only")){				System.out.println("Only ["+sourceObject.getCompletePathOnly()+"] affected");
				ObjectRegistry.logSetBroadMultiCastDomain(sourceObject.getCompletePathOnly(),false);			} else if (command.equals("Include All Descendants")){				System.out.println("All Descendants of ["+sourceObject.getCompletePathOnly()+"] affected, too");
				ObjectRegistry.logSetBroadMultiCastDomain(sourceObject.getCompletePathOnly(),true);			}
			dispose();
		} 
	}
}



