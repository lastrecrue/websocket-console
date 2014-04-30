package com.test.repo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public final class WordRepository {

	private static Logger logger = Logger.getLogger(WordRepository.class.getName());

	private static volatile WordRepository instance = null;

	private static String fichierPath = "src/main/resources/liste.de.mots.francais.frgut.min6.txt";

	private Set<String> wordSet = new HashSet<String>();
	private Map<String, String> scrambleMap = new HashMap<String, String>();

	private WordRepository() throws IOException {
		super();

		initWordCount();

	}

	private void initWordCount() throws IOException {
		InputStream ips = new FileInputStream(fichierPath);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String ligne;
		while ((ligne = br.readLine()) != null) {
			if (!StringUtils.isBlank(ligne)) {
				wordSet.add(ligne);
			}
		}
	}

	public final static WordRepository getInstance() throws IOException {
		if (WordRepository.instance == null) {
			try {
				WordRepository.instance = new WordRepository();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "", e);
				throw e;
			}
		}
		return WordRepository.instance;
	}

	public String getScrambledRandomWord() {
		double random = Math.random();
		int randInt = (int) (random * (double) wordSet.size());
		int i = 0;
		for (String unScrambledWord : wordSet) {
			if (i >= randInt) {
				String scrambledWord = scrambleWorld(unScrambledWord);
				scrambleMap.put(unScrambledWord, scrambledWord);
				logger.info("scrambledWord : " + scrambledWord + ", unScrambledWord : " + unScrambledWord);
				return scrambledWord;
			}
			i++;
		}
		throw new IndexOutOfBoundsException();
	}

	String scrambleWorld(String unScrambledWord) {
		int length = unScrambledWord.length();
		List<Integer> randList = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			double random = Math.random();
			int randInt = (int) (random * ((double) length));
			if (randList.contains(randInt)) {
				i--;
			} else {
				randList.add(randInt);
			}
		}
		String result = "";
		for (Integer integer : randList) {
			result += unScrambledWord.charAt(integer);
		}
		return result;
	}

	public int getWordCount() {
		return wordSet.size();
	}

	public static String getFichierPath() {
		return fichierPath;
	}

	static void setFichierPath(String fichierPath) {
		WordRepository.fichierPath = fichierPath;
	}

	public String getUnScrambledWordWord(String scrambledWord) {
		return scrambleMap.get(scrambledWord);
	}

	public boolean isUnscrambledWord(String scrambledWord, String unscrambledWord) {
		if (scrambleMap.containsKey(unscrambledWord)) {
			return scrambleMap.get(unscrambledWord).equals(scrambledWord);
		}
		return false;

	}

}
