package bus.uigen.jung;

import java.awt.Color;
import java.awt.Container;
import java.awt.Shape;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public interface JungGraphManager<VertexType, EdgeType> {

	//    public static final LengthFunction<Number> UNITLENGTHFUNCTION = new SpringLayout.UnitLengthFunction<Number>(
	//            100);
	public static final int EDGE_LENGTH = 100;

	public Container getGraphContainer();

	public void setGraphContainer(Container graphContainer);

	public Layout<VertexType, EdgeType> getGraphLayout();

	public void setLayout(Layout<VertexType, EdgeType> layout);

	public Graph<VertexType, EdgeType> getGraph();

	public void setGraph(Graph<VertexType, EdgeType> graph);

	public Transformer<VertexType, String> getVertexLabelTransformer();

	public void setVertexLabelTransformer(
			Transformer<VertexType, String> vertexLabelTransformer);

	public Transformer<VertexType, String> getVertexToolTipTransformer();

	public void setVertexToolTipTransformer(
			Transformer<VertexType, String> vertexToolTipTransformer);

	public Transformer<EdgeType, String> getEdgeToolTipTransformer();

	public void setEdgeToolTipTransformer(
			Transformer<EdgeType, String> edgeToolTipTransformer);

	public Transformer<EdgeType, String> getEdgeLabelTransformer();

	public void setEdgeLabelTransformer(
			Transformer<EdgeType, String> edgeLabelTransformer);

	public boolean isForest();

	public void setForest(boolean isForest);

	//    @Override
	public void init();
	
	public void addAndDisplayVertex(VertexType aVertex);
	

	void addAndDisplayEdge(EdgeType anEdge, VertexType aVertex1, VertexType aVertex2);

//	void addChild(EdgeType anEdge, VertexType aParentVertex,
//			VertexType aChildVertex);

	void removeAndUndisplayVertex(VertexType aVertex);

	void removeAndDisplayEdge(EdgeType anEdge);

	void addAndLayoutVertex(VertexType aVertex);

	void renderGraph();

	void setPostRenderer(Paintable newVal);

	VisualizationViewer<VertexType, EdgeType> getVisualizationViewer();

	void setVisualizationViewer(VisualizationViewer<VertexType, EdgeType> newVal);

	void setVertexIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> newVal);

	void setEdgeIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> newVal);

	void setVertexVisibile(VertexType aVertex, boolean newVal);

	void getVertexVisibile(VertexType aVertex);

	void setEdgeVisibile(VertexType aVertex, boolean newVal);

	void getEdgeVisibile(VertexType aVertex);

	LayoutType getLayoutType();

	void setLayoutType(LayoutType newVal);

	JungShapeModelDisplayer getJungShapeModelDisplayer();

	Transformer<VertexType, Shape> getVertexShapeTransformer();

	Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> getEdgeShapeTransformer();

	void setEdgeShapeTransformer(
			Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> newVal);

	void setVertexShapeTransformer(Transformer<VertexType, Shape> newVal);

	void setColors(VertexType aVertex, List<Color> aColors);

	List<Color> getColors(VertexType aVertex);

//	void setRadial(boolean newVal);
//
//	boolean isRadial();

}