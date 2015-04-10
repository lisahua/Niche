package syntacticAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.testng.annotations.Test;

import seal.niche.empirical.logProcessing.AnomalyCalculator;
import seal.niche.empirical.logProcessing.AnomalyFileTracer;
import seal.niche.empirical.logProcessing.BayesCorrelation;
import seal.niche.empirical.logProcessing.CorrelationAccumulator;
import seal.niche.empirical.logProcessing.LifeSpanAccumulator;
import seal.niche.empirical.logProcessing.LifeSpanDetector;
import seal.niche.empirical.logProcessing.NameMannWhitneyCounter;
import seal.niche.empirical.logProcessing.NameRecorder;
import seal.niche.empirical.logProcessing.ProjectAccumulator;
import seal.niche.empirical.logProcessing.VisibilityAccumulator;
import seal.niche.empirical.logProcessing.VisibilityDetector;
import seal.niche.lexicalAnalyser.processing.SimpleNamePrinter;

/**
 * @Author Lisa
 * @Date: Jan 22, 2015
 */
public class TestLogProcessing {
	private String sourcePath = "/Users/admin/Documents/nameExample/snapshots/";
	private String outputFolder = "/Users/admin/Documents/nameExample/workFolder/outputResult/";
	// private String[] projectName = {
	// "abdera-abdera-1.0",
	// "activemq-activemq-4.0", "ambari-release-1.2.0",
	// "any23-any23-0.3.0", "accumulo-1.3.5", "airavata-airavata-0.5",
	// "ant-ANT_15_FINAL", "archiva-archiva-1.3", "aries-0.4",
	// "avro-release-1.5.0", "batik-batik_1_0", "bigtop-release-0.6.0",
	// "camel-camel-2.0.0", "cassandra-cassandra-2.0.0", "chainsaw-1_2_1",
	// "chukwa-chukwa-0.5.0", "click-click-2.2.0", "cayenne-3.0-final",
	// "chainsaw-1_2_1", "cloudstack-2.2.0",
	// "commons-collections-COLLECTIONS_3_0", "commons-io-IO_1_0",
	// "commons-jexl-COMMONS_JEXL-1_1", "commons-lang-LANG_3_0",
	// "commons-math-MATH_2_0", "commons-net-NET_3_0",
	// "commons-vfs-vfs-1.0", "continuum-continuum-1.0", "couchdb-1.0.0",
	// "curator-1.0.0", "cxf-cxf-2.3.0", "deltacloud-release-1.0.0",
	// "deltaspike-deltaspike-project-0.4", "esme-apache-esme-1.2",
	// "felix-3fc86e23a283d0baee26de15d427a7866155151a",
	// "flume-flume-1.2.0", "fop-fop-1_0", "forrest-forrest_06",
	// "giraph-release-0.1.0", "gora-gora-0.2", "hadoop-release-1.0.0",
	// "hama-0.2-RC2", "hbase-0.90.0", "hive-release-0.5.0",
	// "isis-isis-1.0.0", "jackrabbit-2.0.0", "james-2_3_2",
	// "jclouds-jclouds-1.0.0", "jena-jena-2.7.X", "kafka-0.8.0",
	// "knox-0.2.0-branch", "lenya-RELEASE_2_0", "libcloud-0.4.0",
	// "log4j-1_2_16", "lucene-solr-lucene_solr_3_3",
	// "lucy-apache-lucy-incubating-0.2.0", "mahout-mahout-0.1",
	// "manifoldcf-release-1.0", "marmotta-import", "maven-maven-3.0",
	// "mesos-0.10.0", "mina-1.0.0", "mrunit-release-0.5.0-incubating",
	// "myfaces-2_0_0", "nutch-release-1.0", "ode-APACHE_ODE_1.2",
	// "ofbiz-REL-4.0", "oodt-0.2", "openjpa-1.0.0",
	// "opennlp-asf_migration", "openoffice-AOO340.zip.crdownload",
	// "pdfbox-1.0.0", "phoenix-2.2.3", "pig-release-0.1.0", "pivot-1.0",
	// "poi-REL_3_0", "qpid-0.5", "rave-0.10", "river-2.1.1",
	// "roller-roller_2.0", "servicemix-servicemix-5.0.0",
	// "shindig-shindig-project-1.0-incubating", "shiro-1.2.0",
	// "sling-sling-13", "solr-release-1.1.0",
	// "stanbol-stanbol-parent-1-incubating", "stratos-3.0.0-incubating",
	// "struts-STRUTS_2_1_0", "syncope-syncope-0.2", "tajo-release-0.2.0",
	// "tez-release-0.2.0-rc0", "thrift-0.2.0", "tika-0.7",
	// "tiles-tiles-2.0.0", "tomee-tomee-1.0.0", "vysper-0.5",
	// "whirr-release-0.7.0", "wicket-wicket-1.5.0",
	// "xmlbeans-xmlbeans-1-0-0", "zookeeper-release-3.0.0", };
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
			"pig-release-0.1.0", "pivot-1.0",  "roller-roller_2.0",
			"stratos-3.0.0-incubating", "struts-STRUTS_2_1_0",
			"syncope-syncope-0.2", "tez-release-0.2.0-rc0",
			"wicket-wicket-1.5.0", "zookeeper-release-3.0.0",
			"hadoop-release-1.0.0" };
	private String hadoop = "hadoop-release-1.0.0";

	//@Test
	public void testLogProcessing() {
		AnomalyCalculator calc = new AnomalyCalculator();
//		for (String name : projectName) {
			calc.readFile(outputFolder + "hama-0.2-RC2" + ".log");
//		}
	}

	// @Test
	public void testUPFileTracer() {

		AnomalyFileTracer tracer;
		for (String name : projectName) {
			tracer = new AnomalyFileTracer(outputFolder + name
					+ ".methodAnomal", outputFolder + name
					+ ".identifierAnomal");
			tracer.readFile(outputFolder + name + ".upi");
		}
	}

	// @Test
	public void testCorrelationFile() {
		CorrelationAccumulator correlator = new CorrelationAccumulator();
//		String[] testNames = { "phoenix-2.2.3" };
//		for (String name : projectName) {
			correlator.correlatePNBug(outputFolder + "hama-0.2-RC2");
//		}
	}

	// @Test
	public void testProjectCorrelation() {
		ProjectAccumulator projectAc = new ProjectAccumulator(outputFolder
				+ "allProject.correlate");
		for (String project : projectName) {
			projectAc.readFile(outputFolder + project);
		}
	}

	// @Test
	public void testIdentifierCounter() {
		for (String name : projectName) {
			SimpleNamePrinter nameCount = new SimpleNamePrinter(outputFolder
					+ name + ".allName");
			System.out.println(name + ","
					+ nameCount.getNameInDir(sourcePath + name) + ","
					+ nameCount.getfileNum());
		}
	}

	// @Test
	public void testLifeSpan() {
		// LifeSpanDetector lfCounter = new LifeSpanDetector();
		/**
		 * Wed Jul 16 22:44:23 2014 +0000
		 */
		// String time = "Wed Jul 16 22:44:23 2014 +0000";
		// DateFormat format = new SimpleDateFormat(
		// "EEE MMM dd kk:mm:ss yyyy ZZZZ", Locale.ENGLISH);
		// Date date;
		// try {
		// date = format.parse(time);
		// System.out.println(date); //
		// } catch (ParseException e) {

		// e.printStackTrace();
		// }

		for (String name : projectName) {
			new LifeSpanDetector(outputFolder + name, sourcePath + name)
					.detectPreservedFiles();
		}
	}

