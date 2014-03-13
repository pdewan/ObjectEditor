package bus.uigen.attributes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Hashtable;
import java.util.Vector;

import util.web.DocPackageRegistry;
import bus.uigen.uiFrame;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.view.OEGridLayout;


//import bus.uigen.attributes.Direction;

public class AttributeNames {
	@util.annotations.Explanation("Determines the position of a method or property of a displayed object")
	public static final String POSITION = "Position";

	public static final String LABEL = "Label";
	
	public static final String PROMPT = "Prompt";

	public static final String LAYOUT_MANAGER_FACTORY = "Layout Manager Factory";
	
	public static final String HORIZONTAL_BOUND_GAP = "Horizontal Bound Gap"; // for methods/buttons
	
	public static final String HORIZONTAL_GAP = "Horizontal Gap"; // for properties
	
	
	public static final String VERTICAL_GAP = "Vertical Gap";





	public static final String CONTAINER_FACTORY = "Container Factory";

	public static final String LABEL_ABOVE = "Label Above";

	public static final String LABEL_BELOW = "Label Below";

	public static final String LABEL_LEFT = "Label Left";

	public static final String LABEL_SUFFIX = "Label Suffix";

	/*
	 * public static final String ICON_RIGHT = "icon_right"; public static final
	 * String ICON_ABOVE = "icon_above"; public static final String ICON_BELOW=
	 * "icon_below"; public static final String ICON_LEFT = "icon_left";
	 */
	public static final String LABEL_RIGHT = "Label Right";

	public static final String LABEL_POSITION = "Label Position";
	
	
	public static final String UPPER_CASE = "Upper Case";
	public static final String LOWER_CASE = "Lower Case";
	public static final String MIXED_CASE = "Mixed Case";
	public static final String LABEL_CASE = "Label Case";

	


	public static final String LABEL_HAS_NO_BLANKS = "Label has no Blanks";


	// these are essentially String enums for label position
	public static final String LABEL_IS_LEFT = "Label Is Left";

	public static final String LABEL_IS_RIGHT = "Label Is Right";

	public static final String LABEL_IS_ABOVE = "Label Is Above";

	public static final String LABEL_IS_BELOW = "Label Is Below";

	public static final String LABEL_IN_BORDER = "Label In Border";

	public static final String LABEL_IN_UNBOUND = "Label In Unbound";

	public static final String SEPARATE_UNBOUND = "Separate Unbound";

	public static final String SEPARATE_UNBOUND_TITLES = "Separate Unbound Titles";
	
	public static final String BORDER_LAYOUT = "Border Layout";
	
	public static final String GRID_BAG_LAYOUT = "Grid Bag Layout";

	
	public static final String NO_LAYOUT = "No Layout";

	
	public static final String LAYOUT = "Layout";
	public static final String UNBOUND_LAYOUT = "Unbound Layout";

	
	public static final String GRID_LAYOUT = "Grid Layout";
	
	public static final String FLOW_LAYOUT = "Flow Layout";
	
	public static final String ALIGNMENT = "Alignment";
	
	public static final String CENTER = "Center";
	
	public static final String LEFT = "Left";
	
	public static final String RIGHT = "Right";


	public static final String BOUND_PLACEMENT = "Bound Placement";

	public static final String UNBOUND_PROPERTIES_PLACEMENT = "Unbound Properties Placement";
	
	public static final String PROPERTIES_PLACEMENT = "Properties Placement";
	
	public static final String UNBOUND_BUTTONS_PLACEMENT = "Unbound Buttons Placement";

	public static final String BUTTONS_PLACEMENT = "Buttons Placement";

	public static final String ROWS_PLACEMENT = "Rows Placement";

	public static final String COLUMNS_PLACEMENT = "Columns Placement";
	
	public static final String NUM_ROWS = "Num Rows";

	public static final String NUM_COLUMNS = "Num Columns";

	public static final String UNBOUND_BUTTONS_ROW_SIZE = "Unbound Buttons Row Size";

	public static final String COMPRESS_BLANKS = "Compress Blanks";

	public static final String STRETCH_ROWS = "Stretch Rows";

	public static final String STRETCH_UNBOUND_ROWS = "Stretch Unbound Rows";

	public static final String STRETCH_COLUMNS = "Stretch Columns";

	public static final String STRETCH_UNBOUND_COLUMNS = "Stretch Unbound Columns";
	
	public static final String STRETCHABLE_BY_PARENT = "Stretchable By Parent";
	
	public static final String ADD_CONSTRAINT = "Add Constraint";
	
	public static final String ADD_WIDTH_CONSTRAINT = "Add Width Constraint";
	
	public static final String ADD_ANCHOR_CONSTRAINT = "Add Anchor Constraint";
	

	public static final String ADD_WEIGHT_X_CONSTRAINT = "Add Width X Constraint";
	
	public static final String ADD_WEIGHT_Y_CONSTRAINT = "Add Width Y Constraint";
	
	public static final String ADD_FILL_CONSTRAINT = STRETCHABLE_BY_PARENT;


	public static final String ROWS_FULL_SIZE = "Rows Full Size";

	public static final String ALIGN_CELLS = "Align Cells";

	public static final String EQUAL_ROWS = "Equal Rows";
	
	public static final String IS_FLAT_TABLE_ROW = "Is Flat Table Row";
	
	public static final String HAS_FLAT_TABLE_ROW_DESCENDENT = "Has Leaf Flat Table ROW Descendent";
	
	public static final String ONLY_GRAPHIICAL_DESCENDENTS = "Only Graphical Descendents";

	
	public static final String IS_FLAT_TABLE_CELL = "Is Flat Table Cell";
	
	public static final String IS_FLAT_TABLE_COMPONENT = "Is Flat Table Component";

	public static final String SHOW_BUTTON = "Show Button";
	//public static final String SHOW_BUTTON = "Show Button";

	public static final String SHOW_UNBOUND_BUTTONS = "Show Unbound Buttons";

	public static final String TO_STRING_AS_LABEL = "To String As Label";
	
	public static final String USER_OBJECT_AS_LABEL = "User Object As Label";

	public static final String DISPLAY_TO_STRING = "Unparse As To String";

	public static final String NAME_PROPERTY = "Name";
	
	public static final String USE_NAME_AS_LABEL = "Use Name as Label";
	
	public static final String SHOW_READONLY_NAME_CHILD_AS_LABEL = "Show Readonly Name Child As Label";

	public static final String TRUE_LABEL = "True Label";
	public static final String ELIDE_IF_NO_COMPONENTS = "Elide If No Components";

	public static final String ELIDED = "Elided";
	public static final String ELIDE_STRING = "Elide String";
	public static final String ELIDE_STRING_IS_TOSTRING = "Elide String Is ToString";
	
