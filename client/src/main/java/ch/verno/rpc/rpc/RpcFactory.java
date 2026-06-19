package ch.verno.rpc.rpc;

import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Proxy;

public class RpcFactory {

  @Nonnull private final RpcClient rpcClient;

  public RpcFactory(@Nonnull final RpcClient rpcClient) {
    this.rpcClient = rpcClient;
  }

  @Nonnull
  @SuppressWarnings("unchecked")
  public <T> T create(@Nonnull final Class<T> endpointType) {
    if (!endpointType.isInterface()) {
      throw new IllegalArgumentException("RPC endpoint must be an interface: " + endpointType.getName());
    }

    if (!endpointType.isAnnotationPresent(RpcEndpoint.class)) {
      throw new IllegalArgumentException("Missing @RpcEndpoint on: " + endpointType.getName());
    }

    return (T) Proxy.newProxyInstance(
            endpointType.getClassLoader(),
            new Class<?>[]{endpointType},
            (proxy, method, args) -> {
              if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
              }

              return rpcClient.call(endpointType, method, args);
            }
    );
  }
}