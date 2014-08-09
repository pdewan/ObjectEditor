package bus.uigen;

//
// An abstract class from which all widget adaptors
// derive. This class contains all the widget related
// information 
// 
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import util.misc.Common;
import util.trace.Tracer;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.attributes.Configurable;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.menus.RightMenu;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.RootAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.trace.LogicalChildComponentAdded;
import bus.uigen.trace.SubrangeErrror;
import bus.uigen.translator.FormatException;
import bus.uigen.translator.TranslatorRegistry;
import bus.uigen.view.AGenericWidgetShell;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.UniversalWidget;
import bus.uigen.widgets.VirtualCheckBox;
import bus.uigen.widgets.VirtualComboBox;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.VirtualLabel;
import bus.uigen.widgets.VirtualMenu;
import bus.uigen.widgets.VirtualMenuBar;
import bus.uigen.widgets.VirtualMenuItem;
import bus.uigen.widgets.VirtualScrollPane;
import bus.uigen.widgets.VirtualSlider;
import bus.uigen.widgets.VirtualTextComponent;
import bus.uigen.widgets.awt.AWTComponent;
import bus.uigen.widgets.events.VirtualFocusEvent;
import bus.uigen.widgets.events.VirtualFocusListener;
import bus.uigen.widgets.tree.VirtualTree;