	public static final String EXPLANATION = "Explanation";
	
	public static final String SHOW_DEBUG_INFO_WITH_TOOL_TIP = "Debug With Tool Tip";

	
	public static final String KEYWORDS_ANNOTATION = "Keyword Annotation";
	
	public static final String MERGED_ATTRIBUTES_ANNOTATIONS = "Merged Attributes Annotations";

	//public static final String ELIDESTRING = ELIDE_STRING;

	public static final String ELIDE_IMAGE = "Elide Image";

	public static final String MENU_NAME = "Menu NAME";

	public static final String METHOD_MENU_NAME = "Method Menu Name";

	public static final String PLACE_TOOLBAR = "Place Toolbar";
	
	public static final String PATTERN_METHODS_IN_RIGHT_MENU = "Pattern Methods In Right Menu";
	
//	public static final String EDITING_METHODS_IN_RIGHT_MENU = "Editing Methods In Right Menu";
	
    public static final String PATTERN_METHODS_IN_MAIN_MENU = "Pattern Methods In Main Menu";
	
//	public static final String EDITING_METHODS_IN_MAIN_MENU = "Editing Methods In Main Menu";
	
	public static final String PATTERN_METHODS_IN_TOOL_BAR = "Pattern Methods In ToolBar";
		
//	public static final String EDITING_METHODS_IN_TOOL_BAR = "Editing Methods In ToolBar";
	
    public static final String BUTTON_METHODS_IN_MAIN_MENU = "Button Methods In Main Menu";
    
    public static final String BUTTON_METHODS_IN_RIGHT_MENU = "Button Methods In Right Menu";
    
    public static final String BUTTON_METHODS_IN_TOOL_BAR = "Button Methods In Right Menu";



	public static final String TOOLBAR = "Toolbar";

	public static final String ICON = "Icon";

	//public static final String RIGHTMENU = "rightMenu";

	public static final String RIGHT_MENU = "Right Menu";

	public static final String VISIBLE = "Visible";
	
	public static final String INVISIBLE_IF_NULL = "Not Visible If Null";
	
		
	public static final String INDIRECTLY_VISIBLE = VISIBLE;
	
	public static final String SCROLLED = "Scrolled";
	
	public static final String METHODS_VISIBLE = "Methods Visible";

	public static final String LABELLED = "Is Labelled";

	//public static final String HELPERLABEL = "helperLabel";

	public static final String HELPER_LABEL = "Helper Label";

	public static final String HELPER_ICON = "Helper Icon";

	public static final String HELPER_METHOD = "Helper Method";

	public static final String HELPER_LOCN = "Helper Location";

	public static final String TITLE = "Title";

	//public static final String POPUP_MENU = "Right Menu";

	public static final String TOOLBAR_ICON = "Icon";

	public static final String TOOLBAR_METHOD = "Toolbar";

	public static final String PREFERRED_WIDGET = "Widget";

	public static final String PREFERRED_WIDGET_ADAPTER = "Widget Adapter";

	public static final String DIRECTION = "Direction";
	
	public static final String COMPONENT_BACKGROUND = "Component Background";
	public static final String CONTAINER_BACKGROUND = "Container Background";
	public static final String COMPONENT_FOREGROUND = "Component Foreground";
	public static final String SELECTION_COLOR = "Selection Color";
	public static final String DRAWING_PANEL_COLOR = "Drawing Panel Color";


	public static final String LABEL_COLOR = "Label Color";

	public static final String TEXT_FIELD_LENGTH = "Number of Columns";
	
	public static final String SHOW_COLUMN_PREFIX = "Show Column Prefix";
	
	public static final String SHOW_ROW_PREFIX = "Show Row Prefix";
	
	public static final String SHOW_COLUMN_SUFFIX = "Show Column Suffix";
	
	public static final String COLUMN_PREFIX_WIDTH = "Column Prefix Width";
	
	public static final String COLUMN_PREFIX_COLOR = "Column Prefix Color";
	
	public static final String ROW_PREFIX_COLOR = "Column Prefix Color";
	
	public static final String COLUMN_SUFFIX_WIDTH = "Column Suffix Width";
	
	public static final String COLUMN_SUFFIX_COLOR = "Column Suffix Color";
	
	public static final String ROW_PREFIX_WIDTH = "Row Prefix Width";

	public static final String LABEL_WIDTH = "Label Width";
	
	public static final String FILLER_WIDTH = "Filler Width";

	
	public static final String ROW_LABELS_WIDTH = "Row Labels Width";
	
	public static final String ROW_LABEL_WIDTH = "Row Label Width";
	
	public static final String ROW_INDENT_WIDTH = "Row Indent Width";

	public static final String COMPONENT_WIDTH = "Component Width";
	
	public static final String COMPONENT_PADDING = "Component Padding"; // label width + padding --> component width

	public static final int DEFAULT_COMPONENT_PADDING = 10;
	
	public static final String COMPONENT_HEIGHT = "Component Height";
	
	
	
	public static final String SHELL_WIDTH = "Shell Width";
	
	public static final String SHELL_HEIGHT = "Shell Height";
	
	public static final String COMPONENT_X = "Component X";
	
	public static final String COMPONENT_Y = "Component Y";
	
	public static final String CONTAINER_WIDTH = "Container Width";
	
	//public static final String CONTAINER_WIDTH = COMPONENT_WIDTH;

	
	public static final String CONTAINER_HEIGHT = "Container Height";
	
	//public static final String CONTAINER_HEIGHT = COMPONENT_HEIGHT;
	
	

	
	public static final String ELIDE_COMPONENT_WIDTH = "Elide Component Width";	

	public static final String DECINCUNIT = "Dec Inc Unit";

	public static final String INCREMENTAL = "Incremental Feedback";
	
	public static final String ACTION_MODE = "actionMode";

	public static final String DOUBLE_CLICK_METHOD = "Double Click Method";
	
	public static final String OPEN_ON_DOUBLE_CLICK = "Open On Double Click";
	
	//public static final String IMPLCIT_REFRESH = "Implicit Refresh";

	public static final String SELECT_METHOD = "Select Method";
	
	public static final String DEFAULT_EXPANDED = "Expanded";
	
	public static final String SHOW_ELIDE_HANDLES = "Show Elide Handles";
	
	public static final String EXPAND_METHOD = "Expansion Method";
	
	public static final String SELECT_HANDLERS = "Select Handlers";
	
	public static final String EXPAND_HANDLERS = "Expand Handlers";

	// public static final Direction HORIZONTAL = Direction.horizontal;
	public static final String HORIZONTAL = "horizontal";

