package bus.uigen.view;import bus.uigen.view.OEGridLayout;import bus.uigen.widgets.LayoutManagerFactory;import java.awt.LayoutManager;

public class OEGridLayoutFactory implements LayoutManagerFactory {	//static int id;
	public LayoutManager createLayoutManager () {		/*
		Container panel = new JPanel();		panel.setName("" + getNewID());
		//panel.setBackground(Color.white);
		return panel;		//return new Panel();		//return new JPanel();		 *		 */		return createuiGridLayout();
	}	 	public static LayoutManager createuiGridLayout () {		LayoutManager layout = new OEGridLayout();		//layout.setName("" + getNewID());		//panel.setBackground(Color.white);		return layout;		//return new Panel();		//return new JPanel();	}	  
 }
