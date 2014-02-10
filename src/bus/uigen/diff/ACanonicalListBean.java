package bus.uigen.diff;

import java.util.ArrayList;
import java.util.List;

import bus.uigen.oadapters.VectorAdapter;

public class ACanonicalListBean extends ArrayList implements List  {
	VectorAdapter vectorAdapter;
	public ACanonicalListBean (VectorAdapter aVectorAdapter) {
		vectorAdapter = aVectorAdapter;
	}

}
