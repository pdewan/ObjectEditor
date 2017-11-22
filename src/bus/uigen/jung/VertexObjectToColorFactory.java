package bus.uigen.jung;

public class VertexObjectToColorFactory {
	static VertexObjectToColor colorer = new AVertexObjectToColor();

	public static VertexObjectToColor getColorer() {
		return colorer;
	}

	public static void setColorer(VertexObjectToColor colorer) {
		VertexObjectToColorFactory.colorer = colorer;
	}


}
