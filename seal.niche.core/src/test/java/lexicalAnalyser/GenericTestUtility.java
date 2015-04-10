package lexicalAnalyser;

import java.util.ArrayList;

import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.processing.BasicNameExtractor;
import seal.niche.lexicalAnalyser.tokenizer.CamelCaseSplitter;
import seal.niche.lexicalAnalyser.tokenizer.POSTagger;

public class GenericTestUtility {
	protected BasicNameExtractor nameExtractor = new BasicNameExtractor();
	protected CamelCaseSplitter nameSplitter = CamelCaseSplitter.getInstance();
	protected POSTagger tagger = POSTagger.getInstance();

	// @Test

	public ArrayList<IdentifierNode> generateNodesDir(String dir) {
		ArrayList<IdentifierNode> nodeSet = nameExtractor.getNameInDir(dir);
		// nodeSet = nameSplitter.executeNames(nodeSet);
		// nodeSet = tagger.executeNames(nodeSet);
		return nodeSet;
	}

}
