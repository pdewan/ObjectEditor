package bus.uigen.controller.models;

import java.util.ArrayList;
import java.util.List;

import util.annotations.Explanation;
import util.annotations.ReturnsClassExplanation;
import util.annotations.ReturnsClassWebDocuments;
import util.annotations.ShowDebugInfoWithToolTip;
import util.trace.TraceableClassToInstancesFactory;
import bus.uigen.ObjectEditor;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.widgets.VirtualTextArea;
@ShowDebugInfoWithToolTip(false)
public class AProblemDetails implements ProblemDetails {
	Class exceptionClass;
	ClassProxy exceptionClassProxy;
	String explanations;
	String[] webDocumentation;
	int count;
	boolean showClassInformation = false;
	List<Exception> exceptions;
	List<String> exceptionMessages;
	
	boolean preSetShowClassInformation = false;
	public static final String NO_EXPLANATION = "No Explanation";
	public AProblemDetails(Class anExceptionClass) {
		exceptionClass = anExceptionClass;	
		exceptionClassProxy = AClassProxy.classProxy(exceptionClass);
		initExplanation();
		initWebDocumentation();
		initExceptions();
		initCount();
		
	}
	void initExplanation() {
		List<ClassProxy> superTypes = IntrospectUtility
		.getSuperTypes(exceptionClassProxy);
		explanations = AClassDescriptor.getExplanationAnnotation(superTypes);
		if (explanations == null || explanations == "") {
			explanations = NO_EXPLANATION; ;
		}
	}
	void initWebDocumentation() {
		webDocumentation = AClassDescriptor.getHTMLDocumentation(exceptionClassProxy);
		
	}
	void initExceptions() {
		exceptions  =TraceableClassToInstancesFactory.getOrCreateTraceableClassToInstances().
						getClassToInstances().get(exceptionClass);
		exceptionMessages = new ArrayList(exceptions.size());
		for (Exception exception:exceptions) {
			exceptionMessages.add(exception.getMessage());
		}
		
	}
	void initCount() {
		
		if (exceptions == null)
			count = 0;
		else
			count = exceptions.size();
	}
	@ReturnsClassWebDocuments(true)
	public String[] getWebDocumentation() {
		return webDocumentation;
	}
	public void showAllCasesThisSession() {
		ObjectEditor.edit(exceptionMessages, VirtualTextArea.class);
	}
	@ReturnsClassExplanation(true)
	public String getExplanation() {
		return explanations;
	}
	public int getNumberOfCasesThisSession() {
		return count;
	}
	public boolean preSetShowExperienceOfOthers() {
		return preSetShowClassInformation;
	}
	public boolean getShowExperienceOfOthers() {
		return showClassInformation;
	}
	@Explanation("When set, shows experience others have had with this problem. Can be set only if communication of logs with server is enabled")
	public void setShowExperienceOfOthers(boolean showClassInformation) {
		this.showClassInformation = showClassInformation;
	}
	
	

}
