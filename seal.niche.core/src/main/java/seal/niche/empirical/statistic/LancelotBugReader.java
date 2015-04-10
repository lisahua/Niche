package seal.niche.empirical.statistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Author Lisa
 * @Date: Oct 14, 2014
 */
public class LancelotBugReader {

	public static void compareVersions(String file1, String file2) {
		HashMap<String, Integer> bugMap1 = new HashMap<String, Integer>();
		HashMap<String, Integer> bugMap2 = new HashMap<String, Integer>();
		String outputFile = "/Users/admin/Documents/nameExample/versionAnalysis.txt";

		BufferedReader reader;
		PrintWriter writer;
		try {
			reader = new BufferedReader(new FileReader(file1));
			writer = new PrintWriter(outputFile);
			String line = "";
			int id = 0;
			for (; (line = reader.readLine()) != null; id++) {
				String[] tokens = line.split(",");
				String name = tokens[1] + "/" + tokens[2] + "%" + tokens[4];
				bugMap1.put(name, id);
			}
			System.out.println(id);
			reader.close();
			id = 0;
			reader = new BufferedReader(new FileReader(file2));
			for (; (line = reader.readLine()) != null; id++) {
				String[] tokens = line.split(",");
				String name = tokens[1] + "/" + tokens[2] + "%" + tokens[4];
				if (bugMap1.containsKey(name)) {
					writer.println(name + "," + bugMap1.get(name));
					bugMap1.remove(name);
				} else {
					bugMap2.put(name, id);
				}
			}
			System.out.println(id);
			reader.close();
			// print file A only
			writer.println("File A only: ");
			for (Map.Entry<String, Integer> entry : bugMap1.entrySet()) {
				writer.println(entry.getKey() + "," + entry.getValue());
			}
			// print file B only
			writer.println("File B only: ");
			for (Map.Entry<String, Integer> entry : bugMap2.entrySet()) {
				writer.println(entry.getKey() + "," + entry.getValue());
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void compareDelta(String file1, String file2) {
		BufferedReader reader;
		HashSet<String> bugSet = new HashSet<String>();
		try {
			reader = new BufferedReader(new FileReader(file1));
			String line = "";
			int tag = 0;
			while ((line = reader.readLine()) != null) {
				if (line.contains("File A")) {
					tag = 1;
					continue;
				} else if (line.contains("File B")) {
					tag = 2;
					break;
				}
				if (tag != 1)
					continue;
				bugSet.add(line.split(",")[0]);
			}
			System.out.println(bugSet.size());
			reader.close();
			reader = new BufferedReader(new FileReader(file2));
			tag = 0;
			int count=0;
			while ((line = reader.readLine()) != null) {
				if (line.contains("File A")) {
					tag = 1;
					continue;
				} else if (line.contains("File B")) {
					tag = 2;
					break;
				}
				if (tag != 1)
					continue;
				if (bugSet.contains(line.split(",")[0])) {
					count++;
					System.out.println(line);
				}
			}
			System.out.println(count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String pkg = "/Users/admin/Documents/nameExample/versionAnalysis-elasticsearch-";
		compareDelta(pkg + "0.19-1.0.txt", pkg + "0.90-1.0.txt");
	}

}
