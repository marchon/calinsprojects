package ro.calin.aggregator;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 28.04.2012
 * Time: 23:02
 */
public interface ClusteringService {
    void startService();
    void stopService();
    void requestClusteringForNonclusteredArticles();
}
