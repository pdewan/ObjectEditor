package bus.uigen.jung;

import java.awt.Shape;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;

import java.util.List;
import java.awt.Color;
import java.awt.geom.Ellipse2D;

import org.apache.commons.collections15.functors.ConstantTransformer;

public class AVertexListShapeModelTransformer<VertexType> extends  EllipseVertexShapeTransformer<VertexType>{
//public class AVertexListShapeModelTransformer<VertexType> extends  ConstantTransformer<VertexType>{

		JungGraphManager<VertexType, Object> jungGraphManager;
		RingsCompositeShape ringsCompositeShape = new ARingsAttributedCompositeShape();
		Ellipse2D.Float regularShape = new Ellipse2D.Float(-10,-10,20,20);
		Color regularColor;

        public AVertexListShapeModelTransformer(JungGraphManager<VertexType, Object> aJungGraphManager) {
        	jungGraphManager = aJungGraphManager;
        	
//            setSizeTransformer(new ClusterVertexSizeTransformer<V>(20));
        }
        @SuppressWarnings("unchecked")
		@Override
        public Shape transform(VertexType v) {
//        	Shape aPrototypeShape = super.transform(v);
        	List<Color> aColors = jungGraphManager.getVertexColors(v);
        	if (aColors == null)
        		return regularShape;
        	ringsCompositeShape.set(aColors, regularShape);
        	return ringsCompositeShape;
        	
        	
//            if(v instanceof Graph) {
//                int size = ((Graph)v).getVertexCount();
//                if (size < 8) {   
//                    int sides = Math.max(size, 3);
//                    return factory.getRegularPolygon(v, sides);
//                }
//                else {
//                    return factory.getRegularStar(v, size);
//                }
//            }
//            return super.transform(v);
        }
    }