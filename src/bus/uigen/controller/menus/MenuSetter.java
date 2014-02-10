package bus.uigen.controller.menus;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;import java.util.Hashtable;
// this class seems to have not effect any longer
public class MenuSetter extends Hashtable<String, Boolean> {

	
	//Hashtable menuNameHash = new Hashtable();	public MenuSetter() {//		addAll();	}
	
	public void addAll() {
	
	
			
		
		
		
	//omitted from file
	put(AttributeNames.FILE_MENU, new Boolean(true));
	put(AttributeNames.NEW_OBJECT_MENU_NAME, new Boolean(true));	
	put("Exit", new Boolean(true));		
			//omitted from edit	// keep these for now	put("Edit", new Boolean(true));
	put("Cut", new Boolean(true));
	put("Copy", new Boolean(true));	put("Paste", new Boolean(true));	put("Clear", new Boolean(true));
	put("Select Up", new Boolean(true));
	put("Select Down", new Boolean(true));
	put("Select All", new Boolean(true));
	put("Settings", new Boolean(true));
	put("Selection", new Boolean(true));//	public static	final String UPDATE_COMMAND	= "Update";	put(uiFrame.UPDATE_COMMAND, new Boolean(true));				put(uiFrame.EDIT_MENU, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.COPY_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.PASTE_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.CLEAR_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SELECT_UP_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SELECT_DOWN_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SELECT_ALL_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SETTINGS_COMMAND, new Boolean(true));	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SELECTION_COMMAND, new Boolean(true));//	public static	final String UPDATE_COMMAND	= "Update";	put(uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER +uiFrame.UPDATE_COMMAND, new Boolean(true));		/*	put("Copy", new Boolean(true));	put("Paste", new Boolean(true));	put("Clear", new Boolean(true));	put("Select Up", new Boolean(true));	put("Select Down", new Boolean(true));	put("Select All", new Boolean(true));	put("Settings", new Boolean(true));	put("Selection", new Boolean(true));	*/		int i = 0;		//omitted from view	//put("View", new Boolean(true));	//public static	final String ATTRIBUTES_MENU_NAME =	"Customize";    put(uiFrame.CUSTOMIZE_MENU, new Boolean(true));//	omitted from Customize?	put(uiFrame.SELECTED_COMMAND, new Boolean(true));		put(uiFrame.CUSTOMIZE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SELECTED_COMMAND, new Boolean(true));		put(AttributeNames.VIEW_MENU, new Boolean(true));

		
	//public static	final String BEAN_METHODS_MENU_NAME	= "Bean";	put(uiFrame.BEAN_METHODS_MENU_NAME, new Boolean(true));   //not sure if this will work.
	//put("Bean", new Boolean(true));   //not sure if this will work.
	
	
	//public static	final String REFRESH_COMMAND = "Refresh";	put(uiFrame.REFRESH_COMMAND, new Boolean(true));
		//public static	final String AUTO_REFRESH_COMMAND =	"Auto Refresh";
	put(uiFrame.AUTO_REFRESH_COMMAND, new Boolean(true));
	
	//public static	final String AUTO_REFRESH_ALL_COMMAND =	"Auto Refresh All Frames";	put(uiFrame.AUTO_REFRESH_ALL_COMMAND, new Boolean(true));	/*
	//public static	final String DEEP_ELIDE_2 =	"Expand	2";	put(uiFrame.DEEP_ELIDE_2, new Boolean(true));	*/
	/*	
	//public static	final String DEEP_ELIDE_3 =	"Expand	3";	put(uiFrame.DEEP_ELIDE_3, new Boolean(true));
	*/	
	//public static	final String DEEP_ELIDE_4 =	"Expand	4";
	put(uiFrame.DEEP_ELIDE_4, new Boolean(true));
		//public static	final String ELIDE_COMMAND = "Expand/Collapse";	put(uiFrame.ELIDE_COMMAND, new Boolean(true));	
	//public static	final String FORWARD_ADAPTER_NAME =	"Forward";		put(uiFrame.FORWARD_ADAPTER_NAME, new Boolean(true));	
	//public static	final String BACK_ADAPTER_NAME = "Back";	put(uiFrame.BACK_ADAPTER_NAME, new Boolean(true));	
	//public static	final String REPLACE_FRAME_COMMAND = "Replace Window";		put(uiFrame.REPLACE_FRAME_COMMAND, new Boolean(true));	
	//public static	final String NEW_FRAME_COMMAND = "New Editor";	put(uiFrame.NEW_FRAME_COMMAND, new Boolean(true));	
	//public static	final String NEW_TEXT_FRAME_COMMAND	= "New Text	Editor";
	put(uiFrame.NEW_TEXT_FRAME_COMMAND, new Boolean(true));
		//public static	final String NEW_SCROLL_PANE_COMMAND = "New	Window Right";
	put(uiFrame.NEW_SCROLL_PANE_COMMAND, new Boolean(true));		//public static	final String NEW_SCROLL_PANE_BOTTOM_COMMAND	= "New Window Bottom";
	put(uiFrame.NEW_SCROLL_PANE_BOTTOM_COMMAND, new Boolean(true));
		//public static	final String TREE_PANEL_COMMAND	= "Tree";
	put(uiFrame.TREE_PANEL_COMMAND, new Boolean(true));		//public static	final String TREE_PANEL_COMMAND	= "Tree";		put(uiFrame.GRAPH_LOGICAL_STRUCTURE_COMMAND, new Boolean(true));
		//public static	final String DRAW_PANEL_COMMAND	= "Drawing";	put(uiFrame.DRAW_PANEL_COMMAND, new Boolean(true));	
	//public static	final String MAIN_PANEL_COMMAND	= "Main	Panel";	put(uiFrame.MAIN_PANEL_COMMAND, new Boolean(true));		// let us add the full name//	public static	final String REFRESH_COMMAND = "Refresh";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.REFRESH_COMMAND, new Boolean(true));		//public static	final String AUTO_REFRESH_COMMAND =	"Auto Refresh";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.AUTO_REFRESH_COMMAND, new Boolean(true));		//public static	final String AUTO_REFRESH_ALL_COMMAND =	"Auto Refresh All Frames";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.AUTO_REFRESH_ALL_COMMAND, new Boolean(true));	/*	//public static	final String DEEP_ELIDE_2 =	"Expand	2";	put(uiFrame.DEEP_ELIDE_2, new Boolean(true));	*/	/*		//public static	final String DEEP_ELIDE_3 =	"Expand	3";	put(uiFrame.DEEP_ELIDE_3, new Boolean(true));	*/		//public static	final String DEEP_ELIDE_4 =	"Expand	4";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.DEEP_ELIDE_4, new Boolean(true));		//public static	final String ELIDE_COMMAND = "Expand/Collapse";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.ELIDE_COMMAND, new Boolean(true));		//public static	final String FORWARD_ADAPTER_NAME =	"Forward";		put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.FORWARD_ADAPTER_NAME, new Boolean(true));		//public static	final String BACK_ADAPTER_NAME = "Back";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.BACK_ADAPTER_NAME, new Boolean(true));		//public static	final String REPLACE_FRAME_COMMAND = "Replace Window";		put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.REPLACE_FRAME_COMMAND, new Boolean(true));		//public static	final String NEW_FRAME_COMMAND = "New Editor";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_FRAME_COMMAND, new Boolean(true));		//public static	final String NEW_TEXT_FRAME_COMMAND	= "New Text	Editor";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_TEXT_FRAME_COMMAND, new Boolean(true));		//public static	final String NEW_SCROLL_PANE_COMMAND = "New	Window Right";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_SCROLL_PANE_COMMAND, new Boolean(true));		//public static	final String NEW_SCROLL_PANE_BOTTOM_COMMAND	= "New Window Bottom";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_SCROLL_PANE_BOTTOM_COMMAND, new Boolean(true));		//public static	final String TREE_PANEL_COMMAND	= "Tree";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.TREE_PANEL_COMMAND, new Boolean(true));		//public static	final String DRAW_PANEL_COMMAND	= "Drawing";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.DRAW_PANEL_COMMAND, new Boolean(true));		//public static	final String MAIN_PANEL_COMMAND	= "Main	Panel";	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.MAIN_PANEL_COMMAND, new Boolean(true));	
	//public static	final String TOOLBAR_COMMAND = "Toolbar";
	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.TOOLBAR_COMMAND, new Boolean(true));
		//public static	final String WINDOW_HISTORY_PANEL_COMMAND =	"Windows";	  	put(AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.WINDOW_HISTORY_PANEL_COMMAND, new Boolean(true));		// File commands
	//public static	final String SAVE_COMMAND =	"Save";	  	put(uiFrame.SAVE_COMMAND, new Boolean(true));	
	//public static	final String SAVE_AS_FILE_COMMAND =	"Save As...";		put(uiFrame.SAVE_AS_FILE_COMMAND, new Boolean(true));	
	//public static	final String SAVE_TEXT_AS_FILE_COMMAND = "Save Text	As...";	   	put(uiFrame.SAVE_TEXT_AS_FILE_COMMAND, new Boolean(true));	
	//public static	final String SAVE_TEXT_FILE_COMMAND	= "Save	Text...";	put(uiFrame.SAVE_TEXT_FILE_COMMAND, new Boolean(true));	
	//public static	final String OPEN_FILE_COMMAND = "Open...";	
	put(uiFrame.OPEN_FILE_COMMAND, new Boolean(true));
		//public static	final String LOAD_FILE_COMMAND = "Load...";		put(uiFrame.LOAD_FILE_COMMAND, new Boolean(true));	
	//public static	final String UPDATE_ALL_COMMAND	= "UpdateAll";		put(uiFrame.UPDATE_ALL_COMMAND, new Boolean(true));	
	//public static	final String DONE_COMMAND =	"Done";	 
	put(uiFrame.DONE_COMMAND, new Boolean(true));	//	 File commands	//public static	final String SAVE_COMMAND =	"Save";	  	put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SAVE_COMMAND, new Boolean(true));		//public static	final String SAVE_AS_FILE_COMMAND =	"Save As...";		put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SAVE_AS_FILE_COMMAND, new Boolean(true));		//public static	final String SAVE_TEXT_AS_FILE_COMMAND = "Save Text	As...";	   	put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SAVE_TEXT_AS_FILE_COMMAND, new Boolean(true));		//public static	final String SAVE_TEXT_FILE_COMMAND	= "Save	Text...";	put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.SAVE_TEXT_FILE_COMMAND, new Boolean(true));		//public static	final String OPEN_FILE_COMMAND = "Open...";		put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.OPEN_FILE_COMMAND, new Boolean(true));		//public static	final String LOAD_FILE_COMMAND = "Load...";		put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.LOAD_FILE_COMMAND, new Boolean(true));		//public static	final String UPDATE_ALL_COMMAND	= "UpdateAll";		put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.UPDATE_ALL_COMMAND, new Boolean(true));		//public static	final String DONE_COMMAND =	"Done";	 	put(AttributeNames.FILE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.DONE_COMMAND, new Boolean(true));		
		//public static	final String MAIN_PANEL_NAME = "main"; 
	put(AttributeNames.MAIN_PANEL_NAME, new Boolean(true));	String[] otherMenus = {uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.UNDO_COMMAND, 			uiFrame.EDIT_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.REDO_COMMAND};	addMenus (otherMenus);
	
	}	
	public void addMenus (String[] menuNames) {		for (int i = 0; i <  menuNames.length; i++)			this.put(menuNames[i], true);					}	public void removeMenus (String[] menuNames) {		for (int i = 0; i <  menuNames.length; i++)			this.put(menuNames[i], false);					}		public Boolean get (String key) {		Boolean retVal = super.get (key);		if (retVal == null) return true;		return retVal;		//return retVal != null && retVal;	}	
	
}