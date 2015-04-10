package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @Author Lisa
 * @Date: Feb 22, 2015
 */
public class PureMethodBugFix {

	public void readFile(String file) {
		readCorrelate(file + ".pureCorrelate2", file + ".correlate",
				readPureFile(file + ".pure2"));

	}

	private void readCorrelate(String writeFile, String file,
			final HashMap<String,String> pureFiles) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			PrintWriter writer = new PrintWriter(writeFile);
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (pureFiles.containsKey(tokens[0])) {
					String[]  names = pureFiles.get(tokens[0]).split(",");
					writer.println(line+","+names.length);
				}
				else {
					writer.println(line+",0");
				}
			}
			reader.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String,String> readPureFile(String file) {
		HashMap<String,String> excptFiles = new HashMap<String,String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			String prevFile = "";
			String hasExcpt ="";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					if (!prevFile.equals("")) {
						if (hasExcpt.length()>0) {
							String fileName = prevFile;
							for (int i = 0; i < 7; i++) {
								fileName = fileName.substring(fileName
										.indexOf("/")+1);
							}
							excptFiles.put(fileName,hasExcpt);
						}
					}
					prevFile = line;
					hasExcpt = "";
				} else if (line.contains("-->")) {
					hasExcpt += line.split("-->")[0]+",";
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excptFiles;
	}
}
