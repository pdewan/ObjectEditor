package bus.uigen.shapes;

import shapes.BoundedShape;
import util.annotations.Visible;
import util.misc.ThreadSupport;
import util.models.AListenableVector;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
//import collections.StringInShapes;

public class AStringInShapes extends AnAddressSpaceTransformingListenableShapeVector implements StringInShapes {
	int x, y;
	public static final int INITIAL_SIZE = 2;
	String string;
	public static final CharacterInAShape reference = 
			new ACharacterInAShape('c', 0, 0);
	public AStringInShapes (int anX, int aY) {
		super (INITIAL_SIZE, reference);
		x = anX;
		y = aY;
	}
	public void setString(String aString) {
		clear();
		for (int i = 0; i < aString.length(); i++) {
			CharacterInAShape shape = new ACharacterInAShape(aString.charAt(i), x + i*ACharacterInAShape.SHAPE_WIDTH, y); 
			add (shape);
		}	
		string = aString;
	}
	@Visible(false)
	public String getString() {
		return string;
	}
	
	public void removeChar(int i) {
		super.removeElementAt(i);
	}
	
	public static void main (String[] args) {
		StringInShapes stringInShapes = new AStringInShapes(150, 150);
		stringInShapes.setString("hello");
//		OEFrame frame = ObjectEditor.drawEdit(stringInShapes);
		OEFrame frame = ObjectEditor.graphicsOnlyEdit(stringInShapes);
		for (int i = stringInShapes.getString().length() -1; i >=0; i--) {
			stringInShapes.removeElementAt(i);
		    ThreadSupport.sleep(1000);		
		}
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) 
		        stringInShapes.setString("gooodbye");
			else
				stringInShapes.setString("hello");
		    ThreadSupport.sleep(1000);
		
		}
		for (int i = 0; i < stringInShapes.getString().length();  i++) {
//			stringInShapes.removeElementAt(i);
			stringInShapes.removeElement(stringInShapes.get(0));

		    ThreadSupport.sleep(1000);		
		}
		stringInShapes.setString("hello");
		for (int i = 0; i <  stringInShapes.getString().length()*2;  i = i +2) {
			
			BoundedShape newVal = 			new ACharacterInAShape((char) ('I' + i), 250 + i*ACharacterInAShape.SHAPE_WIDTH, 150); 

			stringInShapes.insertElementAt(newVal, i);
		}
		
		for (int i=0; i < stringInShapes.size(); i++) {
			ACharacterInAShape shape = (ACharacterInAShape) stringInShapes.get(i);
			System.out.println(shape.displayedChar.getText());
		}
		
		
		
	}

		

}
