package cern.accsoft.steering.aloha.plugin.xxx.sensitivity;

import Jama.Matrix;
import cern.accsoft.steering.aloha.calc.sensitivity.AbstractSensitivityMatrixContributor;
import cern.accsoft.steering.aloha.calc.sensitivity.PerturbedColumn;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.plugin.disp.meas.DispersionMeasurement;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.domain.optics.Optic;
import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;
import cern.accsoft.steering.jmad.model.JMadModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Ti8CollimatorConstraintsContributor extends AbstractSensitivityMatrixContributor {
	private final static Logger LOGGER = LoggerFactory.getLogger(Ti8CollimatorConstraintsContributor.class);

	/** the minimal norm */
	private final static double MIN_NORM = 0.0000001;

	/** All the constraints */
	private List<OpticsConstraint> opticsConstraints = Arrays.asList(new OpticsConstraint[] {
			new ElementsOpticsDiffConstraint("TCDIH.87441", "TCDIH.87904", MadxTwissVariable.MUX, 4.0 / 6.0),
			new ElementsOpticsDiffConstraint("TCDIH.87441", "TCDIH.88121", MadxTwissVariable.MUX, 5.0 / 6.0),
			new ElementsOpticsDiffConstraint("TCDIV.87645", "TCDIV.87804", MadxTwissVariable.MUY, 1.0 / 6.0),
			new ElementsOpticsDiffConstraint("TCDIV.87645", "TCDIV.88123", MadxTwissVariable.MUY, 2.0 / 6.0) });

	/** the measurement, whose model we would like to constrain */
	private DispersionMeasurement measurement;

	/** Here we store the unperturbed values for each constraint */
	private Matrix unperturbedValues = new Matrix(1, 1);

	/** The constructor which needs the measurement */
	public Ti8CollimatorConstraintsContributor(DispersionMeasurement measurement) {
		this.measurement = measurement;
	}

	@Override
	public Matrix calcCorrectorSensityMatrix() {
		return null;
	}

	@Override
	public Matrix calcMonitorSensityMatrix() {
		return null;
	}

	@Override
	public PerturbedColumn calcPerturbedColumn(double delta, Double normalizationFactor) {
		Matrix diffVector = calcActualValues().minus(this.unperturbedValues);
		diffVector.timesEquals(1 / delta);

		/* if no normalization-factor is given, we have to calc our own. */
		if (normalizationFactor == null) {
			normalizationFactor = diffVector.normF() / this.unperturbedValues.normF();
			if (normalizationFactor < MIN_NORM) {
				LOGGER.warn("Normalization Factor for perturbed Constraint column is smaller than " + MIN_NORM
						+ ". Maybe the choice for delta of the parameter was too small.");
				normalizationFactor = 1.0;
			}
		}

		diffVector.timesEquals(1 / normalizationFactor);
		return new PerturbedColumn(diffVector, normalizationFactor);
	}

	@Override
	public Matrix getDifferenceVector() {
		return getTargetValues().minus(calcActualValues());
	}

	public Matrix getTargetValues() {
		Matrix values = new Matrix(getMatrixRowCount(), 1);
		for (int i = 0; i < this.opticsConstraints.size(); i++) {
			values.set(i, 0, this.opticsConstraints.get(i).getTargetValue());
		}
		return values;
	}

	private Matrix calcActualValues() {
		Matrix values = new Matrix(getMatrixRowCount(), 1);
		Optic optics;
		try {
			optics = getModel().getOptics();
		} catch (JMadModelException e) {
			LOGGER.error("Getting Optics from model failed.", e);
			return values;
		}

		for (int i = 0; i < this.opticsConstraints.size(); i++) {
			values.set(i, 0, this.opticsConstraints.get(i).calcValue(optics));
		}
		return values;
	}

	@Override
	public Matrix getDifferenceVectorErrors() {
		return new Matrix(getMatrixRowCount(), 1);
	}

	@Override
	public int getMatrixRowCount() {
		return this.opticsConstraints.size();
	}

	@Override
	public Measurement getMeasurement() {
		return this.measurement;
	}

	@Override
	public String getName() {
		return "Ti8 Collimator constraints";
	}

	@Override
	public void initUnperturbed() {
		this.unperturbedValues = calcActualValues();
	}

	/**
	 * @return the model to work with
	 */
	private JMadModel getModel() {
		return this.measurement.getModelDelegate().getJMadModel();
	}

}
