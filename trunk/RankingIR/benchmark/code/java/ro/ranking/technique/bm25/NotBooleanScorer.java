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
 * Boolean Scorer that matches all documents that do NOT contains any term (NOT
 * operator).<BR>
 *
 *
 */
public class NotBooleanScorer extends AbstractBooleanScorer {

	private int doc = -1;
	private int numDocs;

	public NotBooleanScorer(Similarity similarity, Scorer[] scorer, int numDocs)
			throws IOException {
		super(similarity, scorer);
		this.numDocs = numDocs;
		for (int i = 0; i < this.subScorer.length; i++)
			this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);

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
		while (this.doc < this.numDocs - 1) {
			this.doc++;
			int count = 0;
			for (int i = 0; i < this.subScorer.length; i++) {
				if (this.subScorerNext[i])
					if (this.subScorer[i].docID() != this.doc) {
						count++;
					} else {
						this.subScorerNext[i] = (this.subScorer[i].nextDoc() != NO_MORE_DOCS);
						count = 0;
					}
				else
					count++;
				if (count == this.subScorer.length)
					return this.doc;
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
		return 1;
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
