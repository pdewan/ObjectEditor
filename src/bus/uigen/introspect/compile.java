package bus.uigen.introspect;

import java.io.*;
import java.lang.reflect.*;

// Try to write  a java file
// Compile it and instantiate an
// object of that class.

public class compile {
  public static void main(String[] args) {
    File javaFile = new File("child.java");
    try {
      PrintWriter fs = new PrintWriter(new FileOutputStream(javaFile));
      
      // Generate the java code
      fs.println("public class child {\n");
      fs.println("private int id = 114;\n");
      fs.println("public int getId() {\n return id;\n}\n");
      fs.println("public void setId(int newid) {\n id=newid;\n}\n");
      fs.println("}\n");
      fs.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Compile the generated code
    String arguments[] = new String[2];
    //arguments[0] = "-classpath";
    //arguments[1] = classpath;
    arguments[0] = "-nowarn";
    arguments[1] = "child.java";
/*
    sun.tools.javac.Main compiler;
    try {
      compiler = new sun.tools.javac.Main(System.err, "javac");
      compiler.compile(arguments);
    } catch (Exception e) {
      System.err.println("Could not load the JDK compiler");
      System.exit(1);
    }
    */
    
    // Try to instantiate the generated class
    // Have to use reflection, because the class is
    // not defined till runtime.
    try {
      Class childClass = Class.forName("child");
      Object childObject = childClass.newInstance();
      
      // Get the Method
      Class[] params = new Class[1];
      params[0] = Integer.TYPE;
      Method setMethod = childClass.getMethod("setId", params);
      Method getMethod = childClass.getMethod("getId", null);
      
      // Invoke the methods and see if it works!
      
      Object[] arg = new Object[1];
      arg[0] = new Integer(112175);
      setMethod.invoke(childObject, arg);
      
      Integer returnval = (Integer) getMethod.invoke(childObject, null);
      System.err.println("Got the return value "+returnval);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
