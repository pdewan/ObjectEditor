package bus.uigen.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.util.Hashtable;

import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.awt.AWTComponent;
import bus.uigen.widgets.awt.AWTContainer;

import slc.SLComposer;

public class SLComposerProxy extends AWTContainer  {
	//Container getContainer();
	SLComposer composer;
	public SLComposerProxy(SLComposer theComposer) {
		super (theComposer.getView().getContainer());
		composer = theComposer;
		
	}
	public Container getContainer() {
		return (Container) component;
	}
	public void setContainer (Container theContainer) {
		component = theContainer;
	}
	public void add (VirtualComponent c) {
		//if (c instanceof AnAWTComponent) {
			getContainer().add(((AWTComponent) c).getAWTComponent());
			c.setParent(this);
		//}		
	}
	public void add (VirtualComponent c, int pos) {
		if (c instanceof AWTComponent) {
			getContainer().add(((AWTComponent) c).getAWTComponent(), pos);
			c.setParent(this);
		}		
	}
	public void add (VirtualComponent c, String direction) {
		getContainer().add((Component) c.getPhysicalComponent(), direction ) ;
	}
	public void add (VirtualComponent c, Object constraint) {
		getContainer().add(((AWTComponent) c).getAWTComponent(), constraint);
		c.setParent(this);
		
	}
	public void add (VirtualComponent c, Object constraint, int pos) {
		getContainer().add(((AWTComponent) c).getAWTComponent(), constraint, pos);
		c.setParent(this);
		
	}
	public void remove (VirtualComponent c) {
		//if (c instanceof AnAWTComponent) {
			getContainer().remove(((AWTComponent) c).getAWTComponent());
			//c.setParent(null);
		//}		
	}
	public void remove ( int pos) {
			//VirtualComponent c = this.getComponent(pos);
		
			getContainer().remove( pos);
			//c.setParent(null);
			
		
	}
	public void removeAll () {
		getContainer().removeAll();
	}
	public int getComponentCount() {
		return getContainer().getComponentCount();
	}
	public int countComponents() {
		return getContainer().countComponents();
	}
	
	
	public void setLayout (Object layoutManager) {
		getContainer().setLayout((LayoutManager) layoutManager);
	}
	public void setLayout (LayoutManager layoutManager) {
		getContainer().setLayout(layoutManager);
	}
	public Object getLayout() {
		return getContainer().getLayout();
	}
	
	public VirtualComponent[] getComponents() {
		Component[] components = getContainer().getComponents();
		VirtualComponent[] retVal = new VirtualComponent[components.length];
		for (int i = 0; i < components.length; i++)
			retVal[i] = AWTComponent.virtualComponent(components[i]);
		return retVal;
	}
	public VirtualComponent getComponent(int pos) {
		return AWTComponent.virtualComponent(getContainer().getComponents()[pos]);
	}
	
	//static transient Hashtable<Container, VirtualContainer> getContainer()sToVirtualContainers = new Hashtable();	
	public static VirtualContainer virtualContainer (Container c) {	
		return (VirtualContainer) AWTComponent.virtualComponent(c);
		/*
		if (c == null) return null;
		VirtualComponent vcomp = AnAWTComponent.existingVirtualComponent(c);
		if (c != null)
			return (VirtualContainer) vcomp;
		VirtualContainer vgetContainer() = new AnAWTContainer(c);
		AnAWTComponent.register (c, vgetContainer());
		*/
		/*
			getContainer()sToVirtualContainers.get(c);
		if (vc == null) {
			vc = new AnAWTContainer(c);
			getContainer()sToVirtualContainers.put(c, vc);			
		}
		*/
		//return vgetContainer();
			
	}
	
	

}
