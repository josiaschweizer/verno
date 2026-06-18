package ch.verno.server.config.rpc;

import ch.verno.contract.rpc.RpcEndpoint;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;

import java.util.Map;

public class RpcResourceRegistry {

  @Nonnull private final Map<String, Object> resources;

  public RpcResourceRegistry() {
    this.resources = New.map();
  }

  public void register(@Nonnull final Object resource) {
    final var resourceType = resource.getClass();
    final var annotation = resourceType.getAnnotation(RpcResource.class);
    if (annotation == null) {
      throw new IllegalStateException("Missing @RpcResource on " + resourceType.getName());
    }

    final var endpointType = annotation.value();
    if (!endpointType.isAnnotationPresent(RpcEndpoint.class)) {
      throw new IllegalStateException("Missing @RpcEndpoint on " + endpointType.getName());
    } else if (!endpointType.isAssignableFrom(resourceType)) {
      throw new IllegalStateException(resourceType.getName() + " does not implement " + endpointType.getName());
    }

    resources.put(endpointType.getName(), resource);
  }

  @Nonnull
  public Object getResource(@Nonnull final String endpointClassName) {
    final var resource = resources.get(endpointClassName);
    if (resource == null) {
      throw new IllegalStateException("No RPC resource found for endpoint: " + endpointClassName);
    }

    return resource;
  }
}