package bus.uigen.jung;

import java.awt.Shape;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import java.util.List;
import java.awt.Color;

public class AVertexListShapeModelTransformer<VertexType> extends  EllipseVertexShapeTransformer<VertexType>{

		JungGraphManager<VertexType, Object> jungGraphManager;
		RingsCompositeShape ringsCompositeShape = new ARingsCompositeShape();

        public AVertexListShapeModelTransformer(JungGraphManager<VertexType, Object> aJungGraphManager) {
        	jungGraphManager = aJungGraphManager;
//            setSizeTransformer(new ClusterVertexSizeTransformer<V>(20));
        }
        @SuppressWarnings("unchecked")
		@Override
        public Shape transform(VertexType v) {
        	Shape aPrototypeShape = super.transform(v);
        	List<Color> aColors = jungGraphManager.getColors(v);
        	if (aColors == null)
        		return aPrototypeShape;
        	ringsCompositeShape.set(aColors, aPrototypeShape);
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