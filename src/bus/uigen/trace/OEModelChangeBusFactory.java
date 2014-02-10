package bus.uigen.trace;

public class OEModelChangeBusFactory {
	static OEModelChangeBus bus;
	public static OEModelChangeBus getModelChangeBus() {
		if (bus == null) {
			bus = new AnOEModelChangeBus();
		}
		return bus;
	}

}
