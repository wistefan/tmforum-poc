package org.fiware.tmforum.domain.ngsi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines to what kind of Property a field should be mapped.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AttributeGetter {

	AttributeType value() default AttributeType.PROPERTY;

	String targetName();

}


