package seal.niche.empirical.logProcessing.qualitative;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class InvokeMatching {
	static HashMap<String, Integer> methodInvoke = new HashMap<String, Integer>();

	public static void combineInvokeCommit() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allINInvoke.txt"));
		String line = "";
		// int count = 0;
		while ((line = reader.readLine()) != null) {
			String[] token = line.split(",");
			String file = token[0].substring(token[0].lastIndexOf("/") + 1);
			// if (methodInvoke.containsKey(file)) {
			// }
			if (!token[1].equals(""))
				methodInvoke.put(file, Integer.parseInt(token[1]));

		}
		readAllMethods();
		reader.close();
		System.out.println(methodInvoke.size());
	}

	public static void combineStartEndCommit() {

	}

	private static void readAllMethods() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodINBF.txt"));
		PrintWriter writer = new PrintWriter(ConfigUtility.outputPath
				+ "allMethodINBFInvoke.txt");
		String line = "";
		HashMap<String, HashSet<String>> files = new HashMap<String, HashSet<String>>();
		while ((line = reader.readLine()) != null) {
			String[] token = line.split(",");
			String file = token[1].substring(token[1].lastIndexOf("/") + 1);
			for (int count = token.length; count < 11; count++) {
				line += ",";
			}
			HashSet<String> methods = files.containsKey(file) ? files.get(file)
					: new HashSet<String>();
			methods.add(line);
			files.put(file, methods);
		}
		for (String key : methodInvoke.keySet()) {
			if (files.containsKey(key)) {
				int ivkCount = methodInvoke.get(key);
				int methods = files.get(key).size();
				int[] m_c = new int[methods];
				while (ivkCount > 0) {
					for (int i = 0; i < methods && ivkCount > 0; i++, ivkCount--)
						m_c[i] += 1;
				}
				int i = 0;
				for (String l : files.get(key)) {
					writer.println(l + "," + m_c[i++]);
				}
				files.remove(key);
			}
		}
		int count = 0;
		for (HashSet<String> file : files.values()) {
			for (String mtd : file) {
				writer.println(mtd + "," + 0);
				count++;
			}
		}
		System.out.println(count);
		reader.close();
		writer.close();

	}

	public static void countInvokeRate() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodINBFInvoke.txt"));
		String line = "";
		int v_0 = 0, v_1 = 0, v_2 = 0;
		int bf_0 = 0, bf_1 = 0, bf_2 = 0, cg_0 = 0, cg_1 = 0, cg_2 = 0;
		double bfRate_0 = 0, bfRate_1 = 0, bfRate_2 = 0;
		while ((line = reader.readLine()) != null) {
			String[] token = line.substring(line.indexOf(",,") + 2).split(",");

			int bf = token[0].equals("") ? 0 : Integer.parseInt(token[0]);
			int cg = token[1].equals("") ? 0 : Integer.parseInt(token[1]);
			double bf_rate = bf + cg == 0 ? 0 : bf * 1.0 / (bf + cg);
			int invoke = Integer
					.parseInt(line.substring(line.lastIndexOf(",") + 1));
			switch (invoke) {
			case 0:
				v_0++;
				bf_0 += bf;
				cg_0 += cg;
				bfRate_0 += bf_rate;
				break;
			case 1:
				v_1++;
				bf_1 += bf;
				cg_1 += cg;
				bfRate_1 += bf_rate;
				break;
			default:
				v_2++;
				bf_2 += bf;
				cg_2 += cg;
				bfRate_2 += bf_rate;
				break;
			}
		}
		v_0 = 268468 - v_1 - v_2;
		System.out.println(v_0 + "," + bfRate_0 / v_0 + "," + bf_0 * 1.0 / v_0
				+ "," + cg_0 * 1.0 / v_0);
		System.out.println(v_1 + "," + bfRate_1 / v_1 + "," + bf_1 * 1.0 / v_1
				+ "," + cg_1 * 1.0 / v_1);
		System.out.println(v_2 + "," + bfRate_2 / v_2 + "," + bf_2 * 1.0 / v_2
				+ "," + cg_2 * 1.0 / v_2);

	}
}
