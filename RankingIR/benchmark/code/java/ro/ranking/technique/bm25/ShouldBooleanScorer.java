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

import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;

/**
 * Boolean Scorer that matches all documents that contains at least one term (OR
 * operator).<BR>
 *
 *
 */
public class ShouldBooleanScorer extends AbstractBooleanScorer {

	private boolean initializated = false;
	private int doc = Integer.MAX_VALUE;

	public ShouldBooleanScorer(Similarity similarity, Scorer scorer[])
			throws IOException {
		super(similarity, scorer);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.lucene.search.Scorer#nextDoc()
	 */
	@Override
	public int nextDoc() throws IOException {
		if (!this.initializated) {
			this.initializated = true;
			if (this.init()) {
				return this.doc;
			} else {
				return NO_MORE_DOCS;
			}
		}
		int min = NO_MORE_DOCS;
		// AVANZO LOS TERMDOCS CON MENOR ID
		for (int i = 0; i < this.subScorer.length; i++) {
			if (this.subScorerNext[i] && this.subScorer[i].docID() == this.doc) {
				this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);
			}
			if (this.subScorerNext[i] && this.subScorer[i].docID() < min)
				min = this.subScorer[i].docID();
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
		double result = 0f;
		for (int i = 0; i < this.subScorer.length; i++) {
			if (this.subScorer[i].docID() == this.doc)
				result += this.subScorer[i].score();

		}
		return (float) result;
	}

	private boolean init() throws IOException {
		boolean result = false;
		for (int i = 0; i < this.subScorer.length; i++) {
			this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);
			if (this.subScorerNext[i] && this.subScorer[i].docID() < this.doc) {
				this.doc = this.subScorer[i].docID();
				result = true;
			}
		}
		return result;
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
