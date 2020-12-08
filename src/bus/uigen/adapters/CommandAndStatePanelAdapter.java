package bus.uigen.adapters;

//import bus.uigen.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.SwingConstants;

import shapes.ComponentModel;
import util.models.ADynamicMatrix;
import util.models.ADynamicSparseList;
import util.models.DynamicMatrix;
import util.trace.Tracer;
import util.trace.uigen.BoundComponentPanelAdded;
import util.trace.uigen.ColumnPrefixLabelAdded;
import util.trace.uigen.ColumnSuffixLabelAdded;
import util.trace.uigen.ColumnTitleLabelAdded;
import util.trace.uigen.FlatTableBuildingStarted;
import util.trace.uigen.HierarchicalColumnTitlePanelFillingStarted;
import util.trace.uigen.IllegalComponentAddPosition;
import util.trace.uigen.LeafAdapterInconsistency;
import util.trace.uigen.LogicalChildComponentAdded;
import util.trace.uigen.RowLabelCreationStarted;
import util.trace.uigen.RowPanelAdded;
import util.trace.uigen.RowPanelComponentAdded;
import util.trace.uigen.RowSubPanelAdded;
import util.trace.uigen.UIComponentAdded;
import util.trace.uigen.UIComponentCreationStarted;
import util.trace.uigen.UnboundButtonAdded;
import util.trace.uigen.UnboundPropertiesUIComponentAdded;
import bus.uigen.PropertySetter;
import bus.uigen.WidgetAdapterInterface;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.compose.ButtonCommand;
import bus.uigen.compose.ComponentPanel;
import bus.uigen.controller.ADoubleClickMouseListener;
import bus.uigen.controller.ASelectionTriggerMouseListener;
import bus.uigen.controller.ASortTriggerMouseListener;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.controller.models.AColumnDisplayerAbstractModel;
import bus.uigen.controller.models.AColumnEliderModel;
import bus.uigen.controller.models.AColumnHiderModel;
import bus.uigen.controller.models.AColumnSiblingDisplayerModel;
import bus.uigen.controller.models.AProjectionGroupDisplayerModel;
import bus.uigen.introspect.Attribute;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.HashtableAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.RootAdapter;
import bus.uigen.oadapters.ShapeObjectAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.view.OEGridLayout;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.ContainerFactory;
import bus.uigen.widgets.LabelSelector;
import bus.uigen.widgets.LayoutManagerFactory;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualLabel;
import bus.uigen.widgets.swing.SwingPanelFactory;

