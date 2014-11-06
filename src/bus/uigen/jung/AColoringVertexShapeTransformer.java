package bus.uigen.jung;

import java.awt.Shape;

import org.apache.commons.collections15.Transformer;

public class AColoringVertexShapeTransformer<VertexType> implements Transformer<VertexType, Shape> {
	JungGraphManager<VertexType, Object> jungGraphManager;
	public AColoringVertexShapeTransformer(JungGraphManager<VertexType, Object> aJungGraphManager) {
		jungGraphManager = aJungGraphManager;
	}
	
	@Override
	public Shape transform(VertexType arg0) {
		return null;
	}

}
