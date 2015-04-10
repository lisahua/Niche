package seal.niche.syntacticAnalyser.usagePattern;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * @Author Lisa
 * @Date: Jan 18, 2015
 * 
 *        APC Visitor marks all impure-prone methods and save to temporary
 *        files, for lexical analysis in the next step. In case it will run out
 *        of memory space when loading POS tagger, we separate syntactic
 *        analysis and lexical analysis.
 */
public class UsagePatternClassVisitor extends ASTVisitor {

	HashSet<String> methodList = new HashSet<String>();
	HashMap<String, String> fieldMap = new HashMap<String, String>();

	public boolean visit(MethodDeclaration md) {
		String methodName = processMethodName(md);
		// FIXME: heuristic constructor filter
		if (methodName.charAt(0) - 'A' >= 0 && methodName.charAt(0) - 'Z' <= 0)
			return true;
		else {
			UsagePatternMethodVisitor methodVisitor = new UsagePatternMethodVisitor();
			md.accept(methodVisitor);
			String bugs = methodVisitor.getUsagePattern();
			// if (bugs.trim().length() > 0)
			methodList.add(methodName + "-->" + bugs.trim());
		}
		return true;
	}

	public HashSet<String> getBugs() {
		return methodList;
	}

	public boolean visit(FieldDeclaration fd) {
		String vString = "";
		String tString = fd.getType().toString();
		for (Object o : fd.fragments()) {
			vString += o.toString().split("=")[0];
		}
		fieldMap.put(vString.trim(), tString.trim());

		return true;
	}

	public String getFieldMapString() {
		String fieldDic = "";
		for (Map.Entry<String, String> entry : fieldMap.entrySet())
			fieldDic += entry.getKey() + ":" + entry.getValue() + ",";
		return fieldDic;
	}

	private String processMethodName(MethodDeclaration md) {
		return md.getName().getIdentifier();
	}
}