public class CommandAndStatePanelAdapter extends PanelAdapter implements
		FocusListener {
	public CommandAndStatePanelAdapter() {
	}

	// ComponentPanel componentPanel;
	VirtualContainer componentPanel;

	// Container physicalComponentPanel;
	// VirtualContainer virtualComponentPanel;
	// adapters and corresponding widgets
	Vector propertyAdapters = new Vector();

	/*
	 * Vector partiallyBoundAdapters = new Vector(); Vector
	 * partiallyBoundCommands = new Vector();
	 * 
	 * Vector unboundAdapters = new Vector();
	 */
	Vector unboundCommands = new Vector();

	int MAX_ITEMS = 100;

	// Vector partiallyBound = new Vector();
	ADynamicSparseList rowItems = new ADynamicSparseList();

	ADynamicSparseList columnItems = new ADynamicSparseList();

	ADynamicSparseList upperRowItems;

	ADynamicSparseList lowerRowItems;

	ADynamicSparseList leftColumnItems;

	ADynamicSparseList rightColumnItems;

	ADynamicSparseList<ObjectAdapter> sortedPropertiesList = new ADynamicSparseList();

	ADynamicSparseList<ObjectAdapter> unSortedPropertiesList = new ADynamicSparseList();

	ADynamicSparseList<ButtonCommand> sortedCommandsList = new ADynamicSparseList();

	ADynamicSparseList<ButtonCommand> unSortedCommandsList = new ADynamicSparseList();
   
	Vector unboundProperties = new Vector(); // do not think this is ever used, sorted and unsrotedProperties replaced it I think
	//Vector unboundProperties; // do not think this is ever used, sorted and unsrotedProperties replaced it I think

	Vector unboundButtons = new Vector();

	// Vector propertyComponents = new Vector();
	// commands
	Vector commands;

	// Vector remaining
	// Vector buttons = new Vector();
	// Component[][] childComponents; // 2D array initialized later
	DynamicMatrix childComponents = new ADynamicMatrix();

	/*
	 * DynamicMatrix commandMatrix = new ADynamicMatrix(); DynamicMatrix
	 * propertiesMatrix = new ADynamicMatrix();
	 */
	boolean initialized = false;
	boolean boundComponentsAdded = false;

	VirtualContainer unboundButtonsPanel;

	VirtualContainer unboundPropertiesPanel;

	VirtualContainer boundComponentsPanel;

	VirtualContainer unboundColumnTitlePanel;
	boolean unboundColumnTitlePanelNotToBeAdded = false;
	boolean unboundColumnTitlePanelFilled = false;
	int unboundColumnTitlePos = 0;

	// uiObjectAdapter objectAdapter;

	LayoutManager layoutManager;

	Object lastAssignedValue;
	LayoutManager initialLayout;
	
	@Override
	public int defaultWidth() {
		return 0;
	}
//	@Override
//	protected int defaultHeight() {
//		return 25;
//	}
	
	void initCommands(ObjectAdapter adapter) {
		if (commands == null) {
			// commands = ComponentPanel.createCommandsWithButtons(this
			// .getObjectAdapter().getUIFrame(),
			// getObjectAdapter().getRealObject(), getObjectAdapter());
			commands = ComponentPanel.createCommandsWithButtons(adapter.getUIFrame(), adapter
					.computeAndMaybeSetViewObject(), adapter);
			//boolean addedCommands = processCommands(commands);
			addedCommands = processCommands(commands, adapter);
			if (firstTime && commands.size() > 0) {
				added = true;
				empty = false;
			}
		}
		
	}
	
	public boolean hasCommands() {
		return addedCommands;
	}

	public VirtualComponent instantiateComponent(ClassProxy cclass,
			ObjectAdapter adapter) {
		// objectAdapter = adapter;
		VirtualComponent retVal;
		ContainerFactory containerFactory = adapter.getContainerFactory();
		LayoutManagerFactory layoutManagerFactory = adapter
				.getLayoutManagerFactory();
		if (containerFactory != null) {
			componentPanel = containerFactory.createContainer();
			manualAdds = true;
			retVal = componentPanel;
		} else {
			// componentPanel = SwingPanelFactory.createJPanel();
			componentPanel = PanelSelector.createPanel();
			// object adapter is not instantiated
//			setColors(componentPanel);
			componentPanel.setName("Component Panel " + adapter.toDebugText() + " CommandAndStatePanelAdapter.instantiateComponent");
			instantiatedComponent = true;
			
			retVal = componentPanel;
			RightMenuManager.bindToRightMenu(componentPanel, adapter);
			// componentPanel = PanelSelector.createPanel();

		}
		//initialLayout = (LayoutManager) componentPanel.getLayout();
		// this seems to be 
		setLayout(adapter, componentPanel, 1, 1);
		// no longer initial layout, but later layout
		initialLayout = (LayoutManager) componentPanel.getLayout();
		//componentPanel.setLayout(new uiGridLayout(1,1));
		/*
		 * if (componentPanel.getLayout() != null) System.out.println("Layout:"
		 * + componentPanel.getLayout().getClass() );
		 */
		// partiallyBound.insertElementAt ("foo", 5);
		// componentPanel = new ComponentPanel();
		// uiObjectAdapter objectAdapter = getObjectAdapter();
		if (layoutManagerFactory != null) {
			layoutManager = (LayoutManager) layoutManagerFactory.createLayoutManager();
			componentPanel.setLayout(layoutManager); // maybe only the sub panels should have this
			manualAdds = true;
		}
		//initCommands(adapter);
		// componentPanel.add(new JLabel("Placeholder"), 0);
		// virtualComponentPanel = AnAWTContainer.virtualContainer
		// (componentPanel);
		// return virtualComponentPanel;
		// return componentPanel;
		return retVal;
		// return AnAWTContainer.virtualContainer (componentPanel);
		// componentPanel.init("Object", )
		// return componentPanel.getContainer();
		// return (new JTabbedPane());
		// return (new SwingPanelFactory()).createPanel();
		// return PanelSelector.createPanel();
	}

	public void setUIComponentTypedValue(Object newval) {
		// commands =
		// uiGenerator.createCommandList(this.getObjectAdapter().getUIFrame(),
		// this.getObjectAdapter().getRealObject());
		// commands =
		// ComponentPanel.createCommandsWithButtons(this.getObjectAdapter().getUIFrame(),
		// this.getObjectAdapter().getRealObject());
		lastAssignedValue = newval;
		/*
		 * commands =
		 * ComponentPanel.createCommandsWithButtons(this.getObjectAdapter
		 * ().getUIFrame(), newval); processCommands(commands);
		 */

		// componentPanel.init("component Panel", newval,
		// this.getObjectAdapter());
	}

	static int getRow(ButtonCommand c) {
		return c.getRow();
//		MethodDescriptorProxy md = c.getMethodDescriptor();
//		return c.getRow();
		// return ((Integer) md.getValue(AttributeNames.ROW)).intValue();
	}
	static int getPosition(ButtonCommand c) {
		return c.getPosition();
//		MethodDescriptorProxy md = c.getMethodDescriptor();
//		return c.getRow();
		// return ((Integer) md.getValue(AttributeNames.ROW)).intValue();
	}

	static int getCol(ButtonCommand c) {
		return c.getColumn();
//		MethodDescriptorProxy md = c.getMethodDescriptor();
//		return c.getColumn();
		// return ((Integer) md.getValue(AttributeNames.COLUMN)).intValue();
	}

	static int getRow(ObjectAdapter compAdapter) {
		return compAdapter.getRowAttribute();
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.ROW)).intValue();
	}

	static int getCol(ObjectAdapter compAdapter) {
		return compAdapter.getColumnAttribute();
		// return ((Integer)
		// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)).intValue();
	}

	int componentNo = 0;

	int maxRow = 0;

	int maxCol = 0;

	int numUnboundComponents = 0;

	int childCount;

	static int getRow(Object o) {
		if (o instanceof ObjectAdapter)
			return getRow(((ObjectAdapter) o));
		else if (o instanceof ButtonCommand)
			return getRow((ButtonCommand) o);
		else
			return -1;

	}
	
	Object getComputedAddConstraint(Object o) {
		Object c1 = getAddConstraint(o);
		if (c1 != null)
			return c1;
		if (adapter.isGridBagLayout())
			return getGridBagConstraints(o);
		return null;
	}
	
	 GridBagConstraints getGridBagConstraints(Object o) {
		Object c1 = getAddConstraint(o);
		if (c1 != null)
			return (GridBagConstraints) c1;
		 GridBagConstraints c = new GridBagConstraints();
		 c.gridx = getCol(o);
		 c.gridy = getRow(o);
		 Double weightX = getAddWeightXConstraint(o);
		 Double weightY = getAddWeightYConstraint(o);
		 Integer width = getAddWidthConstraint(o);
		 Integer anchor = getAddAnchorConstraint(o);
		 Integer fill = getAddFillConstraint(o);
		 if (weightX != null)
			 c.weightx = weightX;
		 else
			 c.weightx = 1.0; // should this depend on direction
		 if (weightY != null)
			 c.weighty = weightY;
//		 else 
//			 c.weighty = 0; //newval != currentModel
		 if (width != null)
			 c.gridwidth = width;
		 if (anchor != null)
			 c.anchor = anchor;
//		 else
//			 c.anchor = GridBagConstraints.EAST;
//		 if ()
		 if (fill != null)
		    c.fill = fill;
		 
//		 if (adapter.getAlignment().equals(AttributeNames.HORIZONTAL))
//			 c.fill = GridBagConstraints.HORIZONTAL;
//		 else if (adapter.getAlignment().equals(AttributeNames.VERTICAL))
//			 c.fill = GridBagConstraints.VERTICAL;
//			 

		 return c;
	}
	
	static Integer getAddWidthConstraint(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getAddWidthConstraint();
		else if (o instanceof ButtonCommand)
			return((ButtonCommand) o).getAddWidthConstraint();
		else
			return null;
	}
	
	static Object getAddConstraint(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getAddConstraint();
		else if (o instanceof ButtonCommand)
			return((ButtonCommand) o).getAddConstraint();
		else
			return null;
	}
	
	static Double getAddWeightXConstraint(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getAddWeightXConstraint();
		else if (o instanceof ButtonCommand)
			return((ButtonCommand) o).getAddWeightXConstraint();
		else
			return null;
	}
	static Double getAddWeightYConstraint(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getAddWeightYConstraint();
		else if (o instanceof ButtonCommand)
			return((ButtonCommand) o).getAddWeightYConstraint();
		else
			return null;
	}
	
	static Integer getAddAnchorConstraint(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getAddAnchorConstraint();
		else if (o instanceof ButtonCommand)
			return((ButtonCommand) o).getAddAnchorConstraint();
		else
			return null;
	}
	
	 Integer getAddFillConstraint(Object o) {
		Integer retVal = null;
		if (o instanceof ObjectAdapter)
			retVal = ((ObjectAdapter) o).getAddFillConstraint();
		else if (o instanceof ButtonCommand)
			retVal = ((ButtonCommand) o).getAddFillConstraint();
		if (retVal == null)
			return getComputedAddFillConstraint(o);
		
		return retVal;
	}
	
	static Boolean getStretchableByParent(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getStretchableByParent();
		else if (o instanceof ButtonCommand)
			return((ButtonCommand) o).getStretchableByParent();
		else
			return null;
	}
	
	 Integer getComputedAddFillConstraint(Object o) {
		Boolean stretchable = getStretchableByParent(o);
		if (stretchable == null || !stretchable) return null;
		if (adapter.getDirection().equals(AttributeNames.VERTICAL))
			return GridBagConstraints.HORIZONTAL;
		else if (adapter.getDirection().equals(AttributeNames.HORIZONTAL))
			return GridBagConstraints.VERTICAL;
		return null;
			
	}
	
	
	
	static int getPosition(Object o) {
		if (o instanceof ObjectAdapter)
			return getPosition(((ObjectAdapter) o));
		else if (o instanceof ButtonCommand)
			return getPosition((ButtonCommand) o);
		else
			return -1;

	}

	static int getCol(Object o) {
		if (o instanceof ObjectAdapter)
			return getCol(((ObjectAdapter) o));
		else if (o instanceof ButtonCommand)
			return getCol((ButtonCommand) o);
		else
			return -1;

	}

	VirtualComponent getComponent(int row, int col) {
		if (col >= childComponents.numCols())
			return filler(null);
		Object cell = childComponents.get(row, col);
		if (cell == null)
			return filler(cell);
		VirtualComponent retVal = getComponent(cell);
		if (retVal == null)
			return filler(cell);
		return retVal;
	}

	public static VirtualComponent getComponent(ObjectAdapter adapter) {
		VirtualComponent c = adapter.getUIComponent();
		if (adapter.getGenericWidget() == null)
			return c;
		return adapter.getGenericWidget().getContainer();
	}

	VirtualComponent getComponent(Object o) {
		if (o instanceof ObjectAdapter) {
			return getComponent((ObjectAdapter) o);
			// return ((uiObjectAdapter) o).getGenericWidget().getContainer();
		} else if (o instanceof ButtonCommand)
			return ((ButtonCommand) o).getButton();
		else if (o instanceof VirtualComponent)
			return (VirtualComponent) o;
		else
			return filler(o);
		// return null;
	}
	String getClassViewGroup(Object o) {
		if (o instanceof ObjectAdapter) {
			return ((ObjectAdapter) o).getClassViewGroup();
			// return ((uiObjectAdapter) o).getGenericWidget().getContainer();
		} else if (o instanceof ButtonCommand)
			return ((ButtonCommand) o).getClassViewGroup();
		return null;
	}

	static String getLabelAbove(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getLabelAbove();
		else if (o instanceof ButtonCommand)
			return ((ButtonCommand) o).getLabelAbove();
		else
			return null;
	}

	static String getLabelBelow(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getLabelBelow();
		else if (o instanceof ButtonCommand)
			return ((ButtonCommand) o).getLabelBelow();
		else
			return null;
	}

	static String getLabelLeft(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getLabelLeft();
		else if (o instanceof ButtonCommand)
			return ((ButtonCommand) o).getLabelLeft();
		else
			return null;
	}

	static String getLabelRight(Object o) {
		if (o instanceof ObjectAdapter)
			return ((ObjectAdapter) o).getLabelRight();
		else if (o instanceof ButtonCommand)
			return ((ButtonCommand) o).getLabelRight();
		else
			return null;
	}

	static boolean thereExistsLabelAbove(ADynamicSparseList objects) {
		for (int i = 0; i < objects.size(); i++) {
			// return (getLabelAbove(objects.get(i)) != null) ;
			if (getLabelAbove(objects.get(i)) != null)
				return true;
		}
		return false;
	}

	static boolean thereExistsLabelBelow(ADynamicSparseList objects) {
		for (int i = 0; i < objects.size(); i++) {
			// return (getLabelBelow(objects.get(i)) != null) ;
			if (getLabelBelow(objects.get(i)) != null)
				return true;
		}
		return false;
	}

	static boolean thereExistsLabelLeft(ADynamicSparseList objects) {
		for (int i = 0; i < objects.size(); i++) {
			// return (getLabelLeft(objects.get(i)) != null) ;
			if (getLabelLeft(objects.get(i)) != null)
				return true;
		}
		return false;
	}

	static boolean thereExistsLabelRight(ADynamicSparseList objects) {
		for (int i = 0; i < objects.size(); i++) {
			// return (getLabelRight(objects.get(i)) != null) ;
			if (getLabelRight(objects.get(i)) != null)
				return true;
		}
		return false;
	}

	static boolean thereExistsLabelLeftInColumn(DynamicMatrix matrix, int colNum) {
		for (int i = 0; i < matrix.numRows(); i++) {
			if (matrix.numCols(i) < colNum - 1)
				return false;
			return (getLabelLeft(matrix.get(i, colNum)) != null);
		}
		return false;
	}

	public boolean getLeftLabelColumn(Vector<VirtualComponent> retVal) {
		// Vector<VirtualComponent> retVal = new Vector();
		boolean foundLabel = false;
		int maxRows = Math.max(childComponents.numRows(), rowItems.size());
		for (int i = 0; i < maxRows; i++) {
			Object matrixItem = null;
			Object rowItem = null;
			if (i < childComponents.numRows() && childComponents.numCols(i) > 0)
				matrixItem = childComponents.get(i, 0);
			if (matrixItem != null) {
				String label = getLabelLeft(matrixItem);
				if (label != null)
					foundLabel = true;
				// c.add (createLeftLabel(getLabelLeft(matrixItem)));
				// c.add (createLeftLabel(label));
				retVal.addElement(createLeftLabel(matrixItem));
				continue;
			}
			if (i < rowItems.size())
				rowItem = rowItems.get(i);
			/*
			 * if (rowItem != null) foundLabel = true;
			 */
			String label = getLabelLeft(rowItem);
			if (label != null)
				foundLabel = true;
			// c.add (createLeftLabel(getLabelLeft(rowItem)));
			// c.add (createLeftLabel(label));
			retVal.add(createLeftLabel(rowItem));

		}

		return foundLabel;
	}

	public boolean fillLeftLabelColumn(VirtualContainer c) {
		boolean foundLabel = false;
		int maxRows = Math.max(childComponents.numRows(), rowItems.size());
		for (int i = 0; i < maxRows; i++) {
			Object matrixItem = null;
			Object rowItem = null;
			if (i < childComponents.numRows() && childComponents.numCols(i) > 0)
				matrixItem = childComponents.get(i, 0);
			if (matrixItem != null) {
				String label = getLabelLeft(matrixItem);
				if (label != null)
					foundLabel = true;
				// c.add (createLeftLabel(getLabelLeft(matrixItem)));
				// c.add (createLeftLabel(label));
				c.add(createLeftLabel(matrixItem));
				continue;
			}
			if (i < rowItems.size())
				rowItem = rowItems.get(i);
			/*
			 * if (rowItem != null) foundLabel = true;
			 */
			String label = getLabelLeft(rowItem);
			if (label != null)
				foundLabel = true;
			// c.add (createLeftLabel(getLabelLeft(rowItem)));
			// c.add (createLeftLabel(label));
			c.add(createLeftLabel(rowItem));

		}
		if (getObjectAdapter().getStretchRows())
		c.setLayout(new GridLayout(maxRows, 1));
		else
			c.setLayout(new OEGridLayout(maxRows, 1));
		// resetLayout
		// resetLayout(c, maxRows, 1);
		// setLayout(getObjectAdapter(), c, maxRows, 1);
		return foundLabel;
	}
	

	public boolean fillTopLabelRow(VirtualContainer c) {
		boolean foundLabel = false;
		int maxCols = Math.max(childComponents.numCols(0), columnItems.size());
		// if (childComponents.numRows() > 0) {
		// int numCols = childComponents.numCols(0);
		for (int colNum = 0; colNum < maxCols; colNum++) {
			if (0 < childComponents.numRows()
					&& colNum < childComponents.numCols(0)) {
				Object matrixItem = childComponents.get(0, colNum);
				if (matrixItem != null) {
					String label = getLabelAbove(matrixItem);
					if (label != null)
						foundLabel = true;
					// c.add (createTopLabel(getLabelAbove(matrixItem)));
					// VirtualLabel jLabel = createTopLabel(label);
					VirtualComponent jLabel = createTopLabel(matrixItem);
					// jLabel.setMaximumSize(new Dimension (jLabel.getWidth(),
					// 3));
					// jLabel.setSize(new Dimension (jLabel.getWidth(), 30));
					// jLabel.setMaximumSize(new Dimension (jLabel.getWidth(),
					// 3));
					// jLabel.setPreferredSize(new Dimension (jLabel.getWidth(),
					// 30));
					c.add(jLabel);

					continue;
				}
				if (colNum < columnItems.size()) {
					Object colItem = columnItems.get(colNum);
					/*
					 * if (colItem != null) { foundLabel = true;
					 * 
					 * //c.add (createTopLabel(getLabelAbove(colItem)));
					 * //continue; }
					 */
					String label = getLabelAbove(colItem);
					if (label != null)
						foundLabel = true;
					// VirtualLabel jLabel = createTopLabel(label);
					VirtualComponent jLabel = createTopLabel(colItem);
					// jLabel.setSize(new Dimension (jLabel.getWidth(), 3));
					// jLabel.setMaximumSize(new Dimension (jLabel.getWidth(),
					// 3));
					// jLabel.setPreferredSize(new Dimension (jLabel.getWidth(),
					// 3));
					c.add(jLabel);
					// c.add (createTopLabel(getLabelAbove(colItem)));

				}
			}
			if (rowItems.size() > 0) {
				Object rowItem = rowItems.get(0);
				if (rowItem != null) {
					String label = getLabelAbove(rowItem);
					if (label != null)
						foundLabel = true;
					// c.add (createTopLabel(getLabelAbove(rowItem)));
					c.add(createTopLabel(rowItem));
					// continue;
				}

			}

		}

		c.setLayout(new GridLayout(1, maxCols));
		return foundLabel;
	}

	public boolean fillBottomLabelRow(VirtualContainer c) {
		boolean foundLabel = false;
		int maxCols = Math.max(childComponents.numCols(0), columnItems.size());
		// if (childComponents.numRows() > 0) {
		// int numCols = childComponents.numCols(0);
		for (int colNum = 0; colNum < maxCols; colNum++) {
			if (0 < childComponents.numRows()
					&& colNum < childComponents.numCols(0)) {
				Object matrixItem = childComponents.get(0, colNum);
				if (matrixItem != null) {
					String label = getLabelBelow(matrixItem);
					if (label != null)
						foundLabel = true;
					// c.add (createBottomLabel(getLabelAbove(matrixItem)));
					// VirtualLabel jLabel = createBottomLabel(label);
					VirtualComponent jLabel = createBottomLabel(matrixItem);
					// jLabel.setMaximumSize(new Dimension (jLabel.getWidth(),
					// 3));
					// jLabel.setSize(new Dimension (jLabel.getWidth(), 30));
					// jLabel.setMaximumSize(new Dimension (jLabel.getWidth(),
					// 3));
					// jLabel.setPreferredSize(new Dimension (jLabel.getWidth(),
					// 30));
					c.add(jLabel);

					continue;
				}
				if (colNum < columnItems.size()) {
					Object colItem = columnItems.get(colNum);
					/*
					 * if (colItem != null) { foundLabel = true;
					 * 
					 * //c.add (createBottomLabel(getLabelAbove(colItem)));
					 * //continue; }
					 */
					String label = getLabelAbove(colItem);
					if (label != null)
						foundLabel = true;
					// VirtualLabel jLabel = createBottomLabel(label);
					VirtualComponent jLabel = createBottomLabel(colItem);
					// jLabel.setSize(new Dimension (jLabel.getWidth(), 3));
					// jLabel.setMaximumSize(new Dimension (jLabel.getWidth(),
					// 3));
					// jLabel.setPreferredSize(new Dimension (jLabel.getWidth(),
					// 3));
					c.add(jLabel);
					// c.add (createBottomLabel(getLabelAbove(colItem)));

				}
			}
			if (rowItems.size() > 0) {
				Object rowItem = rowItems.get(0);
				if (rowItem != null) {
					String label = getLabelAbove(rowItem);
					if (label != null)
						foundLabel = true;
					// c.add (createBottomLabel(getLabelAbove(rowItem)));
					c.add(createBottomLabel(rowItem));
					// continue;
				}

			}

		}

		c.setLayout(new GridLayout(1, maxCols));
		return foundLabel;
	}

	public boolean fillRightLabelColumn(VirtualContainer c) {
		boolean foundLabel = false;
		int maxRows = Math.max(childComponents.numRows(), rowItems.size());
		int lastColNum = childComponents.numCols() - 1;
		for (int i = 0; i < maxRows; i++) {
			Object matrixItem = null;
			Object rowItem = null;
			if (i < childComponents.numRows()
					&& childComponents.numCols(i) > lastColNum)
				matrixItem = childComponents.get(i, lastColNum);
			if (matrixItem != null) {
				String label = getLabelRight(matrixItem);
				if (label != null)
					foundLabel = true;
				// c.add (createRightLabel(getLabelRight(matrixItem)));
				c.add(createRightLabel(matrixItem));

				continue;
			}
			if (i < rowItems.size())
				rowItem = rowItems.get(i);
			// if (rowItem != null) foundLabel = true;
			String label = getLabelRight(rowItem);
			if (label != null)
				foundLabel = true;
			// c.add (createRightLabel(getLabelRight(rowItem)));
			c.add(createRightLabel(rowItem));

		}
		c.setLayout(new GridLayout(maxRows, 1));
		return foundLabel;
	}

	public boolean getRightLabelColumn(Vector<VirtualComponent> retVal) {
		// Vector<VirtualComponent> retVal = new Vector();
		boolean foundLabel = false;
		int maxRows = Math.max(childComponents.numRows(), rowItems.size());
		int lastColNum = childComponents.numCols() - 1;
		for (int i = 0; i < maxRows; i++) {
			Object matrixItem = null;
			Object rowItem = null;
			if (i < childComponents.numRows()
					&& childComponents.numCols(i) > lastColNum)
				matrixItem = childComponents.get(i, lastColNum);
			if (matrixItem != null) {
				String label = getLabelRight(matrixItem);
				if (label != null)
					foundLabel = true;
				// c.add (createRightLabel(getLabelRight(matrixItem)));
				retVal.add(createRightLabel(matrixItem));

				continue;
			}
			if (i < rowItems.size())
				rowItem = rowItems.get(i);
			// if (rowItem != null) foundLabel = true;
			String label = getLabelRight(rowItem);
			if (label != null)
				foundLabel = true;
			// c.add (createRightLabel(getLabelRight(rowItem)));
			retVal.add(createRightLabel(rowItem));

		}
		return foundLabel;
	}

	void setChild(int row, int col, Object element) {
		childComponents.setOrInsertNewRowSouth(row, col, element);
		/*
		 * String label; if ((label = getLabelAbove(element)) != null)
		 * childComponents.setOrInsertNewRowNorth( row -1, col, new JLabel
		 * (label)); if ((label = getLabelBelow(element)) != null)
		 * childComponents.setOrInsertNewRowSouth( row + 1, col, new JLabel
		 * (label)); if ((label = getLabelLeft(element)) != null)
		 * childComponents.setOrInsertNewColumnWest( row, col - 1, new JLabel
		 * (label)); if ((label = getLabelRight(element)) != null)
		 * childComponents.setOrInsertNewColumnEast( row, col - 1, new JLabel
		 * (label));
		 */

	}

	/*
	 * void fillMatrixLabel (DynamicMatrix childComponents, int row, int col,
	 * boolean north, boolean south, boolean west, boolean east) { Object
	 * element = childComponents.get( row, col); if (element == null) return;
	 * 
	 * String label; if (north && (label = getLabelAbove(element)) != null) { if
	 * (row == 0) childComponents.setOrInsertNewRowNorth( 0, col, new JLabel
	 * (label)); else childComponents.setOrInsertNewRowSouth( row -1, col, new
	 * JLabel (label)); } if (south && (label = getLabelBelow(element)) != null)
	 * childComponents.setOrInsertNewRowNorth( row + 1, col, new JLabel
	 * (label)); if (west && (label = getLabelLeft(element)) != null) { if (col
	 * == 0) childComponents.setOrInsertNewColumnWest( row, 0, new JLabel
	 * (label)); else childComponents.setOrInsertNewColumnEast( row, col - 1,
	 * new JLabel (label)); } if (east && (label = getLabelRight(element)) !=
	 * null) childComponents.setOrInsertNewColumnWest( row, col, new JLabel
	 * (label)); }
	 */
	/*
	 * VirtualLabel createLabel(String text) { return createLabel (null, text);
	 * }
	 */

	// JLabel createLabel (String text) {
	VirtualLabel createLabel(Object item, String text) {
		// VirtualComponent createLabel (String text) {
		if (text == null)
			return filler(item);
		/*
		 * else if (text.equals("")) text = " ";
		 */
		// JLabel label = new JLabel();
		// VirtualLabel label = LabelSelector.createLabel("");;
		VirtualLabel label = LabelSelector.createLabel(text);
		;
		/*
		 * int height = label.getHeight(); int width = label.getWidth();
		 */
		// label.setSize(new Dimension(100, 100));
		// label.setPreferredSize(label.getPreferredSize());
		/*
		 * JLabel label = new JLabel(text);
		 */
		ButtonCommand.maybeChangeLabelOrIcon(label, text, null);
		// label.setSize( new Dimension (160, 60));
		// label.setPreferredSize(new Dimension (160, 60));
		// return label;
		// label.setAlignmentX (Component.CENTER_ALIGNMENT);
		// label.setHorizontalAlignment(SwingConstants.CENTER);
		// label.setBorder(new LineBorder(Color.BLACK));
		// return label;
		return encloseAndSize(item, label);
	}

	/*
	 * VirtualLabel createLabel(String text, String icon) { return
	 * createLabel(null, text, icon); }
	 */
	// JLabel createLabel (String text, String icon) {
	VirtualLabel createLabel(Object item, String text, String icon) {
	
		if (text == null)
			return filler(item);
		// JLabel label = new JLabel();
		VirtualLabel label = LabelSelector.createLabel("");
		/*
		 * JLabel label = new JLabel(text);
		 */
		ButtonCommand.maybeChangeLabelOrIcon(label, text, icon);
		// return label;
		// label.setAlignmentX (Component.CENTER_ALIGNMENT);
		// label.setHorizontalAlignment(SwingConstants.CENTER);
		// label.setBorder(new LineBorder(Color.BLACK));
		// return label;
		return encloseAndSize(item, label);
	}

	static final String LL = "Left Label";

	boolean isLabel(Object o, String name) {
		return o != null && o instanceof Component
				&& ((Component) o).getName().equals(name);
	}

	boolean isLeftLabel(Object o) {
		return isLabel(o, LL);
	}
	Integer labelWidth (ObjectAdapter adapter) {
		return adapter.getLabelWidth();
	}
	Integer labelWidth (ButtonCommand c) {
//		MethodDescriptorProxy md = c.getMethodDescriptor();
//		Integer retVal = (Integer) AttributeManager.getInheritedAttributeValue(md, AttributeNames.LABEL_WIDTH, null);
		Integer retVal = c.getLabelWidth();
		return retVal;
	}

	VirtualLabel encloseAndSize(Object item, VirtualLabel l) {
		// return l;
		if (item == null)
			return l;
		Integer width = null;
		if (item instanceof ObjectAdapter)
			width = labelWidth ((ObjectAdapter) item);
		else if (item instanceof ButtonCommand)
			width = labelWidth ((ButtonCommand) item);
		
//		if (!(item instanceof uiObjectAdapter))
//			return l;
//		uiObjectAdapter itemAdapter = (uiObjectAdapter) item;
//		
//		int width = itemAdapter.getLabelWidth();
		if (width == null)
			return l;
		if (width == 0 || getObjectAdapter().getStretchRows())
			return l;
		// setWidth(l, getObjectAdapter().getLabelLength());
		OEMisc.setWidth(l, width);
		return l;
		/*
		 * VirtualContainer c = PanelSelector.createPanel(); //Misc.setWidth(c,
		 * width); c.add(l); //Misc.setWidth(c, width); return c;
		 */

	}

	// VirtualLabel createLeftLabel (String text) {
	Integer getHeight(Object item) {
		if (!(item instanceof ObjectAdapter)) return null;
		ObjectAdapter adapter = (ObjectAdapter) item;
		Integer height;
		if (adapter.getUIComponent() instanceof VirtualContainer)
			height = adapter.getContainerHeight();
		else
			height = adapter.getComponentHeight();
		return height;		
		
	}
	void maybeSetHeight(VirtualLabel label, Object item) {
		Integer height = getHeight(item);
		if (height == null) return;
		OEMisc.setHeight(label, height);
	
	}
	VirtualComponent createLeftLabel(Object item) {
		String text = getLabelLeft(item);
		// VirtualLabel label = createLabel(text);
		VirtualLabel label = createLabel(item, text);
		label.setName(LL + " " + item + ":" + text);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		maybeSetHeight(label, item);
		// label.setVerticalAlignment(SwingConstants.BOTTOM);
		// label.setHorizontalAlignment(SwingConstants.EAST);
		// label.setSize(new Dimension(60, label.getPreferredSize().height));
		// label.setPreferredSize(label.getSize());
		return label;

		// return encloseAndSize(item, label);
	}

	static final String RL = "Right Label";

	// VirtualLabel createRightLabel (String text) {
	VirtualComponent createRightLabel(Object item) {
		String text = getLabelRight(item);
		VirtualLabel label = createLabel(item, text);
		label.setName(RL + ":" + text);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.CENTER);
		return label;
		// return encloseAndSize(item, label);
	}

	static final String TL = "Top Label";

	boolean isTopLabel(Object o) {
		return isLabel(o, TL);
	}

	// VirtualLabel createTopLabel (String text) {
	VirtualComponent createTopLabel(Object item) {
		String text = getLabelAbove(item);
		// JLabel label = createLabel (text);
		VirtualLabel label = createLabel(item, text);
		label.setName(TL + ":" + text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		// return encloseAndSize(item, label);
		return label;
	}

	VirtualLabel createColumnTitleLabel(ObjectAdapter adapter) {
		return createColumnTitleLabel(adapter, adapter.columnTitle());
		/*
		 * VirtualLabel label =
		 * LabelSelector.createLabel(adapter.columnTitle()); label.setName(TL);
		 * label.setHorizontalAlignment(SwingConstants.CENTER);
		 * label.setVerticalAlignment(SwingConstants.CENTER); int width =
		 * adapter.getComponentWidth(); if (width != 0) Misc.setWidth(label,
		 * width); return label;
		 */

	}

	VirtualLabel createColumnTitleLabelFiller(ObjectAdapter adapter) {
		return createColumnTitleLabel(adapter, " ");
		/*
		 * VirtualLabel label =
		 * LabelSelector.createLabel(adapter.columnTitle()); label.setName(TL);
		 * label.setHorizontalAlignment(SwingConstants.CENTER);
		 * label.setVerticalAlignment(SwingConstants.CENTER); int width =
		 * adapter.getComponentWidth(); if (width != 0) Misc.setWidth(label,
		 * width); return label;
		 */

	}
	// must get the exact value, zero will not suffice
	// as it will be overridden by a default that is different
	// from the one used to get the default component width
	int getComponentWidth(ObjectAdapter adapter) {	
		
		if (adapter.isElided())
			return adapter.getElideComponentWidth();
		WidgetShell ws = adapter.getGenericWidget();
		if (ws != null) {
			VirtualContainer container = ws.getContainer();
			if (container != null) {
				int width = container.getWidth();
				if (width != 0)
					return width;
//				width = container.getPreferredSize().width;
//				if (width != 0)
//					return width;
				
				/*
				 * double prefferedWidth =
				 * container.getPreferredSize().getWidth(); double i =
				 * container.getMinimumSize().getWidth(); int j = width;
				 */
			}
		}
		Integer componentWidth = adapter.getComponentWidth();
		if (componentWidth != null) 
			return componentWidth;
//		if (componentWidth == null) {
			WidgetAdapterInterface componentWidgetAdapter = adapter.getWidgetAdapter();
			if (componentWidgetAdapter == null)
				return 0;
			return componentWidgetAdapter.defaultWidth();
		//}
		//return 0;
			//componentWidth = 0;
		// moved the code earlier
//		if (adapter.isElided())
//			componentWidth = adapter.getElideComponentWidth();
		//return componentWidth;

	}

	VirtualLabel createColumnTitleLabel(ObjectAdapter adapter,
			String columnTitle) {
		int componentWidth = getComponentWidth(adapter);
		// return createColumnTitleLabel(columnTitle,
		// adapter.getComponentWidth());
		return createColumnTitleLabel(columnTitle, componentWidth);
		/*
		 * VirtualLabel label = LabelSelector.createLabel(columnTitle);
		 * label.setName(TL);
		 * label.setHorizontalAlignment(SwingConstants.CENTER);
		 * label.setVerticalAlignment(SwingConstants.CENTER); int width =
		 * adapter.getComponentWidth(); if (width != 0) Misc.setWidth(label,
		 * width); return label;
		 */

	}

	VirtualLabel createColumnTitleLabel(String columnTitle, int width) {
		VirtualLabel label = LabelSelector.createLabel(columnTitle);
		// label.setBorder(new TitledBorder(" "));
		label.setName(columnTitle + "(CommandAndStatePanelAdapter-->createColumnTitleLabel)");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		// label.setFont(label.getFont().deriveFont(4));
		// int width = adapter.getComponentWidth();
		int height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width != 0)
			// Misc.setWidth(label, width);
			OEMisc.setSize(label, width, height);
		// Misc.setHeight(label, 20);
		ColumnTitleLabelAdded.newCase(getObjectAdapter(), label, this);
		return label;

	}

	VirtualLabel createColumnPrefixLabel(ObjectAdapter adapter) {
		String columnTitle = "+";
		int width = adapter.getColumnPrefixWidth();
		Color color = adapter.getColumnPrefixBackground();
		VirtualLabel label = LabelSelector.createLabel(columnTitle);
		// label.setBorder(new TitledBorder(" "));
		label.setName(TL);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		// label.setFont(label.getFont().deriveFont(4));
		// int width = adapter.getComponentWidth();
		int height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width != 0)
			// Misc.setWidth(label, width);
			OEMisc.setSize(label, width, height);
		if (color != null) {
			label.setBackground(AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR));
		}
		ColumnPrefixLabelAdded.newCase(getObjectAdapter(), label, this);


		// Misc.setHeight(label, 20);
		return label;

	}

	VirtualLabel createRowPrefixLabel(ObjectAdapter adapter, boolean isBlank) {
		String columnTitle = "+";
		if (isBlank)
			columnTitle = BLANK_TITLE;
		int width = adapter.getRowPrefixWidth();
		Color color = adapter.getRowPrefixBackground();
		VirtualLabel label = LabelSelector.createLabel(columnTitle);
		// label.setBorder(new TitledBorder(" "));
		label.setName(TL);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		// label.setFont(label.getFont().deriveFont(4));
		// int width = adapter.getComponentWidth();
		int height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width != 0)
			// Misc.setWidth(label, width);
			OEMisc.setSize(label, width, height);
		if (color != null) {
			label.setBackground(color);
		}

		// Misc.setHeight(label, 20);
		return label;

	}

	VirtualLabel createColumnSuffixLabel(ObjectAdapter adapter) {
		String columnTitle = "-";
		int width = adapter.getColumnSuffixWidth();
		Color color = adapter.getColumnSuffixBackground();
		VirtualLabel label = LabelSelector.createLabel(columnTitle);
		// label.setBorder(new TitledBorder(" "));
		label.setName(TL);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		// label.setFont(label.getFont().deriveFont(4));
		// int width = adapter.getComponentWidth();
		//int height = 11;
		int height = OEMisc.SWING_DEFAULT_HEIGHT;
		if (width != 0)
			// Misc.setWidth(label, width);
			OEMisc.setSize(label, width, height);
		if (color != null) {
			label.setBackground(color);
		}
		ColumnSuffixLabelAdded.newCase(getObjectAdapter(), label, this);


		// Misc.setHeight(label, 20);
		return label;

	}

	VirtualContainer getColumnTitlePanel() {
		if (unboundColumnTitlePanel == null) {
			unboundColumnTitlePanel = PanelSelector.createPanel();
			unboundColumnTitlePanel.setName("Unbound Column Title of " + getObjectAdapter().toDebugText());
			setColors(unboundColumnTitlePanel);
//			Color containerBackground = getObjectAdapter().getContainerBackground();
//				if (containerBackground != null) {
//					unboundColumnTitlePanel.setBackground(containerBackground);
//				}		
			
		}

		return unboundColumnTitlePanel;

	}

	@Override
	public void refillColumnTitle(CompositeAdapter firstRowAdapter) {

		if (getObjectAdapter().getSeparateUnboundTitles()
				&& unboundColumnTitlePanelNotToBeAdded
				&& firstRowAdapter.getShowChildrenColumnTitle()) {
			if (childIsFlatTable)
				fillHierarchicalColumnTitlePanel(getColumnTitlePanel(),
						firstRowAdapter);
			else
				fillColumnTitlePanel(getColumnTitlePanel(), firstRowAdapter);

		}

	}

	ObjectAdapter columnTitleAdapter;
	VirtualLabel columnTitleLabel;

	// void fillColumnTitlePanel(VirtualContainer panel, uiContainerAdapter
	// adapter) {
	@Override
	public void processDeferredFillColumnTitlePanel(CompositeAdapter adapter) {
		if (!titlePanelFillingDeferred)
			return;
		if (childIsFlatTable)
			fillHierarchicalColumnTitlePanel(getColumnTitlePanel(), adapter);
		else
			fillColumnTitlePanel(getColumnTitlePanel(), adapter);
	}

	boolean titlePanelFillingDeferred = false;
	ObjectAdapter titleRowAdapter = null;

	boolean oldfillColumnTitlePanel(VirtualContainer panel,
			CompositeAdapter adapter) {
		if (panel == null)
			return false;
		/*
		 * if (adapter.getChildAdapterCount() == 0) return;
		 */
		if (!adapter.getShowColumnTitles())
			return false;
		Vector<ObjectAdapter> labels = adapter.getColumnAdapters();
		if (labels.size() == 0) {
			titlePanelFillingDeferred = true;
			return true;
		}
		titlePanelFillingDeferred = false;
		panel.removeAll();
		// Vector<String> labels = adapter.getColumnTitles();
		// Vector<uiObjectAdapter> labels = adapter.getColumnAdapters();
		panel.setLayout(new BorderLayout());
		VirtualContainer childPanel = PanelSelector.createPanel();
		childPanel.setName("child panel of " + getObjectAdapter());
		setColors(childPanel, getObjectAdapter());
		// panel.setBackground(Color.lightGray);
		// childPanel.setBackground(Color.lightGray);
		// VirtualContainer panel = PanelSelector.createPanel();
		if (adapter.isTopDisplayedAdapter())
			setLayoutUnboundRow(adapter, childPanel, labels.size());
		// setLayoutUnboundColumnTitles(adapter, childPanel, labels.size());
		else
			// setLayoutUnboundColumnTitles(adapter.getParentAdapter(),
			// childPanel, labels.size());
			setLayoutUnboundRow(adapter.getParentAdapter(), childPanel, labels
					.size());
		// setLayoutRow(adapter, panel, labels.size());
		for (int i = 0; i < labels.size(); i++) {
			// childPanel.add(createColumnTitleLabel(labels.elementAt(i),
			// adapter));
			VirtualLabel label = createColumnTitleLabel(labels.elementAt(i));
			// System.out.println("Label Width" +
			// label.getPreferredSize().getWidth());
			childPanel.add(label);
			// Object[] models = new AColumnDisplayerAbstractModel[3];
			Object[] models;
			ObjectAdapter labelAdapter = labels.elementAt(i);

			if (labelAdapter.getParentAdapter().getProjectionGroups() == null)
				models = new AColumnDisplayerAbstractModel[2];
			else
				models = new AColumnDisplayerAbstractModel[3];
			models[0] = new AColumnHiderModel(labelAdapter);
			if (models.length == 3) {
				models[1] = new AProjectionGroupDisplayerModel(labelAdapter);
				models[2] = new AColumnSiblingDisplayerModel(labelAdapter);
			} else
				models[1] = new AColumnSiblingDisplayerModel(labelAdapter);
			RightMenuManager.bindToRightMenu(labelAdapter.getUIFrame(), label,
					models);
			if (getObjectAdapter() instanceof VectorAdapter
					|| getObjectAdapter() instanceof HashtableAdapter)
				label.addMouseListener(new ASortTriggerMouseListener(
						(CompositeAdapter) getObjectAdapter(), labelAdapter));
			// RightMenuManager.bindToRightMenu(label, new
			// AColumnDisplayerModel(labels.elementAt(i)));
			// panel.add(createColumnTitleLabel(labels.elementAt(i)));
		}
		panel.add(childPanel);
		if (adapter.isLabelled()
				&& adapter.getLabelPosition().equals(
						AttributeNames.LABEL_IS_LEFT)
				&& !"".equals(adapter.getLabel())) {
			// uiObjectAdapter childAdapter = adapter.getChildAdapterAt(0);
			int labelWidth = adapter.getLabelWidth();

			VirtualLabel parentLabel = LabelSelector.createLabel(" ");
			OEMisc.setWidth(parentLabel, labelWidth);
			panel.add(parentLabel, BorderLayout.WEST);
			parentLabel.setBackground(Color.lightGray);
			panel.validate();
		}
		return true;
	}

	public static void attachEventHandlersToColumnTitle(
			ObjectAdapter tableAdapter, ObjectAdapter labelAdapter,
			VirtualLabel label) {
		attachRightMenusToLabel(labelAdapter, label);
		if (!hasDescendentRows(tableAdapter)
				&&

				(tableAdapter instanceof VectorAdapter || tableAdapter instanceof HashtableAdapter))
			label.addMouseListener(new ASortTriggerMouseListener(
					(CompositeAdapter) tableAdapter, labelAdapter));

	}

	public static void attachRightMenusToLabel(ObjectAdapter labelAdapter,
			VirtualLabel label) {
		Vector models = new Vector();
		// if (labelAdapter instanceof uiContainerAdapter)
		if (labelAdapter.getParentAdapter().getAllowColumnElide())
		models.add(new AColumnEliderModel(labelAdapter));
		// if (!labelAdapter.getParentAdapter().hasFlatTableRowDescendent())
		if (labelAdapter.getParentAdapter().getAllowColumnHide()) {
		models.add(new AColumnHiderModel(labelAdapter));
		models.add(new AColumnSiblingDisplayerModel(labelAdapter));
		}
		if (labelAdapter.getParentAdapter().getProjectionGroups() != null) {
			models.add(new AProjectionGroupDisplayerModel(labelAdapter));
		}
		if (models.size() > 0)
		RightMenuManager.bindToRightMenu(labelAdapter.getUIFrame(), label,
				models);
		/*
		 * if (labelAdapter.getParentAdapter().getProjectionGroups() == null)
		 * models = new AColumnDisplayerAbstractModel[2]; else models = new
		 * AColumnDisplayerAbstractModel[3]; models[0] = new
		 * AColumnHiderModel(labelAdapter); if (models.length == 3) { models[1]
		 * = new AProjectionGroupDisplayerModel(labelAdapter); models[2] = new
		 * AColumnSiblingDisplayerModel(labelAdapter); } else models[1] = new
		 * AColumnSiblingDisplayerModel(labelAdapter);
		 * RightMenuManager.bindToRightMenu(labelAdapter.getUIFrame(), label,
		 * models);
		 */

	}

	public static void oldAttachRightMenusToLabel(ObjectAdapter labelAdapter,
			VirtualLabel label) {
		Object[] models;

		if (labelAdapter.getParentAdapter().getProjectionGroups() == null)
			models = new AColumnDisplayerAbstractModel[2];
		else
			models = new AColumnDisplayerAbstractModel[3];
		models[0] = new AColumnHiderModel(labelAdapter);
		if (models.length == 3) {
			models[1] = new AProjectionGroupDisplayerModel(labelAdapter);
			models[2] = new AColumnSiblingDisplayerModel(labelAdapter);
		} else
			models[1] = new AColumnSiblingDisplayerModel(labelAdapter);
		RightMenuManager.bindToRightMenu(labelAdapter.getUIFrame(), label,
				models);

	}

	boolean childIsFlatTable = false;
	// why is the value 0 or 1, why not boolean?
	int fillColumnTitlePanel(VirtualContainer panel, CompositeAdapter adapter) {
		if (panel == null)
			return 0;

		/*
		 * if (adapter.getChildAdapterCount() == 0) return;
		 */
		if (!adapter.getShowColumnTitles())
			return 0;
		// panel.removeAll();

		Vector<ObjectAdapter> labels = adapter.getColumnAdapters();
		// int labelHeight = adapter.getHeightOfNonShapeDescendents();
		// if (adapter.flattenTable() &&
		// adapter.getTopRowWithPrimitivesAndComposites() ) {
		if (adapter.isFlatTableRow()) {
			if (labels.size() == 0) {
				titlePanelFillingDeferred = true;
			} else
				fillHierarchicalColumnTitlePanel(panel, adapter);
			childIsFlatTable = true;
			return 1; // we will do this later
		}

		if (labels.size() == 0) {
			titlePanelFillingDeferred = true;
			// return labelHeight;
			return 1;
		}

		titlePanelFillingDeferred = false;
		panel.removeAll();
		// Vector<String> labels = adapter.getColumnTitles();
		// Vector<uiObjectAdapter> labels = adapter.getColumnAdapters();
		panel.setLayout(new BorderLayout());
		VirtualContainer childPanel = PanelSelector.createPanel();
		childPanel.setName (" Child Panel of " + getObjectAdapter());
		setColors(childPanel, getObjectAdapter());
		// panel.setBackground(Color.lightGray);
		// childPanel.setBackground(Color.lightGray);
		// VirtualContainer panel = PanelSelector.createPanel();
		if (adapter.isLabelled() && !adapter.getStretchColumns()) {
			setLayoutUnboundRow(adapter, childPanel, labels.size() + 1);
			// not clear this label is needed, in any case it screws things up in alignment
			VirtualLabel label = createColumnTitleLabel(BLANK_TITLE, adapter
					.getLabelWidth());
			childPanel.add(label);
		} else
			setLayoutUnboundRow(adapter, childPanel, labels.size());
		/*
		 * if (adapter.isTopDisplayedAdapter()) setLayoutUnboundRow(adapter,
		 * childPanel, labels.size()); //setLayoutLabelStack(adapter,
		 * childPanel,labelHeight, labels.size());
		 * //setLayoutUnboundColumnTitles(adapter, childPanel, labels.size());
		 * else //setLayoutUnboundColumnTitles(adapter.getParentAdapter(),
		 * childPanel, labels.size());
		 * //setLayoutUnboundRow(adapter.getParentAdapter(), childPanel,
		 * labels.size()); setLayoutUnboundRow(adapter, childPanel,
		 * labels.size()); //setLayoutLabelStack(adapter.getParentAdapter(),
		 * childPanel, labelHeight, labels.size()); //setLayoutRow(adapter,
		 * panel, labels.size());
		 */
		for (int i = 0; i < labels.size(); i++) {
			// childPanel.add(createColumnTitleLabel(labels.elementAt(i),
			// adapter));
			VirtualLabel label = createColumnTitleLabel(labels.elementAt(i));
			// System.out.println("Label Width" +
			// label.getPreferredSize().getWidth());
			childPanel.add(label);
			ObjectAdapter labelAdapter = labels.elementAt(i);
			attachEventHandlersToColumnTitle(getObjectAdapter(), labelAdapter,
					label);
			/*
			 * Object[] models ;
			 * 
			 * if (labelAdapter.getParentAdapter().getProjectionGroups() ==
			 * null) models = new AColumnDisplayerAbstractModel[2]; else models
			 * = new AColumnDisplayerAbstractModel[3]; models[0] = new
			 * AColumnHiderModel(labelAdapter); if (models.length == 3) {
			 * models[1] = new AProjectionGroupDisplayerModel(labelAdapter);
			 * models[2] = new AColumnSiblingDisplayerModel(labelAdapter); }
			 * else models[1] = new AColumnSiblingDisplayerModel(labelAdapter);
			 * RightMenuManager.bindToRightMenu(labelAdapter.getUIFrame(),
			 * label, models); if (getObjectAdapter() instanceof uiVectorAdapter
			 * || getObjectAdapter() instanceof uiHashtableAdapter)
			 * label.addMouseListener(new
			 * ASortTriggerMouseListener((uiContainerAdapter)
			 * getObjectAdapter(), labelAdapter));
			 * //RightMenuManager.bindToRightMenu(label, new
			 * AColumnDisplayerModel(labels.elementAt(i)));
			 * //panel.add(createColumnTitleLabel(labels.elementAt(i)));
			 */

		}
		/*
		 * for (int height = 1; height < labelHeight; height++) { for (int i =
		 * 0; i < labels.size(); i++) { VirtualLabel label =
		 * createColumnTitleLabelFiller(labels.elementAt(i));
		 * System.out.println("Label Width" +
		 * label.getPreferredSize().getWidth()); childPanel.add(label); } }
		 */
		panel.add(childPanel);
		if (adapter.isLabelled()
				&& adapter.getLabelPosition().equals(
						AttributeNames.LABEL_IS_LEFT)
				&& !"".equals(adapter.getLabel())) {
			// uiObjectAdapter childAdapter = adapter.getChildAdapterAt(0);
			int labelWidth = adapter.getLabelWidth();

			VirtualLabel parentLabel = LabelSelector.createLabel(" ");
			OEMisc.setWidth(parentLabel, labelWidth);
			panel.add(parentLabel, BorderLayout.WEST);
			parentLabel.setBackground(Color.lightGray);
			panel.validate();
		}
		return 1;
		// return labelHeight;
	}

	static final String BL = "Bottom Label";

	boolean isBottomLabel(Object o) {
		return isLabel(o, BL);
	}

	// VirtualLabel createBottomLabel (String text) {
	VirtualComponent createBottomLabel(Object item) {
		String text = getLabelBelow(item);
		VirtualLabel label = createLabel(item, text);
		label.setName(BL + ":" + text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.TOP);
		return label;
		// return encloseAndSize(item, label);
	}

	void fillMatrixLabels(DynamicMatrix childComponents, boolean north,
			boolean south, boolean west, boolean east,
			boolean eastWestBoundaries, boolean northSouthBoundaries) {
		fillMatrixLabels(childComponents, north, south, west, east,
				northSouthBoundaries, eastWestBoundaries, 0, childComponents
						.numRows(), 0, childComponents.numCols(), true);

	}

	void fillMatrixLabelsExceptBoundaries(DynamicMatrix childComponents,
			boolean north, boolean south, boolean west, boolean east) {
		fillMatrixLabels(childComponents, north, south, west, east, false,
				false, 0, childComponents.numRows(), 0, childComponents
						.numCols(), false);

	}

	void fillMatrixLabelsExceptEastWestBoundaries(
			DynamicMatrix childComponents, boolean north, boolean south,
			boolean west, boolean east) {
		fillMatrixLabels(childComponents, north, south, west, east, true,
				false, 0, childComponents.numRows(), 0, childComponents
						.numCols(), false);

	}

	void fillMatrixLabelsExceptNorthSouthBoundaries(
			DynamicMatrix childComponents, boolean north, boolean south,
			boolean west, boolean east) {
		fillMatrixLabels(childComponents, north, south, west, east, false,
				true, 0, childComponents.numRows(), 0, childComponents
						.numCols(), false);

	}

	void fillMatrixLabels(DynamicMatrix childComponents, boolean north,
			boolean south, boolean west, boolean east,
			boolean northSouthBoundaries, boolean eastWestBoundaries,
			int startRow, int stopRow, int startCol, int stopCol,
			boolean boundaries) {
		// int row = 0;
		int row = startRow;
		while (row < childComponents.numRows()) {
			int col = 0;
			int curRow = row;
			int nextRow = row + 1;
			while (col < childComponents.numCols(curRow) && col < stopCol) {
				int curCol = col;
				Object element = childComponents.get(curRow, curCol);
				if (element == null) {
					col++;
					continue;
				}
				String label;

				if (west && (label = getLabelLeft(element)) != null) {
					// JPanel enclosing = new JPanel();
					// enclosing.add(createLeftLabel(label));
					boolean inserted;
					if (!(curCol == 0 && /* !boundaries */!eastWestBoundaries)) {
						// { col++; continue;}

						// else {
						if (curCol == 0) {
							// inserted =
							// childComponents.setOrInsertNewColumnWest( curRow,
							// 0, createLeftLabel(label));
							inserted = childComponents
									.setOrInsertNewColumnWest(curRow, 0,
											createLeftLabel(element));
							// print (childComponents);
							// inserted =
							// childComponents.setOrInsertNewColumnWest( curRow,
							// 0, enclosing);
						} else {
							// inserted =
							// childComponents.setOrInsertNewColumnEast( curRow,
							// curCol - 1, createLeftLabel(label));
							inserted = childComponents
									.setOrInsertNewColumnEast(curRow,
											curCol - 1,
											createLeftLabel(element));
							// print (childComponents);
							// inserted =
							// childComponents.setOrInsertNewColumnWest( curRow,
							// 0, enclosing);
						}
						if (inserted) {
							col++;
							curCol++;
							stopCol++;
						}
					}
				}
				// print (childComponents);
				if (east && (label = getLabelRight(element)) != null) {
					if (!(curCol == stopCol - 1 && /* !boundaries */!eastWestBoundaries)) {
						// {col++; continue;}
						if (childComponents.setOrInsertNewColumnWest(curRow,
								curCol + 1, createRightLabel(element)))
							col++;
						// print (childComponents);
					}
				}
				if (north && (label = getLabelAbove(element)) != null) {
					if (!(curRow == 0 && /* !boundaries */!northSouthBoundaries)) { // {
						// col++;
						// continue;}
						// col ++;

						boolean inserted;
						if (row == 0) {
							inserted = childComponents.setOrInsertNewRowNorth(
									0, curCol, createTopLabel(element));
							print(childComponents);
						} else {
							inserted = childComponents
									.setOrInsertNewRowSouth(curRow - 1, curCol,
											createTopLabel(element));
							print(childComponents);

						}
						if (inserted) {
							nextRow++;
							curRow++;
						}
					}
				}
				if (south && (label = getLabelBelow(element)) != null) {
					if (!(curRow == stopRow - 1 && /* !boundaries */!northSouthBoundaries)) {// {row++;
						// continue;}
						if (childComponents.setOrInsertNewRowNorth(curRow + 1,
								curCol, createBottomLabel(element)))
							nextRow++;
					}
				}

				col++;
			}
			row = nextRow;
		}

		/*
		 * for (int i = 0; i < childComponents.numRows(); i++) { for (int j = 0;
		 * j < childComponents.numCols(i); ++) { fillMatrixLabel
		 * (childComponents, i, j, north, south, west, east); } }
		 */
	}

	void fillRowLabels(ADynamicSparseList row, boolean north, boolean south) {
		/*
		 * for (int i = 0; i < row.size(); i++) { fillRowLabels (row, i, north,
		 * south); }
		 */
		int rowNum = 0;
		while (rowNum < row.size()) {
			int curRow = rowNum;
			int nextRow = rowNum + 1;
			Object element = row.get(curRow);
			if (element == null) {
				rowNum++;
				continue;
			}
			;
			String label;

			if (north && (label = getLabelAbove(element)) != null) {
				boolean inserted = false;
				if (rowNum == 0) {
					inserted = row.setOrInsertNewElementAbove(0,
							createTopLabel(element));
				} else {
					inserted = row.setOrInsertNewElementBelow(curRow - 1,
							createTopLabel(element));

				}
				if (inserted) {
					nextRow++;
					curRow++;
				}
			}
			if (south && (label = getLabelBelow(element)) != null) {
				if (row.setOrInsertNewElementAbove(curRow + 1,
						createBottomLabel(element)))
					nextRow++;
			}

			rowNum = nextRow;
		}

	}

	void fillColumnLabels(ADynamicSparseList column, boolean west, boolean east) {
		/*
		 * for (int i = 0; i < row.size(); i++) { fillRowLabels (row, i, north,
		 * south); }
		 */
		int colNum = 0;
		while (colNum < column.size()) {
			int curCol = colNum;
			int nextCol = colNum + 1;
			Object element = column.get(curCol);
			if (element == null) {
				colNum++;
				continue;
			}
			;
			String label;

			if (west && (label = getLabelLeft(element)) != null) {
				boolean inserted = false;
				if (colNum == 0) {
					// inserted = column.setOrInsertNewElementAbove( 0,
					// createLeftLabel(label));
					inserted = column.setOrInsertNewElementAbove(0,
							createLeftLabel(element));
				} else {
					// inserted = column.setOrInsertNewElementBelow(curCol -1,
					// createLeftLabel(label));
					inserted = column.setOrInsertNewElementBelow(curCol - 1,
							createLeftLabel(element));

				}
				if (inserted) {
					nextCol++;
					curCol++;
				}
			}
			if (east && (label = getLabelRight(element)) != null) {
				if (column.setOrInsertNewElementAbove(curCol + 1,
						createRightLabel(element)))
					nextCol++;
			}

			colNum = nextCol;
		}

	}

	/*
	 * void fillRowLabels (ADynamicSparseList row, int index, boolean north,
	 * boolean south) { Object element = row.get(index); if (element == null)
	 * return; String label; if (north && (label = getLabelAbove(element)) !=
	 * null) if (index == 0) row.setOrInsertNewElementAbove(0,
	 * createLabel(label)); else row.setOrInsertNewElementBelow(index - 1,
	 * createLabel(label)); if (south && (label = getLabelBelow(element)) !=
	 * null) row.setOrInsertNewElementBelow(index + 1, createLabel(label)); }
	 */

	/*
	 * void processAdapter (uiObjectAdapter compAdapter) { int row = getRow
	 * (compAdapter); int col = getCol (compAdapter); if (row < 0 && col < 0) {
	 * //numUnboundComponents ++; unboundAdapters.addElement(compAdapter); }
	 * else if (row < 0 || col < 0) {
	 * partiallyBoundAdapters.addElement(compAdapter); } else {
	 * 
	 * //maxRow = Math.max( maxRow, getRow (compAdapter)); //maxRow = Math.max(
	 * maxRow, getRow (compAdapter));
	 * //System.out.println(compAdapter.getPropertyName() + "Col:" +
	 * compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)); //maxCol =
	 * Math.max(maxCol, getCol (compAdapter)); setChild (row, col, compAdapter);
	 * 
	 * //childComponents.set(row, col, compAdapter); } }
	 */
	// return true if something added
	void processClassViewGroup(Object attributed, String classViewGroup) {
		if (AttributeNames.DRAW_VIEW_GROUP.equals(classViewGroup)) {
			VirtualComponent virtualComponent = getComponent(attributed);
			Component component = (Component) virtualComponent.getPhysicalComponent();
			uiFrame frame = adapter.getUIFrame();
			frame.showDrawPanel();
			try {
			ComponentModel componentModel = new ComponentModel(component);
			frame.getDrawing().put(attributed.toString(), componentModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ;
		}
		ObjectAdapter ancestorAdapter = adapter.getAncestorWhoseClassIs(classViewGroup);
		WidgetAdapterInterface ancestorWidgetAdapter = ancestorAdapter.getWidgetAdapter();
		if (ancestorWidgetAdapter instanceof CommandAndStatePanelAdapter) {
			CommandAndStatePanelAdapter ancestorCommandAndStatePanelAdapter = (CommandAndStatePanelAdapter) ancestorWidgetAdapter;
			ancestorCommandAndStatePanelAdapter.processCommandOrAdapter(attributed);
			addToAncestor(ancestorCommandAndStatePanelAdapter, attributed);
		}
	}
		
	
	boolean maybeProcessCommandOrAdapter(Object attributed) {
		boolean retVal;
		if (manualAdds) {

			return false;
		}
		String classViewGroup = getClassViewGroup(attributed);
		if (classViewGroup != null) {
			processClassViewGroup(attributed, classViewGroup);
			return false;
		}
		return processCommandOrAdapter(attributed);
		
		// Boolean showButton = (Boolean)
		// ClassDescriptor.getInheritedAttribute(c.getMethod(),
		// AttributeNames.SHOW_BUTTON).getValue();

//		int row = getRow(attributed);
//		int col = getCol(attributed);
//		if (row < 0 && col < 0) {
//			retVal = true;
//			// numUnboundComponents ++;
//			if (attributed instanceof uiObjectAdapter) {
//				processProperty((uiObjectAdapter) attributed);
//
//				// unboundProperties.addElement(attributed);
//			} else {
//				if (attributed instanceof ButtonCommand) {
//					ButtonCommand command = (ButtonCommand) attributed;
//					// Boolean showButton = (Boolean)
//					// AttributeManager.getInheritedAttribute(command.getMethod(),
//					// AttributeNames.SHOW_BUTTON).getValue();
//					// if (showButton &&
//					// getObjectAdapter().getShowUnboundButtons()) {
//					if (getObjectAdapter().getShowUnboundButtons()) {
//
//						// (getObjectAdapter().getShowUnboundButtons() )
//						//unboundButtons.addElement(attributed);
////						String classViewGroup = command.getClassViewGroup();
////						if (classViewGroup == null)
//							processCommand((ButtonCommand) command);
////						else {
////							
////						}
//					} else
//						return false;
//				} else
//					return false;
//			}
//		}
//		/*
//		 * } else if (row < 0 || col < 0) partiallyBound.addElement(attributed);
//		 */
//		else if (row < 0)
//			columnItems.set(col, attributed);
//		else if (col < 0) {
//			if (!childComponentsOverriden())
//				rowItems.set(row, attributed);
//			else
//				childComponents.set(row, 0, attributed);
//
//		} else {
//
//			// maxRow = Math.max( maxRow, getRow (compAdapter));
//			// maxRow = Math.max( maxRow, getRow (compAdapter));
//			// System.out.println(compAdapter.getPropertyName() + "Col:" +
//			// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN));
//			// maxCol = Math.max(maxCol, getCol (compAdapter));
//			setChild(row, col, attributed);
//
//			// childComponents.set(row, col, compAdapter);
//
//		}
//		return true;
	}
	boolean processCommandOrAdapter(Object attributed) {
		boolean retVal;
//		if (manualAdds) {
//
//			return false;
//		}
//		String classViewGroup = getClassViewGroup(attributed);
//		if (classViewGroup != null) {
//			processClassViewGroup(attributed, classViewGroup);
//			return false;
//		}
		
		// Boolean showButton = (Boolean)
		// ClassDescriptor.getInheritedAttribute(c.getMethod(),
		// AttributeNames.SHOW_BUTTON).getValue();

		int row = getRow(attributed);
		int col = getCol(attributed);
		if (adapter.isGridBagLayout()) { // we will handle it specially, assuming it goes to the childComponents matrix

			if (attributed instanceof ButtonCommand && row < 0 && col < 0) { // show method in menu 
				return false;
			}
			if (row < 0 ) {
//				Tracer.warning("No row number for grid bag component:" + adapter + " assuming it is 0");
				row = 0;
			}
			if (col < 0) {
//				Tracer.warning("No column number for grid bag component:" + adapter + " assuming it is 0");
				col = 0;

			}
			
			
		}
//		if (row < 0 && attributed instanceof ButtonCommand) {
//			row = getPosition(attributed);
//		}
		if (row < 0 && col < 0) {
			retVal = true;
			// numUnboundComponents ++;
			if (attributed instanceof ObjectAdapter) {
				processProperty((ObjectAdapter) attributed);

				// unboundProperties.addElement(attributed);
			} else {
				if (attributed instanceof ButtonCommand) {
					ButtonCommand command = (ButtonCommand) attributed;
					// Boolean showButton = (Boolean)
					// AttributeManager.getInheritedAttribute(command.getMethod(),
					// AttributeNames.SHOW_BUTTON).getValue();
					// if (showButton &&
					// getObjectAdapter().getShowUnboundButtons()) {
					if (getObjectAdapter().getShowUnboundButtons()) {

						// (getObjectAdapter().getShowUnboundButtons() )
						//unboundButtons.addElement(attributed);
//						String classViewGroup = command.getClassViewGroup();
//						if (classViewGroup == null)
							processCommand((ButtonCommand) command);
//						else {
//							
//						}
					} else
						return false;
				} else
					return false;
			}
		}
		/*
		 * } else if (row < 0 || col < 0) partiallyBound.addElement(attributed);
		 */
		else if (row < 0)
			columnItems.set(col, attributed);
		else if (col < 0) {
			if (!childComponentsOverriden())
				rowItems.set(row, attributed);
			else
				childComponents.set(row, 0, attributed);

		} else {

			// maxRow = Math.max( maxRow, getRow (compAdapter));
			// maxRow = Math.max( maxRow, getRow (compAdapter));
			// System.out.println(compAdapter.getPropertyName() + "Col:" +
			// compAdapter.getMergedAttributeValue(AttributeNames.COLUMN));
			// maxCol = Math.max(maxCol, getCol (compAdapter));
			setChild(row, col, attributed);

			// childComponents.set(row, col, compAdapter);

		}
		return true;
	}

	void processPartiallyBound(Object compAdapter) {
		int row = getRow(compAdapter);
		int col = getCol(compAdapter);
		if (row < 0) {
			row = childComponents.lastFilledRow(col) + 1;
		} else if (col < 0) {
			col = childComponents.lastFilledColumn(row) + 1;
		}
		setChild(row, col, compAdapter);

	}

	/*
	 * void processUnbound () { for (int i = 0; i < unbound.size(); i++)
	 * processUnbound (unbound.elementAt(i)); }
	 */
	/*
	 * void processPartiallyBound () { for (int i = 0; i <
	 * partiallyBound.size(); i++) processPartiallyBound
	 * (partiallyBound.elementAt(i)); }
	 */
	void processUnbound(int startPropIndex, int startButtonsIndex) {

		CompositeAdapter objectAdapter = (CompositeAdapter) getObjectAdapter();
		if (objectAdapter.getDirection() == null)
			verticalUnbound(startPropIndex, startButtonsIndex);
		else if (objectAdapter.getDirection().equals(AttributeNames.HORIZONTAL))
			horizontalUnbound(startPropIndex, startButtonsIndex);
		else if (objectAdapter.getDirection().equals(AttributeNames.VERTICAL))
			verticalUnbound(startPropIndex, startButtonsIndex);
		else if (objectAdapter.getDirection().equals(AttributeNames.BOX))
			boxUnbound(startPropIndex, startButtonsIndex);
		numUnboundPropertiesAdded = unboundProperties.size();
		//numUnboundButtonsAdded = unboundButtons.size();
		numUnboundButtonsAdded = unSortedCommandsList.size() + sortedCommandsList.size();

	}

	int numUnboundPropertiesAdded = 0;

	int insertUnsortedPropertiesFrom = 0;

	int insertSortedPropertiesFrom = 0;

	int numUnboundButtonsAdded = 0;

	void processUnbound() {
		processUnbound(numUnboundPropertiesAdded, numUnboundButtonsAdded);
	}

	int firstUnboundRow;

	// int lastRow;
	// int lastCol;
	/*
	 * void horizontalUnbound(int startPropIndex, int startButtonsIndex) { int
	 * row = childComponents.numRows(); //int row = firstUnboundRow; for (int i
	 * = startPropIndex; i < unboundProperties.size(); i++) { setChild (row, i,
	 * unboundProperties.elementAt(i)); } for (int i = startButtonsIndex; i <
	 * unboundButtons.size(); i++) { setChild (row, i,
	 * unboundButtons.elementAt(i)); } }
	 */

	void horizontalUnbound(int startPropIndex, int startButtonsIndex) {
		int index = columnItems.lastFilledSlot() + 1;
		// int row = firstUnboundRow;
		for (int i = startPropIndex; i < unboundProperties.size(); i++) {
			columnItems.set(index++, unboundProperties.elementAt(i));
		}
		for (int i = startButtonsIndex; i < unboundButtons.size(); i++) {
			columnItems.set(index++, unboundButtons.elementAt(i));
		}
	}

	/*
	 * void verticalUnbound(int startPropIndex, int startButtonsIndex) { int row
	 * = childComponents.numRows(); //int row = firstUnboundRow; for (int i =
	 * startPropIndex; i < unboundProperties.size(); i++) { setChild (row++, 0,
	 * unboundProperties.elementAt(i)); } for (int i = startButtonsIndex; i <
	 * unboundButtons.size(); i++) { setChild (row++, 0,
	 * unboundButtons.elementAt(i)); } }
	 */

	void verticalUnbound(int startPropIndex, int startButtonsIndex) {
		int index = rowItems.lastFilledSlot() + 1;
		// int row = firstUnboundRow;
		for (int i = startPropIndex; i < unboundProperties.size(); i++) {
			rowItems.set(index++, unboundProperties.elementAt(i));
		}
		for (int i = startButtonsIndex; i < unboundButtons.size(); i++) {
			rowItems.set(index++, unboundButtons.elementAt(i));
		}
	}

	void boxUnbound(int startPropIndex, int startButtonsIndex) {
		int numCols = Math.max(4, childComponents.numCols());
		int row = childComponents.numRows();
		// int row = firstUnboundRow;
		int col = 0;
		for (int i = startPropIndex; i < unboundProperties.size(); i++) {
			setChild(row, col++, unboundProperties.elementAt(i));
			if (col >= numCols) {
				row++;
				col = 0;
			}
		}
		for (int i = startButtonsIndex; i < unboundButtons.size(); i++) {
			setChild(row, col++, unboundButtons.elementAt(i));
			if (col >= numCols) {
				row++;
				col = 0;
			}
		}
	}
	Map<CommandAndStatePanelAdapter, java.util.List> ancestorState = new HashMap();
	void addToAncestor(CommandAndStatePanelAdapter ancestor, Object attributed) {
		List attributedList = ancestorState.get(ancestor);
		if (attributedList == null) {
			attributedList = new ArrayList();
			ancestorState.put(ancestor, attributedList);
		}
		attributedList.add(attributed);		
	}
	public void clearAncestorState() {
		ancestorState.clear();
	}

	boolean processCommands(Vector commands, ObjectAdapter adapter) {
		boolean retVal = false;
		/*
		 * if (!getObjectAdapter().getShowButtons() &&
		 * !getObjectAdapter().getShowUnboundButtons()) return false;
		 */
		// boolean hideUnBoundButtons =
		// !getObjectAdapter().getShowUnboundButtons();
		/*
		 * if (!getObjectAdapter().getShowUnboundButtons()) return false;
		 */
		for (Enumeration elements = commands.elements(); elements
				.hasMoreElements();) {
			// Command c = (Command) commands.elementAt(i);
			ButtonCommand c = (ButtonCommand) elements.nextElement();
			/*
			 * Boolean showButton = (Boolean)
			 * ClassDescriptor.getInheritedAttribute(c.getMethod(),
			 * AttributeNames.SHOW_BUTTON).getValue(); if (!showButton &&
			 * hideUnBoundButtons) continue;
			 */
			if (manualAdds)
				manualAdd(c);
			else {
				retVal |= maybeProcessCommandOrAdapter(c);
//				String classViewGroup = c.getClassViewGroup();
//				if (classViewGroup == null) {
//					retVal |= processCommandOrAdapter(c);
//				} else {
//					if (AttributeNames.DRAW_VIEW_GROUP.equals(classViewGroup)) {
//						uiFrame frame = adapter.getUIFrame();
//						frame.showDrawPanel();
//						try {
//						ComponentModel componentModel = new ComponentModel((Component) c.getButton().getPhysicalComponent());
//						frame.getDrawing().put(c.getLabel(), componentModel);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						return retVal;
//					}
//					uiObjectAdapter ancestorAdapter = adapter.getAncestorWhoseClassIs(classViewGroup);
//					uiWidgetAdapterInterface ancestorWidgetAdapter = ancestorAdapter.getWidgetAdapter();
//					if (ancestorWidgetAdapter instanceof CommandAndStatePanelAdapter) {
//						CommandAndStatePanelAdapter ancestorCommandAndStatePanelAdapter = (CommandAndStatePanelAdapter) ancestorWidgetAdapter;
//						ancestorCommandAndStatePanelAdapter.processCommandOrAdapter(c);
//						addToAncestor(ancestorCommandAndStatePanelAdapter, c);
//					}
//				}
				//retVal |= processCommandOrAdapter(c);
			}
		}
		return retVal;

	}

	/*
	 * public boolean processDirectionBean (String direction) { return true; }
	 */
	void resetLayout(VirtualContainer c, int numRows, int numCols, int hgap,
			int vgap) {
		LayoutManager lm = (LayoutManager) c.getLayout();
		if (lm instanceof OEGridLayout) {
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, vgap));
		} else if (lm instanceof GridLayout) {
			c.setLayout(new GridLayout(numRows, numCols, hgap, vgap));
		}

	}

	void resetLayout(VirtualContainer c, int numRows, int numCols) {
		resetLayout(c, numRows, numCols, getObjectAdapter().getHorizontalGap(),
				getObjectAdapter().getVerticalGap());

	}
	
	

	
	void setLayout(ObjectAdapter adapter, VirtualContainer c, int numRows,
			int numCols, int hgap, int vgap) {
		if (adapter.isNoLayout())
			c.setLayout((LayoutManager) null);
			//c.setLayout(new FlowLayout());
		else
		if (adapter.isFlowLayout()) {
			FlowLayout flowLayout = new FlowLayout(adapter.getAlignment(), adapter.getHorizontalGap(), adapter.getVerticalGap());
//			c.setLayout(new FlowLayout(adapter.getAlignment(), adapter.getHorizontalGap(), adapter.getVerticalGap()));
			c.setLayout(flowLayout);

		} if (adapter.isBorderLayout()) {
			BorderLayout borderLayout = new BorderLayout();
			c.setLayout(borderLayout);
		} else if (adapter.isGridBagLayout()) {
			GridBagLayout gridBagLayout = new GridBagLayout();
			c.setLayout(gridBagLayout);
		}
		
		else if (/* numCols == 1 || */adapter.getStretchRows()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, vgap));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}
	void setUnboundLayout(ObjectAdapter adapter, VirtualContainer c, int numRows,
			int numCols, int hgap, int vgap) {
		if (adapter.isUnboundNoLayout())
			c.setLayout((LayoutManager) null);
			//c.setLayout(new FlowLayout());
		else
		if (adapter.isUnboundFlowLayout()) {
			c.setLayout(new FlowLayout(adapter.getAlignment(), adapter.getHorizontalGap(), adapter.getVerticalGap()));
		} else
		

		if (/* numCols == 1 || */adapter.getStretchRows()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, vgap));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}
	
	void setColumnLayout(ObjectAdapter adapter, VirtualContainer c, 
			int numCols) {
		

		if (/* numCols == 1 || */adapter.getStretchColumns()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(1, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			c.setLayout(new OEGridLayout(1, numCols));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}

	void setLayoutUnbound(ObjectAdapter adapter, VirtualContainer c,
			int numRows, int numCols, int hgap, int vgap) {
		if (adapter.isUnboundNoLayout())
			c.setLayout((LayoutManager) null);
			//c.setLayout(new FlowLayout());
		else
		if (adapter.isUnboundFlowLayout()) {
			c.setLayout(new FlowLayout(adapter.getAlignment(), hgap, vgap));
			
		}
		else
		if (/* numCols == 1 || */adapter.getStretchUnboundRows()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, vgap));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}

	void setLayoutLabelStack(ObjectAdapter adapter, VirtualContainer c,
			int numRows, int numCols) {

		if (/* numCols == 1 || */adapter.getStretchUnboundColumns()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			c.setLayout(new OEGridLayout(numRows, numCols,
					getObjectAdapter().getHorizontalGap(), getObjectAdapter().getVerticalGap()));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}

	void setLayoutRow(ObjectAdapter adapter, VirtualContainer c, int numCols) {
		setLayoutRow(adapter, c, numCols, getObjectAdapter().getHorizontalBoundGap()
				);

	}
	void setLayoutColumn(ObjectAdapter adapter, VirtualContainer c, int numRows) {
		setLayoutColumn(adapter, c, numRows, getObjectAdapter().getHorizontalBoundGap(),
				getObjectAdapter().getVerticalGap());

	}

	void setLayoutUnboundRow(ObjectAdapter adapter, VirtualContainer c,
			int numCols) {
//		setLayoutUnboundRow(adapter, c, numCols, getObjectAdapter().getHorizontalGap(),
//				getObjectAdapter().getVerticalGap());
		setLayoutUnboundRow(adapter, c, numCols, adapter.getHorizontalGap()
				);

	}

	void setLayoutUnboundColumnTitles(ObjectAdapter adapter,
			VirtualContainer c, int numCols) {
		c.setLayout(new GridLayout(1, numCols));
		// setLayoutUnboundRow(adapter, c, numCols, uiGridLayout.DEFAULT_HGAP,
		// uiGridLayout.DEFAULT_VGAP);

	}

	void setLayoutRow(ObjectAdapter adapter, VirtualContainer c, int numCols,
			int hgap) {
		int numRows = 1;
		
		if (adapter.isFlowLayout()) {
			c.setLayout(new FlowLayout(adapter.getAlignment(), hgap, 0));
			
		} else

		if (/* numCols == 1 || */adapter.getStretchColumns()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, 0));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}
	void setLayoutColumn(ObjectAdapter adapter, VirtualContainer c, int numRows,
			int hgap, int vgap) {
		int numCols = 1;
		if (/* numCols == 1 || */adapter.getStretchRows()) {

		//if (/* numCols == 1 || */adapter.getStretchColumns()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, vgap));
			// resetLayout(c, new uiGridLayout(numRows, numCols, 1, 1));
			// resetLayout (c, new GridLayout(numRows, numCols));
			// c.setLayout(new uiGridLayout(numRows, numCols));
		}

	}

	void setLayoutUnboundRow(ObjectAdapter adapter, VirtualContainer c,
			int numCols, int hgap) {
		int numRows = 1;

	
		if (/* numCols == 1 || */adapter.getStretchUnboundColumns()) {
			// if (numCols == 1 || adapter.getStretchRows()) {
			c.setLayout(new GridLayout(numRows, numCols));

		} else {
			/*
			 * if (numRows == 1) { FlowLayout lm = new FlowLayout ();
			 * lm.setAlignment(FlowLayout.LEADING); c.setLayout( lm); return; }
			 */

			// c.setLayout(new uiGridLayout(numRows, numCols, 1, 1));
			//c.setLayout(new());
			c.setLayout(new OEGridLayout(numRows, numCols, hgap, 0));
			
		}

	}

	public static void resetWidget(VirtualComponent c) {
		VirtualContainer p = null;
		if (c instanceof VirtualContainer)
			p = (VirtualContainer) c;
		if (p == null)
			return;
		VirtualComponent[] vcs = p.getComponents();
		if (vcs.length == 0)
			return;
		LayoutManager l = (LayoutManager) p.getLayout();
		p.setLayout(l);
		for (int i = 0; i < vcs.length; i++) {
			// p.remove(vcs[i]);
			// p.remove(vcs[i]);
			resetWidget(vcs[i]);
			// p.add(vcs[i]);
		}

	}

	void resetLayout(VirtualContainer c, LayoutManager layout) {
		/*
		 * Vector<Component> originalComponents = new Vector(); for (int i = 0;
		 * i < c.getComponentCount(); i ++) {
		 * originalComponents.add(c.getComponent(i)); } c.removeAll();
		 */
		c.setLayout(layout);
		/*
		 * for (int i = 0; i < originalComponents.size(); i++) {
		 * c.add(originalComponents.elementAt(i)); }
		 */

	}

	void setLayout(ObjectAdapter adapter, VirtualContainer c, int numRows,
			int numCols) {

		setLayout(adapter, c, numRows, numCols, adapter.getHorizontalBoundGap(),
				adapter.getVerticalGap());

	}

	void setLayoutUnbound(ObjectAdapter adapter, VirtualContainer c,
			int numRows, int numCols) {

		setLayoutUnbound(adapter, c, numRows, numCols,
				adapter.getHorizontalGap(), adapter.getVerticalGap());

	}

	int getRows(LayoutManager lm) {
		if (lm instanceof OEGridLayout) {
			return ((OEGridLayout) lm).getRows();
		} else if (lm instanceof GridLayout) {
			return ((GridLayout) lm).getRows();
		} else
			return -1;
	}

	int getColumns(LayoutManager lm) {
		if (lm instanceof OEGridLayout) {
			return ((OEGridLayout) lm).getColumns();
		} else if (lm instanceof GridLayout) {
			return ((GridLayout) lm).getColumns();
		} else
			return -1;
	}

	void setColumns(LayoutManager lm, int newVal) {
		if (lm instanceof OEGridLayout) {
			((OEGridLayout) lm).setColumns(newVal);
		} else if (lm instanceof GridLayout) {
			((GridLayout) lm).setColumns(newVal);
		}
	}

	void setRows(LayoutManager lm, int newVal) {
		if (lm instanceof OEGridLayout) {
			((OEGridLayout) lm).setRows(newVal);
		} else if (lm instanceof GridLayout) {
			((GridLayout) lm).setRows(newVal);
		}
	}

	public boolean processDirectionBean(String direction) {
		// Component c = getUIComponent();
		VirtualComponent c = unboundPropertiesPanel;
		if (c == null)
			return true;

		try {
			VirtualContainer cn = (VirtualContainer) c;
			int count = cn.getComponentCount();
			LayoutManager lm = (LayoutManager) cn.getLayout();
			// uiGridLayout lm = (uiGridLayout) cn.getLayout();
			if (AttributeNames.HORIZONTAL.equals(direction)) {
				// if (lm.getRows() == 1)
				if (getRows(lm) == 1)
					return true;
				else
					// cn.setLayout(new uiGridLayout(1, count));
					// cn.setLayout(new uiGridLayout(1, count,
					// uiGridLayout.DEFAULT_HGAP, 0));
					resetLayout(cn, 1, count, getObjectAdapter().getHorizontalGap(), 0);
			// this is wrong, should be VERTICAL
			} else if (AttributeNames.HORIZONTAL.equals(direction )) {
				if (lm == null)
					return false;
				// if(lm.getColumns() == 1)
				if (getColumns(lm) == 1)
					return true;
				else
					// cn.setLayout(new uiGridLayout(count, 1));

					// cn.setLayout(new uiGridLayout(count, 1, 0,
					// uiGridLayout.DEFAULT_VGAP));
					// cn.setLayout(new GridLayout(count, 1));
					resetLayout(cn, count, 1, getObjectAdapter().getHorizontalGap(), 0);
			}
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	public boolean processDirectionVector(String direction) {
		// Container awtComponent = (Container)
		// getWidgetAdapter().getUIComponent();
		VirtualContainer awtComponent = unboundPropertiesPanel;
		try {
			LayoutManager l = (LayoutManager) awtComponent.getLayout();
			// if (!(l instanceof uiGridLayout)) return false;
			// uiGridLayout lm = (uiGridLayout)(awtComponent.getLayout());
			// Check to see if direction is horizontal or
			// vertical.
			if (direction == null) {
				// direction = AttributeNames.HORIZONTAL;
				// direction = (String)
				// getObjectAdapter().getTempAttributeValue(AttributeNames.DIRECTION);
				direction = adapter.getDirection();
			}
			if (direction.equals(AttributeNames.HORIZONTAL))
				// lm.setColumns(lm.getColumns()+1);
				setColumns(l, (getColumns(l) + 1));
			else if (direction.equals(AttributeNames.VERTICAL))
				// lm.setRows(lm.getRows()+1);
				setRows(l, (getRows(l) + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		awtComponent.doLayout();
		return false;
	}

	public boolean processDirectionHashtable(String direction) {
		return true;
	}

	public boolean processDirection(String direction) {
		if (unboundPropertiesPanel == null)
			return false;
		CompositeAdapter objectAdapter = (CompositeAdapter) getObjectAdapter();
		
		if (objectAdapter instanceof VectorAdapter)
			return processDirectionVector(direction);
		else if (objectAdapter instanceof HashtableAdapter)
			return processDirectionHashtable(direction);
		if (objectAdapter instanceof ClassAdapter)
			return processDirectionBean(direction);
		return true;
	}

	public void processDirection() {
		if (unboundPropertiesPanel != null)
			processDirectionAddLabelsForUnbound();
		else
			processDirectionSinglePanel();
	}

	/*
	 * public void processDirectionHashtable () { Container containW =
	 * unboundPropertiesPanel; uiContainerAdapter objectAdapter =
	 * (uiContainerAdapter) getObjectAdapter(); String direction = (String)
	 * objectAdapter.getTempAttributeValue(AttributeNames.DIRECTION); boolean
	 * alignHorizontal = direction.equals(AttributeNames.HORIZONTAL); boolean
	 * foundUnlabeledComposite = objectAdapter.foundUnlabeledComposite();
	 * 
	 * if ( alignHorizontal) containW.setLayout(new uiGridLayout(2, hSize +
	 * numFeatures, uiGridLayout.DEFAULT_HGAP, 0)); else
	 * //containW.setLayout(new uiGridLayout(features.length, 1));
	 * 
	 * //if (foundUnlabeledComposite && v.size() > 1) if
	 * (foundUnlabeledComposite && hSize > 1) //containW.setLayout(new
	 * uiGridLayout(v.size(), 1, 0, uiGridLayout.DEFAULT_VGAP));
	 * containW.setLayout(new uiGridLayout(hSize + numFeatures, 2, 0,
	 * uiGridLayout.DEFAULT_VGAP)); else //containW.setLayout(new
	 * uiGridLayout(v.size(), 1)); containW.setLayout(new uiGridLayout(hSize +
	 * numFeatures, 2)); }
	 */

	// this.getWidgetAdapter().childComponentsAdded();
	// }
	// for now this is the same as above
	int getBoxNumColumns (int numFeatures) {
		Integer numCols = getObjectAdapter().getNumColumns();
		if (numCols == null)
			return 4;
		else
			return numCols;
		
	}
	int getBoxNumRows (int numFeatures, int numColumns) {
		Integer numRows = getObjectAdapter().getNumRows();
		if (numRows  == null)
			return numFeatures/numColumns;
		else return numRows;
		
	}
	static final int  BOX_HGAP = 30;
	public void processDirectionAddLabelsForUnbound() {
		CompositeAdapter objectAdapter = (CompositeAdapter) getObjectAdapter();
		boolean foundUnlabeledComposite = objectAdapter
				.foundUnlabeledComposite();
		// String direction = (String)
		// objectAdapter.getTempAttributeValue(AttributeNames.DIRECTION);
		String direction = objectAdapter.getDirection();
		boolean alignHorizontal = direction.equals(AttributeNames.HORIZONTAL);
		boolean square = direction.equals(AttributeNames.SQUARE);
		boolean box = direction.equals(AttributeNames.BOX);
		if (alignHorizontal) {
			fillColumnLabels(sortedPropertiesList, true, true);
			fillColumnLabels(unSortedPropertiesList, true, true);
		} else {
			// maybe add labels here,
			// check the status of parent. whether horizontal stuff is needed
			fillRowLabels(sortedPropertiesList, true, true);
			fillRowLabels(unSortedPropertiesList, true, true);
		}
		// Container containW = (Container) getWidgetAdapter().getUIComponent();
		VirtualContainer containW = unboundPropertiesPanel;
		// int numFeatures = unboundProperties.size();
		int numFeatures = unSortedPropertiesList.size()
				+ sortedPropertiesList.size();
		// this.getWidgetAdapter().childComponentsAdded();
		if (numFeatures == 0)
			return;

		if (alignHorizontal) {

			// containW.setLayout(new uiGridLayout(1, unboundProperties.size(),
			// uiGridLayout.DEFAULT_HGAP, 0));
			if (getObjectAdapter().isTopDisplayedAdapter())
				setLayoutUnboundRow(getObjectAdapter(), containW, numFeatures,
						getObjectAdapter().getHorizontalGap());
			else
				// setLayoutUnboundRow(getObjectAdapter().getParentAdapter(),
				// containW, numFeatures,uiGridLayout.DEFAULT_HGAP,
				// uiGridLayout.DEFAULT_VGAP );
				setLayoutUnboundRow(getObjectAdapter(), containW, numFeatures,
						getObjectAdapter().getHorizontalGap());
			// containW.setLayout(new uiGridLayout(1, numFeatures,
			// uiGridLayout.DEFAULT_HGAP, 0));
			/*
			 * if (containW.getLayout() != null) { System.out.println("layout:"
			 * + containW.getLayout().getClass());
			 * System.out.println("containW:" + containW.getClass()); }
			 */
			// objectAdapter.setTempAttributeValue(AttributeNames.DIRECTION,
			// AttributeNames.HORIZONTAL);
			objectAdapter.setDirection(AttributeNames.HORIZONTAL);
		} else
		// fisayo changed the below to make the ui more presentable....support
		// the square value for the direction
		if (square) {
			// int feature_count = features.length;
			// containW.setLayout(new
			// uiGridLayout((int)Math.ceil(Math.sqrt(feature_count)),
			// (int)Math.ceil(Math.sqrt(feature_count))));
			// containW.setLayout(new
			// uiGridLayout((int)Math.ceil(Math.sqrt(numFeatures)),
			// (int)Math.ceil(Math.sqrt(numFeatures))));
			setLayoutUnbound(objectAdapter, containW, (int) Math.ceil(Math
					.sqrt(numFeatures)), (int) Math
					.ceil(Math.sqrt(numFeatures)));
			

		}

		else
		// fisayo changed the below to make the ui more presentable....support
		// the square value for the direction
		if (box) {
			
//			setLayoutUnbound(objectAdapter, containW, (int) Math
//					.ceil(numFeatures / 2), (int) Math.ceil(numFeatures / 2));
			int numColumns = getBoxNumColumns(numFeatures);
			int numRows = getBoxNumRows(numFeatures, numColumns);
			//setLayoutUnbound(objectAdapter, containW, numRows, numColumns, BOX_HGAP, getObjectAdapter().getVerticalGap());
			setLayoutUnbound(objectAdapter, containW, numRows, numColumns, getObjectAdapter().getHorizontalGap(), getObjectAdapter().getVerticalGap());
			//			setLayoutUnbound(objectAdapter, containW, (int) Math
//					.ceil(numFeatures / 2), (int) Math.ceil(numFeatures / 2));

		}

		else {
			int numRows = numFeatures;
			// moving this here, otherwise nunUiRows gets added twice
			int numUIRows = numRows;
			// seem to be incrementing numUIRows twice
			if (unboundColumnTitlePanelNotToBeAdded)
				numRows++;
			// containW.setLayout(new uiGridLayout(features.length, 1));
			// if (foundUnlabeledComposite && features.length > 1)
			// if (foundUnlabeledComposite && numFeatures > 1)
			// moving this up
			//int numUIRows = numRows;
			if (getObjectAdapter().isFlatTable())
				numUIRows++;
			if (foundUnlabeledComposite && numRows > 1)
				// containW.setLayout(new uiGridLayout(features.length, 1, 0,
				// getObjectAdapter().getVerticalGap()));
				// containW.setLayout(new uiGridLayout(numFeatures, 1, 0,
				// uiGridLayout.DEFAULT_VGAP));
				// setLayout(objectAdapter, containW, numFeatures, 1, 0,
				setLayoutUnbound(getObjectAdapter(), containW, numUIRows, 1, 0,
						getObjectAdapter().getVerticalGap());
			else
				// containW.setLayout(new uiGridLayout(features.length, 1));
				// containW.setLayout(new uiGridLayout(numFeatures, 1));
				// containW.setLayout(new GridLayout (numFeatures, 1));
				// setLayout(objectAdapter, containW, numFeatures, 1);
				setLayoutUnbound(getObjectAdapter(), containW, numUIRows, 1);
		}
		// dunno if we need both of the ones below
		/*
		 * if (objectAdapter.getClass().equals(uiClassAdapter.class))
		 * ((uiContainerAdapter) objectAdapter.getParent()).makeColumnTitles();
		 */
		objectAdapter.makeColumnTitles();

		// getWidgetAdapter().childComponentsAdded();
	}

	// this.getWidgetAdapter().childComponentsAdded();

	// }

	/*
	 * void processCommand (Command c) {
	 * 
	 * int row = getRow (c); int col = getCol (c); if (row < 0 && col < 0) {
	 * //numUnboundComponents ++; unboundCommands.addElement(c); } else if (row
	 * < 0 || col < 0) { partiallyBoundCommands.addElement(c); } else {
	 * 
	 * //maxRow = Math.max( maxRow, getRow (compAdapter)); //maxRow = Math.max(
	 * maxRow, getRow (compAdapter));
	 * //System.out.println(compAdapter.getPropertyName() + "Col:" +
	 * compAdapter.getMergedAttributeValue(AttributeNames.COLUMN)); //maxCol =
	 * Math.max(maxCol, getCol (compAdapter)); //childComponents.set(row, col,
	 * c); setChild (row, col, c); } }
	 */
	boolean manualAdds = false;

	public void manualAdd(ButtonCommand c) {
		if (!manualAdds)
			return;
		int pos = c.getPosition();
		if (pos == -1) {
			if (getObjectAdapter().getShowUnboundButtons())
				unSortedCommandsList.add(c);
		} else
			sortedCommandsList.setOrInsertNewElementBelow(c.getPosition(), c);
		/*
		 * Object constraint = c.getAddConstraint(); Component comp =
		 * c.getButton(); if (incrementalAdds) { if (pos != -1 && constraint !=
		 * null) componentPanel.add(comp, constraint, pos); else if (pos == -1
		 * && constraint == null) componentPanel.add(comp); else if (pos == -1)
		 * componentPanel.add(comp, constraint); else componentPanel.add(comp,
		 * pos); return; }
		 */
	}

	public void addUnsortedCommands(VirtualContainer componentPanel) {
		for (int i = 0; i < unSortedCommandsList.size(); i++) {
			ButtonCommand c = (ButtonCommand) unSortedCommandsList.get(i);
			if (c == null)
				continue;
			Object constraint = c.getAddConstraint();
			VirtualComponent comp = c.getButton();
			if (constraint == null)
				componentPanel.add(comp);
			else
				componentPanel.add(comp, constraint);
		}

	}

	public void addSortedCommands(VirtualContainer componentPanel) {
		for (int i = 0; i < sortedCommandsList.size(); i++) {
			ButtonCommand c = (ButtonCommand) sortedCommandsList.get(i);
			if (c == null)
				continue;
			int pos = Math.min(c.getPosition(), componentPanel
					.getComponentCount());
			Object constraint = c.getAddConstraint();
			VirtualComponent comp = c.getButton();

			if (constraint == null)
				componentPanel.add(comp, pos);
			else
				componentPanel.add(comp, constraint, pos);
		}

	}

	public void addUnsortedProperties(VirtualContainer componentPanel) {
		for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = (ObjectAdapter) unSortedPropertiesList
					.get(i);
			if (compAdapter == null)
				continue;
			Object constraint = compAdapter.getAddConstraint();
			VirtualComponent comp = getComponent(compAdapter);
			if (constraint == null)
				componentPanel.add(comp);
			else
				componentPanel.add(comp, constraint);
		}

	}

	public void addSortedProperties(VirtualContainer componentPanel) {
		for (int i = 0; i < sortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = (ObjectAdapter) sortedPropertiesList
					.get(i);
			if (compAdapter == null)
				continue;
			int pos = Math.min(compAdapter.getPosition(), componentPanel
					.getComponentCount());
			Object constraint = compAdapter.getAddConstraint();
			VirtualComponent comp = getComponent(compAdapter);

			if (constraint == null)
				componentPanel.add(comp, pos);
			else
				componentPanel.add(comp, constraint, pos);
		}

	}

	/*
	 * boolean noInsert = true; boolean singleInsert = false; boolean
	 * multipleInsert = false;
	 */
	enum InsertionStatus {
		NONE, SINGLE, MULITIPLE
	};

	InsertionStatus insertionStatus = InsertionStatus.NONE;

	ObjectAdapter insertedAdapter;

	// VirtualComponent insertedComponent;
	int insertedPosition;

	boolean added = false;
	boolean empty = true;

	boolean childComponentsOverriden() {
		return childComponentsOverriden;
	}

	void processProperty(ObjectAdapter compAdapter) {
		/*
		 * // forget about position, asume that just sets the row or column
		 * //maybe we should not! //unSortedPropertiesList.add(compAdapter);
		 */
		int pos = compAdapter.getPosition();
	     pos = -1; // just forcing, which seems to give  better semantics
		if (!childComponentsOverriden()) {
			// if (manualAdds) {
			if (pos < 0) {
				int index = compAdapter.getIndex();
				if (index < 0 || hasDescendentRows)
					unSortedPropertiesList.add(compAdapter);
				else {
					unSortedPropertiesList.setOrInsertNewElementAbove(index,
							compAdapter);
					insertUnsortedPropertiesFrom = Math.min(
							insertUnsortedPropertiesFrom, index);
				}
			} else {
				
				sortedPropertiesList.setOrInsertNewElementAbove(pos,
						compAdapter);
				insertSortedPropertiesFrom = Math.min(
						insertSortedPropertiesFrom, pos);

			}
		} else {
			if (pos < 0) {
				int lastIndex = childComponents.numRows();
				childComponents.set(lastIndex, 0, compAdapter);
			} else {
				childComponents.set(pos, 0, compAdapter);
			}
		}
	}
		
		void processCommand(ButtonCommand command) {
			/*
			 * // forget about position, asume that just sets the row or column
			 * //maybe we should not! //unSortedPropertiesList.add(compAdapter);
			 */
			//int pos = compAdapter.getPosition();
			//Integer pos = (Integer) AttributeManager.getInheritedAttributeValue(command.getTargetObject(), command.getMethodDescriptor(), AttributeNames.POSITION, null);
			Integer pos = command.getPosition();

			// pos = -1; // just forcing for now
			if (pos != null && pos != -1)
				sortedCommandsList.setOrInsertNewElementBelow(pos, command);
			else
				unSortedCommandsList.add(command);
			

	}

	@Override
	public void add(ObjectAdapter compAdapter) {
		CompositeAdapter parentAdapter = getObjectAdapter()
				.getParentAdapter();
		add((VirtualContainer) parentAdapter.getUIComponent(),
				getComponent(compAdapter), compAdapter);
	}

	boolean hasDescendentRows = false;

	public boolean hasDescendentRows() {
		return hasDescendentRows;
	}

	public static boolean hasDescendentRows(ObjectAdapter tableAdapter) {
		CommandAndStatePanelAdapter widgetAdapter = (CommandAndStatePanelAdapter) tableAdapter
				.getWidgetAdapter();
		return widgetAdapter.hasDescendentRows();
	}

	public void add(VirtualContainer parent, VirtualComponent comp,
			ObjectAdapter compAdapter) {

		CompositeAdapter adapter = (CompositeAdapter) getObjectAdapter();
		// add directly to ancestor
		// not a good idea, will not support elide
		/*
		 * uiContainerAdapter topRow = adapter.getTopFlatTableRow(); if
		 * (!adapter.isFlatTableCell() && compAdapter.isFlatTableCell() &&
		 * adapter != topRow ) { topRow.getWidgetAdapter().add(compAdapter);
		 * 
		 * }
		 */
		if (adapter.hasFlatTableRowDescendent() && adapter.isFlatTableRow()) {
			adapter.getParentAdapter().getWidgetAdapter().add(compAdapter);
			return;
		}

		if (adapter.isFlatTableRow()) {
			added = true; // will be added much later
			return;
		}

		if (compAdapter.isFlatTableRow()
				&& compAdapter.hasFlatTableRowDescendent()) {
			return;
		}

		/*
		 * if (compAdapter.isFlatTableRow() && compAdapter.getParentAdapter() !=
		 * adapter) { hasDescendentRows = true; }
		 */

		// this will not be needed later
		if (adapter.isFlatTableComponent() && !adapter.isFlatTableCell())
			return; // will be processed by top row.

		/*
		 * 
		 * //if (adapter.flattenTable()) { if (!adapter.isFlatTableCell() &&
		 * (adapter.isFlatTableComponent() || adapter.isFlatTableRow())){
		 * 
		 * //if (adapter.getTopRowWithPrimitivesAndComposites()) if
		 * (adapter.isFlatTableRow()) added = true;
		 * 
		 * return; }
		 */

		if (comp.getParent() != null && comp.isVisible())
			return;
		// if ( compAdapter.getParentAdapter() instanceof uiRootAdapter) {
		if (compAdapter.getParentAdapter() instanceof RootAdapter) {
			super.add(parent, comp, compAdapter);
			return;
		}
		if (compAdapter.isFlatTableRow()
				&& compAdapter.getParentAdapter() != adapter) {
			hasDescendentRows = true;
			/*
			 * uiWidgetAdapterInterface childWidgetAdapter =
			 * compAdapter.getWidgetAdapter(); if (childWidgetAdapter != null &&
			 * childWidgetAdapter instanceof CommandAndStatePanelAdapter)
			 * 
			 * setChildNumLabelledNodes(((CommandAndStatePanelAdapter)
			 * childWidgetAdapter).getNumLabelledNodes());
			 */
		}
		if (compAdapter.isFlatTableRow()) {
			WidgetAdapterInterface childWidgetAdapter = compAdapter
					.getWidgetAdapter();
			if (childWidgetAdapter != null
					&& childWidgetAdapter instanceof CommandAndStatePanelAdapter)

				setChildNumLabelledNodes(((CommandAndStatePanelAdapter) childWidgetAdapter)
						.getNumLabelledNodes());
		}

		if (replaceParent != null) { // we are replacing removed adapter with
			// this one.
			constrainedAdd(replaceParent, compAdapter, compAdapter.getGenericWidget().getContainer());
//			replaceParent.add(compAdapter.getGenericWidget().getContainer(),
//					replacePos);
//			compAdapter.getWidgetAdapter().setParentContainer(replaceParent);
			replaceParent = null;
			return;
		}

		// int pos = compAdapter.getPosition();
		Object constraint = compAdapter.getAddConstraint();

		added = true;
		empty = false;

		maybeProcessCommandOrAdapter(compAdapter);

	}

	public void linkUIComponentToMe(VirtualComponent c) {
		super.linkUIComponentToMe(c);
		setAttributes(c);

	}

	public void remove(VirtualContainer parent, VirtualComponent comp,
			ObjectAdapter compAdapter) {
		compAdapter.getParentContainer().remove(comp);
	}

	void removeAll(VirtualContainer c) {
		if (c == null)
			return;
		VirtualComponent[] components = c.getComponents();
		for (int i = 0; i < components.length; i++) {
			// components[i].setVisible(false);
			if (components[i] instanceof VirtualContainer)
				removeAll((VirtualContainer) components[i]);
			c.remove(components[i]);
		}
		// c.removeAll();
	}

	void removeAllChildren(VirtualContainer c) {
		VirtualComponent[] components = c.getComponents();
		for (int i = 0; i < components.length; i++) {
			// components[i].setVisible(false);
			/*
			 * if (components[i] instanceof VirtualContainer)
			 * removeAll((VirtualContainer) components[i]);
			 */
			c.remove(components[i]);
		}
		// c.removeAll();
	}

	void removeFromParent(VirtualComponent c) {
		if (c == null)
			return;
		if (c.getParent() == null)
			return;
		c.getParent().remove(c);
	}

	void removeComponentFromParent(ObjectAdapter o) {
		VirtualComponent c = getComponent((ObjectAdapter) o);
		if (c == null)
			return;
		if (c.getParent() == null)
			return;
		c.getParent().remove(c);
	}
	void removeComponentFromParent(ButtonCommand o) {
		VirtualComponent c = o.getButton();
		if (c == null)
			return;
		if (c.getParent() == null)
			return;
		c.getParent().remove(c);
	}

	void removeAllComponents(DynamicMatrix matrix) {
		for (int i = 0; i < matrix.numRows(); i++) {
			for (int j = 0; j < matrix.numCols(i); j++) {
				Object o = matrix.get(i, j);
				if (o instanceof ObjectAdapter) {
					removeComponentFromParent((ObjectAdapter) o);
					/*
					 * VirtualComponent c = getComponent((uiObjectAdapter) o);
					 * removeFromParent(c);
					 */
				} else if (o instanceof ButtonCommand) {
					removeComponentFromParent((ButtonCommand) o);
				}
			}
		}

	}

	void removeAllComponents(ADynamicSparseList list) {
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof ObjectAdapter) {
				removeComponentFromParent((ObjectAdapter) o);
				/*
				 * VirtualComponent c = getComponent((uiObjectAdapter) o);
				 * removeFromParent(c);
				 */
			}
			else if (o instanceof ButtonCommand) {
				removeComponentFromParent((ButtonCommand) o);
			}
		}

	}
	//void removeComponents(ADynamicSparseList list) {
	void removeComponents(List list) {
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof ObjectAdapter) {
				removeComponentFromParent((ObjectAdapter) o);
				/*
				 * VirtualComponent c = getComponent((uiObjectAdapter) o);
				 * removeFromParent(c);
				 */
			} else if (o instanceof ButtonCommand) {
				removeComponentFromParent((ButtonCommand) o);
			}
		}

	}

	void removeAllDynamicComponents(ADynamicSparseList list) {
	//void removeAllDynamicComponents(List list) {
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof ObjectAdapter
					&& ((ObjectAdapter) o).isDynamicChild()) {
				VirtualComponent c = getComponent((ObjectAdapter) o);
				removeFromParent(c);
			}
		}

	}

	void removeAllBoundComponents() {
		removeAllComponents(rowItems);
		removeAllComponents(columnItems);
		removeAllComponents(childComponents);

	};

	void removeAllUnBoundComponents() {
		removeAllComponents(sortedPropertiesList);
		removeAllComponents(unSortedPropertiesList);

	};

	boolean removeAllDynamicComponents() {
		boolean retVal = sortedPropertiesList.size() > 0
				|| unSortedPropertiesList.size() > 0;
		removeAllDynamicComponents(sortedPropertiesList);
		removeAllDynamicComponents(unSortedPropertiesList);
		return retVal;
	};

	void removeAllInvisible(VirtualContainer c) {
		VirtualComponent[] components = c.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i].isVisible())
				continue;
			c.remove(components[i]);
			// components[i].setVisible(false);
		}
	}
	// this is intended to clear all propertyies but it clears everything so the whole thing can be regenerated
	// I guess the resson is that properties and non properties may be mixed together. 
	// In practice only the unbound properties panel should be recreated I believe
	public void removeAllProperties(VirtualContainer widget) { // assuming only
		// unbound
		// dynamic
		clearAll(); // properties
		/*
		 * // exist
		 * 
		 * if (unboundPropertiesPanel != null) //
		 * unboundPropertiesPanel.removeAll();
		 * removeAll(unboundPropertiesPanel); else
		 * removeUnboundFromComponentPanel();
		 * unboundProperties.removeAllElements();
		 * sortedPropertiesList.removeAllElements();
		 * unSortedPropertiesList.removeAllElements(); numSortedPropertiesAdded
		 * = 0; numUnboundPropertiesAdded = 0; numUnsortedPropertiesAdded = 0;
		 * added = false; childComponentsOverriden = false; overrideComputed =
		 * false;
		 */

	}

	// not called by anyone
	void redisplay() { // assuming only
		// unbound
		// dynamic
		// properties
		// exist

		if (unboundPropertiesPanel != null)
			// unboundPropertiesPanel.removeAll();
			removeAll(unboundPropertiesPanel);
		else
			removeUnboundFromComponentPanel();
		if (boundComponentsPanel != null)
			removeAll(boundComponentsPanel);

		insertSortedPropertiesFrom = 0;
		numUnboundPropertiesAdded = 0;
		insertUnsortedPropertiesFrom = 0;
		added = false;
		empty = true;
		childComponentsOverriden = false;
		overrideComputed = false;
		boundComponentsAdded = false;
		unboundColumnTitlePanelNotToBeAdded = false;
		unboundColumnTitlePanelFilled = false;

	}

	void clearDynamicComponents() { // assuming only
		// unbound
		// dynamic
		// properties
		// exist
		removeAllDynamicComponents();
		/*
		 * if (unboundPropertiesPanel != null) //
		 * unboundPropertiesPanel.removeAll();
		 * removeAll(unboundPropertiesPanel); else
		 * removeUnboundFromComponentPanel();
		 */
		/*
		 * if (boundComponentsPanel != null) removeAll(boundComponentsPanel);
		 */
		/*
		 * unboundProperties.removeAllElements();
		 * sortedPropertiesList.removeAllElements();
		 * unSortedPropertiesList.removeAllElements();
		 */
		insertSortedPropertiesFrom = 0;
		numUnboundPropertiesAdded = 0;
		insertUnsortedPropertiesFrom = 0;
		// should find out if there were
		added = true; // this seems wrong, but is necessary
		// empty = false;
		childComponentsOverriden = false;
		overrideComputed = false;
		// boundComponentsAdded = false;
		unboundColumnTitlePanelNotToBeAdded = false;
		unboundColumnTitlePanelFilled = false;
		// buildPanel();

	}

	void redisplayUnbound() { // assuming only
		clearDynamicComponents();
		/*
		 * // unbound // dynamic // properties // exist
		 * removeAllDynamicComponents();
		 * 
		 * numSortedPropertiesAdded = 0; numUnboundPropertiesAdded = 0;
		 * numUnsortedPropertiesAdded = 0; added = true;
		 * childComponentsOverriden = false; overrideComputed = false;
		 * //boundComponentsAdded = false; unboundColumnTitlePanelFilled =
		 * false;
		 */
		buildPanel();

	}

	public void childComponentsAdditionStarted() {
		// if (!getIncrementalChildAddition())
		clearAll();
		initCommands(getObjectAdapter());
		/*
		 * removeAllBoundComponents(); removeAllUnBoundComponents(); rowItems =
		 * new ADynamicSparseList(); columnItems = new ADynamicSparseList();
		 * sortedPropertiesList = new ADynamicSparseList();
		 * numSortedPropertiesAdded = 0; unSortedPropertiesList = new
		 * ADynamicSparseList(); numUnsortedProperthiesAdded = 0; //
		 * sortedCommandsList = new ADynamicSparseList(); //
		 * unSortedCommandsList = new ADynamicSparseList(); unboundProperties =
		 * new Vector(); // Vector unboundButtons = new Vector(); // Vector
		 * propertyComponents = new Vector(); // commands // Vector commands; //
		 * Vector remaining // Vector buttons = new Vector(); // Component[][]
		 * childComponents; // 2D array initialized later childComponents = new
		 * ADynamicMatrix();
		 * 
		 * initialized = false; added = false;
		 * 
		 * if (unboundButtonsPanel != null) { removeAll(unboundButtonsPanel);
		 * removeFromParent(unboundButtonsPanel); //unboundButtonsPanel = null;
		 * // would like to reuse it //unboundButtonsPanel.removeAll(); } if
		 * (unboundPropertiesPanel != null) { //
		 * unboundPropertiesPanel.removeAll();
		 * removeAllChildren(unboundPropertiesPanel);
		 * removeFromParent(unboundPropertiesPanel); //unboundPropertiesPanel =
		 * null;
		 * 
		 * } if (boundComponentsPanel != null && boundComponentsPanel !=
		 * componentPanel) { // boundComponentsPanel.removeAll();
		 * removeAllChildren(boundComponentsPanel);
		 * removeFromParent(boundComponentsPanel); boundComponentsPanel = null;
		 * } if (componentPanel != null) {
		 * componentPanel.setLayout(initialLayout);
		 * removeAllChildren(componentPanel); }
		 * 
		 * 
		 * //removeAll(componentPanel);
		 * 
		 * boundComponentsAdded = false; unboundColumnTitlePanelFilled = false;
		 */

	}

	public void removePossiblyChildPanelOfComponentPanel(VirtualContainer c) {
		if (c == null || c == componentPanel)
			return;

	}

	public boolean isEmpty() {
		return empty;
	}
	public void clearAncestors() {
		Set<CommandAndStatePanelAdapter> ancestors = ancestorState.keySet();
		for (CommandAndStatePanelAdapter ancestor: ancestors) {
			ancestor.clear(ancestorState.get(ancestor));
		}
	}
	public void clear(List attributedList) {
		removeComponents(attributedList);
		removeColumnnItems(attributedList);
		removeRowItems(attributedList);
		removeChildComponents(attributedList);
		removeSortedProperties(attributedList);
		removeUnSortedProperties(attributedList);
		removeSortedCommandList(attributedList);
		removeUnSortedCommandList(attributedList);
		// what about soreted and unsroted buttons?
		
	}
	public void removeColumnnItems(List attributedList) {
		for (Object component:attributedList) {
			columnItems.remove(component);
		}		
	}
	public void removeRowItems(List attributedList) {
		for (Object component:attributedList) {
			rowItems.remove(component);
		}		
	}
	public void removeChildComponents(List attributedList) {
		for (Object component:attributedList) {
			childComponents.remove(component);
		}		
	}
	public void removeSortedProperties(List attributedList) {
		for (Object component:attributedList) {
			if (component instanceof ObjectAdapter)
			sortedPropertiesList.remove((ObjectAdapter) component);
		}		
	}
	public void removeUnSortedProperties(List attributedList) {
		for (Object component:attributedList) {
			if (component instanceof ObjectAdapter)
			unSortedPropertiesList.remove((ObjectAdapter) component);
		}		
	}
	public void removeSortedCommandList(List attributedList) {
		for (Object component:attributedList) {
			if (component instanceof ButtonCommand)
			sortedCommandsList.remove((ButtonCommand) component);
		}		
	}
	public void removeUnSortedCommandList(List attributedList) {
		for (Object component:attributedList) {
			if (component instanceof ButtonCommand)
			unSortedCommandsList.remove((ButtonCommand) component);
		}		
	}

	public void clearAll() {
		// ancestor does not redraw so two wrongs make one right
		//clearAncestors();
		removeAllBoundComponents();
		removeAllUnBoundComponents();
		rowItems = new ADynamicSparseList();
		columnItems = new ADynamicSparseList();
		sortedPropertiesList = new ADynamicSparseList();
		insertSortedPropertiesFrom = 0;
		unSortedPropertiesList = new ADynamicSparseList();
		insertUnsortedPropertiesFrom = 0;
		// sortedCommandsList = new ADynamicSparseList();
		// unSortedCommandsList = new ADynamicSparseList();
		
		
		
		// commenting this out because I dont think this is used at all
		unboundProperties = new Vector();
		
		
		// Vector unboundButtons = new Vector();
		// Vector propertyComponents = new Vector();
		// commands
		// Vector commands;
		// Vector remaining
		// Vector buttons = new Vector();
		// Component[][] childComponents; // 2D array initialized later
		childComponents = new ADynamicMatrix();

		initialized = false;
		added = false;
		empty = true;
		removePossiblyChildPanelOfComponentPanel(unboundButtonsPanel);
		removePossiblyChildPanelOfComponentPanel(unboundPropertiesPanel);
		removePossiblyChildPanelOfComponentPanel(boundComponentsPanel);
		/*
		 * if (unboundButtonsPanel != null && unboundButtonsPanel !=
		 * componentPanel) { removeAllChildren(unboundButtonsPanel);
		 * removeFromParent(unboundButtonsPanel); //unboundButtonsPanel = null;
		 * // would like to reuse it //unboundButtonsPanel.removeAll(); } if
		 * (unboundPropertiesPanel != null && unboundPropertiesPanel !=
		 * componentPanel) { // unboundPropertiesPanel.removeAll();
		 * removeAllChildren(unboundPropertiesPanel);
		 * removeFromParent(unboundPropertiesPanel); //unboundPropertiesPanel =
		 * null;
		 * 
		 * } if (boundComponentsPanel != null && boundComponentsPanel !=
		 * componentPanel) { // boundComponentsPanel.removeAll();
		 * removeAllChildren(boundComponentsPanel);
		 * removeFromParent(boundComponentsPanel); boundComponentsPanel = null;
		 * }
		 */
		if (componentPanel != null) {
			componentPanel.setLayout(initialLayout);
			removeAllChildren(componentPanel);
			if (unboundPropertiesPanel != null)
				unboundPropertiesPanel = null;
			if (unboundButtonsPanel != null)
				unboundButtonsPanel = null;
		
		}
		/*
		 * if (componentPanel != null)
		 * 
		 * // componentPanel.removeAll(); removeAllChildren(componentPanel);
		 */

		// removeAll(componentPanel);
		commands = null;
		boundComponentsAdded = false;
		unboundColumnTitlePanelNotToBeAdded = false;
		unboundColumnTitlePanelFilled = false;
		// titlePanelFillingDeferred = false;
		titleRowAdapter = null;

	}

	public void removeFromContaningList(ObjectAdapter compAdapter) {
		if (unboundProperties.contains(compAdapter))
			// unboundProperties.remove(pos);
			unboundProperties.remove(compAdapter);

		if (unSortedPropertiesList.contains(compAdapter))
			// unSortedPropertiesList.remove(pos);
			unSortedPropertiesList.remove(compAdapter);
		if (sortedPropertiesList.contains(compAdapter))
			// sortedPropertiesList.remove(pos);
			sortedPropertiesList.remove(compAdapter);
		insertSortedPropertiesFrom = sortedPropertiesList.size();
		numUnboundPropertiesAdded = unboundProperties.size();
		// tin case of deletion it will be size and in case of redislay it
		// should be 0
		insertUnsortedPropertiesFrom = Math.min(insertUnsortedPropertiesFrom,
				unSortedPropertiesList.size());

		// insertUnsortedPropertiesFrom = unSortedPropertiesList.size();

	}

	public void remove(ObjectAdapter compAdapter) {
		removeComponentFromParent(compAdapter);
		removeFromContaningList(compAdapter);
		boolean isTitleChild = compAdapter.getParentAdapter().getTitleChild() == compAdapter;
		if (!childIsFlatTable && isTitleChild
				&& compAdapter.getDirection() == AttributeNames.HORIZONTAL
				&& compAdapter.getClass().equals(ClassAdapter.class))
			((CompositeAdapter) compAdapter.getParent()).makeColumnTitles();
		ObjectAdapter adapter = getObjectAdapter();
		if (adapter.hasFlatTableRowDescendent() && adapter.isFlatTableRow()) {
			adapter.getParentAdapter().getWidgetAdapter().remove(compAdapter);
			return;
		}

	}

	public void remove(VirtualContainer parent, int pos,
			ObjectAdapter compAdapter) {
		remove(compAdapter);
		/*
		 * // VirtualComponent c = componentPanel.getComponent(pos);
		 * removeComponentFromParent(compAdapter);
		 * 
		 * removeFromContaningList(compAdapter); if (compAdapter.getDirection()
		 * == AttributeNames.HORIZONTAL &&
		 * compAdapter.getClass().equals(uiClassAdapter.class))
		 * ((uiContainerAdapter) compAdapter.getParent()).makeColumnTitles();
		 */

	}

	VirtualContainer replaceParent;

	int replacePos;

	public void removeForReplacement(VirtualContainer parent, int pos,
			ObjectAdapter childAdapter) {
		if (childAdapter instanceof ShapeObjectAdapter) {
			childAdapter.getWidgetAdapter().removeFromParentUIContainer();
			return;
		}
		// this stuff should be in widget adapter also
		replaceParent = childAdapter.getWidgetAdapter().getParentContainer();
		if (replaceParent == null) replaceParent = parent; // hack till I understand why it is null
		VirtualComponent replaceComponent = childAdapter.getGenericWidget()
				.getContainer();
		replacePos = OEMisc.indexOf(replaceParent, replaceComponent);
		if (replacePos >= 0) {
		replaceParent.remove(replacePos);
		replaceParent.validate();
		} else
			Tracer.warning("why is replace pos < 0");

	}

	/*
	 * public boolean processDirection(String direction) {
	 * 
	 * return true; }
	 */
	boolean firstTime = true;
	boolean addedCommands;

	public void childComponentsAdded(boolean foundProperties) {
		/*
		 * commands = ComponentPanel.createCommandsWithButtons(this
		 * .getObjectAdapter().getUIFrame(), lastAssignedValue);
		 */
		// this if should never be taken
		if (commands == null) {
			// commands = ComponentPanel.createCommandsWithButtons(this
			// .getObjectAdapter().getUIFrame(),
			// getObjectAdapter().getRealObject(), getObjectAdapter());
			commands = ComponentPanel.createCommandsWithButtons(this
					.getObjectAdapter().getUIFrame(), getObjectAdapter()
					.computeAndMaybeSetViewObject(), getObjectAdapter());
			//boolean addedCommands = processCommands(commands);
			addedCommands = processCommands(commands, getObjectAdapter());
			if (!addedCommands && !foundProperties)
				return;
			// maybe we need variable to see if this is first time
			// if (firstTime && addedCommands && commands.size() > 0) {
			if (firstTime && commands.size() > 0) {
				added = true;
				empty = false;
			}
		}
		// maybeOverrideChildComponents();
		// if (this.getObjectAdapter() instanceof uiVectorAdapter)
		buildPanel();
		firstTime = false;

	}

	boolean childComponentsOverriden = false;

	boolean overrideComputed = false;

	void maybeOverrideChildComponents() {
		if (overrideComputed)
			return;
		if (childComponents.numRows() > 0)
			return;
		if (!getObjectAdapter().getDirection().equals(AttributeNames.VERTICAL))
			return;
		int index = 0;
		for (int i = 0; i < sortedPropertiesList.size(); i++) {
			childComponents.set(index, 0, sortedPropertiesList.get(i));
			index++;
		}
		for (int i = 0; i < rowItems.size(); i++) {
			childComponents.set(index, 0, rowItems.get(i));
			index++;
		}
		for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			childComponents.set(index, 0, unSortedPropertiesList.get(i));
			index++;
		}
		childComponentsOverriden = true;
		sortedPropertiesList.removeAllElements();
		unSortedPropertiesList.removeAllElements();
		rowItems.removeAllElements();
		overrideComputed = true;
	}

	int extraRows;

	int findLastCol(int row) {
		// int i;
		for (int i = maxCol; i >= 0; i--) {
			// if (childComponents[row][i] == null) continue;
			if (childComponents.get(row, i) == null)
				continue;
			else
				return i;

		}
		return -1;
	}

	void fillBound(VirtualContainer c) {

	}

	void fillUnbound() {

	}

	boolean needUnboundPropertiesPanel() {
		// return false;

		return getObjectAdapter().getSeparateUnbound()
				&& (unSortedPropertiesList.size() > 0 
						|| sortedPropertiesList.size() > 0
						);

	}

	boolean needUnboundButtonsPanel() {
		// return false;

		return getObjectAdapter().getShowUnboundButtons()
				&& getObjectAdapter().getSeparateUnbound()
				//&& unboundButtons.size() > 0;
				&& unSortedCommandsList.size() + sortedCommandsList.size() > 0;

	}

	VirtualContainer getUnboundPropertiesPanel() {
		if (unboundPropertiesPanel != null)
			return unboundPropertiesPanel;
		// if (firstUnboundRow > 0)
		if (childComponents.numRows() > 0 || columnItems.size() > 0
				|| rowItems.size() > 0 || needUnboundButtonsPanel()) {
			// return new JPanel(); {
			VirtualContainer retVal = PanelSelector.createPanel();
			retVal.setName("Unbound properties panel of " + getObjectAdapter());
			setColors(retVal);
			return retVal;
			//return PanelSelector.createPanel();
		} else
			return componentPanel;

	}

	VirtualContainer getUnboundButtonPanel(Integer numCols) {
		if (unboundButtonsPanel != null)
			return unboundButtonsPanel;
		int unboundSize = unSortedCommandsList.size() + sortedCommandsList.size();
		VirtualContainer retVal;
		// if (firstUnboundRow > 0)
		if (childComponents.numRows() > 0 || columnItems.size() > 0
				|| rowItems.size() > 0 || needUnboundPropertiesPanel()) {
			// return new JPanel();
			retVal = PanelSelector.createPanel();
			retVal.setName ("unbound buttons panel of " + getObjectAdapter());
			setColors(retVal);
		} else
			retVal = componentPanel;

		// unboundButtonsPanel = PanelSelector.createPanel();
		// unboundButtonsPanel.setLayout(new GridLayout(unboundCommands
//		if (unboundButtons.size() < numCols)
//			numCols = unboundButtons.size();
		int numRows;
		if (numCols == null) {
			numRows = 1;
			numCols = 0;
		} else {
			//if(unboundButtons.size() < numCols) {
			if(unboundSize < numCols) {
				//numCols = unboundButtons.size();
				numCols = unboundSize;
			}
			//numRows = unboundButtons.size()
			numRows = unboundSize
			/ numCols;
		}
			
			
		//if (unboundButtons.size() > 1) {
		if (unboundSize > 1) {
			if (getObjectAdapter().getStretchUnboundColumns())
				retVal.setLayout(new GridLayout(
						//unboundButtons.size() / numCols, numCols));
						numRows, numCols, 
						getObjectAdapter().getHorizontalBoundGap(),
						getObjectAdapter().getVerticalGap()));
			else
				retVal.setLayout(new OEGridLayout(
						//unboundButtons.size() / numCols, numCols));
					numRows, numCols, getObjectAdapter().getHorizontalBoundGap(),
					getObjectAdapter().getVerticalGap()));
		}
		return retVal;
	}

	// void fillSeparateBound (int statIndex)
	void print(DynamicMatrix matrix) {
		((ADynamicMatrix) matrix).print();
	}

	void incrementLayout(VirtualContainer panel) {
		if (panel == null)
			return;
		LayoutManager l = (LayoutManager) panel.getLayout();
		if (l instanceof GridLayout) {
			GridLayout grid = (GridLayout) l;
			int numRows = grid.getRows() + 1;
			grid.setRows(numRows);
		} else if (l instanceof OEGridLayout) {
			OEGridLayout grid = (OEGridLayout) l;
			int numRows = grid.getRows() + 1;
			grid.setRows(numRows);
		}

	}

	void decrementLayout(VirtualContainer panel) {
		if (panel == null)
			return;
		LayoutManager l = (LayoutManager) panel.getLayout();
		if (l instanceof GridLayout) {
			GridLayout grid = (GridLayout) l;
			int numRows = grid.getRows() - 1;
			grid.setRows(numRows);
		} else if (l instanceof OEGridLayout) {
			OEGridLayout grid = (OEGridLayout) l;
			int numRows = grid.getRows() - 1;
			grid.setRows(numRows);
		}

	}

	boolean addToUnboundPanelWithPossiblyColumnTitle(
			VirtualContainer unboundPropertiesPanel,
			// VirtualComponent c, uiObjectAdapter attributed) {
			VirtualComponent c, Object attributed) {
		return addToUnboundPanelWithPossiblyColumnTitle(unboundPropertiesPanel,
				c, attributed, -1);
	}

	int columnLabelHeight = 1;

	boolean addToUnboundPanelWithPossiblyColumnTitle(
			VirtualContainer unboundPropertiesPanel,
			// VirtualComponent c, uiObjectAdapter attributed) {
			VirtualComponent c, Object attributed, int componentPos) {
		// boolean isFlatTableRow = getObjectAdapter().isFlatTableRow();
		int pos = componentPos;
		if (c.isVisible()) {
			// if (getObjectAdapter().getSeparateUnboundTitles() && attributed
			// instanceof uiContainerAdapter)
			// if (!unboundColumnTitlePanelNotToBeAdded) {
			// if (getObjectAdapter().getSeparateUnboundTitles() &&
			// !unboundColumnTitlePanelNotToBeAdded && attributed instanceof
			// uiContainerAdapter) {
			/*
			 * if (getObjectAdapter().getSeparateUnboundTitles() &&
			 * unboundColumnTitlePanelNotToBeAdded && componentPos ==
			 * unboundColumnTitlePos + 1 && attributed instanceof
			 * uiContainerAdapter && ((uiContainerAdapter)
			 * attributed).getColumnAdapters().size() > 0) {
			 * 
			 * // this is taking care of projection changes. The column adapters
			 * can be zero if chidren have not been added
			 * 
			 * //System.out.println("Command Panel prev size:" +
			 * unboundPropertiesPanel.getComponentCount());
			 * 
			 * VirtualContainer panel = getColumnTitlePanel();
			 * System.out.println("Command Panel prev size:" +
			 * unboundPropertiesPanel.getComponentCount());
			 * unboundPropertiesPanel.remove(unboundColumnTitlePos);
			 * uiContainerAdapter oadapter = (uiContainerAdapter) attributed;
			 * //boolean foundLabels = fillColumnTitlePanel(panel, oadapter);
			 * 
			 * columnLabelHeight = fillColumnTitlePanel(panel, oadapter);
			 * //System.out.println("Command Panel new size:" +
			 * unboundPropertiesPanel.getComponentCount()); if
			 * (unboundPropertiesPanel.getComponentCount() ==
			 * unboundColumnTitlePos) unboundPropertiesPanel.add(panel); else
			 * unboundPropertiesPanel.add(panel, unboundColumnTitlePos);
			 * //System.out.println("Command Panel new size:" +
			 * unboundPropertiesPanel.getComponentCount());
			 * 
			 * } else
			 */

			if ( getObjectAdapter().getSeparateUnboundTitles()
					&& !unboundColumnTitlePanelNotToBeAdded
					&& attributed instanceof CompositeAdapter
					&& getObjectAdapter().getDirection().equals(
							AttributeNames.VERTICAL)) {
				CompositeAdapter oadapter = (CompositeAdapter) attributed;
				
				/*
				 * if (unboundColumnTitlePanelFilled) { componentPos++; pos++; }
				 */
				if (oadapter.getShowChildrenColumnTitle() && !oadapter.hasNoComponents()
//						&& oadapter.showColumnTitle() // added this and fillColumnTitlePanel has it
						&& oadapter.getDirection().equals(
								AttributeNames.HORIZONTAL)) {

					VirtualContainer panel = getColumnTitlePanel();
					
					// the variable columnLabelHeight seems like a strange name for the return val
					columnLabelHeight = fillColumnTitlePanel(panel, oadapter);
//	moving this down					
//					unboundColumnTitlePanelFilled = true;

					// if (foundLabels) {
					if (columnLabelHeight > 0) {
						unboundColumnTitlePanelFilled = true; // moved from above

						if (pos == -1) {
							unboundColumnTitlePos = panel.getComponentCount();
							unboundPropertiesPanel.add(panel);
						} else {
							unboundColumnTitlePos = pos;
							unboundPropertiesPanel.add(panel, pos);
							pos++;
							// pos += columnLabelHeight;
						}
						//not clear we need to increment the layout as the caller has already set the layout
						//incrementLayout(unboundPropertiesPanel);
					}
					unboundColumnTitlePanelNotToBeAdded = true;

				}
			}
			if (pos < 0)
				unboundPropertiesPanel.add(c);
			else if (pos > unboundPropertiesPanel.getComponentCount()) {
//				Tracer.warning("Illegal position to add:" + pos
//						+ " adding to end");
				IllegalComponentAddPosition.newExample(pos, this);
				unboundPropertiesPanel.add(c);
			} else {
				unboundPropertiesPanel.add(c, pos);
				UnboundPropertiesUIComponentAdded.newCase(unboundPropertiesPanel, c, pos);
//				System.out.println("Added +" + c.getName() + " to unboundprops " + unboundPropertiesPanel.getName());
				// System.out.println("Component Width " +
				// c.getPreferredSize().getWidth());
			}
			// resetWidget(c);
		} else {
			int panelSize = unboundPropertiesPanel.getComponentCount();
			System.err.println("container size:" + panelSize);
			VirtualComponent lastComponent = unboundPropertiesPanel
					.getComponent(panelSize - 1);
			// c.setVisible(true);
			if (lastComponent != c) {
				System.err.println("last component to be made visible");
				c.setVisible(true);
			} else {
				// removeAllInvisible(unboundPropertiesPanel);
				unboundPropertiesPanel.remove(c);
				unboundPropertiesPanel.add(c);
			}
			c.setVisible(true);
		}
		if (attributed instanceof ObjectAdapter) {
			ObjectAdapter element = (ObjectAdapter) attributed;
			/*
			 * ((uiObjectAdapter) attributed).getWidgetAdapter()
			 * .setParentContainer(unboundPropertiesPanel);
			 */
			element.getWidgetAdapter().setParentContainer(
					unboundPropertiesPanel);
			if (element.getShowChildrenColumnTitle())
				unboundColumnTitlePanelNotToBeAdded = true; // meaning we are
															// not going to fill
															// it
		}
		return componentPos != pos;
	}

	void fillTitledAdapterMatrix(DynamicMatrix adapterMatrix, int row) {
		int prevRow = row - 1;
		int col = 0;
		while (col < adapterMatrix.numCols()) {
			ObjectAdapter parentAdapter = (ObjectAdapter) adapterMatrix
					.get(prevRow, col);
			if (parentAdapter == null
					|| !(parentAdapter instanceof CompositeAdapter)) {
				col++;
				continue;
			}
			Vector<ObjectAdapter> curAdapters = ((CompositeAdapter) parentAdapter)
					.getNonGraphicsChildAdapters();
			if (curAdapters.size() == 0) {
				col++;
				continue;
			}
			for (int childNum = 0; childNum < curAdapters.size(); childNum++) {
				ObjectAdapter child = curAdapters.get(childNum);
				adapterMatrix.set(row, col, child);
				col += Math.max(1, child.getNonGraphicsLeafAdapters().size());
			}

		}

	}

	DynamicMatrix titledAdapterMatrix;

	void fillHierarchicalTitlesOfFlatTable(DynamicMatrix adapterMatrix,
			CompositeAdapter topAdapter, int numLevels) {
		// the rows will not be of same size, top levels will have fewer columns		
		adapterMatrix.set(0, 0, topAdapter);
		for (int i = 1; i < numLevels + 1; i++) {
			fillTitledAdapterMatrix(adapterMatrix, i);
		}

	}

	boolean rowHasParentsWithSingleChild(DynamicMatrix adapterMatrix, int rowNum) {
		for (int col = 0; col < adapterMatrix.numCols(rowNum); col++) {
			ObjectAdapter objectAdapter = (ObjectAdapter) adapterMatrix
					.get(rowNum, col);
			if (!(objectAdapter instanceof CompositeAdapter))
				return false;
			CompositeAdapter containerAdapter = (CompositeAdapter) objectAdapter;
			if (containerAdapter.isDynamic())
				return false;
			if (containerAdapter.getNumberOfChildren() > 1)
				return false;
		}
		return true;

	}

	void pruneOnlyChildren(DynamicMatrix adapterMatrix) {
		for (int row = adapterMatrix.numRows() - 1; row >= 0; row--) {
			if (rowHasParentsWithSingleChild(adapterMatrix, row)) {
				adapterMatrix.removeRow(row + 1);
			}
		}

	}

	// static final String BLANK_TITLE = "----------";
	static final String BLANK_TITLE = " ";

	public int fillHierarchicalColumnTitlePanel(VirtualContainer titlePanel,
			CompositeAdapter containerAdapter) {
//		HierarchicalColumnTitlePanelFillingStarted.newCase(containerAdapter, titlePanel, this);
		
		titlePanel.removeAll();
		HierarchicalColumnTitlePanelFillingStarted.newCase(containerAdapter, titlePanel, this);

		Vector<ObjectAdapter> leafAdapters = containerAdapter
				.getNonGraphicsLeafAdapters();

		int numCols = leafAdapters.size();
		int numLevels = containerAdapter.getHeightOfNonShapeDescendents();
		// int numDescendents = containerAdapter.getNumberOfNonShapeLeaves();
		titledAdapterMatrix = new ADynamicMatrix(numLevels + 1, numCols);
		fillHierarchicalTitlesOfFlatTable(titledAdapterMatrix,
				containerAdapter, numLevels);
		pruneOnlyChildren(titledAdapterMatrix);
		numLevels = titledAdapterMatrix.numRows() - 1;
		// setLayoutLabelStack(adapter, titlePanel, numLevels, numCols);
		setLayout(adapter, titlePanel, numLevels, 1, adapter.getHorizontalGap(), adapter.getVerticalGap());
		for (int i = 1; i < numLevels + 1; i++) {
			VirtualContainer rowTitlePanel = PanelSelector.createPanel();
			setColors(rowTitlePanel);
			rowTitlePanel.setName ("Row Title Panel of " + getObjectAdapter().toDebugText());
			setColors(rowTitlePanel, getObjectAdapter());
			// maybe do setLayoutRow
			//setLayoutRow(containerAdapter, rowTitlePanel,0, adapter.getHorizontalGap());
			// as component lengths are being manipulated, we do not want stretch columns
			// but what if rows are stretched?
			//rowTitlePanel.setLayout(new uiGridLayout(1, 0, adapter.getHorizontalGap(), 0));
			
			setLayoutUnboundRow(containerAdapter, rowTitlePanel, 0);
			
			boolean filled = fillRowLabels(rowTitlePanel, containerAdapter,
					true);
			if (!filled & containerAdapter.isLabelled()) {
				// VirtualLabel titleLabel = createColumnTitleLabel("   ",
				// containerAdapter.getLabelWidth());
				if (containerAdapter.showColumnPrefix()) {
					VirtualLabel prefixLabel = createColumnPrefixLabel(containerAdapter);
					rowTitlePanel.add(prefixLabel);
				}
				VirtualLabel titleLabel = createColumnTitleLabel(BLANK_TITLE,
						containerAdapter.getLabelWidth());
				rowTitlePanel.add(titleLabel);
				if (containerAdapter.showColumnSuffix()) {
					VirtualLabel suffixLabel = createColumnSuffixLabel(containerAdapter);
					rowTitlePanel.add(suffixLabel);
				}
			}
			// setLayoutRow(adapter, rowTitlePanel, numCols);
			int j = 0;
			// for (int j = 0; j < numCols; j++) {
			while (j < numCols) {
				int columnWidth = getComponentWidth(leafAdapters.get(j));
				ObjectAdapter cellAdapter = (ObjectAdapter) titledAdapterMatrix
						.get(i, j);
				VirtualLabel label;

				if (cellAdapter == null) {
					if (containerAdapter.showColumnPrefix()) {
						VirtualLabel prefixLabel = createColumnPrefixLabel(containerAdapter);
						rowTitlePanel.add(prefixLabel);
					}
					label = createColumnTitleLabel(BLANK_TITLE, columnWidth);
					rowTitlePanel.add(label);
					if (containerAdapter.showColumnSuffix()) {
						VirtualLabel suffixLabel = createColumnSuffixLabel(containerAdapter);
						rowTitlePanel.add(suffixLabel);
					}
					j++;

				} else {
					if (cellAdapter.showColumnPrefix()) {
						VirtualLabel prefixLabel = createColumnPrefixLabel(cellAdapter);
						rowTitlePanel.add(prefixLabel);
					}
					label = createColumnTitleLabel(cellAdapter.columnTitle(),
							columnWidth);
					rowTitlePanel.add(label);
					VirtualLabel suffixLabel = createColumnSuffixLabel(containerAdapter);
					if (cellAdapter.showColumnSuffix()) {
						rowTitlePanel.add(suffixLabel);
					}
					attachEventHandlersToColumnTitle(cellAdapter
							.getTopFlatTableRow().getParentAdapter(),
							cellAdapter, label);
					int containerWidth = 0;
					int numDescendents = cellAdapter
							.getNumberOfNonShapeLeaves();
					if (cellAdapter instanceof CompositeAdapter
							&& numDescendents > 0) {
						// int numDescendents =
						// cellAdapter.getNumberOfNonShapeLeaves();
						
						// this loop will make sure each row has same number of cells
						// by replicating container adapter labels based on how many
						// descendents they have.
						for (int k = 1; k < numDescendents; k++) {
							if ((j + k)>= leafAdapters.size()) {
								LeafAdapterInconsistency.newExample((j+k), leafAdapters.size(), this); 
//								Tracer.warning("Leaf adapter inconsistency, accessing:" + (j+k) + "size:" + leafAdapters.size() + "\n Hide the main panel to continue working, and report this error if you have not received the multiple visit error. This error does not change the behavior of your program.");
								break;
							} else
							columnWidth = getComponentWidth(leafAdapters.get(j
									+ k));
							// columnWidth += leafAdapters.get(j +
							// k).getComponentWidth();
							if (cellAdapter.showColumnPrefix()) {
								VirtualLabel prefixLabel = createColumnPrefixLabel(cellAdapter);
								rowTitlePanel.add(prefixLabel);
							}
							label = createColumnTitleLabel(cellAdapter
									.columnTitle(), columnWidth);
							attachEventHandlersToColumnTitle(cellAdapter
									.getTopFlatTableRow().getParentAdapter(),
									cellAdapter, label);
							rowTitlePanel.add(label);
							if (cellAdapter.showColumnSuffix()) {
								suffixLabel = createColumnSuffixLabel(containerAdapter);
								rowTitlePanel.add(suffixLabel);
							}
						}
						j += numDescendents;
						// Misc.setWidth(label, columnWidth);
					} else {
//						Message.warning("Not incrementing j");
						j++;
					}
				}
				// rowTitlePanel.add(label);
				// titlePanel.add(label);

			}
			titlePanel.add(rowTitlePanel);
		}

		return 1;

	}

	int numLabelledNodes(Vector<ObjectAdapter> nodes) {
		return nodes.size();
		/*
		 * int retVal = 0; for (int i = 0; i < nodes.size(); i++) { if
		 * (!nodes.get(i).isLabelled()) continue; else retVal++; }
		 * 
		 * return retVal;
		 */
	}

	Vector<ObjectAdapter> getLabelledFlatRowAncestors(
			ObjectAdapter leafFlatRow) {

		Vector<ObjectAdapter> retVal = new Vector();
		/*
		 * if (!leafFlatRow.getParentAdapter().isFlatTableRow()) return retVal;
		 */
		ObjectAdapter nextFlatRow = leafFlatRow;
		while (nextFlatRow != null && nextFlatRow.isFlatTableRow()) {
			// if (nextFlatRow.isLabelled() && !nextFlatRow.isOnlyChild())
			if (nextFlatRow.isLabelled())
				retVal.add(nextFlatRow);
			nextFlatRow = nextFlatRow.getParentAdapter();

		}
		return retVal;

	}

	static final int ROW_LABEL_SIZE = 10;
	Vector<ObjectAdapter> flatRowAncestors;

	public Vector<ObjectAdapter> getLabelledFlatRowAncestors() {
		if (flatRowAncestors == null)
			flatRowAncestors = getLabelledFlatRowAncestors(getObjectAdapter());
		return flatRowAncestors;

	}

	int numLabelledNodes = -1;

	public int getNumLabelledNodes() {
		if (numLabelledNodes == -1) {
			numLabelledNodes = numLabelledNodes(getLabelledFlatRowAncestors());
		}
		return numLabelledNodes;

	}

	int childMaxLabelledNodes = -1;

	public void setChildNumLabelledNodes(int numNodes) {
		childMaxLabelledNodes = Math.max(childMaxLabelledNodes, numNodes);
	}

	public int getChildMaxNumLabelledNodes() {
		return childMaxLabelledNodes;
	}

	VirtualLabel myLabel;

	public void resetMyLabel() {
		if (myLabel == null)
			return;
		String text = getObjectAdapter().getLabelWithoutSuffix();
		myLabel.setText(text);

	}

	boolean fillRowLabels(VirtualContainer panel,
			CompositeAdapter containerAdapter, boolean isColumnTitlePanel) {
		// Vector<uiObjectAdapter> labelNodes =
		// getFlatRowAncestors(containerAdapter);
		RowLabelCreationStarted.newCase(this.getObjectAdapter(), this);
		Vector<ObjectAdapter> labelNodes = getLabelledFlatRowAncestors();

		// int numLabelledNodes = numLabelledNodes(labelNodes);
		int numLabelledNodes = getNumLabelledNodes();
		int maxNumLabelNodes = numLabelledNodes;
		ObjectAdapter tableAdapter = containerAdapter.getTableAdapter();
		if (tableAdapter.getWidgetAdapter() instanceof CommandAndStatePanelAdapter
				&& !containerAdapter.getIndented())

			maxNumLabelNodes = ((CommandAndStatePanelAdapter) tableAdapter
					.getWidgetAdapter()).getChildMaxNumLabelledNodes();
		if (maxNumLabelNodes == 0)
			return false;
		// int totalLabelWidth =
		// containerAdapter.getTableAdapter().getRowLabelsWidth();
		// int labelWidth = totalLabelWidth/Math.max(numLabelledNodes, 1);
		// int labelWidth = totalLabelWidth/maxNumLabelNodes;

		// int tabelLabelWidth = tableAdapter.getLabelWidth();
		int tabelLabelWidth = containerAdapter.getLabelWidth();
		int tabelIntentWidth = tableAdapter.getRowIndentWidth();
		int labelWidth = 0;
		boolean isIndented = containerAdapter.getIndented();
		for (int i = labelNodes.size() - 1; i >= 1; i--) {
			ObjectAdapter labelAdapter = labelNodes.get(i);

			String text;
			if (isColumnTitlePanel || !isIndented)
				text = BLANK_TITLE;
			else
				text = labelAdapter.getLabelWithoutSuffix();
			// int labelWidth;
			if (isIndented)
				labelWidth = labelAdapter.getLabelWidth();
			else
				labelWidth = tabelIntentWidth;
			/*
			 * else labelWidth = totalLabelWidth/labe;
			 */
			VirtualLabel label = createColumnTitleLabel(text, labelWidth);
			panel.add(label);
			
		}
		int nextIndentNode = labelNodes.size();
		String text;
		labelWidth = tabelLabelWidth;
		boolean isBlank;
		if (labelNodes.size() > 0) {
			ObjectAdapter mainLabelAdapter = labelNodes.get(0);
			isBlank = labelNodes.size() == maxNumLabelNodes; // rightmost label
			text = mainLabelAdapter.getLabelWithoutSuffix();
			// isBlank = false;
		} else {
			nextIndentNode = labelNodes.size() + 1;
			text = BLANK_TITLE;
			isBlank = true;
		}

		VirtualLabel prefixLabel = createRowPrefixLabel(tableAdapter, isBlank);
		panel.add(prefixLabel);

		VirtualLabel label = createColumnTitleLabel(text, labelWidth);
		if (!isColumnTitlePanel) {
			myLabel = label;
			RightMenuManager.bindToRightMenu(label, getObjectAdapter());
			label.addMouseListener(new ASelectionTriggerMouseListener(containerAdapter));
			label.addMouseListener(new ADoubleClickMouseListener(containerAdapter, containerAdapter.getUIFrame()));

		}
		// myLabel = createColumnTitleLabel(text, labelWidth);
		// this will not work
		/*
		 * if (myLabel == null) { myLabel = createColumnTitleLabel(text,
		 * labelWidth); } else { myLabel.setText(text); Misc.setWidth(myLabel,
		 * labelWidth); }
		 */

		panel.add(label);
		// panel.add(myLabel);
		if (!containerAdapter.getIndented()) {
			for (int i = nextIndentNode; i < maxNumLabelNodes; i++) {

				// String text;

				text = BLANK_TITLE;
				// int labelWidth;

				label = createColumnTitleLabel(text, tabelIntentWidth);
				// VirtualLabel label = createColumnTitleLabel(text,
				// tabelIntentWidth);
				panel.add(label);
			}
		}

		// return labelNodes.size() > 0;
		return true;

	}

	boolean oldFillRowLabels(VirtualContainer panel,
			CompositeAdapter containerAdapter, boolean isColumnTitlePanel) {
		// Vector<uiObjectAdapter> labelNodes =
		// getFlatRowAncestors(containerAdapter);
		Vector<ObjectAdapter> labelNodes = getLabelledFlatRowAncestors();

		// int numLabelledNodes = numLabelledNodes(labelNodes);
		int numLabelledNodes = getNumLabelledNodes();
		int maxNumLabelNodes = numLabelledNodes;
		if (!containerAdapter.getIndented())

			maxNumLabelNodes = ((CommandAndStatePanelAdapter) containerAdapter
					.getTableAdapter().getWidgetAdapter())
					.getChildMaxNumLabelledNodes();
		if (maxNumLabelNodes == 0)
			return false;
		int totalLabelWidth = containerAdapter.getTableAdapter()
				.getRowLabelsWidth();
		// int labelWidth = totalLabelWidth/Math.max(numLabelledNodes, 1);
		int labelWidth = totalLabelWidth / maxNumLabelNodes;
		int labelLength = 0;
		for (int i = labelNodes.size() - 1; i >= 0; i--) {
			ObjectAdapter labelAdapter = labelNodes.get(i);

			String text;
			if (isColumnTitlePanel)
				text = BLANK_TITLE;
			else
				text = labelAdapter.getLabelWithoutSuffix();
			// int labelWidth;
			if (containerAdapter.getIndented())
				labelWidth = labelAdapter.getLabelWidth();
			/*
			 * else labelWidth = totalLabelWidth/labe;
			 */
			VirtualLabel label = createColumnTitleLabel(text, labelWidth);
			panel.add(label);
		}
		if (!containerAdapter.getIndented()) {
			for (int i = labelNodes.size(); i < maxNumLabelNodes; i++) {

				String text;

				text = BLANK_TITLE;
				// int labelWidth;
				/*
				 * if (containerAdapter.getIndented()) labelWidth =
				 * labelAdapter.getLabelWidth();
				 */
				/*
				 * else labelWidth = totalLabelWidth/labe;
				 */
				VirtualLabel label = createColumnTitleLabel(text, labelWidth);
				panel.add(label);
			}
		}
		/*
		 * if (!containerAdapter.getIndented()) { labelWidth = totalLabelWidth -
		 * labelWidth*numLabelledNodes; if (labelWidth > 0) { VirtualLabel label
		 * = createColumnTitleLabel(BLANK_TITLE, labelWidth); panel.add(label);
		 * } }
		 */
		// return labelNodes.size() > 0;
		return true;

	}

	public void buildFlatTable() {
		FlatTableBuildingStarted.newCase(getObjectAdapter(), this);
		CompositeAdapter containerAdapter = (CompositeAdapter) getObjectAdapter();

		VirtualContainer topPanel = (VirtualContainer) containerAdapter
				.getUIComponent();
		topPanel.removeAll();
		fillRowLabels(topPanel, containerAdapter, false);

		Vector<ObjectAdapter> leafAdapters = containerAdapter
				.getNonGraphicsLeafAdapters();

		int numCols = leafAdapters.size();
		/*
		 * Vector<uiObjectAdapter> labelNodes =
		 * getFlatRowAncestors(containerAdapter); for (int i = labelNodes.size()
		 * - 1; i >= 0; i--) { uiObjectAdapter labelAdapter = labelNodes.get(i);
		 * VirtualLabel label =
		 * createColumnTitleLabel(labelAdapter.getLabelWithoutSuffix(),
		 * labelAdapter.getLabelWidth()); topPanel.add(label); }
		 */
		// VirtualContainer titlePanel = getColumnTitlePanel();
		// fillHierarchicalColumnTitlePanel(titlePanel, containerAdapter);
		// VirtualContainer rowPanel = PanelSelector.createPanel();
		// setLayoutRow(containerAdapter, topPanel, numCols);
		/*
		 * if (containerAdapter.getShowChildrenColumnTitle()) {
		 * //containerAdapter.getParentAdapter().getWidgetAdapter().
		 * processDeferredFillColumnTitlePanel(containerAdapter);
		 * containerAdapter
		 * .getTableAdapter().getWidgetAdapter().processDeferredFillColumnTitlePanel
		 * (containerAdapter); }
		 */
		setLayoutUnboundRow(containerAdapter, topPanel, 0);

		for (int i = 0; i < leafAdapters.size(); i++) {
			if (leafAdapters.elementAt(i).showColumnPrefix()) {
				VirtualLabel prefixLabel = createColumnPrefixLabel(leafAdapters
						.elementAt(i));
				topPanel.add(prefixLabel);
			}
			topPanel.add(getComponent(leafAdapters.elementAt(i)));
			leafAdapters.elementAt(i).setParentContainer(topPanel);
			if (leafAdapters.elementAt(i).showColumnSuffix()) {
				VirtualLabel suffixLabel = createColumnSuffixLabel(containerAdapter);
				topPanel.add(suffixLabel);
			}
		}

		// rowPanel.add(getComponent(leafAdapters.elementAt(i)));
		// setLayout(containerAdapter, topPanel, 2, 1);
		// topPanel.add(titlePanel);
		// topPanel.add(rowPanel);
		topPanel.validate();
		getObjectAdapter().getUIFrame().validate();
		if (containerAdapter.getShowChildrenColumnTitle()) {
			// containerAdapter.getParentAdapter().getWidgetAdapter().processDeferredFillColumnTitlePanel(containerAdapter);
			containerAdapter.getTableAdapter().getWidgetAdapter()
					.processDeferredFillColumnTitlePanel(containerAdapter);
		}

	}

	@Override
	public Vector<ObjectAdapter> getDisplayChildrenAdapters() {
		Vector<ObjectAdapter> retVal = new Vector();
		for (int i = 0; i < sortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = sortedPropertiesList.get(i);
			if (compAdapter != null)
				retVal.add(compAdapter);
		}
		for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			ObjectAdapter compAdapter = unSortedPropertiesList.get(i);
			if (compAdapter != null)
				retVal.add(compAdapter);
		}
		return retVal;
	}
	
	boolean isMainPanelVisible() {
		return !getObjectAdapter().getUIFrame().isOnlyGraphicsPanel()
//				getObjectAdapter().getUIFrame().getTopViewManager().mainPanelIsVisible() 
				|| getObjectAdapter().getUIFrame().isManualMainContainer();

	}

	@Override
	public void descendentUIComponentsAdded() {
		if (!isMainPanelVisible()) {
			Tracer.info(this, "Ignoring descendents creation as main panel is not visible");
			return;
		}
		if (componentPanel.getParent() == null) {
			Tracer.info(this, "Ignoring descendents creation as parent is null ");
			return;
		}
		if (!getObjectAdapter().isTopTableRow())
			return;
		if (getObjectAdapter().getShowChildrenColumnTitle())
			getObjectAdapter().getParentAdapter().getWidgetAdapter()
					.processDeferredFillColumnTitlePanel(
							(CompositeAdapter) getObjectAdapter());
		if (!added)
			return;
		added = false;
		// forceBuildPanel();

		CompositeAdapter containerAdapter = (CompositeAdapter) getObjectAdapter();
		if (containerAdapter.isFlatTableRow()
				&& !(containerAdapter.hasFlatTableRowDescendent()))
			buildFlatTable();
		else if (hasDescendentRows) {
			forceBuildPanel();
		}
		/*
		 * if (containerAdapter.flattenTable()) { //if
		 * (containerAdapter.getTopRowWithPrimitivesAndComposites()) if
		 * (containerAdapter.isFlatTableRow()) buildFlatTable(); return; }
		 */

	}

	@Override
	public void rebuildPanel() {
		CompositeAdapter containerAdapter = (CompositeAdapter) getObjectAdapter();
		/*
		 * if (containerAdapter.flattenTable()) { if
		 * (containerAdapter.getTopRowWithPrimitivesAndComposites())
		 * buildFlatTable(); return; }
		 */

		if (containerAdapter.isFlatTableRow()) {
			buildFlatTable();
			/*
			 * added = true; buildPanel();
			 */
		} else {
			added = true;
			buildPanel();
		}

	}

	public void buildPanel() {

		CompositeAdapter containerAdapter = (CompositeAdapter) getObjectAdapter();

		// if (containerAdapter.flattenTable()) {
		if (!containerAdapter.isFlatTableCell()
				&& (containerAdapter.isFlatTableComponent() || containerAdapter
						.isFlatTableRow())) {
			/*
			 * if (containerAdapter.getTopRowWithPrimitivesAndComposites())
			 * buildFlatTable();
			 */
			return;
		}
		if (hasDescendentRows)
			return;
		if (!added)
			return;
		added = false;
		forceBuildPanel();
	}
	
	public void addCommands (ADynamicSparseList<ButtonCommand> commandsList) {
		for (int i = numUnboundButtonsAdded; i < commandsList.size(); i++) {
			VirtualComponent c = getComponent(commandsList.get(i));
			if (c.getParent() != null)
				continue;
//			unboundButtonsPanel.add(getComponent(unboundButtons
//					.elementAt(i)));
			unboundButtonsPanel.add(c);
			UnboundButtonAdded.newCase(unboundButtonsPanel, c, this);
//			System.out.println("adding" + c.getName() + " to " + unboundButtonsPanel.getName());
		}
	}
	
	void manualBuildPanel() {
		addUnsortedProperties(componentPanel);
		addUnsortedCommands(componentPanel);
		addSortedProperties(componentPanel);
		addSortedCommands(componentPanel);		
	}
	
	void buildGridBagPanel() {
		int currentRow = childComponents.firstFilledRow(0);
		while (currentRow <= childComponents.size() && currentRow != -1) {
			int currentColumn = childComponents.firstFilledColumn(currentRow, 0);
			while (currentColumn != -1 && currentColumn < childComponents.numCols(currentRow)) {
				Object element = childComponents.get(currentRow, currentColumn);
				add(componentPanel, element);

				currentColumn = childComponents.firstFilledColumn(currentRow, currentColumn + 1);
			}
			currentRow = childComponents.firstFilledRow(currentRow + 1);	
			
			
		}
		
	}

	public void forceBuildPanel() {

		// if (getObjectAdapter().getShowColumnTitles())

		/*
		 * if (getObjectAdapter().getShowChildrenColumnTitle())
		 * getObjectAdapter().getParentAdapter().getWidgetAdapter().
		 * processDeferredFillColumnTitlePanel((uiContainerAdapter)
		 * getObjectAdapter());
		 */

		// System.out.println("Start of build panel");
		// print (childComponents);
		// if (initialized) return;
		// componentPanel = new JPanel();
		UIComponentCreationStarted.newCase(getObjectAdapter().getRealObject(), this);
		if (manualAdds) {
//			manualBuildPanel();
//			addUnsortedProperties(componentPanel);
//			addUnsortedCommands(componentPanel);
//			addSortedProperties(componentPanel);
//			addSortedCommands(componentPanel);
			return;
		}
		if (adapter.isGridBagLayout()) {
			buildGridBagPanel();
			return;
		}
		if (!initialized)
			firstUnboundRow = childComponents.numRows();
		ObjectAdapter objectAdapter = getObjectAdapter();
		// boolean showButtons = getObjectAdapter().getShowButtons();

		if (needUnboundPropertiesPanel()) {
			unboundPropertiesPanel = getUnboundPropertiesPanel();
			// int unboundPosition = 0;
			/*
			 * if (childComponents.numRows() > 0) unboundPropertiesPanel = new
			 * JPanel(); else unboundPropertiesPanel = componentPanel;
			 */

			// print (childComponents);
			processDirectionAddLabelsForUnbound();
			// print (childComponents);
			/*
			 * for (int i = this.numUnboundPropertiesAdded; i <
			 * unboundProperties.size(); i++) { Component c =
			 * getComponent(unboundProperties.elementAt(i)); if (c.getParent()
			 * != null) continue; unboundPropertiesPanel.add(c);
			 * ((uiObjectAdapter)
			 * unboundProperties.elementAt(i)).getWidgetAdapter
			 * ().setParentContainer(unboundPropertiesPanel); }
			 * 
			 * numUnboundPropertiesAdded = unboundProperties.size();
			 */
			if (insertionStatus != InsertionStatus.NONE) {
				unboundPropertiesPanel.removeAll();
				insertSortedPropertiesFrom = 0;
				insertionStatus = InsertionStatus.NONE;
			}
			// for (int i = this.numSortedPropertiesAdded; i <
			// sortedPropertiesList.size(); i++) {
			int unboundPosition = 0;
			// / this assumes no properties in vector
			/*
			 * if (unboundColumnTitlePanelFilled) unboundPosition++;
			 */

			unboundPosition += sortedPropertiesList
					.filledIndexFor(insertSortedPropertiesFrom);
			boolean positionIncremented = false;
			if (unboundColumnTitlePanelFilled
					&& unboundPosition >= unboundColumnTitlePos) {
				unboundPosition++;
				positionIncremented = true;
			}

			for (int i = insertSortedPropertiesFrom; i < sortedPropertiesList
					.size(); i++) {
				ObjectAdapter property = (ObjectAdapter) sortedPropertiesList
						.get(i);
				if (property == null)
					continue; // with graphics some properties are null
				VirtualComponent c = getComponent(sortedPropertiesList.get(i));
				if (c.getParent() != null) {
					unboundPosition++;
					continue;
				}

				boolean columnTitleAdded = addToUnboundPanelWithPossiblyColumnTitle(
						unboundPropertiesPanel, c, property, unboundPosition);
				unboundPosition++;
				if (columnTitleAdded)
					unboundPosition++;

			}

			insertSortedPropertiesFrom = sortedPropertiesList.size();

			unboundPosition += unSortedPropertiesList
					.filledIndexFor(insertUnsortedPropertiesFrom);
			if (unboundColumnTitlePanelFilled
					&& unboundPosition >= unboundColumnTitlePos
					&& !positionIncremented) {
				unboundPosition++;
				positionIncremented = true;
			}

			// for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			// fillColumnLabels (unSortedPropertiesList, true, true);
			// for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			for (int i = insertUnsortedPropertiesFrom; i < unSortedPropertiesList
					.size(); i++) {
				/*
				 * Object o = unSortedPropertiesList.get(i); if (!(o instanceof
				 * uiObjectAdapter )) { continue; }
				 */
				// uiObjectAdapter attributed = (uiObjectAdapter)
				// unSortedPropertiesList.get(i);
				Object attributed = unSortedPropertiesList.get(i);
				if (attributed == null)
					continue;
				VirtualComponent c = getComponent(attributed);
				if (c.getParent() != null && c.isVisible()) {
					// c.setVisible(true);
					unboundPosition++;
					continue;
				}
				boolean columnTitleAdded = addToUnboundPanelWithPossiblyColumnTitle(
						unboundPropertiesPanel, c, attributed, unboundPosition);
				unboundPosition++;
				if (columnTitleAdded)
					unboundPosition++;
				/*
				 * //c.revalidate(); if (c.isVisible())
				 * unboundPropertiesPanel.add(c); else { int panelSize =
				 * unboundPropertiesPanel.getComponentCount();
				 * System.out.println("container size:" + panelSize);
				 * VirtualComponent lastComponent =
				 * unboundPropertiesPanel.getComponent(panelSize -1); if
				 * (lastComponent == c) { System.out.println("last component to
				 * be made visible"); c.setVisible(true); } else {
				 * unboundPropertiesPanel.remove(c);
				 * unboundPropertiesPanel.add(c); } } if (attributed instanceof
				 * uiObjectAdapter) ((uiObjectAdapter)
				 * attributed).getWidgetAdapter
				 * ().setParentContainer(unboundPropertiesPanel);
				 */
			}
			insertUnsortedPropertiesFrom = unSortedPropertiesList.size();

		}
		/*
		 * if (unboundPropertiesPanel != null)
		 * unboundPropertiesPanel.validate();
		 */
		if (needUnboundButtonsPanel()) {
			Integer numCols = getObjectAdapter().getUnboundButtonsRowSize();
			if (unboundButtonsPanel == null) {
				// unboundButtonsPanel = new JPanel(new
				// GridLayout(unboundCommands.size()/numCols,numCols));
				// make it component panel if no properties - do not create apen
				// in that case
				unboundButtonsPanel = getUnboundButtonPanel(numCols);
				// unboundButtonsPanel = PanelSelector.createPanel();
				// //unboundButtonsPanel.setLayout(new
				// GridLayout(unboundCommands
				// if (unboundButtons.size() > 1) {
				// unboundButtonsPanel.setLayout(new GridLayout(unboundButtons
				// .size()
				// / numCols, numCols));
				// }
			}
			//for (int i = numUnboundButtonsAdded; i < sortedCommandsList.size(); i++) {
//			for (int i = 0; i < sortedCommandsList.size(); i++) {
//				VirtualComponent c = getComponent(sortedCommandsList.get(i));
//				if (c.getParent() != null)
//					continue;
////				unboundButtonsPanel.add(getComponent(unboundButtons
////						.elementAt(i)));
//				unboundButtonsPanel.add(c);
//			}
			if (getObjectAdapter().getUnboundButtonsPlacement() == null || getObjectAdapter().getUnboundButtonsPlacement() == BorderLayout.EAST ||
					getObjectAdapter().getUnboundButtonsPlacement() == BorderLayout.SOUTH) {
				addCommands(sortedCommandsList);
				addCommands(unSortedCommandsList);
			} else {
				addCommands(unSortedCommandsList);
				addCommands(sortedCommandsList);
			}
//			addCommands(sortedCommandsList);
//
//			//numUnboundButtonsAdded = unboundButtons.size();
//			numUnboundButtonsAdded = sortedCommandsList.size();
//			addCommands(unSortedCommandsList);

//			for (int i = 0; i < unSortedCommandsList.size(); i++) {
//				VirtualComponent c = getComponent(unSortedCommandsList.get(i));
//				if (c.getParent() != null)
//					continue;				
//				unboundButtonsPanel.add(c);
//			}
			numUnboundButtonsAdded = sortedCommandsList.size() + unSortedCommandsList.size();
//			for (int i = numUnboundButtonsAdded; i < unboundButtons.size(); i++) {
//				VirtualComponent c = getComponent(unboundButtons.elementAt(i));
//				if (c.getParent() != null)
//					continue;
//				unboundButtonsPanel.add(getComponent(unboundButtons
//						.elementAt(i)));
//			}
//			numUnboundButtonsAdded = unboundButtons.size();
		}

		// if (unboundPropertiesPanel != null && unboundButtonsPanel == null &&
		// childComponents.numRows() == 0 ) return;
		// if (unboundPropertiesPanel == null && unboundButtonsPanel == null &&
		// childComponents.numRows() == 0 ) return;
		// assming that only static components are bound!
		if (boundComponentsAdded)
			return;
		boundComponentsAdded = true;
		// if ((unboundPropertiesPanel == null || unboundButtonsPanel == null)
		if ((unboundPropertiesPanel == null || unboundButtonsPanel == null)
				&& (childComponents.numRows() == 0) && rowItems.size() == 0
				&& (columnItems.size() == 0))
			return; // componentpanel is unboundProperties or unbound buttons or
		// nothing and thus does not have children
		// why do we need this
		/*
		 * if (childComponents.numRows() == 0 && rowItems.size() == 0 &&
		 * columnItems.size() == 0 && unboundProperties.size() == 0 &&
		 * unboundButtons.size() == 0) return;
		 */
		if (unboundPropertiesPanel == null && unboundButtonsPanel == null) {

			// processPartiallyBound();
			processUnbound();
			boundComponentsPanel = componentPanel;
			fillPanel();
		} else {

			componentPanel.setLayout(new BorderLayout());
			// boundComponentsPanel = componentPanel;
			if (childComponents.numRows() > 0 || rowItems.size() > 0
					|| columnItems.size() > 0) {
				if (boundComponentsPanel == null) {
					// boundComponentsPanel = new JPanel();
					boundComponentsPanel = PanelSelector.createPanel();
					boundComponentsPanel.setName("bound components panel of" + getObjectAdapter());
					setColors(boundComponentsPanel, getObjectAdapter());

					setLayout(adapter, boundComponentsPanel, 1, 1);
				}
				fillPanel();
			}
			// fillPanel();
			componentPanel.setLayout(new BorderLayout());
			if (unboundPropertiesPanel != null
					&& unboundPropertiesPanel != componentPanel
					&& unboundPropertiesPanel.getParent() == null)
				componentPanel.add(unboundPropertiesPanel, objectAdapter
						.getPropertiesPlacement());
			if (unboundButtonsPanel != null
					&& unboundButtonsPanel.getParent() == null)
				componentPanel.add(unboundButtonsPanel, objectAdapter
						.getUnboundButtonsPlacement());
	//					.getButtonsPlacement());
			if (boundComponentsPanel != null
					&& boundComponentsPanel.getParent() == null) {
				componentPanel.add(boundComponentsPanel, objectAdapter
						.getBoundPlacement());
//				System.out.println("added:"+  boundComponentsPanel.getName() + " to " + componentPanel.getName() );
				BoundComponentPanelAdded.newCase(componentPanel, boundComponentsPanel, this);
			}

		}

		initialized = true;

	}

	@Override
	public Vector<ObjectAdapter> getChildrenAdaptersInDisplayOrder() {
		int numSortedProperties = sortedPropertiesList.size();
		int numUnsortedProperties = unSortedPropertiesList.size();
		// uiObjectAdapter[] retVal = new uiObjectAdapter[numSortedProperties +
		// numUnsortedProperties];
		Vector<ObjectAdapter> retVal = new Vector();
		for (int i = 0; i < numSortedProperties; i++) {
			ObjectAdapter element = sortedPropertiesList.get(i);
			if (element != null)
				retVal.addElement(element);
		}
		for (int i = 0; i < unSortedPropertiesList.size(); i++) {
			ObjectAdapter element = unSortedPropertiesList.get(i);
			if (element != null)
				retVal.addElement(element);
		}
		return retVal;
	}

	// JLabel filler() {
	VirtualLabel filler(Object item) {
		// return null;
		/*
		 * Component retVal = new JLabel (""); retVal.setMaximumSize(new
		 * Dimension(0, 0)); retVal.setSize(new Dimension(0, 0));
		 * retVal.setPreferredSize(new Dimension(0, 0)); retVal.hide();
		 */
		// return retVal;
		// return createLabel(" ");
		VirtualLabel retVal = createLabel(item, adapter.getCellFillerLabel(), adapter
				.getCellFillerIcon());
		retVal.setName("Filler Label:" + adapter.toDebugText());
		//Integer width = adapter.getLabelWidth();
		Integer width = adapter.getFillerWidth();
		if (width != null)
			OEMisc.setWidth(retVal, width);
		return retVal;
		// new JTextField ("blank", 5);
	}

	int nextRow;

	int nextCol;

	void add(VirtualContainer c, Object o) {
		// this makes no sense!
		/*
		 * if (c.getParent() != null) return;
		 */
		// JPanel enclosingPanel = new JPanel();
		// enclosingPanel.add(getComponent(o));
		// c.add(enclosingPanel);
		VirtualComponent component = getComponent(o);
		constrainedAdd(c, o, component);
//		if (component.getParent() != null)
//			return;
//		Object constraint = getComputedAddConstraint(o);
//		if (constraint == null)
//		// c.add(getComponent(o));
//		c.add(component);
//		else
//			c.add(component, constraint);
////		System.out.println("Added component:" + component.getName() + " to " + c.getName());
//		UIComponentAdded.newCase(c, component, this);
//		if (o instanceof ObjectAdapter)
//			((ObjectAdapter) o).getWidgetAdapter().setParentContainer(c);
	}
	
	void constrainedAdd(VirtualContainer c, Object o, VirtualComponent component) {
//		// this makes no sense!
//		/*
//		 * if (c.getParent() != null) return;
//		 */
//		// JPanel enclosingPanel = new JPanel();
//		// enclosingPanel.add(getComponent(o));
//		// c.add(enclosingPanel);
//		VirtualComponent component = getComponent(o);
		if (component.getParent() != null)
			return;
		Object constraint = getComputedAddConstraint(o);
		if (constraint == null)
		// c.add(getComponent(o));
		c.add(component);
		else
			c.add(component, constraint);
//		System.out.println("Added component:" + component.getName() + " to " + c.getName());
		UIComponentAdded.newCase(c, component, this);
		if (o instanceof ObjectAdapter)
			((ObjectAdapter) o).getWidgetAdapter().setParentContainer(c);
	}

	void removeOverlappingRows(DynamicMatrix matrix, ADynamicSparseList list) {
		// System.out.println("start of overlapping rows");
		// print (matrix);
		int nextFilledRowNum = 0;
		while (true) {
			// System.out.println("beginning of overlapping rows loop");
			// print (matrix);
			nextFilledRowNum = list.firstFilledSlot(nextFilledRowNum);
			if (nextFilledRowNum == -1)
				return;
			int nextEmptyRowNum = list.firstEmptySlot(nextFilledRowNum);
			int newStartRowNum = matrix.nextEmptyRows(nextFilledRowNum,
					nextEmptyRowNum - nextFilledRowNum);
			for (int i = nextFilledRowNum; i < newStartRowNum; i++)
				list.insertElementAt(null, nextFilledRowNum);
			nextFilledRowNum = newStartRowNum + nextEmptyRowNum
					- nextFilledRowNum;

		}

		/*
		 * for (int i = 0; i < list.size(); i++) { if (list.get(i) != null) {
		 * int nextEmptyRow = matrix.nextEmptyRow(i); int j = i; for (; j <
		 * nextEmptyRow; j++ ) list.insertElementAt(null, j); i = j; } }
		 */
	}

	void removeOverlappingColumns(DynamicMatrix matrix, ADynamicSparseList list) {
		int nextFilledColNum = 0;
		while (true) {
			nextFilledColNum = list.firstFilledSlot(nextFilledColNum);
			if (nextFilledColNum == -1)
				return;
			int nextEmptyColNum = list.firstEmptySlot(nextFilledColNum);
			int newStartColNum = matrix.nextEmptyColumns(nextFilledColNum,
					nextEmptyColNum - nextFilledColNum);
			for (int i = nextFilledColNum; i < newStartColNum; i++)
				list.insertElementAt(null, nextFilledColNum);
			nextFilledColNum = newStartColNum + nextEmptyColNum
					- nextFilledColNum;

		}
		/*
		 * for (int i = 0; i < list.size(); i++) { if (list.get(i) != null) {
		 * int nextEmptyRow = matrix.nextEmptyRow(i); int j = i; for (; j <
		 * nextEmptyRow; j++ ) list.insertElementAt(null, j); i = j; } }
		 */
	}

	int overlappingRowsSplit(DynamicMatrix matrix, ADynamicSparseList list) {
		int firstMatrixRow = matrix.firstFilledRow(0);
		if (firstMatrixRow < 0)
			return -1;
		int lastMatrixRow = matrix.numRows() - 1;
		int nextFilledRowNum = 0;
		// int prevFilledRowNum = -1;
		while (true) {
			nextFilledRowNum = list.firstFilledSlot(nextFilledRowNum);
			if (nextFilledRowNum == -1)
				return list.size();

			if (nextFilledRowNum >= firstMatrixRow)
				return nextFilledRowNum;

			// if (nextFilledRowNum < firstMatrixRow) {
			// prevFilledRowNum = nextFilledRowNum;
			nextFilledRowNum++;
			// continue;
			// }
			// return nextFilledRowNum;
		}
		/*
		 * for (int i = 0; i < list.size(); i++) { if (list.get(i) != null) {
		 * int nextEmptyRow = matrix.nextEmptyRow(i); int j = i; for (; j <
		 * nextEmptyRow; j++ ) list.insertElementAt(null, j); i = j; } }
		 */
	}

	void splitOverlappingRows(DynamicMatrix matrix, ADynamicSparseList list) {
		int splitIndex = overlappingRowsSplit(matrix, list);
		if (splitIndex <= 0) {
			upperRowItems = new ADynamicSparseList();
			lowerRowItems = list;
			return;
		}
		if (splitIndex >= list.size()) {
			upperRowItems = list;
			lowerRowItems = new ADynamicSparseList();
			return;
		}
		upperRowItems = new ADynamicSparseList();
		boolean upperNull = true;
		for (int i = 0; i < splitIndex; i++) {
			Object item = list.get(i);
			if (item != null)
				upperNull = false;
			upperRowItems.set(i, item);
		}
		if (upperNull) {
			upperRowItems = new ADynamicSparseList();
		}
		lowerRowItems = new ADynamicSparseList();
		boolean lowerNull = true;
		for (int i = splitIndex; i < list.size(); i++) {
			Object item = list.get(i);
			if (item != null)
				lowerNull = false;
			lowerRowItems.set(i - splitIndex, item);
		}
		if (lowerNull) {
			lowerRowItems = new ADynamicSparseList();
		}

	}

	int overlappingColumnsSplit(DynamicMatrix matrix, ADynamicSparseList list) {
		int firstMatrixCol = matrix.firstFilledColumn(0);
		if (firstMatrixCol < 0)
			return -1;
		// int lastMatrixCol = matrix.numCols() - 1;
		int nextFilledColNum = 0;
		// int prevFilledRowNum = -1;
		while (true) {
			nextFilledColNum = list.firstFilledSlot(nextFilledColNum);
			if (nextFilledColNum == -1)
				return list.size();

			if (nextFilledColNum >= firstMatrixCol)
				return nextFilledColNum;

			// if (nextFilledRowNum < firstMatrixRow) {
			// prevFilledRowNum = nextFilledRowNum;
			nextFilledColNum++;
			// continue;
			// }
			// return nextFilledRowNum;
		}
		/*
		 * for (int i = 0; i < list.size(); i++) { if (list.get(i) != null) {
		 * int nextEmptyRow = matrix.nextEmptyRow(i); int j = i; for (; j <
		 * nextEmptyRow; j++ ) list.insertElementAt(null, j); i = j; } }
		 */
	}

	void splitOverlappingColumnsOld(DynamicMatrix matrix,
			ADynamicSparseList list) {
		// print(childComponents);
		int splitIndex = overlappingColumnsSplit(matrix, list);
		// print(childComponents);
		if (splitIndex <= 0) {
			leftColumnItems = new ADynamicSparseList();
			rightColumnItems = list;
			return;
		}
		if (splitIndex >= list.size()) {
			leftColumnItems = list;
			rightColumnItems = new ADynamicSparseList();
			return;
		}
		leftColumnItems = new ADynamicSparseList();
		boolean leftNull = true;
		for (int i = 0; i < splitIndex; i++) {
			Object item = list.get(i);
			if (item != null)
				leftNull = false;
			leftColumnItems.set(i, item);
		}
		if (leftNull) {
			leftColumnItems = new ADynamicSparseList();
		}
		rightColumnItems = new ADynamicSparseList();
		boolean rightNull = true;
		for (int i = splitIndex; i < list.size(); i++) {
			Object item = list.get(i);
			if (item != null)
				rightNull = false;
			rightColumnItems.set(i - splitIndex, item);
		}
		if (rightNull) {
			rightColumnItems = new ADynamicSparseList();
		}

	}

	void handleOverlappingColumns(DynamicMatrix matrix, ADynamicSparseList list) {
		// print(childComponents);
		int leftMatrixColumn = matrix.firstFilledColumn();
		int rightMatrixColumn = matrix.lastFilledColumn();
		int firstListColumn = list.firstFilledSlot();
		int lastListColumn = list.lastFilledSlot();
		if (firstListColumn > rightMatrixColumn) {
			leftColumnItems = new ADynamicSparseList();
			rightColumnItems = list;
			return;
		}
		if (lastListColumn < leftMatrixColumn) {
			rightColumnItems = new ADynamicSparseList();
			leftColumnItems = list;
			return;
		}
		// we come here only in case of overlap
		leftColumnItems = new ADynamicSparseList();
		rightColumnItems = new ADynamicSparseList();
		/*
		 * int leftListSize = leftMatrixColumn;
		 * 
		 * if (list.get(leftListSize) != null && getLabelRight
		 * (list.get(leftListSize)) != null) leftListSize --;
		 */
		for (int i = firstListColumn; i < leftMatrixColumn && i < list.size(); i++) {
			leftColumnItems.set(i, list.get(i));
		}
		int firstOverlappingListItem = list.firstFilledSlot(leftMatrixColumn);
		// boolean firstOverlappingListItemHasLeftLabel =
		// getLabelLeft(list.get(firstOverlappingListItem));
		for (int i = firstOverlappingListItem; i < list.size(); i++) {
			rightColumnItems.set(rightMatrixColumn + i
					- firstOverlappingListItem + 1, list.get(i));
		}
		return;
		/*
		 * //int rightMatrixCol = matrix.lastFilledCol(); int splitIndex =
		 * overlappingColumnsSplit(matrix, list); print(childComponents); if
		 * (splitIndex <= 0) { leftColumnItems = new ADynamicSparseList();
		 * rightColumnItems = list; return; } if (splitIndex >= list.size()) {
		 * leftColumnItems = list; rightColumnItems = new ADynamicSparseList();
		 * return; } leftColumnItems = new ADynamicSparseList(); boolean
		 * leftNull = true; for (int i = 0; i < splitIndex; i ++) { Object item
		 * = list.get(i); if (item != null) leftNull = false;
		 * leftColumnItems.set( i, item); } if (leftNull) { leftColumnItems =
		 * new ADynamicSparseList(); } rightColumnItems = new
		 * ADynamicSparseList(); boolean rightNull = true; for (int i =
		 * splitIndex; i < list.size(); i ++) { Object item = list.get(i); if
		 * (item != null) rightNull = false; rightColumnItems.set(i -
		 * splitIndex, item); } if (rightNull) { rightColumnItems = new
		 * ADynamicSparseList(); }
		 */

	}

	void trimChildComponentRows() {
		int upRowNum = upperRowItems.size();
		int lowerRowNum = lowerRowItems.size();
		if (childComponents.numRows() > upRowNum) {
			for (int i = 0; i < upRowNum; i++)
				childComponents.removeRow(0);
		}
		int translatedLowerRowNum = lowerRowNum - upRowNum;
		for (int i = translatedLowerRowNum; i < childComponents.numRows(); i++)
			childComponents.removeRow(translatedLowerRowNum);

	}

	void trimChildComponentColumns() {
		int leftColumnNum = leftColumnItems.size();
		int rightColumnNum = rightColumnItems.size();
		if (childComponents.numCols() > leftColumnNum) {
			for (int i = 0; i < leftColumnNum; i++)
				childComponents.removeColumn(0);
		}
		int translatedRightColNum = rightColumnNum - leftColumnNum;
		for (int i = translatedRightColNum; i < childComponents.numCols(); i++)
			childComponents.removeColumn(translatedRightColNum);

	}

	VirtualContainer createColumnPanelGridLayout(ADynamicSparseList list,
			int curColNum, int nextEmptyCol, boolean thereExistsLabelAbove,
			boolean thereExistsLabelBelow) {
		if (list == null || nextEmptyCol == curColNum)
			return null;		
		VirtualContainer colPanel;
		if (getObjectAdapter().getStretchableByParent() && rowItems.size() == 0)
			colPanel = boundComponentsPanel;
		else
			colPanel = SwingPanelFactory.createPanelStatic();
		int numSubRows = 1;
		if (thereExistsLabelAbove)
			numSubRows++;
		if (thereExistsLabelBelow)
			numSubRows++;
		setLayout(getObjectAdapter(), colPanel, numSubRows, nextEmptyCol
				- curColNum);
		if (thereExistsLabelAbove) {
			for (int i = curColNum; i < nextEmptyCol; i++) {

				// three loops
				// / add all cols aboves
				// add cols
				// add all cols below
				Object colItem = list.get(i);

				add(colPanel, createTopLabel(colItem));
			}
		}
		for (int i = curColNum; i < nextEmptyCol; i++) {

			Object colItem = list.get(i);
			add(colPanel, getComponent(colItem));
		}
		if (thereExistsLabelBelow) {

			for (int i = curColNum; i < nextEmptyCol; i++) {

				// three loops
				// / add all cols aboves
				// add cols
				// add all cols below
				Object colItem = list.get(i);

				add(colPanel, createBottomLabel(colItem));
			}
		}

		return colPanel;
	}

	VirtualContainer createColumnsPanelBorderLayout(ADynamicSparseList list,
			int curColNum, int nextEmptyCol, boolean thereExistsLabelAbove,
			boolean thereExistsLabelBelow) {
		if (list == null || nextEmptyCol == curColNum)
			return null;
		if (rowItems.size() == 0)
			return createColumnsPanelBorderLayoutNoRows(list, curColNum, nextEmptyCol, thereExistsLabelAbove, thereExistsLabelBelow);
		//VirtualContainer colPanel = SwingPanelFactory.createJPanel();
		VirtualContainer colPanel;

	    if (getObjectAdapter().getStretchableByParent() && rowItems.size() == 0 ) {
	    	colPanel = boundComponentsPanel;
	    }
	    else {
		colPanel = PanelSelector.createPanel();
	    colPanel.setName("Columns Panel: " + getObjectAdapter().toText());
	    
	    }
		setColors(colPanel);

		setColumnLayout(getObjectAdapter(), colPanel, nextEmptyCol - curColNum);
		
		for (int i = curColNum; i < nextEmptyCol; i++) {
			Object colItem = list.get(i);
			if (thereExistsLabelAbove || thereExistsLabelBelow) {

				VirtualContainer borderPanel = PanelSelector.createPanel();
				setColors(borderPanel);
				borderPanel.setLayout(new BorderLayout());
				colPanel.add(borderPanel);
				borderPanel.add(getComponent(colItem));
				if (thereExistsLabelAbove) {
					borderPanel
							.add(createTopLabel(colItem), BorderLayout.NORTH);
				}
				if (thereExistsLabelBelow) {
					borderPanel.add(createBottomLabel(colItem),
							BorderLayout.SOUTH);
				}

			} else {
				add(colPanel, getComponent(colItem));
			}

		}

		return colPanel;
	}

	void fillColumns(VirtualContainer parent, int numRows/*
														 * , boolean
														 * stretchColumns
														 */) {
		removeOverlappingColumns(childComponents, columnItems);
		int prevColNum = 0;
		int curColNum = columnItems.firstFilledSlot();
		int numGridCols = 0;
		int numGridRows = 1;
		boolean thereExistsLabelAbove = thereExistsLabelAbove(columnItems);
		boolean thereExistsLabelBelow = thereExistsLabelBelow(columnItems);
		if (thereExistsLabelAbove)
			numGridRows++;
		if (thereExistsLabelBelow)
			numGridRows++;
		/*
		 * int numComponents = rowItems.numFilledElements()*2; int
		 * actualComponents = 0; if (curRowNum > 0) numComponents ++; if
		 * (rowItems.lastFilledSlot() >= childComponents.numRows())
		 * numComponents --;
		 */
		// setLayout (this.getObjectAdapter(), parent, numComponents, 1);
		// parent.setLayout (new uiGridLayout(numComponents, 1));
		if (curColNum == 0) {
			add(parent, columnItems.get(curColNum));
			numGridCols++;
			prevColNum = curColNum;
			curColNum = columnItems.firstFilledSlot(prevColNum);
		}
		while (true) {
			// int nextRowNum = rowItems.firstFilledSlot(curRowNum + 1);
			if (curColNum > prevColNum) {
				VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
				setLayout(getObjectAdapter(), matrixPanel, numRows, curColNum
						- prevColNum);
				/*
				 * if (stretchColumns) fillPanelStretchColumns(matrixPanel, 0,
				 * prevColNum, numRows, curColNum - prevColNum); else
				 */
				fillPanel(matrixPanel, 0, prevColNum, numRows, curColNum
						- prevColNum);
				/*
				 * if (thereExistsLabelLeft) parent.add(filler());
				 */
				parent.add(matrixPanel);
				/*
				 * if (thereExistsLabelRight) parent.add(filler());
				 */

				numGridCols++;
				int nextEmptyCol = columnItems.firstEmptySlot(curColNum);
				if (nextEmptyCol == curColNum + 1 && !thereExistsLabelAbove
						&& !thereExistsLabelBelow)
					add(parent, columnItems.get(curColNum));
				else {
					VirtualContainer colPanel = createColumnPanelGridLayout(
							columnItems, curColNum, nextEmptyCol,
							thereExistsLabelAbove, thereExistsLabelBelow);

					add(parent, colPanel);
					curColNum = nextEmptyCol - 1;
				}
				numGridRows++;
			}
			prevColNum = curColNum;
			curColNum = columnItems.firstFilledSlot(prevColNum + 1);
			if (curColNum == -1)
				break;
		}
		if (prevColNum < childComponents.numCols() - 1) {
			VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
			fillPanel(matrixPanel, 0, prevColNum, numRows, childComponents
					.numCols()
					- prevColNum);
			parent.add(matrixPanel);
			numGridCols++;
		}
		setLayout(this.getObjectAdapter(), parent, 1, numGridCols);
		// parent.setLayout(new uiGridLayout(numGridRows, 1));
		/*
		 * if (numComponents != actualComponents) System.out.println("assertion
		 * error");
		 */

	}

	public VirtualContainer createRowPanelGridLayout(ADynamicSparseList list,
			int curRowNum, int nextEmptyRow, boolean thereExistsLabelLeft,
			boolean thereExistsLabelRight) {
		if (list == null || curRowNum == nextEmptyRow)
			return null;
		VirtualContainer rowPanel = SwingPanelFactory.createPanelStatic();
		int numSubCols = 1;
		if (thereExistsLabelLeft)
			numSubCols++;
		if (thereExistsLabelRight)
			numSubCols++;
		setLayout(getObjectAdapter(), rowPanel, nextEmptyRow - curRowNum,
				numSubCols);
		for (int i = curRowNum; i < nextEmptyRow; i++) {
			Object rowItem = list.get(i);
			if (thereExistsLabelLeft && rowItem instanceof ObjectAdapter)
				// add (rowPanel, createLeftLabel(getLabelLeft(rowItem)));
				add(rowPanel, createLeftLabel(rowItem));
			add(rowPanel, getComponent(rowItem));
			if (thereExistsLabelRight)
				add(rowPanel, createRightLabel(rowItem));
		}
		return rowPanel;

	}

	public VirtualContainer createRowsPanelBorderLayout(ADynamicSparseList list,
			int curRowNum, int nextEmptyRow, boolean thereExistsLabelLeft,
			boolean thereExistsLabelRight) {
		if (list == null || curRowNum == nextEmptyRow)
			return null;
		if (columnItems.size() == 0) {
			return createRowsPanelBorderLayoutNoColumns(list, curRowNum, nextEmptyRow, thereExistsLabelLeft, thereExistsLabelRight);
		}
		//VirtualContainer rowPanel = SwingPanelFactory.createJPanel();
		
		VirtualContainer rowsPanel;
		if (columnItems.size() == 0 && getObjectAdapter().getStretchableByParent())
			rowsPanel = boundComponentsPanel;
		else {
		rowsPanel = PanelSelector.createPanel();
		setColors(rowsPanel);
		rowsPanel.setName("Rows Panel:" + getObjectAdapter().toText());
		}
		
		/*
		 * int numSubCols = 1; if (thereExistsLabelLeft) numSubCols++; if
		 * (thereExistsLabelRight) numSubCols++;
		 */
		// setLayout(getObjectAdapter(), rowPanel, nextEmptyRow - curRowNum, 1);
		//setLayoutRow(getObjectAdapter(), rowPanel, nextEmptyRow - curRowNum);
		setLayoutColumn(getObjectAdapter(), rowsPanel, nextEmptyRow - curRowNum);

		for (int i = curRowNum; i < nextEmptyRow; i++) {
			VirtualContainer borderPanel = null;

			Object rowItem = list.get(i);
			if (thereExistsLabelLeft || thereExistsLabelRight) {
				borderPanel = PanelSelector.createPanel();
				setColors(borderPanel);
				borderPanel.setName("border panel:" + rowItem);
				borderPanel.setLayout(new BorderLayout());
				rowsPanel.add(borderPanel);
				borderPanel.add(getComponent(rowItem));
				if (thereExistsLabelLeft)
					borderPanel
							.add(createLeftLabel(rowItem), BorderLayout.WEST);
				if (thereExistsLabelRight)
					borderPanel.add(createRightLabel(rowItem),
							BorderLayout.EAST);

			} else {
				add(rowsPanel, getComponent(rowItem));
			}
			/*
			 * if (thereExistsLabelLeft && rowItem instanceof uiObjectAdapter)
			 * //add (rowPanel, createLeftLabel(getLabelLeft(rowItem))); add
			 * (rowPanel, createLeftLabel(rowItem)); add (rowPanel,getComponent(
			 * rowItem)); if (thereExistsLabelRight) add (rowPanel,
			 * createRightLabel(rowItem));
			 */
		}
		return rowsPanel;

	}
	public VirtualContainer createRowsPanelBorderLayoutNoColumns(ADynamicSparseList list,
			int curRowNum, int nextEmptyRow, boolean thereExistsLabelLeft,
			boolean thereExistsLabelRight) {
		
		//VirtualContainer rowPanel = SwingPanelFactory.createJPanel();
		VirtualContainer rowsPanel;
		if ( getObjectAdapter().getStretchableByParent())
			rowsPanel = boundComponentsPanel;
		else {
		rowsPanel = PanelSelector.createPanel();
		setColors(rowsPanel);
		rowsPanel.setName("Rows Panel:" + getObjectAdapter().toText());
		}
		VirtualContainer rowItemsPanel = rowsPanel;
		if (thereExistsLabelLeft || thereExistsLabelRight) {
			rowItemsPanel = PanelSelector.createPanel();
			setColors(rowItemsPanel);
			rowItemsPanel.setName("Row Items Panel: " + getObjectAdapter());
			rowsPanel.setLayout(new BorderLayout());
			rowsPanel.add(rowItemsPanel, BorderLayout.CENTER);
		}
		if (rowItemsPanel != boundComponentsPanel || 
				boundComponentsPanel.getLayout() instanceof OEGridLayout) // assume this means the default layout in instantiate component that has only one row and columnb
		setLayoutColumn(getObjectAdapter(), rowItemsPanel, nextEmptyRow - curRowNum);
		
		//VirtualContainer rowsPanel = boundComponentsPanel;
		if (thereExistsLabelLeft) {
		VirtualContainer leftLabelPanel = PanelSelector.createPanel();
		setColors(leftLabelPanel);
		leftLabelPanel.setName("Left Label Panel of" + getObjectAdapter().toText());
		fillLeftLabelColumn(leftLabelPanel);
//		if (!fillLeftLabelColumn(leftLabelPanel))
//			createEmptyColumnBoundary(leftLabelPanel);
			rowsPanel.add(leftLabelPanel, BorderLayout.WEST);
		}
		if (thereExistsLabelRight) {
			VirtualContainer rightLabelPanel = PanelSelector.createPanel();
			setColors(rightLabelPanel);
			rightLabelPanel.setName("Right Label Panel of" + getObjectAdapter());
			fillRightLabelColumn(rightLabelPanel);
//			if (!fillLeftLabelColumn(leftLabelPanel))
//				createEmptyColumnBoundary(leftLabelPanel);
				rowsPanel.add(rightLabelPanel, BorderLayout.EAST);
		}
		int numRows = nextEmptyRow - curRowNum;

		if (numRows <= 3 && adapter.isBorderLayout() && rowItemsPanel == boundComponentsPanel ) {
			
		
		if (numRows == 1) {
			Object element = rowItems.get(numRows);						
			Object constraint = getAddConstraint(element);
			if (constraint != null) {
				rowItemsPanel.add(getComponent(element), constraint);
			} else {
				rowItemsPanel.add(getComponent(element));
			}
				
//			colsPanel.add(getComponent(columnItems.get(0)));
		} else  {
			Object element1 = rowItems.get(curRowNum);						
			Object constraint1 = getAddConstraint(element1);
			Object element2 = rowItems.get(curRowNum + 1);						
			Object constraint2 = getAddConstraint(element2);
			if (constraint1 != null) {
				rowItemsPanel.add(getComponent(element1), constraint1);
			} else {
				rowItemsPanel.add(getComponent(element1), BorderLayout.NORTH);
			}
			if (constraint2 != null) {
				rowItemsPanel.add(getComponent(element2), constraint2);
			} else {
				rowItemsPanel.add(getComponent(element2));
			}
			if (numRows == 3) {
				Object element3 = rowItems.get(curRowNum + 2);
				Object constraint3 = getAddConstraint(element3);
				if (constraint3 != null) {
					rowItemsPanel.add(getComponent(element3), constraint3);
				} else {
					rowItemsPanel.add(getComponent(element3), BorderLayout.SOUTH);
				}
				
				
			}
		}}
		
		else {
		for (int i = curRowNum; i < nextEmptyRow; i++) {
			//VirtualContainer borderPanel = null;
			// special hack for BorderLayout			

			Object rowItem = list.get(i);
			//rowsPanel.add(getComponent(rowItem));
			

			add(rowItemsPanel, getComponent(rowItem));
			
			
			
		}
		}
		return rowsPanel;

	}
	
	VirtualContainer createColumnsPanelBorderLayoutNoRows(ADynamicSparseList list,
			int curColNum, int nextEmptyCol, boolean thereExistsLabelAbove,
			boolean thereExistsLabelBelow) {
		
		//VirtualContainer rowPanel = SwingPanelFactory.createJPanel();
		VirtualContainer colsPanel;
		if ( getObjectAdapter().getStretchableByParent())
			colsPanel = boundComponentsPanel;
		else {
			colsPanel = PanelSelector.createPanel();
			colsPanel.setName("Columns Panel:" + getObjectAdapter());
			setColors(colsPanel);
		}
		VirtualContainer colItemsPanel = colsPanel;
		if (thereExistsLabelAbove || thereExistsLabelBelow) {
			colItemsPanel = PanelSelector.createPanel();
			setColors(colItemsPanel);
			colItemsPanel.setName("Col Items Panel: " + getObjectAdapter());
			colsPanel.setLayout(new BorderLayout());
			colsPanel.add(colItemsPanel, BorderLayout.CENTER);
		}
		setLayoutRow(getObjectAdapter(), colItemsPanel, nextEmptyCol - curColNum);
		
		//VirtualContainer rowsPanel = boundComponentsPanel;
		if (thereExistsLabelAbove) {
		VirtualContainer topLabelPanel = PanelSelector.createPanel();
		setColors(topLabelPanel);
		topLabelPanel.setName("Top Label Panel:" + getObjectAdapter());
		fillTopLabelRow(topLabelPanel);
//		if (!fillLeftLabelColumn(topLabelPanel))
//			createEmptyColumnBoundary(topLabelPanel);
			colsPanel.add(topLabelPanel, BorderLayout.NORTH);
		}
		if (thereExistsLabelBelow) {
			VirtualContainer bottomLabelPanel = PanelSelector.createPanel();
			setColors(bottomLabelPanel);
			bottomLabelPanel.setName("Bottom Label Panel of" + getObjectAdapter());
			fillBottomLabelRow(bottomLabelPanel);
//			if (!fillTopLabelColumn(leftLabelPanel))
//				createEmptyColumnBoundary(leftLabelPanel);
				colsPanel.add(bottomLabelPanel, BorderLayout.SOUTH);
		}
		

		for (int i = curColNum; i < nextEmptyCol; i++) {
			//VirtualContainer borderPanel = null;

			Object colItem = list.get(i);
			//rowsPanel.add(getComponent(rowItem));
			add(colItemsPanel, getComponent(colItem));
			
			
		}
		return colsPanel;

	}

	/*
	 * public Container createRowPanelInSeparateRows(ADynamicSparseList list,
	 * int curRowNum, int nextEmptyRow , boolean thereExistsLabelLeft, boolean
	 * thereExistsLabelRight) { if (list == null || curRowNum == nextEmptyRow)
	 * return null; Container rowPanel = SwingPanelFactory.createJPanel(); int
	 * numSubCols = 1; if (thereExistsLabelLeft) numSubCols++; if
	 * (thereExistsLabelRight) numSubCols++; setLayout (getObjectAdapter(),
	 * rowPanel, nextEmptyRow - curRowNum, numSubCols); for (int i = curRowNum;
	 * i < nextEmptyRow; i++) { Object rowItem = list.get(i); if
	 * (thereExistsLabelLeft) add (rowPanel,
	 * createLeftLabel(getLabelLeft(rowItem))); add (rowPanel,getComponent(
	 * rowItem)); if (thereExistsLabelRight) add (rowPanel,
	 * createRightLabel(getLabelRight(rowItem))); } return rowPanel; }
	 */

	public void fillMatrixRow(VirtualContainer c, int rowNum) {
		if (rowNum > childComponents.numRows() - 1)
			return;
		for (int i = 0; i < childComponents.numCols(rowNum); i++) {
			add(c, childComponents.get(rowNum, i));
		}
		c.setLayout(new GridLayout(1, childComponents.numCols(rowNum)));

	}

	boolean hasLabel(int rowNum, String label, int startCol, int stopCol) {
		for (int i = startCol; i <= stopCol; i++)
			if (isLabel(childComponents.get(rowNum, i), label))
				return true;
		return false;
	}

	boolean hasTopLabel(int rowNum, int startCol, int stopCol) {
		return hasLabel(rowNum, TL, startCol, stopCol);
	}

	boolean hasBottomLabel(int rowNum, int startCol, int stopCol) {
		return hasLabel(rowNum, BL, startCol, stopCol);
	}

	void fillRowsSmartButNotUseful(VirtualContainer parent, int numCols,
			boolean stretchColumns) {
		removeOverlappingRows(childComponents, rowItems);
		int prevRowNum = 0;
		int curRowNum = rowItems.firstFilledSlot();
		int numGridRows = 0;
		int numGridCols = 1;
		// boolean thereExistsLabelLeft = thereExistsLabelLeft(rowItems);
		// boolean thereExistsLabelRight = thereExistsLabelRight(rowItems);
		boolean thereExistsLabelLeft = false;
		boolean thereExistsLabelRight = false;
		if (thereExistsLabelLeft)
			numGridCols++;
		if (thereExistsLabelRight)
			numGridCols++;
		/*
		 * int numComponents = rowItems.numFilledElements()*2; int
		 * actualComponents = 0; if (curRowNum > 0) numComponents ++; if
		 * (rowItems.lastFilledSlot() >= childComponents.numRows())
		 * numComponents --;
		 */
		// setLayout (this.getObjectAdapter(), parent, numComponents, 1);
		// parent.setLayout (new uiGridLayout(numComponents, 1));
		if (curRowNum == 0) {
			add(parent, rowItems.get(curRowNum));
			numGridRows++;
			prevRowNum = curRowNum;
			curRowNum = rowItems.firstFilledSlot(prevRowNum);
		}
		while (true) {
			// int nextRowNum = rowItems.firstFilledSlot(curRowNum + 1);

			if (curRowNum > prevRowNum) {
				VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
				// setLayout (getObjectAdapter(), matrixPanel, curRowNum -
				// prevRowNum, numCols);
				fillPanelEqualRows(matrixPanel, prevRowNum, 0, curRowNum
						- prevRowNum, numCols, stretchColumns);
				/*
				 * if (stretchColumns) fillPanelStretchColumns(matrixPanel,
				 * prevRowNum, 0, curRowNum - prevRowNum, numCols); else
				 * fillPanel (matrixPanel, prevRowNum, 0, curRowNum -
				 * prevRowNum, numCols);
				 */
				/*
				 * if (thereExistsLabelLeft) parent.add(filler());
				 */
				parent.add(matrixPanel);
				/*
				 * if (thereExistsLabelRight) parent.add(filler());
				 */

				numGridRows++;
				if (curRowNum >= rowItems.size()
						&& curRowNum >= childComponents.numRows())
					break;
				int nextEmptyRow = rowItems.firstEmptySlot(curRowNum);
				if (nextEmptyRow == curRowNum + 1 && !thereExistsLabelLeft
						&& !thereExistsLabelRight)
					add(parent, rowItems.get(curRowNum));
				else {
					VirtualContainer rowPanel = createRowPanelGridLayout(
							rowItems, curRowNum, nextEmptyRow,
							thereExistsLabelLeft, thereExistsLabelRight);
					/*
					 * Container rowPanel = SwingPanelFactory.createJPanel();
					 * int numSubCols = 1; if (thereExistsLabelLeft)
					 * numSubCols++; if (thereExistsLabelRight) numSubCols++;
					 * setLayout (getObjectAdapter(), rowPanel, nextEmptyRow -
					 * curRowNum, numSubCols); for (int i = curRowNum; i <
					 * nextEmptyRow; i++) { Object rowItem = rowItems.get(i); if
					 * (thereExistsLabelLeft) add (rowPanel,
					 * createLabel(getLabelLeft(rowItem))); add (rowPanel,
					 * rowItems.get(i)); if (thereExistsLabelRight) add
					 * (rowPanel, createLabel(getLabelRight(rowItem))); }
					 */
					/*
					 * Container parentPanel = SwingPanelFactory.createJPanel();
					 * parentPanel.add( rowPanel); parent.add( rowPanel);
					 */
					add(parent, rowPanel);
					curRowNum = nextEmptyRow - 1;
				}
				numGridRows++;
			}
			prevRowNum = curRowNum + 1;
			curRowNum = rowItems.firstFilledSlot(prevRowNum);
			if (prevRowNum >= childComponents.numRows() && curRowNum == -1)
				break;
			if (curRowNum == -1)
				curRowNum = childComponents.numRows();
			// if (curRowNum == -1) break;
		}
		if (prevRowNum < childComponents.numRows() - 1) {
			VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
			fillPanel(matrixPanel, prevRowNum, 0, childComponents.numRows()
					- prevRowNum, numCols);
			parent.add(matrixPanel);
			numGridRows++;
		}
		setLayout(this.getObjectAdapter(), parent, numGridRows, 1);
		// parent.setLayout(new uiGridLayout(numGridRows, 1));
		/*
		 * if (numComponents != actualComponents) System.out.println("assertion
		 * error");
		 */

	}

	void fillRows(VirtualContainer parent, int numCols, boolean unequalRows,
			boolean putLeftRightLabels) {
		// print (childComponents);
		removeOverlappingRows(childComponents, rowItems);
		// System.out.println("afterRemovingOverlapping rows");
		// print (childComponents);
		int prevRowNum = -1; // last item from rowItems filled
		//int prevRowNum = 0; // last item from rowItems filled
		int curRowNum = rowItems.firstFilledSlot();
		int numGridRows = 0; // number of total rows filled, used in grid layout
		int numGridCols = 1;
		
		// not sure what this is used for
		Vector<VirtualComponent> leftLabels = null;
		Vector<VirtualComponent> rightLabels = null;
		boolean thereExistsLeftLabel = false;
		boolean thereExistsRightLabel = false;
		if (!getObjectAdapter().getStretchRows()) {
			leftLabels = new Vector();
			rightLabels = new Vector();
			thereExistsLeftLabel = putLeftRightLabels
					&& getLeftLabelColumn(leftLabels);
			thereExistsRightLabel = putLeftRightLabels
					&& getRightLabelColumn(rightLabels);
		}

		if (!thereExistsLeftLabel)
			leftLabels = null;
		if (!thereExistsRightLabel)
			rightLabels = null;

		
		if (thereExistsLeftLabel)
			numGridCols++;
		if (thereExistsRightLabel)
			numGridCols++;
		
//		if (curRowNum == 0) {
//			add(parent, rowItems.get(curRowNum));
//			numGridRows++;
//			prevRowNum = curRowNum;
//			//curRowNum = rowItems.firstFilledSlot(prevRowNum);
//			curRowNum = rowItems.firstFilledSlot(prevRowNum + 1);
//		}
//		print(childComponents);
		while (true) {
			// int nextRowNum = rowItems.firstFilledSlot(curRowNum + 1);

			if (curRowNum > prevRowNum + 1) {
				
//				fillPanelEqualRows(parent, prevRowNum, 0, curRowNum
//						- prevRowNum, numCols, unequalRows, leftLabels,
//						rightLabels);
				fillPanelEqualRows(parent, prevRowNum + 1, 0, curRowNum
						- (prevRowNum + 1), numCols, unequalRows, leftLabels,
						rightLabels);
			}
				numGridRows += curRowNum - (prevRowNum + 1);
				
				//numGridRows += curRowNum - prevRowNum;
				

				// numGridRows++;
				if (curRowNum >= rowItems.size()
						&& curRowNum >= childComponents.numRows())
					break;
				//int nextEmptyRow = rowItems.firstEmptySlot(curRowNum);
				int nextEmptyRow = rowItems.firstEmptySlot(curRowNum + 1);
				/*
				 * if (nextEmptyRow == curRowNum + 1 && !thereExistsLabelLeft &&
				 * !thereExistsLabelRight) add (parent,
				 * rowItems.get(curRowNum)); else { Container rowPanel =
				 * createRowPanel(rowItems, curRowNum, nextEmptyRow,
				 * thereExistsLabelLeft, thereExistsLabelRight);
				 * 
				 * add (parent, rowPanel); curRowNum = nextEmptyRow - 1; }
				 * numGridRows++;
				 */
				for (int i = curRowNum; i < nextEmptyRow; i++) {
					// add (parent, rowItems.get(i));
					/*
					 * addLabelledRowItem(parent, rowItems.get(i),
					 * thereExistsLabelLeft, thereExistsLabelRight);
					 */
					addLabelledRowItem(parent, rowItems.get(i),
							thereExistsLeftLabel, thereExistsRightLabel);
					prevRowNum = i;
					numGridRows++;
				}
			//}
				curRowNum = rowItems.firstFilledSlot(prevRowNum + 1);
			//prevRowNum = curRowNum + 1;
//			prevRowNum = curRowNum;
//			curRowNum = rowItems.firstFilledSlot(prevRowNum + 1);
			if (prevRowNum >= childComponents.numRows() && curRowNum == -1)
				break;
			if (curRowNum == -1)
				curRowNum = childComponents.numRows();
			// if (curRowNum == -1) break;
		}
		if (prevRowNum < childComponents.numRows() - 1) {
			VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
			fillPanel(matrixPanel, prevRowNum, 0, childComponents.numRows()
					- prevRowNum, numCols);
			parent.add(matrixPanel);
			numGridRows++;
		}
		setLayout(this.getObjectAdapter(), parent, numGridRows, 1);
		// setLayout (this.getObjectAdapter(), )

	}

	void addLabelledRowItem(VirtualContainer parent, Object rowItem,
			boolean thereExistsLeftLabel, boolean thereExistsRightLabel) {

		if (!thereExistsLeftLabel && !thereExistsRightLabel) {
			add(parent, rowItem);
			return;
		}
		VirtualContainer childPanel = PanelSelector.createPanel();
		setColors(childPanel);
		childPanel.setName("child panel:" + rowItem);
		if (getObjectAdapter().getStretchRows()) {
			childPanel.setLayout(new GridLayout(1, 3));
		} else {
			childPanel.setLayout(new BorderLayout());
		}
		if (thereExistsLeftLabel) {
			// VirtualComponent leftLabel = createLabel(getLabelLeft(rowItem));
			VirtualComponent leftLabel = createLeftLabel(rowItem);
			childPanel.add(leftLabel, BorderLayout.WEST);

		}
		add(childPanel, rowItem);
		if (thereExistsRightLabel) {
			// VirtualComponent rightLabel =
			// createLabel(getLabelRight(rowItem));
			VirtualComponent rightLabel = createRightLabel(rowItem);
			childPanel.add(rightLabel, BorderLayout.EAST);

		}
		add(parent, childPanel);

	}

	public void createEmptyRowBoundary(VirtualContainer c) {
		int width = c.getWidth();
		if (width == 0)
			width = adapter.getEmptyBorderWidth();
		c.setSize(new VirtualDimension(width, adapter.getEmptyBorderHeight()));
		c.setPreferredSize(c.getSize());
		//c.setBackground(Color.blue);
	}

	public void createEmptyColumnBoundary(VirtualContainer c) {
		int height = c.getHeight();
		if (height == 0)
			height = adapter.getEmptyBorderHeight();
		c.setSize(new VirtualDimension(adapter.getEmptyBorderWidth(), height));
		c.setPreferredSize(c.getSize());
		//c.setBackground(Color.blue);
	}

	public void fillMatrix() {
		// boolean showBoundaryLabels = false;
		if (adapter.getShowBoundaryLabels()) {
			// if (showBoundaryLabels) {
			/*
			 * if (boundComponentsPanel == componentPanel &&
			 * (!getObjectAdapter().getStretchRows() ||
			 * objectAdapter.isTopDisplayedAdapter())) { VirtualContainer temp =
			 * boundComponentsPanel; boundComponentsPanel =
			 * PanelSelector.createPanel(); //temp.setLayout(new
			 * uiGridLayout(1,1,0, 0)); //temp.setLayout(new FlowLayout());
			 * temp.add(boundComponentsPanel); temp.setLayout(new
			 * uiGridLayout(1,1,0, 0)); temp.setLayout(new FlowLayout()); }
			 */
			if (getObjectAdapter().getStretchRows()) {
				fillMatrixLabelsExceptBoundaries(childComponents, true, true,
						true, true);
				VirtualContainer boundSubPanel = SwingPanelFactory
						.createPanelStatic();
				// boundSubPanel.setLayout(boundComponentsPanel.getLayout());
				// boundComponentsPanel.setLayout(new uiGridLayout(1, 1));
				// boundComponentsPanel.add(leftLabelPanel);
				// boundComponentsPanel.add(rightLabelPanel);
				fillPanel(boundSubPanel, 0, 0, childComponents.numRows(),
						childComponents.numCols());
				boundComponentsPanel.setLayout(new BorderLayout());
				boundComponentsPanel.add(boundSubPanel);
				// fillRows(boundComponentsPanel, childComponents.numCols() );
				// fillRows (boundSubPanel,childComponents.numCols(),
				// objectAdapter.getStretchColumns() );

				VirtualContainer leftLabelPanel = PanelSelector.createPanel();
				setColors(leftLabelPanel);
				leftLabelPanel.setName("Left Label Panel of" + getObjectAdapter());
				if (!fillLeftLabelColumn(leftLabelPanel))
					createEmptyColumnBoundary(leftLabelPanel);
				boundComponentsPanel.add(leftLabelPanel, BorderLayout.WEST);
				VirtualContainer rightLabelPanel = SwingPanelFactory
						.createPanelStatic();
				if (!fillRightLabelColumn(rightLabelPanel))
					createEmptyColumnBoundary(rightLabelPanel);
				boundComponentsPanel.add(rightLabelPanel, BorderLayout.EAST);
				VirtualContainer topLabelPanel = PanelSelector.createPanel();
				setColors(topLabelPanel);
				topLabelPanel.setName("Top Label Panel of" + getObjectAdapter());
				if (!fillTopLabelRow(topLabelPanel)) {
					createEmptyRowBoundary(topLabelPanel);
					// topLabelPanel.setSize(new Dimension
					// (topLabelPanel.getWidth(), 2));
					// topLabelPanel.setPreferredSize(new Dimension
					// (topLabelPanel.getWidth(), 3));
					// topLabelPanel.setPreferredSize(topLabelPanel.getSize());
				}
				boundComponentsPanel.add(topLabelPanel, BorderLayout.NORTH);
				VirtualContainer bottomLabelPanel = SwingPanelFactory
						.createPanelStatic();
				if (!fillBottomLabelRow(bottomLabelPanel)) {
					createEmptyRowBoundary(bottomLabelPanel);
					// bottomLabelPanel.setSize(new Dimension
					// (bottomLabelPanel.getWidth(), 2));
					// bottomLabelPanel.setPreferredSize(new Dimension
					// (bottomLabelPanel.getWidth(), 3));
					// bottomLabelPanel.setPreferredSize(bottomLabelPanel.getSize());
				}
				boundComponentsPanel.add(bottomLabelPanel, BorderLayout.SOUTH);
			} else {
				fillMatrixLabelsExceptEastWestBoundaries(childComponents, true,
						true, true, true);
				VirtualContainer boundSubPanel = PanelSelector.createPanel();
				setColors(boundSubPanel);
				boundSubPanel.setName("Bounded Components sub panel of" + getObjectAdapter());

				boundComponentsPanel.setLayout(new BorderLayout());
				boundComponentsPanel.add(boundSubPanel);
				Vector<VirtualComponent> leftLabels = new Vector();
				Vector<VirtualComponent> rightLabels = new Vector();
				boolean thereExistsLeftLabel = getLeftLabelColumn(leftLabels);
				boolean thereExistsRightLabel = getRightLabelColumn(rightLabels);
				if (!thereExistsLeftLabel)
					leftLabels = null;
				if (!thereExistsRightLabel)
					rightLabels = null;
				fillPanelInSeparateRows(boundSubPanel, 0, 0, childComponents
						.numRows(), childComponents.numCols(), leftLabels,
						rightLabels);
				// getLeftLabelColumn(), getRightLabelColumn());
				// need to handle top and bottom labels also
				VirtualContainer topLabelPanel = PanelSelector.createPanel();
				setColors(topLabelPanel);
				topLabelPanel.setName("Top Label Panel of" + getObjectAdapter());

				if (!fillTopLabelRow(topLabelPanel)) {
					createEmptyRowBoundary(topLabelPanel);
					// topLabelPanel.setSize(new Dimension
					// (topLabelPanel.getWidth(), 2));
					// topLabelPanel.setPreferredSize(new Dimension
					// (topLabelPanel.getWidth(), 3));
					// topLabelPanel.setPreferredSize(topLabelPanel.getSize());
				}
				boundComponentsPanel.add(topLabelPanel, BorderLayout.NORTH);
				VirtualContainer bottomLabelPanel = SwingPanelFactory
						.createPanelStatic();
				if (!fillBottomLabelRow(bottomLabelPanel)) {
					createEmptyRowBoundary(bottomLabelPanel);
					// bottomLabelPanel.setSize(new Dimension
					// (bottomLabelPanel.getWidth(), 2));
					// bottomLabelPanel.setPreferredSize(new Dimension
					// (bottomLabelPanel.getWidth(), 3));
					// bottomLabelPanel.setPreferredSize(bottomLabelPanel.getSize());
				}
				boundComponentsPanel.add(bottomLabelPanel, BorderLayout.SOUTH);
			}
			/**
			 * boundComponentsPanel.add(leftLabelPanel, BorderLayout.WEST);
			 * boundComponentsPanel.add(rightLabelPanel, BorderLayout.EAST);
			 * boundComponentsPanel.add(topLabelPanel, BorderLayout.NORTH);
			 * boundComponentsPanel.add(boundSubPanel);
			 */

			// fillPanel (boundComponentsPanel, 0, 0, childComponents.numRows(),
			// childComponents.numCols());
		} else {
			fillMatrixLabels(childComponents, true, true, true, true, true,
					true);
			// fillPanel (boundComponentsPanel, 0, 0, childComponents.numRows(),
			// childComponents.numCols());
			// not syure if this useof stretchColumns is right. seems in eithe
			// case fillers will be
			// addes. It is an issue of whether we want colums aligned. Maybe we
			// should
			// have separate parameter for that. Maybe not even that. I am not
			// sure
			// if wither choice makes a difference if the same grid layout is
			// used for
			// adding rows and columns.
			if (adapter.getStretchRows()
			// && !objectAdapter.getStretchColumns())
					&& adapter.getAlignCells())
				fillPanel(boundComponentsPanel, 0, 0,
						childComponents.numRows(), childComponents.numCols());
			else
				fillPanelInSeparateRows(boundComponentsPanel, 0, 0,
						childComponents.numRows(), childComponents.numCols());
		}
	}

	// does not align the labels
	public void fillRowsAndMatrixNoBoundaryLabels() {
		fillMatrixLabels(childComponents, true, true, true, true, true, true);
		fillRowLabels(rowItems, true, true);
		fillRows(boundComponentsPanel, childComponents.numCols(), false, true);

	}

	public void fillRowsAndMatrix() {
		// print(childComponents);
		// boolean showBoundaryLabels = objectAdapter.getStretchRows();
		if (adapter.getShowBoundaryLabels()) {
			// if (showBoundaryLabels) {
			fillMatrixLabelsExceptEastWestBoundaries(childComponents, true,
					true, true, true);
			// print(childComponents);
			// fillMatrixLabels(childComponents, true, true, true, true);
			// fillRowLabels (rowItems, true, true);
			// VirtualContainer boundSubPanel =
			// SwingPanelFactory.createJPanel();
			VirtualContainer boundSubPanel = boundComponentsPanel;
			if (getObjectAdapter().getStretchRows())
				boundSubPanel = SwingPanelFactory.createPanelStatic();
			// boundSubPanel.setLayout(boundComponentsPanel.getLayout());
			// boundComponentsPanel.setLayout(new uiGridLayout(1, 1));
			// boundComponentsPanel.add(leftLabelPanel);
			// boundComponentsPanel.add(rightLabelPanel);

			// fillRows(boundComponentsPanel, childComponents.numCols() );
			fillRowLabels(rowItems, true, true);
			/*
			 * fillRows(boundSubPanel, childComponents.numCols(), objectAdapter
			 * //.getStretchColumns(), false); .getStretchColumns(),
			 * !getObjectAdapter().getStretchRows());
			 */
			fillRows(boundSubPanel, childComponents.numCols(), !adapter
			// .getStretchColumns(), false);
					.getEqualRows(), !getObjectAdapter().getStretchRows());

			if (getObjectAdapter().getStretchRows()) {
				VirtualContainer leftLabelPanel = SwingPanelFactory
						.createPanelStatic();
				fillLeftLabelColumn(leftLabelPanel);
				VirtualContainer rightLabelPanel = SwingPanelFactory
						.createPanelStatic();
				fillRightLabelColumn(rightLabelPanel);
				boundComponentsPanel.setLayout(new BorderLayout());
				boundComponentsPanel.add(leftLabelPanel, BorderLayout.WEST);
				boundComponentsPanel.add(rightLabelPanel, BorderLayout.EAST);
				boundComponentsPanel.add(boundSubPanel);
			}
		} else {
			fillRowsAndMatrixNoBoundaryLabels();
		}

	}

	public void fillColumnsAndMatrix() {
		fillMatrixLabels(childComponents, true, true, true, true, true, true);
		fillColumnLabels(columnItems, true, true);
		fillColumns(boundComponentsPanel, childComponents.numRows());

	}

	public void fillMatrixRowsAndColumns() {
		fillMatrixLabels(childComponents, true, true, true, true, true, true);
		splitOverlappingRows(childComponents, rowItems);
		trimChildComponentRows();
		fillRowLabels(upperRowItems, true, true);
		fillRowLabels(lowerRowItems, true, true);
		fillColumnLabels(columnItems, true, true);
		VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
		fillColumns(matrixPanel, childComponents.numRows());
		boolean thereExistsLabelLeft = thereExistsLabelLeft(rowItems);
		boolean thereExistsLabelRight = thereExistsLabelRight(rowItems);
		VirtualContainer upperRowPanel = createRowPanelGridLayout(
				upperRowItems, 0, upperRowItems.size(), thereExistsLabelLeft,
				thereExistsLabelRight);
		VirtualContainer lowerRowPanel = createRowPanelGridLayout(
				lowerRowItems, 0, lowerRowItems.size(), thereExistsLabelLeft,
				thereExistsLabelRight);

		boundComponentsPanel.setLayout(new BorderLayout());
		if (upperRowPanel != null)
			boundComponentsPanel.add(upperRowPanel, BorderLayout.NORTH);
		boundComponentsPanel.add(matrixPanel, BorderLayout.CENTER);
		if (lowerRowPanel != null)
			boundComponentsPanel.add(lowerRowPanel, BorderLayout.SOUTH);

	}
	
	public VirtualContainer maybeAddBoundary(VirtualContainer component) {
		return component;
//		if (getObjectAdapter().getStretchableByParent())
//			return component;
//		VirtualContainer boundaryPanel = PanelSelector.createPanel();
//		boundaryPanel.setName("Panel with Boundary:" + getObjectAdapter().toText());
//		
//		boundaryPanel.setLayout(new BorderLayout());
//		boundaryPanel.add(component, BorderLayout.CENTER);
//		
//		VirtualContainer leftLabelBoundary = PanelSelector.createPanel();
//		//fillLeftLabelColumn(leftLabelBoundary);
//		leftLabelBoundary.setName("Left Label Boundary");
//		createEmptyColumnBoundary(leftLabelBoundary);
//		boundaryPanel.add(leftLabelBoundary, BorderLayout.WEST);
//		
//		VirtualContainer rightLabelBoundary = PanelSelector.createPanel();
//		//fillRightLabelColumn(rightLabelBoundary);
//
//		rightLabelBoundary.setName("Right Label Boundary");
//		createEmptyColumnBoundary(rightLabelBoundary);
//		boundaryPanel.add(rightLabelBoundary, BorderLayout.EAST);
//		
//		VirtualContainer topLabelBoundary = PanelSelector.createPanel();
//		//fillTopLabelRow(topLabelBoundary);
//		topLabelBoundary.setName("Top Label Boundary");
//		createEmptyRowBoundary(topLabelBoundary);
//		boundaryPanel.add(topLabelBoundary, BorderLayout.NORTH);
//		
//		VirtualContainer bottomLabelBoundary = PanelSelector.createPanel();
//		//fillBottomLabelRow(bottomLabelBoundary);
//
//		bottomLabelBoundary.setName("Bottom Label Boundary");
//		createEmptyColumnBoundary(bottomLabelBoundary);
//		boundaryPanel.add(bottomLabelBoundary, BorderLayout.SOUTH);
//		
//		return boundaryPanel;
		
		
		
	}

	public void fillPanel() {
		// System.out.println ("Matrix in fill panel");
		// print (childComponents);

		if (columnItems.numFilledElements() == 0
				&& rowItems.numFilledElements() == 0
				&& columnItems.numFilledElements() == 0) {
			fillMatrix();
			/*
			 * fillMatrixLabels(childComponents, true, true, true, true);
			 * fillPanel (boundComponentsPanel, 0, 0, childComponents.numRows(),
			 * childComponents.numCols());
			 */
			return;
		}
		// boolean nonEmptyMatrix = childComponents.numRows() != 0;
		if (childComponents.numRows() != 0) {
			if (columnItems.numFilledElements() == 0) {
				fillRowsAndMatrix();
				/*
				 * fillMatrixLabels(childComponents, true, true, true, true);
				 * fillRowLabels (rowItems, true, true);
				 * fillRows(boundComponentsPanel, childComponents.numCols() );
				 */
			} else if (rowItems.numFilledElements() == 0) {
				fillColumnsAndMatrix();
				/*
				 * fillMatrixLabels(childComponents, true, true, true, true);
				 * fillColumnLabels (columnItems, true, true);
				 * fillColumns(boundComponentsPanel, childComponents.numRows());
				 */
			} else if (getObjectAdapter().getRowsFullSize()) {
				fillMatrixRowsAndColumns();
				/*
				 * fillMatrixLabels(childComponents, true, true, true, true);
				 * splitOverlappingRows(childComponents, rowItems);
				 * trimChildComponentRows(); fillRowLabels (upperRowItems, true,
				 * true); fillRowLabels (lowerRowItems, true, true);
				 * fillColumnLabels (columnItems, true, true); Container
				 * matrixPanel = SwingPanelFactory.createJPanel();
				 * fillColumns(matrixPanel, childComponents.numRows()); boolean
				 * thereExistsLabelLeft = thereExistsLabelLeft(rowItems);
				 * boolean thereExistsLabelRight =
				 * thereExistsLabelRight(rowItems); Container upperRowPanel =
				 * createRowPanel(upperRowItems, 0, upperRowItems.size(),
				 * thereExistsLabelLeft, thereExistsLabelRight); Container
				 * lowerRowPanel = createRowPanel(lowerRowItems, 0,
				 * lowerRowItems.size(), thereExistsLabelLeft,
				 * thereExistsLabelRight);
				 * 
				 * boundComponentsPanel.setLayout(new BorderLayout()); if
				 * (upperRowPanel != null)
				 * boundComponentsPanel.add(upperRowPanel, BorderLayout.NORTH);
				 * boundComponentsPanel.add(matrixPanel, BorderLayout.CENTER);
				 * if (lowerRowPanel != null)
				 * boundComponentsPanel.add(lowerRowPanel, BorderLayout.SOUTH);
				 * //int rowSplit = overlappingRowsSplit(childComponents,
				 * rowItems)
				 */;
			} else {
				print(childComponents);
				fillMatrixLabels(childComponents, true, true, true, true, true,
						true);
				// print (childComponents);

				// doing this before overlap detection so a label maybe
				// separated from the widget
				fillColumnLabels(columnItems, true, true);
				/*
				 * //print (childComponents); fillColumnLabels
				 * (rightColumnItems, true, true);
				 */
				// print (childComponents);
				handleOverlappingColumns(childComponents, columnItems);
				print(childComponents);
				// trimChildComponentColumns();
				// print (childComponents);
				/*
				 * fillColumnLabels(leftColumnItems, true, true);
				 * 
				 * print (childComponents); fillColumnLabels (rightColumnItems,
				 * true, true); print (childComponents);
				 */
				fillRowLabels(rowItems, true, true);

				// print (childComponents);
				VirtualContainer matrixPanel = SwingPanelFactory.createPanelStatic();
				fillRows(matrixPanel, childComponents.numCols(), false, true);
				boolean thereExistsLabelAbove = thereExistsLabelAbove(columnItems);
				boolean thereExistsLabelBelow = thereExistsLabelBelow(columnItems);
				VirtualContainer leftColPanel = createColumnPanelGridLayout(
						leftColumnItems, 0, leftColumnItems.size(),
						thereExistsLabelAbove, thereExistsLabelBelow);
				VirtualContainer rightColPanel = createColumnPanelGridLayout(
						rightColumnItems, 0, rightColumnItems.size(),
						thereExistsLabelAbove, thereExistsLabelBelow);

				boundComponentsPanel.setLayout(new BorderLayout());
				if (leftColPanel != null)
					boundComponentsPanel.add(leftColPanel, BorderLayout.WEST);
				boundComponentsPanel.add(matrixPanel, BorderLayout.CENTER);
				if (rightColPanel != null)
					boundComponentsPanel.add(rightColPanel, BorderLayout.EAST);
				// int rowSplit = overlappingRowsSplit(childComponents,
				// rowItems);

			}

		} else { // no matrix, only rows and columns

			fillRowLabels(rowItems, true, true);
			fillColumnLabels(columnItems, true, true);
			// VirtualContainer matrixPanel = SwingPanelFactory.createJPanel();
			/*
			 * // actually it will never be 0! if (childComponents.numRows() >
			 * 0) fillColumns(matrixPanel, childComponents.numRows());
			 */
			VirtualContainer rowPanel = null;
			if (rowItems.size() > 0) {
				// must check strethible py parent as in colItems case
				boolean thereExistsLabelLeft = thereExistsLabelLeft(rowItems);
				boolean thereExistsLabelRight = thereExistsLabelRight(rowItems);
				// VirtualContainer rowPanel =
				// createRowPanelGridLayout(rowItems, 0,
				// rowItems.size(),
				// thereExistsLabelLeft, thereExistsLabelRight);
				rowPanel = createRowsPanelBorderLayout(rowItems, 0, rowItems
						.size(), thereExistsLabelLeft,
						thereExistsLabelRight);

//				if (getObjectAdapter().getStretchRows())
//					rowPanel = createRowPanelGridLayout(rowItems, 0, rowItems
//							.size(), thereExistsLabelLeft,
//							thereExistsLabelRight);
//				else
//					rowPanel = createRowPanelBorderLayout(rowItems, 0, rowItems
//							.size(), thereExistsLabelLeft,
//							thereExistsLabelRight);
			}
			VirtualContainer colsPanel = null;
			if (columnItems.size() > 0) {
				boolean thereExistsLabelAbove = thereExistsLabelAbove(columnItems);
				boolean thereExistsLabelBelow = thereExistsLabelBelow(columnItems);
				/*
				 * VirtualContainer colPanel =
				 * createColumnPanelGridLayout(columnItems, 0,
				 * columnItems.size(), thereExistsLabelAbove,
				 * thereExistsLabelBelow);
				 */
				// all this for the buttons around the increment decrement operation. Should really use layout factory,
				// forcing the if to be not taken
				if (/*getObjectAdapter().getStretchableByParent() &&*/ adapter.isBorderLayout()  && rowItems.size() == 0 && columnItems.size() <= 3 ) {
					if (getObjectAdapter().getStretchableByParent())
						colsPanel = boundComponentsPanel;	
					else {						
						colsPanel = PanelSelector.createPanel();
						colsPanel.setName("Columns Border Panel: " + getObjectAdapter());	
						setColors(colsPanel, getObjectAdapter());
					}
					colsPanel.setLayout(new BorderLayout());
					if (columnItems.size() == 1) {
						Object element = columnItems.get(0);						
						Object constraint = getAddConstraint(element);
						if (constraint != null) {
							colsPanel.add(getComponent(element), constraint);
						} else {
							colsPanel.add(getComponent(element));
						}
							
//						colsPanel.add(getComponent(columnItems.get(0)));
					} else  {
						Object element1 = columnItems.get(0);						
						Object constraint1 = getAddConstraint(element1);
						Object element2 = columnItems.get(1);						
						Object constraint2 = getAddConstraint(element2);
						if (constraint1 != null) {
							colsPanel.add(getComponent(element1), constraint1);
						} else {
							colsPanel.add(getComponent(element1), BorderLayout.WEST);
						}
						if (constraint2 != null) {
							colsPanel.add(getComponent(element2), constraint2);
						} else {
							colsPanel.add(getComponent(element2));
						}
						if (columnItems.size() == 3) {
							Object element3 = columnItems.get(2);
							Object constraint3 = getAddConstraint(element3);
							if (constraint3 != null) {
								colsPanel.add(getComponent(element3), constraint3);
							} else {
								colsPanel.add(getComponent(element3), BorderLayout.EAST);
							}
							
							
						}
					}
					
//					} else  {
//						colsPanel.add(getComponent(columnItems.get(0)), BorderLayout.WEST);
//						colsPanel.add(getComponent(columnItems.get(1)));
//						if (columnItems.size() == 3)
//							colsPanel.add(getComponent(columnItems.get(2)), BorderLayout.EAST);
//
//					}
					//return;
				} 
				else

				if (getObjectAdapter().getStretchRows()) {
					colsPanel = createColumnPanelGridLayout(columnItems, 0,
							columnItems.size(), thereExistsLabelAbove,
							thereExistsLabelBelow);
				} else {
					colsPanel = createColumnsPanelBorderLayout(columnItems, 0,
							columnItems.size(), thereExistsLabelAbove,
							thereExistsLabelBelow);
				}
			}
			
			
			if (colsPanel == boundComponentsPanel || rowPanel == boundComponentsPanel)
				return;		
			
			setColors(colsPanel);

			
			
			if (getObjectAdapter().getStretchableByParent()) {
				boundComponentsPanel.setLayout(new BorderLayout()); //{
			if (rowPanel != null) {
				rowPanel = maybeAddBoundary(rowPanel);
				boundComponentsPanel.add(rowPanel, getObjectAdapter()
						.getRowsPlacement());
			}
//			} else
//				boundComponentsPanel.add(rowPanel);
			// boundComponentsPanel.add(matrixPanel, BorderLayout.CENTER);
			 if (colsPanel != null) {
				 colsPanel = maybeAddBoundary(colsPanel);
				boundComponentsPanel.add(colsPanel, getObjectAdapter()
						.getColumnsPlacement());
			 }
			} else {
				if (rowPanel != null) {
					rowPanel = maybeAddBoundary(rowPanel);
					add(boundComponentsPanel, rowPanel);
				}
				if (colsPanel != null) {
					 colsPanel = maybeAddBoundary(colsPanel);
					add(boundComponentsPanel, colsPanel);
				}
			}
				
			// int rowSplit = overlappingRowsSplit(childComponents, rowItems);

		}

		/*
		 * int numRows = childComponents.numRows(); int numCols =
		 * childComponents.numCols(); boundComponentsPanel.setLayout(new
		 * GridLayout(numRows, numCols)); for (int i = 0; i <
		 * childComponents.numRows(); i++) { for (int j = 0; j < numCols; j++) {
		 * //Component c = childComponents[i][j]; Component c = getComponent(i,
		 * j); System.out.println ("adding component" + i + " " + j + " " +
		 * c.getClass() + "name" + c.getName()); boundComponentsPanel.add (c); }
		 * }
		 */

	}

	public void fillPanel(VirtualContainer boundComponentsPanel, int fromRow,
			int fromCol, int numRows, int numCols) {
		// int numRows = childComponents.numRows();
		// int numCols = childComponents.numCols();
		if (numRows == 0)
			return;

		setLayout(getObjectAdapter(), boundComponentsPanel, numRows, numCols);
		// boundComponentsPanel.setLayout(new uiGridLayout(numRows, numCols,
		// uiGridLayout.DEFAULT_HGAP, 0));
		// boundComponentsPanel.setLayout(new GridLayout(numRows, numCols));
		// for (int i = fromRow; i < childComponents.numRows(); i++) {
		for (int i = fromRow; i < fromRow + numRows; i++) {
			for (int j = fromCol; j < fromCol + numCols; j++) {
				// Component c = childComponents[i][j];
				VirtualComponent c = getComponent(i, j);
				// System.out.println ("adding component" + i + " " + j + " " +
				// c.getClass() + "name" + c.getName());
				if (c == null || c.getParent() != null)
					continue;

				boundComponentsPanel.add(c);
				if (childComponents.get(i, j) instanceof ObjectAdapter)
					((ObjectAdapter) childComponents.get(i, j))
							.getWidgetAdapter().setParentContainer(
									boundComponentsPanel);
			}

		}

	}

	// why not pass stretch cols to this method. It can decid
	public void fillPanelInSeparateRows(VirtualContainer parentPanel,
			int fromRow, int fromCol, int numRows, int numCols) {
		fillPanelInSeparateRows(parentPanel, fromRow, fromCol, numRows,
				numCols, null, null);
		/*
		 * //int numRows = childComponents.numRows(); //int numCols =
		 * childComponents.numCols(); if (numRows == 0) return;
		 * 
		 * //setLayout (getObjectAdapter(), parentPanel, numRows, numCols);
		 * //boundComponentsPanel.setLayout(new uiGridLayout(numRows, numCols,
		 * uiGridLayout.DEFAULT_HGAP, 0)); //boundComponentsPanel.setLayout(new
		 * GridLayout(numRows, numCols)); //parentPanel.setLayout(new
		 * GridLayout(numRows, 1)); // not sure if we need to set the layout
		 * here // looks like we do! setLayout (objectAdapter, parentPanel,
		 * numRows, 1); //for (int i = fromRow; i < childComponents.numRows();
		 * i++) { for (int i = fromRow; i < fromRow + numRows; i++) {
		 * //VirtualContainer rowPanel = SwingPanelFactory.createJPanel();
		 * VirtualContainer rowPanel = PanelSelector.createPanel();
		 * //rowPanel.setLayout(new GridLayout(1, numCols)); setLayout
		 * (objectAdapter, rowPanel, 1, numCols); int realNumCols = numCols; if
		 * (objectAdapter.getStretchColumns()) realNumCols =
		 * childComponents.numCols(i); //for (int j = fromCol; j < fromCol +
		 * numCols; j++) { for (int j = fromCol; j < fromCol + realNumCols; j++)
		 * { //Component c = childComponents[i][j]; VirtualComponent c =
		 * getComponent(i, j); //System.out.println("Adding :" + c + " at row" +
		 * i + " col" + j); //System.out.println ("adding component" + i + " " +
		 * j + " " + c.getClass() + "name" + c.getName()); if (c == null ||
		 * c.getParent() != null) continue;
		 * 
		 * //boundComponentsPanel.add (c); rowPanel.add(c); if
		 * (childComponents.get(i, j) instanceof uiObjectAdapter)
		 * ((uiObjectAdapter) childComponents.get(i,
		 * j)).getWidgetAdapter().setParentContainer(parentPanel); }
		 * parentPanel.add(rowPanel); }
		 */

	}

	public void fillPanelInSeparateRows(VirtualContainer parentPanel,
			int fromRow, int fromCol, int numRows, int numCols,
			Vector<VirtualComponent> leftLabels,
			Vector<VirtualComponent> rightLabels) {
		// int numRows = childComponents.numRows();
		// int numCols = childComponents.numCols();
		if (numRows == 0)
			return;
		boolean thereExistsRightLabel = (rightLabels != null && rightLabels
				.size() > 0);
		boolean thereExistsLeftLabel = leftLabels != null
				&& leftLabels.size() >= 0;
		// int nextLabelIndex = fromRow;
		// why was this commented out
		 setLayout (getObjectAdapter(), parentPanel, numRows, numCols);
		// boundComponentsPanel.setLayout(new uiGridLayout(numRows, numCols,
		// uiGridLayout.DEFAULT_HGAP, 0));
		// boundComponentsPanel.setLayout(new GridLayout(numRows, numCols));
		// parentPanel.setLayout(new GridLayout(numRows, 1));
		// not sure if we need to set the layout here
		// looks like we do!
		//setLayout(adapter, parentPanel, numRows, 1);
		// for (int i = fromRow; i < childComponents.numRows(); i++) {
		for (int i = fromRow; i < fromRow + numRows; i++) {
			// VirtualContainer rowPanel = SwingPanelFactory.createJPanel();
			VirtualContainer rowPanel = PanelSelector.createPanel();
			setColors(rowPanel);
			rowPanel.setName("Row Panel " + i + " of " + getObjectAdapter().toDebugText());
			// rowPanel.setLayout(new GridLayout(1, numCols));
			// setLayout(objectAdapter, rowPanel, 1, numCols);
			setLayoutRow(adapter, rowPanel, numCols);
			//setLayoutRow(adapter, rowPanel, 1);
			int realNumCols = numCols;
			// if (objectAdapter.getStretchColumns())
			if (!adapter.getEqualRows())
				realNumCols = childComponents.numCols(i);
			// for (int j = fromCol; j < fromCol + numCols; j++) {
			for (int j = fromCol; j < fromCol + realNumCols; j++) {
				// Component c = childComponents[i][j];
				VirtualComponent c = getComponent(i, j);
				// System.out.println("Adding :" + c + " at row" + i + " col" +
				// j);
				// System.out.println ("adding component" + i + " " + j + " " +
				// c.getClass() + "name" + c.getName());
				if (c == null || c.getParent() != null)
					continue;

				// boundComponentsPanel.add (c);
				rowPanel.add(c);
				RowPanelComponentAdded.newCase(rowPanel, c, this);
//				System.out.println("Added: "  + c.getName() + " to " + rowPanel.getName());
				if (childComponents.get(i, j) instanceof ObjectAdapter)
					((ObjectAdapter) childComponents.get(i, j))
							.getWidgetAdapter().setParentContainer(parentPanel);
			}
			if (thereExistsLeftLabel || thereExistsRightLabel) {
				VirtualContainer borderPanel = PanelSelector.createPanel();
				setColors(borderPanel);
				borderPanel.setName("Border Panel ");
				borderPanel.setLayout(new BorderLayout());
				parentPanel.add(borderPanel);
				borderPanel.add(rowPanel);
				if (thereExistsLeftLabel)
					borderPanel.add(leftLabels.elementAt(i), BorderLayout.WEST);
				if (thereExistsRightLabel)
					borderPanel
							.add(rightLabels.elementAt(i), BorderLayout.EAST);
			} else {
//				if (rowPanel.getComponentCount() == 1) {
//					rowPanel.setLayout(new FlowLayout());
//				}
				parentPanel.add(rowPanel);
				RowPanelAdded.newCase(parentPanel, rowPanel, this);
//				System.out.println ("added rowPanel:" + rowPanel.getName() + " to:" + parentPanel.getName());
			}

		}

	}
	

	int firstNonBottomLabelRow(int rowNum, int stopRow, int startCol,
			int stopCol) {
		if (rowNum > stopRow)
			return rowNum; // ok to go beyond stopRow as we will subtract 1
		// later
		if (!hasBottomLabel(rowNum, startCol, stopCol))
			return rowNum;
		return firstNonBottomLabelRow(rowNum + 1, stopRow, startCol, stopCol);
	}

	int firstIndependentRow(int rowNum, int stopRow, int startCol, int stopCol) {
		int curRowNum = rowNum;
		while (true) {
			curRowNum++;
			if (curRowNum > stopRow)
				return curRowNum; // ok to go beyond stopRow as we will
			// subtract 1 later
			if (hasBottomLabel(curRowNum, startCol, stopCol))
				continue;
			if (curRowNum > rowNum
					&& hasTopLabel(curRowNum - 1, startCol, stopCol))
				continue;
			return curRowNum;
		}
	}

	void fillPanelEqualRows(VirtualContainer parentPanel, int fromRow,
			int fromCol, int numRows, int numCols, boolean unequalRows) {
		fillPanelEqualRows(parentPanel, fromRow, fromCol, numRows, numCols,
				unequalRows, null, null);
	}

	// strech columns prevents filling of fillers in empty cells.
	// so it should make a difference only when stretch rows is on
	void fillPanelEqualRows(VirtualContainer parentPanel, int fromRow,
			int fromCol, int numRows, int numCols, boolean unequalRows,
			Vector<VirtualComponent> leftLabels,
			Vector<VirtualComponent> rightLabels) {
		if (numRows == 0)
			return;
		int lastRow = fromRow + numRows - 1;
		int lastCol = fromCol + numCols - 1;
		int startRow = fromRow;
		int stopRow = fromRow;
		int numPanels = 0;
		/*
		 * Vector<VirtualComponent> leftLabels = null; Vector<VirtualComponent>
		 * rightLabels = null;
		 * 
		 * if (!getObjectAdapter().getStretchRows()) { leftLabels = new
		 * Vector(); rightLabels = new Vector(); boolean thereExistsLeftLabel =
		 * getLeftLabelColumn(leftLabels); boolean thereExistsRightLabel =
		 * getRightLabelColumn(rightLabels);
		 * 
		 * 
		 * if (!thereExistsLeftLabel) leftLabels = null; if
		 * (!thereExistsRightLabel) rightLabels = null; }
		 */
		while (true) {
			if (startRow > lastRow)
				break;
			if (hasTopLabel(startRow, fromCol, lastCol))
				stopRow++;
			// stopRow = firstIndependentRow (stopRow, lastRow, fromCol,
			// lastCol) - 1;
			stopRow = firstIndependentRow(startRow, lastRow, fromCol, lastCol) - 1;
			VirtualContainer childPanel;
			
			
		
			childPanel = parentPanel;
			// not sure why the two panels would not be the same!

			if (startRow == fromRow && stopRow == lastRow)
				;// childPanel = parentPanel;
			else {

				numPanels++;
				 childPanel = PanelSelector.createPanel();
				 setColors(childPanel);
				 parentPanel.add(childPanel);
				 childPanel.setName("Sub Panel: " + getObjectAdapter().toDebugText() + " " + 
							"StartRow " + startRow + "StopRow " + stopRow);
				 RowSubPanelAdded.newCase(parentPanel, childPanel, this);
				 
//				 System.out.println("added " + childPanel.getName() + " to " + parentPanel.getName());
			}

			// fillPanel (childPanel, startRow, fromCol, stopRow + 1 - startRow,
			// childComponents.maxCols(startRow, stopRow));
			if (unequalRows) {
				/*
				 * fillPanelInSeparateRows(childPanel, startRow, fromCol,
				 * stopRow + 1 - startRow, childComponents.maxCols(startRow,
				 * stopRow));
				 */
				fillPanelInSeparateRows(childPanel, startRow, fromCol, stopRow
						+ 1 - startRow, childComponents.maxCols(startRow,
						stopRow), leftLabels, rightLabels);

			} else {
				/*
				 * fillPanelInSeparateRows(childPanel, startRow, fromCol,
				 * stopRow + 1 - startRow, numCols);
				 */
				fillPanelInSeparateRows(childPanel, startRow, fromCol, stopRow
						+ 1 - startRow, numCols, leftLabels, rightLabels);
			}
			startRow = Math.max(startRow, stopRow) + 1;
			// startRow = stopRow + 1;

		}
		// the layout needs rows also so willhappen later
		/*
		 * if (numPanels > 0) parentPanel.setLayout(new GridLayout(numPanels,
		 * 1));
		 */

	}
	void fillPanelEqualRowsOld(VirtualContainer parentPanel, int fromRow,
			int fromCol, int numRows, int numCols, boolean unequalRows,
			Vector<VirtualComponent> leftLabels,
			Vector<VirtualComponent> rightLabels) {
		if (numRows == 0)
			return;
		int lastRow = fromRow + numRows - 1;
		int lastCol = fromCol + numCols - 1;
		int startRow = fromRow;
		int stopRow = fromRow;
		int numPanels = 0;
		/*
		 * Vector<VirtualComponent> leftLabels = null; Vector<VirtualComponent>
		 * rightLabels = null;
		 * 
		 * if (!getObjectAdapter().getStretchRows()) { leftLabels = new
		 * Vector(); rightLabels = new Vector(); boolean thereExistsLeftLabel =
		 * getLeftLabelColumn(leftLabels); boolean thereExistsRightLabel =
		 * getRightLabelColumn(rightLabels);
		 * 
		 * 
		 * if (!thereExistsLeftLabel) leftLabels = null; if
		 * (!thereExistsRightLabel) rightLabels = null; }
		 */
		while (true) {
			if (startRow > lastRow)
				break;
			if (hasTopLabel(startRow, fromCol, lastCol))
				stopRow++;
			// stopRow = firstIndependentRow (stopRow, lastRow, fromCol,
			// lastCol) - 1;
			stopRow = firstIndependentRow(startRow, lastRow, fromCol, lastCol) - 1;
			VirtualContainer childPanel;
			childPanel = parentPanel;
			// not sure why the two panels would not be the same!

			if (startRow == fromRow && stopRow == lastRow)
				;// childPanel = parentPanel;
			else {

				numPanels++;
				// childPanel = SwingPanelFactory.createJPanel();
				// parentPanel.add(childPanel);
			}

			// fillPanel (childPanel, startRow, fromCol, stopRow + 1 - startRow,
			// childComponents.maxCols(startRow, stopRow));
			if (unequalRows) {
				/*
				 * fillPanelInSeparateRows(childPanel, startRow, fromCol,
				 * stopRow + 1 - startRow, childComponents.maxCols(startRow,
				 * stopRow));
				 */
				fillPanelInSeparateRows(childPanel, startRow, fromCol, stopRow
						+ 1 - startRow, childComponents.maxCols(startRow,
						stopRow), leftLabels, rightLabels);

			} else {
				/*
				 * fillPanelInSeparateRows(childPanel, startRow, fromCol,
				 * stopRow + 1 - startRow, numCols);
				 */
				fillPanelInSeparateRows(childPanel, startRow, fromCol, stopRow
						+ 1 - startRow, numCols, leftLabels, rightLabels);
			}
			startRow = Math.max(startRow, stopRow) + 1;
			// startRow = stopRow + 1;

		}
		// the layout needs rows also so willhappen later
		/*
		 * if (numPanels > 0) parentPanel.setLayout(new GridLayout(numPanels,
		 * 1));
		 */

	}

	public void removeUnbound() {
		for (int i = 0; i < unboundProperties.size(); i++) {
			componentPanel.remove(getComponent(unboundProperties.elementAt(i)));
		}
		for (int i = 0; i < unboundButtons.size(); i++) {
			componentPanel.remove(getComponent(unboundButtons.elementAt(i)));
		}
	}

	public void removeUnboundFromComponentPanel() {
		for (int i = 0; i < unboundProperties.size(); i++) {
			componentPanel.remove(getComponent(unboundProperties.elementAt(i)));
		}
	}

	void processDirectionSinglePanel() {
		if (unboundProperties.size() == 0 && unboundButtons.size() == 0)
			processDirectionAddLabelsForUnbound();
		else {
			// removeUnbound();
			// processUnbound();
			// fillPanel (firstUnboundRow);
			// fillPanel (childComponents.numRows());
		}

	}

	public boolean processAttribute(Attribute attrib) {
		if (super.processAttribute(attrib))
			return true;
		else if (layoutManager == null)
			return false;
		else {
			if (PropertySetter.setProperty(layoutManager, attrib.getName(),
					attrib.getValue()))
				return true;
			else
				return false;
		}
	}

	boolean processAttributesCalled = false;

	public void processAttributes() {
		resetMyLabel();
		/*
		 * if (unboundColumnTitlePanelNotToBeAdded ||
		 * getObjectAdapter().hasOnlyGraphicsDescendents() )
		 * 
		 * return; //redisplayUnbound();
		 */
	}
	/*
	 * @Override public String toString() { if (adapter == null) return
	 * super.toString(); else return adapter.toString(); }
	 */

}