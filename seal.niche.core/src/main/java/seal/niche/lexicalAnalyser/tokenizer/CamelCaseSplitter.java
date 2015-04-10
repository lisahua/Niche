package seal.niche.lexicalAnalyser.tokenizer;

import java.util.ArrayList;

import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.model.NameToken;

public class CamelCaseSplitter implements INameExecutor {
	private static CamelCaseSplitter splitter = new CamelCaseSplitter();

	public static CamelCaseSplitter getInstance() {
		return splitter;
	}

	private CamelCaseSplitter() {

	}

//	public IdentifierNode executeSingleName(IdentifierNode node) {
//		ArrayList<NameToken> token = node.getTokens();
//		String name = token.get(0).getToken().trim();
//		String[] tokens;
//		// if all upper case, split with "_"
//		if (allUpperCase(name)) {
//			tokens = name.split("_");
//		} else {
//			tokens = name.split("(?=[A-Z][^A-Z])|_");
//		}
//		for (String s : tokens) {
//			if (!s.equals(""))
//				token.add(new NameToken(s));
//		}
//		token.remove(0);
//		return node;
//	}

	private boolean allUpperCase(String name) {
		char[] charArray = name.toCharArray();
		for (char c : charArray) {
			if (c >= 'a')
				return false;
		}
		return true;
	}

	public String[] executeSingleName(String name) {
		String[] tokens;
		if (allUpperCase(name)) {
			tokens = name.split("_");
		} else {
			tokens = name.split("(?=[A-Z][^A-Z])|_");
		}
		
		return tokens;
	}
	public String[] splitStatement(String node) {
		String[] tokenS = node.split(" |_|\\(|\\)|\\.");
		return tokenS;
	}
}
