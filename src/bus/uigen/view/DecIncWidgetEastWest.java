package bus.uigen.view;

import bus.uigen.*;
import bus.uigen.adapters.*;
import bus.uigen.ars.*;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.widgets.ButtonSelector;
import bus.uigen.widgets.VirtualButton;
import bus.uigen.widgets.VirtualContainer;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.border.*;


public class DecIncWidgetEastWest extends DecIncWidget {
	
	
	
	
	public DecIncWidgetEastWest(String text, int numColumns, int _decincunit) {
		super (text, numColumns, _decincunit);
	}
	public DecIncWidgetEastWest() {
		//super ();
	}
	
	
		
	VirtualButton getIncrementerButton () {
		/*
		Icon errorIcon = new ImageIcon("fooobarr.gif");
		Icon upIcon = new ImageIcon("fooobarr.gif");
		JButton up = new JButton();
		up.setIcon(upIcon);
		*/
		//String label = objectAdapter.getLabel();
		/*
		JButton up;
		String label;
		String iconFile;
		if (cdIncDec != null) {
			MethodDescriptor md = cdIncDec.getMethodDescriptor(incrementer.getName());
			iconFile = (String) md.getValue(AttributeNames.ICON);
			label = (String) md.getValue(AttributeNames.LABEL);
			if (label == null) label = ">";
			if (iconFile == null && label!= null && isFileName(label))
				iconFile = label;
			if (iconFile != null) {
				 Icon icon = new ImageIcon(iconFile);
				 up = new JButton();
				 up.setIcon(icon);	
				 return up;
			}
			
		} else label = ">";	
		up = new JButton(label);
		*/
		//JButton up = new JButton(">");
		VirtualButton up = ButtonSelector.createButton(">");
		//up.setVerticalAlignment(SwingConstants.TOP);
		return up;
		
	}
	VirtualButton getDecrementerButton () {
		//Icon downIcon = new ImageIcon("ST.gif");
		//JButton down = new JButton();
		//JButton down = new JButton("<");
		VirtualButton down = ButtonSelector.createButton("<");
		//down.setIcon(downIcon);
		//down.setVerticalAlignment(SwingConstants.TOP);
		return down;
		
	}	
	void addButtons (VirtualContainer c, VirtualButton up, VirtualButton down) {
		c.add(up, BorderLayout.EAST);
		c.add(down, BorderLayout.WEST);;
		
	}
	
	
}

