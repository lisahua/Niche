package seal.niche.lexicalAnalyser.processing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;

import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.tokenizer.AbbrviationStemmer;
import seal.niche.lexicalAnalyser.tokenizer.CamelCaseSplitter;
import seal.niche.lexicalAnalyser.tokenizer.POSTagger;

/**
 * @Author Lisa
 * @Date: Oct 11, 2014
 * 
 *        I separate the name parsing and name processing because executing two
 *        together results in out of memory error.
 */
public class NameProcessingDecorator {
	private CamelCaseSplitter splitter = CamelCaseSplitter.getInstance();
	private POSTagger tagger = POSTagger.getInstance();
	private AbbrviationStemmer abbrStemmer = AbbrviationStemmer.getInstance();

	public void readNameFile(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			PrintWriter writer = new PrintWriter(filePath + ".nodup");
			String line = "";
			HashSet<String> identifierSet = new HashSet<String>();
			String readingFile = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (line.contains("/")) {
					writer.println(readingFile);
					for (String s : identifierSet) {
						writer.println(s+","+executeSingleName(s));
					}
					identifierSet.clear();
					readingFile = line;
				} else {
					identifierSet.add(line);
				}
			}
			writer.println(readingFile);
			for (String s : identifierSet) {
				writer.println(s);
			}
			reader.close();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String executeSingleName(String nameWithComma) {
		return tagger.executeSingleNameString(splitter
				.executeSingleName(nameWithComma.replace(",", "")));
		// abbrStemmer.executeSingleName(node);
		// System.out.println(node.getTokenString());

	}
}
