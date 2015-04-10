package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Jan 22, 2015
 */
public class CorrelationAccumulator {
	private HashMap<String, Integer> bugFixMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> changesMap = new HashMap<String, Integer>();

	private void readLogFile(String file) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 2) {
					String fileName = tokens[1].substring(tokens[1].indexOf("org"));
					int isBuggy = Integer.parseInt(tokens[2]);
					if (isBuggy == 1) {
						int number = bugFixMap.containsKey(fileName) ? bugFixMap
								.get(fileName) : 0;
						bugFixMap.put(fileName, number + 1);
					} else {
						int number = changesMap.containsKey(fileName) ? changesMap
								.get(fileName) : 0;
						changesMap.put(fileName, number + 1);
					}
				}
			}
			reader.close();
		} catch (Exception e) {

		}
	}

	private HashMap<String, Integer> readPurityFile(String file) {
		HashMap<String, Integer> fileNum = new HashMap<String, Integer>();
		String prevFile = "";
		int isPure = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					if (prevFile.length() > 1) {
						fileNum.put(prevFile, isPure);
						isPure = 0;
					}
					prevFile = getFile(line);
				} else if (line.contains("-->")) {
					isPure++;
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return fileNum;
	}

	private HashMap<String, Integer> readStatsFile(String file) {
		HashMap<String, Integer> fileNum = new HashMap<String, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 0) {
					String fileName = tokens[1].substring(9);
					int num = fileNum.containsKey(fileName) ? fileNum
							.get(fileName) : 0;
					fileNum.put(fileName, num + 1);
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return fileNum;
	}

	public void correlatePNBug(String file) {
		HashMap<String, Integer> purityNum = readPurityFile(file + ".purity");
		HashMap<String, Integer> identifierNum = readStatsFile(file
				+ ".identifierAnomal.stats");
		HashMap<String, Integer> methodNum = readStatsFile(file
				+ ".methodAnomal.stats");
		readLogFile(file + ".log.msg");
		try {
			PrintWriter writer = new PrintWriter(file+".correlateNew");
			for (Map.Entry<String, Integer> entry : purityNum.entrySet()) {
				String fileName = entry.getKey();
				int identifierBug = identifierNum.containsKey(fileName) ? identifierNum
						.get(fileName) : 0;
				int mtdBug = methodNum.containsKey(fileName) ? methodNum
						.get(fileName) : 0;
				int logBugFix = bugFixMap.containsKey(fileName) ? bugFixMap
						.get(fileName) : 0;
				int changeNum = changesMap.containsKey(fileName) ? changesMap
						.get(fileName) : 0;
				writer.println(fileName + "," + entry.getValue() + ","
						+ identifierBug + "," + mtdBug + "," + logBugFix + ","
						+ changeNum);
			}
			writer.close();
			System.out.println("finish correlation "+file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String getFile(String absolute) {
		int pos = absolute.indexOf("/", 0);
		for (int i = 7; i >= 0; i--)
			pos = absolute.indexOf("/", pos + 1);
		return absolute.substring(pos + 1);
	}

}
