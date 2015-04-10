package seal.niche.lexicalAnalyser.model;

public class TokenPairModel {
	private String textA;
	private String textB;
	private int number;
	private String nameA;
	private String nameB;

	public TokenPairModel(String tA, String tB) {
		textA = tA;
		textB = tB;
	}
	
	public TokenPairModel(String tA, String tB, String nameA, String nameB) {
		textA = tA;
		textB = tB;
		this.nameA = nameA;
		this.nameB = nameB;
	}

	public String getTextA() {
		return textA;
	}

	public String getTextB() {
		return textB;
	}

	public void addNumber() {
		number++;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int num) {
		number = num;
	}

	public boolean equals(Object that) {
		if (!(that instanceof TokenPairModel))
			return false;
		TokenPairModel compare = (TokenPairModel) that;
		return compare.getTextA().toLowerCase().equals(textA.toLowerCase())
				&& compare.getTextB().toLowerCase().equals(textB.toLowerCase());
	}

	public String getNameA() {
		return nameA;
	}

	public void setNameA(String nameA) {
		this.nameA = nameA;
	}

	public String getNameB() {
		return nameB;
	}

	public void setNameB(String nameB) {
		this.nameB = nameB;
	}

	public void setTextA(String textA) {
		this.textA = textA;
	}

	public void setTextB(String textB) {
		this.textB = textB;
	}

	public String toString() {
		return textA+" "+textB+","+nameA + " " + nameB + " " + number;
	}
	public int hashCode() {
		return 0;
	}
}
