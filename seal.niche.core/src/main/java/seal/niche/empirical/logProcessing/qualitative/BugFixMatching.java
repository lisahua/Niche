package seal.niche.empirical.logProcessing.qualitative;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class BugFixMatching {
	static String[] projects = new String[] { "accumulo", "airavata",
			"archiva", "cayenne", "collection", "io", "lang", "vfs",
			"continuum", "curator", "cxf", "deltaspike", "giraph", "gora",
			"hadoop", "hama", "hbase", "jackrabbit", "james", "jclouds",
			"mahout", "manifoldcf", "marmotta", "maven", "mina", "face", "ode",
			"openjpa", "opennlp", "phoenix", "pig", "pivot", "roller",
			"stratos", "struts", "syncope", "tez", "wicket", "zookeeper" };
	static HashMap<String, HashSet<String>> methodNames = new HashMap<String, HashSet<String>>();
	static int count = 0;
	static HashMap<String, String> fileNames = new HashMap<String, String>();

	public static void matchBFWithMethod() throws Exception {
		readMethods();
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allLogBF.txt"));
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allMethodINBF.txt");
		String line = "";
		int proj = 0;
		HashMap<String, HashSet<String>> fileCmts = new HashMap<String, HashSet<String>>();
		while ((line = reader.readLine()) != null) {
			if (line.contains(projects[proj])) {
				String[] token = line.split(",");
				String file = token[1].substring(token[1].lastIndexOf("/") + 1);
				HashSet<String> commits = fileCmts.containsKey(file) ? fileCmts
						.get(file) : new HashSet<String>();
				commits.add(token[0] + "," + token[2] + "," + token[3] + ","
						+ token[4]);
				fileCmts.put(file, commits);
			} else if (proj < 38 && line.contains(projects[proj + 1])) {
				// mergeFiles(projects[proj], fileCmts, writer);
				mergeFilesBF(projects[proj], fileCmts, writer);
				proj++;
				String[] token = line.split(",");
				String file = token[1].substring(token[1].lastIndexOf("/") + 1);
				HashSet<String> commits = fileCmts.containsKey(file) ? fileCmts
						.get(file) : new HashSet<String>();
				commits.add(token[0] + "," + token[2] + "," + token[3] + ","
						+ token[4]);
				fileCmts.put(file, commits);
			} else {
				String[] token = line.split(",");
				String file = token[1].substring(token[1].lastIndexOf("/") + 1);
				HashSet<String> commits = fileCmts.containsKey(file) ? fileCmts
						.get(file) : new HashSet<String>();
				commits.add(token[0] + "," + token[2] + "," + token[3] + ","
						+ token[4]);
				fileCmts.put(file, commits);
			}
		}
		reader.close();
		System.out.println(count);
		for (HashSet<String> set : methodNames.values())
			for (String l : set)
				writer.println(l);
		writer.close();

	}

	private static void mergeFilesBF(String proj,
			HashMap<String, HashSet<String>> fileCmts, PrintWriter writer) {
		for (String file : fileCmts.keySet()) {
			HashSet<String> set = fileCmts.get(file);
			int bf = 0, change = 0, churns = 0;
			String commits = "";
			String bugFix = "";
			String bfWindow = "";
			for (String cmt : set) {
				String[] token = cmt.split(",");
				if (token[1].equals("1")) {
					bf++;
					bugFix += token[0] + "-";
				} else
					change++;
				bfWindow += token[1];
				if (token.length >= 3)
					churns += Integer.parseInt(token[2])
							+ Integer.parseInt(token[3]);
				commits += token[0] + "-";
			}
			set.clear();
			// FIXME file-level churn and bf,change
			// String key = proj + "," + file;
			String key = file;
			if (methodNames.containsKey(key)) {
				// boolean flag = false;
				HashSet<String> in_line = new HashSet<String>();
				for (String line : methodNames.get(key)) {
					if (line.contains("1,0,1,,") || line.contains("1,1,0,,")
							|| line.contains("1,1,1,,")
							|| line.contains("1,0,0,,")
							|| line.contains("0,1,0,,")
							|| line.contains("0,1,1,,")
							|| line.contains("0,0,1,,")) {
						in_line.add(line);
						writer.println(line + "," + bf + "," + change + ","
								+ churns + "," + bugFix + "," + commits + ","
								+ bfWindow);
					}
				}

				// all in_0
				if (in_line.size() == 0) {
					int bf_count = bf, cg_count = change;
					int size = methodNames.get(key).size();
					int[] bf_per = new int[size];
					int[] cg_per = new int[size];
					while (bf_count > 0 || cg_count > 0) {
						int id_bf = 0, id_cg = 0;
						for (String s : methodNames.get(key)) {
							if (bf_count > 0) {
								bf_per[id_bf++]+=2;
								bf_count-=2;
							}
							else if (cg_count > 0) {
								cg_per[id_cg++]+=2;
								cg_count-=2;
							}
						}
					}
					bf_count = 0;
					cg_count = 0;
					int id_bf = 0, id_cg = 0;
					for (String s : methodNames.get(key)) {
						if (bf_per[id_bf] == 0 && cg_per[id_cg] == 0) {
							writer.println(s + "," + bf_per[id_bf] + ","
									+ cg_per[id_cg]);
							id_bf++;
							id_cg++;
						} else {
							writer.println(s + "," + bf_per[id_bf++] + ","
									+ cg_per[id_cg++] + "," + churns + ","
									+ bugFix + "," + commits + "," + bfWindow);
						}
					}
				} else {
					for (String s : methodNames.get(key)) {
						if (!in_line.contains(s)) {
							writer.println(s);
						}
					}
				}
				methodNames.remove(key);
			} else {
				// System.out.println(key);
				count++;
			}
		}
		fileCmts.clear();
	}

	private static void readMethods() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodIN.txt"));
		String line = "";
		int proj = 0;
		while ((line = reader.readLine()) != null) {

			if (line.contains(projects[proj])) {
				String[] token = line.split(",");
				String file = token[1].substring(token[1].lastIndexOf("/") + 1);
				// String key = proj + "," + file;
				String key = file;
				HashSet<String> set = methodNames.containsKey(key) ? methodNames
						.get(key) : new HashSet<String>();
				set.add(line);
				methodNames.put(key, set);
			} else if (proj < 38 && line.contains(projects[proj + 1])) {
				proj++;
				String[] token = line.split(",");
				String file = token[1].substring(token[1].lastIndexOf("/") + 1);
				// String key = proj + "," + file;
				String key = file;
				HashSet<String> set = methodNames.containsKey(key) ? methodNames
						.get(key) : new HashSet<String>();
				set.add(line);
				methodNames.put(key, set);
			} else {
				String[] token = line.split(",");
				String file = token[1].substring(token[1].lastIndexOf("/") + 1);
				// String key = proj + "," + file;
				String key = file;
				HashSet<String> set = methodNames.containsKey(key) ? methodNames
						.get(key) : new HashSet<String>();
				set.add(line);
				methodNames.put(key, set);
			}
		}
		reader.close();
	}

	public static void countINBF() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodINBF.txt"));
		String line = "";
		int in_2 = 0, in_1 = 0, in_0 = 0;
		int fix_0 = 0, fix_1 = 0, fix_2 = 0;
		int cg_0 = 0, cg_1 = 0, cg_2 = 0;
		double fixRate_0 = 0, fixRate_1 = 0, fixRate_2 = 0;
		int bf_in = 0, bf_cn = 0, nbf_in = 0, fix = 0;
		while ((line = reader.readLine()) != null) {
			String[] token = line.split(",,");
			int bf = 0, cg = 0;
			double rate = 0;
			if (token.length > 1) {
				token = token[1].split(",");
				bf = Integer.parseInt(token[0]);
				if (bf > 0)
					fix++;
				cg = Integer.parseInt(token[1]);
			}
			if (bf + cg > 0)
				rate = bf * 1.0 / (bf + cg);
			if (line.contains("1,0,1,,") || line.contains("1,1,0,,")
					|| line.contains("1,1,1,,")) {
					in_2++;
				fix_2 += bf;
				cg_2 += cg;
				fixRate_2 += rate;
				if (bf > 0)
					bf_in++;
				else
					nbf_in++;
			}
			if (line.contains("1,0,0,,") || line.contains("0,1,0,,")
					|| line.contains("0,1,1,,") || line.contains("0,0,1,,")) {
					in_1++;
				fix_1 += bf;
				cg_1 += cg;
				fixRate_1 += rate;
				if (bf > 0)
					bf_in++;
				else 
					nbf_in++;
			} else {
				if (line.contains(",,")) {
					in_0++;
					fix_0 += bf;
					cg_0 += cg;
					fixRate_0 += rate;
					if (bf > 0)
						bf_cn++;
				}
			}
		}
		int total = 268468;
		in_0 = total - in_1 - in_2;
		System.out.println(in_0 + "," + fixRate_0 / in_0 + "," + fix_0 * 1.0
				/ in_0 + "," + cg_0 * 1.0 / in_0);
		System.out.println(in_1 + "," + fixRate_1 / in_1 + "," + fix_1 * 1.0
				/ in_1 + "," + cg_1 * 1.0 / in_1);
		System.out.println(in_2 + "," + fixRate_2 / in_2 + "," + fix_2 * 1.0
				/ in_2 + "," + cg_2 * 1.0 / in_2);

		System.out.println("conditional P(F|I),P(F|~I),F(I|F), F(I|~F):\n " + bf_in * 1.0 / (in_1 + in_2) + ","
				+ (bf_cn * 1.0 / total) / (1 - (in_1 + in_2) * 1.0 / total)
				+ "," + bf_in * 1.0 / fix + "," + (nbf_in * 1.0 / total)
				/ (1 - fix * 1.0 / total));
	}
}
