package ch.verno.server.config.rpc;

import ch.verno.contract.rpc.RpcRequest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
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

    final var method = findMethod(
            endpointType,
            request.method(),
            request.arguments().size()
    );

    final var arguments = convertArguments(method, request.arguments());

    try {
      return method.invoke(resource, arguments);
    } catch (java.lang.reflect.InvocationTargetException exception) {
      final var cause = exception.getCause();

      if (cause instanceof Exception causedException) {
        throw causedException;
      }

      if (cause instanceof Error causedError) {
        throw causedError;
      }

      throw exception;
    }
  }

  @Nonnull
  private Method findMethod(@Nonnull final Class<?> endpointType,
                            @Nonnull final String methodName,
                            final int argumentCount) {
    for (final var method : endpointType.getMethods()) {
      if (method.getName().equals(methodName)
              && method.getParameterCount() == argumentCount) {
        return method;
      }
    }

    throw new IllegalStateException("No RPC method found: " + endpointType.getName() + "#" + methodName
    );
  }

  @Nonnull
  private Object[] convertArguments(@Nonnull final Method method,
                                    @Nonnull final List<JsonNode> arguments) {
    final var parameterTypes = method.getParameterTypes();
    final var convertedArguments = new Object[parameterTypes.length];

    for (int i = 0; i < parameterTypes.length; i++) {
      convertedArguments[i] = objectMapper.convertValue(
              arguments.get(i),
              parameterTypes[i]
      );
    }

    return convertedArguments;
  }
}