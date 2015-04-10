package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @Author Lisa
 * @Date: Jan 25, 2015
 */
public class NameMannWhitneyCounter {

	private ArrayList<Integer> bless10 = new ArrayList<Integer>();
	private ArrayList<Integer> b10to20 = new ArrayList<Integer>();
	private ArrayList<Integer> b20to30 = new ArrayList<Integer>();
	private ArrayList<Integer> b30to40 = new ArrayList<Integer>();
	private ArrayList<Integer> b40to50 = new ArrayList<Integer>();
	private ArrayList<Integer> bmore50 = new ArrayList<Integer>();

	private ArrayList<Integer> cless10 = new ArrayList<Integer>();
	private ArrayList<Integer> c10to20 = new ArrayList<Integer>();
	private ArrayList<Integer> c20to30 = new ArrayList<Integer>();
	private ArrayList<Integer> c30to40 = new ArrayList<Integer>();
	private ArrayList<Integer> c40to50 = new ArrayList<Integer>();
	private ArrayList<Integer> cmore50 = new ArrayList<Integer>();
private String outputPath = "";
	public NameMannWhitneyCounter(String outputPath) {
		this.outputPath = outputPath;
	}
	public void readFile(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path+".correlate1"));
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (tokens.length < 4)
					continue;
				int iName = Integer.parseInt(tokens[1]);
				if (iName <= 10) {
					bless10.add(Integer.parseInt(tokens[2]));
					cless10.add(Integer.parseInt(tokens[3]));
				} else if (iName <= 20) {
					b10to20.add(Integer.parseInt(tokens[2]));
					c10to20.add(Integer.parseInt(tokens[3]));
				} else if (iName <= 30) {
					b20to30.add(Integer.parseInt(tokens[2]));
					c20to30.add(Integer.parseInt(tokens[3]));
				} else if (iName <= 40) {
					b30to40.add(Integer.parseInt(tokens[2]));
					c30to40.add(Integer.parseInt(tokens[3]));
				} else if (iName <= 50) {
					b40to50.add(Integer.parseInt(tokens[2]));
					c40to50.add(Integer.parseInt(tokens[3]));
				} else {
					bmore50.add(Integer.parseInt(tokens[2]));
					cmore50.add(Integer.parseInt(tokens[3]));
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void printDate() {
		try {
			PrintWriter writer = new PrintWriter(outputPath+"all.bless10.txt");
			for (int bf:bless10) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.b10to20.txt");
			for (int bf:b10to20) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.b20to30.txt");
			for (int bf:b20to30) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.b30to40.txt");
			for (int bf:b30to40) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.b40to50.txt");
			for (int bf:b40to50) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.bmore50.txt");
			for (int bf:bmore50) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.cless10.txt");
			for (int bf:cless10) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.c10to20.txt");
			for (int bf:c10to20) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.c20to30.txt");
			for (int bf:c20to30) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.c30to40.txt");
			for (int bf:c30to40) {
				writer.println(bf);
			}
			writer.close();
			writer = new PrintWriter(outputPath+"all.c40to50.txt");
			for (int bf:c40to50) {
				writer.println(bf);
			}
			writer.close();
			
			writer = new PrintWriter(outputPath+"all.cmore50.txt");
			for (int bf:cmore50) {
				writer.println(bf);
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
