package bus.uigen.jung;

import java.awt.Shape;

import org.apache.commons.collections15.Transformer;

public class AnAttributeBasedVertexShapeTransformer<VertexType> implements Transformer<VertexType, Shape> {
	JungGraphManager<VertexType, Object> jungGraphManager;
	public AnAttributeBasedVertexShapeTransformer(JungGraphManager<VertexType, Object> aJungGraphManager) {
		jungGraphManager = aJungGraphManager;
	}
	
	@Override
	public Shape transform(VertexType arg0) {
		return null;
	}

}
