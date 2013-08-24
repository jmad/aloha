package cern.accsoft.steering.aloha.plugin.traj.read.yasp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactoryImpl;
import cern.accsoft.steering.aloha.machine.Monitor;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManagerImpl;
import cern.accsoft.steering.aloha.plugin.traj.meas.data.TrajectoryData;
import cern.accsoft.steering.aloha.util.LoggedTestCase;
import cern.accsoft.steering.aloha.util.TestFile;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.yasp.YaspFileReader;

public class StearingDataNoiseTest extends LoggedTestCase {

    private TestFile stabilityList = new TestFile(YaspStabilityList.STABILITY_LIST_FILENAME);
    private ArrayList<TestFile> testFiles = new ArrayList<TestFile>();

    private YaspTrajectoryMeasurementReader YaspTrajectDataNoise = null;

    @Before
    public void initTestFiles() {

        TestFile testFile = new TestFile("TRAJ_TI2_28-10-07_23-03-14_CY53567.data");
        writeTestFile(testFile, new String[] { "3621.0", "329.7", "846.8" });
        testFiles.add(testFile);

        testFile = new TestFile("TRAJ_TI2_28-10-07_23-03-27_CY53567.data");
        writeTestFile(testFile, new String[] { "3566.3", "522.6", "1010.8" });
        testFiles.add(testFile);

        testFile = new TestFile("TRAJ_TI2_28-10-07_23-03-53_CY53568.data");
        writeTestFile(testFile, new String[] { "2966.5", "715.4", "356.1" });
        testFiles.add(testFile);

        String content = "";
        for (TestFile file : testFiles) {
            content += file.getFile().getName() + "\n";
        }
        stabilityList.write(content);
    }

    private void writeTestFile(TestFile testFile, String[] positions) {
        testFile.write("# MONITOR  %s micron\n" + "* NAME  PLANE  BEAM  POS  RMS  SUM  HW-STATUS STATUS STATUS-TAG\n"
                + " BPCK.610015          H 1     " + positions[0] + "         1.6         0.0  0  0  OK\n"
                + " BPCK.610211          H 1     " + positions[1] + "         0.8         0.0  0  0  OK\n"
                + " BPCK.610312          H 1     " + positions[2] + "         0.6         0.0  0  0  OK\n");
    }

    @After
    public void clearTestFiles() throws Exception {
        for (TestFile file : testFiles) {
            file.delete();
        }
        stabilityList.delete();
    }

    @Test
    public void testGetNoise() throws ReaderException {
        YaspTrajectDataNoise = new MockStabilityDataReader();

        TrajectoryData data = YaspTrajectDataNoise.read(Arrays.asList(new File[] { stabilityList.getFile() }));

        assertNotNull(data.getRmsValue(new Monitor("BPCK.610015", Plane.HORIZONTAL)));
        assertEquals(3.631167e-4, data.getRmsValue(new Monitor("BPCK.610015", Plane.HORIZONTAL)), 0.0001);
        assertEquals(1.928500e-4, data.getRmsValue(new Monitor("BPCK.610211", Plane.HORIZONTAL)), 0.0001);
        assertEquals(3.406647e-4, data.getRmsValue(new Monitor("BPCK.610312", Plane.HORIZONTAL)), 0.0001);

        /* these values should be zero, since they are not available: */
        assertEquals(0.0, data.getRmsValue(new Monitor("BPCK.610312", Plane.VERTICAL)), 0.0);
        assertEquals(0.0, data.getRmsValue(new Monitor("BPCK.612312", Plane.HORIZONTAL)), 0.0);
    }

    /**
     * a mock-object for the stability-data reader, which implements the creation of the stabilityData object.
     * 
     * @author kfuchsbe
     */
    private class MockStabilityDataReader extends YaspTrajectoryMeasurementReader {

        private MockStabilityDataReader() {
            setReadingDataReader(new YaspFileReader());

            MachineElementsManager machineElementsManager = new MachineElementsManagerImpl();
            AlohaBeanFactoryImpl factory = new AlohaBeanFactoryImpl();

            factory.setMachineElementsManager(machineElementsManager);
            setAlohaBeanFactory(factory);
        }

    }
}
