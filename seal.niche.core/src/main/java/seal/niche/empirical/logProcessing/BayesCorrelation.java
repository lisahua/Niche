package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;

/**
 * @Author Lisa
 * @Date: Jan 22, 2015
 */
public class BayesCorrelation {
	private PrintWriter writer;

	public BayesCorrelation() {
		// try {
		// writer = new PrintWriter(writerPath);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}

	public void readFile(String filePath) {
		try {
			// String correlate = readCorrelate(filePath);
			// writer.println(filePath + "," + bugChange + "," + correlate);
			System.out.println(filePath + "," + readCorrelate(filePath));
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private String readCorrelate(String filePath) throws Exception {
		BufferedReader logReader = new BufferedReader(new FileReader(filePath
				+ ".correlate"));
		int iNameAndBugFix = 0;
		int iNameAndChange = 0;
		int bugFixCName = 0;
		int changeCName = 0;
		int iName=0;
		int bugFix = 0;
		int change = 0;
		int goodName =0;
		String line = "";
		while ((line = logReader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length > 3) {
				int hasIName = Integer.parseInt(tokens[1])
						+ Integer.parseInt(tokens[2])
						+ Integer.parseInt(tokens[3]);
				int hasBugFix = Integer.parseInt(tokens[4]);
				int hasChange = Integer.parseInt(tokens[5]);
				if (hasIName > 0 && hasBugFix > 0) {
					iNameAndBugFix++;
					iName++;
					bugFix ++;
				} else if (hasIName > 0 && hasChange > 0) {
					iNameAndChange ++;
					iName ++;
					change ++;
				} else if (hasBugFix > 0 && hasIName == 0) {
					bugFixCName ++;
					bugFix ++;
					goodName ++;
				} else if (hasChange > 0 && hasIName == 0) {
					changeCName ++;
					goodName ++;
					change++;
				}
			}
		}
		logReader.close();
		return iName+","+goodName+","+bugFix+","+change+","+iNameAndBugFix + "," + iNameAndChange + "," + bugFixCName + ","
				+ changeCName;

	}

	protected void finalize() {
		writer.close();
	}

}
