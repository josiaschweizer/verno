package ch.verno.rpc.rpc;

import ch.verno.contract.rpc.RpcException;
import ch.verno.contract.rpc.RpcRequest;
import ch.verno.contract.rpc.RpcResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RpcClient {

  @Nonnull private final String rpcUrl;
  @Nonnull private final RestTemplate restTemplate;
  @Nonnull private final ObjectMapper objectMapper;

  public RpcClient(@Nonnull final String rpcUrl,
                   @Nonnull final RestTemplate restTemplate,
                   @Nonnull final ObjectMapper objectMapper) {
    this.rpcUrl = rpcUrl;
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public Object call(@Nonnull final Class<?> endpointType,
                     @Nonnull final Method method,
                     @Nullable final Object[] args) {
    try {
      final List<JsonNode> arguments = args == null
              ? List.of()
              : Arrays.stream(args)
              .map(argument -> (JsonNode) objectMapper.valueToTree(argument))
              .toList();

      final var request = new RpcRequest(
              endpointType.getName(),
              method.getName(),
              arguments
      );

      final var response = restTemplate.postForObject(rpcUrl, request, RpcResponse.class);
      if (response == null) {
        throw new RpcException("RPC response is null.");
      }

      if (!response.success()) {
        throw new RpcException(response.errorMessage());
      }

      final var returnType = objectMapper
              .getTypeFactory()
              .constructType(method.getGenericReturnType());

      return objectMapper.convertValue(response.result(), returnType);
    } catch (final Exception exception) {
      throw new RpcException("RPC call failed: " + endpointType.getName() + "#" + method.getName(), exception);
    }
  }
}