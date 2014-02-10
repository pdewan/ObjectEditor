package bus.uigen.trace;

import java.util.HashSet;
import java.util.Set;

import util.trace.ObjectError;
import bus.uigen.oadapters.ObjectAdapter;

public class EditOfNonTree extends ObjectError {
	Set<String> references;
	public EditOfNonTree(String aMessage, Set<String> aReferenceSet, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		references = aReferenceSet;
	}
	public static void newCase(Set<String> aReferenceSet, Object aTarget, Object aFinder) {
//		Set<String> aPathSet = new HashSet();
//		for (ObjectAdapter adapter: aReferenceSet) {
//			aPathSet.add(adapter.getReferencePath());
//		}
		String aMessage = "Object:"  + aTarget + " displayed multiple times with following references:\n " + aReferenceSet + 
		"\n Remove one of these references in the displayed logical structure by, for instance, " +
		"renaming the getter for the property storing the shared object or adding the @util.annotations.Visible(false) annotation before the getter.\n" +
		"If you think it is safe to tareverse  all of these  references then add the annotattion @util.annotattions.AllowMultipleEqualReferences(false) nefore the getter.";
		new EditOfNonTree(aMessage, aReferenceSet, aTarget, aFinder);
	}

}
