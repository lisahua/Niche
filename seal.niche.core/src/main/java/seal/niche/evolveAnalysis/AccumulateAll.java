package seal.niche.evolveAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * @Author Lisa
 * @Date: Feb 9, 2015
 */
public class AccumulateAll {

	PrintWriter fieldDWriter = null, fieldIWriter = null, mtdDWriter = null,
			mtdIWriter = null, paraDWriter = null, paraIWriter = null,
			vDWriter = null, vIWriter = null;

	public AccumulateAll(String dir) {
		try {
			fieldDWriter = new PrintWriter(dir + "fieldD.txt");
			fieldIWriter = new PrintWriter(dir + "fieldI.txt");
			mtdDWriter = new PrintWriter(dir + "mtdD.txt");
			mtdIWriter = new PrintWriter(dir + "mtdI.txt");
			paraDWriter = new PrintWriter(dir + "paraD.txt");
			paraIWriter = new PrintWriter(dir + "paraI.txt");
			vDWriter = new PrintWriter(dir + "varD.txt");
			vIWriter = new PrintWriter(dir + "varI.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readInvokeFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("m-")) {
					mtdIWriter.println(line);
				} else if (line.contains("f-")) {
					fieldIWriter.println(line);
				} else if (line.contains("v-")) {
					vIWriter.println(line);
				} else if (line.contains("p-")) {
					paraIWriter.println(line);
				}
			}
			reader.close();
			paraIWriter.flush();
			mtdIWriter.flush();
			fieldIWriter.flush();
			vIWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readDeclareFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("m-")) {
					mtdDWriter.println(line);
				} else if (line.contains("f-")) {
					fieldDWriter.println(line);
				} else if (line.contains("v-")) {
					vDWriter.println(line);
				} else if (line.contains("p-")) {
					paraDWriter.println(line);
				}
			}
			reader.close();
			mtdDWriter.flush();
			vDWriter.flush();
			paraDWriter.flush();
			fieldDWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
