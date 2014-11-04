package bus.uigen.jung;

import java.util.List;
import java.util.Observer;

import bus.uigen.shapes.OEShapeModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public interface JungShapeModelDisplayer extends VisualizationViewer.Paintable, Observer{
	public List<OEShapeModel> getShapes() ;

}
