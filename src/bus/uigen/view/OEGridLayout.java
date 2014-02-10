package bus.uigen.view;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import bus.uigen.ars.*;

public class OEGridLayout implements LayoutManager, java.io.Serializable {
    int hgap;
    int vgap;
    int rows;
    int cols;	//public static final int  DEFAULT_HGAP = 5;	//public static final int DEFAULT_VGAP = 15;
	public static final int  DEFAULT_HGAP = 0;
	public static final int DEFAULT_VGAP = 0;
    /**
     * Creates a grid layout with a default of one column per component,
     * in a single row.
     */
    public OEGridLayout() {
	this(1, 0, 0, 0);
    }

    /**
     * Creates a grid layout with the specified rows and columns.
     * @param rows the rows
     * @param cols the columns
     */
    public OEGridLayout(int rows, int cols) {
	this(rows, cols, 0, 0);
    }

    /**
     * Creates a grid layout with the specified rows, columns,
     * horizontal gap, and vertical gap.
     * @param rows the rows; zero means 'any number.'
     * @param cols the columns; zero means 'any number.'  Only one of 'rows'
     * and 'cols' can be zero, not both.
     * @param hgap the horizontal gap variable
     * @param vgap the vertical gap variable
     * @exception IllegalArgumentException If the rows and columns are invalid.
     */
    public OEGridLayout(int rows, int cols, int hgap, int vgap) {
    	//hgap = DEFAULT_HGAP;
    	//vgap = DEFAULT_VGAP;
	if ((rows == 0) && (cols == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.rows = rows;
	this.cols = cols;
	this.hgap = hgap;
	this.vgap = vgap;
    }

    /**
     * Returns the number of rows in this layout.
     */
    public int getRows() {
	return rows;
    }

    /**
     * Sets the number of rows in this layout.
     * @param rows number of rows in this layout
     */
    public void setRows(int rows) {
	if ((rows == 0) && (this.cols == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.rows = rows;
    }

    /**
     * Returns the number of columns in this layout.
     */
    public int getColumns() {
	return cols;
    }

    /**
     * Sets the number of columns in this layout.
     * @param cols number of columns in this layout
     */
    public void setColumns(int cols) {
	if ((cols == 0) && (this.rows == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.cols = cols;
    }

    /**
     * Returns the horizontal gap between components.
     */
    public int getHgap() {
	return hgap;
    }
    
    /**
     * Sets the horizontal gap between components.
     * @param hgap the horizontal gap between components
     */
    public void setHgap(int hgap) {
	this.hgap = hgap;
    }
    
    /**
     * Returns the vertical gap between components.
     */
    public int getVgap() {
	return vgap;
    }
    
    /**
     * Sets the vertical gap between components.
     * @param vgap the vertical gap between components
     */
    public void setVgap(int vgap) {
	this.vgap = vgap;
    }

    /**
     * Adds the specified component with the specified name to the layout.
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout. Does not apply.
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp) {
    }

    /** 
     * Returns the preferred dimensions for this layout given the components
     * int the specified panel.
     * @param parent the component which needs to be laid out 
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container parent) {
	Insets insets = parent.getInsets();
	int ncomponents = parent.getComponentCount();
	int nrows = rows;
	int ncols = cols;

	if (nrows > 0) {
	    ncols = (ncomponents + nrows - 1) / nrows;
	} else {
	    nrows = (ncomponents + ncols - 1) / ncols;
	}
	int w = 0;
	int h = 0;

	for (int r = 0; r < nrows; r++) {
	  int tw = 0;
	  for (int c = 0; c < ncols; c++) {
	    int i = r*ncols+c;
	    if (i < ncomponents) {
	      tw+= parent.getComponent(i).getPreferredSize().width;
	    }
	  }
	  if (w < tw) {
	    w = tw;
	  }
	}

	for (int c = 0; c < ncols; c++) {
	  int th = 0;
	  for (int r = 0; r < nrows; r++) {
	    //int i = c*nrows+r;
	    int i = r*ncols+c;
	    if (i < ncomponents) {
	      th+= parent.getComponent(i).getPreferredSize().height;
	    }
	  }
	  if (h < th) {
	    h = th;
	  }
	}	/*
	return new Dimension(insets.left + insets.right + w + (ncols-1)*hgap, 
			     insets.top + insets.bottom + h + (nrows-1)*vgap);
    */
		return new Dimension(insets.left + insets.right + w + (ncols)*hgap, 
			     insets.top + insets.bottom + h + (nrows)*vgap);
		}

    /**
     * Returns the minimum dimensions needed to layout the components 
     * contained in the specified panel.
     * @param parent the component which needs to be laid out 
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent) {
	Insets insets = parent.getInsets();
	int ncomponents = parent.getComponentCount();
	int nrows = rows;
	int ncols = cols;

	if (nrows > 0) {
	    ncols = (ncomponents + nrows - 1) / nrows;
	} else {
	    nrows = (ncomponents + ncols - 1) / ncols;
	}
	int w = 0;
	int h = 0;

	for (int r = 0; r < nrows; r++) {
	  int tw = 0;
	  for (int c = 0; c < ncols; c++) {
	    int i = r*ncols+c;
	    if (i < ncomponents) {
	      tw+= parent.getComponent(i).getMinimumSize().width;
	    }
	  }
	  if (w < tw) {
	    w = tw;
	  }
	}

	for (int c = 0; c < ncols; c++) {
	  int th = 0;
	  for (int r = 0; r < nrows; r++) {
	    //int i = c*nrows+r;
	    int i = r*ncols+c;
	    if (i < ncomponents) {
	      th+= parent.getComponent(i).getMinimumSize().height;
	    }
	  }
	  if (h < th) {
	    h = th;
	  }
	}
		/*
	return new Dimension(insets.left + insets.right + w + (ncols-1)*hgap, 
			     insets.top + insets.bottom + h + (nrows-1)*vgap);
    */
	return new Dimension(insets.left + insets.right + w + (ncols)*hgap, 
			     insets.top + insets.bottom + h + (nrows)*vgap);	}

    /** 
     * Lays out the container in the specified panel.  
     * @param parent the specified component being laid out
     * @see Container
     */
    public void layoutContainer(Container parent) {
	Insets insets = parent.getInsets();
	int ncomponents = parent.getComponentCount();
	int nrows = rows;
	int ncols = cols;

	if (ncomponents == 0) {
	    return;
	}
	if (nrows > 0) {
	    ncols = (ncomponents + nrows - 1) / nrows;
	} else {
	    nrows = (ncomponents + ncols - 1) / ncols;
	}
	
        //System.out.println("lc nrows, ncols="+nrows+","+ncols);
	int w=0, h=0;
	
	//for (int c = 0, x = insets.left ; c < ncols ; c++, x += w + hgap) {	for (int c = 0, x = insets.left + hgap; c < ncols ; c++, x += w + hgap) {
	    //for (int r = 0, y = insets.top ; r < nrows ; r++, y += h + vgap) {
		for (int r = 0, y = insets.top + vgap ; r < nrows ; r++, y += h + vgap) {		int i = r * ncols + c;
		if (i < ncomponents) {
		  Dimension d = parent.getComponent(i).getPreferredSize();
                  //System.out.println("Preferred dimension of c"+i+" is "+d);
		  w = d.width;
		  h = d.height;
		  Component cmp = parent.getComponent(i);
		  //System.out.println("Setting component "+i+"'s bounds to x="+x+", y="+y+", w="+w+", h="+h);
		  cmp.setBounds(x, y, w, h);
		  if (cmp instanceof Container) {
		  cmp.doLayout();
		  }
		}
	    }
	}
    }
    
    /**
     * Returns the String representation of this uiGridLayout's values.
     */
    public String toString() {
	return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + 
	    			       ",rows=" + rows + ",cols=" + cols + "]";
    }
}
