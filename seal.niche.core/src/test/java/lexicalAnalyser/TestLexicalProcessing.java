package lexicalAnalyser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.testng.annotations.Test;

import seal.niche.lexicalAnalyser.processing.NameProcessingDecorator;
import seal.niche.lexicalAnalyser.processing.SimpleNamePrinter;

public class TestLexicalProcessing {

	//@Test
	public void printNodes() {
		SimpleNamePrinter printer = new SimpleNamePrinter(
				"src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0.txt");
		printer.getNameInDir("/Users/admin/Documents/snapshot/elasticsearch-1.0.0");
		// printNodes(nodeSet);
	}

	// public void testSoot() {
	// Pack jtp = PackManager.v().getPack("jtp");
	// }
	// @Test
	public void printProcessedNodes() {
		NameProcessingDecorator processor = new NameProcessingDecorator();
		processor
				.readNameFile("src/test/resources/seal/niche/output/lexicalAnalysis/elasticsearch-1.0.0.txt");
	}

}
