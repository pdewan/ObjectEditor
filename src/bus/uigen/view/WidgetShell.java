package bus.uigen.view;

import javax.swing.border.Border;

import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualLabel;

public interface WidgetShell {

	public void setAllowBorder(boolean newVal);

	public boolean getAllowBorder();

	public void setBorder(Border newVal);

	public void setObjectAdapter(ObjectAdapter newValue);

	public ObjectAdapter getObjectAdapter();

	public void elideHandle();

	public void init(VirtualContainer theContainer);

	public void invalidate();

	public VirtualContainer getContainer();
	
	public void processAttributes();
	public void setLabelWidth();
	
	public void cleanUpForReuse() ;
	public void refreshElideComponent();

	/*
	 public Container getPhysicalContainer() {
	 return container;
	 }
	 */
	public VirtualContainer getParent();

	public void elide();

	public boolean internalElideWithoutHandle();

	public boolean internalElide();

	public void expand();

	public void subExpand();

	public boolean isElided();

	public boolean isExplicitlyElided();
	public boolean redoExpand();

	public boolean internalExpand();

	public boolean expandElidedString();

	public void toggleElide();

	public void subToggleElide();

	public String getLabel();

	public void setLabel(String l);

	public void setEdited(boolean edited);

	public void editedUIComponentFocusLost();

	public void editedUIComponentFocusGained();

	public boolean isEdited();

	public void setEdited();

	public void setUpdated();

	public void setPreRead();

	public void setPreWrite();

	public boolean isLabelVisible();

	public String toString();

	public void showColumnTitle(String title);

	public void showColumnTitle();

	public void hideColumnTitle();

	public void setLabelVisible(boolean v);

	public void setComponent(VirtualComponent nc);

	/*
	 public Frame getFrame(Component c) {
	 //System.out.println("parameter" +c);
	 if (f == null) {
	 //System.out.println("null f");
	 while (c != null) {
	 
	 //System.out.println("componnet" + c.getName());
	 if (c instanceof Frame) {
	 f = (Frame) c;
	 uiF = (uiFrame) f;
	 return f;
	 }
	 c = c.getParent();
	 }
	 }
	 //System.out.println("returning" + f);
	 return f;
	 }
	 */
	/*
	 public uiFrame getUIFrame(Component c) {
	 if (uiF == null)
	 uiF = (uiFrame) getFrame(c);
	 //System.out.println("uif = " + uiF);
	 return uiF;
	 }
	 public uiFrame getUIFrame() {
	 if (uiF == null)
	 if (f == null)
	 uiF = (uiFrame) getFrame(this);
	 
	 //System.out.println("uif = " + uiF);
	 return uiF;
	 }
	 */
	/*
	 public uiFrame getUIFrame(Component c) {
	 return getUIFrame();
	 
	 }
	 */
	public uiFrame getUIFrame();

	public void setParentContainer(VirtualContainer parent);

	VirtualLabel getElideHandle();

	void hideElideHandle();

	void showElideHandle();

}