package bus.uigen.oadapters;

import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.util.Observer;

import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;

import util.models.Refresher;
import util.models.RemotePropertyChangeListener;
import bus.uigen.ComponentValueChangedListener;
import bus.uigen.ModelAdapter;
import bus.uigen.attributes.AttributeCollection;
import bus.uigen.attributes.LocalAttributeListener;
import bus.uigen.controller.Selectable;
import bus.uigen.undo.CommandListener;

public interface ObjectAdapterInterface extends Observer, Refresher, PropertyChangeListener, RemotePropertyChangeListener,
TableModelListener, TreeModelListener, ComponentValueChangedListener,
AttributeCollection, LocalAttributeListener, Selectable,
MutableTreeNode, /* ActionListener, MouseListener, */CommandListener,
ModelAdapter{

}
