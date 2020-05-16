package lse;

import java.io.FileNotFoundException;

public class LSETester {
	public static void main (String args[]) {
		LittleSearchEngine amazon = new LittleSearchEngine();
		try {
			amazon.makeIndex("docs.txt", "noisewords.txt");
			//System.out.println(amazon.loadKeywordsFromDocument("AliceCh1.txt"));
			//System.out.println(amazon.mergeKeywords(kws););
			//System.out.println(amazon.loadKeywordsFromDocument("TesterFile.txt"));
			//System.out.println(amazon.getKeyword("What,ever"));
			//System.out.println(amazon.top5search("hola", "desk"));
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