;
public abstract class WidgetAdapter implements WidgetAdapterInterface,
		VirtualFocusListener {
	protected VirtualScrollPane spane;

	public static final Color SELECTION_COLOR = AttributeNames.DEFAULT_SELECTION_COLOR;
	public static final int DEFAULT_COMPONENT_WIDTH = 150;
	public static final int DEFAULT_FLAT_TABLE_CELL_HEIGHT = 20;
	// uiComponentValueChangedListener objectAdapter;
	protected ObjectAdapter adapter;
	// VirtualComponent uiComponent;
	WidgetShell genericWidget;

	// Constructor
	public WidgetAdapter() {
	}

	public String toString() {
		if (adapter == null)
			return super.toString();
		else {
			return adapter.toDebugText();
				/*super.toString() + "(" +
				adapter.getPropertyName() + ":"
					+ adapter.getIndex() + ")";
					*/
		}

	}

	// Property accessor methods for the
	// uiComponent property.
	public void setUIComponent(VirtualComponent c) {
		// uiComponent = c;
		if (c == null)
			linkUIComponentToMe();
		else {
			linkUIComponentToMe(c);

		}
		
		 if (getUIComponent() != null)
			 processAttributes();
		 //getUIComponent().setToolTipText(getObjectAdapter().getToolTipText());
		 
		/*
		 * Container container = c.getParent(); if (container instanceof
		 * uiGenericWidget) genericWidget = (uiGenericWidget) c.getParent();
		 */
	}

	VirtualContainer container;

	public void setParentContainer(VirtualContainer newVal) {
		container = newVal;

	}

	public VirtualContainer getParentContainer() {
		return container;
	}

	public boolean isComponentAtomic() {
		return false;
	}

	/*
	 * public VirtualComponent getUIComponent() { return uiComponent; }
	 */
	public abstract VirtualComponent getUIComponent();

	public void emptyComponent() {
		getUIComponent().invalidate();
	}

	// Dummy method
	public void setViewObject(Object obj) {
	}

	public void setPreWrite() {
	};

	public VirtualComponent instantiateComponent(Class cclass,
			ObjectAdapter adapter) {
		try {
			if (cclass.isInterface())
				return null;
			return AWTComponent.virtualComponent((Component) cclass
					.newInstance());
		} catch (Exception e) {
			return null;
		}
	}

	public boolean processDescendentAttribute(ObjectAdapter descendent,
			Attribute attrib) {
		return true;
	}

	public void processAttributes() {
		if (getUIComponent() == null)
			return;
		setFont(getUIComponent(), getObjectAdapter());
		setTextAttributes(getObjectAdapter().getTextAttributes());
//		System.out.println(getObjectAdapter().getPath());
		//Font font = getObjectAdapter().getFont();
		//if (font != null)
		//getUIComponent().setFont(getObjectAdapter().getFont());
		AGenericWidgetShell.maybeSetTooltipText(getUIComponent(), getObjectAdapter().getExplanation());
	}
	
	public void setTextAttributes(Map<TextAttribute, Object> newVal) {
		if (newVal == null) return;
		VirtualComponent c = getUIComponent();
		Font font = (Font) c.getFont();
		Font newFont = font.deriveFont(newVal);
		c.setFont(newFont);
	
		
	}
	
	public static void setFont(UniversalWidget c, ObjectAdapter adapter) {
		setFont(c, adapter.getFont(), adapter.getFontName(), adapter.getFontStyle(), adapter.getFontSize());
	}
	
//	public static void oldSetFont(UniversalWidget c, Font f, String name, Integer style, Integer size) {			
//			if (c == null)
//				return;
//			if (f != null) {
//				c.setFont(f);
//				return;
//			}
//			Font oldFont = c.getFont();
//			if (oldFont == null) return;
//			int newStyle;
//			if (style == null)
//				newStyle = oldFont.getStyle();
//			else 
//				newStyle = style;
//			int newSize;
//			if (size == null)
//				newSize = oldFont.getSize();
//			else
//				newSize = size;
//			String newName;
//			if (name == null)
//				newName = oldFont.getName();
//			else
//				newName = name;
//			c.setFont(new Font(newName, newStyle, newSize));
//	}
	public static void setFont(UniversalWidget c, Font f, String name, Integer style, Integer size) {			
		if (c == null)
			return;
		if (c instanceof VirtualTextComponent || c instanceof VirtualComboBox || c instanceof VirtualLabel)  {
			VirtualComponent component = (VirtualComponent) c;
			int height = component.getHeight();
			int width = component.getWidth();
			Integer defaultHeight = (Integer) AttributeNames.getDefaultOrSystemDefault(AttributeNames.COMPONENT_HEIGHT);
			Integer defaulWidth = (Integer) AttributeNames.getDefaultOrSystemDefault(AttributeNames.COMPONENT_HEIGHT);
			if (c.getFont() == null )
				return;
			int oldFontSize = ((Font) c.getFont()).getSize();
			if (size != null) {
			double increase = (double) size/(double) oldFontSize;
			if (height == 0 && defaultHeight == null) { 
				height = 20;
			} else if (height == 0) {
				height = defaultHeight;
			}
	
			if (width == 0 && defaulWidth == null) { 
				width = 150;
			} else if (width == 0) {
				width = defaulWidth;
			}
			height = (int) ( height*increase);
			width = (int) ( width*increase);
			WidgetAdapter.setSize(component, width, height);
			}
		}
		if (f != null) {
			c.setFont(f);
			return;
		}
		
		Font oldFont = (Font) c.getFont();
		c.setFont(Common.getFont(oldFont, name, style, size));
		
//		if (oldFont == null) return;
//		int newStyle;
//		if (style == null)
//			newStyle = oldFont.getStyle();
//		else 
//			newStyle = style;
//		int newSize;
//		if (size == null)
//			newSize = oldFont.getSize();
//		else
//			newSize = size;
//		String newName;
//		if (name == null)
//			newName = oldFont.getName();
//		else
//			newName = name;
//		c.setFont(new Font(newName, newStyle, newSize));
}
	
	public static void setFontInFrameTree(uiFrame uiF, Font f, String name, Integer style, Integer size) {		
		VirtualFrame frame = uiF.getFrame();
		if (frame == null)
			return;
		setFontInContainerTree(frame.getContentPane(), f, name, style, size);
		setFontInMenuBar(frame.getMenuBar(), f, name, style, size);
		setFontInContainerTree(uiF.getToolBar(), f, name, style, size);
		Enumeration<RightMenu> rightMenus = RightMenuManager.getRightMenus();
		while (rightMenus.hasMoreElements()) {
			setFontInMenuTree (rightMenus.nextElement().getPopup(), f, name, style, size);
		}
		
	}
	
	public  static void setFontInContainerTree(VirtualContainer c, Font f, String name, Integer style, Integer size) {		
		if (c == null)
			return;
		VirtualComponent[] components = c.getComponents();
		for (int i = 0; i < components.length; i++) {
			VirtualComponent comp = components[i];
			if (comp.getPhysicalComponent() instanceof JComponent) {
				JComponent jComponent = (JComponent) comp.getPhysicalComponent();
				Border border = jComponent.getBorder();
				if (border instanceof TitledBorder) {
					TitledBorder titledBorder = (TitledBorder) border;
					Integer fontSize = (Integer) AttributeNames.getDefaultOrSystemDefault(AttributeNames.FONT_SIZE);
					if (fontSize != null) {
						Font oldFont = titledBorder.getTitleFont();
						Font newFont = Common.toFontSize(oldFont, fontSize);
						titledBorder.setTitleFont(newFont);
						jComponent.setBorder(titledBorder);

					} else {
						titledBorder = titledBorder = new TitledBorder(titledBorder.getBorder(), 
								titledBorder.getTitle(),
								// this.getContainer().setBorder (new TitledBorder(border,
								// borderLabel,
										titledBorder.getTitleJustification(), TitledBorder.DEFAULT_POSITION);
						jComponent.setBorder(titledBorder);
					}
				}
			}
			setFont(comp, f, name, style, size);
			if (comp  instanceof VirtualContainer) {
				VirtualContainer container = (VirtualContainer) comp;
				// cannot get the border
				
				setFontInContainerTree(container, f, name, style, size);
			}
		}
	}
	
	public static void setFontInMenuTree(VirtualMenu c, Font f, String name, Integer style, Integer size) {		
		//VirtualComponent[] components = c.getComponents();		
		if (c == null)
			return;
		setFont(c, f, name, style, size);
		for (int i = 0; i < c.getItemCount(); i++) {
			try {
			VirtualMenuItem comp = c.getItem(i);
			setFont(comp, f, name, style, size);
			if (comp  instanceof VirtualMenu) {
				setFontInMenuTree((VirtualMenu) comp, f, name, style, size);
			}
			} catch (Exception e) {
				Tracer.info(e.getMessage());
			}
		}
	}
	public static void setFontInMenuBar(VirtualMenuBar c, Font f, String name, Integer style, Integer size) {		
		//VirtualComponent[] components = c.getComponents();
		if (c == null)
			return;
		for (int i = 0; i < c.getComponentCount(); i++) {
			UniversalWidget comp = c.getComponentAtIndex(i);
			//setFont(comp, f, name, style, size);
			if (comp  instanceof VirtualMenu) {
				setFontInMenuTree((VirtualMenu) comp, f, name, style, size);
			}
		}
	}

	// Method for objectAdapter to register as
	// a listener for uiValueChanged events
	public void addUIComponentValueChangedListener(
			ComponentValueChangedListener l) {
		adapter = (ObjectAdapter) l;
	}

	public void removeUIComponentValueChangedListener(
			ComponentValueChangedListener l) {
		adapter = null;
	}

	public void cleanUp() {

	}

	public ObjectAdapter getObjectAdapter() {
		if (adapter instanceof ObjectAdapter)
			return (ObjectAdapter) adapter;
		else
			return null;
	}

	public Vector<ObjectAdapter> getChildrenAdaptersInDisplayOrder() {
		return displayChildren;
	}

	public void setObjectAdapter(ObjectAdapter newValue) {
		adapter = newValue;
		//System.out.println(this);
	}

	protected boolean firstTime = true;

	// Method to be invoked by an actual instance of
	// this class when a change occurs in the widget
	// Just transfer the event to the objectAdapter
	// which does whatever is necessary.

	public boolean uiComponentValueChanged() {

		// if (edited) {
		// edited = false;
		// getUIContainer().setUpdated();
		// }

		//return adapter.uiComponentValueChanged((boolean) true);
		boolean retVal = adapter.uiComponentValueChanged((boolean) true);
		adapter.getUIFrame().doUpdateAll();
		return retVal;

		/*
		 * if (this.getUIContainer() != null &&
		 * this.getUIContainer().getUIFrame() != null)
		 * this.getUIContainer().getUIFrame().doImplicitRefresh();
		 */

	}

	/*
	 * boolean edited = false; public void setEdited(boolean newVal) { edited =
	 * newVal; } public boolean getEdited() { return edited; }
	 */
	public void uiComponentValueEdited(boolean edited) {

		adapter.uiComponentValueEdited(edited);

	}

	/*
	 * public void uiComponentValueEdited() {
	 * 
	 * //if (objectAdapter.getEdited()) return; //edited = true;
	 * //getUIContainer().setEdited();
	 * objectAdapter.uiComponentValueEdited(true);
	 * 
	 * 
	 *  } public void uiComponentValueUpdated() {
	 * 
	 * //if (objectAdapter.getEdited()) return; //edited = true;
	 * //getUIContainer().setEdited();
	 * objectAdapter.uiComponentValueEdited(false);
	 * 
	 * 
	 *  }
	 */
	public void uiComponentValueChanged(Object source) {

		// if (edited) {
		// edited = false;
		// genericWidget.setUpdated();
		// }
		adapter.uiComponentValueChanged(source);
	}

	// Method invoked when thene is a value change in the
	// Object (by the objectAdapter).
	public void objectValueChanged(ValueChangedEvent evt) {
		if (!evt.getNewValue().equals(getUIComponentValue()))
			setUIComponentValue(evt.getNewValue());
	}

	public void uiComponentFocusGained() {
		/*
		 * wiill have generic widget directly worry about this
		 * objectAdapter.uiComponentFocusGained();
		 */
	}

	public void uiComponentFocusGained(VirtualFocusEvent e) {
		/*
		 * wiill have generic widget directly worry about this
		 * objectAdapter.uiComponentFocusGained(e);
		 */
	}

	public void uiComponentFocusLost() {
		if (adapter != null)
			adapter.uiComponentFocusLost();
	}

	public boolean processDirection(String direction) {
		// System.out.println("E****: Process Direction in uiWidgetAdapter
		// should not have been called.");
		return false;
	}

	/*
	 * public boolean processDirection(String direction) { VirtualComponent c =
	 * getUIComponent(); if (c == null) return true;
	 * 
	 * try { VirtualContainer cn = (VirtualContainer) c; int count =
	 * cn.getComponentCount(); uiGridLayout lm = (uiGridLayout) cn.getLayout();
	 * if (AttributeNames.HORIZONTAL.equals(direction)) { if (lm.getRows() == 1)
	 * return true; else //cn.setLayout(new uiGridLayout(1, count));
	 * cn.setLayout(new uiGridLayout(1, count, uiGridLayout.DEFAULT_HGAP, 0)); }
	 * else { if (lm == null) return false; if(lm.getColumns() == 1) return
	 * true; else //cn.setLayout(new uiGridLayout(count, 1)); cn.setLayout(new
	 * uiGridLayout(count, 1, 0, uiGridLayout.DEFAULT_VGAP)); } } catch
	 * (Exception e) {return false;} return false;
	 *  }
	 */

	public void processDirection() {

	}

	public boolean processAttribute(Attribute attrib) {
		// System.out.println("staring process attribute in widget adapter" +
		// attrib.getName());

		VirtualComponent c = getUIComponent();

		if (c != null && c instanceof Configurable) {
			if (((Configurable) c).setAttribute(attrib.getName(), attrib
					.getValue())) {
				return true;
			}
		}
		// not clear the following code does anything useful
//		if ("direction".equals(attrib.getName().toLowerCase())) {
//			if (processDirection(((String) attrib.getValue())))
//				return true;
//			
//
//		}

		// This is what causes duplicate labels in checkbox
		// Check if the component has a property corresponding to name
		// this causes heavy inefficiency as toolkit widgets are introspected
		// am removing it and calling dummySetProperty instead
		if (PropertySetter.dummySetProperty(getUIComponent(), attrib.getName()
				.toLowerCase(), attrib.getValue()))
			return true;
		else
			return false;
	}

	// Abstract methods to be implemented by
	// actual widgetAdapters.
	public void setUIComponentValue(Object newValue) {
		// any subtle problems here?
		// why make this check when caller probably has
		// if (Misc.equals(getUIComponentValue(), newValue)) return;
		// System.out.println("uiWidgetAdapter:setUIComponentValue " +
		// newValue);
		// Translate to required type and
		// pass value on to setUIComponentTypedValue()
		// System.out.println("setUI Comp Value" + newValue);
		if (adapter == null)
			return;
		
		// if (objectAdapter != null && objectAdapter.isEdited()) return;
		if (adapter.isEdited())
			return;
		
		Object temp = null;
		if (notInRange(newValue)) {
			//temp = "";
			SubrangeErrror.newCase(newValue, getObjectAdapter().getBeautifiedPath(), this);
//			Tracer.error("Value: " + newValue + " of " + getObjectAdapter().getBeautifiedPath() + " not in range.");
//			temp = null;
//			newValue = null;
		}
		else {
		try {
			// System.out.println("calling registry" + getType() + "to" +
			// newValue);
			temp = TranslatorRegistry.convert(getType(), newValue,
					getObjectAdapter());
		} catch (FormatException e) {
			return;
		}
		}
		if (temp != null)
			setUIComponentTypedValue(temp);
		else
			setUIComponentTypedValue(newValue);
		// edited = false;
		if (getUIContainer() != null)
			getUIContainer().setUpdated();
	}

	// Return the type that this widget adaptor supports
	public String getType() {
		return "java.lang.Object";
	}

	public abstract void setUIComponentTypedValue(Object newValue);

	public abstract Object getUIComponentValue();

	// public abstract void setUIComponentEditable();
	// public abstract void setUIComponentUneditable();
	public void setUIComponentEditable() {
	}

	public void setUIComponentUneditable() {
	}

	public WidgetShell getUIContainer() {
		if (getObjectAdapter() == null)
			return null;

		if (genericWidget == null)
			// return genericWidget = getUIComponent().getParent();
			return getObjectAdapter().getGenericWidget();
		else
			return genericWidget;
		// return genericWidget;
	}

	/*
	 * public Component getGenericWidget () { return
	 * objectAdapter.getGenericWidget(); }
	 */
	// public abstract void setUIComponentSelected();
	/*
	 * public abstract void setUIComponentDeselected();
	 */
	/*
	 * Color oldColor = Color.white; public void setUIComponentSelected() { if
	 * (getUIComponent().getBackground() != SELECTION_COLOR) oldColor =
	 * getUIComponent().getBackground();
	 * getUIComponent().setBackground(SELECTION_COLOR); } public void
	 * setUIComponentDeselected() { getUIComponent().setBackground(oldColor);
	 * getUIComponent().repaint(); }
	 */
	Color oldColor = Color.white;

	// public Color getOriginalBackground (Component genericWidget) {
	public Object getOriginalBackground(VirtualComponent genericWidget) {

		if (genericWidget == null) {
			// System.out.println("returning white");
			return oldColor;
		}
		if (genericWidget.getBackground() !=  AttributeNames.getSelectionColor())
			return genericWidget.getBackground();
		// else return oldColor;
		else
			return getOriginalBackground(genericWidget.getParent());

	}

	protected Color oldUIComponentColor = Color.white;

	public void setUIComponentSelected() {
		// System.out.println ("Selected in uiWidgetAdapter");

		// Component genericWidget = getUIContainer();
		if (getUIContainer() == null)
			return;

		VirtualComponent genericWidget = getUIContainer().getContainer();
		if (genericWidget == null)
			return;
		oldColor = (Color) getOriginalBackground(genericWidget);
		genericWidget.setBackground(AttributeNames.getSelectionColor());
		/*
		 * oldUIComponentColor = getOriginalBackground(getUIComponent());
		 * getUIComponent().setBackground(SELECTION_COLOR);
		 */

		/*
		 * oldColor = getOriginalBackground (getUIComponent());
		 * getUIComponent().setBackground(SELECTION_COLOR);
		 */
		getUIComponent().repaint();
		// genericWidget.repaint();
	}

	public void setUIComponentDeselected() {
		// System.out.println("Delected in uiWIdgetAdapter");
		// Component genericWidget = getUIContainer();
		if (getUIContainer() == null)
			return;

		VirtualComponent genericWidget = getUIContainer().getContainer();
		if (genericWidget == null)
			return;
		// if (! (virtualGenericWidget instanceof AnAWTComponent)) return;

		// Component genericWidget = getUIContainer().getContainer();
		if (genericWidget.getBackground() == AttributeNames.getSelectionColor()) {
			genericWidget.setBackground(oldColor);
			// genericWidget.repaint();
		}
		/*
		 * if (getUIComponent().getBackground() ==
		 * uiWidgetAdapter.SELECTION_COLOR) {
		 * getUIComponent().setBackground(oldUIComponentColor);
		 * //genericWidget.repaint(); }
		 */
		getUIComponent().repaint();
		/*
		 * if (getUIComponent().getBackground() ==
		 * uiWidgetAdapter.SELECTION_COLOR) {
		 * getUIComponent().setBackground(oldColor);
		 * //getUIComponent().repaint(); getUIComponent().repaint(); }
		 */
	}

	public void setUIComponentSelected(ObjectAdapter[] child) {

	}

	public void setUIComponentDeselected(ObjectAdapter[] child) {

	}

	public abstract void linkUIComponentToMe(VirtualComponent c);

	/*
	 * public void linkUIComponentToMe(VirtualComponent c) { //if (c == null) c =
	 * getUIComponent();
	 *  }
	 */
	public void beginTransaction() {
		MethodInvocationManager.beginTransaction();
	}

	public void endTransaction() {
		MethodInvocationManager.endTransaction();
	}

	public Object invokeMethod(Object parentObject, String methodName,
			Object[] params) {
		return invokeMethod(parentObject, methodName, params, null, null);

	}

	public Object invokeMethod(Object parentObject, String methodName,
			Object[] params, ClassProxy retType, ClassProxy[] paramTypes) {
		return MethodInvocationManager.invokeMethod(getObjectAdapter()
				.getUIFrame(), parentObject, IntrospectUtility.getMethod(parentObject,
				methodName, retType, paramTypes), params, getObjectAdapter());

	}

	public String componentToText() {
		return this.getObjectAdapter().toTextLine();
	}

	public boolean processAtomicOperations(ObjectAdapter childAdapter) {
		ObjectAdapter parentAdapter = childAdapter.getParentAdapter();

		return processAtomicOperationsOfSelf(parentAdapter);
		/*
		 * if (parentAdapter.isAtomic()) {
		 * setUIComponentTypedValue(parentAdapter.getRealObject()); return true; }
		 * return false;
		 */

	}

	public boolean processAtomicOperationsOfSelf(ObjectAdapter myAdapter) {
		if (myAdapter.isAtomic()) {
			if (myAdapter.isRecursive())
				return false;
			setUIComponentValue(myAdapter.getRealObject());
			return true;
		}
		return false;

	}

	public void childComponentsAdditionStarted() {
		displayChildren.clear();

	}

	public void add(VirtualContainer parent, VirtualComponent comp,
			ObjectAdapter childAdapter) {
		if (!processAtomicOperations(childAdapter)) {
			parent.add(comp);
			LogicalChildComponentAdded.newCase(parent, comp, this);
//			System.out.println("added component:" + comp.getName() + " to:" +parent.getName());
			setParentContainer(parent);
			displayChildren.add(childAdapter);
		}
	}

	public void add(VirtualContainer parent, VirtualComponent comp, int pos) {
		parent.add(comp, pos);
		// setParentContainer (parent);

	}
	Vector<ObjectAdapter> displayChildren = new Vector();

	public void add(VirtualComponent comp, int pos) {
		if (getUIComponent() instanceof Container) {
			add((VirtualContainer) getUIComponent(), pos);
			
		}
	}
	public Vector<ObjectAdapter> getDisplayChildrenAdapters() {
		return displayChildren;
		
	}

	public void removeFromParentUIContainer() {

	}
	public  void remove(ObjectAdapter compAdapter) {
		
	}

	public void remove(VirtualContainer parent, VirtualComponent comp,
			ObjectAdapter childAdapter) {
		if (!processAtomicOperations(childAdapter)) {
			parent.remove(comp);
			setParentContainer(parent);
		}
	}

	public void remove(VirtualComponent comp, ObjectAdapter childAdapter) {
		if (getUIComponent() instanceof VirtualContainer)
			remove((VirtualContainer) this.getUIComponent(), comp, childAdapter);
	}

	public void remove(VirtualContainer parent, VirtualComponent comp) {
		parent.remove(comp);
		// setParentContainer (parent);

	}

	public void remove(VirtualComponent comp) {
		if (getUIComponent() instanceof VirtualContainer)
			remove((VirtualContainer) getUIComponent(), comp);
		// setParentContainer (parent);

	}

	public void remove(int pos) {
		if (getUIComponent() instanceof VirtualContainer)
			((VirtualContainer) getUIComponent()).remove(pos);
		// setParentContainer (parent);

	}

	public void remove(VirtualContainer parent, int index,
			ObjectAdapter childAdapter) {
		if (!processAtomicOperations(childAdapter)) {
			parent.remove(index);
		}
	}
	
	

	public void remove(int index, ObjectAdapter childAdapter) {
		if (index < 0)
			return;
		if (getUIComponent() instanceof VirtualContainer)
			remove((VirtualContainer) getUIComponent(), index, childAdapter);
	}

	public void removeForReplacement(VirtualContainer parent, int index,
			ObjectAdapter childAdapter) {
		if (!processAtomicOperations(childAdapter)) {
			parent.remove(index);
		}
	}

	public void childComponentsAdded(boolean hasProperties) {
		if (hasProperties)
		processAtomicOperationsOfSelf(this.getObjectAdapter());
	}

	  public void descendentUIComponentsAdded() {
		  
	  }

	public void removeAllProperties(VirtualContainer widget) {
		if (getObjectAdapter().isAtomic())
			setUIComponentTypedValue(getObjectAdapter().getRealObject());
		else {
			widget.removeAll();
		}
	}

	public void removeLast() {
		if (getUIComponent() instanceof VirtualContainer) {
			VirtualContainer container = (VirtualContainer) getUIComponent();
			container.remove(container.getComponentCount() - 1);
		}
	}

	public void removeAll() {
		if (getUIComponent() instanceof VirtualContainer)
			removeAllProperties((VirtualContainer) getUIComponent());
		/*
		 * if (getObjectAdapter().isAtomic())
		 * setUIComponentTypedValue(getObjectAdapter().getRealObject()); else {
		 * widget.removeAll(); }
		 */
	}

	public boolean uiIsContainer() {
		return getUIComponent() instanceof VirtualContainer;
	}

	public void invalidate() {
		if (getUIComponent() != null)
			getUIComponent().invalidate();
	}

	public void removeForReplacement(int index, ObjectAdapter childAdapter) {
//		if (childAdapter.isVisible())
		removeForReplacement((VirtualContainer) getUIComponent(), index,
				childAdapter);
	}

	// Methods invoked on focus gain/loss in the view
	public void focusGained(VirtualFocusEvent e) {
		if (e.isTemporary())
			return;
		else {
			uiComponentFocusGained();
		}
	}

	public void focusLost(VirtualFocusEvent e) {
		if (e.isTemporary())
			return;
		else {
			uiComponentFocusLost();
		}
	}

	public boolean isEmpty() {
		return false;
	}
	public int defaultWidth() {
		return DEFAULT_COMPONENT_WIDTH;
	}
	public int defaultHeight() {
		if (adapter.isFlatTableCell())
			return DEFAULT_FLAT_TABLE_CELL_HEIGHT;
		else return 0;
	}
	public void setAttributes(VirtualComponent component) {
		setSize(component);
		setColors(component);
		

	}
	public void setAttributes() {
		setAttributes(getUIComponent());
	}
	public void setColors(VirtualComponent component) {
		// this was commented out
		setColors(component, adapter);

	
	}
//	public static void setColors(ObjectAdapter adapter, VirtualComponent component) {
//		Color background = adapter.getComponentBackground();
//		if (background != null)
//			component.setBackground(background);
//		Color foreground = adapter.getComponentForeground();
//		if (foreground != null)
//			component.setForeground(foreground);
//	
//	}
	// GenericWidgetShell also sets colors, maybe it should call this method
	public static void setColors(VirtualComponent component, ObjectAdapter adapter) {
		if (adapter == null || component instanceof VirtualTree) {
			return;
		}
		if (component instanceof VirtualContainer || component instanceof VirtualSlider)
			setContainerBackground( component, adapter);		
		else
			setComponentBackground(component, adapter);			
//		Color componentBackground = adapter.getComponentBackground();
//		Color containerBackground = adapter.getContainerBackground();
//		
//		if (componentBackground != null)
//			component.setBackground(componentBackground);
		Color foreground = adapter.getComponentForeground();
		if (foreground != null)
			component.setForeground(foreground);
	
	}
	public static void setComponentBackground(VirtualComponent component, ObjectAdapter adapter) {		
		Color componentBackground = adapter.getComponentBackground();
		if (componentBackground != null)
			component.setBackground(componentBackground);		
	
	}
	public static void setContainerBackground(VirtualComponent component, ObjectAdapter adapter) {	
//		if (adapter instanceof RootAdapter && adapter.getUIFrame().isDummy()) {
//			return;
//		}
		Color containerBackground = adapter.getContainerBackground();
		if (containerBackground != null && component != adapter.getUIFrame().getDrawPanel())
			component.setBackground(containerBackground);		
	
	}
	public static void setColors(VirtualContainer component, ObjectAdapter adapter) {
		Color background = adapter.getContainerBackground();
		if (background != null)
			component.setBackground(background);
		Color foreground = adapter.getComponentForeground();
		if (foreground != null)
			component.setForeground(foreground);
	
	}
	public static void setSize(VirtualComponent component, Integer width, Integer height) {
		
		if (width != null && height != null && width > 0  && height > 0) {
			OEMisc.setSize(component, width, height);
			return;
		}
  		if (width != null && width != 0)
  			OEMisc.setWidth(component, width);
  		//int height = adapter.getComponentHeight();
  		if (height != null && height != 0)
  			OEMisc.setHeight(component, height);
	}
	protected boolean instantiatedComponent = false;
	static boolean isContainer (VirtualComponent component) {
		return (component instanceof VirtualContainer) && 
				(!(component instanceof VirtualComboBox) )
				&& (!(component instanceof VirtualCheckBox) )
				&& (!(component instanceof VirtualLabel));
	}
	public void setSize(VirtualComponent component) {
		//VirtualComponent component = getUIComponent();
		// if someone else created it, they should have set its size
		if (!instantiatedComponent)
			return;
		Integer x = null;
		Integer y = null;
		Integer width;
		Integer height;
		VirtualComponent maybeScrolled = component;
		if (component instanceof VirtualScrollPane) {
			 maybeScrolled = ((VirtualScrollPane) component).getScrolledComponent();
		}
		//if (component instanceof VirtualContainer) {
		if (isContainer(maybeScrolled)) {
//		if (maybeScrolled instanceof VirtualContainer) {
			width  = adapter.getContainerWidth();
			height = adapter.getContainerHeight();
			
		}
		else 
		{
			width = adapter.getComponentWidth();
			height = adapter.getComponentHeight();
		}
		if (adapter.getWidgetShell() == null || !adapter.getCreateWidgetShell()){
//			||
				// this stops recursion
//				component == adapter.getWidgetShell().getContainer()) {
		x = adapter.getComponentX();
		y = adapter.getComponentY();
		} 
//		else {
//			VirtualContainer container = adapter.getWidgetShell().getContainer();
//			setSize(container);
//
//		}
//		if ( (x == null|| y == null) & width == null && height == null && maybeScrolled instanceof VirtualContainer && !(maybeScrolled instanceof VirtualComboBox))

		if ( (x == null|| y == null) & width == null && height == null && isContainer(maybeScrolled ))
			return;
		if ( x != null && y != null & width == null && height == null ) {
			component.setLocation(x, y);
			return;
		}
		
		if (width == null)
			width = defaultWidth();
		if (height == null)
			height = defaultHeight();
		
		if (width > 0 && height > 0) {
			if (x != null && y != null)
				component.setBounds(x, y, width, height);
			else
			    OEMisc.setSize(component, width, height);
			return;
		}
		if (width > 0)
//  		if (width != 0)
  			OEMisc.setWidth(component, width, x, y);
  		//int height = adapter.getComponentHeight();
		if (height > 0)
//  		if (height != 0)
  			OEMisc.setHeight(component, height, x, y);
	}
	
	public boolean delegateSelectionToWidgetShell() {
		return true;
	}
	  public boolean delegateOpenToWidgetShell() {
		  return true;
	  }
	protected  boolean notInRange (Object newVal) {
		if (newVal == null) return true;
//		  if (!(newVal instanceof Integer)) return false;
		  if (!IntrospectUtility.isIntFamily(getObjectAdapter().getPropertyClass())) return false;
		  Number number = (Number) newVal;
		  long intVal = number.longValue();
		  
		  //int intVal = (Integer) newVal;
		 
		  
		  /*
		  if (getObjectAdapter() == null)
			  return false;
			  */
		  Long maxVal = (Long) getObjectAdapter().getMaxValue();
		  Long minVal = (Long) getObjectAdapter().getMinValue();
		  if (maxVal == null && minVal == null)
			  return false;
		  if (minVal == null)
			  return intVal > maxVal;
		  else if (maxVal == null)
			  return intVal < minVal;		  
		  return intVal < minVal || intVal > maxVal;
		  
	  }
	 public  void userInputUpdated(boolean newVal) {
		 
	 }
	 boolean incrementalChildAddition;
	 public void setIncrementalChildAddition(boolean newVal) {
		 incrementalChildAddition = newVal;
	 }
	 public boolean getIncrementalChildAddition() {
		 return incrementalChildAddition;
	 }
	@Override
	public void processDeferredFillColumnTitlePanel(CompositeAdapter adapter) {
		// TODO Auto-generated method stub
		return;
	}
	@Override
	public void refillColumnTitle(CompositeAdapter firstRowAdapter) {
		
	}
	@Override
	public void add(ObjectAdapter compAdapter) {
		displayChildren.add(compAdapter);
		
	}
	@Override
	public void rebuildPanel() {
		
	}
	public boolean needChildrenObjectAdapters() {
		return true;
	}
	
	public boolean hasCommands() {
		return false;
	}

}
