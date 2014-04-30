package com.test.repo;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public final class WordRepositoryTest {

	@Test
	public void getWordCountTest() throws IOException {
		WordRepository.setFichierPath("src/test/resources/test.txt");
		WordRepository wordRepositoryInstance = WordRepository.getInstance();
		Assert.assertEquals(3, wordRepositoryInstance.getWordCount());
	}

	@Test
	public void scrambleWorldTest() throws IOException {
		WordRepository instance = WordRepository.getInstance();
		String unScrambledWord = "tdddest";
		String scrambleWorld = instance.scrambleWorld(unScrambledWord);
		Assert.assertEquals(unScrambledWord.length(), scrambleWorld.length());

	}

}
