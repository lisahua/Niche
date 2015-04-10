package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import seal.niche.empirical.logProcessing.ReverseLineInputStream;

/**
 * @Author Lisa
 * @Date: Feb 1, 2015
 */
public class LifeSpanINCN {
	PrintWriter writer;
	HashMap<String, HashSet<String>> iNames = new HashMap<String, HashSet<String>>();
	HashMap<String, HashSet<String>> cNames = new HashMap<String, HashSet<String>>();

	public LifeSpanINCN(String outputPath) {
		try {
			writer = new PrintWriter(outputPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readFile(String path) {
		readIN(path);
		readCN(path);
		readLogReverse(path);
	}

	private void readIN(String path) {
		readPurityFile(path + ".purity");
		readStatsFile(path + ".methodAnomal.stats");
		readStatsFile(path + ".identifierAnomal.stats");
	}

	private void readCN(String path) {
		readConsistentName(path + ".allNames");
	}

	private void readConsistentName(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String file = "";
			// int typeValue=0,methodValue=0,fieldValue=0,paraValue=0,varValue =
			// 0;
			HashSet<String> fileNames = new HashSet<String>();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/")) {
					if (file.length() > 1) {
						if (iNames.containsKey(file)) {
							fileNames.removeAll(iNames.get(file));
						}
						cNames.put(file, fileNames);
					}
					file = getFile(line);
					fileNames = new HashSet<String>();
				} else if (!line.startsWith("t,")) {
					fileNames.add(line.split(",")[1].trim());
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void readPurityFile(String file) {
		HashSet<String> pNames = new HashSet<String>();
		String prevFile = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					prevFile = getFile(line);
					if (prevFile.length() > 1) {
						putIntoFileNames(prevFile, pNames);
					}
				} else if (line.contains("-->")) {
					pNames.add(line.split("-->")[0].trim());
				}
			}
			putIntoFileNames(prevFile, pNames);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readStatsFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("--");
				if (tokens.length > 0) {
					String[] fileName = tokens[1].split(",");
					if (fileName.length > 0)
						putIntoFileNames(fileName[1], fileName[0]);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void putIntoFileNames(String file, HashSet<String> name) {
		HashSet<String> names = (iNames.containsKey(file)) ? iNames.get(file)
				: new HashSet<String>();
		names.addAll(name);
		iNames.put(file, names);
	}

	private void putIntoFileNames(String file, String name) {
		HashSet<String> names = (iNames.containsKey(file)) ? iNames.get(file)
				: new HashSet<String>();
		names.add(name);
		iNames.put(file, names);
	}

	private String getFile(String absolute) {
		int pos = absolute.indexOf("/", 0);
		for (int i = 5; i >= 0; i--)
			pos = absolute.indexOf("/", pos + 1);
		return absolute.substring(pos + 1);
	}

	private void readLogReverse(String path) {
		DateFormat format = new SimpleDateFormat(
				"EEE MMM dd kk:mm:ss yyyy ZZZZ", Locale.ENGLISH);
		Date initDate = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new ReverseLineInputStream(new File(path + ".log"))));
			// PrintWriter writer = new PrintWriter(path + ".lifespan");
			String line = "";
			String currentFile = "";
			long dayDiff = 0;
			HashSet<String> fileCache = new HashSet<String>();
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() < 3)
					continue;
				else if (line.startsWith("Date: ")) {
					if (initDate == null) {
						initDate = format.parse(line.substring(5).trim());
					}
					Date currentDate = format.parse(line.substring(5).trim());
					dayDiff = (currentDate.getTime() - initDate.getTime())
							/ (1000 * 60 * 60 * 24);
					printAllLifeSpan(fileCache, dayDiff);
				} else if (line.startsWith("---")) {
					if (!line.contains("/null")) {
						currentFile = line.substring(6);
						// fileCache.add(currentFile);
					}
				} else if (line.startsWith("+++")) {
					if (!line.contains("/null")) {
						currentFile = line.substring(6);
						// fileCache.add(currentFile);
					}
				} else if (line.startsWith("- ")) {
					fileCache.add(currentFile);
				}
			}
			writer.close();
			reader.close();
			System.out.println(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printAllLifeSpan(HashSet<String> files, long dayDiff) {
		for (String file : files) {
			if (iNames.containsKey(file)) {
				for (String iName : iNames.get(file)) {
					writer.println(file + "--" + iName + ",1," + dayDiff);
				}
				iNames.remove(file);
			}
			if (cNames.containsKey(file)) {
				for (String cName : cNames.get(file)) {
					writer.println(file + "--" + cName + ",1," + dayDiff);
				}
				cNames.remove(file);
			}
		}
		writer.flush();
	}

}
