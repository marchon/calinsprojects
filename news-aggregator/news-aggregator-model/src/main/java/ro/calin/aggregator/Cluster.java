package ro.calin.aggregator;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Calin
 * Date: 28.04.2012
 * Time: 20:54
 */
public class Cluster {
    private long id;
    private List<String> frequentWords;
    private List<Article> articles;
    private Article center;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getFrequentWords() {
        return frequentWords;
    }

    public void setFrequentWords(List<String> frequentWords) {
        this.frequentWords = frequentWords;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Article getCenter() {
        return center;
    }

    public void setCenter(Article center) {
        this.center = center;
    }
}