	public static final String VERTICAL = "vertical";

	public static final String FIT_TO_BOUND = "Fit To Bound";

	public static final String BOX = "box";

	public static final String SQUARE = "square";

	public static final String ROW = "Row Number";

	public static final String COLUMN = "Column Number";
	
	public static final String CLASS_VIEW_GROUP = "Class View Group";
	
	


	public static final String VIEW_GROUP = "View Group";
	
	public static final String VIEW_GROUPS = "View Groups";

	public static final String ONLY_DYNAMIC_PROPERTIES = "Only Dynamic Properties";

	public static final String ONLY_DYNAMIC_METHODS = "Only Dynamic Methods";

	public static final String CELL_FILLER_LABEL = "Cell Filler Label";

	public static final String CELL_FILLER_ICON = "Cell Filler Icon";

	public static final String SHOW_BOUNDARY_LABELS = "Boundary Labels";	
	
	public static final String SHOW_BOUNDARY = "Show Boundary";	

	public static final String EMPTY_BORDER_WIDTH = "Empty Border Width";

	public static final String EMPTY_BORDER_HEIGHT = "Empty Border Height";

	public static final String SHOW_UNLABELLED_BORDER = "Show Unlabelled Border";

	public static final String SHOW_BORDER = "Show Border";

	public static final String HASHTABLE_CHILDREN = "Hashtable Children";

	public static final String KEYS_ONLY = "Keys Only";

	public static final String VALUES_ONLY = "Values Only";

	public static final String KEYS_AND_VALUES = "Keys And Values";

	public static final String LABEL_KEYS = "Label Keys";

	public static final String LABEL_VALUES = "Label Values";

	public static final String HORIZONTAL_KEY_VALUE = "Horizontal Key Value";

	public static final String INDENTED = "Indented";

	public static final String SHOW_BLANK_COLUMN = "Show Blank Column";

	public static final String SHOW_TREE = "Show Tree";

	public static final String IS_UNDOABLE = "Is Undoable";
	
	public static final String NOT_UNDOABLE_EMPTIES_UNDO_HISTORY = "Not Undoable Empties Undo History";
	
	public static final String INVERSE = "Inverse";
	
	public static final String IS_ADD_ALL_METHOD = "Is Add All Method";
	
	public static final String IS_REMOVE_ALL_METHOD	= "Is Remove All Method";
	
	public static final String IS_REMOVE_METHOD	= "Is Remove Method";
	
	//public static final String IS_INDEX_OF_METHOD = "Is Index of Method";
	
	public static final String ADD_ALL_METHOD = "Add All Method";
	
	public static final String READ_ELEMENT_METHOD = "Read Element Method";
	
	//public static final String INSERT_ELEMENT_AT_METHOD = "Insert Element At Method";

	public static final String SORT_PROPERTY = "Sort Property";
	
	public static final String SORT_VECTOR_PATH = "Sort Vector Path";

	public static final String SORT = "Sort";

	public static final String PRIMARY_PROPERTY = "Primary Property";

	public static final String NESTED_RELATION = "Nested Relation";
	
	public static final String VECTOR_NAVIGATOR = "Vector Navigator";
	
	public static final String VECTOR_NAVIGATOR_SIZE = "Vector Navigator Size";
	
	public static final String SHOW_VECTOR_NAVIGATION_COMMANDS = "Show Vector Navigation Commands";

	public static final String UN_NEST_HT_RECORD = "UnNest HT Record";

	public static final String UN_NEST_MATRIX = "UnNest Matrix";
	
	public static final String USER_OBJECT_IS_FIRST_STATIC_CHILD = "First Child Is User Object";
	
	public static final String FIRST_CHILD_IS_USER_OBJECT = "First Child Is User Object";

	public static final String HASHTABLE_SORT_KEYS = "Sort Keys";
	
	public static final String LIST_SORT_USER_OBJECT = "Sort User Object";
	
	public static final String AUTO_SAVE = "Auto Save";
	
	public static final String COLUMN_LABELS = "Column Labels";
	
	public static final String ROWS_LABELLED = "Rows Labelled";
	
	public static final String MAX_VALUE = "Max Value";
	
	public static final String MIN_VALUE = "Min Value";
	
	public static final String STEP_VALUE = "Step Value";
	
	public static final String SHOW_SYSTEM_MENUS = "Show System Menus";
	
	public static final String  SHOW_OBJECT_MENUS = "Show Object Menus";
	
	public static final String SHOW_INTERFACE_MENUS = "Show Interface Menus";
	
	public static final String SHOW_SUPERCLASS_MENUS = "Show Superclass Menus";
	
	public static final String SEPARATE_THREAD = "Separate Thread";
	
	public static final String READ_ONLY = "Read Only";
	
	public static final String FONT = "Font";
	
	public static final String FONT_NAME = "Font Name";
	
	public static final String FONT_STYLE = "Font Style";
	
	public static final String FONT_SIZE = "Font Size";
	
	public static final String EXPANSION_OBJECT = "Expansion Object";
	
	public static final String SELECTION_IS_LINK = "Selection is Link";
	
	public static final String COMPONENT_SELECTABLE = "Component Selectable";
	
	public static final String	HTML_DOCUMENTATION = "HTML Documentation";
	
	//public static final String	Z_INDEX = "Z Index";
	
	public static final String PROPAGATE_CHANGE = "Propagate Sets";
	
	public static final String SHOW_RECURSIVE = "Show Recursive";
	
	public static final String SHOW_COLUMN_TITLES = "Show Column Titles";
	
	public static final String ALLOW_COLUMN_ELIDE = "Allow Column Elide";
	
	public static final String ALLOW_COLUMN_HIDE = "Allow Column Hide";
	
	public static final String MANUAL_INVOCATION_UI = "Manual UI";
	
	public static final String INPLACE_METHOD_RESULT = "In Place Method Result";
	
	public static final String PERSIST_INVOCATION_WINDOW = "PERSIST INVOCATION WINDOW";

	
	public static final String METHOD_BUTTON_TITLE = "Method Button Title";
	
	public static final String APPLY_TITLE = "Apply Title";
	
	public static final String NAME_TITLE = "Name Title";
		
	public static final String SIGNATURE_TITLE = "Signature Title";
	
	public static final String SHOW_PARAMETER_TYPE = "Show Parameter Type";
	
	public static final String DEFAULT_EXPAND_PARAMETER_VALUE = "Expand Parameter Value";
	
	public static final String EXPAND_PRIMITIVE_CHILDREN = "Expand Children";
	
	public static final String SHOW_PARAMETER_ELIDE_HANDLE = "Show Parameter Elide Handle";
	
