package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @Author Lisa
 * @Date: Feb 23, 2015
 */
public class ComparePureBFRate {

	public void readFiles(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			int pureVerb = 0, pureBF=0,impureVerb=0,impureBF = 0;

			while ((line=reader.readLine())!=null) {
				String[] tokens = line.split(",");
				if (tokens.length==0) continue;
				String lastToken = tokens[tokens.length-1];
				
				if (lastToken.contains("x")) {
					impureVerb += Integer.parseInt(lastToken.substring(1));
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
