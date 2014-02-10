package bus.uigen.undo;
import java.util.Hashtable;
import java.util.Vector;

public class Inverses {
	static Hashtable contents = new Hashtable();
	static String[] prefixes = {"un", "im", "ir", "in", "dis", "a", "de", "undo", "anti"};
	public static void add(String entry, String antonym) {
		put(entry, antonym);
		put(antonym, entry);
	}
	public static void put (String entry, String antonym) {		
		if (!contents.containsKey(entry)) {
			contents.put(entry, new Vector());
		}
		Vector antonyms = (Vector) contents.get(entry);
		if (!antonyms.contains(antonym))
			antonyms.addElement(antonym);
		
	}
	public static boolean contains (String entry, String antonym) {
		Vector antonyms =  (Vector) contents.get(entry);
		return antonyms != null && antonyms.contains(antonym);
	}
	public static boolean hasAntonymPrefix (String entry, String antonym) {
		for (int i = 0; i < prefixes.length; i++) {
			if ((prefixes[i] + entry).toLowerCase().equals(antonym.toLowerCase()) ||
				(prefixes[i] + antonym).toLowerCase().equals(entry.toLowerCase()))
				return true;
		}
		return false;
	}
	public static boolean isAntonym(String entry, String antonym) {
		return inDictionary(entry, antonym) || hasAntonymPrefix(entry, antonym);
	}
	public static boolean inDictionary(String entry, String antonym) {
		//if (entry.equals(antonym)) return false;
		NameTokenizer entryEnumeration = new NameTokenizer(entry);
		NameTokenizer antonymEnumeration = new NameTokenizer(antonym);
		//String nextEntryPrefix = "";
		//String nextAntonymPrefix = "";
		//boolean foundAntonymWord = false;
		while (entryEnumeration.hasMoreElements()) {
				String nextEntryWord = entryEnumeration.nextElement().toLowerCase();
				if (!antonymEnumeration.hasMoreElements()) return false;
				String nextAntonymWord = antonymEnumeration.nextElement().toLowerCase();
				//String nextAntonymWord = antonymEnumeration.nextElement().toLowerCase();
				if (contains(nextEntryWord, nextAntonymWord)) {

					String nextEntrySuffix = entryEnumeration.currentSuffix();
					String nextAntonymSuffix = antonymEnumeration.currentSuffix();
					String nextEntryPrefix = entryEnumeration.currentPrefix();
					String nextAntonymPrefix = antonymEnumeration.currentPrefix();
					return nextEntrySuffix.equals(nextAntonymSuffix) && nextEntryPrefix.equals(nextAntonymPrefix);
				}
				//nextEntryPrefix += nextEntryWord;
				//nextAntonymPrefix += nextAntonymWord;
					
				/*
				else if (!nextEntryWord.equals(nextAntonymWord))
					return false;
					*/
					/*
				if (!nextEntryWord.equals(nextAntonymWord) && 
					!contains(nextEntryWord, nextAntonymWord))
					return false;
					*/
				
		}
		return false;
	}
	public static boolean inDictionaryOld(String entry, String antonym) {
		//if (entry.equals(antonym)) return false;
		NameTokenizer entryEnumeration = new NameTokenizer(entry);
		NameTokenizer antonymEnumeration = new NameTokenizer(antonym);
		boolean foundAntonymWord = false;
		while (entryEnumeration.hasMoreElements()) {
				String nextEntryWord = entryEnumeration.nextElement().toLowerCase();
				if (!antonymEnumeration.hasMoreElements()) return false;
				String nextEntrySuffix = entryEnumeration.currentSuffix();
				String nextAntonymWord = antonymEnumeration.nextElement().toLowerCase();
				String nextAntonymSuffix = antonymEnumeration.currentSuffix();
				if (!nextEntrySuffix.equals(nextAntonymSuffix))
					return false;
				//String nextAntonymWord = antonymEnumeration.nextElement().toLowerCase();
				if (!foundAntonymWord && contains(nextEntryWord, nextAntonymWord)) 
					foundAntonymWord = true;
				else if (!nextEntryWord.equals(nextAntonymWord))
					return false;
					/*
				if (!nextEntryWord.equals(nextAntonymWord) && 
					!contains(nextEntryWord, nextAntonymWord))
					return false;
					*/
				
		}
		return foundAntonymWord;
	}
	/*
	public static boolean containsFirstWord (String entry, String antonym) {
		int entryFirstWordEnd = firstWordEnd(entry);
		if (entryFirstWordEnd == entry.length()) 
			return contains(entry, antonym);
		else {
			String entryFirstWord = firstWord(entry, entryFirstWordEnd);
			String entryNonFirstWords = nonFirstWords(entry, entryFirstWordEnd);
			int antonymFirstWordEnd = firstWordEnd(antonym);
			String antonymFirstWord = firstWord(antonym, antonymFirstWordEnd);
			String antonymNonFirstWords = nonFirstWords(antonym, antonymFirstWordEnd);
			if (!entryNonFirstWords.equals(antonymNonFirstWords))
				return false;
			else
				return contains(entryFirstWord, antonymFirstWord);			
			
		}
	}
	public static Vector getAntonyms (String entry) {
		return (Vector) contents.get(entry);
	}
	public static int firstWordEnd(String entry) {
		for (int i = 0; i < entry.length(); i++) {
			if (entry.charAt(i) < 'a' || entry.charAt(i) > 'z') return i;
		}
		return entry.length();
	}
	public static String firstWord(String s, int i) {
		return s.substring(0, i);
	}
	public static String nonFirstWords(String s, int i) {
		return s.substring(i, s.length());
	}
	*/
	static {
		init();
	}
	public static void init () {
		add("add", "delete");
		add("add", "remove");
		add("add", "subtract");
		add("plus", "minus");
		add("put", "remove");
		add("inc", "dec");
		add("insert", "remove");
		add("insert", "delete");
		add("do", "undo");
		add("push", "pop");
		add("push", "pull");
		add("enqueue", "dequeue");
		add("hide", "show");
		add("up", "down");
		add("join", "leave");
		add("next", "previous");
	}	
	
}
