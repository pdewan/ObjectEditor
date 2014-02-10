package bus.uigen;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import util.misc.Common;
import util.trace.Tracer;

import bus.uigen.HierarchicalNameTableTree;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ClassNameTable;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.sadapters.GenericPrimitiveToPrimitive;

public class HierarchicalKeywordsToClassTable extends HierarchicalNameTableTree {
	//static ObjectEditor objectEditor;
	public HierarchicalKeywordsToClassTable(Vector<String> initialList,
			//ObjectEditor theObjectEditor, 
			String theName) {
			 super (theName);
			 //ObjectEditor.keyWordsToClassNames = new Hashtable();
			 //objectEditor = theObjectEditor;
			 Tracer.userMessage("CREATING CLASS INDEX, WHICH INVOLVES LOADING AND INSTANTIATING CLASS FILES IN THE DIRECTORY." + 
					 " \n THIS MAY RESULT IN SEVERAL  ERROR MESSAGES, WHICH CAN BE IGNORED" );
			 processClassNames(initialList);
			 Tracer.userMessage("CLASS INDEX FINISHED" );
				 
	}
	
	public HierarchicalKeywordsToClassTable(
			//ObjectEditor theObjectEditor,
	
			String theName) {
		super (theName);
		//objectEditor = theObjectEditor;
	}
	@Override
	HierarchicalNameTableTree createChildNode(String name) {
		// TODO Auto-generated method stub
		return new HierarchicalKeywordsToClassTable(name);
	}

	@Override
	char getDelimiter() {
		// TODO Auto-generated method stub
		return AttributeNames.KEYWORD_SEPARATOR;
	}

	@Override
	Object getValue(String childName) {
		// TODO Auto-generated method stub
		Object classTable = ObjectEditor.keyWordsToClassNames.get(childName);
		if (classTable == null)
			return "";
		return classTable;
		//return keyWordsToClassNames.get(childName);
	}
	/*
	public void open(String newClassName) {
		objectEditor.newInstance(newClassName);
	}
	*/
	void processClassNames(Vector<String> classNames) {
		for (int i = 0; i < classNames.size(); i++) {
			processClass(classNames.elementAt(i));
		}
		Vector<String> keyWords = util.misc.Common.toVector(ObjectEditor.keyWordsToClassNames.keys());
		super.init(keyWords);
	}
	void processInstanceOf(ClassProxy c) {
		if (c.isInterface())
			return;
		if (!(c instanceof AClassProxy))
				return;
		Object obj = util.misc.Common.asynchronousNewInstance(((AClassProxy)c).getJavaClass());
		if (obj != null) {
			ObjectAdapter adapter = uiGenerator.toTopAdapter(obj);
			if (adapter == null) return;
			uiGenerator.deepSetAttributes(adapter);	
			deepAssociatePreferredWidgetWithClass(adapter);
		}
	}
	void deepAssociatePreferredWidgetWithClass(ObjectAdapter adapter) {
		String preferredWidget = adapter.getPreferredWidget();
		if (preferredWidget != null) {
			String shortName = Common.shortClassName(preferredWidget);
			ClassProxy targetClass = null;
			if (adapter.getRealObject() == null)
				targetClass = adapter.getPropertyClass();
			else
			 targetClass = ACompositeLoggable.getTargetClass(adapter.getRealObject());
			if (targetClass == null) return;
			if ( GenericPrimitiveToPrimitive.isPrimitiveClassStatic(targetClass.getName()) && adapter.getParentAdapter() != null) {
				targetClass = ACompositeLoggable.getTargetClass(adapter.getParentAdapter().getRealObject());
			}
			ObjectEditor.associateKeywordWithClassName(ObjectEditor.WIDGET_KEYWORD + 
					AttributeNames.KEYWORD_SEPARATOR + shortName, targetClass);
		}
		if (adapter instanceof ClassAdapter) {
			ClassAdapter classAdapter = (ClassAdapter) adapter;
			Enumeration elements = classAdapter.getChildAdapters();
			while (elements.hasMoreElements())
				deepAssociatePreferredWidgetWithClass((ObjectAdapter) elements.nextElement());
		}
			
	}
	void processClass(String className) {
		try {
			ClassProxy c = RemoteSelector.classProxy(util.misc.Common.asynchronousClassForName(className));
			if (c == null)
				return;
			String[] keywords = AClassDescriptor.getKeywordsAnnotation(c);
			String[] urlString = AClassDescriptor.getHTMLDocumentation(c);
			String explanation = AClassDescriptor.getExplanationAnnotation(c);		
			final String helpString = AClassDescriptor.getAnnotationString(className);	
			 if (explanation != null)
				  ObjectEditor.associateKeywordWithClassName(ObjectEditor.EXPLANATION_ANNOTATION_KEYWORD, c);
			 if (keywords != null)
				  ObjectEditor.associateKeywordWithClassName(ObjectEditor.KEYWORDS_ANNOTATION_KEYWORD, c);
			 if (urlString != null)
				  ObjectEditor.associateKeywordWithClassName(ObjectEditor.HTML_DOCUMENTATION_ANNOTATION_KEYWORD, c);
			//String description = ClassDescriptor.getAnnotationString(c);
			/*
			if (explanation != null) {
				helpString += explanation;
			}
			*/
			if (keywords != null) {
				
				//helpString += "\n\n" + toString(keywords);
				for (int i = 0; i < keywords.length; i++) {
					/*
					ClassNameTable classNames = ObjectEditor.keyWordsToClassNames.get(keywords[i]);
					if (classNames ==null) { 
						classNames = new ClassNameTable();
						ObjectEditor.keyWordsToClassNames.put(keywords[i], classNames);
					}
					*/
					//ClassNameTable classNames = ObjectEditor.getClassNameTable(keywords[i]);
					//classNames.put(className, description);
					ObjectEditor.associateKeywordWithClassName(keywords[i], c);
					
					
					
				}				
			}
			processInstanceOf(c);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
