package bus.uigen.jung;

import java.awt.Color;
import java.awt.Container;
import java.awt.Paint;
import java.awt.Shape;
import java.util.List;

import javax.swing.Icon;

import org.apache.commons.collections15.Transformer;

import bus.uigen.CompleteOEFrame;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer.Vertex;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel;
/*
 * In progress
 */
public class ADelegatingJungGraphManager<VertexType,EdgeType> extends AMonolithicJungGraphManager<VertexType, EdgeType>
	implements JungGraphManager<VertexType, EdgeType> {
	JungGraphManager<VertexType, EdgeType> delegate;
	public ADelegatingJungGraphManager(
			JungGraphManager<VertexType, EdgeType> aJungGraphManager,
			Graph<VertexType, EdgeType> aGraph,
			Container aGraphContainer) {
		super(aGraph, aGraphContainer);
		delegate = aJungGraphManager;
	}
	public ADelegatingJungGraphManager(
			Graph<VertexType, EdgeType> aGraph,
			Container aGraphContainer) {
		super(aGraph, aGraphContainer);
	}
	public Container getGraphContainer() {
		return delegate.getGraphContainer();
	}

	public Layout<VertexType, EdgeType> getGraphLayout() {
		return delegate.getGraphLayout();
	}

	public void setLayout(Layout<VertexType, EdgeType> layout) {
		delegate.setLayout(layout);
	}

	public void setGraph(Graph<VertexType, EdgeType> graph) {
		delegate.setGraph(graph);
	}

	public Transformer<VertexType, String> getVertexLabelTransformer() {
		return delegate.getVertexLabelTransformer();
	}

	public void setVertexLabelTransformer(
			Transformer<VertexType, String> vertexLabelTransformer) {
		delegate.setVertexLabelTransformer(vertexLabelTransformer);
	}

	public Transformer<VertexType, String> getVertexToolTipTransformer() {
		return delegate.getVertexToolTipTransformer();
	}

	public void setVertexToolTipTransformer(
			Transformer<VertexType, String> vertexToolTipTransformer) {
		delegate.setVertexToolTipTransformer(vertexToolTipTransformer);
	}

	public Transformer<EdgeType, String> getEdgeToolTipTransformer() {
		return delegate.getEdgeToolTipTransformer();
	}

	public void setEdgeToolTipTransformer(
			Transformer<EdgeType, String> edgeToolTipTransformer) {
		delegate.setEdgeToolTipTransformer(edgeToolTipTransformer);
	}

	public Transformer<EdgeType, String> getEdgeLabelTransformer() {
		return delegate.getEdgeLabelTransformer();
	}

	public void setEdgeLabelTransformer(
			Transformer<EdgeType, String> edgeLabelTransformer) {
		delegate.setEdgeLabelTransformer(edgeLabelTransformer);
	}

	public void init() {
		delegate.init();
	}

	public void addAndDisplayVertex(VertexType aVertex) {
		delegate.addAndDisplayVertex(aVertex);
	}

	public void addAndDisplayEdge(EdgeType anEdge, VertexType aVertex1,
			VertexType aVertex2) {
		delegate.addAndDisplayEdge(anEdge, aVertex1, aVertex2);
	}

	public void removeAndUndisplayVertex(VertexType aVertex) {
		delegate.removeAndUndisplayVertex(aVertex);
	}

	public void removeAndDisplayEdge(EdgeType anEdge) {
		delegate.removeAndDisplayEdge(anEdge);
	}

	public void addAndLayoutVertex(VertexType aVertex) {
		delegate.addAndLayoutVertex(aVertex);
	}

	public void setPostRenderer(Paintable newVal) {
		delegate.setPostRenderer(newVal);
	}

	public VisualizationViewer<VertexType, EdgeType> getVisualizationViewer() {
		return delegate.getVisualizationViewer();
	}

	public void setVisualizationViewer(
			VisualizationViewer<VertexType, EdgeType> newVal) {
		delegate.setVisualizationViewer(newVal);
	}

	public void setVertexIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, VertexType> newVal) {
		delegate.setVertexIncludePredicate(newVal);
	}

	public void setEdgeIncludePredicate(
			TableBasedGraphElementInclusionPredicate<VertexType, EdgeType, EdgeType> newVal) {
		delegate.setEdgeIncludePredicate(newVal);
	}

	public void setVertexVisibile(VertexType aVertex, boolean newVal) {
		delegate.setVertexVisibile(aVertex, newVal);
	}

	public boolean getVertexVisibile(VertexType aVertex) {
		return delegate.getVertexVisibile(aVertex);
	}

	public void setEdgeVisibile(EdgeType aVertex, boolean newVal) {
		delegate.setEdgeVisibile(aVertex, newVal);
	}

	public boolean getEdgeVisibile(EdgeType aVertex) {
		return delegate.getEdgeVisibile(aVertex);
	}

	public LayoutType getLayoutType() {
		return delegate.getLayoutType();
	}

	public void setLayoutType(LayoutType newVal) {
		delegate.setLayoutType(newVal);
	}

	public JungShapeModelDisplayer getJungShapeModelDisplayer() {
		return delegate.getJungShapeModelDisplayer();
	}

	public Transformer<VertexType, Shape> getVertexShapeTransformer() {
		return delegate.getVertexShapeTransformer();
	}

	public Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> getEdgeShapeTransformer() {
		return delegate.getEdgeShapeTransformer();
	}

	public void setEdgeShapeTransformer(
			Transformer<Context<Graph<VertexType, EdgeType>, EdgeType>, Shape> newVal) {
		delegate.setEdgeShapeTransformer(newVal);
	}

	public void setVertexShapeTransformer(Transformer<VertexType, Shape> newVal) {
		delegate.setVertexShapeTransformer(newVal);
	}

	public void setVertexColors(VertexType aVertex, List<Color> aColors) {
		delegate.setVertexColors(aVertex, aColors);
	}

	public List<Color> getVertexColors(VertexType aVertex) {
		return delegate.getVertexColors(aVertex);
	}

	public VertexLabel<VertexType, EdgeType> getVertexLabelRenderer() {
		return delegate.getVertexLabelRenderer();
	}

	public void setVertexLabelRenderer(VertexLabel<VertexType, EdgeType> newVal) {
		delegate.setVertexLabelRenderer(newVal);
	}

	public void setVertexFillColor(VertexType aVertex, Paint aColor) {
		delegate.setVertexFillColor(aVertex, aColor);
	}

	public Paint getVertexFillColor(VertexType aVertex) {
		return delegate.getVertexFillColor(aVertex);
	}

	public void setVertexDrawColor(VertexType aVertex, Paint aColor) {
		delegate.setVertexDrawColor(aVertex, aColor);
	}

	public Paint getVertexDrawColor(VertexType aVertex) {
		return delegate.getVertexDrawColor(aVertex);
	}

	public void setEdgeDrawColor(EdgeType anEdge, Paint aColor) {
		delegate.setEdgeDrawColor(anEdge, aColor);
	}

	public Paint getEdgeDrawColor(EdgeType anEdge) {
		return delegate.getEdgeDrawColor(anEdge);
	}

	public void setVertexFillPaintTransformer(
			Transformer<VertexType, Paint> newVal) {
		delegate.setVertexFillPaintTransformer(newVal);
	}

	public void setEdgeDrawPaintTransformer(Transformer<EdgeType, Paint> newVal) {
		delegate.setEdgeDrawPaintTransformer(newVal);
	}

	public void setVertexDrawPaintTransformer(
			Transformer<VertexType, Paint> newVal) {
		delegate.setVertexDrawPaintTransformer(newVal);
	}

	public Transformer<VertexType, Paint> getVertexDrawPaintTransformer() {
		return delegate.getVertexDrawPaintTransformer();
	}

	public Transformer<VertexType, Paint> getVertexFillPaintTransformer() {
		return delegate.getVertexFillPaintTransformer();
	}

	public Transformer<VertexType, Icon> getVertexIconTransformer() {
		return delegate.getVertexIconTransformer();
	}

	public Vertex<VertexType, EdgeType> getVertexRenderer() {
		return delegate.getVertexRenderer();
	}

	public void setVertexRenderer(Vertex<VertexType, EdgeType> newVal) {
		delegate.setVertexRenderer(newVal);
	}

	public Transformer<EdgeType, Paint> getEdgeDrawPaintTransformer() {
		return delegate.getEdgeDrawPaintTransformer();
	}

	public CompleteOEFrame getOEFrame() {
		return delegate.getOEFrame();
	}

	public TableDrivenColorer<VertexType> getVertexFillColorer() {
		return delegate.getVertexFillColorer();
	}

	public void setVertexFillColorer(
			TableDrivenColorer<VertexType> vertexFillColorer) {
		delegate.setVertexFillColorer(vertexFillColorer);
	}

	public TableDrivenColorer<VertexType> getVertexDrawColorer() {
		return delegate.getVertexDrawColorer();
	}

	public void setVertexDrawColorer(
			TableDrivenColorer<VertexType> vertexDrawColorer) {
		delegate.setVertexDrawColorer(vertexDrawColorer);
	}

	
	
}
