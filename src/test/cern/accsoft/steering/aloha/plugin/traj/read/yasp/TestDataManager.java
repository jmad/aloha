package cern.accsoft.steering.aloha.plugin.traj.read.yasp;

import java.util.ArrayList;

import cern.accsoft.steering.aloha.util.TestFile;

public class TestDataManager {
	TestDataSet dataset = null;

	/* the dummy - files */
	private TestFile correctorListFile = new TestFile(
			CorrectorList.CORRECTOR_LIST_FILENAME);

	private ArrayList<TestFile> correctorFilesPlus = new ArrayList<TestFile>();
	private ArrayList<TestFile> correctorFilesMinus = new ArrayList<TestFile>();

	public TestDataManager(TestDataSet dataset) {
		this.dataset = dataset;
	}

	public enum TestDataSet {
		SIMPLE_NONZERO_RESPONSE;
	}

	public class TestData {
		public int measurementNumber = 1;
		public String correctorListFileName = null;
		public String stabilityListFileName = null;
	}

	public TestData getTestData() {
		TestData testData = null;

		if (dataset == TestDataSet.SIMPLE_NONZERO_RESPONSE) {
			testData = new TestData();
			testData.correctorListFileName = CorrectorList.CORRECTOR_LIST_FILENAME;
		}

		return testData;
	}

	public final void create() {
		if (dataset == TestDataSet.SIMPLE_NONZERO_RESPONSE) {
			createSimpleDataSet();
		}
	}

	public final void clean() {
		if (dataset == TestDataSet.SIMPLE_NONZERO_RESPONSE) {
			deleteSimpleDataset();
		}
	}

	private final void createSimpleDataSet() {
		correctorListFile.write("MBB.610523.H \n MBIDH.20150.H");

		correctorFilesPlus.clear();
		correctorFilesPlus.add(new TestFile("RM.MBB.610523.H.plus.1"));
		correctorFilesPlus.add(new TestFile("RM.MBIDH.20150.H.plus.1"));

		correctorFilesMinus.clear();
		correctorFilesMinus.add(new TestFile("RM.MBB.610523.H.minus.1"));
		correctorFilesMinus.add(new TestFile("RM.MBIDH.20150.H.minus.1"));

		correctorFilesPlus
				.get(0)
				.write(
						"# CORRECTOR\n"
								+ "MBB.610523           H 1 RBIB.610523             8277.00\n"
								+ "MBIDH.20150          H 1 RBIH.20150             -6000.00\n"
								+ "# MONITOR\n"
								+ "BPCK.610340          H 1     1203.4         0.5         0.0  0  0  OK\n"
								+ "BPCK.610539          H 1     2699.9         1.8         0.0  0  0  OK\n"
								+ "BPMIH.20204          H 1     1352.4         0.5         0.0  0  0  OK");

		correctorFilesMinus
				.get(0)
				.write(
						"# CORRECTOR\n"
								+ "MBB.610523           H 1 RBIB.610523             8276.80\n"// 0.2
								// should
								// work
								+ "MBIDH.20150          H 1 RBIH.20150             -6000.09\n" // smaller
								// than
								// 0.1
								// should
								// have
								// no
								// effect
								+ "# MONITOR\n"
								+ "BPCK.610340          H 1     1203.2         0.5         0.0  0  0  OK\n"
								+ "BPCK.610539          H 1     2698.9         1.8         0.0  0  0  OK\n"
								+ "BPMIH.20204          H 1     1352.9         0.5         0.0  0  0  OK");

		correctorFilesPlus
				.get(1)
				.write(
						"# CORRECTOR\n"
								+ "MBB.610523           H 1 RBIB.610523             8276.90\n"
								+ "MBIDH.20150          H 1 RBIH.20150             -5900.00\n"
								+ "# MONITOR\n"
								+ "BPCK.610340          H 1     1200.2         0.5         0.0  0  0  OK\n"
								+ "BPCK.610539          H 1     2698.1         1.8         0.0  0  0  OK\n"
								+ "BPMIH.20204          H 1     1452.4         0.5         0.0  0  0  OK");

		correctorFilesMinus
				.get(1)
				.write(
						"# CORRECTOR\n"
								+ "MBB.610523           H 1 RBIB.610523             8276.90\n"
								+ "MBIDH.20150          H 1 RBIH.20150             -6100.00\n"
								+ "# MONITOR\n"
								+ "BPCK.610340          H 1     1203.2         0.5         0.0  0  0  OK\n"
								+ "BPCK.610539          H 1     2698.9         1.8         0.0  0  0  OK\n"
								+ "BPMIH.20204          H 1     1352.4         0.5         0.0  0  0  OK");

	}

	private void deleteSimpleDataset() {
		if (correctorListFile != null) {
			correctorListFile.delete();
		}

		deleteCorrectorFiles(correctorFilesPlus);
		deleteCorrectorFiles(correctorFilesMinus);
	}

	private void deleteCorrectorFiles(ArrayList<TestFile> correctorFiles) {
		for (int i = 0; i < correctorFiles.size(); i++) {
			correctorFiles.get(i).delete();
		}
	}

}
