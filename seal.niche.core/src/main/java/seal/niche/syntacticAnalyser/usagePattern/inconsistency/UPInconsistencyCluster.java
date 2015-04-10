package seal.niche.syntacticAnalyser.usagePattern.inconsistency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import seal.niche.empirical.statistic.ConfigUtility;
import seal.niche.syntacticAnalyser.parser.CustomizedFileReader;

/**
 * @Author Lisa
 * @Date: Jan 20, 2015
 */
public class UPInconsistencyCluster extends CustomizedFileReader {
	// @SuppressWarnings("unchecked")
	// private HashSet<String>[] clusters = new
	// HashSet[ConfigUtility.UP_INCONSISTENT_CLUSTER];
	private HashMap<String, Integer> nameNumber = new HashMap<String, Integer>();
	private String processingAPI = "";

	public UPInconsistencyCluster(String outputPath) {
		try {
			writer = new PrintWriter(outputPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processLine(String namePattern) {
		initCluster(namePattern);

		HashSet<String> anomaly = clusterNumber();
		if (anomaly != null)
			for (String name : anomaly)
				writer.println(name + "--" + namePattern);
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
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private HashSet<String> getAnomalyCandidate(
	// HashMap<String, Integer> clusters) {
	//
	// return anomalyName;
	// }

	private void initCluster(String namePattern) {
		// for (Set<String> set : clusters) {
		// set = new HashSet<String>();
		// }
		nameNumber.clear();
		String[] tokens = namePattern.split(",");
		processingAPI = tokens[0];
		for (int i = 1; i < tokens.length; i++) {
			String[] strNum = tokens[i].split(":");
			if (strNum.length > 1) {
				String parseName = strNum[0];
				int value = Integer.parseInt(strNum[1]);
				if (!parseName.contains("("))
					nameNumber.put(parseName, value);
				else {
					int lastPara = parseName.lastIndexOf(")");
					while (lastPara == parseName.length() - 1) {
						parseName = parseName.substring(0,
								parseName.length() - 1);
						lastPara = parseName.lastIndexOf(")");
					}
					if (lastPara > 0 && lastPara < strNum[0].length() - 1)
						nameNumber.put(
								strNum[0].substring(lastPara).replace(")", ""),
								value);

				}
			}
		}

	}

	private HashSet<String> clusterNumber() {
		int cNum = 0;
		int nameSize = nameNumber.keySet().size();
		String[] names = nameNumber.keySet().toArray(new String[nameSize]);
		int[] clusterTable = new int[nameSize];
		HashMap<Integer, Integer> clusterNum = new HashMap<Integer, Integer>();

		for (int i = 0; i < nameSize - 1; i++) {
			for (int j = i + 1; j < nameSize; j++) {
				String isSimiliar = isSimiliar(names[i], names[j]);
				if (isSimiliar.length() > 1) {
					if (clusterTable[j] == 0) {
						if (clusterTable[i] == 0) {
							clusterTable[i] = ++cNum;
							clusterNum.put(cNum, nameNumber.get(names[i]));
						}
						clusterTable[j] = clusterTable[i];
						clusterNum.put(
								clusterTable[i],
								clusterNum.get(clusterTable[i])
										+ nameNumber.get(names[j]));
					} else {
						clusterTable[i] = clusterTable[j];
						clusterNum.put(
								clusterTable[j],
								clusterNum.get(clusterTable[j])
										+ nameNumber.get(names[i]));

					}
				}
			}
		}
		int sum = 0;
		HashSet<String> anomalyName = new HashSet<String>();
		int clusterSum = 0;
		for (int num : clusterNum.values())
			sum += num;
		clusterSum = sum;
		int countAnomalyCandidate = 0;
		for (int i = 0; i < nameSize; i++)
			if (clusterTable[i] == 0) {
				sum += nameNumber.get(names[i]);
				countAnomalyCandidate++;
			}
		if (countAnomalyCandidate > ConfigUtility.UP_INCONSISTENT_CLUSTER
				|| (clusterSum * 1.0) / sum < 1 - ConfigUtility.UP_CLUSTER_INCONSISTENT_THRESHOLD)
			return null;
		for (int i = 0; i < nameSize; i++) {
			if (clusterTable[i] != 0)
				continue;
			String name = names[i];
			if (similarToType(name) || name.length() < 3)
				continue;
			if ((nameNumber.get(name) * 1.0) / sum < ConfigUtility.UP_CLUSTER_INCONSISTENT_THRESHOLD) {
				anomalyName.add(name);
			}
		}

		return anomalyName;
	}

	private String isSimiliar(String str1, String str2) {
		String[] token1 = splitter.executeSingleName(str1);
		String[] token2 = splitter.executeSingleName(str2);
		for (int i = 0; i < token1.length; i++) {
			for (int j = 0; j < token2.length; j++) {
				if (tokenSimilarStemmer(token1[i], token2[j]))
					return token1[i].toLowerCase();
			}
		}
		return "";
	}

	private boolean similarToType(String name) {
		String isSimiar = isSimiliar(name, processingAPI);
		if (isSimiar.length() > 1)
			return true;
		return false;
	}

	private boolean tokenSimilarStemmer(String token1, String token2) {
		if (token1.length() < 2 || token2.length() < 2)
			return false;
		token1 = token1.toLowerCase();
		token2 = token2.toLowerCase();
		if (token1.equals(token2))
			return true;
		if (token1.contains(token2) || token2.contains(token1))
			return true;
		if ((token1.endsWith("ies") || token1.endsWith("ed"))
				&& token2.contains(token1.substring(0, token1.length() - 3)))
			return true;
		if ((token2.endsWith("ies") || token2.endsWith("ed"))
				&& token1.contains(token2.substring(0, token2.length() - 3)))
			return true;

		// char[] char1 = token1.toLowerCase().toCharArray();
		// char[] char2 = token2.toLowerCase().toCharArray();
		// int[][] num = new int[token1.length() + 1][token2.length() + 1];

		// Actual algorithm
		// for (int i = 1; i <= char1.length; i++)
		// for (int j = 1; j <= char2.length; j++)
		// if (char1[i - 1] == char2[j - 1])
		// num[i][j] = 1 + num[i - 1][j - 1];
		// else
		// num[i][j] = Math.max(num[i - 1][j], num[i][j - 1]);
		return false;
	}
}
