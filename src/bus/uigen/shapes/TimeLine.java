package bus.uigen.shapes;

import shapes.FlexibleShape;
import shapes.LabelShape;
import util.models.AListenableVector;

public interface TimeLine {

	public int getScale();

	public void setScale(int scale);

	public String getName();

	public void setName(String name);

	public int getLowX();

	public void setLowX(int lowX);

	public void addSegment(int start, int end);

	public int getY();

	public void setY(int y);

	public LabelShape getNameLabel();

	public AListenableVector<FlexibleShape> getLines();

	void moveLowX(int increment);

	void moveY(int increment);

	int getMaxTime();

}