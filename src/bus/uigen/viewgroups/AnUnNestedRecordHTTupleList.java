package bus.uigen.viewgroups;

import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class AnUnNestedRecordHTTupleList {
	Vector<AnUnNestedRecordHTTuple> contents = new Vector(); 
	Hashtable<Object, AnUnNestedRecordHTTuple> tupleModelCache = new Hashtable();
	HashtableStructure hashtable;
	RecordStructure record;
	uiFrame frame;
	ObjectAdapter parentAdapter;
	ForFutureUseRelation nestedRelation;
	public  AnUnNestedRecordHTTupleList(/*ForFutureUseRelation theUnNestedRelation,*/ RecordStructure theRecord, HashtableStructure theHashtable, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		init(/*theUnNestedRelation,*/ theRecord, theHashtable, theFrame, theParentAdapter);
		/*
		relation = theRelation;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		nestedRelation = theNestedRelation;
		*/
	}
	protected void init (/*ForFutureUseRelation theUnNestedRelation,*/ RecordStructure theRecord, HashtableStructure theHashtable, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		contents.clear();
		record = theRecord;
		hashtable = theHashtable;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		makeTable();
		//nestedRelation = theUnNestedRelation;
	}
	void makeTable () {
		Vector keys = hashtable.keys();
		for (int i = 0; i < keys.size(); i++) {
			Object key = keys.elementAt(i);
			AnUnNestedRecordHTTuple tuple = makeTupleModel(record, hashtable, key, frame, parentAdapter);
			insertElementBasic(tuple, i);
		}
		
	}
	public int size() {
		return contents.size();
	}
	public AnUnNestedRecordHTTuple elementAt(int i) {
		AnUnNestedRecordHTTuple retVal = contents.elementAt(i);
		return retVal;
		//return contents.elementAt(i);
	}
	public void setElementAt(AnUnNestedRecordHTTuple model, int index) {
		contents.setElementAt(model, index);
	}
	public void removeElementAt(int index) {
		AnUnNestedRecordHTTuple tuple = contents.elementAt(index);
		Object key = tuple.getKey();
		hashtable.remove(key);
		contents.removeElementAt(index);
	}
	protected void insertElementBasic (AnUnNestedRecordHTTuple tuple, int index) {
		contents.insertElementAt(tuple, index);
	}
	public void insertElementAt(AnUnNestedRecordHTTuple tuple, int index) {
		//ATupleModel tuple = contents.elementAt(index);
		hashtable.put(tuple.getKey(), tuple.getValue());
		//hashtable.insertElementAt(tuple.getRecord().getTargetObject(), tuple.getIndex(), null);
		contents.insertElementAt(tuple, index);
	}
	public void addElement(AnUnNestedRecordHTTuple tuple) {
		//ATupleModel tuple = contents.elementAt(index);
		insertElementAt(tuple, contents.size());
		/*
		hashtable.insertElementAt(tuple.getRecord().getTargetObject(), tuple.getIndex(), null);
		contents.addElement(tuple);
		*/
	}
	protected void addElementBasic(AnUnNestedRecordHTTuple tuple) {
		//ATupleModel tuple = contents.elementAt(index);
		contents.addElement(tuple);
	}
	protected AnUnNestedRecordHTTuple makeTupleModel (RecordStructure theRecordStructure, HashtableStructure theHashtableStructure, Object theKey,  uiFrame theFrame, ObjectAdapter theParentAdapter)  {
		//Object targetObject = theRecordStructure.getTargetObject();
		
		AnUnNestedRecordHTTuple tupleModel = null;
		if (theKey != null)
			//createNew = true;
			tupleModel = tupleModelCache.get(theKey);
		if (tupleModel == null) {
			tupleModel =	 new AnUnNestedRecordHTTuple( theRecordStructure,  theHashtableStructure,  theKey,   theFrame,  theParentAdapter);			
			if (theKey != null)
			tupleModelCache.put(theKey, tupleModel);
		} else
			tupleModel.init( theRecordStructure,  theHashtableStructure,  theKey,   theFrame,  theParentAdapter);
		// no point removing the element from the cache perhaps
		return tupleModel;
	}
	public void refreshView() {
		contents.clear();
		makeTable();
	}
	

}
