package seal.niche.lexicalAnalyser.processing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * @Author Lisa
 * @Date: Feb 21, 2015
 */
public class InterfaceChecker {

	public static boolean isInterface(String line) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String fileString = null;
		int sum = 0;
		try {
			fileString = FileUtils.readFileToString(new File(line));
			parser.setSource(fileString.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			
			IASTVisitor mdVisitor = new IASTVisitor();
			cu.accept(mdVisitor);
			return mdVisitor.hasInteface;
		} catch (IOException e) {
//			e.printStackTrace();
		}
		return false;
	}
}

class IASTVisitor extends ASTVisitor {
	boolean hasInteface = false;

	public boolean visit(TypeDeclaration decleration) {
		hasInteface = hasInteface ? hasInteface : decleration.isInterface();
		return true;
	}
}
