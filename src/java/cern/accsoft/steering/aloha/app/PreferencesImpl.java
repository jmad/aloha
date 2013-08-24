package cern.accsoft.steering.aloha.app;

import java.text.NumberFormat;

public class PreferencesImpl implements Preferences {
	
	/*
	 * the variables which hold preferences
	 */
	private NumberFormat numberFormat;
	private String inputPath = "";
	private Integer measurementNumber = null;
	boolean selfTestEnabled = false;

	public PreferencesImpl() {
		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(10);
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	/**
	 * @return the path where to look for data.
	 */
	public String getDataPath() {
		return inputPath;
	}

	/**
	 * @return the measurement number which shall be loaded by default
	 */
	public Integer getMeasurementNumber() {
		return this.measurementNumber;
	}

	@Override
	public boolean isSelfTestEnabled() {
		return this.selfTestEnabled;
	}

	@Override
	public void setInputPath(String dataPath) {
		this.inputPath = dataPath;
	}

	@Override
	public void setMeasurementNumber(Integer measurementNumber) {
		this.measurementNumber = measurementNumber;
	}

	@Override
	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	@Override
	public void setSelfTestEnabled(boolean enabled) {
		this.selfTestEnabled = enabled;
	}
}