	public static final String IMPLICIT_METHOD_INVOCATION = "Implicit Method Invocation";
	
	public static final String RESET_METHOD_INOVOKER = "Reset Method Invoker";
	
	public static final String CREATE_WIDGET_SHELL = "WIDGET SHELL";	
	
	public static final String GRAPHICS_VIEW = "Graphics View";
	public static final String SHOW_PARAMETER_GRAPHICS_VIEW = "Show Parameter Graphics View";
	public static final String INFORM_ABOUT_DESCENDENTS = "Inform About Descendents";
	public static final String PROJECTION_GROUPS = "Projection Groups";
	public static final String IMPLICIT_REFRESH = "Implicit Refresh";
	public static final String REFRESH_ON_NOTIFICATION = "Implicit Refresh On Notification";
	public static final String IS_ATOMIC_SHAPE = "IS ATOMIC SHAPE";
	public static final String IS_COMPOSITE_SHAPE = "IS COMPOSITE SHAPE";
	public static final String IS_SHAPE_SPECIFICATION_REQUIRED = "IS SHAPE SPECIFICTION REQUIRED";


	public static final String RETURNS_CLASS_EXPLANATION = "Returns Class Explanation";
	public static final String RETURNS_CLASS_WEB_DOCUMENTS = "Returns Class Web Documents";
	public static final String WEB_DOCUMENTS_METHOD = "Web Documents Method";
	public static final String EXPLANATION_METHOD = "Explanation Method";
	public static final String IS_NESTED_SHAPES_CONTAINER = "NESTED SHAPES CONTAINER";
	public static final String ALLOW_MULTIPLE_EQUAL_REFERENCES = "ALLOW MULTIPLE EQUAL REFERENCES";
	

	public static final String ALL_MENUS_CHOICE = "ALL_MENUS CHOICE";
	public static final String NO_MENUS[] = {};
	public static final String PREDEFINED_MENUS_CHOICE = "PREDEFINED_MENUBAR_CHOICE";
	public static String[] ALL_MENUS = {ALL_MENUS_CHOICE};
	
	public static String KEY_LABEL = "name";

	public static final String NEW_OBJECT_MENU_NAME = "New";

	public static final String TOOLBAR_PANEL_NAME = "toolbar";

	public static final String TREE_PANEL_NAME = "tree";

	public static final String DRAW_PANEL_NAME = "draw";
	public static final String DRAW_VIEW_GROUP = DRAW_PANEL_NAME;

	public static final String SECONDARY_PANEL_NAME = "secondary";

	// public static final String METHODS_MENU_NAME = "Methods";
	// public static final String METHODS_MENU_NAME = "Object";
	public static final String MAIN_PANEL_NAME = "main";

	public static final String CLASS_ATTRIBUTES_KEYWORD = AttributeNames.ATTRIBUTES_KEYWORD  + AttributeNames.KEYWORD_SEPARATOR + "Class";

	public static final String ATTRIBUTES_KEYWORD = "Attributes";

	public static final char KEYWORD_SEPARATOR = '.';

	public static final String COMMON_MENU = "Common";
	

	public static final String VIEW_MENU = "View";

	public static final String FILE_MENU = "File";
	
	public static final String PROMOTE_ONLY_CHILD = "Promote Only Child";
	
	public static String[] NON_COMMON_MENUS = {FILE_MENU, uiFrame.EDIT_MENU, VIEW_MENU, uiFrame.CUSTOMIZE_MENU, uiFrame.HELP_MENU_NAME};
	public static final String TEXT_ATTRIBUTES = "Text Attributes";

