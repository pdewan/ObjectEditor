package bus.uigen.undo;
public class TypeComparator {
	public boolean equal(Object[] p1Types, Object[] p2Types) {
		if (p1Types.length != p2Types.length) return false;
		for (int i = 0; i < p1Types.length; i++) {
			if (p1Types[i] != p2Types[i])
				return false;
		}
		return true;		
	}
	public boolean isCarOf (Object[] p1Types, Object[] p2Types) {
		if (p1Types.length + 1 != p2Types.length) return false;
		for (int i = 0; i < p1Types.length; i++) {
			if (p1Types[i] != p2Types[i+1])
				return false;
		}
		return true;
	}	
}
