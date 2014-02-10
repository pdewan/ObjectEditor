package bus.uigen.editors;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;import javax.swing.JTable;import javax.swing.table.TableModel;import bus.uigen.editors.Connections;import bus.uigen.undo.*;
import bus.uigen.oadapters.*;import bus.uigen.visitors.*;import bus.uigen.introspect.*;import bus.uigen.ars.*;
import bus.uigen.attributes.*;import bus.uigen.controller.*;import bus.uigen.view.AGenericWidgetShell;import bus.uigen.view.OEFrameSelector;import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.WidgetAdapter;import bus.uigen.uiFrame;import bus.uigen.AutomaticRefresh;import bus.uigen.ObjectEditor;//import java.util.Vector;

public interface  AdapterMatrix {
	
			
	public void set (int rowNum, int colNum, ObjectAdapter adapter);


}
