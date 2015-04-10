package seal.niche.lexicalAnalyser.processing;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.model.IdentifierType;

public class NameASTVisitor extends ASTVisitor {

	List<IdentifierNode> typeList = new ArrayList<IdentifierNode>();

	public boolean visit(TypeDeclaration decleration) {

		typeList.add(generateTypeNameNode(decleration));
		return true;
	}

	private IdentifierNode generateTypeNameNode(TypeDeclaration td) {
		// class declaration
		IdentifierNode node = new IdentifierNode(IdentifierType.CLASS);
		node.setModifier(td.getModifiers());
		node.setType(td.getSuperclassType());
		node.setName(td.getName().getIdentifier());

		// hierachy declaration
		// System.out.println(.split("\n")[0]);

		node.setChildren2(generateFieldNameNode(td.getFields()));
		node.setChildren(generateMethodNameNode(td.getMethods()));

		return node;
	}

	private ArrayList<IdentifierNode> generateFieldNameNode(
			FieldDeclaration[] fds) {
		ArrayList<IdentifierNode> nodes = new ArrayList<IdentifierNode>();
		for (FieldDeclaration fd : fds) {
			if (fd.getType().isPrimitiveType()) continue;
			IdentifierNode node = new IdentifierNode(IdentifierType.FIELD);
			node.setModifier(fd.getModifiers());
			node.setType(fd.getType());
			node.setDeclare(generateDeclaration(fd.toString()));

			@SuppressWarnings("rawtypes")
			List fragment = fd.fragments();
			String fString = "";
			for (Object o : fragment)
				fString += o.toString().split("=")[0] + "\t";
			node.setName(fString);
			nodes.add(node);
		}
		return nodes;
	}

	private ArrayList<IdentifierNode> generateMethodNameNode(
			MethodDeclaration[] mds) {
		// method declaration
		ArrayList<IdentifierNode> methods = new ArrayList<IdentifierNode>();
		for (MethodDeclaration md : mds) {
//			if (md.isConstructor() ) continue;
			int modifier = 	md.getModifiers();
		if (modifier==Modifier.ABSTRACT) continue;
			
			IdentifierNode node = new IdentifierNode(IdentifierType.METHOD);
			node.setModifier(modifier);
			node.setType(md.getReturnType2());
			node.setName(md.getName().getIdentifier());
			node.setDeclare(generateDeclaration(md.toString()));
			VariableVisitor fVisitor = new VariableVisitor();
			md.accept(fVisitor);
			node.setChildren(fVisitor.getClassInstances());
			List<SingleVariableDeclaration> parameters = md.parameters();
			node.setChildren2(generateParameter(parameters));
			methods.add(node);
		}
		return methods;
	}

	private ArrayList<IdentifierNode> generateParameter(
			List<SingleVariableDeclaration> parameters) {
		ArrayList<IdentifierNode> paras = new ArrayList<IdentifierNode>();
		for (SingleVariableDeclaration var : parameters) {
			if (var.getType().isPrimitiveType()) continue;
			IdentifierNode node = new IdentifierNode(IdentifierType.METHOD);
			node.setModifier(var.getModifiers());
			node.setType(var.getType());
			node.setName(var.getName().getIdentifier());
			paras.add(node);
		}
		return paras;
	}

	public List<IdentifierNode> getTypeList() {
		return typeList;
	}

	class VariableVisitor extends ASTVisitor {
		ArrayList<IdentifierNode> nodeList = new ArrayList<IdentifierNode>();

		public boolean visit(VariableDeclarationStatement decleration) {
			IdentifierNode node = generateNameNode(decleration);
			if (node!=null)
			nodeList.add(node);
			return true;
		}

		public ArrayList<IdentifierNode> getClassInstances() {
			return nodeList;
		}

		private IdentifierNode generateNameNode(VariableDeclarationStatement vd) {
			// variable declaration
			if (vd.getType().isPrimitiveType()) return null;
			IdentifierNode node = new IdentifierNode(IdentifierType.VARIABLE);
			node.setModifier(vd.getModifiers());
			node.setType(vd.getType());
			node.setDeclare(generateDeclaration(vd.toString()));
			@SuppressWarnings("rawtypes")
			List fragment = vd.fragments();
			String fString = "";
			for (Object o : fragment)
				fString += o.toString().split("=")[0] + "\t";
			node.setName(fString);

			return node;
		}
	}

	private String generateDeclaration(String descrip) {
		String[] fieldS = descrip.split("\n");
		for (int i = 0; i < fieldS.length; i++) {
			if (fieldS[i].trim().startsWith("/")
					|| fieldS[i].trim().startsWith("*"))
				continue;
			return fieldS[i].trim();

		}
		return "";
	}
}
