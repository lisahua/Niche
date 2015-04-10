package seal.niche.evolveAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Feb 8, 2015
 */
public class LifeSpanWindow {

	PrintWriter declareWriter = null, invokeWriter = null;

	public void readFile(String path, int window) {
		try {
			declareWriter = new PrintWriter(path + ".declareAddCmtW");
			invokeWriter = new PrintWriter(path + ".invokeAddCmtW");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		HashMap<String, String> declareC = readDeclCommit(path + ".declareAddCommit");
		HashMap<String, String> invokeC = readInvkCommit(path + ".invokeAddCommit");
		readLogWindow(path + ".log.msg", window, declareC, invokeC);
	}

	private HashMap<String, String> readDeclCommit(String path) {
		HashMap<String, String> declareC = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 2) {
					declareC.put(tokens[1] + "," + tokens[2], tokens[0]);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return declareC;
	}
	private HashMap<String, String> readInvkCommit(String path) {
		HashMap<String, String> invkC = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 3) {
					 invkC.put(tokens[1] + "," + tokens[3].substring(0,2)+ tokens[2], tokens[0]);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return invkC;
	}

	
	private void readLogWindow(String path, int window,
			HashMap<String, String> declareC, HashMap<String, String> invokeC) {
		HashMap<String, String> dNameFix = new HashMap<String, String>();
		HashMap<String, Integer> dDeleteID = new HashMap<String, Integer>();
		HashMap<String, String> iNameFix = new HashMap<String, String>();
		HashMap<String, Integer> iDeleteID = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length < 3)
					continue;
				String file = getFile(tokens[1]);
				// String name = tokens[2];
				for (String declare : declareC.keySet()) {
					if (declare.contains(file)) {
						String allFix = dNameFix.containsKey(declare) ? dNameFix
								.get(declare) : "";
						dNameFix.put(declare, allFix + tokens[2]);
						if (declareC.get(declare).equals(tokens[0])) {
							dDeleteID.put(declare, allFix.length());
						}
					}
				}
				for (String invoke : invokeC.keySet()) {
					if (invoke.contains(file)) {
						String allFix = iNameFix.containsKey(invoke) ? iNameFix
								.get(invoke) : "";
						iNameFix.put(invoke, allFix + tokens[2]);
						if (invokeC.get(invoke).equals(tokens[0])) {
							iDeleteID.put(invoke, allFix.length());
						}
					}
				}
			}
			reader.close();
			for (Map.Entry<String, String> entry : dNameFix.entrySet()) {
				String name = entry.getKey();
				declareWriter.println(name + "," + dDeleteID.get(name) + ","
						+ entry.getValue().length() + "," + entry.getValue());
			}
			for (Map.Entry<String, String> entry : iNameFix.entrySet()) {
				String name = entry.getKey();
				invokeWriter.println(name + "," + iDeleteID.get(name) + ","
						+ entry.getValue().length() + "," + entry.getValue());
			}
			declareWriter.flush();
			invokeWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFile(String absolute) {

		if (!absolute.contains("/"))
			return absolute;
		return absolute.substring(absolute.lastIndexOf("/") + 1).trim();
	}

}
