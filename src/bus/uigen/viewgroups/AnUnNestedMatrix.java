package bus.uigen.viewgroups;

import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTypeRegistry;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class AnUnNestedMatrix {
	Vector<Object> contents = new Vector(); 
	//Hashtable<Object, AnUnNestedRecordHTTuple> tupleModelCache = new Hashtable();
	VectorStructure matrix;
	Vector<VectorStructure> childVectors = new Vector();
	Vector<Integer> childIndices = new Vector();
	//RecordStructure record;
	uiFrame frame;
	CompositeAdapter parentAdapter;
	public  AnUnNestedMatrix(VectorStructure vector, uiFrame theFrame, CompositeAdapter theParentAdapter) {
		init(/*theUnNestedRelation,*/ vector, theFrame, theParentAdapter);
		
	}
	protected void init (VectorStructure theVector, uiFrame theFrame, CompositeAdapter theParentAdapter) {
		contents.clear();
		matrix = theVector;
		//hashtable = theHashtable;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		makeChildVectors();
		//nestedRelation = theUnNestedRelation;
	}
	void makeChildVectors () {
		//int globalPos = 0;
		//Vector keys = hashtable.keys();
		for (int i = 0; i < matrix.size(); i++) {
			Object origElement =  matrix.elementAt(i);
			Object element = GenerateViewObject.getViewObject(parentAdapter, origElement, null);
			ConcreteType childType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(element), element, parentAdapter.getUIFrame());
			  if (!(childType instanceof VectorStructure)) {
					System.out.println("E**" + element.getClass() + " is not a vector");
					continue;
					//return false;
				}
			VectorStructure childVector = (VectorStructure) childType;
			for (int j = 0; j < childVector.size(); j++) {
				childVectors.addElement(childVector);
				childIndices.addElement(j);
				/*
				childVectors.setElementAt(childVector, globalPos);
				childIndices.setElementAt(j, globalPos);
				*/
				//globalPos++;
			}
		};
		
	}
	public int size() {
		return childVectors.size();
	}
	public Object elementAt(int index) {
		VectorStructure childVector = childVectors.elementAt(index);
		int childIndex = childIndices.elementAt(index);
		Object retVal = childVector.elementAt(childIndex);
		return retVal;		
		//return contents.elementAt(i);
	}
	public void setElementAt(Object element, int index) {
		VectorStructure childVector = childVectors.elementAt(index);
		int childIndex = childIndices.elementAt(index);
		childVector.setElementAt(element, childIndex);
	}
	public void removeElementAt(int index) {
		VectorStructure childVector = childVectors.elementAt(index);
		int childIndex = childIndices.elementAt(index);
		childVector.removeElementAt(childIndex, null);
	}
	
	public void insertElementAt(Object element, int index) {
		//ATupleModel tuple = contents.elementAt(index);
		VectorStructure childVector = childVectors.elementAt(index);
		int childIndex = childIndices.elementAt(index);
		childVector.insertElementAt(element, childIndex, null);
		//contents.insertElementAt(tuple, index);
	}
	public void addElement(Object element) {
		//ATupleModel tuple = contents.elementAt(index);
		insertElementAt(element, size());
		/*
		hashtable.insertElementAt(tuple.getRecord().getTargetObject(), tuple.getIndex(), null);
		contents.addElement(tuple);
		*/
	}
	
	public void refreshView() {
		refreshChildrenVectors();
		childVectors.clear();
		childIndices.clear();
		makeChildVectors();
	}
	
	void refreshChildrenVectors() {
		Object lastVector = null;
		for (int i = 0; i < childVectors.size(); i++) {
			Object newVector = childVectors.elementAt(i);
			if (newVector == lastVector) continue;
			lastVector = newVector;
			OEMisc.askViewObjectToRefresh(lastVector);
		}
	}

}
