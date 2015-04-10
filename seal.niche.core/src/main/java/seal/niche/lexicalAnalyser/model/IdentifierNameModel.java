package seal.niche.lexicalAnalyser.model;

public class IdentifierNameModel {
	private String name;
	private NameToken[] nameTokens;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NameToken[] getNameTokens() {
		return nameTokens;
	}

	public void setNameTokens(NameToken[] nameTokens) {
		this.nameTokens = nameTokens;
	}

	public IdentifierNameModel(String name) {
		this.name = name;
	}

	public IdentifierNameModel(String name, NameToken[] tokens) {
		this.name = name;
		nameTokens = tokens;

	}
}
