package seal.niche.empirical.logProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import seal.niche.lexicalAnalyser.model.IdentifierNode;
import seal.niche.lexicalAnalyser.processing.NameASTVisitor;

/**
 * @Author Lisa
 * @Date: Jan 23, 2015
 */
public class NameRecorder {
	private PrintWriter writer;

	public NameRecorder() {
		
	}
	public NameRecorder(String outputPath) {
		try {
			writer = new PrintWriter(outputPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getNameInDir(String dirPath) {
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null) return;
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].getName().endsWith(".java")) {
					if (!listOfFiles[i].isDirectory())
						getNameInFile(listOfFiles[i]);
				} else if (listOfFiles[i].isDirectory()) {
					getNameInDir(listOfFiles[i].getAbsolutePath());
				}
			}
		writer.flush();
	}

	public void getNameInFile(File file) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String fileString = null;
		try {
			fileString = FileUtils.readFileToString(file);
			writer.println(file.getAbsolutePath());
			parser.setSource(fileString.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

			NameASTVisitor mdVisitor = new NameASTVisitor();
			cu.accept(mdVisitor);
			List<IdentifierNode> typeList = mdVisitor.getTypeList();
			for (IdentifierNode node : typeList) {
				// nodeSet.add(node);
				writer.println("t," + node.getName() + "," + node.getDeclare());
				for (IdentifierNode fNode : node.getChildren2()) {
					writer.println("f," + fNode.getName()+","+fNode.getDeclare());
				}
				for (IdentifierNode mNode : node.getChildren()) {
					writer.println("m," + mNode.getName() + ","
							+ mNode.getDeclare());
					for (IdentifierNode pNode : mNode.getChildren2()) {
						writer.println("p," + pNode.getName() + ","
								+ pNode.getDeclare());
					}
					for (IdentifierNode vNode : mNode.getChildren()) {
						writer.println("v," + vNode.getName() + ","
								+ vNode.getDeclare());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void finalize() {
		writer.close();
	}
	
	public int getNumOfMethods (String path) {
		int count = 0;
		try {
			BufferedReader reader  = new BufferedReader(new FileReader(path)) ;
			String line = "";
			while ((line=reader.readLine())!=null) {
				if (line.startsWith("m,"))
					count++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
