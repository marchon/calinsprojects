/*******************************************
 * Modified based on TermQuery by Hui Fang (hfang@ece.udel.edu)
 * Last updated: 08/16/2009
 * For Lucene-2.9-dev
 * Implementaiton of Axiomatic retrieval functions: F2-EXP 
 * References: H. Fang and C. Zhai. "An Exploration of Axiomatic Approaches to Information Retrieval", SIGIR'05.
 ******************************************/
package ro.ranking.technique.axiomatic;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.ComplexExplanation;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;

public class AxiomaticTermQuery extends TermQuery {

	public AxiomaticTermQuery(Term t, float avgDL, float paramK, float paramS) {
		super(t);
		term = t;
		this.avgDL = avgDL;
		this.paramK = paramK;
		this.paramS = paramS;
	}

	public Weight createWeight(Searcher searcher) throws IOException {
		return new AXTermWeight(searcher);
	}

	private Term term;
	private float avgDL;
	private float paramS;
	private float paramK;

	public void setAvgDL(float avgDL) {
		this.avgDL = avgDL;
	}

	public void setParamK(float paramK) {
		this.paramK = paramK;
	}

	public void setParamS(float paramS) {
		this.paramS = paramS;
	}

	private class AXTermWeight extends Weight {
		private Similarity similarity;
		private float value;
		private float idf;
		private float queryNorm;
		private float queryWeight;

		public String toString() {
			return "weight(" + AxiomaticTermQuery.this + ")";
		}

		public Query getQuery() {
			return AxiomaticTermQuery.this;
		}

		public float getValue() {
			return value;
		}

		public AXTermWeight(Searcher searcher) throws IOException {
			this.similarity = getSimilarity(searcher);
			idf = (float) Math.pow((searcher.maxDoc() + 0.5f)
					/ (searcher.docFreq(term) + 0.5f), paramK);
		}

		public float sumOfSquaredWeights() {
			queryWeight = getBoost(); // compute query weight
			return queryWeight * queryWeight; // square it
		}

		public void normalize(float queryNorm) {
			this.queryNorm = queryNorm;
			value = idf;
		}

		public Explanation explain(IndexReader reader, int doc)
				throws IOException {

			ComplexExplanation result = new ComplexExplanation();
			result.setDescription("weight(" + getQuery() + " in " + doc
					+ "), product of:");

			// explain query weight
			Explanation queryExpl = new Explanation();
			queryExpl.setDescription("queryWeight(" + getQuery() + ") :");
			Explanation boostExpl = new Explanation(getBoost(), "boost");
			if (getBoost() != 1.0f)
				queryExpl.addDetail(boostExpl);
			queryExpl.setValue(boostExpl.getValue());
			result.addDetail(queryExpl);

			String field = term.field();
			ComplexExplanation fieldExpl = new ComplexExplanation();
			fieldExpl.setDescription("fieldWeight(" + term + " in " + doc
					+ "), product of:");

			Explanation idfExpl = new Explanation(idf, "idf(docFreq="
					+ reader.docFreq(term) + ", numDocs=" + reader.numDocs()
					+ ")");
			
			AxiomaticTermScorer axSc = (AxiomaticTermScorer)scorer(reader, true, true);
			Explanation tfExpl = axSc.explain(doc);
			fieldExpl.addDetail(tfExpl);
			fieldExpl.addDetail(idfExpl);

			Explanation fieldNormExpl = new Explanation();
			byte[] fieldNorms = reader.norms(field);
			float docLen = 1.0f / Similarity.decodeNorm(fieldNorms[doc])
					/ Similarity.decodeNorm(fieldNorms[doc]);
			float lenNormFactor = 1.0f / (tfExpl.getValue() + paramS + paramS
					* docLen / avgDL);

			float fieldNorm = fieldNorms != null ? lenNormFactor : 0.0f;
			fieldNormExpl.setValue(fieldNorm);
			fieldNormExpl.setDescription("fieldNorm(field=" + field + ", doc="
					+ doc + ")");
			fieldExpl.addDetail(fieldNormExpl);

			fieldExpl.setMatch(Boolean.valueOf(tfExpl.isMatch()));
			fieldExpl.setValue(tfExpl.getValue() * idfExpl.getValue()
					* fieldNormExpl.getValue());

			result.addDetail(fieldExpl);
			result.setMatch(fieldExpl.getMatch());

			// combine them
			result.setValue(queryExpl.getValue() * fieldExpl.getValue());

			if (queryExpl.getValue() == 1.0f)
				return fieldExpl;

			return result;
		}

		@Override
		public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder,
				boolean topScorer) throws IOException {
			TermDocs termDocs = reader.termDocs(term);

			if (termDocs == null)
				return null;

			return new AxiomaticTermScorer(this, termDocs, similarity, reader
					.norms(term.field()), avgDL, paramS);		}
	}

}
