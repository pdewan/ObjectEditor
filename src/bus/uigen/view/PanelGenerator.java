package bus.uigen.view;import bus.uigen.ars.*;import bus.uigen.oadapters.*;import java.awt.Container;
//
// New version of the uiGenerator class
// Works with FeatureDescriptor wrappers
// around elements (fields, properties, methods, constants)
// and is more attribute/connection oriented.
//
public class PanelGenerator {
  // Generate a Panel representing 
  // the object to be editted	/*
  public static uiPanel generatePanel(Object object) {
    // Algorithm is as follows
    // Generate a ClassDescriptor and store it for later
    // use.
    // Obtain the PropertyDescriptor (which stores instance
    // attributes)
    uiPanel panel = new uiPanel();
    
  }	*/
  
  protected void addComponents(Object parentObject, 
			       Object object, 
			       ObjectAdapter parentAdapter,
			       Container container) {
    // Create a temporary "AttributeStore" and 
    // call getValue() on it to get any required att value
    // this object should take care of all inheritance/default
    // issues	  /*
    AttributeStore.initialise(object, parentObject);	  */
    // Do what is required for this instance
    // Figure out what widget to use and instantiate it
    // Set any widget specific attributes
    // Do the connection in a separate method to make it
    // possible tp Bus-Connect them if required later on
    
    
    // Get the widget position and add it at the appropriate position
    // in the parent component

    // Get a list of methods that go in the pop-up menu
    // and create a pop-up menu associated with this object
    
    // Get a list of field's and properties, 
    // maybe sort them according to their positions?
    // recursively call addComponents.
  }
}
