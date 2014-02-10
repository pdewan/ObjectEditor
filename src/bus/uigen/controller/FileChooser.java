package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.ars.*;import javax.swing.*;
import java.io.File;

//no references to this
public class FileChooser {
  
  public static File getInstanceOf(Class type) {
    
    JFileChooser chooser = new JFileChooser();
    int returnvalue = chooser.showDialog(null, "Select");
    if (returnvalue == JFileChooser.APPROVE_OPTION)
      return chooser.getSelectedFile();
    else
      return null;
  }
  /*
  public static String getInstanceOf(Class type) {
    File file = getInstanceOf(type);
    if (file != null)
      return file.getPath();
    else
      return null;
  }  */
}
