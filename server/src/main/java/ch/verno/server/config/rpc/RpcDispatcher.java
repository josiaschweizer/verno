package ch.verno.server.config.rpc;

import ch.verno.contract.rpc.RpcRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Method;
import java.util.List;

public class RpcDispatcher {

  @Nonnull private final RpcResourceRegistry registry;
  @Nonnull private final ObjectMapper objectMapper;

  public RpcDispatcher(@Nonnull final RpcResourceRegistry registry,
                       @Nonnull final ObjectMapper objectMapper) {
    this.registry = registry;
    this.objectMapper = objectMapper;
  }

  @Nonnull
  public Object dispatch(@Nonnull final RpcRequest request) throws Exception {
    final var endpointType = Class.forName(request.endpoint());
    final var resource = registry.getResource(request.endpoint());

    final var method = findMethod(endpointType, request.method(), request.arguments().size());
    final var arguments = convertArguments(method, request.arguments());

    return method.invoke(resource, arguments);
  }

  @Nonnull
  private Method findMethod(@Nonnull final Class<?> endpointType,
                            @Nonnull final String methodName,
                            final int argumentCount) {
    for (final var method : endpointType.getMethods()) {
      if (method.getName().equals(methodName) && method.getParameterCount() == argumentCount) {
        return method;
      }
    }

    throw new IllegalStateException("No RPC method found: " + endpointType.getName() + "#" + methodName);
  }

  @Nonnull
  private Object[] convertArguments(@Nonnull final Method method,
                                    @Nonnull final List<JsonNode> arguments) {
    final var parameterTypes = method.getParameterTypes();
    final var convertedArguments = new Object[parameterTypes.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      convertedArguments[i] = objectMapper.convertValue(arguments.get(i), parameterTypes[i]);
    }

    return convertedArguments;
  }
}