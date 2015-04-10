package syntacticAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import seal.niche.empirical.statistic.PureMethodBugFix;
import seal.niche.syntacticAnalyser.purityAnalysis.ImpureVerbProjectCompare;
import seal.niche.syntacticAnalyser.purityAnalysis.PurityAnalysisProcessor;
import seal.niche.syntacticAnalyser.purityAnalysis.PurityVerbRatioCalculator;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public class TestPurityAnalysis {
	private String sourcePath = "/Users/admin/Documents/nameExample/snapshots/";
	private String outputFolder = "/Users/admin/Documents/nameExample/workFolder/outputResult/";

	private static String[] projectName = { "accumulo-1.3.5",
			"airavata-airavata-0.5", "archiva-archiva-1.3",
			"cayenne-3.0-final", "commons-collections-COLLECTIONS_3_0",
			"commons-io-IO_1_0", "commons-lang-LANG_3_0",
			"commons-vfs-vfs-1.0", "continuum-continuum-1.0", "curator-1.0.0",
			"cxf-cxf-2.3.0", "deltaspike-deltaspike-project-0.4",
			"giraph-release-0.1.0", "gora-gora-0.2", "hama-0.2-RC2",
			"hbase-0.90.0", "jackrabbit-2.0.0", "james-2_3_2",
			"jclouds-jclouds-1.0.0", "mahout-mahout-0.1",
			"manifoldcf-release-1.0", "marmotta-import", "maven-maven-3.0",
			"mina-1.0.0", "myfaces-2_0_0", "ode-APACHE_ODE_1.2",
			"openjpa-1.0.0", "opennlp-asf_migration", "phoenix-2.2.3",
			"pig-release-0.1.0", "pivot-1.0", "roller-roller_2.0",
			"stratos-3.0.0-incubating", "struts-STRUTS_2_1_0",
			"syncope-syncope-0.2", "tez-release-0.2.0-rc0",
			"wicket-wicket-1.5.0", "zookeeper-release-3.0.0",
			"hadoop-release-1.0.0" };

	// @Test
	public void testPurityRatio() {
		PurityVerbRatioCalculator ratioCal = new PurityVerbRatioCalculator();
		ratioCal.readFile("src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0-bug.txt");

		PurityAnalysisProcessor processor = new PurityAnalysisProcessor(
				"src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0-purity.txt",
				ratioCal.calculateRatio());
		processor
				.readFile("src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0-bug.txt");
		processor.printPureVerbs();
	}

	// @Test
	public void testPurityProcessor() {
		PurityAnalysisProcessor processor = new PurityAnalysisProcessor(
				"src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0-purity.txt");
		processor
				.readFile("src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0-bug.txt");

	}

	// @Test
	public void testPureCorrelate() {
		PureMethodBugFix pureBF = new PureMethodBugFix();
		for (String name : projectName) {
			pureBF.readFile(outputFolder + name);
		}
	}

	// @Test
	public void testCountMethods() {
		HashMap<String, String> verbFreqAll = new HashMap<String, String>();
		HashMap<String, String> verbOccurAll = new HashMap<String, String>();
		try {
			for (String name : projectName) {
				BufferedReader reader = new BufferedReader(new FileReader(
						outputFolder + name + ".verbFrq.csv"));
				for (Map.Entry<String, String> entry : verbFreqAll.entrySet()) {
					verbFreqAll.put(entry.getKey(), entry.getValue() + ",");
					verbOccurAll.put(entry.getKey(),
							verbOccurAll.get(entry.getKey()) + ",");
				}
				String line = "";
				while ((line = reader.readLine()) != null) {
					String[] tokens = line.split(",");
					String verb = tokens[0];
					int pure = Integer.parseInt(tokens[1]);
					int impure = Integer.parseInt(tokens[2]);
					String freqs = verbFreqAll.containsKey(verb) ? verbFreqAll
							.get(verb) : "";
					String occur = verbOccurAll.containsKey(verb) ? verbOccurAll
							.get(verb) : "";
					verbFreqAll.put(verb, freqs + pure * 1.0 / (pure + impure));
					verbOccurAll.put(verb, occur + (pure + impure));

				}
				reader.close();
			}
			PrintWriter writer = new PrintWriter(outputFolder + "verbTable.csv");

			for (Map.Entry<String, String> entry : verbFreqAll.entrySet()) {
				writer.println(entry.getKey() + "," + entry.getValue() + ","
						+ verbOccurAll.get(entry.getKey()));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void testUsagePattern() {
		HashMap<String, String> usages = new HashMap<String, String>();
		try {
			for (String name : projectName) {
				BufferedReader reader = new BufferedReader(new FileReader(
						outputFolder + name + ".identifierAnomal"));
				String line = "";
				while ((line = reader.readLine()) != null) {
					String apiCall = line.split(",")[0].split("--")[1];
					String proj = usages.containsKey(apiCall) ? usages
							.get(apiCall) : "";
					usages.put(apiCall, proj + "," + name);
				}
				reader.close();
			}
			for (Map.Entry<String, String> entry : usages.entrySet()) {
				if (entry.getValue().split(",").length > 2)
					System.out.println(entry.getKey() + "," + entry.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void analyzeSum() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					outputFolder + "allINCorr.csv"));
			PrintWriter writer = new PrintWriter(outputFolder + "bfRate.csv");
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				int count = Integer.parseInt(tokens[2])
						+ Integer.parseInt(tokens[3]);
				if (count > 0)
					writer.println(tokens[1] + "," + tokens[2] + ","
							+ tokens[3]);
			}
			reader.close();
			writer.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

//	@Test
	public void testOverlap() {
		ImpureVerbProjectCompare compare = new ImpureVerbProjectCompare();
		for (String name : projectName) {
			compare.readFile(outputFolder + name + ".verbFrq.csv");
		}
		 System.out.println("pure overlap");
		 compare.getPureOverlap();
		 System.out.println("pure contradict");
		 compare.getPureContradiction();
		 System.out.println("impure overlap");
		 compare.getImpurePureOverlap();
		 System.out.println("impure contradict");
		 compare.getImpureContradiction();
//		System.out.println("pure conflict in IN");
//		ArrayList<Integer> pureINConflict = new ArrayList<Integer>();
//
//		for (int i = 0; i < projectName.length; i++) {
//			pureINConflict.add(compare.getConflictPureTermIN(outputFolder
//					+ projectName[i] + ".purity", i));
//		}
//		for (int i : pureINConflict)
//			System.out.println(i);
//
//		System.out.println("impure conflict in IN");
//		ArrayList<Integer> impureINConflict = new ArrayList<Integer>();
//		for (int i = 0; i < projectName.length; i++) {
//			impureINConflict.add(compare.getConflictImpureTermIN(outputFolder
//					+ projectName[i] + ".pure2", i));
//		}
//		for (int i : impureINConflict)
//			System.out.println( i);
	}

	// @Test
	public void testPureMtd() {
		try {
			for (String name : projectName) {
				BufferedReader reader = new BufferedReader(new FileReader(
						outputFolder + name + ".pure"));
				String line = "";
				int count = 0;
				while ((line = reader.readLine()) != null) {
					if (line.contains("-->"))
						count++;
				}
				System.out.println(count);
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
