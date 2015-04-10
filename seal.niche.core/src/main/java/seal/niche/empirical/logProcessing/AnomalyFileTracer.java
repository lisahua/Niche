package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

import seal.niche.syntacticAnalyser.parser.CustomizedFileReader;

/**
 * @Author Lisa
 * @Date: Jan 21, 2015
 */
public class AnomalyFileTracer {
	private String mtdAnomaly, identifierAnomaly;

	public AnomalyFileTracer(String methodAnomaly, String identifierAnomaly) {
		mtdAnomaly = methodAnomaly;
		this.identifierAnomaly = identifierAnomaly;
	}

	/**
	 * read upi file
	 */
	public void readFile(String file) {
		constructFiles(file);
	}

	private void constructFiles(String file) {
		HashMap<String, String> mtdFile = new HashMap<String, String>();
		HashMap<String, String> identifierFile = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 4) {
					String relativeF = getFile(tokens[4]);
					String mtd = tokens[0] + "--" + tokens[2];
					mtdFile.put(mtd, relativeF);
					String name = tokens[0] + "--" + tokens[1];
					identifierFile.put(name, relativeF);
				}
			}
			reader.close();
			parseNameAnomaly(mtdAnomaly, mtdFile);
			parseNameAnomaly(identifierAnomaly, identifierFile);
			System.out.println("finish parsing " + file);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private void parseNameAnomaly(String anomalyFile,
			HashMap<String, String> dict) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					anomalyFile));
			PrintWriter writer = new PrintWriter(anomalyFile + ".stats");
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("--");
				if (tokens.length > 1) {
					String name = tokens[1].split(",")[0] + "--" + tokens[0];
					writer.println(name + "," + dict.get(name));
					System.out.println(dict.get(name));
				}
			}
			reader.close();
			writer.close();
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

}
