/**
 * 
 */
package cern.accsoft.steering.aloha.bean.aware;

import cern.accsoft.steering.aloha.calc.variation.VariationData;

/**
 * @author tbaer
 * 
 */
public interface VariationDataAware extends BeanAware {

	public void setVariationData(VariationData variationData);
}
