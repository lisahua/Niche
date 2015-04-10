package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MannWhitneyDirAccumulator {

	public void readFile(String filePath) {
		try {
			readCorrelate(filePath);
			// writer.println(filePath + "," + bugChange + "," + correlate);
			System.out.println(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readCorrelate(String filePath) throws Exception {
		BufferedReader logReader = new BufferedReader(new FileReader(filePath
				+ ".correlate1"));
		int subLevel = 1;
		PrintWriter writer = new PrintWriter(filePath + ".correlate" + subLevel);
		String line = "";
		HashMap<String, Integer> dirIName = new HashMap<String, Integer>();
		HashMap<String, Integer> dirBugFix = new HashMap<String, Integer>();
		HashMap<String, Integer> dirChange = new HashMap<String, Integer>();
		while ((line = logReader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length > 3) {
				String dir = getDir(tokens[0], subLevel);
				try {
					int iName = dirIName.containsKey(dir) ? dirIName.get(dir)
							: 0;
					dirIName.put(
							dir,
							iName + Integer.parseInt(tokens[1])
									+ Integer.parseInt(tokens[2])
									+ Integer.parseInt(tokens[3]));
					int bugFix = dirBugFix.containsKey(dir) ? dirBugFix
							.get(dir) : 0;
					dirBugFix.put(dir, bugFix + Integer.parseInt(tokens[4]));

					int change = dirChange.containsKey(dir) ? dirChange
							.get(dir) : 0;
					dirChange.put(dir, change + Integer.parseInt(tokens[5]));
				} catch (NumberFormatException e) {

				}
			}
		}
		logReader.close();
		for (Map.Entry<String, Integer> entry : dirIName.entrySet()) {
			String dir = entry.getKey();
			int bugFix = dirBugFix.containsKey(dir) ? dirBugFix.get(dir) : 0;
			int change = dirChange.containsKey(dir) ? dirChange.get(dir) : 0;
			writer.println(dir + "," + entry.getValue() + "," + bugFix + ","
					+ change);
			writer.flush();
		}
		writer.close();
	}

	private String getDir(String path, int level) {
		if (!path.contains("/"))
			return path;
		for (; level > 0; level--) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		return path;
	}
}
