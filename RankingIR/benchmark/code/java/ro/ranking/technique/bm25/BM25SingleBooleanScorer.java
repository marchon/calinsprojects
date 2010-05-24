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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Similarity;

import ro.ranking.technique.bm25.BM25BooleanQuery.BooleanTermQuery;

/**
 * BM25SingleBooleanScorer, calculates the total relevance value based boolean
 * expression, that has just one common operator (AND, OR, NOT) for all terms.<BR>
 */
public class BM25SingleBooleanScorer extends Scorer {

  private AbstractBooleanScorer booleanScorer = null;

  /**
   * Fill scorer array with BM25TermScorers.
   * @param reader
   * @param termQuery
   * @param similarity
   * @throws IOException
   */
  public BM25SingleBooleanScorer(IndexReader reader,
                                 BooleanTermQuery[] termQuery, Similarity similarity)
      throws IOException {
    super(similarity);

    Scorer[] scorer = new Scorer[termQuery.length];
    for (int i = 0; i < scorer.length; i++) {
      scorer[i] = new BM25TermScorer(reader, termQuery[i].termQuery,
          similarity);
    }

    if (termQuery[0].occur == BooleanClause.Occur.MUST)
      this.booleanScorer = new MustBooleanScorer(similarity, scorer);
    else if (termQuery[0].occur == BooleanClause.Occur.SHOULD)
      this.booleanScorer = new ShouldBooleanScorer(similarity, scorer);
    else
      this.booleanScorer = new NotBooleanScorer(similarity, scorer,
          reader.numDocs());

  }

  /**
   * Fill scorer array with BM25FTermScorers using the fields, boosts and bParams parameters.
   * @param reader
   * @param termQuery
   * @param similarity
   * @param fields
   * @param boosts
   * @param bParams
   * @throws IOException
   */
  public BM25SingleBooleanScorer(IndexReader reader,
                                 BooleanTermQuery[] termQuery, Similarity similarity,
                                 String[] fields, float[] boosts, float[] bParams)
      throws IOException {
    super(similarity);
    Scorer[] scorer = new Scorer[termQuery.length];

    for (int i = 0; i < scorer.length; i++) {
      scorer[i] = new BM25FTermScorer(reader, termQuery[i].termQuery,
          fields, boosts, bParams, similarity);
    }

    if (termQuery[0].occur == BooleanClause.Occur.MUST)
      this.booleanScorer = new MustBooleanScorer(similarity, scorer);
    else if (termQuery[0].occur == BooleanClause.Occur.SHOULD)
      this.booleanScorer = new ShouldBooleanScorer(similarity, scorer);
    else
      this.booleanScorer = new NotBooleanScorer(similarity, scorer,
          reader.numDocs());

  }

  /*
    * (non-Javadoc)
    *
    * @see org.apache.lucene.search.Scorer#docID()
    */
  @Override
  public int docID() {
    return booleanScorer.docID();
  }

  @Override
  public int nextDoc() throws IOException {
    return booleanScorer.nextDoc();
  }


  /*
      * (non-Javadoc)
      *
      * @see org.apache.lucene.search.Scorer#score()
      */
  @Override
  public float score() throws IOException {
    return booleanScorer.score();

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
