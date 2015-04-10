package seal.niche.syntacticAnalyser.parser;

import java.io.PrintWriter;

import seal.niche.lexicalAnalyser.tokenizer.CamelCaseSplitter;

/**
 * @Author Lisa
 * @Date: Jan 19, 2015
 */
public abstract class CustomizedFileReader {
	
	protected String filePath = "";
	protected PrintWriter writer;
	protected CamelCaseSplitter splitter = CamelCaseSplitter.getInstance();

	public abstract void readFile(String file) ;

	public abstract void processLine(String line);


}