//	 @Test
	public void testNameRecorder() {
		NameRecorder nameRecorder;
		for (String name : projectName) {
			nameRecorder = new NameRecorder(outputFolder + name + ".allNameExpPrimitive");
			nameRecorder.getNameInDir(sourcePath + name);
//			new NameRecorder()
//			 .getNameInFile(new
//			 File("/Users/admin/Desktop/FSOutputSummer.java"));
//			System.out.println(name);
		}
	}

//	 @Test
	public void testBayesCorrelation() {
		BayesCorrelation correlator = new BayesCorrelation();
		for (String name : projectName) {
			correlator.readFile(outputFolder + name);
		}
	}

	// @Test
	public void testNameMannWhitney() {
		NameMannWhitneyCounter nmw = new NameMannWhitneyCounter(outputFolder);
		for (String name : projectName) {
			nmw.readFile(outputFolder + name);
		}
		nmw.printDate();
	}

	// @Test
	public void execute() {
		String[] items = { "bless10", "b10to20", "b20to30", "b30to40",
				"b40to50", "bmore50" };
		String[] items2 = { "cless10", "c10to20", "c20to30", "c30to40",
				"c40to50", "cmore50" };
		for (int i = 0; i < items.length - 1; i++) {
			for (int j = i + 1; j < items.length; j++)
				System.out.println("cliff.delta(" + items[i] + "," + items[j]
						+ ")");
		}
		for (int i = 0; i < items2.length - 1; i++) {
			for (int j = i + 1; j < items2.length; j++)
				System.out.println("cliff.delta(" + items2[i] + "," + items2[j]
						+ ")");
		}
	}

	// @Test
	public void redoLifeSpan() {
		String name = "accumulo-1.3.5";
		new LifeSpanDetector(outputFolder + name, sourcePath + name)
				.detectPreservedFiles();
	}

 // @Test
	public void testLifeSpanAccumulate() {
		LifeSpanAccumulator lfAccum = new LifeSpanAccumulator();
		for (String name : projectName) {
			lfAccum.countAvgLifeSpan(outputFolder + name);
		}
	}

	// @Test
	public void testVisibility() {
		VisibilityDetector visDetector = new VisibilityDetector();
		for (String name : projectName) {
			visDetector.readFile(outputFolder + name);
		}
	}

	// @Test
	public void testVisAccumulate() {
		VisibilityAccumulator visDetector = new VisibilityAccumulator();
		for (String name : projectName) {
			visDetector.readFile(outputFolder + name);
		}
	}

	// @Test
	public void testConcateVisibility() {
		try {
			PrintWriter writer = new PrintWriter(outputFolder + "allCorrelate");
			for (String name : projectName) {
				BufferedReader reader = new BufferedReader(new FileReader(
						outputFolder + name + ".invokeFile.stats"));
				String line = "";
				while ((line = reader.readLine()) != null) {
					writer.println(line);
				}
				reader.close();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
