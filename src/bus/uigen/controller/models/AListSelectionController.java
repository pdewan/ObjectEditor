package bus.uigen.controller.models;

import java.util.List;

import util.annotations.ComponentWidth;
import util.models.AListSelection;
import util.models.AListenableString;
import util.models.AllOrSelectedList;
import util.models.CheckedObject;
import util.models.ListSelection;
import util.models.ListenableVector;
import util.models.VectorChangeEvent;

public class AListSelectionController implements ListSelectionController  {
	List<CheckedObject<String>> source;
	//ListenableVector<CheckedObject<String>> originalFriends;
	ListSelection<CheckedObject<String>, String> selectedElements;
	AllOrSelectedList<String> allOrSelectedList;
	boolean isSelected;

	AListenableString searchString  = new AListenableString("");
	boolean searchStringInitialized = false;
	public AListSelectionController (List<CheckedObject<String>> theSource, AllOrSelectedList<String> theAllOrSelectedList,
			ListSelection<CheckedObject<String>, String> theSelectedElements) {
		source = theSource;
		selectedElements = theSelectedElements;
		allOrSelectedList = theAllOrSelectedList;
		searchString.addVectorListener(this);
	}
	public void selected(Object newVal) {
		if (newVal instanceof String ) {
			String selectedString = (String) newVal;
			if (selectedString.startsWith("Selected"))
				all();
		}
	}
	public void all() {
		isSelected = !isSelected;
		allOrSelectedList.setSelected(isSelected);
		selectedElements.setQuery(searchString.toString());
		
//		for (int i = 0; i < source.size(); i++) {
//			source.get(i).setStatus(true);
//		}
	}
	int numSelected() {
		int retVal = 0;
		for (int i = 0; i < source.size(); i++) {
			if (source.get(i).getStatus()) 
				retVal++;
		}
		return retVal;
	}
	@ComponentWidth(90)
	public String getSelected() {
		return "Selected (" + numSelected() + " )";
	}
	
	
	public AListenableString getSearch() {
		return searchString;		
	}
	public void setSearch(AListenableString newVal) {
		searchString = newVal;
		selectedElements.setQuery(newVal.toString());
	}
	@Override
	public void updateVector(VectorChangeEvent evt) {
//		if (!searchStringInitialized) {		
//			searchStringInitialized = true;
//			switch (evt.getEventType()) {
//			case VectorChangeEvent.InsertComponentEvent: 
//				int index = evt.getPosition();
//				char insertedChar = searchString.charAt(index);
//				searchString.clear();
//				searchString.addElement(insertedChar);
//				break;
//			
//			}
//		}
		selectedElements.setQuery(searchString.toString());
		
	}
	
	public String toString() {
		return "AFriendListSelection " + getSelected() + " " + getSearch();
	}

}
