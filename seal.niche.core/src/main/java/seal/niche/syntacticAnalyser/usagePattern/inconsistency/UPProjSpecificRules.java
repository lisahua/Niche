package seal.niche.syntacticAnalyser.usagePattern.inconsistency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author Lisa
 * @Date: Mar 6, 2015
 */
public class UPProjSpecificRules {
	private ArrayList<HashMap<String, String>> apiUse = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> inFPVMaps = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> inMMaps = new ArrayList<HashMap<String, String>>();

	private void readUP(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path
					+ ".upi.tmp"));
			String line = "";
			HashMap<String, String> names = new HashMap<String, String>();
			while ((line = reader.readLine()) != null) {
				String name = "";
				String[] token = line.split(",");
				for (int i = 1; i < token.length - 2; i++) {
					if (token[i].length() < 1)
						continue;
					name += token[i].trim() + ",";
				}
				String current = names.containsKey(token[0]) ? names
						.get(token[0]) : "";
				names.put(token[0], current + "," + name);
			}
			apiUse.add(names);
			// System.out.println(names.size());
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void readFile(String path) {
		readINs(path);
		readUP(path);
	}

	private void readINs(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path
					+ ".identifierAnomal"));
			String line = "";
			HashMap<String, String> useName = new HashMap<String, String>();
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("--");
				String api = tokens[1].split(",")[0];
				String name = useName.containsKey(api) ? useName.get(api) : "";
				useName.put(api, name + "," + tokens[0]);
			}
			reader.close();
			inFPVMaps.add(useName);

			HashMap<String, String> mIName = new HashMap<String, String>();
			reader = new BufferedReader(new FileReader(path + ".methodAnomal"));
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("--");
				String api = tokens[1].split(",")[0];
				String name = useName.containsKey(api) ? useName.get(api) : "";
				mIName.put(api, name + "," + tokens[0]);
			}
			reader.close();
			inMMaps.add(mIName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, String> mergeOverlap(int i) {
		HashMap<String, String> allOtherName = new HashMap<String, String>();
		for (int j = 0; j < apiUse.size(); j++) {
			if (i == j)
				continue;
			HashMap<String, String> otherMap = apiUse.get(j);
			for (Map.Entry<String, String> entry : otherMap.entrySet()) {
				String use = entry.getKey();
				String current = allOtherName.containsKey(use) ? allOtherName
						.get(use) : "";
				allOtherName.put(use, current + "," + entry.getValue());
			}
		}
		return allOtherName;
	}

	public void getOverlap() {

		for (int i = 0; i < apiUse.size(); i++) {
			HashMap<String, String> pilot = apiUse.get(i);
			// HashSet<String> apiNames = new HashSet<String>();
			HashMap<String, String> useNameContra = new HashMap<String, String>();
			HashMap<String, String> useNameOverlap = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : pilot.entrySet()) {
				String use = entry.getKey();
				String overlaps = "";
				String contradict = "";
				for (int j = 0; j < apiUse.size(); j++) {
					if (i == j)
						continue;
					if (!apiUse.get(j).containsKey(use))
						continue;
					// ok contains
					HashSet<String> pilotTokens = new HashSet<String>(
							Arrays.asList(entry.getValue().split(",")));
					HashSet<String> allOtherTokens = new HashSet<String>(
							Arrays.asList(apiUse.get(j).get(use).split(",")));
					pilotTokens.remove("");
					allOtherTokens.remove("");
					for (String pilotT : pilotTokens) {
						if (allOtherTokens.contains(pilotT)) {
							overlaps += pilotT + ",";
						} else {
							contradict += pilotT + ",";
						}
					}
					if (overlaps.length() > 1)
						useNameOverlap.put(use, overlaps);
					if (contradict.length() > 1)
						useNameContra.put(use, contradict);
				}
			}
			// checkINOverlap(useNameOverlap,i);
			// count # of names
			int uniqueUse = 0;
			for (Map.Entry<String, String> entry : pilot.entrySet()) {
				String use = entry.getKey();
				if (!useNameContra.containsKey(use)
						&& !useNameOverlap.containsKey(use)) {
					HashSet<String> term = new HashSet<String>(
							Arrays.asList(entry.getValue().split(",")));
					term.remove("");
					uniqueUse += term.size();
				}
			}
			System.out.println(uniqueUse + "," + countTerm(useNameContra) + ","
					+ countTerm(useNameOverlap));
			// HashMap<String,String> merge = useNameContra;
			// merge.putAll(useNameOverlap);

			// System.out.println(uniqueUse + "," + useNameContra.size() + ","
			// + useNameContra.size() + "," + useNameOverlap.size());

		}
	}

	private int countTerm(HashMap<String, String> maps) {
		int uniqueUse = 0;
		for (Map.Entry<String, String> entry : maps.entrySet()) {
			HashSet<String> term = new HashSet<String>(Arrays.asList(entry
					.getValue().split(",")));
			term.remove("");
			uniqueUse += term.size();
		}
		return uniqueUse;

	}

	public void checkINOverlap(HashMap<String, String> ins, int i) {
		HashMap<String, String> fpvMap = inFPVMaps.get(i);
		HashMap<String, String> mMap = inMMaps.get(i);
		int fpvOverlap = 0, mOverlap = 0;

		for (Map.Entry<String, String> entry : ins.entrySet()) {
			String use = entry.getKey();
			HashSet<String> termSet = new HashSet<String>(Arrays.asList(entry
					.getValue().split(",")));
			termSet.remove("");
			if (fpvMap.containsKey(use)) {
				HashSet<String> inSet = new HashSet<String>(
						Arrays.asList(fpvMap.get(use).split(",")));
				inSet.remove("");
				for (String in : inSet) {
					if (termSet.contains(in))
						fpvOverlap++;
				}
			}
			if (mMap.containsKey(use)) {
				HashSet<String> inSet = new HashSet<String>(Arrays.asList(mMap
						.get(use).split(",")));
				inSet.remove("");
				for (String in : inSet) {
					if (termSet.contains(in))
						mOverlap++;
				}
			}
		}
		// System.out.println(fpvOverlap+","+mOverlap);
	}
}
