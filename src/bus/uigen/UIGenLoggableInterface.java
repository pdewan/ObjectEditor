package bus.uigen;import java.rmi.Remote;
import java.rmi.RemoteException;
import logging.logger.*;
public interface UIGenLoggableInterface extends Remote
{
	public void uigenReplayToModel(Object command) throws RemoteException;
	public void uigenLogUnivPropertyChange(UnivPropertyChange upc) throws RemoteException;
	public void uigenLogUnivVectorEvent(UnivVectorEvent uve) throws RemoteException;
	public void uigenReturnValue(ForwardReturnValue frv) throws RemoteException;
	public void uigenGiveNotifyOnLoggableEvent(Object command) throws RemoteException;
	public void uigenProcessModify(Object objectName, AttribValue values[]) throws RemoteException;
}
