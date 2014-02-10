package bus.uigen.controller.models;

import java.net.URL;
import java.util.List;

import util.annotations.Visible;
import util.models.AListenableHashtable;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.FieldDescriptorProxy;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.view.AcrobatFrame;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AHelpOperationsModel extends ABasicHelpOperationsModel implements FrameModel {
//	uiFrame frame;
	AListenableHashtable<AListenableHashtable, String> helpObject = new AListenableHashtable();
	AListenableHashtable<AListenableHashtable<String, String>, String> methodAnnotationTables;
	AListenableHashtable<String, String> attributeNameToExplanation;
	AListenableHashtable<String, ClassNameTable> keyWordsToClassNames =  new AListenableHashtable();
	static AcrobatFrame acrobatFrame;
	
	//AListenableHashtable<String, String> methodAnnotations;
	uiFrame helpFrame;
	boolean topAdapterAnnotationPut;
//	ObjectEditorDescription oeDescription = new AnObjectEditorDescription();
	@Override
	@Visible(false)
	public void init(uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		// TODO Auto-generated method stub
//		frame = theFrame;
		super.init(theFrame, theObject, theObjectAdapter);
		methodAnnotationTables = frame.getAnnotationManager().getAnnoationTables();
		methodAnnotationTables.setUserObject("");
		List<String> sourceClasses = frame.getSourceClassNames();
		attributeNameToExplanation =  getConstantsDocumentation(RemoteSelector.classProxy(AttributeNames.class));
//		processClassNames(sourceClasses);
		//ObjectEditor.setDefaultAttribute(AttributeNames.TOOLBAR, false);
		//ObjectEditor.setAttribute(AListenableHashtable.class, AttributeNames.HASHTABLE_CHILDREN, AttributeNames.KEYS_ONLY);
		//ObjectEditor.setAttribute(AListenableHashtable.class, AttributeNames.LABELLED, false);
		//ObjectEditor.setAttribute(AListenableHashtable.class, AttributeNames.METHODS_VISIBLE, false);
		//if (frame.getTopAdapter() != null) {
		//helpObject.put(methodAnnotations, "");
		//}
		
	}
	AcrobatFrame getAcrobatFrame() {
		if (acrobatFrame ==null)
			acrobatFrame = new AcrobatFrame();
		return acrobatFrame;
	}
	public void customizatonAttributes() {
		ObjectEditor.treeBrowse(attributeNameToExplanation, (boolean) false);
		
	}
	AListenableHashtable<String, String> getConstantsDocumentation(ClassProxy c) {
		ClassDescriptorInterface cdesc = ClassDescriptorCache.getClassDescriptor(c);
	    FieldDescriptorProxy[] constants = cdesc.getConstantDescriptors();
	    if (constants == null)
	      return null;
	    AListenableHashtable<String, String> retVal = new AListenableHashtable();
	    for (int i=0; i<constants.length; i++) {	    	
	    	Object instance = null;
	    	FieldProxy field = constants[i].getField();
	    	String helpString = nullString;
	      try {
	    	  instance = c.newInstance();
	    	  helpString += field.get(instance) + "\n";
	      } catch (Exception e) {
	    	  instance = null;
	      }
	      util.annotations.Explanation explanation = (util.annotations.Explanation) field.getAnnotation(util.annotations.Explanation.class);
	      if (explanation != null) {	    	 
	    	  helpString += explanation.value();  
	    	  
	      }
	      String key = field.getName();	 
	      retVal.put(key, helpString);
	    }
	    return retVal;
	    
	}
	
	final static String nullString = "";
	/*
	String toString (String[] keywords) {
		String retVal = "Keywords: ";
		for (int i = 0; i  < keywords.length; i  ++) {
			if (i != 0)
				retVal += ", ";
			retVal += keywords[i];
		}
		return retVal;
	}
	*/
	/*
	void processClass(String className) {
		try {
			Class c = Class.forName(className);
			String label = ClassDescriptor.getLabel(c);
			String urlString = ClassDescriptor.getHTMLDocumentation(c);
			String explanation = ClassDescriptor.getExplanationAnnotation(c);
			String[] keywords = ClassDescriptor.getKeywordsAnnotation(c);			
			String helpString = nullString;			
			if (explanation != null) {
				helpString += explanation;
			}
			if (keywords != null) {
				helpString += "\n\n" + toString(keywords);
				for (int i = 0; i < keywords.length; i++) {
					ClassNameTable classNames = keyWordsToClassNames.get(keywords[i]);
					if (classNames ==null) {
						classNames = new ClassNameTable();
						keyWordsToClassNames.put(keywords[i], classNames);
					}
					if (!classNames.contains(c.getName())) {						
							classNames.put(c.getName(), helpString);
					}
				}				
			}
			if (urlString != null) {
				helpString += "\n\nMore info available at:" + urlString;
				try {
				URL url = new URL(urlString);				
				classToURL.put(label, url);
				} catch (Exception e) {
					Message.error("The URLSting:" + urlString + "of class:" + c + " is not a valid URL");
				}
			}
			
			if (helpString!= nullString ) {
				AListenableHashtable<String, String> menuTable = frame.getAnnotationManager().getMenuTable(label);
				if (menuTable != null)
				methodAnnotationTables.put(menuTable, helpString);
			}
		} catch (Exception e) {
			
		}
	}
	*/

	
	void processClass(String className, ObjectAdapter theObjectAdapter) {
		super.processClass(className, theObjectAdapter);
		try {
			ClassProxy c = RemoteSelector.forName(className);
			String label = AClassDescriptor.getLabel(c);
			String[] urlString = AClassDescriptor.getHTMLDocumentation(c);
			String explanation = AClassDescriptor.getExplanationAnnotation(c);
			String[] keywords = AClassDescriptor.getKeywordsAnnotation(c);			
			final String helpString = AClassDescriptor.getAnnotationString(className);	
			 if (explanation != null)
				  ObjectEditor.associateKeywordWithClassName(ObjectEditor.EXPLANATION_ANNOTATION_KEYWORD, c);
			 if (keywords != null)
				  ObjectEditor.associateKeywordWithClassName(ObjectEditor.KEYWORDS_ANNOTATION_KEYWORD, c);
			 if (urlString != null)
				  ObjectEditor.associateKeywordWithClassName(ObjectEditor.HTML_DOCUMENTATION_ANNOTATION_KEYWORD, c);
			/*
			if (explanation != null) {
				helpString += explanation;
			}
			*/
			if (keywords != null) {
				//helpString += "\n\n" + toString(keywords);
				for (int i = 0; i < keywords.length; i++) {
					ClassNameTable classNames = keyWordsToClassNames.get(keywords[i]);
					if (classNames ==null) {
						classNames = new ClassNameTable();
						keyWordsToClassNames.put(keywords[i], classNames);
					}
					if (!classNames.contains(c.getName())) {						
							classNames.put(c.getName(), helpString);
					}
				}				
			}
//			if (urlString != null) {
//				try {
//				for (int i = 0; i < urlString.length; i++) {
//					URI uri = toURI(urlString[i]);
//					String key = label + ": " + toKeyword(urlString[i]);	
//					classToURL.put(key, uri);
//
//
//				}
//
//				} catch (Exception e) {
//					Tracer.error("The URLSting:" + urlString + "of class:" + c + " is not a valid URL");
//				}
//			}
			
			if (helpString!= nullString ) {
				AListenableHashtable<String, String> menuTable = frame.getAnnotationManager().getMenuTable(label);
				if (menuTable != null)
				methodAnnotationTables.put(menuTable, helpString);
			}
		} catch (Exception e) {
			
		}
	}
	void putTopAdapterAnnotation() {
		
	}
	void showPDFURL(URL theURL) {
		getAcrobatFrame().showPDFURL(theURL);
	}
	void showURL(URL theURL) {
		if (theURL.toString().endsWith(".pdf"))
			showPDFURL(theURL);
		else
			showHTMLURL(theURL);
	}
	
	void showHTMLURL (URL theURL) {
		ObjectEditor.setMethodAttribute(AWindowOperationsModel.class, "secondaryPanel", AttributeNames.TOOLBAR_METHOD, true);
		ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "newWindowRight", AttributeNames.TOOLBAR_METHOD, true);
		ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "newWindowBottom", AttributeNames.TOOLBAR_METHOD, true);
		//ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "forward", AttributeNames.TOOLBAR_METHOD, true);
		//ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "back", AttributeNames.TOOLBAR_METHOD, true);
		//uiFrame topFrame = uiFrameSelector.createFrame(helpURL, false);
		//topFrame.setFrameKind(uiFrame.HELP_FRAME);
		//ObjectEditor.edit(topFrame, helpURL);
		//topFrame.setVisible(true);
		//uiFrame frame = ObjectEditor.edit(helpURL, false);
		uiFrame topFrame = ObjectEditor.browse(theURL, false);
		//uiFrame topFrame = ObjectEditor.edit(helpURL, false);
		topFrame.showToolBar();
		topFrame.setSize(800, 600);
		//ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "forward", AttributeNames.TOOLBAR_METHOD, false);
		//ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "back", AttributeNames.TOOLBAR_METHOD, false);
		ObjectEditor.setMethodAttribute(AWindowOperationsModel.class, "secondaryPanel", AttributeNames.TOOLBAR_METHOD, false);
		ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "newWindowRight", AttributeNames.TOOLBAR_METHOD, false);
		ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "newWindowBottom", AttributeNames.TOOLBAR_METHOD, false);
	}
	/*
	public void htmlDocumentation() {
		try {
			java.net.URL helpURL;
			//helpURL = new URL("http://www.cs.unc.edu/~dewan/oe/");
			//helpURL = new URL("http://www.cs.unc.edu/~dewan/comp14/");
			//helpURL = new URL ("http://java.sun.com/j2se/1.5.0/docs/api/");
			helpURL = new URL ("http://www.cs.unc.edu/");
			showURL(helpURL);
			
		} catch (Exception e) {
			e.printStackTrace();
			//return null;
		}
	}
	*/

	public void summary() {
		//helpFrame = ObjectEditor.treeBrowse(helpObject);
		
		
		helpFrame = ObjectEditor.treeBrowse(methodAnnotationTables, (boolean) false);
		helpFrame.setTitle("Help on " + frame.getTitle());
	    
		/*
		uiObjectAdapter topAdapter = frame.getTopAdapter();		
		if (topAdapter != null && !topAdapter.getToolTipText().equals("")) {
			//ObjectEditor.edit(topAdapter.getToolTipText());
			
			
			
			ObjectEditor.treeBrowse(frame.getAnnotationManager().getMethodAnnotations());
		} else
			ObjectEditor.edit("No help text.\n Programmer can make annotation or attribute definition to set it.");
		*/
	}
	
	public void indexedClasses() {
		ObjectEditor.defaultObjectEditor().indexedClasses();
		/*
		ObjectEditor.treeBrowse(keyWordsToClassNames, "class names for selected indexed item shown here", 
				"explanation of selected class name shown here", 
				(boolean) false);
				*/
	}
	
//	public ObjectEditorDescription aboutObjectEditor() {
//		return oeDescription;
//	}
	
	
	

}
