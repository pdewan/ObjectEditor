package bus.uigen.controller.models;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import util.annotations.Explanation;
import util.annotations.Visible;

import bus.uigen.AutomaticRefresh;
import bus.uigen.ObjectEditor;
import bus.uigen.ObjectRegistry;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.view.SaveAsListener;
import bus.uigen.visitors.IsSerializableAdapterVisitor;
import bus.uigen.visitors.ToTextAdapterVisitor;
import bus.uigen.visitors.UpdateAdapterVisitor;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ABasicFileOperationsModel extends AnAbstractOperationsModel implements FrameModel {

	public ABasicFileOperationsModel () {
		
	}
	
	public boolean preDone() {
		return frame.doneEnabled();
	}
	@Explanation("Executed when an object parameter of a method invoved by ObjectEditor has been completely entered")
	public void done() {
		frame.notifyDoneListeners();
		
	}
	
	
	
	}
	
