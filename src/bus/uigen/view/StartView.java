package bus.uigen.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import util.trace.TraceableListener;
import bus.uigen.widgets.Painter;

public interface StartView  extends Painter, TraceableListener, MouseListener, KeyListener, Runnable{
	void setVisible(boolean newVal);
}
