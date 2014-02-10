package bus.uigen.controller.models;

import java.util.Hashtable;
import java.util.Map;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AModelRegistry {
public static final String UNDO_REDO_MODEL = "UndoRedoModel";
public static final String DO_MODEL = "DoModel";
public static final String FILE_MODEL = "FileModel";
public static final String FONT_OPERATIONS_MODEL = "FontModel";
public static final String FONT_SIZE_MODEL = "FontSizeModel";
public static final String HELP_MODEL = "HelpModel";
public static final String NEW_EDTOR_MODEL = "NewEditorModel";
public static final String REFRESH_MODEL = "RefreshModel";
public static final String SELECTION_MODEL = "SelectionModel";
public static final String SOURCE_MODEL = "SourceModel";
public static final String TOOKLIT_MODEL = "ToolkitModel";
public static final String WINDOW_MODEL = "WindowModel";
public static final String MISC_MODEL = "MiscModel";
	
	Map<String, FrameModel> registeredModels = new Hashtable();
	public void registerModel(String name, FrameModel model ) {
		registeredModels.put(name, model);
	}
	
	public FrameModel getRegisteredModel(String name) {
		return registeredModels.get(name);
	}
	public void registerUndoRedoModel(AnUndoRedoModel model ) {
		registeredModels.put(UNDO_REDO_MODEL, model);
	}
	public AnUndoRedoModel getUndoRedoModel() {
		return (AnUndoRedoModel) registeredModels.get(UNDO_REDO_MODEL);
	}
	public void registerDoModel(ADoOperationsModel model ) {
		registeredModels.put(DO_MODEL, model);
	}
	public ADoOperationsModel getDoModel() {
		return (ADoOperationsModel) registeredModels.get(DO_MODEL);
	}
	public void registerSourceModel(ABasicSourceOperationsModel model ) {
		registeredModels.put(SOURCE_MODEL, model);
	}
	public ASourceOperationsModel getSourceModel() {
		return (ASourceOperationsModel) registeredModels.get(SOURCE_MODEL);
	}
	
}

