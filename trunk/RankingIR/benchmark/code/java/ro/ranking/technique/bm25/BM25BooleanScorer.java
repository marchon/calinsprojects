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
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;

import ro.ranking.technique.bm25.BM25BooleanQuery.BooleanTermQuery;

/**
 * BM25BooleanScorer, calculates the total relevance value based in a boolean
 * expression.<BR>
 *
 *
 */
public class BM25BooleanScorer extends Scorer {

	private AbstractBooleanScorer shouldBooleanScorer;
	private AbstractBooleanScorer mustBooleanScorer;
	private AbstractBooleanScorer notBooleanScorer;
	private boolean hasMoreShould = false;
	private boolean hasMoreMust = false;
	private boolean hasMoreNot = false;
	private int doc = -1;
	private int ndocs;
	private boolean initialized = false;

  /**
   * Build a BM25BooleanScorer composed of atoms that are BM25TermScorers.
   * The scorer will give the score for a boolean formula combining the subscorers.
   * @param reader
   * @param should - array of BM25TermScorers appearing as SHOULD
   * @param must - array of BM25TermScorers appearing as MUST
   * @param not - array of BM25TermScorers appearing as NOT
   * @param similarity
   * @throws IOException
   */
	public BM25BooleanScorer(IndexReader reader, BooleanTermQuery[] should,
	                         BooleanTermQuery[] must, BooleanTermQuery[] not,
	                         Similarity similarity) throws IOException {
		super(similarity);

		this.ndocs = reader.numDocs();

		if (should != null && should.length > 0) {

			Scorer[] shouldScorer = new Scorer[should.length];
			for (int i = 0; i < shouldScorer.length; i++) {
				shouldScorer[i] = new BM25TermScorer(reader,
						should[i].termQuery, similarity);
			}
			this.shouldBooleanScorer = new ShouldBooleanScorer(similarity,
					shouldScorer);

		} else
			this.shouldBooleanScorer = new MatchAllBooleanScorer(similarity,
					this.ndocs);

		if (must != null && must.length > 0) {
			Scorer[] mustScorer = new Scorer[must.length];
			for (int i = 0; i < mustScorer.length; i++) {
				mustScorer[i] = new BM25TermScorer(reader, must[i].termQuery,
						similarity);
			}

			this.mustBooleanScorer = new MustBooleanScorer(similarity,
					mustScorer);
		} else
			this.mustBooleanScorer = new MatchAllBooleanScorer(similarity,
					this.ndocs);

		if (not != null && not.length > 0) {
			Scorer[] notScorer = new Scorer[not.length];
			for (int i = 0; i < notScorer.length; i++) {
				notScorer[i] = new BM25TermScorer(reader, not[i].termQuery,
						similarity);
			}

			this.notBooleanScorer = new NotBooleanScorer(similarity, notScorer,
					this.ndocs);
		} else
			this.notBooleanScorer = new MatchAllBooleanScorer(similarity,
					this.ndocs);
	}

