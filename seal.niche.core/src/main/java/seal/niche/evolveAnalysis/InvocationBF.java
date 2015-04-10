package seal.niche.evolveAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Feb 2, 2015
 */
public class InvocationBF {
	String[] list = { "accumulo", "airavata", "archiva", "cayenne",
			"commons/collect", "commons/io", "commons/lang", "commons/vfs",
			"continuum", "curator", "cxf", "deltaspike", "giraph", "hama",
			"hbase", "jackrabbit", "james", "jclouds", "mahout", "manifoldcf",
			"marmotta", "maven", "mina", "myfaces", "ode", "ofbiz", "openjpa",
			"opennlp", "phoenix", "pig", "pivot", "roller", "stratos",
			"struts", "syncope", "tez", "wicket", "zookeeper", "hadoop" };
	HashSet<String> projects = new HashSet<String>(Arrays.asList(list));
	TreeMap<String, Integer> projectIC = new TreeMap<String, Integer>();
	TreeMap<String, Integer> projectBF = new TreeMap<String, Integer>();
	TreeMap<String, Integer> projectC = new TreeMap<String, Integer>();

	public void readFile(String file) {
		readInvocation(file);
	}

	private void readInvocation(String iPath) {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(iPath));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				try {
					saveToProject(getProject(tokens[0]),
							Integer.parseInt(tokens[1]),
							Integer.parseInt(tokens[2]),
							Integer.parseInt(tokens[3]));
				} catch (Exception e) {

				}
			}
			for (Map.Entry<String, Integer> entry : projectIC.entrySet()) {
				String proj = entry.getKey();
				System.out.println(proj + "," + projectIC.get(proj) + ","
						+ projectBF.get(proj) + "," + projectC.get(proj));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void saveToProject(String project, int ic, int bugFix, int change) {
		if (project.equals(""))
			return;
		int count = projectIC.containsKey(project) ? projectIC.get(project) : 0;
		projectIC.put(project, count + ic);
		count = projectBF.containsKey(project) ? projectBF.get(project) : 0;
		projectBF.put(project, count + bugFix);
		count = projectC.containsKey(project) ? projectC.get(project) : 0;
		projectC.put(project, count + change);
	}

	private String getProject(String path) {
		for (String proj : projects)
			if (path.contains(proj))
				return proj;
		return "";
	}
}
