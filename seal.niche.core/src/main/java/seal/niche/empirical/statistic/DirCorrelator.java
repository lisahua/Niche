package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Jan 31, 2015
 */
public class DirCorrelator {
	private HashMap<String, Integer> dirIName = new HashMap<String, Integer>();
	private HashMap<String, Integer> dirAllNames = new HashMap<String, Integer>();
	private HashMap<String, Integer> dirMtdCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> dirBugFix = new HashMap<String, Integer>();
	private HashMap<String, Integer> dirChange = new HashMap<String, Integer>();
	private PrintWriter writer;

	public void readFile(String path, String name) {
		readBFCorrelate(path + ".correlateTop", name);
		readConsistentName(path + ".allNames");
	}

	// public void readMethodCount(String path) {
	//
	// }

	private void readConsistentName(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String dir = "";
			// int typeValue=0,methodValue=0,fieldValue=0,paraValue=0,varValue =
			// 0;
			int allNameCount = 0, methodCount = 0;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/")) {
					if (dir.length() > 1) {
						saveAllName(dir, allNameCount);
						saveMethodName(dir, methodCount);
					}
					dir = getDir(line);
					allNameCount = 0;
					methodCount = 0;
				} else if (line.startsWith("m,")) {
					methodCount++;
				} else {
					allNameCount++;
				}
				// } else if (line.startsWith("t,")) {
				// allNameCount++;
				// } else if (line.startsWith("m,")) {
				//
				// } else if (line.startsWith("f,")) {
				//
				// } else if (line.startsWith("p,")) {
				//
				// } else if (line.startsWith("v,")) {
				//
				// }
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readBFCorrelate(String path, String name) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length < 3)
					continue;
				String dir = name + "/" + tokens[0];
				dirIName.put(
						dir,
						Integer.parseInt(tokens[1])
								+ Integer.parseInt(tokens[2])
								+ Integer.parseInt(tokens[3]));
				dirBugFix.put(dir, Integer.parseInt(tokens[4]));
				dirChange.put(dir, Integer.parseInt(tokens[5]));
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDir(String path) {

		String[] tokens = path.split("/");
		if (tokens.length >= 10)
			return tokens[6] + "/" + tokens[7] + "/" + tokens[8] + "/"
					+ tokens[9];
		else
			return path;
	}

	public void printAllDirCorrelate(String path) {
		try {
			PrintWriter writer = new PrintWriter(path);
			for (Map.Entry<String, Integer> entry : dirIName.entrySet()) {
				String dir = entry.getKey();
				if (!dirAllNames.containsKey(dir)) {
					continue;
				}
				int cName = dirAllNames.get(dir) - entry.getValue();

				writer.println(dir + "," + dirMtdCount.get(dir) + ","
						+ dirIName.get(dir) + "," + cName + ","
						+ dirBugFix.get(dir) + "," + dirChange.get(dir));
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveAllName(String dir, int count) {
		int nCount = dirAllNames.containsKey(dir) ? dirAllNames.get(dir) : 0;
		dirAllNames.put(dir, nCount + count);
	}

	private void saveMethodName(String dir, int count) {
		int mCount = dirMtdCount.containsKey(dir) ? dirMtdCount.get(dir) : 0;
		dirMtdCount.put(dir, mCount + count);
	}
}
