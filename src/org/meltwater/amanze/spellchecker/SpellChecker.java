package org.meltwater.amanze.spellchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

public class SpellChecker {
	private static Hashtable<String, String> dictionary;
	private static FileWriter fw;
	private static BufferedWriter bw;
	
	public static void main(String[] args){
		dictionary = new Hashtable<String, String>();
		long startTime = System.nanoTime();

		loadDictionary(args[1]);
		loadFile(args[0]);
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Time spent: " + duration/1000000 + " ms");
	}
	
	/*
	 * loads the dictionary provided through the console parameters
	 */
	private static void loadDictionary(String dic){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/org/meltwater/amanze/spellchecker/"+dic)));
		    String rawLine;
		    while ((rawLine = reader.readLine()) != null) {
				String word = rawLine.toLowerCase().trim();
				dictionary.put(word, word);
		    }
		    System.out.println("Done reading dictionary: src/org/meltwater/amanze/spellchecker/"+dic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	}
	
	/*
	 * returns a permutation of scrambled arrangements of the text which is not in the dictionary
	 */
	private final static ArrayList<String> edits(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i < word.length(); ++i) 
        	result.add(word.substring(0, i) + word.substring(i+1));
        for(int i=0; i < word.length()-1; ++i) 
        	result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
        for(int i=0; i < word.length(); ++i) 
        	for(char c='a'; c <= 'z'; ++c) 
        		result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
        for(int i=0; i <= word.length(); ++i) 
        	for(char c='a'; c <= 'z'; ++c) 
        		result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
        return result;
    }
	
	/*The method gets various forms of a word and chooses the one that closely fits the one queried
	 */
	private static String correct(String word) {
        ArrayList<String> list = edits(word);
        HashMap<String, String> candidates = new HashMap<String, String>();
        
        for(String s : list) 
        	if(dictionary.containsKey(s)) candidates.put(dictionary.get(s),s);
        
        if(candidates.size() > 0) 
        	return candidates.get(Collections.max(candidates.keySet()));
        
        for(String s : list) 
        	for(String w : edits(s)) 
        		if(dictionary.containsKey(w)) candidates.put(dictionary.get(w),w);
        
        return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
    }

	/*
	 * loads the sample document for spell-checking
	 */
	private static void loadFile(String document){
		try {
			initOutput();
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/org/meltwater/amanze/spellchecker/"+document)));
		    String rawLine;
		    while ((rawLine = reader.readLine()) != null) {
				String[] words = rawLine.split(" ");
				for (String word : words) {
					boolean isCapitalized = !(word.equals(word.toLowerCase()));
					word = word.toLowerCase();
					if (dictionary.contains(word) || word.matches(".*[.,()]")) {
						bw.write((isCapitalized ? capitalize(word) : word)+" ");
					}else{
						String out = correct(word);
						bw.write((isCapitalized ? capitalize(out) : out)+ " ");
					}
				}
				bw.newLine();
		    }

			bw.close();
		    System.out.println("Done loading document src/org/meltwater/amanze/spellchecker/"+document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String capitalize(final String line) {
	   return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	
	/*
	 * The method initializes the FileWriter and BufferedWriter objects for out output
	 */
	private static void initOutput(){
		File file = new File("src/org/meltwater/amanze/spellchecker/document-corrected.txt");
		try {
			if (!file.exists())
				file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
