package cern.accsoft.steering.aloha.plugin.traj.read.yasp;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Test;

import cern.accsoft.steering.aloha.util.LoggedTestCase;
import cern.accsoft.steering.aloha.util.TestFile;
import cern.accsoft.steering.aloha.util.io.NameListException;


public class CorrectorListTest extends LoggedTestCase {
	private TestFile testFile = null;
	private CorrectorList correctorList = null;

	@After
	public void deleteTestFile() {
		if (testFile != null) {
			testFile.delete();
		}
	}

	@Test(expected = NameListException.class)
	public void testParseEmptyFileName() throws NameListException {
		correctorList = new CorrectorList(new File(""));
		correctorList.parse();
	}

	@Test(expected = NameListException.class)
	public void testParseWrongFileName() throws NameListException {
		correctorList = new CorrectorList(new File("CORR.LST"));
		correctorList.parse();
	}

	@Test(expected = NameListException.class)
	public void testParseFileNotFound() throws NameListException {
		correctorList = new CorrectorList(new File("CORR.LIST"));
		correctorList.parse();
	}

	@Test
	public void testParseEmptyFile() throws NameListException {
		testFile = new TestFile("CORR.LIST");
		testFile.write("");
		correctorList = new CorrectorList(new File("CORR.LIST"));
		assertEquals("Initially Correctors - List has to be empty!", 0, correctorList
				.getNames().size());
		correctorList.parse();
		assertEquals("Correctors - List has to be still empty!", 0, correctorList
				.getNames().size());
	}

	@Test
	public void testParseEmptyLine() throws NameListException {
		testFile = new TestFile("CORR.LIST");
		testFile.write("\n   \n   ");
		correctorList = new CorrectorList(new File("CORR.LIST"));
		assertEquals("Initially Correctors - List has to be empty!", 0, correctorList
				.getNames().size());
		correctorList.parse();
		assertEquals("Correctors - List has to be still empty!", 0, correctorList
				.getNames().size());
	}

	@Test
	public void testParseValidList() throws NameListException {
		testFile = new TestFile("CORR.LIST");
		testFile.write("\n MDLH.610104.H \n" + "MDLH.610206.H\n"
				+ "MCIAH.20804.H  \n   ");
		correctorList = new CorrectorList(new File("CORR.LIST"));
		assertEquals("Initially Correctors - List has to be empty!", 0, correctorList
				.getNames().size());
		correctorList.parse();
		assertEquals("Now Correctors - List should contain 3 items.", 3, correctorList
				.getNames().size());

		assertEquals("Item 0:", "MDLH.610104.H", correctorList.getNames().get(0));
		assertEquals("Item 1:", "MDLH.610206.H", correctorList.getNames().get(1));
		assertEquals("Item 2:", "MCIAH.20804.H", correctorList.getNames().get(2));
	}

	@Test
	public void testParseTwice() throws NameListException {
		testFile = new TestFile("CORR.LIST");
		testFile.write("\n MDLH.610104.H \n" + "MDLH.610206.H\n"
				+ "MCIAH.20804.H  \n   ");
		correctorList = new CorrectorList(new File("CORR.LIST"));
		assertEquals("Initially Correctors - List has to be empty!", 0, correctorList
				.getNames().size());
		correctorList.parse();
		assertEquals("Now Correctors - List should contain 3 items.", 3, correctorList
				.getNames().size());

		assertEquals("Item 0:", "MDLH.610104.H", correctorList.getNames().get(0));
		assertEquals("Item 1:", "MDLH.610206.H", correctorList.getNames().get(1));
		assertEquals("Item 2:", "MCIAH.20804.H", correctorList.getNames().get(2));

		// parse once more and the items should remain the same.
		correctorList.parse();
		assertEquals("Now Correctors - List should still contain 3 items.", 3,
				correctorList.getNames().size());

		assertEquals("Item 0:", "MDLH.610104.H", correctorList.getNames().get(0));
		assertEquals("Item 1:", "MDLH.610206.H", correctorList.getNames().get(1));
		assertEquals("Item 2:", "MCIAH.20804.H", correctorList.getNames().get(2));

	}
}
