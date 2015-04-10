package seal.niche.lexicalAnalyser.model;

//import java.lang.reflect.Modifier;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.Type;

public class IdentifierNode {
	private IdentifierType identifierType;
	private Type type;
//	private ArrayList<NameToken> nameToken = new ArrayList<NameToken>();
	private int modifier;
	private String name;
	private ArrayList<IdentifierNode> children = new ArrayList<IdentifierNode>();
	private ArrayList<IdentifierNode> children2 = new ArrayList<IdentifierNode>();
	private String declare = "";

	public String getDeclare() {
		return declare;
	}

	public void setDeclare(String declare) {
		this.declare = declare;
	}

	public IdentifierNode(IdentifierType type) {
		identifierType = type;
	}

	public ArrayList<IdentifierNode> getChildren2() {
		return children2;
	}

	public void setChildren2(ArrayList<IdentifierNode> children2) {
		this.children2 = children2;
	}

//	public IdentifierType getIdentifierType() {
//		return identifierType;
//	}
//
//	public void setIdentifierType(IdentifierType identifierType) {
//		this.identifierType = identifierType;
//	}

	public void setIdentifierType(int identifierType) {

	}

	public Type getType() {
		return type;
	}

	public IdentifierNode setType(Type type) {
		if (type != null)
			this.type = type;
		return this;
	}

	public String getName() {
		return name;
	}

//	public ArrayList<NameToken> getTokens() {
//		return nameToken;
//	}
//
	public IdentifierNode setName(String name) {
		this.name =name;
		return this;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		// String modifierS = Modifier.toString(modifier);
		if (Modifier.isPublic(modifier))
			this.modifier = Modifier.PUBLIC;
		else if (Modifier.isProtected(modifier))
			this.modifier = Modifier.PROTECTED;
		else if (Modifier.isPrivate(modifier))
			this.modifier = Modifier.PRIVATE;
	}

	public ArrayList<IdentifierNode> getChildren() {
		return children;
	}

	public IdentifierNode setChildren(ArrayList<IdentifierNode> children) {
		this.children = children;
		return this;
	}

	public IdentifierNode addChildren(IdentifierNode node) {
		children.add(node);
		return this;
	}

//	public String toString() {
//		switch (identifierType) {
//		case CLASS:
//			return identifierType + "\t" + modifier + "\t" + type + "\t"
//					+ nameToken.get(0) + "\t";
//		case FIELD:
//			return "\t" + identifierType + "\t" + modifier + "\t" + type + "\t"
//					+ nameToken.get(0) + "\t";
//		case METHOD:
//			return "\t" + identifierType + "\t" + modifier + "\t" + type + "\t"
//					+ nameToken.get(0) + "\t";
//		case VARIABLE:
//			return "\t\t" + identifierType + "\t" + modifier + "\t" + type
//					+ "\t" + nameToken.get(0) + "\t";
//		default:
//			break;
//		}
//		return "";
//	}

//	public boolean equals(Object obj) {
//		if (obj instanceof IdentifierNode) {
//			IdentifierNode that = (IdentifierNode) obj;
//			if (that.getName().equals(this.getName()))
//				return true;
//		}
//		return false;
//	}

//	public String getTokenString() {
//		String result = "";
//		for (NameToken s : nameToken) {
//			result += s.getToken() + "_" + s.getType() + " ";
//		}
//		return result;
//	}
}
