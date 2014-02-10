package bus.uigen.view;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;import javax.swing.JTree;import javax.swing.tree.TreeModel;import javax.swing.tree.TreePath;import javax.swing.tree.TreeNode;import slm.SLModel;
import shapes.ShapeModel;
import slc.SLComposer;import bus.uigen.editors.Connections;import bus.uigen.undo.*;
import bus.uigen.oadapters.*;import bus.uigen.visitors.*;import bus.uigen.introspect.*;import bus.uigen.ars.*;
import bus.uigen.attributes.*;import bus.uigen.compose.ComponentPanel;
import bus.uigen.controller.*;import bus.uigen.view.AGenericWidgetShell;import bus.uigen.view.OEFrameSelector;import bus.uigen.widgets.ScrollPaneSelector;
import bus.uigen.widgets.VirtualComponent;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualSplitPane;
import bus.uigen.*;

public class ASmartSplitPaneTopViewManager extends ASplitPaneTopViewManager  {
	
	
	void maybeReparentSplitPane (VirtualSplitPane splitPane) {
		VirtualComponent rightComponent = splitPane.getRightComponent();
		if (rightComponent != null) return;
		VirtualComponent leftComponent = splitPane.getLeftComponent();
		if (leftComponent == null) return;
		VirtualContainer parent = splitPane.getParent();
		if (parent == null) return;
		parent.remove(splitPane);
		splitPane.remove(leftComponent);
		parent.add(leftComponent);
		
	}
	
	

	boolean hasLeftComponent(VirtualSplitPane splitPane) {
		return splitPane.getLeftComponent() != null;
	}
	boolean hasRightComponent(VirtualSplitPane splitPane) {
		return splitPane.getRightComponent() != null;
	}
	
	void reconnect (VirtualSplitPane splitPane, VirtualComponent leftComponent) {
		if (hasLeftComponent(splitPane)) {
			return;
		}
		VirtualContainer parent = leftComponent.getParent();
		if (parent == null)
			return; // should never happen
		parent.remove(leftComponent);
		parent.add(splitPane);
		replaceLeftComponent(splitPane, leftComponent);
	}
	
	void reconnectMain() {
		reconnect (mainSplitPane, mainPane);
		
	}
	void reconnectTree() {
		reconnect (treeSplitPane, treePanel);
		
	}
		
	void addDrawToMain () {
		reconnectMain();
		super.addDrawToMain();
		
	}
	void addSecondaryToMain () {
		reconnectMain();
		super.addSecondaryToMain();
	}
	void addDrawToTree() {
		reconnectTree();
		super.addDrawToTree();
	}
	
	void addMainToFrame() {		
		if (hasRightComponent(mainSplitPane)) {
			super.addMainToFrame();
			return;
		}
		clearLeftComponent(mainSplitPane);
		//frame.add(mainPane, BorderLayout.CENTER);
		frame.add(getMainContainer(), BorderLayout.CENTER);
	}
	void addMainToTree() {
		reconnectTree();
		if (hasRightComponent(mainSplitPane)) {
			super.addMainToTree();
		} else			
			replaceRightComponent (treeSplitPane, mainPane);
		//maybeAddSecondary();
	}
	void addTreeToFrame() {
		if (hasRightComponent(treeSplitPane)) {
			super.addTreeToFrame();
			return;
		}
		clearLeftComponent(treeSplitPane);
		frame.add(treePanel, BorderLayout.CENTER);
	}
	
	void removeMainFromFrame() {
		if (mainSplitPane == null)
			return;
		if (hasLeftComponent(mainSplitPane))
			super.removeMainFromFrame();
		else
			frame.remove(mainPane);
	}
	/*
	void removeMainFromTree() {
		if (hasLeftComponent(treeSplitPane))
			super.removeMainFromTree();
		else
			
			treeSplitPane.remove(mainScrollPane);
	}
	*/
		
			}
