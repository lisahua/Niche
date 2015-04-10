package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Feb 1, 2015
 */
public class DirAccumulator {
	private HashMap<String, Integer> dirFile, dirMethod, dirAllName, dirIName,
			dirBF, dirChange, dirBFMtd, dirChangeMtd;
	PrintWriter writer;

	public DirAccumulator(String outputFile) {
		try {
			writer = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readDir(String path, String name) {
		dirFile = new HashMap<String,Integer>();
		dirMethod = new HashMap<String, Integer>();
		dirAllName = new HashMap<String, Integer>();
		dirIName = new HashMap<String, Integer>();
		dirBF = new HashMap<String, Integer>();
		dirChange = new HashMap<String, Integer>();
		dirBFMtd = new HashMap<String, Integer>();
		dirChangeMtd = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length < 4)
					continue;
				String dir = getDir(tokens[0]);
				setFile(dir);
				setMethod(dir, tokens[1]);
				setIName(dir, tokens[2]);
				setAllName(dir, tokens[3]);
				setFileBF(dir, tokens[4]);
				setFileChange(dir, tokens[5]);
				setMethodBF(dir, tokens[6]);
				setMethodChange(dir, tokens[7]);
			}
			reader.close();
			printDir(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDir(String path) {
		String[] tokens = path.split("/");
		if (tokens.length > 3)
			return tokens[0] ;
		else
			return path;
	}

	private void setFile(String dir) {
		int count = dirFile.containsKey(dir) ? dirFile.get(dir) : 0;
		dirFile.put(dir, count + 1);
	}

	private void setMethod(String dir, String value) {
		int count = dirMethod.containsKey(dir) ? dirMethod.get(dir) : 0;
		dirMethod.put(dir, count + Integer.parseInt(value));
	}

	private void setIName(String dir, String value) {
		int count = dirIName.containsKey(dir) ? dirIName.get(dir) : 0;
		dirIName.put(dir, count + Integer.parseInt(value));
	}

	private void setAllName(String dir, String value) {
		int count = dirAllName.containsKey(dir) ? dirAllName.get(dir) : 0;
		dirAllName.put(dir, count + Integer.parseInt(value)-1);
	}

	private void setFileBF(String dir, String value) {
		int count = dirBF.containsKey(dir) ? dirBF.get(dir) : 0;
		dirBF.put(dir, count + Integer.parseInt(value));
	}

	private void setFileChange(String dir, String value) {
		int count = dirChange.containsKey(dir) ? dirChange.get(dir) : 0;
		dirChange.put(dir, count + Integer.parseInt(value));
	}

	private void setMethodBF(String dir, String value) {
		int count = dirBFMtd.containsKey(dir) ? dirBFMtd.get(dir) : 0;
		dirBFMtd.put(dir, count + Integer.parseInt(value));
	}

	private void setMethodChange(String dir, String value) {
		int count = dirChangeMtd.containsKey(dir) ? dirChangeMtd.get(dir) : 0;
		dirChangeMtd.put(dir, count + Integer.parseInt(value));
	}

	private void printDir(String name) {
		for (Map.Entry<String, Integer> entry : dirMethod.entrySet()) {
			if (entry.getValue()==0) continue;
			String dir = entry.getKey();
			writer.println(name + "/" + dir + "," + dirFile.get(dir) + ","
					+ entry.getValue() + "," + dirIName.get(dir) + ","
					+ dirAllName.get(dir) + "," + dirBF.get(dir) + ","
					+ dirChange.get(dir) + "," + dirBFMtd.get(dir) + ","
					+ dirChangeMtd.get(dir));
		}
		writer.flush();
	}
}
