package seal.niche.empirical.logProcessing.qualitative;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class FileBugFixConditional {
	static HashMap<String, HashSet<String>> methodNames = new HashMap<String, HashSet<String>>();
	static int bf_in = 0, bf_cn = 0, nbf_in = 0, bugfix = 0, in = 0, total = 0;

	public static void matchBFWithMethod() throws Exception {
		// readMethods();
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodINBFInvoke.txt"));
		String line = "";
		String file = "";
		HashMap<String, Boolean> buggySet = new HashMap<String, Boolean>();
		HashMap<String, Boolean> inSet = new HashMap<String, Boolean>();
		while ((line = reader.readLine()) != null) {
			String[] token = line.split(",");
			file = token[1].substring(token[1].lastIndexOf("/") + 1);
			if (token[3].equals("1") || token[4].equals("1")
					|| token[5].equals("1"))
				inSet.put(file, true);
			else if (!inSet.containsKey(file))
				inSet.put(file, false);
			if (!token[7].equals("0") && !token[7].equals(""))
				buggySet.put(file, true);
			else if (!buggySet.containsKey(file))
				buggySet.put(file, false);
			// else if (token.length < 8)
			// System.out.println(line);
		}
		total = inSet.size();
		for (String key : inSet.keySet()) {
			if (buggySet.containsKey(key) && buggySet.get(key) == true)
				bugfix++;
			if (inSet.get(key) == true)
				in++;
			if (inSet.get(key) == true && buggySet.get(key) == true)
				bf_in++;
			else if (inSet.get(key) == true && buggySet.get(key) == false)
				nbf_in++;
			else if (inSet.get(key) == false && buggySet.get(key) == true)
				bf_cn++;
		}
		reader.close();
		System.out.println("conditional P(F|I),P(F|~I),F(I|F), F(I|~F):\n "
				+ bf_in * 1.0 / in + "," + (bf_cn * 1.0 / total)
				/ (1 - in * 1.0 / total) + "," + bf_in * 1.0 / bugfix + ","
				+ (nbf_in * 1.0 / total) / (1 - bugfix * 1.0 / total));
	}

}
