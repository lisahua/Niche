package syntacticAnalysis;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.testng.annotations.Test;

import seal.niche.syntacticAnalyser.purityAnalysis.ImpureVerbProjectCompare;
import seal.niche.syntacticAnalyser.purityAnalysis.PurityAnalysisProcessor;
import seal.niche.syntacticAnalyser.purityAnalysis.PurityVerbRatioCalculator;
import seal.niche.syntacticAnalyser.usagePattern.UsagePatternProcessor;
import seal.niche.syntacticAnalyser.usagePattern.inconsistency.UPInconsistencyAggregator;
import seal.niche.syntacticAnalyser.usagePattern.inconsistency.UPInconsistencyCluster;
import seal.niche.syntacticAnalyser.usagePattern.inconsistency.UPInconsistentProcessor;
import seal.niche.syntacticAnalyser.usagePattern.inconsistency.UPProjSpecificRules;

public class TestUsagePatternProcessor {
	private String folderPath = "/Users/admin/Documents/nameExample/snapshots/";
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
	public void testVisitor() {
		UsagePatternProcessor processor = new UsagePatternProcessor(
				"src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0-up2.txt");
		processor
				.getNameInDir("/Users/admin/Documents/snapshot/elasticsearch-1.0.0");
	}

	// @Test
	public void testPurityAnalysis() {
		for (String prj : projectName) {
			UsagePatternProcessor upProcessor = new UsagePatternProcessor(
					outputFolder + prj + ".up");
			upProcessor.getNameInDir(folderPath + prj);
			System.out.println(outputFolder + prj + ".up");

			System.out.println(outputFolder + prj + ".purity");
			PurityVerbRatioCalculator ratioCal = new PurityVerbRatioCalculator();
			ratioCal.readFile(outputFolder + prj + ".up");
			PurityAnalysisProcessor purityProcessor = new PurityAnalysisProcessor(
					outputFolder + prj + ".purity", ratioCal.calculateRatio());
			purityProcessor.readFile(outputFolder + prj + ".up");
			// purityProcessor.printPureVerbs();

			UPInconsistentProcessor upiProcessor = new UPInconsistentProcessor(
					outputFolder + prj + ".upi");
			upiProcessor.readFile(outputFolder + prj + ".up");
			upiProcessor.sortUsagePattern();

			UPInconsistencyAggregator icDetector = new UPInconsistencyAggregator(
					outputFolder + prj + ".name", outputFolder + prj
							+ ".method");
			System.out.println(outputFolder + prj + ".upi");
			icDetector.readFile(outputFolder + prj + ".upi");

			UPInconsistencyCluster cluster = new UPInconsistencyCluster(
					outputFolder + prj + ".identifierAnomal");
			cluster.readFile(outputFolder + prj + ".name");
			cluster = new UPInconsistencyCluster(outputFolder + prj
					+ ".methodAnomal");
			cluster.readFile(outputFolder + prj + ".method");

		}
	}

	//@Test
	public void testPureMethodAnalysis() {
		HashMap<String, Point> verbFrequency = new HashMap<String, Point>();
		for (String prj : projectName) {

			// System.out.println(outputFolder + prj + ".pure");
			PurityVerbRatioCalculator ratioCal = new PurityVerbRatioCalculator();
			ratioCal.readFile(outputFolder + prj + ".up");

			// PureMethodProcessor purityProcessor = new PureMethodProcessor(
			// outputFolder + prj + ".pure3", ratioCal.calculateRatio());
			// purityProcessor.readFile(outputFolder + prj + ".up");
			// purityProcessor.filterInterface();
			// purityProcessor.printPureVerbs();
			try {
				PrintWriter writer = new PrintWriter(outputFolder + prj
						+ ".verbFrq.csv");
				for (Map.Entry<String, Point> entry : ratioCal.getAllVerbs()
						.entrySet()) {
					String verb = entry.getKey();
					Point counter = verbFrequency.containsKey(verb) ? verbFrequency
							.get(verb) : new Point(0, 0);
					counter.y = counter.y + entry.getValue().y;
					counter.x = counter.x + entry.getValue().x;
					verbFrequency.put(entry.getKey(), counter);
					writer.println(verb + "," + counter.x + "," + counter.y);
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// try {
		// PrintWriter writer = new PrintWriter(outputFolder
		// + "allVerbFrequency.csv");
		// for (Map.Entry<String, Point> entry : verbFrequency.entrySet()) {
		// Point counter = entry.getValue();
		// writer.println(entry.getKey() + "," + counter.x + ","
		// + (counter.x + counter.y));
		// }
		// writer.close();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}

	//@Test
	public void testNumUP() {
		try {
			for (String name : projectName) {
				BufferedReader reader = new BufferedReader(new FileReader(
						outputFolder + name + ".upi"));
				String line = "";
				HashSet<String> lineSet = new HashSet<String>();
				while ((line = reader.readLine()) != null) {
					lineSet.add(line);
				}
				reader.close();
				System.out.println(lineSet.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void testUPSpecificRule() {
		UPProjSpecificRules upSpec  = new UPProjSpecificRules();
		for (String name:projectName) {
			upSpec.readFile(outputFolder+name);
		}
		upSpec.getOverlap();
		
	}
	public void testFPV() {
		ArrayList<HashMap<String,String>> identifiers = new ArrayList<HashMap<String,String>>();
		try {
			
			for (String name:projectName) {
			BufferedReader reader = new BufferedReader(new FileReader(outputFolder+name+".methodAnomal"));
			String line = "";
			HashMap<String,String> useName = new HashMap<String,String>();
			while ((line=reader.readLine())!=null) {
				String api = line.split("--")[1].split(",")[0];
				String apiName = line.split("--")[0];
				String temp = useName.containsKey(api)? useName.get(api):"";
				useName.put(api, temp+","+apiName);
			}
			identifiers.add(useName);
			reader.close();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testCorpus() {
		ImpureVerbProjectCompare compare = new ImpureVerbProjectCompare();
		for (String name : projectName) {
			compare.readFile(outputFolder + name + ".verbFrq.csv");
		}
		compare.getCorpus();
	}
}
