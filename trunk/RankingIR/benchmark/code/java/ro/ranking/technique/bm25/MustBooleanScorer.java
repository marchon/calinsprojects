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
 * Boolean Scorer that matches all documents that contains all terms (AND
 * operator).<BR>
 *
 *
 */
public class MustBooleanScorer extends AbstractBooleanScorer {

	private boolean initializated = false;
	private int doc = -1;

	public MustBooleanScorer(Similarity similarity, Scorer[] scorer)
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

	private boolean init() throws IOException {
		for (int i = 0; i < this.subScorer.length; i++) {
			this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);
			if (this.subScorerNext[i] && this.subScorer[i].docID() > this.doc) {
				this.doc = this.subScorer[i].docID();
			}
		}
		return false;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.lucene.search.Scorer#next()
	 */
	@Override
	public int nextDoc() throws IOException {
		// INIT SUBSCORERS
		if (!this.initializated) {
			this.init();
			this.initializated = true;
		} else {
			int max = -1;
			for (int i = 0; i < this.subScorer.length; i++) {
				if (this.subScorerNext[i]
						&& this.subScorer[i].docID() == this.doc) {
					this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);
					if (this.subScorerNext[i] && this.subScorer[i].docID() > max)
						max = this.subScorer[i].docID();
				}
			}
			this.doc = max;
		}
		while (true) {
			int count = 0;
			boolean more = true;
			for (int i = 0; i < this.subScorer.length && more; i++) {
				if (this.subScorerNext[i]) {
					if (this.subScorer[i].docID() == this.doc) {
						count++;
					}
					if (this.subScorer[i].docID() < this.doc) {
						this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);
						if (this.subScorerNext[i]
								&& this.subScorer[i].docID() > this.doc) {
							this.doc = this.subScorer[i].docID();
							more = false;
							count = 0;
						}
					}
					if (count == this.subScorer.length)
						return this.doc;
				} else {
					this.doc = NO_MORE_DOCS;
					return NO_MORE_DOCS;
				}
			}

		}
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
				result = this.subScorer[i].score() + result;

		}
		return (float) result;
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
