package bus.uigen;

import java.rmi.*;

public interface uiProxyInterface extends Remote {
  public void setObject(Object object) throws RemoteException;
  public String getName() throws RemoteException;
  public void setName(String name) throws RemoteException;
}
