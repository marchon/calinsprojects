package ro.ranking.benchmarking.chart;

import java.util.Map;

public class PRChartInfo {
	private String corpus;
	private Map<String, double[]> pinterps;
	
	public PRChartInfo(String corpus) {
		setCorpus(corpus);
	}
	
	public PRChartInfo(String corpus, Map<String, double[]> pinterps) {
		setCorpus(corpus);
		setPinterps(pinterps);
	}
	
	public String getCorpus() {
		return corpus;
	}
	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}
	public Map<String, double[]> getPinterps() {
		return pinterps;
	}
	public void setPinterps(Map<String, double[]> pinterps) {
		this.pinterps = pinterps;
	}
}
