package seal.niche.evolveAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @Author Lisa
 * @Date: Feb 9, 2015
 */
public class WindowSizeChange {

	public void readFile(String dir, int window) {
		String[] files = { "fieldD.txt", "fieldI.txt", "mtdD.txt", "mtdI.txt",
				"paraD.txt", "paraI.txt", "varD.txt", "varI.txt" };
		System.out.print("\n" + window + ",");
		try {
			int count = 0;
			int bfCount = 0;
			for (String s : files) {
				BufferedReader reader = new BufferedReader(new FileReader(dir
						+ s));
				String line = "";
				while ((line = reader.readLine()) != null) {
					count++;
					String[] tokens = line.split(",");
					int delDec = 0, total = 0;
					try {
						delDec = Integer.parseInt(tokens[2]);
						total = Integer.parseInt(tokens[3]);
					} catch (Exception e) {
					}
					if (window < 0) {
						total = (delDec - window) > total ? total
								: (delDec - window);
						bfCount += getBF(tokens[4].substring(delDec, total));
					} else if (window>0){
						total = (delDec - window) > 0 ? delDec - window : 0;
						bfCount += getBF(tokens[4].substring(total, delDec));
					} else {
						if (tokens[4].charAt(delDec)=='1')
							bfCount ++;
					}
				}
				reader.close();
				System.out.print(bfCount * 1.0
						/ (count * (Math.abs(window) + 1))+",");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getBF(String file) {
		int count = 0;
		for (char c : file.toCharArray()) {
			if (c == '1')
				count++;
		}
		return count;
	}
}
