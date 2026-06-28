package ch.verno.rpc.rpc;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.rpc.RpcException;
import ch.verno.contract.rpc.RpcRequest;
import ch.verno.contract.rpc.RpcResponse;
import ch.verno.lib.New;
import ch.verno.lib.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
              ? New.list()
              : Arrays.stream(args)
              .map(argument -> (JsonNode) objectMapper.valueToTree(argument))
              .toList();

      final var request = new RpcRequest(
              endpointType.getName(),
              method.getName(),
              arguments
      );

      final var headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      final var tenantId = TenantContext.get();
      if (tenantId != null) {
        headers.set(VernoConstants.X_MANDANT, tenantId.toString());
      }

      final var requestEntity = new HttpEntity<>(request, headers);

      final var response = restTemplate.postForObject(
              rpcUrl,
              requestEntity,
              RpcResponse.class
      );

      if (response == null) {
        throw new RpcException("RPC response is null.");
      }

      if (!response.success()) {
        throw new RpcException(response.errorMessage());
      }

      final var returnType = objectMapper
              .getTypeFactory()
              .constructType(method.getGenericReturnType());

      if (response.result() == null) {
        if (returnType.isTypeOrSubTypeOf(Optional.class)) {
          return Optional.empty();
        }

        return null;
      }

      return objectMapper.convertValue(response.result(), returnType);
    } catch (RpcException exception) {
      throw exception;
    } catch (Exception exception) {
      throw new RpcException("RPC call failed: " + endpointType.getName() + "#" + method.getName(), exception);
    }
  }
}