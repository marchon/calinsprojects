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

import org.apache.lucene.search.Similarity;

/**
 * Boolean Scorer that matches all documents.<BR>
 *
 *
 */
public class MatchAllBooleanScorer extends AbstractBooleanScorer {

	private int doc = -1;
	private int ndocs;

	public MatchAllBooleanScorer(Similarity similarity, int numDocs)
			throws IOException {
		super(similarity, null);
		this.ndocs = numDocs;
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

	@Override
	public int nextDoc() throws IOException {
		this.doc++;
		if (this.doc >= this.ndocs)
			this.doc = NO_MORE_DOCS;
		return this.doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.search.Scorer#score()
	 */
	@Override
	public float score() throws IOException {
		return 0;
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