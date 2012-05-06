package ro.calin.aggregator;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 28.04.2012
 * Time: 22:08
 */
public interface FetchingService {
    void registerFetcher(String name, ArticleFetcher fetcher);
    void registerSourceForFetching(Source source);
    void startDaemon();
    void stopDaemon();
}
