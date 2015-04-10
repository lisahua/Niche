package seal.niche.evolveAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Feb 3, 2015
 */
public class LifeSpanClassify {
	HashMap<String, Integer> identifierAnomaly = new HashMap<String, Integer>();
	HashMap<String, String> path_name = new HashMap<String, String>();
	HashMap<String, Integer> paraLF = new HashMap<String, Integer>();
	HashMap<String, Integer> lvLF = new HashMap<String, Integer>();
	HashMap<String, Integer> fieldLF = new HashMap<String, Integer>();
	String[] list = { "accumulo", "airavata", "archiva", "cayenne",
			"commons/collect", "commons/io", "commons/lang", "commons/vfs",
			"continuum", "curator", "cxf", "deltaspike", "giraph", "hama",
			"hbase", "jackrabbit", "james", "jclouds", "mahout", "manifoldcf",
			"marmotta", "maven", "mina", "myfaces", "ode", "ofbiz", "openjpa",
			"opennlp", "phoenix", "pig", "pivot", "roller", "stratos",
			"struts", "syncope", "tez", "wicket", "zookeeper", "hadoop" };
	// HashMap<String, Integer> projects = new HashMap<String, Integer>();
	int paraC = 0, fieldC = 0, varC = 0;
	int periodNow = -1;

	public void readFile(String path) {
		periodNow = -1;
		readIdentifierAnomaly(path + ".identifierAnomal.stats");
		readLifeSpan(path + ".lifespan");
		readType(path + ".allNames");
		
	}

	private void readLifeSpan(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			// boolean hasSetProj = false;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				String file = tokens[0];
				int lifespan = Integer.parseInt(tokens[2]);
				if (identifierAnomaly.containsKey(file)
						&& identifierAnomaly.get(file) == -1) {
					identifierAnomaly.put(file, lifespan);
				}
				if (periodNow<0 || (periodNow > lifespan && lifespan > 0)) {
					periodNow = lifespan;
				}
			}
			reader.close();
		} catch (Exception e) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(path
						+ "s"));
				String line = "";
				while ((line = reader.readLine()) != null) {
					String[] tokens = line.split(",");
					String file = tokens[0];
					int lifespan = Integer.parseInt(tokens[2]);
					if (identifierAnomaly.containsKey(file)
							&& identifierAnomaly.get(file) == -1) {
						identifierAnomaly.put(file, lifespan);
					}
					if (periodNow<0 || (periodNow > lifespan && lifespan > 0)) {
						periodNow = lifespan;
					}
				}
				reader.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	private void readType(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String id_name = "";
			String fileName = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/")) {
					fileName = getFile(line);
					if (identifierAnomaly.containsKey(fileName)) {
						id_name = path_name.get(fileName);
						continue;
					}
				} else if (id_name.length() > 1) {
					if (line.contains(id_name)) {
						char c = line.charAt(0);
						int days = 0;
						switch (c) {
						case 'p': paraC++;
							days = identifierAnomaly.containsKey(fileName)
									&& identifierAnomaly.get(fileName) > 0 ? identifierAnomaly
									.get(fileName) : periodNow;
							if (days > 0)
								paraLF.put(fileName, days);
							break;
						case 'v': varC++;
							days = identifierAnomaly.containsKey(fileName)
									&& identifierAnomaly.get(fileName) > 0 ? identifierAnomaly
									.get(fileName) : periodNow;
							if (days > 0)
								lvLF.put(fileName, days);
							break;
						case 'f': fieldC ++;
							days = identifierAnomaly.containsKey(fileName)
									&& identifierAnomaly.get(fileName) > 0 ? identifierAnomaly
									.get(fileName) : periodNow;
							if (days > 0)
								fieldLF.put(fileName, days);
							break;
						default:
							break;
						}
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readIdentifierAnomaly(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			int count = 0;
			while ((line = reader.readLine()) != null) {
				count++;
				String[] tokens = line.split("--");
				if (tokens.length > 0) {
					String[] fileName = tokens[1].split(",");
					if (fileName.length > 0) {
						identifierAnomaly.put(fileName[1], -1);
						path_name.put(fileName[1], fileName[0]);
					}
				}
			}
			reader.close();
			System.out.println(file+","+count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFile(String absolute) {
		int pos = absolute.indexOf("/", 0);
		for (int i = 5; i >= 0; i--)
			pos = absolute.indexOf("/", pos + 1);
		return absolute.substring(pos + 1);
	}

	public void printAll(String path) {
		try {
			PrintWriter writer = new PrintWriter(path);

			// paraLF.removeAll(Collections.singleton(null));
			// lvLF.removeAll(Collections.singleton(null));
			// fieldLF.removeAll(Collections.singleton(null));
			writer.println("Parameter");
			for (Map.Entry<String, Integer> entry : paraLF.entrySet())
				writer.print(entry.getValue() + ",");
			writer.println("\n\n variable");
			for (Map.Entry<String, Integer> entry : lvLF.entrySet())
				writer.print(entry.getValue() + ",");
			writer.println("\n\n field");
			for (Map.Entry<String, Integer> entry : fieldLF.entrySet())
				writer.print(entry.getValue() + ",");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
