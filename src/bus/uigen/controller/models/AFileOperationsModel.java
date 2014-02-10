package bus.uigen.controller.models;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import util.annotations.Visible;

import bus.uigen.AutomaticRefresh;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.view.SaveAsListener;
import bus.uigen.visitors.IsSerializableAdapterVisitor;
import bus.uigen.visitors.ToTextAdapterVisitor;
import bus.uigen.visitors.UpdateAdapterVisitor;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AFileOperationsModel extends ABasicFileOperationsModel implements FrameModel, SaveAsListener {
//	uiFrame frame;
//	Vector<MethodProxy> constructors = new Vector();
//	Object obj;
//	public AFileOperationsModel (uiFrame theFrame) {
//		frame = theFrame;
//	}
	public AFileOperationsModel () {
		
	}
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//		frame.getBrowser().addSaveAsListener(this);
//		//obj = frame.getBrowser().getOriginalAdapter().getRealObject();
//		obj = theObject;
//		saveFileName = OEMisc.objectToSavedFile(obj);
//		
//	}
	/*
	Hashtable<String, Object> commandTable = new Hashtable();
	public  String[] getDynamicCommands () {
		String[] commands = new String[commandTable.size()];
		Enumeration<String> keys = commandTable.keys();
		int i = 0;
		while (keys.hasMoreElements()) {
			commands[i] = keys.nextElement();
			i++;
		}
		return commands;
	}
	
	public Object invokeDynamicCommand(String command) {
		
	}
	*/
	
	public Vector<MethodProxy> getVirtualMethods() {
		return frame.getConstructors();
	}
	
	
		
	public void open() {
		doOpen();
		
	}
	public boolean preSave() {
		//return saveEnabled;
		return saveAsEnabled && saveFileName != null;
	}
	public void save() {
		doSave();
		
	}
	boolean saveAsEnabled = false;
	@Visible(false)
	public void saveAsEnabled (boolean newVal) {
		saveAsEnabled = newVal;
	}
	public boolean preSaveAs() {
		
		//return isSavable(frame.getOriginalAdapter());
		//return uiFrame.isSavable(adapter);
		return saveAsEnabled;
		
		
	}
	public void saveAs() {
		doSaveAs();
	}
	
	public void saveTextAs() {
		doSaveTextAs();

	}
	public void load() {
		doLoad();
		
	}
	public void updateAll() {
		doUpdateAll();
	}
	public void exit() {
		doExit();
	}
	
	public boolean preDone() {
		return frame.doneEnabled();
	}
	
	public void done() {
		frame.notifyDoneListeners();
		
	}
	/*
	public void addConstructor(VirtualMethod m) {
		if (constructors.contains(m))
			return;
		constructors.add(m);
		
	}
	*/
	String saveFileName =   null;
	String lastFileName;
	String lastDirectoryName;
	String lastSaveFileName;
	String lastTextDirectoryName;
	String lastTextFileName;
	String lastSaveDirectoryName;
	static FileDialog   fileDialog = new FileDialog(new Frame(), "Save as", FileDialog.SAVE);
	static FileDialog   textFileDialog = new FileDialog(new Frame(), "Save Text as", FileDialog.SAVE);
	void setFileDirectoryPair(FileDialog fileDialog) {
		if (lastFileName == null)
			fileDialog.setFile(frame.getTitle() + ".obj");
		else
			fileDialog.setFile(lastFileName);
		if (lastDirectoryName   != null)
			fileDialog.setDirectory(lastDirectoryName);
		/*
		AFileDirectoryPair fdp = fileDirectoryMapping.get(obj.getClass());
		if (fdp == null)
		fileDialog.setFile(this.getTitle() + ".obj");
		else {
		fileDialog.setFile(fdp.getDirectoryName());
		fileDialog.setFile(fdp.getFileName());
		}
		*/
	}
	void doSave() {
		if (saveFileName != null)
			
			saveState(saveFileName);
	}
	void doSaveAs() {
		//Object obj = getOriginalAdapter().getRealObject();
		//if (obj   == null) return;
		//FileDialog fileDialog = new FileDialog(new Frame(), "Save as", FileDialog.SAVE);
		fileDialog.setMode(FileDialog.SAVE);
		setFileDirectoryPair(textFileDialog);
		/*
		AFileDirectoryPair fdp = fileDirectoryMapping.get(obj.getClass());
		if (fdp == null)
		fileDialog.setFile(this.getTitle() + ".obj");
		else {
		fileDialog.setFile(fdp.getDirectoryName());
		fileDialog.setFile(fdp.getFileName());
		}
		*/
		//fileDialog.setFile(this.getTitle() + ".obj");
		fileDialog.setVisible(true);
		//System.out.println (fileDialog.getFile());
		String fileName = fileDialog.getFile();
		String directoryName = fileDialog.getDirectory();
		if (fileName == null) return; 
		//fileDirectoryMapping.put (new AFileDirectoryPair(directory, file));
		lastFileName = fileName;
		lastDirectoryName = directoryName;
		setSaveFileName (lastDirectoryName + lastFileName + ".obj");
		//saveFileName = lastDirectoryName + lastFileName;
		saveState(directoryName+System.getProperty("file.separator")+fileName +".obj");
	}
	boolean saveEnabled = false;
	void setSaveFileName(String newVal) {
		if ((newVal == null) || (newVal.equals("")))
			return;
		saveFileName = newVal;
		//saveItem.setLabel("Save "   + getShortFileName(newVal));
		frame.setAutomaticTitle(newVal);
		
		//saveItem.setName("Save " + newVal);
		saveEnabled = true;
	}
	void saveState(String   fileName) {
		// Try to write out the object
		doUpdateAll();

		frame.setCursor(Cursor.WAIT_CURSOR);

		//Object obj = getTopAdapter().getRealObject();
		Object obj = frame.getBrowser().getOriginalAdapter().getRealObject();
		//String fileName = directory+System.getProperty("file.separator")+file;
		try {
			//ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream(directory+System.getProperty("file.separator")+file));
			ObjectOutputStream f = new ObjectOutputStream(new   FileOutputStream(fileName));
			f.writeObject(obj);
			f.close();
		}
		catch (Exception e) {
			// Error in writing object
			JOptionPane.showMessageDialog(null, 
				//"Error writing object   in directory "+directory,
				"Error writing object  "+fileName,
				"Error",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			
		}
		/*   
		// Write out the class attributes (viz the AttributeManager)
		try {
		//ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream(directory+System.getProperty("file.separator")+"class_attributes"));
		ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream(fileName +"class_attributes"));
		f.writeObject(AttributeManager.getEnvironment());
		f.close();
		} catch (Exception e) {
		}
		
		try {
		Hashtable   instanceAttributes = InstanceAttributesToHashtable.getHashtable(getAdapter());
		//ObjectOutputStream f = new ObjectOutputStream(new FileOutputStream(directory+System.getProperty("file.separator")+"instance_attributes"));
		ObjectOutputStream f = new ObjectOutputStream(new   FileOutputStream(fileName+"instance_attributes"));
		f.writeObject(instanceAttributes);
		f.close();
		} catch (Exception e) {
		}
		*/
		frame.setEdited(false);
		frame.setCursor(Cursor.DEFAULT_CURSOR);
	}
	static boolean isSavable (ObjectAdapter adapter) {
		return !((new   IsSerializableAdapterVisitor(adapter)).traverse()).contains(new Boolean(false));
		/*
		try {
		ObjectOutputStream f = new ObjectOutputStream(new   FileOutputStream());
		f   = new ObjectOutputStream (new FileOutputStream (new File(
		f.writeObject(obj);
		f.close();
		return true;
		}
		catch (Exception e) {
		// Error in writing object
		return false;
		
		}
		*/
	}
	void doLoad() {
		//fileDialog = new FileDialog(new   Frame(), "Load from", FileDialog.LOAD);
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setTitle("Load");
		setFileDirectoryPair(fileDialog);
		//fileDialog.setFile("serialized.obj");
		//fileDialog.setFile(this.getTitle() + ".obj");
		fileDialog.setVisible(true);
		String fileName = fileDialog.getFile();
		if (fileName == null) return; 
		loadState(fileDialog.getDirectory(), fileName);
		lastFileName = fileName;
		lastDirectoryName = fileDialog.getDirectory();
	}

	//
	void loadState(String   directory, String file) {
		// Try to load the object
		frame.setCursor(Cursor.WAIT_CURSOR);

		try {
			ObjectInputStream   f = new ObjectInputStream(new FileInputStream(directory+System.getProperty("file.separator")+file));
			Object obj = f.readObject();
			f.close();
			frame.getBrowser().getAdapter().refreshValue(obj);
		} catch (Exception e) {
			// Error in writing object
			JOptionPane.showMessageDialog(null, 
				"Error reading object from "+directory+file,
				"Error",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		//loadAttributes(directory, file);
		/*
		// Load the class attributes
		try {
		ObjectInputStream   f = new ObjectInputStream(new FileInputStream(directory+System.getProperty("file.separator")+"class_attributes"));
		uiAttributeManager m = (uiAttributeManager) f.readObject();
		f.close();
		AttributeManager.setEnvironment(m);
		} catch (Exception e) {
		e.printStackTrace();
		}

		// Load the instance attributes
		try {
		ObjectInputStream   f = new ObjectInputStream(new FileInputStream(directory+System.getProperty("file.separator")+"instance_attributes"));
		Hashtable   table = (Hashtable) f.readObject();
		f.close();
		HashtableToInstanceAttributes.setHashtable(getAdapter(), table);
		} catch (Exception e) {
		e.printStackTrace();
		}
		try {
		refreshAttributes("");
		} catch (Exception e) {
		e.printStackTrace();
		}
		*/
		frame.setCursor(Cursor.getDefaultCursor());
	}
	void doOpen() {
		//fileDialog = new FileDialog(new   Frame(), "Load from", FileDialog.LOAD);
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setTitle("Open");
		setFileDirectoryPair(fileDialog);
		//fileDialog.setFile("serialized.obj");
		fileDialog.setFile(frame.getTitle() + ".obj");
		fileDialog.setVisible(true);
		String fileName = fileDialog.getFile();
		if (fileName == null) return; 
		openState(fileDialog.getDirectory(), fileName);
		lastFileName = fileName;
		lastDirectoryName = fileDialog.getDirectory();
	}
	void openState(String   directory, String file) {
		// Try to load the object
		String fullName = directory+file;
		//uiFrame f = uiGenerator.generateUIFrameFromFile(fullName);
		Object obj = uiGenerator.getSavedObject(fullName);
		uiFrame editor = ObjectEditor.edit(obj);
		setSaveFileName(fullName);
		frame.setVisible(true);
		//f.loadAttributes(directory, file);

		
	}
	
	void doSaveTextAs() {
		//Object obj = getOriginalAdapter().getRealObject();
		//if (obj   == null) return;
		//FileDialog fileDialog = new FileDialog(new Frame(), "Save as", FileDialog.SAVE);
		textFileDialog.setMode(FileDialog.SAVE);
		setTextFileDirectoryPair(fileDialog);
		/*
		AFileDirectoryPair fdp = fileDirectoryMapping.get(obj.getClass());
		if (fdp == null)
		fileDialog.setFile(this.getTitle() + ".obj");
		else {
		fileDialog.setFile(fdp.getDirectoryName());
		fileDialog.setFile(fdp.getFileName());
		}
		*/
		//fileDialog.setFile(this.getTitle() + ".obj");
		textFileDialog.setVisible(true);
		//System.out.println (fileDialog.getFile());
		lastTextFileName = textFileDialog.getFile();
		lastTextDirectoryName = textFileDialog.getDirectory();
		if (lastTextFileName == null) return; 
		//saveFileName = lastDirectoryName + lastFileName;
		saveText(lastTextDirectoryName+System.getProperty("file.separator")+lastTextFileName +".txt");
	}
	void setTextFileDirectoryPair(FileDialog fileDialog) {
		if (lastTextFileName == null)   
			fileDialog.setFile(frame.getTitle());
		else
			fileDialog.setFile(lastTextFileName);
		if (lastTextDirectoryName   != null)
			fileDialog.setDirectory(lastTextDirectoryName);
		else if (lastDirectoryName !=   null)
			fileDialog.setDirectory(lastDirectoryName);
		/*
		AFileDirectoryPair fdp = fileDirectoryMapping.get(obj.getClass());
		if (fdp == null)
		fileDialog.setFile(this.getTitle() + ".obj");
		else {
		fileDialog.setFile(fdp.getDirectoryName());
		fileDialog.setFile(fdp.getFileName());
		}
		*/
	}
	private void saveText(String fileName) {

		frame.setCursor(Cursor.WAIT_CURSOR);
		Vector results = new Vector();
		StringBuffer sb = new StringBuffer();
		(new ToTextAdapterVisitor(frame.getBrowser().getOriginalAdapter())).traverse(frame.getBrowser().getOriginalAdapter(), results, 0, 0);
		for (int i = 0; i < results.size(); i++ ) {
			sb.append((String) results.elementAt(i));
			System.out.println(results.elementAt(i));
		}
		System.out.println(fileName);
		//System.out.println(sb.toString());
		

		try {
			
			PrintStream f = new  PrintStream(new FileOutputStream(fileName));
			f.println(sb.toString());
			f.close();
		}
		catch (Exception e) {
			// Error in writing object
			JOptionPane.showMessageDialog(null, 
				//"Error writing object   in directory "+directory,
				"Error writing object  "+fileName,
				"Error",
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			
		}   
		frame.setCursor(Cursor.DEFAULT_CURSOR);
	}
	
	void doUpdateAll () {
		// for some reason this was commented out
		subDoUpdateAll();
		if(!ObjectEditor.shareBeans()){
		subDoUpdateAll();
		} else{
		ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,"doUpdateAll"));
		}
		
	}
	void subDoUpdateAll () {
		for (Enumeration adapters   = frame.getBrowser().getAdapterHistory().elements(); adapters.hasMoreElements();)
			(new UpdateAdapterVisitor(((ObjectAdapter) adapters.nextElement()))).traverse();
		
		//setEdited(false);
		//doUpdate(getOriginalAdapter());
		//(new UpdateAdapterVisitor(getOriginalAdapter())).traverse();
		frame.doImplicitRefresh();
	}
	
	void doExit() {
		if (frame.isTopFrame()) {
			System.exit(0);
		}
	}
	
	
	}
	
