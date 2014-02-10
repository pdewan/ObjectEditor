package bus.uigen.controller.menus;
/*
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuContainer;
import java.awt.MenuItem;
*/
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import util.models.ADynamicSparseList;


import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.ExtendedMenu;
import bus.uigen.trace.MenuGroupCreated;
import bus.uigen.widgets.VirtualMenu;
import bus.uigen.widgets.VirtualMenuBar;
import bus.uigen.widgets.VirtualMenuComponent;
import bus.uigen.widgets.VirtualMenuContainer;
import bus.uigen.widgets.VirtualMenuItem;

public class AMenuGroup {
	VirtualMenuComponent  menu;
	String fullName;
	AMenuDescriptor menuDescriptor;
	Hashtable<String, AMenuGroup> children = new Hashtable();	
	public VirtualMenuComponent getMenu() {
		return menu;
	}
	/*
	public void setMenu(Menu theMenu) {
		menu = theMenu;
	}
	*/
	public String getFullName() {
		return fullName;
		
	}
	public  AMenuGroup(String theFullName, VirtualMenuComponent theMenu, AMenuDescriptor theMenuDescriptor) {
		fullName = theFullName;
		menuDescriptor = theMenuDescriptor;
		menu = theMenu;
		MenuGroupCreated.newCase(theFullName, theMenu, theMenuDescriptor, this);
	}
	
	public void putSubMenuGroup (String childName, AMenuGroup childMenu) {
		//if (children.contains(childName)) return;
		children.put(childName, childMenu);
		if (!unsortedChildren.contains(childName))
			unsortedChildren.add(childName);
	}
	public AMenuGroup getSubMenuGroup (String childName) {
		return children.get(childName);
	}
	
	ADynamicSparseList<String> sortedChildren = new ADynamicSparseList();
	Vector<String> unsortedChildren = new Vector();
	void sortChildren () {
		//Enumeration<String> names = children.keys();
		Enumeration<String> names = unsortedChildren.elements();
		//ADynamicSparseList<String> sortedChildren = new ADynamicSparseList();
		//Vector<String> unsortedChildren = new Vector();
		while (names.hasMoreElements()) {
			String childName = names.nextElement();
			//if (!unsortedChildren.remove(arg0))
			String childFullName = childFullName (childName);
			Object position = menuDescriptor.getAttribute(childFullName, AttributeNames.POSITION);	
			/*
			if (position == null)
				unsortedChildren.add(childName);
			else
			*/
			if (position != null) {
				if ((Integer) position < 0) { // help or some other end menu, might want to index menus from right
					unsortedChildren.remove(childName);
					unsortedChildren.add(childName);
				} else
				sortedChildren.setOrInsertNewElementAbove((Integer) position, childName) ;
				// the enumeration does not seem to make a copy of the vector
				//unsortedChildren.removeElement(childName);
			}
			
		}
		
		for (int i = 0; i < sortedChildren.size(); i++) {
			Object nextChild = sortedChildren.get(i);
			if (nextChild != null)
				unsortedChildren.remove(nextChild);
		}
		
	}
	public static void add (VirtualMenuContainer menu, VirtualMenuComponent childMenu) {
		if (childMenu.getParent() != null) return;
		if (menu instanceof VirtualMenuBar && childMenu instanceof VirtualMenu)
			((VirtualMenuBar) menu).add((VirtualMenu)childMenu);
		else if (menu instanceof VirtualMenu && childMenu instanceof VirtualMenuItem)
			((VirtualMenu) menu).add((VirtualMenuItem)childMenu);
		else
			System.out.println("E**: Could not add:" + childMenu.getClass() + " to" + menu.getClass());
	}
	public static void add (VirtualMenuContainer menu, String item) {
		
		if (menu instanceof VirtualMenu) {
			
			((VirtualMenu) menu).add(item);
		}
	}
	String childFullName (String childName)	{
		String childFullName =  childName;
		if (fullName != "")
		 childFullName = fullName + uiFrame.MENU_NESTING_DELIMITER + childName;
		return childFullName;
	}
	void displayChild (String childName) {
		if (childName == null)
			return;
		String childFullName =  childFullName(childName);
		/*
		String childFullName =  childName;
		if (childName != "")
		 childFullName = fullName + uiFrame.MENU_NESTING_DELIMITER + childName;
		 */
		AMenuGroup childMenuGroup = children.get(childName);
		VirtualMenuComponent childMenu = childMenuGroup.getMenu(); 
		//Object position = menuDescriptor.getAttribute(childFullName, AttributeNames.POSITION);	
		Object label_below = menuDescriptor.getAttribute(childFullName, AttributeNames.LABEL_BELOW);	
		VirtualMenuComponent cMenu = null;
		if (! (childMenu instanceof VirtualMenuComponent)) {
			//childMenuGroup.displayMenuTree();
			return;
		}
		//cMenu = (Menu) childMenu;
		if (childMenu.getParent() != null) {
			childMenuGroup.displayMenuTree();
			return;
		}
		if (! (menu instanceof VirtualMenuContainer)) return;
		VirtualMenuContainer myContainer = (VirtualMenuContainer) menu;
		
		//if (childMenu instanceof Menu)
		//add (menu, (Menu) childMenu);
		add (myContainer, childMenu);
		if (label_below != null)
				add (myContainer, (String) label_below);
		childMenuGroup.displayMenuTree();
	}
	public void checkPreInMenuTree() {
		if (children.size() == 0) {
			if (menu.getUserObject() instanceof VirtualMethodMenuItem) {
				((VirtualMethodMenuItem) menu.getUserObject()).checkPre();
			}
		} else {
			Enumeration<AMenuGroup> elements = children.elements();
			while (elements.hasMoreElements())
				elements.nextElement().checkPreInMenuTree();
		}
	
			
		
		
	}
	public void registerFullNamesInMenuTree() {
		if (children.size() == 0) {		
			if (menu.getUserObject() instanceof VirtualMethodMenuItem) {
				((VirtualMethodMenuItem) menu.getUserObject()).registerFullName(getFullName());
			}
		} else {
			Enumeration<AMenuGroup> elements = children.elements();
			while (elements.hasMoreElements())
				elements.nextElement().registerFullNamesInMenuTree();
		}
	}
	public void displayMenuTree () {
		if (children.size() == 0) return;
		sortChildren();
		for (int i = 0; i < sortedChildren.size(); i++) {
			displayChild (sortedChildren.get(i));
		}
		for (int i = 0; i < unsortedChildren.size(); i++) {
			displayChild (unsortedChildren.elementAt(i));
		}
			
		/* 
		Enumeration<String> names = children.keys();
		
		while (names.hasMoreElements()) {
			String childName = names.nextElement();
			//Menu childMenu = new uiExtendedMenu(childName); 
			String childFullName = fullName + uiFrame.MENU_NESTING_DELIMITER + childName;
			AMenuGroup childMenuGroup = children.get(childName);
			MenuContainer childMenu = childMenuGroup.getMenu(); 
			Object position = menuDescriptor.getAttribute(childFullName, AttributeNames.POSITION);	
			Object label_below = menuDescriptor.getAttribute(childFullName, AttributeNames.LABEL_BELOW);	
			if (position == null) {
				menu.add(childMenu);
				if (label_below != null)
					menu.add((String) label_below);
			} else {
				menu.insert(childMenu, (Integer) position);
				if (label_below != null)
					menu.insert((String) label_below, (Integer) position + 1);
			}
			//maybe someone else will recurse
			childMenuGroup.displayMenuTree();
		}
		
	}
	*/
	}

}
