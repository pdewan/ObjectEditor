package bus.uigen.controller.models;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import util.annotations.Visible;
import util.misc.Common;
import util.trace.Tracer;
import util.web.SrcPackageRegistry;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualTextArea;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ABasicSourceOperationsModel extends AnAbstractOperationsModel implements FrameModel {
	String[] sourceClasses;
	Vector<String> sourceClassesVector;
	File[] sourceFiles;
	String[] sourceURLs;
	boolean separateSrcBin = false;
	boolean foundSourceDirectory;	
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {		
		super.init(theFrame, theObject, theObjectAdapter);
		String[] template = {};
//		frame.setGlobalSourceDirectory("src");
		sourceClasses = frame.getSourceClassNames().toArray(template);
		sourceFiles = new File[sourceClasses.length];
		sourceURLs = new String[sourceClasses.length];
		sourceClassesVector = Common.deepArrayToVector(sourceClasses);
		separateSrcBinDirectories();
		if (!foundSourceDirectory)
			separateSrcBinDirectories();
		
//		refreshSourceFiles();		
		frame.getModelRegistry().registerSourceModel(this);
	}
	
	void setFoundSourceDirectory() {
		foundSourceDirectory = sourceFiles.length > 1 && sourceFiles[0].exists();
	}
	
		
	
	void refreshSourceFiles() {
		foundSourceDirectory = false;
		for (int i = 0; i < sourceClasses.length; i++) {
			String aClassName =  sourceClasses[i];
			String aURL = toURL(i);
			if (aURL == null) {
			   sourceFiles[i] = frame.open(aClassName);	
			   foundSourceDirectory = sourceFiles[i].exists() | foundSourceDirectory;
			} else {
				sourceURLs[i] = aURL;
			}
		}	
	
	}
	
	
	
	
	  void separateSrcBinDirectories() {
		separateSrcBin = !separateSrcBin;
		try {
			if (separateSrcBin) {
				frame.setGlobalSourceDirectory("src");
			} else {
				frame.setGlobalSourceDirectory(null);
			}
			refreshSourceFiles();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//frame.setSourceDirectory(fileDialog.getDirectory());		
	}
	  
	   boolean fileExists(int index) {			
			
				return sourceFiles[index] != null && 
				sourceFiles[index].exists();
		}
	   boolean urlExists(int index) {			
			
			return sourceURLs[index] != null;
	}
	   String toURL(int index) {
		   if (sourceClasses[index] == null)
			   return null;
		   String aClassName = sourceClasses[index];
		   String url = SrcPackageRegistry.bestURL(aClassName);
		   if (url == null || url == "") {
			   return null;
		   }
		   return url + "/" + Common.classNameToSourceFileName(aClassName);
		}
	
	public boolean preDynamicCommands(String className) {
		
		int index = sourceClassesVector.indexOf(className);
		if (index < 0)
			return false;
		else
//			return sourceFiles[index] != null && 
//			sourceFiles[index].exists();
			return urlExists(index) || fileExists(index);
	}
	@util.annotations.Explanation("If the item is disabled, the source file for this class could not be located at the specified selected source directory.")
	public String[] getDynamicCommands() {
		return sourceClasses;
		//return frame.getCustomizeClassNames();
	}
	public void invokeDynamicCommand (String className) {
		int index = sourceClassesVector.indexOf(className);
		if (index == -1 )
			return;
		File sourceFile = sourceFiles[index];
		if (sourceFile != null)
			showSource(className, sourceFile);
		String sourceURL = sourceURLs[index];
		if (sourceURL != null) {
			ABasicHelpOperationsModel.showInExistingBrowser(sourceURL);
		}
//		frame.showSource(className);
	}
	
	@Visible(false)
	public static void showSource(String aClassName, File aSourceFile) {
		try {
		String text = util.misc.Common.toText(aSourceFile);
//		Hashtable attributeTable = new Hashtable();
//		attributeTable.put(AttributeNames.PREFERRED_WIDGET, VirtualTextArea.class.getName());
//		uiFrame editor = ObjectEditor.edit(text, attributeTable, null);
		uiFrame editor = (uiFrame) ObjectEditor.edit(text, VirtualTextArea.class);

		editor.setSize(500, 400);
		editor.setTitle(aClassName);
		} catch (Exception e) {
			Tracer.userMessage(e.getMessage());
			e.printStackTrace();
		}
		
		//defaultOE.source(c);
	}
	

}
