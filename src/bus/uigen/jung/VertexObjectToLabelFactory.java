package bus.uigen.jung;

public class VertexObjectToLabelFactory {
	static VertexObjectToLabel labeler = new AVertexObjectToLabel();

	public static VertexObjectToLabel getLabeler() {
		return labeler;
	}

	public static void setLabeler(VertexObjectToLabel labeler) {
		VertexObjectToLabelFactory.labeler = labeler;
	}


}
