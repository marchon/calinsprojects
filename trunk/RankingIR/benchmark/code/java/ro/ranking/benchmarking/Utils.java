package ro.ranking.benchmarking;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.util.Version;

public class Utils {
	private Utils() {
	}

	static public float getDocLen(float norm) {
		return 1.0f / norm / norm;
	}

	static public float getAvgDocLen(SegmentInfos segInfo) throws Exception {
		float sumT = 0.0f;
		float numT = 0.0f;

		int numSegments = segInfo.size();
		for (int i = 0; i < numSegments; i++) {
			SegmentInfo info = segInfo.info(i);
			SegmentReader reader = null;
			reader = SegmentReader.get(false, info, 1);
			Collection<String> fieldNames = reader
					.getFieldNames(IndexReader.FieldOption.ALL);
			Iterator<String> it = fieldNames.iterator();
			byte[] b = new byte[reader.maxDoc()];
			while (it.hasNext()) {
				String fieldName = (String) it.next();
				reader.norms(fieldName, b, 0);
				float sum = 0.0f;
				for (int j = 0; j < b.length; j++) {
					float dl = getDocLen(Similarity.getDefault()
							.decodeNormValue(b[j]));
					sum += dl;
				}

				sumT += sum;
				numT += b.length;
			}
			reader.close();
		}

		if (sumT == 0 || numT == 0) {
			throw new Exception("Unable to calculate average length.");
		}

		return sumT / numT;
	}

	private static String[] searchPatterns = {
			"org.apache.lucene.analysis.%Uname%Analyzer",
			"org.apache.lucene.analysis.standard.%Uname%Analyzer",
			"org.apache.lucene.analysis.%cc%.%Uname%Analyzer",
			"ro.ranking.analysis.%name%.%Uname%Analyzer" };

	private static Map<String, String> nameToISO = new HashMap<String, String>();
	static {
		nameToISO.put("english", "en");
		nameToISO.put("persian", "fa");
	}

	private static String instPattern(String pattern, String repl) {
		String urepl = repl.substring(0, 1).toUpperCase() + repl.substring(1);

		String ret = pattern.replaceAll("%name%", repl);
		ret = ret.replaceAll("%Uname%", urepl);

		if (pattern.indexOf("%cc%") != -1) {
			String cc = nameToISO.get(repl);
			if (cc != null) {
				ret = ret.replaceAll("%cc%", cc);
			}
		}

		return ret;
	}

	public static Class<? extends Analyzer> loadAnalyzerClass(String name) {
		for (String pattern : searchPatterns) {
			try {
				return (Class<? extends Analyzer>) Class.forName(instPattern(
						pattern, name));
			} catch (Exception e) {
				// ignore
			}
		}

		return SimpleAnalyzer.class;
	}

	private static Analyzer defaultAnalyzer = null;

	public static void setDefaultAnalyzer(Analyzer defaultAnalyzer) {
		Utils.defaultAnalyzer = defaultAnalyzer;
	}

	public static Analyzer getDefaultAnalyzer() {
		if (defaultAnalyzer == null) {
			defaultAnalyzer = new SimpleAnalyzer(Version.LUCENE_31);
		}
		return defaultAnalyzer;
	}

	public static void main(String[] args) throws IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		Constructor<Analyzer> analyzerConstructor = (Constructor<Analyzer>) Utils
				.loadAnalyzerClass("standard").getConstructor(Version.class);
		Utils.setDefaultAnalyzer(analyzerConstructor
				.newInstance(Version.LUCENE_31));
		
		System.out.println(getDefaultAnalyzer());
	}
}
