package bus.uigen.diff;

import java.util.HashMap;
import java.util.Map;

import bus.uigen.oadapters.ClassAdapter;

public class ACanonicalBean extends HashMap implements Map  {
	ClassAdapter classAdapter;
	public ACanonicalBean (ClassAdapter aClassAdapter) {
		classAdapter = aClassAdapter;
	}

}
