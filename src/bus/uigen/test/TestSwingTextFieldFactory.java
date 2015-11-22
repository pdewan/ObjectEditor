package bus.uigen.test;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bus.uigen.widgets.FrameFactory;
import bus.uigen.widgets.PanelFactory;
import bus.uigen.widgets.TextFieldFactory;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.swing.SwingFrameFactory;
import bus.uigen.widgets.swing.SwingPanelFactory;
import bus.uigen.widgets.swing.SwingTextFieldFactory;

public class TestSwingTextFieldFactory extends SwingTextFieldFactory implements TextFieldFactory {

	@Override
	protected JTextField createJTextField(String aText) {
		JTextField aJTextField = new JTextField();
		aJTextField.setBackground(Color.PINK);
		aJTextField.setForeground(Color.RED);
		return aJTextField;
	}

}