  /**
   * Build a BM25BooleanScorer composed of atoms that are BM25FTermScorers.
   * The scorer will give the score for a boolean formula combining the subscorers.
   * Each subscorer combines the fields' scores using the given boosts and bParams.
   * @param reader
   * @param should - array of BM25FTermScorers appearing as SHOULD
   * @param must - array of BM25FTermScorers appearing as MUST
   * @param not - array of BM25FTermScorers appearing as NOT
   * @param similarity
   * @throws IOException
   */
	public BM25BooleanScorer(IndexReader reader, BooleanTermQuery[] should,
	                         BooleanTermQuery[] must, BooleanTermQuery[] not,
	                         Similarity similarity, String[] fields, float[] boosts,
	                         float[] bParams) throws IOException {
		super(similarity);
		this.ndocs = reader.numDocs();
		if (should != null && should.length > 0) {
			Scorer[] shouldScorer = new Scorer[should.length];
			for (int i = 0; i < shouldScorer.length; i++) {
				shouldScorer[i] = new BM25FTermScorer(reader,
						should[i].termQuery, fields, boosts, bParams,
						similarity);
			}

			this.shouldBooleanScorer = new ShouldBooleanScorer(similarity,
					shouldScorer);
		} else
			this.shouldBooleanScorer = new MatchAllBooleanScorer(similarity,
					this.ndocs);

		if (must != null && must.length > 0) {
			Scorer[] mustScorer = new Scorer[must.length];
			for (int i = 0; i < mustScorer.length; i++) {
				mustScorer[i] = new BM25FTermScorer(reader, must[i].termQuery,
						fields, boosts, bParams, similarity);
			}

			this.mustBooleanScorer = new MustBooleanScorer(similarity,
					mustScorer);
		} else
			this.mustBooleanScorer = new MatchAllBooleanScorer(similarity,
					this.ndocs);

		if (not != null && not.length > 0) {
			Scorer[] notScorer = new Scorer[not.length];
			for (int i = 0; i < notScorer.length; i++) {
				notScorer[i] = new BM25FTermScorer(reader, not[i].termQuery,
						fields, boosts, bParams, similarity);
			}

			this.notBooleanScorer = new NotBooleanScorer(similarity, notScorer,
					this.ndocs);
		} else
			this.notBooleanScorer = new MatchAllBooleanScorer(similarity,
					this.ndocs);

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

	private void init() throws IOException {
		this.hasMoreShould = (this.shouldBooleanScorer.nextDoc() != NO_MORE_DOCS);
		this.hasMoreMust = (this.mustBooleanScorer.nextDoc() != NO_MORE_DOCS);
		this.hasMoreNot = (this.notBooleanScorer.nextDoc() != NO_MORE_DOCS);
	}

	private void doNext() throws IOException {
		if (this.hasMoreShould && this.shouldBooleanScorer.docID() == this.doc)
			this.hasMoreShould = (this.shouldBooleanScorer.nextDoc() != NO_MORE_DOCS);
		if (this.hasMoreMust && this.mustBooleanScorer.docID() == this.doc)
			this.hasMoreMust = (this.mustBooleanScorer.nextDoc() != NO_MORE_DOCS);
		if (this.hasMoreNot && this.notBooleanScorer.docID() == this.doc)
			this.hasMoreNot = (this.notBooleanScorer.nextDoc() != NO_MORE_DOCS);
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.apache.lucene.search.Scorer#nextDoc()
		 */
	@Override
	public int nextDoc() throws IOException {

		if (!this.initialized) {
			this.initialized = true;
			this.init();
		} else {
			this.doNext();
		}

		while (this.doc < this.ndocs - 1) {
			this.doc++;
			if (this.hasMoreMust) {
				if (this.mustBooleanScorer.docID() < this.doc)
					this.hasMoreMust = (this.mustBooleanScorer.nextDoc() != NO_MORE_DOCS);
			} else {
				this.doc = NO_MORE_DOCS;
				return NO_MORE_DOCS;
			}

			if (this.hasMoreNot) {
				if (this.notBooleanScorer.docID() < this.doc)
					this.hasMoreNot = (this.notBooleanScorer.nextDoc() != NO_MORE_DOCS);
			} else {
				this.doc = NO_MORE_DOCS;
				return NO_MORE_DOCS;
			}

			if (this.hasMoreShould) {
				if (this.shouldBooleanScorer.docID() < this.doc)
					this.hasMoreShould = (this.shouldBooleanScorer.nextDoc() != NO_MORE_DOCS);
			}

			if (this.hasMoreMust && this.hasMoreNot) {
				if (this.mustBooleanScorer.docID() == this.notBooleanScorer.docID())
					return this.doc;
			} else {
				this.doc = NO_MORE_DOCS;
				return NO_MORE_DOCS;
			}
		}

		this.doc = NO_MORE_DOCS;
		return NO_MORE_DOCS;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.search.Scorer#score()
	 */
	@Override
	public float score() throws IOException {
		float result = 0f;
		if (this.hasMoreMust && this.mustBooleanScorer.docID() == doc)
			result += this.mustBooleanScorer.score();

		if (this.hasMoreShould && this.shouldBooleanScorer.docID() == doc)
			result += this.shouldBooleanScorer.score();

		return result;
	}

	@Override
	public int advance(int target) throws IOException {
		if (target == NO_MORE_DOCS)
			return NO_MORE_DOCS;
		while ((this.nextDoc() != NO_MORE_DOCS) && this.docID() < target) {
		}

		return this.docID();
	}

}
