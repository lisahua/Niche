package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * @Author Lisa
 * @Date: Feb 1, 2015
 */
public class BayesianCorrelation {
	PrintWriter writer;

	public BayesianCorrelation(String output) {
		try {
			writer = new PrintWriter(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readFileCorrelation(String path, String name) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			int[] value = new int[11];
			int fileCount = 0;
			while ((line = reader.readLine()) != null) {
				fileCount ++;
				String[] tokens = line.split(",");
				for (int i = 1; i < tokens.length; i++) {
					value[i - 1] += Integer.parseInt(tokens[i]);
				}
			}
			reader.close();
			String printL = "";
			for (int v : value)
				printL += "," + v;
			writer.println(name + ","+fileCount+printL);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
