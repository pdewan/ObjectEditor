package bus.uigen.shapes;

import shapes.FlexibleLineShape;
import bus.uigen.ObjectEditor;

public class ALineWithAnArrow implements LineWithAnArrow {
	Fork fork;
	FlexibleLineShape line;
	public static final int ARROW_LENGTH = 10;
	public ALineWithAnArrow(int anX, int aY, int aLength) {
		fork = new AFork(anX, aY, ARROW_LENGTH, Math.PI*3/4, Math.PI/4);
		line = new ALineModel(anX, aY, aLength, Math.PI/2);
	}
	
	public FlexibleLineShape getLine() {
		return line;
	}
	public Fork getFork() {
		return fork;
	}
	public void setX(int newVal) {
		fork.setX(newVal);
		line.setX(newVal);
	}
	public void setY (int newVal) {
		fork.setY(newVal);
		line.setY(newVal);
	}
	public static void main (String[] args) {
		LineWithAnArrow lineWithAnArrow = new ALineWithAnArrow(50, 50, 20);
		ObjectEditor.graphicsOnlyEdit(lineWithAnArrow);		
		
	}

}
