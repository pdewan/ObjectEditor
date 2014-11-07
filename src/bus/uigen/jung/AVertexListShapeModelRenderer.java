package bus.uigen.jung;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.Icon;

import shapes.ShapeModel;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class AVertexListShapeModelRenderer<V,E> extends BasicVertexRenderer<V, E>{

	public AVertexListShapeModelRenderer() {
		super();
	}
	
	protected void paintShapeForVertex(RenderContext<V,E> rc, V v,  Layout<V,E> layout, RingsCompositeShape shape) {
		 Point2D p = layout.transform(v);
		    p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);
		    float x = (float)p.getX();
		    float y = (float)p.getY();
      GraphicsDecorator g = rc.getGraphicsContext();
      for (ShapeModel aShapeModel:shape.getComponents()) {
    	  g.fillOval((int) x + aShapeModel.getX(), (int) y + aShapeModel.getY(), aShapeModel.getWidth(), aShapeModel.getHeight());
      }
    	  
     
//      Paint oldPaint = g.getPaint();
//      Paint fillPaint = rc.getVertexFillPaintTransformer().transform(v);
//      if(fillPaint != null) {
//          g.setPaint(fillPaint);
//          g.fill(shape);
//          g.setPaint(oldPaint);
//      }
//      Paint drawPaint = rc.getVertexDrawPaintTransformer().transform(v);
//      if(drawPaint != null) {
//      	g.setPaint(drawPaint);
//      	Stroke oldStroke = g.getStroke();
//      	Stroke stroke = rc.getVertexStrokeTransformer().transform(v);
//      	if(stroke != null) {
//      		g.setStroke(stroke);
//      	}
//      	g.draw(shape);
//      	g.setPaint(oldPaint);
//      	g.setStroke(oldStroke);
		
	}
protected void paintIconForVertex(RenderContext<V,E> rc, V v, Layout<V,E> layout) {
	Shape shape = rc.getVertexShapeTransformer().transform(v);
	if (shape instanceof RingsCompositeShape) {
		paintShapeForVertex(rc, v, layout, (RingsCompositeShape) shape);
		return;
    }
	super.paintIconForVertex(rc, v, layout);
}
}



