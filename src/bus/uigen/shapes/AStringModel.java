package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.rmi.RemoteException;
import java.text.AttributedString;
import java.util.List;

import shapes.FlexibleTextShape;
import shapes.StringModel;
import shapes.TextModel;
import shapes.TextShape;
import util.annotations.Explanation;
import util.misc.ThreadSupport;
import util.models.AListenableVector;
import bus.uigen.ObjectEditor;

//import shapes.shapes.StringModel;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.STRING_PATTERN)

	@Explanation("Predefined String Shape")
	public class AStringModel extends AShapeModel implements FlexibleTextShape {
	protected AttributedString attributedString;
		
		public AStringModel (Rectangle theBounds)
	    {
			try {
	        shapeModel = new shapes.StringModel(theBounds);
	        init();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		public AStringModel (String s)
	    {
			try {
				 shapeModel = new shapes.StringModel(s);
				 init();
			//super(createjavax.swing.JTextField(s), new Rectangle(0, 0, 7*s.length(), 20));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		public AStringModel (String s, int x, int y, int width, int height)
	    {
			try {
	        shapeModel = new shapes.StringModel (x, y, width, height, s);
	        init();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		public AStringModel (String s, int x, int y)
	    {
			try {
//	        shapeModel = new shapes.StringModel (x, y, 0, 0, s);
	        shapeModel = new shapes.StringModel (s, x, y);
	        init();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		public AStringModel ()
	    {
			try {
	        shapeModel = new shapes.StringModel();
	        init();
	    }catch (Exception e) {
			e.printStackTrace();
		}
	    }
		
		shapes.StringModel getStringModel() {
			return (shapes.StringModel) shapeModel;
		}
		@Override
		public String getText() {
			try {
			return getStringModel().getText();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public void setText(String newValue) {
			try {
			getStringModel().setText(newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	
	@Override
	public AttributedString getAttributedString() {
		return attributedString;
	}
	@Override
	public void setAttributedString(AttributedString newVal) {
		attributedString = newVal;
	}	
//public void copy (Object aReference) {
//		
////		shapeModel.copy(((AStringModel) aReference).shapeModel);
//		((StringModel) shapeModel).copy((StringModel) ((AStringModel) aReference).shapeModel);
//	}
	public static void main (String[] args) {		
		
		String s = "hello";
		FlexibleTextShape stringModel = new AStringModel(s, 50, 100, 21, -1);
		AttributedString attributedString = new AttributedString(s);
		attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		stringModel.setAttributedString(attributedString);
		ObjectEditor.edit(stringModel);
		FlexibleTextShape stringModel2 = new AStringModel(s, 50, 100, 21, -1);
		ObjectEditor.edit(stringModel2);
		ThreadSupport.sleep(2000);
		stringModel2.setColor(Color.GREEN);
//		ObjectEditor.edit(new AStringModel("hello", 50, 100));

//		List list = new AListenableVector();
//		int maxElements = 100;
//		for (int i = 0; i < maxElements; i++) {
//			list.add(new AStringModel("" + i, i*2, i*2));
//		}
//		ObjectEditor.edit(list);
//		
	}

}
