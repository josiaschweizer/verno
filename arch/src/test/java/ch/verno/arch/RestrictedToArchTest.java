package ch.verno.arch;

import ch.verno.lib.annotation.RestrictedTo;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

class RestrictedToArchTest {

  @Test
  @DisplayName("restricted methods and custronctors may only be called by allowed classes")
  void test_1() {
    JavaClasses classes = new ClassFileImporter().importPackages("ch.verno");

    for (var javaClass : classes) {
      for (JavaCodeUnit codeUnit : javaClass.getCodeUnits()) {
        for (JavaAccess<?> access : codeUnit.getAccessesFromSelf()) {
          final var target = access.getTarget().resolveMember().orElse(null);
          if (target == null) {
            continue;
          }

          RestrictedTo restrictedTo = null;
          if (target.reflect() instanceof AnnotatedElement annotatedElement) {
            restrictedTo = annotatedElement.getAnnotation(RestrictedTo.class);
          }

          if (restrictedTo == null) {
            continue;
          }

          Class<?> callerClass = access.getOriginOwner().reflect();
          boolean allowed = Arrays.stream(restrictedTo.value()).anyMatch(allowedClass -> allowedClass.isAssignableFrom(callerClass));

          if (!allowed) {
            fail("""
                    Restricted call detected:
                    Caller: %s
                    Target: %s
                    Allowed: %s
                    """
                    .formatted(
                            callerClass.getName(),
                            target.getFullName(),
                            Arrays.toString(restrictedTo.value())
                    ));
          }
        }
      }
    }
  }
}