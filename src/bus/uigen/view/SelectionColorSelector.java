package bus.uigen.view;import java.awt.Color;import bus.uigen.ars.*;import bus.uigen.attributes.AttributeNames;public class SelectionColorSelector  {	static Color selectionColor = AttributeNames.DEFAULT_SELECTION_COLOR;
	  public static void setColor(Color newVal) {
		selectionColor = newVal;  }  public static  Color getColor() {	  return selectionColor;
	    }
}