package bus.uigen.jung;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;

public class ClusterVertexSizeTransformer<V>  implements Transformer<V,Integer> {
    	int size;
        public ClusterVertexSizeTransformer(Integer size) {
            this.size = size;
        }

        public Integer transform(V v) {
            if(v instanceof Graph) {
                return 30;
            }
            return size;
        }
    

}
