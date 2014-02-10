package bus.uigen.jung;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class AnEdgeRendererTrapper<V, E>  implements Renderer<V,E>{
	Renderer<V,E> delegate;

	public void render(RenderContext<V, E> rc, Layout<V, E> layout) {
		delegate.render(rc, layout);
	}

	public void renderVertex(RenderContext<V, E> rc, Layout<V, E> layout, V v) {
		delegate.renderVertex(rc, layout, v);
	}

	public void renderVertexLabel(RenderContext<V, E> rc, Layout<V, E> layout,
			V v) {
		delegate.renderVertexLabel(rc, layout, v);
	}

	public void renderEdge(RenderContext<V, E> rc, Layout<V, E> layout, E e) {
		delegate.renderEdge(rc, layout, e);
	}

	public void renderEdgeLabel(RenderContext<V, E> rc, Layout<V, E> layout, E e) {
		delegate.renderEdgeLabel(rc, layout, e);
	}

	public void setVertexRenderer(
			edu.uci.ics.jung.visualization.renderers.Renderer.Vertex<V, E> r) {
		delegate.setVertexRenderer(r);
	}

	public void setEdgeRenderer(
			edu.uci.ics.jung.visualization.renderers.Renderer.Edge<V, E> r) {
		delegate.setEdgeRenderer(r);
	}

	public void setVertexLabelRenderer(
			edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel<V, E> r) {
		delegate.setVertexLabelRenderer(r);
	}

	public void setEdgeLabelRenderer(
			edu.uci.ics.jung.visualization.renderers.Renderer.EdgeLabel<V, E> r) {
		delegate.setEdgeLabelRenderer(r);
	}

	public edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel<V, E> getVertexLabelRenderer() {
		return delegate.getVertexLabelRenderer();
	}

	public edu.uci.ics.jung.visualization.renderers.Renderer.Vertex<V, E> getVertexRenderer() {
		return delegate.getVertexRenderer();
	}

	public edu.uci.ics.jung.visualization.renderers.Renderer.Edge<V, E> getEdgeRenderer() {
		return delegate.getEdgeRenderer();
	}

	public edu.uci.ics.jung.visualization.renderers.Renderer.EdgeLabel<V, E> getEdgeLabelRenderer() {
		return delegate.getEdgeLabelRenderer();
	}
	

}
