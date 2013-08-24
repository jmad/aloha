package cern.accsoft.steering.aloha.calc.variation;

import java.util.List;

import org.apache.log4j.Logger;

import cern.accsoft.steering.aloha.calc.algorithm.Algorithm;
import cern.accsoft.steering.aloha.meas.Measurement;
import cern.accsoft.steering.aloha.meas.MeasurementManager.ModelDelegateInstance;
import cern.accsoft.steering.aloha.model.ModelDelegate;
import cern.accsoft.steering.aloha.model.ModelDelegateManager;
import cern.accsoft.steering.jmad.domain.knob.Knob;
import cern.accsoft.steering.jmad.domain.knob.KnobType;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.model.KnobManager;

/**
 * This class encapsulates the functionality to vary parameters of the madx-model by a {@link Algorithm}. It
 * encapsulates a {@link Knob} of a model. The class then provides a possibility to set these variable with a new value
 * and to reset it afterwards. NOTE: no direct reference to the knob is stored, since the knob has to be applied to all
 * models.
 * 
 * @author kfuchsbe
 */
public class KnobVariationParameter extends AbstractVariationParameter implements VariationParameter {

    /** the logger for the class */
    private final static Logger logger = Logger.getLogger(KnobVariationParameter.class);

    /** The model knob-type to which to apply */
    private KnobType knobType = null;

    /** The knob-key */
    private String knobKey = null;

    /**
     * the model delegate manager. It is needed to find out all actually loaded models and to set the values to the knob
     * in each model.
     */
    private ModelDelegateManager modelDelegateManager = null;

    public KnobVariationParameter(ModelDelegateManager modelDelegateManager, Knob knob) {
        this(modelDelegateManager, knob.getType(), knob.getKey());
    }

    public KnobVariationParameter(ModelDelegateManager modelDelegateManager, KnobType type, String knobKey) {
        super(createKey(type, knobKey), 0.0);
        this.modelDelegateManager = modelDelegateManager;
        this.knobType = type;
        this.knobKey = knobKey;
    }

    /**
     * transports the actual value to the knob.
     */
    protected void apply() {
        List<ModelDelegateInstance> modelDelegateInstances = this.modelDelegateManager.getModelDelegateInstances();
        for (ModelDelegateInstance instance : modelDelegateInstances) {
            JMadModel model = instance.getModelDelegate().getJMadModel();
            Knob knob = getKnob(model);
            if (knob != null) {
                knob.setOffset(getActualOffset());
            } else {
                logger.warn("could not find knob of type '" + this.knobType + "' and key '" + this.knobKey
                        + "' in model + '" + instance.toString() + "'. Cannot apply value.");
            }
        }
    }

    /**
     * return the Knob which corresponds to parameter in the given model.
     * 
     * @param model the model for which to get the knob
     * @return the Knob
     */
    public final Knob getKnob(JMadModel model) {
        if (model == null) {
            return null;
        }
        KnobManager knobManager = model.getKnobManager();
        return knobManager.getKnob(this.knobType, this.knobKey);
    }

    /**
     * composes a key for the knob depending on its type and its model-internal key.
     * 
     * @param knob the Knob for which to create the key
     * @return the key
     */
    public final static String createKey(Knob knob) {
        return createKey(knob.getType(), knob.getKey());
    }

    /**
     * composes a key for a knob depending on its model-internal key and its type.
     * 
     * @param type the type of the knob
     * @param knobKey the model internal key of the knob
     * @return the key
     */
    public final static String createKey(KnobType type, String knobKey) {
        return type.getPrefix() + ":" + knobKey;
    }

    @Override
    public Double getActiveMeasurementAbsoluteValue() {
        ModelDelegate activeDelegate = this.modelDelegateManager.getActiveModelDelegate();

        if (activeDelegate == null) {
            return null;
        }

        Knob knob = getKnob(activeDelegate.getJMadModel());
        if (knob != null) {
            return knob.getTotalValue();
        }

        return null;
    }

    @Override
    public Double getActiveMeasurementInitialValue() {
        Double actualValue = getActiveMeasurementAbsoluteValue();
        if (actualValue != null) {
            return actualValue - getOffsetChange();
        }
        return null;
    }

    @Override
    public Double getActiveMeasurementRelativeChange() {
        Double initialValue = getActiveMeasurementInitialValue();
        if ((initialValue != null) && (initialValue.doubleValue() != 0)) {
            return getOffsetChange() / initialValue;
        }
        return Double.NaN;
    }

    @Override
    public double getOffsetChange() {
        return (getActualOffset() - getInitialOffset());
    }

}
