package bus.uigen.diff;

import java.util.HashMap;
import java.util.Map;

import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.HashtableAdapter;

public class ACanonicalMapBean extends ACanonicalBean implements Map  {
//	HashtableAdapter hashtableAdapter;
	public ACanonicalMapBean (HashtableAdapter aHashTableAdapter) {
		super(aHashTableAdapter);
//		hashtableAdapter = aHashTableAdapter;
	}

}
