package seal.niche.lexicalAnalyser.model;

import java.util.HashMap;

public class NameToken {
	TokenType type;
	String token;
	@SuppressWarnings("serial")
	private static final HashMap<String, TokenType> typeMap = new HashMap<String, TokenType>() {
		{
			put("CC", TokenType.CC);
			put("CD", TokenType.CD);
			put("DT", TokenType.DT);
			put("EX", TokenType.EX);
			put("FW", TokenType.FW);
			put("IN", TokenType.IN);
			put("JJ", TokenType.JJ);
			put("JJR", TokenType.JJR);
			put("JJS", TokenType.JJS);
			put("LS", TokenType.LS);
			put("MD", TokenType.MD);
			put("NN", TokenType.NN);
			put("NNS", TokenType.NNS);
			put("NNP", TokenType.NNP);
			put("NNPS", TokenType.NNPS);
			put("PDT", TokenType.PDT);
			put("POS", TokenType.POS);
			put("PRP", TokenType.PRP);
			put("RB", TokenType.RB);
			put("RBR", TokenType.RBR);
			put("RBS", TokenType.RBS);
			put("RP", TokenType.RP);
			put("SYM", TokenType.SYM);
			put("TO", TokenType.TO);
			put("UH", TokenType.UH);
			put("VB", TokenType.VB);
			put("VBD", TokenType.VBD);
			put("VBG", TokenType.VBG);
			put("VBN", TokenType.VBN);
			put("VBP", TokenType.VBP);
			put("VBZ", TokenType.VBZ);
			put("WDT", TokenType.WDT);
			put("WP", TokenType.WP);
			put("WRB", TokenType.WRB);

		}
	};

	public NameToken(String token) {
		this.token = token;
	}

	public NameToken(String token, String type) {
		this.token = token;
		this.type = getType(type);
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = getType(type);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String toString() {
		return token + "_" + type;
	}

	private TokenType getType(String t) {
		return typeMap.get(t);
	}
}