	public static final Color CAROLINA_BLUE = new Color(86, 160, 211);
//	public static final Color LIGHT_BLUE = new Color(0, 204, 204);
//	public static final Color LIGHT_BLUE = new Color(204, 229, 255);
	public static final Color LIGHT_BLUE = new Color(219, 229, 241);



	
	public static final Color DEFAULT_MAIN_WINDOW_COLOR = LIGHT_BLUE;
	public static final Color DEFAULT_GRAPHICS_COLOR = CAROLINA_BLUE;
	public static final Color DEFAULT_MENUBAR_COLOR = CAROLINA_BLUE; // not used so far
//	public static final Color DEFAULT_SELECTION_COLOR = new Color (153, 153, 255);
	public static final Color DEFAULT_SELECTION_COLOR = Color.CYAN;
	

	
static final String[] attributeNameArray = {
			AttributeNames.AUTO_SAVE,
			AttributeNames.ALIGN_CELLS,
			AttributeNames.BOUND_PLACEMENT,
			AttributeNames.CELL_FILLER_ICON,
			AttributeNames.CELL_FILLER_LABEL,
			AttributeNames.COLUMN,
			AttributeNames.COLUMNS_PLACEMENT,
			AttributeNames.COMPONENT_WIDTH,
			AttributeNames.COMPRESS_BLANKS,
			AttributeNames.CONTAINER_FACTORY,
			AttributeNames.DECINCUNIT,
			AttributeNames.DIRECTION,
			AttributeNames.DOUBLE_CLICK_METHOD,			
			AttributeNames.ELIDE_IMAGE,
			AttributeNames.ELIDE_STRING,
			AttributeNames.EMPTY_BORDER_HEIGHT,
			AttributeNames.EMPTY_BORDER_WIDTH,
			AttributeNames.EQUAL_ROWS,
			AttributeNames.DEFAULT_EXPANDED,
			AttributeNames.EXPANSION_OBJECT,
			AttributeNames.FIT_TO_BOUND,
			AttributeNames.FIRST_CHILD_IS_USER_OBJECT,
			AttributeNames.FONT,
			AttributeNames.FONT_NAME,
			AttributeNames.FONT_STYLE,
			AttributeNames.FONT_SIZE,
			AttributeNames.GRAPHICS_VIEW,
			AttributeNames.HASHTABLE_CHILDREN,
			AttributeNames.HASHTABLE_SORT_KEYS,
			AttributeNames.HELPER_ICON,
			AttributeNames.HELPER_LABEL,
			AttributeNames.HELPER_LOCN,
			AttributeNames.HORIZONTAL_KEY_VALUE,
			AttributeNames.ICON,
			AttributeNames.IMPLICIT_REFRESH,
			AttributeNames.INCREMENTAL,
			AttributeNames.INDENTED,
			AttributeNames.COLUMN_LABELS,
			AttributeNames.ROWS_LABELLED,
			AttributeNames.LABEL,
			AttributeNames.LABEL_ABOVE,
			AttributeNames.LABEL_BELOW,
			AttributeNames.LABEL_CASE,
			// AttributeNames.LABEL_IN_BORDER,
			// AttributeNames.LABEL_IN_UNBOUND,
			// AttributeNames.LABEL_IS_ABOVE,
			// AttributeNames.LABEL_IS_BELOW,
			// AttributeNames.LABEL_IS_LEFT,
			// AttributeNames.LABEL_IS_RIGHT,
			AttributeNames.LABEL_KEYS, AttributeNames.LABEL_LEFT,
			AttributeNames.LABEL_POSITION, AttributeNames.LABEL_RIGHT,
			AttributeNames.LABEL_SUFFIX, AttributeNames.LABEL_VALUES,
			AttributeNames.LABEL_WIDTH, AttributeNames.LABELLED,
			AttributeNames.LAYOUT_MANAGER_FACTORY,
			AttributeNames.LIST_SORT_USER_OBJECT,
			AttributeNames.MAX_VALUE,
			AttributeNames.MIN_VALUE,
			AttributeNames.MENU_NAME,
			AttributeNames.METHOD_MENU_NAME, 
			AttributeNames.NAME_PROPERTY,
			AttributeNames.SHOW_READONLY_NAME_CHILD_AS_LABEL,
			AttributeNames.NESTED_RELATION,
			AttributeNames.ONLY_DYNAMIC_METHODS,
			AttributeNames.ONLY_DYNAMIC_PROPERTIES,
			AttributeNames.OPEN_ON_DOUBLE_CLICK,
			AttributeNames.TOOLBAR,
			AttributeNames.PLACE_TOOLBAR, 
			AttributeNames.PATTERN_METHODS_IN_RIGHT_MENU,
//			AttributeNames.EDITING_METHODS_IN_RIGHT_MENU,
			AttributeNames.EXPANSION_OBJECT,
			AttributeNames.EXPAND_METHOD,
			AttributeNames.EXPAND_HANDLERS,
			//AttributeNames.POPUP_MENU,
			AttributeNames.POSITION, AttributeNames.PREFERRED_WIDGET,
			AttributeNames.PREFERRED_WIDGET_ADAPTER,
			AttributeNames.PRIMARY_PROPERTY, 
			AttributeNames.READ_ONLY,
			AttributeNames.RIGHT_MENU,
			AttributeNames.ROW, AttributeNames.ROWS_FULL_SIZE,
			AttributeNames.ROWS_PLACEMENT, AttributeNames.SELECT_METHOD,
			AttributeNames.SELECT_HANDLERS,
			AttributeNames.SEPARATE_THREAD,
			AttributeNames.SEPARATE_UNBOUND,
			AttributeNames.SEPARATE_UNBOUND_TITLES,
			AttributeNames.SHOW_BLANK_COLUMN, AttributeNames.SHOW_BORDER,
			AttributeNames.SHOW_BOUNDARY_LABELS, AttributeNames.SHOW_BUTTON,
			AttributeNames.SHOW_OBJECT_MENUS,
			AttributeNames.SHOW_INTERFACE_MENUS,
			AttributeNames.SHOW_SUPERCLASS_MENUS,
			AttributeNames.SHOW_SYSTEM_MENUS,
			AttributeNames.SHOW_TREE, AttributeNames.SHOW_UNBOUND_BUTTONS,
			AttributeNames.SHOW_UNLABELLED_BORDER, AttributeNames.SORT,
			AttributeNames.SORT_PROPERTY, AttributeNames.STRETCH_COLUMNS,
			AttributeNames.STRETCH_ROWS,
			AttributeNames.STRETCH_UNBOUND_COLUMNS,
			AttributeNames.STRETCH_UNBOUND_ROWS,
			AttributeNames.SELECTION_IS_LINK,
			AttributeNames.TEXT_FIELD_LENGTH, AttributeNames.TITLE,
			AttributeNames.TO_STRING_AS_LABEL, AttributeNames.TOOLBAR_ICON,
			AttributeNames.TOOLBAR_METHOD, 
			AttributeNames.EXPLANATION,
			AttributeNames.UN_NEST_HT_RECORD,
			AttributeNames.UN_NEST_MATRIX,
			AttributeNames.UNBOUND_BUTTONS_PLACEMENT,
			AttributeNames.UNBOUND_BUTTONS_ROW_SIZE,
			AttributeNames.UNBOUND_PROPERTIES_PLACEMENT,
			AttributeNames.DISPLAY_TO_STRING, 
			//AttributeNames.VALUES_ONLY,
			AttributeNames.VIEW_GROUP, 
			AttributeNames.VISIBLE,
			AttributeNames.CREATE_WIDGET_SHELL
			};

	// public static final String PREV_ROW_LABEL = "prevRowLabel";
	// public static final String PREV_COLUMN_LABEL = "prevColumnLabel";

	static Hashtable systemDefaults = new Hashtable();

	static Hashtable defaults = new Hashtable();

	static boolean initialized = false;

	static void init() {
		if (initialized)
			return;
		initialized = true;
		initDefaults();
		initSystemDefaults();
		return;
	}

