package seal.niche.lexicalAnalyser.processing;

import java.io.File;
import java.util.ArrayList;

import seal.niche.lexicalAnalyser.model.IdentifierNode;

/**
 * @Author Lisa
 * @Date: Jan 16, 2015
 */
public interface INameExtractor {
	public ArrayList<IdentifierNode> getNameInDir(String dirPath);
	public ArrayList<IdentifierNode> getNameInFile(File file);
}
