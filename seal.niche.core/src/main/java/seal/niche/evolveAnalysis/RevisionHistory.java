package seal.niche.evolveAnalysis;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @Author Lisa
 * @Date: Mar 3, 2015
 */
public class RevisionHistory {
	private ArrayList<Revision> revisions = new ArrayList<Revision>();
	private int revSize = 0;

	public void readLogFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			String commit = "";
			Revision revision = null;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				if (commit.length() > 0 && !tokens[0].equals(commit)) {
					revisions.add(revision);
					revision = null;
				}
				if (revision == null) {
					commit = tokens[0];
					revision = new Revision(tokens[0], tokens[2]);
					revision.addFile(tokens[1]);
				} else {
					revision.addFile(tokens[1]);
				}
			}

			reader.close();
			revSize = revisions.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double[] readINCommit(String file, int window) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			int inCount = 0;
			double[] rates = new double[2];
			while ((line = reader.readLine()) != null) {
				// if (line.contains("m--")) continue;
				inCount++;
				String[] tokens = line.split(",");
				for (int i = revisions.size() - 1; i >= 0; i--) {
					if (tokens[0].equals(revisions.get(i).id)) {
						double[] rate = getTwoBFRate(i, window, tokens[1]);
						rates[0] += rate[0];
						rates[1] += rate[1];
						inCount++;
						break;
					}
				}

			}
			reader.close();
			if (inCount > 0) {
				rates[0] = rates[0] * 1.0 / inCount;
				rates[1] = rates[1] * 1.0 / inCount;
			}
			return rates;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public double readINDeleteCommit(String file, int window) {
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			String line = "";
//			int bfCount = 0;
//			int inCount = 0;
//			while ((line = reader.readLine()) != null) {
//				// if (line.contains("m--")) continue;
//
//				boolean start = false;
//				int count = 0;
//				inCount++;
//				String[] tokens = line.split(",");
//				for (Revision revision : revisions) {
//					if (!start) {
//						if (tokens[0].equals(revision.id)) {
//							start = true;
//						}
//					}
//					if (start && count < window) {
//						count++;
//						if (revision.containFile(tokens[1]) && revision.isFix)
//							bfCount++;
//					}
//				}
//			}
//			reader.close();
//			if (inCount > 0)
//				return bfCount * 1.0 / inCount;
//			return 0;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	private double[] getTwoBFRate(int id, int window, String file) {
		//
		double[] rates = new double[2];

		int left = (id - window) < 0 ? 0 : id - window;
		int right = (id + window) >= revSize ? revSize - 1 : id + window;
		int bfCount = 0;
		for (int tmp = id-1; tmp >= left; tmp--) {
			Revision rev = revisions.get(tmp);
			if (rev.containFile(file) && rev.isFix)
				bfCount++;
		}
		rates[0] = bfCount ;
		bfCount = 0;
		for (int tmp = id+1; tmp <= right; tmp++) {
			Revision rev = revisions.get(tmp);
			if (rev.containFile(file) && rev.isFix)
				bfCount++;
		}
		rates[1] += bfCount ;
		return rates;
	}
}
