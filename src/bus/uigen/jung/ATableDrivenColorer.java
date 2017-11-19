package bus.uigen.jung;

import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus.uigen.oadapters.ObjectAdapter;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class ATableDrivenColorer<ElementType> implements TableDrivenColorer<ElementType> {
	
	Map<ElementType, Paint> vertexToColor = new HashMap();
	static Paint defaultColor = Color.MAGENTA;
	static Paint selectedColor = Color.CYAN;
	static Paint graphColor = defaultColor;
	JungGraphManager jungGraphManager;
	public ATableDrivenColorer(JungGraphManager aJungGraphManager) {
		jungGraphManager = aJungGraphManager;
	}
	protected Paint defaultColor(ElementType input) {
		return defaultColor;
	}
	
	protected Paint selectedColor(ElementType input) {
		return selectedColor;
		
	}
	
	protected Paint graphColor(ElementType input) {
		return Color.MAGENTA;
		
	}

	@Override
	public Paint transform(ElementType input) {
		
		PickedState aState = jungGraphManager.getVisualizationViewer().getPickedVertexState();
		if (aState.isPicked(input) ) {
			return selectedColor(input);
		}
		if (input instanceof Graph) {
			return graphColor(input);
		}
		Paint retVal= vertexToColor.get(input);
//		if (retVal == null) return defaultColor;
		if (retVal == null) {			
			return defaultColor(input);		
		}
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
