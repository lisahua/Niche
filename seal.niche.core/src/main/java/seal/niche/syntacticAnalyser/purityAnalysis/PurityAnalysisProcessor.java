package seal.niche.syntacticAnalyser.purityAnalysis;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import seal.niche.empirical.statistic.ConfigUtility;
import seal.niche.syntacticAnalyser.parser.UsagePatternFileReader;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public class PurityAnalysisProcessor extends UsagePatternFileReader {
	private Set<String> pureVerbs;
	private HashMap<String, Double> verbRatioMap;

	public PurityAnalysisProcessor(String bugOutputPath) {
		pureVerbs = ConfigUtility.getPURITY_VERBS();
		try {
			writer = new PrintWriter(bugOutputPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PurityAnalysisProcessor(String bugOutputPath,
			HashMap<String, Double> pureVerbMap) {
		try {
			writer = new PrintWriter(bugOutputPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		verbRatioMap = pureVerbMap;
		this.pureVerbs = pureVerbMap.keySet();
	}

	@Override
	public void processLine(String usagePattern) {
		if (!isPureMethod(usagePattern))
			if (pureVerbs.contains(getVerbFromUsagePattern(usagePattern))) {
				writer.println(usagePattern);
			}
	}

	public void printPureVerbs() {

		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(
				new ValueComparator(verbRatioMap));
		sortedMap.putAll(verbRatioMap);
		for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}

	}
}
