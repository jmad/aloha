package cern.accsoft.steering.aloha.calc;


public interface CalculatorListener {

    default void changedCalculatedValues(Calculator calculator) {
    }

    default void changedVariationParameters(Calculator calculator) {
    }

}
