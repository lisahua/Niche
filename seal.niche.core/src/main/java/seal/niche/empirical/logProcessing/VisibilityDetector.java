package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Jan 26, 2015
 */
public class VisibilityDetector {
	private HashMap<String, String> iFiles = new HashMap<String, String>();
	private HashMap<String, Integer> iFileCount = new HashMap<String, Integer>();
//private 	 HashSet<String> invokeFiles = new HashSet<String>();
	public void readFile(String filePath) {
		iFiles = readPurityFile(filePath + ".purity");
		iFiles.putAll(readStatsFile(filePath + ".methodAnomal.stats"));
		iFiles.putAll(readStatsFile(filePath + ".identifierAnomal.stats"));
		iFiles.remove("");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".up"));
			PrintWriter writer = new PrintWriter(filePath + ".invoke");
			String line = "";
			String fileName = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					fileName = getFile(line);
				} else if (line.trim().length() > 2) {
					getInvokedFile(line,fileName);
				}
			}
			for (Map.Entry<String, Integer> entry : iFileCount.entrySet()) {
				writer.println(iFiles.get(entry.getKey()) + ","
						+ entry.getValue());
			}
			reader.close();
			writer.close();
			iFiles.clear();
			iFileCount.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, String> readPurityFile(String file) {
		HashMap<String, String> pNames = new HashMap<String, String>();
		String prevFile = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					prevFile = line;
				} else if (line.contains("-->")) {
					pNames.put(getClassName(prevFile), getFile(prevFile));
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return pNames;
	}

	private HashMap<String, String> readStatsFile(String file) {
		HashMap<String, String> pNames = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("--");
				if (tokens.length > 0) {
					String[] fileName = tokens[1].split(",");
					if (fileName.length > 0) {
						pNames.put(getClassName(fileName[1]),
								getFile(fileName[1]));
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pNames;
	}

	private String getFile(String absolute) {
		int pos = absolute.indexOf("/", 0);
		for (int i = 5; i >= 0; i--)
			pos = absolute.indexOf("/", pos + 1);
		return absolute.substring(pos + 1);
	}

	// TODO if too much, restrict to method level.
	private String getClassName(String name) {
		name = name.contains("/") ? name.substring(name.lastIndexOf("/")+1)
				: name;
		if (name.length() > 5)
			return name.substring(0, name.length() - 5);
		return name;
	}

	private void getInvokedFile(String up,String file) {
		// HashSet<String> invokeFiles = new HashSet<String>();
		for (String ivkFile : iFiles.keySet()) {
			if (up.contains(ivkFile)) {
				int count = iFileCount.containsKey(ivkFile) ? iFileCount
						.get(ivkFile) : 0;
				iFileCount.put(ivkFile, count + 1);
//			invokeFiles.add(file);
			}
		}
	}
}
