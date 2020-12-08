package bus.uigen.editors;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Vector;
import java.awt.Point;
import java.util.Enumeration;
import java.awt.Rectangle;
import slm.SLModel;
import shapes.LineModel;
import shapes.OvalModel;
import shapes.ShapeModel;
import util.models.AListenable;
import util.models.Listenable;
import util.undo.Listener;
public class Connections extends AListenable implements Listener {
	
	SLModel drawing;
	Vector connections = new Vector();
	Vector lineKeys = new Vector();
	
	public Connections () throws RemoteException {
		try {
		if (drawing == null) {
			drawing = new SLModel();
			//drawing.addListener(this);
		     //initialize();
		}
		} catch (Exception e) {
			e.printStackTrace();
			//System.err.println(e);
		}
	}
	
	
	
	public void connect(String sourceKey, String destinationKey, String connectionName) {
		try {
		Connection connection = new Connection(drawing);
		//connection.setDrawing(drawing);
		connection.makeConnection(sourceKey, destinationKey, connectionName);		
		connections.addElement(connection);
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void setDrawing (SLModel newVal) {
		drawing = newVal;
	}
	public SLModel getDrawing () {
		return drawing;
	}
	
	public void reset() {
		System.err.println("reset");
	}
	
	public void update(Listenable model, Object arg)
    {  
		System.err.println("update");		
	
    }
	
}