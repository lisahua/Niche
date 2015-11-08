package tracability;

import org.testng.annotations.Test;

import seal.niche.empirical.logProcessing.qualitative.CombineMethodName;
import seal.niche.empirical.logProcessing.qualitative.ConfigUtility;

public class TestMethodCorrelation {
	@Test
	public void testMethodNames() {
		// CombineMethodName.combineMethods(ConfigUtility.outputPath+"allNames.txt");
		try {
			CombineMethodName.combineINObjWithMethod();
			// CombineMethodName.combinePureMethods();
			// CombineMethodName.combineImpureMethods();
			// CombineMethodName.formatImpureMethods();;
//			CombineMethodName.combineINWithAllMethods();
			CombineMethodName.countINFiles();;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
