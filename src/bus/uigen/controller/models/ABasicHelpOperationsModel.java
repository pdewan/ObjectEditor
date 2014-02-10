package bus.uigen.controller.models;

import java.net.URI;
import java.util.Enumeration;
import java.util.List;

import util.annotations.Visible;
import util.models.AListenableHashtable;
import util.trace.Tracer;
import util.web.PackageRegistry;
import bus.uigen.uiFrame;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;

@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ABasicHelpOperationsModel extends AnAbstractOperationsModel implements FrameModel {
	ObjectEditorDescription oeDescription = new AnObjectEditorDescription();
	String classesWithURLs[];
	static AListenableHashtable<String, URI> stringToURL = new AListenableHashtable();
	public ObjectEditorDescription aboutObjectEditor() {
		return oeDescription;
	}
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		super.init(theFrame, theObject, theObjectAdapter);
		List<String> sourceClasses = frame.getSourceClassNames();
		processClassNames(sourceClasses, theObjectAdapter);
	}
	void processClassNames(List<String> sourceClasses, ObjectAdapter theObjectAdapter) {
		if (sourceClasses.size() == 0 && theObjectAdapter.getRealObject() != null) {
			// OE class, in which case there will be no user source classses
			processClass(theObjectAdapter.getRealObject().getClass().getName(), theObjectAdapter);
		}
		for (int i = 0; i < sourceClasses.size(); i++)
			processClass(sourceClasses.get(i), theObjectAdapter);
//		String theTopClass = theObjectAdapter.getRealObject().getClass().getName();
	
		Enumeration<String> classes = stringToURL.keys();
		classesWithURLs = new String[stringToURL.size()];
		for (int i = 0; i < classesWithURLs.length; i++) {
			classesWithURLs[i] = classes.nextElement();
			
		}
//		int i = 0;
//		while (classes.hasMoreElements())  {
//			classesWithURLs[i] = classes.nextElement();
//		}
			
	}
	@Visible(false)
	public static void showInExistingBrowser(URI theURL) {
		try {
			java.awt.Desktop.getDesktop().browse(theURL);
		} catch (Exception e) {
			Tracer.error("Could not show URL:" + theURL);
		}

	}
	@Visible(false)
	public static void showInExistingBrowser( String theURL) {
		URI uri = stringToURL.get(theURL);
//		if (uri == null) {
//			uri = toURI(aClassName, theURL);
//			if (uri == null) {
//				return;
//			}
//			stringToURL.put(theURL, uri );
//		}
		showInExistingBrowser(uri);

	}
	public String[] getDynamicCommands() {
		return classesWithURLs;
		//return frame.getCustomizeClassNames();
	}
	public void invokeDynamicCommand (String commandName) {
//		URI uri = classToURL.get(className);
//		showInExistingBrowser(uri);
		showInExistingBrowser(commandName);
	}
	void processClass(String className, ObjectAdapter theObjectAdapter) {
		try {			
			ClassProxy c = RemoteSelector.forName(className);
			String label = AClassDescriptor.getLabel(c);			
			String[] urlString = null;
			if (className.equals(theObjectAdapter.getRealObject().getClass().getName())) {
				urlString = theObjectAdapter.getHTMLDocumentation();
			} else {
				urlString = AClassDescriptor.getHTMLDocumentation(c);
			}
			if (urlString != null) {
				//helpString += "\n\nMore info available at:" + urlString;
				try {
//				URI[] uri = new URI[urlString.length];
				for (int i = 0; i < urlString.length; i++) {
//					uri[i] = java.net.URI.create(urlString[i]);
					URI uri = PackageRegistry.toURI(className, urlString[i]);
					String key = label + ": " + PackageRegistry.toKeyword(urlString[i]);	
					stringToURL.put(key, uri);


				}
//				URL url = new URL(urlString);	
//				URI url = java.net.URI.create(urlString);	
//				classToURL.put(label, uri);
				} catch (Exception e) {
					Tracer.error("The URLSting:" + urlString + "of class:" + className + " is not a valid URL");
				}
			}
			
			
		} catch (Exception e) {
			
		}
	}

}
