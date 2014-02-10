package bus.uigen.controller;import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;import java.util.Hashtable;

public class MenuSetter extends Hashtable {

	
	//Hashtable menuNameHash = new Hashtable();
	
	public MenuSetter() {
	
	//BELOW ARE THOSE THAT WOULD NORMALLY BE EXTENDED FROM THIS CLASS
	//put("Methods Menu", new Boolean(true));   //not sure if this will work.		//put("Television", new Boolean(true));   //not sure if this will work.	
		
	put("On", new Boolean(true));   //not sure if this will work.			put("Get Channel", new Boolean(true));   //not sure if this will work.		
	put("Channel DOWN", new Boolean(true));   //not sure if this will work.		
	put("Set Volume ...", new Boolean(true));   //not sure if this will work.		
		//____________________________________________________________________
			
		
		
		
	//omitted from file
	put("File", new Boolean(true));
	put("New", new Boolean(true));	
	put("Exit", new Boolean(true));		
			//omitted from edit	put("Edit", new Boolean(true));
	put("Cut", new Boolean(true));
	put("Copy", new Boolean(true));	put("Paste", new Boolean(true));	put("Clear", new Boolean(true));
	put("Select Up", new Boolean(true));
	put("Select Down", new Boolean(true));
	put("Select All", new Boolean(true));
	put("Settings", new Boolean(true));
	put("Selection", new Boolean(true));		//omitted from view	put("View", new Boolean(true));

	//omitted from Customize?
	put("Selected", new Boolean(true));	
	//public static	final String BEAN_METHODS_MENU_NAME	= "Bean";	put(uiFrame.BEAN_METHODS_MENU_NAME, new Boolean(true));   //not sure if this will work.
	//put("Bean", new Boolean(true));   //not sure if this will work.
	
		//public static	final String ATTRIBUTES_MENU_NAME =	"Customize";  
	//put(uiFrame.ATTRIBUTES_MENU_NAME, new Boolean(true));	put(uiFrame.CUSTOMIZE_MENU, new Boolean(true));		
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
	put(uiFrame.TREE_PANEL_COMMAND, new Boolean(true));
		//public static	final String DRAW_PANEL_COMMAND	= "Drawing";	put(uiFrame.DRAW_PANEL_COMMAND, new Boolean(true));	
	//public static	final String MAIN_PANEL_COMMAND	= "Main	Panel";	put(uiFrame.MAIN_PANEL_COMMAND, new Boolean(true));	
	//public static	final String TOOLBAR_COMMAND = "Toolbar";
	put(uiFrame.TOOLBAR_COMMAND, new Boolean(true));
		//public static	final String WINDOW_HISTORY_PANEL_COMMAND =	"Windows";	  	put(uiFrame.WINDOW_HISTORY_PANEL_COMMAND, new Boolean(true));	
	//public static	final String SAVE_COMMAND =	"Save";	  	put(uiFrame.SAVE_COMMAND, new Boolean(true));	
	//public static	final String SAVE_AS_FILE_COMMAND =	"Save As...";		put(uiFrame.SAVE_AS_FILE_COMMAND, new Boolean(true));	
	//public static	final String SAVE_TEXT_AS_FILE_COMMAND = "Save Text	As...";	   	put(uiFrame.SAVE_TEXT_AS_FILE_COMMAND, new Boolean(true));	
	//public static	final String SAVE_TEXT_FILE_COMMAND	= "Save	Text...";	put(uiFrame.SAVE_TEXT_FILE_COMMAND, new Boolean(true));	
	//public static	final String OPEN_FILE_COMMAND = "Open...";	
	put(uiFrame.OPEN_FILE_COMMAND, new Boolean(true));
		//public static	final String LOAD_FILE_COMMAND = "Load...";		put(uiFrame.LOAD_FILE_COMMAND, new Boolean(true));	
	//public static	final String UPDATE_COMMAND	= "Update";	put(uiFrame.UPDATE_COMMAND, new Boolean(true));	
	//public static	final String UPDATE_ALL_COMMAND	= "UpdateAll";		put(uiFrame.UPDATE_ALL_COMMAND, new Boolean(true));	
	//public static	final String DONE_COMMAND =	"Done";	 
	put(uiFrame.DONE_COMMAND, new Boolean(true));
		//public static	final String MAIN_PANEL_NAME = "main"; 
	put(AttributeNames.MAIN_PANEL_NAME, new Boolean(true));
	
	}
	
	
}