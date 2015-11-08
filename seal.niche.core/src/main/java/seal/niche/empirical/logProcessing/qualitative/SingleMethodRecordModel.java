package seal.niche.empirical.logProcessing.qualitative;

public class SingleMethodRecordModel {
	private String project = "";
	private String fileName = "";
	private String simpleFileName = "";
	private char type;
	private String name;
	private int bugFix = 0;
	private int nonFix = 0;
	private int in_pure;
	private int in_impure;
	private int in_obj;
	private int cMethod = 0;
	private int cObj = 0;
	private int invoke_num;
	private String startCommit = "";
	private String endCommit = "";
	private int lifeSpan;
	private String commits="";
	private int loc = 0;
	private int churn = 0;
	private int parameters = 0;
	private int fanIn = 0;
	private int fanOut = 0;
	private int complexity = 0;
	private int fieldRead = 0;
	private int fieldWrite = 0;
	private int developers = 0;
	
	public SingleMethodRecordModel(String proj, String file, String simpleFile,
			String name) {
		project = proj;
		fileName = file;
		this.name = name;
		simpleFileName = simpleFile;
	}

	public String getSimpleFileName() {
		return simpleFileName;
	}

	public void setSimpleFileName(String simpleFileName) {
		this.simpleFileName = simpleFileName;
	}

	public void setBFIN() {

	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBugFix() {
		return bugFix;
	}

	public void setBugFix(int bugFix) {
		this.bugFix = bugFix;
	}
	public void addBugFix(int bugFix) {
		this.bugFix += bugFix;
	}
	public int getNonFix() {
		return nonFix;
	}

	public void setNonFix(int nonFix) {
		this.nonFix = nonFix;
	}
	public void addNonFix(int nonFix) {
		this.nonFix += nonFix;
	}
	public int getIn_pure() {
		return in_pure;
	}

	public void setIn_pure(int in_pure) {
		this.in_pure = in_pure;
	}

	public int getIn_impure() {
		return in_impure;
	}

	public void setIn_impure(int in_impure) {
		this.in_impure = in_impure;
	}

	public int getIn_obj() {
		return in_obj;
	}

	public void setIn_obj(int in_obj) {
		this.in_obj = in_obj;
	}

	public int getcMethod() {
		return cMethod;
	}

	public void setcMethod(int cMethod) {
		this.cMethod = cMethod;
	}

	public int getcObj() {
		return cObj;
	}

	public void setcObj(int cObj) {
		this.cObj = cObj;
	}

	public int getInvoke_num() {
		return invoke_num;
	}

	public void setInvoke_num(int invoke_num) {
		this.invoke_num = invoke_num;
	}

	public String getStartCommit() {
		return startCommit;
	}

	public void setStartCommit(String startCommit) {
		this.startCommit = startCommit;
	}

	public String getEndCommit() {
		return endCommit;
	}

	public void setEndCommit(String endCommit) {
		this.endCommit = endCommit;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void setLifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	public String getCommits() {
		return commits;
	}

	public void setCommits(String commits) {
		this.commits = commits;
	}
	public void addCommits(String commits) {
		this.commits += commits;
	}
	public int getLoc() {
		return loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}

	public int getChurn() {
		return churn;
	}

	public void setChurn(int churn) {
		this.churn = churn;
	}
	public void addChurn(int churn) {
		this.churn += churn;
	}
	public int getParameters() {
		return parameters;
	}

	public void setParameters(int parameters) {
		this.parameters = parameters;
	}

	public int getFanIn() {
		return fanIn;
	}

	public void setFanIn(int fanIn) {
		this.fanIn = fanIn;
	}

	public int getFanOut() {
		return fanOut;
	}

	public void setFanOut(int fanOut) {
		this.fanOut = fanOut;
	}

	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	public int getFieldRead() {
		return fieldRead;
	}

	public void setFieldRead(int fieldRead) {
		this.fieldRead = fieldRead;
	}

	public int getFieldWrite() {
		return fieldWrite;
	}

	public void setFieldWrite(int fieldWrite) {
		this.fieldWrite = fieldWrite;
	}

	public int getDevelopers() {
		return developers;
	}

	public void setDevelopers(int developers) {
		this.developers = developers;
	}

}
