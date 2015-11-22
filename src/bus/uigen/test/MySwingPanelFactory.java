package bus.uigen.test;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bus.uigen.widgets.FrameFactory;
import bus.uigen.widgets.PanelFactory;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.swing.SwingFrameFactory;
import bus.uigen.widgets.swing.SwingPanelFactory;

public class MySwingPanelFactory extends SwingPanelFactory implements PanelFactory {

	@Override
	protected JPanel createJPanel() {
		JPanel aJPanel = new JPanel();
		aJPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return aJPanel;
	}

}
