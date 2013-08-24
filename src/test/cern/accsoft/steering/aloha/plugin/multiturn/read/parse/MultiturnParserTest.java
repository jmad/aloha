package cern.accsoft.steering.aloha.plugin.multiturn.read.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDataValue;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnVar;
import cern.accsoft.steering.aloha.util.TestFile;
import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.Status;

public class MultiturnParserTest extends
		cern.accsoft.steering.aloha.util.LoggedTestCase {

	private cern.accsoft.steering.aloha.util.TestFile testFile = new TestFile(
			"test.mtdata");

	@After
	public void tearDown() {
		this.testFile.delete();
	}

	@Test
	public void testSimpleFile() throws MultiturnParserException {
		writeTestData();

		MultiturnParser parser = new MultiturnParserImpl();
		MultiturnData data = parser.parse(this.testFile.getFile());
		assertEquals("Should be 11 dataValues", 11, data.getDataValues(
				Plane.HORIZONTAL).size());

		/*
		 * just as an example try the 6th value:
		 */
		MultiturnDataValue dataValue = data.getDataValues(Plane.HORIZONTAL)
				.get(5);
		assertEquals("BPM.5L1.B2", dataValue.getName());
		assertEquals(Plane.HORIZONTAL, dataValue.getPlane());
		assertEquals(BeamNumber.BEAM_2, dataValue.getBeam());

		assertEquals(244.93707446897076, dataValue.getValue(MultiturnVar.BETA),
				1e-10);
		assertEquals(24.650998153259863, dataValue
				.getValue(MultiturnVar.BETA_ERROR), 1e-10);
		assertEquals(Status.OK, dataValue.getStatus());
		assertTrue(dataValue.isOk());
	}

	private void writeTestData() {
		testFile
				.write("#acquisitionStamp 1970-01-01 1:00:00.000\n"
						+ "#optics A1100C1100A1000L1000_FLAT_INJ\n"
						+ "#beam 2\n"
						+ "#analysisType Harmonic\n"
						+ "#plane[0=horizontal, 1=vertical]) 0\n"
						+ "#bunch 0\n"
						+ "#BPMname s index status(1==good,0==bad) tune tuneError phase phaseError amplitude apmlitudeError beta betaError phase_nom beta_nom\n"
						+ "BPMSW.1L1.B2 26637.4081999988 0 1 0.30192000000000013 0.0 0.7658199276333463 5.013251380024464E-6 0.41866506889068617 1.318760946791574E-5 9.490234213312544 33.36230008548012 64.10534053 52.92505684\n"
						+ "BPMS.2L1.B2 26627.3541999988 1 1 0.3016200000000001 0.0 0.05989559543514633 5.919156027147481E-6 0.3545899491005214 1.318760946791574E-5 2.553642910392715 21.071140999733803 64.0799769 59.42864279\n"
						+ "BPMSY.4L1.B2 26600.5686999988 2 1 0.3017400000000001 0.0 0.861828071063678 2.2699907503456415E-6 0.9246175272144538 1.318760946791574E-5 4.62909733748603 190.53723554344378 64.03634602 187.1954045 \n"
						+ "BPMWB.4L1.B2 26507.7126999988 3 1 0.30198000000000014 0.0 0.9845039160309369 3.4164087438662433E-6 0.6143507383747767 1.318760946791574E-5 248.27053467432594 252.21126784940313 63.89844983 79.32163349\n"
						+ "BPMYA.4L1.B2 26486.6561999988 4 1 0.30192000000000013 0.0 0.027612914257775525 2.8205298763531745E-6 0.744141465042031 1.318760946791574E-5 106.44605342542982 16.70657197855004 63.85744782 92.95332592\n"
						+ "BPM.5L1.B2 26465.5381999988 5 1 0.30186000000000013 0.0 0.04696388340713684 1.8983819040237691E-6 1.1056116948520966 1.318760946791574E-5 244.93707446897076 24.650998153259863 63.83195415 188.5965841\n"
						+ "BPMR.6L1.B2 26433.6381999988 6 1 0.30192000000000013 0.0 0.07850832510570324 3.1825177880338497E-6 0.6595008650936551 1.318760946791574E-5 102.77943246180037 8.420909887938347 63.793452 80.03133827\n"
						+ "BPM.7L1.B2 26390.4541999988 7 1 0.30216000000000015 0.0 0.13549795138637877 2.98221446925081E-6 0.7037968784691556 1.318760946791574E-5 129.55891097193333 4.755101400455819 63.71530694 97.07332796\n"
						+ "BPM.8L1.B2 26351.3862106011 8 1 0.30210000000000015 0.0 0.31569806108042736 6.24009528685846E-6 0.33635275390817554 1.318760946791574E-5 17.155160071207295 0.7455019222834298 63.5484071 25.07930168\n"
						+ "BPM.9L1.B2 26310.9212212035 9 1 0.3017400000000001 0.0 0.4532495521112665 2.5606239813114597E-6 0.8196725679766939 1.318760946791574E-5 138.23814009080522 14.482266351309697 63.44563122 137.0753217\n"
						+ "BPM.10L1.B2 26271.8542318058 10 1 0.30198000000000014 0.0 0.5575144719471506 5.65767122872151E-6 0.37097829646396085 1.318760946791574E-5 33.38883093617952 2.755171812826889 63.31117784 24.07893738\n");
	}

}
