package bus.uigen.viewgroups;


import util.annotations.Visible;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTypeRegistry;
import bus.uigen.sadapters.VectorStructure;

public class AVectorNavigator implements OEView {
	VectorStructure vectorStructure;
	int startIndex;
	int endIndex;
	int filterSize;
	int lastVectorStructureSize;
	int maxPages;
	int numPages;
	int currentPageNumber;
	String[] commands = {"1"};
	boolean showCommands = true;
	
	void init () {
		initIndices();
		initPages();
		if (showCommands) {
		initCommands();
		initPositions();
		}
	}
	
	void initIndices () {
		startIndex = 0;
		endIndex = Math.min(filterSize, lastVectorStructureSize);
	}
	
	public AVectorNavigator(VectorStructure theVectorStructure, int theFilterSize, boolean theShowCommands) {
		vectorStructure = theVectorStructure;
		filterSize = theFilterSize;
		lastVectorStructureSize = vectorStructure.size();
		showCommands = theShowCommands;
		
		
		//ObjectEditor.setMethodAttribute(this, "1", AttributeNames.POSITION, 0 );
		init();
	}
	void initPages() {
		currentPageNumber = 0;
		numPages = lastVectorStructureSize/filterSize;
		int mod = lastVectorStructureSize % filterSize;
		if (mod != 0)
			numPages++;
		maxPages = numPages;
		
		
	}
	void initCommands() {
		
		//commands = new String[maxPages + 2];
		commands = new String[maxPages];
		//for (int i = 0; i < maxPages; i++) {
		for (int i = 0; i < maxPages; i++) {
			commands[i] = "" + (1 + i);
			//ObjectEditor.setMethodAttribute(this, commands[i], AttributeNames.POSITION, i );
		}
//		commands[maxPages] = "next";
//		commands[maxPages + 1] = "previous";
		
		
	}
	String leftMostCommand() {
		return "Previous";
//		if (commands.length == 0) {
//			return  "Next";
//		} 
//		return commands[0];
	}
	void initPositions() {
		String leftMostCommand = leftMostCommand();
//		ObjectEditor.setMethodAttribute(this, commands[0], AttributeNames.LABEL_LEFT, "");
//		ObjectEditor.setMethodAttribute(this, commands[0], AttributeNames.LABEL_WIDTH, 610 - (int) (maxPages*13.5));
//		ObjectEditor.setMethodAttribute(this, leftMostCommand, AttributeNames.LABEL_LEFT, "");
//		ObjectEditor.setMethodAttribute(this, leftMostCommand, AttributeNames.LABEL_WIDTH, 610 - (int) (maxPages*13.5));
		ObjectEditor.setMethodAttribute(this, "Previous", AttributeNames.COLUMN, 0 );
		for (int i = 0; i < maxPages; i++) {
		//for (int i = 0; i < maxPages + 2; i++) {
			//commands[i] = "" + (1 + i);
			ObjectEditor.setMethodAttribute(this, commands[i], AttributeNames.COLUMN, i+1 );
			//ObjectEditor.setMethodAttribute(this, commands[i], AttributeNames.COLUMN, i );
		}
		ObjectEditor.setMethodAttribute(this, "Next", AttributeNames.COLUMN, maxPages + 1 );
//		ObjectEditor.setMethodAttribute(this, "Previous", AttributeNames.COLUMN, maxPages + 1 );
	}
	public int size() {	
		int currentVectorStructureSize = vectorStructure.size();
		if (currentVectorStructureSize != lastVectorStructureSize) {
			lastVectorStructureSize = currentVectorStructureSize;
			initIndices();
			initPages();
		}
		//else
		//return Math.min(filterSize, vectorStructure.size() - startIndex);
		return endIndex - startIndex;
	}
	public Object elementAt(int pos) {
		return vectorStructure.elementAt(startIndex + pos);
	}
	public boolean preSetElementAt() {
		return vectorStructure.hasSetChildMethod();
	}
	public void setElementAt(Object element, int pos) {
		vectorStructure.setElementAt(element, startIndex + pos);
	}
	public boolean preNext() {
		return endIndex < vectorStructure.size();
	}
	public void next() {
		startIndex = Math.min(startIndex + filterSize, vectorStructure.size() - 1);
		moveEndIndex();
		currentPageNumber ++;
	}
	void moveEndIndex() {
		endIndex = Math.min(startIndex + filterSize, vectorStructure.size());
	}
	public boolean prePrevious() {
		return startIndex > 0;
	}
	 public void previous() {
		startIndex = Math.max(startIndex - filterSize, 0);
		endIndex = Math.max(startIndex + filterSize, filterSize);
		currentPageNumber--;
	}
	@Override
	//@Visible(false)
	public void retarget(Object realObject) {
		if (realObject == null)
			return;
		  ClassProxy c = RemoteSelector.getClass(realObject);
		ConcreteType realConcreteType = ConcreteTypeRegistry.createConcreteType(c, realObject, null);
		if (!(realConcreteType instanceof VectorStructure))
				return;
		vectorStructure = (VectorStructure) realConcreteType;
		//filterSize = theFilterSize;
		startIndex = 0;
		endIndex = filterSize;
		
	}
	public boolean preDynamicCommands (String theCommand) {
//		if (theCommand.equals("next"))
//			return preNext();
//		else if (theCommand.equals("previous"))
//			return prePrevious();
		int index = getIndex(theCommand);
		return (index != -1 && index != currentPageNumber && index < numPages) ;
		
	}
	
	public String[] getDynamicCommands() {
		return commands;
	}
	public String toString() {
		return "vector navigator of " + vectorStructure.getTargetObject().toString();
	}
	int getIndex(String theCommand) {
		int index;
		try {
		 index = Integer.parseInt(theCommand) - 1;
		} catch (Exception e) {
			index = -1;
			e.printStackTrace();
		}
		return index;
		
	}
	public void invokeDynamicCommand (String theCommand) {
//		if (theCommand.equals("next")) {
//			next();
//			return;
//		} else if (theCommand.equals("previous")) {
//			previous();
//			return;
//		}
		int index = getIndex(theCommand);
		currentPageNumber = index;
		startIndex = filterSize*index;
		moveEndIndex();
		
		
//		try {
//			fontSize = Integer.parseInt(theFontSizeString);
//			setFontSizesOfAllFrames();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	

}
