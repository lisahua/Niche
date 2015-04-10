package seal.niche.evolveAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @Author Lisa
 * @Date: Feb 8, 2015
 */
public class DeclarationDeleteCommit {
	HashMap<String, HashSet<String>> fileNum = new HashMap<String, HashSet<String>>();
	HashMap<String, HashSet<String>> fileDeclare = new HashMap<String, HashSet<String>>();
	HashMap<String, HashSet<String>> fileInvoke = new HashMap<String, HashSet<String>>();
	PrintWriter decWriter = null, invokeWriter = null;

	public void readFile(String file, int windowSize) {
		
		HashMap<String, HashSet<String>>  impure = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> up = new HashMap<String,HashSet<String>>();
		try {
			decWriter = new PrintWriter(file + ".declareAddCommit");
			invokeWriter = new PrintWriter(file + ".invokeAddCommit");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HashMap<String, HashSet<String>> pure = readPurityFile(file + ".purity");
		up.putAll(readStatsFile(file + ".methodAnomal.stats"));
		up.putAll(readStatsFile(file + ".identifierAnomal.stats"));
		readNameFile(file + ".allNameExpPrimitive");
		readLogFile(file + ".log", windowSize);
	}

	private HashMap<String, HashSet<String>> readPurityFile(String file) {
		HashMap<String, HashSet<String>> files = new HashMap<String, HashSet<String>>();
		String prevFile = "";
		HashSet<String> isPure = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					if (prevFile.length() > 1 && isPure.size() > 0) {
						files.put(prevFile, isPure);
						isPure = new HashSet<String>();
					}
					prevFile = getFile(line);
				} else if (line.contains("-->")) {
					isPure.add(line.split("-->")[0].trim());
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}
	
	private HashMap<String, HashSet<String>> readStatsFile(String file) {
		HashMap<String, HashSet<String>> up = new HashMap<String, HashSet<String>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			HashSet<String> files;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 0) {
					String fileName = getFile(tokens[1]);
					files = fileNum.containsKey(fileName) ? fileNum
							.get(fileName) : new HashSet<String>();
					files.add(tokens[0].split("--")[1].trim());
					up.put(fileName, files);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return up;
	}

	private void readNameFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			String curFile = "";
			HashSet<String> names = null;
			HashSet<String> declare = new HashSet<String>();
			HashSet<String> invoke = new HashSet<String>();
			String curMtd = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					if (declare.size() > 0) {
						fileDeclare.put(curFile, declare);
						fileInvoke.put(curFile, invoke);
					}
					curFile = getFile(line);
					declare = new HashSet<String>();
					invoke = new HashSet<String>();
					names = fileNum.containsKey(curFile) ? fileNum.get(curFile)
							: null;
				} else {
					if (names == null)
						continue;
					for (String name : names) {
						if (line.contains(name)) {
							char type = line.charAt(0);
							String declS = line.substring(2);
							if (declS.contains(",")) {
								declS = declS.substring(declS.indexOf(",") + 1)
										.trim();
							}
							while (declS.startsWith(",")) {
								declS = declS.substring(1).trim();
							}
							if (type == 'm')
								curMtd = declS;
							if (declS.length() > 5) {
								declare.add(type + "--" + declS);
								invoke.add(type + "--" + declS);
							}
							if (type == 'p') {
								declare.add(type + "--" + curMtd);
								invoke.add(type + "--" + curMtd);
							}
						}
					}
				}

			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readLogFile(String path, int windowSize) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			String curFile = "";
			String commitNo = "";
			HashSet<String> names = null;
			HashSet<String> declare = new HashSet<String>();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("commit")) {
					commitNo = line.substring(7, 17);
				} else if (line.startsWith("---")) {
					if (!line.contains("null") && line.length() > 6) {
						curFile = getFile(line.substring(6));
						names = fileNum.get(curFile);
						declare = fileDeclare.get(curFile);
					}
				} else if (line.startsWith("+++")) {
					if (!line.contains("null") && line.length() > 6) {
						curFile = getFile(line.substring(6));
						names = fileNum.get(curFile);
						declare = fileDeclare.get(curFile);
					}
//				} else if (line.startsWith("-")) {
				} else if (line.startsWith("+")) {
					if (declare != null && declare.size() > 0) {
						for (String dec : declare) {
							if (line.contains(dec.substring(3))) {
								decWriter.println(commitNo + "," + curFile
										+ "," + dec.substring(0, 3)
										+ getDecName(dec));
								break;
							}
						}
					}
					if (names != null && names.size() > 0) {
						for (String name : names) {
							if (line.contains(name)) {
								String invoke = "";
								if (fileInvoke.containsKey(curFile)) {
									for (String ivk : fileInvoke.get(curFile))
										if (ivk.contains(name))
											invoke = ivk;
								}
								invokeWriter.println(commitNo + "," + curFile
										+ "," + name + "," + invoke);
								break;
							}
						}
					}
				}
			}
			reader.close();
			invokeWriter.flush();
			decWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFile(String absolute) {
		// int pos = absolute.indexOf("/", 0);
		// for (int i = 7; i >= 0; i--)
		// pos = absolute.indexOf("/", pos + 1);
		// return absolute.substring(pos + 1);
		if (!absolute.contains("/"))
			return absolute;
		return absolute.substring(absolute.lastIndexOf("/") + 1).trim();
	}

	private String getDecName(String dec) {
		if (dec.contains("(")) {
			String[] names = dec.substring(0, dec.indexOf("(")).split(" ");
			return names[names.length - 1];
		} else if (dec.contains(";")) {
			String[] names = dec.substring(0, dec.indexOf(";")).split(" ");
			return names[names.length - 1];
		}
		return "";
	}
}
