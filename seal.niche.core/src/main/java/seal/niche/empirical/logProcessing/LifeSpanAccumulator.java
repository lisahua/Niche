package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

/**
 * @Author Lisa
 * @Date: Jan 26, 2015
 */
public class LifeSpanAccumulator {
	// private String filePath;
	private DateFormat format = new SimpleDateFormat(
			"EEE MMM dd kk:mm:ss yyyy ZZZZ", Locale.ENGLISH);

	public void countAvgLifeSpan(String filePath) {
		HashSet<String> iNames = readPurityFile(filePath + ".pure");
		iNames.addAll(readStatsFile(filePath + ".methodAnomal.stats"));
		iNames.addAll(readStatsFile(filePath + ".identifierAnomal.stats"));
//		int longestSpan = countLongestSpan(filePath);
		int iNameDay = 0, iNameC = 0, cNameDay = 0, cNameC = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".lifespan"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 3) {
					String name = getFile(tokens[0]) + ","+tokens[1];
					if (iNames.contains(name)) {
						iNameDay += Integer.parseInt(tokens[2]);
						iNameC++;
					} else {
						cNameDay += Integer.parseInt(tokens[2]);
						cNameC++;
					}
				}
			}
			reader.close();
			System.out.println(filePath + "," +iNameDay
					+ "," + iNameC + "," + cNameDay + "," + cNameC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int countLongestSpan(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".log"));
			String line = "";
			Date initDate = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Date:")) {
					initDate = format.parse(line.split("Date:")[1].trim());
					break;
				}
			}
			reader.close();
			reader = new BufferedReader(new InputStreamReader(
					new ReverseLineInputStream(new File(filePath + ".log"))));
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Date:")) {
					Date date = format.parse(line.split("Date:")[1].trim());
					return (int) (( initDate.getTime()-date.getTime() ) / (1000 * 60 * 60 * 24));
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private HashSet<String> readPurityFile(String file) {
		HashSet<String> pNames = new HashSet<String>();
		String prevFile = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					prevFile = getFile(line);
				} else if (line.contains("-->")) {
					pNames.add(prevFile + "," + line.split("-->")[0]);
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return pNames;
	}

	private HashSet<String> readStatsFile(String file) {
		HashSet<String> pNames = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("--");
				if (tokens.length > 0) {
					String[] fileName = tokens[1].split(",");
					if (fileName.length > 0)
						pNames.add(getFile(fileName[1]) + "," + fileName[0]);
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return pNames;
	}

	private String getFile(String absolute) {
		if (!absolute.contains("/")) return absolute;
		int pos = absolute.lastIndexOf("/");
		return absolute.substring(pos + 1);
	}
}
