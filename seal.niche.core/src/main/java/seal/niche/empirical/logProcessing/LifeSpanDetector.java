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
public class LifeSpanDetector {
	private HashMap<String, HashMap<String, String>> fileNameMap;
	private Stack<String> fileCommit;
	private String path;

	public LifeSpanDetector(String path, String projFolder) {
		this.path = path;
		readLogStats(path + ".log.msg");
		readFile(path + ".declare", projFolder.length() + 1);
	}

	public void detectPreservedFiles() {
		DateFormat format = new SimpleDateFormat(
				"EEE MMM dd kk:mm:ss yyyy ZZZZ", Locale.ENGLISH);
		Date initDate = null;
		// TODO
		// HashMap<String, String> oldFileTrace = new HashMap<String, String>();
		// HashSet<String> commitCacheAdd = new HashSet<String>();
		HashSet<String> commitCacheDelete = new HashSet<String>();
		HashMap<String, String> declNameMap = fileNameMap
				.get(fileCommit.peek());
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new ReverseLineInputStream(new File(path + ".log"))));
			PrintWriter writer = new PrintWriter(path + ".lifespans");
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

						// }
					}
					// clear cache
					// commitCacheAdd.clear();
					commitCacheDelete.clear();
				} else if (line.startsWith("---")) {
					if (line.length() > 6)
						currentFile = line.substring(6);
				} else if (line.startsWith("+++")) {
					if (line.length() > 6)
						currentFile = line.substring(6);
				} else if (line.startsWith("diff")) {
					if (!fileCommit.isEmpty())
						fileCommit.pop();
					if (!fileCommit.isEmpty())
						declNameMap = fileNameMap.get(fileCommit.peek());

				}
				// else if (line.startsWith("+ ")) {
				// if (declNameMap == null)
				// continue;
				// String parseDecl = isDecleration(line, declNameMap);
				// if (parseDecl.length() > 1)
				// commitCacheAdd.add(parseDecl);
				// }
				else if (line.startsWith("- ")) {
					if (declNameMap == null)
						continue;
					String parseDecl = isDecleration(line, declNameMap);
					if (parseDecl.length() > 1) {
						commitCacheDelete.add(parseDecl);
					}
				}
			}
			writer.close();
			reader.close();
			System.out.println(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String isDecleration(String line,
			HashMap<String, String> declNameMap) {
		line = line.substring(2).trim();
		if (declNameMap.containsKey(line))
			return declNameMap.get(line);
		return "";
	}

	private void readFile(String declareFile, int sIndex) {
		fileNameMap = new HashMap<String, HashMap<String, String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					declareFile));
			String line = "";
			String fileName = "";
			HashMap<String, String> ref = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("/")) {
					if (fileName.length() > 1)
						fileNameMap.put(fileName, ref);
					fileName = line.substring(sIndex);
					fileNameMap.put(fileName, new HashMap<String, String>());
					ref = new HashMap<String, String>();
				} else {
					String[] tokens = line.split(",");
					if (tokens.length > 2)
						ref.put(tokens[2].trim(), tokens[1].trim());
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
}
