package seal.niche.lexicalAnalyser.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import seal.niche.lexicalAnalyser.model.IdentifierNode;

public class SimpleNamePrinter {
	ArrayList<IdentifierNode> nodeSet = new ArrayList<IdentifierNode>();
	// PrintWriter writer;
	private int fileNum = 0;

	public SimpleNamePrinter(String outputPath) {
		// try {
		// writer = new PrintWriter(outputPath);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}

	public int getNameInDir(String dirPath) {
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();
		int sum = 0;
		if (listOfFiles == null)
			return sum;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().endsWith(".java")) {
				if (!listOfFiles[i].isDirectory())
					sum += getNameInFile(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				sum += getNameInDir(listOfFiles[i].getAbsolutePath());
			}
		}
		return sum;
	}

	public int getNameInFile(File file) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String fileString = null;
		int sum = 0;
		try {
			fileString = FileUtils.readFileToString(file);
			// writer.println(file.getAbsolutePath());
			fileNum++;
			parser.setSource(fileString.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

			NameASTVisitor mdVisitor = new NameASTVisitor();
			cu.accept(mdVisitor);
			List<IdentifierNode> typeList = mdVisitor.getTypeList();
			for (IdentifierNode node : typeList) {
				// nodeSet.add(node);
				// writer.println("," + node.getName());
				for (IdentifierNode mNode : node.getChildren()) {
					// writer.println("m," + mNode.getName());
					sum++;
					for (IdentifierNode vNode : mNode.getChildren()) {
						// writer.println("v," + vNode.getName());
						sum++;
					}
				}
				for (IdentifierNode fNode : node.getChildren2()) {
					// writer.println("f," + fNode.getName());
					sum++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sum;
	}

	public int getfileNum() {
		return fileNum;
	}
}
