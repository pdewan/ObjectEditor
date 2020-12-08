package bus.uigen.view;

import bus.uigen.adapters.*;
import bus.uigen.ars.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.translator.*;


public class ModelClass {
	
	ClassProxy modelClass = null; //don't need this cuz widget passes in displayed val
	
	public ModelClass(ClassProxy propertyClass) {  //you should probably pass in the 
										  //objectadapter here so that you can get the type below
		
		modelClass = (ClassProxy)propertyClass;
		
		System.err.println("created model for decinc of type " +  modelClass.getName());
	}
	
	
	public String getNextValue(String currentValue, int _decincunit) throws Exception{
	//create a new object of the type and then check what kind of type
	//it is by checking it's instance
		
		if (modelClass.getName().equals("java.lang.Integer"))  {
			
			Integer newValue;	
			int temp = ((Integer)(TranslatorRegistry.convert("java.lang.Integer", currentValue))).intValue()+_decincunit;
			newValue =	new Integer(temp);
			
			
			//assuming StringToInteger translator will be called...well if you look at 
			//setUIComponentValue in uiWidgetAdatper newVal is of type integer so I will create it
			//here as one and feed it to TransRegis... subsequently, setValue in uiTextCompAdap casts the
			//returned value to String
			
			if (newValue != null)
				return ((String)TranslatorRegistry.convert("java.lang.String", newValue));	
			else 
				return "";
			
		}
		
		else if (modelClass.getName().equals("java.lang.Double")) {
			
			Double newValue;	
	//don't thinkthere is a translator for this?
			double decincunit = (new Integer(_decincunit)).doubleValue();
			
			double temp = Double.valueOf(currentValue).doubleValue();
			temp = temp + decincunit;
			return (String.valueOf(temp));
	/*		
			//double temp = ((Double)(TranslatorRegistry.convert("java.lang.Double", currentValue))).doubleValue()+1.0;
			System.err.println("incing double w/ " + temp);			
			newValue =	new Double(temp);
	
			if (newValue != null)
				return ((String)TranslatorRegistry.convert("java.lang.String", newValue));	
			else 
				return "";
	*/		
	
		}

		else if (modelClass.getName().equals("java.lang.Boolean"))  {

			Boolean newValue;
			boolean temp = !((Boolean)(TranslatorRegistry.convert("java.lang.Boolean", currentValue))).booleanValue();
			newValue =	new Boolean(temp);

			if (newValue != null)
				return ((String)TranslatorRegistry.convert("java.lang.String", newValue));	
			else 
				return "";
		}		
		
		else if (modelClass.getName().equals("java.lang.String"))  {}
		
		else {System.err.println("Model doesn't support incrememnt of modelclass");}
	
		return currentValue;
	
	}
	
	public String getPreviousValue(String currentValue, int _decincunit) throws Exception {
//create a new object of the type and then check what kind of type
	//it is by checking it's instance
		

		if (modelClass.getName().equals("java.lang.Integer"))  {
			Integer newValue;
			int temp = ((Integer)(TranslatorRegistry.convert("java.lang.Integer", currentValue))).intValue()-_decincunit;
			newValue =	new Integer(temp);
					
			//assuming StringToInteger translator will be called...well if you look at 
			//setUIComponentValue in uiWidgetAdatper newVal is of type integer so I will create it
			//here as one and feed it to TransRegis... subsequently, setValue in uiTextCompAdap casts the
			//returned value to String
			
			if (newValue != null)
				return ((String)TranslatorRegistry.convert("java.lang.String", newValue));	
			else 
				return "";
		}
		
		
		else if (modelClass.getName().equals("java.lang.Double")) {
			
			Double newValue;	
			double decincunit = (new Integer(_decincunit)).doubleValue();

			double temp = Double.valueOf(currentValue).doubleValue();
			temp = temp - decincunit;
			return (String.valueOf(temp));
			
			/*
			double temp = ((Double)(TranslatorRegistry.convert("java.lang.Double", currentValue))).doubleValue()-1;
			newValue =	new Double(temp);
	
			if (newValue != null)
				return ((String)TranslatorRegistry.convert("java.lang.String", newValue));	
			else 
				return "";
			*/
			
		}
		else if (modelClass.getName().equals("java.lang.Boolean"))  {
			Boolean newValue;
			boolean temp = !((Boolean)(TranslatorRegistry.convert("java.lang.Boolean", currentValue))).booleanValue();
			newValue =	new Boolean(temp);
		
			if (newValue != null)
				return ((String)TranslatorRegistry.convert("java.lang.String", newValue));	
			else 
				return "";
			
		}
		
		else if (modelClass.getName().equals("java.lang.String"))  {}
		
		else {System.err.println("Model doesn't support decrement of modelclass");}

	return currentValue;
	
	}
}//end