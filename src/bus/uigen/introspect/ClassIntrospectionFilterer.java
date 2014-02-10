package bus.uigen.introspect;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTextField;

import util.misc.Common;
import bus.uigen.ObjectEditor;
import bus.uigen.diff.AChangeDescription;
import bus.uigen.models.AComponentDrawer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.RowToRecord;
import bus.uigen.test.ALogo;

public class ClassIntrospectionFilterer {
	static Set<String> ignoreComponentsPackages = new HashSet();
	static Set<String> doNotIgnoreComponentsPackages = new HashSet();

	static Set<String> doNotIgnoreComponentsClasses = new HashSet();
	static Set<String> ignoreComponentsClasses = new HashSet();
	
	static Set<String> ignoreOperationsPackages = new HashSet();
	static Set<String> doNotIgnoreOperationsPackages = new HashSet();

	static Set<String> doNotIgnoreOperationsClasses = new HashSet();
	static Set<String> ignoreOperationsClasses = new HashSet();
	static final String[] initIgnoredComponentsPackagePrefixes = {"java", "bus.uigen"} ;
	static final String[] initIgnoredPropertiesPackagePrefixes = initIgnoredComponentsPackagePrefixes;
	
	static List<String> ignoredComponentsPackagePrefixes = Arrays.asList(initIgnoredComponentsPackagePrefixes);
	static List<String> ignoredPropertiesPackagePrefixes = Arrays.asList(initIgnoredPropertiesPackagePrefixes);
	
//	static boolean ignoreComponentsOfJavaPackagesByDefault = true;
//	static boolean ignoreOperationsOfJavaPackagesByDefault = true;
//	static boolean ignoreComponentsOfOEPackagesByDefault = true;
//	static boolean ignoreOperationsOfOEPackagesByDefault = true;
	public static String JAVA_CLASS_PREFIX = "java";
	public static String OE_CLASS_PREFIX = "bus.uigen";
	
	public void addIgnoredComponentPackagePrefix(String aPackagePrefix) {
		ignoredComponentsPackagePrefixes.add(aPackagePrefix);
	}
	
	public void addIgnoredPropertiesPackagePrefix(String aPackagePrefix) {
		ignoredPropertiesPackagePrefixes.add(aPackagePrefix);
	}

	
//	public void ignoreComponentsOfJavaPackagesByDefault(boolean newVal) {
//		ignoreComponentsOfJavaPackagesByDefault = newVal;
//	}
//	
//	public void ignoreOperationsOfJavaPackagesByDefault(boolean newVal) {
//		ignoreOperationsOfJavaPackagesByDefault = newVal;
//	}
	 
	public static boolean isJavaClass (String aPackageName) {
		return aPackageName.startsWith(JAVA_CLASS_PREFIX);
		
	}
	
	public static void ignoreComponentsOfPackages (String[] somePackageNames) {
		for (String aPackageName:somePackageNames)  {
			ignoreComponentsOfPackage(aPackageName);
		}
	}
    public static void ignoreComponentsOfPackage (String aPackageName) {
    	ignoreComponentsPackages.add(aPackageName);		
	}
    
	public static void doNoIgnoreComponentsOfPackages (String[] somePackageNames) {
		for (String aPackageName:somePackageNames)  {
			doNotIgnoreComponentsOfPackage(aPackageName);
		}
	}
    public static void doNotIgnoreComponentsOfPackage (String aPackageName) {
    	doNotIgnoreComponentsPackages.add(aPackageName);		
	}
    
    public static void ignoreComponentsOfClasses (Class[] someClasses) {
		for (Class aClass:someClasses)  {
			ignoreComponentsOfClass(aClass);
		}
	}
	
   public static void doNotIgnoreComponentsOfClass (Class aClass) {
	   doNotIgnoreComponentsClasses.add(aClass.getName());
		
	}
   public static void doNotIgnoreComponentsOfClasses (Class[] someClasses) {
	   for (Class aClass:someClasses)  {
	   doNotIgnoreComponentsOfClass(aClass);
	   }
		
	}
   
   
   public static void doNotIgnoreComponentsOfClass (ClassProxy aClassProxy) {
	   
	   doNotIgnoreComponentsClasses.add(aClassProxy.getName());

  	}
   
   public static void ignoreComponentsOfClass (Class aClass) {
	   ignoreComponentsClasses.add(aClass.getName());
		
  	}
   
   public static void ignoreComponentsOfClass (ClassProxy aClassProxy) {
	   ignoreComponentsClasses.add(aClassProxy.getName());

		
 	}
   
// methods
   public static void ignoreOperationsOfPackage (String aPackageName) {
	   ignoreOperationsPackages.add(aPackageName);
		
	}
	
