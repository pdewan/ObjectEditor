package bus.uigen.introspect;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.BeanDescriptor;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Vector;import bus.uigen.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class BeanInfoWriter {
  
  public static boolean writeBeanInfo(ClassDescriptorInterface vinfo, String filename) {

   
    StringWriter swriter = new StringWriter();
    PrintWriter writer = new PrintWriter(swriter);

    // Begin writing the BeanInfo
    ClassProxy beanclass = vinfo.getBeanDescriptor().getBeanClass();
    StringTokenizer tokenizer = new StringTokenizer(beanclass.getName(), 
						    ".", 
						    false);
    String beanpackage ="";
    String beanname = "";
    while (tokenizer.hasMoreTokens()) {
      beanname = tokenizer.nextToken();
      if (tokenizer.hasMoreTokens()) {		  if (beanpackage.equals(""))			  beanpackage = beanname;		  else
			beanpackage = beanpackage+"."+beanname;
      }
    }
    
    writeHeader(writer, beanpackage, beanname);
    writeBeanDescriptor(writer, beanclass, vinfo.getBeanDescriptor());
    writeMethodDescriptors(writer, beanclass, vinfo.getMethodDescriptors());
    writePropertyDescriptors(writer, beanclass, vinfo.getPropertyDescriptors());
    writeEnding(writer);
    writer.close();
    return writeFormattedJavaFile(filename, swriter.toString());
  }


  private static void writeHeader(PrintWriter writer, 
				  String pack,
				  String name) {
    if (!"".equals(pack))
      writer.println("package "+pack+";");

    writer.println("import java.beans.*;");
    writer.println("import java.lang.reflect.*;");
    writer.println("import bus.uigen.AttributeNames;");	writer.println("import bus.uigen.uiClassFinder;");
    
    writer.println();
    // Call the class a BeanInfo since were not
    // writing out the FieldDescriptors right now
    writer.println("public class "+name+"BeanInfo extends SimpleBeanInfo {");
  }

  private static void writeEnding(PrintWriter writer) {
    writer.println("}");
  }

  private static void writeDeclarations(PrintWriter writer) {
    writer.println("Class c;");
    writer.println("Class[] params;");
  }
  

  private static void writeBeanDescriptor(PrintWriter writer, 
					  ClassProxy beanClass,
					  BeanDescriptorProxy bd) {
    writer.println("public BeanDescriptor getBeanDescriptor() {");
    writer.println("try {");
    writeDeclarations(writer);
    //writer.println("c = "+beanClass.getName()+".class;");	writer.println("c = uiClassFinder.forName(\""+beanClass.getName()+"\");");
    writer.println("BeanDescriptor bd = new BeanDescriptor(c);");
    
    Enumeration attribNames = bd.attributeNames();
    String att;
    Object val;
    while (attribNames.hasMoreElements()) {
      att = (String) attribNames.nextElement();
      val = bd.getValue(att);
      writeAttribute(writer, "bd", att, val);
    }    

    writer.println("return bd;");
    writer.println("} catch (Exception e) {");
    writer.println("return null;");
    writer.println("}");
    writer.println("}");
  }


  private static void writeMethodDescriptors(PrintWriter writer,
					     ClassProxy beanClass,
					     MethodDescriptorProxy[] methods) {
    Vector v = new Vector();
    for (int i=0; i<methods.length; i++) {
      Boolean visible = (Boolean) methods[i].getValue(AttributeNames.VISIBLE);
      if (visible == null ||
	  visible.booleanValue()) {
	v.addElement(methods[i]);
      }
    }
    if (v.size() == 0)
      return;
    methods = new MethodDescriptorProxy[v.size()];
    for (int i=0; i<v.size(); i++)
      methods[i] = (MethodDescriptorProxy ) v.elementAt(i);

    writer.println("public MethodDescriptor[] getMethodDescriptors() {");
    writer.println("try {");
    writeDeclarations(writer);
    //writer.println("c = "+beanClass.getName()+".class;");	writer.println("c = uiClassFinder.forName(\""+beanClass.getName()+"\");");
    writer.println("Method m;");
    writer.println("MethodDescriptor[] array = new MethodDescriptor["+methods.length+"];");
    writer.println("MethodDescriptor md;");
    for (int i=0; i<methods.length; i++) {

      MethodProxy m = methods[i].getMethod();
      //VirtualMethod m = methods[i];
      ClassProxy[] params = m.getParameterTypes();
      writer.println("params = new Class["+params.length+"];");
      for (int j=0; j<params.length; j++) {
	String paramtype = "";
	try {
	  if (params[j].isPrimitive()) 
	    paramtype = PrimitiveClassList.getWrapperType(params[j]).getName()+".TYPE";
	  else if (params[j].isArray())			   paramtype = params[j].getComponentType().getName() + "[].class";	  else
	    paramtype = params[j].getName()+".class";
	} catch (Exception e) {e.printStackTrace();}
	writer.println("params["+j+"] = "+paramtype+";");
      }
      writer.println("m = c.getMethod(\""+m.getName()+"\",params);");
      writer.println("md = new MethodDescriptor(m);");
      
      // Write out the properties
      if (!methods[i].getName().equals(methods[i].getDisplayName()))
	writer.println("md.setDisplayName(\""+methods[i].getDisplayName()+"\");");
      Enumeration attribNames = methods[i].attributeNames();
      String att;
      Object val;
      while (attribNames.hasMoreElements()) {
	att = (String) attribNames.nextElement();
	val = methods[i].getValue(att);
	writeAttribute(writer, "md", att, val);
      }
      writer.println("array["+i+"] = md;");
      writer.println();
    }
    writer.println("return array;");
    writer.println("} catch (Exception e) {");
    writer.println("return null;\n }");
    writer.println("}");
  }
  

  private static void writePropertyDescriptors(PrintWriter writer,
					       ClassProxy beanClass,
					       PropertyDescriptorProxy[] properties) {

    Vector v = new Vector();
    for (int i=0; i<properties.length; i++) {
      Boolean visible = (Boolean) properties[i].getValue(AttributeNames.VISIBLE);
      if (!properties[i].getName().equals("") && (visible == null ||
	  visible.booleanValue() )) {
	v.addElement(properties[i]);
      }
    }
	// do not want to return as writing no properties == all properties	/*
    if (v.size() == 0)
      return;	*/
    properties = new PropertyDescriptorProxy[v.size()];
    for (int i=0; i<v.size(); i++)
      properties[i] = (PropertyDescriptorProxy ) v.elementAt(i);
    // maybe comment this out - should not return if length is 0
    /*	if (properties.length == 0)
      return;	*/
    writer.println("public PropertyDescriptor[] getPropertyDescriptors() {");
    writer.println();
    writer.println("try {");
    writeDeclarations(writer);
    writer.println("PropertyDescriptor[] array = new PropertyDescriptor["+properties.length+"];");
    writer.println("PropertyDescriptor pd;");
    

    for (int i=0; i<properties.length; i++) {
      writer.println("pd = new PropertyDescriptor(\""+properties[i].getName()+"\", "+beanClass.getName()+".class");
      /*	  if (properties[i].getWriteMethod() == null) 
	writer.println(", \""+properties[i].getReadMethod().getName()+"\", null);");
      else
	writer.println(");");
	  */	  if (properties[i].getWriteMethod() == null) 		  if (properties[i].getReadMethod() == null) 			  writer.println(", null, null);");		  else
			writer.println(", \""+properties[i].getReadMethod().getName()+"\", null);");
      else
		writer.println(");");
    
      // Write out the properties
      if (!properties[i].getName().equals(properties[i].getDisplayName()))
	writer.println("pd.setDisplayName(\""+properties[i].getDisplayName()+"\");");
      Enumeration attribNames = properties[i].attributeNames();
      String att;
      Object val;
      while (attribNames.hasMoreElements()) {
	att = (String) attribNames.nextElement();
	val = properties[i].getValue(att);
	writeAttribute(writer, "pd", att, val);
      }
      writer.println("array["+i+"] = pd;");
      writer.println();
    }
    writer.println("return array;");
    writer.println("}catch (Exception e) {return null;}");
    writer.println("}");
  }


  private static void writeFieldDescriptors(PrintWriter writer,
					       Class beanClass,
					       FieldDescriptor[] fields) {
    
    Vector v = new Vector();
    for (int i=0; i<fields.length; i++) {
      Boolean visible = (Boolean) fields[i].getValue(AttributeNames.VISIBLE);
      if (visible == null ||
	  visible.booleanValue()) {
	v.addElement(fields[i]);
      }
    }
    if (v.size() == 0)
      return;
    fields = new FieldDescriptor[v.size()];
    for (int i=0; i<v.size(); i++)
      fields[i] = (FieldDescriptor ) v.elementAt(i);
    
    if (fields.length == 0)
      return;
    writer.println("public FieldDescriptor[] getFieldDescriptors() {");
    writer.println();
    writer.println("try {");
    writeDeclarations(writer);
    writer.println("FieldDescriptor[] array = new FieldDescriptor["+fields.length+"];");
    writer.println("FieldDescriptor pd;");
    writer.println("Field f;");
    writer.println("Class cl = "+beanClass.getName()+".class;");
    writer.println();
    for (int i=0; i<fields.length; i++) {
      writer.println("f = cl.getField(\""+fields[i].getName()+"\");");
      writer.println("pd = new FieldDescriptor(f);");
    
      // Write out the attributes
      if (!fields[i].getName().equals(fields[i].getDisplayName()))
	writer.println("pd.setDisplayName(\""+fields[i].getDisplayName()+"\");");
      Enumeration attribNames = fields[i].attributeNames();
      String att;
      Object val;
      while (attribNames.hasMoreElements()) {
	att = (String) attribNames.nextElement();
	val = fields[i].getValue(att);
	writeAttribute(writer, "pd", att, val);
      }
      writer.println("array["+i+"] = pd;");
      writer.println();
    }
    writer.println("return array;");
    writer.println("}catch (Exception e) {return null;}");
    writer.println("}");
  }


  private static void writeAttribute(PrintWriter writer, 
				     String prefix, 
				     String att, 
				     Object val) {
    if (val instanceof Method) {
      writeMethodAttribute(writer, prefix, att, (Method) val);
    }
    else {
      writer.print(prefix+".setValue(\""+att+"\", ");
      // output some repsn of the object
      if (val instanceof String) 
	writer.println("\""+val+"\");");
      else {
	writer.print("new "+val.getClass().getName()+"("+val.toString()+")");
	writer.println(");");
      }
    }
  }

  private static void writeMethodAttribute(PrintWriter writer, 
					   String prefix, 
					   String att, 
					   Method val) {
    Class[] args = val.getParameterTypes();
    writer.println("params = new Class["+args.length+"];");
    for (int i=0; i<args.length; i++) 
      writer.println("params["+i+"] = "+args[i].getName()+".class;");
    writer.print(prefix+".setValue(\""+att+"\", ");
    writer.println(val.getDeclaringClass().getName()+".class.getMethod(\""+val.getName()+"\", params));");
  }

  private static boolean writeFormattedJavaFile(String filename, String text) {
    try {
      System.err.println("Writing file "+filename);
      FileWriter fout = new FileWriter(filename);
      StringReader sr = new StringReader(text);
  
      try {
	com.ibm.cf.CodeFormatter cf = new com.ibm.cf.CodeFormatter();
	cf.formatCode(sr, fout);
	sr.close();
	fout.close();
	return true;
      }  catch (Exception e) {
	// CodeFormatter not available
	// dump contents of text to file
	PrintWriter pw = new PrintWriter(fout);
	pw.print(text);
	pw.close();
	return true;
      }
    }  catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}



