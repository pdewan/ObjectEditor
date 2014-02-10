package bus.uigen.shapes;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Stroke;

import shapes.BoundedShape;
import shapes.FlexibleLineShape;
import util.annotations.Visible;
import util.misc.ThreadSupport;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class AFork implements Fork {
	int x = 0;
	int y = 0;
	FlexibleLineShape leftLine;
	FlexibleLineShape rightLine;
	public AFork (int anX, int anY, int aLength, double leftLineAngle, double rightLineAngle) {
		x = anX;
		y = anY;
		leftLine = new ALineModel(x, y, aLength, leftLineAngle);
		rightLine = new ALineModel(x, y, aLength, rightLineAngle);		
	}
	
	public FlexibleLineShape getLeftLine() {
		return leftLine;
	}
	
	public FlexibleLineShape getRightLine() {
		return rightLine;
	}
	
	public void setX(int newVal) {
		leftLine.setX(newVal);
		rightLine.setX(newVal);
	}
	public void setY(int newVal) {
		leftLine.setY(newVal);
		rightLine.setY(newVal);
	}
	@Visible(false)
	public int getX() {
		return leftLine.getX();
	}
	@Visible(false)
	public int getY() {
		return leftLine.getY();
	}
	public void setMagnification(double newVal) {
		leftLine.setMagnification(newVal);
		rightLine.setMagnification(newVal);
	}
	@Visible(false)
	public double getMagnification() {
		return leftLine.getMagnification();
	}
	
//	public void moveX (int distance) {
//		leftLine.moveX(distance);
//		rightLine.moveX(distance);
//		
//	}


//	@Override
//	public void resetForm() {
//		leftLine.resetForm();
//		rightLine.resetForm();		
//	}
	
	@Override
	public void rotate(int units) {
		leftLine.rotate(units);
		rightLine.rotate(units);		
	}
	
//	@Visible(false)
//	@Override
//	public Color getColor() {
//		return leftLine.getColor();
//	}
//
//	@Override
//	public void setColor(Color newVal) {
//		leftLine.setColor(newVal);
//		rightLine.setColor(newVal);		
//	}


	
	@Override
	public void setWidth(int width) {
		
	}

	@Override
	public void setHeight(int height) {
		
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBounds(Rectangle newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean copy(BoundedShape aReference) {
		if (!copyable(aReference)) return false;
		leftLine.copy(((Fork) aReference).getLeftLine());
		rightLine.copy(((Fork) aReference).getRightLine());

		return true;
	}

	@Override
	public void rotate(double angle) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		Fork fork = new AFork(150, 150, 10, Math.PI/4, Math.PI * 3/4);
		fork.getLeftLine().setColor(Color.BLUE);
		fork.getRightLine().setColor(Color.RED);
		OEFrame frame = ObjectEditor.edit(fork);
		boolean rotateLeftLine = false;
		while (true) {
			rotateLeftLine = !rotateLeftLine;
			for (int i = 0; i < 10; i++) {
			ThreadSupport.sleep(100);
			if (rotateLeftLine) 
				fork.getLeftLine().rotate(2);
			else
				fork.getRightLine().rotate(2);
			fork.setMagnification(fork.getMagnification()*1.2);
//			frame.refresh();
			}
//			fork.resetForm();
		}
	}

	@Override
	public boolean copyable(BoundedShape aReference) {
		// TODO Auto-generated method stub
		return aReference instanceof Fork;
	}

	@Override
	public double getAngle() {
		return leftLine.getAngle();
	}

	@Override
	public void setAngle(double newVal) {
		double oldAngle = getAngle();
		if (oldAngle == newVal) return;
		double rotateAmount = newVal - oldAngle;
		rotate(rotateAmount);
		
	}


}
