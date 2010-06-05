package ro.ranking.technique.f2exp;

/*******************************************
 * Modified based on TermScorer.java by: Hui Fang (hfang@ece.udel.edu)
 * Last updated: 08/16/2009
 * For Lucene-2.9-dev
 * Implementaiton of Axiomatic retrieval functions: F2-EXP
 * References: H. Fang and C. Zhai. "An Exploration of Axiomatic Approaches to Information Retrieval", SIGIR'05.
 ******************************************/

import java.io.IOException;

import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;

public class AxiomaticTermScorer extends Scorer {

	private Weight weight;
	private TermDocs termDocs;
	private byte[] norms;
	private float weightValue;
	private float avgLength;
	private float paramS;
	private int doc;

	private final int[] docs = new int[32]; // buffered doc numbers
	private final int[] freqs = new int[32]; // buffered term freqs
	private int pointer;
	private int pointerMax;

	AxiomaticTermScorer(Weight weight, TermDocs td, Similarity similarity,
			byte[] norms, float avgDL, float paramS) {
		super(similarity);
		this.weight = weight;
		this.termDocs = td;
		this.norms = norms;
		this.weightValue = weight.getValue();
		this.paramS = paramS;
		this.avgLength = avgDL;
	}

	public void score(Collector c) throws IOException {
		nextDoc();
		score(c, Integer.MAX_VALUE);
	}

	protected boolean score(Collector c, int end) throws IOException {
		c.setScorer(this);
		while (doc < end) { // for docs in window
			c.collect(doc); // collect score

			if (++pointer >= pointerMax) {
				pointerMax = termDocs.read(docs, freqs); // refill buffers
				if (pointerMax != 0) {
					pointer = 0;
				} else {
					termDocs.close(); // close stream
					doc = Integer.MAX_VALUE; // set to sentinel value
					return false;
				}
			}
			doc = docs[pointer];
		}
		return true;
	}

	public int docID() {
		return doc;
	}

	public int nextDoc() throws IOException {
		pointer++;
		if (pointer >= pointerMax) {
			pointerMax = termDocs.read(docs, freqs); // refill buffer
			if (pointerMax != 0) {
				pointer = 0;
			} else {
				termDocs.close(); // close stream
				return doc = NO_MORE_DOCS; // set to sentinel value
			}
		}
		doc = docs[pointer];
		return doc;
	}

	public float score() {
		int f = freqs[pointer];

		float fieldNorm = this.getSimilarity().decodeNormValue(
				this.norms[this.docID()]);
		float length = 1 / (fieldNorm * fieldNorm);

		return f * weightValue / (f + paramS + paramS * length / avgLength);
	}

	public Explanation explain(int doc) throws IOException {
		TermQuery query = (TermQuery) weight.getQuery();
		Explanation tfExplanation = new Explanation();
		int tf = 0;
		while (pointer < pointerMax) {
			if (docs[pointer] == doc)
				tf = freqs[pointer];
			pointer++;
		}
		if (tf == 0) {
			if (termDocs.skipTo(doc)) {
				if (termDocs.doc() == doc) {
					tf = termDocs.freq();
				}
			}
		}
		termDocs.close();
		tfExplanation.setValue(getSimilarity().tf(tf) * getSimilarity().tf(tf));
		tfExplanation.setDescription("tf(termFreq(" + query.getTerm() + ")="
				+ tf + ")");

		return tfExplanation;
	}

	public String toString() {
		return "scorer(" + weight + ")";
	}

	@Override
	public int advance(int target) throws IOException {
		// first scan in cache
		for (pointer++; pointer < pointerMax; pointer++) {
			if (docs[pointer] >= target) {
				return doc = docs[pointer];
			}
		}

		// not found in cache, seek underlying stream
		boolean result = termDocs.skipTo(target);
		if (result) {
			pointerMax = 1;
			pointer = 0;
			docs[pointer] = doc = termDocs.doc();
			freqs[pointer] = termDocs.freq();
		} else {
			doc = NO_MORE_DOCS;
		}
		return doc;
	}

}