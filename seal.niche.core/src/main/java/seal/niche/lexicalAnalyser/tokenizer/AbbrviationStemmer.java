package seal.niche.lexicalAnalyser.tokenizer;

import seal.niche.lexicalAnalyser.model.IdentifierNode;

/**
 * @Author Lisa
 * @Date: Jan 16, 2015
 */
public class AbbrviationStemmer implements INameExecutor {
	private static AbbrviationStemmer abbrStemmer = new AbbrviationStemmer();

	private AbbrviationStemmer() {

	}

	public static AbbrviationStemmer getInstance() {
		return abbrStemmer;
	}

	
	public IdentifierNode executeSingleName(IdentifierNode node) {
		// TODO Adapt from AMAP, have bug in AMAP
		return node;
	}

}
