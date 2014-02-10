package bus.uigen.controller;

import java.lang.reflect.Field;import bus.uigen.introspect.*;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.local.AClassProxy;


public class MethodParametersViewInfo extends SimpleViewInfo {
  public FieldDescriptorProxy[] getFieldDescriptors() {
    FieldDescriptorProxy[] array= new FieldDescriptorProxy[4];
    FieldDescriptorProxy fd;
    ClassProxy c = AClassProxy.classProxy(MethodParameters.class);
    FieldProxy f;
    
    try {
      f = c.getField("CopyOnSelect");
      fd = new AFieldDescriptorProxy(f);
      fd.setDisplayName("Copy object to clipboard on selection?");
      array[0] = fd;
      f = c.getField("ConfirmOnMethod");
      fd = new AFieldDescriptorProxy(f);
      fd.setDisplayName("Confirm when method takes implicit argument?");
      array[1] = fd;
      f = c.getField("FeedbackOnConstant");
      fd = new AFieldDescriptorProxy(f);
      fd.setDisplayName("Message when constant selected?");
      array[2] = fd;
      f = c.getField("EditBeanInfo");
      fd = new AFieldDescriptorProxy(f);
      fd.setDisplayName("Edit BeanInfo?");
      array[3] = fd;
      return array;
    } catch (Exception e) {e.printStackTrace();return null;}
  }
}
