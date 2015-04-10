package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;

/**
 * @Author Lisa
 * @Date: Jan 22, 2015
 */
public class ProjectAccumulator {
	private PrintWriter writer;

	public ProjectAccumulator(String writerPath) {
		try {
			writer = new PrintWriter(writerPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	public void readFile(String filePath) {
		try {
			String bugChange = getBugFixChange(filePath);
			String correlate = readCorrelate(filePath);
			writer.println(filePath + "," + bugChange + "," + correlate);
			System.out.println(filePath + "," + bugChange + "," + correlate);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private String getBugFixChange(String filePath) throws Exception {
		BufferedReader logReader = new BufferedReader(new FileReader(filePath
				+ ".log.msg"));
		HashSet<String> bugs = new HashSet<String>();
		HashSet<String> nonbugs = new HashSet<String>();
		int addLines = 0, deleteLines = 0;
		String line = "";
		while ((line = logReader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length > 3) {
				String commit = tokens[0];
				int isBuggy = Integer.parseInt(tokens[2]);
				addLines += Integer.parseInt(tokens[3]);
				deleteLines += Integer.parseInt(tokens[4]);
				if (isBuggy == 1)
					bugs.add(commit);
				else
					nonbugs.add(commit);

			}
		}
		logReader.close();
		return bugs.size() + "," + nonbugs.size()+","+addLines+","+deleteLines;
	}

	private String readCorrelate(String filePath) throws Exception {
		BufferedReader logReader = new BufferedReader(new FileReader(filePath
				+ ".correlate"));
		int purity = 0;
		int methodAb = 0;
		int nameAb = 0;
		String line = "";
		while ((line = logReader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length > 3) {
				purity += Integer.parseInt(tokens[1]);
				nameAb += Integer.parseInt(tokens[2]);
				methodAb += Integer.parseInt(tokens[3]);

			}
		}
		logReader.close();
		return purity + "," + nameAb + "," + methodAb;

	}
	protected void finalize() {
		writer.close();
	}
}
