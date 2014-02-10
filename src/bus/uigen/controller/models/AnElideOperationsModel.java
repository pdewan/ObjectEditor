package bus.uigen.controller.models;

import java.awt.Cursor;
import java.util.Enumeration;
import java.util.Vector;

import util.annotations.Explanation;
import util.annotations.Visible;

import bus.uigen.AutomaticRefresh;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.uiFrame;
import bus.uigen.controller.SelectionManager;
//import bus.uigen.editors.ShapesAdapter;
import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
import bus.uigen.visitors.ElideAdapterVisitor;
import bus.uigen.visitors.ElideWithoutHandleAdapterVisitor;
import bus.uigen.visitors.ToggleElideAdapterVisitor;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AnElideOperationsModel extends AnAbstractOperationsModel implements FrameModel {
//	uiFrame frame;
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//	}
	@Explanation ("Condenses the display of the selected object")
	public void elide() {
		ObjectAdapter selection = (ObjectAdapter) SelectionManager.getCurrentSelection();
		//ObjectAdapter selection = uiSelectionManager.getSelectedObjects();
		toggleElide(SelectionManager.getSelections());
	}
	@Explanation ("Expands the elided display of the selected or top object and its descendents")
	 public void deepExpand() {
		ObjectAdapter selection = (ObjectAdapter) SelectionManager.getCurrentSelection();
		if (selection != null) {
			deepElide(selection, 5);
			//(new ElideAdapterVisitor (selection)).traverseContainersFrom(DEEP_ELIDE_LEVEL);
		} else deepElide(frame.getBrowser().getDefaultAdapter(), 5);
	}
	Vector getSelections() {
		Vector selections = SelectionManager.getSelections();
		if (selections == null || selections.size() == 0) {
			selections = new Vector();
			selections.add(frame.getBrowser().getDefaultAdapter());
		}
		return selections;
		
	}
	@Explanation ("Expands/elides the  display of the selected or top object and its children")
	public void elideChildren() {
		
		Vector selections = getSelections();
		for (int i = 0; i < selections.size(); i++) {
			if (selections.get(i) instanceof CompositeAdapter)
				toggleElideChildren ((CompositeAdapter) selections.get(i));
		}
		
		/*
		
		
		if (selection != null) {
			deepElide(selection, 5);
			//(new ElideAdapterVisitor (selection)).traverseContainersFrom(DEEP_ELIDE_LEVEL);
		} else deepElide(frame.getBrowser().getDefaultAdapter(), 5);
		*/
	}
	void toggleElideChildren (CompositeAdapter parent) {
		for (int i = 0; i < parent.getChildAdapterCount(); i++) {
			ObjectAdapter compAdapter = parent.getChildAdapterAt(i);
			toggleElide(compAdapter);
			
		}
	}
	@Explanation ("Hides/shows handles in a tabular display in the main window, which can be used for selection purposes")
	public void handles() {
		elideHandle(SelectionManager.getSelections());
	}
	
	void toggleElide (ObjectAdapter adapter, int level) {
		if (adapter == null) return;
		if(!ObjectEditor.shareBeans()){
			subToggleElide(adapter, level);
		} else{
			if(ObjectEditor.coupleElides()){
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter,level));
			} else{
				if (ObjectEditor.colabMode())
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter.getPath(),level));
			}
		}
	}
	void internalElide (ObjectAdapter adapter, int level) {
		if (adapter == null) return;
		if(!ObjectEditor.shareBeans()){
			subInternalElide(adapter, level);
		} else{
			if(ObjectEditor.coupleElides()){
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter,level));
			} else{
				if (ObjectEditor.colabMode())
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter.getPath(),level));
			}
		}
	}
	void internalElideTopChildren (ObjectAdapter adapter) {
		internalElide (adapter, 2);
	}
	void internalElideTopChildren () {
		internalElideTopChildren (frame.getAdapter());
	}
	void toggleElide (Vector adapters) {
		for (int i = 0; i < adapters.size(); i++) {
			toggleElide((ObjectAdapter) adapters.elementAt(i));
		}
			
	}
	
	
	void toggleElide (ObjectAdapter adapter) {
		if (adapter == null) return;
		if(!ObjectEditor.shareBeans()){
			subToggleElide(adapter);
		} else{
			if(ObjectEditor.coupleElides()){
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter,1));
			} else{
				if (ObjectEditor.colabMode())
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter.getPath(),1));
			}
		}
	}
	
	void subToggleElide(ObjectAdapter adapter){
		WidgetShell gw = adapter.getGenericWidget();
		if (gw != null)
			gw.toggleElide();		
		frame.validate();
	}
	
	
	void subDeepElide(ObjectAdapter adapter, int level){
		if (!frame.getBrowser().getCurrentAdapters().contains(adapter.getTopAdapter()))
			adapter =   frame.getBrowser().getDefaultAdapter();
		//System.out.println("deep elide"   + adapter);
		(new ElideAdapterVisitor (adapter)).traverse(level);      
		//this.doLayout();
		//this.pack();
		frame.validate();
	}
	
	void subToggleElide(ObjectAdapter adapter, int level){
		if (!frame.getBrowser().getCurrentAdapters().contains(adapter.getTopAdapter()))
			adapter =   frame.getBrowser().getDefaultAdapter();
		//System.out.println("deep elide"   + adapter);
		//(new ToggleElideAdapterVisitor (adapter)).traverse(level);
		(new ToggleElideAdapterVisitor (adapter)).visitContainersAt(level); 
		//this.doLayout();
		//this.pack();
		frame.validate();
	}
	void subInternalElide(ObjectAdapter adapter, int level){
		if (!frame.getBrowser().getCurrentAdapters().contains(adapter.getTopAdapter()))
			adapter =   frame.getBrowser().getDefaultAdapter();
		//System.out.println("deep elide"   + adapter);
		//(new ToggleElideAdapterVisitor (adapter)).traverse(level);
		(new ElideWithoutHandleAdapterVisitor (adapter)).visitContainersAt(level); 
		//this.doLayout();
		//this.pack();
		frame.validate();
	}
	void deepElide   (ObjectAdapter adapter, int level) {
		if (adapter == null) return;
		if(!ObjectEditor.shareBeans()){
			subDeepElide(adapter, level);
		} else{
			if(ObjectEditor.coupleElides()){
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter,level));
			} else{
				if (ObjectEditor.colabMode())
				ObjectRegistry.logAutomaticRefresh(new AutomaticRefresh(frame,adapter.getPath(),level));
			}
		}
	}
	  void elideHandle (ObjectAdapter o) {
			WidgetShell gw = o.getGenericWidget();
			if (gw != null)
				gw.elideHandle();
		}
		 void elideHandle (Vector adapters) {
			for (int i = 0; i < adapters.size(); i++) {
				elideHandle((ObjectAdapter) adapters.elementAt(i));
			}
		}
	
}
