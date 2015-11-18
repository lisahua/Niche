package tracability;

import org.testng.annotations.Test;

import seal.niche.empirical.logProcessing.qualitative.BugFixMatching;
import seal.niche.empirical.logProcessing.qualitative.CombineMethodName;
import seal.niche.empirical.logProcessing.qualitative.FileBugFixConditional;
import seal.niche.empirical.logProcessing.qualitative.InvokeMatching;
import seal.niche.empirical.logProcessing.qualitative.QualitativeAnalysis;

public class TestMethodCorrelation {
	//@Test
	public void testMethodNames() {
		// CombineMethodName.combineMethods(ConfigUtility.outputPath+"allNames.txt");
		try {
			// CombineMethodName.combineINObjWithMethod();
			// CombineMethodName.combinePureMethods();
			// CombineMethodName.combineImpureMethods();
			// CombineMethodName.formatImpureMethods();;
			// CombineMethodName.combineINWithAllMethods();
//			CombineMethodName.countINMethods();;
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTraceBFCommit() {
		try {
//			 BugFixMatching.matchBFWithMethod();
			 BugFixMatching.countINBF();
			FileBugFixConditional.matchBFWithMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void testTraceInvokeCommit() {
		try {
//			 InvokeMatching.combineInvokeCommit();;
			 InvokeMatching.countInvokeRate();;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
		public void testTraceQuality() {
			try {
//				 InvokeMatching.combineInvokeCommit();;
				QualitativeAnalysis.prepareQualitative();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
