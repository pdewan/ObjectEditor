//
//
package bus.uigen;

import bus.agent.*;
import java.rmi.*;

public class RegisterObject {
  public static String register(String busAgentName, Object object, String objectname) {
    // First get a handle to the bus agent
    BusAgent agent;
    try {
      agent = (BusAgent) Naming.lookup(busAgentName);
      String r = agent.register(object, objectname);
      return r;
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage is java RegisterObject <bus agent name> <object name>");
      System.exit(0);
    }
    Integer n = new Integer(10);
    String gid = register(args[0], n, args[1]);
    System.out.println("Registered object: "+gid);
  }
}
