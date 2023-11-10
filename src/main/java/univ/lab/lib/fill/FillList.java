package univ.lab.lib.fill;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for Lists
 * When XML parsed, creates a new list and puts nodes with specified tag inside
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FillList {
    String attribute();
}
