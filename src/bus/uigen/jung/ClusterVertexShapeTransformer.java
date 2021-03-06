package bus.uigen.jung;

import java.awt.Shape;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;

public class ClusterVertexShapeTransformer<V> extends  EllipseVertexShapeTransformer<V>{



        public ClusterVertexShapeTransformer() {
            setSizeTransformer(new ClusterVertexSizeTransformer<V>(20));
        }
        @SuppressWarnings("unchecked")
		@Override
        public Shape transform(V v) {
            if(v instanceof Graph) {
                int size = ((Graph)v).getVertexCount();
                if (size < 8) {   
                    int sides = Math.max(size, 3);
                    return factory.getRegularPolygon(v, sides);
                }
                else {
                    return factory.getRegularStar(v, size);
                }
            }
            return super.transform(v);
        }
    }