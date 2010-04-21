package ro.calin.wsd;

import java.util.Arrays;


public class Sentence {
	private Word[] words;
	private Word headWord;
	private String headWordSense;

	private int left;
	private int right;
	
	public Sentence(Word[] words, Word headWord, String headWordSense) {
		this(words, headWord, headWordSense, words.length,
				words.length);
	}

	public Sentence(Word[] words, Word headWord, String headWordSense,
			int left, int right) {
		setWords(words);
		setHeadWord(headWord);
		setHeadWordSense(headWordSense);
		setLeft(left);
		setRight(right);
	}

	public Word[] getWords() {
		return words;
	}

	public void setWords(Word[] words) {
		this.words = words;
	}

	public Word getHeadWord() {
		return headWord;
	}

	public void setHeadWord(Word headWord) {
		this.headWord = headWord;
	}

	public String getHeadWordSense() {
		return headWordSense;
	}

	public void setHeadWordSense(String headWordSense) {
		this.headWordSense = headWordSense;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}
	
	public Word[] getWordContext(Word word) {
		int i;
		for (i = 0; i < words.length; i++) {
			if(words[i].equals(word)) break;
		}
		
		if(i == words.length)
			return null;
		
		int len = words.length - 1;
		int a = i - left;
		int b = i + right;
		
		if(a < 0) {
			b += (-a);
			a = 0;
		}
		
		if(b > len) {
			a += (len - b);
			b = len;
		}
		
		a = a < 0? 0 : a;
		
		Word[] res = new Word[Math.min(left + right, len)];
		System.arraycopy(words, a, res, 0, i - a);
		System.arraycopy(words, i + 1, res, i - a, b - i);
		
		return res;
	}
	
	public Word[] getHeadWordCotext() {
		return getWordContext(headWord);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{[");
		for (int i = 0; i < words.length; i++) {
			sb.append(words[i]);
			if(i != words.length - 1)
				sb.append('|');
		}
		sb.append(']');
		
		sb.append(" / ");
		sb.append(headWord);
		sb.append("->");
		sb.append(headWordSense);
		sb.append('}');
		
		return sb.toString();
	}
}
