package org.fiware.tmforum.domain.ngsi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AttributeSetter {
	AttributeType value() default AttributeType.PROPERTY;

	String targetName();

	// allows to define the class to be used for the field. Required in case a List is used, since the concrete class would be hidden due to type-erasure
	Class<?> targetClass() default Object.class;

}
