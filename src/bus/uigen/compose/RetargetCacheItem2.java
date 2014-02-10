package bus.uigen.compose;
import java.util.*;

public class RetargetCacheItem2   //caches the information of a possible transition from one classes UI to another
	//to save time in the evaluation of the best UI from the provided set.
{
	
	//public	int numSProps = 0;
	public int numSNProps = 0;
	public int numSBProps = 0;
	public int numSSProps = 0;
	
	public int numANProps = 0;
	public int numABProps = 0;
	public int numASProps = 0;
	
	public int numRNProps = 0;
	public int numRBProps = 0;
	public int numRSProps = 0;
	
	public double esttime = 0;

	
	//public	int numSCmds  = 0;
	
	/*
	public	int propDiff = 0;
	public	int methDiff = 0;
	*/
	
	public	Hashtable sharedMeths = null;
	public	Hashtable sharedProps = null;
	public	Vector unsharedMeths = null;
	public	Vector unsharedProps = null;
		
}
