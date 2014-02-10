package bus.uigen.viewgroups;

import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class ANestedTupleListModel {
	Vector<ANestedTupleModel> contents = new Vector(); 
	VectorStructure relation;
	uiFrame frame;
	ObjectAdapter parentAdapter;
	ANestedRelationModel nestedRelation;
	public  ANestedTupleListModel(ANestedRelationModel theNestedRelation, VectorStructure theRelation, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		init(theNestedRelation, theRelation, theFrame, theParentAdapter);
		/*
		relation = theRelation;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		nestedRelation = theNestedRelation;
		*/
	}
	protected void init (ANestedRelationModel theNestedRelation, VectorStructure theRelation, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		contents.clear();
		relation = theRelation;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		nestedRelation = theNestedRelation;
	}
	public int size() {
		return contents.size();
	}
	public ANestedTupleModel elementAt(int i) {
		ANestedTupleModel retVal = contents.elementAt(i);
		return retVal;
		//return contents.elementAt(i);
	}
	public void setElementAt(ANestedTupleModel model, int index) {
		contents.setElementAt(model, index);
	}
	public void removeElementAt(int index) {
		ANestedTupleModel tuple = contents.elementAt(index);
		relation.removeElementAt(tuple.getIndex(), null);
		contents.removeElementAt(index);
	}
	protected void insertElementBasic (ANestedTupleModel tuple, int index) {
		contents.insertElementAt(tuple, index);
	}
	public void insertElementAt(ANestedTupleModel tuple, int index) {
		//ATupleModel tuple = contents.elementAt(index);
		relation.insertElementAt(tuple.getRecord().getTargetObject(), tuple.getIndex(), null);
		contents.insertElementAt(tuple, index);
	}
	public void addElement(ANestedTupleModel tuple) {
		//ATupleModel tuple = contents.elementAt(index);
		relation.insertElementAt(tuple.getRecord().getTargetObject(), tuple.getIndex(), null);
		contents.addElement(tuple);
	}
	protected void addElementBasic(ANestedTupleModel tuple) {
		//ATupleModel tuple = contents.elementAt(index);
		contents.addElement(tuple);
	}
	

}
