package ro.calin.aggregator;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 28.04.2012
 * Time: 22:26
 */
public interface Persistence {
    void persistArticle(Article article);
    void persistCluster(Cluster cluster);
    void persistSource(Source source);

    List<Source> getSources();

    List<Cluster> getClusters();
    List<Cluster> getClustersByWords(List<String> words);

    List<Article> getNonclusteredArticles();
    List<Article> getArticlesByClusterAndDate(long clusterId, Date date);
    List<Article> getArticlesByDate(Date date);
    List<Article> getArticlesByProperties(Map<String, String> properties);

    Article getArticleWithContent(long articleId);
}
