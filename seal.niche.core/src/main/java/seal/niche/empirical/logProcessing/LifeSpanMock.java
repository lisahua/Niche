package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LifeSpanMock {
	private HashMap<String, String> filesCommit = new HashMap<String, String>();
	private HashMap<String, Date> commitDate = new HashMap<String, Date>();
	private DateFormat format = new SimpleDateFormat(
			"EEE MMM dd kk:mm:ss yyyy ZZZZ", Locale.ENGLISH);
	private Date lastCommit = null;
	private ArrayList<Integer> iless25 = new ArrayList<Integer>();
	private ArrayList<Integer> i25to50 = new ArrayList<Integer>();
	private ArrayList<Integer> imore50 = new ArrayList<Integer>();
	private ArrayList<Integer> cless25 = new ArrayList<Integer>();
	private ArrayList<Integer> c25to50 = new ArrayList<Integer>();
	private ArrayList<Integer> cmore50 = new ArrayList<Integer>();
	private int timePeriod = 0;
	private HashMap<String, Integer> dirINameC = new HashMap<String, Integer>();

	public void readCorrelation(String filePath) {
		readLogStats(filePath + ".log.msg");
		readLog(filePath + ".log");
		readCorrelate1(filePath + ".correlate1");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".correlate"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length > 5) {
					int iNames = Integer.parseInt(tokens[1])
							+ Integer.parseInt(tokens[2])
							+ Integer.parseInt(tokens[3]);
					String file = tokens[0];
					if (iNames > 0) {
						addIName(file);
					} else {
						addCName(file);
					}
				}
			}
			filesCommit.clear();
			commitDate.clear();
			dirINameC.clear();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readLogStats(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				filesCommit.put(tokens[1], tokens[0]);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readLog(String filePath) {
		try {
			Date firstCommit = null;
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = "";
			String commitNo = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("commit ")) {
					commitNo = line.split("commit")[1].trim().substring(0, 10);
				} else if (line.startsWith("Date:")) {

					Date date = format.parse(line.substring(5).trim());
					if (firstCommit == null)
						firstCommit = date;
					commitDate.put(commitNo, date);
					lastCommit = date;
				}
			}
			reader.close();
			timePeriod = (int) ((firstCommit.getTime() - lastCommit.getTime()) / (1000 * 60 * 60 * 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addIName(String file) {
		int dayDiff = timePeriod;
		if (filesCommit.containsKey(file)) {
			String commit = filesCommit.get(file);
			if (commitDate.containsKey(commit)) {
				Date cD = commitDate.get(commit);
				dayDiff = (int) ((cD.getTime() - lastCommit.getTime())
						/ (1000 * 60 * 60 * 24));
				if (dayDiff < 0) {
					System.out.println();
				}
			}

		}

		String dir = getDir(file, 1);
		if (!dirINameC.containsKey(dir))
			return;
		int iName = dirINameC.get(dir);
		if (iName < 25) {
			iless25.add(dayDiff);
		} else if (iName < 50) {
			i25to50.add(dayDiff);
		} else {
			imore50.add(dayDiff);
		}
	}

	private void addCName(String file) {
		int dayDiff = timePeriod;
		if (filesCommit.containsKey(file)) {
			String commit = filesCommit.get(file);
			if (commitDate.containsKey(commit)) {
				dayDiff = (int) ((commitDate.get(commit).getTime() - lastCommit
						.getTime()) / (1000 * 60 * 60 * 24));

			}
		}
		String dir = getDir(file, 1);
		if (!dirINameC.containsKey(dir))
			return;
		int cName = dirINameC.get(dir);
		if (cName < 25) {
			cless25.add(dayDiff);
		} else if (cName < 50) {
			c25to50.add(dayDiff);
		} else {
			cmore50.add(dayDiff);
		}
	}

	private void readCorrelate1(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				dirINameC.put(tokens[0], Integer.parseInt(tokens[1]));
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printLifeSpan(String path) {
		try {
			PrintWriter writer = new PrintWriter(path + "allLF.iless25");
			for (Integer i : iless25)
				writer.println(i);
			writer.close();
			writer = new PrintWriter(path + "allLF.i25to50");
			for (Integer i : i25to50)
				writer.println(i);
			writer.close();
			writer = new PrintWriter(path + "allLF.imore50");
			for (Integer i : imore50)
				writer.println(i);
			writer.close();
			writer = new PrintWriter(path + "allLF.cless25");
			for (Integer i : cless25)
				writer.println(i);
			writer.close();
			writer = new PrintWriter(path + "allLF.c25to50");
			for (Integer i : c25to50)
				writer.println(i);
			writer.close();
			writer = new PrintWriter(path + "allLF.cmore50");
			for (Integer i : cmore50)
				writer.println(i);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getDir(String path, int level) {
		if (!path.contains("/"))
			return path;
		for (; level > 0; level--) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		return path;
	}
}
