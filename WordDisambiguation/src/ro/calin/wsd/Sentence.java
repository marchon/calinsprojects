package ro.calin.wsd;

public class Sentence {
	private Word[] words;
	private Word headWord;
	private String headWordSense;

	private int left;
	private int right;

	public Sentence(Word[] words, Word headWord, String headWordSense) {
		this(words, headWord, headWordSense, Integer.MAX_VALUE,
				Integer.MAX_VALUE);
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
		//TODO: implement this
		return getWords();
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
