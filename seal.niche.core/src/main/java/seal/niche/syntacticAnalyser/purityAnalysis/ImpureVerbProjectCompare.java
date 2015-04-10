package seal.niche.syntacticAnalyser.purityAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

import seal.niche.empirical.statistic.ConfigUtility;
import seal.niche.lexicalAnalyser.tokenizer.CamelCaseSplitter;

/**
 * @Author Lisa
 * @Date: Feb 27, 2015
 */
public class ImpureVerbProjectCompare {
	ArrayList<HashSet<String>> pureVerbs = new ArrayList<HashSet<String>>();
	ArrayList<HashSet<String>> impureVerbs = new ArrayList<HashSet<String>>();
	private final double threshold = ConfigUtility.PURITY_VERB_RATIO_THRESHOLD;
	private CamelCaseSplitter splitter = CamelCaseSplitter.getInstance();

	public void readFile(String file) {
		int pureMethods = 0, impureMethods = 0, pureTerms = 0, impureTerms = 0, inPure = 0, inImpure = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			HashSet<String> pure = new HashSet<String>();
			HashSet<String> impure = new HashSet<String>();
			while ((line = reader.readLine()) != null) {
				String[] token = line.split(",");
				int pureC = Integer.parseInt(token[1]);
				int impureC = Integer.parseInt(token[2]);
				pureMethods += pureC;
				impureMethods += impureC;
				if (token[0].length() < 2)
					continue;
				if (pureC * 1.0 / (pureC + impureC) > threshold) {
					pure.add(token[0]);
					pureTerms++;
					inPure += impureC;
				} else {
					impure.add(token[0]);
					impureTerms++;
					inImpure += pureC;
				}
			}
			pureVerbs.add(pure);
			impureVerbs.add(impure);
			System.out.println(pureMethods + "," + impureMethods + ","
					+ pureTerms + "," + impureTerms + "," + inPure + ","
					+ inImpure + ",");
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPureOverlap() {
		for (int i = 0; i < pureVerbs.size(); i++) {
			int overlap = 0;
			HashSet<String> allOther = new HashSet<String>();
			for (int j = 0; j < pureVerbs.size(); j++) {
				if (i == j)
					continue;
				allOther.addAll(pureVerbs.get(j));
			}
			for (String pureTerm : pureVerbs.get(i)) {
				if (allOther.contains(pureTerm))
					overlap++;
			}
			System.out.println(overlap);
		}
	}

	public void getPureContradiction() {

		for (int i = 0; i < pureVerbs.size(); i++) {
			// System.out.println("\n project "+i);
			int contract = 0;
			HashSet<String> allOther = new HashSet<String>();
			for (int j = 0; j < impureVerbs.size(); j++) {
				if (i == j)
					continue;
				allOther.addAll(impureVerbs.get(j));
			}
			for (String pureTerm : pureVerbs.get(i)) {
				if (allOther.contains(pureTerm)) {
					System.out.print(", " + pureTerm);
					contract++;
				}
			}
			System.out.println(contract);
		}
	}

	public void getImpurePureOverlap() {
		for (int i = 0; i < impureVerbs.size(); i++) {
			int overlap = 0;
			HashSet<String> allOther = new HashSet<String>();
			for (int j = 0; j < impureVerbs.size(); j++) {
				if (i == j)
					continue;
				allOther.addAll(impureVerbs.get(j));
			}
			for (String pureTerm : impureVerbs.get(i)) {
				if (allOther.contains(pureTerm))
					overlap++;
			}
			System.out.println(overlap);
		}
	}

	public void getImpureContradiction() {

		for (int i = 0; i < impureVerbs.size(); i++) {
			// System.out.println("\n project "+i);
			int contract = 0;
			HashSet<String> allOther = new HashSet<String>();
			for (int j = 0; j < pureVerbs.size(); j++) {
				if (i == j)
					continue;
				allOther.addAll(pureVerbs.get(j));
			}
			for (String pureTerm : impureVerbs.get(i)) {
				if (allOther.contains(pureTerm)) {
					System.out.print(", " + pureTerm);
					contract++;
				}
			}
			System.out.println(contract);
		}
	}

	public int getConflictPureTermIN(String inPath, int index) {
		HashSet<String> allOther = new HashSet<String>();
		for (int j = 0; j < impureVerbs.size(); j++) {
			if (index == j)
				continue;
			allOther.addAll(impureVerbs.get(j));
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inPath));
			String line = "";
			int inPureConflict = 0;
			while ((line = reader.readLine()) != null) {
				if (line.contains("-->")) {
					String name = splitter.executeSingleName(line.split("-->")[0])[0];
					for (String term : allOther) {
						if (name.equals(term)) {
							System.out.print("," + name);
							inPureConflict++;
							break;
						}
					}
				}
			}
			System.out.println();
			reader.close();
			return inPureConflict;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getConflictImpureTermIN(String inPath, int index) {
		HashSet<String> allOther = new HashSet<String>();
		for (int j = 0; j < pureVerbs.size(); j++) {
			if (index == j)
				continue;
			allOther.addAll(pureVerbs.get(j));
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inPath));
			String line = "";
			int inPureConflict = 0;
			while ((line = reader.readLine()) != null) {
				if (line.contains("-->")) {
					String name =  splitter.executeSingleName(line.split("-->")[0])[0];
					for (String term : allOther) {
						if (name.equals(term)) {
							System.out.print("," + name);
							inPureConflict++;
							break;
						}
					}
				}
			}
			System.out.println();
			reader.close();
			return inPureConflict;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void getCorpus() {
		HashSet<String> pureVerb = new HashSet<String>();
		HashSet<String> impureVerb = new HashSet<String>();
		
		for (String pVerb: pureVerbs.get(0)) {
			int id=1;
			for (;id< pureVerbs.size();id++) {
				if (!pureVerbs.get(id).contains(pVerb))
					break;
			}
			if (id==pureVerbs.size()) {
				pureVerb.add(pVerb);
				System.out.print(","+pureVerb);
			}
		}
		System.out.println("corpus pure terms "+pureVerb.size());
		
		for (String pVerb: impureVerbs.get(0)) {
			int id=1;
			for (;id< impureVerbs.size();id++) {
				if (!impureVerbs.get(id).contains(pVerb))
					break;
			}
			if (id==impureVerbs.size()) {
				impureVerb.add(pVerb);
				System.out.print(","+pVerb);
			}
		}
		System.out.println("corpus impure terms "+impureVerb.size());
//		//exclusion
//		HashSet<String> excludePure = new HashSet<String>();
//		HashSet<String> excludeImpure = new HashSet<String>();
//		
//		for (String pure: pureVerb) {
//			if (!impureVerb.contains(pure))
//				excludePure.add(pure);
//		}
//		System.out.println("exclude corpus pure terms "+excludePure.size());
//		
//		for (String impure: impureVerb) {
//			if (!pureVerb.contains(impure))
//				excludeImpure.add(impure);
//		}
//		System.out.println("exclude corpus impure terms "+excludeImpure.size());
//		
			
	}
	public void findCompleteViolate() {
		
	}
	
	
}
