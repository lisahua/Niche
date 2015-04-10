package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author Lisa
 * @Date: Jan 29, 2015
 */
public class FileAllCorrelation {
	HashMap<String, Integer> fileMethod;
	HashMap<String, Integer> fileAllName;
	HashMap<String, Integer> fileIName;
	HashMap<String, Integer> fileBF;
	HashMap<String, Integer> fileChange;
	HashMap<String, Integer> invokeIN, invokeCN;
	PrintWriter writer;
	

	public void readFile(String file) {
		fileMethod = new HashMap<String, Integer>();
		fileAllName = new HashMap<String, Integer>();
		fileIName = new HashMap<String, Integer>();
		fileBF = new HashMap<String, Integer>();
		fileChange = new HashMap<String, Integer>();
		invokeIN = new HashMap<String, Integer>();
		invokeCN = new HashMap<String, Integer>();
		readBFCorrelate(file + ".correlate");
		readConsistentName(file + ".allNameExpPrimitive");
		readInvocation(file + ".INinvoke", file + ".CNInvoke");
		printAll(file + ".fileCorrelateExpPrimitive");
	}

	// private String getfile(String path) {
	//
	// String[] tokens = path.split("/");
	// if (tokens.length >= 3)
	// return tokens[0] + "/" + tokens[1] + "/" + tokens[2];
	// else
	// return path;
	// }

	private void readConsistentName(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String file = "";
			// int typeValue=0,methodValue=0,fieldValue=0,paraValue=0,varValue =
			// 0;
			int allNameCount = 0, methodCount = 0;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/")) {
					if (file.length() > 1) {
						fileMethod.put(file, methodCount);
						fileAllName.put(file, allNameCount);
					}
					file = getFile(line);
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

	private void readBFCorrelate(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length < 4)
					continue;
				String fName = tokens[0];
				fileIName.put(
						fName,
						Integer.parseInt(tokens[1])
								+ Integer.parseInt(tokens[2])
								+ Integer.parseInt(tokens[3]));
				fileBF.put(fName, Integer.parseInt(tokens[4]));
				fileChange.put(fName, Integer.parseInt(tokens[5]));
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readInvocation(String iPath, String cPath) {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(iPath));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				invokeIN.put(tokens[0], Integer.parseInt(tokens[1]));
			}
			reader = new BufferedReader(new FileReader(cPath));
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				invokeCN.put(tokens[0], Integer.parseInt(tokens[1]));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void printAll(String path) {
		try {
			writer = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (Map.Entry<String, Integer> entry : fileIName.entrySet()) {
			String file = entry.getKey();
			int method = fileMethod.get(file);
			int bf = fileBF.get(file);
			int change = fileChange.get(file);
			int iName = entry.getValue();
			int cName = fileAllName.get(file) - entry.getValue();
			int bf_method = bf == 0 ? 0
					: (((int) (Math.random() * method)) + 1) * bf;
			int nbf_method = change == 0 ? 0
					: (((int) (Math.random() * method)) + 1) * change;

			int bf_in = (bf == 0 || iName == 0) ? 0
					: (int) (Math.random() * bf * iName);
			int bf_cn = (bf == 0 || cName == 0) ? 0
					: (int) (Math.random() * bf * cName);
			int nbf_in = (change == 0 || iName == 0) ? 0 : (int) (Math.random()
					* change * iName);
			int nbf_cn = (change == 0 || cName == 0) ? 0 : (int) (Math.random()
					* change * cName);
			int ic = invokeIN.containsKey(file) ? invokeIN.get(file) : 0;
			int cc = invokeCN.containsKey(file) ? invokeCN.get(file) : 0;

			int[] list = { method, iName, cName, bf, change, bf_method,
					nbf_method, bf_in, bf_cn, nbf_in, nbf_cn,
					(int) (Math.random() * iName),
					(int) (Math.random() * cName) };
			String line = "";
			for (int value : list) {
				line += "," + value;
			}
			writer.println(file + line);
		}
		writer.flush();
		writer.close();
	}

	private String getFile(String path) {
		try {
			for (int level = 9; level > 0; level--) {
				path = path.substring(path.indexOf("/") + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
}
