package bus.uigen.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import util.misc.Common;
import util.trace.uigen.WidgetShellComponentAdded;
import bus.uigen.AutomaticRefresh;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.WidgetAdapter;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.ADoubleClickMouseListener;
import bus.uigen.controller.ASelectionTriggerMouseListener;
import bus.uigen.controller.AnElideHandleMouseListener;
import bus.uigen.controller.SelectionManager;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.PrimitiveAdapter;
import bus.uigen.widgets.LabelSelector;
import bus.uigen.widgets.TextFieldSelector;
import bus.uigen.widgets.VirtualCheckBox;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualLabel;
import bus.uigen.widgets.VirtualScrollPane;
import bus.uigen.widgets.VirtualSlider;
import bus.uigen.widgets.VirtualTextField;
import bus.uigen.widgets.awt.AWTComponent;

public class AGenericWidgetShell implements
		java.io.Serializable /* JTabbedPane */, WidgetShell {
	// VirtualContainer virtualContainer;
	VirtualContainer container;

	// JLabel labelComponent;
	VirtualLabel labelComponent;
	// VirtualContainer labelContainer;
	// VirtualLabel labelContainer;

	// String label = "";
	String label;

	// JLabel elideHandle;
	VirtualLabel elideHandle;

	boolean elideHandleIsVisible = false;

	static final String INVISIBLE_LABEL = "";
	static final String EDITED_INVISIBLE_LABEL = "*";

	// static String uneditedLabel = "";
	String uneditedLabel = INVISIBLE_LABEL;

	// String editedInvisibleLabel = "*";
	// String editedInvisibleLabel = EDITED_INVISIBLE_LABEL;

	String editedLabel;

	// = editedInvisibleLabel;
	static Color EDITED_COLOR = Color.yellow;

	static Color OUT_OF_DATE_COLOR = Color.yellow;

	Object originalColor;

	// JLabel nullWidget;
	VirtualComponent nullWidget;

	VirtualComponent component;

	// protected JLabel elideComponent;
	// protected VirtualLabel elideComponent;
	// protected VirtualComponent elideComponent;
	protected VirtualComponent elideComponent;

	protected VirtualLabel labelWidget;

	String elideString = "", elideImage = "";

	protected boolean elided = true;

	ObjectAdapter adapter;

	boolean preRead = true;

	boolean preWrite = true;

	void setText(VirtualComponent c, String text) {
		if (c instanceof VirtualLabel)
			((VirtualLabel) c).setText(text);
		else if (c instanceof VirtualTextField)
			((VirtualTextField) c).setText(text);
	}

	public void addMouseListeners(VirtualComponent component) {
		RightMenuManager.bindToRightMenu(component, adapter);
		// component.addMouseListener(mouseAdapter);

		component.addMouseListener(new AnElideHandleMouseListener(this));
		component.addMouseListener(new ASelectionTriggerMouseListener(this
				.getObjectAdapter()));
		component.addMouseListener(new ADoubleClickMouseListener(
				getObjectAdapter(), getUIFrame()));
		if (component instanceof VirtualScrollPane) {
			addMouseListeners(((VirtualScrollPane) component)
					.getScrolledComponent());
		}

	}

	public static boolean checkMask(MouseEvent e, int mask) {
		int modifiers;
		// System.err.println("modifiers" + e.getModifiers() + "mask" + mask
		// +":");
		modifiers = e.getModifiers();
		if (modifiers == 0)
			modifiers = e.BUTTON1_MASK;
		return (modifiers & mask) == mask;
	}

	
	 

	// MouseAdapter mouseAdapter = new AMouseAdapter();
	// MouseAdapter mouseAdapter = new AGenericWidgetMouseAdapter(this);
	// MouseAdapter mouseAdapter = null;

	GridBagConstraints labelConstraints;

	GridBagConstraints componentConstraints = new GridBagConstraints();

	GridBagLayout gridBagLayout = new GridBagLayout();

	JLabel handle;

	boolean showBorder = true;

	boolean showHandles = true;
	Icon downIcon;
	Icon upIcon;
	static final String ELIDE_HANDLE_TEXT = ">>";
	static final String EXPAND_HANDLE_TEXT = "<<";

	void makeElideHandleLabel() {
		if (upIcon == null)
			getElideHandle().setText(ELIDE_HANDLE_TEXT);
		else
			getElideHandle().setIcon(upIcon);
	}

	void makeExpandHandleLabel() {
		if (elideHandle == null)
			return;
		if (downIcon == null)
			getElideHandle().setText(EXPAND_HANDLE_TEXT);
		else
			getElideHandle().setIcon(downIcon);
	}

	public AGenericWidgetShell() {
		/*
		 * try {
		 * 
		 * downIcon = new ImageIcon("dn.gif"); upIcon = new ImageIcon("up.gif");
		 * 
		 * } catch (Exception e) {
		 * 
		 * }
		 */

		// init (new JPanel());

		// put this in init
		// makeBorder();
	}

	/* public */void setMaximumSize(VirtualDimension d) {
		/*
		 * if (container instanceof JPanel) { ((JPanel)
		 * container).setMaximumSize(d); }
		 */
		container.setMaximumSize(d);
	}

	/* public */void setAlignmentX(float newVal) {
		/*
		 * if (container instanceof JPanel) { ((JPanel)
		 * container).setAlignmentX(newVal); }
		 */
		container.setAlignmentX(newVal);

	}

	/* public */void setAlignmentY(float newVal) {
		/*
		 * if (container instanceof JPanel) { ((JPanel)
		 * container).setAlignmentY(newVal); }
		 */
		container.setAlignmentY(newVal);
	}

	// boolean allowBorder = true;
	public void setAllowBorder(boolean newVal) {
		// allowBorder = newVal;
	}

	int i = 0;

	public boolean getAllowBorder() {
		if (adapter == null)
			return true;
		return adapter.getShowBorder();
		// return allowBorder;
	}

	public void setBorder(Border newVal) {
		// if (!getAllowBorder() || !isLabelVisible()) return;
		// if (!getAllowBorder() || container == null)
		if (container == null)
			return;
		
		container.setBorder(newVal);
		/*
		 * if (container instanceof JPanel) { ((JPanel)
		 * container).setBorder(newVal); }
		 */
	}

	String toolTipText;

	/*
	 * Font font; String fontName; Integer fontStyle; Integer fontSize;
	 */
	public void setObjectAdapter(ObjectAdapter newValue) {
		if (adapter == newValue)
			return;
		adapter = newValue;
		toolTipText = adapter.getToolTipText();
		// mouseAdapter = new AGenericWidgetMouseAdapter(this);
		/*
		 * font = adapter.getFont(); fontName = adapter.getFontName(); fontStyle
		 * = adapter.getFontStyle(); fontSize = adapter.getFontSize();
		 */
		// labelVisible = newValue.isLabelled();
		// setUIComponentValue will handle this
		// setEdited (listener.isEdited());
	}

	public ObjectAdapter getObjectAdapter() {
		return adapter;
	}

	public void elideHandle() {
		if (elideHandleIsVisible)
			hideElideHandle();
		else
			showElideHandle();
	}

	@Override
	public void hideElideHandle() {
		if (!elideHandleIsVisible)
			return;
		container.remove(getElideHandle());
		elideHandleIsVisible = false;
		getUIFrame().validate();
	}

	@Override
	public void showElideHandle() {
		if (elideHandleIsVisible)
			return;
		container.add(getElideHandle(), BorderLayout.EAST);
		elideHandleIsVisible = true;
		if (isElided())
			// elideHandle.setText("+");
			makeElideHandleLabel();
		else
			// elideHandle.setText("-");
			makeExpandHandleLabel();
		getUIFrame().validate();
	}

	boolean prevBorderEmpty = false;
	String prevBorderLabel;

	public void init(VirtualContainer theContainer) {
		/*
		 * virtualContainer = theContainer; if (! (virtualContainer instanceof
		 * AnAWTContainer)) return; //container = theContainer; container =
		 * (Container) ((AnAWTContainer) virtualContainer).getAWTComponent();
		 */
		// wait until attrs initialized
		// makeBorder(); // moving from cunstructor
		// Misc.setWidth(theContainer, 400);
		// Misc.setHeight(theContainer, 200);

		container = theContainer;
		// uiWidgetAdapter.setColors(container, getObjectAdapter());
		int componentCount = container.getComponentCount();
		if (componentCount != 0)
			System.err.println("non zero component count" + componentCount);
		// System.err.println ("Num container children:" +
		// container.getComponentCount());
		// widget = this;

		// this.setMinimumSize(new Dimension(50,50));
		// setMaximumSize(new Dimension(0,0));
		this.setMaximumSize(new VirtualDimension(0, 0));
		this.setAlignmentX(0);
		// container.setAlignmentX(0);
		this.setAlignmentY(0);
		// container.setAlignmentY(0);
		// elideHandle = new JLabel("+");
		// elideHandle = LabelSelector.createLabel("+");
		// if (upIcon != null)
		// elideHandle = LabelSelector.createLabel(upIcon);
		// else
		// elideHandle = LabelSelector.createLabel(EXPAND_HANDLE_TEXT);
		//
		// elideHandle.setName("Elide Handle " + getObjectAdapter() +
		// "(AGenericWidgetShell.init)");
		BorderLayout tightWidget = new BorderLayout(0, 0);
		tightWidget.setHgap(0);
		tightWidget.setVgap(0);

		// BorderLayout tightWidget = new BorderLayout();
		// this.setLayout(tightWidget);
		container.setLayout(tightWidget);
		// labelComponent = LabelSelector.createLabel("");
		// labelComponent.setName("Label Component " + getObjectAdapter() +
		// "(AGenericWidgetShell.init)");
		// labelComponent.setToolTipText(toolTipText);
		// Misc.setWidth(labelComponent, getObjectAdapter().getLabelWidth());
		// uiWidgetAdapter.setFont(labelComponent, getObjectAdapter());

		WidgetAdapter.setFont(container, getObjectAdapter());

		addMouseListeners(getContainer());
		maybeSetTooltipText(getContainer(), toolTipText);

		// this.getComponent().addMouseListener(mouseAdapter);
		// if (labelComponent != null) {
		//
		// addMouseListeners(labelComponent);
		// }

		// addMouseListeners(elideHandle);

		getObjectAdapter().getUIFrame().addKeyListener(getContainer());

		if (ObjectEditor.shareBeans() && ObjectEditor.coupleElides())
			ObjectRegistry.addWidget(this);
	}

	public void invalidate() {
		container.invalidate();
	}

	/* public */void add(VirtualComponent component) {
		
		if (component.getParent() == null) {
			WidgetShellComponentAdded.newCase(container, component, this);
//			System.err.println("adding " +  component.getName() + " to " + container.getName());
			container.add(component, BorderLayout.CENTER);

		}
		
	}

	/* public */void remove(VirtualComponent component) {
		if (container == null || component == null)
			return;
		container.remove(component);
	}

	public VirtualContainer getContainer() {
		return container;
	}

	/*
	 * public Container getPhysicalContainer() { return container; }
	 */
	public VirtualContainer getParent() {
		return container.getParent();
	}

	boolean labelAligned = false;

	// method is not called
	/* public */void alignLabel(int direction) {
		// remove(component);
		// remove(label);
		labelAligned = true;
		/*
		 * labelConstraints.anchor = direction; componentConstraints.gridx = 0;
		 * componentConstraints.gridy = 1; elideComponentConstraints.gridx = 0;
		 * elideComponentConstraints.gridy = 2;
		 * gridBagLayout.setConstraints(labelComponent, labelConstraints);
		 * gridBagLayout.setConstraints (component, componentConstraints);
		 * gridBagLayout.setConstraints (elideComponent,
		 * elideComponentConstraints);
		 */
		makeBorder();
		// doLayout();
		getContainer().doLayout();
		// System.err.println("aligning label" + labelConstraints.anchor);
		// add(label, labelConstraints);
		// add(component, componentConstraints);
	}

	/* public */void elideWithoutHandle() {
		elide();
		hideElideHandle();
	}

	public void elide() {
		if (internalElide()) {
			/*
			 * if ((elideComponent != null) && listener instanceof
			 * uiContainerAdapter) { if (component != null) remove(component);
			 * add(elideComponent); isElided = true;
			 */
			/*
			 * elideComponent.setVisible(true); component.setVisible(false);
			 */
			// labelComponent.setVisible(false);
			/*
			 * if (getParent() != null) getParent().doLayout();
			 */
			getUIFrame().validate();
		}

	}

	// public boolean isElided = true;
	static final int ELIDE_LENGTH = 60;

	public static String elideText(ObjectAdapter adapter, String elideString) {
		/*
		 * if (adapter == null || adapter.getElideStringIsToString()) return
		 * elideString;
		 * 
		 * String textLine = adapter.toTextLine();
		 */
		String textLine = elideString;
		if (textLine.length() > ELIDE_LENGTH)
			return textLine.substring(0, ELIDE_LENGTH) /* + "..." */;
		else
			return textLine;
		// return textRep;
	}

	/* public */String elideText(ObjectAdapter adapter) {
		return elideText(adapter, elideString);
		/*
		 * if (adapter == null) return elideString;
		 * 
		 * String textLine = adapter.toTextLine(); if (textLine.length() >
		 * ELIDE_LENGTH) return textLine.substring(0, ELIDE_LENGTH) else return
		 * textLine; //return textRep;
		 */
	}

	public boolean internalElideWithoutHandle() {
		boolean retVal = internalElide();
		hideElideHandle();
		return retVal;
	}

	public boolean internalElide() {
		// explicitElided = true;
		if (explicitExpanded)
			return false;

		return forceInternalElide();

	}

	/* public */boolean forceInternalElide() {
		// if ((elideComponent != null) && !elideComponent.isVisible()) {
		// System.err.println("internal elide");

		elideComponent = getElideComponent();
		if (elideComponent == null)
			return false;
		if (!elided || elideComponent.getParent() == null) {
			elided = true;
			if (expandedElideString) {
				// System.err.println("expanded elide string");
				restoreElideString();
				return true;
			}
			// System.err.println(elideString + elideComponent);
			if (component != null)
				remove(component);
			// component.setVisible(false);
			// elideComponent.setVisible(true);
			if (adapter != null && elideComponent != null) {
				
				refreshElideComponent();
//				String newElideString = adapter.getElideString();
//				if (!elideString.equals(newElideString)) {
//					elideString = newElideString;
//					setText(elideComponent, elideString);
//				}
				// this.setBorder(null);
			}
			add(elideComponent);
			// Misc.setSize(elideComponent, adapter.getElideComponentWidth(),
			// 33);
			// elideHandle.setText("+");
			makeElideHandleLabel();
			// showElideHandle();

			makeBorder();
			return (true);
		} else
			return false;
		// labelComponent.setVisible(false);
		/*
		 * if (getParent() != null) getParent().doLayout(); else
		 * System.err.println ("null parent");
		 */

	}

	public void expand() {
		if (!ObjectEditor.shareBeans()) {
			subExpand();
		} else {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this));
			} else {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(adapter
						.getPath()));
			}
		}
	}

	public void subExpand() {
		if (internalExpand()) {
			/*
			 * 
			 * //if ((elideComponent != null) && elideComponent.isVisible()) {
			 * if ((elideComponent != null) && isElided) {
			 * remove(elideComponent); if (component != null) add(component);
			 * isElided = false;
			 */
			/*
			 * elideComponent.setVisible(false); component.setVisible(true);
			 */
			// labelComponent.setVisible(true);
			// getUIFrame(this).validate();
			getUIFrame().validate();
			/*
			 * if (getParent() != null) getParent().doLayout();
			 */
		}

	}

	boolean expandedElideString = false;

	/* public */String expandedElideString() {
		if (adapter == null)
			return null;
		if ((adapter.isRecursive() && !(adapter instanceof PrimitiveAdapter)))
			return "<Recursive>";
		if (adapter.getRealObject() == null)
			return "<Unspecified (null)>";
		// if (adapter instanceof uiVectorAdapter
		// &&
		// // ((uiContainerAdapter) listener).childrenCreated() &&
		// ((uiContainerAdapter) adapter).getChildAdapterCount() == 0
		// &&
		// EditorRegistry.getEditorClass(RemoteSelector.getClass(adapter.computeAndMaybeSetViewObject()))
		// == null)
		// // && (uiBean.toVector( listener.getRealObject())).size() == 0)
		// return "<Empty>";
		// if (adapter instanceof uiHashtableAdapter
		// &&
		// // ((uiContainerAdapter) listener).childrenCreated() &&
		// ((uiContainerAdapter) adapter).getChildAdapterCount() == 0
		// &&
		// EditorRegistry.getEditorClass(RemoteSelector.getClass(adapter.computeAndMaybeSetViewObject()))
		// == null)
		// // && (uiBean.toHashtable( listener.getRealObject())).size() == 0)
		// return "<Empty>";
		/*
		 * if (listener instanceof uiVectorAdapter && !((uiContainerAdapter)
		 * listener).getChildren().hasMoreElements()) { return "<Out of Date>";
		 * }
		 */
		/*
		 * if (listener instanceof uiClassAdapter && ((uiClassAdapter)
		 * listener).childrenCreated() && !((uiClassAdapter)
		 * listener).getChildren().hasMoreElements())
		 */
		if (adapter.hasNoProperties() && !adapter.hasCommands())
			return "<No Properties>";

		return null;
	}

	String expandString = null;

	/* public */void expandElideString(String s) {
		expandedElideString = true;
		if (component == null)
			return;
		if (component.getParent() != null)
			remove(component);
		elideComponent = getElideComponent();
		if (elideComponent != null) {
			if (elideComponent.getParent() == null)
				add(elideComponent);
			// elideComponent.setText(s);
			setText(elideComponent, s);
			expandString = s;
		}
		hideElideHandle();
	}

	/* public */void restoreElideString() {
		expandedElideString = false;
		if (elideString != null && elideComponent != null)
			// elideComponent.setText(elideString);
			setText(elideComponent, elideString);
	}

	/* public */void restoreComponent() {
		if (getObjectAdapter().hasOnlyGraphicsDescendents()) {
			CompositeAdapter parentAdapter = getObjectAdapter()
					.getParentAdapter();
			if (parentAdapter.getWidgetAdapter() == null)
				return;
			parentAdapter.getWidgetAdapter().remove(getObjectAdapter());
		}
		expandedElideString = false;
		expandString = null;
		if (elideComponent != null) {
			remove(elideComponent);
			add(component);
			restoreElideString();
		} else
			System.err.println("Elide component should not be null");
	}

	public boolean isElided() {
		return elided;
	}

	public boolean redoExpand() {
		if (component == null)
			return false;
		// System.err.println("redo expand");
		if (elided)
			return false;
		// unnecessarily was eliding empty vectors
		// if (getObjectAdapter() instanceof uiVectorAdapter)
		// return false;
		String s;
		if ((s = expandedElideString()) == null && expandedElideString) {
			restoreComponent();
			return true;
		}
		// System.err.println("nothing restored");
		if (s != null && s != expandString) {
			expandElideString(s);
			return true;
		}
		return false;

	}

	boolean explicitElided = false;
	boolean explicitExpanded = false;

	// called after all atributes have been set
	public boolean internalExpand() {
		if (explicitElided)
			return false;
		return forceInternalExpand();

	}

	public boolean expandElidedString() {
		String s = expandedElideString();
		if (s != null) {
			expandElideString(s);
			return true;
		}
		return false;
	}

	boolean attributesInitialized = false;

	public void processAttributes() {
		attributesInitialized = true;
		setObjectAdapterAttributes();
		// uiObjectAdapter gp = adapter.getGrandParentAdapter();
		/*
		 * int labelLength = listener.getLabelLength(); //if (labelLength != 0)
		 * Misc.setWidth(labelComponent, labelLength);
		 */
		setLabelWidth();

		showUnlabelledBorder = adapter.getShowUnlabelledBorder();
		/*
		 * if ((gp != null) && (gp.getSeparateUnboundTitles()) ||
		 * adapter.getColumnTitleStatus() ==
		 * uiObjectAdapter.ColumnTitleStatus.disabled) { labelFilter = true;
		 * nonEmptyBorder = true;
		 * setLabelWithoutChangingBorder(adapter.getLabel()); } else if
		 * (adapter.getColumnTitleStatus() ==
		 * uiObjectAdapter.ColumnTitleStatus.show) {
		 * showColumnTitle(adapter.columnTitle()); } else if
		 * (adapter.getColumnTitleStatus() ==
		 * uiObjectAdapter.ColumnTitleStatus.hide) { hideColumnTitle(); }
		 */

		if (adapter.getWidgetShellColumnTitleStatus() == ObjectAdapter.ColumnTitleStatus.hide) {
			hideColumnTitle();
		} else if (adapter.getWidgetShellColumnTitleStatus() == ObjectAdapter.ColumnTitleStatus.show) {
			showColumnTitle(adapter.columnTitle());
			// } else if (adapter.getParentAdapter() != null &&
			// adapter.getParentAdapter().isFlatTableRow() &&
			// adapter.getParentAdapter().hasFlatTableRowDescendent()) {
		} else if (adapter.getParentAdapter() != null
				&& adapter.isFlatTableRow()
				&& adapter.getParentAdapter().hasFlatTableRowDescendent()) {
			// adapter.getParentAdapter().isFlatRowAndHasFlatRowDescendents()) {
			labelFilter = false;
		} else {
			labelFilter = true;
			nonEmptyBorder = true;
			setLabelWithoutChangingBorder(adapter.getLabel());
		}

		makeBorder();
		if (adapter.getShowElideHandles()
				&& !(adapter instanceof PrimitiveAdapter))
			showElideHandle();
	}

	public static void formatLeftLabel(VirtualLabel label) {
		label.setHorizontalAlignment(SwingConstants.LEFT);
		// label.setVerticalAlignment(SwingConstants.BOTTOM);

	}

	public static void formatRightLabel(VirtualLabel label) {

	}

	public static void formatTopLabel(VirtualLabel label) {
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.TOP);
		// label.setBackground(Color.cyan);

	}

	public static void formatBottomLabel(VirtualLabel label) {

	}

	public void setLabelWidth() {
		if (labelComponent == null)
			return;
		OEMisc.setWidth(getLabelComponent(), adapter);
	}

	/* public */boolean forceInternalExpand() {
		// component.setVisible(true);
		// System.err.println("internal expand");
		/*
		 * String s; if ((s = expandedElideString()) == null &&
		 * expandedElideString) { restoreComponent(); return true; } if ( s !=
		 * null) { expandElideString(s); elided = false; return true; }
		 */
		// setObjectAdapterAttributes();

		if (!preRead)
			return false;
		if (adapter instanceof CompositeAdapter) {
			((CompositeAdapter) adapter).createChildrenPropagating();
			if (adapter.hasNoComponents() 
					&& !adapter.isTopAdapter()
					&& adapter.getElideIfNoComponents()
					) {
				internalElide();
				return false;
			}
		}
		
		if (redoExpand())
			return true;
		// System.err.println("redo expand failed");
		if (!elided)
			return false;
		// System.err.println("not expanded");
		elided = false;
		if (expandElidedString())
			return true;

		if (elideComponent != null) {
			remove(elideComponent);
			// remove(labelComponent);
		}
		// elideHandle.setText("-");
		makeExpandHandleLabel();
		// System.err.println("not null elideComponent");
		try {
			if (component != null)
				add(component);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (adapter instanceof CompositeAdapter)
			((CompositeAdapter) adapter).expandPrimitiveChildren();
		makeBorder();
		// System.err.println("not null component");
		return true;
		/*
		 * if (elideComponent != null) elideComponent.setVisible(false);
		 */

		// labelComponent.setVisible(true);
		/*
		 * if (getParent() != null) getParent().doLayout();
		 */
	}

	static public boolean lastComponent(VirtualComponent c, VirtualContainer p) {
		return c == p.getComponent(p.getComponentCount() - 1);
	}

	@Override
	public void toggleElide() {
		if (!ObjectEditor.shareBeans()) {
			subToggleElide();
		} else {
			if (ObjectEditor.coupleElides()) {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(this,
						"toggleElide"));
			} else {
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(adapter
						.getPath(), "toggleElide"));
			}
		}
	}

	public void subToggleElide() {
		toggleInternalElide();
		/*
		 * if (elided) { internalExpand(); elided = false; } else {
		 * internalElide(); elided = true; }
		 */
		/*
		 * Container p = getParent(); if (p != null) { //specially process last
		 * component
		 * 
		 * if (lastComponent(this, p)) getUIFrame(this).doRefresh(); else
		 * p.doLayout(); }
		 */
		// getUIFrame(this).doRefresh();
		// System.err.println("about to validate");
		getUIFrame().validate();
	}

	/* public */void toggleInternalElide() {
		if (elided) {
			explicitExpanded = true;
			explicitElided = false;
			forceInternalExpand();
			elided = false;
		} else {
			explicitElided = true;
			explicitExpanded = false;
			internalElide();
			elided = true;
		}
	}

	public boolean isExplicitlyElided() {
		return explicitElided;
	}

	public String getLabel() {
		// return labelComponent.getText();
		// return label;
		if (label == null)
			setLabel(getObjectAdapter().getLabel());
		return label; // conditonal connect composers may not work
		// return uneditedLabel;
	}

	/* public */String getUneditedLabel() {
		// return labelComponent.getText();
		return uneditedLabel;
		// return uneditedLabel;
	}

	/* public */String getEditedLabel() {
		// return labelComponent.getText();
		return editedLabel;
		// return uneditedLabel;
	}

	/*
	 * public JLabel getLabelComponent() { return labelComponent; }
	 */
	void setUneditedLabel(String l) {
		/*
		 * if (l != null && l.equals(uneditedLabel)) return;
		 */
		/*
		 * if (l != null && l.equals("B") || l.equals(""))
		 * System.err.println("Where did this come from?");
		 */
		uneditedLabel = l;
		// if (l == null || !labelVisible)
		if (l == null || !isLabelVisible())
			// editedLabel = EDITED_INVISIBLE_LABEL;
			setEditedLabel(EDITED_INVISIBLE_LABEL);
		else
			// editedLabel = label + EDITED_INVISIBLE_LABEL;
			// editedLabel = label + EDITED_INVISIBLE_LABEL;
			setEditedLabel(label + EDITED_INVISIBLE_LABEL);

	}

	public void setLabel(String l) {
		setLabelWithoutChangingBorder(l);
		// System.err.println("setting label to" + l + ":");
		/*
		 * if (l == null && label != null) { setEditedLabel(l); return; } // if
		 * (label != l || label.equals(l)) return; borderVisible = true; if
		 * (label != null && label.equals(l)) return; // label = l;
		 * internalSetLabel(l); setEditedLabel(l); //adding this
		 * 
		 * 
		 * 
		 * // System.err.println(l+":"+l.equals("Name"));
		 */

		makeBorder();
		// labelComponent.setText(l);
	}

	void setLabelWithoutChangingBorder(String l) {
		// System.err.println("setting label to" + l + ":");
		if (l == null && label != null) {
			internalSetLabel(l);
			setUneditedLabel(l);
			return;
		}
		// if (label != l || label.equals(l)) return;
		nonEmptyBorder = true;
		if (label != null && label.equals(l))
			return;
		// label = l;
		internalSetLabel(l);
		setUneditedLabel(l);
		// adding this

		/*
		 * uneditedLabel = l; if (l == null || !labelVisible) editedLabel =
		 * editedInvisibleLabel; else editedLabel = label +
		 * editedInvisibleLabel;
		 */

		// System.err.println(l+":"+l.equals("Name"));

		// makeBorder();
		// labelComponent.setText(l);
	}

	boolean isEdited = false;

	public void setEdited(boolean edited) {
		// isEdited = edited;
		if (edited)
			setEdited();
		else
			setUpdated();
	}

	public void editedUIComponentFocusLost() {
		/*
		 * if (component != null) { System.err.println("changing edited color");
		 * component.setBackground(EDITED_COLOR); }
		 */
	}

	public void editedUIComponentFocusGained() {
		/*
		 * System.err.println("changing original color"); if (component != null)
		 * { component.setBackground(originalColor); }
		 */
	}

	boolean edited = false;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited() {
		// System.err.println("setting label to" + l + ":");
		if (!adapter.getShowBorder() && !isLabelVisible())
			return;
		edited = true;

		// label = editedLabel;
		internalSetLabel(editedLabel);

		/*
		 * if (!labelVisible) borderJustification = TitledBorder.LEFT;
		 */
		// System.err.println(l+":"+l.equals("Name"));

		makeBorder();
		// labelComponent.setText(l);
	}

	public void setUpdated() {
		// System.err.println("setting label to" + l + ":");
		if (!adapter.getShowBorder() && !isLabelVisible())
			return;
		// label = e
		edited = false;
		// setPreRead();
		setPreWrite();
		// label = uneditedLabel;
		// why this
//		if (component != null)
//			component.setBackground(originalColor);
		

		// makeColumnTitle should be setting this border
		// not when the user makes change
		if (label != null && label.equals(uneditedLabel))
			return;
		// label = uneditedLabel;
		internalSetLabel(uneditedLabel);
		makeBorder();
		// labelComponent.setText(l);
	}

	/* public */void setPreRead(boolean newVal) {
		if (preRead == newVal)
			return;
		preRead = newVal;
		if (preRead)
			internalExpand();
		else
			internalElide();
	}

	/* public */void setPreWrite(boolean newVal) {
		if (preWrite == newVal)
			return;
		preWrite = newVal;
		// this.setEnabled(preWrite);
		container.setEnabled(preWrite);
	}

	public void setPreRead() {
		if (adapter == null)
			return;
		setPreRead(adapter.getPreRead());
	}

	public void setPreWrite() {
		if (adapter == null)
			return;
		setPreWrite(adapter.getPreWrite());
	}

	public boolean isLabelVisible() {
		// return edited || (labelVisible && showColumnTitle);
		// return edited || (labelVisible && showColumnTitle);
		return (labelVisible && labelFilter);
		// return labelComponent.isVisible();
	}

	public boolean isBorderLabelVisible() {
		// return edited || (labelVisible && showColumnTitle);
		return edited || (labelVisible && labelFilter);

		// return labelComponent.isVisible();
	}

	public String toString() {
		if (getObjectAdapter() != null)
			// return getObjectAdapter().toString();
			return getObjectAdapter().toDebugText();
		else
			return this.getLabel();
	}

	public static String blank(String s) {
		StringBuffer sb = new StringBuffer(s.length());
		for (int index = 0; index < s.length(); index++)
			sb.append(' ');
		// System.err.println("returning" + sb.toString() + ";");
		return sb.toString();
	}

	String currentLabelPos;
	boolean nonBorderLabel;
	boolean showUnlabelledBorder = false;
	Border border = null;

	public void cleanUpForReuse() {
		currentLabelPos = null;
	}

	/* public */void makeBorder() {
		if (!attributesInitialized)
			return;

		// this.setMinimumSize(new Dimension(50, 50));

		// if (isElided()) this.setBorder(null);
		// commenting out entire section because of duplicate label
		String borderLabel;

		// if (label == null) return;

		// if ((isBorderLabelVisible()) && label != null)
		if ((isBorderLabelVisible()) && label != null /* && !prevBorderEmpty */)
			borderLabel = label;
		else
			// borderLabel = "";
			borderLabel = INVISIBLE_LABEL;
		// if (component instanceof JCheckBox && !isElided()){
		if (component != null && component instanceof VirtualCheckBox
		// && !isElided()
		) {
			String cbLabel = getObjectAdapter().getLabelRight();
			if (cbLabel == null) {
				// cbLabel = borderLabel;
				cbLabel = adapter.getLabelWithoutSuffix();
			}

			// if (component instanceof JCheckBox || isElided() ) {
			if (getContainer() == null)
				return;
			// int componentCount = container.getComponentCount();
			this.setBorder(null);
			// ((VirtualCheckBox) component).setLabel(borderLabel);
			if (isLabelVisible())
				((VirtualCheckBox) component).setLabel(cbLabel);
			// this.getContainer().setBorder(null);
			// JCheckBox checkBox = (JCheckBox) component;

			return;

		}

		// String borderLabel;
		// Border border = null;
		// border = new SoftBevelBorder(SoftBevelBorder.RAISED,Color.lightGray,
		// Color.gray);
		// border = new EmptyBorder(0,0,0,0);
		// border = new SoftBevelBorder(SoftBevelBorder.RAISED,Color.lightGray,
		// Color.gray);
		// add these two lines for testing
		/*
		 * border = new EmptyBorder(0, 0, 0, 0); this.setBorder(new
		 * TitledBorder(border, " ", // this.getContainer().setBorder (new
		 * TitledBorder(border, borderLabel, borderJustification,
		 * TitledBorder.DEFAULT_POSITION));
		 */
		boolean labelChanged = false;
		boolean borderTypeChanged = false;
		if (adapter.getShowBorder() && nonEmptyBorder) {
			// if (listener.getShowBorder() ) {
			if (prevBorderLabel == null || prevBorderEmpty) {
				// || !borderLabel.equals(prevBorderLabel)) {
				borderTypeChanged = true;
				border = new SoftBevelBorder(SoftBevelBorder.RAISED,
						Color.lightGray, Color.gray);

				// border = new EmptyBorder(0, 0, 0, 0);
				// prevBorderLabel = borderLabel;
				prevBorderEmpty = false;
			}
			/*
			 * else setBorder = false;
			 */
			// return;

		} else {
			if (prevBorderLabel == null || !prevBorderEmpty) {
				// || !borderLabel.equals(prevBorderLabel)) {
				borderTypeChanged = true;
				border = new EmptyBorder(0, 0, 0, 0);
				prevBorderEmpty = true;
				// prevBorderLabel = borderLabel;

			}
			/*
			 * else setBorder = false;
			 */
			// return;
		}
		labelChanged = !borderLabel.equals(prevBorderLabel);

		// System.err.println("Label Visible" + labelVisible);
		// if ((labelVisible || isEdited()) && label != null)
		// if ((labelVisible ) && label != null)
		/*
		 * if ((isLabelVisible() ) && label != null) borderLabel = label; else
		 * borderLabel = "";
		 */
		// borderLabel = " ";
		// System.err.println("border Label" + borderLabel + "label" + label);
		// this seems like unreachable code.commenting it out
		// moving code from below and commenting out check
		// if
		// (adapter.getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER)) {
		if (/* !isElided() && */adapter.getLabelPosition().equals(
				AttributeNames.LABEL_IN_BORDER)) {

			if (labelChanged || borderTypeChanged) {
				TitledBorder titledBorder = new TitledBorder(border, borderLabel,
						// this.getContainer().setBorder (new TitledBorder(border,
						// borderLabel,
								borderJustification, TitledBorder.DEFAULT_POSITION);
//				this.setBorder(new TitledBorder(border, borderLabel,
//				// this.getContainer().setBorder (new TitledBorder(border,
//				// borderLabel,
//						borderJustification, TitledBorder.DEFAULT_POSITION));
				Integer fontSize = getObjectAdapter().getFontSize();
				if (fontSize != null) {
					Font oldFont = titledBorder.getTitleFont();
					Font newFont = Common.toFontSize(oldFont, fontSize);
					titledBorder.setTitleFont(newFont);

				}
				setBorder(titledBorder);
				
				prevBorderLabel = borderLabel;
			}
			if (nonBorderLabel && getLabelComponent().getParent() != null) {
				getLabelComponent().getParent().remove(getLabelComponent());
			}
			nonBorderLabel = false;

		} else {
			borderLabel = label;

			if (edited && !isLabelVisible() && adapter.getShowBorder()
					&& labelChanged
					&& !(EDITED_INVISIBLE_LABEL.equals(prevBorderLabel))) {
				this.setBorder(new TitledBorder(border, EDITED_INVISIBLE_LABEL,
						// this.getContainer().setBorder (new
						// TitledBorder(border, borderLabel,
						TitledBorder.LEFT/* borderJustification */,
						TitledBorder.DEFAULT_POSITION));
				prevBorderLabel = EDITED_INVISIBLE_LABEL;
			}

			else
			// if (listener.getShowBorder() && setBorder &&
			// !prevBorderLabel.equals("")) {
			if (/* !isElided() && */adapter.getShowBorder() && labelChanged
					&& !INVISIBLE_LABEL.equals(prevBorderLabel)) {

				this.setBorder(new TitledBorder(border, INVISIBLE_LABEL,
				// this.getContainer().setBorder (new TitledBorder(border,
				// borderLabel,
						borderJustification, TitledBorder.DEFAULT_POSITION));
				prevBorderLabel = INVISIBLE_LABEL;
			}
			/*
			 * else if (showUnlabelledBorder) { this.setBorder(new
			 * TitledBorder(border, INVISIBLE_LABEL, //
			 * this.getContainer().setBorder (new TitledBorder(border,
			 * borderLabel, borderJustification,
			 * TitledBorder.DEFAULT_POSITION));
			 * 
			 * }
			 */
			else if (!adapter.getShowBorder()
					&& (!nonBorderLabel || labelChanged)) {// not sure why this
															// option is needed
				/*
				 * this.setBorder(new TitledBorder(border, "", //
				 * this.getContainer().setBorder (new TitledBorder(border,
				 * borderLabel, borderJustification,
				 * TitledBorder.DEFAULT_POSITION));
				 */
				this.setBorder(null);
				prevBorderLabel = null;
			}
			if (!isLabelVisible()) {
				if (nonBorderLabel) {
					if (getLabelComponent().getParent() != null)
						getLabelComponent().getParent().remove(
								getLabelComponent());
				}
				nonBorderLabel = false;
				return;
			}
			// if (nonBorderLabel &&
			// listener.getLabelPosition().equals(currentLabelPos) &&
			// prevBorderLabel == borderLabel)
			if (nonBorderLabel
					&& adapter.getLabelPosition().equals(currentLabelPos))
				return;
			/*
			 * if (nonBorderLabel &&
			 * listener.getLabelPosition().equals(currentLabelPos) &&
			 * prevBorderLabel != borderLabel) {
			 * 
			 * labelComponent.setText(borderLabel);
			 * 
			 * return; }
			 */
			// labelComponent.setText(borderLabel);
			prevBorderLabel = borderLabel;
			nonBorderLabel = true;
			currentLabelPos = adapter.getLabelPosition();
			/*
			 * this.setBorder(new TitledBorder(border, "", //
			 * this.getContainer().setBorder (new TitledBorder(border,
			 * borderLabel, borderJustification,
			 * TitledBorder.DEFAULT_POSITION));
			 */

			if (getLabelComponent().getParent() != null) {
				VirtualContainer labelParent = getLabelComponent().getParent();
				labelParent.remove(getLabelComponent());
				labelParent.doLayout();
			}

			if (currentLabelPos.equals(AttributeNames.LABEL_IS_LEFT)) {
				/*
				 * if (adapter.getDefinedLabel() == null &&
				 * !(borderLabel.equals(""))) {
				 * labelComponent.setText(borderLabel + ":"); //borderLabel =
				 * borderLabel + ":"; }
				 */
				getContainer().add(getLabelComponent(), BorderLayout.WEST);
				formatLeftLabel(getLabelComponent());
				// getContainer().add(labelContainer, BorderLayout.WEST);
				// labelComponent.setText(borderLabel + ":");

			} else if (currentLabelPos.equals(AttributeNames.LABEL_IS_RIGHT)) {
				getContainer().add(getLabelComponent(), BorderLayout.WEST);
			} else if (currentLabelPos.equals(AttributeNames.LABEL_IS_ABOVE)) {
				VirtualDimension size = null;
				int componentWidth = 0;
				if (component != null)
					size = (VirtualDimension) component.getPreferredSize();

				getContainer().add(getLabelComponent(), BorderLayout.NORTH);
				formatTopLabel(getLabelComponent());
				/*
				 * if (size != null) { componentWidth = size.width; int height =
				 * getContainer().getPreferredSize().height; int width =
				 * getContainer().getPreferredSize().width;
				 * getContainer().setPreferredSize(new Dimension(size.width,
				 * height)); }
				 */

			} else if (currentLabelPos.equals(AttributeNames.LABEL_IS_BELOW)) {
				getContainer().add(getLabelComponent(), BorderLayout.SOUTH);
			}
		}
		/*
		 * if (component instanceof JCheckBox && !isElided()) { //if (component
		 * instanceof JCheckBox || isElided()) {
		 * 
		 * if (getContainer() == null) return; if (labelComponent == null) {
		 * 
		 * labelComponent = new JLabel(label); //this.add(labelComponent,
		 * BorderLayout.EAST ); System.err.println("Generic widget, adding label
		 * component EAST"); this.getContainer().add(labelComponent,
		 * BorderLayout.EAST );
		 * 
		 * //((JCheckBox) component).setLabel(label);
		 * 
		 * } //component.setSize(20, component.getSize().height);
		 * //this.setBorder (new TitledBorder(new
		 * SoftBevelBorder(SoftBevelBorder.RAISED,Color.lightGray, Color.gray),
		 * this.setBorder (new TitledBorder(border,
		 * //this.getContainer().setBorder (new TitledBorder(border,
		 * borderLabel, TitledBorder.DEFAULT_JUSTIFICATION,
		 * TitledBorder.DEFAULT_POSITION)); //label, TitledBorder.CENTER,
		 * TitledBorder.LEFT));
		 * 
		 * } else // this.setBorder (new javax.swing.border.TitledBorder(new
		 * SoftBevelBorder(SoftBevelBorder.RAISED,Color.lightGray, Color.gray),
		 * label)); //component.setSize(20, component.getSize().height);
		 * this.setBorder (new TitledBorder(border, borderLabel,
		 * //this.getContainer().setBorder (new TitledBorder(border,
		 * borderLabel, borderJustification, TitledBorder.DEFAULT_POSITION));
		 */

		// this.doLayout();
		/*
		 * if (!labelVisible) this.setBorder (new
		 * javax.swing.border.TitledBorder(new
		 * SoftBevelBorder(SoftBevelBorder.RAISED), "")); if (component
		 * instanceof JCheckBox) this.setBorder (new
		 * javax.swing.border.TitledBorder(new
		 * SoftBevelBorder(SoftBevelBorder.RAISED,Color.lightGray, Color.gray),
		 * label, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.RIGHT));
		 * //this.setBorder (new javax.swing.border.TitledBorder(label));
		 * this.setBorder (new javax.swing.border.TitledBorder(new
		 * SoftBevelBorder(SoftBevelBorder.RAISED,Color.lightGray, Color.gray),
		 * label));
		 * 
		 * //this.setBorder(new TitledBorder(new
		 * LineBorder(this.getBackground()),label, TitledBorder.CENTER,
		 * TitledBorder.DEFAULT_POSITION)); else else this.setBorder (new
		 * javax.swing.border.TitledBorder(new
		 * SoftBevelBorder(SoftBevelBorder.RAISED), "")); //this.setBorder (new
		 * javax.swing.border.TitledBorder("")); //this.setBorder(new
		 * LineBorder(Color.gray));
		 */

	}

	int borderJustification = TitledBorder.DEFAULT_JUSTIFICATION;

	// int borderJustification = TitledBorder.TRAILING;

	/* public */void setBorderVisible(boolean v) {
		// labelComponent.setVisible(v);
		nonEmptyBorder = v;
		// System.err.println("label visible" + labelVisible);
		makeBorder();

	}

	boolean nonEmptyBorder = true;

	/* public */void setBorderJustification(int j) {
		// labelComponent.setVisible(v);
		borderJustification = j;
		// System.err.println("label visible" + labelVisible);
		makeBorder();

	}

	String oldLabel = "";

	void setTrueLabel(String newVal) {
		if (newVal != null && newVal.equals(oldLabel))
			return;
		oldLabel = newVal;
	}

	String getTrueLabel() {
		return oldLabel;
	}

	public void showColumnTitle(String title) {
		// if (!label.equals(title)) {
		labelFilter = true;
		if (!getLabel().equals(title)) {
			setTrueLabel(label);
			// oldLabel = label;
			// label = title;
			internalSetLabel(title);
			setUneditedLabel(title);
		}
		if (adapter.getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER))
			nonEmptyBorder = false;
		// borderJustification = TitledBorder.LEFT;
		borderJustification = TitledBorder.CENTER;
		// setLabelVisible(true);
		// labelVisible = true;
		// set label visible makes border
		// makeBorder();
	}

	public void showColumnTitle() {
		showColumnTitle(label);

	}

	String prevLabelComponentLabel;

	void internalSetLabel(String newVal) {
		if (newVal != null && newVal.equals(label))
			return;
		label = newVal;
		// if (labelComponent != null && labelComponent.getParent() != null) {
		if (labelComponent != null) {
			if (newVal != prevLabelComponentLabel) {
				getLabelComponent().setText(newVal);
				prevLabelComponentLabel = newVal;
			}
		}
	}

	boolean labelFilter = true;

	public void hideColumnTitle() {
		labelFilter = false;
		if (label == null) {
			internalSetLabel(getLabel());
			setUneditedLabel(getLabel());
		}
		if (!label.equals(oldLabel)) {
			// label = oldLabel;
			// internalSetLabel(oldLabel);
			internalSetLabel(getTrueLabel());
			if (getLabel().equals("B"))
				System.err.println("got it");
			setUneditedLabel(getLabel());
			// setUneditedLabel(getTrueLabel());
		}
		if (adapter.getLabelPosition().equals(AttributeNames.LABEL_IN_BORDER))
			nonEmptyBorder = false;
		borderJustification = TitledBorder.CENTER;
		// labelVisible = false;
		// setLabelVisible(false);
		// set label visible makes border
		// makeBorder();
	}

	boolean labelVisible = false;

	public void setLabelVisible(boolean v) {
		setLabelVisibleWithoutChangingBorder(v);
		// labelComponent.setVisible(v);
		/*
		 * if (component instanceof VirtualCheckBox && !v)
		 * System.err.println("VCB being made invisible"); if (labelVisible ==
		 * v) return; labelVisible = v; // System.err.println("label visible" +
		 * labelVisible); if (!labelVisible) { editedLabel =
		 * editedInvisibleLabel; if (edited) // label = editedLabel;
		 * internalSetLabel(editedLabel); }
		 */

		makeBorder();

	}

	void setEditedLabel(String newVal) {
		if (newVal != null && newVal.equals(editedLabel))
			return;
		editedLabel = newVal;
	}

	void setLabelVisibleWithoutChangingBorder(boolean v) {
		// labelComponent.setVisible(v);
		// if (component instanceof VirtualCheckBox && !v)
		// System.err.println("VCB being made invisible");
		if (labelVisible == v)
			return;
		labelVisible = v;
		// System.err.println("label visible" + labelVisible);
		if (!labelVisible) {
			// editedLabel = EDITED_INVISIBLE_LABEL;
			setEditedLabel(EDITED_INVISIBLE_LABEL);
			if (edited)
				// label = editedLabel;
				internalSetLabel(editedLabel);
		}

		// makeBorder();

	}

	/* public */VirtualComponent getComponent() {
		return component;
	}

	// uiFrame uiF;
	void setColor(Color c) {
		if (c == null)
			return;
		originalColor = c;
		if (getContainer() != null)
			getContainer().setBackground(c);
		if (getComponent() != null)
			getComponent().setBackground(c);
	}
	void setColor(ObjectAdapter objectAdapter) {
		Color componentBackground = objectAdapter.getComponentBackground();
		Color containerBackground = objectAdapter.getContainerBackground();
		setColor(getComponent(), componentBackground, containerBackground); // component background?
		setColor(getContainer(), componentBackground, containerBackground);
		Color componentForeground = objectAdapter.getComponentForeground();
		if (componentForeground != null  && 
				getComponent() != null)
		  getComponent().setForeground(componentForeground);
		
//		VirtualComponent component = getComponent();
//		VirtualComponent container = getContainer();
		
	}
	// duplicates code in widget adapter to set color, can perhaps remove code in widget adapter
	void setColor (VirtualComponent aComponent, Color componentBackground, Color containerBackground) {
		if (aComponent == null)
			return;
		if (aComponent instanceof VirtualContainer)
			setContainerColor ((VirtualContainer) aComponent, containerBackground);
		else if (aComponent instanceof VirtualSlider) {
			setComponentColor (aComponent, containerBackground);
		}
		else
			setComponentColor (aComponent, componentBackground);
//		setComponentColor ( aComponent, containerBackground);

		
	}
	
	void setContainerColor (VirtualContainer aContainer, Color containerBackground) {
		if (containerBackground != null) {
			aContainer.setBackground(containerBackground);
		}		
	}
	
	void setComponentColor (VirtualComponent aComponent, Color background) {
		if (background != null) {
			aComponent.setBackground(background);
		}		
	}

	void setObjectAdapterAttributes() {
		boolean isWidgetShellLabelled = getObjectAdapter().isLabelled()
				&& !getObjectAdapter().isFlatTableRowDescendent();
		setLabelVisibleWithoutChangingBorder(isWidgetShellLabelled);
		// setLabelVisibleWithoutChangingBorder(getObjectAdapter().isLabelled());
		setLabelWithoutChangingBorder(getObjectAdapter().getLabel());
		setElideString(getObjectAdapter().getElideString());
//		setColor(getObjectAdapter().getComponentBackground());
//		setColor(getObjectAdapter().getContainerBackground());
		setColor(getObjectAdapter());

		setSize();
	}

	public void setSize() {
		// VirtualComponent component = getUIComponent();
		// if someone else created it, they should have set its size
		if (container == null)
			return;

		Integer x = null;
		Integer y = null;
		Integer width;
		Integer height;

		width = adapter.getShellWidth();
		height = adapter.getShellHeight();
		x = adapter.getComponentX();
		y = adapter.getComponentY();

		if ((x == null || y == null) & width == null && height == null)
			return;
		// if ( x != null && y != null & width == null && height == null ) {
		// container.setLocation(x, y);
		// return;
		// }
		if (width == null)
			width = adapter.getComponentWidth();
		if (height == null)
			height = adapter.getComponentHeight();

		if (width == null)
			width = adapter.getWidgetAdapter().defaultWidth();
		if (height == null)
			height = adapter.getWidgetAdapter().defaultWidth();

		if (width > 0 && height > 0) {
			if (x != null && y != null)
				container.setBounds(x, y, width, height);
			else
				OEMisc.setSize(container, width, height);
			return;
		}
		if (width != 0)
			OEMisc.setWidth(container, width, x, y);
		// int height = adapter.getComponentHeight();
		if (height != 0)
			OEMisc.setHeight(container, height, x, y);
	}

	public void setComponent(VirtualComponent nc) {
		// by the time the component is set, the attributes of objectAdapter
		// have been set
		// setObjectAdapterAttributes();
		if (nc == null || nc == component)
			return;

		if (component != null) {
			// remove(component);
			forceInternalElide();
		}
		component = nc;
		// component.setToolTipText(toolTipText);
		if (originalColor != null)
			component.setBackground(originalColor);
		else
			originalColor = nc.getBackground();
		/*
		 * component.addMouseListener(mouseAdapter);
		 * RightMenuManager.bindToRightMenu(component, adapter);
		 */
		if (adapter.getComponentSelectable())
			addMouseListeners(component);
		maybeSetTooltipText(component, toolTipText);
		// forceInternalExpand();
		/*
		 * GridBagConstraints c = new GridBagConstraints(); c.gridx =
		 * GridBagConstraints.RELATIVE; c.gridwidth =
		 * GridBagConstraints.REMAINDER; c.gridheight=
		 * GridBagConstraints.REMAINDER; c.fill = GridBagConstraints.BOTH;
		 * c.weightx = 1; c.weighty = 1; c.insets = new Insets(0, 5, 0, 0);
		 * componentConstraints = c; gridBagLayout.setConstraints(component,
		 * componentConstraints);
		 */
		// add(nc, c);
		// System.err.println("set component" + listener );
		/*
		 * if (listener instanceof uiPrimitiveAdapter) {
		 * System.err.println("expanding" + listener); internalExpand(); }
		 */
		/*
		 * component.setVisible(true); add(component);
		 */
		// add(component, componentConstraints);
		// getFrame();
		// uiF = (uiFrame) f;
		// this.alignLabel(GridBagConstraints.NORTH);
	}

	GridBagConstraints elideComponentConstraints;
	
	public void refreshElideComponent() {
		String newElideString;
		if (adapter.unparseAsToString())
			newElideString = adapter.getRealObject().toString();
		else
			newElideString =		adapter.getElideString();
		if (!elideString.equals(newElideString)) {
			elideString = newElideString;
			setText(elideComponent, elideString);
		}
		
	}

	public VirtualComponent getElideComponent() {
		String elideString = getObjectAdapter().getElideString();
		String elideImage = getObjectAdapter().getElideImage();
		if (elideComponent == null) {
			if (elideImage != null) {
				elideComponent = LabelSelector.createLabel(new ImageIcon(
						elideImage));

			} else {
				elideComponent = TextFieldSelector.createTextField(elideString);
				((VirtualTextField) elideComponent).setEditable(false);
			}

			// elideComponent.setEnabled(false);
			elideComponent.setBackground(container.getBackground());
			elideComponent.setForeground(Color.BLACK);
			elideComponent.setName("Elide Component (getElideComponent())");
			addMouseListeners(elideComponent);
			// setElideComponent(elideComponent);
			OEMisc.setWidth(elideComponent, getObjectAdapter()
					.getElideComponentWidth());
			// Misc.setSize(elideComponent,
			// getObjectAdapter().getElideComponentWidth(), 33);
			// Misc.setSize(container,
			// getObjectAdapter().getElideComponentWidth(), 33);
			// Misc.setHeight(elideComponent, 33);
		}

		return elideComponent;
	}

	/* public */void setElideComponent(VirtualComponent elc) {
		if (elideComponent != null)
			remove(elideComponent);

		elideComponent = elc;
		OEMisc.setWidth(elideComponent, getObjectAdapter()
				.getElideComponentWidth());
		// elideComponent.setHorizontalAlignment(SwingConstants.LEFT);
		/*
		 * GridBagConstraints c = new GridBagConstraints();
		 * elideComponentConstraints = c; c.gridx = GridBagConstraints.RELATIVE;
		 * c.gridwidth = GridBagConstraints.REMAINDER; c.gridheight=
		 * GridBagConstraints.REMAINDER; c.fill = GridBagConstraints.BOTH;
		 * c.weightx = 1; c.weighty = 1; //elc.setVisible(false);
		 * elc.setVisible(true); gridBagLayout.setConstraints(elideComponent,
		 * elideComponentConstraints);
		 */
		/*
		 * elideComponent.setVisible(false);
		 */
		if (elided)
			add(elideComponent);
		else
			internalExpand();

		// add(elideComponent, componentConstraints);
		// add(elc, c);
		// elideComponent.addMouseListener(mouseAdapter);
	}

	/* public */static void addNullWidget(VirtualContainer container, String s) {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		// ((JPanel )container).add ( getNullWidget(s), c);
		container.add(getNullWidget(s));
	}

	/* public */static void addNullWidget(VirtualContainer container) {
		addNullWidget(container, "<Uninitialized >");
	}

	public static VirtualComponent getNullWidget(String s) {
		VirtualComponent nullWidget = AWTComponent.virtualComponent(new JLabel(
				s));
		return nullWidget;
		// return new JLabel(s);
	}

	public static VirtualComponent getNullWidget() {
		return getNullWidget("");
	}

	void initiallyExpandListener() {
		if (adapter instanceof PrimitiveAdapter) {
			System.err.println("expanding" + adapter);
			internalExpand();
		}
	}

	/* public */void setElideString(String string) {
		if (elideComponent == null)
			return;
		// VirtualComponent elideComponent = getElideComponent();
		if (elideComponent != null
				&& !(elideComponent instanceof VirtualTextField))
			return;
		if (string.equals(elideString))
			return;
		else {
			// listener = list;
			// JLabel l = new JLabel(string);
			// VirtualLabel l = LabelSelector.createLabel(string);

			// if (elideComponent == null) {
			// elideComponent = TextFieldSelector.createTextField(string);
			// elideComponent.setEnabled(false);
			// addMouseListeners(elideComponent);
			// setElideComponent(elideComponent);
			// } else
			((VirtualTextField) elideComponent).setText(string);
			elideString = string;
			/*
			 * VirtualComponent l = TextFieldSelector.createTextField(string);
			 * l.setEnabled(false); //VirtualComponent l =
			 * LabelSelector.createLabel(string);
			 * 
			 * 
			 * addMouseListeners(l); elideString = string; setElideComponent(l);
			 * // initiallyExpandListener();
			 */
			return;
		}
	}

	/* public */void setElideImage(String gif) {
		if (gif.equals(elideImage))
			return;
		else {
			// adapter = list;
			// JLabel icon = new JLabel(new ImageIcon(gif));
			VirtualLabel icon = LabelSelector.createLabel(new ImageIcon(gif));
			/*
			 * icon.addMouseListener(mouseAdapter);
			 * RightMenuManager.bindToRightMenu(icon, adapter);
			 */
			addMouseListeners(icon);
			elideImage = gif;
			setElideComponent(icon);
			// initiallyExpandListener();
			return;
		}
	}

	// Color oldColor;
	Object oldColor;

	/* public */void selectElideComponent() {
		// System.err.println("Elide Component Selected");
		if (elideComponent == null)
			return;
		if (elideComponent.getBackground() != AttributeNames.getSelectionColor())
			oldColor = elideComponent.getBackground();
		elideComponent.setBackground((Color) AttributeNames.getDefaultOrSystemDefault(AttributeNames.SELECTION_COLOR));
	}

	/* public */void unselectElideComponent() {
		if (elideComponent == null)
			return;
		// System.err.println("Elide Component unselected");
		elideComponent.setBackground(oldColor);
	}

	private void doPasteAction() {
		System.err.println("Paste");
		Object selection = ((ObjectAdapter) SelectionManager
				.getCurrentSelection()).getRealObject();
		System.err.println("Setting value to " + selection);
		adapter.refreshValue(selection);
		adapter.uiComponentValueChanged();
		System.err.println("Done");
	}

	private void doCreateAction() {
		System.err.println("Create: Not implemented");
	}

	private void doOtherAction(ActionEvent evt) {
		System.err.println("Select/Edit: TBDS");
	}

	Frame f = null;

	uiFrame uiF = null;

	/*
	 * public Frame getFrame(Component c) { //System.err.println("parameter"
	 * +c); if (f == null) { //System.err.println("null f"); while (c != null) {
	 * 
	 * //System.err.println("componnet" + c.getName()); if (c instanceof Frame)
	 * { f = (Frame) c; uiF = (uiFrame) f; return f; } c = c.getParent(); } }
	 * //System.err.println("returning" + f); return f; }
	 */
	/*
	 * public uiFrame getUIFrame(Component c) { if (uiF == null) uiF = (uiFrame)
	 * getFrame(c); //System.err.println("uif = " + uiF); return uiF; } public
	 * uiFrame getUIFrame() { if (uiF == null) if (f == null) uiF = (uiFrame)
	 * getFrame(this);
	 * 
	 * //System.err.println("uif = " + uiF); return uiF; }
	 */
	/*
	 * public uiFrame getUIFrame(Component c) { return getUIFrame(); }
	 */
	public uiFrame getUIFrame() {
		if (adapter == null) {
			// System.err.println("Null Adapter of generic widget, do not know
			// UIFrame");
			return null;
		} else
			return adapter.getUIFrame();
	}

	public void setParentContainer(VirtualContainer parent) {
		this.getContainer().setBackground(parent.getBackground());
	}

	@Override
	public VirtualLabel getElideHandle() {
		if (elideHandle == null) {
			if (upIcon != null)
				elideHandle = LabelSelector.createLabel(upIcon);
			else
				elideHandle = LabelSelector.createLabel(EXPAND_HANDLE_TEXT);

			elideHandle.setName("Elide Handle " + getObjectAdapter()
					+ "(AGenericWidgetShell.getElideHandle)");
			addMouseListeners(elideHandle);
			makeExpandHandleLabel();
		}

		return elideHandle;
	}
	
	public static void maybeSetTooltipText(VirtualComponent component, String theToolTipText) {
		if (theToolTipText == null || theToolTipText == "")
			return;
		else
			component.setToolTipText(theToolTipText);
	}

	VirtualLabel getLabelComponent() {
		if (labelComponent == null) {
			labelComponent = LabelSelector.createLabel(getObjectAdapter()
					.getLabel());
			// setLabelWithoutChangingBorder(getObjectAdapter().getLabel());
			labelComponent.setName("Label Component " + getObjectAdapter()
					+ "(AGenericWidgetShell.getLabelComponent)");
			maybeSetTooltipText(labelComponent, toolTipText);
			int labelWidth = getObjectAdapter().getLabelWidth();
			OEMisc.setWidth(labelComponent, labelWidth);
			// OEMisc.setWidth(labelComponent,
			// getObjectAdapter().getLabelWidth());
			WidgetAdapter.setFont(labelComponent, getObjectAdapter());
			if (labelComponent != null) {

				addMouseListeners(labelComponent);
			}
			setLabelWidth();
			// labelContainer = labelComponent;
		}
		return labelComponent;
	}

}