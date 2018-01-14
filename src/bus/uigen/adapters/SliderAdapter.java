
package bus.uigen.adapters;import java.util.Vector;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.trace.Tracer;
import util.trace.uigen.SliderOverflow;
import bus.uigen.WidgetAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.SliderSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualSlider;
import bus.uigen.widgets.events.VirtualActionEvent;
import bus.uigen.widgets.events.VirtualActionListener;
import bus.uigen.widgets.events.VirtualFocusListener;


// JSliderAdapter
// An adaptor between the JSlider widget
// and any Primitive type

public class SliderAdapter extends WidgetAdapter 
implements ChangeListener, VirtualFocusListener, VirtualActionListener, javax.swing.BoundedRangeModel {
 
  public SliderAdapter() {
  }
  javax.swing.DefaultBoundedRangeModel defaultModel = new DefaultBoundedRangeModel();
  // Implementation of 
  // abstract methods
  
  public String getType() {
    return "java.lang.Integer";
  }
  
  int value;
  VirtualSlider slider;
  boolean linked = false;
  public VirtualComponent instantiateComponent(ClassProxy cclass, ObjectAdapter adapter) {

	   //slider.setValue((Integer) adapter.getValue()*2);
	    slider = SliderSelector.createSlider();
		  instantiatedComponent = true;

	    //slider.addChangeListener(this);	    
	    //slider.addActionListener((ActionListener) this);
	   
	    return slider;
	  }
  public VirtualComponent getUIComponent() {
	  return slider;
  }
  //double scaleFactor = 1;
  //final int MAX_SLIDER_VALUE = 100;
  //int maxValue = DEFAULT_SLIDER_RANGE;
  int maxValue = defaultModel.getMaximum();
  int minValue = defaultModel.getMinimum();
  static int DEFAULT_SLIDER_RANGE = 100;
  static int DEFAULT_MAX_FIRST_VALUE_RATIO= 2;
  Object minObject, maxObject;
  int error = 0;
  //int scaledValue;
  public void setUIComponentTypedValue(Object newval) {
    //Component c = getUIComponent();
    //if (c instanceof JSlider) {
    	//slider = (JSlider) c;
		//int value;	  
	    try {		if (newval == null)
			return ;
		else			
           value = ((Integer) newval).intValue();
	    } catch (ClassCastException ce) {
	    	Tracer.error("Slider model cannot be: " + newval.getClass().getSimpleName());
	    	value = 0;
	    }
	    if (notInRange(newval))
	    	return;
	    
	    if (value > maxValue) {
//	    	Tracer.warning("New slider value: " + value + " > " + "the max value: " + maxValue + '\n' + "Increasing the max value.");
	    	SliderOverflow.newCase(value, maxValue, this);
	    	
	    }
	    if (value < minValue) {
//	    	Tracer.warning("New slider value: " + value + " < " + "the min value: " + minValue + '\n' + "Decreasing the min value.");
	    	SliderOverflow.newCase(minValue, maxValue, this);
	    }
	    maxValue = Math.max(maxValue, value);
		minValue = Math.min(minValue, value);
		//slider.updateUI();
		/*
		if ((maxValue - minValue) > DEFAULT_SLIDER_RANGE  )
			scaleFactor = (maxValue - minValue)/ (double)DEFAULT_SLIDER_RANGE;
		else
			scaleFactor = 1;
		scaledValue = (int) ((value - minValue)/scaleFactor);
		*/
		//slider.setValue((int) (scaledValue));
		//slider.updateUI();
		notifyListeners();
		
		
      //((JSlider) c).setValue(value);
    
    /*if (c instanceof JSlider && newval instanceof String) {
      int value = 0;
      try {
	value = Float.valueOf((String) newval).intValue();
      } catch (NumberFormatException e) {
      }
      ((JSlider) c).setValue(value);
    }*/
  }
 
  public Object getUIComponentValue() {
    //Component c = getUIComponent();
    //if (c instanceof JSlider)
      //return Integer.toString((int) (slider.getValue()*scaleFactor + minValue));
	  return (Integer) (getValue());
	  //return Integer.toString((int) (getValue()));
      //return Integer.toString((int) (slider.getValue()));
    //else
      //return "0";
  }
  boolean editable;
  public void setUIComponentEditable() {
	  /*
    Component c = getUIComponent();
    if (c instanceof JSlider) {
      ((JSlider) c).setValueIsAdjusting(false);
    }
    */
	  // is this right?
	  editable = true;
	  //slider.updateUI();
	  slider.setValueIsAdjusting(editable);
  }
  
  public void setUIComponentUneditable() {
	  /*
    Component c = getUIComponent();
    if (c instanceof JSlider) {
      ((JSlider) c).setValueIsAdjusting(true);
    }
    */
	  editable = false;
	  //slider.updateUI();
	  slider.setValueIsAdjusting(editable);
  }

/*
  Color oldColor = Color.white;
  public void setUIComponentSelected() {	  super.setUIComponentSelected();
     if (getUIComponent().getBackground() !=SelectionColorSelector.getColor())
       oldColor = getUIComponent().getBackground();
   getUIComponent().setBackground(SelectionColorSelector.getColor());
  }
 
  
  public void setUIComponentDeselected() {	  super.setUIComponentDeselected();
   getUIComponent().setBackground(oldColor);
  }
  */

  public void linkUIComponentToMe() {
    //if (c instanceof JSlider) {
     // JSlider s = (JSlider) c;
      slider.addChangeListener(this);
      slider.addFocusListener(this);
      int curValue = (Integer) getObjectAdapter().getValue();
      maxValue = Math.max(maxValue, curValue*DEFAULT_MAX_FIRST_VALUE_RATIO);
      maxObject = getObjectAdapter().getMaxValue();
      if (maxObject != null && maxObject instanceof Number) {
    	  maxValue = ((Number) maxObject).intValue();
      } else if (maxValue < curValue)
    	  maxValue = Math.max(maxValue, curValue*2);
      minObject = getObjectAdapter().getMinValue();
      if (minObject != null && minObject instanceof Number) {
    	  minValue = ((Number) minObject).intValue();
      } else if (minValue > curValue)
    	  minValue = Math.min(minValue, maxValue - DEFAULT_SLIDER_RANGE);
      slider.setModel(this);
      //scaleFactor = (maxValue - minValue)/DEFAULT_SLIDER_RANGE  + 1;
      linked = true;	  //Container parent = c.getParent();	  /*
	  if (parent != null)		c.setBackground(parent.getBackground());	  */
    //}
  }
  public void linkUIComponentToMe(VirtualComponent c) {
	  if (slider == c && linked)
		  return;
	  if (c == slider) {
		  	
	  		super.setAttributes(c);
	  	
	  }
	  slider = (VirtualSlider) c;
	  linkUIComponentToMe();
  }

  // Method invoked when the text value of the  
  // AWT component changes
  public void stateChanged(ChangeEvent e) {
    //System.out.println("Event");
	  if ( value != slider.getValue()) {
//		  System.out.println("new slider value:" + slider.getValue());
		  value = slider.getValue();
    uiComponentValueChanged();
	  }
  }
  public void actionPerformed(VirtualActionEvent e) {
	  if ( value != slider.getValue()) {
//		  System.out.println("new slider value:" + slider.getValue());
		  value = slider.getValue();

	       uiComponentValueChanged();
	  }
	     
   }
  /*

  // Methods invoked on focus gain/loss in the view
  public void focusGained(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusGained();
    }
  }

  public void focusLost(FocusEvent e) {
    if (e.isTemporary())
      return;
    else {
      uiComponentFocusLost();
    }
  }
  */
 public  int getMinimum() {
	 return minValue;
 }
  
  // Method descriptor #3 (I)V
  public  void setMinimum(int arg0) {
	  minValue = arg0;
  }
  
  // Method descriptor #1 ()I
  public  int getMaximum() {
	  return maxValue;
  }  
  
  // Method descriptor #3 (I)V
  public void setMaximum(int arg0) {
	  maxValue = arg0;
  }
  
  // Method descriptor #1 ()I
  public  int getValue() {
	  return value;
  }
  
  // Method descriptor #3 (I)V
  public  void setValue(int arg0) {
	  if (value == arg0)
		  return;
	  value = arg0;
	  //if ( value != arg0)
//		  System.out.println("new slider value:" + arg0);
    uiComponentValueChanged();
  }
  
  // Method descriptor #5 (Z)V
  public void setValueIsAdjusting(boolean arg0) {
	  editable = arg0;
	  //defaultModel.setValueIsAdjusting(arg0);
  }
  
  // Method descriptor #2 ()Z
  public  boolean getValueIsAdjusting() {
	  return editable;
	  //return defaultModel.getValueIsAdjusting();
  }
  
  // Method descriptor #1 ()I
  public  int getExtent() {
	  return defaultModel.getExtent();
  }
  
  // Method descriptor #3 (I)V
  public  void setExtent(int arg0) {
	  defaultModel.setExtent(arg0);
  }
  
  // Method descriptor #4 (IIIIZ)V
  public  void setRangeProperties(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
	  defaultModel.setRangeProperties(arg0, arg1, arg2, arg3, arg4);
  }
  
  Vector<ChangeListener> listeners = new Vector();
  
  // Method descriptor #23 (Ljavax/swing/event/ChangeListener;)V
  public  void addChangeListener(javax.swing.event.ChangeListener l) {
	  if (listeners.contains(l))
		  return;
	  listeners.add(l);
  }
  
  // Method descriptor #23 (Ljavax/swing/event/ChangeListener;)V
  public  void removeChangeListener(javax.swing.event.ChangeListener arg0) {
  }
  
  public void notifyListeners() {
	  for (int i = 0; i < listeners.size(); i++)
		  listeners.get(i).stateChanged(new ChangeEvent(this));
  }

  
}



