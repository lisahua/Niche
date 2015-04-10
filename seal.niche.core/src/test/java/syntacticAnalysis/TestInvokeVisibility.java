package syntacticAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import org.testng.annotations.Test;

import seal.niche.empirical.logProcessing.NameRecorder;
import seal.niche.empirical.statistic.BayesianCorrelation;
import seal.niche.empirical.statistic.DirAccumulator;
import seal.niche.empirical.statistic.DirCorrelator;
import seal.niche.empirical.statistic.FileAllCorrelation;
import seal.niche.empirical.statistic.LifeSpanINCN;
import seal.niche.empirical.statistic.TopLevelDirDetector;
import seal.niche.evolveAnalysis.InvocationBF;
import seal.niche.evolveAnalysis.LifeSpanClassify;

/**
 * @Author Lisa
 * @Date: Jan 26, 2015
 */
public class TestInvokeVisibility {
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
	public void countMethods() {
		NameRecorder recorder = new NameRecorder();
		for (String name : projectName) {
			System.out.println(name
					+ ","
					+ recorder.getNumOfMethods(outputFolder + name
							+ ".allNames"));
		}

	}

	// @Test
	public void getNameAdd() {
		for (String name : projectName) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						outputFolder + name + ".allNames"));
				int field = 0, method = 0, variable = 0, parameter = 0;
				int clazz = 0;
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("f,"))
						field++;
					else if (line.startsWith("m,"))
						method++;
					else if (line.startsWith("v"))
						variable++;
					else if (line.startsWith("p"))
						parameter++;
					else
						clazz++;
				}
				System.out.println(name + "," + method + "," + field + ","
						+ variable + "," + clazz / 2 + "," + parameter);
				reader.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}

	// @Test
	public void testTopLevelDirDetector() {
		TopLevelDirDetector topDetector = new TopLevelDirDetector();
		for (String name : projectName) {
			topDetector.readFile(outputFolder + name);
		}
	}

	// @Test
	public void testScriptGenerate() {
		// for (String name:projectName) {
		// System.out.println("find "+name
		// +" -maxdepth 3 -type d >> allDirs.txt");
		// }

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					sourcePath + "allDirs.txt"));
			PrintWriter writer = new PrintWriter(sourcePath
					+ "scriptFileCount.csv");
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.split("/").length > 3) {
					writer.println(line + "," + "find " + line
							+ " -name *.java | wc -l");
				}
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// @Test
	public void correlateDirWithMet() {
		DirCorrelator dirC = new DirCorrelator();
		for (String name : projectName) {
			dirC.readFile(outputFolder + name, name);
		}
		dirC.printAllDirCorrelate(outputFolder + "allDirCorrelate.txt");
	}

	// @Test
	public void testFileAllCorrelation() {
		FileAllCorrelation fileCor = new FileAllCorrelation();
		for (String name : projectName) {
			fileCor.readFile(outputFolder + name);

		}
	}

	// @Test
	public void testDirAccumulator() {
		DirAccumulator dirA = new DirAccumulator(outputFolder
				+ "dirAccumulate3.csv");
		for (String name : projectName)
			dirA.readDir(outputFolder + name + ".fileCorrelate", name);
	}

	// @Test
	public void testLifeSpanINCN() {
		LifeSpanINCN lifespan = new LifeSpanINCN(outputFolder
				+ "allLifeSpan.csv");
		for (String name : projectName) {
			lifespan.readFile(outputFolder + name);
			System.out.println(name);
		}
	}

	// @Test
	public void testBayesianCorrelation() {
		BayesianCorrelation bayes = new BayesianCorrelation(outputFolder
				+ "bayes.csv");
		for (String name : projectName)
			bayes.readFileCorrelation(outputFolder + name
					+ ".fileCorrelateExpPrimitive", name);
	}

	// @Test
	public void generateRandom() {
		int count = 50;
		while (count > 0) {
			double value = Math.random();
			if (value < 0.3) {
				System.out.println(value);
				count--;
			}
		}
		System.out.println("\n\n");
		count = 50;
		while (count > 0) {
			double value = Math.random();
			if (value < 0.8 && value > 0.1) {
				System.out.println(value);
				count--;
			}
		}
	}

	//@Test
	public void testInvocation() {
		InvocationBF bf = new InvocationBF();
//		for (String name : projectName)
			bf.readFile(outputFolder + "invokeMethod.stat");
	}
	//@Test
	public void testLifeSpanClassify () {
		LifeSpanClassify  life = new LifeSpanClassify ();
		for (String name:projectName) {
			life.readFile(outputFolder+name);
		}
		life.printAll(outputFolder+"lifespanClass.txt");
	}
}
