package bus.uigen.jung;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATableDrivenColorer<ElementType> implements TableDrivenColorer<ElementType> {
	
	Map<ElementType, Paint> vertexToColor = new HashMap();
	Paint defaultColor = Color.RED;
	public ATableDrivenColorer() {
		
	}

	@Override
	public Paint transform(ElementType input) {
		Paint retVal= vertexToColor.get(input);
		if (retVal == null) return defaultColor;
		return retVal;
	}

	@Override
	public void setColor(ElementType anElement, Paint aPaint) {
		vertexToColor.put(anElement, aPaint);
		
	}

	@Override
	public Paint getColor(ElementType anElement) {
		return vertexToColor.get(anElement);
	}

	@Override
	public void setDefaultColor(Paint aPaint) {
		defaultColor = aPaint;
	}

	@Override
	public Paint getDefaultColor() {
		return defaultColor;
	}

}