  public static void doNotIgnoreOperationsOfClass (Class aClass) {
	   doNotIgnoreComponentsClasses.add(aClass.getName());
		
	}
  
  public static void doNotIgnoreOperationsOfClass (ClassProxy aClassProxy) {
	  doNotIgnoreComponentsClasses.add(aClassProxy.getName());
		
 	}
  
  public static void ignoreOperationsOfClass (Class aClass) {
	   ignoreOperationsClasses.add(aClass.getName());
		
 	}
  
  public static void ignoreOperationsOfClass (ClassProxy aClassProxy) {
	   ignoreOperationsClasses.add(aClassProxy.getName());		
	}
  public static void doNotIgnoreOperationsOfPackage (String aPackageName) {
  	doNotIgnoreOperationsPackages.add(aPackageName);		
	}
  public static void doNotIgnoreOperationsOfPackages (String[] somePackageNames) {
		for (String aPackageName:somePackageNames)  {
			doNotIgnoreOperationsOfPackage(aPackageName);
		}
	}
  
  public static boolean componentsIgnored(ClassProxy aClassProxy) {
	  util.annotations.DisplayToString displayToString = (util.annotations.DisplayToString) aClassProxy
				.getAnnotation(util.annotations.DisplayToString.class);
	  if (displayToString != null) {
		  return displayToString.value();		  
	  }
	  String className = aClassProxy.getName();
	  if (doNotIgnoreComponentsClasses.contains(className)) {
		  return false;
	  }
	  if (ignoreComponentsClasses.contains(className)) {
		  return true;
	  }
	  String packageName = Common.classNameToPackageName(className);;
	  if (doNotIgnoreComponentsPackages.contains(packageName)) {
		  return false;
	  }
	  if (ignoreComponentsPackages.contains(packageName)) {
		  return true;
	  }
	  for (String packagePrefix:ignoredComponentsPackagePrefixes) {
		  if (packageName.startsWith(packagePrefix))
			  return true;
	  }
//	  if (isJavaClass(packageName) && ignoreComponentsOfJavaPackagesByDefault)
//		  return true;
	
	  return false;
  }
  
  
  public static boolean operationsIgnored(ClassProxy aClassProxy) {
	  String className = aClassProxy.getName();
	  if (doNotIgnoreOperationsClasses.contains(className)) {
		  return false;
	  }
	  if (ignoreOperationsClasses.contains(className)) {
		  return true;
	  }
//	  String packageName = aClassProxy.getPackageName();
	  String packageName = Common.classNameToPackageName(className);;

	  if (doNotIgnoreOperationsPackages.contains(packageName)) {
		  return false;
	  }
	  if (ignoreOperationsPackages.contains(packageName))
		  return true;
	  for (String packagePrefix:ignoredPropertiesPackagePrefixes) {
		  if (packageName.startsWith(packagePrefix))
			  return true;
	  }
//	  if (isJavaClass(packageName) && ignoreOperationsOfJavaPackagesByDefault)
//		  return true;
	  return false;
  }
  
  static {
	  final String[] doNotIgnorePackages = {"java.util", "bus.uigen.models",
			  "bus.uigen.controller.models", "bus.uigen.test", "bus.uigen.util", 
			  "bus.uigen.viewgroups", "bus.uigen.shapes", "bus.uigen.widgets.display", 
			  "bus.uigen.jung", "java.awt.geom", "bus.uigen.test.vehicle"};
	  final Class[] doNotIgnoreClasses = {Rectangle.class, AChangeDescription.class, ALogo.class, RowToRecord.class};
	  doNoIgnoreComponentsOfPackages(doNotIgnorePackages);
	  doNotIgnoreOperationsOfPackages(doNotIgnorePackages);
	  doNotIgnoreComponentsOfClasses (doNotIgnoreClasses);
	  ignoreOperationsOfClass(void.class);
	  ignoreComponentsOfClass(void.class);
//	   final String[] ignorePackages = {
//		  "java.awt", "java.awt.color", "java.awt.datatransfer", "java.awt.dnd", "java.awt.event", "java.awt.font", "java.awt.font", 
//		  "java.awt.geom", "java.awt.im", "java.awt.im.spi", "java.awt.image", "java.awt.image.renderable", "java.awt.print"
//		  "javax.swing", "javax.swing.border", "javax.swing.colorchooser", "javax."};
	  
  }
  
  public static void main(String[] args) {
//	  ObjectEditor.edit("hello world");
	  
	  ObjectEditor.edit(new JTextField("hello"));
  }
 
  

}
