package bus.uigen.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import bus.uigen.oadapters.ObjectAdapter;

public class CustomTreeCellRender extends DefaultTreeCellRenderer {
	 @Override
	    public Component getTreeCellRendererComponent(JTree tree, Object value,
	            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
	        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
            ObjectAdapter aNode = (ObjectAdapter) value;
            setForeground(aNode.getComponentForeground());
            setOpaque(true);
            setBackground(aNode.getComponentBackground());
            Dimension aSize = getSize();
            Dimension aPreferredSize = getPreferredSize();
            String aText = aNode.getExplanation();
            double aWidth = aPreferredSize.getWidth();
            Integer aComponentWidth = aNode.getComponentWidth();
            if (aComponentWidth != null) {
            	aPreferredSize.width = aComponentWidth;
            	setPreferredSize(aPreferredSize);
            } 
//            else {
//            	aPreferredSize.width = 300;
//            	setPreferredSize(aPreferredSize);
//            }

            if (aText != null) {
            	setToolTipText(aText);
            }

//	        // Assuming you have a tree of Strings
//	        String node = (String) ((DefaultMutableTreeNode) value).getUserObject();
//
//	        // If the node is a leaf and ends with "xxx"
//	        if (leaf && node.endsWith("xxx")) {
//	            // Paint the node in blue
//	            setForeground(new Color(13, 57 ,115));
//	        }

	        return this;
	    }
}
