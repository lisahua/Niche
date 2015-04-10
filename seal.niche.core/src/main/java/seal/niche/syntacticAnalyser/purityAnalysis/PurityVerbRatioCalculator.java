package seal.niche.syntacticAnalyser.purityAnalysis;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import seal.niche.empirical.statistic.ConfigUtility;
import seal.niche.syntacticAnalyser.parser.UsagePatternFileReader;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public class PurityVerbRatioCalculator extends UsagePatternFileReader {
	private HashMap<String, Integer> pureVerbMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> impureVerbMap = new HashMap<String, Integer>();
	private HashMap<String, Point> verbFrequency = new HashMap<String, Point>();

	@Override
	public void processLine(String usagePattern) {
		String verb = getVerbFromUsagePattern(usagePattern);
		if (verb.length()<1) return ;
		Point counter = (verbFrequency.containsKey(verb)) ? verbFrequency.get(verb)
				: new Point(0,0);
		if (!isPureMethod(usagePattern)) {
			addToImpureMap(verb);
			counter.y = counter.y+1;
		}
		else {
			addToPureMap(verb);
			counter.x = counter.x +1;
		}
		verbFrequency.put(verb, counter);
	}

	public HashMap<String, Double> calculateRatio() {
		pureVerbMap.remove("");
		impureVerbMap.remove("");
		HashMap<String, Double> verbRatioMap = new HashMap<String, Double>();
		for (Map.Entry<String, Integer> entry : pureVerbMap.entrySet()) {
			String verb = entry.getKey();
			double ratio = impureVerbMap.containsKey(verb) ? (entry.getValue() * 1.0)
					/ (impureVerbMap.get(verb) + entry.getValue())
					: 1.0;
					
			if (ratio > ConfigUtility.PURITY_VERB_RATIO_THRESHOLD && ratio < 1)
				verbRatioMap.put(verb, ratio);
		}
		System.out.println("number of verbs: " + verbRatioMap.size());
		return verbRatioMap;
		// StringBuilder pureVerbs = new StringBuilder();
		// ConfigUtility.setPURITY_VERBS(verbRatioMap.keySet());
		/*
		 * TreeMap<String, Double> sortedMap = new TreeMap<String, Double>( new
		 * ValueComparator(verbRatioMap)); sortedMap.putAll(verbRatioMap); for
		 * (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
		 * System.out.println(entry.getKey() + ", " + entry.getValue() + "," +
		 * pureVerbMap.get(entry.getKey())); }
		 */
		// System.out.println("get: " + pureVerbMap.get("get") * 1.0
		// / (pureVerbMap.get("get") + impureVerbMap.get("get")) + ","
		// + pureVerbMap.get("get"));

	}

	public HashMap<String, Point> getAllVerbs() {
		return verbFrequency;
	}

	private void addToImpureMap(String verb) {
		int occurrence = (impureVerbMap.containsKey(verb)) ? impureVerbMap
				.get(verb) : 0;
		impureVerbMap.put(verb, occurrence + 1);

	}

	private void addToPureMap(String verb) {
		int occurrence = (pureVerbMap.containsKey(verb)) ? pureVerbMap
				.get(verb) : 0;
		pureVerbMap.put(verb, occurrence + 1);
	}
}

class ValueComparator implements Comparator<String> {
	HashMap<String, Double> values;

	public ValueComparator(HashMap<String, Double> valueMaps) {
		values = valueMaps;
	}

	public int compare(String arg0, String arg1) {
		if (values.get(arg0) > values.get(arg1))
			return 1;
		else
			return -1;
	}

}
