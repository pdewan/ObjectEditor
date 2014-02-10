package bus.uigen.test;
import shapes.FlexibleLineShape;
import util.annotations.Visible;
import bus.uigen.ObjectEditor;
import bus.uigen.shapes.ALineModel;



public class AVisibleTester {
	FlexibleLineShape lineModel = new ALineModel(0, 0, 100, 100);
//	@Visible(false)
	public FlexibleLineShape getLine() {
		return lineModel;
	}
	@Visible(false)
	public void setLine(FlexibleLineShape newVal) {
		lineModel = newVal;		
	}
	
	public static void main(String[] args) {
		ObjectEditor.edit(new AVisibleTester());
	}

}
