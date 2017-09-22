package vectorspace;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;


public  class Document implements Comparable<Document> {
	
	/**
	 * A hashmap for term frequencies.
	 * Maps a term to the number of times this terms appears in this document. 
	 */
	private HashMap<String, Integer> termFrequency;
	
	/**
	 * The name of the file to read.
	 */
	
	ArrayList<String> stopWord;
	
	
	private String filename;
	
	/**
	 * The constructor.
	 * It takes in the name of a file to read.
	 * It will read the file and pre-process it.
	 * @param filename the name of the file
	 * @throws IOException 
	 */
	public Document(String filename) throws IOException {
		this.filename = filename;
		termFrequency = new HashMap<String, Integer>();
		
		readFileAndPreProcess();
	}
	
	public Document() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method will read in the file and do some pre-processing.
	 * The following things are done in pre-processing:
	 * Every word is converted to lower case.
	 * Every character that is not a letter or a digit is removed.
	 * We don't do any stemming.
	 * Once the pre-processing is done, we create and update the 
	 * 
	 */
	public void readFile() throws FileNotFoundException{
		Scanner in = new Scanner(new File("Test/659EnglishStopwords.txt"));
		stopWord = new ArrayList<String>();
		while(in.hasNext()){
			String nextWord = in.next();
			String filteredWord = nextWord.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
			if (!(filteredWord.equalsIgnoreCase(""))) {
					stopWord.add(filteredWord);
						
			}
		}
	}
	
	
	
	private void readFileAndPreProcess() throws IOException {
		try {
			Scanner in = new Scanner(new File(filename));
			readFile();
			System.out.println("Reading file: " + filename + " and preprocessing");
			
			while (in.hasNext()) {
				String nextWord = in.next();
				for(int  i=1; i <stopWord.size(); i++){
					String filteredWord = nextWord.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
					if (!(filteredWord.equalsIgnoreCase(""))&&!(filteredWord.equals(stopWord.get(i)))) {
						if (termFrequency.containsKey(filteredWord)) {
							int oldCount = termFrequency.get(filteredWord);
							termFrequency.put(filteredWord, ++oldCount);
						} else {
							termFrequency.put(filteredWord, 1);
						}	
					//System.out.print("[" + filteredWord + " (" + termFrequency.get(filteredWord) + ") ]\t");
				}
				}
				
				//File file = new File("Result/" + filename);
				//FileWriter fileWriter = new FileWriter(file);
				//fileWriter.write(filteredWord + "  " + termFrequency.get(filteredWord));
				//fileWriter.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not Found ! ");
		}
	}
	
	/**
	 * This method will return the term frequency for a given word.
	 * If this document doesn't contain the word, it will return 0
	 * @param word The word to look for
	 * @return the term frequency for this word in this document
	 */
	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}
	
	/**
	 * This method will return a set of all the terms which occur in this document.
	 * @return a set of all terms in this document
	 */
	public Set<String> getTermList() {
		return termFrequency.keySet();
	}

	@Override
	/**
	 * The overriden method from the Comparable interface.
	 */
	public int compareTo(Document other) {
		return filename.compareTo(other.getFileName());
	}

	/**
	 * @return the filename
	 */
	private String getFileName() {
		return filename;
	}
	
	/**
	 * This method is used for pretty-printing a Document object.
	 * @return the filename
	 */
	public String toString() {
		return filename;
	}
}