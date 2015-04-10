package seal.niche.syntacticAnalyser.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import seal.niche.lexicalAnalyser.tokenizer.CamelCaseSplitter;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public abstract class UsagePatternFileReader extends CustomizedFileReader {
	protected HashMap<String, String> fieldTypeMap = new HashMap<String, String>();
	protected String processingClass = "";
	protected String filePath = "";
	protected PrintWriter writer;
	protected boolean notifyFileChange = true;
	protected CamelCaseSplitter splitter = CamelCaseSplitter.getInstance();

	public void readFile(String file) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {

					fileChange(line);
					continue;
				} else if (!line.contains("-->")) {
					String[] fields = line.split(",");
					for (String field : fields) {
						String[] type = field.split(":");
						if (type.length > 1)
							fieldTypeMap.put(type[0], type[1]);
					}

					continue;
				} else {
					processLine(line);
				}
			}
			reader.close();
			if (writer != null)
				writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void processLine(String usagePattern);

	public void fileChange(String file) {
		if (notifyFileChange && writer != null)
			writer.println(file);
		fieldTypeMap.clear();
		filePath = file;
		try {
			processingClass = file.substring(file.lastIndexOf("/") + 1,
					file.length() - 5);
		} catch (Exception e) {
			// FIXME
		}
	}

	protected boolean isPureMethod(String usagePattern) {
		String[] nameUsage = usagePattern.split("-->");
		if (nameUsage.length > 1) {
			if (nameUsage[1].contains("2"))
				return false;
			HashSet<String> usageSet = new HashSet<String>(
					Arrays.asList(nameUsage[1].split(",")));
			usageSet.remove("");
			usageSet.remove("1");
			usageSet.remove("2");
			for (String usage : usageSet)
				if (!usage.contains("["))
					return false;
		}
		return true;
	}

	protected String getVerbFromUsagePattern(String usagePattern) {
		return getVerbFromMtdName(usagePattern.split("-->")[0]);
	}

	protected String getVerbFromMtdName(String mtdName) {
		try {
			return splitter.executeSingleName(mtdName)[0].trim();
		} catch (Exception e) {
	
		}
		return "";
	}

}
