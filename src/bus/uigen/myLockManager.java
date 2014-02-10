// Test lock manager written for
// demo of uigenerator 1.1
package bus.uigen;
import java.util.*;

public class myLockManager {
  private Hashtable myHashTable = new Hashtable();

  public boolean lock(Object obj, Object requestor) {
    if (myHashTable.containsKey(obj)) {
      if (myHashTable.get(obj) == requestor) {
	return true;
      } 
      else {
	return false;
      }
    } else {
      myHashTable.put(obj, requestor); 
      return true;
    }
  }
  public void unlock(Object obj, Object requestor) {
    if (myHashTable.get(obj) == requestor) {
      myHashTable.remove(obj);
    }
  }
}