	static void initDefaults() {
		// if (initialized) return;
		// initialized = true;
		/*
		 * defaults.put(LABELLED, new Boolean(true)); defaults.put(POSITION, new
		 * Integer(-1)); defaults.put(VISIBLE, new Boolean(true));
		 * defaults.put(INCREMENTAL, new Boolean(false));
		 * //defaults.put(LABELLED, new Boolean(true));
		 * defaults.put(TOOLBAR_METHOD, new Boolean(false));
		 * defaults.put(TEXT_FIELD_LENGTH, new Integer(10));
		 * defaults.put(DECINCUNIT, new Integer(1));
		 * defaults.put(DOUBLE_CLICK_METHOD, new Boolean(false));
		 * defaults.put(TO_STRING_AS_LABEL, new Boolean(false));
		 * defaults.put(COLUMN, new Integer(-1)); defaults.put(ROW, new
		 * Integer(-1)); defaults.put (COMPRESS_BLANKS, new Boolean(false));
		 * defaults.put (SEPARATE_UNBOUND, new Boolean(true)); defaults.put
		 * (ROWS_FULL_SIZE, new Boolean(false)); defaults.put (SHOW_BUTTONS, new
		 * Boolean(true)); defaults.put (SHOW_UNBOUND_BUTTONS, new
		 * Boolean(false)); defaults.put (BOUND_PLACEMENT, BorderLayout.NORTH);
		 * defaults.put (UNBOUND_PROPERTIES_PLACEMENT, BorderLayout.CENTER);
		 * defaults.put( UNBOUND_BUTTONS_PLACEMENT, BorderLayout.SOUTH);
		 * defaults.put (ROWS_PLACEMENT, BorderLayout.CENTER); defaults.put(
		 * COLUMNS_PLACEMENT, BorderLayout.WEST);
		 * defaults.put(UNBOUND_BUTTONS_ROW_SIZE, new Integer(1));
		 * //defaults.put(CELL_FILLER_LABEL, " ");
		 * defaults.put(CELL_FILLER_LABEL, " "); defaults.put (STRETCH_ROWS, new
		 * Boolean(false)); defaults.put (STRETCH_COLUMNS, new Boolean(true));
		 * defaults.put (SHOW_BOUNDARY_LABELS, new Boolean(true)); defaults.put
		 * (SHOW_UNLABELLED_BORDER, new Boolean(false)); defaults.put
		 * (EMPTY_BORDER_WIDTH, new Integer(3)); defaults.put
		 * (EMPTY_BORDER_HEIGHT, new Integer(2)); //defaults.put
		 * (HASHTABLE_CHILDREN, KEYS_AND_VALUES); defaults.put
		 * (HASHTABLE_CHILDREN, VALUES_ONLY); defaults.put(LABEL_KEYS, false );
		 * //defaults.put(LABEL_VALUES, true );
		 * defaults.put(HORIZONTAL_KEY_VALUE, true); defaults.put(INDENTED,
		 * true); defaults.put(SHOW_BLANK_COLUMN, false);
		 * defaults.put(SHOW_TREE, false);
		 */
		// defaults.put(LABEL_POSITION, LABEL_IN_BORDER);
		// defaults.put(SHOW_BORDER, false);
		//defaults.put(STRETCH_COLUMNS, new Boolean(false));
		//defaults.put(STRETCH_ROWS, new Boolean(false));
		// defaults.put(STRETCH_UNBOUND_COLUMNS, new Boolean(true));
		// defaults.put(STRETCH_UNBOUND_ROWS, new Boolean(true));
		//defaults.put(TEXT_FIELD_LENGTH, new Integer(5));
		//defaults.put(COMPONENT_WIDTH, new Integer(150));
		//defaults.put(SEPARATE_UNBOUND_TITLES, new Boolean(true));
		//defaults.put(COMPONENT_WIDTH, new Integer(100));

	}

