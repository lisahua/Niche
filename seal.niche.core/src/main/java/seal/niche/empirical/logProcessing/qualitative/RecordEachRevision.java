package seal.niche.empirical.logProcessing.qualitative;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class RecordEachRevision {
	HashSet<SingleMethodRecordModel> allMethods = new HashSet<SingleMethodRecordModel>();

	private void initMethodNames() throws Exception {
		HashSet<String> methods = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allNames.txt"));
		String line = "";
		String fileName = "";
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("/")) {
				methods.remove("");
				if (methods.size() > 0) {
					for (String s : methods) {
						allMethods.add(new SingleMethodRecordModel(
								recordProjects(fileName), fileName,
								getSimpleFileName(fileName), s));
					}
				}
				methods.clear();
				fileName = getFileName(line);
			} else if (line.startsWith("m,")) {
				methods.add(line.trim().substring(2).split(",")[0]);
			}
		}
		reader.close();
	}

	private void initBugFixCommit() throws Exception {
		HashMap<String, SingleMethodRecordModel> allCommitF = new HashMap<String, SingleMethodRecordModel>();

		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allLogBFChurn.txt"));
		String line = "";

		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length < 4)
				continue;
			String simpleName = getSimpleFileName(tokens[1]);
			SingleMethodRecordModel model = allCommitF.containsKey(simpleName) ? allCommitF
					.get(simpleName) : new SingleMethodRecordModel("", "",
					simpleName, "");
			if (tokens[2].equals("1"))
				model.addBugFix(1);
			model.addChurn(Integer.parseInt(tokens[3])
					+ Integer.parseInt(tokens[4]));
			model.addCommits(tokens[0]);
		}
		reader.close();

	}

	private void initDeclareCommit() throws Exception {
		HashSet<SingleMethodRecordModel> allCommitM = new HashSet<SingleMethodRecordModel>();
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allDeclareAddCommit.txt"));
		String line = "";

		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length < 3)
				continue;
			if (tokens[2].contains("m--")) {
				SingleMethodRecordModel model = new SingleMethodRecordModel("",
						"", tokens[1], tokens[2].substring(3));
				model.setStartCommit(tokens[0]);
			}
		}
		reader.close();
		reader = new BufferedReader(new FileReader(
				ConfigUtility.outputPath + "allDeclareDeleteCommit.txt"));
		
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(",");
			if (tokens.length < 3)
				continue;
			if (tokens[2].contains("m--")) {
				SingleMethodRecordModel model = new SingleMethodRecordModel("",
						"", tokens[1], tokens[2].substring(3));
				model.setEndCommit(tokens[0]);
			}
		}
		reader.close();
		
	}

	public void initIN() {

	}

	private String recordProjects(String path) {
		int index = path.indexOf("/");
		String proj = path.substring(0, index);
		return proj;
	}

	private String getFileName(String file) {
		int index = file
				.indexOf("/Users/admin/Documents/nameExample/snapshots/");
		// recordProjects(file);
		if (index >= 0)
			return file.substring(index + 45);
		return file;
	}

	private String getSimpleFileName(String file) {
		int index = file.lastIndexOf("/");
		if (index >= 0)
			return file.substring(index + 1);
		return file;

	}
}
