package bus.uigen.adapters;

import java.awt.Container;

import bus.uigen.WidgetAdapter;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.view.OEGridLayout;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.awt.AWTContainer;

public abstract class AnAWTWidgetAdapter extends WidgetAdapter  {
	 public boolean processDirection(String direction) {
		   VirtualComponent c = getUIComponent();
		   if (! (c instanceof AWTContainer)) return false;
		   if (c == null) return true;
	    
	      try {
		Container cn =  (Container)((AWTContainer) c).getAWTComponent();
		
		int count = cn.getComponentCount();
		OEGridLayout lm = (OEGridLayout) cn.getLayout();
		if (AttributeNames.HORIZONTAL.equals(direction)) {
		  if (lm.getRows() == 1)
		    return true;
		  else
		    //cn.setLayout(new uiGridLayout(1, count));
			  cn.setLayout(new OEGridLayout(1, count, OEGridLayout.DEFAULT_HGAP, 0));
		} else {
		  if (lm == null) return false;
		  if(lm.getColumns() == 1)
		    return true;
		  else
		    //cn.setLayout(new uiGridLayout(count, 1));
			  cn.setLayout(new OEGridLayout(count, 1, 0, OEGridLayout.DEFAULT_VGAP));
		}
	      } catch (Exception e) {return false;}
	    return false;
		  
	  }

}
