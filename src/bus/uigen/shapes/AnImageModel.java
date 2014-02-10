package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import shapes.ImageShape;
import shapes.LabelModel;
import util.annotations.Explanation;
import util.misc.ThreadSupport;
import util.models.AListenableVector;
import bus.uigen.ObjectEditor;

//import shapes.shapes.LabelModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.IMAGE_PATTERN)
@Explanation("An Image with all the operations you will ever need")
public class AnImageModel extends AShapeModel implements ImageShape {
	
	
	public AnImageModel (String theImageFile)
    {
		try {
			 shapeModel = new shapes.ImageModel(theImageFile);
			 init();
		//super(createjavax.swing.JTextField(s), new Rectangle(0, 0, 7*s.length(), 20));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public AnImageModel (String imageFile, int x, int y)
    {
		try {
        shapeModel = new shapes.ImageModel(imageFile, x, y);       
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public AnImageModel (String imageFile, int x, int y, int width, int height)
    {
		try {
        shapeModel = new shapes.ImageModel(imageFile, x, y, width, height);       
        init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	
	@Override
	public String getImageFileName() {
		return getImageModel().getImageFileName();
	}
	@Override
	public void setImageFileName(String iconFile) {
		
		getImageModel().setImageFileName(iconFile);
	}
	shapes.ImageModel getImageModel() {
		return (shapes.ImageModel) shapeModel;
	}
public void copy (Object aReference) {
		
		shapeModel.copy(((AnImageModel) aReference).shapeModel);
	}
	
//	
//	public static void main (String[] args) {
//		AnImageModel model = new AnImageModel("foo");
//		model.setBounds(new Rectangle(30, 30, 40, 40));
//		model.setText("Hiiiiiii");
//		model.setColor(Color.BLUE);
//		ObjectEditor.edit(model);
//		model.setText("Byeee");
//		model.setColor(Color.BLACK);		
//	}
    public static void main (String[] args) {
    	ImageShape image = new AnImageModel("holygrail2.PNG", 0, 0, 30, 50);
    	ObjectEditor.edit(image);
    	ThreadSupport.sleep(2000);
    	image.setHeight(100);
    }
}
