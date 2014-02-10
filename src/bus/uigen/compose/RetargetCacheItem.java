package bus.uigen.compose;
import java.util.*;

import java.util.Hashtable;

public class RetargetCacheItem   //caches the information of a possible transition from one classes UI to another
	//to save time in the evaluation of the best UI from the provided set.
{
	
public	String name = "";
	
	public Vector metVec = null;
	public	Vector propVec = null;

	public	Hashtable propHash = null;
	public	Hashtable metHash = null;
	public double estGenTime = 0;

	Hashtable comparedItems = new Hashtable();
		
}
