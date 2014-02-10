package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import java.awt.MenuItem;import java.lang.reflect.*;


public class ConstructorMenuItem extends MenuItem{
  Constructor constructor;
  
  Constructor getConstructor() {
    return constructor;
  }

  public ConstructorMenuItem(String methodName, Constructor m) {
    super(methodName);
    constructor = m;
  }
}
