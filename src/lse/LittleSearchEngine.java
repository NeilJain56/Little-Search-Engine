package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	private Scanner file;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		if(docFile == null) {
			throw new FileNotFoundException();
		}
		
		file = new Scanner(new File(docFile));
		
		HashMap<String, Occurrence> hashy = new HashMap<String, Occurrence>();
		
		while(file.hasNext()) {
			String keyword = getKeyword(file.next());
			if(keyword != null) {
				if(hashy.containsKey(keyword)) {
					Occurrence found = hashy.get(keyword);
					found.frequency++;
				}
				else {
					Occurrence found = new Occurrence(docFile, 1);
					hashy.put(keyword, found);	
				}
			}
		}
		return hashy;
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	//return null;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(String keyword : kws.keySet()) {
			ArrayList<Occurrence> occ = new ArrayList<Occurrence>();
			
			if(keywordsIndex.containsKey(keyword)) {
				occ=keywordsIndex.get(keyword);
			}
			occ.add(kws.get(keyword));
			
			insertLastOccurrence(occ);
			//System.out.println(hi);
			//System.out.println(keyword);
			//System.out.println(occ);
			//System.out.println("HERE" + occ + " ");
			keywordsIndex.put(keyword, occ);
			
		}
		//System.out.println(keywordsIndex);
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(word == null) {
			return null;
		}
		word = word.toLowerCase();
		word = removeTrail(word);
		
		if(word == null) {
			return null;
		}
		if(noiseWords.contains(word)) {
			return null;
		}
		boolean containsChar = false;
		for(int z=0; z<word.length(); z++) {
			if(!Character.isLetter(word.charAt(z))) {
				containsChar = true;
			}
		}
		if(containsChar == false) {
			return word;
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return null;
	}
	private String removeTrail(String word) {
		
		for(int i = word.length()-1; i >= 0; i--) {
			if(word.equals("")) {
				return null;
			}
			if(word.length() == 1) {
				if(isPunc(word.charAt(0))) {
					return null;
				}
				return word;
			}
			if(isPunc(word.charAt(i))) {
				word = word.substring(0, word.length()-1);
			}
			else {
				break;
			}
			
		}
		return word;
	}
	private boolean isPunc(char c) {
		if(c == '.' || c == ',' || c == '?' || c == ':' || c == ';' || c == '!') {
			return true;
		}
		return false;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs){
		if(occs.size()==1 || occs.size()==0) {
			return null;
		}
		ArrayList<Occurrence> occs2 = new ArrayList<Occurrence>();
		
		Occurrence target = occs.get(occs.size()-1);
		
		for(int i=0; i<=occs.size()-2; i++) {
			occs2.add(occs.get(i));
		}
		occs.remove(occs.size()-1);
		int low = 0; 
		int high = occs2.size()-1;
		int frequency = target.frequency;
		ArrayList<Integer> returner = new ArrayList<Integer>();
		int mid = 0;
		
		while (low <= high) {
			mid = ((low + high)/2);
			returner.add(mid);
			
			if(occs2.get(mid).frequency == frequency) {
				break;
			}
			else if(occs2.get(mid).frequency > frequency) {
				low = mid +1;
			}
			else if(occs2.get(mid).frequency < frequency) {
				high = mid - 1;
			}
		}
		if(occs2.get(mid).frequency == frequency) {
		occs.add(mid+1, target);	
		}
		else if (mid == occs2.size()-1 && occs2.get(mid).frequency > frequency) {
			occs.add(target);
		}
		else if(occs2.get(mid).frequency < frequency) {
			occs.add(mid, target);
		}
		else if (occs2.get(mid).frequency > frequency) {
			occs.add(mid+1, target);
		}
		//System.out.println(returner);
		return returner;
		
	}
	/*public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		if(occs.size() == 1 || occs.size() == 0) {
			return null;
		}
		int low = 0; 
		int high = occs.size()-2;
		int target = occs.get(occs.size()-1).frequency;
		int mid = 0;
		ArrayList<Integer> midpoints = new ArrayList<Integer>();
		
		while(high >= low) {
			
			mid = ((high + low)/ 2);
			midpoints.add(mid);
			
			if(occs.get(mid).frequency == target) {
				break;
			}
			else if(occs.get(mid).frequency < 0) {
				high = mid -1;
			}
			else if(occs.get(mid).frequency > 0) {
				low = mid +1;
				if(high <= mid) {
					mid = mid+1;
				}
			}
	
			
		}
		midpoints.add(mid);
		
		Occurrence temp = occs.remove(occs.size()-1);
		occs.add(midpoints.get(midpoints.size()-1), temp);
		return midpoints;
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		// return null;
	}*/
	
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	/*public ArrayList<String> top5search(String kw1, String kw2){
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<String> top = new ArrayList<String>();
		ArrayList<Occurrence> occ1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> occ2 = new ArrayList<Occurrence>();
		for(String key : keywordsIndex.keySet()) {
			key =key.toLowerCase();
			if(kw1.equals(key)) {
				occ1 = keywordsIndex.get(key);
			}
			if(kw2.equals(key)) {
				occ2 = keywordsIndex.get(key);
			}
			
		}
		ArrayList<Occurrence> total = new ArrayList<Occurrence>();
		
		int i = 0;
		while(occ1!=null && i<occ1.size()-1) {
			total.add(occ1.get(i));
			insertLastOccurrence(total);
			i++;
		}
		int z = 0;
		while(occ2!=null && z<occ2.size()-1) {
			total.add(occ2.get(z));
			z++;
		}
		
	
	}
	public ArrayList<Occurrence> sort(ArrayList<Occurrence> occs){
		if(occs.size()==1 || occs.size()==0) {
			return null;
		}
		ArrayList<Occurrence> occs2 = new ArrayList<Occurrence>();
		
		Occurrence target = occs.get(occs.size()-1);
		
		for(int i=0; i<=occs.size()-2; i++) {
			occs2.add(occs.get(i));
		}
		occs.remove(occs.size()-1);
		int low = 0; 
		int high = occs2.size()-1;
		int frequency = target.frequency;
		//ArrayList<Integer> returner = new ArrayList<Integer>();
		int mid = 0;
		
		while (low <= high) {
			mid = ((low + high)/2);
			//returner.add(mid);
			
			if(occs2.get(mid).frequency == frequency) {
				break;
			}
			else if(occs2.get(mid).frequency > frequency) {
				low = mid +1;
			}
			else if(occs2.get(mid).frequency < frequency) {
				high = mid - 1;
			}
		}
		if(occs2.get(mid).frequency == frequency) {
		occs.add(mid+1, target);	
		}
		else if (mid == occs2.size()-1 && occs2.get(mid).frequency > frequency) {
			occs.add(target);
		}
		else if(occs2.get(mid).frequency < frequency) {
			occs.add(mid, target);
		}
		else if (occs2.get(mid).frequency > frequency) {
			occs.add(mid+1, target);
		}
		//return returner;
		return occs;
	}*/
	
	public ArrayList<String> top5search(String kw1, String kw2) {
		
		ArrayList<String> top5= new ArrayList<String>(); 
		ArrayList<Occurrence> occ1= new ArrayList<Occurrence>(); 
		ArrayList<Occurrence> occ2= new ArrayList<Occurrence>();
		
		for(String key : keywordsIndex.keySet()){
			if(kw1.equalsIgnoreCase(key)){
				occ1= keywordsIndex.get(key);
			}
			
			if(kw2.equalsIgnoreCase(key)){
				occ2= keywordsIndex.get(key);
			}
		}
		
		
		int max;
		int smax;
		while(top5.size() < 5){
			max= 0;
			smax= 0;
			String name;
			
			if(occ1.size() + occ2.size() == 0){
				return top5;
			}
			
			if(occ2.isEmpty()){
			max= occ1.get(0).frequency;
			}
			
			else if(occ1.isEmpty()){
			smax= occ2.get(0).frequency;
			}
			
			else{
				smax= occ2.get(0).frequency;
				max= occ1.get(0).frequency;
				
			}
			
			if(max >= smax){
				name= occ1.get(0).document;
				if(!(top5.contains(name))){
				top5.add(name);
				}
				occ1.remove(0);
			}
			else{
				name= occ2.get(0).document;
				if(!(top5.contains(name))){
				top5.add(name);
				}
				occ2.remove(0);
			}
		}
		
		return top5;
	}


		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		//return null;
	
	//}
}
