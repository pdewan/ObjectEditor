package bus.uigen.view;
import bus.uigen.uiFrame;import bus.uigen.controller.AToolbarManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualScrollPane;

import java.util.Vector;import java.awt.Container;import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public interface TopViewManager /*extends Frame*/ {
		
	public void init (uiFrame theFrame, AToolbarManager theToolbarManager);	
	public Vector containers();
	public VirtualContainer getTreeContainer();
	public VirtualContainer getSecondaryContainer();	public VirtualContainer getMainContainer();
	public VirtualContainer newContainer(int direction, VirtualComponent scrolledComponent);
	//public VirtualScrollPane newContainer(int direction, VirtualComponent scrolledComponent);
	public VirtualContainer newContainer(VirtualComponent scrolledComponent) ;	public VirtualContainer newSecondaryContainer();	public VirtualContainer newTreeContainer();
	public boolean isEmptyMainPanel();
	public void setTreeContainer(VirtualContainer c);
		public boolean drawPanelIsVisible();
	public void drawPanel();
	public void mainPanel();
	public void secondaryPanel();
	//public JToolBar getToolBar ();
	//public JButton addToolBarButton(String label,   Icon icon, VirtualMethod method, String place_toolbar, int pos);
	
	//public JButton addUIGenToolBarButton(String label, Icon icon,   Object obj);
	//public JButton addToolBarButton(Object targetObject, String label,   Icon icon, VirtualMethod method, String place_toolbar, int pos);	//public Container createManualToolbar();
	//public void createToolBar();
	
	public void toolBar();
	//public JPanel getToolPanel();
	public void showMainPanel();
	//public void removeToolBarButtons(uiObjectAdapter adapter);
	//public void addToolBarButtons(uiObjectAdapter   adapter);
	//public int toolBarCount();
	//public JButton addUIGenToolBarButton(String label, Icon icon,   Object obj);	//public void addUIFrameToolBarButton(String label,   Icon icon);
	//public JButton addUIFrameToolBarButton(String   label, Icon icon, ActionListener listener);
	
	
		public boolean emptyFrame() ;	
	public boolean maybeSetEmptyFrameSize();	public boolean frameIsToolBar();	public int numChars(VirtualContainer toolBar);
	public boolean maybeSetToolBarSize();
	public void setSize();
	public void setLocation();
	public void hideMainPanelWithoutRearranging();	public void hideMainPanel() ;
	public boolean mainPanelIsVisible() ;	
	
	public void showDrawPanel();	public void hideDrawPanel();	
	public void windowHistoryPanel();	public void treePanel();	public void setTreePanelIsVisible (boolean newVal);
	public void hideTreePanel() ;		public void showSecondaryScrollPane();	public void hideSecondaryScrollPane();	public boolean secondaryScrollPaneIsVisible() ;	public void showTreePanel();	/*
	public void clearTreeSelection();
	public void setJTreeSelectionPaths(TreePath[]   selectedPaths);
	*/	
	public void maybeShowJTree();
		
	public void hideToolBar() ;
	//public void useManualToolbar (boolean newVal);
	
	
	public void showToolBar();
	//public void addToBottomPanel(Container toadd) ;	//public void addToMiddlePanel(Container toadd) ;	public boolean treePanelIsVisible();
	
	public void setNonDefaultToolbarMethodFound(boolean newVal) ;
	public boolean getNonDefaultToolbarMethodFound () ;
	public void maybeShowToolbar();	}
