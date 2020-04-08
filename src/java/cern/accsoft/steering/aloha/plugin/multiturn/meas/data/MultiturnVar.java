/**
 * 
 */
package cern.accsoft.steering.aloha.plugin.multiturn.meas.data;

import cern.accsoft.steering.jmad.domain.var.Variable;
import cern.accsoft.steering.jmad.domain.var.VariableUtil;
import cern.accsoft.steering.util.meas.data.Status;

/**
 * This enum represents the different possible values stored in a multiturn
 * file.
 * <p>
 * for the moment only the ones used by aloha are implemented
 * 
 * @author kfuchsbe
 * 
 */
public enum MultiturnVar implements Variable {
	/*
	 * BPMname s index status(1==good,0==bad) tune tuneError phase phaseError
	 * amplitude apmlitudeError beta betaError phase_nom beta_nom
	 */
	BPM_NAME("BPMname", String.class) {
		@Override
		public void setValue(MultiturnDataValueImpl dataValue, String fileValue) {
			dataValue.setName(fileValue);
		}
	},
	STATUS("status(1==good,0==bad)", Integer.class) {
		@Override
		public void setValue(MultiturnDataValueImpl dataValue, String fileValue) {
			int iValue = Integer.parseInt(fileValue);
			if (iValue == 1) {
				dataValue.setStatus(Status.OK);
			} else {
				dataValue.setStatus(Status.NOT_OK);
			}
		}
	},
	TUNE("tune"), TUNE_ERROR("tuneError"), PHASE("phase"), PHASE_ERROR(
			"phaseError"), AMPLITUDE("amplitude"), AMPLITUDE_ERROR(
			"apmlitudeError"), BETA("beta"), BETA_ERROR("betaError"), //
	UNKNOWN("", null) {
		@Override
		public void setValue(MultiturnDataValueImpl dataValue, String fileValue) {
			/* set nothing */
		}
	};

	/** the tag which indicates the enum in the file */
	private final String tag;

	/** the type of the variable */
	private final Class<?> valueClass;

	private MultiturnVar(String tag, Class<?> valueType) {
		this.tag = tag;
		this.valueClass = valueType;
	}

	private MultiturnVar(String tag) {
		this(tag, Double.class);
	}

	/**
	 * 
	 * has to return true, if the given value represents the enmu.
	 * 
	 * @param value
	 *            the value to test
	 * @return true if it is a match
	 */
	public boolean match(String value) {
		return this.tag.trim().equalsIgnoreCase(value.trim());
	}

	/**
	 * finds the correct Column-enum from the tag.
	 * 
	 * @param tag
	 *            the tag which describes the column in the file
	 * @return the column enum-value.
	 */
	public static final MultiturnVar fromTag(String tag) {
		for (MultiturnVar column : MultiturnVar.values()) {
			if (column.match(tag)) {
				return column;
			}
		}
		return UNKNOWN;
	}

	/**
	 * has to be implemented by each enum to convert the value correctly and set
	 * it to the right property. The default is to set it as a double value with
	 * the enmu value as key.
	 * 
	 * @param dataValue
	 *            the datValue object to which to set the value
	 * @param fileValue
	 *            the string value to set
	 */
	public void setValue(MultiturnDataValueImpl dataValue, String fileValue) {
		dataValue.setDoubleValue(this, Double.parseDouble(fileValue));
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getUnit() {
		return null;
	}

	@Override
	public String toString() {
		return VariableUtil.toString(this);
	}

	@Override
	public Class<?> getValueClass() {
		return this.valueClass;
	}

}
