package bus.uigen.test;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.widgets.VirtualContainer;
import bus.uigen.widgets.VirtualFrame;
import bus.uigen.widgets.awt.AWTContainer;
import bus.uigen.widgets.swing.SwingFrame;

public class AutoTextInCustomFrameWithCustomDrawing {

	public static void main (String[] args) {
		SquaringCounterWithButtons counter = new SquaringCounterWithButtons();
		BarChartDrawingPanel circleDrawingPanel = new BarChartDrawingPanel(counter);
		JFrame frame=  new JFrame();
		frame.setLayout(new GridLayout(2, 0));
		JPanel textPanel = new JPanel();
		frame.add(textPanel);
		frame.add(circleDrawingPanel);
//		uiFrame editor = ObjectEditor.createOEFrame(frame);
		ObjectEditor.editInMainContainer(counter, textPanel);

		frame.setSize(300, 400);
		frame.setVisible(true);
		
		
		
		
	}

}
