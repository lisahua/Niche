package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Jan 26, 2015
 */
public class VisibilityAccumulator {
	private HashMap<String, String> iFiles = new HashMap<String, String>();
	private HashMap<String, ArrayList<Integer>> iFileCount = new HashMap<String, ArrayList<Integer>>();
	private HashMap<String, Integer> invokeFiles = new HashMap<String, Integer>();
	private HashMap<String, Integer> fileBFCorrelate = new HashMap<String, Integer>();
	private HashMap<String, Integer> fileChangeCorrelate = new HashMap<String, Integer>();
	// PrintWriter writer;
	PrintWriter invokeFileWriter, cNameWriter;

//	public VisibilityAccumulator(String outputFolder) {
//		
//	}

	public void readFile(String filePath) {
		iFiles = readPurityFile(filePath + ".purity");
		iFiles.putAll(readStatsFile(filePath + ".methodAnomal.stats"));
		iFiles.putAll(readStatsFile(filePath + ".identifierAnomal.stats"));
		iFiles.remove("");
		readBugFix(filePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".up"));

			String line = "";
			String fileName = "";
			// HashSet<String> cacheFile = new HashSet<String>();
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					fileName = getFile(line);
				} else if (line.contains("-->")) {
					String[] tokens = line.split("-->");
					if (tokens.length > 1)
						getInvokedFile(tokens[1], fileName);
				} else if (line.trim().length() > 2) {
					getInvokedFile(line, fileName);
				}
			}

			reader.close();
			iFiles.clear();
			iFileCount.clear();
			// fileBFCorrelate.clear();
			printInvokeFiles(filePath);
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
			e.printStackTrace();
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
		name = name.contains("/") ? name.substring(name.lastIndexOf("/") + 1)
				: name;
		if (name.length() > 5)
			return name.substring(0, name.length() - 5);
		return name;
	}

	private void getInvokedFile(String up, String file) {
		// HashSet<String> invokeFiles = new HashSet<String>();
		// if (file == null)
		// return invokeFiles;
		for (String ivkFile : iFiles.keySet()) {
			if (up.contains(ivkFile)) {
				ArrayList<Integer> list = iFileCount.containsKey(ivkFile) ? iFileCount
						.get(ivkFile) : new ArrayList<Integer>();
				if (fileBFCorrelate.containsKey(file)) {
					int bgCount = fileBFCorrelate.get(file);
					list.add(bgCount);
				}
				iFileCount.put(ivkFile, list);
				// invokeFiles.add(file);
				int count = invokeFiles.containsKey(file) ? invokeFiles
						.get(file) : 0;
				invokeFiles.put(file, count + 1);
			}
		}

	}

	private void readBugFix(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".correlate"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				fileBFCorrelate.put(tokens[0], Integer.parseInt(tokens[4]));
				fileChangeCorrelate.put(tokens[0], Integer.parseInt(tokens[5]));
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readInvokeC(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".invoke"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printInvokeFiles(String outputFolder) {
		try {
			// writer = new PrintWriter(outputFolder + "allInvokeCName.stats");
			invokeFileWriter = new PrintWriter(outputFolder
					+ ".INinvoke");
			cNameWriter = new PrintWriter(outputFolder + ".CNInvoke");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// print invoke (sickAPI,bf, change)
		for (Map.Entry<String, Integer> entry : invokeFiles.entrySet()) {
			String file = entry.getKey();
			String line = file + "," + +entry.getValue() + ","
					+ fileBFCorrelate.get(file) + ","
					+ fileChangeCorrelate.get(file);
			invokeFileWriter.println(line);
			fileBFCorrelate.remove(file);
			fileChangeCorrelate.remove(file);
		}
		invokeFileWriter.flush();
		for (Map.Entry<String, Integer> entry : fileBFCorrelate.entrySet()) {
			String file = entry.getKey();
			cNameWriter.println(file + "," + entry.getValue() + ","
					+ fileChangeCorrelate.get(file));
		}
		cNameWriter.flush();
	}
}
