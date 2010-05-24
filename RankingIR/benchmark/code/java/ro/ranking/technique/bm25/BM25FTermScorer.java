package ro.ranking.technique.bm25;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;

/**
 * Calculate the relevance value of a term applying BM25F function ranking. The
 * {@link BM25FParameters} k1,b_field, boost_field are used.<BR>
 *
 *
 * @see BM25FParameters
 */
public class BM25FTermScorer extends Scorer {

	private TermDocs[] termDocs;
	private float idf = 0f;
	private String[] fields;
	private float[] boosts;
	private float[] bParam;
	private boolean[] termDocsNext;
	private int doc = Integer.MAX_VALUE;
	private boolean initializated = false;
	private byte[][] norms;
	private float[] averageLengths;
	private float K1;
	private int len;
	private float termBoost;
	private int docFreq;
	private int numDocs;
	private String termText;

	public BM25FTermScorer(IndexReader reader, TermQuery term, String[] fields,
	                       float[] boosts, float[] bParams, Similarity similarity) {
		super(similarity);
		this.fields = fields;
		this.boosts = boosts;
		this.bParam = bParams;
		len = fields.length;
		this.termDocs = new TermDocs[len];
		this.termDocsNext = new boolean[len];
		this.norms = new byte[len][];
		this.averageLengths = new float[len];
		this.K1 = BM25FParameters.getK1();
		this.termBoost = term.getBoost();
		this.numDocs = reader.numDocs();
		this.termText = term.getTerm().text();

		try {
			this.docFreq = reader.docFreq(new Term(BM25FParameters.getIdfField(), termText));
			for (int i = 0; i < len; i++) {
				String field = this.fields[i];
				this.termDocs[i] = reader.termDocs(new Term(field, termText));
				norms[i] = reader.norms(field);
				averageLengths[i] = BM25FParameters.getAverageLength(field);
			}
			this.idf = this.getSimilarity().idf(docFreq, numDocs);
          } catch (IOException e) {
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.search.Scorer#docID()
	 */
	@Override
	public int docID() {
		return this.doc;
	}

	private boolean init() throws IOException {
		boolean result = false;
		for (int i = 0; i < len; i++) {
			this.termDocsNext[i] = this.termDocs[i].next();
			if (this.termDocsNext[i] && this.termDocs[i].doc() < this.doc) {
				result = true;
				this.doc = this.termDocs[i].doc();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.lucene.search.Scorer#nextDoc()
	 */
	@Override
	public int nextDoc() throws IOException {
		if (!initializated) {
			this.initializated = true;
			if (this.init()) {
				return this.doc;
			} else {
				return NO_MORE_DOCS;
			}
		}

		int min = NO_MORE_DOCS;

		for (int i = 0; i < len; i++) {
			if (this.termDocsNext[i] && this.termDocs[i].doc() == this.doc) {
				this.termDocsNext[i] = this.termDocs[i].next();
			}
			if (this.termDocsNext[i] && this.termDocs[i].doc() < min)
				min = this.termDocs[i].doc();
		}
		return (this.doc = min);
	}


	/*
		 * (non-Javadoc)
		 *
		 * @see org.apache.lucene.search.Scorer#score()
		 */
	@Override
	public float score() throws IOException {
		float acum = 0f;

		for (int i = 0; i < len; i++) {
			if (this.termDocs[i].doc() == doc) {
				float av_length = this.averageLengths[i];
				float fieldNorm = this.getSimilarity().decodeNormValue(norms[i][this.docID()]);
				float length = 1 / (fieldNorm * fieldNorm);

				float aux = this.bParam[i] * length / av_length;

				aux += (1 - this.bParam[i]);
				acum += (this.termBoost * this.boosts[i] * this.termDocs[i].freq()) / aux;
			}
		}

		acum /= (this.K1 + acum);
		acum *= this.idf;
		return acum;
	}

	@Override
	public int advance(int target) throws IOException {
		if (target == NO_MORE_DOCS)
			return NO_MORE_DOCS;
		while (this.nextDoc() != NO_MORE_DOCS && this.docID() < target) {
		}

		return this.docID();
	}
}
