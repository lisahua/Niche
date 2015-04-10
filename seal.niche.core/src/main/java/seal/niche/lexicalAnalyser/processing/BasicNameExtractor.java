package seal.niche.lexicalAnalyser.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import seal.niche.lexicalAnalyser.model.IdentifierNode;

public class BasicNameExtractor implements INameExtractor {

	public ArrayList<IdentifierNode> getNameInDir(String dirPath) {
		ArrayList<IdentifierNode> nodeSet = new ArrayList<IdentifierNode>();
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().endsWith(".java")) {
				// System.out.println(listOfFiles[i].getName().substring(0,
				// listOfFiles[i].getName().length() - 5));
				nodeSet.addAll(getNameInFile(listOfFiles[i]));
			} else if (listOfFiles[i].isDirectory()) {
				nodeSet.addAll(getNameInDir(listOfFiles[i].getAbsolutePath()));
			}
		}
		return nodeSet;
	}

	public ArrayList<IdentifierNode> getNameInFile(File file) {
		ArrayList<IdentifierNode> nodeSet = new ArrayList<IdentifierNode>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String fileString = null;
		try {
			fileString = FileUtils.readFileToString(file);
			parser.setSource(fileString.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

			NameASTVisitor mdVisitor = new NameASTVisitor();
			cu.accept(mdVisitor);
			List<IdentifierNode> typeList = mdVisitor.getTypeList();
			for (IdentifierNode node : typeList) {
				nodeSet.add(node);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodeSet;

	}

	public ArrayList<IdentifierNode> getNameInFile(String file) {
		return getNameInFile(new File(file));
	}
}
