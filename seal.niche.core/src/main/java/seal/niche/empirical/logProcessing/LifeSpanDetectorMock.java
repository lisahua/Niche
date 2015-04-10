package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Stack;

/**
 * @Author Lisa
 * @Date: Jan 22, 2015
 */
public class LifeSpanDetectorMock {
	HashMap<String, HashSet<String>> fileNames = new HashMap<String, HashSet<String>>();
	private Stack<String> fileCommit;
	private String path;

	public LifeSpanDetectorMock(String path, String projFolder) {
		this.path = path;
		readLogStats(path + ".log.msg");
		readPurityFile(path + ".purity");
		readStatsFile(path + ".methodAnomal.stats");
		readStatsFile(path + ".identifierAnomal.stats");
		// readFile(path + ".declare", projFolder.length() + 1);
	}

	public void detectPreservedFiles() {
		DateFormat format = new SimpleDateFormat(
				"EEE MMM dd kk:mm:ss yyyy ZZZZ", Locale.ENGLISH);
		Date initDate = null;
		// TODO
		// HashMap<String, String> oldFileTrace = new HashMap<String, String>();
		// HashSet<String> commitCacheAdd = new HashSet<String>();
		HashSet<String> commitCacheDelete = new HashSet<String>();
		HashSet<String> iNames = fileNames.get(fileCommit.peek());
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new ReverseLineInputStream(new File(path + ".log"))));
			PrintWriter writer = new PrintWriter(path + ".lifespan");
			String line = "";
			String currentFile = "";
			long dayDiff = 0;
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
				} else if (line.startsWith("commit ")) {
					// save state
					for (String deletedDecl : commitCacheDelete) {
						// if (!commitCacheAdd.contains(deletedDecl)) {
						writer.println(currentFile + "," + deletedDecl + ","
								+ dayDiff + "," + line.split("commit ")[1]);
						HashSet<String> iNameAfterDelete = fileNames
								.get(currentFile);
						iNameAfterDelete.remove(deletedDecl);
						fileNames.put(currentFile, iNameAfterDelete);
						// }
					}
					// clear cache
					// commitCacheAdd.clear();
					commitCacheDelete.clear();
				} else if (line.startsWith("---")) {
					currentFile = line.substring(6);
				} else if (line.startsWith("+++")) {
					currentFile = line.substring(6);
				} else if (line.startsWith("diff")) {
					if (!fileCommit.isEmpty())
						fileCommit.pop();
					if (!fileCommit.isEmpty())
						iNames = fileNames.get(fileCommit.peek());

				}
				// else if (line.startsWith("+ ")) {
				// if (iNames == null || iNames.size()==0)
				// continue;
				// String parseDecl = isDecleration(line, iNames);
				// if (parseDecl.length() > 1)
				// commitCacheAdd.add(parseDecl);
				// }

				else if (line.startsWith("- ")) {
					if (iNames == null || iNames.size() == 0)
						continue;
					String parseDecl = isDecleration(line, iNames);
					if (parseDecl.length() > 1)
						commitCacheDelete.add(parseDecl);
				}
			}
			writer.close();
			reader.close();
			System.out.println(path);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private String isDecleration(String line, HashSet<String> iNames) {
		line = line.substring(2).trim();
		for (String iName : iNames) {
			if (line.contains(iName)) {
				return iName;
			}
		}
		return "";
	}

	// private void readFile(String declareFile, int sIndex) {
	// fileNameMap = new HashMap<String, HashMap<String, String>>();
	// try {
	// BufferedReader reader = new BufferedReader(new FileReader(
	// declareFile));
	// String line = "";
	// String fileName = "";
	// HashMap<String, String> ref = null;
	// while ((line = reader.readLine()) != null) {
	// if (line.startsWith("/")) {
	// if (fileName.length() > 1)
	// fileNameMap.put(fileName, ref);
	// fileName = line.substring(sIndex);
	// fileNameMap.put(fileName, new HashMap<String, String>());
	// ref = new HashMap<String, String>();
	// } else {
	// String[] tokens = line.split(",");
	// if (tokens.length > 2)
	// ref.put(tokens[2].trim(), tokens[1].trim());
	// }
	// }
	// reader.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private void readLogStats(String logStats) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(logStats));
			String line = "";
			fileCommit = new Stack<String>();
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				fileCommit.push(tokens[1]);
			}
			reader.close();
		} catch (Exception e) {
			// e.printStackTrace();
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

	private String getFile(String absolute) {
		int pos = absolute.indexOf("/", 0);
		for (int i = 5; i >= 0; i--)
			pos = absolute.indexOf("/", pos + 1);
		return absolute.substring(pos + 1);
	}

	private void putIntoFileNames(String file, HashSet<String> name) {
		HashSet<String> names = (fileNames.containsKey(file)) ? fileNames
				.get(file) : new HashSet<String>();
		names.addAll(name);
		fileNames.put(file, names);
	}

	private void putIntoFileNames(String file, String name) {
		HashSet<String> names = (fileNames.containsKey(file)) ? fileNames
				.get(file) : new HashSet<String>();
		names.add(name);
		fileNames.put(file, names);
	}
}
