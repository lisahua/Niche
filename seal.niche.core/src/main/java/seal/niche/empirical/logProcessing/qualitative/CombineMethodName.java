package seal.niche.empirical.logProcessing.qualitative;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class CombineMethodName {
	static String[] projects = new String[] { "accumulo", "airavata",
			"archiva", "cayenne", "collection", "io", "lang", "vfs",
			"continuum", "curator", "cxf", "deltaspike", "giraph", "gora",
			"hadoop", "hama", "hbase", "jackrabbit", "james", "jclouds",
			"mahout", "manifoldcf", "marmotta", "maven", "mina", "face", "ode",
			"openjpa", "opennlp", "phoenix", "pig", "pivot", "roller",
			"stratos", "struts", "syncope", "tez", "wicket", "zookeeper" };

	private static String[] projectName = { "accumulo-1.3.5",
			"airavata-airavata-0.5", "archiva-archiva-1.3",
			"cayenne-3.0-final", "commons-collections-COLLECTIONS_3_0",
			"commons-io-IO_1_0", "commons-lang-LANG_3_0",
			"commons-vfs-vfs-1.0", "continuum-continuum-1.0", "curator-1.0.0",
			"cxf-cxf-2.3.0", "deltaspike-deltaspike-project-0.4",
			"giraph-release-0.1.0", "gora-gora-0.2", "hadoop-release-1.0.0",
			"hama-0.2-RC2", "hbase-0.90.0", "jackrabbit-2.0.0", "james-2_3_2",
			"jclouds-jclouds-1.0.0", "mahout-mahout-0.1",
			"manifoldcf-release-1.0", "marmotta-import", "maven-maven-3.0",
			"mina-1.0.0", "myfaces-2_0_0", "ode-APACHE_ODE_1.2",
			"openjpa-1.0.0", "opennlp-asf_migration", "phoenix-2.2.3",
			"pig-release-0.1.0", "pivot-1.0", "roller-roller_2.0",
			"stratos-3.0.0-incubating", "struts-STRUTS_2_1_0",
			"syncope-syncope-0.2", "tez-release-0.2.0-rc0",
			"wicket-wicket-1.5.0", "zookeeper-release-3.0.0" };
	static TreeMap<String, Integer> projectCount = new TreeMap<String, Integer>();

	public static void combineMethods(String path) {
		HashSet<String> methods = new HashSet<String>();
		ArrayList<String> objects = new ArrayList<String>();
		// ArrayList<String> methods = new ArrayList<String>();
		int count = 1, objCount = 1;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
					+ "allMethodNames.txt");
			PrintWriter objectWriter = new PrintWriter(ConfigUtility.outputPath
					+ "allObjectName.txt");
			String line = "";
			String fileName = "", methodName = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/")) {
					// if (!fileName.contains("Test")) {
					methods.remove("");
					if (methods.size() > 0) {
						for (String s : methods) {
							writer.println(count++ + "," + fileName + "---" + s);
							// recordProjects(fileName);
						}
					}
					// }
					methods.clear();
					objects.remove("");
					if (objects.size() > 0) {
						for (String s : objects) {
							objectWriter.println(objCount++ + "," + fileName
									+ "---" + s);
							recordProjects(fileName);
						}
					}
					objects.clear();

					fileName = getFileName(line);
				} else if (line.startsWith("m,")) {
					String name = line.trim().substring(2).split(",")[0];
					methods.add(name);
					methodName = name;
				} else {
					if (line.contains("f,"))
						objects.add(line.trim());
					else
						objects.add(line.trim() + "," + methodName);
				}
			}
			for (Map.Entry<String, Integer> entry : projectCount.entrySet()) {
				System.out.println(entry.getKey() + "," + entry.getValue());
			}
			reader.close();
			writer.close();
			objectWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getFileName(String file) {
		int index = file
				.indexOf("/Users/admin/Documents/nameExample/snapshots/");
		// recordProjects(file);
		if (index >= 0)
			return file.substring(index + 45);
		return file;
	}

	private static String recordProjects(String path) {
		int index = path.indexOf("/");
		String proj = path.substring(0, index);
		int count = projectCount.containsKey(proj) ? projectCount.get(proj) : 0;
		projectCount.put(proj, count + 1);
		return proj;
	}

	public static void combineINObjWithMethod() throws Exception {
		BufferedReader reader = null;
		String line = "";
		HashSet<String> in_obj = new HashSet<String>();
		HashSet<String> in_obj_match = new HashSet<String>();
		HashMap<String, String> in_file = new HashMap<String, String>();
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allIdentifierAnomal.stats");
		for (String proj : projectName) {
			reader = new BufferedReader(new FileReader(ConfigUtility.inputPath
					+ proj + ".identifierAnomal.stats"));
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				String file = tokens[1]
						.substring(tokens[1].lastIndexOf("/") + 1);
				in_obj.add(file + "," + tokens[0].split("--")[1]);
				in_file.put(file, "");
			}
		}
		reader.close();
		reader = new BufferedReader(new FileReader(ConfigUtility.outputPath
				+ "allObjectName.txt"));
		while ((line = reader.readLine()) != null) {
			String[] token = line.split(",");
			String fileName = token[1].split("--")[0];
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			String key = fileName + "," + token[2];
			if (in_obj.contains(key)) {
				if (token.length > 4)
					writer.println(token[1].split("--")[0] + "," + token[2]
							+ "," + token[4]);
				else
					writer.println(token[1].split("--")[0] + "," + token[2]
							+ ",");
				in_obj_match.add(token[1].split("--")[0] + "," + token[2]);
			}
			if (in_file.containsKey(fileName)) {
				in_file.put(fileName, token[1].split("--")[0]);
			}
			// else
			// writer.println(key + ",");
		}
		for (String s : in_obj) {
			if (!in_obj_match.contains(s)) {
				String file = s.split(",")[0];
				if (in_file.containsKey(file))
					writer.println(in_file.get(file) + "," + s.split(",")[1]
							+ ",");
				else
					System.out.println(in_file.get(s.split(",")[0]));
			}
		}
		reader.close();
		writer.close();
	}

	public static void combinePureMethods() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allPure.txt"));
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allPureMethod.txt");
		String line = "", fileName = "";
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("/")) {
				fileName = getFileName(line);
			} else {
				// if (line.contains("."))
				writer.println(fileName + "," + line.split("-->")[0]);
			}
		}
		reader.close();
		writer.close();
	}

	public static void combineImpureMethods() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allImpure.txt"));
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allImpureMethod.txt");
		String line = "";
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(",");
			writer.println(tokens[1] + "," + tokens[0].split("--")[1]);
		}
		reader.close();
		writer.close();
	}

	public static void formatImpureMethods() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allImpureMethod.txt"));
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allImpureMethod2.txt");
		String line = "";
		int proj = 0;
		while ((line = reader.readLine()) != null) {
			if (line.contains(projects[proj])) {
				writer.println(projectName[proj] + "/" + line);
			} else {

				for (String s : projects) {
					if (line.contains(s)) {
						proj++;
						System.out.println(line);
						break;
					}
				}
			}

		}
		reader.close();
		writer.close();
	}

	public static void combineINWithAllMethods() throws Exception {
		BufferedReader impureReader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allImpureMethod.txt"));
		BufferedReader pureReader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allPureMethod.txt"));
		BufferedReader objReader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allIdentifierAnomal.txt"));
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allMethodIN.txt");
		HashSet<String> impure = new HashSet<String>();
		HashSet<String> pure = new HashSet<String>();
		HashSet<String> impure_match = new HashSet<String>();
		HashSet<String> pure_match = new HashSet<String>();
		HashMap<String, Boolean> obj = new HashMap<String, Boolean>();
		HashSet<String> obj_match = new HashSet<String>();
		HashMap<String, String> lines = new HashMap<String, String>();
		String line = "";
		while ((line = impureReader.readLine()) != null) {
			impure.add(line.trim());
		}
		while ((line = pureReader.readLine()) != null) {
			pure.add(line.trim());
		}
		while ((line = objReader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length > 2)
				obj.put(tokens[0] + "," + tokens[2], false);
			else
				obj.put(tokens[0] + ",", false);
		}
		impureReader.close();
		pureReader.close();
		objReader.close();
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodNames.txt"));
		int countImpure = impure.size();
		int countPure = pure.size();
		int countObj = obj.size();
		while ((line = reader.readLine()) != null) {
			String key = line.split(",")[1] + "," + line.split(",")[2];
			int impureN = 0, pureN = 0, objN = 0;
			if (impure.contains(key)) {
				impureN = 1;
				impure.remove(key);
				countImpure--;
			}

			if (pure.contains(key)) {
				pureN = 1;
				countPure--;
				pure.remove(key);
			}
			if (obj.containsKey(key)) {
				objN = 1;
				countObj--;
				obj.put(key, true);
			}
			lines.put(line, objN + "," + impureN + "," + pureN + ",");
		}
		for (String s : obj.keySet()) {
			if (!obj.get(s)) {
				obj_match.add(s.replace(",", ""));
			}
		}
		for (String s : impure)
			impure_match.add(s.split(",")[0]);
		for (String s : pure)
			pure_match.add(s.split(",")[0]);
		for (String s : lines.keySet()) {
			String[] token = lines.get(s).split(",");
			int impureN = 0, pureN = 0, objN = 0;
			if (token.length > 2) {
				impureN = Integer.parseInt(token[1]);
				pureN = Integer.parseInt(token[2]);
				objN = Integer.parseInt(token[0]);
			} else {
				System.out.println(s + "," + lines.get(s));
			}
			String key = s.split(",")[1];
			if (impure_match.contains(key)) {
				impureN = 1;
				impure_match.remove(key);
				countImpure--;
			}

			if (pure_match.contains(key)) {
				pureN = 1;
				countPure--;
				pure_match.remove(key);
			}
			// System.out.println(key);
			// for (String sb: obj_match)
			// System.out.println(sb);
			if (obj_match.contains(key)) {
				objN = 1;
				countObj--;
				obj_match.remove(key);
			}
			if (impureN == 1 || pureN == 1 || objN == 1) {
				lines.put(s, objN + "," + impureN + "," + pureN + ",");
			}
		}
		// for (String s : obj_match)
		// System.out.println(s);
		// for (String s : pure_match)
		// System.out.println(s);
		// for (String s : impure_match)
		// System.out.println(s);
		System.out.println(countObj + "," + countImpure + "," + countPure);
		reader.close();
		for (String s : lines.keySet())
			writer.println(s + "," + lines.get(s));
		writer.close();
	}

	public static void countINMethods() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodIN.txt"));
		String line ="";
		int in_2=0,in_1=0,in_0=0;
		while ((line = reader.readLine()) != null) {
			if (line.contains("1,0,1")||line.contains("1,1,0")||line.contains("1,1,1")) {in_2++;
			System.out.println(line);
			}
			if (line.contains("1,0,0")||line.contains("0,1,0")||line.contains("0,1,1")||line.contains("0,0,1")) in_1++;
			else in_0++;
		}
		System.out.println(in_2+","+in_1+","+in_0);
	}
	
	
}
