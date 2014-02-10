package bus.uigen.test;

import util.annotations.Explanation;
import util.annotations.Visible;
import util.trace.Tracer;
import bus.uigen.ObjectEditor;

public class TestVisible {
	char[] p1 = {'h', 'i'};
	char[] p2 = p1;
	
	@Explanation ("This property will not be displayed if the other one is also visible")
	public char[] getP1() {
		return p1;
	}
	@Explanation ("Testing getter  explanation")
	public int getInt() {return 0;}

	public void setP1(char[] p1) {
		this.p1 = p1;
	}
	@Visible(false)
	public char[] getP2() {
		return p2;
	}
	
	public void setP2(char[] p2) {
		this.p2 = p2;
	}
	public static void main (String args[]) {
//		Message.showInfo(true);
		ObjectEditor.edit(new TestVisible());		
	}

}
