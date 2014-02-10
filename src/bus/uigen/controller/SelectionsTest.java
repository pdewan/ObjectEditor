package bus.uigen.controller;import bus.uigen.uiFrame;
import java.util.*;public class SelectionsTest  {	public static String concatStrings(String s1, String s2) {
		return s1 + s2;	}	public static String concatList (Vector stringList) {
		String retVal = "";
		for (int i=0; i < stringList.size(); i++) 
			retVal += stringList.elementAt(i);
		return retVal;	}	public static String invert(String s) {
		String retVal = "";
		for (int i=s.length()-1; i >= 0; i--) {			retVal += s.charAt(i);
		}
		return retVal;	}
}

