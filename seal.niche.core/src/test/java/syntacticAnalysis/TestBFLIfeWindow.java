package syntacticAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Stack;

import org.testng.annotations.Test;

import seal.niche.evolveAnalysis.AccumulateAll;
import seal.niche.evolveAnalysis.DeclarationDeleteCommit;
import seal.niche.evolveAnalysis.LifeSpanWindow;
import seal.niche.evolveAnalysis.RevisionHistory;
import seal.niche.evolveAnalysis.WindowSizeChange;

/**
 * @Author Lisa
 * @Date: Feb 8, 2015
 */
public class TestBFLIfeWindow {
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
	public void testBFLifeWindow() {
		DeclarationDeleteCommit lifeW = new DeclarationDeleteCommit();
		for (String name : projectName)
			lifeW.readFile(outputFolder + name, 0);
		// for (int window = -50;window <=50;window++) {
		//
		// }
	}

	// @Test
	public void testLifeSpanWindow() {
		LifeSpanWindow lfWindow = new LifeSpanWindow();
		for (String name : projectName) {
			lfWindow.readFile(outputFolder + name, 1);
		}
	}

	// @Test
	public void testAccumulate() {
		AccumulateAll acc = new AccumulateAll(outputFolder);
		for (String name : projectName) {
			acc.readDeclareFile(outputFolder + name + ".declareCmtW");
			acc.readInvokeFile(outputFolder + name + ".invokeCmtW");
		}
	}

	// @Test
	public void testWindowSize() {
		WindowSizeChange window = new WindowSizeChange();
		for (int i = -50; i < 51; i++) {
			window.readFile(outputFolder, i);
		}
	}

	// @Test
	public void testSwap() {
		BufferedReader reader;
		try {
			String line = "";
			Stack<String> lines = new Stack<String>();
			reader = new BufferedReader(new FileReader(outputFolder
					+ "rename.csv"));
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("-"))
					lines.push(line.substring(1));
				else
					lines.push("-" + line);
			}
			reader.close();
			while (!lines.isEmpty()) {
				System.out.println(lines.pop());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//@Test
	public void testAddBugFix() {
		Stack<String> printLine = new Stack<String>();
		for (int i = 1; i < 31; i++) {
			double[] rates = new double[2];
			for (String name : projectName) {
				RevisionHistory history = new RevisionHistory();
				history.readLogFile(outputFolder+name+".log.msg");
				double[] rate = history.readINCommit(outputFolder+name+".invokeAddCommit", i);
				rates[0] += rate[0];
				rates[1] += rate[1];
			}
			printLine.push("-"+i+","+rates[1]/(projectName.length*i));	
			System.out.println(i+","+rates[0]/(projectName.length*i));	
		}
		while (!printLine.isEmpty()) {
			System.out.println(printLine.pop());
		}
	}
	//@Test
//	public void testDeleteBugFix() {
//		Stack<String> printLine = new Stack<String>();
//		for (int i = 1; i < 31; i++) {
//			double rate = 0;
//			for (String name : projectName) {
//				RevisionHistory history = new RevisionHistory();
//				history.readLogFile(outputFolder+name+".log.msg");
//				rate += history.readINDeleteCommit(outputFolder+name+".declareCommit", i);
//			}
//			printLine.push("-"+i+","+rate/(projectName.length*i));	
//		}
//		while (!printLine.isEmpty()) {
//			System.out.println(printLine.pop());
//		}
//	}
	@Test
	public void random () {
		for (int i=0;i<100;i++) {
			System.out.println((Math.random()+1)*0.3);
		}
	}
}
