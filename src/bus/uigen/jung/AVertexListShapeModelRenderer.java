package bus.uigen.jung;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class AVertexListShapeModelRenderer<V,E> extends BasicVertexRenderer<V, E>{

	public AVertexListShapeModelRenderer() {
		super();
	}
	protected void paintShapeForVertex(RenderContext<V,E> rc, V v, Shape shape) {
		super.paintShapeForVertex(rc, v, shape);
//        GraphicsDecorator g = rc.getGraphicsContext();
//        Paint oldPaint = g.getPaint();
//        Paint fillPaint = rc.getVertexFillPaintTransformer().transform(v);
//        if(fillPaint != null) {
//            g.setPaint(fillPaint);
//            g.fill(shape);
//            g.setPaint(oldPaint);
//        }
//        Paint drawPaint = rc.getVertexDrawPaintTransformer().transform(v);
//        if(drawPaint != null) {
//        	g.setPaint(drawPaint);
//        	Stroke oldStroke = g.getStroke();
//        	Stroke stroke = rc.getVertexStrokeTransformer().transform(v);
//        	if(stroke != null) {
//        		g.setStroke(stroke);
//        	}
//        	g.draw(shape);
//        	g.setPaint(oldPaint);
//        	g.setStroke(oldStroke);
//        }
    }

}
