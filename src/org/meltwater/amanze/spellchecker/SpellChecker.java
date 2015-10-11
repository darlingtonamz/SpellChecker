package org.meltwater.amanze.spellchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class SpellChecker {
	static Hashtable<String, String> dictionary;
	static FileWriter fw;
	static BufferedWriter bw;
	
	public static void main(String[] args){
		dictionary = new Hashtable<String, String>();
		loadDictionary();
		loadFile();
		//System.out.println(dictionary.get("hiv"));
	}
	
	static void loadDictionary(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/org/meltwater/amanze/spellchecker/dictionary")));
		    String rawLine;
		    while ((rawLine = reader.readLine()) != null) {
				String word = rawLine.toLowerCase().trim();
				dictionary.put(word, word);
		    }
		    System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	}
	
	static void loadFile(){
		try {
			initOutput();
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/org/meltwater/amanze/spellchecker/document.txt")));
		    String rawLine;
		    while ((rawLine = reader.readLine()) != null) {
				String[] words = rawLine.toLowerCase().split(" ");
				for (String word : words) {
					if (dictionary.contains(word)) {
						//System.out.println(word +" : "+ true);
						bw.write(word+" ");
					}else{
						bw.write("??? ");
					}
				}
				bw.newLine();
		    }

			bw.close();
		    System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initOutput(){
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