	static void initSystemDefaults() {
		// if (initialized) return;
		// initialized = true;
		systemDefaults.put(LABELLED, new Boolean(true));
		systemDefaults.put(SCROLLED, new Boolean(false));
		systemDefaults.put(POSITION, new Integer(-1));
		systemDefaults.put(VISIBLE, new Boolean(true));
		systemDefaults.put(ELIDED, new Boolean(false));
		systemDefaults.put(INCREMENTAL, new Boolean(false));
		// systemDefaults.put(LABELLED, new Boolean(true));
		systemDefaults.put(TOOLBAR_METHOD, new Boolean(false));
		
//		systemDefaults.put(EDITING_METHODS_IN_RIGHT_MENU, new Boolean(true));
		systemDefaults.put(PATTERN_METHODS_IN_RIGHT_MENU, new Boolean(true));
//		systemDefaults.put(EDITING_METHODS_IN_MAIN_MENU, new Boolean(false));
		systemDefaults.put(PATTERN_METHODS_IN_MAIN_MENU, new Boolean(false));
//		systemDefaults.put(EDITING_METHODS_IN_TOOL_BAR, new Boolean(true));
		systemDefaults.put(PATTERN_METHODS_IN_TOOL_BAR, new Boolean(false));
		
//		systemDefaults.put(EDITING_METHODS_IN_RIGHT_MENU, new Boolean(true));
		systemDefaults.put(BUTTON_METHODS_IN_RIGHT_MENU, new Boolean(true));
//		systemDefaults.put(EDITING_METHODS_IN_MAIN_MENU, new Boolean(false));
		systemDefaults.put(BUTTON_METHODS_IN_MAIN_MENU, new Boolean(false));
//		systemDefaults.put(EDITING_METHODS_IN_TOOL_BAR, new Boolean(true));
		systemDefaults.put(BUTTON_METHODS_IN_TOOL_BAR, new Boolean(false));
		
		systemDefaults.put(PLACE_TOOLBAR,  AttributeNames.TOOLBAR_PANEL_NAME);
		systemDefaults.put(LABEL_CASE, AttributeNames.MIXED_CASE);

		systemDefaults.put(LABEL_HAS_NO_BLANKS, false);

		//systemDefaults.put(TEXT_FIELD_LENGTH, new Integer(15));
		systemDefaults.put(LABEL_WIDTH, new Integer(16));
		systemDefaults.put(FILLER_WIDTH, new Integer(16));
		systemDefaults.put(LABEL_POSITION, LABEL_IN_BORDER);
		systemDefaults.put(LABEL_SUFFIX, ":");
		systemDefaults.put(DECINCUNIT, new Integer(1));
		systemDefaults.put(DOUBLE_CLICK_METHOD, new Boolean(false));
		systemDefaults.put(SELECT_METHOD, new Boolean(false));
		systemDefaults.put(IMPLICIT_REFRESH, new Boolean(true));
		systemDefaults.put(REFRESH_ON_NOTIFICATION, false);
		systemDefaults.put(SELECT_HANDLERS, new Vector());
		systemDefaults.put(VIEW_GROUPS, new Vector());
		systemDefaults.put(EXPAND_METHOD, new Boolean(false));
		systemDefaults.put(EXPAND_HANDLERS, new Vector());
		systemDefaults.put(OPEN_ON_DOUBLE_CLICK, new Boolean(false));
		systemDefaults.put(TO_STRING_AS_LABEL, new Boolean(false));
		systemDefaults.put(USER_OBJECT_AS_LABEL, new Boolean(true));
		systemDefaults.put(COLUMN, new Integer(-1));
		systemDefaults.put(ROW, new Integer(-1));
		systemDefaults.put(COMPRESS_BLANKS, new Boolean(false));
		systemDefaults.put(SEPARATE_UNBOUND, new Boolean(true));
		systemDefaults.put(SEPARATE_THREAD, new Boolean(false));
		systemDefaults.put(ROWS_FULL_SIZE, new Boolean(false));
		//systemDefaults.put(SHOW_BUTTON, new Boolean(false));
//		systemDefaults.put(SHOW_BUTTON, new Boolean(true));
		systemDefaults.put(SHOW_UNBOUND_BUTTONS, new Boolean(false));
		systemDefaults.put(SEPARATE_UNBOUND_TITLES, new Boolean(true));	
		systemDefaults.put(LAYOUT, GRID_LAYOUT);
		systemDefaults.put(UNBOUND_LAYOUT, GRID_LAYOUT);
		systemDefaults.put(ALIGNMENT, FlowLayout.CENTER);
		systemDefaults.put(BOUND_PLACEMENT, BorderLayout.CENTER);

//		systemDefaults.put(BOUND_PLACEMENT, BorderLayout.SOUTH);
		systemDefaults.put(PROPERTIES_PLACEMENT, BorderLayout.CENTER);
		systemDefaults.put(UNBOUND_PROPERTIES_PLACEMENT, BorderLayout.CENTER);

		systemDefaults.put(BUTTONS_PLACEMENT, BorderLayout.SOUTH);
		systemDefaults.put(UNBOUND_BUTTONS_PLACEMENT, BorderLayout.SOUTH);

		systemDefaults.put(ROWS_PLACEMENT, BorderLayout.CENTER);
		systemDefaults.put(COLUMNS_PLACEMENT, BorderLayout.WEST);
		//systemDefaults.put(NUM_ROWS, new Integer(4));
		systemDefaults.put(NUM_COLUMNS, new Integer(3));
		//systemDefaults.put(UNBOUND_BUTTONS_ROW_SIZE, new Integer(6));
		// systemDefaults.put(CELL_FILLER_LABEL, " ");
		systemDefaults.put(CELL_FILLER_LABEL, " ");
		systemDefaults.put(STRETCH_ROWS, new Boolean(false));
		systemDefaults.put(STRETCHABLE_BY_PARENT, new Boolean(false));
		//systemDefaults.put(STRETCH_ROWS, new Boolean(true));
		//systemDefaults.put(STRETCH_COLUMNS, new Boolean(false));
		systemDefaults.put(STRETCH_COLUMNS, new Boolean(true));
		systemDefaults.put(STRETCH_UNBOUND_COLUMNS, new Boolean(false));
		systemDefaults.put(EQUAL_ROWS, new Boolean(false));
		systemDefaults.put(ALIGN_CELLS, new Boolean(false));
		systemDefaults.put(SHOW_BOUNDARY_LABELS, new Boolean(true));
		systemDefaults.put(SHOW_UNLABELLED_BORDER, new Boolean(false));
		systemDefaults.put(EMPTY_BORDER_WIDTH, new Integer(3));
		systemDefaults.put(EMPTY_BORDER_HEIGHT, new Integer(2));
		//systemDefaults.put(SHOW_BORDER, false);
		systemDefaults.put (HASHTABLE_CHILDREN, KEYS_AND_VALUES);
		//systemDefaults.put(HASHTABLE_CHILDREN, VALUES_ONLY);
		systemDefaults.put(LABEL_KEYS, false);
		// systemDefaults.put(LABEL_VALUES, true );
		systemDefaults.put(HORIZONTAL_KEY_VALUE, true);
		systemDefaults.put(INDENTED, false);
		systemDefaults.put(SHOW_BLANK_COLUMN, false);
		systemDefaults.put(SHOW_TREE, false);
		systemDefaults.put(USE_NAME_AS_LABEL, false);
		systemDefaults.put(NAME_PROPERTY, KEY_LABEL);
		systemDefaults.put(NESTED_RELATION, false);
		systemDefaults.put(VECTOR_NAVIGATOR, false);
		systemDefaults.put(SHOW_VECTOR_NAVIGATION_COMMANDS, true);
		systemDefaults.put(VECTOR_NAVIGATOR_SIZE, 5);
		systemDefaults.put(FIRST_CHILD_IS_USER_OBJECT, false);
		systemDefaults.put(USER_OBJECT_IS_FIRST_STATIC_CHILD, false);
		systemDefaults.put(SORT, false);
		systemDefaults.put(HASHTABLE_SORT_KEYS, true);
		systemDefaults.put(LIST_SORT_USER_OBJECT, false);
		systemDefaults.put(COMPONENT_PADDING, DEFAULT_COMPONENT_PADDING);
		//systemDefaults.put(COMPONENT_WIDTH, new Integer(150));
		systemDefaults.put(ROW_LABEL_WIDTH, new Integer(20));
		systemDefaults.put(AUTO_SAVE, false);
		systemDefaults.put(SHOW_INTERFACE_MENUS, false);
		systemDefaults.put(SHOW_SUPERCLASS_MENUS, false);
		systemDefaults.put(SHOW_OBJECT_MENUS, true);
		systemDefaults.put(SHOW_SYSTEM_MENUS, true);
		systemDefaults.put(READ_ONLY, false);
		//systemDefaults.put(TOOL_TIP_TEXT, "No tooltip text set. Programmer can make an attribute definition or annotattion to set it");
		systemDefaults.put(EXPLANATION, "");
		systemDefaults.put(SHOW_DEBUG_INFO_WITH_TOOL_TIP, true);
		systemDefaults.put(SELECTION_IS_LINK, false);
		systemDefaults.put(PROPAGATE_CHANGE, true);
		//systemDefaults.put(Z_INDEX, false);
		systemDefaults.put(SHOW_READONLY_NAME_CHILD_AS_LABEL, true);
		systemDefaults.put(DEFAULT_EXPANDED, true);
		systemDefaults.put(EXPAND_PRIMITIVE_CHILDREN, false);
		systemDefaults.put(SHOW_ELIDE_HANDLES, false);
		systemDefaults.put(ELIDE_STRING_IS_TOSTRING, true);
		systemDefaults.put(SHOW_RECURSIVE, false);	
		systemDefaults.put(SHOW_COLUMN_TITLES, true);
		systemDefaults.put(ALLOW_COLUMN_ELIDE, false);
		systemDefaults.put(ALLOW_COLUMN_HIDE, false);

		systemDefaults.put(MANUAL_INVOCATION_UI, false);
		//systemDefaults.put(METHOD_BUTTON_TITLE, SIGNATURE_TITLE);
		systemDefaults.put(METHOD_BUTTON_TITLE, APPLY_TITLE);
		systemDefaults.put(INPLACE_METHOD_RESULT, false);
		systemDefaults.put(SHOW_PARAMETER_ELIDE_HANDLE, false);
		systemDefaults.put(SHOW_PARAMETER_TYPE, true);
		systemDefaults.put(DEFAULT_EXPAND_PARAMETER_VALUE, false);
		systemDefaults.put(IMPLICIT_METHOD_INVOCATION, false);
		systemDefaults.put(RESET_METHOD_INOVOKER, true);
		systemDefaults.put(GRAPHICS_VIEW, true);
		systemDefaults.put(SHOW_PARAMETER_GRAPHICS_VIEW, false);
		systemDefaults.put(INFORM_ABOUT_DESCENDENTS, true);
		systemDefaults.put(NOT_UNDOABLE_EMPTIES_UNDO_HISTORY, false);
		systemDefaults.put(ROWS_LABELLED, false);
		//systemDefaults.put(IMPLICIT_REFRESH, true);
		systemDefaults.put(IS_FLAT_TABLE_ROW, false);
		systemDefaults.put(HAS_FLAT_TABLE_ROW_DESCENDENT, false);
		systemDefaults.put(IS_FLAT_TABLE_CELL, false);
		systemDefaults.put(IS_FLAT_TABLE_COMPONENT, false);
		systemDefaults.put(COLUMN_PREFIX_WIDTH, 7);
		systemDefaults.put(COLUMN_SUFFIX_WIDTH, 7);
		systemDefaults.put(SHOW_COLUMN_PREFIX, false);
		systemDefaults.put(SHOW_ROW_PREFIX, true);
		systemDefaults.put(SHOW_COLUMN_SUFFIX, false);
		systemDefaults.put(ROW_INDENT_WIDTH, 10);
		systemDefaults.put(ROW_PREFIX_WIDTH, 10);
//		systemDefaults.put(COMPONENT_BACKGROUND, DEFAULT_MAIN_WINDOW_COLOR);
		systemDefaults.put(CONTAINER_BACKGROUND, DEFAULT_MAIN_WINDOW_COLOR);

		systemDefaults.put(SELECTION_COLOR, DEFAULT_SELECTION_COLOR);
		systemDefaults.put(DRAWING_PANEL_COLOR, DEFAULT_GRAPHICS_COLOR);


		systemDefaults.put(COLUMN_PREFIX_COLOR, Color.lightGray);
		systemDefaults.put(COLUMN_SUFFIX_COLOR, Color.darkGray);
		systemDefaults.put(LABEL_COLOR, Color.gray);
		systemDefaults.put(HORIZONTAL_GAP, OEGridLayout.DEFAULT_HGAP);
		//systemDefaults.put(HORIZONTAL_BOUND_GAP, uiGridLayout.DEFAULT_HGAP);
		systemDefaults.put(VERTICAL_GAP, OEGridLayout.DEFAULT_VGAP);
		systemDefaults.put(CREATE_WIDGET_SHELL, true);
		
		systemDefaults.put(ELIDE_COMPONENT_WIDTH, 200);
		
	
		
//		systemDefaults.put(PREDEFINED_MENUS_CHOICE, NO_MENUS);
		
		systemDefaults.put(PREDEFINED_MENUS_CHOICE, new String[]{COMMON_MENU});
		
//		systemDefaults.put(PREDEFINED_MENUS_CHOICE, ALL_MENUS);
		
//		systemDefaults.put(PREDEFINED_MENUS_CHOICE, NON_COMMON_MENUS);

		
		systemDefaults.put(DocPackageRegistry.URI_HOME, DocPackageRegistry.DEFAULT_URI_HOME);
		systemDefaults.put(ALLOW_MULTIPLE_EQUAL_REFERENCES, false);
		
		systemDefaults.put(PERSIST_INVOCATION_WINDOW, false);
		systemDefaults.put(IS_COMPOSITE_SHAPE, false);
		systemDefaults.put(IS_SHAPE_SPECIFICATION_REQUIRED, false);
		systemDefaults.put(INVISIBLE_IF_NULL, false);
		systemDefaults.put(ELIDE_IF_NO_COMPONENTS, true);




		//systemDefaults.put(COMPONENT_SELECTABLE, false);


		//systemDefaults.put(SCROLLED, true);

		//systemDefaults.put(SEPARATE_UNBOUND_TITLES, true);
		//systemDefaults.put(FONT_NAME, Font.SANS_SERIF);
		//systemDefaults.put(FONT_SIZE, 18);
		//Font newFont = new Font (Font.SANS_SERIF, Font.BOLD, 18);
		//systemDefaults.put(FONT, newFont);
		//systemDefaults.put(TEXT_FIELD_LENGTH, 0);

	}
	public static String[] getAttributeNames() {
		return attributeNameArray;
	}
	public static void setDefault(String name, Object value) {
		init();
		if (value ==null)
			defaults.remove(name);
		// initDefaults();
		else
			defaults.put(name, value);
	}
	
	

