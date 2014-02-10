package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.text.AttributedString;

import shapes.BoundedShape;
import shapes.LabelModel;
import shapes.LabelShape;
import shapes.StringModel;
import util.annotations.Explanation;
import bus.uigen.ObjectEditor;

//import shapes.shapes.LabelModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.LABEL_PATTERN)
@Explanation("A Label with all the operations you will ever need")
public class ALabelModel extends AShapeModel implements LabelShape {
	
	public ALabelModel (Rectangle theBounds)
    {
		try {
        shapeModel = new shapes.LabelModel(theBounds, null);
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public ALabelModel (String s)
    {
		try {
			 shapeModel = new shapes.LabelModel(s, null);
			 init();
		//super(createjavax.swing.JTextField(s), new Rectangle(0, 0, 7*s.length(), 20));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public ALabelModel (String s, String imageFile, int x, int y, int width, int height)
    {
		try {
        shapeModel = new shapes.LabelModel(s, imageFile, x, y, width, height, null);       
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public ALabelModel ()
    {
		try {
        shapeModel = new shapes.LabelModel((Component) null); 
        init();
    }catch (Exception e) {
		e.printStackTrace();
	}
    }
	@Override
	public String getText() {
		return getLabelModel().getText();
	}
	@Override
	public void setText(String newValue) {
		 getLabelModel().setText(newValue);
		 
	}
	@Override
	public String getImageFileName() {
		return getLabelModel().getImageFileName();
	}
	@Override
	public void setImageFileName(String iconFile) {
		
		getLabelModel().setImageFileName(iconFile);
	}
	shapes.LabelModel getLabelModel() {
		return (shapes.LabelModel) shapeModel;
	}
//public void copy (BoundedShape aReference) {
//		
////		shapeModel.copy(((ALabelModel) aReference).shapeModel);
//	  shapeModel.copy(aReference);
//
////		((LabelModel) shapeModel).copy((LabelModel) ((ALabelModel) aReference).shapeModel);
//
//	}
	
	public javax.swing.JLabel getLabel() {
		return getLabelModel().getLabel();
	}
	AttributedString attributedString;
	public AttributedString getAttributedString() {
		return attributedString;
	}
	public void setAttributedString(AttributedString attributedString) {
		this.attributedString = attributedString;
	}
	public static void main (String[] args) {
		ALabelModel model = new ALabelModel("foo");
		model.setBounds(new Rectangle(30, 30, 40, 40));
		model.setText("Hiiiiiii");
		model.setColor(Color.BLUE);
		ObjectEditor.edit(model);
		model.setText("Byeee");
		model.setColor(Color.BLACK);		
	}

}
