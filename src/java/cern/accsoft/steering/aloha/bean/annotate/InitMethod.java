/**
 * 
 */
package cern.accsoft.steering.aloha.bean.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cern.accsoft.steering.aloha.bean.AlohaBeanFactory;

/**
 * it this annotation is added to a method of a class that is instantiated by
 * {@link AlohaBeanFactory} then the method is called by the
 * {@link AlohaBeanFactory} after all the beans are injected. Throuh this method
 * any kind of initiation of the configured instance can be done.
 * 
 * @author kfuchsbe
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitMethod {
	/* nothing else to do */
}
