package util.trace.uigen;

public class OEModelChangerFactory {
	static OEModelChanger modelChanger;
	public static OEModelChanger getModelChanger() {
		if (modelChanger == null) {
			modelChanger = new AnOEModelChanger();
		}
		return modelChanger;
	}

}
