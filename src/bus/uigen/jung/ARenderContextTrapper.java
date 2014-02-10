package bus.uigen.jung;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeIndexFunction;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class ARenderContextTrapper<V,E> implements RenderContextTrapper<V,E>{
	RenderContext<V,E> delegate;	
	

	@Override
	public void setRenderContext(RenderContext<V, E> newVal) {
		delegate = newVal;		
	}

	@Override
	public RenderContext<V, E> getRenderContext() {
		return delegate;
	}
	
	

	public int getLabelOffset() {
		return delegate.getLabelOffset();
	}

	public void setLabelOffset(int labelOffset) {
		delegate.setLabelOffset(labelOffset);
	}

	public float getArrowPlacementTolerance() {
		return delegate.getArrowPlacementTolerance();
	}

	public void setArrowPlacementTolerance(float arrow_placement_tolerance) {
		delegate.setArrowPlacementTolerance(arrow_placement_tolerance);
	}

	public Transformer<Context<Graph<V, E>, E>, Shape> getEdgeArrowTransformer() {
		return delegate.getEdgeArrowTransformer();
	}

	public void setEdgeArrowTransformer(
			Transformer<Context<Graph<V, E>, E>, Shape> edgeArrowTransformer) {
		delegate.setEdgeArrowTransformer(edgeArrowTransformer);
	}

	public Predicate<Context<Graph<V, E>, E>> getEdgeArrowPredicate() {
		return delegate.getEdgeArrowPredicate();
	}

	public void setEdgeArrowPredicate(
			Predicate<Context<Graph<V, E>, E>> edgeArrowPredicate) {
		delegate.setEdgeArrowPredicate(edgeArrowPredicate);
	}

	public Transformer<E, Font> getEdgeFontTransformer() {
		return delegate.getEdgeFontTransformer();
	}

	public void setEdgeFontTransformer(Transformer<E, Font> edgeFontTransformer) {
		delegate.setEdgeFontTransformer(edgeFontTransformer);
	}

	public Predicate<Context<Graph<V, E>, E>> getEdgeIncludePredicate() {
		return delegate.getEdgeIncludePredicate();
	}

	public void setEdgeIncludePredicate(
			Predicate<Context<Graph<V, E>, E>> edgeIncludePredicate) {
		delegate.setEdgeIncludePredicate(edgeIncludePredicate);
	}

	public Transformer<Context<Graph<V, E>, E>, Number> getEdgeLabelClosenessTransformer() {
		return delegate.getEdgeLabelClosenessTransformer();
	}

	public void setEdgeLabelClosenessTransformer(
			Transformer<Context<Graph<V, E>, E>, Number> edgeLabelClosenessTransformer) {
		delegate.setEdgeLabelClosenessTransformer(edgeLabelClosenessTransformer);
	}

	public EdgeLabelRenderer getEdgeLabelRenderer() {
		return delegate.getEdgeLabelRenderer();
	}

	public void setEdgeLabelRenderer(EdgeLabelRenderer edgeLabelRenderer) {
		delegate.setEdgeLabelRenderer(edgeLabelRenderer);
	}

	public Transformer<E, Paint> getEdgeFillPaintTransformer() {
		return delegate.getEdgeFillPaintTransformer();
	}

	public void setEdgeFillPaintTransformer(
			Transformer<E, Paint> edgePaintTransformer) {
		delegate.setEdgeFillPaintTransformer(edgePaintTransformer);
	}

	public Transformer<E, Paint> getEdgeDrawPaintTransformer() {
		return delegate.getEdgeDrawPaintTransformer();
	}

	public void setEdgeDrawPaintTransformer(
			Transformer<E, Paint> edgeDrawPaintTransformer) {
		delegate.setEdgeDrawPaintTransformer(edgeDrawPaintTransformer);
	}

	public Transformer<E, Paint> getArrowDrawPaintTransformer() {
		return delegate.getArrowDrawPaintTransformer();
	}

	public void setArrowDrawPaintTransformer(
			Transformer<E, Paint> arrowDrawPaintTransformer) {
		delegate.setArrowDrawPaintTransformer(arrowDrawPaintTransformer);
	}

	public Transformer<E, Paint> getArrowFillPaintTransformer() {
		return delegate.getArrowFillPaintTransformer();
	}

	public void setArrowFillPaintTransformer(
			Transformer<E, Paint> arrowFillPaintTransformer) {
		delegate.setArrowFillPaintTransformer(arrowFillPaintTransformer);
	}

	public Transformer<Context<Graph<V, E>, E>, Shape> getEdgeShapeTransformer() {
		return delegate.getEdgeShapeTransformer();
	}

	public void setEdgeShapeTransformer(
			Transformer<Context<Graph<V, E>, E>, Shape> edgeShapeTransformer) {
		delegate.setEdgeShapeTransformer(edgeShapeTransformer);
	}

	public Transformer<E, String> getEdgeLabelTransformer() {
		return delegate.getEdgeLabelTransformer();
	}

	public void setEdgeLabelTransformer(Transformer<E, String> edgeStringer) {
		delegate.setEdgeLabelTransformer(edgeStringer);
	}

	public Transformer<E, Stroke> getEdgeStrokeTransformer() {
		return delegate.getEdgeStrokeTransformer();
	}

	public void setEdgeStrokeTransformer(
			Transformer<E, Stroke> edgeStrokeTransformer) {
		delegate.setEdgeStrokeTransformer(edgeStrokeTransformer);
	}

	public Transformer<E, Stroke> getEdgeArrowStrokeTransformer() {
		return delegate.getEdgeArrowStrokeTransformer();
	}

	public void setEdgeArrowStrokeTransformer(
			Transformer<E, Stroke> edgeArrowStrokeTransformer) {
		delegate.setEdgeArrowStrokeTransformer(edgeArrowStrokeTransformer);
	}

	public GraphicsDecorator getGraphicsContext() {
		return delegate.getGraphicsContext();
	}

	public void setGraphicsContext(GraphicsDecorator graphicsContext) {
		delegate.setGraphicsContext(graphicsContext);
	}

	public EdgeIndexFunction<V, E> getParallelEdgeIndexFunction() {
		return delegate.getParallelEdgeIndexFunction();
	}

	public void setParallelEdgeIndexFunction(
			EdgeIndexFunction<V, E> parallelEdgeIndexFunction) {
		delegate.setParallelEdgeIndexFunction(parallelEdgeIndexFunction);
	}

	public PickedState<E> getPickedEdgeState() {
		return delegate.getPickedEdgeState();
	}

	public void setPickedEdgeState(PickedState<E> pickedEdgeState) {
		delegate.setPickedEdgeState(pickedEdgeState);
	}

	public PickedState<V> getPickedVertexState() {
		return delegate.getPickedVertexState();
	}

	public void setPickedVertexState(PickedState<V> pickedVertexState) {
		delegate.setPickedVertexState(pickedVertexState);
	}

	public CellRendererPane getRendererPane() {
		return delegate.getRendererPane();
	}

	public void setRendererPane(CellRendererPane rendererPane) {
		delegate.setRendererPane(rendererPane);
	}

	public JComponent getScreenDevice() {
		return delegate.getScreenDevice();
	}

	public void setScreenDevice(JComponent screenDevice) {
		delegate.setScreenDevice(screenDevice);
	}

	public Transformer<V, Font> getVertexFontTransformer() {
		return delegate.getVertexFontTransformer();
	}

	public void setVertexFontTransformer(
			Transformer<V, Font> vertexFontTransformer) {
		delegate.setVertexFontTransformer(vertexFontTransformer);
	}

	public Transformer<V, Icon> getVertexIconTransformer() {
		return delegate.getVertexIconTransformer();
	}

	public void setVertexIconTransformer(
			Transformer<V, Icon> vertexIconTransformer) {
		delegate.setVertexIconTransformer(vertexIconTransformer);
	}

	public Predicate<Context<Graph<V, E>, V>> getVertexIncludePredicate() {
		return delegate.getVertexIncludePredicate();
	}

	public void setVertexIncludePredicate(
			Predicate<Context<Graph<V, E>, V>> vertexIncludePredicate) {
		delegate.setVertexIncludePredicate(vertexIncludePredicate);
	}

	public VertexLabelRenderer getVertexLabelRenderer() {
		return delegate.getVertexLabelRenderer();
	}

	public void setVertexLabelRenderer(VertexLabelRenderer vertexLabelRenderer) {
		delegate.setVertexLabelRenderer(vertexLabelRenderer);
	}

	public Transformer<V, Paint> getVertexFillPaintTransformer() {
		return delegate.getVertexFillPaintTransformer();
	}

	public void setVertexFillPaintTransformer(
			Transformer<V, Paint> vertexFillPaintTransformer) {
		delegate.setVertexFillPaintTransformer(vertexFillPaintTransformer);
	}

	public Transformer<V, Paint> getVertexDrawPaintTransformer() {
		return delegate.getVertexDrawPaintTransformer();
	}

	public void setVertexDrawPaintTransformer(
			Transformer<V, Paint> vertexDrawPaintTransformer) {
		delegate.setVertexDrawPaintTransformer(vertexDrawPaintTransformer);
	}

	public Transformer<V, Shape> getVertexShapeTransformer() {
		return delegate.getVertexShapeTransformer();
	}

	public void setVertexShapeTransformer(
			Transformer<V, Shape> vertexShapeTransformer) {
		delegate.setVertexShapeTransformer(vertexShapeTransformer);
	}

	public Transformer<V, String> getVertexLabelTransformer() {
		return delegate.getVertexLabelTransformer();
	}

	public void setVertexLabelTransformer(Transformer<V, String> vertexStringer) {
		delegate.setVertexLabelTransformer(vertexStringer);
	}

	public Transformer<V, Stroke> getVertexStrokeTransformer() {
		return delegate.getVertexStrokeTransformer();
	}

	public void setVertexStrokeTransformer(
			Transformer<V, Stroke> vertexStrokeTransformer) {
		delegate.setVertexStrokeTransformer(vertexStrokeTransformer);
	}

	public MultiLayerTransformer getMultiLayerTransformer() {
		return delegate.getMultiLayerTransformer();
	}

	public void setMultiLayerTransformer(MultiLayerTransformer basicTransformer) {
		delegate.setMultiLayerTransformer(basicTransformer);
	}

	public GraphElementAccessor<V, E> getPickSupport() {
		return delegate.getPickSupport();
	}

	public void setPickSupport(GraphElementAccessor<V, E> pickSupport) {
		delegate.setPickSupport(pickSupport);
	}

	

}
