package bus.uigen.compose;

import bus.uigen.*;
import bus.uigen.ars.*;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.RemoteSelector;

import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class CalcActionListener  implements ActionListener {  
	
	Vector comps = null;
	String currName = null;
	String currType = null;
	String op = null;
	
	
	public CalcActionListener(Vector _comps, String _currName, String _currType, String _op) {
		comps = _comps;
		currName = _currName;
		currType = _currType;
		op = _op;
		
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		try {
			System.out.println(op);		if (op.equals("total")) 
			showTotal();
				else if (op.equals("average")) 
			showAverage();
						}
		catch (Exception ex) {ex.printStackTrace();}	
} 	

	
	public  void showAverage() {
		try {
			
			float avg = 0;
			
			if ( currType.equals("int")) {
				int total= 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Integer)(IntrospectUtility.getGetterMethod(
							ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).intValue();
							//RemoteSelector.getClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).intValue();
				avg = (float)total/comps.size();
				
			}
			
			else if ( currType.equals("double")) {
				double total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Double)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).doubleValue();
				avg = (float)total/comps.size();
			}

			else if ( currType.equals("float")) {
				double total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Float)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).floatValue();
				avg =(float) total/comps.size();
			}
			
			else if ( currType.equals("long")) {
				double total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Long)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).longValue();
				avg = (float)total/comps.size();
			}
			
			JOptionPane.showMessageDialog(null, ("Average is " + avg));
		}
		catch (Exception e) {
			e.printStackTrace();}

		
	}
	
	
	public  void showTotal() {
		try {
			if ( currType.equals("int")) {
				int total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Integer)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).intValue();
				JOptionPane.showMessageDialog(null, ("Total is " + total));
			}
			
			else if ( currType.equals("double")) {
				double total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Double)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).doubleValue();
				JOptionPane.showMessageDialog(null, ("Total is " + total));
			}

			else if ( currType.equals("float")) {
				double total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Float)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).floatValue();
				JOptionPane.showMessageDialog(null, ("Total is " + total));
			}
			
			else if ( currType.equals("long")) {
				double total = 0;
				for (int i = 0; i < comps.size(); i++) 
					total = total + ((Long)(IntrospectUtility.getGetterMethod(ACompositeLoggable.getTargetClass(comps.elementAt(i)),currName).invoke(comps.elementAt(i),null))).longValue();
				JOptionPane.showMessageDialog(null, ("Total is " + total));
			}
			

		}
		catch (Exception e) {
			e.printStackTrace();}
		
	}
}
	
	
	