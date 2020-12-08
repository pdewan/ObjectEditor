package bus.uigen.test;

import bus.uigen.ObjectEditor;
import util.models.ATerminalModel;
import util.models.TerminalModel;

public class TerminalModelDriver {
	public static void main (String[] args) {
		TerminalModel aTerminalModel = new AnExtendedTerminalModel();
		ObjectEditor.setDoPrints(false);
		ObjectEditor.edit(aTerminalModel);
	}
}
