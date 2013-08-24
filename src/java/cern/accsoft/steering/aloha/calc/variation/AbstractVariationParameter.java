package cern.accsoft.steering.aloha.calc.variation;

public abstract class AbstractVariationParameter implements VariationParameter {

	/** the value which is used as default variation for a parameter */
	public final static double DEFAULT_DELTA = 0.0001;

	private double actualOffset = 0.0;
	public double lastOffset = 0.0;

	private String key = null;
	private double initialOffset = 0.0;
	private double sensity = 0.0;
	private double delta = DEFAULT_DELTA;
	private double updateFactor = 1.0;
	private double error = 0.0;

	public AbstractVariationParameter(String key, double initialOffset) {
		super();
		this.key = key;
		this.initialOffset = initialOffset;
		init();
	}

	protected final void init() {
		actualOffset = initialOffset;
		lastOffset = initialOffset;
		sensity = 0.0;
		error = 0.0;
	}

	/**
	 * to be overridden by subclass: must transport the actual value to the
	 * desired targets
	 */
	protected abstract void apply();

	@Override
	public final String getKey() {
		return key;
	}

	@Override
	public final String getName() {
		return key; // .toUpperCase();
	}

	@Override
	public final double getInitialOffset() {
		return initialOffset;
	}

	@Override
	public final void setInitialOffset(double initialValue) {
		this.initialOffset = initialValue;
	}

	@Override
	public final double getDelta() {
		return delta;
	}

	@Override
	public final void setDelta(double delta) {
		this.delta = delta;
	}

	@Override
	public final double getSensity() {
		return this.sensity;
	}

	@Override
	public final void setSensity(double sensity) {
		this.sensity = sensity;
	}

	@Override
	public final void reset() {
		init();
		apply();
	}

	@Override
	public final void undo() {
		setActualOffset(lastOffset);
	}

	@Override
	public final double getActualOffset() {
		return actualOffset;
	}

	@Override
	public final void setActualOffset(double actualOffset) {
		lastOffset = this.actualOffset;
		this.actualOffset = actualOffset;
		apply();
	}

	@Override
	public final void setUpdateFactor(double factor) {
		this.updateFactor = factor;
	}

	@Override
	public final double getUpdateFactor() {
		return this.updateFactor;
	}

	/**
	 * adds the given value to the parameter. (may be scaled internally)
	 * 
	 * @param the
	 *            value that shall be added
	 */
	public final void addValueScaled(double value) {
		setActualOffset(getActualOffset() + (value * getUpdateFactor()));
	}

	@Override
	public void addDelta() {
		setActualOffset(getActualOffset() + getDelta());
	}
	

	@Override
	public double getError() {
		return this.error;
	}

	@Override
	public void setError(double error) {
		this.error = error;
	}

}