	public static Object getSystemDefault(String name) {
		// initSystemDefaults();
		init();
		return systemDefaults.get(name);
	}
	
	public static Object getDefaultOrSystemDefault(String name) {
		Object retVal = getDefault(name);
		if (retVal == null)
			retVal = getSystemDefault(name);
		return retVal;
	}
	
	public static Color getSelectionColor() {
		return (Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR);
	}
	public static Color getDrawingPanelColor() {
		return (Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.DRAWING_PANEL_COLOR);
	}
	public static Object getDefault(String name) {
		// initDefaults();
		init();
		/*
		 * if (name.equals(POSITION)) return new Integer(-1); else if
		 * (name.equals(VISIBLE)) return new Boolean(true); else if
		 * (name.equals(INCREMENTAL)) return new Boolean(false); else if
		 * (name.equals(LABELLED)) return new Boolean(true); else if
		 * (name.equals(TOOLBAR_METHOD)) return new Boolean(false); else if
		 * (name.equals(NUM_COLUMNS)) return new Integer(15); else if
		 * (name.equals(DECINCUNIT)) return new Integer(1); else if
		 * (name.equals(DOUBLE_CLICK_METHOD)) return new Boolean (false); else
		 * if (name.equals(TO_STRING_AS_LABEL)) return new Boolean (false); else
		 * if (name.equals(ROW)) return new Integer (-1); else if
		 * (name.equals(COLUMN)) return new Integer (-1); else if
		 * (name.equals(LABEL_ABOVE)) return null; else if
		 * (name.equals(LABEL_BELOW)) return null; else if
		 * (name.equals(LABEL_LEFT)) return null; else if
		 * (name.equals(LABEL_RIGHT)) return null; return null;
		 */
		return defaults.get(name);
	}
/*
	public static Object getMethodAttribute(MethodDescriptor md, String name) {
		Object val = md.getValue(name);
		if (val != null)
			return val;
		ViewInfo cd = ClassDescriptorCache.getClassDescriptor(md.getMethod().getDeclaringClass());
		val = cd.getAttribute(name);
		if (val != null)
			return val;		
		val = getDefault(name);
		if (val != null)
			return val;
		else
			return getSystemDefault(name);

	}
	*/
	public static final String DEPENDENCY_PREFIX = "=" ;

	public static final String COMPONENT_NAME = "%ComponentName";

	public static final String ANY_ELEMENT = "element";
	
	public static final String ANY_COMPONENT = "*";

	public static final String ANY_KEY = "key";

	public static final String ANY_VALUE = "value";

	public static final String PATH_SEPARATOR = ".";

	public static final String PARENT_REFERENCE = "^";

	
}
