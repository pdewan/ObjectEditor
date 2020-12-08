package bus.uigen.undo;
import java.util.Enumeration;

import util.undo.Undoer;
import slm.ShapesList;
import shapes.RemoteShape;
import shapes.ShapeModel;
public class SLPutCommand extends util.undo.AbstractCommand
{
    private ShapesList shapesList;
    private RemoteShape originalShapeModel = null;
    private RemoteShape shapeModelArg;
    private String keyArg;
	
    public SLPutCommand (ShapesList theShapesList,
       String theKey, RemoteShape theShapeModel)
    {
        shapesList = theShapesList;
        keyArg = theKey;
        shapeModelArg = theShapeModel;

    }
	public RemoteShape getShapeModel() {
		return shapeModelArg;
	}
	public String getKey() {
		return keyArg;
	}
    public boolean isUndoable()
    {
        return(true);
    }
    public void execute()
    {
        originalShapeModel = shapesList.put(keyArg, shapeModelArg);
		//System.err.println("shapeModelArg " + shapeModelArg + "original" + originalShapeModel);
    }
    public void undo()
    {
        if (originalShapeModel == null)
            shapesList.remove(keyArg);
        else
            shapesList.put(keyArg, originalShapeModel);
    }
    public void redo()
    {
        execute();
    }

    public static void printKeys(ShapesList theShapesList)
    {
        System.err.println("Printing Keys");
        for (Enumeration keys = theShapesList.keys();
                keys.hasMoreElements();)
            {
                String key = (String) keys.nextElement();
                System.err.println("Key" + (String) key);
            }
    }

    public static void put (Undoer undoer, ShapesList theShapesList,
        String theKey, RemoteShape theShapeModel)
    {
        SLPutCommand c =  new SLPutCommand(theShapesList, theKey, theShapeModel);
        undoer.execute(c);
    }


}

