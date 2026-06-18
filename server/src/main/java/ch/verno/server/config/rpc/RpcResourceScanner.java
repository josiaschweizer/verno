package ch.verno.server.config.rpc;

import ch.verno.contract.rpc.RpcResource;
import com.google.inject.Injector;
import io.github.classgraph.ClassGraph;
import jakarta.annotation.Nonnull;

public class RpcResourceScanner {

  @Nonnull private final Injector injector;

  public RpcResourceScanner(@Nonnull final Injector injector) {
    this.injector = injector;
  }

  public void scanAndRegister(@Nonnull final RpcResourceRegistry registry,
                              @Nonnull final String basePackage) {
    try (final var scanResult = new ClassGraph()
            .enableAnnotationInfo()
            .acceptPackages(basePackage)
            .scan()) {

      final var resourceClasses = scanResult
              .getClassesWithAnnotation(RpcResource.class)
              .loadClasses();

      for (final var resourceClass : resourceClasses) {
        final var resource = injector.getInstance(resourceClass);
        registry.register(resource);
      }
    }
  }
}