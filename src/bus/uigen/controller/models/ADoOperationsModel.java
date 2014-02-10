package bus.uigen.controller.models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JOptionPane;

import util.annotations.Visible;
import util.misc.Common;
import util.trace.Tracer;

import bus.uigen.uiFrame;
import bus.uigen.Instantiator;
import bus.uigen.controller.ObjectClipboard;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionListener;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.SelectionManager;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.loggable.AnIdentifiableLoggable;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTypeRegistry;
import bus.uigen.sadapters.VectorStructure;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ADoOperationsModel extends AnAbstractOperationsModel implements FrameModel, SelectionListener {
//	uiFrame frame;
//	@Visible(false)
//	public void init(uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//		SelectionManager.addSelectionListener(this);
//		frame.getModelRegistry().registerDoModel(this);	
//	}

	ObjectAdapter singleOperand;

	boolean selectionExists() {
		return SelectionManager.getCurrentSelection() != null;
	}

	ObjectAdapter[] column;

	public boolean preCut() {
		boolean preCopy = preCopy();
		if (!preCopy())
			return false;
		if (singleOperand != null)
			return singleOperand.preDeleteFromParent();
		else {
			boolean retVal = true;
			for (int i = 0; i < column.length; i++)
				retVal |= column[0].preDeleteFromParent();
			return retVal;

		}

		/*
		 * //uiObjectAdapter s = (uiObjectAdapter)
		 * uiSelectionManager.getCurrentSelection(); operand = (uiObjectAdapter)
		 * uiSelectionManager.getCurrentSelection(); if (operand != null) {
		 * //operand = s; column = null; return operand.preDeleteFromParent(); }
		 * operand = null; column = uiSelectionManager.getColumn(); if (column ==
		 * null || column.length == 0) return false; boolean retVal = true; for
		 * (int i = 0; i < column.length; i++) retVal |=
		 * column[0].preDeleteFromParent(); return retVal; //return (s != null &&
		 * s.preDeleteFromParent()); //return selectionExists();
		 * 
		 */
	}

	public boolean preCopy() {
		// uiObjectAdapter s = (uiObjectAdapter)
		// uiSelectionManager.getCurrentSelection();
		singleOperand = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		if (singleOperand != null) {
			// operand = s;
			column = null;
			return true;
		}
		singleOperand = null;
		column = SelectionManager.getColumn();
		if (column == null || column.length == 0)
			return false;
		return true;
		/*
		 * boolean retVal = true; for (int i = 0; i < column.length; i++) retVal |=
		 * column[0].preDeleteFromParent(); return retVal;
		 */
		// return (s != null && s.preDeleteFromParent());
		// return selectionExists();
	}

	public void copy() {
		// uiObjectAdapter
		// operand = (uiObjectAdapter) uiSelectionManager.getCurrentSelection();
		// uiSelectionManager.unselect();
		if (!preCopy())
			return;
		doCopy();
		/*
		 * if (singleOperand != null) {
		 * ObjectClipboard.set(singleOperand.getObject());
		 * //singleOperand.deleteFromParent(); } else if (column != null) {
		 * uiSelectionManager.putColumnInClipboard();
		 *  }
		 */
	}

	void doCopy() {
		// uiObjectAdapter
		// operand = (uiObjectAdapter) uiSelectionManager.getCurrentSelection();
		// uiSelectionManager.unselect();

		if (singleOperand != null) {
			ObjectClipboard.set(singleOperand.getObject());
			// singleOperand.deleteFromParent();
		} else if (column != null) {
			SelectionManager.putColumnInClipboard();
			/*
			 * frame.beginTransaction(); for (int i = 0; i < column.length; i++)
			 * column[i].deleteFromParent(); frame.endTransaction();
			 */
		}
	}

	public void cut() {
		// uiObjectAdapter
		// operand = (uiObjectAdapter) uiSelectionManager.getCurrentSelection();
		// uiSelectionManager.unselect();
		if (!preCut())
			return;
		doCopy();
		if (singleOperand != null) {
			// ObjectClipboard.set(singleOperand.getObject());
			singleOperand.deleteFromParent();
		} else if (column != null) {
			// uiSelectionManager.putColumnInClipboard();
			frame.beginTransaction();
			for (int i = 0; i < column.length; i++)
				column[i].deleteFromParent();
			frame.endTransaction();
		}
	}

	public boolean preDelete() {
		Vector<ObjectAdapter> selections = SelectionManager.getSelections();
		boolean retVal = true;
		for (int i = 0; i < selections.size(); i++)
			retVal |= selections.elementAt(i).preDeleteFromParent();
		return retVal;
	}

	public void delete() {
		Vector<ObjectAdapter> selections = SelectionManager.getSelections();
		frame.beginTransaction();
		for (int i = 0; i < selections.size(); i++)
			selections.elementAt(i).deleteFromParent();
		frame.endTransaction();
		/*
		 * uiObjectAdapter s = (uiObjectAdapter)
		 * uiSelectionManager.getCurrentSelection();
		 * //uiSelectionManager.unselect(); if (s != null) {
		 * //ObjectClipboard.add(s.getObject()); s.deleteFromParent(); }
		 */
	}

	public boolean preClear() {
		singleOperand = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		if (singleOperand == null)
			singleOperand = frame.getAdapter();
		if (!(singleOperand instanceof CompositeAdapter))
			return false;
		return ((CompositeAdapter) singleOperand).preClear();

	}
	public boolean preDeleteAllElements() {
		return preClear();
	}

	public void deleteAllElements() {
		if (!preClear())
			return;
		((CompositeAdapter) singleOperand).clear();
	}

	/*
	 * public boolean preCopy() { return selectionExists(); }
	 */
	/*
	 * public void copy() { //Selectable s =
	 * uiSelectionManager.getCurrentSelection(); singleOperand =
	 * (uiObjectAdapter) uiSelectionManager.getCurrentSelection(); if
	 * (singleOperand != null) { ObjectClipboard.clear();
	 * ObjectClipboard.set(singleOperand.getObject()); }
	 *  }
	 */
	
	void setOperandToVectorSelectionOrAdapter(CompositeAdapter structuredOperand) {
		vectorOperand = null;
		if (structuredOperand == null)
			return ;
		if (!(structuredOperand instanceof VectorAdapter))
			return;
		vectorOperand = (VectorAdapter) structuredOperand;
	}
	int insertionIndex;
	boolean doPreInsertRows() {
		insertionIndex = singleOperand.getRealVectorIndex();
		structuredOperand = singleOperand.getParentAdapter();
		setOperandToVectorSelectionOrAdapter(structuredOperand);
		if (vectorOperand == null)
			return false;
		/*
		if (structuredOperand == null)
			return false;
		if (!(structuredOperand instanceof uiVectorAdapter))
			return false;
		vectorOperand = (uiVectorAdapter) structuredOperand;
		*/
		if  (!vectorOperand.canInsertChild())
			return false;
		if (!vectorOperand.hasValidateInsertElementAt())
			return true;
		try {
		addableRowType = vectorOperand.addableElementType();
		if (AClassProxy.classProxy(AnIdentifiableLoggable.class).isAssignableFrom(addableRowType))
			return true;
		
		Object newRow = Instantiator.newInstance(addableRowType);
		if (!vectorOperand.validateInsertChild(insertionIndex, newRow))
			return false;
		// Class elementClass = vectorAdapter.addableElementType();
		return true;
		} catch (Exception e) {
			return true;
		}
	}
	boolean doPreInsertColumns() {
		ObjectAdapter[] column = SelectionManager.getColumn();
		if (column == null || column.length == 0)
			return false;		
		structuredOperand = column[0].getGrandParentAdapter();
		insertionIndex = column[0].getParentAdapter().getVectorIndex();
		setOperandToVectorSelectionOrAdapter(structuredOperand);
		//doPreInsertRows();
		if (vectorOperand == null)
			return false;
		setOperandToNestedVectorSelectionOrAdapter(vectorOperand);
		if (nestedVectorOperand == null)
			return false;
		setElementClass();
		for (int i = 0; i < numExistingRows; i++) {
			VectorAdapter vectorAdapter = (VectorAdapter) nestedVectorOperand
					.getIndexedAdaptersVector().elementAt(i);			
			// elementClass = vectorAdapter.addableElementType();
			Object newValue = Instantiator.newInstance(elementClass);
			if (newValue == null)
				return false;
			if (!vectorAdapter.validateInsertChild(insertionIndex, newValue))
				return false;

		}
		return true;
	}
	boolean insertRows = false;
	boolean insertColumns = false;
	public boolean preInsertInt() {
		insertRows = false;
		insertColumns = false;
		singleOperand = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		boolean retVal = false;
		
		if (singleOperand != null) {
			retVal = doPreInsertRows();
			insertRows = retVal;
			return retVal;
		} else {
			selectedColumn = SelectionManager.getColumn();
			if (selectedColumn == null)
				return false;
			retVal = doPreInsertColumns();
			insertColumns = retVal;
			return retVal;
		}
	}
	
	public void insert (int numColumnsOrRows) {
		if (!preInsertInt())
			return;
		if (insertColumns)
			doInsertColumns(numColumnsOrRows);
		else if (insertRows)
			doInsertRows(numColumnsOrRows);
		
	}

	Object[] clipboardColumnCopy;
	ObjectAdapter[] selectedColumn;
	ObjectAdapter[] clipboardColumn;

	boolean deepCopyClipboardColumnForPaste() {
		selectedColumn = SelectionManager.getColumn();
		clipboardColumn = ObjectClipboard.getColumn();
		boolean retVal = true;
		if (selectedColumn == null || clipboardColumn == null)
			return false;
		if (selectedColumn.length != clipboardColumn.length)
			return false;
		clipboardColumnCopy = new Object[clipboardColumn.length];
		for (int i = 0; i < clipboardColumn.length; i++) {
			Object copy = deepCopyForPaste(selectedColumn[i],
					clipboardColumn[i].getRealObject());
			if (copy == null)
				return false;
			clipboardColumnCopy[i] = copy;
		}
		return SelectionManager.getColumnAdapterParent().getClass()
				.isAssignableFrom(
						ObjectClipboard.getColumnAdapterParent().getClass());
	}

	Object o_copy;

	Object deepCopyForPaste(ObjectAdapter operand, Object o) {
		if (o == null || operand == null)
			return null;
		if (!operand.getPropertyClass().isAssignableFrom(ACompositeLoggable.getTargetClass(o)))
			return null;
		if (operand.getClass().equals(PrimitiveAdapter.class))
			return o;
		return Common.deepCopy(o);
	}

	public boolean prePaste() {
		// uiObjectAdapter s = (uiObjectAdapter)
		// uiSelectionManager.getCurrentSelection();
		singleOperand = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		Object o = ObjectClipboard.getFirst();
		if (singleOperand != null && o != null) {
			// Object o = ObjectClipboard.getFirst();
			o_copy = deepCopyForPaste(singleOperand, o);
			// Misc.deepCopy(o);
			return o_copy != null;
			
		} else {
			if (SelectionManager.getColumn() == null)
				return false;
			return deepCopyClipboardColumnForPaste();
			
		}
	}

	void refreshSelectedColumn(Object[] objects) {
		if (objects == null)
			return;
		frame.beginTransaction();
		for (int i = 0; i < objects.length; i++) {
			selectedColumn[i].refreshValue(objects[i]);
			selectedColumn[i].uiComponentValueChanged();
		}
		frame.endTransaction();
	}

	public void paste() {
		if (!prePaste())
			Tracer.userMessage("Cannot paste");
		if (singleOperand != null) {
			singleOperand.refreshValue(o_copy);
			singleOperand.uiComponentValueChanged();
		} else {
			refreshSelectedColumn(clipboardColumnCopy);

		}
	

	}

	public boolean prePasteAfter() {
		boolean retVal = prePaste();
		if (!retVal)
			return false;
		int index = singleOperand.getVectorIndex();
		return singleOperand.getParentAdapter().validateInsertChild(index,
				ObjectClipboard.getFirst());

		// return true;
	}

	public void pasteAfter() {
		if (!prePasteAfter()) {
			Tracer.userMessage("Cannot do inserting paste.");
			return;
		}
		int pos = singleOperand.getRealVectorIndex() + 1;
		VectorAdapter vectorAdapter = (VectorAdapter) singleOperand
				.getParentAdapter();
		vectorAdapter.insertObject(o_copy, pos);
		
	}

	void setOperandToSelectionOrAdapter() {
		singleOperand = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		if (singleOperand == null)
			singleOperand = frame.getAdapter();
	}

	CompositeAdapter structuredOperand;

	void setOperandToStructuredSelectionOrAdapter() {
		setOperandToSelectionOrAdapter();
		ObjectAdapter adapter = singleOperand;
		if (!(adapter instanceof CompositeAdapter))
			adapter = frame.getAdapter();
		if (!(adapter instanceof CompositeAdapter))
			structuredOperand = null;
		else
			structuredOperand = (CompositeAdapter) adapter;

	}

	VectorAdapter nestedVectorOperand;
	VectorAdapter existingChildVectorAdapter;
	Class childVectorClass;
	VectorStructure newChildVectorStructure;
	VectorAdapter vectorOperand;

	void setOperandToVectorSelectionOrAdapter() {
		setOperandToStructuredSelectionOrAdapter();
		if (structuredOperand == null) {
			vectorOperand = null;
			return;
		}
		if (!(structuredOperand instanceof VectorAdapter)) {
			if (frame.getAdapter() instanceof VectorAdapter)
				vectorOperand = (VectorAdapter) frame.getAdapter();
			else {
				vectorOperand = null;
				return;
			}
		} else
			vectorOperand = (VectorAdapter) structuredOperand;

	}
	void setOperandToNestedVectorSelectionOrAdapter(VectorAdapter vectorOperand) {		
		if (vectorOperand == null) {
			nestedVectorOperand = null;
			return;
		} else
			nestedVectorOperand = vectorOperand;

		existingChildVectorAdapter = null;
		//nestedVectorOperand = (uiVectorAdapter) structuredOperand;
		
		ClassProxy childClass = nestedVectorOperand.addableElementType();		
		if (childClass == null) {
			nestedVectorOperand = null;
			return;
		}
		ConcreteType childType = ConcreteTypeRegistry.createConcreteType(
				childClass, null, null);
		if (childType instanceof VectorStructure)
			newChildVectorStructure = (VectorStructure) childType;
		else
			nestedVectorOperand = null;
		
	}


	void setOperandToNestedVectorSelectionOrAdapter() {
		setOperandToVectorSelectionOrAdapter();
		setOperandToNestedVectorSelectionOrAdapter(vectorOperand);
		if (nestedVectorOperand == null)
			return;
		
		existingChildVectorAdapter = null;
		nestedVectorOperand = (VectorAdapter) structuredOperand;
		ClassProxy childClass = nestedVectorOperand.addableElementType();
		
	}

	Object addedElement;
	ConcreteType addedConcreteType;

	void setNewElement() {
		setOperandToStructuredSelectionOrAdapter();
		setNewElement(structuredOperand);
		
	}

	void setNewElement(CompositeAdapter structuredOperand) {
		addedElement = null;
		if (structuredOperand == null)
			return;
		ClassProxy elementClass = structuredOperand.addableElementType();
		if (elementClass == null)
			return;
		addedElement = Instantiator.newInstance(elementClass);
		if (addedElement != null)
			addedConcreteType = ConcreteTypeRegistry.createConcreteType(
					ACompositeLoggable.getTargetClass(addedElement), addedElement, frame);
	}

	public boolean preAddElement() {

		setNewElement();
		if (addedElement == null)
			return false;
		return structuredOperand.validateAddChild(addedElement);

	}

	public boolean preAddInt() {
		setOperandToVectorSelectionOrAdapter();
		return doPreAddInt();
		// return nestedVectorOperand != null;
	}

	boolean doPreAddInt() {
		// setOperandToNestedVectorSelectionOrAdapter();
		if (vectorOperand == null)
			return false;
		// check if top vector can be added to

		// Class addableRowType = nestedVectorOperand.addableElementType();
		addableRowType = vectorOperand.addableElementType();
		/*
		if (numExistingRows > 0) {
			elementClass = ((uiVectorAdapter) vectorOperand
					.getIndexedAdaptersVector().elementAt(0))
					.addableElementType();
		} else {
			VectorStructure addableRowStructure = (VectorStructure) ConcreteTypeRegistry
					.createConcreteType(addableRowType, null, null);
			elementClass = addableRowStructure.addableElementType();
		}
		*/
		if (!vectorOperand.canAddChild())
			return false;
		if (!vectorOperand.hasValidateAddElement())
			return true;
		if (AClassProxy.classProxy(AnIdentifiableLoggable.class).isAssignableFrom(addableRowType))
			return true;
		try {
			Object newRow = Instantiator.newInstance(addableRowType);
		
		if (!vectorOperand.validateAddChild(newRow))
			return false;
		// Class elementClass = vectorAdapter.addableElementType();
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public boolean preAdd() {
		return preAddInt();
	}

	public void add() {
		add(1);
	}

	public void add(int numRows) {
		if (!preAddInt())
			return;
		frame.beginTransaction();
		doAddRows(numRows);
		frame.endTransaction();
	}

	void doAddRows(int numRows) {
		doAddOrInsertRows(numRows, true);
		/*
		for (int i = numExistingRows; i < numExistingRows + numRows; i++) {
			Object newRow = uiInstantiator.newInstance(addableRowType);
			if (newRow == null)
				return;
			vectorOperand.addObject(newRow);
			VectorStructure newVectorStructure = (VectorStructure) ConcreteTypeRegistry
					.createConcreteType(addableRowType, newRow, frame);
			// uiVectorAdapter vectorAdapter = (uiVectorAdapter)
			// nestedVectorOperand.getIndexedAdaptersVector().elementAt(i);
			// Class elementClass = vectorAdapter.addableElementType();
			for (int j = 0; j < maxExistingCols; j++) {
				Object newValue = uiInstantiator.newInstance(elementClass);
				
				newVectorStructure.addElement(newValue, nestedVectorOperand);
			}
		}
		*/
	}
	
	void doInsertRows(int numRows) {
		doAddOrInsertRows(numRows, false);
	}
	
	void doAddOrInsertRows(int numRows, boolean add) {
		for (int i = numExistingRows; i < numExistingRows + numRows; i++) {
			Object newRow = Instantiator.newInstance(addableRowType);
			if (newRow == null)
				return;
			if (add)
				vectorOperand.addObject(newRow);
			else
				vectorOperand.insertObject(newRow, insertionIndex);
			VectorStructure newVectorStructure = (VectorStructure) ConcreteTypeRegistry
					.createConcreteType(addableRowType, newRow, frame);
			// uiVectorAdapter vectorAdapter = (uiVectorAdapter)
			// nestedVectorOperand.getIndexedAdaptersVector().elementAt(i);
			// Class elementClass = vectorAdapter.addableElementType();
			for (int j = 0; j < maxExistingCols; j++) {
				Object newValue = Instantiator.newInstance(elementClass);
				/*
				 * if (newValue == null) return;
				 */
				newVectorStructure.addElement(newValue, nestedVectorOperand);
			}
		}
	}

	ClassProxy elementClass;
	ClassProxy addableRowType;

	public boolean preAddIntInt() {
		setOperandToNestedVectorSelectionOrAdapter();
		if (nestedVectorOperand == null)
			return false;
		if (!doPreAddInt())
			return false;
		return doPreAddColumns();
	}
	void setElementClass() {
		numExistingRows = nestedVectorOperand.getNumberOfDynamicChildren();
		if (numExistingRows > 0) {
			elementClass = ((VectorAdapter) vectorOperand
					.getIndexedAdaptersVector().elementAt(0))
					.addableElementType();
		} else {
			VectorStructure addableRowStructure = (VectorStructure) ConcreteTypeRegistry
					.createConcreteType(addableRowType, null, null);
			elementClass = addableRowStructure.addableElementType();
		}
	}
	boolean doPreAddColumns() {
		// check if rows can be added to
		/*
		numExistingRows = nestedVectorOperand.getNumberOfDynamicChildren();
		if (numExistingRows > 0) {
			elementClass = ((uiVectorAdapter) vectorOperand
					.getIndexedAdaptersVector().elementAt(0))
					.addableElementType();
		} else {
			VectorStructure addableRowStructure = (VectorStructure) ConcreteTypeRegistry
					.createConcreteType(addableRowType, null, null);
			elementClass = addableRowStructure.addableElementType();
		}
		*/
		setElementClass();
		//numExistingRows = nestedVectorOperand.getNumberOfDynamicChildren();
		maxExistingCols = 0;
		for (int i = 0; i < numExistingRows; i++) {
			VectorAdapter vectorAdapter = (VectorAdapter) nestedVectorOperand
					.getIndexedAdaptersVector().elementAt(i);
			maxExistingCols = Math.max(vectorAdapter
					.getNumberOfDynamicChildren(), maxExistingCols);
			// elementClass = vectorAdapter.addableElementType();
			Object newValue;
			//try {
			 newValue = Instantiator.newInstance(elementClass);
//			} catch (Exception e) {
//				newValue = null;
//			}
			if (newValue == null)
				return false;
			if (!vectorAdapter.validateAddChild(newValue))
				return false;

		}
		return true;
	}

	public boolean preAddToChildren() {
		return preAddToChildrenInt();
	}

	public boolean preAddToChildrenInt() {
		setOperandToNestedVectorSelectionOrAdapter();
		if (nestedVectorOperand == null)
			return false;
		return doPreAddColumns();
	}
	@Visible(false)
	public void addToChildren() {
		addToChildren(1);
	}
	@Visible(false)
	public void addToChildren(int numColumns) {
		if (!preAddToChildrenInt())
			return;
		frame.beginTransaction();
		doAddColumns(numColumns);
		frame.endTransaction();
	}

	int numExistingRows;
	int maxExistingCols;
	@Visible(false)
	public void add(int numRows, int numCols) {
		if (!preAddIntInt())
			return;
		/*
		 * int numExistingRows =
		 * nestedVectorOperand.getNumberOfDynamicChildren(); int maxExistingCols =
		 * 0;
		 */
		// numExistingRows = nestedVectorOperand.getNumberOfDynamicChildren();
		// maxExistingCols = 0;
		frame.beginTransaction();
		doAddColumns(numCols);
		/*
		 * for (int i = 0; i < numExistingRows ; i++) { uiVectorAdapter
		 * vectorAdapter = (uiVectorAdapter)
		 * nestedVectorOperand.getIndexedAdaptersVector().elementAt(i); //Class
		 * elementClass = vectorAdapter.addableElementType(); for (int j = 0; j <
		 * numCols; j++) {
		 * 
		 * Object newValue = uiInstantiator.newInstance(elementClass);
		 * 
		 * vectorAdapter.addObject(newValue);
		 *  }
		 *  } //Class addableRowType; //if (numExistingRows > 0)
		 * //addableRowType =
		 * nestedVectorOperand.getIndexedAdaptersVector().elementAt(0).getRealObject().getClass();
		 * //else //Class elementClass; //Class addableRowType =
		 * nestedVectorOperand.addableElementType();
		 */
		maxExistingCols += numCols;
		// Class elementClass = vectorAdapter.addableElementType();
		doAddRows(numRows);

		frame.endTransaction();

	}

	void doAddColumns(int numCols) {
		doAddOrInsertColumns(numCols, true);
		/*
		for (int i = 0; i < numExistingRows; i++) {
			uiVectorAdapter vectorAdapter = (uiVectorAdapter) nestedVectorOperand
					.getIndexedAdaptersVector().elementAt(i);
			// Class elementClass = vectorAdapter.addableElementType();
			for (int j = 0; j < numCols; j++) {

				Object newValue = uiInstantiator.newInstance(elementClass);
				
				vectorAdapter.addObject(newValue);

			}

		}
		*/

	}
	void doInsertColumns(int numCols) {
		doAddOrInsertColumns(numCols, false);
		/*
		for (int i = 0; i < numExistingRows; i++) {
			uiVectorAdapter vectorAdapter = (uiVectorAdapter) nestedVectorOperand
					.getIndexedAdaptersVector().elementAt(i);
			// Class elementClass = vectorAdapter.addableElementType();
			for (int j = 0; j < numCols; j++) {

				Object newValue = uiInstantiator.newInstance(elementClass);
				
				vectorAdapter.insertObject(newValue, insertionIndex);

			}

		}
		*/

	}
	void doAddOrInsertColumns(int numCols, boolean add) {

		for (int i = 0; i < numExistingRows; i++) {
			VectorAdapter vectorAdapter = (VectorAdapter) nestedVectorOperand
					.getIndexedAdaptersVector().elementAt(i);
			// Class elementClass = vectorAdapter.addableElementType();
			for (int j = 0; j < numCols; j++) {

				Object newValue = Instantiator.newInstance(elementClass);
				/*
				 * if (newValue == null) return;
				 */
				if (add)
					vectorAdapter.addObject(newValue);
				else
					vectorAdapter.insertObject(newValue, insertionIndex);

			}

		}

	}

	public boolean prePasteElement() {
		return false;
	}

	public void pasteElement() {

	}

	public boolean preLink() {
		ObjectAdapter s = (ObjectAdapter) SelectionManager
				.getCurrentSelection();
		Object o = ObjectClipboard.getFirst();
		return (s != null && o != null && s.getPropertyClass()
				.isAssignableFrom(ACompositeLoggable.getTargetClass(o)));

	}

	public void link() {
		try {
			ObjectAdapter s = (ObjectAdapter) SelectionManager
					.getCurrentSelection();
			Object o = ObjectClipboard.getFirst();
			if (s != null && o != null) {
				if (s.getPropertyClass().isAssignableFrom(ACompositeLoggable.getTargetClass(o))) {
					s.refreshValue(o);
					s.uiComponentValueChanged();
					// s.propagateChange();
					frame.validate();
				} else if (s instanceof VectorAdapter) {
					VectorAdapter vectorAdapter = (VectorAdapter) s;
					vectorAdapter.addObject(o);
				} else
					throw new Exception();
			}
		} catch (Exception e2) {
			Tracer.userMessage("Cannot link");
			/*
			 * JOptionPane.showMessageDialog(null, //"Error writing object in
			 * directory "+directory, "Type mismatch", "Error",
			 * JOptionPane.ERROR_MESSAGE);
			 */
		}

	}
	@Visible(false)
	public void singleItemSelected() {
		frame.checkPreInMenuTreeAndButtonCommands();
	}
	@Visible(false)
	public void noItemSelected() {
		frame.checkPreInMenuTreeAndButtonCommands();
	}
	@Visible(false)
	public void multipleItemsSelected() {
		frame.checkPreInMenuTreeAndButtonCommands();
	}

}
