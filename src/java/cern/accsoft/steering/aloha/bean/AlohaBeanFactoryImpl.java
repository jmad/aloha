/**
 * 
 */
package cern.accsoft.steering.aloha.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.app.HelperDataManager;
import cern.accsoft.steering.aloha.bean.annotate.InitMethod;
import cern.accsoft.steering.aloha.bean.aware.AlohaBeanFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.ChartFactoryAware;
import cern.accsoft.steering.aloha.bean.aware.HelperDataManagerAware;
import cern.accsoft.steering.aloha.bean.aware.MachineElementsManagerAware;
import cern.accsoft.steering.aloha.bean.aware.MeasurementManagerAware;
import cern.accsoft.steering.aloha.bean.aware.NoiseWeighterAware;
import cern.accsoft.steering.aloha.bean.aware.SensityMatrixManagerAware;
import cern.accsoft.steering.aloha.bean.aware.VariationDataAware;
import cern.accsoft.steering.aloha.calc.NoiseWeighter;
import cern.accsoft.steering.aloha.calc.sensitivity.SensitivityMatrixManager;
import cern.accsoft.steering.aloha.calc.variation.VariationData;
import cern.accsoft.steering.aloha.gui.dv.ChartFactory;
import cern.accsoft.steering.aloha.machine.manage.MachineElementsManager;
import cern.accsoft.steering.aloha.meas.MeasurementManager;

/**
 * @author kfuchsbe
 * 
 */
public class AlohaBeanFactoryImpl implements AlohaBeanFactory {

	/** The logger for the class */
	private final static Logger logger = Logger
			.getLogger(AlohaBeanFactoryImpl.class);

	/*
	 * all the instances which are directly injected by spring and then used to
	 * configure the classes.
	 */
	private MachineElementsManager machineElementsManager;
	private NoiseWeighter noiseWeighter;
	private HelperDataManager helperDataManager;
	private ChartFactory chartFactory;
	private MeasurementManager measurementManager;
	private VariationData variationData;
	private SensitivityMatrixManager sensityMatrixManager;

	@Override
	public <T> T create(Class<? extends T> clazz) {
		T instance;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Could not instantiate class of type '"
					+ clazz.getCanonicalName() + "'."
					+ "\n Maybe it is an abstract class?"
					+ "\n Does it have a no-argument construcor?", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Error while instanting class of type '"
					+ clazz.getCanonicalName() + "'.", e);
		}

		configure(instance);
		return instance;
	}

	/**
	 * injects all the necessary beans to the instance
	 * 
	 * @param instance
	 *            the object to configure
	 */
	public <T> void configure(T instance) {
		if (instance instanceof AlohaBeanFactoryAware) {
			((AlohaBeanFactoryAware) instance).setAlohaBeanFactory(this);
		}
		if (instance instanceof MachineElementsManagerAware) {
			((MachineElementsManagerAware) instance)
					.setMachineElementsManager(getMachineElementsManager());
		}
		if (instance instanceof NoiseWeighterAware) {
			((NoiseWeighterAware) instance)
					.setNoiseWeighter(getNoiseWeighter());
		}
		if (instance instanceof HelperDataManagerAware) {
			((HelperDataManagerAware) instance)
					.setHelperDataManager(getHelperDataManager());
		}
		if (instance instanceof ChartFactoryAware) {
			((ChartFactoryAware) instance).setChartFactory(getChartFactory());
		}
		if (instance instanceof MeasurementManagerAware) {
			((MeasurementManagerAware) instance)
					.setMeasurementManager(getMeasurementManager());
		}
		if (instance instanceof VariationDataAware) {
			((VariationDataAware) instance)
					.setVariationData(getVariationData());
		}
		if (instance instanceof SensityMatrixManagerAware) {
			((SensityMatrixManagerAware) instance)
					.setSensityMatrixManager(getSensityMatrixManager());
		}
		/*
		 * after all the beans are injected we try to call the init method, if
		 * available.
		 */
		callInitMethods(instance);
	}

	/**
	 * searches the class for methods which are annotated with
	 * {@link InitMethod} annotation and tries to execute them.
	 */
	private <T> void callInitMethods(T instance) {
		Class<?> clazz = instance.getClass();
		for (Method method : clazz.getMethods()) {
			if (method.getAnnotation(InitMethod.class) != null) {
				try {
					method.invoke(instance);
				} catch (IllegalArgumentException e) {
					logger.error("Could not invoke init method '"
							+ method.getName() + "' of class '"
							+ clazz.getCanonicalName() + "'.", e);
				} catch (IllegalAccessException e) {
					logger.error("Could not invoke init method '"
							+ method.getName() + "' of class '"
							+ clazz.getCanonicalName() + "'.", e);
				} catch (InvocationTargetException e) {
					logger.error("Could not invoke init method '"
							+ method.getName() + "' of class '"
							+ clazz.getCanonicalName() + "'.", e);
				}
			}
		}
	}

	public void setMachineElementsManager(
			MachineElementsManager machineElementsManager) {
		this.machineElementsManager = machineElementsManager;
	}

	private MachineElementsManager getMachineElementsManager() {
		return machineElementsManager;
	}

	public void setNoiseWeighter(NoiseWeighter noiseWeighter) {
		this.noiseWeighter = noiseWeighter;
	}

	private NoiseWeighter getNoiseWeighter() {
		return noiseWeighter;
	}

	public void setHelperDataManager(HelperDataManager helperDataManager) {
		this.helperDataManager = helperDataManager;
	}

	private HelperDataManager getHelperDataManager() {
		return helperDataManager;
	}

	public void setChartFactory(ChartFactory chartFactory) {
		this.chartFactory = chartFactory;
	}

	private ChartFactory getChartFactory() {
		return chartFactory;
	}

	public void setMeasurementManager(MeasurementManager measurementManager) {
		this.measurementManager = measurementManager;
	}

	private MeasurementManager getMeasurementManager() {
		return measurementManager;
	}

	public void setVariationData(VariationData variationData) {
		this.variationData = variationData;
	}

	private VariationData getVariationData() {
		return variationData;
	}

	public void setSensityMatrixManager(
			SensitivityMatrixManager sensityMatrixManager) {
		this.sensityMatrixManager = sensityMatrixManager;
	}

	private SensitivityMatrixManager getSensityMatrixManager() {
		return sensityMatrixManager;
	}

}
