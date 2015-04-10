package seal.niche.syntacticAnalyser.purityAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import seal.niche.empirical.statistic.ConfigUtility;
import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.processing.InterfaceChecker;
import seal.niche.lexicalAnalyser.processing.NameASTVisitor;
import seal.niche.syntacticAnalyser.parser.UsagePatternFileReader;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public class PureMethodProcessor extends UsagePatternFileReader {
	private Set<String> pureVerbs;
	private HashMap<String, Double> verbRatioMap;
	private BufferedReader reader = null;
	private PrintWriter filterWriter = null;

	public PureMethodProcessor(String bugOutputPath) {
		pureVerbs = ConfigUtility.getPURITY_VERBS();
		try {
			writer = new PrintWriter(bugOutputPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PureMethodProcessor(String bugOutputPath,
			HashMap<String, Double> pureVerbMap) {
		try {
			writer = new PrintWriter(bugOutputPath);
			reader = new BufferedReader(new FileReader(bugOutputPath));
			filterWriter = new PrintWriter(bugOutputPath + "2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		verbRatioMap = pureVerbMap;
		this.pureVerbs = pureVerbMap.keySet();
	}

	@Override
	public void processLine(String usagePattern) {
		if (isPureMethod(usagePattern))
			if (!pureVerbs.contains(getVerbFromUsagePattern(usagePattern))) {
				writer.println(usagePattern);
			}
	}

	public void printPureVerbs() {

		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(
				new ValueComparator(verbRatioMap));
		sortedMap.putAll(verbRatioMap);
		for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}
	}

	public void filterInterface() {
		writer.flush();
		try {
			String line = "";
			boolean isInterface = false;
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					isInterface = InterfaceChecker.isInterface(line);
					filterWriter.println(line);
				} else if (!isInterface) {
					filterWriter.println(line);
				}
			}
			filterWriter.flush();
			filterWriter.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
