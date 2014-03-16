package bus.uigen.test;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import util.annotations.Column;
import util.annotations.Row;

public class ARowColumnPositioner extends ACompositeExample{
	@Row(0)
	@Column(0)
	public String getString() {
		return super.getString();
	}	
	@Row(0)
	@Column(1)
	public int getInt() {
		return super.getInt();
	}
	@Row(1)
	@Column(0)
	public String getIntAndString() {
		return super.getIntAndString();
	}
	@Row(1)
	@Column(1)
	public void incInt() {
		super.incInt();
	}
	public static void main (String[] args) {
		OEFrame frame = ObjectEditor.edit(new ARowColumnPositioner());
		frame.setSize(550, 150);
	}


}
