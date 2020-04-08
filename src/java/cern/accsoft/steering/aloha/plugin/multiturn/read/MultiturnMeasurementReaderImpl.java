/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.read;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.MultiturnMeasurement;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.MultiturnMeasurementImpl;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnData;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDataImpl;
import cern.accsoft.steering.aloha.plugin.multiturn.meas.data.MultiturnDifferenceDataImpl;
import cern.accsoft.steering.aloha.plugin.multiturn.read.parse.MultiturnParser;
import cern.accsoft.steering.aloha.plugin.multiturn.read.parse.MultiturnParserException;
import cern.accsoft.steering.aloha.plugin.multiturn.read.parse.MultiturnParserImpl;
import cern.accsoft.steering.aloha.read.MeasurementReaderOptions;
import cern.accsoft.steering.util.meas.read.ReaderException;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * This is the implementation of the reader for multiturn data
 * 
 * @author kfuchsbe
 * 
 */
public class MultiturnMeasurementReaderImpl implements
		MultiturnMeasurementReader, AlohaBeanFactoryAware,
		MachineElementsManagerAware {

	/** the bean-factory to use to create other objects */
	private AlohaBeanFactory alohaBeanFactory;

	/** the data reader to use for reading the data */
	private MultiturnParser multiturnParser;

	/** the machineElements manager */
	private MachineElementsManager machineElementsManager;

	/**
	 * a file filter for dispersion-data
	 */
	private FileFilter fileFilter = new FileFilter() {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			} else {
				return isHandling(f);
			}
		}

		@Override
		public String getDescription() {
			return MultiturnMeasurementReaderImpl.this.getDescription();
		}
	};

	@InitMethod
	public void init() {
		this.multiturnParser = getAlohaBeanFactory().create(
				MultiturnParserImpl.class);
	}

	@Override
	public MultiturnMeasurement read(List<File> files,
			ModelDelegate modelDelegate, MeasurementReaderOptions options) throws ReaderException {
		if ((files.size() < 1) || (files.size() > 2)) {
			throw new ReaderException(
					files.size()
							+ " files were given but this reader can only handle 1 or 2 files.\r\n"
							+ "When 2 files are given one must contain the horizontal and one the vertical data.");
		}

		MultiturnData data = getAlohaBeanFactory().create(
				MultiturnDataImpl.class);

		String name = "";
		for (File file : files) {
			try {
				if (name.length() > 0) {
					name += ", ";
				}
				name += file.getName();
				getMultiturnParser().parse(file, data);
			} catch (MultiturnParserException e) {
				throw new ReaderException(
						"Could not load multiturn data from file '"
								+ file.getAbsolutePath() + "'.", e);
			}
		}

		/* ensure that the unavailable bpms are deactivated */
		getMachineElementsManager().deactivateUnavailableMonitors(
				Arrays.asList(new MultiturnData[] { data }));

		MultiturnDifferenceDataImpl diffData = getAlohaBeanFactory().create(
				MultiturnDifferenceDataImpl.class);
		MultiturnMeasurement measurement = new MultiturnMeasurementImpl(name,
				modelDelegate, data, diffData);
		getAlohaBeanFactory().configure(measurement);
		diffData.setMeasurement(measurement);
		return measurement;
	}

	@Override
	public String getDescription() {
		return "Multiturn measurement data";
	}

	@Override
	public FileFilter getFileFilter() {
		return this.fileFilter;
	}

	@Override
	public boolean isHandling(List<File> files) {
		if ((files.size() < 1) || (files.size() > 2)) {
			return false;
		}
		for (File file : files) {
			if (!isHandling(file)) {
				return false;
			}
		}
		return true;
	}

	private boolean isHandling(File file) {
		return file.getName().toLowerCase().endsWith(".mtdata");
	}

	@Override
	public void setAlohaBeanFactory(AlohaBeanFactory alohaBeanFactory) {
		this.alohaBeanFactory = alohaBeanFactory;
	}

	private AlohaBeanFactory getAlohaBeanFactory() {
		return this.alohaBeanFactory;
	}

	private MultiturnParser getMultiturnParser() {
		return multiturnParser;
	}

	@Override
	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
	}

	private MachineElementsManager getMachineElementsManager() {
		return this.machineElementsManager;
	}

	@Override
	public boolean requiresOptions() {
		return false;
	}

}
