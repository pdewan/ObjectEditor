package bus.uigen;

import java.rmi.*;
import java.rmi.server.*;
import java.security.*;
//import bus.uigen.*;


public class uiProxy extends UnicastRemoteObject implements uiProxyInterface {
  
  private String name="";
 
  public uiProxy() throws RemoteException {
  }

  public void setObject(Object object) throws RemoteException {
    System.out.println("Connecting...");
    myLockManager lman = new myLockManager();
    uiFrame frame = uiGenerator.generateUIFrame(object, 
						lman);
    frame.show();
  }

  public String getName() throws RemoteException{
    return name;
  }

  public void setName(String n) throws RemoteException{
    name = n;
  }
   
  public static void main(String args[])
   {
    if (args.length != 2) {
      System.out.println("Usage is java uiProxy <bus agent name> <object name>");
      System.exit(0);
    }
    try {
      uiProxy proxy = new uiProxy();
      proxy.name = args[1];
      String gid = RegisterObject.register(args[0], proxy, args[1]);
      System.out.println("Registered object: "+gid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}




