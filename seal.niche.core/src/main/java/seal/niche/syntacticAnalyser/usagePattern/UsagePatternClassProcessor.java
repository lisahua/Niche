package seal.niche.syntacticAnalyser.usagePattern;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * @Author Lisa
 * @Date: Jan 18, 2015
 */
public class UsagePatternClassProcessor extends ASTVisitor {
	ArrayList<String> bugs = new ArrayList<String>();

	public boolean visit(TypeDeclaration declaration) {
		UsagePatternClassVisitor cVisitor = new UsagePatternClassVisitor();
		declaration.accept(cVisitor);
		bugs.add(cVisitor.getFieldMapString());
		bugs.addAll(cVisitor.getBugs());
		return true;
	}

	public ArrayList<String> getUsagePatterns() {
		return bugs;
	}
	
}
