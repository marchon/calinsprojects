package ro.calin.aggregator;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 28.04.2012
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public interface ArticleFetcher {
    List<Article> fetch(Source source);
}
