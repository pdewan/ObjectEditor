package bus.uigen;

import java.awt.Component;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JComponent;

import bus.uigen.controller.models.InteractiveMethodInvoker;
import bus.uigen.controller.models.MethodInvocationFrameCreationListener;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualDimension;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.VirtualPoint;

public interface OEFrame {
	public static final String SUPPRESS_NOTIFICATION_PROCESSING = "Suppress  Notification Processing";
//	public void addMenuObject(Object menuObject);
	public VirtualContainer getDrawPanel() ;
	public Component getDrawComponent();
	public VirtualComponent getDrawVirtualComponent();
	public void showToolBar();
	public void hideToolBar();
	public void showDrawPanel() ;
	public void hideMainPanel();
	public void showTreePanel();
	public void setSize(int newWidth, int newHeight);
	public void setTitle(String newVal);
	public void setLocation(int newX, int newY);
	public void setFullRefreshOnEachOperation(boolean newVal);
	public boolean getFullRefreshOnEachOperation();
	public void refresh();
	public  void select (Object object, int index);
	public  void select (Object object, String property);
	public void setDemoFont();
	public List<String> getSourceClassNames();
	public List<ClassProxy> getSourceClasses(); 
	public ObjectAdapter getObjectAdapterFromPath(String path);
	public VirtualFrame getFrame();
	public Object getGlassPaneModel();	
	public JComponent setGlassPaneModel(Object aGlassPaneModel);
	public JComponent setTelePointerModel(Object aGlassPaneModel);
	public  VirtualDimension getSize();
	public VirtualPoint getLocation();
	public void dispose();
	public void addMethodInvocationFrameCreationListener (MethodInvocationFrameCreationListener aListener) ;
	 public  void notifyMethodInvocationFrameCreated (OEFrame aParentFrame, OEFrame anInvocationFrame, InteractiveMethodInvoker anInteractiveMethodInvoker);
	ObjectAdapter getObjectAdapter(Object anObject);	
	 public  void setGraphicsWindowLocked( boolean newVal);
	boolean isSuppressPropertyNotifications();
	void setSuppressPropertyNotifications(boolean suppressPropertyNotifications);
	void setAutoExitEnabled(boolean newVal);
	boolean getAutoExitEnabled();
	void addWindowListener(WindowListener newVal);

}
