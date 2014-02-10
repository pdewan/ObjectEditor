package bus.uigen.controller.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.annotations.Visible;
import util.trace.TraceableClassToInstancesFactory;
import util.trace.TraceableError;
import util.trace.TraceableWarning;
import util.trace.Tracer;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ABasicProblemOperationsModel extends AnAbstractOperationsModel implements FrameModel {
	List<Class<? extends Exception>> traceableWarningsOrErrors = new ArrayList();;
	List<String> traceableWarningsOrErrorsNamesList = new ArrayList();
	String[] traceableWarningsOrErrorsNames;
	Map<String, Class<? extends Exception>> nameToTraceableClass = new HashMap();
	
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		super.init(theFrame, theObject, theObjectAdapter);
		Set<Class<? extends Exception>> traceableClasses = TraceableClassToInstancesFactory.getOrCreateTraceableClassToInstances().getClassToInstances().keySet();
		processClassses(traceableClasses);
		
	}
	void processClassses(Set<Class<? extends Exception>> traceableClasses) {
		for (Class<? extends Exception> traceableClass:traceableClasses) {
			if (TraceableWarning.class.isAssignableFrom(traceableClass) ||
				TraceableError.class.isAssignableFrom(traceableClass)) {
				traceableWarningsOrErrors.add(traceableClass);	
				traceableWarningsOrErrorsNamesList.add(traceableClass.getSimpleName());
				nameToTraceableClass.put(traceableClass.getSimpleName(), traceableClass);
			}
		}
		String[] template = {};
		traceableWarningsOrErrorsNames = traceableWarningsOrErrorsNamesList.toArray(template);
		
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
//	@Visible(false)
//	public static void showInExistingBrowser( String theURL) {
//		URI uri = stringToURL.get(theURL);
//
//		showInExistingBrowser(uri);
//
//	}
	public String[] getDynamicCommands() {
		return traceableWarningsOrErrorsNames;
		//return frame.getCustomizeClassNames();
	}
	public void invokeDynamicCommand (String commandName) {
		Class exceptionClass = nameToTraceableClass.get(commandName);
//		ClassProxy classProxy = AClassProxy.classProxy(exceptionClass);		
		System.out.println(commandName);
		ProblemDetails exceptionDetails = new AProblemDetails(exceptionClass);
		ObjectEditor.edit(exceptionDetails);
//		showInExistingBrowser(commandName);
	}
//	void processClass(String className) {
//		try {
//			ClassProxy c = RemoteSelector.forName(className);
//			String label = AClassDescriptor.getLabel(c);
//			String[] urlString = AClassDescriptor.getHTMLDocumentation(c);
//			if (urlString != null) {
//				//helpString += "\n\nMore info available at:" + urlString;
//				try {
////				URI[] uri = new URI[urlString.length];
//				for (int i = 0; i < urlString.length; i++) {
////					uri[i] = java.net.URI.create(urlString[i]);
//					URI uri = PackageRegistry.toURI(className, urlString[i]);
//					String key = label + ": " + PackageRegistry.toKeyword(urlString[i]);	
//					stringToURL.put(key, uri);
//
//
//				}
////				URL url = new URL(urlString);	
////				URI url = java.net.URI.create(urlString);	
////				classToURL.put(label, uri);
//				} catch (Exception e) {
//					Tracer.error("The URLSting:" + urlString + "of class:" + className + " is not a valid URL");
//				}
//			}
//			
//			
//		} catch (Exception e) {
//			
//		}
//	}

}
