package bus.uigen.jung;

import edu.uci.ics.jung.visualization.RenderContext;

public interface RenderContextTrapper<V,E> extends RenderContext<V,E> {
	 void setRenderContext(RenderContext<V,E> newVal);
	 RenderContext<V,E> getRenderContext();	

}
