package bus.uigen.editors;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.event.TreeSelectionEvent;

import bus.uigen.uiGenerator;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.LabelSelector;
import bus.uigen.widgets.PanelSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.tree.VirtualTree;

public class TreeBrowserAdapter extends TreeAdapter {
		VirtualContainer childPanel, parentPanel;
	public VirtualComponent instantiateComponent(Class cclass, ObjectAdapter adapter) {
    try {	  //rootTreeNode = this.getObjectAdapter();
	  //frame = this.getObjectAdapter().getUIFrame();
		//return createJTree();		
		jTree = (VirtualTree) cclass.newInstance(); 
		//parentPanel = new JPanel();
		parentPanel = PanelSelector.createPanel();		parentPanel.setLayout(new BorderLayout());		//parentPanel.setLayout(new RowColumnLayout());
		//childPanel = new JPanel();
		childPanel = PanelSelector.createPanel();		//childPanel.add(new JLabel("Details of selection in left panel shown here"));
		childPanel.add(LabelSelector.createLabel ("Details of selection in left panel shown here"));
		parentPanel.add(jTree, BorderLayout.CENTER);		parentPanel.add(childPanel, BorderLayout.EAST);	  //return (Container) cclass.newInstance();
		//return jTree;		return parentPanel;				//return new JTree();
      //return treePanel;
    } catch (Exception e) {
      e.printStackTrace();
      //return new JPanel();
      return PanelSelector.createPanel();
    }
  }	public void linkUIComponentToMe(Component component) {
		super.linkUIComponentToMe(jTree);
		//uiGenerator.generateInUIPanel(frame, new Integer(5), childPanel);				/*
		rootTreeNode = this.getObjectAdapter();
		frame = this.getObjectAdapter().getUIFrame();		//initJTree((JTree) component);		initJTree();
		*/
	}
	public void valueChanged(TreeSelectionEvent e)  {		super.valueChanged(e);
		childPanel.removeAll();		uiGenerator.generateInContainer(frame, 				((ObjectAdapter) SelectionManager.getCurrentSelection()).getRealObject(), childPanel);			//parentPanel.invalidate();		parentPanel.validate();
	}		
      
    
	

}
