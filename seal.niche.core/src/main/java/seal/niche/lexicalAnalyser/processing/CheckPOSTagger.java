package seal.niche.lexicalAnalyser.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import seal.niche.lexicalAnalyser.tokenizer.CamelCaseSplitter;

public class CheckPOSTagger {
	HashSet<String> tags = new HashSet<String>();
	CamelCaseSplitter splitter = CamelCaseSplitter.getInstance();
//	POSTaggingProcessor tagger = POSTaggingProcessor.getInstance();
PrintWriter writer ;

public CheckPOSTagger(String  output) {
	try {
		writer = new PrintWriter(output);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}
	public void getNameInDir(String dirPath) {
		File folder = new File(dirPath);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null)
			return;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].getName().endsWith(".java")) {
				if (!listOfFiles[i].isDirectory())
					getNameInFile(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				getNameInDir(listOfFiles[i].getAbsolutePath());
			}
		}

	}

	private void getNameInFile(File file) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String fileString = null;
		try {
			fileString = FileUtils.readFileToString(file);
			parser.setSource(fileString.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

			MethodASTVisitor mdVisitor = new MethodASTVisitor();
			cu.accept(mdVisitor);
			HashSet<String> mNames = mdVisitor.methodNames();
			for (String mName : mNames) {
				processName(mName);
			}
			for (String tag: tags) {
				System.out.println(tag);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processName(String name) {
		String[] tokens = splitter.splitStatement(name);
		for (String token : tokens) {
			writer.print(" "+ token);
		}
		writer.println();
	}
}

class MethodASTVisitor extends ASTVisitor {

	HashSet<String> mNames = new HashSet<String>();

	public boolean visit(MethodDeclaration md) {
		mNames.add(md.getName().toString());
		return true;
	}

	public HashSet<String> methodNames() {
		return mNames;
	}

}
