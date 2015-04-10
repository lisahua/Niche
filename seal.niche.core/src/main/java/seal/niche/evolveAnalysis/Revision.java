package seal.niche.evolveAnalysis;

import java.util.HashSet;

/**
 * @Author Lisa
 * @Date: Mar 3, 2015
 */
public class Revision {
	String id = "";
	HashSet<String> files = new HashSet<String>();
	boolean isFix = false;

	public Revision(String id, String isFix) {
		this.id = id;
		this.isFix = isFix.equals("1") ? true : false;
	}

	public void addFile(String file) {
		try {
			file = file.substring(file.lastIndexOf("/") + 1);
			files.add(file);
		} catch (Exception e) {

		}
	}

	public boolean containFile(String file) {
		try {
			file = file.substring(file.lastIndexOf("/") + 1);
			return files.contains(file);
		} catch (Exception e) {

		}
		return false;
	}
}
