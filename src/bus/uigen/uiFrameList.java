// Maintains a list
// of all the active uiFrames.
package bus.uigen;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class uiFrameList {
  static Vector<uiFrame> list = new Vector();
  
  public static Vector<uiFrame> getList() {
    return list;
  }
 
  public static Set<OEFrame> getOEFrames() {
	  Set<OEFrame> retVal = new HashSet();
	  retVal.addAll(list);
	  return retVal;
	  
  }
  
 
  
  public static Set<OEFrame> getOEFramesOtherThan(Set<OEFrame> excludeSet) {
	  Set<OEFrame> allFrames = getOEFrames();
	  allFrames.removeAll(excludeSet);
	  return allFrames;	  
  }
  
  public static void dispose (Set<OEFrame> frameSet) {
	  for (OEFrame frame: frameSet) {
		  frame.dispose();
	  }
  }

  public static void addFrame(uiFrame f) {
    list.addElement(f);
  }

  public static void removeFrame(uiFrame f) {
    list.removeElement(f);
  }
  
  public static OEFrame currentFrame() {
//	  if (list.size() == 0) return null;
	  return list.lastElement();	  
  }
  public static uiFrame previousFrame() {
	  if (list.size() < 2) return null;
	  return list.get(list.size() - 2);
  }
  public static int indexOfFrame(OEFrame frame) {
	  return list.indexOf(frame);
  }
  public static uiFrame getFrame (int index) {
	  return list.get(index);
  }
}
