package bus.uigen.introspect;

// "Static" class that maintains
// the mapping between Interfaces/Classes
// and preferred implementing classes/sub classes
// useful for specializing parameter types, etc
import java.util.*;//import bus.uigen.uiGenerator;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;

public class uiClassMapper {
  // A hashtable of Vectors.  static final String CLASS_SUFFIX = "Impl";  static final String CLASS_PREFIX1 = "A";  static final String CLASS_PREFIX2 = "An";
  static private Hashtable mapping;// = new Hashtable();  private static String[] objectSubclassArray = {"Object", "String", "Vector", "Hashtable", "Integer", "Double"};
      static void init() {	  mapping = new Hashtable();	  Vector v = util.misc.Common.arrayToVector(objectSubclassArray);	  Set classes = ClassDescriptorCache.getClasses();
	  for (Object nextClass:classes) {
//		  Object nextClass = classes.nextElement();
		  String fullName;
		  if (nextClass instanceof ClassProxy)
			  fullName = ((ClassProxy) nextClass).getName();
		  else 
			  fullName = (String) nextClass;		  //String className = ClassDescriptor.toShortName(((Class) classes.nextElement()).getName());
		  String className = AClassDescriptor.toShortName(fullName);		  if (!v.contains(className))			  v.addElement(className);
	  }	  
	  //mapping.put(Object.class, uiGenerator.arrayToVector(objectSubclassArray));
	  mapping.put(Object.class, v);  }
  /*
  static public void updateClassMapping(String baseClass,
					   String subClass) {	 // System.out.println("Mapping updated");
    Vector v = (Vector) mapping.get(baseClass);
    if (v == null) {
      v = new Vector();
      v.addElement(baseClass);
      mapping.put(baseClass, v);
    }
    // First check to see if subClass already exists
    // in the Vector
    if (v.contains(subClass)) {
      // Move subClass to the "top"
      v.removeElement(subClass);
      v.insertElementAt(subClass, 0);
      return;
    }
    else {
      // Keep only the last ten classes
      if (v.size() > 10)
	v.removeElementAt(10);
      v.insertElementAt(subClass, 0);	 // System.out.println("mapping size" + v.size());
      return;
    }
  }  */
  static public void updateClassMapping(ClassProxy baseClass,
					   String subClass) {	 // System.out.println("Mapping updated");	  /*
	  Vector v = (Vector) mapping.get(baseClass);
    if (v == null) {
	  */
	  if (mapping == null)
		  init();	Object o = mapping.get(baseClass);
	Vector v;	if (o != null)		v = (Vector) o;	else {
    
      v = new Vector();
      v.addElement(AClassDescriptor.toShortName(baseClass.getName()));
      mapping.put(baseClass, v);
    }
    // First check to see if subClass already exists
    // in the Vector
    if (v.contains(subClass)) {
      // Move subClass to the "top"
      v.removeElement(subClass);
      v.insertElementAt(subClass, 0);
      return;
    }
    else {
      // Keep only the last ten classes
      if (v.size() > 10)
	v.removeElementAt(10);
      v.insertElementAt(subClass, 0);	 // System.out.println("mapping size" + v.size());
      return;
    }
  }    static public boolean isSubClass(String baseClass, String subClass ) {
	  //System.out.println("isSubClass" + baseClass + subClass);	  try {
		  return RemoteSelector.forName(baseClass).isAssignableFrom (RemoteSelector.forName(subClass));
	  } catch (Exception e) {		  return false;
	  }  }
   static public boolean isSubClass(ClassProxy baseClass, ClassProxy subClass ) {
	  //System.out.println("isSubClass" + baseClass + subClass);
		  return baseClass.isAssignableFrom (subClass);
	    }    public static String toClassName(String baseClass)  {	  //System.out.println("to clas name " + baseClass);	  String classFromInterface = "";
	  
	  try {		  classFromInterface = baseClass;
		  //System.out.println("after try" + baseClass);	    if (RemoteSelector.forName(baseClass).isInterface())	{
			//System.out.println("is interface");			if (isSubClass (baseClass, classFromInterface = (baseClass + CLASS_SUFFIX)))
				  updateClassMapping(RemoteSelector.forName(baseClass), classFromInterface);			
			else if (isSubClass (baseClass, classFromInterface = (CLASS_PREFIX1 + baseClass))) 
				  updateClassMapping(RemoteSelector.forName(baseClass), classFromInterface);
						else if (isSubClass (baseClass, classFromInterface = (CLASS_PREFIX2 + baseClass)))
				  updateClassMapping(RemoteSelector.forName(baseClass), classFromInterface);
			  		} 	  } catch (Exception e) {
		  //System.out.println("exception");		  //System.out.println(baseClass + e);
							
	  }
	  	  //System.out.println("return value " + baseClass);	  return classFromInterface;
  }
	  

  static public Vector getClassMapping(ClassProxy baseClass)  {	  if (mapping == null)		  init();
	  Vector v = (Vector) mapping.get(baseClass);	String classFromInterface;	String baseClassName = AClassDescriptor.toShortName(baseClass.getName());
    if (v == null) {
      v = new Vector();		  
	  v.addElement(baseClassName);
	  try {		if (baseClass.isInterface())	{			  if (isSubClass (baseClass,  uiClassFinder.forName (classFromInterface = baseClassName + CLASS_SUFFIX)))
				  updateClassMapping(baseClass, classFromInterface);
			  else if (isSubClass (baseClass,  uiClassFinder.forName(classFromInterface = CLASS_PREFIX1 + baseClassName)))
				  updateClassMapping(baseClass, classFromInterface);			  else if (isSubClass (baseClass,  uiClassFinder.forName(classFromInterface = CLASS_PREFIX2 + baseClassName)))
				  updateClassMapping(baseClass, classFromInterface);
			  Vector v2 = (Vector) mapping.get(baseClass);
			  if (v2 == null) 
			      return v;
			  else return v2;		} else 			  return v;			  	  } catch (Exception e) {
		  //System.out.println(e);	  }
    }
    return v;
  }
  /*static public Vector getClassMapping(Class baseClass)  {
	  Vector v = (Vector) mapping.get(baseClass);	String classFromInterface;
    if (v == null) {
      v = new Vector();		  
	  v.addElement(baseClass);
	  try {		if (baseClass.isInterface())	{			  if (isSubClass (baseClass, classFromInterface = (baseClass + CLASS_SUFFIX)))
				  updateClassMapping(baseClass, classFromInterface);
			  if (isSubClass (baseClass, classFromInterface = (CLASS_PREFIX1 + baseClass)))
				  updateClassMapping(baseClass, classFromInterface);			  if (isSubClass (baseClass, classFromInterface = (CLASS_PREFIX2 + baseClass)))
				  updateClassMapping(baseClass, classFromInterface);
			  Vector v2 = (Vector) mapping.get(baseClass);
			  if (v2 == null) 
			      return v;
			  else return v2;		} else 			  return v;			  	  } catch (Exception e) {
		  //System.out.println(e);	  }
    }
    return v;
  }
*/

}