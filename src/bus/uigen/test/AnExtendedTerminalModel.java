package bus.uigen.test;

import util.annotations.Column;
import util.annotations.Row;
import util.models.ATerminalModel;

public class AnExtendedTerminalModel extends ATerminalModel {
	
	
	@Row(2)
	@Column(0)
	public void next() {
		setInput("next");
	}
	
	public void load(String aTutorial) {
		setInput("load " + aTutorial);
	}
	

}
