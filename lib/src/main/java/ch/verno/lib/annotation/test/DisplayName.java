package ch.verno.lib.annotation.test;

import ch.verno.lib.Publ;
import org.jetbrains.annotations.NonNls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@org.junit.jupiter.api.DisplayName(Publ.EMPTY_STRING)
public @interface DisplayName {

  @NonNls
  String value();

}