package bus.uigen.controller.models;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

import util.annotations.Visible;
import util.misc.Common;

import bus.uigen.ObjectEditor;
import bus.uigen.myLockManager;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeManager;
import bus.uigen.attributes.AttributeEditor;
import bus.uigen.controller.MethodParameters;
import bus.uigen.controller.SelectionManager;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.uiClassFinder;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ASourceOperationsModel extends ABasicSourceOperationsModel implements FrameModel {
//	uiFrame frame;
//	String[] sourceClasses;
//	Vector<String> sourceClassesVector;
//	File[] sourceFiles;
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {		
//		frame = theFrame;
		super.init(theFrame, theObject, theObjectAdapter);
//		String[] template = {};
//		sourceClasses = frame.getSourceClassNames().toArray(template);
//		sourceFiles = new File[sourceClasses.length];
//		sourceClassesVector = Common.deepArrayToVector(sourceClasses);
//		refreshSourceFiles();		
//		frame.getModelRegistry().registerSourceModel(this);
	}
	
	 void initFileDialog () {
		if (fileDialog != null)
			return;
		//sourceClasses = util.Misc.filterOutJavaClasses(frame.getCustomizeClassNames());
//		String[] template = {};
//		sourceClasses = frame.getSourceClassNames().toArray(template);
//		sourceFiles = new File[sourceClasses.length];
//		sourceClassesVector = Misc.deepArrayToVector(sourceClasses);
		refreshSourceFiles();
		fileDialog = new JFileChooser();
		fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	
		//frame.getModelRegistry().registerSourceModel(this);
		try {
		File currentDirectory = new File(".");
		fileDialog.setCurrentDirectory(currentDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//fileDialog.set
	}	
	/*
	public void init (uiFrame theFrame, Object theObject) {
//		if (fileDialog != null)
//			return;
		frame = theFrame;
		//sourceClasses = util.Misc.filterOutJavaClasses(frame.getCustomizeClassNames());
		String[] template = {};
		sourceClasses = frame.getSourceClassNames().toArray(template);
		sourceFiles = new File[sourceClasses.length];
		sourceClassesVector = Misc.deepArrayToVector(sourceClasses);
		refreshSourceFiles();
		fileDialog = new JFileChooser();
		fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	
		frame.getModelRegistry().registerSourceModel(this);
		try {
		File currentDirectory = new File(".");
		fileDialog.setCurrentDirectory(currentDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//fileDialog.set
	}
	*/
	
//	void refreshSourceFiles() {
//		for (int i = 0; i < sourceClasses.length; i++) {
//			sourceFiles[i] = frame.open(sourceClasses[i]);
//		}		
//	}
	
	public void allSource() {
		for (int i = 0; i < sourceClasses.length; i++) {
			//ObjectEditor.showSource(sourceClasses[i]);
			frame.showSource(sourceClasses[i]);
		}
		
	}
	
	//static FileDialog   fileDialog = new FileDialog(new Frame(), "Choose Source Root Folder", FileDialog.LOAD);
	static JFileChooser   fileDialog;
	@util.annotations.Explanation ("Sets the root of the directory/folder containing source files")
	public  void setSourceDirectory() {
		initFileDialog();
		try {
			int retVal = fileDialog.showOpenDialog((Component) frame.getContainer().getPhysicalComponent());
		//fileDialog.setVisible(true);
		//fileDialog.setVisible(true);
		
		if (retVal == JFileChooser.APPROVE_OPTION)
		frame.setGlobalSourceDirectory(fileDialog.getSelectedFile().getCanonicalPath());
		refreshSourceFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//frame.setSourceDirectory(fileDialog.getDirectory());		
	}
	
//	public  void separateSrcBinDirectories() {
//		
//		try {
//			frame.setGlobalSourceDirectory("../src");
//			refreshSourceFiles();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		//frame.setSourceDirectory(fileDialog.getDirectory());		
//	}
	
//	public boolean preDynamicCommands(String className) {
//		int index = sourceClassesVector.indexOf(className);
//		if (index < 0)
//			return false;
//		else
//			return sourceFiles[index] != null && 
//			sourceFiles[index].exists();
//	}
//	@util.annotations.Explanation("If the item is disabled, the source file for this class could not be located at the specified selected source directory.")
//	public String[] getDynamicCommands() {
//		return sourceClasses;
//		//return frame.getCustomizeClassNames();
//	}
//	public void invokeDynamicCommand (String className) {
//		frame.showSource(className);
//	}
		/*
		//if (uiParameters.EditBeanInfo) {
			try {
				//Frame editor = uiGenerator.generateUIFrame(ClassDescriptorCache.getClassDescriptor(Class.forName(name)), (myLockManager) null);
				//uiFrame editor = uiGenerator.generateUIFrame(ClassDescriptorCache.getClassDescriptor(uiClassFinder.forName(className)), (myLockManager) null);
				//editor.setVisible(true);
				Class c = uiClassFinder.forName(className);
				ObjectEditor.showSource(c);
			}   catch (Exception ex) {}
		}
		//else {
			//Frame   editor = new uiAttributeEditor(AttributeManager.getEnvironment().getClassAttributeManager(className));
		//}
	}
	*/
	/*
	public Vector<VirtualMethod> getVirtualMethods() {
		return frame.getConstructors();
	}
	*/

}
