package seal.niche.lexicalAnalyser.tokenizer;

import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.model.NameToken;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger implements INameExecutor {
	private static MaxentTagger tagger;
	public static POSTagger posTagger = new POSTagger();

	private POSTagger() {
		try {
			tagger = new MaxentTagger(
					"src/main/resources/seal/niche/POSTaggerModel/english-left3words-distsim.tagger");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static POSTagger getInstance() {
		return posTagger;
	}

//	public IdentifierNode executeSingleName(IdentifierNode node) {
//
//		for (int i = 0; i < node.getTokens().size(); i++) {
//			String tt = tagger.tagString(node.getTokens().get(i).getToken());
//			String[] ttype = tt.split("_");
//			if (ttype.length < 2)
//				continue;
//			node.getTokens().get(i).setType(ttype[1].trim());
//		}
//		return node;
//	}

	public NameToken executeSingleName(String token) {
		String tt = tagger.tagString(token);
		String[] ttype = tt.split("_");
		if (ttype.length < 2)
			return null;
		return new NameToken(token, ttype[1].trim());
	}

	public NameToken[] executeSingleName(String[] tokens) {
		NameToken[] ntList = new NameToken[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			ntList[i] = executeSingleName(tokens[i]);
		}
		return ntList;
	}

	public String executeSingleNameString(String[] tokens) {
		String nameTokenString = "";
		for (int i = 0; i < tokens.length; i++) {
			String tt = tagger.tagString(tokens[i]);
			String[] ttype = tt.split("_");
			if (ttype.length < 2)
				continue;
			nameTokenString += ttype[1].trim()+"_";
		}
		return nameTokenString;
	}
}
