package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.xml.serialize.Printer;

/**
 * @Author Lisa
 * @Date: Jan 29, 2015
 */
public class TopLevelDirDetector {
	HashMap<String, Integer> dirPurity = new HashMap<String, Integer>();
	HashMap<String, Integer> dirIAnomal = new HashMap<String, Integer>();
	HashMap<String, Integer> dirMAnomal = new HashMap<String, Integer>();
	HashMap<String, Integer> dirBugFix = new HashMap<String, Integer>();
	HashMap<String, Integer> dirChange = new HashMap<String, Integer>();
	PrintWriter writer;

	public void readFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file
					+ ".correlate"));
			writer = new PrintWriter(file + ".correlateTop");
			String line = "";
			dirPurity = new HashMap<String, Integer>();
			dirIAnomal = new HashMap<String, Integer>();
			dirMAnomal = new HashMap<String, Integer>();
			dirBugFix = new HashMap<String, Integer>();
			dirChange = new HashMap<String, Integer>();

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length < 4)
					continue;
				String dir = getDir(tokens[0]).trim();
				setPurity(tokens[1], dir);
				setIAnomal(tokens[2], dir);
				setMAnomal(tokens[3], dir);
				setBugFix(tokens[4], dir);
				setChange(tokens[5], dir);
			}
			reader.close();
			printAll();
			System.out.println(file+","+dirBugFix.size());
			writer.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private String getDir(String path) {

		String[] tokens = path.split("/");
		if (tokens.length>=3)
			return tokens[0]+"/"+tokens[1]+"/"+tokens[2];
		else 
			return path;
	}

	private void setPurity(String value, String dir) {
		int pCount = dirPurity.containsKey(dir) ? dirPurity.get(dir) : 0;
		dirPurity.put(dir, pCount + Integer.parseInt(value));
	}

	private void setIAnomal(String value, String dir) {
		int iCount = dirIAnomal.containsKey(dir) ? dirIAnomal.get(dir) : 0;
		dirIAnomal.put(dir, iCount + Integer.parseInt(value));
	}

	private void setMAnomal(String value, String dir) {
		int iCount = dirMAnomal.containsKey(dir) ? dirMAnomal.get(dir) : 0;
		dirMAnomal.put(dir, iCount + Integer.parseInt(value));
	}

	private void setBugFix(String value, String dir) {
		int iCount = dirBugFix.containsKey(dir) ? dirBugFix.get(dir) : 0;
		dirBugFix.put(dir, iCount + Integer.parseInt(value));
	}

	private void setChange(String value, String dir) {
		int iCount = dirChange.containsKey(dir) ? dirChange.get(dir) : 0;
		dirChange.put(dir, iCount + Integer.parseInt(value));
	}

	private void printAll() {
		for (Map.Entry<String, Integer> entry : dirPurity.entrySet()) {
			String dir = entry.getKey();
			writer.println(dir + "," + entry.getValue() + ","
					+ dirIAnomal.get(dir) + "," + dirMAnomal.get(dir) + ","
					+ dirBugFix.get(dir) + "," + dirChange.get(dir));
		}
		
		writer.flush();
	}
}
