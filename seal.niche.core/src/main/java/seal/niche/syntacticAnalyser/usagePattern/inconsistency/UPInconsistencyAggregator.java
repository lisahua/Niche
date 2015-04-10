package seal.niche.syntacticAnalyser.usagePattern.inconsistency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import seal.niche.syntacticAnalyser.parser.CustomizedFileReader;

/**
 * @Author Lisa
 * @Date: Jan 20, 2015
 */
public class UPInconsistencyAggregator extends CustomizedFileReader {
	String processingAPI;
	HashMap<String, Integer> nameMap = new HashMap<String, Integer>();
	HashMap<String, Integer> mtdMap = new HashMap<String, Integer>();
	PrintWriter nameWriter, methodWriter;

	public UPInconsistencyAggregator(String namePath, String mtdPath) {
		try {
			nameWriter = new PrintWriter(namePath);
			methodWriter = new PrintWriter(mtdPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readFile(String file) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				processLine(line);
			}
			reader.close();
			nameWriter.close();
			methodWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processLine(String usagePattern) {
		String[] tokens = usagePattern.split(",");
		if (processingAPI == null) {
			processingAPI = tokens[0].trim();
		} else if (!processingAPI.equals(tokens[0].trim())) {
			initNewAPI(usagePattern);
		}
		addNameToMap(tokens);
	}

	private void initNewAPI(String usagePattern) {
		findAnomaly();
		String[] tokens = usagePattern.split(",");
		processingAPI = tokens[0].trim();
		nameMap.clear();
		mtdMap.clear();
	}

	private void addNameToMap(String[] tokens) {
		if (tokens.length<3)
			return;
		String name = tokens[1].trim();
		String method = tokens[2].trim();
		if (!name.equals("")) {
			int times = nameMap.containsKey(name) ? nameMap.get(name) : 0;
			nameMap.put(name, times + 1);
		}
		if (!method.equals("")) {
			int times = mtdMap.containsKey(method) ? mtdMap.get(method) : 0;
			mtdMap.put(method, times + 1);
		}
	}

	private void findAnomaly() {
		String names = "";
		String methods = "";
		if (nameMap.size() > 1) {
			for (Map.Entry<String, Integer> entry : nameMap.entrySet())
				names += entry.getKey() + ":" + entry.getValue() + ",";
			nameWriter.println(processingAPI + "," + names);
		}
		if (mtdMap.size() > 1) {
			for (Map.Entry<String, Integer> entry : mtdMap.entrySet())
				methods += entry.getKey() + ":" + entry.getValue() + ",";
			methodWriter.println(processingAPI + "," + methods);
		}
	}
	
}
