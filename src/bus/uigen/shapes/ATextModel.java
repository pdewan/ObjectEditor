package bus.uigen.shapes;

import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.text.AttributedString;
import java.util.List;

import shapes.FlexibleTextShape;
import shapes.TextModel;
import shapes.TextShape;
import util.models.AListenableVector;
import bus.uigen.ObjectEditor;

//import shapes.shapes.TextModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.TEXT_PATTERN)
public class ATextModel extends AShapeModel implements FlexibleTextShape {
	AttributedString attributedString;
	
	public ATextModel (Rectangle theBounds)
    {
		try {
        shapeModel = new shapes.TextModel(theBounds);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ATextModel (String s)
    {
		try {
			 shapeModel = new shapes.TextModel(s);
			 init();
		//super(createjavax.swing.JTextField(s), new Rectangle(0, 0, 7*s.length(), 20));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ATextModel (String s, int x, int y, int width, int height)
    {
		try {
        shapeModel = new shapes.TextModel(s, x, y, width, height);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ATextModel ()
    {
		try {
        shapeModel = new shapes.TextModel();
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }
	
	shapes.TextModel getTextModel() {
		return (shapes.TextModel) shapeModel;
	}
	@Override
	public String getText() {
		return getTextModel().getText();
	}
	@Override
	public void setText(String newValue) {
		getTextModel().setText(newValue);
	}	
	public static void main (String[] args) {
		ObjectEditor.edit(new ATextModel("hello", 100, 200, 200, 100));
		List list = new AListenableVector();
		int maxElements = 300;
		for (int i = 0; i < maxElements; i++) {
			list.add(new ATextModel("" + i, i*2, i*2, 20, 20));
		}
		ObjectEditor.edit(list);
	}
	@Override
	public AttributedString getAttributedString() {
		return attributedString;
	}
	@Override
	public void setAttributedString(AttributedString newVal) {
		attributedString = newVal;
	}
//	public void copy (Object aReference) {
//		
//		((TextModel) shapeModel).copy((TextModel) ((ATextModel) aReference).shapeModel);
//	}

}
