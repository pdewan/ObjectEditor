package bus.uigen.controller.models;

import java.awt.BorderLayout;
import java.awt.Color;

import util.models.AListenableString;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AListSelectionControllerAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setAttribute(AListSelectionController.class, AttributeNames.STRETCH_COLUMNS, false);
		ObjectEditor.setAttribute(AListenableString.class, AttributeNames.SHOW_BORDER, false);

		ObjectEditor.setPropertyLabelled(AListSelectionController.class, "Selected", false);
		ObjectEditor.setPropertyLabelled(AListSelectionController.class, "Search", false);
		//ObjectEditor.setPropertyAttribute(AFriendListSelection.class, "Search", AttributeNames.LABEL_LEFT, "Search");
		//ObjectEditor.setPropertyAttribute(AFriendListSelection.class, "Search", AttributeNames.LABEL_WIDTH, 60);

		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Search", AttributeNames.PROMPT, "Start Typing a Name");
		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Selected", AttributeNames.COMPONENT_FOREGROUND, Color.BLUE);
		ObjectEditor.setMethodAttribute(AListSelectionController.class, "All", AttributeNames.LABEL, "All/Selected");

		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "*", AttributeNames.COMPONENT_HEIGHT, 22);
		ObjectEditor.setMethodAttribute(AListSelectionController.class, "All", AttributeNames.COMPONENT_HEIGHT, 22);


		ObjectEditor.setMethodAttribute(AListSelectionController.class, "All", AttributeNames.COMPONENT_BACKGROUND, Color.BLUE);
		ObjectEditor.setMethodAttribute(AListSelectionController.class, "All", AttributeNames.COMPONENT_FOREGROUND, Color.WHITE);
		ObjectEditor.setMethodAttribute(AListSelectionController.class, "All", AttributeNames.COLUMN, 0);
		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Search", AttributeNames.COLUMN, 2);
		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Search", AttributeNames.REFRESH_ON_NOTIFICATION, true);

		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Selected", AttributeNames.LABEL_RIGHT, "");
		//ObjectEditor.setPropertyAttribute(AFriendListSelection.class, "Search", AttributeNames.LABEL_LEFT, "up.gif");

		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Selected", AttributeNames.LABEL_WIDTH, 360);
		//ObjectEditor.setPropertyAttribute(AFriendListSelection.class, "Search", AttributeNames.LABEL_WIDTH, 30);

		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Search", AttributeNames.COMPONENT_WIDTH, 200);

		ObjectEditor.setPropertyAttribute(AListSelectionController.class, "Selected", AttributeNames.COLUMN, 1);

		ObjectEditor.setHorizontal(AListSelectionController.class);
		//ObjectEditor.setAttribute(AFriendListSelection.class, AttributeNames.BUTTONS_PLACEMENT, BorderLayout.WEST);

		ObjectEditor.setAttribute(AListSelectionController.class, AttributeNames.SHOW_BUTTON, new Boolean(true) );
		//ObjectEditor.setAttribute(AFriendListName.class, AttributeNames.SHOW_BUTTON, new Boolean(true) );

		ObjectEditor.setAttribute(AListSelectionController.class, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(true) );
		  return null;
	}

}
