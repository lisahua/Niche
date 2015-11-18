package seal.niche.empirical.logProcessing.qualitative;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class QualitativeAnalysis {

	public static void prepareQualitative() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allMethodINBFInvoke.txt"));
		BufferedReader objreader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allIdentifierAnomal.txt"));
		PrintWriter objwriter = new PrintWriter(ConfigUtility.outputPath
				+ "qualitativeAnalysis_obj.txt");
		PrintWriter purewriter = new PrintWriter(ConfigUtility.outputPath
				+ "qualitativeAnalysis_pure.txt");
		PrintWriter impurewriter = new PrintWriter(ConfigUtility.outputPath
				+ "qualitativeAnalysis_impure.txt");
		String line = "";
		HashMap<String, String> fileName = new HashMap<String, String>();
		while ((line = objreader.readLine()) != null) {
			String[] token = line.split(",");
			fileName.put(token[0], token[1]);
		}

		while ((line = reader.readLine()) != null) {
			String[] token = line.split(",");
			if (token[3].equals("1")
					&& (!token[7].equals("0") && !token[7].equals("0"))) {
				objwriter.println(fileName.get(token[1]) + "," + line);
				System.out.println(fileName.get(token[1]));
			}
			if (token[4].equals("1")
					&& (!token[7].equals("0") && !token[7].equals("0"))) {
				purewriter.println(line);
			}
			if (token[5].equals("1")
					&& (!token[7].equals("0") && !token[7].equals("0"))) {
				if (line.contains("hadoop"))
					impurewriter.println(line);
			}
		}
		objwriter.close();
		purewriter.close();
		impurewriter.close();
		reader.close();

	}

}
