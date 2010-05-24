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
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.*;

/**
 * Calculate the relevance value of a single term applying BM25 function
 * ranking. The {@link BM25Parameters} k1, and b are used.<BR>
 *
 *
 * @see BM25Parameters
 */
public class BM25TermScorer extends Scorer {

	private TermQuery term;
	private IndexReader reader;
	private TermDocs termDocs;
	private float idf;
	private float avgLength;
	private byte[] norm;
	private float b;
	private float k1;
	private int doc = -1;

	public BM25TermScorer(IndexReader reader, TermQuery term, Similarity similarity)
			throws IOException {
		super(similarity);
		this.reader = reader;
		this.term = term;
		this.idf = this.getSimilarity().idf(reader.docFreq(term.getTerm()), reader.numDocs());
		this.norm = this.reader.norms(this.term.getTerm().field());
		this.avgLength = BM25Parameters.getAverageLength(this.term.getTerm().field());
		this.b = BM25Parameters.getB();
		this.k1 = BM25Parameters.getK1();
		this.termDocs = this.reader.termDocs(this.term.getTerm());
	}

	@Override
	public int docID() {
		return doc;
	}



	@Override
	public int nextDoc() throws IOException {
		boolean result = this.termDocs.next();
		if (result) {
			doc = this.termDocs.doc();
		} else {
			this.termDocs.close();
			doc = NO_MORE_DOCS;
		}
		return doc;
	}

	@Override
	public float score() throws IOException {
		float fieldNorm = this.getSimilarity().decodeNormValue(this.norm[this.docID()]);
		float length = 1 / (fieldNorm * fieldNorm);

		// LENGTH NORMALIZATION
		float result = this.b * (length / this.avgLength);
		result += 1 - this.b;

		result = (this.term.getBoost() * this.termDocs.freq()) / result;
		// FREQ SATURATION
		result /= (result + this.k1);

		return result * this.idf;

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
