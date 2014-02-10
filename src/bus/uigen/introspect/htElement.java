package bus.uigen.introspect;

import java.beans.BeanDescriptor;
import java.beans.FeatureDescriptor;import java.beans.PropertyDescriptor;

import bus.uigen.attributes.AnAttributeName;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.sadapters.BeanToRecord;


public class htElement {
  private FeatureDescriptorProxy fd;
  //private String attribute;
  AnAttributeName attribute;
  private AClassDescriptor cd;
  ObjectAdapter objectAdapter;

  public htElement(FeatureDescriptorProxy fd, 
		   String attribute,
		   AClassDescriptor cd) {
    this.fd = fd;
    //this.attribute = attribute;
    this.attribute = new AnAttributeName(attribute);
    this.cd = cd;
  }
  public htElement(FeatureDescriptorProxy fd, 
		   AnAttributeName attribute,
		   AClassDescriptor cd,
		   ObjectAdapter theObjectAdapter) {
   this.fd = fd;
   //this.attribute = attribute;
   this.attribute = attribute;
   this.cd = cd;
   objectAdapter = theObjectAdapter;
 }

  public htElement(FeatureDescriptorProxy fd, 
		   String name, 
		   String attribute,
		   AClassDescriptor cd,
		   ObjectAdapter theObjectAdapter) {
    this.fd = fd;
    this.name = name;
    //this.name = new AnAttributeName (name);
    //this.attribute = attribute;
    this.attribute = new AnAttributeName(attribute);
    this.cd = cd;
  }
  
  public htElement(FeatureDescriptorProxy fd, 
		   String name, 
		   AnAttributeName attribute,
		   AClassDescriptor cd,
		   ObjectAdapter theObjectAdapter) {
   this.fd = fd;
   this.name = name;
   //this.name = new AnAttributeName (name);
   this.attribute = attribute;
   //this.attribute = new AnAttributeName(attribute);
   this.cd = cd;
 }
  
  private String name = null;
  //private AnAttributeName name = null;
  public String getName() {
  //public AnAttributeName getName() {
    if (name != null)
      return name;
    else
      return fd.getName();
    	//return new AnAttributeName(fd.getName());
  }
  

  public Object getValue() {
    Object o;
    /*
    if (AttributeNames.LABEL.equals(attribute))
      o = fd.getDisplayName();
    else
    */
      //o = fd.getValue(attribute);
      o = fd.getValue(attribute.getValue());
     
    if (o != null)
      return o;
    if (objectAdapter != null && objectAdapter instanceof CompositeAdapter && (fd instanceof PropertyDescriptorProxy)) {
    	CompositeAdapter parentAdapter = (CompositeAdapter) objectAdapter;
    	String propertyName = fd.getName();
    	ObjectAdapter childAdapter = parentAdapter.getChildAdapterMapping(propertyName);
    	if (childAdapter != null) {
    		o = childAdapter.getMergedTempOrDefaultAttributeValue(attribute.getValue());
    		if (o != null)
    			return o;
    		o = childAdapter.getComputedAttributeValue(attribute.getValue());
    		if (o != null)
    			return o;
    	}
    	  	
    	
    }
    // this is relevant only if objectAdapter is null
    //else {
      //o = AttributeNames.getDefault(attribute);
      o = AttributeNames.getDefault(attribute.getValue());
      if (o != null) return o;
      o = AttributeNames.getSystemDefault(attribute.getValue()) ; 
      if (o != null) return o;  
    	  return "";
    //}
  }

  public void setValue(Object v) {	  System.out.println (v);	  	  System.out.println (attribute);
	  /*
    if (AttributeNames.LABEL.equals(attribute))
      fd.setDisplayName((String) v);
    else
    */
      //fd.setValue(attribute, v);
      fd.setValue(attribute.getValue(), v);
  }

  public void setAttributeName(AnAttributeName s) {
    //attribute = s;
	  attribute.setValue(s.getValue());
  }
  
}
